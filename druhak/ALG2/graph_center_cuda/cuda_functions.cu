#include <cuda_runtime.h>
#include <cstdint>
#include <vector>
#include <limits>
#include <cstdio>
#include "classes.h"

struct DeviceGraph {
  const uint32_t* d_offsets;
  const uint32_t* d_neighbors;
  uint32_t num_nodes;
  uint32_t num_neighbors;
};

__global__ void expand_frontier(const DeviceGraph dgraph,
                                const uint32_t* d_frontier, uint32_t frontier_size,
                                uint32_t* d_next_frontier, unsigned int* d_next_size,
                                unsigned int* d_visited, uint32_t* d_distances,
                                unsigned int depth) {
  uint32_t tid = blockIdx.x * blockDim.x + threadIdx.x;
  if (tid >= frontier_size) return;
  uint32_t u = d_frontier[tid];
  uint32_t s = dgraph.d_offsets[u];
  uint32_t e = dgraph.d_offsets[u + 1];
  for (uint32_t ei = s; ei < e; ++ei) {
    uint32_t v = dgraph.d_neighbors[ei];
    int old = atomicExch(&d_visited[v], 1);
    if (old == 0) {
      d_distances[v] = depth + 1;
      unsigned int pos = atomicAdd(d_next_size, 1u);
      d_next_frontier[pos] = v;
    }
  }
}

// Host wrapper: single-source level-synchronous BFS.
// distances (host) will be sized to num_nodes; unreachable nodes set to SIZE_MAX.
void bfs_frontier_cuda(const Graph &graph, size_t source, std::vector<size_t> &distances) {
  uint32_t num_nodes = static_cast<uint32_t>(graph.get_node_count());
  std::vector<uint32_t> h_off, h_nbr;
  graph.export_csr_u32(h_off, h_nbr);
  uint32_t total_neighbors = static_cast<uint32_t>(h_nbr.size());

  // device CSR
  uint32_t *d_off = nullptr, *d_nbr = nullptr;
  cudaMalloc(&d_off, (h_off.size()) * sizeof(uint32_t));
  cudaMalloc(&d_nbr, (h_nbr.size()) * sizeof(uint32_t));
  cudaMemcpy(d_off, h_off.data(), h_off.size() * sizeof(uint32_t), cudaMemcpyHostToDevice);
  cudaMemcpy(d_nbr, h_nbr.data(), h_nbr.size() * sizeof(uint32_t), cudaMemcpyHostToDevice);

  // visited (0/1), distances (uint32_t, UINT32_MAX = unreachable)
  unsigned int *d_visited = nullptr;
  uint32_t *d_dist = nullptr;
  cudaMalloc(&d_visited, num_nodes * sizeof(unsigned int));
  cudaMalloc(&d_dist, num_nodes * sizeof(uint32_t));
  cudaMemset(d_visited, 0, num_nodes * sizeof(unsigned int));
  const uint32_t UNREACH = std::numeric_limits<uint32_t>::max();
  cudaMemcpy(d_dist, &UNREACH, sizeof(uint32_t), cudaMemcpyHostToDevice); // set first element only
  // set whole distances array to UNREACH
  cudaMemset(d_dist, 0xFF, num_nodes * sizeof(uint32_t)); // 0xFF gives UINT32_MAX

  // frontiers
  uint32_t *d_front0 = nullptr, *d_front1 = nullptr;
  cudaMalloc(&d_front0, num_nodes * sizeof(uint32_t));
  cudaMalloc(&d_front1, num_nodes * sizeof(uint32_t));

  unsigned int *d_next_size = nullptr;
  cudaMalloc(&d_next_size, sizeof(unsigned int));

  // copy initial frontier (source)
  uint32_t src = static_cast<uint32_t>(source);
  cudaMemcpy(d_front0, &src, sizeof(uint32_t), cudaMemcpyHostToDevice);
  unsigned int one = 1u;
  cudaMemcpy(d_next_size, &one, sizeof(unsigned int), cudaMemcpyHostToDevice); // reuse as cur_size holder if needed
  // mark visited[source] = 1 and dist[source] = 0
  unsigned int one_u = 1u;
  cudaMemcpy(&d_visited[src], &one_u, sizeof(unsigned int), cudaMemcpyHostToDevice); // NOTE: invalid: cannot cudaMemcpy to &d_visited[src]
  // Instead set visited and dist via small kernel or host buffer:
  cudaMemcpy(d_visited, &one_u, sizeof(unsigned int), cudaMemcpyHostToDevice); // set index 0, we will swap if src !=0 below

  // Simple helper: if source != 0, write single elements using cudaMemcpy with offsets:
  if (src != 0) {
    cudaMemcpy(d_visited + src, &one_u, sizeof(unsigned int), cudaMemcpyHostToDevice);
  }
  uint32_t zero32 = 0;
  cudaMemcpy(d_dist + src, &zero32, sizeof(uint32_t), cudaMemcpyHostToDevice);

  // iterative BFS
  DeviceGraph dgraph { d_off, d_nbr, num_nodes, total_neighbors };
  uint32_t *d_cur_front = d_front0;
  uint32_t *d_next_front = d_front1;
  unsigned int cur_size = 1u;
  unsigned int depth = 0u;

  while (cur_size > 0) {
    // reset next size
    cudaMemset(d_next_size, 0, sizeof(unsigned int));
    // launch kernel
    const unsigned int block = 256;
    unsigned int grid = (cur_size + block - 1) / block;
    expand_frontier<<<grid, block>>>(dgraph, d_cur_front, cur_size, d_next_front, d_next_size, d_visited, d_dist, depth);
    cudaDeviceSynchronize();

    // get next size
    unsigned int next_size = 0;
    cudaMemcpy(&next_size, d_next_size, sizeof(unsigned int), cudaMemcpyDeviceToHost);
    if (next_size == 0) break;

    // swap frontiers
    std::swap(d_cur_front, d_next_front);
    cur_size = next_size;
    ++depth;
  }

  // copy distances back and convert to size_t
  std::vector<uint32_t> h_dist_u32(num_nodes);
  cudaMemcpy(h_dist_u32.data(), d_dist, num_nodes * sizeof(uint32_t), cudaMemcpyDeviceToHost);
  distances.resize(num_nodes);
  for (uint32_t i = 0; i < num_nodes; ++i) {
    if (h_dist_u32[i] == UNREACH) distances[i] = SIZE_MAX;
    else distances[i] = static_cast<size_t>(h_dist_u32[i]);
  }

  // free
  cudaFree(d_off);
  cudaFree(d_nbr);
  cudaFree(d_visited);
  cudaFree(d_dist);
  cudaFree(d_front0);
  cudaFree(d_front1);
  cudaFree(d_next_size);
}
// cpp
#pragma once
#include <vector>
#include <string>
#include <cstdint>

using std::vector;
using std::size_t;
using std::string;

class Graph {
public:
  // existing constructors / members omitted for brevity
  size_t get_node_count() const;
  const vector<size_t>& get_csr_offsets() const;
  const vector<size_t>& get_csr_neighbors() const;

  // Export CSR as 32-bit arrays for device use.
  // Caller must ensure graph size fits in 32-bit indices.
  void export_csr_u32(vector<uint32_t> &out_offsets, vector<uint32_t> &out_neighbors) const {
    const auto &off = get_csr_offsets();
    const auto &nbr = get_csr_neighbors();
    out_offsets.resize(off.size());
    out_neighbors.resize(nbr.size());
    for (size_t i = 0; i < off.size(); ++i) out_offsets[i] = static_cast<uint32_t>(off[i]);
    for (size_t i = 0; i < nbr.size(); ++i) out_neighbors[i] = static_cast<uint32_t>(nbr[i]);
  }
};
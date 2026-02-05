#include <iostream>
#include <vector>
#include <map>
#include <set> // binary tree
#include <stack>
#include <queue>
#include <algorithm>
#include <unordered_set> // hash table
#include <unordered_map>
#include <limits>
#include <string>

using std::cout, std::cin, std::endl, std::vector;
using label = size_t;
using weightedEdge = std::pair<size_t, label>; // distance, label
using Graph = vector<vector<weightedEdge>>;    // assumes labels are from 0 to .size()-1

struct compareDists
{
  bool operator()(const weightedEdge &w1, const weightedEdge &w2)
  {
    return w1.first > w2.first; // min-heap
  }
};

/// @brief calculates the shortest path from start to target using Dijkstra's algorithm
/// @param g
/// @param start
/// @param target
/// @return returns pair of (distance, path)
std::pair<size_t, vector<label>> dijkstra(const Graph &g, const label start, const label target)
{
  const size_t INF = std::numeric_limits<size_t>::max();
  vector<size_t> bestDist(g.size(), INF);
  vector<label> prev(g.size(), -1);

  for (label i = 0; i < g.size(); i++)
  {
    prev[i] = i;
  }

  std::priority_queue<weightedEdge, vector<weightedEdge, std::greater<weightedEdge>>> pq; // min-heap
  pq.push({0, start});
  bestDist[start] = 0;

  while (!pq.empty())
  {
    // TODO: jak funguji reference na objekty, ktere zmenily sve inicialni umisteni
    const auto &[currentDistance, currentNode] = pq.top();
    pq.pop();

    if (currentNode == target)
    {
      break; // found shortest path to target
    }

    // Dreal with obsolete duplicated nodes
    if (currentDistance > bestDist[currentNode])
    {
      continue; // outdated entry
    }

    for (const auto &[v, w] : g[currentNode])
    { // v - value, w - weight
      size_t newDist = currentDistance + w;
      if (newDist < bestDist[v])
      { // takes care of found/completed nodes just like in BFS
        bestDist[v] = newDist;
        prev[v] = currentNode;
        pq.push({newDist, v}); // potential source of duplicit entries
      }
    }
  }

  size_t length = bestDist[target];
  // Path reconstruction from start to target
  vector<label> path;
  label currentNode = target;
  while (currentNode != start)
  {
    path.push_back(currentNode);
    currentNode = prev[currentNode];
  }
  path.push_back(start);
  std::reverse(path.begin(), path.end());

  return {length, path};
}

struct Node
{
  double frequency;
  char character;
  Node *left;
  Node *right;

  Node(double freq, char ch) : frequency(freq), character(ch), left(nullptr), right(nullptr) {}
  Node(double freq) : frequency(freq), character('\0'), left(nullptr), right(nullptr) {}

  bool isLeaf()
  {
    return left == nullptr && right == nullptr;
  };

  // TODO: destructor to free memory
};

/// @brief Constructs a Huffman tree from a frequency map
/// @param frequencyMap A map of characters to their frequencies
/// @return Pointer to the root of the Huffman tree
Node *constructHtree(const std::map<char, double> &frequencyMap)
{
  std::priority_queue<Node *, std::vector<Node *>, compareNodes> pq;
  for (const auto &[ch, freq] : frequencyMap)
  {
    pq.push(new Node(freq, ch));
  }

  while (pq.size() > 1)
  {
    Node *left = pq.top();
    pq.pop();
    Node *right = pq.top();
    pq.pop();
    Node *merged = new Node(left->frequency + right->frequency);
    merged->left = left;
    merged->right = right;
    pq.push(merged);
  }

  return pq.top();
}

struct compareNodes
{
  bool operator()(const Node *n1, const Node *n2)
  {
    return n1->frequency > n2->frequency; // min-heap
  }
};

void createCodeTable(Node *root, const std::string &prefix, std::map<char, std::string> &codeTable)
{
  if (root->isLeaf())
  {
    codeTable[root->character] = prefix;
    return;
  }
  if (root->left)
  {
    createCodeTable(root->left, prefix + "0", codeTable);
  }
  if (root->right)
  {
    createCodeTable(root->right, prefix + "1", codeTable);
  }
}

std::string decode(Node *root, const std::string &encodedText) // Generated Decode function
{
  std::string decodedText;
  Node *currentNode = root;
  for (char bit : encodedText)
  {
    if (bit == '0')
    {
      currentNode = currentNode->left;
    }
    else if (bit == '1')
    {
      currentNode = currentNode->right;
    }

    if (currentNode->isLeaf())
    {
      decodedText += currentNode->character;
      currentNode = root; // reset to root for next character
    }
  }
  return decodedText;
}

int main()
{

  return 0;
}

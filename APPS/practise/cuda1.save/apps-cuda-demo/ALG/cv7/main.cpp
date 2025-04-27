#include "Graph.h"

int main () {
  Graph g;

  g.insert(1);

  vector<int> neighbors;

  neighbors.push_back(1);

  g.insert(2, neighbors);
  
  neighbors.push_back(2);
  
  
  g.insert(3, neighbors);


  neighbors.clear();
  neighbors.push_back(3);

  g.insert(4, neighbors);
  g.insert(5, neighbors);



  g.insert(6);
  g.insert(7);

  neighbors.push_back(6);
  neighbors.push_back(7);
  
  g.insert(8, neighbors);


  neighbors.clear();
  neighbors.push_back(2);
  neighbors.push_back(8);

  g.insert(9, neighbors);

  neighbors.clear();
  neighbors.push_back(9);
  neighbors.push_back(10);

  g.insert(10, neighbors);
  

  g.bfs(1);


  return 0;
}
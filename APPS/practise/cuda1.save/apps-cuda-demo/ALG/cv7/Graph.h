#pragma once
#include <iostream>
#include <vector>
using namespace std;

class Graph
{
private:
  class Vertex
  {
  public:
    int id;
    vector<Vertex *> neighbors;
    unsigned int color = 2; // 0 - black, 1 - gray, 2 - white

    Vertex(int id)
    {
      this->id = id;
    };
  };

  vector<Vertex *> vertices;
  Vertex* findVertex(int id);
public:
  void insert(int id);
  void insert(int id, vector<int> neighborsIDs);

  void bfs(int id);
  void dfs(int id);

};
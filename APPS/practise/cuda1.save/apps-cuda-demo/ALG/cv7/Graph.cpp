#include "Graph.h"
#include <queue>
#include <stack>
void Graph::insert(int id)
{
  this->vertices.push_back(new Vertex(id));
}

Graph::Vertex *Graph::findVertex(int id)
{
  // for (int i = 0; i < this->vertices.size(); i++)
  // {
  //   if(this->vertices[i]->id == id) {
  //     return this->vertices[i];
  //   }
  // }

  for (auto vertex : this->vertices)
  {
    if (vertex->id == id)
    {
      return vertex;
    }
  }

  return nullptr;
}

void Graph::insert(int id, vector<int> neighborsIDs)
{
  Vertex *newVertex = new Vertex(id);

  this->vertices.push_back(newVertex);

  for (auto neighborID : neighborsIDs)
  {
    Vertex *neighbor = findVertex(neighborID);

    if (neighbor)
    {
      newVertex->neighbors.push_back(neighbor);
      if (newVertex != neighbor)
      {
        neighbor->neighbors.push_back(newVertex);
      }
    }
  }
}

void Graph::bfs(int id)
{
  Vertex *startingVertex = findVertex(id);

  queue<Vertex *> que;
  que.push(startingVertex);
  startingVertex->color = 1; // gray

  while (!que.empty())
  {
    Vertex *currentVertex = que.front();
    que.pop();

    cout << currentVertex->id << endl;

    currentVertex->color = 0; // black

    for (auto neighbor : currentVertex->neighbors)
    {
      if (neighbor->color == 2)
      { // only undiscovered neighbors
        que.push(neighbor);
        neighbor->color = 1;
      }
    }
  }
}

void Graph::dfs(int id)
{
  Vertex *startingVertex = findVertex(id);

  stack<Vertex *> que;
  que.push(startingVertex);
  startingVertex->color = 1; // gray

  while (!que.empty())
  {
    Vertex *currentVertex = que.top();
    que.pop();

    cout << currentVertex->id << endl;

    currentVertex->color = 0; // black

    for (auto neighbor : currentVertex->neighbors)
    {
      if (neighbor->color == 2)
      { // only undiscovered neighbors
        que.push(neighbor);
        neighbor->color = 1;
      }
    }
  }
}
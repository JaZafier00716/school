#include <iostream>
#include <vector>
#include <map>
#include <set> // binary tree
#include <stack>
#include <queue>
#include <algorithm>
#include <unordered_set> // hash table
#include <unordered_map>

using std::cout, std::cin, std::endl, std::vector;
using ScalarType = double;
using Matrix = vector<vector<ScalarType>>;

struct Node
{
  Node *left;
  Node *right;
  int key;
  int height;
  Node(const int val) : left(nullptr), right(nullptr), key(val), height(0) {}
};

class BinaryTree
{
private:
  Node *root;

  void clear(Node *node)
  {
    if (node)
    {
      clear(node->left);
      clear(node->right);
      delete node;
    }
  }

  int height(Node *node)
  {
    if (!node)
      return -1;
    return node->height;
  }

  int balanceFactor(Node *node)
  {
    return height(node->left) - height(node->right);
  }

  void updateHeight(Node *node)
  {
    if (node)
    {
      node->height = 1 + std::max(height(node->left), height(node->right));
    }
  }

  Node* rotateRight(Node* r) {
    Node* c = r->left;
    Node* t2 = c->right;
    c->right = r;
    r->left = t2;
    updateHeight(r);
    updateHeight(c);
    
    return c;
  }


  Node* rotateLeft(Node* r) {
    Node* c = r->right;
    Node* t2 = c->left;
    c->left = r;
    r->right = t2;
    updateHeight(r);
    updateHeight(c);
    
    return c;
  }

  Node* balance(Node* node) {
    updateHeight(node);
    int bf = balanceFactor(node);

    if(bf > 1) {
      if(balanceFactor(node->left) < 0) {
        // Left-Right case
        node->left = rotateLeft(node->left);
      }
      return rotateRight(node);
    }
    if(bf < -1) {
      if(balanceFactor(node->right) > 0) {
        // Right-Left case
        node->right = rotateRight(node->right);
      }
      return rotateLeft(node);
    }

    return node;
  }

  Node *insert(Node *node, const int key)
  {
    if (!node)
    {
      return new Node(key);
    }
    if (key < node->key)
    {
      node->left = insert(node->left, key);
    }
    else if (key > node->key)
    {
      node->right = insert(node->right, key);
    }
    else
    {
      // key already exists, do nothing
      return node;
    }
    return balance(node);
  }

  Node *findMinKey(Node *node)
  {
    if (!node)
    {
      std::cerr << "Error: findMinKey called with null node." << std::endl;
      return nullptr;
    }
    while (node->left)
    {
      node = node->left;
    }
    return node;
  }

  Node *remove(Node *node, const int key)
  {
    if (!node)
    {
      return nullptr;
    }
    if (key < node->key)
    {
      node->left = remove(node->left, key);
    }
    else if (key > node->key)
    {
      node->right = remove(node->right, key);
    }
    else
    {
      // Node with the key found
      if (!node->left || !node->right)
      {
        // 0 or 1 child
        Node *temp = node->left ? node->left : node->right;
        delete node;
        return temp;
      }
      else
      {
        // 2 children
        Node *successor = findMinKey(node->right);
        if (successor)
        {
          node->key = successor->key;
          node->right = remove(node->right, successor->key);
        }
      }
    }

    return node;
  }

  bool containsKey(Node *node, const int key)
  {
    if (!node)
      return false;
    if (node->key == key)
      return true;
    else if (key < node->key)
      return containsKey(node->left, key);
    else
      return containsKey(node->right, key);
  }

  void preOrderPrint(Node *node)
  {
    if (!node)
      return;
    std::cout << node->key << " ";
    preOrderPrint(node->left);
    preOrderPrint(node->right);
  }

public:
  BinaryTree() : root(nullptr) {}
  ~BinaryTree()
  {
    clear(root);
  }

  void insert(const int key)
  {
    root = insert(root, key);
  }

  void remove(const int key)
  {
    root = remove(root, key);
  }

  bool containsKey(const int key)
  {
    return containsKey(root, key);
  }

  void preOrderPrint()
  {
    preOrderPrint(root);
    std::cout << std::endl;
  }

  void printLevelOrder()
  {
    if (!root)
    {
      std::cout << "Tree is empty." << std::endl;
      return;
    }
    std::queue<Node *> q;
    q.push(root);
    while (!q.empty())
    {
      size_t nodeCount = q.size();

      bool foundNewNode = false;
      for (size_t i = 0; i < nodeCount; ++i)
      {
        Node *currentNode = q.front();
        q.pop();
        if (currentNode)
        {
          std::cout << currentNode->key << " ";
          q.push(currentNode->left);
          q.push(currentNode->right);
          if (currentNode->left || currentNode->right)
          {
            foundNewNode = true;
          }
        }
        else
        {
          std::cout << "# ";
        }
      }
      std::cout << std::endl;
      if (!foundNewNode)
      {
        break;
      }
    }
  }
};

int main()
{
  const std::vector<int> values = {2,3,4,5,6,7,8};
  BinaryTree tree;
  for (int val : values)
  {
    tree.insert(val);
  }
  tree.preOrderPrint();
  tree.printLevelOrder();
  
  return 0;
}
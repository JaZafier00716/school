#include <iostream>
#include <vector>
#include <cstdint>
#include <fstream>
#include <sstream>

using namespace std;

struct Node
{
  Node *left;
  Node *right;
  int value;

  Node(int value) : value(value)
  {
    left = nullptr;
    right = nullptr;
  }
};

class BST
{
private:
  Node *root;
  int total;
  vector<int> distances;

  Node *insert(Node *node, int value)
  {
    if (!node)
    {
      return new Node(value); // height
    }

    if (value > node->value)
    {
      // child in right sub_tree
      node->right = insert(node->right, value); // add childdd += node->left->size * 2;
    }
    else
    {
      if (value < node->value)
      {
        // child in left sub_tree
        node->left = insert(node->left, value); // add child
      }
      else
      {
        std::cerr << "value already in binary tree" << endl;
        return node; // value already in binary tree
      }
    }
    return node;
  }

  int distance(Node* node, int a, int b) {
    if(!node) return 0;
    if(a < node->value && b < node->value) {
      return distance(node->left, a, b);
    }
    if(a > node->value && b > node->value) {
      return distance(node->right, a, b);
    }
    return depth(node, a) + depth(node, b);
  }

  int depth(Node* node, int value) {
    if(!node) return 0;
    if(node->value == value) return 0;
    if(value < node->value) {
      return 1 + depth(node->left, value);
    } else {
      return 1 + depth(node->right, value);
    }
  }


public:
  BST()
  {
    total = 0;
    root = nullptr;
  }

  int insertAndSum(int value) {
    root = insert(root, value);

    for(int v : distances) {
      total += distance(root, v, value);
    }
    distances.push_back(value);
    return total;
  }

};

std::vector<int> readIntegersFromFile(const std::string& filename) {
    std::ifstream file(filename);
    std::vector<int> numbersVec;

    if (!file.is_open()) {
        std::cerr << "Unable to open file: " << filename << std::endl;
        return numbersVec;
    }

    std::string line;

    // change if to while to read the whole file
    if (std::getline(file, line)) {
        std::stringstream lineAsStream(line);
        int num;
        while (lineAsStream >> num) {  // NOTE: this assumes that the input file has the data we want
            numbersVec.push_back(num);
        }
    }

    file.close();
    return numbersVec;
}

int main(int argc, char **argv)
{
  if(argc != 3) {
    cerr << "Usage: " << argv[0] << " <input_size> <input_file>" << endl;
    return 1;
  }
  vector<int> values(stoi(argv[1]));
  
  values = readIntegersFromFile(argv[2]);


  BST *tree = new BST();

  int i = 0;

  for (const int &value : values)
  {
    int totalDistance = tree->insertAndSum(value);
    cout << "After inserting " << value << ", total distance is: " << totalDistance << endl;
    // cout << "----------------------------------------------------------------------" << endl;
    i++;
    if(i > 20) {
      break;
    }
  }
}
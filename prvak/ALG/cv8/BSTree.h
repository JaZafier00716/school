#pragma once
#include <iostream>
using namespace std;


class BSTree
{
private:
  class Node {
    public:
      int value;
      Node *left = nullptr;
      Node *right = nullptr;

      Node(int value) {
        this->value = value;
      }
  };

  Node* root = nullptr;

  int countRoots(Node*root) {
    if(root  == nullptr) {
      return 0;
    }

    return 1 + countRoots(root->left) + countRoots(root->right);
  }

  // void printSorted(Node*root, bool asc) {
  //   if(asc) {
  //     if(root->left) printSorted(root->left, asc);
  //     cout << root->value << " ";
  //     if(root->right) printSorted(root->right, asc);
  //   } else {
  //     if(root->right) printSorted(root->right, asc);
  //     cout << root->value << " ";
  //     if(root->left) printSorted(root->left, asc);
  //   }
  // }

  void printSorted(Node* root, bool asc) {
    if(root == nullptr) {
      return;
    }


    printSorted(asc ? root->left : root->right, asc);
    cout << root->value << " ";
    printSorted(asc ? root->right : root->left, asc);
  }

  int height(Node* root) {
    if(root == nullptr) {
      return 0;
    }

    return 1+max(height(root->right), height(root->left));
    // int r = height(root->right);
    // int l = height(root->left);
    // return 1+(r > l ? r : l);
  }

  bool isBalanced(Node* root) {
    if(this->root == nullptr) {
      return true;
    }

    int l = height(root->left);
    int r = height(root->right);

    return abs(l-r) <= 1;
  }

  void iAmGroot(int value, Node*& root);
public:
  void iAmGroot(int value);

  int countRoots() {
    return countRoots(this->root);
  }
  void printSorted(bool asc = true) {
    printSorted(this->root, asc);
  }

  int height() {
    return height(this->root);
  }

  bool isBalanced() {
    return isBalanced(this->root);
  }
};


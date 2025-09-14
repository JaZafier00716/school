#include "BSTree.h"

void BSTree::iAmGroot(int value) {
  this->iAmGroot(value, this->root);
}

void BSTree::iAmGroot(int value, Node*& root) {
  if(root == nullptr) {
    root = new Node(value);
    return;
  }

  if(value < root->value) {
    iAmGroot(value, root->left);
  }

  if(value > root->value) {
    iAmGroot(value, root->right);
  }

  


}
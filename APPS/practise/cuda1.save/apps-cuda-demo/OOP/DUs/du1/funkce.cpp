#include "funkce.h"

Node::Node()
{
  this->left = nullptr;
  this->right = nullptr;
}

Node::Node(int key, double value)
{
  this->key = key;
  this->value = value;
  this->left = nullptr;
  this->right = nullptr;
}

Node::~Node()
{
  if (this->left != nullptr)
  {
    delete this->left;
    this->left = nullptr;
  }
  if (this->right != nullptr)
  {
    delete this->right;
    this->right = nullptr;
  }
}

void Node::SetLeft(Node *new_node)
{
  this->left = new_node;
};

void Node::SetRight(Node *new_node)
{
  this->right = new_node;
};

void Node::SetKey(int key)
{
  this->key = key;
};

void Node::SetValue(double value)
{
  this->value = value;
};

Node *Node::GetLeft()
{
  return this->left;
};

Node *Node::GetRight()
{
  return this->right;
};

void Node::CreateNext(int key, double value)
{
  Node *new_node = new Node(key, value);
  if (!new_node)
  {
    cout << "error" << endl;
  }

  if (value <= this->value)
  {
    if (this->left == nullptr)
    {
      this->SetLeft(new_node);
    }
    else
    {
      delete new_node;
      this->left->CreateNext(key, value);
    }
  }
  else
  {
    if (this->right == nullptr)
    {
      this->SetRight(new_node);
    }
    else
    {
      delete new_node;
      this->right->CreateNext(key, value);
    }
  }
}

void Node::PrintTree()
{
  cout << "[value: " << this->value;
  cout << " left: ";
  if (this->left != nullptr)
  {
    this->left->PrintTree();
  } else {
    cout << "nullptr";
  }
  cout << " right: ";
  if (this->right != nullptr)
  {
    this->right->PrintTree();
  } else {
    cout << "nullptr";
  }
  cout << "]";
}

void Node::AltPrint(int level) {
  // Print value
  for (int i = 0; i < level; i++)
  { 
    if(level > 0 ) cout << "|";
    cout << "\t";
  }
  cout << "value:\t" << this->value << endl;

  // Print Left
  for (int i = 0; i < level; i++)
  { 
    if(level > 0 ) cout << "|";
    cout << "\t";
  }
  cout << "left:\t";

  // Print Left Children
  if(this->left != nullptr) {
    cout << endl;
    this->left->AltPrint(level+1);
  } else {
    cout << "nullptr" << endl;
  }

  // Print Right
  for (int i = 0; i < level; i++)
  { 
    if(level > 0 ) cout << "|";
    cout << "\t";
  }
  cout << "right:\t";

  // Print Right Children
  if(this->right != nullptr) {
    cout << endl;
    this->right->AltPrint(level+1);
  } else {
    cout << "nullptr" << endl;
  }
}
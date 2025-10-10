#include "header.h"

int main(int argc, char *argv[])
{
  if(argc != 3) {
    std::cerr << "Usage: " << argv[0] << " <construction_data> <deletion_data>" << std::endl;
    return -1;
  }
  const auto creation_values = readIntegersFromFile(argv[1]);
  const auto deletion_values = readIntegersFromFile(argv[2]);


  BinaryTree tree;
  for (int val : creation_values)
  {
    tree.insert(val);
  }

  for (int val : deletion_values)
  {
    tree.remove(val);
  }

  tree.postOrderPrint();
  tree.preOrderPrint();
  tree.inOrderPrint();
  
  return 0;
}
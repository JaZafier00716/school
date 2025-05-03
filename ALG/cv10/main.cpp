#include <iostream>
#include <string>
#include <vector>
using namespace std;

enum
{
  LEFT = -1,
  RIGHT = 1
};

class ArrowedInt
{
public:
  int value;
  int direction = LEFT;
  ArrowedInt(int value)
  {
    this->value = value;
  }
};

void printArr(const vector<ArrowedInt *> &array)
{
  for (const auto element : array)
  {
    cout << element->value;
  }
}

bool isMobile(const vector<ArrowedInt *> &array, int index)
{
  int direction = array[index]->direction;
  if (index == 0 && direction == LEFT)
  {
    return false;
  }
  if (index == array.size() - 1 && direction == RIGHT)
  {
    return false;
  }

  if (array[index]->value > array[index + direction]->value)
  {
    return true;
  }
  return false;
}

int getLargestMobile(const vector<ArrowedInt *> &array)
{
  int largestMobile = -1;
  
  for (int i = 0; i < array.size(); i++)
  {
    if (isMobile(array, i))
    {
      if (largestMobile == -1)
      {
        largestMobile = i;
        continue;
      }
      if (array[i]->value > array[largestMobile]->value)
      {
        largestMobile = i;
        cout << "max: " << array[i]->value << endl;
      }
    }
  }
  return largestMobile;
}

void swap(vector<ArrowedInt *> &array, int index)
{
  ArrowedInt *temp = array[index];
  int direction = array[index]->direction;

  array[index] = array[index + direction];
  array[index + direction] = temp;
}

void changeDirs(vector<ArrowedInt *> &array, int index)
{
  for (auto child : array)
  {
    if (child->value > array[index]->value)
    {
      child->direction = -child->direction;
    }
  }
}

void JohnsonTrotter(vector<ArrowedInt *> &array)
{
  int largestMobile;
  do
  {
    cout << "--------" << endl;
    largestMobile = getLargestMobile(array);
    if (largestMobile == -1)
    {
      break;
    }
    int direction = array[largestMobile]->direction;

    swap(array, largestMobile);

    printArr(array);
    cout << " ->" << largestMobile << "" << array[largestMobile]->direction << endl;
    cout << endl;

    changeDirs(array, largestMobile);

  } while (largestMobile != -1);
}

int main()
{
  vector<ArrowedInt *> aInts;
  unsigned int N = 3;

  for (int i = 0; i < N; i++)
  {
    aInts.push_back(new ArrowedInt(i));
  }

  JohnsonTrotter(aInts);
  // printArr(aInts);

  return 0;
}
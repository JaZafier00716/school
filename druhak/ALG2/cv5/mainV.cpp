#include <iostream>
#include <vector>
#include <exception>

using std::cout, std::cin, std::endl, std::vector;


template<typename T>
struct itemWIthPriority
{
  T item;
  int priority;

  bool operator<(const itemWIthPriority &other) const
  {
    return priority < other.priority;
  }
};


// children:
// 2i + 1
// 2i + 2
// parent:
// (i - 1) / 2 - because of integer division

class MyHeap {
private:
  vector<int> data;
  size_t parentIndex(size_t i) {
    if (i == 0) {
      throw std::out_of_range("Root node has no parent");
      return 0;
    }

    return (i - 1) / 2;
  }

  void heapify(size_t i) {
    int largest = i;
    size_t left = 2 * i + 1;
    size_t right = 2 * i + 2;
    if (left < data.size() && data[left] > data[largest]) {
      largest = left;
    }
    if (right < data.size() && data[right] > data[largest]) {
      largest = right;
    }
    if (largest != i) {
      std::swap(data[i], data[largest]);
      heapify(largest);
    }
  }

  void makeHeap() {
    for (int i = (data.size() / 2) - 1; i >= 0; i--) { // until overflow
      heapify(i);
    }
  }

public:

  MyHeap() = default;
  MyHeap(const vector<int> &values) : data(values) {
    makeHeap();
  }

  MyHeap(const vector<int> &&values) : data(std::move(values)) {
    makeHeap();
  }

  void insert(int key) {
    data.push_back(key);
    size_t current_index = data.size() - 1;
    size_t parent_index = parentIndex(current_index);
    while (data[current_index] < data[parent_index]) { // Works only because parent(0) = 0
      std::swap(data[current_index], data[parent_index]);
      current_index = parent_index;
      parent_index = parentIndex(current_index);
    }
  }

  int getMax() {
    if (data.empty()) {
      throw std::out_of_range("Heap is empty");
    }
    int maximum = data[0];

    // Heap reconstruction
    data[0] = data.back();
    data.pop_back();
    if(!data.empty()) {
      heapify(0);
    }

    return maximum;
  }

  void print() {
    for (const auto &el : data) {
      cout << el << " ";
    }
    cout << endl;
  }

};


int main () {
  vector<int> prednaska = {2,9,7,6,5,8};

  MyHeap heap(std::move(prednaska));
  prednaska.clear();
  heap.print();
  heap.insert(10);
  heap.print();
  cout << "Max: " << heap.getMax() << endl;
  heap.print();
}


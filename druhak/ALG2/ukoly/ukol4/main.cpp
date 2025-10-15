#include<vector>
#include<exception>
#include<iostream>
#include<algorithm>
#include <fstream>
#include <string>
#include <sstream>

using std::vector;

class Heap{
    private:
    vector<int> data;
    size_t n = 2;
    
    size_t parent(size_t index) const {
        if(index == 0){return 0;} // note that heap root has no parent
        return (index - 1) / n;
    }

    void heapify(size_t index){
        size_t largest = index;
        vector<size_t> children(n);

        // Init children
        for(size_t i = 0; i < n; i++){
            children[i] = n * index + i + 1;
        }

        // find largest child
        for(const auto child : children){
            if(child >= data.size()){ break;} // indices can be out of bounds
            if(data[child] > data[largest]){
                largest = child;
            }
        }

        if(largest != index){
            std::swap(data[index], data[largest]);
            heapify(largest);  // restore heap property on subtree
        }
    }

    void makeHeap(){
        // build heap from bottom to top
        for(int i = (data.size()-2) / n; i >= 0; i--){
            heapify(i);
        }
    }

    public:
    Heap() = default;

    Heap(size_t childrenNum){
        n = childrenNum;
    }

    Heap(const vector<int>& input, size_t childrenNum = 2){
        data = input;
        n = childrenNum;
        makeHeap();
    }

    Heap(vector<int>&& input, size_t childrenNum = 2){
        data = std::move(input);
        n = childrenNum;
        makeHeap();
    }

    void insert(const int value){
        size_t currentIndex = data.size();
        data.push_back(value);
        size_t parentIndex = parent(currentIndex);

        // repair heap property by pushing the new number up
        while(currentIndex != 0 and data[currentIndex] > data[parentIndex]){
            std::swap(data[currentIndex], data[parentIndex]);
            currentIndex = parentIndex;
            parentIndex = parent(currentIndex);
        }
    }

    int getMax(){
        if( data.empty() ){
            throw std::out_of_range("Trying to extract item from empty heap");
        }
        int maxValue = data[0];

        data[0] = data.back();
        data.pop_back();
        if( data.size() > 1){
            heapify(0);
        }

        return maxValue;
    }

    void print() const {
        for(const int item : data){
            std::cout << item << " ";
        }
        std::cout << "\n";
    }

    void clear() {
        data.clear();
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

void test(vector<int> data, size_t n){
    // vector<int> data = {10, 11, 1, 2, 3, 100, 200};
    Heap heap(data, n);
    // for(const int item : data){
    //     heap.insert(item);
    // }
    heap.print();
    heap.getMax();
    heap.print();
    heap.insert(42);
    heap.print();
    heap.insert(-5);
    heap.print(); 
    heap.getMax();
    heap.print();
}


int main([[maybe_unused]] int argc, [[maybe_unused]] char* argv[]){
    if(argc != 3) {
        std::cerr << "Usage: " << argv[0] << " <number_of_children> <file>\n";
        return -1;
    }

    test(readIntegersFromFile(argv[2]), std::stoul(argv[1]));
    return 0;
}
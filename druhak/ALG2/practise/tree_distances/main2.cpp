#include <iostream>
#include <vector>
#include <cstdint>
#include <fstream>
#include <sstream>
#include <string>

using namespace std;

struct ChildData {
    long long distanceSum;
    long long nodeNum; // <- changed to 64-bit
};

struct Node {
    Node *left;
    Node *right;
    int value;
    ChildData leftData;
    ChildData rightData;

    Node(int value) : value(value) {
        left = nullptr;
        right = nullptr;
        leftData = {0LL, 0LL};
        rightData = {0LL, 0LL};
    }
};

class BST {
private:
    Node *root;
    long long total;

    Node *addNode(Node *node, int value, int &height) {
        if (!node) {
            node = new Node(value);
            height = 1;
            return node;
        }

        if (value > node->value) {
            // insert into right subtree
            node->right = addNode(node->right, value, height);

            // update right child aggregated data (pre-incremented height was used for the child)
            node->rightData.nodeNum += 1;
            node->rightData.distanceSum += height;

            // all nodes in left subtree are 'height' farther from the new node
            total += node->leftData.distanceSum + node->leftData.nodeNum * (long long)height;
        } else if (value < node->value) {
            // insert into left subtree
            node->left = addNode(node->left, value, height);

            node->leftData.nodeNum += 1;
            node->leftData.distanceSum += height;

            total += node->rightData.distanceSum + node->rightData.nodeNum * (long long)height;
        } else {
            // duplicate: ignore
            height = 0;
            return node;
        }

        // distance from the newly added child up to this node
        total += height;

        // parent sees child's height increased by 1
        height++;

        return node;
    }

public:
    BST() {
        total = 0;
        root = nullptr;
    }

    // returns running total (64-bit)
    long long addNode(int value) {
        int height = 0;
        root = addNode(root, value, height);
        return total;
    }
};

// read all integers (any whitespace) from file into vector<int>
vector<int> readIntegersFromFile(const string &filename) {
    ifstream file(filename);
    vector<int> numbersVec;
    if (!file.is_open()) {
        cerr << "Unable to open file: " << filename << endl;
        return numbersVec;
    }
    string line;
    while (getline(file, line)) {
        stringstream ss(line);
        long long num_ll;
        while (ss >> num_ll) {
            numbersVec.push_back(static_cast<int>(num_ll));
        }
    }
    file.close();
    return numbersVec;
}

// read all integers (any whitespace) as long long
vector<long long> readLongLongsFromFile(const string &filename) {
    ifstream file(filename);
    vector<long long> numbersVec;
    if (!file.is_open()) {
        cerr << "Unable to open file: " << filename << endl;
        return numbersVec;
    }
    string line;
    while (getline(file, line)) {
        stringstream ss(line);
        long long num;
        while (ss >> num) numbersVec.push_back(num);
    }
    file.close();
    return numbersVec;
}

int main(int argc, char *argv[]) {
    if (argc != 4) {
        cout << "Usage: " << argv[0] << " <size> <input_file> <expected_output_file>" << endl;
        return 1;
    }

    // Note: we ignore the <size> argument, keep it for compatibility
    auto input_values = readIntegersFromFile(argv[2]);
    auto output_values = readLongLongsFromFile(argv[3]);

    if (output_values.size() && output_values.size() != input_values.size()) {
        cerr << "Warning: expected-output size != input size. Proceeding but comparisons may be mismatched." << endl;
    }

    BST tree;
    vector<long long> outputs;
    outputs.reserve(input_values.size());

    for (size_t i = 0; i < input_values.size(); ++i) {
        long long total = tree.addNode(input_values[i]);
        outputs.push_back(total);
    }

    for (size_t i = 0; i < output_values.size(); ++i) {
        if (i < output_values.size()) {
            if (output_values[i] != outputs[i]) {
                cout << "Test failed at insertion of " << i << "th value " << input_values[i] << endl;
                cout << "Expected: " << output_values[i] << ", Got: " << outputs[i] << endl;
                return 1;
            }
        } else {
            // no expected value provided for this index
            cout << "No expected output at index " << i << "; got " << outputs[i] << endl;
        }
    }

    cout << "All checks passed." << endl;
    return 0;
}

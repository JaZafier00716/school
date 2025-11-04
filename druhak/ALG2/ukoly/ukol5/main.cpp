#include <algorithm>
#include <iostream>
#include <set>
#include <fstream>
#include <string>
#include <sstream>
#include <vector>
#include <map>

using namespace std;


void k_combinations(const vector<int> &elements, const size_t k, const size_t start, set<int>& current, vector<set<int>>& result) {
    if (current.size() == k) {
        result.push_back(current);
        return;
    }

    for (size_t i = start; i < elements.size(); i++) {
        current.insert(elements[i]);
        k_combinations(elements, k, i + 1, current, result);
        current.erase(elements[i]);
    }
}

vector<set<int>> get_combinations(const set<int>& input_set, int k) {
    const vector<int> elements(input_set.begin(), input_set.end());
    vector<set<int>> result;
    set<int> current;

    k_combinations(elements, k, 0, current, result);
    return result;
}

vector<int> orderSet(const set<int>& input_set) {
    vector<int> result(input_set.begin(), input_set.end());
    // ranges::sort(result);
    sort(result.begin(), result.end());

    return result;
}

map<int, int> count_simplices(const vector<set<int>>& simplexes) { // <dimension, number_of_simplexes_in_dimension>
    map<int, int> result;
    map<vector<int>, int> occurrence_num;

    for (const auto& simplex : simplexes) {
        const size_t n = simplex.size();
        for (size_t k = 1; k <= n; k++) {
            auto faces = get_combinations(simplex, k);
            for (const auto& face : faces) {
                vector<int> orderedVertices = orderSet(face);
                occurrence_num[orderedVertices] = occurrence_num[orderedVertices] + 1;
            }
        }
    }

    for (const auto& [vertices, count] : occurrence_num) {
        int dimension = vertices.size() - 1;
        result[dimension]++;
    }

    return result;
}

vector<vector<int>> bounds(const vector<set<int>>& simplexes) {
    map<vector<int>, int> occurrence_num;

    for (const auto& simplex : simplexes) {
        auto faces = get_combinations(simplex, simplex.size()-1);
        for (const auto& face : faces) {
            vector<int> orderedVertices = orderSet(face);
            occurrence_num[orderedVertices] = occurrence_num[orderedVertices] + 1;
        }
    }

    vector<vector<int>> result;
    for (const auto& [vertice, count] : occurrence_num) {
        if (count == 1) {
            result.push_back(vertice);
        }
    }


    return result;
}

vector<set<int>> LoadFromFile(const string& filename) {
    ifstream file(filename);
    vector<set<int>> simplexes;

    if (!file.is_open()) {
        std::cerr << "Unable to open file: " << filename << std::endl;
        return simplexes;
    }

    string line;

    while (getline(file, line)) {
        set<int> numberSet;
        stringstream ss(line);
        int num;
        while (ss >> num) {
            numberSet.insert(num);
        }
        if (!numberSet.empty()) {
            simplexes.push_back(numberSet);
        }
    }

    file.close();

    return simplexes;
}

int main(const int argc, char **argv) {
    if (argc != 2) {
        std::cerr << "Usage: " << argv[0] << " <filename>" << std::endl;
        return EXIT_FAILURE;
    }

    auto inputSet = LoadFromFile(argv[1]);

    map<int, int> k_simplices_count = count_simplices(inputSet);

    int chi = 0;
    for (const auto& [dimension, count] : k_simplices_count) {
        if (dimension % 2 == 0) {
            chi += count;
        } else {
            chi -= count;
        }
        switch (dimension) {
            case 0:
                cout << "Vertices: " << count << endl;
                break;
            case 1:
                cout << "Edges: " << count << endl;
                break;
            case 2:
                cout << "Triangles: " << count << endl;
                break;
            case 3:
                cout << "Tetrahedrons: " << count << endl;
                break;
        }
    }
    cout << "chi: " << chi << endl << endl;

    auto boundaries = bounds(inputSet);

    if (boundaries.empty()) {
        cout << "Boundary:" << endl;
        cout << "is empty" << endl;
    }
    for (const auto &bound : boundaries) {
        for (size_t i = 0; i < bound.size(); i++) {
            cout << bound[i] << (i < bound.size() - 1 ? " " : "");
        }
        cout << endl;
    }

    return 0;
}
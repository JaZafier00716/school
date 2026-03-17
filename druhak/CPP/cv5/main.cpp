#include <algorithm>
#include <print>
#include <functional>
#include <vector>
#include <iostream>
#include <memory>
using std::size_t;

class Tree {
    Tree* parent;

    std::unique_ptr<Tree> left;
    std::unique_ptr<Tree> right;
};



class Vec {
private:
    int* begin = nullptr;
    int* end = nullptr;
    size_t capacity = 0;

public:
    Vec() noexcept = default;

    Vec(const Vec& other) {
        if (other.capacity != 0) {
            begin = new int[other.capacity];
            const size_t size = other.size();
            std::copy_n(other.begin, size, begin);
            end = begin + size;
            capacity = other.capacity;
        }
    }

    Vec(Vec&& tmp) noexcept {
        begin = tmp.begin;
        tmp.begin = nullptr;
        end = tmp.end;
        tmp.end = nullptr;
        capacity = tmp.capacity;
        tmp.capacity = 0;
    }

    Vec& operator=(const Vec& other) {
        if (this != &other) {
            Vec copy(other);
            std::swap(begin, copy.begin);
            std::swap(end, copy.end);
            std::swap(capacity, copy.capacity);
        }
        return *this;
    }

    ~Vec() {
        delete[] begin;
        begin = nullptr;
        end = nullptr;
        capacity = 0;
    }

    int& operator[](const size_t index) const noexcept {
        return begin[index];
    }

    void emplace_back(const int item) {
        const size_t size = this->size();
        if (size == capacity) {
            capacity = std::max(32ul, capacity*2);
            const auto new_data = new int[capacity];
            std::copy(begin, end, new_data);
            delete[] begin;
            begin = new_data;
            end = begin + size;
        }

        *end = item;
        end++;
    }

    size_t size() const noexcept {
        return static_cast<size_t>(end - begin);
    }

};

Vec createVec() {
    Vec vec;

    for (int i = 0; i < 10; ++i) {
        vec.emplace_back(i*i);
    }

    return vec;
}

Vec mutate(Vec vector) {
    vector[0] = 1989;
    return vector;
}

// Vec mutate(const Vec& orig) { // Does not allow move, it will call copy constructor instead, which is not what we want. Because we're using constant reference
//     Vec vector { orig };
//     vector[0] = 1989;
//     return vector;
// }



int main() {
    const auto vec = createVec();
    const auto vec2 {createVec() }; // Does not call any constructor, it uses RVO

    auto vec3 = mutate(vec);
    auto vec4 = mutate(createVec()); //

    auto vec_ptr = std::make_shared<Vec>();
    auto vec_ptr2 = std::make_unique<Vec>();

    return 0;
}

#include <iostream>
#include <numeric>
#include <print>
#include <vector>

void print(const auto & item) {
    auto a = item;


    std::cout << item << std::endl;
}


template<typename T>
T sum(T a, T b, T c) {
    return a + b + c;
}

template<typename T>
class ContainerWrapper {
    T container;
    using iter = decltype(container.begin());
};

auto get_iter(auto& iterable) -> decltype(iterable.begin()) {
    return iterable.begin();
}

template<typename... T>
auto better_sum(T... args) {
    return (... + args);
}

template<typename T, typename Alloc = std::allocator<T>>
class Vec {
private:
    Alloc allocator;
    T* data = nullptr;
    size_t *begin_ptr = nullptr;
    size_t *end_ptr = nullptr;
    size_t reserved = 0;


    size_t size() {
        return static_cast<size_t>(end_ptr - begin_ptr);
    }

    void reallocate_if_needed() {
        auto sz = size();
        if (sz == reserved) {
            reserved *= 2;
            T* new_ptr = allocator.allocate(reserved);

            std::uninitialized_move(begin_ptr, end_ptr, new_ptr);

            allocator.deallocate(begin_ptr, sz);

            begin_ptr = new_ptr;
            end_ptr = begin_ptr + sz;
        }
    }

    void push_back_unsafe(T t) {
        new(end_ptr) T{std::move(t)};
        ++end_ptr;
    }

    template <class Arg>
    void push_back_converted(const Arg& arg);

public:
    Vec(Alloc allocator = Alloc{}) : allocator{std::move(allocator)} {}

    Vec(size_t reserved, Alloc allocator = Alloc{}) :
        reserved{reserved},
        allocator{std::move(allocator)} {

        begin_ptr = allocator.allocate(reserved);
        end_ptr = begin_ptr;
    }

    Vec(std::initializer_list<T> list, Alloc allocator = Alloc{}) :
        reserved {0},
        allocator{std::move(allocator)}
    {
        begin_ptr = allocator.allocate(reserved);
        end_ptr = begin_ptr;

        for (T& t : list) {
            // unsafe because we know we have enough memory
            push_back_unsafe(std::move(t));
        }
    }

    void push_back(T t) {
        reallocate_if_needed();
        push_back_unsafe(std::move(t));
    }

    void emplace_back(T item) {

    }
};

template<typename T, typename Alloc>
template<typename Arg>
void Vec<T, Alloc>::push_back_converted(const Arg& arg) {
    push_back(T{arg});
};

int main () {
    print(10);
    print("pseudosacrosanct perversion");
    print(better_sum(1,2,3,4,5,6));

}
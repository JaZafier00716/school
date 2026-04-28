#ifndef FP_TUPLE_HPP
#define FP_TUPLE_HPP
#include <cstddef>
#include <utility>

namespace fp {
    template<size_t idx, typename ... Args>
    struct tuple_impl;

    template<size_t idx, typename T, typename ... Ts>
    struct tuple_impl<idx, T, Ts...> : tuple_impl<idx+1, Ts...> {
        T elem;

        tuple_impl(T&& t, Ts&&... ts) : elem {std::forward(t)}, tuple_impl<idx+1, Ts...>{std::forward(ts)...} {}

        tuple_impl(const T& t, const Ts&... ts) : elem {std::forward(t)}, tuple_impl<idx+1, Ts...>{std::forward(ts)...} {}

    };

    template<size_t idx>
    struct tuple_impl<idx> {

    };



    // auto get(tuple_impl<>& tuple) {
    //
    // }

    template<typename ... Ts>
    class tuple : tuple_impl<0, Ts...> {
        template<size_t idx, typename...  T>
        friend auto& get(tuple<T...>& t);
    public:
        tuple(Ts&&... ts) : tuple_impl<0, Ts...>{std::forward(ts)...} {}

        tuple(const Ts... ts) : tuple_impl<0, Ts...>{std::forward(ts)...} {}

    };

    template<size_t idx, typename ... Ts>
    auto& get_impl(tuple_impl<idx, Ts...>& tuple) {
        return tuple.elem;
    }

    template<size_t idx, typename ... Ts>
    auto& get(tuple<Ts...>& t) {
        return get_impl<idx>(t);
    }

    inline auto sum() {
        return 0;
    }

    template<typename RT>
    inline RT sum_type() {
        return RT{};
    }

    template<typename RT, typename T, typename... Ts>
    inline RT sum_type(T fst, Ts... rest) { // sum for some type - type dependent
        return fst + sum_type<RT>(rest...);
    }


    template<typename T, typename... Ts>
    T sum(T fst, Ts... rest) {
        return sum_type(fst, rest...);
    }

    // Product type
    // struct S {
    //     int a;
    //     int b;
    //     int c;
    //     bool d;
    // };

    // Sum type
    // union U {
    //     int a;
    //     int b;
    //     int c;
    //     bool d;
    // };

    // template<typename T>
    // T sum() {
    //     return T{0};
    // }

}


#endif
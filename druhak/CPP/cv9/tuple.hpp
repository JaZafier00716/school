#ifndef FP_TUPLE_HPP
#define FP_TUPLE_HPP

namespace fp {
    template<typename ... Args>
    class tuple {


    };

    template<typename T, typename... Ts>
    T sum(T fst, Ts... rest) {
        return fst + sum(rest...);
    }

    template<typename T>
    T sum() {
        return T{0};
    }

}


#endif
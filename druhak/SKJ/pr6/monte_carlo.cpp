#include <pybind11/pybind11.h>
#include <random>


namespace py = pybind11;

double calculate_pi(int points) {
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_real_distribution<> dis(0.0, 1.0);

    int inside_circle = 0;

    for (int i = 0; i < points; ++i) {
        double x = dis(gen);
        double y = dis(gen);

        if (x * x + y * y <= 1.0) {
            ++inside_circle;
        }
    }

    return (static_cast<double>(inside_circle) / points) * 4.0;
}

PYBIND11_MODULE(fast_math, m) {
    m.doc() = "A module for fast mathematical calculations using Monte Carlo methods.";
    m.def("calculate_pi", &calculate_pi, "Calculate the value of pi using the Monte Carlo method.", py::arg("points") = 1000000);
}
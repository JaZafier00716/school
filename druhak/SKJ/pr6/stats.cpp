#include <pybind11/pybind11.h>
#include <string>
#include <iostream>

namespace py = pybind11;

struct Student {
    std::string name;
    int age;

    void print_info() const {
        std::cout << "Name: " << name << ", Age: " << age << std::endl;
    }
};

PYBIND11_MODULE(stats, m) {
    py::class_<Student>(m, "Student")
        .def(py::init<std::string, int>())
        .def("print_info", &Student::print_info)
        .def_readwrite("name", &Student::name)
        .def_readwrite("age", &Student::age);
}
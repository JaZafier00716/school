from setuptools import setup
from pybind11.setup_helpers import Pybind11Extension, build_ext

ext_modules = [
    Pybind11Extension(
	"fast_math",
	["monte_carlo.cpp"],
	extra_compile_args=['-O3'],
    ),
]


ext_modules = [
    Pybind11Extension(
        "stats",
        ["stats.cpp"],
        extra_compile_args=['-O3'],
    ),
]

setup(
    name="fast_math",
    version="0.1",
    author="JZ",
    description="A fast math library implemented in C++ and exposed to Python using pybind11.",
    ext_modules=ext_modules,
    cmdclass={"build_ext": build_ext},
    zip_safe=False,
    python_requires=">=3.6",
)

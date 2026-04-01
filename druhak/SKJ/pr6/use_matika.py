import ctypes
import os

lib_path = os.path.join(os.path.dirname(__file__), 'matika.so')
matika_lib = ctypes.CDLL(lib_path)

matika_lib.factorial.argtypes = [ctypes.c_uint]
matika_lib.factorial.restype = ctypes.c_ulonglong

cislo = 10

vysledek = matika_lib.factorial(cislo)
print(f"Faktoriál čísla {cislo} je {vysledek}.")
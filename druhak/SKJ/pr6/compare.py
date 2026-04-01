import time
import random
import fast_math

def calculate_pi_python(points):
    inside_circle = 0
    for _ in range(points):
        x = random.uniform(0, 1)
        y = random.uniform(0, 1)
        if x**2 + y**2 <= 1:
            inside_circle += 1
    return (inside_circle / points) * 4



points = 10_000_000
    
start_time = time.time()
pi_cpp = fast_math.calculate_pi(points)
end_time = time.time()

start_time_python = time.time()
pi_python = calculate_pi_python(points)
end_time_python = time.time()


print(f"Time taken to calculate Pi in C++: {end_time - start_time:.4f} seconds")
print(f"Pi calculated in C++: {pi_cpp}")
print(f"Time taken to calculate Pi in Python: {end_time_python - start_time_python:.4f} seconds")
print(f"Pi calculated in Python: {pi_python}")

print(f"C++ was {((end_time - start_time) / (end_time_python - start_time_python)):.2f} times faster than Python.")
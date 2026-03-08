import datetime as dt
import functools as ft

def logger(prefix):
    def read_decorator(f):
      @ft.wraps(f) # This is used to preserve the metadata of the original function, such as its name and docstring.
      
      def wrapper(*args, **kwargs):
        t_start = dt.datetime.now()
        f(*args, **kwargs)
        t_end = dt.datetime.now()
        dur = t_end - t_start
        print (f"{prefix} Time taken: {dur.microseconds} microseconds")
      return wrapper
    return read_decorator

@logger("My logger prefix:") # This is the decorator syntax, it is equivalent to test_fun = logger(test_fun)
def test_fun(greetings):
    print(f"This is a long operation... {greetings}")

test_fun("SKJ class")

print(print.__name__)
print(test_fun.__name__)

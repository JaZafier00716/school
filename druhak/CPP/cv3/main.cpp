#include <print>
#include <functional>


void apply(int x, int y, auto fn) {
  fn(x, y);
  fn(y, x);
}

int main () {
  int z = 5;


  auto fn = [&z, abc = 10](int x, int y) -> auto {
    std::println("{} + {} + {} = {}", z, x, y, z + x + y);
  };

  auto fn1 = [](int x, int y) -> auto {
    std::println("noop");
  };

  // auto function = fn;
  // function = fn1;

  std::function<void(int, int)> function = fn;

  function(1,2);


  z = 10;

  fn(2, 3);
  std::println("Hello World!");


  class Lambda {
      int& z;
    public:
      Lambda(int& z) : z(z) {}
      auto operator()(int x, int y) const -> void {
        std::println("{} + {} + {} = {}", z, x, y, z + x + y);    
      }
  };

  Lambda fn2 = Lambda{z};

  function = fn2;

  function(2, 3);

  z = 13;

  fn2(2, 3);


  apply(1, 2, fn);
  apply(2, 3, fn2);
  apply(2, 3, [](int x, int y) {
    std::println("{} * {} = {}", x, y, x * y);
  });

  auto mutating = [called = 0]() mutable {
    ++called;
    std::println("Lambda was called {} times", called);
  };

  for (int i = 0; i < 5; ++i) {
    mutating();
  }
}
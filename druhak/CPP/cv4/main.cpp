#include <print>
#include <functional>
#include <vector>

class Parent {
  public:
  virtual void mem_fn() {
    std::println("Parent::mem_fn called");
  }
};

class Child : public Parent {
  virtual void mem_fn() override {
    std::println("Child::mem_fn called");
  }
};

void call_mem_fn(Parent& p) {
  p.mem_fn();
}

namespace {
  class C {
    public:
      virtual void fn() {}

  };
}


void call_fn(C& c) {
  c.fn();
}


void cast_to_parent(Parent& p) {
}

void cast_to_child(Parent& p) {
  // Parent doesn't know how many children it has, so it can't call the child function.
  // To call the child function, we need to cast the parent reference to a child reference.
  // This is called downcasting, and it is not safe because the parent reference may not actually refer to a child object. 
  // If we try to downcast a parent reference that does not refer to a child object, we will get undefined behavior.
  Child& c_ref = dynamic_cast<Child&>(p); // This will throw std::bad_cast if p is not actually a Child object
  // Dynamic cast of pointer returns nullptr if the cast fails, while dynamic cast of reference throws std::bad_cast if the cast fails.
  Child* c_ptr = dynamic_cast<Child*>(&p); // This will return nullptr if p is not actually a Child object

}

  struct C1 { int a = 5; };
  struct C2 { int a = 10; };
  struct C3 {  };

  struct Inheritance : public virtual C1, public C2, public C3 { 
    // Virtual inheritance is used to solve the diamond problem, where multiple inheritance can lead to ambiguity when accessing members of the base classes. 
    // In this case, C1 is a virtual base class, so there will be only one instance of C1 in the inheritance hierarchy, and both C2 and C3 will share that instance. 
    // This allows us to access C1::a without ambiguity. 
    void print_a() {
      std::println("C1::a = {}, C2::a = {}", C1::a, C2::a); // Need to specify which 'a' we want to access, otherwise it will be ambiguous
    }
  };

  class ConsParent {
    public:
      ConsParent(int x, int y) {}
  };

  class Constructors : public ConsParent {
    std::vector<int> vec;
    std::string str;
    int a;

    public:
     Constructors() : ConsParent(1, 2), vec{}, str{"abcd"}, a{} {}
    };
    

struct S {
  S() {
    std::println("S::S()");
  };

  ~S() {
    std::println("S::~S()");
  };
};

int main () {

  {
    S s; // S::S() is called here
  } // S::~S() is called here, when s goes out of scope


  S* s1 = new S; // S::S() is called here
  delete s1; // S::~S() is called here, when s1 is deleted

  S* s_arr = new S[10]; // S::S() is called for each element in the array, when s_arr is created

  delete[] s_arr; // S::~S() is called for each element in the array, when s_arr is deleted


  std::vector<S> s_vec;
  s_vec.reserve(10); // S::S() is called for each element in the vector, when s_vec is created

  S* s_uninit = static_cast<S*>(operator new[](10 * sizeof(S))); // Allocates memory for 10 S objects, but does not call their constructors

  new(s_uninit+0) S{}; // Call the constructor for the first object
  new(s_uninit+1) S{}; // Call the constructor for the second object
  new(s_uninit+2) S{}; // Call the constructor for the third object


  s_uninit[0].~S(); // Call the destructor for the first object
  s_uninit[1].~S(); // Call the destructor for the second object
  s_uninit[2].~S(); // Call the destructor for the third object

  std::allocator<S> alloc;

  S* s_alloc = alloc.allocate(10); // Allocates memory for 10 S objects, but does not call their constructors
  alloc.deallocate(s_alloc, 10); // Deallocates the memory allocated for 10 S objects, but does not call their destructors


  operator delete[](s_uninit); // Deallocates the memory allocated for the 10 S objects, but does not call their destructors


  Parent p;
  call_mem_fn(p);

  Child c;
  call_mem_fn(c);

  // Do not use raw pointers, use smart pointers instead. 
  // Smart pointers automatically manage memory and ensure that resources are properly released when they are no longer needed.
  // Parent* ptr = new Child();

  // delete ptr;

  Parent c_copy = c; // Object slicing occurs here, c_copy is a Parent object, not a Child object
  call_mem_fn(c_copy); // Calls Parent::mem_fn, not Child::mem_fn

  cast_to_parent(c); // Upcasting - Casting to parent - Calls Parent::mem_fn, not Child::mem_fn

  Parent& c_ref = c; // Downcasting - Reference to parent - Calls Parent::mem_fn, not Child::mem_fn

  cast_to_child(c_ref);


  const auto vector = []() -> std::vector<int> { // Immediately invoked lambda
    std::vector<int> vector;

    for(size_t i =0; i < 1000; ++i) {
      vector.emplace_back(i*i);
    }

    return vector;
  }();

  // [&]() -> { // Automatically captures all local variables by reference -- not optimal
  // };

  Inheritance inh;
  inh.print_a();



  return 0;
}
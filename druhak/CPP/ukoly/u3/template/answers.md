# Memory management
[*] Can individual elements be shared among different JSON containers (array/object) with the mentioned
design? Can you take one specific element (e.g. an integer) allocated on the heap and place it inside
an array and also inside an object?
> No, because raw pointers are owned uniquely by the container, so it cannot be owned by multiple containers.
    
[*] Could references (or non-owned pointers) be used for storing the elements in JSON containers?
How would it affect the usability of arrays and objects? Try it and see how easy or hard it is :)
> Technically yes, however it would make it more prone to errors, since the user would have to ensure 
> the presence of the referenced element during the entire lifetime of the container.

# Indexing
[*] Think about this interface. What are its benefits or disadvantages? Is it better to put the
indexers into the root `Value` element? Or should it only be supported by types that actually
implement indexing (arrays/objects)? Think about the trade-offs (compile-time safety vs ergonomics).
> Putting the indexers into the root `Value` element allows accessing elements in polymorphic way,
> however it also allows indexing on unindexable types, which will throw an exception at runtime

[*] Think about the return type. What should it be? Is `std::optional` required here?
> since `operator []` returns a pointer, it could return `nullptr, so `std::optional` is not necessary.

# Copying
[*] How can you implement copying for a polymorphic object hierarchy? What should be the return type
of the `clone` method? If you are interested, look for "C++ covariance".
> The `clone` method should be virtual and return a pointer to the parent class `Value*`
> Child classes can override it and return a pointer to their own type...

# Visitor
[*] Think about the constness of the `accept` method and of the methods in the visitor. What
parameter type should they take? Should it be const or not? Consider creating two classes
to represent visitors, `Visitor` and `MutatingVisitor` (or similar names).
> The `accept` method should be const, since it does not modify the element itself, but only allows the read-only access to the visitor.
> The `Visitor` class should have const methods, since it is meant for read-only traversal, while the `MutatingVisitor` class should have non-const methods, since it is meant for in-place edits.

[*] How would the implementation look like if you have used e.g. algebraic data types instead?
Would you need the Visitor design pattern in that case?
> With algebraic data types, Visitor design pattern is less necessary, since we can use `std::visit` to perform operation on the variant types.
> However, if we want to add new operations without modifying the existing code, we can still use Visitor pattern by defining a visitor class and implementing the visit methods for each type in the variant.
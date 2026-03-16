#pragma once

#include <cstddef>
#include <ostream>
#include <string>
#include <unordered_map>
#include <vector>

class Integer;
class Null;
class Array;
class Object;

class Visitor {
public:
  virtual ~Visitor() = default;
  virtual void visit(const Integer &integer) = 0;
  virtual void visit(const Null &null_value) = 0;
  virtual void visit(const Array &array) = 0;
  virtual void visit(const Object &object) = 0;
};

class MutatingVisitor {
public:
  virtual ~MutatingVisitor() = default;
  virtual void visit(Integer &integer) = 0;
  virtual void visit(Null &null_value) = 0;
  virtual void visit(Array &array) = 0;
  virtual void visit(Object &object) = 0;
};

class Value {
public:
  Value() = default;
  virtual ~Value() = default;

  Value(const Value &) = delete;
  Value(Value &&) = delete;
  Value &operator=(const Value &) = delete;
  Value &operator=(Value &&) = delete;

  virtual Value *operator[](size_t index);
  virtual const Value *operator[](size_t index) const;
  virtual Value *operator[](const std::string &key);
  virtual const Value *operator[](const std::string &key) const;

  [[nodiscard]] virtual Value *clone() const = 0;

  virtual void accept(Visitor &visitor) const = 0;
  virtual void accept(MutatingVisitor &visitor) = 0;
};

class Integer : public Value {
public:
  explicit Integer(int value);

  [[nodiscard]] int get_value() const;

  Value *operator[](size_t index) override;
  const Value *operator[](size_t index) const override;
  Value *operator[](const std::string &key) override;
  const Value *operator[](const std::string &key) const override;

  [[nodiscard]] Integer *clone() const override;

  void accept(Visitor &visitor) const override;
  void accept(MutatingVisitor &visitor) override;

private:
  int value;
};

class Null : public Value {
public:
  Value *operator[](size_t index) override;
  const Value *operator[](size_t index) const override;
  Value *operator[](const std::string &key) override;
  const Value *operator[](const std::string &key) const override;

  [[nodiscard]] Null *clone() const override;

  void accept(Visitor &visitor) const override;
  void accept(MutatingVisitor &visitor) override;
};

class Array : public Value {
public:
  Array();
  explicit Array(const std::vector<Value *> &values);
  ~Array() override;

  Value *operator[](size_t index) override;
  const Value *operator[](size_t index) const override;
  Value *operator[](const std::string &key) override;
  const Value *operator[](const std::string &key) const override;

  [[nodiscard]] Array *clone() const override;

  [[nodiscard]] size_t size() const;
  void append(Value *value);
  void remove(size_t index);

  void accept(Visitor &visitor) const override;
  void accept(MutatingVisitor &visitor) override;

private:
  std::vector<Value *> values;
};

class Object : public Value {
public:
  Object();
  explicit Object(const std::unordered_map<std::string, Value *> &values);
  ~Object() override;

  Value *operator[](size_t index) override;
  const Value *operator[](size_t index) const override;
  Value *operator[](const std::string &key) override;
  const Value *operator[](const std::string &key) const override;

  [[nodiscard]] Object *clone() const override;

  [[nodiscard]] size_t size() const;
  [[nodiscard]] std::vector<std::string> keys() const;
  void insert(const std::string &key, Value *value);
  void remove(const std::string &key);

  void accept(Visitor &visitor) const override;
  void accept(MutatingVisitor &visitor) override;

private:
  std::unordered_map<std::string, Value *> values;
};

class PrintVisitor : public Visitor {
public:
  explicit PrintVisitor(std::ostream &stream);

  void visit(const Integer &integer) override;
  void visit(const Null &null_value) override;
  void visit(const Array &array) override;
  void visit(const Object &object) override;

private:
  std::ostream &stream;
};

class RemoveNullVisitor : public MutatingVisitor {
public:
  void visit(Integer &integer) override;
  void visit(Null &null_value) override;
  void visit(Array &array) override;
  void visit(Object &object) override;
};

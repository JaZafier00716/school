#include "tasks.h"

#include <algorithm>
#include <stdexcept>

Value *Value::operator[](size_t /*index*/) {
  throw std::runtime_error("Cannot index this value with a number");
}

const Value *Value::operator[](size_t /*index*/) const {
  throw std::runtime_error("Cannot index this value with a number");
}

Value *Value::operator[](const std::string & /*key*/) {
  throw std::runtime_error("Cannot index this value with a string");
}

const Value *Value::operator[](const std::string & /*key*/) const {
  throw std::runtime_error("Cannot index this value with a string");
}

Integer::Integer(const int value) : value(value) {}

int Integer::get_value() const {
  return value;
}

Value *Integer::operator[](size_t /*index*/) {
  throw std::runtime_error("Cannot index an integer");
}

const Value *Integer::operator[](size_t /*index*/) const {
  throw std::runtime_error("Cannot index an integer");
}

Value *Integer::operator[](const std::string & /*key*/) {
  throw std::runtime_error("Cannot index an integer");
}

const Value *Integer::operator[](const std::string & /*key*/) const {
  throw std::runtime_error("Cannot index an integer");
}

Integer *Integer::clone() const {
  return new Integer(value);
}

void Integer::accept(Visitor &visitor) const {
  visitor.visit(*this);
}

void Integer::accept(MutatingVisitor &visitor) {
  visitor.visit(*this);
}

Value *Null::operator[](size_t /*index*/) {
  throw std::runtime_error("Cannot index null");
}

const Value *Null::operator[](size_t /*index*/) const {
  throw std::runtime_error("Cannot index null");
}

Value *Null::operator[](const std::string & /*key*/) {
  throw std::runtime_error("Cannot index null");
}

const Value *Null::operator[](const std::string & /*key*/) const {
  throw std::runtime_error("Cannot index null");
}

Null *Null::clone() const {
  return new Null();
}

void Null::accept(Visitor &visitor) const {
  visitor.visit(*this);
}

void Null::accept(MutatingVisitor &visitor) {
  visitor.visit(*this);
}

Array::Array() = default;

Array::Array(const std::vector<Value *> &values) : values(values) {}

Array::~Array() {
  for (Value *value : values) {
    delete value;
  }
}

Value *Array::operator[](size_t index) {
  if (index >= values.size()) {
    return nullptr;
  }
  return values[index];
}

const Value *Array::operator[](size_t index) const {
  if (index >= values.size()) {
    return nullptr;
  }
  return values[index];
}

Value *Array::operator[](const std::string & /*key*/) {
  throw std::runtime_error("Cannot index an array with a string");
}

const Value *Array::operator[](const std::string & /*key*/) const {
  throw std::runtime_error("Cannot index an array with a string");
}

Array *Array::clone() const {
  std::vector<Value *> cloned_values;
  cloned_values.reserve(values.size());

  try {
    for (const Value *value : values) {
      cloned_values.push_back(value->clone());
    }
  } catch (...) {
    for (Value *value : cloned_values) {
      delete value;
    }
    throw;
  }

  return new Array(cloned_values);
}

size_t Array::size() const {
  return values.size();
}

void Array::append(Value *value) {
  values.push_back(value);
}

void Array::remove(const size_t index) {
  if (index >= values.size()) {
    return;
  }

  delete values[index];
  values.erase(values.begin() + static_cast<std::ptrdiff_t>(index));
}

void Array::accept(Visitor &visitor) const {
  visitor.visit(*this);
}

void Array::accept(MutatingVisitor &visitor) {
  visitor.visit(*this);
}

Object::Object() = default;

Object::Object(const std::unordered_map<std::string, Value *> &values) : values(values) {}

Object::~Object() {
  for (auto &[key, value] : values) {
    (void)key;
    delete value;
  }
}

Value *Object::operator[](size_t /*index*/) {
  throw std::runtime_error("Cannot index an object with a number");
}

const Value *Object::operator[](size_t /*index*/) const {
  throw std::runtime_error("Cannot index an object with a number");
}

Value *Object::operator[](const std::string &key) {
  const auto it = values.find(key);
  if (it == values.end()) {
    return nullptr;
  }
  return it->second;
}

const Value *Object::operator[](const std::string &key) const {
  const auto it = values.find(key);
  if (it == values.end()) {
    return nullptr;
  }
  return it->second;
}

Object *Object::clone() const {
  std::unordered_map<std::string, Value *> cloned_values;

  try {
    for (const auto &[key, value] : values) {
      cloned_values[key] = value->clone();
    }
  } catch (...) {
    for (auto &[key, value] : cloned_values) {
      (void)key;
      delete value;
    }
    throw;
  }

  return new Object(cloned_values);
}

size_t Object::size() const {
  return values.size();
}

std::vector<std::string> Object::keys() const {
  std::vector<std::string> key_list;
  key_list.reserve(values.size());

  for (const auto &[key, value] : values) {
    (void)value;
    key_list.push_back(key);
  }

  std::sort(key_list.begin(), key_list.end());
  return key_list;
}

void Object::insert(const std::string &key, Value *value) {
  const auto it = values.find(key);
  if (it != values.end()) {
    delete it->second;
    it->second = value;
    return;
  }

  values.insert({key, value});
}

void Object::remove(const std::string &key) {
  const auto it = values.find(key);
  if (it == values.end()) {
    return;
  }

  delete it->second;
  values.erase(it);
}

void Object::accept(Visitor &visitor) const {
  visitor.visit(*this);
}

void Object::accept(MutatingVisitor &visitor) {
  visitor.visit(*this);
}

PrintVisitor::PrintVisitor(std::ostream &stream) : stream(stream) {}

void PrintVisitor::visit(const Integer &integer) {
  stream << integer.get_value();
}

void PrintVisitor::visit(const Null & /*null_value*/) {
  stream << "null";
}

void PrintVisitor::visit(const Array &array) {
  stream << "[";
  for (size_t i = 0; i < array.size(); ++i) {
    if (i > 0) {
      stream << ", ";
    }

    const Value *value = array[i];
    if (value != nullptr) {
      value->accept(*this);
    }
  }
  stream << "]";
}

void PrintVisitor::visit(const Object &object) {
  stream << "{";

  const std::vector<std::string> key_list = object.keys();
  for (size_t i = 0; i < key_list.size(); ++i) {
    if (i > 0) {
      stream << ", ";
    }

    const std::string &key = key_list[i];
    stream << key << ": ";

    const Value *value = object[key];
    if (value != nullptr) {
      value->accept(*this);
    }
  }

  stream << "}";
}

void RemoveNullVisitor::visit(Integer & /*integer*/) {}

void RemoveNullVisitor::visit(Null & /*null_value*/) {}

void RemoveNullVisitor::visit(Array &array) {
  size_t index = 0;
  while (index < array.size()) {
    Value *value = array[index];
    if (dynamic_cast<Null *>(value) != nullptr) {
      array.remove(index);
      continue;
    }

    if (value != nullptr) {
      value->accept(*this);
    }
    ++index;
  }
}

void RemoveNullVisitor::visit(Object &object) {
  const std::vector<std::string> key_list = object.keys();

  for (const std::string &key : key_list) {
    Value *value = object[key];
    if (dynamic_cast<Null *>(value) != nullptr) {
      object.remove(key);
      continue;
    }

    if (value != nullptr) {
      value->accept(*this);
    }
  }
}

#pragma once
#include <iostream>
#include <algorithm>
#include <optional>
#include <vector>

using std::cin, std::cout, std::endl, std::string, std::optional, std::vector;


struct Table {
  vector<optional<string>> entries;
  size_t number_of_keys;

  Table(const size_t size) {
    number_of_keys = 0;
    entries.assign(size, std::nullopt);
  }

  void printTable() const {
    for (size_t i = 0; i < entries.size(); i++) {
      const auto &entry = entries[i];
      if (entry.has_value()) {
        cout << i << ": " << entry.value() << " ";
      } else {
      }
    }
    cout << endl;
  }

  size_t hashIndex(const string &key) const {
    return std::hash<string>{}(key) % entries.size();
  }

  int lookup(const string &key) const {
    const size_t index = hashIndex(key);
    if (entries[index].has_value() && entries[index].value() == key) {
      return static_cast<int>(index);
    }
    return -1;
  }

  bool remove(const string &key) {
    const int index = lookup(key);
    if (index != -1) {
      entries[index].reset();
      number_of_keys--;
      return true;
    }
    return false;
  }

  vector<optional<string>> getEntries() const {
    std::vector<optional<string>> keys;
    keys.reserve(number_of_keys);
    for (const auto &entry : entries) {
      if (entry.has_value()) {
        keys.push_back(entry.value());
      }
    }
    return keys;
  }
};

class CuckooHashTable {
private:
  Table t1;
  Table t2;
  size_t max_insert_iterations;
  double resize_factor;
  double max_occupancy;

  bool needsResize() const {
    return static_cast<double>(t1.number_of_keys + t2.number_of_keys)
    / static_cast<double>(t1.entries.size() + t2.entries.size())
    > max_occupancy;
  }

  void resize() {
    std::vector<std::optional<string>> all_keys = t1.getEntries();
    auto keys2 = t2.getEntries();
    all_keys.insert(all_keys.end(), keys2.begin(), keys2.end());

    const auto new_size1 = static_cast<size_t>(static_cast<double>(t1.entries.size()) * resize_factor);
    const auto new_size2 = static_cast<size_t>(static_cast<double>(t2.entries.size()) * resize_factor);
    t1 = Table(new_size1);
    t1.number_of_keys = 0;
    t2 = Table(new_size2);
    t2.number_of_keys = 0;

    for (const auto &key : all_keys) {
      insert(key.value());
    }
  }
public:
  CuckooHashTable() :
    t1(10),
    t2(11),
    max_insert_iterations(5),
    resize_factor(1.5),
    max_occupancy(0.5)
  {}
  CuckooHashTable(const size_t t1_size, const size_t t2_size, const double max_occupancy, const double resize_factor) :
    t1(t1_size),
    t2(t2_size),
    max_insert_iterations(5),
    resize_factor(resize_factor),
    max_occupancy(max_occupancy)
  {}

  bool lookup(const string &key) const {
    return  t1.lookup(key) != -1 || t2.lookup(key) != -1;
  }

  bool remove(const string &key) {
    return t1.remove(key) || t2.remove(key);
  }

  bool insert(const string &key) {
    if (lookup(key)) {
      return false;
    }
    if (needsResize()) {
      resize();
    }
    string current_key = key;
    for (size_t i = 0; i < max_insert_iterations; i++) {
      const size_t index = t1.hashIndex(current_key);
      if (!t1.entries.at(index).has_value()) {
        // index is empty
        t1.entries.at(index) = current_key;
        t1.number_of_keys++;
        return true;
      }
      std::swap(current_key, t1.entries.at(index).value()); // swap current_key with the key at index

      const size_t index2 = t2.hashIndex(current_key);
      if (!t2.entries.at(index2).has_value()) {
        // index2 is empty
        t2.entries.at(index2) = current_key;
        t2.number_of_keys++;
        return true;
      }
      std::swap(current_key, t2.entries.at(index2).value()); // swap current_key with the key at index2
    }
    resize();
    insert(current_key);
    return true;
  }

  void printTables() const {
    cout << "Table1:" << endl;
    t1.printTable();
    cout << "Table2:" << endl;
    t2.printTable();
  }
};
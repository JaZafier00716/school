#include <iostream>
#include <vector>
#include <map>
#include <set> // binary tree
#include <stack>
#include <queue>
#include <algorithm>
#include <unordered_set> // hash table
#include <unordered_map>
#include <string>
#include <functional> // hash functions
#include <optional>


using std::cout, std::cin, std::endl, std::vector, std::string;


class NaiveHashTable {
  private:
    vector<vector<string>> table;
    double max_occupancy;
    double resize_factor;
    size_t number_of_keys;

    bool needsResize() const {
      return static_cast<double>(number_of_keys + 1) / table.size() > max_occupancy;
    }

    void resize() {
      size_t new_size = static_cast<size_t>(table.size() * resize_factor);
      auto old_table = std::move(table); // copie tabulky
      
      table.assign(new_size, {}); // vytvoreni nove tabulky
      number_of_keys = 0;

      for(const auto &bucket : old_table) {
        for (const auto &key : bucket) {
          size_t index = hashIndex(key);
          table[index].push_back(key);
        }
      }

    }

    size_t hashIndex(const string &key) const {
      // std::hash<string> hasher;
      // return hasher(key) % table.size();
      return std::hash<string>{}(key) % table.size(); // {} - pouziti default constructoru
    }

  public:

  bool lookup(const string &key) const {
    size_t index = hashIndex(key);
    auto item_pointer = std::find(table[index].begin(), table[index].end(), key);
    return item_pointer != table[index].end();
  }


  bool remove(const string &key) {
    size_t index = hashIndex(key);
    auto item_pointer = std::find(table[index].begin(), table[index].end(), key);
    if (item_pointer != table[index].end()) {
      std::swap(*item_pointer, table[index].back()); // swap with the last element to enable O(1) removal

      table[index].pop_back();
      --number_of_keys;
      return true;
    }
    return false;
  }


  bool insert(const string &key) {
    size_t index = hashIndex(key);
    auto item_pointer = std::find(table[index].begin(), table[index].end(), key);
    if (item_pointer == table[index].end()) {
      if (needsResize()) {
        resize();
      }
      table[index].push_back(key);
      ++number_of_keys;
      return true;
    }
    return false;
  }
};

enum State {
  EMPTY,
  OCCUPIED,
  DELETED
};

struct Entry {
  string key;
  State state;
  Entry() : key(""), state(EMPTY) {}
};


class LinearProbingTable {
  private:
    vector<Entry> table;
    double max_occupancy;
    double resize_factor;
    size_t number_of_keys;

    bool needsResize() const {
      return static_cast<double>(number_of_keys + 1) / table.size() > max_occupancy;
    }

    void resize() {
      size_t new_size = static_cast<size_t>(table.size() * resize_factor);
      auto old_table = std::move(table); // copie tabulky
      
      table.assign(new_size, Entry()); // vytvoreni nove tabulky
      number_of_keys = 0;

      for(const auto &entry : old_table) {
        if(entry.state == OCCUPIED) {
          insert(entry.key);
        }
      }
    }

    size_t hashIndex(const string &key) const {

    }

  public:

  bool lookup(const string &key) const {
    size_t initial_index = hashIndex(key);
    // probing
    for(size_t i = 0; i < table.size(); i++) {
      size_t current_index = (initial_index + i) % table.size();
      const Entry& entry = table[current_index];

      if(entry.state == OCCUPIED && entry.key == key) {
        return true;
      }
      if(entry.state == EMPTY) {
        return false; // key not found
      }
    }
    return false;
  }


  bool remove(const string &key) {
    size_t initial_index = hashIndex(key);
    // probing
    for(size_t i = 0; i < table.size(); i++) {
      size_t current_index = (initial_index + i) % table.size();
      Entry &entry = table[current_index];

      if(entry.state == OCCUPIED && entry.key == key) {
        entry.state = DELETED;
        --number_of_keys;
        entry.key = ""; // redundant, but cleans up the key
        return true;
      }
      if(entry.state == EMPTY) {
        return false; // key not found
      }
    }
    return false;
  }


  bool insert(const string &key) {
    if(needsResize()) {
      resize();
    }
    size_t initial_index = hashIndex(key);
    std::optional<size_t> insert_index = std::nullopt;
    // probing
    for(size_t i = 0; i < table.size(); i++) {
      size_t current_index = (initial_index + i) % table.size();
      Entry &entry = table[current_index];
    
      if(entry.state == OCCUPIED && key == entry.key) {
        return false; // key already exists
      }
      if(entry.state == DELETED) {
        if(!insert_index.has_value()) {
          insert_index = current_index; // remember first deleted slot
        }
      }
      if(entry.state == EMPTY) {
        if(!insert_index.has_value()) {
          insert_index = current_index;
        }
        // insert the key
        table[insert_index.value()].state = OCCUPIED;
        table[insert_index.value()].key = key;
        ++number_of_keys;
        return true;
      }
    }
    if(insert_index.has_value()) {
      table[insert_index.value()].state = OCCUPIED;
      table[insert_index.value()].key = key;
      ++number_of_keys;
      return true;
    }
    return false;
  }
};

int main () {





  return 0;
}

#pragma once

#include <string>
#include <cstdint>
#include <functional>
#include <iostream>
#include <utility>
#include <cstring>
#include <vector>
#include <optional>

using CodePoint = uint32_t;

class UTF8String {
private:
  uint8_t* data;
  size_t size; // Number of bytes in the string
  size_t capacity; // Allocated capacity in bytes
  size_t length; // Number of Unicode code points in the string
public:
  UTF8String() : data(nullptr), size(0), capacity(0), length(0) {}
  UTF8String(const char* str) {
    size = std::strlen(str);
    capacity = (size*2) +1; // Allocate more than needed to reduce future reallocations
    data = new uint8_t[capacity];
    std::memcpy(data, str, size);
  }
  UTF8String(const std::vector<CodePoint>& codePoints) {
    size = 0;
    length = 0;
    for(const auto& cp : codePoints) {
      length++;
      if (cp < 0x80) {
        // 1-byte character
        size += 1;
      } else if (cp < 0x800) {
        // 2-byte character (110xxxxxx 10xxxxxx)
        size += 2;
      } else if (cp < 0x10000) {
        // 3-byte character (1110xxxx 10xxxxxx 10xxxxxx)
        size += 3;
      } else {
        // 4-byte character (11110xxx 10xxxxxx 10xxxxxx 10xxxxxx)
        size += 4;
      }
    }
    capacity = (size*2) +1; // Allocate more than needed to reduce future reallocations
    data = new uint8_t[capacity];
    size_t index = 0;
    for(const auto& cp : codePoints) {
      if (cp < 0x80) {
        // 1-byte character
        data[index++] = static_cast<uint8_t>(cp);
      } else if (cp < 0x800) {
        // 2-byte character (110xxxxxx 10xxxxxx)
        data[index++] = static_cast<uint8_t>(0xC0 | (cp >> 6));
        data[index++] = static_cast<uint8_t>(0x80 | (cp & 0x3F));
      } else if (cp < 0x10000) {
        // 3-byte character (1110xxxx 10xxxxxx 10xxxxxx)
        data[index++] = static_cast<uint8_t>(0xE0 | (cp >> 12));
        data[index++] = static_cast<uint8_t>(0x80 | ((cp >> 6) & 0x3F));
        data[index++] = static_cast<uint8_t>(0x80 | (cp & 0x3F));
      } else {
        // 4-byte character (11110xxx 10xxxxxx 10xxxxxx 10xxxxxx)
        data[index++] = static_cast<uint8_t>(0xF0 | (cp >> 18));
        data[index++] = static_cast<uint8_t>(0x80 | ((cp >> 12) & 0x3F));
        data[index++] = static_cast<uint8_t>(0x80 | ((cp >> 6) & 0x3F));
        data[index++] = static_cast<uint8_t>(0x80 | (cp & 0x3F));
      }
    }
  }
  UTF8String(const UTF8String& other) {
    size = other.size;
    capacity = other.capacity;
    length = other.length;
    data = new uint8_t[capacity];
    std::memcpy(data, other.data, size);
  }
  UTF8String& operator=(const UTF8String& other) {
    if (this != &other) {
      delete[] data;
      size = other.size;
      capacity = other.capacity;
      length = other.length;
      data = new uint8_t[capacity];
      std::memcpy(data, other.data, size);
    }
    return *this;
  }

  std::optional<uint8_t> operator[](size_t index) const {
    if (index >= size) {
      return std::nullopt;
    }
    return data[index];
  }

  std::optional<CodePoint> nth_code_point(size_t index) const {
    size_t byteIndex = 0;
    size_t codePointIndex = 0;
    while (byteIndex < size) {
      if (codePointIndex == index) {
        uint8_t firstByte = data[byteIndex];
        if (firstByte < 0x80) {
          return firstByte; // 1-byte character
        } else if (firstByte < 0xE0) {
          return ((firstByte & 0x1F) << 6) | (data[byteIndex + 1] & 0x3F); // 2-byte character
        } else if (firstByte < 0xF0) {
          return ((firstByte & 0x0F) << 12) | ((data[byteIndex + 1] & 0x3F) << 6) | (data[byteIndex + 2] & 0x3F); // 3-byte character
        } else {
          return ((firstByte & 0x07) << 18) | ((data[byteIndex + 1] & 0x3F) << 12) | ((data[byteIndex + 2] & 0x3F) << 6) | (data[byteIndex + 3] & 0x3F); // 4-byte character
        }
      }
      uint8_t firstByte = data[byteIndex];
      if (firstByte < 0x80) {
        byteIndex += 1; // Move to next character
      } else if (firstByte < 0xE0) {
        byteIndex += 2; // Move to next character
      } else if (firstByte < 0xF0) {
        byteIndex += 3; // Move to next character
      } else {
        byteIndex += 4; // Move to next character
      }
      codePointIndex++;
    }
    return std::nullopt; // Index out of bounds
  }

  void append(char c) {
    if (size + 1 >= capacity) {
      // Need to reallocate
      capacity = (capacity * 2) + 1;
      uint8_t* newData = new uint8_t[capacity];
      std::memcpy(newData, data, size);
      delete[] data;
      data = newData;
    }
    data[size] = static_cast<uint8_t>(c);
    size++;
    length++; // Assuming we're appending a single-byte character
  }

  void append(CodePoint cp) {
    if (cp < 0x80) {
      append(static_cast<char>(cp));
    } else if (cp < 0x800) {
      append(static_cast<char>(0xC0 | (cp >> 6)));
      append(static_cast<char>(0x80 | (cp & 0x3F)));
    } else if (cp < 0x10000) {
      append(static_cast<char>(0xE0 | (cp >> 12)));
      append(static_cast<char>(0x80 | ((cp >> 6) & 0x3F)));
      append(static_cast<char>(0x80 | (cp & 0x3F)));
    } else {
      append(static_cast<char>(0xF0 | (cp >> 18)));
      append(static_cast<char>(0x80 | ((cp >> 12) & 0x3F)));
      append(static_cast<char>(0x80 | ((cp >> 6) & 0x3F)));
      append(static_cast<char>(0x80 | (cp & 0x3F)));
    }
    length++; // Increment code point count
  }

  size_t get_byte_count() const {
    return size;
  }
  size_t get_point_count() const {
    return length;
  }
  
  ~UTF8String() {
    delete[] data;
  }

  void print_bytes_hex() const {
    for (size_t i = 0; i < size; ++i) {
      std::cout << std::hex << "0x" << static_cast<int>(data[i]) << " ";
    }
    std::cout << std::dec << std::endl; // Reset to decimal
  }

};

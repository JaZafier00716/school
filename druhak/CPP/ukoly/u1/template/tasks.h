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
    size_t length{}; // Number of Unicode code points in the string

    void resize();

    bool needs_resize(const size_t additionalBytes) const {
        return (size + additionalBytes) >= capacity;
    }

public:
    UTF8String() : data(nullptr), size(0), capacity(0), length(0) {}
    explicit UTF8String(const char* str);
    explicit UTF8String(const std::string& str);
    explicit UTF8String(const std::vector<CodePoint>& codePoints);
    UTF8String(const UTF8String& other);

    ~UTF8String() {
        delete[] data;
    }

    UTF8String& operator=(const UTF8String& other);
    UTF8String operator+(const UTF8String& other) const;
    UTF8String& operator+=(const UTF8String& other);
    bool operator==(const UTF8String& other) const;
    bool operator!=(const UTF8String& other) const;
    std::optional<uint8_t> operator[](size_t index) const;

    std::optional<CodePoint> nth_code_point(size_t index) const;
    void append(char c);
    void append(CodePoint cp);

    // Convert UTF8String to std::string
    std::string to_string() const {
        return std::string(reinterpret_cast<const char*>(data), size);
    }

    // Conversion operator for implicit conversion to std::string
    operator std::string() const {
        return to_string();
    }

    size_t get_byte_count() const {
        return size;
    }

    size_t get_point_count() const {
        return length;
    }

    void print_bytes_hex() const {
        for (size_t i = 0; i < size; ++i) {
            std::cout << std::hex << "0x" << static_cast<int>(data[i]) << " ";
        }
        std::cout << std::dec << std::endl; // Reset to decimal
    }
};

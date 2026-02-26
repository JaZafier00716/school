#include "tasks.h"


/*
 * Private methods:
 */
void UTF8String::resize() {
    capacity = (capacity * 2) + 1; // Double the capacity and add one to avoid zero capacity
    uint8_t* newData = new uint8_t[capacity];
    std::memcpy(newData, data, size);
    delete[] data;
    data = newData;
}

/*
 *  Constructors:
 */

UTF8String::UTF8String(const char* str) {
    size = std::strlen(str);
    length = std::strlen(str);
    capacity = (size * 2) + 1; // Allocate more than needed to reduce future reallocations
    data = new uint8_t[capacity];
    std::memcpy(data, str, size);
}

UTF8String::UTF8String(const std::string& str) {
    size = str.size();
    length = str.size();
    capacity = (size * 2) + 1; // Allocate more than needed to reduce future reallocations
    data = new uint8_t[capacity];
    std::memcpy(data, str.data(), size);
}

UTF8String::UTF8String(const std::vector<CodePoint>& codePoints) {
    size = 0;
    for (const auto& cp : codePoints) {
        if (cp < 0x80) {
            // 1-byte character
            size += 1;
        }
        else if (cp < 0x800) {
            // 2-byte character (110xxxxxx 10xxxxxx)
            size += 2;
        }
        else if (cp < 0x10000) {
            // 3-byte character (1110xxxx 10xxxxxx 10xxxxxx)
            size += 3;
        }
        else {
            // 4-byte character (11110xxx 10xxxxxx 10xxxxxx 10xxxxxx)
            size += 4;
        }
    }
    capacity = (size * 2) + 1; // Allocate more than needed to reduce future reallocations
    size = 0; // Will be updated as we append code points
    data = new uint8_t[capacity];
    for (const auto& cp : codePoints) {
        append(cp);
    }
    length = codePoints.size();
}

UTF8String::UTF8String(const UTF8String& other) {
    size = other.size;
    capacity = other.capacity;
    length = other.length;
    data = new uint8_t[capacity];
    std::memcpy(data, other.data, size);
}

/*
 *  Operators:
 */
UTF8String& UTF8String::operator=(const UTF8String& other) {
    if (this != &other) {
        delete[] data;
        size = other.size;
        length = other.length;
        capacity = other.capacity;
        data = new uint8_t[capacity];
        std::memcpy(data, other.data, size);
    }
    return *this;
}

UTF8String UTF8String::operator+(const UTF8String& other) const {
    UTF8String result;
    result.size = size + other.size;
    result.length = length + other.length;
    result.capacity = (result.size * 2) + 1; // Allocate more than needed to reduce future reallocations
    result.data = new uint8_t[result.capacity];
    std::memcpy(result.data, data, size);
    std::memcpy(result.data + size, other.data, other.size);
    return result;
}

UTF8String& UTF8String::operator+=(const UTF8String& other) {
    if (needs_resize(other.size)) {
        resize();
    }
    std::memcpy(data + size, other.data, other.size);
    size += other.size;
    length += other.length;
    return *this;
}

bool UTF8String::operator==(const UTF8String& other) const {
    if (size != other.size) {
        return false;
    }
    return std::memcmp(data, other.data, size) == 0;
}

bool UTF8String::operator!=(const UTF8String& other) const {
    return !(*this == other);
}

std::optional<uint8_t> UTF8String::operator[](const size_t index) const {
    if (index >= size) {
        return std::nullopt;
    }
    return data[index];
}

/*
 *  Methods:
 */
std::optional<CodePoint> UTF8String::nth_code_point(const size_t index) const {
    size_t byteIndex = 0;
    size_t codePointIndex = 0;
    while (byteIndex < size) {
        if (codePointIndex == index) {
            uint8_t firstByte = data[byteIndex];
            if (firstByte < 0x80) {
                return firstByte; // 1-byte character
            }
            else if (firstByte < 0xE0) {
                return ((firstByte & 0x1F) << 6) | (data[byteIndex + 1] & 0x3F); // 2-byte character
            }
            else if (firstByte < 0xF0) {
                return ((firstByte & 0x0F) << 12) | ((data[byteIndex + 1] & 0x3F) << 6) | (data[byteIndex + 2] & 0x3F);
                // 3-byte character
            }
            else {
                return ((firstByte & 0x07) << 18) | ((data[byteIndex + 1] & 0x3F) << 12) | ((data[byteIndex + 2] & 0x3F)
                    << 6) | (data[byteIndex + 3] & 0x3F); // 4-byte character
            }
        }
        uint8_t firstByte = data[byteIndex];
        if (firstByte < 0x80) {
            byteIndex += 1; // Move to next character
        }
        else if (firstByte < 0xE0) {
            byteIndex += 2; // Move to next character
        }
        else if (firstByte < 0xF0) {
            byteIndex += 3; // Move to next character
        }
        else {
            byteIndex += 4; // Move to next character
        }
        codePointIndex++;
    }
    return std::nullopt; // Index out of bounds
}

void UTF8String::append(const char c) {
    if (needs_resize(1)) {
        resize();
    }
    data[size] = static_cast<uint8_t>(c);
    size++;
    length++; // Assuming we're appending a single-byte character
}

void UTF8String::append(const CodePoint cp) {
    if (cp < 0x80) {
        append(static_cast<char>(cp));
    }
    else if (cp < 0x800) {
        append(static_cast<char>(0xC0 | (cp >> 6)));
        append(static_cast<char>(0x80 | (cp & 0x3F)));
        length--; // Adjust length since append(char) increments it
    }
    else if (cp < 0x10000) {
        append(static_cast<char>(0xE0 | (cp >> 12)));
        append(static_cast<char>(0x80 | ((cp >> 6) & 0x3F)));
        append(static_cast<char>(0x80 | (cp & 0x3F)));
        length -= 2; // Adjust length since append(char) increments it
    }
    else {
        append(static_cast<char>(0xF0 | (cp >> 18)));
        append(static_cast<char>(0x80 | ((cp >> 12) & 0x3F)));
        append(static_cast<char>(0x80 | ((cp >> 6) & 0x3F)));
        append(static_cast<char>(0x80 | (cp & 0x3F)));
        length -= 3; // Adjust length since append(char) increments it
    }
}

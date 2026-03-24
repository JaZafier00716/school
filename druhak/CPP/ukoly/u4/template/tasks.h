/// UTF-8 string (reuse your previous implementation and modify it)
#pragma once

#include <cstddef>
#include <string>
#include <cstdint>
#include <functional>
#include <iostream>
#include <memory>
#include <utility>
#include <cstring>
#include <vector>
#include <optional>

using CodePoint = uint32_t;


class UTF8String {
private:
    std::vector<uint8_t> data;
    size_t length{}; // Number of Unicode code points in the string

public:
    UTF8String() : length(0) {}
    explicit UTF8String(const char* str);
    explicit UTF8String(const std::string& str);
    explicit UTF8String(const std::vector<uint8_t>& bytes);
    explicit UTF8String(const std::vector<CodePoint>& codePoints);
    UTF8String(const UTF8String& other);
    UTF8String(UTF8String&& other) noexcept;

    UTF8String& operator=(const UTF8String& other);
    UTF8String& operator=(UTF8String&& other) noexcept;
    UTF8String operator+(const UTF8String& other) const;
    UTF8String& operator+=(const UTF8String& other);
    bool operator==(const UTF8String& other) const;
    bool operator!=(const UTF8String& other) const;
    std::optional<uint8_t> operator[](size_t index) const;

    [[nodiscard]] std::optional<CodePoint> nth_code_point(size_t index) const;
    void append(char c);
    void append(CodePoint cp);

    class BytesIterator {
    public:
        BytesIterator(const UTF8String* owner, size_t index): owner(owner), index(index) {}

        uint8_t operator*() const;
        BytesIterator& operator++();
        BytesIterator& operator--();
        BytesIterator operator++(int);
        BytesIterator operator--(int);
        BytesIterator& operator+=(std::ptrdiff_t offset);
        BytesIterator& operator-=(std::ptrdiff_t offset);
        BytesIterator operator+(std::ptrdiff_t offset) const;
        BytesIterator operator-(std::ptrdiff_t offset) const;
        std::ptrdiff_t operator-(const BytesIterator& other) const;
        bool operator==(const BytesIterator& other) const;
        bool operator!=(const BytesIterator& other) const;

    private:
        const UTF8String* owner;
        size_t index;
    };

    class BytesView {
    public:
        explicit BytesView(const UTF8String* owner): owner(owner) {}

        BytesIterator begin() const;
        BytesIterator end() const;

    private:
        const UTF8String* owner;
    };

    class CodePointIterator {
    public:
        CodePointIterator(const UTF8String* owner, size_t byteIndex): owner(owner), byteIndex(byteIndex) {}

        CodePoint operator*() const;
        CodePointIterator& operator++();
        CodePointIterator& operator--();
        CodePointIterator operator++(int);
        CodePointIterator operator--(int);
        bool operator==(const CodePointIterator& other) const;
        bool operator!=(const CodePointIterator& other) const;

    private:
        const UTF8String* owner;
        size_t byteIndex;
    };

    class CodePointView {
    public:
        explicit CodePointView(const UTF8String* owner): owner(owner) {}

        CodePointIterator begin() const;
        CodePointIterator end() const;

    private:
        const UTF8String* owner;
    };

    [[nodiscard]] BytesView bytes() const {
        return BytesView(this);
    }

    [[nodiscard]] CodePointView codepoints() const {
        return CodePointView(this);
    }

    // Convert UTF8String to std::string
    [[nodiscard]] std::string to_string() const {
        return std::string(data.begin(), data.end());
    }

    // Conversion operator for implicit conversion to std::string
    explicit operator std::string() const {
        return to_string();
    }

    [[nodiscard]] size_t get_byte_count() const {
        return data.size();
    }

    [[nodiscard]] size_t get_point_count() const {
        return length;
    }

    void print_bytes_hex() const {
        for (const unsigned char i : data) {
            std::cout << std::hex << "0x" << static_cast<int>(i) << " ";
        }
        std::cout << std::dec << std::endl; // Reset to decimal
    }
};

/// Binary tree
// Big data that we cannot afford to copy
struct BigData {
    explicit BigData(const int value): value(value) {}

    BigData(const BigData&) = delete;
    BigData& operator=(const BigData&) = delete;

    int value;
};

class Tree {
public:
    explicit Tree(int value);
    explicit Tree(std::shared_ptr<BigData> value);

    BigData& get_value();
    [[nodiscard]] const BigData& get_value() const;

    Tree* get_parent();
    [[nodiscard]] const Tree* get_parent() const;
    [[nodiscard]] bool has_parent() const;

    Tree* get_left_child();
    [[nodiscard]] const Tree* get_left_child() const;
    Tree* get_right_child();
    [[nodiscard]] const Tree* get_right_child() const;

    Tree* get_root();
    [[nodiscard]] const Tree* get_root() const;

    std::unique_ptr<Tree> take_left_child();
    std::unique_ptr<Tree> take_right_child();
    std::unique_ptr<Tree> take_child(Tree& child);

    std::unique_ptr<Tree> set_left_child(std::unique_ptr<Tree> child);
    std::unique_ptr<Tree> set_right_child(std::unique_ptr<Tree> child);

    void swap_children();
    bool is_same_tree_as(Tree* other) const;
    void replace_value(std::shared_ptr<BigData> new_value);

    class InorderIterator {
    public:
        explicit InorderIterator(Tree* node): node(node) {}

        Tree& operator*() const;
        Tree* operator->() const;
        InorderIterator& operator++();
        InorderIterator operator++(int);
        bool operator==(const InorderIterator& other) const;
        bool operator!=(const InorderIterator& other) const;

    private:
        Tree* node;
    };

    class ConstInorderIterator {
    public:
        explicit ConstInorderIterator(const Tree* node): node(node) {}

        const Tree& operator*() const;
        const Tree* operator->() const;
        ConstInorderIterator& operator++();
        ConstInorderIterator operator++(int);
        bool operator==(const ConstInorderIterator& other) const;
        bool operator!=(const ConstInorderIterator& other) const;

    private:
        const Tree* node;
    };

    InorderIterator begin();
    InorderIterator end();
    [[nodiscard]] ConstInorderIterator begin() const;
    [[nodiscard]] ConstInorderIterator end() const;

private:
    std::shared_ptr<BigData> value;
    Tree* parent{nullptr};
    std::unique_ptr<Tree> left_child;
    std::unique_ptr<Tree> right_child;
};

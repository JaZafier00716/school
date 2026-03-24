#include "tasks.h"

#include <stdexcept>

namespace {
size_t codepoint_byte_width(const uint8_t firstByte) {
    if (firstByte < 0x80) {
        return 1;
    }
    if ((firstByte & 0xE0) == 0xC0) {
        return 2;
    }
    if ((firstByte & 0xF0) == 0xE0) {
        return 3;
    }
    if ((firstByte & 0xF8) == 0xF0) {
        return 4;
    }
    // Invalid leading byte: treat it as one byte to keep iteration progressing.
    return 1;
}

size_t count_codepoints(const std::vector<uint8_t>& bytes) {
    size_t count = 0;
    size_t index = 0;
    while (index < bytes.size()) {
        index += codepoint_byte_width(bytes[index]);
        if (index > bytes.size()) {
            index = bytes.size();
        }
        ++count;
    }
    return count;
}

CodePoint decode_codepoint_at(const std::vector<uint8_t>& bytes, const size_t byteIndex) {
    const uint8_t firstByte = bytes[byteIndex];
    if (firstByte < 0x80) {
        return firstByte;
    }
    if ((firstByte & 0xE0) == 0xC0) {
        return ((firstByte & 0x1F) << 6) | (bytes[byteIndex + 1] & 0x3F);
    }
    if ((firstByte & 0xF0) == 0xE0) {
        return ((firstByte & 0x0F) << 12) | ((bytes[byteIndex + 1] & 0x3F) << 6) | (bytes[byteIndex + 2] & 0x3F);
    }
    if ((firstByte & 0xF8) == 0xF0) {
        return ((firstByte & 0x07) << 18) | ((bytes[byteIndex + 1] & 0x3F) << 12) |
            ((bytes[byteIndex + 2] & 0x3F) << 6) | (bytes[byteIndex + 3] & 0x3F);
    }
    return firstByte;
}

size_t previous_codepoint_start(const std::vector<uint8_t>& bytes, size_t from) {
    if (from == 0) {
        return 0;
    }

    --from;
    while (from > 0 && (bytes[from] & 0xC0) == 0x80) {
        --from;
    }
    return from;
}
}

/*
 *  Constructors:
 */

UTF8String::UTF8String(const char* str) {
    const auto byteCount = std::strlen(str);
    data.insert(data.end(), str, str + byteCount);
    length = count_codepoints(data);
}

UTF8String::UTF8String(const std::string& str) {
    data.insert(data.end(), str.begin(), str.end());
    length = count_codepoints(data);
}

UTF8String::UTF8String(const std::vector<uint8_t>& bytes) {
    data = bytes;
    length = count_codepoints(data);
}

UTF8String::UTF8String(const std::vector<CodePoint>& codePoints) {
    for (const auto& cp : codePoints) {
        append(cp);
    }
}

UTF8String::UTF8String(const UTF8String& other) {
    length = other.length;
    data = other.data;
}

UTF8String::UTF8String(UTF8String&& other) noexcept {
    data = std::move(other.data);
    length = other.length;
    other.length = 0;
}

/*
 *  Operators:
 */
UTF8String& UTF8String::operator=(const UTF8String& other) {
    if (this != &other) {
        data = other.data;
        length = other.length;
    }
    return *this;
}

UTF8String& UTF8String::operator=(UTF8String&& other) noexcept {
    if (this != &other) {
        data = std::move(other.data);
        length = other.length;
        other.length = 0;
    }
    return *this;
}

UTF8String UTF8String::operator+(const UTF8String& other) const {
    UTF8String result;
    result.data.reserve(data.size() + other.data.size());
    result.data.insert(result.data.end(), data.begin(), data.end());
    result.data.insert(result.data.end(), other.data.begin(), other.data.end());
    result.length = length + other.length;
    return result;
}

UTF8String& UTF8String::operator+=(const UTF8String& other) {
    data.insert(data.end(), other.data.begin(), other.data.end());
    length += other.length;
    return *this;
}

bool UTF8String::operator==(const UTF8String& other) const {
    if (data.size() != other.data.size()) {
        return false;
    }
    return data == other.data;
}

bool UTF8String::operator!=(const UTF8String& other) const {
    return !(*this == other);
}

std::optional<uint8_t> UTF8String::operator[](const size_t index) const {
    if (index >= data.size()) {
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
    while (byteIndex < data.size()) {
        if (codePointIndex == index) {
            return decode_codepoint_at(data, byteIndex);
        }
        byteIndex += codepoint_byte_width(data[byteIndex]);
        codePointIndex++;
    }
    return std::nullopt; // Index out of bounds
}

void UTF8String::append(const char c) {
    data.push_back(static_cast<uint8_t>(c));
    length++; // Assuming we're appending a single-byte character
}

void UTF8String::append(const CodePoint cp) {
    if (cp < 0x80) {
        append(static_cast<char>(cp));
        return;
    }
    else if (cp < 0x800) {
        data.push_back(static_cast<char>(0xC0 | (cp >> 6)));
        data.push_back(static_cast<char>(0x80 | (cp & 0x3F)));
    }
    else if (cp < 0x10000) {
        data.push_back(static_cast<char>(0xE0 | (cp >> 12)));
        data.push_back(static_cast<char>(0x80 | ((cp >> 6) & 0x3F)));
        data.push_back(static_cast<char>(0x80 | (cp & 0x3F)));
    }
    else {
        data.push_back(static_cast<char>(0xF0 | (cp >> 18)));
        data.push_back(static_cast<char>(0x80 | ((cp >> 12) & 0x3F)));
        data.push_back(static_cast<char>(0x80 | ((cp >> 6) & 0x3F)));
        data.push_back(static_cast<char>(0x80 | (cp & 0x3F)));
    }
    length++;
}

uint8_t UTF8String::BytesIterator::operator*() const {
    return owner->data[index];
}

UTF8String::BytesIterator& UTF8String::BytesIterator::operator++() {
    ++index;
    return *this;
}

UTF8String::BytesIterator& UTF8String::BytesIterator::operator--() {
    --index;
    return *this;
}

UTF8String::BytesIterator UTF8String::BytesIterator::operator++(int) {
    const BytesIterator old = *this;
    ++(*this);
    return old;
}

UTF8String::BytesIterator UTF8String::BytesIterator::operator--(int) {
    const BytesIterator old = *this;
    --(*this);
    return old;
}

UTF8String::BytesIterator& UTF8String::BytesIterator::operator+=(const std::ptrdiff_t offset) {
    if (offset >= 0) {
        index += static_cast<size_t>(offset);
    }
    else {
        index -= static_cast<size_t>(-offset);
    }
    return *this;
}

UTF8String::BytesIterator& UTF8String::BytesIterator::operator-=(const std::ptrdiff_t offset) {
    if (offset >= 0) {
        index -= static_cast<size_t>(offset);
    }
    else {
        index += static_cast<size_t>(-offset);
    }
    return *this;
}

UTF8String::BytesIterator UTF8String::BytesIterator::operator+(const std::ptrdiff_t offset) const {
    BytesIterator copy = *this;
    copy += offset;
    return copy;
}

UTF8String::BytesIterator UTF8String::BytesIterator::operator-(const std::ptrdiff_t offset) const {
    BytesIterator copy = *this;
    copy -= offset;
    return copy;
}

std::ptrdiff_t UTF8String::BytesIterator::operator-(const BytesIterator& other) const {
    return static_cast<std::ptrdiff_t>(index) - static_cast<std::ptrdiff_t>(other.index);
}

bool UTF8String::BytesIterator::operator==(const BytesIterator& other) const {
    return owner == other.owner && index == other.index;
}

bool UTF8String::BytesIterator::operator!=(const BytesIterator& other) const {
    return !(*this == other);
}

UTF8String::BytesIterator UTF8String::BytesView::begin() const {
    return BytesIterator(owner, 0);
}

UTF8String::BytesIterator UTF8String::BytesView::end() const {
    return BytesIterator(owner, owner->data.size());
}

CodePoint UTF8String::CodePointIterator::operator*() const {
    return decode_codepoint_at(owner->data, byteIndex);
}

UTF8String::CodePointIterator& UTF8String::CodePointIterator::operator++() {
    byteIndex += codepoint_byte_width(owner->data[byteIndex]);
    return *this;
}

UTF8String::CodePointIterator& UTF8String::CodePointIterator::operator--() {
    byteIndex = previous_codepoint_start(owner->data, byteIndex);
    return *this;
}

UTF8String::CodePointIterator UTF8String::CodePointIterator::operator++(int) {
    const CodePointIterator old = *this;
    ++(*this);
    return old;
}

UTF8String::CodePointIterator UTF8String::CodePointIterator::operator--(int) {
    const CodePointIterator old = *this;
    --(*this);
    return old;
}

bool UTF8String::CodePointIterator::operator==(const CodePointIterator& other) const {
    return owner == other.owner && byteIndex == other.byteIndex;
}

bool UTF8String::CodePointIterator::operator!=(const CodePointIterator& other) const {
    return !(*this == other);
}

UTF8String::CodePointIterator UTF8String::CodePointView::begin() const {
    return CodePointIterator(owner, 0);
}

UTF8String::CodePointIterator UTF8String::CodePointView::end() const {
    return CodePointIterator(owner, owner->data.size());
}

namespace {
Tree* leftmost(Tree* node) {
    Tree* current = node;
    while (current != nullptr && current->get_left_child() != nullptr) {
        current = current->get_left_child();
    }
    return current;
}

const Tree* leftmost(const Tree* node) {
    const Tree* current = node;
    while (current != nullptr && current->get_left_child() != nullptr) {
        current = current->get_left_child();
    }
    return current;
}

Tree* inorder_successor(Tree* node) {
    if (node == nullptr) {
        return nullptr;
    }
    if (node->get_right_child() != nullptr) {
        return leftmost(node->get_right_child());
    }

    Tree* current = node;
    Tree* parent = current->get_parent();
    while (parent != nullptr && parent->get_right_child() == current) {
        current = parent;
        parent = parent->get_parent();
    }
    return parent;
}

const Tree* inorder_successor(const Tree* node) {
    if (node == nullptr) {
        return nullptr;
    }
    if (node->get_right_child() != nullptr) {
        return leftmost(node->get_right_child());
    }

    const Tree* current = node;
    const Tree* parent = current->get_parent();
    while (parent != nullptr && parent->get_right_child() == current) {
        current = parent;
        parent = parent->get_parent();
    }
    return parent;
}
}

Tree::Tree(const int value): value(std::make_shared<BigData>(value)) {}

Tree::Tree(std::shared_ptr<BigData> value): value(std::move(value)) {}

BigData& Tree::get_value() {
    return *value;
}

const BigData& Tree::get_value() const {
    return *value;
}

Tree* Tree::get_parent() {
    return parent;
}

const Tree* Tree::get_parent() const {
    return parent;
}

bool Tree::has_parent() const {
    return parent != nullptr;
}

Tree* Tree::get_left_child() {
    return left_child.get();
}

const Tree* Tree::get_left_child() const {
    return left_child.get();
}

Tree* Tree::get_right_child() {
    return right_child.get();
}

const Tree* Tree::get_right_child() const {
    return right_child.get();
}

Tree* Tree::get_root() {
    Tree* root = this;
    while (root->parent != nullptr) {
        root = root->parent;
    }
    return root;
}

const Tree* Tree::get_root() const {
    const Tree* root = this;
    while (root->parent != nullptr) {
        root = root->parent;
    }
    return root;
}

std::unique_ptr<Tree> Tree::take_left_child() {
    auto previous = std::move(left_child);
    if (previous != nullptr) {
        previous->parent = nullptr;
    }
    return previous;
}

std::unique_ptr<Tree> Tree::take_right_child() {
    auto previous = std::move(right_child);
    if (previous != nullptr) {
        previous->parent = nullptr;
    }
    return previous;
}

std::unique_ptr<Tree> Tree::take_child(Tree& child) {
    if (left_child.get() == &child) {
        return take_left_child();
    }
    if (right_child.get() == &child) {
        return take_right_child();
    }
    throw std::runtime_error("Passed node is not a child of this node");
}

std::unique_ptr<Tree> Tree::set_left_child(std::unique_ptr<Tree> child) {
    auto previous = std::move(left_child);
    if (previous != nullptr) {
        previous->parent = nullptr;
    }

    left_child = std::move(child);
    if (left_child != nullptr) {
        left_child->parent = this;
    }

    return previous;
}

std::unique_ptr<Tree> Tree::set_right_child(std::unique_ptr<Tree> child) {
    auto previous = std::move(right_child);
    if (previous != nullptr) {
        previous->parent = nullptr;
    }

    right_child = std::move(child);
    if (right_child != nullptr) {
        right_child->parent = this;
    }

    return previous;
}

void Tree::swap_children() {
    std::swap(left_child, right_child);
    if (left_child != nullptr) {
        left_child->parent = this;
    }
    if (right_child != nullptr) {
        right_child->parent = this;
    }
}

bool Tree::is_same_tree_as(Tree* other) const {
    if (other == nullptr) {
        return false;
    }
    return get_root() == other->get_root();
}

void Tree::replace_value(std::shared_ptr<BigData> new_value) {
    value = std::move(new_value);

    if (left_child != nullptr) {
        left_child->replace_value(value);
    }
    if (right_child != nullptr) {
        right_child->replace_value(value);
    }
}

Tree& Tree::InorderIterator::operator*() const {
    return *node;
}

Tree* Tree::InorderIterator::operator->() const {
    return node;
}

Tree::InorderIterator& Tree::InorderIterator::operator++() {
    node = inorder_successor(node);
    return *this;
}

Tree::InorderIterator Tree::InorderIterator::operator++(int) {
    const InorderIterator old = *this;
    ++(*this);
    return old;
}

bool Tree::InorderIterator::operator==(const InorderIterator& other) const {
    return node == other.node;
}

bool Tree::InorderIterator::operator!=(const InorderIterator& other) const {
    return !(*this == other);
}

const Tree& Tree::ConstInorderIterator::operator*() const {
    return *node;
}

const Tree* Tree::ConstInorderIterator::operator->() const {
    return node;
}

Tree::ConstInorderIterator& Tree::ConstInorderIterator::operator++() {
    node = inorder_successor(node);
    return *this;
}

Tree::ConstInorderIterator Tree::ConstInorderIterator::operator++(int) {
    const ConstInorderIterator old = *this;
    ++(*this);
    return old;
}

bool Tree::ConstInorderIterator::operator==(const ConstInorderIterator& other) const {
    return node == other.node;
}

bool Tree::ConstInorderIterator::operator!=(const ConstInorderIterator& other) const {
    return !(*this == other);
}

Tree::InorderIterator Tree::begin() {
    return InorderIterator(leftmost(this));
}

Tree::InorderIterator Tree::end() {
    return InorderIterator(nullptr);
}

Tree::ConstInorderIterator Tree::begin() const {
    return ConstInorderIterator(leftmost(this));
}

Tree::ConstInorderIterator Tree::end() const {
    return ConstInorderIterator(nullptr);
}


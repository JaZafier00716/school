#include "../include/editor/gap_buffer.h"
#include <cassert>
#include <iostream>
#include <string>

// Helper to extract the full string from the buffer
std::string get_full_text(const editor::GapBuffer& buf) {
    auto [left, right] = buf.get_text_spans();
    return std::string(left) + std::string(right);
}

void test_basic_insertion() {
    editor::GapBuffer buf;
    buf.insert("Hello");
    assert(get_full_text(buf) == "Hello");
    assert(buf.length() == 5);
}

void test_cursor_movement_and_insertion() {
    editor::GapBuffer buf;
    buf.insert("HelloWorld");

    // Move cursor back 5 spaces (between Hello and World)
    buf.move_cursor_by(-5);
    buf.insert(", ");

    assert(get_full_text(buf) == "Hello, World");
}

void test_backspace_and_delete() {
    editor::GapBuffer buf;
    buf.insert("Cat");

    // Cursor is at the end. Backspace should delete 't'
    buf.backspace();
    assert(get_full_text(buf) == "Ca");

    // Move to the start (index 0). Delete should remove 'C'
    buf.move_cursor_to(editor::BufferIndex{0});
    buf.delete_char();
    assert(get_full_text(buf) == "a");
}

int main() {
    test_basic_insertion();
    test_cursor_movement_and_insertion();
    test_backspace_and_delete();

    std::cout << "All GapBuffer tests passed! We are robust.\n";
    return 0;
}
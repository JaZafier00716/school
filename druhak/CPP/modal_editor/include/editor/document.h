//
// Created by jan on 3/12/26.
//

#ifndef MODAL_EDITOR_DOCUMENT_H
#define MODAL_EDITOR_DOCUMENT_H
#include "editor/gap_buffer.h"
#include <vector>
#include <cstddef>

namespace editor {
    struct Position {
        std::size_t row;
        std::size_t col;
    };

    class Document {
    private:
        GapBuffer text;
        std::vector<std::size_t> line_starts;
        Position cursor_pos;

        [[nodiscard]] BufferIndex position_to_index(Position pos) const noexcept;
        [[nodiscard]] std::size_t get_line_length(std::size_t row) const noexcept;

    public:
        Document();

        void insert(char c);
        void insert_newline();
        void backspace();

        void move_cursor(Position new_pos);
        void move_cursor_up();
        void move_cursor_down();
        void move_cursor_left();
        void move_cursor_right();

        [[nodiscard]] Position get_cursor_position() const noexcept;
        [[nodiscard]] std::size_t get_line_count() const noexcept;

        [[nodiscard]] const GapBuffer& get_buffer() const noexcept {
            return text;
        }
    };
} // editor

#endif //MODAL_EDITOR_DOCUMENT_H
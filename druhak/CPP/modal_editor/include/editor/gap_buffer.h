//
// Created by jan on 3/12/26.
//

#ifndef MODAL_EDITOR_GAP_BUFFER_H
#define MODAL_EDITOR_GAP_BUFFER_H
#include <vector>
#include <string_view>
#include <utility>
#include <cstddef>
#include <stdexcept>

namespace editor {
    struct BufferIndex {
        std::size_t value;
        explicit BufferIndex(const std::size_t value) : value(value) {}
    };

    class GapBuffer {
    private:
        std::vector<char> buffer;
        std::size_t gap_start;
        std::size_t gap_end;

        void expand_gap(std::size_t required_space);
        void shift_gap_to(std::size_t target_index);
    public:
        explicit GapBuffer(std::size_t initial_capacity = 1024);

        /**
         * @param c character to insert at cursor's position
         */
        void insert(char c);
        void insert(std::string_view str);

        void backspace();
        void delete_char();

        void move_cursor_to(BufferIndex new_pos);
        void move_cursor_by(long long offset);

        [[nodiscard]] std::size_t length() const noexcept;
        [[nodiscard]] BufferIndex cursor_position() const noexcept;
        [[nodiscard]] bool is_empty() const noexcept;

        [[nodiscard]] std::pair<std::string_view, std::string_view> get_text_spans() const;
    };
}

#endif //MODAL_EDITOR_GAP_BUFFER_H
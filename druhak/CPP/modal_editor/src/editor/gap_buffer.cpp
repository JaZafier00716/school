//
// Created by jan on 3/12/26.
//
#include <algorithm>
#include <stdexcept>
#include "../../include/editor/gap_buffer.h"

namespace editor {
    GapBuffer::GapBuffer(const std::size_t initial_capacity) : buffer(initial_capacity), gap_start(0),
                                                               gap_end(initial_capacity) {}

    void GapBuffer::expand_gap(const std::size_t required_space) {
        const std::size_t current_gap_size = gap_end - gap_start;
        if (current_gap_size >= required_space) {
            return;
        }

        const auto new_capacity = buffer.size() * 2 + required_space;
        const auto text_after_gap = buffer.size() - gap_end;

        std::vector<char> new_buffer(new_capacity);

        std::copy_n(
            buffer.begin(),
            static_cast<long>(gap_start),
            new_buffer.begin()
        );

        const std::size_t new_gap_end = new_capacity - text_after_gap;
        std::copy(
            buffer.begin() + static_cast<long>(gap_end),
            buffer.end(),
            new_buffer.begin() + static_cast<long>(new_gap_end)
        );

        buffer = std::move(new_buffer);
        gap_end = new_gap_end;
    }

    void GapBuffer::shift_gap_to(const std::size_t target_index) {
        if (target_index == gap_start) {
            // current position
            return;
        }
        if (target_index < gap_start) {
            // Cursor moved left
            const auto distance = gap_start - target_index;

            std::copy_backward(
                buffer.begin() + static_cast<long>(target_index),
                buffer.begin() + static_cast<long>(gap_start),
                buffer.begin() + static_cast<long>(gap_end)
            );

            gap_start -= distance;
            gap_end -= distance;
        }
        else {
            // Cursor moved right
            const std::size_t distance = target_index - gap_start;

            std::copy(
                buffer.begin() + static_cast<long>(gap_end),
                buffer.begin() + static_cast<long>(gap_end + distance),
                buffer.begin() + static_cast<long>(gap_start)
            );

            gap_start += distance;
            gap_end += distance;
        }
    }

    void GapBuffer::insert(const char c) {
        expand_gap(1);
        buffer[gap_start] = c;
        ++gap_start;
    }

    void GapBuffer::insert(const std::string_view str) {
        expand_gap(str.length());

        std::ranges::copy(
            str,
            buffer.begin() + static_cast<long>(gap_start)
        );
        gap_start += str.length();
    }

    void GapBuffer::backspace() {
        if (gap_start > 0) {
            --gap_start;
        }
    }

    void GapBuffer::delete_char() {
        if (gap_end < buffer.size()) {
            ++gap_end;
        }
    }

    void GapBuffer::move_cursor_to(const BufferIndex new_pos) {
        if (new_pos.value > length()) {
            throw std::out_of_range("Cursor position out of bounds");
        }
        shift_gap_to(new_pos.value);
    }

    void GapBuffer::move_cursor_by(const long long offset) {
        const auto current_pos = static_cast<long long>(gap_start);
        const auto new_pos = current_pos + offset;

        if (new_pos < 0) {
            shift_gap_to(0);
        } else if (new_pos > static_cast<long long>(length())) {
            shift_gap_to(length());
        } else {
            shift_gap_to(static_cast<std::size_t>(new_pos));
        }
    }

    std::size_t GapBuffer::length() const noexcept {
        return buffer.size() - (gap_end - gap_start);
    }

    BufferIndex GapBuffer::cursor_position() const noexcept {
        return BufferIndex(gap_start);
    }

    bool GapBuffer::is_empty() const noexcept {
        return length() == 0;
    }

    std::pair<std::string_view, std::string_view> GapBuffer::get_text_spans() const {
        std::string_view left_text_span(buffer.data(), gap_start);
        std::string_view right_text_span(buffer.data() + gap_end, buffer.size() - gap_end);

        return {left_text_span, right_text_span};
    }
} // editor

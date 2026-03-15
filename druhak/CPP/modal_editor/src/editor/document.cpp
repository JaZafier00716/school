//
// Created by jan on 3/12/26.
//

#include "../../include/editor/document.h"

namespace editor {
    BufferIndex Document::position_to_index(const Position pos) const noexcept {
        const auto safe_row = std::min(pos.row, line_starts.size() - 1);
        const auto max_col = get_line_length(safe_row);
        const auto safe_col = std::min(pos.col, max_col);

        return BufferIndex(line_starts[safe_row] + safe_col);
    }

    std::size_t Document::get_line_length(const std::size_t row) const noexcept {
        if (row >= line_starts.size()) {
            return 0;
        }

        if (row == line_starts.size() - 1) {
            // Last line: length is total text length minus start of last line
            return text.length() - line_starts[row];
        }

        return line_starts[row + 1] - line_starts[row] - 1; // -1 to exclude newline character
    }

    Document::Document() : cursor_pos({0, 0}) {
        line_starts.push_back(0);
    }

    void Document::insert(char c) {
        if (c == '\n') {
            insert_newline();
            return;
        }

        text.insert(c);

        ++cursor_pos.col;

        for (std::size_t i = cursor_pos.row + 1; i < line_starts.size(); ++i) {
            ++line_starts[i];
        }
    }

    void Document::insert_newline() {
        text.insert('\n');

        const auto new_line_start_index = text.cursor_position().value;

        line_starts.insert(line_starts.begin() + static_cast<long>(cursor_pos.row + 1), new_line_start_index);

        ++cursor_pos.row;
        cursor_pos.col = 0;

        for (std::size_t i = cursor_pos.row + 1; i < line_starts.size(); ++i) {
            ++line_starts[i];
        }
    }

    void Document::backspace() {
        if (cursor_pos.row == 0 && cursor_pos.col == 0) {
            return;
        }

        if (cursor_pos.col > 0) {
            text.backspace();
            --cursor_pos.col;

            for (size_t i = cursor_pos.row + 1; i < line_starts.size(); ++i) {
                --line_starts[i];
            }
        } else {
            const auto prev_row = cursor_pos.row - 1;
            const auto prev_col = get_line_length(prev_row);

            text.backspace();

            line_starts.erase(line_starts.begin() + static_cast<long>(cursor_pos.row));

            for (std::size_t i = cursor_pos.row; i < line_starts.size(); ++i) {
                --line_starts[i];
            }

            cursor_pos.row = prev_row;
            cursor_pos.col = prev_col;
        }
    }

    void Document::move_cursor(const Position new_pos) {
        const auto target_index = position_to_index(new_pos);

        text.move_cursor_to(target_index);

        const auto safe_row = std::min(new_pos.row, line_starts.size() - 1);
        const auto max_col = get_line_length(safe_row);
        cursor_pos = {
                .row = safe_row,
                .col = std::min(new_pos.col, max_col)
            };
    };

    void Document::move_cursor_up() {
        if (cursor_pos.row > 0) {
            move_cursor({
                    .row = cursor_pos.row - 1,
                    .col = cursor_pos.col
                });
        }
    }

    void Document::move_cursor_down() {
        if (cursor_pos.row + 1 < line_starts.size()) {
            move_cursor({
                    .row = cursor_pos.row + 1,
                    .col = cursor_pos.col
                });
        }
    }

    void Document::move_cursor_left() {
        if (cursor_pos.col > 0) {
            move_cursor({
                .row = cursor_pos.row,
                .col = cursor_pos.col - 1
            });
        } else if (cursor_pos.row > 0) {
            const auto prev_line_length = get_line_length(cursor_pos.row - 1);
            move_cursor({
                .row = cursor_pos.row - 1,
                .col = prev_line_length
            });
        }
    }

    void Document::move_cursor_right() {
        const auto current_line_length = get_line_length(cursor_pos.row);
        if (cursor_pos.col < current_line_length) {
            move_cursor({
                .row = cursor_pos.row,
                .col = cursor_pos.col + 1
            });
        } else if (cursor_pos.row + 1 < line_starts.size()) {
            move_cursor({
                .row = cursor_pos.row + 1,
                .col = 0
            });
        }
    }

    Position Document::get_cursor_position() const noexcept {
        return cursor_pos;
    }

    std::size_t Document::get_line_count() const noexcept {
        return line_starts.size();
    }
} // editor

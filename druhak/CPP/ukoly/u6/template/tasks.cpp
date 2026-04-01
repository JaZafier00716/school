#include "tasks.h"
#include <functional>
#include <string>
#include <cctype>
#include <cstring>

namespace {
    void skip_ws(std::istream& is) {
        while (true) {
            int ch = is.peek();
            if (ch == EOF) {
                return;
            }
            if (!std::isspace(static_cast<unsigned char>(ch))) {
                return;
            }
            is.get();
        }
    }

    bool consume_literal(std::istream& is, const char* literal) {
        for (size_t i = 0; literal[i] != '\0'; ++i) {
            int ch = is.get();
            if (ch == EOF || static_cast<char>(ch) != literal[i]) {
                return false;
            }
        }
        return true;
    }

    std::optional<String> parse_string_value(std::istream& is) {
        int first = is.get();
        if (first != '"') {
            return std::nullopt;
        }

        std::string out;
        while (true) {
            int ch = is.get();
            if (ch == EOF) {
                return std::nullopt;
            }
            if (ch == '"') {
                return String{out};
            }
            if (ch == '\\') {
                int esc = is.get();
                if (esc == EOF) {
                    return std::nullopt;
                }
                if (esc != '\\' && esc != '"') {
                    return std::nullopt;
                }
                out.push_back(static_cast<char>(esc));
                continue;
            }
            out.push_back(static_cast<char>(ch));
        }
    }

    std::optional<Number> parse_number_value(std::istream& is) {
        int ch = is.peek();
        if (ch == EOF) {
            return std::nullopt;
        }

        bool negative = false;
        if (ch == '-') {
            is.get();
            negative = true;
            ch = is.peek();
            if (ch == EOF || !std::isdigit(static_cast<unsigned char>(ch))) {
                return std::nullopt;
            }
        }

        if (!std::isdigit(static_cast<unsigned char>(ch))) {
            return std::nullopt;
        }

        double value = 0.0;
        while (true) {
            ch = is.peek();
            if (ch == EOF || !std::isdigit(static_cast<unsigned char>(ch))) {
                break;
            }
            is.get();
            value = value * 10.0 + static_cast<double>(ch - '0');
        }

        bool seen_dot = false;
        if (is.peek() == '.') {
            is.get();
            seen_dot = true;
        }

        if (seen_dot) {
            double factor = 0.1;
            while (true) {
                ch = is.peek();
                if (ch == EOF || !std::isdigit(static_cast<unsigned char>(ch))) {
                    break;
                }
                is.get();
                value += static_cast<double>(ch - '0') * factor;
                factor *= 0.1;
            }
        }

        if (negative) {
            value = -value;
        }
        return Number{value};
    }

    std::optional<Value> parse_value(std::istream& is);

    std::optional<Array> parse_array_value(std::istream& is) {
        int first = is.get();
        if (first != '[') {
            return std::nullopt;
        }

        Array array{};
        skip_ws(is);

        int ch = is.peek();
        if (ch == ']') {
            is.get();
            return array;
        }

        while (true) {
            auto item = parse_value(is);
            if (!item.has_value()) {
                return std::nullopt;
            }
            array.items.push_back(std::move(item.value()));

            skip_ws(is);
            ch = is.peek();
            if (ch == ',') {
                is.get();
                skip_ws(is);
                continue;
            }
            if (ch == ']') {
                is.get();
                return array;
            }
            return std::nullopt;
        }
    }

    std::optional<Object> parse_object_value(std::istream& is) {
        int first = is.get();
        if (first != '{') {
            return std::nullopt;
        }

        Object object{};
        skip_ws(is);

        int ch = is.peek();
        if (ch == '}') {
            is.get();
            return object;
        }

        while (true) {
            if (is.peek() != '"') {
                return std::nullopt;
            }
            auto key = parse_string_value(is);
            if (!key.has_value()) {
                return std::nullopt;
            }

            skip_ws(is);
            if (is.get() != ':') {
                return std::nullopt;
            }

            skip_ws(is);
            auto value = parse_value(is);
            if (!value.has_value()) {
                return std::nullopt;
            }
            object.items[key->value] = std::move(value.value());

            skip_ws(is);
            ch = is.peek();
            if (ch == ',') {
                is.get();
                skip_ws(is);
                continue;
            }
            if (ch == '}') {
                is.get();
                return object;
            }
            return std::nullopt;
        }
    }

    std::optional<Value> parse_value(std::istream& is) {
        skip_ws(is);
        int ch = is.peek();
        if (ch == EOF) {
            return std::nullopt;
        }

        if (ch == 'n') {
            if (!consume_literal(is, "null")) {
                return std::nullopt;
            }
            return Value{Null{}};
        }
        if (ch == 't') {
            if (!consume_literal(is, "true")) {
                return std::nullopt;
            }
            return Value{Boolean{true}};
        }
        if (ch == 'f') {
            if (!consume_literal(is, "false")) {
                return std::nullopt;
            }
            return Value{Boolean{false}};
        }
        if (ch == '"') {
            auto str = parse_string_value(is);
            if (!str.has_value()) {
                return std::nullopt;
            }
            return Value{std::move(str.value())};
        }
        if (ch == '[') {
            auto arr = parse_array_value(is);
            if (!arr.has_value()) {
                return std::nullopt;
            }
            return Value{std::move(arr.value())};
        }
        if (ch == '{') {
            auto obj = parse_object_value(is);
            if (!obj.has_value()) {
                return std::nullopt;
            }
            return Value{std::move(obj.value())};
        }
        if (ch == '-' || std::isdigit(static_cast<unsigned char>(ch))) {
            auto num = parse_number_value(is);
            if (!num.has_value()) {
                return std::nullopt;
            }
            return Value{num.value()};
        }
        return std::nullopt;
    }
}

bool Array::operator==(const Array& other) const {
    return other.items == this->items;
}

bool Object::operator==(const Object& other) const {
    return other.items == this->items;
}

bool String::operator==(const String& other) const {
    return other.value == this->value;
}

bool Null::operator==(const Null&) const {
    return true;
}

bool Number::operator==(const Number& other) const {
    return other.value == this->value;
}

bool Boolean::operator==(const Boolean& other) const {
    return other.value == this->value;
}

std::optional<Value> parse_json(std::istream& is) {
    return parse_value(is);
}

std::ostream& operator<<(std::ostream& os, const Value& value) {
    auto write_escaped = [&os](const std::string& text) {
        for (char ch : text) {
            if (ch == '\\') {
                os << "\\\\";
            } else if (ch == '"') {
                os << "\\\"";
            } else {
                os << ch;
            }
        }
    };

    std::visit(overloaded{
        [&os](const Boolean& boolean) {
            os << (boolean.value ? "true" : "false");
        },
        [&os](const Number& number) {
            os << number.value;
        },
        [&os, &write_escaped](const String& string) {
            os << '"';
            write_escaped(string.value);
            os << '"';
        },
        [&os](const Null&) {
            os << "null";
        },
        [&os](const Array& array) {
            os << '[';
            for (size_t i = 0; i < array.items.size(); ++i) {
                if (i > 0) {
                    os << ", ";
                }
                os << array.items[i];
            }
            os << ']';
        },
        [&os, &write_escaped](const Object& object) {
            os << '{';
            size_t i = 0;
            for (const auto& item: object.items) {
                if (i > 0) {
                    os << ", ";
                }
                os << '"';
                write_escaped(item.first);
                os << "\": " << item.second;
                ++i;
            }
            os << '}';
        }
    }, value);
    return os;
}


std::vector<uint8_t> serialize(const Value& value) {
    std::vector<uint8_t> out;

    auto write_u8 = [&out](uint8_t byte) {
        out.push_back(byte);
    };

    auto write_size = [&out](size_t value) {
        uint8_t bytes[sizeof(size_t)];
        std::memcpy(bytes, &value, sizeof(size_t));
        out.insert(out.end(), bytes, bytes + sizeof(size_t));
    };

    auto write_double = [&out](double value) {
        uint8_t bytes[sizeof(double)];
        std::memcpy(bytes, &value, sizeof(double));
        out.insert(out.end(), bytes, bytes + sizeof(double));
    };

    std::function<void(const Value&)> write_value = [&](const Value& val) {
        std::visit(overloaded{
            [&](const Null&) {
                write_u8(0);
            },
            [&](const Boolean& boolean) {
                write_u8(1);
                write_u8(boolean.value ? 1 : 0);
            },
            [&](const Number& number) {
                write_u8(2);
                write_double(number.value);
            },
            [&](const String& string) {
                write_u8(3);
                write_size(string.value.size());
                out.insert(out.end(), string.value.begin(), string.value.end());
            },
            [&](const Array& array) {
                write_u8(4);
                write_size(array.items.size());
                for (const auto& item : array.items) {
                    write_value(item);
                }
            },
            [&](const Object& object) {
                write_u8(5);
                write_size(object.items.size());
                for (const auto& item : object.items) {
                    write_size(item.first.size());
                    out.insert(out.end(), item.first.begin(), item.first.end());
                    write_value(item.second);
                }
            }
        }, val);
    };

    write_value(value);
    return out;
}

Value deserialize(const std::vector<uint8_t>& data) {
    size_t offset = 0;

    auto read_u8 = [&data, &offset]() -> uint8_t {
        uint8_t value = data[offset];
        offset += 1;
        return value;
    };

    auto read_size = [&data, &offset]() -> size_t {
        size_t value = 0;
        std::memcpy(&value, data.data() + offset, sizeof(size_t));
        offset += sizeof(size_t);
        return value;
    };

    auto read_double = [&data, &offset]() -> double {
        double value = 0.0;
        std::memcpy(&value, data.data() + offset, sizeof(double));
        offset += sizeof(double);
        return value;
    };

    std::function<Value()> read_value = [&]() -> Value {
        uint8_t type = read_u8();
        switch (type) {
            case 0:
                return Null{};
            case 1: {
                uint8_t b = read_u8();
                return Boolean{b != 0};
            }
            case 2:
                return Number{read_double()};
            case 3: {
                size_t size = read_size();
                std::string value;
                value.resize(size);
                std::memcpy(value.data(), data.data() + offset, size);
                offset += size;
                return String{std::move(value)};
            }
            case 4: {
                size_t size = read_size();
                Array array{};
                array.items.reserve(size);
                for (size_t i = 0; i < size; ++i) {
                    array.items.push_back(read_value());
                }
                return array;
            }
            case 5: {
                size_t size = read_size();
                Object object{};
                for (size_t i = 0; i < size; ++i) {
                    size_t key_size = read_size();
                    std::string key;
                    key.resize(key_size);
                    std::memcpy(key.data(), data.data() + offset, key_size);
                    offset += key_size;
                    object.items[std::move(key)] = read_value();
                }
                return object;
            }
            default:
                return Null{};
        }
    };

    return read_value();
}

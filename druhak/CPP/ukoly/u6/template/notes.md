# Function-by-function notes

This file describes the helper functions and the public functions I implemented in tasks.cpp.

## Streaming JSON parser helpers

### skip_ws

Skips spaces and newlines by peeking and consuming whitespace characters. This keeps parsing streaming and avoids reading ahead.

### consume_literal

Consumes a fixed literal string (like "null" or "true") from the stream. Returns false if any character does not match.

### parse_string_value

Parses a JSON string starting with a double quote. Accepts only ASCII and allows escaping of backslash (\\) and quote (\"). Any other escape sequence is an error. Stops at the closing quote.

### parse_number_value

Parses a number manually without std::stod. Supports optional leading minus and an optional fractional part. A leading dot is rejected, and parsing stops when a non-digit (or non-digit after a dot) is encountered.

### parse_array_value

Parses an array starting with '['. It reads items one by one using parse_value, then expects either a comma or a closing bracket. Rejects extra commas or missing values.

### parse_object_value

Parses an object starting with '{'. It expects a string key, a colon, and a JSON value. After each pair, it expects either a comma or a closing brace. Rejects non-string keys and malformed separators.

### parse_value

Dispatches to the correct parser based on the next character (peek). This keeps parsing streaming-safe: it never consumes characters for a type it does not accept.

## Public parser entry

### parse_json

Entry point that parses a single JSON value from the stream by calling parse_value. It only consumes a prefix of the stream, leaving any remainder untouched.

## JSON printing

### operator<< (Value)

Prints JSON values using std::visit. Strings and object keys are escaped for backslashes and quotes to match JSON output requirements. Arrays and objects are printed with commas and spaces between elements.

## Binary serialization and deserialization

### serialize

Writes a JSON value to a byte vector. The first byte is a type ID, followed by the type-specific payload:

- Null: only the type ID.
- Boolean: type ID + one byte (0 or 1).
- Number: type ID + 8 bytes of double.
- String: type ID + size_t length + raw string bytes.
- Array: type ID + size_t length + serialized items.
- Object: type ID + size_t length + (key length + key bytes + value) pairs in std::map order.

### deserialize

Reconstructs a JSON value from a byte vector. Reads the type ID first, then reads the corresponding payload and builds nested Values recursively.

## Line-by-line walkthroughs

Below are the key functions with the exact code I wrote, followed by a line-by-line explanation.

### skip_ws

```cpp
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
```

- Line 1: Define a helper that takes a stream by reference.
- Line 2: Loop until we hit a non-space or EOF.
- Line 3: Peek the next character without consuming it.
- Line 4-6: If EOF, there is nothing more to skip.
- Line 7-9: If the next char is not whitespace, stop.
- Line 10: Consume the whitespace character and continue.

### consume_literal

```cpp
bool consume_literal(std::istream& is, const char* literal) {
	for (size_t i = 0; literal[i] != '\0'; ++i) {
		int ch = is.get();
		if (ch == EOF || static_cast<char>(ch) != literal[i]) {
			return false;
		}
	}
	return true;
}
```

- Line 1: Helper that matches an exact C string.
- Line 2: Iterate through all characters of the literal.
- Line 3: Consume one character from the stream.
- Line 4-6: If EOF or mismatch, signal failure.
- Line 7: If all chars match, signal success.

### parse_string_value

```cpp
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
```

- Line 1: Parse a JSON string and return String or nullopt.
- Line 2: Consume the opening quote.
- Line 3-5: If not a quote, this is not a string.
- Line 7: Accumulate characters into out.
- Line 8: Loop until closing quote or error.
- Line 9-11: EOF inside a string is an error.
- Line 12-14: Closing quote ends the string.
- Line 15-25: Handle escape sequences for \\ and \" only.
- Line 26: Push a normal character into out.

### parse_number_value

```cpp
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
```

- Line 1: Parse a number and return Number or nullopt.
- Line 2-5: If there is no character, fail.
- Line 7-15: Handle optional leading minus and ensure a digit follows.
- Line 17-19: Reject if the next char is not a digit.
- Line 21-29: Build the integer part by multiplying by 10 and adding digits.
- Line 31-35: If a dot is present, mark that we saw a fractional part.
- Line 37-46: Build the fractional part using decreasing factors (0.1, 0.01, ...).
- Line 48-51: Apply sign and return the Number.

### parse_array_value

```cpp
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
```

- Line 1-5: Consume '[' and fail if it is missing.
- Line 7-8: Create an empty array and skip spaces.
- Line 10-13: If the next char is ']', this is an empty array.
- Line 15-20: Parse one value; if it fails, the array is invalid.
- Line 21: Store the parsed item.
- Line 23-26: If there is a comma, consume it and parse another item.
- Line 27-31: If there is a closing ']', finish successfully.
- Line 32: Any other character is invalid (missing comma or closing bracket).

### parse_object_value

```cpp
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
```

- Line 1-5: Consume '{' and fail if it is missing.
- Line 7-8: Create an empty object and skip spaces.
- Line 10-13: If the next char is '}', this is an empty object.
- Line 15-19: Keys must start with a quote; otherwise fail.
- Line 20-23: Parse the key string.
- Line 25-28: Expect a colon after optional spaces.
- Line 30-34: Parse the value and store it under the key.
- Line 36-40: If there is a comma, consume it and parse another pair.
- Line 41-45: If there is a closing '}', finish successfully.
- Line 46: Any other character is invalid.

### parse_value

```cpp
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
```

- Line 1: Skip leading whitespace.
- Line 2-5: If there is no input, fail.
- Line 7-14: Match "null" and return Null.
- Line 15-22: Match "true" and return Boolean(true).
- Line 23-30: Match "false" and return Boolean(false).
- Line 31-37: Parse a string when the next char is a quote.
- Line 38-44: Parse an array when the next char is '['.
- Line 45-51: Parse an object when the next char is '{'.
- Line 52-58: Parse a number when the next char is '-' or digit.
- Line 59: Any other character is invalid.

### parse_json

```cpp
std::optional<Value> parse_json(std::istream& is) {
	return parse_value(is);
}
```

- Line 1: Entry point for parsing.
- Line 2: Delegate to parse_value and return its result.

### operator== implementations

```cpp
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
```

- Each operator compares the underlying fields for equality.
- Null always compares equal to Null, so it returns true unconditionally.

### operator<< (Value)

```cpp
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
```

- Line 1: Overload stream output for Value.
- Line 2-12: Define a helper that escapes \\ and \" inside JSON strings.
- Line 14: Use std::visit to pattern-match on the variant type.
- Line 15-21: Print booleans as true/false.
- Line 22-24: Print numbers using the stream default.
- Line 25-29: Print strings with quotes and escaped contents.
- Line 30-32: Print null.
- Line 33-42: Print arrays with commas and spaces between items.
- Line 43-55: Print objects with escaped keys and JSON values.
- Line 56-57: Return the stream.

### serialize

```cpp
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
```

- Line 1-2: Create the output byte buffer.
- Line 4-6: Helper to append one byte.
- Line 8-12: Helper to append a size_t in native byte order.
- Line 14-18: Helper to append a double in native byte order.
- Line 20-47: Recursive writer that emits type ID and payload for each Value.
- Line 49: Start the serialization from the root value.
- Line 50: Return the accumulated bytes.

### deserialize

```cpp
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
```

- Line 1-2: Track a byte offset into the data buffer.
- Line 4-8: Helper to read a single byte.
- Line 10-14: Helper to read a size_t.
- Line 16-20: Helper to read a double.
- Line 22-55: Recursive reader that reconstructs each Value by type ID.
- Line 57: Deserialize the root value and return it.

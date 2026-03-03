#include <functional>
#include <regex>

using Parser = std::function<const char*(const char*)>;

auto create_char_parser(const char c) -> Parser;

auto regex_match(const char* pattern) -> Parser;

auto skip_ws() -> Parser;

auto any_of(const std::vector<Parser>& parsers) -> Parser;

auto sequence(const std::vector<Parser>& parsers) -> Parser;

auto repeat(const Parser& parser, int repeats) -> Parser;

auto create_word_parser(const char* word) -> Parser;
#include "tasks.h"

// TODO: implement functions from tasks.h


auto create_char_parser(const char c) -> Parser {
  return [c](const char* str) -> const char* {
    if (*str == c) {
      return str + 1;
    } else {
      return nullptr;
    }
  };
}


auto regex_match(const char* pattern) -> Parser {
  return [re = std::regex(pattern)](const char* str) -> const char* {
    std::cmatch match;
    if (std::regex_search(str, match, re) && match.position(0) == 0) {
      return str + match.length(0);
    }
    return nullptr;
  };
}

auto skip_ws() -> Parser {
  return [](const char* str) -> const char* {
    while (*str == ' ' || *str == '\t' || *str == '\n' || *str == '\r') {
      str++;
    }
    return str;
  };
}


auto any_of(const std::vector<Parser>& parsers) -> Parser {
  return [parsers](const char* str) -> const char* {
    for (const auto& parser : parsers) {
      if (const char* result = parser(str)) {
        return result;
      }
    }
    return nullptr;
  };
}


auto sequence(const std::vector<Parser>& parsers) -> Parser {
  return [parsers](const char* str) -> const char* {
    const char* current = str;
    for(const auto& parser : parsers) {
      if(const char* result = parser(current)) {
        current = result;
      } else {
        return nullptr;
      }
    }
    return current;
  };
}


auto repeat(const Parser& parser, int repeats) -> Parser {
  return [parser, repeats](const char* str) -> const char* {
    const char* current = str;
    for (int i = 0; i < repeats; ++i) {
      if (const char* result = parser(current)) {
        current = result;
      } else {
        return nullptr;
      }
    }
    return current;
  };
}


auto create_word_parser(const char* word) -> Parser {
  return [word](const char* str) -> const char* {
    const char* w = word;
    const char* current = str;
    while (*w) {
      if (*current == *w) {
        current++;
        w++;
      } else {
        return nullptr;
      }
    }
    return current;
  };
}
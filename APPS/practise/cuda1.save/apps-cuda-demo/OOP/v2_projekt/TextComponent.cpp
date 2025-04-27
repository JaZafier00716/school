#include "TextComponent.h"

TextComponent::TextComponent(
  const string& tag, 
  const vector<ComponentAttribute>& attributes, 
  const string& text,
  Color textColor) : UIComponent(tag, attributes), text(text), textColor(textColor) {}

TextComponent::~TextComponent()
{
  cout << "TextComponent descructor" << endl;
}

void TextComponent::setText(const string& text) {
  this->text = text;
}

void TextComponent::setText(const char* text) {
  this->text = string(text);
}

string TextComponent::getText() const {
  return this->text;
}

void TextComponent::setTextColor(const Color textColor) {
  this->textColor = textColor;
}

void TextComponent::render() const {
  this->printTagAttrib(false);

  cout << getTextAnsiCode(this->textColor) << this->text << getTextAnsiCode(Color::DEFAULT) << endl;

  this->printTagAttrib(true);
}

/**
 * @brief Returns the ANSI escape code corresponding to a Color.
 * @param color Color enum value
 * @return ANSI escape sequence as std::string
 */
string TextComponent::getTextAnsiCode(Color color)
{
  switch (color)
  {
  case Color ::DEFAULT:
    return "\033[0m";
  case Color ::BLACK:
    return "\033[30m";
  case Color ::RED:
    return "\033[31m";
  case Color ::GREEN:
    return "\033[32m";
  case Color ::YELLOW:
    return "\033[33m";
  case Color ::BLUE:
    return "\033[34m";
  case Color ::MAGENTA:
    return "\033[35m";
  case Color ::CYAN:
    return "\033[36m";
  case Color ::WHITE:
    return "\033[37m";
  case Color ::BRIGHT_BLACK:
    return "\033[90m";
  case Color ::BRIGHT_RED:
    return "\033[91m";
  case Color ::BRIGHT_GREEN:
    return "\033[92m";
  case Color ::BRIGHT_YELLOW:
    return "\033[93m";
  case Color ::BRIGHT_BLUE:
    return "\033[94m";
  case Color ::BRIGHT_MAGENTA:
    return "\033[95m";
  case Color ::BRIGHT_CYAN:
    return "\033[96m";
  case Color ::BRIGHT_WHITE:
    return "\033[97m";
  default:
    return "\033[0m";
  }
}
#include "TextComponent.h"

/**
 * @brief Constructs a TextComponent with optional tag, attributes, text content, and text color.
 *
 * @param tag The tag name of the component (default is empty).
 * @param attributes The list of attributes for the component (default is empty).
 * @param text The text content to display (default is empty).
 * @param textColor The color of the text (default is DEFAULT).
 */
TextComponent::TextComponent(
    const string &tag,
    const vector<ComponentAttribute> &attributes,
    const string &text,
    Color textColor) : UIComponent(tag, attributes), text(text), textColor(textColor) {}

/**
 * @brief Virtual destructor.
 */
TextComponent::~TextComponent()
{
  cout << "TextComponent descructor" << endl;
}

/**
 * @brief Sets the text content of the component.
 *
 * @param text The text to set.
 */
void TextComponent::setText(const string &text)
{
  this->text = text;
}

/**
 * @brief Sets the text content using a C-style string.
 *
 * @param text The C-style string to set as text.
 */
void TextComponent::setText(const char *text)
{
  this->text = string(text);
}

/**
 * @brief Returns the current text content.
 *
 * @return The text content as a string.
 */
string TextComponent::getText() const
{
  return this->text;
}

/**
 * @brief Sets the color of the text.
 *
 * @param textColor The color to apply to the text.
 */
void TextComponent::setTextColor(const Color textColor)
{
  this->textColor = textColor;
}

/**
 * @brief Renders the text component to the terminal.
 */
void TextComponent::render() const
{
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
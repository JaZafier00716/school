#include "ButtonComponent.h"

/**
 * @brief Constructs a ButtonComponent with optional attributes, text, background color, and text color.
 *
 * @param attributes The list of attributes for the button (default is empty).
 * @param text The text displayed on the button (default is "button").
 * @param backgroundColor The background color of the button (default is DEFAULT).
 * @param textColor The text color of the button (default is DEFAULT).
 */
ButtonComponent::ButtonComponent(
    const vector<ComponentAttribute> &attributes,
    const string &text,
    Color backgroundColor,
    Color textColor) : UIComponent("button", attributes)
{
  this->text = text;
  this->textColor = textColor;
  this->backgroundColor = backgroundColor;

  this->addAttribute("onClick", "");
  this->addAttribute("type", "button");
}

/**
 * @brief Virtual destructor.
 */
ButtonComponent::~ButtonComponent()
{
  cout << "ButtonComponent destructor" << endl;
}

/**
 * @brief Returns the ANSI escape code for a given background color.
 *
 * @param color The background color.
 * @return The corresponding ANSI escape sequence as a string.
 */
string ButtonComponent::getBgAnsiCode(Color color)
{
  switch (color)
  {
  case Color ::DEFAULT:
    return "\033[49m";
  case Color ::BLACK:
    return "\033[40m";
  case Color ::RED:
    return "\033[41m";
  case Color ::GREEN:
    return "\033[42m";
  case Color ::YELLOW:
    return "\033[43m";
  case Color ::BLUE:
    return "\033[44m";
  case Color ::MAGENTA:
    return "\033[45m";
  case Color ::CYAN:
    return "\033[46m";
  case Color ::WHITE:
    return "\033[47m";
  case Color ::BRIGHT_BLACK:
    return "\033[100m";
  case Color ::BRIGHT_RED:
    return "\033[101m";
  case Color ::BRIGHT_GREEN:
    return "\033[102m";
  case Color ::BRIGHT_YELLOW:
    return "\033[103m";
  case Color ::BRIGHT_BLUE:
    return "\033[104m";
  case Color ::BRIGHT_MAGENTA:
    return "\033[105m";
  case Color ::BRIGHT_CYAN:
    return "\033[106m";
  case Color ::BRIGHT_WHITE:
    return "\033[107m";
  default:
    return "\033[49m";
  }
}

/**
 * @brief Renders the button component to the terminal.
 */
void ButtonComponent::render() const
{
  this->printTagAttrib(false);

  cout << ButtonComponent::getBgAnsiCode(this->backgroundColor)
       << TextComponent::getTextAnsiCode(this->textColor)
       << "  " << this->text << "  "
       << ButtonComponent::getBgAnsiCode(Color::DEFAULT)
       << TextComponent::getTextAnsiCode(Color::DEFAULT)
       << endl;

  this->printTagAttrib(true);
}
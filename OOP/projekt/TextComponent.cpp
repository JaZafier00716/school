#include "TextComponent.h"

/**
 * @brief Constructs a TextComponent with position, size, text, and color.
 *
 * @param general_params The general parameter for the size and position of this component
 * @param text Text to display (optional).
 * @param color Color of the text (optional, defaults to TextColor::DEFAULT).
 */
TextComponent::TextComponent(
  UIComponentVariables general_params, 
  const string &text, 
  TextColor color
)
  : UIComponent(general_params), text(text), color(color) {}

/**
 * @brief Renders the text component to the terminal.
 *
 * Overrides the pure virtual render() method from UIComponent.
 */
void TextComponent::render() const
{
  cout << "<Text id={" << this->id << "} width={" << this->vars.w << "} height={" << this->vars.h << "} pos_x={" << this->vars.x << "} pos_y={" << this->vars.y << "}>" << endl;
  cout << UIComponent::getAnsiCode(this->color) << this->text << UIComponent::getAnsiCode(TextColor::DEFAULT) << endl;
  cout << "</Text>" << endl;
}

/**
 * @brief Sets the text of the component.
 * @param text The new text as a std::string.
 */
void TextComponent::setText(string text)
{
  this->text = text;
}

/**
 * @brief Sets the text of the component.
 * @param text The new text as a C-style string.
 */
void TextComponent::setText(char *text)
{
  this->text = string(text);
}
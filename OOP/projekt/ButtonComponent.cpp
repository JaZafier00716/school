#include "ButtonComponent.h"

/**
 * @brief Constructs a ButtonComponent with position, size, label, and color.
 *
 * @param general_params The general parameter for the size and position of this component
 * @param label The text displayed on the button (optional).
 * @param color Color of the label text (optional, defaults to TextColor::DEFAULT).
 */
ButtonComponent::ButtonComponent(
  UIComponentVariables general_params, 
  const string label, 
  TextColor color)
    : UIComponent(general_params), label(label), color(color) {}

void ButtonComponent::onClick()
{
  cout << "Button with id=" << this->id << " was clicked" << endl;
}

void ButtonComponent::render() const
{
  cout << "<Button id={" << this->id << "} width={" << this->vars.w << "} height={" << this->vars.h << "} pos_x={" << this->vars.x << "} pos_y={" << this->vars.y << "}>" << endl;
  cout << UIComponent::getAnsiCode(this->color) << this->label << UIComponent::getAnsiCode(TextColor::DEFAULT) << endl;
  cout << "</Button>" << endl;
}

/**
 * @brief Sets the label of the component.
 * @param label The new label as a std::string.
 */
void ButtonComponent::setLabel(string label)
{
  this->label = label;
}

/**
 * @brief Sets the label of the component.
 * @param label The new label as a C-style string.
 */
void ButtonComponent::setLabel(char *label)
{
  this->label = label;
}
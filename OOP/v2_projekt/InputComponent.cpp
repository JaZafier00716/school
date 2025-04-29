#include "InputComponent.h"

/**
 * @brief Constructs an InputComponent with optional attributes and color settings.
 *
 * Initializes default attributes: `type`, `value`, `placeholder`, `id`, `name`, `onClick`, and `active`.
 *
 * @param attributes List of component attributes (e.g., name, placeholder, value).
 * @param valueColor The color used to display the input value (default is DEFAULT).
 * @param placeholderColor The color used to display the placeholder text (default is DEFAULT).
 */
InputComponent::InputComponent(
    const vector<ComponentAttribute> &attributes,
    Color valueColor,
    Color placeholderColor) : UIComponent("input", attributes)
{
  this->valueColor = valueColor;
  this->placeholderColor = placeholderColor;

  this->addAttribute("type", "text");
  this->addAttribute("value", "");
  this->addAttribute("placeholder", "");
  this->addAttribute("id", "");
  this->addAttribute("name", "");
  this->addAttribute("onClick", "");
  this->addAttribute("active", "false");
}

/**
 * @brief Destructor that logs destruction of the component.
 */
InputComponent::~InputComponent()
{
  cout << "InputComponent destructor" << endl;
}

/**
 * @brief Renders the input component with value and placeholder color formatting.
 */
void InputComponent::render() const
{
  this->printTagAttrib(false);

  cout << TextComponent::getTextAnsiCode(this->placeholderColor) << this->getAttributeValue("placeholder") << endl;
  cout << TextComponent::getTextAnsiCode(this->valueColor) << this->getAttributeValue("value") << TextComponent::getTextAnsiCode(Color::DEFAULT) << endl;

  this->printTagAttrib(true);
}

/**
 * @brief Sets the value of the input field.
 *
 * Updates or adds the `value` attribute.
 *
 * @param value The new input value.
 */
void InputComponent::setValue(string value)
{
  this->editAttribute("value", value);
}

/**
 * @brief Sets the color used for rendering the input's value text.
 *
 * @param valueColor The new text color.
 */
void InputComponent::setValueColor(const Color valueColor)
{
  this->valueColor = valueColor;
}

/**
 * @brief Sets the color used for rendering the placeholder text.
 *
 * @param placeholderColor The new placeholder color.
 */
void InputComponent::setPlaceholderColor(const Color placeholderColor)
{
  this->placeholderColor = placeholderColor;
}

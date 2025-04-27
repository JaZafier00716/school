#include "InputComponent.h"


InputComponent::InputComponent(
    const vector<ComponentAttribute>& attributes,
    Color valueColor,
    Color placeholderColor) : UIComponent("input", attributes)
{
  this->valueColor = valueColor;
  this->placeholderColor = placeholderColor;

  /**
   * @brief Default attributes
   */
  this->addAttribute("type", "text");
  this->addAttribute("value", "");
  this->addAttribute("placeholder", "");
  this->addAttribute("id", "");
  this->addAttribute("name", "");
  this->addAttribute("onClick", "");
  this->addAttribute("active", "false");
}

InputComponent::~InputComponent()
{
  cout << "InputComponent destructor" << endl;
}

void InputComponent::render() const
{
  this->printTagAttrib(false);

  cout << TextComponent::getTextAnsiCode(this->placeholderColor) << this->getAttributeValue("placeholder") << endl;
  cout << TextComponent::getTextAnsiCode(this->valueColor) << this->getAttributeValue("value") << TextComponent::getTextAnsiCode(Color::DEFAULT) << endl;

  this->printTagAttrib(true);
}

void InputComponent::setValue(string value) {
  this->editAttribute("value", value);
}
void InputComponent::setValueColor(const Color valueColor)
{
  this->valueColor = valueColor;
}
void InputComponent::setPlaceholderColor(const Color placeholderColor)
{
  this->placeholderColor = placeholderColor;
}

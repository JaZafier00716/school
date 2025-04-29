#pragma once
#include "UIComponent.h"
#include "TextComponent.h"

/**
 * @class InputComponent
 * @brief A UI component representing a text input field with customizable value and placeholder styling.
 */
class InputComponent : public UIComponent
{
private:
  Color valueColor;         /**< Color of the input value text. */
  Color placeholderColor;   /**< Color of the placeholder text. */

public:
  /**
   * @brief Constructs an InputComponent with optional attributes and color settings.
   * 
   * Initializes default attributes: `type`, `value`, `placeholder`, `id`, `name`, `onClick`, and `active`.
   * 
   * @param attributes List of component attributes (e.g., name, placeholder, value).
   * @param valueColor The color used to display the input value (default is DEFAULT).
   * @param placeholderColor The color used to display the placeholder text (default is DEFAULT).
   */
  InputComponent(
      const vector<ComponentAttribute>& attributes = {},
      Color valueColor = Color::DEFAULT,
      Color placeholderColor = Color::DEFAULT);

  /**
   * @brief Destructor that logs destruction of the component.
   */
  ~InputComponent();

  /**
   * @brief Sets the value of the input field.
   * 
   * Updates or adds the `value` attribute.
   * 
   * @param value The new input value.
   */
  void setValue(string value);

  /**
   * @brief Sets the color used for rendering the input's value text.
   * 
   * @param valueColor The new text color.
   */
  void setValueColor(const Color valueColor);

  /**
   * @brief Sets the color used for rendering the placeholder text.
   * 
   * @param placeholderColor The new placeholder color.
   */
  void setPlaceholderColor(const Color placeholderColor);

  /**
   * @brief Renders the input component with value and placeholder color formatting.
   */
  void render() const override;
};

#pragma once
#include "UIComponent.h"
#include "TextComponent.h"

/**
 * @class ButtonComponent
 * @brief A UI component representing a button with customizable text, background color, and text color.
 */
class ButtonComponent : public UIComponent
{
private:
  Color textColor;       /**< Color of the button's text. */
  Color backgroundColor; /**< Background color of the button. */
  string text;           /**< Text displayed on the button. */

public:
  /**
   * @brief Constructs a ButtonComponent with optional attributes, text, background color, and text color.
   * 
   * Initializes default attributes: `onClick`, `type`.
   * 
   * @param attributes The list of attributes for the button (default is empty).
   * @param text The text displayed on the button (default is "button").
   * @param backgroundColor The background color of the button (default is DEFAULT).
   * @param textColor The text color of the button (default is DEFAULT).
   */
  ButtonComponent(
      const vector<ComponentAttribute>& attributes = {},
      const string &text = "button",
      Color backgroundColor = Color::DEFAULT,
      Color textColor = Color::DEFAULT);

  /**
   * @brief Destructor that logs destruction of the component.
   */
  ~ButtonComponent();

  /**
   * @brief Returns the ANSI escape code for a given background color.
   * 
   * @param color The background color.
   * @return The corresponding ANSI escape sequence as a string.
   */
  static string getBgAnsiCode(Color color);

  /**
   * @brief Renders the button component to the terminal.
   */
  void render() const override;
};

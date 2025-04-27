#pragma once
#include "UIComponent.h"

/**
 * @class TextComponent
 * @brief A UI component that displays styled text in the terminal.
 *
 * Inherits from UIComponent and adds text functionality to render a string of text
 * with a specific color. It supports setting the text using either `std::string`
 * or a C-style string (`char*`).
 */
class TextComponent : public UIComponent
{
private:
  string text;     /**< Text to be displayed */
  TextColor color; /**< Color of the text */
public:
  /**
   * @brief Constructs a TextComponent with position, size, text, and color.
   *
   * @param general_params The general parameter for the size and position of this component
   * @param text Text to display (optional).
   * @param color Color of the text (optional, defaults to TextColor::DEFAULT).
   */
  TextComponent(
    UIComponentVariables general_params, 
    const string &text = "", 
    TextColor color = TextColor::DEFAULT);
  ~TextComponent() override = default;

  /**
   * @brief Renders the text component to the terminal.
   *
   * Overrides the pure virtual render() method from UIComponent.
   */
  void render() const override;

  /**
   * @brief Sets the text of the component.
   * @param text The new text as a std::string.
   */
  void setText(string text);

  /**
   * @brief Sets the text of the component.
   * @param text The new text as a C-style string.
   */
  void setText(char *text);
};

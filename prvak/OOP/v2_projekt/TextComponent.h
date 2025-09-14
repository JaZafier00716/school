#pragma once
#include "UIComponent.h"

/**
 * @enum Color
 * @brief Represents ANSI color codes for terminal text formatting.
 */
enum Color
{
  DEFAULT, 
  BLACK,
  RED,
  GREEN,
  YELLOW,
  BLUE,  
  MAGENTA,
  CYAN,   
  WHITE,  
  BRIGHT_BLACK,
  BRIGHT_RED,  
  BRIGHT_GREEN,
  BRIGHT_YELLOW,
  BRIGHT_BLUE,
  BRIGHT_MAGENTA,
  BRIGHT_CYAN,   
  BRIGHT_WHITE   
};

/**
 * @class TextComponent
 * @brief A UI component representing a block of text with customizable color.
 */
class TextComponent : public UIComponent
{
private:
  string text;       /**< The text content displayed by the component. */
  Color textColor;   /**< The color of the displayed text. */

public:
  /**
   * @brief Constructs a TextComponent with optional tag, attributes, text content, and text color.
   * 
   * @param tag The tag name of the component (default is empty).
   * @param attributes The list of attributes for the component (default is empty).
   * @param text The text content to display (default is empty).
   * @param textColor The color of the text (default is DEFAULT).
   */
  TextComponent(
      const string &tag = "",
      const vector<ComponentAttribute>& attributes = {},
      const string &text = "",
      Color textColor = DEFAULT);

  /**
   * @brief Destructor that logs destruction of the component.
   */
  ~TextComponent();

  /**
   * @brief Sets the text content of the component.
   * 
   * @param text The text to set.
   */
  void setText(const string &text);

  /**
   * @brief Sets the text content using a C-style string.
   * 
   * @param text The C-style string to set as text.
   */
  void setText(const char *text);

  /**
   * @brief Returns the current text content.
   * 
   * @return The text content as a string.
   */
  string getText() const;

  /**
   * @brief Sets the color of the text.
   * 
   * @param textColor The color to apply to the text.
   */
  void setTextColor(const Color textColor);

  /**
   * @brief Renders the text component to the terminal.
   */
  void render() const override;

  /**
   * @brief Returns the ANSI escape code corresponding to a Color.
   * 
   * @param color Color enum value.
   * @return ANSI escape sequence as a string.
   */
  static string getTextAnsiCode(Color color);
};

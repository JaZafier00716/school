#pragma once
#include "UIComponent.h"

/**
 * @enum TextColor
 * @brief Represents ANSI color codes for terminal text formatting.
 */
enum Color
{
  DEFAULT,        /**< Default terminal text color */
  BLACK,          /**< Black text */
  RED,            /**< Red text */
  GREEN,          /**< Green text */
  YELLOW,         /**< Yellow text */
  BLUE,           /**< Blue text */
  MAGENTA,        /**< Magenta text */
  CYAN,           /**< Cyan text */
  WHITE,          /**< White text */
  BRIGHT_BLACK,   /**< Bright black (gray) text */
  BRIGHT_RED,     /**< Bright red text */
  BRIGHT_GREEN,   /**< Bright green text */
  BRIGHT_YELLOW,  /**< Bright yellow text */
  BRIGHT_BLUE,    /**< Bright blue text */
  BRIGHT_MAGENTA, /**< Bright magenta text */
  BRIGHT_CYAN,    /**< Bright cyan text */
  BRIGHT_WHITE    /**< Bright white text */
};

class TextComponent : UIComponent
{
private:
  string text;
  Color textColor;

public:
  TextComponent(
      const string &tag = "",
      const vector<ComponentAttribute>& attributes = {},
      const string &text = "",
      Color textColor = DEFAULT);
  ~TextComponent();

  void setText(const string &text);
  void setText(const char *text);
  string getText() const;
  void setTextColor(const Color textColor);

  void render() const override;

  /**
   * @brief Returns the ANSI escape code corresponding to a TextColor.
   * @param color Color enum value
   * @return ANSI escape sequence as std::string
   */
  static string getTextAnsiCode(Color color);
};

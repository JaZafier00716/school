#pragma once
#include <iostream>
#include <string>
using std::string, std::cout, std::cin, std::endl;

/**
 * @enum TextColor
 * @brief Represents ANSI color codes for terminal text formatting.
 */
enum TextColor
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

typedef struct {
  double x; /** X coordinate */
  double y; /** Y coordinate */
  double w; /** Width of the component */
  double h; /** Height of the component */
}UIComponentVariables;

/**
 * @class UIComponent
 * @brief Abstract base class representing a generic UI component with position and size.
 */
class UIComponent
{
  private:
  static int componentCount; /**< Total number of created UI components */
  protected:
  UIComponentVariables vars; /** structure containing all variables */
  unsigned int id;  /** id of component */
public:
  /**
   * @brief Constructs a UIComponent with specified position and size. - used only by children
   * @param general_params The general parameter for the size and position of this component
   */
  UIComponent(UIComponentVariables parameters);

  virtual ~UIComponent() = default;

  /**
   * @brief Purely virtual method to render the component or in this case, to print it in terminal...
   */
  virtual void render() const = 0;

  /**
   * @brief Returns the ANSI escape code corresponding to a TextColor.
   * @param color TextColor enum value
   * @return ANSI escape sequence as std::string
   */
  static string getAnsiCode(TextColor color);

  virtual unsigned int getID() const;
  virtual double getX() const;
  virtual double getY() const;
  virtual double getW() const;
  virtual double getH() const;
  virtual UIComponentVariables getVars() const;
};
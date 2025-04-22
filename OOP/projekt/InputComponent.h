#pragma once
#include "UIComponent.h"
#include <variant>
using std::variant, std::holds_alternative, std::get;

/**
 * @enum ValueType
 * @brief Enum to define different input types for the InputComponent.
 */
enum ValueType
{
  undefined = -1, /** < Undefined input type */
  text = 0,       /** < Text input type */
  number = 1,     /** < Number input type */
  email = 2,      /** < Email input type */
  phone = 3       /** < Phone input type */
};

/**
 * @class InputComponent
 * @brief A UI component that displays input field in the terminal, that handles text, number, email or phone inputs.
 *
 * Inherits from UIComponent and adds input functionality allow user to input values with one of the types
 * defined in ValueType
 */
class InputComponent : public UIComponent
{
private:
  string placeholder;                          /** Placeholder text for the input field */
  ValueType type;                              /** Type of the input (text, number, etc.) */
  variant<string, double, unsigned int> value; /** The value entered in the input */
  TextColor pcolor;                            /** Text color for the placeholder */
  TextColor vcolor;                            /** Text color for the value */
  bool active;                                 /** The active state of the input component (active = user clicked on the input field and can enter text) */

public:
  /**
   * @brief Constructor for the InputComponent class.
   *
   * @param general_params The general parameter for the size and position of this component
   * @param placeholder The placeholder text for the input (default is an empty string)
   * @param pcolor The color of the placeholder text (default is TextColor::DEFAULT)
   * @param vcolor The color of the value text (default is TextColor::DEFAULT)
   * @param value The initial value of the input (default is an empty string)
   * @param type The type of the input (default is ValueType::undefined)
   * @param active The initial active state of the component (default is false)
   */
  InputComponent(
      UIComponentVariables general_params,
      const string &placeholder = "",
      TextColor pcolor = TextColor::DEFAULT,
      TextColor vcolor = TextColor::DEFAULT,
      variant<string, double, unsigned int> value = "",
      ValueType type = undefined,
      bool active = false);
  ~InputComponent() override = default;

  /**
   * @brief Renders the input component on the screen.
   */
  void render() const override;

  /**
   * @brief Handles the input process, allowing the user to enter a value.
   */
  void inputValue();

  /**
   * @brief Toggles the active state of the input component.
   * If the component is active, it proceeds to handle input value.
   */
  void onClick();
};

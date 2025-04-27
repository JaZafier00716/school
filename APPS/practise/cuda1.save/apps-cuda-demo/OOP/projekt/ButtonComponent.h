#pragma once
#include "UIComponent.h"

/**
 * @class ButtonComponent
 * @brief  A clickable UI component that displays styled text in the terminal.
 *
 * Inherits from UIComponent. The ButtonComponent displays a label in a given color
 * and provides a simple onClick handler that simulates interaction by printing to the console.
 */
class ButtonComponent : public UIComponent
{
private:
  string label;    /** Label displayed on the button */
  TextColor color; /**< Color of the label text */
public:
  /**
   * @brief Constructs a ButtonComponent with position, size, label, and color.
   *
   * @param general_params The general parameter for the size and position of this component
   * @param label The text displayed on the button (optional).
   * @param color Color of the label text (optional, defaults to TextColor::DEFAULT).
   */
  ButtonComponent(
    UIComponentVariables general_params, 
    const string label = "", 
    TextColor color = TextColor::DEFAULT
  );
  ~ButtonComponent() override = default;

  /**
   * @brief Simulates a button click.
   *
   * Prints a message to the console indicating the button was clicked.
   */
  void onClick();

  /**
   * @brief Renders the button to the terminal.
   *
   * Displays the button's dimensions, position, and label with appropriate color formatting.
   */
  void render() const override;

  /**
   * @brief Sets the label of the component.
   * @param label The new label as a std::string.
   */
  void setLabel(string label);

  /**
   * @brief Sets the label of the component.
   * @param label The new label as a C-style string.
   */
  void setLabel(char* label);
};

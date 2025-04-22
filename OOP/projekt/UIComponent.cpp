#include "UIComponent.h"

int UIComponent::componentCount = 0; /**< Total number of created UI components */

/**
 * @brief Constructs a UIComponent with specified position and size. - used only by children
 * @param general_params The general parameter for the size and position of this component

 */
UIComponent::UIComponent(UIComponentVariables parameters)
    : vars(parameters)
{
  this->id = componentCount;
  componentCount++;
}

/**
 * @brief Returns the ANSI escape code corresponding to a TextColor.
 * @param color TextColor enum value
 * @return ANSI escape sequence as std::string
 */
string UIComponent::getAnsiCode(TextColor color)
{
  switch (color)
  {
  case TextColor::DEFAULT:
    return "\033[0m";
  case TextColor::BLACK:
    return "\033[30m";
  case TextColor::RED:
    return "\033[31m";
  case TextColor::GREEN:
    return "\033[32m";
  case TextColor::YELLOW:
    return "\033[33m";
  case TextColor::BLUE:
    return "\033[34m";
  case TextColor::MAGENTA:
    return "\033[35m";
  case TextColor::CYAN:
    return "\033[36m";
  case TextColor::WHITE:
    return "\033[37m";
  case TextColor::BRIGHT_BLACK:
    return "\033[90m";
  case TextColor::BRIGHT_RED:
    return "\033[91m";
  case TextColor::BRIGHT_GREEN:
    return "\033[92m";
  case TextColor::BRIGHT_YELLOW:
    return "\033[93m";
  case TextColor::BRIGHT_BLUE:
    return "\033[94m";
  case TextColor::BRIGHT_MAGENTA:
    return "\033[95m";
  case TextColor::BRIGHT_CYAN:
    return "\033[96m";
  case TextColor::BRIGHT_WHITE:
    return "\033[97m";
  default:
    return "\033[0m";
  }
}

unsigned int UIComponent::getID() const {
  return this->id;
}

double UIComponent::getX() const {
  return this->vars.x;
}

double UIComponent::getY() const {
  return this->vars.y;
}

double UIComponent::getW() const {
  return this->vars.w;
}

double UIComponent::getH() const {
  return this->vars.h;
}

UIComponentVariables UIComponent::getVars() const {
  return this->vars;
}
#pragma once
#include "UIComponent.h"
#include "TextComponent.h"

class InputComponent : public UIComponent
{
private:
  Color valueColor;
  Color placeholderColor;

public:
  InputComponent(
      const vector<ComponentAttribute>& attributes = {},
      Color valueColor = Color::DEFAULT,
      Color placeholderColor = Color::DEFAULT);
  ~InputComponent();

  void setValue(string value);
  void setValueColor(const Color valueColor);
  void setPlaceholderColor(const Color placeholderColor);
  void render() const override;
};
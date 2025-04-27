#pragma once
#include "UIComponent.h"
#include "TextComponent.h"


class ButtonComponent : public UIComponent
{
private:
  Color textColor;
  Color backgroundColor;
  string text;
public:
  ButtonComponent(
      const vector<ComponentAttribute>& attributes = {},
      const string &text = "button",
      Color backgroundColor = Color::DEFAULT,
      Color textColor = Color::DEFAULT);
  ~ButtonComponent();

  static string getBgAnsiCode(Color color);

  void render() const override;
};

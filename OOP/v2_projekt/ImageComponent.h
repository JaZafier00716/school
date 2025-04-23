#pragma once
#include "UIComponent.h"

class ImageComponent : public UIComponent
{
private:
  
public:
  ImageComponent(
    const vector<ComponentAttribute>& attributes = {}
  );
  ~ImageComponent();

  void render() const override;
};

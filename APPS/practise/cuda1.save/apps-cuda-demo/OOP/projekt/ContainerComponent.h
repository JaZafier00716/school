#pragma once
#include "UIComponent.h"
#include <vector>
#include <memory>
using std::vector, std::unique_ptr, std::move;

class ContainerComponent : public UIComponent
{
private:
  vector<unique_ptr<UIComponent>> children;
public:
  ContainerComponent(UIComponentVariables general_params, vector<unique_ptr<UIComponent>> children = {});
  ~ContainerComponent() override = default;

  void addChild(unique_ptr<UIComponent> child);
  void removeChild(unsigned int child_id);
  void render() const override;
  const vector<unique_ptr<UIComponent>>& getChildren() const;
};


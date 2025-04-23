#pragma once
#include "UIComponent.h"

class ContainerComponent : public UIComponent
{
private:
  vector<UIComponent*> children;
public:
  ContainerComponent(
    const string& tag = "", 
    const vector<ComponentAttribute>& attributes = {},
    const vector<UIComponent*>& children = {}
  );
  ~ContainerComponent();



  /// @brief function finds child component based on it's index
  /// @param childIndex index of child component
  /// @return returns child component when successful or component with tag "not found" when failed
  UIComponent* getChild(unsigned int childIndex);

  void addChild(UIComponent* child, unsigned int containerIndex);
  void addChild(UIComponent* child);

  bool removeChild(unsigned int childIndex);


  void render() const override;
};

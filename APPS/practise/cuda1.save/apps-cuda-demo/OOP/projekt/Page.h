#pragma once
#include "ContainerComponent.h"
using std::make_unique;


class Page
{
private:
  static ContainerComponent root;
public:
  static void render();
  static void addComponent(unique_ptr<UIComponent> component);
  static void removeComponent(unsigned int child_id);
  static void createDefaultPage();
};


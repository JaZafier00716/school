#pragma once
#include "ContainerComponent.h"
#include "TextComponent.h"
#include "ButtonComponent.h"
#include "InputComponent.h"
#include "ImageComponent.h"
using std::cin;

enum ComponentType {
  Undefined,  // for adding children - in this case, function asks later to specify the type
  Container,
  Text,
  Button,
  Input,
  Image
};

class Page
{
private:
  static vector<unsigned int> freeIDs;
  static unsigned int maxID;
  static ContainerComponent pageContainer;

  static Color getColor(bool text);
public:
  static unsigned int getNextID();

  static unsigned int addComponent(UIComponent *component);

  // returnes id if successful, otherwise returns -1
  static int addComponent(UIComponent *component, unsigned int childID);

  static bool removeComponent(unsigned int index);

  static bool addAttribute(unsigned int componentIndex, const ComponentAttribute& attribute);

  static UIComponent* createComponent(ComponentType type);

  static void render();
};

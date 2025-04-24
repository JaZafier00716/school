#include "Page.h"
#include <limits>

unsigned int Page::maxID = 1;
vector<unsigned int> Page::freeIDs;
ContainerComponent Page::pageContainer("page");

unsigned int Page::getNextID()
{
  if (Page::freeIDs.empty())
  {
    return Page::maxID++;
  }
  else
  {
    unsigned int id = Page::freeIDs.back();
    freeIDs.pop_back();
    return id;
  }
}

unsigned int Page::addComponent(UIComponent *component)
{
  unsigned int id = Page::getNextID();
  component->setID(id);
  Page::pageContainer.addChild((UIComponent *)component);
  return id;
}

int Page::addComponent(UIComponent *component, unsigned int childID)
{
  unsigned int id = Page::getNextID();
  component->setID(id);
  if (Page::pageContainer.addChild((UIComponent *)component, childID))
  {
    return id;
  }
  else
  {
    return -1;
  }
}

bool Page::removeComponent(unsigned int index)
{
  if (Page::pageContainer.removeChild(index))
  {
    Page::freeIDs.push_back(index);
    return true;
  }
  else
  {
    return false;
  }
}

bool Page::addAttribute(unsigned int componentIndex, const ComponentAttribute& attribute)
{
  UIComponent *child = Page::pageContainer.getChild(componentIndex);
  if((child)->getTag() == "not found") {
    return false;
  } else {
    (child)->addAttribute(attribute);
    return true;
  }
}

Color Page::getColor(bool text)
{
  unsigned int color;
  do
  {
    cout << "0\t|\tDEFAULT" << endl;
    cout << "1\t|\tBlack" << endl;
    cout << "2\t|\tRed" << endl;
    cout << "3\t|\tGreen" << endl;
    cout << "4\t|\tYellow" << endl;
    cout << "5\t|\tBlue" << endl;
    cout << "6\t|\tMagenta" << endl;
    cout << "7\t|\tCyan" << endl;
    cout << "8\t|\tWhite" << endl;
    cout << "9\t|\tBright black" << endl;
    cout << "10\t|\tBright red" << endl;
    cout << "11\t|\tBright green" << endl;
    cout << "12\t|\tBright yellow" << endl;
    cout << "13\t|\tBright blue" << endl;
    cout << "14\t|\tBright magenta" << endl;
    cout << "15\t|\tBright cyan" << endl;
    cout << "16\t|\tBright white" << endl;
    cout << "---------------------" << endl;
    cout << "Enter " << (text ? "text" : "background") << "color:\t";
    cin >> color;
    cout << "---------------------" << endl;
  } while (color < 0 || color > 16);
  return (Color)color;
}

UIComponent *Page::createComponent(ComponentType type)
{
  UIComponent *newComponent;
  string tag;
  vector<ComponentAttribute> attributes;
  bool addAttribute;

  int ctype;
  if (type == Undefined)
  {
    do
    {
      cout << "1\t|\tAdd Container component" << endl;
      cout << "2\t|\tAdd Text component" << endl;
      cout << "3\t|\tAdd Button component" << endl;
      cout << "4\t|\tAdd Input component" << endl;
      cout << "5\t|\tAdd Image component" << endl;
      cout << "-----------------------------" << endl;
      cout << "Enter type of the new component:\t";
      cin >> ctype;
    } while (ctype > 5 && ctype < 1);
    type = (ComponentType)ctype;
  }

  if (type == Container || type == Text)
  {
    cout << "Enter component tag:\t";
    cin >> tag;
  }

  do
  {
    cout << "Add attribute? [1 - true, 0 - false]:\t";
    cin >> addAttribute;

    if (addAttribute)
    {
      string name, value;
      cout << "Enter attribute's name:\t";
      cin >> name;
      cout << "Enter attribute's value:\t";
      cin >> value;
      attributes.push_back({name, value});
    }
  } while (addAttribute);

  switch (type)
  {
  case Container:
  {
    bool Addchild;
    vector<UIComponent *> children;

    do
    {
      cout << "Add child? [1 - true, 0 - false]:\t";
      cin >> Addchild;

      if (Addchild)
      {
        UIComponent *child = Page::createComponent(Undefined);
        children.push_back(child);
      }
    } while (Addchild);
    newComponent = new ContainerComponent(tag, attributes, children);
    break;
  }
  case Text:
  {

    string text;
    cout << "Enter text:\t";
    cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
    getline(cin, text);
    cout << endl;

    Color textColor = Page::getColor(true);

    newComponent = new TextComponent(tag, attributes, text, textColor);
    break;
  }
  case Button:
  {
    string buttonText;
    cout << "Enter button text:\t";
    cin >> buttonText;
    Color textColor = Page::getColor(true);
    Color bgColor = Page::getColor(false);

    newComponent = new ButtonComponent(attributes, buttonText, bgColor, textColor);
    break;
  }
  case Input:
  {

    cout << "Enter Value color:" << endl;
    cout << "------------------" << endl;
    Color vcolor = Page::getColor(true);

    cout << "Enter Placeholder color:" << endl;
    cout << "------------------" << endl;
    Color pcolor = Page::getColor(true);

    newComponent = new InputComponent(attributes, vcolor, pcolor);
    break;
  }
  case Image:
  {

    newComponent = new ImageComponent(attributes);
    break;
  }
  }

  newComponent->setID(Page::getNextID());

  return newComponent;
}

void Page::render() {
  Page::pageContainer.render();
}




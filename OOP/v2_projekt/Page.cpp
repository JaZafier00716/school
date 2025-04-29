#include "Page.h"
#include <limits>

unsigned int Page::maxID = 1;                   /**< The maximum ID assigned so far. */
vector<unsigned int> Page::freeIDs;             /**< List of available component IDs. */
ContainerComponent Page::pageContainer("page"); /**< The container that holds all components. */

/**
 * @brief Returns the next available component ID.
 *
 * @return The next available ID for a new component.
 */
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

/**
 * @brief Adds a component to the page container.
 *
 * @param component Pointer to the component to add.
 * @return The ID of the added component, or -1 if the operation failed.
 */
unsigned int Page::addComponent(UIComponent *component)
{
  unsigned int id = Page::getNextID();
  component->setID(id);
  Page::pageContainer.addChild((UIComponent *)component);
  return id;
}

/**
 * @brief Adds a child component to an existing child component by specifying the child ID.
 *
 * @param component Pointer to the child component.
 * @param childID The ID of the child component.
 * @return The ID of the child component if added successfully, or -1 if failed.
 */
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

/**
 * @brief Removes a component from the page container by its index.
 *
 * @param index The index of the component to remove.
 * @return True if removal was successful, false if the component does not exist.
 */
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

/**
 * @brief Adds an attribute to a specific component.
 *
 * @param componentIndex The index of the component to which the attribute should be added.
 * @param attribute The attribute to add.
 * @return True if the attribute was added successfully, false otherwise.
 */
bool Page::addAttribute(unsigned int componentIndex, const ComponentAttribute &attribute)
{
  UIComponent *child = Page::pageContainer.getChild(componentIndex);
  if ((child)->getTag() == "not found")
  {
    return false;
  }
  else
  {
    (child)->addAttribute(attribute);
    return true;
  }
}

/**
 * @brief Returns a color code based on whether it's for text or background.
 *
 * @param text True if the color is for text, false for background.
 * @return Color code corresponding to the requested type.
 */
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

/**
 * @brief Creates a new UI component based on the specified type.
 *
 * @param type The type of component to create (e.g., Text, Button, Container).
 * @return Pointer to the newly created UI component.
 */
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

/**
 * @brief Renders the entire page, including all components and their attributes.
 */
void Page::render()
{
  Page::pageContainer.render();
}

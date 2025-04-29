#include "UIComponent.h"

/**
 * @brief Constructs a UIComponent with an optional tag and attributes.
 *
 * @param tag The tag name of the component (default is empty string).
 * @param attributes A vector of attributes to initialize the component with (default is empty).
 */
UIComponent::UIComponent(const string &tag, const vector<ComponentAttribute> &attributes)
{
  this->tag = tag;
  this->attributes = attributes;
}

/**
 * @brief Adds an attribute to the component.
 *
 * @param attribute The attribute to add.
 */
void UIComponent::addAttribute(ComponentAttribute attribute)
{
  for (auto a : this->attributes)
  { // Check if it already exists in the array
    if (a.name == attribute.name)
    { // if so, update the value
      a.value = attribute.value;
      return;
    }
  }
  attributes.push_back(attribute);
}

/**
 * @brief Adds an attribute to the component by specifying name and value.
 *
 * @param name The name of the attribute.
 * @param value The value of the attribute.
 */
void UIComponent::addAttribute(const string &name, const string &value)
{
  for (auto a : this->attributes)
  { // Check if it already exists in the array
    if (a.name == name)
    { // if so, update the value
      a.value = value;
      return;
    }
  }

  ComponentAttribute newAttribute = {name, value};

  this->attributes.push_back(newAttribute);
}

/**
 * @brief Removes an attribute from the component.
 *
 * @param attributeName The name of the attribute to remove.
 * @return true if the attribute was found and removed, false otherwise.
 */
bool UIComponent::removeAttribute(const string &attributeName)
{
  for (int i = 0; i < this->attributes.size(); i++)
  {
    if (this->attributes[i].name == attributeName)
    {
      this->attributes.erase(this->attributes.begin() + i);
      return true;
    }
  }
  return false;
}

/**
 * @brief Edits an existing attribute's value.
 *
 * @param attributeName The name of the attribute to edit.
 * @param newValue The new value to assign to the attribute.
 */
void UIComponent::editAttribute(const string &attributeName, const string &newValue)
{
  for (auto attribute : this->attributes)
  {
    if (attribute.name == attributeName)
    {
      attribute.value = newValue;
      return;
    }
  }
}

/**
 * @brief Gets the value of a specified attribute.
 *
 * @param attributeName The name of the attribute to retrieve.
 * @return The value of the attribute.
 */
string UIComponent::getAttributeValue(const string &attributeName) const
{
  for (const auto attribute : this->attributes)
  {
    if (attribute.name == attributeName)
    {
      return attribute.value;
    }
  }
  return "";
}

/**
 * @brief Prints the tag with its attributes.
 *
 * @param closing Whether to print a closing tag (e.g., </div>).
 * @param selfClosing Whether to print a self-closing tag (e.g., <img />).
 */
void UIComponent::printTagAttrib(bool closing, bool selfClosing) const
{
  if (closing)
  {
    cout << "</" << this->tag << ">" << endl;
  }
  else
  {

    cout << "<" << this->tag << " id={" << this->id << "} ";

    for (const auto attribute : this->attributes)
    {
      cout << attribute.name << "={" << attribute.value << "} ";
    }

    cout << (selfClosing ? " />" : " >") << endl;
  }
}

/**
 * @brief Renders the component.
 */
void UIComponent::render() const
{
  printTagAttrib(false);
  cout << "Generic Component" << endl;
  printTagAttrib(true);
}

/**
 * @brief Sets the unique ID of the component.
 *
 * @param id The new ID to assign.
 */
void UIComponent::setID(unsigned int id)
{
  this->id = id;
}

/**
 * @brief Returns the unique ID of the component.
 *
 * @return The component's ID.
 */
unsigned int UIComponent::getID()
{
  return this->id;
}

/**
 * @brief Returns the tag name of the component.
 *
 * @return The component's tag name.
 */
string UIComponent::getTag()
{
  return this->tag;
}
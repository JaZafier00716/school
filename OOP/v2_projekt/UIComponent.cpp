#include "UIComponent.h"

UIComponent::UIComponent(const string &tag, const vector<ComponentAttribute>& attributes)
{
  this->tag = tag;
  this->attributes = attributes;
}

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

void UIComponent::render() const
{
  printTagAttrib(false);
  cout << "Generic Component" << endl;
  printTagAttrib(true);
}

void UIComponent::setID(unsigned int id)
{
  this->id = id;
}
unsigned int UIComponent::getID()
{
  return this->id;
}

string UIComponent::getTag() {
  return this->tag;
}
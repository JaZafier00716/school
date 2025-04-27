#pragma once
#include "AbstractComponent.h"
#include <iostream>
#include <vector>
using std::vector, std::cout, std::endl;

class UIComponent : public AbstractComponent
{

private:
  unsigned int id;
  string tag;                            /** Component's name */
  vector<ComponentAttribute> attributes; /** Component's attributes */
public:
  UIComponent(const string &tag = "", const vector<ComponentAttribute> &attributes = {});
  virtual ~UIComponent() = default;

  void addAttribute(ComponentAttribute attribute) override;
  void addAttribute(const string &name, const string &value) override;
  bool removeAttribute(const string &attributeName) override;
  void editAttribute(const string &attributeName, const string &newValue) override;
  string getAttributeValue(const string &attributeName) const override;

  void printTagAttrib(bool closing = false, bool selfClosing = false) const;
  virtual void render() const override;
  unsigned int getID();
  string getTag();
  void setID(unsigned int id);
protected:
};

#pragma once
#include <string>
using std::string;

typedef struct {
  string name;
  string value;
}ComponentAttribute;

/**
 * @class AbstractComponent
 * @brief Purely abstract class that declares rendering and setAttribute methods
 */
class AbstractComponent
{
public:
  AbstractComponent();
  virtual ~AbstractComponent() = 0;
  virtual void render() const = 0;
  virtual void addAttribute(ComponentAttribute attribute) = 0;
  virtual void addAttribute(const string& name, const string& value) = 0;
  virtual bool removeAttribute(const string& attributeName) = 0;
  virtual void editAttribute(const string& attributeName, const string& newValue) = 0;
  virtual string getAttributeValue(const string& attributeName) const = 0;
};

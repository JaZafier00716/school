#pragma once
#include <string>
using std::string;

typedef struct ComponentAttribute {
  string name;
  string value;
};

/**
 * @class AbstractComponent
 * @brief Purely abstract class that declares rendering and setAttribute methods
 */
class AbstractComponent
{
public:
  AbstractComponent();
  virtual ~AbstractComponent() = default;
  virtual void render() const = 0;
  virtual void addAttribute(ComponentAttribute attribute) = 0;
  virtual void addAttribute(const string& name, const string& value);
  virtual bool removeAttribute(const string& attributeName) = 0;
  virtual void editAttribute(const string& attributeName, const string& newValue);
  virtual string getAttributeValue(const string& attributeName) const;
};


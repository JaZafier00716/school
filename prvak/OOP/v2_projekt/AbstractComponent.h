#pragma once
#include <string>
using std::string;

/**
 * @struct ComponentAttribute
 * @brief Represents an attribute with a name and a value for a component.
 */
typedef struct {
  string name;   /**< Name of the attribute. */
  string value;  /**< Value of the attribute. */
} ComponentAttribute;

/**
 * @class AbstractComponent
 * @brief Purely abstract class that declares rendering and attribute management methods.
 */
class AbstractComponent
{
public:
  /**
   * @brief Default constructor.
   */
  AbstractComponent();

  /**
   * @brief Pure virtual destructor to make the class abstract.
   */
  virtual ~AbstractComponent() = 0;

  /**
   * @brief Renders the component.
   */
  virtual void render() const = 0;

  /**
   * @brief Adds an attribute to the component.
   * 
   * @param attribute The attribute to add.
   */
  virtual void addAttribute(ComponentAttribute attribute) = 0;

  /**
   * @brief Adds an attribute to the component by specifying name and value.
   * 
   * @param name The name of the attribute.
   * @param value The value of the attribute.
   */
  virtual void addAttribute(const string& name, const string& value) = 0;

  /**
   * @brief Removes an attribute from the component.
   * 
   * @param attributeName The name of the attribute to remove.
   * @return true if the attribute was found and removed, false otherwise.
   */
  virtual bool removeAttribute(const string& attributeName) = 0;

  /**
   * @brief Edits an existing attribute's value.
   * 
   * @param attributeName The name of the attribute to edit.
   * @param newValue The new value to assign to the attribute.
   */
  virtual void editAttribute(const string& attributeName, const string& newValue) = 0;

  /**
   * @brief Gets the value of a specified attribute.
   * 
   * @param attributeName The name of the attribute to retrieve.
   * @return The value of the attribute.
   */
  virtual string getAttributeValue(const string& attributeName) const = 0;
};

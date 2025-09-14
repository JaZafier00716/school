#pragma once
#include "AbstractComponent.h"
#include <iostream>
#include <vector>
using std::vector, std::cout, std::endl;

/**
 * @class UIComponent
 * @brief Default implementation of AbstractComponent for child components.
 */
class UIComponent : public AbstractComponent
{
private:
  unsigned int id;                       /**< Unique identifier for the component. */
  string tag;                            /**< Component's tag name (e.g., "div", "button"). */
  vector<ComponentAttribute> attributes; /**< List of component's attributes. */

public:
  /**
   * @brief Constructs a UIComponent with an optional tag and attributes.
   * 
   * @param tag The tag name of the component (default is empty string).
   * @param attributes A vector of attributes to initialize the component with (default is empty).
   */
  UIComponent(const string &tag = "", const vector<ComponentAttribute> &attributes = {});

  /**
   * @brief Virtual default destructor.
   */
  virtual ~UIComponent() = default;

  /**
   * @brief Adds an attribute to the component.
   * 
   * @param attribute The attribute to add.
   */
  void addAttribute(ComponentAttribute attribute) override;

  /**
   * @brief Adds an attribute to the component by specifying name and value.
   * 
   * @param name The name of the attribute.
   * @param value The value of the attribute.
   */
  void addAttribute(const string &name, const string &value) override;

  /**
   * @brief Removes an attribute from the component.
   * 
   * @param attributeName The name of the attribute to remove.
   * @return true if the attribute was found and removed, false otherwise.
   */
  bool removeAttribute(const string &attributeName) override;

  /**
   * @brief Edits an existing attribute's value.
   * 
   * @param attributeName The name of the attribute to edit.
   * @param newValue The new value to assign to the attribute.
   */
  void editAttribute(const string &attributeName, const string &newValue) override;

  /**
   * @brief Gets the value of a specified attribute.
   * 
   * @param attributeName The name of the attribute to retrieve.
   * @return The value of the attribute.
   */
  string getAttributeValue(const string &attributeName) const override;

  /**
   * @brief Prints the tag with its attributes.
   * 
   * @param closing Whether to print a closing tag (e.g., </div>).
   * @param selfClosing Whether to print a self-closing tag (e.g., <img />).
   */
  void printTagAttrib(bool closing = false, bool selfClosing = false) const;

  /**
   * @brief Renders the component.
   */
  virtual void render() const override;

  /**
   * @brief Returns the unique ID of the component.
   * 
   * @return The component's ID.
   */
  unsigned int getID();

  /**
   * @brief Returns the tag name of the component.
   * 
   * @return The component's tag name.
   */
  string getTag();

  /**
   * @brief Sets the unique ID of the component.
   * 
   * @param id The new ID to assign.
   */
  void setID(unsigned int id);
};

#pragma once
#include "UIComponent.h"

/**
 * @class ContainerComponent
 * @brief A UI component that can contain and manage a list of child components.
 */
class ContainerComponent : public UIComponent
{
private:
  vector<UIComponent*> children; /**< List of child components within the container. */

public:
  /**
   * @brief Constructs a ContainerComponent with optional tag, attributes, and child components.
   * 
   * @param tag The HTML-like tag name for the container (default is empty).
   * @param attributes List of attributes for the container.
   * @param children List of initial child components.
   */
  ContainerComponent(
    const string& tag = "", 
    const vector<ComponentAttribute>& attributes = {},
    const vector<UIComponent*>& children = {}
  );

  /**
   * @brief Destructor that deletes or cleans up any owned child components.
   * Destructor also logs destruction of the component.
   */
  ~ContainerComponent();

  /**
   * @brief Returns a pointer to the child component at the specified index.
   * 
   * @param childIndex The index of the child component.
   * @return Pointer to the child component if found; otherwise, returns a dummy component with tag "not found".
   */
  UIComponent* getChild(unsigned int childIndex);

  /**
   * @brief Adds a child component at a specific position within the container.
   * 
   * @param child Pointer to the child component to be added.
   * @param containerIndex Index at which to insert the child component.
   * @return True if insertion was successful, false if the index is out of bounds.
   */
  bool addChild(UIComponent* child, unsigned int containerIndex);

  /**
   * @brief Adds a child component to the end of the container.
   * 
   * @param child Pointer to the child component to be added.
   */
  void addChild(UIComponent* child);

  /**
   * @brief Removes a child component at the specified index.
   * 
   * @param childIndex The index of the child component to be removed.
   * @return True if removal was successful, false if the index is invalid.
   */
  bool removeChild(unsigned int childIndex);

  /**
   * @brief Renders the container and all its child components.
   */
  void render() const override;
};

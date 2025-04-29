#include "ContainerComponent.h"

/**
 * @brief Constructs a ContainerComponent with optional tag, attributes, and child components.
 *
 * @param tag The HTML-like tag name for the container (default is empty).
 * @param attributes List of attributes for the container.
 * @param children List of initial child components.
 */
ContainerComponent::ContainerComponent(
    const string &tag,
    const vector<ComponentAttribute> &attributes,
    const vector<UIComponent *> &children) : UIComponent(tag, attributes)
{
  this->children = children;
}

/**
 * @brief Destructor that deletes or cleans up any owned child components.
 * Destructor also logs destruction of the component.
 */
ContainerComponent::~ContainerComponent()
{
  cout << "ContainerComponent desctructor" << endl;
  for (auto child : this->children)
  {
    delete child;
  }
}

/**
 * @brief Renders the container and all its child components.
 */
void ContainerComponent::render() const
{
  this->printTagAttrib();
  for (const auto child : this->children)
  {
    child->render();
  }
  this->printTagAttrib(true);
}

/**
 * @brief Adds a child component to the end of the container.
 *
 * @param child Pointer to the child component to be added.
 */
void ContainerComponent::addChild(UIComponent *child)
{
  this->children.push_back(child);
}

/**
 * @brief Adds a child component at a specific position within the container.
 *
 * @param child Pointer to the child component to be added.
 * @param containerIndex Index at which to insert the child component.
 * @return True if insertion was successful, false if the index is out of bounds.
 */
bool ContainerComponent::addChild(UIComponent *child, unsigned int containerIndex)
{
  if (this->getID() == containerIndex)
  {
    this->addChild(child);
    return true;
  }
  else
  {
    for (auto child : this->children)
    {
      if (ContainerComponent *subContainer = dynamic_cast<ContainerComponent *>(child))
      { /** if child component is container -> call addChild function */
        subContainer->addChild(child, containerIndex);
      }
    }
  }
  return false;
}

/**
 * @brief Returns a pointer to the child component at the specified index.
 *
 * @param childIndex The index of the child component.
 * @return Pointer to the child component if found; otherwise, returns a dummy component with tag "not found".
 */
UIComponent *ContainerComponent::getChild(unsigned int childIndex)
{
  for (UIComponent *child : children)
  {
    if (child->getID() == childIndex)
    {
      return child;
    }

    if (ContainerComponent *subContainer = dynamic_cast<ContainerComponent *>(child))
    {
      UIComponent *result = subContainer->getChild(childIndex);
      if (result && result->getTag() != "not found")
      {
        return result;
      }
    }
  }

  static UIComponent notFound("not found");
  return &notFound;
}

/**
 * @brief Removes a child component at the specified index.
 *
 * @param childIndex The index of the child component to be removed.
 * @return True if removal was successful, false if the index is invalid.
 */
bool ContainerComponent::removeChild(unsigned int childIndex)
{
  for (int i = 0; i < this->children.size(); i++)
  {
    if (this->children[i]->getID() == childIndex)
    { // if child is found in the current container, return true
      this->children.erase(this->children.begin() + i);
      return true;
    }
    if (ContainerComponent *subContainer = dynamic_cast<ContainerComponent *>(this->children[i]))
    { // if the child is a container, call removeChild on it
      if (subContainer->removeChild(childIndex))
      {
        return true;
      }
    }
  }

  return false; // if child is not found in the current container
}
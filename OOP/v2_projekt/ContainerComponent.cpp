#include "ContainerComponent.h"


ContainerComponent::ContainerComponent(
  const string& tag, 
    const vector<ComponentAttribute>& attributes,
    const vector<UIComponent*>& children
) : UIComponent(tag, attributes)
{
  this->children = children;
}

ContainerComponent::~ContainerComponent()
{
  cout << "ContainerComponent desctructor" << endl;
}


void ContainerComponent::render() const {
  this->printTagAttrib();
  for (const auto child : this->children)
  {
    child->render();
  }
  this->printTagAttrib(true);
}

void ContainerComponent::addChild(UIComponent* child) {
  this->children.push_back(child);
}

void ContainerComponent::addChild(UIComponent* child, unsigned int containerIndex) {
  if(this->getID() == containerIndex) {
    this->addChild(child);
  } else {
    for (auto child : this->children)
    {
      if(ContainerComponent* subContainer = dynamic_cast<ContainerComponent*>(child)) { /** if child component is container -> call addChild function */
        subContainer->addChild(child, containerIndex);
      }
    }
  }
}


UIComponent* ContainerComponent::getChild(unsigned int childIndex) {
  for (auto child : children)
  {
    if(child->getID() == childIndex) {
      return child;
    }
    if(ContainerComponent* subContainer = dynamic_cast<ContainerComponent*>(child)) {
      UIComponent* child = subContainer->getChild(childIndex);
      if(child->getTag() != "not found") {
        return child;
      }
    }
  }

  return new UIComponent("not found");
  
}


bool ContainerComponent::removeChild(unsigned int childIndex) {
  for (int i = 0; i < this->children.size(); i++)
  {
    if(this->children[i]->getID() == childIndex) {  // if child is found in the current container, return true
      this->children.erase(this->children.begin() +i);
      return true;
    }
    if(ContainerComponent* subContainer = dynamic_cast<ContainerComponent*>(this->children[i])) { // if the child is a container, call removeChild on it
      if(subContainer->removeChild(childIndex)) {
        return true;
      }
    }
  }
  
  return false; // if child is not found in the current container
}
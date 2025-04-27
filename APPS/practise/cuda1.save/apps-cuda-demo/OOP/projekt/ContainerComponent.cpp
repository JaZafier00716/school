#include "ContainerComponent.h"


ContainerComponent::ContainerComponent(
  UIComponentVariables general_params, 
  vector<unique_ptr<UIComponent>> children) 
  : UIComponent(general_params), children(move(children)) {}

void ContainerComponent::addChild(unique_ptr<UIComponent> child) {
  this->children.push_back(move(child));
}


void ContainerComponent::removeChild(unsigned int child_id) {
  for (int i = 0; i < this->children.size(); i++)
  {
    if(this->children[i]->getID() == child_id) {
      this->children.erase(this->children.begin() + i);
      break;
    }
  }
}

void ContainerComponent::render() const {
  cout << "<Container id={" << this->id << "} width={" << this->vars.w << "} height={" << this->vars.h << "} pos_x={" << this->vars.x << "} pos_y={" << this->vars.y << "}>" << endl;
  for(const auto& child : this->children) {
    child->render();
  }
  cout << "</Container>" << endl;
}


const vector<unique_ptr<UIComponent>>& ContainerComponent::getChildren() const {
  return this->children;
}
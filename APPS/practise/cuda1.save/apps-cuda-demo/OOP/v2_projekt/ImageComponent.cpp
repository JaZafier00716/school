#include "ImageComponent.h"


ImageComponent::ImageComponent(
  const vector<ComponentAttribute>& attributes
) : UIComponent("img", attributes)
{
  this->addAttribute("src", "");
  this->addAttribute("alt", "");
  this->addAttribute("width", "");
  this->addAttribute("height", "");
}

ImageComponent::~ImageComponent()
{
  cout << "ImageComponent destructor" << endl;
}


void ImageComponent::render() const {
  this->printTagAttrib(false, true);
}
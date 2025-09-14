#include "ImageComponent.h"

/**
 * @brief Constructs an ImageComponent with optional attributes.
 *
 * Initializes default attributes: `src`, `alt`, `width`, and `height`.
 *
 * @param attributes The list of attributes for the image (default is empty).
 */
ImageComponent::ImageComponent(
    const vector<ComponentAttribute> &attributes) : UIComponent("img", attributes)
{
  this->addAttribute("src", "");
  this->addAttribute("alt", "");
  this->addAttribute("width", "");
  this->addAttribute("height", "");
}

/**
 * @brief Destructor that logs destruction of the component.
 */
ImageComponent::~ImageComponent()
{
  cout << "ImageComponent destructor" << endl;
}

/**
 * @brief Renders the image component as a self-closing tag.
 */
void ImageComponent::render() const
{
  this->printTagAttrib(false, true);
}
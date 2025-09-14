#pragma once
#include "UIComponent.h"

/**
 * @class ImageComponent
 * @brief A UI component representing an image, rendered as a self-closing tag with standard image attributes.
 */
class ImageComponent : public UIComponent
{
public:
  /**
   * @brief Constructs an ImageComponent with optional attributes.
   * 
   * Initializes default attributes: `src`, `alt`, `width`, and `height`.
   * 
   * @param attributes The list of attributes for the image (default is empty).
   */
  ImageComponent(const vector<ComponentAttribute>& attributes = {});

  /**
   * @brief Destructor that logs destruction of the component.
   */
  ~ImageComponent();

  /**
   * @brief Renders the image component as a self-closing tag.
   */
  void render() const override;
};

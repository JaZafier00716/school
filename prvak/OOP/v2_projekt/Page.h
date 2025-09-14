#pragma once
#include "ContainerComponent.h"
#include "TextComponent.h"
#include "ButtonComponent.h"
#include "InputComponent.h"
#include "ImageComponent.h"
using std::cin;

/**
 * @enum ComponentType
 * @brief Enum representing different component types for page creation.
 */
enum ComponentType {
  Undefined,  /**< Undefined type, used when specifying children types later. */
  Container,  /**< Container component type. */
  Text,       /**< Text component type. */
  Button,     /**< Button component type. */
  Input,      /**< Input component type. */
  Image       /**< Image component type. */
};

/**
 * @class Page
 * @brief Static class that manages page components (such as text, buttons, etc.) and handles operations on them.
 */
class Page
{
private:
  static vector<unsigned int> freeIDs;     /**< List of available component IDs. */
  static unsigned int maxID;               /**< The maximum ID assigned so far. */
  static ContainerComponent pageContainer; /**< The container that holds all components. */

  /**
   * @brief Returns a color code based on whether it's for text or background.
   * 
   * @param text True if the color is for text, false for background.
   * @return Color code corresponding to the requested type.
   */
  static Color getColor(bool text);

public:
  /**
   * @brief Returns the next available component ID.
   * 
   * @return The next available ID for a new component.
   */
  static unsigned int getNextID();

  /**
   * @brief Adds a component to the page container.
   * 
   * @param component Pointer to the component to add.
   * @return The ID of the added component, or -1 if the operation failed.
   */
  static unsigned int addComponent(UIComponent *component);

  /**
   * @brief Adds a child component to an existing child component by specifying the child ID.
   * 
   * @param component Pointer to the child component.
   * @param childID The ID of the child component.
   * @return The ID of the child component if added successfully, or -1 if failed.
   */
  static int addComponent(UIComponent *component, unsigned int childID);

  /**
   * @brief Removes a component from the page container by its index.
   * 
   * @param index The index of the component to remove.
   * @return True if removal was successful, false if the component does not exist.
   */
  static bool removeComponent(unsigned int index);

  /**
   * @brief Adds an attribute to a specific component.
   * 
   * @param componentIndex The index of the component to which the attribute should be added.
   * @param attribute The attribute to add.
   * @return True if the attribute was added successfully, false otherwise.
   */
  static bool addAttribute(unsigned int componentIndex, const ComponentAttribute& attribute);

  /**
   * @brief Creates a new UI component based on the specified type.
   * 
   * @param type The type of component to create (e.g., Text, Button, Container).
   * @return Pointer to the newly created UI component.
   */
  static UIComponent* createComponent(ComponentType type);

  /**
   * @brief Renders the entire page, including all components and their attributes.
   */
  static void render();
};

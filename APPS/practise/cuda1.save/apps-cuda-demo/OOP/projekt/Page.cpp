#include "Page.h"
#include "TextComponent.h"
#include "InputComponent.h"
#include "ButtonComponent.h"

ContainerComponent Page::root = ContainerComponent(UIComponentVariables{0,0,0,0});

void Page::render() {
  cout << "<Page width={" << root.getW() << "} height={" << root.getH() << "} pos_x={" << root.getX() << "} pos_y={" << root.getY() << "} >" << endl;
  
  for(const auto& child : root.getChildren()) {
    child->render();
  }
  cout << "</Page>" << endl;
}
void Page::addComponent(unique_ptr<UIComponent> component) {
  root.addChild(move(component));
}
void Page::removeComponent(unsigned int child_id) {
  root.removeChild(child_id);
}
void Page::createDefaultPage() {
  // Text
  root.addChild(make_unique<TextComponent>(UIComponentVariables{0.0, 0.0, 1920, 1080}, "Default Page", WHITE));

  for (int i = 0; i < 10; i++) {
      // Container to hold inputs
      auto container = make_unique<ContainerComponent>(UIComponentVariables{0.0, 0.0, 800, 1080});
      
      // Container's inputs
      container->addChild(make_unique<InputComponent>(UIComponentVariables{0.0, 0.0, 400, 50}, "Undefined Input", RED, BRIGHT_RED));
      container->addChild(make_unique<InputComponent>(UIComponentVariables{0.0, 50, 400, 50}, "Text Input", GREEN, BRIGHT_GREEN, variant<std::string, double, unsigned int>("InputText"), text, false));
      container->addChild(make_unique<InputComponent>(UIComponentVariables{0.0, 100, 400, 50}, "Number Input", YELLOW, BRIGHT_YELLOW, variant<std::string, double, unsigned int>(150.4), number, false));
      container->addChild(make_unique<InputComponent>(UIComponentVariables{0.0, 150, 400, 50}, "E-mail Input", BLUE, BRIGHT_BLUE, variant<std::string, double, unsigned int>("email&example.com"), email, false));
      container->addChild(make_unique<InputComponent>(UIComponentVariables{0.0, 200, 400, 50}, "Phone Input", MAGENTA, BRIGHT_MAGENTA, variant<std::string, double, unsigned int>((unsigned int)123456789), phone, false));
      container->addChild(make_unique<ButtonComponent>(UIComponentVariables{400, 0.0, 200, 50}, "Submit Button", CYAN));
      container->addChild(make_unique<ButtonComponent>(UIComponentVariables{650, 0.0, 200, 50}, "Reset Button", CYAN));
      root.addChild(move(container));
  }
}

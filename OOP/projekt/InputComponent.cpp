#include "InputComponent.h"
using std::getline;
/**
 * @brief Constructor for the InputComponent class.
 *
 * @param general_params The general parameter for the size and position of this component
 * @param placeholder The placeholder text for the input (default is an empty string)
 * @param pcolor The color of the placeholder text (default is TextColor::DEFAULT)
 * @param vcolor The color of the value text (default is TextColor::DEFAULT)
 * @param value The initial value of the input (default is an empty string)
 * @param type The type of the input (default is ValueType::undefined)
 * @param active The initial active state of the component (default is false)
 */
InputComponent::InputComponent(
    UIComponentVariables general_params,
    const string &placeholder,
    TextColor pcolor,
    TextColor vcolor,
    variant<string, double, unsigned int> value,
    ValueType type,
    bool active)
    : UIComponent(general_params), placeholder(placeholder), pcolor(pcolor), vcolor(vcolor), value(value), active(active), type(type) {}

/**
 * @brief Renders the input component on the screen.
 */
void InputComponent::render() const
{
  cout << "<Placeholder>" << UIComponent::getAnsiCode(this->pcolor) << this->placeholder << UIComponent::getAnsiCode(TextColor::DEFAULT) << "</Placeholder>" << endl;
  cout << "<Input id={" << this->id << "} width={" << this->vars.w << "} height={" << this->vars.h << "} pos_x={" << this->vars.x << "} pos_y={" << this->vars.y << "}>" << endl;

  switch (type)
  {
  case text:
    cout << "Text:\t" << UIComponent::getAnsiCode(this->vcolor) << get<string>(this->value) << UIComponent::getAnsiCode(TextColor::DEFAULT) << endl;
    break;
  case number:
    cout << "Number:\t" << UIComponent::getAnsiCode(this->vcolor) << get<double>(this->value) << UIComponent::getAnsiCode(TextColor::DEFAULT) << endl;
    break;
  case email:
    cout << "E-mail:\t" << UIComponent::getAnsiCode(this->vcolor) << get<string>(this->value) << UIComponent::getAnsiCode(TextColor::DEFAULT) << endl;
    break;
  case phone:
    cout << "Phone number:\t" << UIComponent::getAnsiCode(this->vcolor) << get<unsigned int>(this->value) << UIComponent::getAnsiCode(TextColor::DEFAULT) << endl;
    break;
  case undefined:
    cout << "<<Empty>>" << endl;
  default:
    break;
  }
  cout << "</input>" << endl;
}

/**
 * @brief Toggles the active state of the input component.
 * If the component is active, it proceeds to handle input value.
 */
void InputComponent::onClick()
{
  this->active = !this->active;
  cout << "Input with id=" << this->id << " was clicked" << endl;
  cout << "Input state is now " << (active ? "active" : "inactive") << endl;

  if (active)
  {
    inputValue();
  }
}

/**
 * @brief Handles the input process, allowing the user to enter a value.
 */
void InputComponent::inputValue()
{
  int action;
  do
  {
    cout << "choose either:" << endl;
    cout << "[0] for click - to decativate input" << endl;
    cout << "[1] for input - to input value" << endl;
    cout << "Action:\t";
    cin >> action;
  } while (action != 0 && action != 1);

  if (action == 0)
  {
    onClick();
  }
  else
  {
    int type;
    cout << "Choose your input type:" << endl;
    cout << "[0] for text" << endl;
    cout << "[1] for number" << endl;
    cout << "[2] for email" << endl;
    cout << "[3] for phone" << endl;
    cout << "Your choice:\t";
    cin >> type;
    this->type = (ValueType)type;

    switch (this->type)
    {
    case text:
    {
      string s_val = "";
      cout << "Enter text:\t";
      getline(cin, s_val);
      this->value = s_val;
      break;
    }
    case number:
    {
      double d_val;
      cout << "Enter number:\t";
      cin >> d_val;
      this->value = d_val;
      break;
    }
    case email:
    {

      string s_val = "";
      do
      {
        if (s_val != "")
        {
          cout << "email must contain @" << endl;
        }
        cout << "Enter email";
        getline(cin, s_val);
      } while (s_val.find("@") == string::npos);
      this->value = s_val;
      break;
    }
    case phone:
    {
      unsigned int i_val;
      cout << "Enter phone:\t";
      cin >> i_val;
      this->value = i_val;
    }
    }
  }
}
#include "Page.h"
#include <limits>
#include <ios>
using std::numeric_limits, std::streamsize;

int main()
{
  int action;
  do
  {
    bool AddTochild;
    unsigned int childContainerID = -1;
    int newComponentID = -1;
    cout << "=======================" << endl;
    cout << "Create your own website" << endl;
    cout << "=======================" << endl;
    cout << "0\t|\tRender your current website" << endl;
    cout << "1\t|\tAdd Container component" << endl;
    cout << "2\t|\tAdd Text component" << endl;
    cout << "3\t|\tAdd Button component" << endl;
    cout << "4\t|\tAdd Input component" << endl;
    cout << "5\t|\tAdd Image component" << endl;
    cout << "6\t|\tAdd attribute to component" << endl;
    cout << "7\t|\tRemove component" << endl;
    cout << "-1\t|\tEnd program" << endl;
    cout << "-------------------------------" << endl;
    cout << "Choose your action:\t";
    cin >> action;
    cout << "-------------------------------" << endl;

    if (cin.fail())
    {
      cin.clear();                                         // Clear error flag
      cin.ignore(numeric_limits<streamsize>::max(), '\n'); // Discard invalid input
      cout << "Invalid input. Please enter an integer action.\n";
      continue; // Skip this iteration and prompt again
    }

    if (action >= 1 && action <= 5)
    {
      cout << "Add to page [0], or to child container[1]?:\t";
      cin >> AddTochild;
      if (AddTochild)
      {
        cout << "Enter child container's id:\t";
        cin >> childContainerID;
      }
    }

    switch (action)
    {
    case -1:
      break;
    case 0:
      Page::render();
      break;
    case 1:
    {
      UIComponent *component = Page::createComponent(Container);
      if (AddTochild)
      {
        do
        {
          newComponentID = Page::addComponent(component, childContainerID);
          if (newComponentID == -1)
          {
            cout << "Child container not found" << endl;
            cout << "Enter new child container's id:\t";
            cin >> childContainerID;
          }
          else
          {
            cout << "Child ID is:\t" << newComponentID << endl;
          }
        } while (newComponentID == -1);
      }
      else
      {
        Page::addComponent(component);
      }
      break;
    }
    case 2:
    {
      UIComponent *component = Page::createComponent(Text);
      if (AddTochild)
      {
        do
        {
          newComponentID = Page::addComponent(component, childContainerID);
          if (newComponentID == -1)
          {
            cout << "Child container not found" << endl;
            cout << "Enter new child container's id:\t";
            cin >> childContainerID;
          }
          else
          {
            cout << "Child ID is:\t" << newComponentID << endl;
          }
        } while (newComponentID == -1);
      }
      else
      {
        Page::addComponent(component);
      }
      break;
    }
    case 3:
    {
      UIComponent *component = Page::createComponent(Button);
      if (AddTochild)
      {
        do
        {
          newComponentID = Page::addComponent(component, childContainerID);
          if (newComponentID == -1)
          {
            cout << "Child container not found" << endl;
            cout << "Enter new child container's id:\t";
            cin >> childContainerID;
          }
          else
          {
            cout << "Child ID is:\t" << newComponentID << endl;
          }
        } while (newComponentID == -1);
      }
      else
      {
        Page::addComponent(component);
      }
      break;
    }
    case 4:
    {
      UIComponent *component = Page::createComponent(Input);
      if (AddTochild)
      {
        do
        {
          newComponentID = Page::addComponent(component, childContainerID);
          if (newComponentID == -1)
          {
            cout << "Child container not found" << endl;
            cout << "Enter new child container's id:\t";
            cin >> childContainerID;
          }
          else
          {
            cout << "Child ID is:\t" << newComponentID << endl;
          }
        } while (newComponentID == -1);
      }
      else
      {
        Page::addComponent(component);
      }
      break;
    }
    case 5:
    {
      UIComponent *component = Page::createComponent(Image);
      if (AddTochild)
      {
        do
        {
          newComponentID = Page::addComponent(component, childContainerID);
          if (newComponentID == -1)
          {
            cout << "Child container not found" << endl;
            cout << "Enter new child container's id:\t";
            cin >> childContainerID;
          }
          else
          {
            cout << "Child ID is:\t" << newComponentID << endl;
          }
        } while (newComponentID == -1);
      }
      else
      {
        Page::addComponent(component);
      }
      break;
    }
    case 6:
    {

      bool addAttribute;
      cout << "Enter child component's id:\t";
      cin >> childContainerID;
      do
      {
        cout << "Add attribute? [1 - true, 0 - false]:\t";
        cin >> addAttribute;

        if (addAttribute)
        {
          string name, value;
          cout << "Enter attribute's name:\t";
          cin >> name;
          cout << "Enter attribute's value:\t";
          cin >> value;
          if (Page::addAttribute(childContainerID, {name, value}))
          {
            cout << "Attribute added successfully" << endl;
          }
          else
          {
            cout << "Component not found" << endl;
            cout << "Enter new child component's id:\t";
            cin >> childContainerID;
          }
        }
      } while (addAttribute);
      break;
    }
    case 7:
    {

      cout << "Enter component's id:\t";
      cin >> childContainerID;
      if (Page::removeComponent(childContainerID))
      {
        cout << "Child component removed successfully" << endl;
      }
      else
      {
        cout << "Child component not found" << endl;
      }
      break;
    }

    default:
      cout << "ERROR: Action not found" << endl;
      break;
    }
  } while (action != -1);

  return 0;
}
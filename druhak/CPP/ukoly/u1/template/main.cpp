#include "tasks.h"
using std::endl;
using std::cout;

int main() {
    UTF8String string;
    string.append('A');
    cout << string.get_byte_count() << endl;
    cout << string.get_point_count() << endl;
    string.append('h');
    cout << string.get_byte_count() << endl;
    cout << string.get_point_count() << endl;
    string.append('o');
    cout << string.get_byte_count() << endl;
    cout << string.get_point_count() << endl;
    string.append('j');
    cout << string.get_byte_count() << endl;
    cout << string.get_point_count() << endl;
    string.append(' ');
    cout << string.get_byte_count() << endl;
    cout << string.get_point_count() << endl;
    string.append(static_cast<CodePoint>(0x1F601));
    cout << string.get_byte_count() << endl;
    cout << string.get_point_count() << endl;
    string.append('.');
    cout << string.get_byte_count() << endl;
    cout << string.get_point_count() << endl;

    const UTF8String& str = string;

    cout << (str.nth_code_point(0).value() == 'A' ? "true" : "false") << endl;
    cout << (str.nth_code_point(1).value() == 'h' ? "true" : "false") << endl;
    cout << (str.nth_code_point(2).value() == 'o' ? "true" : "false") << endl;
    cout << (str.nth_code_point(3).value() == 'j' ? "true" : "false") << endl;
    cout << (str.nth_code_point(4).value() == ' ' ? "true" : "false") << endl;
    cout << (str.nth_code_point(5).value() == 0x1F601 ? "true" : "false") << endl;
    cout << (str.nth_code_point(6).value() == '.' ? "true" : "false") << endl;
}

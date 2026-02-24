#include "tasks.h"
using std::endl;
using std::cout;

int main() {
    UTF8String str{"ahoj"};
    str.print_bytes_hex();

    UTF8String str2{std::vector<CodePoint>{0x61, 0x20AC}};
    str2.print_bytes_hex();

    UTF8String str3{str};
    str3.print_bytes_hex();
    str2 = str;
    str2.print_bytes_hex();

    UTF8String str4{std::vector<CodePoint>{0x61, 0x20AC}};
    cout << "Byte count: " << str4.get_byte_count() << endl;
    cout << "Code point count: " << str4.get_point_count() << endl;
    for(size_t i = 0; i < str4.get_byte_count()+1; ++i) {
        auto byte = str4[i];
        if (byte.has_value()) {
            cout << "Byte " << i << ": " << std::hex << static_cast<int>(byte.value()) << std::dec << endl;
        } else {
            cout << "Byte " << i << ": std::nullopt" << endl;
        }
    }

    for(size_t i = 0; i < str4.get_point_count()+1; ++i) {
        auto cp = str4.nth_code_point(i);
        if (cp.has_value()) {
            cout << "Code point " << i << ": " << std::hex << cp.value() << std::dec << endl;
        } else {
            cout << "Code point " << i << ": std::nullopt" << endl;
        }
    }

    UTF8String str5{"ahoj"};
    str5.append(' ');
    str5.append('x');
    str5.append(static_cast<CodePoint>(0x1F601)); // Append Euro sign
    str5.print_bytes_hex();

    return 0;
}

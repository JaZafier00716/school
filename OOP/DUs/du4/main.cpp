#include "DynamicArray.h"

void testDynamicArray() {
    cout << "Test konstruktoru bez parametru:\n";
    DynamicArray arr1;
    arr1.setAt(0, 10);
    cout << "arr1[0] = " << arr1.getAt(0) << endl;
    cout << "Size = " << arr1.getSize() << endl;

    cout << "\nTest konstruktoru s parametrem n:\n";
    DynamicArray arr2(2);
    cout << "current size = " << arr2.getSize() << endl;
    arr2.Add(1);
    arr2.Add(2);
    arr2.Add(3);
    arr2.Add(4);
    arr2.Add(5);
    cout << "new size = " << arr2.getSize() << endl;
    cout << "arr2[2] = " << arr2.getAt(5) << endl;

    cout << "\nTest konstruktoru s polem:\n";
    int initialValues[] = {1, 2, 3, 4, 5};
    DynamicArray arr3(initialValues, 5);
    cout << "arr3[3] = " << arr3.getAt(3) << endl;

    cout << "\nTest pridavani prvku (Add):\n";
    for (int i = 0; i < 10; ++i) {
        arr1.Add(i * 10);
    }
    for (int i = 0; i < arr1.getCount(); ++i) {
        cout << "arr1[" << i << "] = " << arr1.getAt(i) << endl;
    }

    cout << "\nTest odstraneni prvku (removeAt):\n";
    cout << "count before: " << arr3.getCount() << endl; 
    arr3.removeAt(2);
    for (int i = 0; i < 4; ++i) {
        cout << "arr3[" << i << "] = " << arr3.getAt(i) << endl;
    }
    cout << "count after: " << arr3.getCount() << endl;

}

int main() {
    testDynamicArray();
    return 0;
}
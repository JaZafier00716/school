import {TextInput} from 'react-native';
import {SafeAreaView, SafeAreaProvider} from 'react-native-safe-area-context';
import {useState} from "react";

const TextBox = () => {
    const [value, onChangeText] = useState('');

    // If you type something in the text box that is a color,
    // the background will change to that color.
    return (
        <SafeAreaProvider >
            <SafeAreaView
                style={{
                    flex: 1,
                    backgroundColor: value.toLowerCase(),
                    borderStyle: 'solid',
                    borderWidth: 1,
                    borderColor: 'black',
                    padding: 10,
                }}>

            </SafeAreaView>
        </SafeAreaProvider>
    );
};


export default TextBox;
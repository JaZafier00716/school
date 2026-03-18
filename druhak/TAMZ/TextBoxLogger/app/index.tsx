import {Button, TextInput, View} from "react-native";
import {SafeAreaView, SafeAreaProvider} from "react-native-safe-area-context";
import {useEffect, useState} from "react";
import { Storage } from "@/lib/storage";
import DialogWindow from "@/components/DialogWindow";

export default function Index() {
    const [text, setTextState] = useState("");
    const storage_key = "text_arr";
    const session_storage_key = "text_session";
    const [arr, setArr] = useState<string[]>([]);
    const [dialog_visible, setDialogVisible] = useState(false);

    const setText = (value: string) => {
        setTextState(value);
        void Storage.setItem(session_storage_key, value);
    };



    const save_text = async () => {
        const stored = await Storage.getItem(storage_key);
        const arr = stored ? JSON.parse(stored) : [];
        arr.unshift(text);

        await Storage.setItem(storage_key, JSON.stringify(arr));
        setText("");
        console.log("Saved text:", text);
    }

    const fetch_text = async (): Promise<string[]> => {
        const stored = await Storage.getItem(storage_key);
        const items = stored ? (JSON.parse(stored) as string[]) : [];
        setArr(items);
        return items;
    };

    const load_text = async () => {
        await fetch_text();
        setDialogVisible(true);
    }

    const remove_item = async (index: number) => {
        const stored = await Storage.getItem(storage_key);
        if(stored) {
            const arr = JSON.parse(stored);
            arr.splice(index, 1);
            await Storage.setItem(storage_key, JSON.stringify(arr));
            await load_text();
        }
    }

    const clear_storage = async () => {
        await Storage.clear();
        setArr([]);
        setText("");
    }


    useEffect(() => {
        const init = async () => {
            const items = await fetch_text();
            const sessionText = await Storage.getItem(session_storage_key);
            setTextState(sessionText !== null ? sessionText : (items[0] ?? ""));
        };

        void init();
    }, []);

    return (
        <SafeAreaProvider>
            <SafeAreaView
                className={"bg-slate-200 w-full h-full"}
            >
                <View className="flex flex-col items-center justify-start gap-y-4 p-4 h-1/2">
                    <TextInput
                        editable
                        multiline
                        numberOfLines={10}
                        maxLength={256}
                        onChangeText={setText}
                        value={text}
                        className={"bg-slate-50 p-5 border-[1px] border-gray-500 h-1/2 w-full rounded-lg"}
                        placeholder={"Enter some text"}
                    />
                    <View
                        className={"flex flex-row items-center justify-evenly w-full"}
                    >
                        <Button title={"Load"} onPress={() => load_text()}/>
                        <Button title={"Save"} onPress={() => save_text()}/>
                        <Button title={"Clear"} onPress={() => clear_storage()} />
                    </View>
                </View>
                    <DialogWindow
                        isVisible={dialog_visible}
                        setIsVisible={setDialogVisible}
                        text_arr={arr}
                        remove_item={(index) => remove_item(index)}
                        setText={setText}
                    />
            </SafeAreaView>
        </SafeAreaProvider>
    );
}

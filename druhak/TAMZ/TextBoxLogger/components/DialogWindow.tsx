import {View, Text, Modal, Button} from 'react-native'

interface DialogProps {
    isVisible: boolean
    setIsVisible: (visible: boolean) => void
    text_arr: string[]
    remove_item: (index: number) => void
    setText: (text: string) => void
}

const DialogWindow = ({isVisible, text_arr, setIsVisible, remove_item, setText}: DialogProps) => {
    return (
        <Modal
            visible={isVisible}
            transparent={true}
            animationType={"fade"}
            className={"flex flex-col items-center justify-center w-full"}
        >
            <button
                className={"flex flex-col items-center justify-center h-full w-full px-12 border-white border-2 bg-gray-900/50"}
                onClick={() => setIsVisible(false)}
            >
                <View className={"flex flex-col items-center justify-center w-full bg-gray-700/50 px-4 py-4 gap-y-5 rounded-xl"}>
                    {text_arr.length > 0 ? text_arr.map((text, index) => (
                        <button
                            className={"flex flex-row items-center justify-between gap-x-5 bg-gray-700/80 px-4 py-2 rounded-lg w-full"}
                            key={index}
                            onClick={() => setText(text)}
                        >
                            <Text key={index}>{text}</Text>
                            <Button title={"remove"} onPress={() => remove_item(index)}/>
                        </button>
                    )) : (
                        <Text className={"text-gray-100 text-lg"}>No text to display</Text>
                    )}
                    <Button title={"Close"} onPress={() => setIsVisible(false)}/>
                </View>
            </button>
        </Modal>
    )
}
export default DialogWindow

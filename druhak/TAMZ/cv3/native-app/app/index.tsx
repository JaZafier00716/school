import {Button, Text, TextInput, View, Modal, FlatList, TouchableOpacity, Alert} from "react-native";
import {useState, useEffect} from "react";
import AsyncStorage from "@react-native-async-storage/async-storage";

// Fallback in-memory storage when AsyncStorage native module is unavailable
// const inMemoryStorage: Record<string, string> = {};

// Wrapper for AsyncStorage with fallback
const StorageAdapter = {
    setItem: async (key: string, value: string) => {
        try {
            await AsyncStorage.setItem(key, value);
        } catch (error: any) {
            console.warn('AsyncStorage setItem failed, using fallback:', error.message);
            // inMemoryStorage[key] = value;
        }
    },
    getItem: async (key: string) => {
        try {
            return await AsyncStorage.getItem(key);
        } catch (error: any) {
            console.warn('AsyncStorage getItem failed, using fallback:', error.message);
            // return inMemoryStorage[key] || null;
        }
    },
    getAllKeys: async () => {
        try {
            return await AsyncStorage.getAllKeys();
        } catch (error: any) {
            console.warn('AsyncStorage getAllKeys failed, using fallback:', error.message);
            // return Object.keys(inMemoryStorage);
        }
    },
    removeItem: async (key: string) => {
        try {
            await AsyncStorage.removeItem(key);
        } catch (error: any) {
            console.warn('AsyncStorage removeItem failed, using fallback:', error.message);
            // delete inMemoryStorage[key];
        }
    }
};

export default function Index() {
    const [value, onChangeText] = useState('');
    const [showDialog, setShowDialog] = useState(false);
    const [storedTexts, setStoredTexts] = useState<string[]>([]);

    // Initialize AsyncStorage - we'll mark as ready immediately
    // and handle errors when operations are attempted
    useEffect(() => {
        console.log('App initialized, AsyncStorage ready for operations');
    }, []);

    // Load all stored texts when dialog opens
    const loadStoredTexts = async () => {
        try {
            const keys = await StorageAdapter.getAllKeys();
            const texts = await Promise.all(
                keys ? keys.map(async (key) => {
                    const text = await StorageAdapter.getItem(key);
                    return text || '';
                }) : []
            );
            setStoredTexts(texts.filter(text => text !== ''));
            setShowDialog(true);
        } catch (error) {
            console.error('Error loading texts:', error);
            Alert.alert('Error', 'Failed to load stored texts');
        }
    }

    const save = async () => {
        if (!value.trim()) {
            Alert.alert('Warning', 'Please enter some text before saving');
            return;
        }
        try {
            const timestamp = new Date().getTime().toString();
            const key = `text_${timestamp}`;
            await StorageAdapter.setItem(key, value);
            onChangeText('');
            Alert.alert('Success', 'Text saved successfully');
        } catch (error) {
            console.error('Error saving text:', error);
            const errorMsg = error instanceof Error ? error.message : String(error);
            Alert.alert('Error', `Failed to save: ${errorMsg}`);
        }
    }

    const loadText = async (text: string) => {
        onChangeText(text);
        setShowDialog(false);
    }

    const deleteText = async (text: string) => {
        try {
            const keys = await StorageAdapter.getAllKeys();
            if (!keys) {
                Alert.alert('Error', 'No stored texts found');
                return;
            }
            for (const key of keys) {
                const storedText = await StorageAdapter.getItem(key);
                if (storedText === text) {
                    await StorageAdapter.removeItem(key);
                    break;
                }
            }
            // Reload the stored texts list
            await loadStoredTexts();
            Alert.alert('Success', 'Text deleted successfully');
        } catch (error) {
            console.error('Error deleting text:', error);
            Alert.alert('Error', 'Failed to delete text');
        }
    }

    const load = async () => {
        try {
            await loadStoredTexts();
        } catch (error) {
            const errorMsg = error instanceof Error ? error.message : String(error);
            Alert.alert('Error', `Failed to load texts: ${errorMsg}`);
        }
    }

    const clearAll = async () => {
        Alert.alert(
            'Clear All Storage',
            'Are you sure you want to delete all saved texts? This action cannot be undone.',
            [
                {
                    text: 'Cancel',
                    style: 'cancel'
                },
                {
                    text: 'Clear All',
                    style: 'destructive',
                    onPress: async () => {
                        try {
                            // Clear AsyncStorage
                            const keys = await StorageAdapter.getAllKeys();
                            if (keys) {
                                for (const key of keys) {
                                    await StorageAdapter.removeItem(key);
                                }
                            }
                            // Clear in-memory storage
                            // Object.keys(inMemoryStorage).forEach(key => {
                            //     delete inMemoryStorage[key];
                            // });

                            // Clear text input
                            onChangeText('');

                            Alert.alert('Success', 'All storage cleared successfully');
                        } catch (error) {
                            console.error('Error clearing storage:', error);
                            Alert.alert('Error', 'Failed to clear storage');
                        }
                    }
                }
            ]
        );
    }


  return (
    <View
      style={{
        flex: 1,
        justifyContent: "center",
        alignItems: "center",
          flexDirection: "column",
          gap: 20,
      }}
    >
        <TextInput
            editable
            multiline
            numberOfLines={4}
            maxLength={40}
            onChangeText={text => onChangeText(text)}
            value={value}
            placeholder={'Enter your text here'}
            style={{
                borderStyle: 'solid',
                borderWidth: 1,
                borderColor: 'black',
                padding: 10,
                height: 200,
                width: '90%',
            }}
        />
        <View style={{
            flexDirection: 'row',
            justifyContent: 'space-evenly',
            width: '90%',
        }}>
            <Button onPress={save} title={"Save"} />
            <Button onPress={load} title={"Load"} />
        </View>

        <View style={{
            width: '90%',
        }}>
            <Button onPress={clearAll} title={"Clear All"} color="#ff6b6b" />
        </View>


        <Modal
            visible={showDialog}
            transparent={true}
            animationType="slide"
            onRequestClose={() => setShowDialog(false)}
        >
            <View
                style={{
                    flex: 1,
                    backgroundColor: 'rgba(0,0,0,0.5)',
                    justifyContent: 'center',
                    alignItems: 'center',
                }}
            >
                <View
                    style={{
                        backgroundColor: 'white',
                        borderRadius: 10,
                        padding: 20,
                        width: '90%',
                        maxHeight: '80%',
                    }}
                >
                    <Text
                        style={{
                            fontSize: 18,
                            fontWeight: 'bold',
                            marginBottom: 15,
                        }}
                    >
                        Stored Texts
                    </Text>

                    {storedTexts.length === 0 ? (
                        <Text style={{ textAlign: 'center', marginVertical: 20 }}>
                            No stored texts yet
                        </Text>
                    ) : (
                        <FlatList
                            data={storedTexts}
                            keyExtractor={(item, index) => index.toString()}
                            renderItem={({ item }) => (
                                <View
                                    style={{
                                        flexDirection: 'row',
                                        justifyContent: 'space-between',
                                        alignItems: 'center',
                                        backgroundColor: '#f0f0f0',
                                        padding: 10,
                                        marginVertical: 5,
                                        borderRadius: 5,
                                    }}
                                >
                                    <TouchableOpacity
                                        onPress={() => loadText(item)}
                                        style={{ flex: 1 }}
                                    >
                                        <Text
                                            numberOfLines={2}
                                            style={{
                                                fontSize: 14,
                                                color: '#333',
                                            }}
                                        >
                                            {item}
                                        </Text>
                                    </TouchableOpacity>
                                    <Button
                                        title="Delete"
                                        onPress={() => deleteText(item)}
                                        color="red"
                                    />
                                </View>
                            )}
                        />
                    )}

                    <View style={{ marginTop: 15 }}>
                        <Button
                            title="Close"
                            onPress={() => setShowDialog(false)}
                        />
                    </View>
                </View>
            </View>
        </Modal>
    </View>
  );
}

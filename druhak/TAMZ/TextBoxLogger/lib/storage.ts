import { Platform } from 'react-native';

/**
 * Cross-platform storage adapter using createAsyncStorage pattern
 * Uses createAsyncStorage on native platforms and localStorage on web
 */

let storageImpl: any = null;

async function initStorage() {
  if (storageImpl) return storageImpl;

  if (Platform.OS === 'web') {
    // Use localStorage on web
    storageImpl = {
      getItem: async (key: string) => {
        try {
          return localStorage.getItem(key);
        } catch (e) {
          console.error('localStorage getItem error:', e);
          return null;
        }
      },
      setItem: async (key: string, value: string) => {
        try {
          localStorage.setItem(key, value);
        } catch (e) {
          console.error('localStorage setItem error:', e);
        }
      },
      removeItem: async (key: string) => {
        try {
          localStorage.removeItem(key);
        } catch (e) {
          console.error('localStorage removeItem error:', e);
        }
      },
      clear: async () => {
        try {
          localStorage.clear();
        } catch (e) {
          console.error('localStorage clear error:', e);
        }
      },
    };
  } else {
    // Use createAsyncStorage on native platforms
    try {
      const { createAsyncStorage } = await import('@react-native-async-storage/async-storage');
      storageImpl = createAsyncStorage('appDB');
    } catch (e) {
      console.error('Failed to create AsyncStorage instance:', e);
      // Fallback to memory storage
      const memoryStorage: any = {};
      storageImpl = {
        getItem: async (key: string) => memoryStorage[key] ?? null,
        setItem: async (key: string, value: string) => {
          memoryStorage[key] = value;
        },
        removeItem: async (key: string) => {
          delete memoryStorage[key];
        },
        clear: async () => {
          Object.keys(memoryStorage).forEach(key => delete memoryStorage[key]);
        },
      };
    }
  }

  return storageImpl;
}

export const Storage = {
  getItem: async (key: string) => {
    const impl = await initStorage();
    return impl.getItem(key);
  },
  setItem: async (key: string, value: string) => {
    const impl = await initStorage();
    return impl.setItem(key, value);
  },
  removeItem: async (key: string) => {
    const impl = await initStorage();
    return impl.removeItem(key);
  },
  clear: async () => {
    const impl = await initStorage();
    return impl.clear();
  },
};


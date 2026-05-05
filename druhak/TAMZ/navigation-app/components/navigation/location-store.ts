// Lightweight cross-platform storage for saved locations.
// On web we use idb-keyval (IndexedDB). On native we persist a JSON array in AsyncStorage.
import { Platform } from 'react-native';

export type SavedLocation = {
  id: string;
  name: string;
  latitude: number;
  longitude: number;
  createdAt: number;
};

const STORAGE_KEY = 'navigation_app:saved_locations_v1';

let idbKeyval: any = null;
if (Platform.OS === 'web') {
  // import lazily so native bundles don't try to resolve this module
  // eslint-disable-next-line @typescript-eslint/no-var-requires
  idbKeyval = require('idb-keyval');
}

const readNative = async (): Promise<SavedLocation[]> => {
  // AsyncStorage is resolved lazily to avoid failing on web bundling
  // eslint-disable-next-line @typescript-eslint/no-var-requires
  const AsyncStorage = require('@react-native-async-storage/async-storage').default;
  const json = await AsyncStorage.getItem(STORAGE_KEY);
  if (!json) return [];
  try {
    const parsed = JSON.parse(json);
    return Array.isArray(parsed) ? parsed : [];
  } catch {
    return [];
  }
};

const writeNative = async (items: SavedLocation[]) => {
  const AsyncStorage = require('@react-native-async-storage/async-storage').default;
  await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(items));
};

export async function getAllLocations(): Promise<SavedLocation[]> {
  if (Platform.OS === 'web' && idbKeyval) {
    try {
      const all = await idbKeyval.get(STORAGE_KEY);
      return Array.isArray(all) ? all : [];
    } catch {
      return [];
    }
  }

  return await readNative();
}

export async function saveLocation(loc: Omit<SavedLocation, 'id' | 'createdAt'> & { id?: string }) {
  const now = Date.now();
  const item: SavedLocation = {
    id: loc.id ?? `${now}`,
    name: loc.name,
    latitude: loc.latitude,
    longitude: loc.longitude,
    createdAt: now,
  };

  const existing = await getAllLocations();
  // replace if same id, otherwise push
  const filtered = existing.filter((x) => x.id !== item.id);
  filtered.unshift(item);

  if (Platform.OS === 'web' && idbKeyval) {
    await idbKeyval.set(STORAGE_KEY, filtered);
    return item;
  }

  await writeNative(filtered);
  return item;
}

export async function deleteLocation(id: string) {
  const existing = await getAllLocations();
  const filtered = existing.filter((x) => x.id !== id);
  if (Platform.OS === 'web' && idbKeyval) {
    await idbKeyval.set(STORAGE_KEY, filtered);
    return;
  }

  await writeNative(filtered);
}


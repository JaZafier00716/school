import AsyncStorage from "@react-native-async-storage/async-storage";
import type { HistoryEntry } from "./types";

const KEY = "@bmi:history";

export async function getHistory(): Promise<HistoryEntry[]> {
  try {
    const raw = await AsyncStorage.getItem(KEY);
    return raw ? (JSON.parse(raw) as HistoryEntry[]) : [];
  } catch {
    return [];
  }
}

export async function saveEntry(entry: HistoryEntry): Promise<void> {
  const all = await getHistory();
  const i = all.findIndex((e) => e.id === entry.id);
  if (i >= 0) {
    all[i] = entry;
  } else {
    all.unshift(entry);
  }
  await AsyncStorage.setItem(KEY, JSON.stringify(all));
}

export async function deleteEntry(id: string): Promise<void> {
  const all = await getHistory();
  await AsyncStorage.setItem(KEY, JSON.stringify(all.filter((e) => e.id !== id)));
}


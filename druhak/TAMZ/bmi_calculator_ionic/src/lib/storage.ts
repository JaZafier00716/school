import type { HistoryEntry } from "./types";

const DB_NAME = "BMICalculatorDB";
const STORE_NAME = "history";
const STORAGE_KEY = "@bmi:history-cache";

// Initialize IndexedDB
function initDB(): Promise<IDBDatabase> {
  return new Promise((resolve, reject) => {
    const request = indexedDB.open(DB_NAME, 1);

    request.onerror = () => reject(request.error);
    request.onsuccess = () => resolve(request.result);

    request.onupgradeneeded = (event) => {
      const db = (event.target as IDBOpenDBRequest).result;
      if (!db.objectStoreNames.contains(STORE_NAME)) {
        db.createObjectStore(STORE_NAME, { keyPath: "id" });
      }
    };
  });
}

// Get all history entries from IndexedDB
export async function getHistory(): Promise<HistoryEntry[]> {
  try {
    const db = await initDB();
    return new Promise((resolve, reject) => {
      const transaction = db.transaction(STORE_NAME, "readonly");
      const store = transaction.objectStore(STORE_NAME);
      const request = store.getAll();

      request.onerror = () => reject(request.error);
      request.onsuccess = () => {
        const entries = request.result as HistoryEntry[];
        resolve(entries.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()));
      };
    });
  } catch (error) {
    console.error("Error getting history:", error);
    // Try to recover from localStorage cache
    try {
      const cached = localStorage.getItem(STORAGE_KEY);
      return cached ? (JSON.parse(cached) as HistoryEntry[]) : [];
    } catch {
      return [];
    }
  }
}

// Save or update a single entry in IndexedDB
export async function saveEntry(entry: HistoryEntry): Promise<void> {
  try {
    const db = await initDB();
    return new Promise((resolve, reject) => {
      const transaction = db.transaction(STORE_NAME, "readwrite");
      const store = transaction.objectStore(STORE_NAME);
      const request = store.put(entry);

      request.onerror = () => reject(request.error);
      request.onsuccess = async () => {
        // Update localStorage cache
        const allEntries = await getHistory();
        localStorage.setItem(STORAGE_KEY, JSON.stringify(allEntries));
        resolve();
      };
    });
  } catch (error) {
    console.error("Error saving entry:", error);
    throw error;
  }
}

// Delete an entry from IndexedDB
export async function deleteEntry(id: string): Promise<void> {
  try {
    const db = await initDB();
    return new Promise((resolve, reject) => {
      const transaction = db.transaction(STORE_NAME, "readwrite");
      const store = transaction.objectStore(STORE_NAME);
      const request = store.delete(id);

      request.onerror = () => reject(request.error);
      request.onsuccess = async () => {
        // Update localStorage cache
        const allEntries = await getHistory();
        localStorage.setItem(STORAGE_KEY, JSON.stringify(allEntries));
        resolve();
      };
    });
  } catch (error) {
    console.error("Error deleting entry:", error);
    throw error;
  }
}

// Get a single entry by ID
export async function getEntry(id: string): Promise<HistoryEntry | undefined> {
  try {
    const db = await initDB();
    return new Promise((resolve, reject) => {
      const transaction = db.transaction(STORE_NAME, "readonly");
      const store = transaction.objectStore(STORE_NAME);
      const request = store.get(id);

      request.onerror = () => reject(request.error);
      request.onsuccess = () => resolve(request.result as HistoryEntry | undefined);
    });
  } catch (error) {
    console.error("Error getting entry:", error);
    return undefined;
  }
}


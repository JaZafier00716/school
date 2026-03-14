import { useCallback, useRef, useState } from "react";
import { FlatList, Pressable, Text, View } from "react-native";
import { Swipeable } from "react-native-gesture-handler";
import { useFocusEffect, useRouter } from "expo-router";
import { deleteEntry, getHistory } from "../../lib/storage";
import type { HistoryEntry } from "../../lib/types";

function formatDate(iso: string): string {
  return new Date(iso).toLocaleString(undefined, {
    day: "2-digit",
    month: "short",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
}

export default function HistoryScreen() {
  const [entries, setEntries] = useState<HistoryEntry[]>([]);
  const router = useRouter();
  const swipeableRefs = useRef<Map<string, Swipeable>>(new Map());

  // Reload list every time the tab is focused (e.g. after returning from edit)
  useFocusEffect(
    useCallback(() => {
      getHistory().then(setEntries);
    }, [])
  );

  const handleDelete = async (id: string) => {
    swipeableRefs.current.get(id)?.close();
    await deleteEntry(id);
    setEntries((prev) => prev.filter((e) => e.id !== id));
  };

  const handleEdit = (entry: HistoryEntry) => {
    swipeableRefs.current.get(entry.id)?.close();
    router.navigate({
      pathname: "/bmi",
      params: {
        editId: entry.id,
        username: entry.username,
        age: entry.age,
        gender: entry.gender,
        heightCm: String(entry.heightCm),
        weightKg: String(entry.weightKg),
      },
    } as never);
  };

  const renderRightActions = (entry: HistoryEntry) => (
    <View className="flex-row">
      <Pressable
        onPress={() => handleEdit(entry)}
        style={{ width: 72, alignItems: "center", justifyContent: "center", backgroundColor: "#3b82f6" }}
      >
        <Text className="text-sm font-semibold text-white">Edit</Text>
      </Pressable>
      <Pressable
        onPress={() => handleDelete(entry.id)}
        style={{ width: 72, alignItems: "center", justifyContent: "center", backgroundColor: "#ef4444" }}
      >
        <Text className="text-sm font-semibold text-white">Delete</Text>
      </Pressable>
    </View>
  );

  if (entries.length === 0) {
    return (
      <View className="flex-1 items-center justify-center bg-slate-100 gap-2">
        <Text className="text-base text-slate-500">No calculations yet.</Text>
        <Text className="text-sm text-slate-400">Go to BMI and submit a result.</Text>
      </View>
    );
  }

  return (
    <FlatList
      data={entries}
      keyExtractor={(item) => item.id}
      style={{ backgroundColor: "#f1f5f9" }}
      contentContainerStyle={{ paddingBottom: 32 }}
      ItemSeparatorComponent={() => <View style={{ height: 1, backgroundColor: "#e2e8f0" }} />}
      renderItem={({ item }) => (
        <Swipeable
          ref={(ref) => {
            if (ref) swipeableRefs.current.set(item.id, ref);
            else swipeableRefs.current.delete(item.id);
          }}
          renderRightActions={() => renderRightActions(item)}
          rightThreshold={40}
        >
          <View className="bg-white px-5 py-4">
            {/* Row 1: name + BMI value */}
            <View className="flex-row items-center justify-between">
              <Text className="text-lg font-bold text-slate-900">{item.username}</Text>
              <Text className="text-2xl font-extrabold text-blue-600">{item.bmi.toFixed(1)}</Text>
            </View>
            {/* Row 2: date + category */}
            <View className="mt-1 flex-row items-center justify-between">
              <Text className="text-xs text-slate-400">{formatDate(item.createdAt)}</Text>
              <Text className="text-sm font-medium text-slate-600">{item.category}</Text>
            </View>
            {/* Row 3: detail chips */}
            <View className="mt-2 flex-row gap-3">
              {[`${item.heightCm} cm`, `${item.weightKg} kg`, item.gender, `Age ${item.age}`].map((chip) => (
                <Text key={chip} className="rounded bg-slate-100 px-2 py-0.5 text-xs capitalize text-slate-500">
                  {chip}
                </Text>
              ))}
              {item.isInfantFormula ? (
                <Text className="rounded bg-amber-100 px-2 py-0.5 text-xs text-amber-700">Infant</Text>
              ) : null}
            </View>
          </View>
        </Swipeable>
      )}
    />
  );
}


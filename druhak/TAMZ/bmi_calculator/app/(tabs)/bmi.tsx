import { useEffect, useRef, useState } from "react";
import Slider from "@react-native-community/slider";
import { Pressable, ScrollView, Text, TextInput, View } from "react-native";
import Modal from "react-native-modal";
import { useLocalSearchParams, useRouter } from "expo-router";
import { saveEntry } from "../../lib/storage";
import type { Gender, HistoryEntry } from "../../lib/types";

const INFANT_AGE_THRESHOLD_YEARS = 2;

type FormErrors = { username?: string; age?: string };

type Snapshot = {
  username: string;
  age: string;
  gender: Gender;
  heightCm: number;
  weightKg: number;
};

function getBmiCategory(value: number, isInfant: boolean): string {
  if (isInfant) {
    if (value < 23) return "Underweight";
    if (value <= 25) return "Normal weight";
    return "Overweight";
  }
  if (value < 18.5) return "Underweight";
  if (value < 25) return "Normal weight";
  if (value < 30) return "Overweight";
  return "Obesity";
}

function calculateBodyIndex(ageYears: number, heightCm: number, weightKg: number) {
  const hm = heightCm / 100;
  const isInfant = ageYears < INFANT_AGE_THRESHOLD_YEARS;
  return { value: isInfant ? weightKg / hm ** 3 : weightKg / hm ** 2, isInfant };
}

export default function BmiScreen() {
  const router = useRouter();
  const params = useLocalSearchParams<{
    editId?: string;
    username?: string;
    age?: string;
    gender?: string;
    heightCm?: string;
    weightKg?: string;
  }>();

  const [username, setUsername] = useState("");
  const [age, setAge] = useState("");
  const [gender, setGender] = useState<Gender>("female");
  const [heightCm, setHeightCm] = useState(170);
  const [weightKg, setWeightKg] = useState(70);
  const [errors, setErrors] = useState<FormErrors>({});
  const [editId, setEditId] = useState<string | null>(null);

  const [isResultVisible, setIsResultVisible] = useState(false);
  const [bmi, setBmi] = useState<number | null>(null);
  const [usedInfantFormula, setUsedInfantFormula] = useState(false);
  const [snapshot, setSnapshot] = useState<Snapshot | null>(null);

  // Pre-fill form when arriving from the History "Edit" action
  const processedEditId = useRef<string | null>(null);
  useEffect(() => {
    if (params.editId && params.editId !== processedEditId.current) {
      processedEditId.current = params.editId;
      setEditId(params.editId);
      setUsername(params.username ?? "");
      setAge(params.age ?? "");
      if (params.gender === "female" || params.gender === "male") setGender(params.gender);
      if (params.heightCm) setHeightCm(Number(params.heightCm));
      if (params.weightKg) setWeightKg(Number(params.weightKg));
    }
  }, [params.editId, params.username, params.age, params.gender, params.heightCm, params.weightKg]);

  const handleSubmit = async () => {
    const nextErrors: FormErrors = {};
    if (!username.trim()) nextErrors.username = "Username is required.";
    if (!age.trim()) {
      nextErrors.age = "Age is required.";
    } else {
      const n = Number(age.trim());
      if (!Number.isFinite(n) || n <= 0)
        nextErrors.age = "Age must be a valid number greater than 0.";
    }
    if (Object.keys(nextErrors).length > 0) { setErrors(nextErrors); return; }
    setErrors({});

    const ageYears = Number(age.trim());
    const result = calculateBodyIndex(ageYears, heightCm, weightKg);
    const category = getBmiCategory(result.value, result.isInfant);

    const entry: HistoryEntry = {
      id: editId ?? Date.now().toString(),
      username: username.trim(),
      age: age.trim(),
      gender,
      heightCm,
      weightKg,
      bmi: result.value,
      isInfantFormula: result.isInfant,
      category,
      createdAt: new Date().toISOString(),
    };
    await saveEntry(entry);

    setBmi(result.value);
    setUsedInfantFormula(result.isInfant);
    setSnapshot({ username: username.trim(), age: age.trim(), gender, heightCm, weightKg });
    setIsResultVisible(true);
  };

  const getResultMessage = () => {
    if (!snapshot || bmi === null) return "";
    const name = snapshot.username || "There";
    const bmiStr = bmi.toFixed(1);
    const cat = getBmiCategory(bmi, usedInfantFormula);
    if (cat === "Normal weight") return `Hi ${name}, your BMI is ${bmiStr} and you have a normal weight.`;
    if (cat === "Underweight") return `Hi ${name}, your BMI is ${bmiStr} and you are underweight.`;
    if (cat === "Overweight") return `Hi ${name}, your BMI is ${bmiStr} and you are overweight.`;
    return `Hi ${name}, your BMI is ${bmiStr} and you are in the obesity range.`;
  };

  const handleCloseResult = () => {
    setIsResultVisible(false);
    if (editId) {
      setEditId(null);
      processedEditId.current = null;
      router.navigate("/history" as never);
    }
  };

  return (
    <ScrollView className="flex-1 bg-slate-300" contentContainerStyle={{ padding: 24, gap: 24, paddingBottom: 48 }}>
      {editId ? (
        <View className="rounded-lg bg-blue-100 px-4 py-2">
          <Text className="text-sm font-medium text-blue-700">Editing an existing record</Text>
        </View>
      ) : null}

      {/* Username */}
      <View className="gap-1">
        <TextInput
          placeholder="Username"
          value={username}
          onChangeText={(v) => { setUsername(v); if (errors.username) setErrors((p) => ({ ...p, username: undefined })); }}
          className="w-full rounded bg-gray-200 border border-gray-600 px-4 py-2"
        />
        {errors.username ? <Text className="text-sm text-red-600">{errors.username}</Text> : null}
      </View>

      {/* Age */}
      <View className="gap-1">
        <TextInput
          placeholder="Age"
          value={age}
          onChangeText={(v) => { setAge(v); if (errors.age) setErrors((p) => ({ ...p, age: undefined })); }}
          keyboardType="numeric"
          className="w-full rounded bg-gray-200 border border-gray-600 px-4 py-2"
        />
        {errors.age ? <Text className="text-sm text-red-600">{errors.age}</Text> : null}
      </View>

      {/* Gender */}
      <View className="gap-3">
        <Text className="text-base font-semibold text-slate-800">Gender</Text>
        <View className="flex-row gap-6">
          {(["female", "male"] as Gender[]).map((option) => {
            const isSelected = gender === option;
            return (
              <Pressable key={option} onPress={() => setGender(option)} className="flex-row items-center gap-2">
                <View className={`h-5 w-5 rounded-full border-2 ${isSelected ? "border-blue-600" : "border-gray-500"}`}>
                  {isSelected ? <View className="m-[3px] h-2.5 w-2.5 rounded-full bg-blue-600" /> : null}
                </View>
                <Text className="capitalize text-slate-900">{option}</Text>
              </Pressable>
            );
          })}
        </View>
      </View>

      {/* Height */}
      <View className="gap-1">
        <Text className="text-base font-semibold text-slate-800">Height: {heightCm} cm</Text>
        <Slider minimumValue={50} maximumValue={250} step={1} value={heightCm} onValueChange={setHeightCm}
          minimumTrackTintColor="#2563eb" maximumTrackTintColor="#94a3b8" thumbTintColor="#2563eb" />
      </View>

      {/* Weight */}
      <View className="gap-1">
        <Text className="text-base font-semibold text-slate-800">Weight: {weightKg} kg</Text>
        <Slider minimumValue={2} maximumValue={250} step={1} value={weightKg} onValueChange={setWeightKg}
          minimumTrackTintColor="#2563eb" maximumTrackTintColor="#94a3b8" thumbTintColor="#2563eb" />
      </View>

      {/* Submit */}
      <Pressable onPress={handleSubmit} className="w-full rounded-lg bg-blue-600 px-4 py-3">
        <Text className="text-center text-base font-semibold text-white">
          {editId ? "Update" : "Calculate BMI"}
        </Text>
      </Pressable>

      {/* Result bottom sheet */}
      <Modal
        isVisible={isResultVisible}
        onBackdropPress={handleCloseResult}
        onSwipeComplete={handleCloseResult}
        swipeDirection="down"
        style={{ justifyContent: "flex-end", margin: 0 }}
      >
        <View className="rounded-t-3xl bg-white px-6 pb-12 pt-4">
          <View className="mb-5 items-center">
            <View className="h-1.5 w-14 rounded-full bg-slate-300" />
          </View>

          <Text className="text-2xl font-bold text-slate-900">
            {usedInfantFormula ? "Infant BMI Result" : "BMI Result"}
          </Text>
          <Text className="mt-3 text-5xl font-extrabold text-blue-600">
            {bmi?.toFixed(1) ?? "-"}
          </Text>
          <Text className="mt-3 text-base leading-relaxed text-slate-700">
            {getResultMessage()}
          </Text>

          <View className="mt-6 gap-2 rounded-xl bg-slate-100 px-4 py-4">
            {(
              [
                ["Username", snapshot?.username],
                ["Age", snapshot?.age],
                ["Gender", snapshot?.gender],
                ["Height", snapshot ? `${snapshot.heightCm} cm` : undefined],
                ["Weight", snapshot ? `${snapshot.weightKg} kg` : undefined],
                ["Formula", usedInfantFormula ? `Infant-adjusted (age < ${INFANT_AGE_THRESHOLD_YEARS} yrs)` : "Standard BMI"],
              ] as [string, string | undefined][]
            ).map(([label, value]) => (
              <View key={label} className="flex-row justify-between">
                <Text className="font-medium text-slate-500">{label}</Text>
                <Text className="capitalize text-slate-900">{value ?? "-"}</Text>
              </View>
            ))}
          </View>

          <Text className="mt-5 text-center text-sm text-slate-400">
            Swipe down or tap outside to close.
          </Text>
        </View>
      </Modal>
    </ScrollView>
  );
}


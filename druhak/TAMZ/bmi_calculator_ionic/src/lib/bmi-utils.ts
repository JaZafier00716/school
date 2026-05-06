import type { HistoryEntry, BMICalculationInput } from "./types";

export type BMIResult = {
  bmi: number;
  category: string;
  isInfantFormula: boolean;
};

export function calculateBMI(input: BMICalculationInput): BMIResult {
  const age = parseInt(input.age, 10);
  const heightMeters = input.heightCm / 100;
  const isInfant = age < 2;

  let bmi: number;
  let category: string;

  if (isInfant) {
    // Infant formula: weight / height^3
    bmi = input.weightKg / (heightMeters * heightMeters * heightMeters);
    category = getInfantCategory(bmi);
  } else {
    // Standard formula: weight / height^2
    bmi = input.weightKg / (heightMeters * heightMeters);
    category = getStandardCategory(bmi);
  }

  return {
    bmi,
    category,
    isInfantFormula: isInfant,
  };
}

function getInfantCategory(bmi: number): string {
  if (bmi < 23) return "Underweight";
  if (bmi <= 25) return "Normal weight";
  return "Overweight";
}

function getStandardCategory(bmi: number): string {
  if (bmi < 18.5) return "Underweight";
  if (bmi < 25) return "Normal weight";
  if (bmi < 30) return "Overweight";
  return "Obesity";
}

export function createHistoryEntry(
  input: BMICalculationInput,
  bmiResult: BMIResult,
  id: string = crypto.randomUUID()
): HistoryEntry {
  return {
    id,
    username: input.username,
    age: input.age,
    gender: input.gender,
    heightCm: input.heightCm,
    weightKg: input.weightKg,
    bmi: bmiResult.bmi,
    isInfantFormula: bmiResult.isInfantFormula,
    category: bmiResult.category,
    createdAt: new Date().toISOString(),
  };
}

export function getResultMessage(username: string, bmi: number, category: string): string {
  const bmiFormatted = bmi.toFixed(1);

  if (category === "Underweight") {
    return `Hi ${username}, your BMI is ${bmiFormatted} and you are underweight.`;
  }
  if (category === "Normal weight") {
    return `Hi ${username}, your BMI is ${bmiFormatted} and you have a normal weight.`;
  }
  if (category === "Overweight") {
    return `Hi ${username}, your BMI is ${bmiFormatted} and you are overweight.`;
  }
  if (category === "Obesity") {
    return `Hi ${username}, your BMI is ${bmiFormatted} and you are in the obesity range.`;
  }

  return `Hi ${username}, your BMI is ${bmiFormatted}.`;
}

export function formatDate(isoString: string): string {
  return new Date(isoString).toLocaleString(undefined, {
    day: "2-digit",
    month: "short",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
}


export type Gender = "female" | "male";

export type HistoryEntry = {
  id: string;
  username: string;
  age: string;
  gender: Gender;
  heightCm: number;
  weightKg: number;
  bmi: number;
  isInfantFormula: boolean;
  category: string;
  createdAt: string; // ISO string
};


import { useState, useEffect } from "react";
import {
  IonPage,
  IonContent,
  IonInput,
  IonRange,
  IonRadioGroup,
  IonRadio,
  IonButton,
  IonItem,
  IonLabel,
  IonText,
  IonHeader,
  IonToolbar,
  IonTitle,
  useIonRouter,
  IonList,
} from "@ionic/react";
import { useParams } from "react-router-dom";

import type { Gender, BMICalculationInput, HistoryEntry } from "../lib/types";
import { calculateBMI, createHistoryEntry } from "../lib/bmi-utils";
import { saveEntry, getEntry } from "../lib/storage";
import ResultModal from "../components/ResultModal";

import "./BMIPage.css";

export default function BMIPage() {
  const params = useParams<{ editId?: string }>();
  const ionRouter = useIonRouter();
  const editId = params.editId;

  const [isLoading, setIsLoading] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [showResultModal, setShowResultModal] = useState(false);

  const [formData, setFormData] = useState<BMICalculationInput>({
    username: "",
    age: "",
    gender: "female",
    heightCm: 170,
    weightKg: 70,
  });

  const [resultData, setResultData] = useState<HistoryEntry | null>(null);
  const [errors, setErrors] = useState<Record<string, string>>({});

  // Load entry if editing — defined inline so effect has all dependencies
  useEffect(() => {
    const load = async () => {
      if (!editId) {
        setIsEditing(false);
        return;
      }

      setIsLoading(true);
      try {
        const entry = await getEntry(editId);
        if (entry) {
          setFormData({
            username: entry.username,
            age: entry.age,
            gender: entry.gender,
            heightCm: entry.heightCm,
            weightKg: entry.weightKg,
          });
          setIsEditing(true);
        }
      } catch (error) {
        console.error("Error loading entry:", error);
      } finally {
        setIsLoading(false);
      }
    };

    load();
  }, [editId]);

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {};

    if (!formData.username.trim()) {
      newErrors.username = "Username is required";
    }

    if (!formData.age.trim()) {
      newErrors.age = "Age is required";
    } else if (isNaN(Number(formData.age)) || Number(formData.age) <= 0) {
      newErrors.age = "Age must be a valid number greater than 0";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async () => {
    if (!validateForm()) {
      return;
    }

    setIsLoading(true);
    try {
      const bmiResult = calculateBMI(formData);
      const entry = createHistoryEntry(
        formData,
        bmiResult,
        editId || crypto.randomUUID()
      );

      await saveEntry(entry);
      setResultData(entry);
      setShowResultModal(true);
    } catch (error) {
      console.error("Error calculating BMI:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleResultModalClose = () => {
    setShowResultModal(false);
    if (isEditing) {
      // Go back to history after editing
      ionRouter.push("/history", "back");
    } else {
      // Reset form for new entry
      setFormData({
        username: "",
        age: "",
        gender: "female",
        heightCm: 170,
        weightKg: 70,
      });
      setErrors({});
    }
  };

  return (
    <IonPage>
      <IonHeader>
        <IonToolbar>
          <IonTitle>{isEditing ? "Edit BMI" : "Calculate BMI"}</IonTitle>
        </IonToolbar>
      </IonHeader>

      <IonContent className="ion-padding">
        {isEditing && (
          <IonText color="warning" className="block mb-4">
            <p>Editing an existing record</p>
          </IonText>
        )}

        <IonList>
          {/* Username */}
          <IonItem>
            <IonLabel position="stacked">Username</IonLabel>
            <IonInput
              type="text"
              placeholder="Enter your name"
              value={formData.username}
              onIonChange={(e) =>
                setFormData({ ...formData, username: String(e.detail.value) })
              }
            />
            {errors.username && (
              <IonText color="danger" className="error-text">
                {errors.username}
              </IonText>
            )}
          </IonItem>

          {/* Age */}
          <IonItem>
            <IonLabel position="stacked">Age (years)</IonLabel>
            <IonInput
              type="number"
              placeholder="Enter your age"
              value={formData.age}
              onIonChange={(e) =>
                setFormData({ ...formData, age: String(e.detail.value) })
              }
            />
            {errors.age && (
              <IonText color="danger" className="error-text">
                {errors.age}
              </IonText>
            )}
          </IonItem>

          {/* Gender */}
          <IonItem>
            <IonLabel>Gender</IonLabel>
          </IonItem>
          <IonItem>
            <IonLabel>Female</IonLabel>
            <IonRadioGroup
              value={formData.gender}
              onIonChange={(e) =>
                setFormData({ ...formData, gender: String(e.detail.value) as Gender })
              }
            >
              <IonRadio slot="start" value="female" />
            </IonRadioGroup>
          </IonItem>
          <IonItem>
            <IonLabel>Male</IonLabel>
            <IonRadioGroup
              value={formData.gender}
              onIonChange={(e) =>
                setFormData({ ...formData, gender: String(e.detail.value) as Gender })
              }
            >
              <IonRadio slot="start" value="male" />
            </IonRadioGroup>
          </IonItem>

          {/* Height */}
          <IonItem>
            <IonLabel>Height: {formData.heightCm} cm</IonLabel>
          </IonItem>
          <IonItem>
            <IonRange
              min={50}
              max={250}
              step={1}
              value={formData.heightCm}
              onIonChange={(e) =>
                setFormData({
                  ...formData,
                  heightCm: Number(e.detail.value),
                })
              }
            />
          </IonItem>

          {/* Weight */}
          <IonItem>
            <IonLabel>Weight: {formData.weightKg} kg</IonLabel>
          </IonItem>
          <IonItem>
            <IonRange
              min={2}
              max={250}
              step={1}
              value={formData.weightKg}
              onIonChange={(e) =>
                setFormData({
                  ...formData,
                  weightKg: Number(e.detail.value),
                })
              }
            />
          </IonItem>
        </IonList>

        {/* Submit Button */}
        <div className="ion-margin-top">
          <IonButton
            expand="block"
            disabled={isLoading}
            onClick={handleSubmit}
          >
            {isLoading ? "Calculating..." : isEditing ? "Update" : "Calculate BMI"}
          </IonButton>
        </div>

        {/* Result Modal */}
        {resultData && (
          <ResultModal
            isOpen={showResultModal}
            onClose={handleResultModalClose}
            entry={resultData}
          />
        )}
      </IonContent>
    </IonPage>
  );
}


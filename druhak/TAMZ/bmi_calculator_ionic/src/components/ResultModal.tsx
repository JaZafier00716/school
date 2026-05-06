import {
  IonModal,
  IonHeader,
  IonToolbar,
  IonTitle,
  IonContent,
  IonButton,
  IonText,
  IonList,
  IonItem,
  IonLabel,
} from "@ionic/react";

import type { HistoryEntry } from "../lib/types";
import { getResultMessage } from "../lib/bmi-utils";

import "./ResultModal.css";

interface ResultModalProps {
  isOpen: boolean;
  onClose: () => void;
  entry: HistoryEntry;
}

export default function ResultModal({ isOpen, onClose, entry }: ResultModalProps) {
  const message = getResultMessage(entry.username, entry.bmi, entry.category);

  return (
    <IonModal isOpen={isOpen} onDidDismiss={onClose} initialBreakpoint={0.75} breakpoints={[0, 0.75, 1]}>
      <IonHeader>
        <IonToolbar>
          <IonTitle>{entry.isInfantFormula ? "Infant BMI Result" : "BMI Result"}</IonTitle>
        </IonToolbar>
      </IonHeader>

      <IonContent className="ion-padding result-content">
        {/* Message and BMI Value */}
        <div className="result-header">
          <IonText className="result-message">
            <p>{message}</p>
          </IonText>
          <div className="result-bmi-display">
            <div className="result-bmi-value">{entry.bmi.toFixed(1)}</div>
            <div className="result-bmi-category">{entry.category}</div>
          </div>
        </div>

        {/* Details */}
        <h3>Details</h3>
        <IonList>
          <IonItem>
            <IonLabel>
              <p>Username</p>
              <h2>{entry.username}</h2>
            </IonLabel>
          </IonItem>
          <IonItem>
            <IonLabel>
              <p>Age</p>
              <h2>{entry.age} years</h2>
            </IonLabel>
          </IonItem>
          <IonItem>
            <IonLabel>
              <p>Gender</p>
              <h2>{entry.gender}</h2>
            </IonLabel>
          </IonItem>
          <IonItem>
            <IonLabel>
              <p>Height</p>
              <h2>{entry.heightCm} cm</h2>
            </IonLabel>
          </IonItem>
          <IonItem>
            <IonLabel>
              <p>Weight</p>
              <h2>{entry.weightKg} kg</h2>
            </IonLabel>
          </IonItem>
          <IonItem>
            <IonLabel>
              <p>Formula</p>
              <h2>{entry.isInfantFormula ? "Infant-adjusted (weight/height³)" : "Standard BMI (weight/height²)"}</h2>
            </IonLabel>
          </IonItem>
        </IonList>

        {/* Close Button */}
        <div className="ion-margin-top">
          <IonButton expand="block" onClick={onClose}>
            Close
          </IonButton>
        </div>
      </IonContent>
    </IonModal>
  );
}


import { useState } from "react";
import {
  IonPage,
  IonContent,
  IonList,
  IonItem,
  IonItemSliding,
  IonItemOptions,
  IonItemOption,
  IonText,
  IonHeader,
  IonToolbar,
  IonTitle,
  useIonRouter,
  useIonViewWillEnter,
} from "@ionic/react";

import type { HistoryEntry } from "../lib/types";
import { getHistory, deleteEntry } from "../lib/storage";
import { formatDate } from "../lib/bmi-utils";

import "./HistoryPage.css";

export default function HistoryPage() {
  const [entries, setEntries] = useState<HistoryEntry[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const ionRouter = useIonRouter();

  // Reload entries whenever the page becomes active
  useIonViewWillEnter(() => {
    loadHistory();
  });

  const loadHistory = async () => {
    setIsLoading(true);
    try {
      const history = await getHistory();
      setEntries(history);
    } catch (error) {
      console.error("Error loading history:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleEdit = (entry: HistoryEntry) => {
    ionRouter.push(`/bmi/${entry.id}`, "forward");
  };

  const handleDelete = async (entry: HistoryEntry) => {
    try {
      await deleteEntry(entry.id);
      setEntries((prev) => prev.filter((e) => e.id !== entry.id));
    } catch (error) {
      console.error("Error deleting entry:", error);
    }
  };

  if (isLoading && entries.length === 0) {
    return (
      <IonPage>
        <IonHeader>
          <IonToolbar>
            <IonTitle>History</IonTitle>
          </IonToolbar>
        </IonHeader>
        <IonContent className="ion-padding">
          <IonText color="medium">
            <p>Loading...</p>
          </IonText>
        </IonContent>
      </IonPage>
    );
  }

  if (entries.length === 0) {
    return (
      <IonPage>
        <IonHeader>
          <IonToolbar>
            <IonTitle>History</IonTitle>
          </IonToolbar>
        </IonHeader>
        <IonContent className="ion-padding">
          <div className="empty-state">
            <IonText color="medium">
              <p>No calculations yet.</p>
              <p>Go to BMI and submit a result.</p>
            </IonText>
          </div>
        </IonContent>
      </IonPage>
    );
  }

  return (
    <IonPage>
      <IonHeader>
        <IonToolbar>
          <IonTitle>History</IonTitle>
        </IonToolbar>
      </IonHeader>
      <IonContent>
        <IonList>
          {entries.map((entry) => (
            <IonItemSliding key={entry.id}>
              <IonItem routerDirection="none">
                <div className="entry-content">
                  {/* Row 1: name + BMI value */}
                  <div className="entry-header">
                    <IonText>
                      <h2 className="entry-username">{entry.username}</h2>
                    </IonText>
                    <IonText>
                      <h2 className="entry-bmi">{entry.bmi.toFixed(1)}</h2>
                    </IonText>
                  </div>

                  {/* Row 2: date + category */}
                  <div className="entry-meta">
                    <IonText color="medium">
                      <p className="entry-date">{formatDate(entry.createdAt)}</p>
                    </IonText>
                    <IonText>
                      <p className="entry-category">{entry.category}</p>
                    </IonText>
                  </div>

                  {/* Row 3: detail chips */}
                  <div className="entry-chips">
                    <span className="chip">{entry.heightCm} cm</span>
                    <span className="chip">{entry.weightKg} kg</span>
                    <span className="chip">{entry.gender}</span>
                    <span className="chip">Age {entry.age}</span>
                    {entry.isInfantFormula && (
                      <span className="chip chip-infant">Infant</span>
                    )}
                  </div>
                </div>
              </IonItem>

              <IonItemOptions side="end">
                <IonItemOption
                  color="primary"
                  onClick={() => handleEdit(entry)}
                >
                  Edit
                </IonItemOption>
                <IonItemOption
                  color="danger"
                  onClick={() => handleDelete(entry)}
                >
                  Delete
                </IonItemOption>
              </IonItemOptions>
            </IonItemSliding>
          ))}
        </IonList>
      </IonContent>
    </IonPage>
  );
}


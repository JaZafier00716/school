import {
  IonApp,
  IonTabs,
  IonRouterOutlet,
  IonTabBar,
  IonTabButton,
  IonIcon,
  IonLabel,
  IonPage,
  IonHeader,
  IonToolbar,
  IonTitle,
  IonContent,
  IonMenuButton,
  IonMenu,
  IonList,
  IonItem,
  IonText,
  setupIonicReact,
} from "@ionic/react";
import { IonReactRouter } from "@ionic/react-router";
import { calculatorOutline, listOutline } from "ionicons/icons";
import { Redirect, Route } from "react-router-dom";

import BMIPage from "./pages/BMIPage";
import HistoryPage from "./pages/HistoryPage";

/* Core CSS required for Ionic components to work properly */
import "@ionic/react/css/core.css";
import "@ionic/react/css/normalize.css";
import "@ionic/react/css/structure.css";
import "@ionic/react/css/typography.css";
import "@ionic/react/css/padding.css";
import "@ionic/react/css/display.css";
import "@ionic/react/css/flex-utils.css";
import "@ionic/react/css/text-alignment.css";
import "@ionic/react/css/text-transformation.css";

/* Theme variables */
import "./theme/variables.css";

setupIonicReact();

export default function App() {
  return (
    <IonApp>
      <IonReactRouter>
        <IonMenu contentId="main-content">
          <IonHeader>
            <IonToolbar>
              <IonTitle>About</IonTitle>
            </IonToolbar>
          </IonHeader>
          <IonContent className="ion-padding">
            <IonList>
              <IonItem>
                <IonText>
                  <h2>BMI Calculator</h2>
                </IonText>
              </IonItem>
              <IonItem>
                <IonText>
                  <p>
                    <strong>Version:</strong> 1.0.0
                  </p>
                </IonText>
              </IonItem>
              <IonItem>
                <IonText>
                  <p>
                    <strong>Author:</strong> Jan
                  </p>
                </IonText>
              </IonItem>
              <IonItem>
                <IonText>
                  <p>
                    <strong>Created:</strong> 2026
                  </p>
                </IonText>
              </IonItem>
              <IonItem>
                <IonText>
                  <p>
                    <strong>Description:</strong> A cross-platform BMI calculator with
                    history, supporting standard and infant-adjusted indices.
                  </p>
                </IonText>
              </IonItem>
            </IonList>
          </IonContent>
        </IonMenu>

        <IonPage id="main-content">
          <IonHeader>
            <IonToolbar>
              <IonMenuButton slot="start"></IonMenuButton>
              <IonTitle>BMI Calculator</IonTitle>
            </IonToolbar>
          </IonHeader>

          <IonContent>
            <IonTabs>
              <IonRouterOutlet>
                <Route exact path="/bmi">
                  <BMIPage />
                </Route>
                <Route path="/bmi/:editId">
                  <BMIPage />
                </Route>
                <Route exact path="/history">
                  <HistoryPage />
                </Route>
                <Route exact path="/">
                  <Redirect to="/bmi" />
                </Route>
              </IonRouterOutlet>

              <IonTabBar slot="bottom">
                <IonTabButton tab="bmi" href="/bmi">
                  <IonIcon aria-hidden="true" icon={calculatorOutline} />
                  <IonLabel>BMI</IonLabel>
                </IonTabButton>
                <IonTabButton tab="history" href="/history">
                  <IonIcon aria-hidden="true" icon={listOutline} />
                  <IonLabel>History</IonLabel>
                </IonTabButton>
              </IonTabBar>
            </IonTabs>
          </IonContent>
        </IonPage>
      </IonReactRouter>
    </IonApp>
  );
}


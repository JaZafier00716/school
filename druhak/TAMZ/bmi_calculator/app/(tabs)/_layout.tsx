import { useRef, useState } from "react";
import { Dimensions, PanResponder, Pressable, Text, View } from "react-native";
import { Tabs } from "expo-router";
import { Ionicons } from "@expo/vector-icons";
import Modal from "react-native-modal";

const APP_VERSION = "1.0.0";
const APP_AUTHOR = "Jan";
const APP_CREATED = "2026";

export default function TabLayout() {
  const [aboutOpen, setAboutOpen] = useState(false);
  const screenWidth = Dimensions.get("window").width;

  // Swipe from the very left edge to open the about drawer
  const panResponder = useRef(
    PanResponder.create({
      onMoveShouldSetPanResponder: (_, gs) =>
        gs.x0 < 30 && gs.dx > 20 && Math.abs(gs.dy) < 60,
      onPanResponderRelease: (_, gs) => {
        if (gs.dx > 60) setAboutOpen(true);
      },
    })
  ).current;

  return (
    <View style={{ flex: 1 }} {...panResponder.panHandlers}>
      <Tabs
        screenOptions={{
          tabBarActiveTintColor: "#2563eb",
          tabBarLabelStyle: { fontSize: 12 },
          headerLeft: () => (
            <Pressable
              onPress={() => setAboutOpen(true)}
              style={{ marginLeft: 16 }}
              hitSlop={8}
            >
              <Ionicons name="menu-outline" size={24} color="#2563eb" />
            </Pressable>
          ),
        }}
      >
        <Tabs.Screen
          name="bmi"
          options={{
            title: "BMI",
            tabBarIcon: ({ color, size }) => (
              <Ionicons name="calculator-outline" size={size} color={color} />
            ),
          }}
        />
        <Tabs.Screen
          name="history"
          options={{
            title: "History",
            tabBarIcon: ({ color, size }) => (
              <Ionicons name="time-outline" size={size} color={color} />
            ),
          }}
        />
      </Tabs>

      {/* About drawer – slides in from the left, half screen width */}
      <Modal
        isVisible={aboutOpen}
        onBackdropPress={() => setAboutOpen(false)}
        onSwipeComplete={() => setAboutOpen(false)}
        swipeDirection="left"
        animationIn="slideInLeft"
        animationOut="slideOutLeft"
        style={{ margin: 0, justifyContent: "flex-start", alignItems: "flex-start" }}
        backdropOpacity={0.45}
      >
        <View
          style={{
            width: screenWidth * 0.5,
            height: "100%",
            backgroundColor: "white",
          }}
        >
          {/* Drawer header */}
          <View
            style={{
              flexDirection: "row",
              alignItems: "center",
              justifyContent: "space-between",
              paddingHorizontal: 16,
              paddingTop: 56,
              paddingBottom: 14,
              borderBottomWidth: 1,
              borderBottomColor: "#e2e8f0",
            }}
          >
            <Text style={{ fontSize: 18, fontWeight: "700", color: "#0f172a" }}>
              About
            </Text>
            <Pressable onPress={() => setAboutOpen(false)} hitSlop={8}>
              <Ionicons name="close-outline" size={24} color="#475569" />
            </Pressable>
          </View>

          {/* Drawer content */}
          <View style={{ padding: 16, gap: 20 }}>
            {(
              [
                ["App", "BMI Calculator"],
                ["Version", APP_VERSION],
                ["Author", APP_AUTHOR],
                ["Created", APP_CREATED],
                [
                  "Description",
                  "A cross-platform BMI calculator with history, supporting standard and infant-adjusted indices.",
                ],
              ] as [string, string][]
            ).map(([label, value]) => (
              <View key={label}>
                <Text
                  style={{
                    fontSize: 10,
                    fontWeight: "600",
                    color: "#94a3b8",
                    textTransform: "uppercase",
                    letterSpacing: 0.5,
                    marginBottom: 2,
                  }}
                >
                  {label}
                </Text>
                <Text style={{ color: "#1e293b", fontSize: 13 }}>{value}</Text>
              </View>
            ))}
          </View>
        </View>
      </Modal>
    </View>
  );
}


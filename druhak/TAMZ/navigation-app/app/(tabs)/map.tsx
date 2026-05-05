import { useEffect, useMemo, useRef } from 'react';
import {
  Animated,
  KeyboardAvoidingView,
  PanResponder,
  Platform,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  View,
  useWindowDimensions,
} from 'react-native';
import { SafeAreaView, useSafeAreaInsets } from 'react-native-safe-area-context';

import { MapSection } from '@/components/navigation/sections/MapSection';
import { TargetSection } from '@/components/navigation/sections/TargetSection';

export default function MapTabScreen() {
  const insets = useSafeAreaInsets();
  const { height: windowHeight } = useWindowDimensions();

  const collapsedHeight = 84;
  const midHeight = useMemo(() => Math.max(260, Math.round(windowHeight * 0.45)), [windowHeight]);
  const expandedHeight = useMemo(() => Math.max(360, Math.round(windowHeight * 0.78)), [windowHeight]);

  const maxTranslate = Math.max(0, expandedHeight - collapsedHeight);
  const sheetTranslateY = useRef(new Animated.Value(maxTranslate)).current;
  const currentTranslateY = useRef(maxTranslate);
  const gestureStartTranslateY = useRef(maxTranslate);

  useEffect(() => {
    const id = sheetTranslateY.addListener(({ value }) => {
      currentTranslateY.current = value;
    });

    return () => {
      sheetTranslateY.removeListener(id);
    };
  }, [sheetTranslateY]);

  useEffect(() => {
    // Keep the panel in collapsed state when dimensions change.
    sheetTranslateY.setValue(Math.max(0, expandedHeight - collapsedHeight));
  }, [collapsedHeight, expandedHeight, sheetTranslateY]);

  const animateToHeight = (nextHeight: number) => {
    const clampedHeight = Math.min(expandedHeight, Math.max(collapsedHeight, nextHeight));
    const toValue = expandedHeight - clampedHeight;
    Animated.spring(sheetTranslateY, {
      toValue,
      useNativeDriver: true,
      bounciness: 0,
    }).start();
  };

  const toggleSheet = () => {
    const currentHeight = expandedHeight - currentTranslateY.current;
    if (currentHeight <= collapsedHeight + 16) {
      animateToHeight(midHeight);
      return;
    }

    animateToHeight(collapsedHeight);
  };

  const panResponder = useRef(
    PanResponder.create({
      onMoveShouldSetPanResponder: (_evt, gestureState) => Math.abs(gestureState.dy) > 3,
      onPanResponderGrant: () => {
        sheetTranslateY.stopAnimation((value) => {
          gestureStartTranslateY.current = value;
        });
      },
      onPanResponderMove: (_evt, gestureState) => {
        const next = gestureStartTranslateY.current + gestureState.dy;
        sheetTranslateY.setValue(Math.min(maxTranslate, Math.max(0, next)));
      },
      onPanResponderRelease: () => {
        const currentHeight = expandedHeight - currentTranslateY.current;
        const snapPoints = [collapsedHeight, midHeight, expandedHeight];
        const nearest = snapPoints.reduce((prev, next) =>
          Math.abs(next - currentHeight) < Math.abs(prev - currentHeight) ? next : prev
        );
        animateToHeight(nearest);
      },
    })
  ).current;

  return (
    <SafeAreaView edges={['top', 'left', 'right']} style={styles.screen}>
      <View style={styles.mapFullscreen}>
        <MapSection />
      </View>

      <Pressable onPress={toggleSheet} style={[styles.fab, { top: insets.top + 10 }]}>
        <Text style={styles.fabText}>Search & Target</Text>
      </Pressable>

      <Animated.View
        style={[
          styles.sheet,
          {
            height: expandedHeight,
            bottom: insets.bottom + 8,
            transform: [{ translateY: sheetTranslateY }],
          },
        ]}
      >
        <View style={styles.handleArea} {...panResponder.panHandlers}>
          <View style={styles.handle} />
          <Text style={styles.sheetLabel}>Destination</Text>
        </View>

        <KeyboardAvoidingView
          style={styles.sheetBody}
          behavior={Platform.OS === 'ios' ? 'padding' : undefined}
          keyboardVerticalOffset={Platform.OS === 'ios' ? 12 : 0}
        >
          <ScrollView
            contentContainerStyle={styles.sheetContent}
            keyboardShouldPersistTaps="handled"
            keyboardDismissMode="on-drag"
          >
            <TargetSection />
          </ScrollView>
        </KeyboardAvoidingView>
      </Animated.View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    backgroundColor: '#050608',
  },
  mapFullscreen: {
    flex: 1,
  },
  fab: {
    position: 'absolute',
    right: 14,
    backgroundColor: '#111827',
    borderColor: '#2a3040',
    borderWidth: 1,
    borderRadius: 999,
    paddingHorizontal: 14,
    paddingVertical: 9,
    zIndex: 20,
  },
  fabText: {
    color: '#e5e7eb',
    fontWeight: '700',
    fontSize: 12,
  },
  sheet: {
    position: 'absolute',
    left: 10,
    right: 10,
    borderRadius: 16,
    backgroundColor: '#090c12',
    borderColor: '#2a3040',
    borderWidth: 1,
    overflow: 'hidden',
  },
  handleArea: {
    paddingTop: 8,
    paddingBottom: 8,
    alignItems: 'center',
    borderBottomColor: '#1f2431',
    borderBottomWidth: 1,
  },
  handle: {
    width: 42,
    height: 5,
    borderRadius: 999,
    backgroundColor: '#4b5563',
    marginBottom: 6,
  },
  sheetLabel: {
    color: '#94a0b5',
    fontSize: 12,
    fontWeight: '600',
  },
  sheetBody: {
    flex: 1,
  },
  sheetContent: {
    padding: 8,
    paddingBottom: 18,
  },
});


import { ScrollView, StyleSheet, Text, View } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';

import { MapSection } from '@/components/navigation/sections/MapSection';
import { TargetSection } from '@/components/navigation/sections/TargetSection';

export default function MapTabScreen() {
  return (
    <SafeAreaView edges={['top']} style={styles.screen}>
      <View style={styles.header}>
        <Text style={styles.title}>Map</Text>
        <Text style={styles.subtitle}>Choose a destination and keep the compass pinned over the map.</Text>
      </View>

      <ScrollView style={styles.scrollContainer} contentContainerStyle={styles.scrollContent}>
        <View style={styles.mapArea}>
          <MapSection />
        </View>
        <TargetSection />
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    backgroundColor: '#050608',
    paddingHorizontal: 14,
    paddingBottom: 14,
    gap: 12,
  },
  scrollContainer: {
    flex: 1,
  },
  scrollContent: {
    flexGrow: 1,
    paddingHorizontal: 14,
    paddingBottom: 24,
    gap: 12,
  },
  header: {
    gap: 4,
  },
  mapArea: {
    flex: 1,
    minHeight: 0,
    position: 'relative',
  },
  title: {
    color: '#f5f5f5',
    fontSize: 24,
    fontWeight: '700',
    letterSpacing: 0.4,
  },
  subtitle: {
    color: '#94a0b5',
    fontSize: 13,
  },
});


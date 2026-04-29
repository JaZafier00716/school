import { ScrollView, StyleSheet, View } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';

import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';

export default function ExploreScreen() {
  return (
    <SafeAreaView edges={['top']} style={styles.screen}>
      <ScrollView contentContainerStyle={styles.content}>
        <ThemedView style={styles.header}>
          <ThemedText type="title">Explore</ThemedText>
        </ThemedView>

        <ThemedText>
          This app includes a navigation dashboard with live location, compass heading, and target
          navigation features.
        </ThemedText>

        <View style={styles.section}>
          <ThemedText type="defaultSemiBold">Navigation Features</ThemedText>
          <ThemedText>• Live position tracking with GPS accuracy and altitude</ThemedText>
          <ThemedText>• Magnetic compass with heading display</ThemedText>
          <ThemedText>• Target navigation with bearing and distance</ThemedText>
          <ThemedText>• Interactive map with route calculation</ThemedText>
        </View>

        <View style={styles.section}>
          <ThemedText type="defaultSemiBold">Tabs</ThemedText>
          <ThemedText>The app has three main tabs:</ThemedText>
          <ThemedText>• Home: Dashboard with position, compass, and target summary</ThemedText>
          <ThemedText>• Map: Interactive map with compass overlay and target sheet</ThemedText>
          <ThemedText>• Explore: This information page</ThemedText>
        </View>

        <View style={styles.section}>
          <ThemedText type="defaultSemiBold">Permissions</ThemedText>
          <ThemedText>
            The app requires location permissions to track your position and heading. Magnetometer or
            GPS heading is used to determine device orientation.
          </ThemedText>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    backgroundColor: '#050608',
  },
  content: {
    paddingHorizontal: 14,
    paddingTop: 14,
    paddingBottom: 24,
    gap: 12,
  },
  header: {
    gap: 4,
    marginBottom: 8,
  },
  section: {
    gap: 6,
    marginVertical: 6,
  },
});

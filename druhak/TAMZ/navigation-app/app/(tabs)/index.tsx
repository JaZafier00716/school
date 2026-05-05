import { KeyboardAvoidingView, Platform, ScrollView, StyleSheet, Text, View } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';

import { CompassSection } from '@/components/navigation/sections/CompassSection';
import { LivePositionSection } from '@/components/navigation/sections/LivePositionSection';
import { TargetSection } from '@/components/navigation/sections/TargetSection';

export default function NavigationDashboardScreen() {
  return (
    <SafeAreaView edges={['top']} style={styles.screen}>
      <KeyboardAvoidingView
        style={styles.keyboardArea}
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
        keyboardVerticalOffset={Platform.OS === 'ios' ? 8 : 0}
      >
        <ScrollView
          contentContainerStyle={styles.content}
          keyboardShouldPersistTaps="handled"
          keyboardDismissMode="on-drag"
        >
          <View style={styles.header}>
            <Text style={styles.title}>Navigation Dashboard</Text>
            <Text style={styles.subtitle}>
              Track your position, check the compass, and open the Map tab to choose a target.
            </Text>
          </View>

          <LivePositionSection />
          <CompassSection />
          <TargetSection />
        </ScrollView>
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    backgroundColor: '#050608',
  },
  keyboardArea: {
    flex: 1,
  },
  content: {
    paddingHorizontal: 14,
    paddingTop: 14,
    paddingBottom: 24,
    gap: 12,
    backgroundColor: '#050608',
  },
  header: {
    gap: 4,
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

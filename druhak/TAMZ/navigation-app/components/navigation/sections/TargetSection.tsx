import { Link } from 'expo-router';
import { Pressable, Text, View } from 'react-native';

import { SectionCard } from '../SectionCard';
import { useNavigationDashboard } from '../navigation-provider';
import { formatDistance } from '../navigation-math';

export function TargetSection() {
  const { bearingToTarget, clearTarget, distanceToTarget, target } = useNavigationDashboard();

  return (
    <SectionCard title="Target Details">
      {target ? (
        <View style={styles.targetBox}>
          <Text style={styles.readout}>
            Target: {target.latitude.toFixed(6)}, {target.longitude.toFixed(6)}
          </Text>
          <Text style={styles.readout}>Bearing: {bearingToTarget?.toFixed(0) ?? '--'}°</Text>
          <Text style={styles.readout}>
            Distance: {distanceToTarget != null ? formatDistance(distanceToTarget) : '--'}
          </Text>

          <View style={styles.buttonRow}>
            <Link href="/(tabs)/map" asChild>
              <Pressable style={styles.primaryButton}>
                <Text style={styles.primaryButtonText}>Open Map</Text>
              </Pressable>
            </Link>

            <Pressable onPress={clearTarget} style={styles.secondaryButton}>
              <Text style={styles.secondaryButtonText}>Clear Target</Text>
            </Pressable>
          </View>
        </View>
      ) : (
        <View style={styles.emptyState}>
          <Text style={styles.readoutMuted}>No target selected yet.</Text>
          <Link href="/(tabs)/map" asChild>
            <Pressable style={styles.primaryButton}>
              <Text style={styles.primaryButtonText}>Choose on Map</Text>
            </Pressable>
          </Link>
        </View>
      )}
    </SectionCard>
  );
}

const styles = {
  targetBox: {
    gap: 6,
  },
  emptyState: {
    gap: 10,
  },
  buttonRow: {
    flexDirection: 'row' as const,
    flexWrap: 'wrap' as const,
    gap: 8,
    marginTop: 4,
  },
  readout: {
    color: '#d5dde9',
    fontSize: 14,
  },
  readoutMuted: {
    color: '#94a0b5',
    fontSize: 13,
  },
  primaryButton: {
    backgroundColor: '#202735',
    borderRadius: 8,
    paddingHorizontal: 12,
    paddingVertical: 8,
  },
  primaryButtonText: {
    color: '#f2f2f2',
    fontWeight: '600' as const,
    fontSize: 13,
  },
  secondaryButton: {
    backgroundColor: '#151a24',
    borderColor: '#2a3040',
    borderWidth: 1,
    borderRadius: 8,
    paddingHorizontal: 12,
    paddingVertical: 8,
  },
  secondaryButtonText: {
    color: '#f2f2f2',
    fontWeight: '600' as const,
    fontSize: 13,
  },
};


import { Pressable, Text, View } from 'react-native';

import { SectionCard } from '../SectionCard';
import { useNavigationDashboard } from '../navigation-provider';
import { formatDistance } from '../navigation-math';
import { NavigationArrow } from '../NavigationArrow';

export function TargetSection() {
  const { bearingToTarget, clearTarget, distanceToTarget, target, relativeArrowRotation } =
    useNavigationDashboard();

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

          <NavigationArrow rotation={relativeArrowRotation} />

          <View style={styles.buttonRow}>
            <Pressable onPress={clearTarget} style={styles.secondaryButton}>
              <Text style={styles.secondaryButtonText}>Clear Target</Text>
            </Pressable>
          </View>
        </View>
      ) : (
        <View style={styles.emptyState}>
          <Text style={styles.readoutMuted}>No target selected yet.</Text>
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


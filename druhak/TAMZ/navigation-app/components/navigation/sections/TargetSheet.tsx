import { Pressable, Text, View } from 'react-native';

import { useNavigationDashboard } from '../navigation-provider';
import { formatDistance } from '../navigation-math';

type TargetSheetProps = {
  bottomOffset?: number;
};

export function TargetSheet({ bottomOffset = 12 }: TargetSheetProps) {
  const {
    bearingToTarget,
    clearTarget,
    distanceToTarget,
    isFetchingRoute,
    locationGranted,
    permissionError,
    sensorGranted,
    target,
  } = useNavigationDashboard();

  return (
    <View pointerEvents="box-none" style={[styles.container, { bottom: bottomOffset }]}>
      <View style={styles.sheet}>
        <View style={styles.handle} />

        {target ? (
          <View style={styles.content}>
            <Text style={styles.title}>Target Details</Text>
            <Text style={styles.readout}>
              Target: {target.latitude.toFixed(6)}, {target.longitude.toFixed(6)}
            </Text>
            <Text style={styles.readout}>Bearing: {bearingToTarget?.toFixed(0) ?? '--'}°</Text>
            <Text style={styles.readout}>
              Distance: {distanceToTarget != null ? formatDistance(distanceToTarget) : '--'}
            </Text>

            {isFetchingRoute ? <Text style={styles.readoutMuted}>Calculating route...</Text> : null}

            <View style={styles.buttonRow}>
              <Pressable onPress={clearTarget} style={styles.secondaryButton}>
                <Text style={styles.secondaryButtonText}>Clear Target</Text>
              </Pressable>
            </View>
          </View>
        ) : (
          <View style={styles.content}>
            <Text style={styles.title}>Target Details</Text>
            <Text style={styles.readoutMuted}>No target selected yet.</Text>
            <Text style={styles.readoutMuted}>Tap or long-press the map to choose one.</Text>
          </View>
        )}

        {!locationGranted ? (
          <Text style={styles.readoutMuted}>Location access is required to show your position.</Text>
        ) : null}
        {!sensorGranted ? (
          <Text style={styles.readoutMuted}>Compass heading is unavailable right now.</Text>
        ) : null}
        {permissionError ? <Text style={styles.errorText}>Error: {permissionError}</Text> : null}
      </View>
    </View>
  );
}

const styles = {
  container: {
    position: 'absolute' as const,
    left: 14,
    right: 14,
    zIndex: 20,
    elevation: 20,
  },
  sheet: {
    backgroundColor: 'rgba(16, 19, 26, 0.96)',
    borderColor: '#2a3040',
    borderWidth: 1,
    borderRadius: 22,
    paddingHorizontal: 16,
    paddingTop: 10,
    paddingBottom: 14,
    gap: 8,
    shadowColor: '#000',
    shadowOpacity: 0.2,
    shadowRadius: 14,
    shadowOffset: { width: 0, height: 6 },
  },
  handle: {
    alignSelf: 'center' as const,
    width: 42,
    height: 4,
    borderRadius: 999,
    backgroundColor: '#394255',
  },
  content: {
    gap: 4,
  },
  title: {
    color: '#f5f5f5',
    fontSize: 15,
    fontWeight: '700' as const,
  },
  readout: {
    color: '#d5dde9',
    fontSize: 14,
  },
  readoutMuted: {
    color: '#94a0b5',
    fontSize: 13,
  },
  buttonRow: {
    flexDirection: 'row' as const,
    gap: 8,
    marginTop: 4,
  },
  secondaryButton: {
    backgroundColor: '#151a24',
    borderColor: '#2a3040',
    borderWidth: 1,
    borderRadius: 8,
    paddingHorizontal: 12,
    paddingVertical: 8,
    alignSelf: 'flex-start' as const,
  },
  secondaryButtonText: {
    color: '#f2f2f2',
    fontWeight: '600' as const,
    fontSize: 13,
  },
  errorText: {
    color: '#ff8a80',
    fontSize: 13,
  },
};


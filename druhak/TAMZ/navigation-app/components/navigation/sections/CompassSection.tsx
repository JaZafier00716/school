import { Text, View } from 'react-native';

import { CompassDial } from '../CompassDial';
import { NavigationArrow } from '../NavigationArrow';
import { SectionCard } from '../SectionCard';
import { useNavigationDashboard } from '../navigation-provider';
import { formatDistance, headingToCardinal } from '../navigation-math';

export function CompassSection() {
  const {
    bearingToTarget,
    distanceToTarget,
    magHeading,
    relativeArrowRotation,
    sensorGranted,
    target,
  } = useNavigationDashboard();

  return (
    <SectionCard title="Compass & Target Mode">
      <View style={styles.centered}>
        <CompassDial heading={magHeading} />
      </View>
      <Text style={styles.facingLabel}>Facing: {headingToCardinal(magHeading)}</Text>

      {target ? (
        <View style={styles.targetSummary}>
          <Text style={styles.readout}>
            Target bearing: {bearingToTarget?.toFixed(0) ?? '--'}°
          </Text>
          <Text style={styles.readout}>
            Distance: {distanceToTarget != null ? formatDistance(distanceToTarget) : '--'}
          </Text>
          <NavigationArrow rotation={relativeArrowRotation} />
        </View>
      ) : (
        <Text style={styles.readoutMuted}>Open the Map tab to choose a target destination.</Text>
      )}

      {!sensorGranted ? (
        <Text style={styles.readoutMuted}>Compass heading is unavailable right now.</Text>
      ) : null}
    </SectionCard>
  );
}

const styles = {
  centered: {
    alignItems: 'center' as const,
    justifyContent: 'center' as const,
  },
  facingLabel: {
    color: '#f5f5f5',
    fontWeight: '700' as const,
    fontSize: 14,
  },
  targetSummary: {
    gap: 4,
  },
  readout: {
    color: '#d5dde9',
    fontSize: 14,
  },
  readoutMuted: {
    color: '#94a0b5',
    fontSize: 13,
  },
};



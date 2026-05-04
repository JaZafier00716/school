import { Text, View } from 'react-native';

import { CompassDial } from '../CompassDial';
import { SectionCard } from '../SectionCard';
import { useNavigationDashboard } from '../navigation-provider';
import { headingToCardinal } from '../navigation-math';

export function CompassSection() {
  const { magHeading, sensorGranted } = useNavigationDashboard();

  return (
    <SectionCard title="Compass & Target Mode">
      <View style={styles.centered}>
        <CompassDial heading={magHeading} />
      </View>
      <Text style={styles.facingLabel}>Facing: {headingToCardinal(magHeading)}</Text>

      <Text style={styles.readoutMuted}>Set a target on the Map tab to view details in the Target section.</Text>

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


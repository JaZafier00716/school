import { StyleSheet, Text, View } from 'react-native';

import { CompassDial } from './CompassDial';

type MapCompassOverlayProps = {
  heading: number;
};

export function MapCompassOverlay({ heading }: MapCompassOverlayProps) {
  return (
    <View pointerEvents="none" style={styles.container}>
      <View style={styles.card}>
        <CompassDial heading={heading} size={92} showHeadingLabel={false} />
        <View style={styles.headingBadge}>
          <Text style={styles.headingText}>{heading.toFixed(0)}°</Text>
        </View>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    position: 'absolute',
    top: 12,
    right: 12,
    zIndex: 10,
    elevation: 10,
  },
  card: {
    // backgroundColor: 'rgba(10, 13, 19, 0.82)',
    // borderColor: '#2a3040',
    // borderWidth: 1,
    // borderRadius: 16,
    // padding: 10,
    alignItems: 'center',
    // gap: 8,
  },
  headingBadge: {
    backgroundColor: 'rgba(16, 19, 26, 0.95)',
    borderColor: '#2a3040',
    borderWidth: 1,
    borderRadius: 999,
    paddingHorizontal: 10,
    paddingVertical: 4,
  },
  headingText: {
    color: '#f5f5f5',
    fontSize: 13,
    fontWeight: '700',
  },
});


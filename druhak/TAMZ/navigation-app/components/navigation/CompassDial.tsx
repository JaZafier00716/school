import { StyleSheet, Text, View } from 'react-native';

type CompassDialProps = {
  heading: number;
  size?: number;
  showHeadingLabel?: boolean;
};

export function CompassDial({ heading, size = 114, showHeadingLabel = true }: CompassDialProps) {
  const ringSize = size;
  const pointerTop = Math.max(4, size * 0.05);
  const labelSize = Math.max(11, Math.round(size * 0.12));
  const headingSize = Math.max(14, Math.round(size * 0.16));

  return (
    <View style={[styles.container, { width: ringSize + 16, height: ringSize + 16 }]}>
      <View
        style={[
          styles.pointer,
          {
            top: pointerTop,
            borderLeftWidth: Math.max(6, Math.round(size * 0.06)),
            borderRightWidth: Math.max(6, Math.round(size * 0.06)),
            borderBottomWidth: Math.max(12, Math.round(size * 0.12)),
          },
        ]}
      />
      <View
        style={[
          styles.dial,
          {
            width: ringSize,
            height: ringSize,
            borderRadius: ringSize / 2,
            transform: [{ rotate: `${-heading}deg` }],
          },
        ]}>
        <Text style={[styles.cardinal, { fontSize: labelSize, top: ringSize * 0.07, color: '#ffe082' }]}>N</Text>
        <Text style={[styles.cardinal, { fontSize: labelSize, right: ringSize * 0.08 }]}>E</Text>
        <Text style={[styles.cardinal, { fontSize: labelSize, bottom: ringSize * 0.07 }]}>S</Text>
        <Text style={[styles.cardinal, { fontSize: labelSize, left: ringSize * 0.08 }]}>W</Text>
      </View>
      {showHeadingLabel ? (
        <Text style={[styles.heading, { fontSize: headingSize }]}>{heading.toFixed(0)}°</Text>
      ) : null}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    alignItems: 'center',
    justifyContent: 'center',
  },
  pointer: {
    position: 'absolute',
    width: 0,
    height: 0,
    borderLeftColor: 'transparent',
    borderRightColor: 'transparent',
    borderBottomColor: '#ffe082',
    zIndex: 2,
  },
  dial: {
    borderWidth: 1,
    borderColor: '#2a3040',
    backgroundColor: '#0a0d13',
    justifyContent: 'center',
    alignItems: 'center',
    overflow: 'hidden',
  },
  cardinal: {
    position: 'absolute',
    color: '#d5dde9',
    fontWeight: '700',
  },
  heading: {
    color: '#f5f5f5',
    fontWeight: '700',
    marginTop: 8,
  },
});


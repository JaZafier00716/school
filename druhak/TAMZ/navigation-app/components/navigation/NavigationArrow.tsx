import { StyleSheet, View } from 'react-native';

type NavigationArrowProps = {
  rotation: number;
};

export function NavigationArrow({ rotation }: NavigationArrowProps) {
  return (
    <View style={styles.container}>
      <View
        style={[
          styles.arrow,
          {
            transform: [{ rotate: `${rotation}deg` }],
          },
        ]}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    marginTop: 8,
    alignItems: 'center',
    justifyContent: 'center',
    height: 92,
  },
  arrow: {
    width: 0,
    height: 0,
    borderLeftWidth: 16,
    borderRightWidth: 16,
    borderBottomWidth: 50,
    borderLeftColor: 'transparent',
    borderRightColor: 'transparent',
    borderBottomColor: '#58d68d',
  },
});


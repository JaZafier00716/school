import { useEffect, useRef } from 'react';
import { StyleSheet, View } from 'react-native';
import MapView, { Marker, Polyline } from 'react-native-maps';

import { MapCompassOverlay } from '../MapCompassOverlay';
import { useNavigationDashboard } from '../navigation-provider';

export function MapSection() {
  const mapRef = useRef<MapView | null>(null);
  const { currentPosition, locationGranted, magHeading, routePoints, selectTarget, target } =
    useNavigationDashboard();

  useEffect(() => {
    if (!mapRef.current || !currentPosition) return;

    mapRef.current.animateCamera(
      {
        center: currentPosition,
        zoom: 16,
      },
      { duration: 500 }
    );
  }, [currentPosition]);

  return (
    <View style={styles.container}>
      <View style={styles.mapWrapper}>
        <MapView
          ref={mapRef}
          style={styles.map}
          showsUserLocation={locationGranted}
          followsUserLocation={false}
          userInterfaceStyle="dark"
          initialRegion={{
            latitude: currentPosition?.latitude ?? 50.0755,
            longitude: currentPosition?.longitude ?? 14.4378,
            latitudeDelta: 0.03,
            longitudeDelta: 0.03,
          }}
          onLongPress={(event) => {
            void selectTarget(event.nativeEvent.coordinate);
          }}
          onPress={(event) => {
            void selectTarget(event.nativeEvent.coordinate);
          }}
        >
          {target ? <Marker coordinate={target} title="Target" pinColor="#ff6b6b" /> : null}
          {routePoints.length > 1 ? (
            <Polyline coordinates={routePoints} strokeColor="#58d68d" strokeWidth={5} />
          ) : null}
        </MapView>

        <MapCompassOverlay heading={magHeading} />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    minHeight: 0,
  },
  mapWrapper: {
    position: 'relative' as const,
    flex: 1,
    minHeight: 0,
  },
  map: {
    flex: 1,
    minHeight: 0,
    width: '100%',
    borderRadius: 12,
  },
});



import { useCallback, useEffect, useRef, useState } from 'react';
import { StyleSheet, Text, View } from 'react-native';
import MapView, { Callout, Marker, Polyline } from 'react-native-maps';

import { MapCompassOverlay } from '../MapCompassOverlay';
import { useNavigationDashboard } from '../navigation-provider';

export function MapSection() {
  const mapRef = useRef<MapView | null>(null);
  const [labelPositions, setLabelPositions] = useState<Record<string, { x: number; y: number }>>({});
  const updateRafRef = useRef<number | null>(null);
  const { currentPosition, locationGranted, magHeading, routePoints, selectTarget, target, savedLocations } =
    useNavigationDashboard();

  const updateLabelPositions = useCallback(async () => {
    const mapInstance = mapRef.current as unknown as {
      pointForCoordinate?: (coordinate: { latitude: number; longitude: number }) => Promise<{ x: number; y: number }>;
    } | null;

    if (!mapInstance?.pointForCoordinate) return;

    const nextPositions = await Promise.all(
      savedLocations.map(async (loc) => {
        try {
          const point = await mapInstance.pointForCoordinate!({
            latitude: loc.latitude,
            longitude: loc.longitude,
          });
          return [loc.id, point] as const;
        } catch {
          return [loc.id, null] as const;
        }
      })
    );

    const nextMap: Record<string, { x: number; y: number }> = {};
    for (const [id, point] of nextPositions) {
      if (point) {
        nextMap[id] = point;
      }
    }

    setLabelPositions(nextMap);
  }, [savedLocations]);

  const scheduleLabelPositionsUpdate = useCallback(() => {
    if (updateRafRef.current != null) {
      cancelAnimationFrame(updateRafRef.current);
    }

    updateRafRef.current = requestAnimationFrame(() => {
      updateRafRef.current = null;
      void updateLabelPositions();
    });
  }, [updateLabelPositions]);

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

  useEffect(() => {
    scheduleLabelPositionsUpdate();
  }, [savedLocations, currentPosition, scheduleLabelPositionsUpdate]);

  useEffect(() => {
    return () => {
      if (updateRafRef.current != null) {
        cancelAnimationFrame(updateRafRef.current);
      }
    };
  }, []);

  return (
    <View style={styles.container}>
      <View
        style={styles.mapWrapper}
        onLayout={() => {
          scheduleLabelPositionsUpdate();
        }}
      >
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
          onMapReady={() => {
            scheduleLabelPositionsUpdate();
          }}
          onRegionChange={() => {
            scheduleLabelPositionsUpdate();
          }}
          onRegionChangeComplete={() => {
            scheduleLabelPositionsUpdate();
          }}
        >
          {savedLocations.map((loc) => (
            <Marker
              key={loc.id}
              coordinate={{ latitude: loc.latitude, longitude: loc.longitude }}
              title={loc.name}
              opacity={0}
              anchor={{ x: 0.5, y: 1 }}
              tracksViewChanges={false}
              onPress={() => {
                void selectTarget({ latitude: loc.latitude, longitude: loc.longitude });
              }}
            >
              <View collapsable={false} style={styles.hiddenMarkerHitArea} />
              <Callout tooltip>
                <View style={styles.calloutBubble}>
                  <Text style={styles.calloutText}>{loc.name}</Text>
                </View>
              </Callout>
            </Marker>
          ))}

          {target ? <Marker coordinate={target} title="Target" pinColor="#ff6b6b" /> : null}
          {routePoints.length > 1 ? (
            <Polyline coordinates={routePoints} strokeColor="#58d68d" strokeWidth={5} />
          ) : null}
        </MapView>

        <View pointerEvents="box-none" style={styles.savedLabelLayer}>
          {savedLocations.map((loc) => {
            const position = labelPositions[loc.id];
            if (!position) return null;

            return (
              <View
                key={loc.id}
                pointerEvents="box-none"
                style={[
                  styles.savedLabelWrapper,
                  {
                    left: position.x - 110,
                    top: position.y - 86,
                  },
                ]}
              >
                <View style={styles.savedMarkerLabelBubble}>
                  <Text style={styles.savedMarkerLabelText} numberOfLines={3}>
                    {loc.name}
                  </Text>
                  <View style={styles.savedMarkerPointer} />
                </View>
              </View>
            );
          })}
        </View>

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
  savedLabelLayer: {
    ...StyleSheet.absoluteFillObject,
  },
  savedLabelWrapper: {
    position: 'absolute',
    width: 220,
    alignItems: 'center',
  },
  savedMarkerLabelBubble: {
    width: 220,
    alignItems: 'center',
  },
  savedMarkerLabelText: {
    color: '#fff',
    fontSize: 11,
    fontWeight: '700',
    textAlign: 'center',
    width: 220,
    backgroundColor: 'rgba(15, 23, 42, 0.9)',
    borderColor: '#60a5fa',
    borderWidth: 1,
    borderRadius: 8,
    paddingHorizontal: 8,
    paddingVertical: 4,
    marginBottom: 4,
  },
  hiddenMarkerHitArea: {
    width: 1,
    height: 1,
    opacity: 0,
  },
  savedMarkerPointer: {
    width: 0,
    height: 0,
    borderLeftWidth: 10,
    borderRightWidth: 10,
    borderTopWidth: 14,
    borderLeftColor: 'transparent',
    borderRightColor: 'transparent',
    borderTopColor: '#60a5fa',
    marginTop: -1,
  },
  calloutBubble: {
    maxWidth: 240,
    backgroundColor: '#0f172a',
    borderColor: '#60a5fa',
    borderWidth: 1,
    borderRadius: 10,
    paddingHorizontal: 10,
    paddingVertical: 6,
  },
  calloutText: {
    color: '#fff',
    fontSize: 13,
    fontWeight: '700',
    textAlign: 'center',
  },
});



import { useEffect, useMemo, useRef, useState } from 'react';
import {
  ActivityIndicator,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import MapView, { Marker, Polyline, type LatLng } from 'react-native-maps';
import * as Location from 'expo-location';
import { Magnetometer } from 'expo-sensors';

const EARTH_RADIUS_M = 6371000;

type RoutePoint = LatLng;

function toRadians(degrees: number): number {
  return (degrees * Math.PI) / 180;
}

function toDegrees(radians: number): number {
  return (radians * 180) / Math.PI;
}

export function haversineDistanceMeters(from: LatLng, to: LatLng): number {
  const dLat = toRadians(to.latitude - from.latitude);
  const dLon = toRadians(to.longitude - from.longitude);
  const lat1 = toRadians(from.latitude);
  const lat2 = toRadians(to.latitude);

  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);

  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  return EARTH_RADIUS_M * c;
}

export function calculateBearingDegrees(from: LatLng, to: LatLng): number {
  const lat1 = toRadians(from.latitude);
  const lat2 = toRadians(to.latitude);
  const dLon = toRadians(to.longitude - from.longitude);

  const y = Math.sin(dLon) * Math.cos(lat2);
  const x =
    Math.cos(lat1) * Math.sin(lat2) -
    Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);

  return (toDegrees(Math.atan2(y, x)) + 360) % 360;
}

function normalizeDegrees(degrees: number): number {
  return ((degrees % 360) + 360) % 360;
}

function toCompassHeadingFromMagnetometer(x: number, y: number): number {
  const heading = toDegrees(Math.atan2(y, x));
  return normalizeDegrees(heading);
}

function formatDistance(distanceMeters: number): string {
  if (distanceMeters < 1000) {
    return `${distanceMeters.toFixed(0)} m`;
  }
  return `${(distanceMeters / 1000).toFixed(2)} km`;
}

function headingToCardinal(heading: number): string {
  const directions = ['N', 'NE', 'E', 'SE', 'S', 'SW', 'W', 'NW'];
  const index = Math.round(heading / 45) % 8;
  return directions[index];
}

async function fetchRoute(from: LatLng, to: LatLng): Promise<RoutePoint[]> {
  const osrmUrl = `https://router.project-osrm.org/route/v1/driving/${from.longitude},${from.latitude};${to.longitude},${to.latitude}?overview=full&geometries=geojson`;

  try {
    const response = await fetch(osrmUrl);
    if (!response.ok) {
      throw new Error(`Route request failed with status ${response.status}`);
    }

    const data = (await response.json()) as {
      routes?: {
        geometry?: {
          coordinates?: [number, number][];
        };
      }[];
    };

    const coordinates = data.routes?.[0]?.geometry?.coordinates;

    if (!coordinates || coordinates.length === 0) {
      throw new Error('No route geometry returned by OSRM');
    }

    return coordinates.map(([longitude, latitude]) => ({ latitude, longitude }));
  } catch {
    return [from, to];
  }
}

export default function NavigationDashboardScreen() {
  const mapRef = useRef<MapView | null>(null);

  const [permissionError, setPermissionError] = useState<string | null>(null);
  const [sensorGranted, setSensorGranted] = useState<boolean>(false);
  const [locationGranted, setLocationGranted] = useState<boolean>(false);
  const [isFetchingRoute, setIsFetchingRoute] = useState<boolean>(false);

  const [now, setNow] = useState<Date>(new Date());
  const [coords, setCoords] = useState<Location.LocationObjectCoords | null>(null);
  const [magHeading, setMagHeading] = useState<number>(0);

  const [target, setTarget] = useState<LatLng | null>(null);
  const [routePoints, setRoutePoints] = useState<RoutePoint[]>([]);

  const currentPosition = useMemo<LatLng | null>(() => {
    if (!coords) return null;
    return {
      latitude: coords.latitude,
      longitude: coords.longitude,
    };
  }, [coords]);

  const distanceToTarget = useMemo<number | null>(() => {
    if (!currentPosition || !target) return null;
    return haversineDistanceMeters(currentPosition, target);
  }, [currentPosition, target]);

  const bearingToTarget = useMemo<number | null>(() => {
    if (!currentPosition || !target) return null;
    return calculateBearingDegrees(currentPosition, target);
  }, [currentPosition, target]);

  const relativeArrowRotation = useMemo<number>(() => {
    if (bearingToTarget == null) return 0;
    return normalizeDegrees(bearingToTarget - magHeading);
  }, [bearingToTarget, magHeading]);

  useEffect(() => {
    const timerId = setInterval(() => {
      setNow(new Date());
    }, 1000);

    return () => clearInterval(timerId);
  }, []);

  useEffect(() => {
    let isMounted = true;
    let locationSubscription: Location.LocationSubscription | null = null;
    let magnetometerSubscription: { remove: () => void } | null = null;

    const setup = async () => {
      try {
        const locationPermission = await Location.requestForegroundPermissionsAsync();
        const magnetometerPermission = await Magnetometer.requestPermissionsAsync();

        if (!isMounted) return;

        const hasLocationPermission = locationPermission.granted;
        const hasSensorPermission = magnetometerPermission.granted;

        setLocationGranted(hasLocationPermission);
        setSensorGranted(hasSensorPermission);

        if (!hasLocationPermission) {
          setPermissionError('Location permission is required for the dashboard.');
          return;
        }

        const initial = await Location.getCurrentPositionAsync({
          accuracy: Location.Accuracy.BestForNavigation,
        });

        if (isMounted) {
          setCoords(initial.coords);
        }

        locationSubscription = await Location.watchPositionAsync(
          {
            accuracy: Location.Accuracy.BestForNavigation,
            timeInterval: 1000,
            distanceInterval: 1,
          },
          (location) => {
            setCoords(location.coords);
          }
        );

        if (hasSensorPermission) {
          Magnetometer.setUpdateInterval(300);
          magnetometerSubscription = Magnetometer.addListener(({ x, y }) => {
            setMagHeading(toCompassHeadingFromMagnetometer(x, y));
          });
        }
      } catch (error) {
        if (isMounted) {
          setPermissionError(
            error instanceof Error
              ? error.message
              : 'Unable to initialize location/sensor services.'
          );
        }
      }
    };

    void setup();

    return () => {
      isMounted = false;
      locationSubscription?.remove();
      magnetometerSubscription?.remove();
    };
  }, []);

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

  const onSelectTarget = async (point: LatLng) => {
    setTarget(point);

    if (!currentPosition) {
      setRoutePoints([]);
      return;
    }

    setIsFetchingRoute(true);
    const path = await fetchRoute(currentPosition, point);
    setRoutePoints(path);
    setIsFetchingRoute(false);
  };

  const speedMs = coords?.speed ?? null;
  const speedKmh = speedMs != null && speedMs >= 0 ? speedMs * 3.6 : null;
  const locationHeading =
    coords?.heading != null && coords.heading >= 0 ? normalizeDegrees(coords.heading) : null;

  return (
    <View style={styles.screen}>
      <ScrollView contentContainerStyle={styles.scrollContent}>
        <Text style={styles.title}>Navigation Dashboard</Text>

        <View style={styles.card}>
          <Text style={styles.cardTitle}>Live Position</Text>
          <Text style={styles.readout}>Time: {now.toLocaleString()}</Text>
          <Text style={styles.readout}>
            Lat/Lon: {coords?.latitude.toFixed(6) ?? '--'} / {coords?.longitude.toFixed(6) ?? '--'}
          </Text>
          <Text style={styles.readout}>Accuracy: {coords?.accuracy?.toFixed(1) ?? '--'} m</Text>
          <Text style={styles.readout}>Altitude: {coords?.altitude?.toFixed(1) ?? '--'} m</Text>
          <Text style={styles.readout}>Speed: {speedKmh != null ? `${speedKmh.toFixed(1)} km/h` : '--'}</Text>
          <Text style={styles.readout}>
            Course: {locationHeading != null ? `${locationHeading.toFixed(0)}°` : '--'}
          </Text>
        </View>

        <View style={styles.card}>
          <Text style={styles.cardTitle}>Compass & Target Mode</Text>

          <View style={styles.compassContainer}>
            <View style={styles.headingIndicator} />
            <View
              style={[
                styles.compassDial,
                {
                  transform: [{ rotate: `${-magHeading}deg` }],
                },
              ]}>
              <Text style={[styles.cardinalLabel, styles.cardinalNorth]}>N</Text>
              <Text style={[styles.cardinalLabel, styles.cardinalEast]}>E</Text>
              <Text style={[styles.cardinalLabel, styles.cardinalSouth]}>S</Text>
              <Text style={[styles.cardinalLabel, styles.cardinalWest]}>W</Text>
            </View>
            <Text style={styles.headingLabel}>{magHeading.toFixed(0)}°</Text>
          </View>
          <Text style={styles.facingLabel}>Facing: {headingToCardinal(magHeading)}</Text>

          {target ? (
            <View style={styles.targetBox}>
              <Text style={styles.readout}>Target: {target.latitude.toFixed(6)}, {target.longitude.toFixed(6)}</Text>
              <Text style={styles.readout}>Bearing: {bearingToTarget?.toFixed(0) ?? '--'}°</Text>
              <Text style={styles.readout}>
                Distance: {distanceToTarget != null ? formatDistance(distanceToTarget) : '--'}
              </Text>

              <View style={styles.arrowWrap}>
                <View
                  style={[
                    styles.targetArrow,
                    {
                      transform: [{ rotate: `${relativeArrowRotation}deg` }],
                    },
                  ]}
                />
              </View>

              <Pressable
                onPress={() => {
                  setTarget(null);
                  setRoutePoints([]);
                }}
                style={styles.clearButton}
              >
                <Text style={styles.clearButtonText}>Clear Target</Text>
              </Pressable>
            </View>
          ) : (
            <Text style={styles.readoutMuted}>
              Long-press (or tap) on the map to set a destination target.
            </Text>
          )}
        </View>

        <View style={styles.mapCard}>
          <Text style={styles.cardTitle}>Interactive Map</Text>

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
              void onSelectTarget(event.nativeEvent.coordinate);
            }}
            onPress={(event) => {
              void onSelectTarget(event.nativeEvent.coordinate);
            }}
          >
            {target ? <Marker coordinate={target} title="Target" pinColor="#ff6b6b" /> : null}
            {routePoints.length > 1 ? (
              <Polyline coordinates={routePoints} strokeColor="#58d68d" strokeWidth={5} />
            ) : null}
          </MapView>

          {isFetchingRoute ? (
            <View style={styles.routeLoading}>
              <ActivityIndicator color="#f5f5f5" />
              <Text style={styles.readout}>Calculating route...</Text>
            </View>
          ) : null}
        </View>

        {permissionError ? <Text style={styles.errorText}>Error: {permissionError}</Text> : null}
        {!sensorGranted ? (
          <Text style={styles.readoutMuted}>
            Magnetometer access is unavailable. Compass precision may be reduced.
          </Text>
        ) : null}
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    backgroundColor: '#050608',
  },
  scrollContent: {
    paddingHorizontal: 14,
    paddingTop: 14,
    paddingBottom: 24,
    gap: 12,
  },
  title: {
    color: '#f5f5f5',
    fontSize: 24,
    fontWeight: '700',
    letterSpacing: 0.4,
  },
  card: {
    backgroundColor: '#10131a',
    borderColor: '#2a3040',
    borderWidth: 1,
    borderRadius: 14,
    padding: 12,
    gap: 6,
  },
  mapCard: {
    backgroundColor: '#10131a',
    borderColor: '#2a3040',
    borderWidth: 1,
    borderRadius: 14,
    padding: 12,
    gap: 10,
  },
  cardTitle: {
    color: '#e8edf5',
    fontSize: 16,
    fontWeight: '700',
  },
  readout: {
    color: '#d5dde9',
    fontSize: 14,
    fontVariant: ['tabular-nums'],
  },
  readoutMuted: {
    color: '#94a0b5',
    fontSize: 13,
  },
  compassContainer: {
    height: 130,
    borderRadius: 100,
    borderColor: '#2a3040',
    borderWidth: 1,
    backgroundColor: '#0a0d13',
    alignItems: 'center',
    justifyContent: 'center',
    overflow: 'hidden',
    marginTop: 6,
  },
  compassDial: {
    width: 114,
    height: 114,
    borderRadius: 57,
    borderWidth: 1,
    borderColor: '#2a3040',
    justifyContent: 'center',
    alignItems: 'center',
  },
  headingIndicator: {
    position: 'absolute',
    top: 6,
    width: 0,
    height: 0,
    borderLeftWidth: 7,
    borderRightWidth: 7,
    borderBottomWidth: 14,
    borderLeftColor: 'transparent',
    borderRightColor: 'transparent',
    borderBottomColor: '#ffe082',
  },
  cardinalLabel: {
    position: 'absolute',
    color: '#d5dde9',
    fontWeight: '700',
    fontSize: 13,
  },
  cardinalNorth: {
    top: 8,
    color: '#ffe082',
  },
  cardinalEast: {
    right: 10,
  },
  cardinalSouth: {
    bottom: 8,
  },
  cardinalWest: {
    left: 10,
  },
  facingLabel: {
    color: '#f5f5f5',
    fontWeight: '700',
    fontSize: 14,
    marginTop: 6,
  },
  headingLabel: {
    color: '#f5f5f5',
    fontSize: 18,
    fontWeight: '700',
    position: 'absolute',
    bottom: 10,
  },
  targetBox: {
    gap: 6,
    marginTop: 8,
  },
  arrowWrap: {
    marginTop: 8,
    alignItems: 'center',
    justifyContent: 'center',
    height: 100,
  },
  targetArrow: {
    width: 0,
    height: 0,
    borderLeftWidth: 16,
    borderRightWidth: 16,
    borderBottomWidth: 50,
    borderLeftColor: 'transparent',
    borderRightColor: 'transparent',
    borderBottomColor: '#58d68d',
  },
  clearButton: {
    marginTop: 6,
    alignSelf: 'flex-start',
    backgroundColor: '#202735',
    borderRadius: 8,
    paddingHorizontal: 10,
    paddingVertical: 6,
  },
  clearButtonText: {
    color: '#f2f2f2',
    fontWeight: '600',
    fontSize: 13,
  },
  map: {
    width: '100%',
    height: 320,
    borderRadius: 12,
  },
  routeLoading: {
    flexDirection: 'row',
    gap: 8,
    alignItems: 'center',
  },
  errorText: {
    color: '#ff8a80',
    fontSize: 13,
  },
});

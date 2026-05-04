import { createContext, useCallback, useContext, useEffect, useMemo, useRef, useState, type ReactNode } from 'react';
import * as Location from 'expo-location';
import type { LatLng } from 'react-native-maps';

import {
  calculateBearingDegrees,
  haversineDistanceMeters,
  normalizeDegrees,
  smoothHeadingSamples,
} from './navigation-math';

export type RoutePoint = LatLng;

type NavigationContextValue = {
  permissionError: string | null;
  sensorGranted: boolean;
  locationGranted: boolean;
  isFetchingRoute: boolean;
  coords: Location.LocationObjectCoords | null;
  currentPosition: LatLng | null;
  magHeading: number;
  locationHeading: number | null;
  target: LatLng | null;
  routePoints: RoutePoint[];
  distanceToTarget: number | null;
  bearingToTarget: number | null;
  relativeArrowRotation: number;
  speedKmh: number | null;
  selectTarget: (point: LatLng) => Promise<void>;
  clearTarget: () => void;
};

const NavigationContext = createContext<NavigationContextValue | null>(null);

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

export function NavigationProvider({ children }: { children: ReactNode }) {
  const headingSamplesRef = useRef<number[]>([]);
  const isMountedRef = useRef(true);
  const routeRequestIdRef = useRef(0);

  const [permissionError, setPermissionError] = useState<string | null>(null);
  const [sensorGranted, setSensorGranted] = useState<boolean>(false);
  const [locationGranted, setLocationGranted] = useState<boolean>(false);
  const [isFetchingRoute, setIsFetchingRoute] = useState<boolean>(false);
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

  const speedKmh = useMemo<number | null>(() => {
    const speedMs = coords?.speed ?? null;
    return speedMs != null && speedMs >= 0 ? speedMs * 3.6 : null;
  }, [coords]);

  const locationHeading = useMemo<number | null>(() => {
    return coords?.heading != null && coords.heading >= 0 ? normalizeDegrees(coords.heading) : null;
  }, [coords]);

  useEffect(() => {
    let isMounted = true;
    let locationSubscription: Location.LocationSubscription | null = null;
    let headingSubscription: { remove: () => void } | null = null;

    const setup = async () => {
      try {
        const locationPermission = await Location.requestForegroundPermissionsAsync();

        if (!isMounted) return;

        const hasLocationPermission = locationPermission.granted;

        setLocationGranted(hasLocationPermission);
        setSensorGranted(false);

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

        headingSubscription = await Location.watchHeadingAsync((heading) => {
          if (!isMounted) return;

          const nextHeading = heading.trueHeading >= 0 ? heading.trueHeading : heading.magHeading;
          headingSamplesRef.current.push(normalizeDegrees(nextHeading));

          if (headingSamplesRef.current.length > 9) {
            headingSamplesRef.current.splice(0, headingSamplesRef.current.length - 9);
          }

          setSensorGranted(true);
        });
      } catch (error) {
        if (isMounted) {
          setPermissionError(
            error instanceof Error
              ? error.message
              : 'Unable to initialize location/heading services.'
          );
        }
      }
    };

    void setup();

    return () => {
      isMounted = false;
      headingSamplesRef.current = [];
      locationSubscription?.remove();
      headingSubscription?.remove();
    };
  }, []);

  useEffect(() => {
    const headingTimerId = setInterval(() => {
      const nextHeading = smoothHeadingSamples(headingSamplesRef.current);

      if (nextHeading != null) {
        setMagHeading(nextHeading);
      }
    }, 250);

    return () => clearInterval(headingTimerId);
  }, []);

  const selectTarget = useCallback(
    async (point: LatLng) => {
      if (!isMountedRef.current) return;

      const requestId = ++routeRequestIdRef.current;

      setTarget(point);

      if (!currentPosition) {
        setRoutePoints([]);
        return;
      }

      setIsFetchingRoute(true);

      try {
        const path = await fetchRoute(currentPosition, point);
        if (isMountedRef.current && routeRequestIdRef.current === requestId) {
          setRoutePoints(path);
        }
      } finally {
        if (isMountedRef.current && routeRequestIdRef.current === requestId) {
          setIsFetchingRoute(false);
        }
      }
    },
    [currentPosition]
  );

  const clearTarget = useCallback(() => {
    routeRequestIdRef.current += 1;
    setTarget(null);
    setRoutePoints([]);
    setIsFetchingRoute(false);
  }, []);

  const value = useMemo<NavigationContextValue>(
    () => ({
      permissionError,
      sensorGranted,
      locationGranted,
      isFetchingRoute,
      coords,
      currentPosition,
      magHeading,
      locationHeading,
      target,
      routePoints,
      distanceToTarget,
      bearingToTarget,
      relativeArrowRotation,
      speedKmh,
      selectTarget,
      clearTarget,
    }),
    [
      permissionError,
      sensorGranted,
      locationGranted,
      isFetchingRoute,
      coords,
      currentPosition,
      magHeading,
      locationHeading,
      target,
      routePoints,
      distanceToTarget,
      bearingToTarget,
      relativeArrowRotation,
      speedKmh,
      selectTarget,
      clearTarget,
    ]
  );

  useEffect(() => {
    return () => {
      isMountedRef.current = false;
    };
  }, []);

  return <NavigationContext.Provider value={value}>{children}</NavigationContext.Provider>;
}

export function useNavigationDashboard() {
  const context = useContext(NavigationContext);

  if (!context) {
    throw new Error('useNavigationDashboard must be used within a NavigationProvider.');
  }

  return context;
}

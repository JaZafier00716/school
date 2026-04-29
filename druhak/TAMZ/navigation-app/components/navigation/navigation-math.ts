import type { LatLng } from 'react-native-maps';

export const EARTH_RADIUS_M = 6371000;

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

export function normalizeDegrees(degrees: number): number {
  return ((degrees % 360) + 360) % 360;
}

export function angularDistanceDegrees(a: number, b: number): number {
  const delta = Math.abs(normalizeDegrees(a - b));
  return Math.min(delta, 360 - delta);
}

export function circularMeanDegrees(values: number[]): number | null {
  if (values.length === 0) return null;

  let sumSin = 0;
  let sumCos = 0;

  for (const value of values) {
    const radians = toRadians(value);
    sumSin += Math.sin(radians);
    sumCos += Math.cos(radians);
  }

  return normalizeDegrees(toDegrees(Math.atan2(sumSin, sumCos)));
}

export function smoothHeadingSamples(values: number[]): number | null {
  if (values.length === 0) return null;
  if (values.length <= 2) return circularMeanDegrees(values);

  const center = circularMeanDegrees(values);
  if (center == null) return null;

  const sorted = [...values].sort(
    (left, right) => angularDistanceDegrees(left, center) - angularDistanceDegrees(right, center)
  );

  const trimCount = Math.min(1, Math.floor(sorted.length / 3));
  const trimmed = sorted.slice(0, sorted.length - trimCount);

  return circularMeanDegrees(trimmed) ?? center;
}

export function formatDistance(distanceMeters: number): string {
  if (distanceMeters < 1000) {
    return `${distanceMeters.toFixed(0)} m`;
  }

  return `${(distanceMeters / 1000).toFixed(2)} km`;
}

export function headingToCardinal(heading: number): string {
  const directions = ['N', 'NE', 'E', 'SE', 'S', 'SW', 'W', 'NW'];
  const index = Math.round(heading / 45) % 8;
  return directions[index];
}


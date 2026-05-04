import { useEffect, useState } from 'react';
import { StyleSheet, Text } from 'react-native';

import { SectionCard } from '../SectionCard';
import { useNavigationDashboard } from '../navigation-provider';

export function LivePositionSection() {
  const { coords, locationHeading, permissionError, speedKmh } = useNavigationDashboard();
  const [now, setNow] = useState(new Date());

  useEffect(() => {
    const timerId = setInterval(() => {
      setNow(new Date());
    }, 1000);

    return () => clearInterval(timerId);
  }, []);

  return (
    <SectionCard title="Live Position">
      <Text style={styles.readout}>Time: {now.toLocaleString()}</Text>
      <Text style={styles.readout}>
        Lat/Lon: {coords?.latitude.toFixed(6) ?? '--'} / {coords?.longitude.toFixed(6) ?? '--'}
      </Text>
      <Text style={styles.readout}>Accuracy: {coords?.accuracy?.toFixed(1) ?? '--'} m</Text>
      <Text style={styles.readout}>Altitude: {coords?.altitude?.toFixed(1) ?? '--'} m</Text>
      <Text style={styles.readout}>Altitude Accuracy: {coords?.altitudeAccuracy?.toFixed(1) ?? '--'} m</Text>
      <Text style={styles.readout}>Speed: {speedKmh != null ? `${speedKmh.toFixed(1)} km/h` : '--'}</Text>
      <Text style={styles.readout}>
        Course: {locationHeading != null ? `${locationHeading.toFixed(0)}°` : '--'}
      </Text>

      {permissionError ? <Text style={styles.errorText}>Error: {permissionError}</Text> : null}
    </SectionCard>
  );
}

const styles = StyleSheet.create({
  readout: {
    color: '#d5dde9',
    fontSize: 14,
    fontVariant: ['tabular-nums'],
  },
  errorText: {
    color: '#ff8a80',
    fontSize: 13,
  },
});



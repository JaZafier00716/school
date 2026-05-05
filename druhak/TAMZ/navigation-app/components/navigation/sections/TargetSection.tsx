import { useCallback, useEffect, useMemo, useState } from 'react';
import {
  ActivityIndicator,
  Keyboard,
  Platform,
  Pressable,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';

import { SectionCard } from '../SectionCard';
import { useNavigationDashboard } from '../navigation-provider';
import { formatDistance } from '../navigation-math';
import { NavigationArrow } from '../NavigationArrow';
import { getAllLocations, saveLocation, deleteLocation, type SavedLocation } from '../location-store';

type NominatimResult = {
  place_id: number;
  display_name: string;
  lat: string;
  lon: string;
};

export function TargetSection() {
  const { bearingToTarget, clearTarget, distanceToTarget, target, relativeArrowRotation, selectTarget } =
    useNavigationDashboard();

  const [query, setQuery] = useState('');
  const [searching, setSearching] = useState(false);
  const [searchedOnce, setSearchedOnce] = useState(false);
  const [searchError, setSearchError] = useState<string | null>(null);
  const [results, setResults] = useState<NominatimResult[]>([]);
  const [saved, setSaved] = useState<SavedLocation[]>([]);

  useEffect(() => {
    void (async () => {
      const items = await getAllLocations();
      setSaved(items);
    })();
  }, []);

  useEffect(() => {
    if (!query || query.trim().length < 2) {
      setResults([]);
      setSearchedOnce(false);
      setSearchError(null);
      return;
    }
  }, [query]);

  const doSearch = useCallback(async (q: string) => {
    setSearchedOnce(true);
    setSearching(true);
    setSearchError(null);
    try {
      const url = `https://nominatim.openstreetmap.org/search?format=jsonv2&limit=8&addressdetails=1&accept-language=en&q=${encodeURIComponent(
        q
      )}`;

      const headers: Record<string, string> = {};
      // Browsers restrict setting User-Agent; native clients can send one.
      if (Platform.OS !== 'web') {
        headers['User-Agent'] = 'navigation-app/1.0';
      }

      const resp = await fetch(url, {
        headers,
      });

      if (!resp.ok) {
        setResults([]);
        setSearchError(`Search failed (${resp.status}).`);
        return;
      }

      const raw = (await resp.json()) as NominatimResult[] | { error?: string };
      if (Array.isArray(raw)) {
        setResults(raw);
        return;
      }

      setResults([]);
      setSearchError(raw.error ?? 'Search returned an unexpected response.');
    } catch {
      setResults([]);
      setSearchError('Network error while searching. Please try again.');
    } finally {
      setSearching(false);
    }
  }, []);

  const onSelectResult = useCallback(
    async (item: NominatimResult) => {
      const lat = Number(item.lat);
      const lon = Number(item.lon);
      await selectTarget({ latitude: lat, longitude: lon });
      setQuery('');
      setResults([]);
    },
    [selectTarget]
  );

  const onSaveResult = useCallback(async (item: NominatimResult) => {
    const latitude = Number(item.lat);
    const longitude = Number(item.lon);
    const savedItem = await saveLocation({
      name: item.display_name,
      latitude,
      longitude,
    });
    setSaved((s) => [savedItem, ...s.filter((x) => x.id !== savedItem.id)]);
  }, []);

  const onSearchPress = useCallback(async () => {
    const trimmed = query.trim();
    if (trimmed.length < 2) {
      setResults([]);
      setSearchedOnce(false);
      return;
    }

    Keyboard.dismiss();
    await doSearch(trimmed);
  }, [query, doSearch]);

  const onSaveTarget = useCallback(
    async (name?: string) => {
      if (!target) return;
      const title = name ?? `Saved ${new Date().toLocaleString()}`;
      const savedItem = await saveLocation({ name: title, latitude: target.latitude, longitude: target.longitude });
      setSaved((s) => [savedItem, ...s.filter((x) => x.id !== savedItem.id)]);
    },
    [target]
  );

  const onDelete = useCallback(async (id: string) => {
    await deleteLocation(id);
    setSaved((s) => s.filter((x) => x.id !== id));
  }, []);

  const renderResult = (item: NominatimResult) => (
    <View style={styles.resultRow}>
      <Pressable onPress={() => void onSelectResult(item)}>
        <Text style={styles.resultText}>{item.display_name}</Text>
        <Text style={styles.resultMeta}>
          {Number(item.lat).toFixed(6)}, {Number(item.lon).toFixed(6)}
        </Text>
      </Pressable>
      <View style={styles.resultActions}>
        <Pressable onPress={() => void onSelectResult(item)} style={styles.secondaryButton}>
          <Text style={styles.secondaryButtonText}>Navigate</Text>
        </Pressable>
        <Pressable onPress={() => void onSaveResult(item)} style={styles.primaryButton}>
          <Text style={styles.primaryButtonText}>Save</Text>
        </Pressable>
      </View>
    </View>
  );

  const renderSaved = (item: SavedLocation) => (
    <View style={styles.savedRow}>
      <Pressable onPress={() => void selectTarget({ latitude: item.latitude, longitude: item.longitude })} style={{ flex: 1 }}>
        <Text style={styles.resultText}>{item.name}</Text>
        <Text style={styles.resultMeta}>{item.latitude.toFixed(6)}, {item.longitude.toFixed(6)}</Text>
      </Pressable>
      <Pressable onPress={() => void onDelete(item.id)} style={styles.deleteButton}>
        <Text style={{ color: '#fff' }}>Delete</Text>
      </Pressable>
    </View>
  );

  const targetInfo = useMemo(() => {
    if (!target) return null;
    return (
      <View style={styles.targetBox}>
        <Text style={styles.readout}>
          Target: {target.latitude.toFixed(6)}, {target.longitude.toFixed(6)}
        </Text>
        <Text style={styles.readout}>Bearing: {bearingToTarget?.toFixed(0) ?? '--'}°</Text>
        <Text style={styles.readout}>
          Distance: {distanceToTarget != null ? formatDistance(distanceToTarget) : '--'}
        </Text>

        <NavigationArrow rotation={relativeArrowRotation} />

        <View style={styles.buttonRow}>
          <Pressable onPress={clearTarget} style={styles.secondaryButton}>
            <Text style={styles.secondaryButtonText}>Clear Target</Text>
          </Pressable>
          <Pressable onPress={() => void onSaveTarget()} style={[styles.primaryButton, { marginLeft: 8 }] }>
            <Text style={styles.primaryButtonText}>Save Location</Text>
          </Pressable>
        </View>
      </View>
    );
  }, [target, bearingToTarget, distanceToTarget, relativeArrowRotation, clearTarget, onSaveTarget]);

  return (
    <SectionCard title="Target & Search">
      <View style={{ gap: 8 }}>
        <View style={styles.searchRow}>
          <TextInput
            placeholder="Search address or place"
            placeholderTextColor="#94a0b5"
            value={query}
            onChangeText={setQuery}
            onSubmitEditing={() => void onSearchPress()}
            returnKeyType="search"
            style={styles.searchInput}
          />
          <Pressable onPress={() => void onSearchPress()} style={styles.primaryButton}>
            <Text style={styles.primaryButtonText}>Search</Text>
          </Pressable>
        </View>

        {searching ? <ActivityIndicator /> : null}

        {results.length > 0 ? (
          <View>
            {results.map((item) => (
              <View key={String(item.place_id)}>{renderResult(item)}</View>
            ))}
          </View>
        ) : searchError ? (
          <Text style={styles.errorText}>{searchError}</Text>
        ) : searchedOnce && !searching ? (
          <Text style={styles.readoutMuted}>No results found.</Text>
        ) : null}

        {targetInfo}

        <Text style={styles.sectionTitle}>Saved locations</Text>
        {saved.length === 0 ? (
          <Text style={styles.readoutMuted}>No saved locations yet.</Text>
        ) : (
          <View>
            {saved.map((item) => (
              <View key={item.id}>{renderSaved(item)}</View>
            ))}
          </View>
        )}
      </View>
    </SectionCard>
  );
}

const styles = StyleSheet.create({
  targetBox: {
    gap: 6,
  },
  searchInput: {
    flex: 1,
    backgroundColor: '#0f1319',
    borderColor: '#2a3040',
    borderWidth: 1,
    paddingHorizontal: 10,
    paddingVertical: 8,
    borderRadius: 8,
    color: '#e8eef7',
  },
  resultRow: {
    paddingVertical: 10,
    borderBottomWidth: 1,
    borderBottomColor: '#151a24',
    gap: 8,
  },
  searchRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  resultActions: {
    flexDirection: 'row',
    gap: 8,
  },
  savedRow: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 10,
    borderBottomWidth: 1,
    borderBottomColor: '#151a24',
  },
  resultText: {
    color: '#d5dde9',
    fontSize: 14,
  },
  resultMeta: {
    color: '#94a0b5',
    fontSize: 12,
  },
  sectionTitle: {
    color: '#a8b4c6',
    marginTop: 6,
    marginBottom: 2,
  },
  emptyState: {
    gap: 10,
  },
  buttonRow: {
    marginTop: 4,
    flexDirection: 'row',
  },
  readout: {
    color: '#d5dde9',
    fontSize: 14,
  },
  readoutMuted: {
    color: '#94a0b5',
    fontSize: 13,
  },
  errorText: {
    color: '#fca5a5',
    fontSize: 13,
  },
  secondaryButton: {
    backgroundColor: '#151a24',
    borderColor: '#2a3040',
    borderWidth: 1,
    borderRadius: 8,
    paddingHorizontal: 12,
    paddingVertical: 8,
  },
  secondaryButtonText: {
    color: '#f2f2f2',
    fontWeight: '600' as const,
    fontSize: 13,
  },
  primaryButton: {
    backgroundColor: '#3b82f6',
    borderRadius: 8,
    paddingHorizontal: 12,
    paddingVertical: 8,
  },
  primaryButtonText: {
    color: '#fff',
    fontWeight: '600' as const,
    fontSize: 13,
  },
  deleteButton: {
    backgroundColor: '#ef4444',
    paddingHorizontal: 10,
    paddingVertical: 6,
    borderRadius: 8,
    marginLeft: 8,
  },
});




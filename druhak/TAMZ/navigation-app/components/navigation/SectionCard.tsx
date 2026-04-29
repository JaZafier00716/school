import type { ReactNode } from 'react';
import { StyleSheet, Text, View, type ViewStyle } from 'react-native';

type SectionCardProps = {
  title: string;
  children: ReactNode;
  style?: ViewStyle;
};

export function SectionCard({ title, children, style }: SectionCardProps) {
  return (
    <View style={[styles.card, style]}>
      <Text style={styles.title}>{title}</Text>
      {children}
    </View>
  );
}

const styles = StyleSheet.create({
  card: {
    backgroundColor: '#10131a',
    borderColor: '#2a3040',
    borderWidth: 1,
    borderRadius: 14,
    padding: 12,
    gap: 10,
  },
  title: {
    color: '#e8edf5',
    fontSize: 16,
    fontWeight: '700',
  },
});


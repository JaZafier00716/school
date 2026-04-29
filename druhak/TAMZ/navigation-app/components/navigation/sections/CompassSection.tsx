import { useState } from 'react';
import { Text, View } from 'react-native';
import { Gesture, GestureDetector } from 'react-native-gesture-handler';

import { CompassDial } from '../CompassDial';
import { NavigationArrow } from '../NavigationArrow';
import { SectionCard } from '../SectionCard';
import { useNavigationDashboard } from '../navigation-provider';
import { formatDistance, headingToCardinal } from '../navigation-math';

export function CompassSection() {
  const {
    bearingToTarget,
    distanceToTarget,
    magHeading,
    relativeArrowRotation,
    sensorGranted,
    target,
  } = useNavigationDashboard();

  const [isExpanded, setIsExpanded] = useState(false);
  const [startY, setStartY] = useState(0);

  const swipeGesture = Gesture.Pan()
    .onStart((event) => {
      setStartY(event.absoluteY);
    })
    .onUpdate((event) => {
      const deltaY = startY - event.absoluteY;
      if (deltaY > 20) {
        setIsExpanded(true);
      }
    });

  const tapGesture = Gesture.Tap()
    .onEnd(() => {
      setIsExpanded((prev) => !prev);
    });

  const combinedGesture = Gesture.Simultaneous(tapGesture, swipeGesture);

  return (
    <SectionCard title="Compass & Target Mode">
      <View style={styles.centered}>
        <CompassDial heading={magHeading} />
      </View>
      <Text style={styles.facingLabel}>Facing: {headingToCardinal(magHeading)}</Text>

      {target ? (
        <GestureDetector gesture={combinedGesture}>
          <View>
            <View style={[styles.targetSummary, !isExpanded && styles.collapsedTarget]}>
              {isExpanded && (
                <>
                  <Text style={styles.readout}>
                    Target bearing: {bearingToTarget?.toFixed(0) ?? '--'}°
                  </Text>
                  <Text style={styles.readout}>
                    Distance: {distanceToTarget != null ? formatDistance(distanceToTarget) : '--'}
                  </Text>
                </>
              )}
              <NavigationArrow rotation={relativeArrowRotation} />
            </View>
            {!isExpanded && (
              <Text style={styles.slideHint}>⬆ Slide up or tap to expand</Text>
            )}
          </View>
        </GestureDetector>
      ) : (
        <Text style={styles.readoutMuted}>Open the Map tab to choose a target destination.</Text>
      )}

      {!sensorGranted ? (
        <Text style={styles.readoutMuted}>Compass heading is unavailable right now.</Text>
      ) : null}
    </SectionCard>
  );
}

const styles = {
  centered: {
    alignItems: 'center' as const,
    justifyContent: 'center' as const,
  },
  facingLabel: {
    color: '#f5f5f5',
    fontWeight: '700' as const,
    fontSize: 14,
  },
  targetSummary: {
    gap: 4,
  },
  collapsedTarget: {
    minHeight: 80,
  },
  readout: {
    color: '#d5dde9',
    fontSize: 14,
  },
  slideHint: {
    color: '#5a7387',
    fontSize: 12,
    fontStyle: 'italic' as const,
    marginTop: 6,
    textAlign: 'center' as const,
  },
  readoutMuted: {
    color: '#94a0b5',
    fontSize: 13,
  },
};


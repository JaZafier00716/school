#pragma once

#include <algorithm>
#include <cstddef>
#include <numeric>
#include <string>
#include <vector>

namespace dg {

enum class Units {
    Metric,
    Imperial,
};

struct Distance {
    int metricMeters {};
    int imperialFeet {};
};

struct Hole {
    int id {};
    int par {};
    Distance distance {};
    int throwsCount {};
};

struct Round {
    std::string name {"Course Name"};
    std::string location {"Course Location"};
    int totalPar {72};
    std::vector<Hole> holes {};
};

inline int roundTotalThrows(const Round& round) {
    int result = 0;
    for (const auto& hole : round.holes) {
        result += hole.throwsCount;
    }
    return result;
}

inline int roundRelativeToPar(const Round& round) {
    return roundTotalThrows(round) - round.totalPar;
}

struct SavedRound {
    std::string id {};
    std::string playedAtIsoUtc {};
    Round round {};

    [[nodiscard]] int totalThrows() const {
        return roundTotalThrows(round);
    }

    [[nodiscard]] int relativeToPar() const {
        return roundRelativeToPar(round);
    }
};

struct HistoryStatistics {
    int roundsPlayed {0};
    int totalThrows {0};
    double averageRelativeToPar {0.0};
};

inline HistoryStatistics calculateHistoryStatistics(const std::vector<SavedRound>& rounds) {
    HistoryStatistics stats;
    stats.roundsPlayed = static_cast<int>(rounds.size());
    for (const auto& savedRound : rounds) {
        stats.totalThrows += savedRound.totalThrows();
    }

    if (!rounds.empty()) {
        const auto totalRelative = std::accumulate(rounds.begin(), rounds.end(), 0, [](const int acc, const SavedRound& round) {
            return acc + round.relativeToPar();
        });
        stats.averageRelativeToPar = static_cast<double>(totalRelative) / static_cast<double>(rounds.size());
    }

    return stats;
}

struct AppState {
    std::string nickname {"ZAM0074"};
    Units units {Units::Metric};
    bool darkThemeEnabled {true};
    Round round {};

    [[nodiscard]] int totalThrows() const {
        int result = 0;
        for (const auto& hole : round.holes) {
            result += hole.throwsCount;
        }
        return result;
    }

    [[nodiscard]] int relativeScoreUpToHole(std::size_t exclusiveHoleIndex) const {
        int score = 0;
        const auto end = std::min(exclusiveHoleIndex, round.holes.size());
        for (std::size_t i = 0; i < end; ++i) {
            const auto& hole = round.holes[i];
            if (hole.throwsCount > 0) {
                score += hole.throwsCount - hole.par;
            }
        }
        return score;
    }

    void clearCurrentRoundStats() {
        for (auto& hole : round.holes) {
            hole.throwsCount = 0;
        }
    }

    [[nodiscard]] static AppState createDemo() {
        AppState state;
        state.round.holes = {
            Hole{1, 4, Distance{107, 350}, 0},
            Hole{2, 3, Distance{46, 150}, 0},
            Hole{3, 5, Distance{152, 500}, 0},
            Hole{4, 4, Distance{122, 400}, 0},
            Hole{5, 4, Distance{107, 350}, 0},
            Hole{6, 3, Distance{46, 150}, 0},
            Hole{7, 5, Distance{152, 500}, 0},
            Hole{8, 4, Distance{122, 400}, 0},
            Hole{9, 4, Distance{107, 350}, 0},
            Hole{10, 4, Distance{107, 350}, 0},
            Hole{11, 3, Distance{46, 150}, 0},
            Hole{12, 5, Distance{152, 500}, 0},
            Hole{13, 4, Distance{122, 400}, 0},
            Hole{14, 4, Distance{107, 350}, 0},
            Hole{15, 4, Distance{107, 350}, 0},
            Hole{16, 3, Distance{46, 150}, 0},
            Hole{17, 5, Distance{152, 500}, 0},
            Hole{18, 4, Distance{122, 400}, 0},
        };
        return state;
    }
};

inline std::string unitsLabel(const Units units) {
    return units == Units::Metric ? "Meters" : "Feet";
}

inline std::string distanceLabel(const Hole& hole, const Units units) {
    if (units == Units::Metric) {
        return std::to_string(hole.distance.metricMeters) + "m";
    }
    return std::to_string(hole.distance.imperialFeet) + "ft";
}

} // namespace dg


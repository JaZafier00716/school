#include "models.hpp"

#include <cassert>

int main() {
    auto state = dg::AppState::createDemo();

    assert(state.round.holes.size() == 18);
    assert(state.totalThrows() == 0);

    state.round.holes[0].throwsCount = 5;
    state.round.holes[1].throwsCount = 2;

    assert(state.totalThrows() == 7);
    assert(state.relativeScoreUpToHole(1) == 1); // hole 1: 5 - 4
    assert(state.relativeScoreUpToHole(2) == 0); // hole 2: 2 - 3

    state.clearCurrentRoundStats();
    assert(state.totalThrows() == 0);

    state.round.holes[0].throwsCount = 5;
    state.round.holes[1].throwsCount = 2;

    dg::SavedRound savedRound1;
    savedRound1.round = state.round;

    auto secondRound = state.round;
    secondRound.holes[0].throwsCount = 4;
    secondRound.holes[1].throwsCount = 3;
    dg::SavedRound savedRound2;
    savedRound2.round = secondRound;

    const std::vector<dg::SavedRound> history {savedRound1, savedRound2};
    const auto historyStats = dg::calculateHistoryStatistics(history);

    assert(historyStats.roundsPlayed == 2);
    assert(historyStats.totalThrows == 14);
    assert(historyStats.averageRelativeToPar < 0.0);

    return 0;
}


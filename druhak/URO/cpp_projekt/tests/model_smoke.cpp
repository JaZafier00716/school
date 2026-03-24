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

    return 0;
}


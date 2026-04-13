#pragma once

#include "models.hpp"

#include <QDialog>
#include <QLabel>
#include <QTableWidget>

namespace dg {

class RoundSummaryDialog final : public QDialog {
public:
    explicit RoundSummaryDialog(AppState& state, QWidget* parent = nullptr);
    [[nodiscard]] bool wasRoundSaved() const { return wasRoundSaved_; }
    [[nodiscard]] bool shouldJumpToFirstHole() const { return shouldJumpToFirstHole_; }

private:
    AppState& state_;
    QLabel* parLabel_ {nullptr};
    QLabel* totalThrowsLabel_ {nullptr};
    QLabel* averageLabel_ {nullptr};
    QTableWidget* holeTable_ {nullptr};
    bool wasRoundSaved_ {false};
    bool shouldJumpToFirstHole_ {false};

    void populateTable();
    void refreshStats();
};

} // namespace dg


#pragma once

#include "models.hpp"

#include <QDialog>
#include <QLabel>
#include <QTableWidget>

namespace dg {

class RoundSummaryDialog final : public QDialog {
public:
    explicit RoundSummaryDialog(const AppState& state, QWidget* parent = nullptr);

private:
    const AppState& state_;
    QLabel* parLabel_ {nullptr};
    QLabel* totalThrowsLabel_ {nullptr};
    QLabel* averageLabel_ {nullptr};
    QTableWidget* holeTable_ {nullptr};

    void populateTable();
};

} // namespace dg


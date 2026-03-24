#pragma once

#include "models.hpp"

#include <QLabel>
#include <QMainWindow>
#include <QPushButton>

namespace dg {

class CourseWindow final : public QMainWindow {
public:
    explicit CourseWindow(AppState& state, QWidget* parent = nullptr);

private:
    AppState& state_;
    std::size_t currentHoleIndex_ {0};

    QLabel* holeLabel_ {nullptr};
    QLabel* scoreLabel_ {nullptr};
    QLabel* parValueLabel_ {nullptr};
    QLabel* distanceValueLabel_ {nullptr};
    QLabel* throwsValueLabel_ {nullptr};
    QPushButton* minusThrowsButton_ {nullptr};
    QPushButton* plusThrowsButton_ {nullptr};
    QPushButton* previousButton_ {nullptr};
    QPushButton* nextButton_ {nullptr};

    void updateUiFromState();
    void updateThrowCount(int delta);
    void goToPreviousHole();
    void goToNextHole();
    void openSettings();
    void openRoundSummary();
};

} // namespace dg


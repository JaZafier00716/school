#include "course_window.hpp"

#include "game_history_window.hpp"
#include "round_summary_dialog.hpp"
#include "settings_dialog.hpp"
#include "theme_manager.hpp"

#include <QApplication>
#include <QFrame>
#include <QGridLayout>
#include <QHBoxLayout>
#include <QPushButton>
#include <QVBoxLayout>

#include <algorithm>

namespace dg {

CourseWindow::CourseWindow(AppState& state, QWidget* parent)
    : QMainWindow(parent)
    , state_(state) {
    setWindowTitle("DiscGolf Tracker");
    resize(480, 720);

    auto* central = new QWidget(this);
    auto* root = new QVBoxLayout(central);
    root->setContentsMargins(12, 12, 12, 12);
    root->setSpacing(12);
    setCentralWidget(central);

    auto* topCard = new QFrame(central);
    topCard->setObjectName("card");
    auto* topLayout = new QHBoxLayout(topCard);

    auto* titleWrap = new QWidget(topCard);
    auto* titleLayout = new QVBoxLayout(titleWrap);
    titleLayout->setContentsMargins(0, 0, 0, 0);
    auto* title = new QLabel(QString::fromStdString(state_.round.name), titleWrap);
    auto* subtitle = new QLabel(QString::fromStdString(state_.round.location), titleWrap);
    subtitle->setObjectName("sectionMuted");
    auto titleFont = title->font();
    titleFont.setPointSize(16);
    titleFont.setBold(true);
    title->setFont(titleFont);
    titleLayout->addWidget(title);
    titleLayout->addWidget(subtitle);

    auto* historyButton = new QPushButton("History", topCard);
    historyButton->setObjectName("secondaryButton");
    connect(historyButton, &QPushButton::clicked, this, [this] { openHistory(); });

    auto* settingsButton = new QPushButton("Settings", topCard);
    settingsButton->setObjectName("secondaryButton");
    connect(settingsButton, &QPushButton::clicked, this, [this] { openSettings(); });

    topLayout->addWidget(titleWrap, 1);
    topLayout->addWidget(historyButton);
    topLayout->addWidget(settingsButton);
    root->addWidget(topCard);

    auto* scoreCard = new QFrame(central);
    scoreCard->setObjectName("cardAlt");
    auto* scoreLayout = new QHBoxLayout(scoreCard);
    holeLabel_ = new QLabel(scoreCard);
    scoreLabel_ = new QLabel(scoreCard);
    scoreLayout->addWidget(holeLabel_);
    scoreLayout->addStretch();
    scoreLayout->addWidget(scoreLabel_);
    root->addWidget(scoreCard);

    auto* detailsCard = new QFrame(central);
    detailsCard->setObjectName("card");
    auto* detailsLayout = new QGridLayout(detailsCard);
    detailsLayout->addWidget(new QLabel("Par", detailsCard), 0, 0);
    detailsLayout->addWidget(new QLabel("Distance", detailsCard), 0, 1);

    parValueLabel_ = new QLabel(detailsCard);
    distanceValueLabel_ = new QLabel(detailsCard);
    auto valueFont = parValueLabel_->font();
    valueFont.setPointSize(18);
    valueFont.setBold(true);
    parValueLabel_->setFont(valueFont);
    distanceValueLabel_->setFont(valueFont);

    detailsLayout->addWidget(parValueLabel_, 1, 0);
    detailsLayout->addWidget(distanceValueLabel_, 1, 1);
    root->addWidget(detailsCard);

    auto* throwsCard = new QFrame(central);
    throwsCard->setObjectName("card");
    auto* throwsLayout = new QVBoxLayout(throwsCard);
    auto* throwsText = new QLabel("Throws", throwsCard);
    throwsText->setObjectName("sectionMuted");
    throwsValueLabel_ = new QLabel(throwsCard);
    auto throwsFont = throwsValueLabel_->font();
    throwsFont.setPointSize(42);
    throwsFont.setBold(true);
    throwsValueLabel_->setFont(throwsFont);
    throwsValueLabel_->setAlignment(Qt::AlignCenter);

    auto* throwButtons = new QHBoxLayout();
    minusThrowsButton_ = new QPushButton("-", throwsCard);
    plusThrowsButton_ = new QPushButton("+", throwsCard);
    minusThrowsButton_->setObjectName("secondaryButton");
    plusThrowsButton_->setObjectName("primaryButton");
    minusThrowsButton_->setFixedWidth(100);
    plusThrowsButton_->setFixedWidth(100);

    connect(minusThrowsButton_, &QPushButton::clicked, this, [this] { updateThrowCount(-1); });
    connect(plusThrowsButton_, &QPushButton::clicked, this, [this] { updateThrowCount(1); });

    throwButtons->addStretch();
    throwButtons->addWidget(minusThrowsButton_);
    throwButtons->addWidget(plusThrowsButton_);
    throwButtons->addStretch();

    throwsLayout->addWidget(throwsText, 0, Qt::AlignHCenter);
    throwsLayout->addWidget(throwsValueLabel_);
    throwsLayout->addLayout(throwButtons);
    root->addWidget(throwsCard, 1);

    auto* navCard = new QFrame(central);
    navCard->setObjectName("cardAlt");
    auto* navCardLayout = new QVBoxLayout(navCard);
    auto* navRow = new QHBoxLayout();
    previousButton_ = new QPushButton("< Previous", navCard);
    nextButton_ = new QPushButton("Next >", navCard);
    auto* finishButton = new QPushButton("Finish Round", navCard);
    previousButton_->setObjectName("secondaryButton");
    nextButton_->setObjectName("secondaryButton");
    finishButton->setObjectName("primaryButton");

    connect(previousButton_, &QPushButton::clicked, this, [this] { goToPreviousHole(); });
    connect(nextButton_, &QPushButton::clicked, this, [this] { goToNextHole(); });
    connect(finishButton, &QPushButton::clicked, this, [this] { openRoundSummary(); });

    navRow->addWidget(previousButton_);
    navRow->addWidget(nextButton_);
    navCardLayout->addLayout(navRow);
    navCardLayout->addWidget(finishButton);
    root->addWidget(navCard);

    updateUiFromState();
}

void CourseWindow::updateUiFromState() {
    if (state_.round.holes.empty()) {
        holeLabel_->setText("No holes");
        scoreLabel_->setText("Score: 0");
        parValueLabel_->setText("-");
        distanceValueLabel_->setText("-");
        throwsValueLabel_->setText("0");
        previousButton_->setEnabled(false);
        nextButton_->setEnabled(false);
        minusThrowsButton_->setEnabled(false);
        plusThrowsButton_->setEnabled(false);
        return;
    }

    const auto& hole = state_.round.holes[currentHoleIndex_];
    holeLabel_->setText(
        QString("Hole: %1 / %2").arg(static_cast<int>(currentHoleIndex_ + 1)).arg(static_cast<int>(state_.round.holes.size())));
    scoreLabel_->setText(QString("Score: %1").arg(state_.relativeScoreUpToHole(currentHoleIndex_ + 1)));
    parValueLabel_->setText(QString::number(hole.par));
    distanceValueLabel_->setText(QString::fromStdString(distanceLabel(hole, state_.units)));
    throwsValueLabel_->setText(QString::number(hole.throwsCount));

    previousButton_->setEnabled(currentHoleIndex_ > 0);
    nextButton_->setEnabled(currentHoleIndex_ + 1 < state_.round.holes.size());
    minusThrowsButton_->setEnabled(hole.throwsCount > 0);
}

void CourseWindow::updateThrowCount(const int delta) {
    if (state_.round.holes.empty()) {
        return;
    }

    auto& hole = state_.round.holes[currentHoleIndex_];
    hole.throwsCount = std::max(0, hole.throwsCount + delta);
    updateUiFromState();
}

void CourseWindow::goToPreviousHole() {
    if (currentHoleIndex_ > 0) {
        --currentHoleIndex_;
        updateUiFromState();
    }
}

void CourseWindow::goToNextHole() {
    if (currentHoleIndex_ + 1 < state_.round.holes.size()) {
        ++currentHoleIndex_;
        updateUiFromState();
    }
}

void CourseWindow::openSettings() {
    SettingsDialog dialog(state_, this);
    if (dialog.exec() == QDialog::Accepted) {
        ThemeManager::applyTheme(*qApp, state_.darkThemeEnabled);
        updateUiFromState();
    }
}

void CourseWindow::openRoundSummary() {
    RoundSummaryDialog dialog(state_, this);
    dialog.exec();
    if (dialog.shouldJumpToFirstHole()) {
        currentHoleIndex_ = 0;
    }
    updateUiFromState();
}

void CourseWindow::openHistory() {
    GameHistoryWindow dialog(this);
    dialog.exec();
}

} // namespace dg



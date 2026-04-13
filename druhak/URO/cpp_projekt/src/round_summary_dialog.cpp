#include "round_summary_dialog.hpp"

#include "models.hpp"
#include "round_repository.hpp"

#include <QDateTime>
#include <QFrame>
#include <QHeaderView>
#include <QHBoxLayout>
#include <QMessageBox>
#include <QPushButton>
#include <QUuid>
#include <QVBoxLayout>

namespace dg {

RoundSummaryDialog::RoundSummaryDialog(AppState& state, QWidget* parent)
    : QDialog(parent)
    , state_(state) {
    setWindowTitle("Round Summary");
    resize(560, 640);

    auto* root = new QVBoxLayout(this);
    root->setContentsMargins(12, 12, 12, 12);
    root->setSpacing(12);

    auto* statsCard = new QFrame(this);
    statsCard->setObjectName("card");
    auto* statsCardLayout = new QVBoxLayout(statsCard);
    auto* statsRow = new QHBoxLayout();
    parLabel_ = new QLabel(statsCard);
    totalThrowsLabel_ = new QLabel(statsCard);
    averageLabel_ = new QLabel(statsCard);

    statsRow->addWidget(parLabel_);
    statsRow->addWidget(totalThrowsLabel_);
    statsRow->addWidget(averageLabel_);
    statsCardLayout->addLayout(statsRow);
    root->addWidget(statsCard);

    auto* tableCard = new QFrame(this);
    tableCard->setObjectName("cardAlt");
    auto* tableCardLayout = new QVBoxLayout(tableCard);
    holeTable_ = new QTableWidget(tableCard);
    holeTable_->setColumnCount(4);
    holeTable_->setHorizontalHeaderLabels({"Hole", "Par", "Throws", "Relative"});
    holeTable_->horizontalHeader()->setSectionResizeMode(QHeaderView::Stretch);
    holeTable_->setEditTriggers(QAbstractItemView::NoEditTriggers);
    tableCardLayout->addWidget(holeTable_);
    root->addWidget(tableCard, 1);

    auto* actionsCard = new QFrame(this);
    actionsCard->setObjectName("card");
    auto* actionsLayout = new QVBoxLayout(actionsCard);
    auto* saveButton = new QPushButton("Save Round", actionsCard);
    auto* deleteButton = new QPushButton("Delete Round", actionsCard);
    auto* closeButton = new QPushButton("Back", actionsCard);
    saveButton->setObjectName("primaryButton");
    deleteButton->setObjectName("dangerButton");
    closeButton->setObjectName("secondaryButton");

    actionsLayout->addWidget(saveButton);
    actionsLayout->addWidget(deleteButton);
    actionsLayout->addWidget(closeButton);
    root->addWidget(actionsCard);

    connect(saveButton, &QPushButton::clicked, this, [this] {
        SavedRound savedRound;
        savedRound.id = QUuid::createUuid().toString(QUuid::WithoutBraces).toStdString();
        savedRound.playedAtIsoUtc = QDateTime::currentDateTimeUtc().toString(Qt::ISODate).toStdString();
        savedRound.round = state_.round;

        QString errorMessage;
        if (!RoundRepository::appendRound(savedRound, &errorMessage)) {
            QMessageBox::warning(this, "Save Round", errorMessage);
            return;
        }

        state_.clearCurrentRoundStats();
        refreshStats();
        populateTable();
        wasRoundSaved_ = true;
        shouldJumpToFirstHole_ = true;

        QMessageBox::information(this, "Save Round", "Round saved to history and current game reset.");
        accept();
    });
    connect(deleteButton, &QPushButton::clicked, this, [this] {
        const auto answer = QMessageBox::question(this, "Delete Round", "Clear throws from the current round?");
        if (answer != QMessageBox::Yes) {
            return;
        }

        state_.clearCurrentRoundStats();
        refreshStats();
        populateTable();
        shouldJumpToFirstHole_ = true;
        accept();
    });
    connect(closeButton, &QPushButton::clicked, this, &QDialog::accept);

    refreshStats();
    populateTable();
}

void RoundSummaryDialog::refreshStats() {
    const auto holesCount = static_cast<int>(state_.round.holes.size());
    const auto average = holesCount == 0 ? 0.0 : static_cast<double>(state_.totalThrows()) / holesCount;

    parLabel_->setText(QString("Par: %1").arg(state_.round.totalPar));
    totalThrowsLabel_->setText(QString("Total Throws: %1").arg(state_.totalThrows()));
    averageLabel_->setText(QString("Average: %1").arg(average, 0, 'f', 2));
}

void RoundSummaryDialog::populateTable() {
    holeTable_->setRowCount(static_cast<int>(state_.round.holes.size()));

    int row = 0;
    for (const auto& hole : state_.round.holes) {
        const auto relative = hole.throwsCount == 0 ? 0 : hole.throwsCount - hole.par;
        holeTable_->setItem(row, 0, new QTableWidgetItem(QString::number(hole.id)));
        holeTable_->setItem(row, 1, new QTableWidgetItem(QString::number(hole.par)));
        holeTable_->setItem(row, 2, new QTableWidgetItem(QString::number(hole.throwsCount)));
        holeTable_->setItem(row, 3, new QTableWidgetItem(QString::number(relative)));
        ++row;
    }
}

} // namespace dg


#include "game_history_window.hpp"

#include "models.hpp"
#include "round_repository.hpp"

#include <QDateTime>
#include <QFrame>
#include <QHeaderView>
#include <QHBoxLayout>
#include <QLabel>
#include <QMessageBox>
#include <QPushButton>
#include <QScrollArea>
#include <QSizePolicy>
#include <QTableWidget>
#include <QTableWidgetItem>
#include <QVBoxLayout>
#include <QWidget>

#include <algorithm>

namespace dg {
namespace {

QString scoreLabel(const int relativeToPar) {
    if (relativeToPar > 0) {
        return QString("+%1").arg(relativeToPar);
    }
    return QString::number(relativeToPar);
}

QString friendlyDate(const std::string& isoUtc) {
    const auto parsed = QDateTime::fromString(QString::fromStdString(isoUtc), Qt::ISODate);
    if (parsed.isValid()) {
        return parsed.toLocalTime().toString("yyyy-MM-dd HH:mm");
    }
    return QString::fromStdString(isoUtc);
}

void populateHoleTable(QTableWidget* table, const Round& round) {
    table->setRowCount(static_cast<int>(round.holes.size()));

    int row = 0;
    for (const auto& hole : round.holes) {
        const auto relative = hole.throwsCount - hole.par;
        table->setItem(row, 0, new QTableWidgetItem(QString::number(hole.id)));
        table->setItem(row, 1, new QTableWidgetItem(QString::number(hole.par)));
        table->setItem(row, 2, new QTableWidgetItem(QString::number(hole.throwsCount)));
        table->setItem(row, 3, new QTableWidgetItem(scoreLabel(relative)));
        ++row;
    }
}

bool showRoundDetails(QWidget* parent, const SavedRound& round) {
    QDialog dialog(parent);
    dialog.setWindowTitle("Round Details");
    dialog.resize(560, 640);

    auto* root = new QVBoxLayout(&dialog);
    root->setContentsMargins(12, 12, 12, 12);
    root->setSpacing(12);

    auto* statsCard = new QFrame(&dialog);
    statsCard->setObjectName("card");
    auto* statsLayout = new QVBoxLayout(statsCard);

    auto* headline = new QLabel(QString::fromStdString(round.round.name), statsCard);
    auto* meta = new QLabel(
        QString("%1  |  Throws: %2  |  Score: %3")
            .arg(friendlyDate(round.playedAtIsoUtc))
            .arg(round.totalThrows())
            .arg(scoreLabel(round.relativeToPar())),
        statsCard);
    meta->setObjectName("sectionMuted");

    auto headlineFont = headline->font();
    headlineFont.setBold(true);
    headlineFont.setPointSize(14);
    headline->setFont(headlineFont);

    statsLayout->addWidget(headline);
    statsLayout->addWidget(meta);
    root->addWidget(statsCard);

    auto* tableCard = new QFrame(&dialog);
    tableCard->setObjectName("cardAlt");
    auto* tableLayout = new QVBoxLayout(tableCard);
    auto* table = new QTableWidget(tableCard);
    table->setColumnCount(4);
    table->setHorizontalHeaderLabels({"Hole", "Par", "Throws", "Relative"});
    table->horizontalHeader()->setSectionResizeMode(QHeaderView::Stretch);
    table->setEditTriggers(QAbstractItemView::NoEditTriggers);
    populateHoleTable(table, round.round);
    tableLayout->addWidget(table);
    root->addWidget(tableCard, 1);

    auto* actionsCard = new QFrame(&dialog);
    actionsCard->setObjectName("card");
    auto* actionsLayout = new QHBoxLayout(actionsCard);
    auto* deleteButton = new QPushButton("Delete Round", actionsCard);
    auto* closeButton = new QPushButton("Close", actionsCard);
    deleteButton->setObjectName("dangerButton");
    closeButton->setObjectName("secondaryButton");
    actionsLayout->addWidget(deleteButton);
    actionsLayout->addStretch();
    actionsLayout->addWidget(closeButton);
    root->addWidget(actionsCard);

    QObject::connect(closeButton, &QPushButton::clicked, &dialog, &QDialog::reject);
    QObject::connect(deleteButton, &QPushButton::clicked, &dialog, [&dialog, &round] {
        QString errorMessage;
        if (!RoundRepository::deleteRound(round.id, &errorMessage)) {
            QMessageBox::warning(&dialog, "Delete failed", errorMessage);
            return;
        }
        dialog.accept();
    });

    return dialog.exec() == QDialog::Accepted;
}

} // namespace

GameHistoryWindow::GameHistoryWindow(QWidget* parent)
    : QDialog(parent) {
    setWindowTitle("Game History");
    resize(620, 720);

    auto* root = new QVBoxLayout(this);
    root->setContentsMargins(12, 12, 12, 12);
    root->setSpacing(12);

    auto* statsCard = new QFrame(this);
    statsCard->setObjectName("card");
    auto* statsLayout = new QHBoxLayout(statsCard);
    roundsPlayedLabel_ = new QLabel(statsCard);
    totalThrowsLabel_ = new QLabel(statsCard);
    averageRelativeLabel_ = new QLabel(statsCard);
    statsLayout->addWidget(roundsPlayedLabel_);
    statsLayout->addWidget(totalThrowsLabel_);
    statsLayout->addWidget(averageRelativeLabel_);
    root->addWidget(statsCard);

    auto* listCard = new QFrame(this);
    listCard->setObjectName("cardAlt");
    auto* listLayout = new QVBoxLayout(listCard);
    auto* scrollArea = new QScrollArea(listCard);
    scrollArea->setWidgetResizable(true);

    auto* listContent = new QWidget(scrollArea);
    roundsLayout_ = new QVBoxLayout(listContent);
    roundsLayout_->setContentsMargins(8, 8, 8, 8);
    roundsLayout_->setSpacing(8);
    roundsLayout_->addStretch();
    scrollArea->setWidget(listContent);

    listLayout->addWidget(scrollArea);
    root->addWidget(listCard, 1);

    auto* closeButton = new QPushButton("Back", this);
    closeButton->setObjectName("secondaryButton");
    connect(closeButton, &QPushButton::clicked, this, &QDialog::accept);
    root->addWidget(closeButton);

    reloadContent();
}

void GameHistoryWindow::reloadContent() {
    while (roundsLayout_->count() > 1) {
        auto* item = roundsLayout_->takeAt(0);
        delete item->widget();
        delete item;
    }

    auto rounds = RoundRepository::loadRounds();
    std::sort(rounds.begin(), rounds.end(), [](const SavedRound& left, const SavedRound& right) {
        return left.playedAtIsoUtc > right.playedAtIsoUtc;
    });

    const auto stats = calculateHistoryStatistics(rounds);
    roundsPlayedLabel_->setText(QString("Rounds: %1").arg(stats.roundsPlayed));
    totalThrowsLabel_->setText(QString("Throws: %1").arg(stats.totalThrows));
    averageRelativeLabel_->setText(QString("Avg +/- Par: %1").arg(stats.averageRelativeToPar, 0, 'f', 2));

    if (rounds.empty()) {
        auto* emptyLabel = new QLabel("No saved rounds yet.", this);
        emptyLabel->setObjectName("sectionMuted");
        roundsLayout_->insertWidget(0, emptyLabel);
        return;
    }

    for (const auto& round : rounds) {
        auto* cardButton = new QPushButton(this);
        cardButton->setObjectName("historyCardButton");
        cardButton->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Preferred);
        cardButton->setText(
            QString("%1\n%2 | Score: %3 | Throws: %4")
                .arg(QString::fromStdString(round.round.name))
                .arg(friendlyDate(round.playedAtIsoUtc))
                .arg(scoreLabel(round.relativeToPar()))
                .arg(round.totalThrows()));
        cardButton->setCursor(Qt::PointingHandCursor);
        cardButton->setMinimumHeight(62);

        connect(cardButton, &QPushButton::clicked, this, [this, round] {
            if (showRoundDetails(this, round)) {
                reloadContent();
            }
        });

        roundsLayout_->insertWidget(roundsLayout_->count() - 1, cardButton);
    }
}

} // namespace dg


#pragma once

#include <QDialog>

class QLabel;
class QVBoxLayout;

namespace dg {

class GameHistoryWindow final : public QDialog {
public:
    explicit GameHistoryWindow(QWidget* parent = nullptr);

private:
    QLabel* roundsPlayedLabel_ {nullptr};
    QLabel* totalThrowsLabel_ {nullptr};
    QLabel* averageRelativeLabel_ {nullptr};
    QVBoxLayout* roundsLayout_ {nullptr};

    void reloadContent();
};

} // namespace dg


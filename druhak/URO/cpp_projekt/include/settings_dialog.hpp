#pragma once

#include "models.hpp"

#include <QCheckBox>
#include <QDialog>
#include <QLineEdit>
#include <QRadioButton>

namespace dg {

class SettingsDialog final : public QDialog {
public:
    explicit SettingsDialog(AppState& state, QWidget* parent = nullptr);

private:
    AppState& state_;

    QLineEdit* nicknameEdit_ {nullptr};
    QCheckBox* darkModeCheck_ {nullptr};
    QRadioButton* metricButton_ {nullptr};
    QRadioButton* imperialButton_ {nullptr};

    void applyAndAccept();
};

} // namespace dg


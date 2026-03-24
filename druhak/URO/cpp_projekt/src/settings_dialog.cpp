#include "settings_dialog.hpp"

#include <QDialogButtonBox>
#include <QFrame>
#include <QFormLayout>
#include <QHBoxLayout>
#include <QLabel>
#include <QMessageBox>
#include <QPushButton>
#include <QVBoxLayout>

namespace dg {

SettingsDialog::SettingsDialog(AppState& state, QWidget* parent)
    : QDialog(parent)
    , state_(state) {
    setWindowTitle("Settings");
    setModal(true);
    resize(420, 260);

    auto* root = new QVBoxLayout(this);

    auto* settingsCard = new QFrame(this);
    settingsCard->setObjectName("card");
    auto* settingsCardLayout = new QVBoxLayout(settingsCard);

    auto* form = new QFormLayout();
    nicknameEdit_ = new QLineEdit(QString::fromStdString(state_.nickname), this);
    nicknameEdit_->setMaxLength(10);
    form->addRow("Nickname", nicknameEdit_);

    darkModeCheck_ = new QCheckBox("Dark mode", this);
    darkModeCheck_->setChecked(state_.darkThemeEnabled);
    form->addRow("Theme", darkModeCheck_);

    auto* unitsRow = new QHBoxLayout();
    metricButton_ = new QRadioButton("Meters", this);
    imperialButton_ = new QRadioButton("Feet", this);
    metricButton_->setChecked(state_.units == Units::Metric);
    imperialButton_->setChecked(state_.units == Units::Imperial);
    unitsRow->addWidget(metricButton_);
    unitsRow->addWidget(imperialButton_);

    auto* unitsWrap = new QWidget(this);
    unitsWrap->setLayout(unitsRow);
    form->addRow("Distance", unitsWrap);

    settingsCardLayout->addLayout(form);
    root->addWidget(settingsCard);
    root->addStretch();

    auto* buttons = new QDialogButtonBox(QDialogButtonBox::Save | QDialogButtonBox::Cancel, this);
    if (auto* saveButton = buttons->button(QDialogButtonBox::Save)) {
        saveButton->setObjectName("primaryButton");
    }
    if (auto* cancelButton = buttons->button(QDialogButtonBox::Cancel)) {
        cancelButton->setObjectName("secondaryButton");
    }
    root->addWidget(buttons);

    connect(buttons, &QDialogButtonBox::accepted, this, [this] { applyAndAccept(); });
    connect(buttons, &QDialogButtonBox::rejected, this, &QDialog::reject);
}

void SettingsDialog::applyAndAccept() {
    const auto trimmedNickname = nicknameEdit_->text().trimmed();
    if (trimmedNickname.isEmpty()) {
        QMessageBox::warning(this, "Invalid nickname", "Nickname cannot be empty.");
        return;
    }

    state_.nickname = trimmedNickname.toStdString();
    state_.darkThemeEnabled = darkModeCheck_->isChecked();
    state_.units = metricButton_->isChecked() ? Units::Metric : Units::Imperial;
    accept();
}

} // namespace dg


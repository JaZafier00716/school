#include "theme_manager.hpp"

#include <QColor>
#include <QStyleFactory>

namespace dg {

void ThemeManager::applyTheme(QApplication& app, const bool darkMode) {
    app.setStyle(QStyleFactory::create("Fusion"));
    app.setPalette(darkMode ? darkPalette() : lightPalette());

    const auto cardColor = darkMode ? QStringLiteral("#262626") : QStringLiteral("#FFFFFF");
    const auto cardAltColor = darkMode ? QStringLiteral("#1F1F1F") : QStringLiteral("#F0F0F0");
    const auto borderColor = darkMode ? QStringLiteral("#4D4D4D") : QStringLiteral("#B3B3B3");
    const auto buttonBg = darkMode ? QStringLiteral("#303030") : QStringLiteral("#FFFFFF");
    const auto buttonText = darkMode ? QStringLiteral("#F5F5F5") : QStringLiteral("#262626");
    const auto buttonHover = darkMode ? QStringLiteral("#3A3A3A") : QStringLiteral("#ECECEC");
    const auto buttonPrimary = darkMode ? QStringLiteral("#6FCF8C") : QStringLiteral("#3A7F4A");
    const auto buttonPrimaryHover = darkMode ? QStringLiteral("#5CB06B") : QStringLiteral("#2E6B3A");
    const auto buttonSecondary = darkMode ? QStringLiteral("#CF6FB3") : QStringLiteral("#7F3A6B");
    const auto buttonSecondaryHover = darkMode ? QStringLiteral("#B35C9B") : QStringLiteral("#6B2E5A");
    const auto buttonDanger = darkMode ? QStringLiteral("#B0897A") : QStringLiteral("#8C5C4A");
    const auto buttonDangerHover = darkMode ? QStringLiteral("#9D7567") : QStringLiteral("#7A4E3F");
    const auto buttonOnAccent = QStringLiteral("#FFFFFF");
    const auto buttonDisabledBg = darkMode ? QStringLiteral("#4D4D4D") : QStringLiteral("#D9D9D9");
    const auto buttonDisabledText = darkMode ? QStringLiteral("#A8A8A8") : QStringLiteral("#8A8A8A");

    const auto stylesheet = QStringLiteral(R"(
QPushButton {
    background-color: %1;
    color: %2;
    border: 1px solid %3;
    border-radius: 8px;
    padding: 8px;
    font-size: 14px;
}
QPushButton:hover {
    background-color: %4;
}
QPushButton:disabled {
    background-color: %5;
    color: %6;
    border-color: %5;
}
QPushButton#primaryButton {
    background-color: %7;
    color: %8;
    border: 1px solid %7;
}
QPushButton#primaryButton:hover {
    background-color: %9;
}
QPushButton#secondaryButton {
    background-color: %10;
    color: %8;
    border: 1px solid %10;
}
QPushButton#secondaryButton:hover {
    background-color: %11;
}
QPushButton#dangerButton {
    background-color: %12;
    color: %8;
    border: 1px solid %12;
}
QPushButton#dangerButton:hover {
    background-color: %13;
}
QPushButton#historyCardButton {
    background-color: %14;
    color: palette(window-text);
    border: 1px solid %3;
    border-radius: 10px;
    text-align: left;
    padding: 10px;
}
QPushButton#historyCardButton:hover {
    background-color: %15;
}
QLineEdit {
    border-radius: 6px;
    border: 1px solid palette(mid);
    padding: 6px;
}
QTableWidget {
    border: 1px solid palette(mid);
    border-radius: 8px;
}
QFrame#card {
    background-color: %14;
    border: 1px solid %3;
    border-radius: 10px;
}
QFrame#cardAlt {
    background-color: %15;
    border: 1px solid %3;
    border-radius: 10px;
}
QLabel#sectionMuted {
    color: palette(mid);
}
)");
    app.setStyleSheet(stylesheet.arg(
        buttonBg,
        buttonText,
        borderColor,
        buttonHover,
        buttonDisabledBg,
        buttonDisabledText,
        buttonPrimary,
        buttonOnAccent,
        buttonPrimaryHover,
        buttonSecondary,
        buttonSecondaryHover,
        buttonDanger,
        buttonDangerHover,
        cardColor,
        cardAltColor));
}

QPalette ThemeManager::darkPalette() {
    QPalette palette;
    palette.setColor(QPalette::Window, QColor("#121212"));
    palette.setColor(QPalette::WindowText, QColor("#F5F5F5"));
    palette.setColor(QPalette::Base, QColor("#1A1A1A"));
    palette.setColor(QPalette::AlternateBase, QColor("#262626"));
    palette.setColor(QPalette::ToolTipBase, QColor("#F5F5F5"));
    palette.setColor(QPalette::ToolTipText, QColor("#F5F5F5"));
    palette.setColor(QPalette::Text, QColor("#F5F5F5"));
    palette.setColor(QPalette::Button, QColor("#262626"));
    palette.setColor(QPalette::ButtonText, QColor("#F5F5F5"));
    palette.setColor(QPalette::BrightText, QColor("#FFFFFF"));
    palette.setColor(QPalette::Link, QColor("#6FCF8C"));
    palette.setColor(QPalette::Highlight, QColor("#6FCF8C"));
    palette.setColor(QPalette::HighlightedText, QColor("#121212"));
    return palette;
}

QPalette ThemeManager::lightPalette() {
    QPalette palette;
    palette.setColor(QPalette::Window, QColor("#F5F5F5"));
    palette.setColor(QPalette::WindowText, QColor("#262626"));
    palette.setColor(QPalette::Base, QColor("#FFFFFF"));
    palette.setColor(QPalette::AlternateBase, QColor("#E5E5E5"));
    palette.setColor(QPalette::ToolTipBase, QColor("#262626"));
    palette.setColor(QPalette::ToolTipText, QColor("#262626"));
    palette.setColor(QPalette::Text, QColor("#262626"));
    palette.setColor(QPalette::Button, QColor("#FFFFFF"));
    palette.setColor(QPalette::ButtonText, QColor("#262626"));
    palette.setColor(QPalette::BrightText, QColor("#121212"));
    palette.setColor(QPalette::Link, QColor("#3A7F4A"));
    palette.setColor(QPalette::Highlight, QColor("#3A7F4A"));
    palette.setColor(QPalette::HighlightedText, QColor("#FFFFFF"));
    return palette;
}

} // namespace dg


#pragma once

#include <QApplication>
#include <QPalette>

namespace dg {

class ThemeManager {
public:
    static void applyTheme(QApplication& app, bool darkMode);

private:
    static QPalette darkPalette();
    static QPalette lightPalette();
};

} // namespace dg


#include "course_window.hpp"
#include "models.hpp"
#include "theme_manager.hpp"

#include <QApplication>

int main(int argc, char* argv[]) {
    QApplication app(argc, argv);

    auto state = dg::AppState::createDemo();
    dg::ThemeManager::applyTheme(app, state.darkThemeEnabled);

    dg::CourseWindow window(state);
    window.show();

    return app.exec();
}


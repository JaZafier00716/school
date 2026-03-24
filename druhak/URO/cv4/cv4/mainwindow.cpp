#include "mainwindow.h"

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
{
    this->resize(1200, 800);
    QFont app_font = QFont("Roboto", 15);
    this->setFont(app_font);
    create_layout();
    create_navbar();
    create_action_bar();
    create_form();
}

MainWindow::~MainWindow() = default;


void MainWindow::create_layout() {
    QWidget* central_widget = new QWidget();
    this->top_level_layout = new QVBoxLayout();

    central_widget->setLayout(this->top_level_layout);
    this->setCentralWidget(central_widget);
}

void MainWindow::create_navbar(){
    QHBoxLayout* navbar_layout = new QHBoxLayout();
    navbar_layout->setAlignment(Qt::AlignTop);

    QLabel* app_name = new QLabel("Bike Manager");
    navbar_layout->addWidget(app_name);

    QPushButton* home_btn = new QPushButton("Home");
    navbar_layout->addWidget(home_btn);
    QPushButton* store_btn = new QPushButton("Store");
    navbar_layout->addWidget(store_btn);
    QPushButton* info_btn = new QPushButton("Info");
    navbar_layout->addWidget(info_btn);

    navbar_layout->addStretch(1);

    QLineEdit* search = new QLineEdit();
    search->setPlaceholderText("Search");
    navbar_layout->addWidget(search);

    this->top_level_layout->addLayout(navbar_layout);
}

void MainWindow::create_action_bar(){
    QHBoxLayout* create_action_bar_layout = new QHBoxLayout();
    create_action_bar_layout->setAlignment(Qt::AlignTop);

    QPushButton* save_btn = new QPushButton("Save");
    create_action_bar_layout->addWidget(save_btn);
    QPushButton* load_btn = new QPushButton("Store");
    create_action_bar_layout->addWidget(load_btn);
    QPushButton* delete_btn = new QPushButton("Delete");
    create_action_bar_layout->addWidget(delete_btn);

    create_action_bar_layout->addStretch(1);

    this->top_level_layout->addLayout(create_action_bar_layout);
}

void MainWindow::create_form() {
    QGridLayout* form_layout = new QGridLayout();

    QLabel* form_header = new QLabel("Add New Inventory");
    form_layout->addWidget(form_header, 0, 0);

    QLineEdit* brand_input = new QLineEdit();
    brand_input->setPlaceholderText("Brand");
    form_layout->addWidget(brand_input, 1, 0);

    QLineEdit* model_input = new QLineEdit();
    brand_input->setPlaceholderText("Model");
    form_layout->addWidget(model_input, 1, 1);


    QComboBox* bike_type = new QComboBox();
    bike_type->addItems({"MTB", "Road", "Gravel"});
    form_layout->addWidget(bike_type, 1, 2);

    QComboBox* bike_size = new QComboBox();
    bike_type->addItems({"S", "M", "L"});
    form_layout->addWidget(bike_size, 1, 3);

    QTextEdit* desc = new QTextEdit();
    desc->setPlaceholderText("Description");
    form_layout->addWidget(desc, 1, 4, 2, 5);


    QComboBox* wheel_size = new QComboBox();
    bike_type->addItems({"26", "27.5", "29"});
    form_layout->addWidget(wheel_size, 2, 0);

    QLineEdit* stock_input = new QLineEdit();
    brand_input->setPlaceholderText("Stock");
    form_layout->addWidget(stock_input, 2, 1);

    QLineEdit* price_input = new QLineEdit();
    brand_input->setPlaceholderText("Price");
    form_layout->addWidget(price_input, 2, 2, 1, 2);

    QPushButton* clear_btn = new QPushButton("Clear form");
    form_layout->addWidget(clear_btn, 3, 6);

    QPushButton* add_btn = new QPushButton("Add to inventory");
    form_layout->addWidget(add_btn, 3, 7, 1, 2);

    this->top_level_layout->addLayout(form_layout);
}

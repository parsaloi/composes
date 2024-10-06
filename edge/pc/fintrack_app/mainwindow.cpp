#include "mainwindow.h"

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent), accountingEngine(dbManager)
{
    setupUI();
    setupConnections();

    viewModel = new ViewModel(accountingEngine, this);
    connect(viewModel, &ViewModel::balanceChanged, this, &MainWindow::onBalanceChanged);
    connect(viewModel, &ViewModel::profitChanged, this, &MainWindow::onProfitChanged);
    connect(viewModel, &ViewModel::lossChanged, this, &MainWindow::onLossChanged);
    connect(viewModel, &ViewModel::errorOccurred, this, &MainWindow::onErrorOccurred);

    if (!dbManager.initializeDatabase()) {
        QMessageBox::critical(this, "Error", "Failed to initialize database");
    }

    viewModel->updateBalance();
    viewModel->updateProfitLoss();
}

MainWindow::~MainWindow() {}

void MainWindow::setupUI()
{
    centralWidget = new QWidget(this);
    setCentralWidget(centralWidget);

    mainLayout = new QVBoxLayout(centralWidget);

    balanceLabel = new QLabel("Current Balance: 0.00 ADA", this);
    mainLayout->addWidget(balanceLabel);

    profitLossLabel = new QLabel("Profit/Loss: 0.00 ADA", this);
    mainLayout->addWidget(profitLossLabel);

    amountInput = new QLineEdit(this);
    amountInput->setPlaceholderText("Amount");
    mainLayout->addWidget(amountInput);

    categoryCombo = new QComboBox(this);
    categoryCombo->addItem("Category 1");
    categoryCombo->addItem("Category 2");
    mainLayout->addWidget(categoryCombo);

    descriptionInput = new QLineEdit(this);
    descriptionInput->setPlaceholderText("Description");
    mainLayout->addWidget(descriptionInput);

    addIncomeButton = new QPushButton("Add Income", this);
    mainLayout->addWidget(addIncomeButton);

    addExpenseButton = new QPushButton("Add Expense", this);
    mainLayout->addWidget(addExpenseButton);

    setWindowTitle("FinTrack");
    resize(400, 300);
}

void MainWindow::setupConnections()
{
    connect(addIncomeButton, &QPushButton::clicked, this, &MainWindow::onAddIncomeClicked);
    connect(addExpenseButton, &QPushButton::clicked, this, &MainWindow::onAddExpenseClicked);
}

void MainWindow::onAddIncomeClicked()
{
    bool ok;
    double amount = amountInput->text().toDouble(&ok);
    if (!ok) {
        QMessageBox::warning(this, "Invalid Input", "Please enter a valid amount");
        return;
    }

    int categoryId = categoryCombo->currentIndex() + 1; // Assuming category IDs start from 1
    QString description = descriptionInput->text();

    viewModel->addIncome(amount, categoryId, description);
    amountInput->clear();
    descriptionInput->clear();
}

void MainWindow::onAddExpenseClicked()
{
    bool ok;
    double amount = amountInput->text().toDouble(&ok);
    if (!ok) {
        QMessageBox::warning(this, "Invalid Input", "Please enter a valid amount");
        return;
    }

    int categoryId = categoryCombo->currentIndex() + 1; // Assuming category IDs start from 1
    QString description = descriptionInput->text();

    viewModel->addExpense(amount, categoryId, description);
    amountInput->clear();
    descriptionInput->clear();
}

void MainWindow::onBalanceChanged(double newBalance)
{
    balanceLabel->setText(QString("Current Balance: %1 ADA").arg(newBalance, 0, 'f', 2));
}

void MainWindow::onProfitChanged(double newProfit)
{
    profitLossLabel->setText(QString("Profit: %1 ADA").arg(newProfit, 0, 'f', 2));
}

void MainWindow::onLossChanged(double newLoss)
{
    profitLossLabel->setText(QString("Loss: %1 ADA").arg(newLoss, 0, 'f', 2));
}

void MainWindow::onErrorOccurred(const QString& error)
{
    QMessageBox::warning(this, "Error", error);
}

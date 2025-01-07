#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QVBoxLayout>
#include <QLabel>
#include <QPushButton>
#include <QLineEdit>
#include <QComboBox>
#include <QMessageBox>
#include "viewmodel.h"
#include "database_manager.h"
#include "simple_accounting_engine.h"

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    MainWindow(QWidget *parent = nullptr);
    ~MainWindow();

private slots:
    void onAddIncomeClicked();
    void onAddExpenseClicked();
    void onBalanceChanged(double newBalance);
    void onProfitChanged(double newProfit);
    void onLossChanged(double newLoss);
    void onErrorOccurred(const QString& error);

private:
    QWidget *centralWidget;
    QVBoxLayout *mainLayout;
    QLabel *balanceLabel;
    QLabel *profitLossLabel;
    QPushButton *addIncomeButton;
    QPushButton *addExpenseButton;

    QLineEdit *amountInput;
    QComboBox *categoryCombo;
    QLineEdit *descriptionInput;

    DatabaseManager dbManager;
    SimpleAccountingEngine accountingEngine;
    ViewModel *viewModel;

    void setupUI();
    void setupConnections();
};
#endif // MAINWINDOW_H

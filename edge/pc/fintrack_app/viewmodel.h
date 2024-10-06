#ifndef VIEWMODEL_H
#define VIEWMODEL_H

#include <QObject>
#include "simple_accounting_engine.h"

class ViewModel : public QObject {
    Q_OBJECT

public:
    ViewModel(SimpleAccountingEngine& engine, QObject* parent = nullptr);

    Q_INVOKABLE void updateBalance();
    Q_INVOKABLE void addIncome(double amount, int categoryId, const QString& description);
    Q_INVOKABLE void addExpense(double amount, int categoryId, const QString& description);
    Q_INVOKABLE void updateProfitLoss();

signals:
    void balanceChanged(double newBalance);
    void profitChanged(double newProfit);
    void lossChanged(double newLoss);
    void errorOccurred(const QString& error);

private:
    SimpleAccountingEngine& engine;
    std::chrono::system_clock::time_point getCurrentPeriodStart();
    std::chrono::system_clock::time_point getCurrentPeriodEnd();
};

#endif // VIEWMODEL_H

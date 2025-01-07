#include "viewmodel.h"
#include <chrono>

ViewModel::ViewModel(SimpleAccountingEngine& engine, QObject* parent)
    : QObject(parent), engine(engine) {}

void ViewModel::updateBalance() {
    auto balance = engine.calculateBalance();
    if (balance) {
        emit balanceChanged(*balance);
    } else {
        emit errorOccurred("Failed to calculate balance");
    }
}

void ViewModel::addIncome(double amount, int categoryId, const QString& description) {
    Income income;
    income.amount = amount;
    income.date = std::chrono::system_clock::now();
    income.categoryId = categoryId;
    income.description = description.toStdString();

    auto result = engine.addIncome(income);
    if (result) {
        updateBalance();
        updateProfitLoss();
    } else {
        emit errorOccurred("Failed to add income");
    }
}

void ViewModel::addExpense(double amount, int categoryId, const QString& description) {
    Expense expense;
    expense.amount = amount;
    expense.date = std::chrono::system_clock::now();
    expense.categoryId = categoryId;
    expense.description = description.toStdString();

    auto result = engine.addExpense(expense);
    if (result) {
        updateBalance();
        updateProfitLoss();
    } else {
        emit errorOccurred("Failed to add expense");
    }
}

void ViewModel::updateProfitLoss() {
    auto start = getCurrentPeriodStart();
    auto end = getCurrentPeriodEnd();

    auto profit = engine.calculateProfit(start, end);
    if (profit) {
        emit profitChanged(*profit);
    }

    auto loss = engine.calculateLoss(start, end);
    if (loss) {
        emit lossChanged(*loss);
    }
}

std::chrono::system_clock::time_point ViewModel::getCurrentPeriodStart() {
    auto now = std::chrono::system_clock::now();
    auto trackingPeriod = engine.getTrackingPeriod();

    if (!trackingPeriod) {
        // Default to monthly if not set
        trackingPeriod = TrackingPeriod::Monthly;
    }

    using namespace std::chrono;
    auto today = floor<days>(now);
    year_month_day ymd{today};

    switch (*trackingPeriod) {
    case TrackingPeriod::Weekly: {
        auto weekday = year_month_weekday{ymd}.weekday();
        return floor<days>(today - (weekday - Monday));
    }
    case TrackingPeriod::Monthly:
        return sys_days{ymd.year()/ymd.month()/1};
    case TrackingPeriod::Quarterly: {
        int quarter = (static_cast<unsigned>(ymd.month()) - 1) / 3;
        return sys_days{ymd.year()/month{static_cast<unsigned int>(3 * quarter + 1)}/1};
    }
    case TrackingPeriod::Yearly:
        return sys_days{ymd.year()/January/1};
    }

    // Should never reach here, but just in case
    return now;
}

std::chrono::system_clock::time_point ViewModel::getCurrentPeriodEnd() {
    auto start = getCurrentPeriodStart();
    auto trackingPeriod = engine.getTrackingPeriod();

    if (!trackingPeriod) {
        // Default to monthly if not set
        trackingPeriod = TrackingPeriod::Monthly;
    }

    using namespace std::chrono;

    switch (*trackingPeriod) {
    case TrackingPeriod::Weekly:
        return start + weeks{1} - seconds{1};
    case TrackingPeriod::Monthly: {
        year_month_day ymd{floor<days>(start)};
        auto next_month = ymd + months{1};
        return sys_days{next_month} - seconds{1};
    }
    case TrackingPeriod::Quarterly:
        return start + months{3} - seconds{1};
    case TrackingPeriod::Yearly:
        return start + years{1} - seconds{1};
    }

    // Should never reach here, but just in case
    return std::chrono::system_clock::now();
}

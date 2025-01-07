#ifndef SIMPLE_ACCOUNTING_ENGINE_H
#define SIMPLE_ACCOUNTING_ENGINE_H

#include "database_manager.h"
#include <chrono>
#include <optional>

class SimpleAccountingEngine {
public:
    SimpleAccountingEngine(DatabaseManager& dbManager);

    [[nodiscard]] std::optional<double> calculateBalance();
    [[nodiscard]] std::optional<double> calculateProfit(const std::chrono::system_clock::time_point& start, const std::chrono::system_clock::time_point& end);
    [[nodiscard]] std::optional<double> calculateLoss(const std::chrono::system_clock::time_point& start, const std::chrono::system_clock::time_point& end);

    [[nodiscard]] std::optional<int> addIncome(const Income& income);
    [[nodiscard]] std::optional<int> addExpense(const Expense& expense);
    [[nodiscard]] std::optional<TrackingPeriod> getTrackingPeriod();

private:
    DatabaseManager& dbManager;
};

#endif // SIMPLE_ACCOUNTING_ENGINE_H

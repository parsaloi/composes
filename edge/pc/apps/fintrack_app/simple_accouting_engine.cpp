#include "simple_accounting_engine.h"
#include <numeric>

SimpleAccountingEngine::SimpleAccountingEngine(DatabaseManager& dbManager)
    : dbManager(dbManager) {}

std::optional<double> SimpleAccountingEngine::calculateBalance() {
    return dbManager.getTotalBalance();
}

std::optional<double> SimpleAccountingEngine::calculateProfit(const std::chrono::system_clock::time_point& start, const std::chrono::system_clock::time_point& end) {
    auto incomes = dbManager.getIncomes(start, end);
    auto expenses = dbManager.getExpenses(start, end);

    double totalIncome = std::accumulate(incomes.begin(), incomes.end(), 0.0,
                                         [](double sum, const Income& income) { return sum + income.amount; });
    double totalExpense = std::accumulate(expenses.begin(), expenses.end(), 0.0,
                                          [](double sum, const Expense& expense) { return sum + expense.amount; });

    double profit = totalIncome - totalExpense;
    return profit > 0 ? std::optional<double>{profit} : std::nullopt;
}

std::optional<double> SimpleAccountingEngine::calculateLoss(const std::chrono::system_clock::time_point& start, const std::chrono::system_clock::time_point& end) {
    auto incomes = dbManager.getIncomes(start, end);
    auto expenses = dbManager.getExpenses(start, end);

    double totalIncome = std::accumulate(incomes.begin(), incomes.end(), 0.0,
                                         [](double sum, const Income& income) { return sum + income.amount; });
    double totalExpense = std::accumulate(expenses.begin(), expenses.end(), 0.0,
                                          [](double sum, const Expense& expense) { return sum + expense.amount; });

    double loss = totalExpense - totalIncome;
    return loss > 0 ? std::optional<double>{loss} : std::nullopt;
}

std::optional<int> SimpleAccountingEngine::addIncome(const Income& income) {
    return dbManager.addIncome(income);
}

std::optional<int> SimpleAccountingEngine::addExpense(const Expense& expense) {
    return dbManager.addExpense(expense);
}

std::optional<TrackingPeriod> SimpleAccountingEngine::getTrackingPeriod() {
    return dbManager.getTrackingPeriod();
}

#ifndef DATABASE_MANAGER_H
#define DATABASE_MANAGER_H

#include <QtSql>
#include <optional>
#include <vector>
#include "data_structures.h"

class DatabaseManager {
public:
    DatabaseManager();
    ~DatabaseManager();

    [[nodiscard]] bool initializeDatabase();
    [[nodiscard]] std::optional<int> addExpense(const Expense& expense);
    [[nodiscard]] std::optional<int> addIncome(const Income& income);
    [[nodiscard]] bool setTrackingPeriod(TrackingPeriod period);
    [[nodiscard]] std::optional<TrackingPeriod> getTrackingPeriod();
    [[nodiscard]] std::optional<double> getTotalBalance();
    [[nodiscard]] std::vector<Expense> getExpenses(const std::chrono::system_clock::time_point& start, const std::chrono::system_clock::time_point& end);
    [[nodiscard]] std::vector<Income> getIncomes(const std::chrono::system_clock::time_point& start, const std::chrono::system_clock::time_point& end);

private:
    QSqlDatabase db;
    [[nodiscard]] bool createTables();
    [[nodiscard]] bool executeQuery(const QString& query);
};

#endif // DATABASE_MANAGER_H

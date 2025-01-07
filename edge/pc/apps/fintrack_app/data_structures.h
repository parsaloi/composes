#ifndef DATA_STRUCTURES_H
#define DATA_STRUCTURES_H

#include <string>
#include <chrono>

struct ExpenseCategory {
    int id;
    std::string name;
};

struct IncomeCategory {
    int id;
    std::string name;
};

struct Expense {
    int id;
    double amount;
    std::chrono::system_clock::time_point date;
    int categoryId;
    std::string description;
};

struct Income {
    int id;
    double amount;
    std::chrono::system_clock::time_point date;
    int categoryId;
    std::string description;
};

enum class TrackingPeriod {
    Weekly,
    Monthly,
    Quarterly,
    Yearly
};

#endif // DATA_STRUCTURES_H

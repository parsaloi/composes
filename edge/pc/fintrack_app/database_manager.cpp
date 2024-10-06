#include "database_manager.h"
#include <QDebug>

DatabaseManager::DatabaseManager() {
    db = QSqlDatabase::addDatabase("QSQLITE");
    db.setDatabaseName("fintrack.db");
}

DatabaseManager::~DatabaseManager() {
    if (db.isOpen()) {
        db.close();
    }
}

bool DatabaseManager::initializeDatabase() {
    if (!db.open()) {
        qDebug() << "Error: connection with database failed";
        return false;
    }
    return createTables();
}

bool DatabaseManager::createTables() {
    return executeQuery("CREATE TABLE IF NOT EXISTS expense_categories (id INTEGER PRIMARY KEY, name TEXT NOT NULL)") &&
           executeQuery("CREATE TABLE IF NOT EXISTS income_categories (id INTEGER PRIMARY KEY, name TEXT NOT NULL)") &&
           executeQuery("CREATE TABLE IF NOT EXISTS expenses (id INTEGER PRIMARY KEY, amount REAL NOT NULL, date INTEGER NOT NULL, category_id INTEGER NOT NULL, description TEXT, FOREIGN KEY(category_id) REFERENCES expense_categories(id))") &&
           executeQuery("CREATE TABLE IF NOT EXISTS incomes (id INTEGER PRIMARY KEY, amount REAL NOT NULL, date INTEGER NOT NULL, category_id INTEGER NOT NULL, description TEXT, FOREIGN KEY(category_id) REFERENCES income_categories(id))") &&
           executeQuery("CREATE TABLE IF NOT EXISTS settings (key TEXT PRIMARY KEY, value TEXT NOT NULL)");
}

bool DatabaseManager::executeQuery(const QString& query) {
    QSqlQuery sqlQuery;
    if (!sqlQuery.exec(query)) {
        qDebug() << "Error executing query:" << sqlQuery.lastError().text();
        return false;
    }
    return true;
}

std::optional<int> DatabaseManager::addExpense(const Expense& expense) {
    QSqlQuery query;
    query.prepare("INSERT INTO expenses (amount, date, category_id, description) VALUES (:amount, :date, :category_id, :description)");
    query.bindValue(":amount", expense.amount);
    query.bindValue(":date", static_cast<qint64>(std::chrono::system_clock::to_time_t(expense.date)));
    query.bindValue(":category_id", expense.categoryId);
    query.bindValue(":description", QString::fromStdString(expense.description));

    if (query.exec()) {
        return query.lastInsertId().toInt();
    } else {
        qDebug() << "Error adding expense:" << query.lastError().text();
        return std::nullopt;
    }
}

std::optional<int> DatabaseManager::addIncome(const Income& income) {
    QSqlQuery query;
    query.prepare("INSERT INTO incomes (amount, date, category_id, description) VALUES (:amount, :date, :category_id, :description)");
    query.bindValue(":amount", income.amount);
    query.bindValue(":date", static_cast<qint64>(std::chrono::system_clock::to_time_t(income.date)));
    query.bindValue(":category_id", income.categoryId);
    query.bindValue(":description", QString::fromStdString(income.description));

    if (query.exec()) {
        return query.lastInsertId().toInt();
    } else {
        qDebug() << "Error adding income:" << query.lastError().text();
        return std::nullopt;
    }
}

bool DatabaseManager::setTrackingPeriod(TrackingPeriod period) {
    QSqlQuery query;
    query.prepare("INSERT OR REPLACE INTO settings (key, value) VALUES ('tracking_period', :value)");
    query.bindValue(":value", static_cast<int>(period));
    return query.exec();
}

std::optional<TrackingPeriod> DatabaseManager::getTrackingPeriod() {
    QSqlQuery query;
    query.prepare("SELECT value FROM settings WHERE key = 'tracking_period'");
    if (query.exec() && query.next()) {
        bool ok;
        int value = query.value(0).toInt(&ok);
        if (ok && value >= 0 && value <= 3) {
            return static_cast<TrackingPeriod>(value);
        }
    }
    return std::nullopt;
}

std::optional<double> DatabaseManager::getTotalBalance() {
    QSqlQuery query;
    if (query.exec("SELECT (SELECT COALESCE(SUM(amount), 0) FROM incomes) - (SELECT COALESCE(SUM(amount), 0) FROM expenses)") && query.next()) {
        return query.value(0).toDouble();
    }
    return std::nullopt;
}

std::vector<Expense> DatabaseManager::getExpenses(const std::chrono::system_clock::time_point& start, const std::chrono::system_clock::time_point& end) {
    std::vector<Expense> expenses;
    QSqlQuery query;
    query.prepare("SELECT id, amount, date, category_id, description FROM expenses WHERE date BETWEEN :start AND :end");
    query.bindValue(":start", static_cast<qint64>(std::chrono::system_clock::to_time_t(start)));
    query.bindValue(":end", static_cast<qint64>(std::chrono::system_clock::to_time_t(end)));

    if (query.exec()) {
        while (query.next()) {
            Expense expense;
            expense.id = query.value(0).toInt();
            expense.amount = query.value(1).toDouble();
            expense.date = std::chrono::system_clock::from_time_t(query.value(2).toLongLong());
            expense.categoryId = query.value(3).toInt();
            expense.description = query.value(4).toString().toStdString();
            expenses.push_back(expense);
        }
    }
    return expenses;
}

std::vector<Income> DatabaseManager::getIncomes(const std::chrono::system_clock::time_point& start, const std::chrono::system_clock::time_point& end) {
    std::vector<Income> incomes;
    QSqlQuery query;
    query.prepare("SELECT id, amount, date, category_id, description FROM incomes WHERE date BETWEEN :start AND :end");
    query.bindValue(":start", static_cast<qint64>(std::chrono::system_clock::to_time_t(start)));
    query.bindValue(":end", static_cast<qint64>(std::chrono::system_clock::to_time_t(end)));

    if (query.exec()) {
        while (query.next()) {
            Income income;
            income.id = query.value(0).toInt();
            income.amount = query.value(1).toDouble();
            income.date = std::chrono::system_clock::from_time_t(query.value(2).toLongLong());
            income.categoryId = query.value(3).toInt();
            income.description = query.value(4).toString().toStdString();
            incomes.push_back(income);
        }
    }
    return incomes;
}

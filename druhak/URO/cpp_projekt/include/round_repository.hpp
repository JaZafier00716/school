#pragma once

#include "models.hpp"

#include <QString>

#include <vector>

namespace dg {

class RoundRepository final {
public:
    [[nodiscard]] static QString defaultDatabasePath();
    [[nodiscard]] static std::vector<SavedRound> loadRounds(const QString& filePath = {});

    static bool appendRound(const SavedRound& round, QString* errorMessage = nullptr, const QString& filePath = {});
    static bool deleteRound(const std::string& id, QString* errorMessage = nullptr, const QString& filePath = {});

private:
    static bool saveRounds(const std::vector<SavedRound>& rounds, QString* errorMessage, const QString& filePath);
};

} // namespace dg


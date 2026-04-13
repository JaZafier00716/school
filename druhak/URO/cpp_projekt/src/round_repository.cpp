#include "round_repository.hpp"

#include <QDir>
#include <QFile>
#include <QJsonArray>
#include <QJsonDocument>
#include <QJsonObject>
#include <QStandardPaths>

#include <algorithm>

namespace dg {
namespace {

QJsonObject toJson(const Distance& distance) {
    QJsonObject object;
    object["metricMeters"] = distance.metricMeters;
    object["imperialFeet"] = distance.imperialFeet;
    return object;
}

QJsonObject toJson(const Hole& hole) {
    QJsonObject object;
    object["id"] = hole.id;
    object["par"] = hole.par;
    object["distance"] = toJson(hole.distance);
    object["throwsCount"] = hole.throwsCount;
    return object;
}

QJsonObject toJson(const Round& round) {
    QJsonObject object;
    object["name"] = QString::fromStdString(round.name);
    object["location"] = QString::fromStdString(round.location);
    object["totalPar"] = round.totalPar;

    QJsonArray holes;
    for (const auto& hole : round.holes) {
        holes.append(toJson(hole));
    }
    object["holes"] = holes;

    return object;
}

QJsonObject toJson(const SavedRound& round) {
    QJsonObject object;
    object["id"] = QString::fromStdString(round.id);
    object["playedAtIsoUtc"] = QString::fromStdString(round.playedAtIsoUtc);
    object["round"] = toJson(round.round);
    return object;
}

Distance parseDistance(const QJsonObject& object) {
    Distance distance;
    distance.metricMeters = object["metricMeters"].toInt();
    distance.imperialFeet = object["imperialFeet"].toInt();
    return distance;
}

Hole parseHole(const QJsonObject& object) {
    Hole hole;
    hole.id = object["id"].toInt();
    hole.par = object["par"].toInt();
    hole.distance = parseDistance(object["distance"].toObject());
    hole.throwsCount = object["throwsCount"].toInt();
    return hole;
}

Round parseRound(const QJsonObject& object) {
    Round round;
    round.name = object["name"].toString().toStdString();
    round.location = object["location"].toString().toStdString();
    round.totalPar = object["totalPar"].toInt();

    const auto holesArray = object["holes"].toArray();
    round.holes.reserve(holesArray.size());
    for (const auto holeValue : holesArray) {
        round.holes.push_back(parseHole(holeValue.toObject()));
    }

    return round;
}

SavedRound parseSavedRound(const QJsonObject& object) {
    SavedRound round;
    round.id = object["id"].toString().toStdString();
    round.playedAtIsoUtc = object["playedAtIsoUtc"].toString().toStdString();
    round.round = parseRound(object["round"].toObject());
    return round;
}

QString resolvePath(const QString& overridePath) {
    return overridePath.isEmpty() ? RoundRepository::defaultDatabasePath() : overridePath;
}

} // namespace

QString RoundRepository::defaultDatabasePath() {
#ifdef DG_PROJECT_ROOT
    return QDir(QStringLiteral(DG_PROJECT_ROOT)).filePath("round_history.json");
#else
    auto basePath = QStandardPaths::writableLocation(QStandardPaths::AppDataLocation);
    if (basePath.isEmpty()) {
        basePath = QDir::homePath() + "/.discgolf_tracker";
    }

    QDir directory(basePath);
    if (!directory.exists()) {
        directory.mkpath(".");
    }

    return directory.filePath("round_history.json");
#endif
}

std::vector<SavedRound> RoundRepository::loadRounds(const QString& filePath) {
    const auto path = resolvePath(filePath);
    QFile inputFile(path);
    if (!inputFile.exists() || !inputFile.open(QIODevice::ReadOnly)) {
        return {};
    }

    const auto document = QJsonDocument::fromJson(inputFile.readAll());
    if (!document.isObject()) {
        return {};
    }

    const auto root = document.object();
    const auto roundsArray = root["rounds"].toArray();

    std::vector<SavedRound> rounds;
    rounds.reserve(roundsArray.size());
    for (const auto roundValue : roundsArray) {
        rounds.push_back(parseSavedRound(roundValue.toObject()));
    }

    return rounds;
}

bool RoundRepository::saveRounds(const std::vector<SavedRound>& rounds, QString* errorMessage, const QString& filePath) {
    const auto path = resolvePath(filePath);
    QFile outputFile(path);
    if (!outputFile.open(QIODevice::WriteOnly | QIODevice::Truncate)) {
        if (errorMessage != nullptr) {
            *errorMessage = QString("Unable to open %1 for writing.").arg(path);
        }
        return false;
    }

    QJsonArray roundsArray;
    for (const auto& round : rounds) {
        roundsArray.append(toJson(round));
    }

    QJsonObject root;
    root["version"] = 1;
    root["rounds"] = roundsArray;

    const auto bytes = QJsonDocument(root).toJson(QJsonDocument::Indented);
    if (outputFile.write(bytes) < 0) {
        if (errorMessage != nullptr) {
            *errorMessage = QString("Unable to write to %1.").arg(path);
        }
        return false;
    }

    return true;
}

bool RoundRepository::appendRound(const SavedRound& round, QString* errorMessage, const QString& filePath) {
    auto rounds = loadRounds(filePath);
    rounds.push_back(round);
    return saveRounds(rounds, errorMessage, filePath);
}

bool RoundRepository::deleteRound(const std::string& id, QString* errorMessage, const QString& filePath) {
    auto rounds = loadRounds(filePath);
    const auto oldSize = rounds.size();

    rounds.erase(std::remove_if(rounds.begin(), rounds.end(), [&id](const SavedRound& round) { return round.id == id; }), rounds.end());

    if (oldSize == rounds.size()) {
        return true;
    }

    return saveRounds(rounds, errorMessage, filePath);
}

} // namespace dg


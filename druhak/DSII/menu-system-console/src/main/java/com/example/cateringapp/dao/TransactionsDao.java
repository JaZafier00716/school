package com.example.cateringapp.dao;

import com.example.cateringapp.db.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Reusable transaction helper following the optional external-Database pattern.
 */
final class TransactionsDao {
    private static final Logger logger = LoggerFactory.getLogger(TransactionsDao.class);

    <T> T executeInTransaction(Database pDb, SqlFunction<T> action) {
        Database db = Database.connect(pDb);
        boolean ownsDb = pDb == null;

        try {
            if (ownsDb) {
                db.beginTransaction();
            }

            T result = action.apply(db.getConnection());

            if (ownsDb) {
                db.endTransaction();
            }

            return result;
        } catch (SQLException e) {
            if (ownsDb) {
                rollbackQuietly(db);
            }
            logger.error("Database error: {}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        } catch (RuntimeException e) {
            if (ownsDb) {
                rollbackQuietly(db);
            }
            throw e;
        } finally {
            Database.close(pDb, db);
        }
    }

    <T> T withDatabase(Database pDb, SqlSupplier<T> action) {
        Database db = Database.connect(pDb);

        try {
            return action.apply(db);
        } catch (SQLException e) {
            logger.error("Database error: {}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            Database.close(pDb, db);
        }
    }

    private void rollbackQuietly(Database db) {
        if (db != null) {
            try {
                db.rollback();
            } catch (SQLException e) {
                logger.warn("Error rolling back transaction: {}", e.getMessage());
            }
        }
    }

    @FunctionalInterface
    interface SqlFunction<T> {
        T apply(Connection conn) throws SQLException;
    }

    @FunctionalInterface
    interface SqlSupplier<T> {
        T apply(Database db) throws SQLException;
    }
}


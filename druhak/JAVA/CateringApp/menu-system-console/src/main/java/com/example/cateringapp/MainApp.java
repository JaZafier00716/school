package com.example.cateringapp;

import com.example.cateringapp.dao.MenuVersionDao;
import com.example.cateringapp.dao.MenuVersionDaoInterface;
import com.example.cateringapp.dto.MenuVersionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main application entry point.
 * Demonstrates calling the CreateNewMenuVersion stored function or Java implementation and verifying results.
 */
public class MainApp {
    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    private enum ExecutionMode {
        DATABASE,
        JAVA
    }

    private record ResolvedArgs(ExecutionMode mode, long menuId, long userId) {
    }

    public static void main(String[] args) {
        logger.info("=== Menu System Console Application ===");
        logger.info("Starting application...");

        try {
            ResolvedArgs resolvedArgs = parseArguments(args);

            // Initialize DAO
            MenuVersionDaoInterface menuVersionDao = new MenuVersionDao();

            // Call the selected implementation to create new menu version
            logger.info("Creating new menu version using {} implementation...", resolvedArgs.mode());
            long newVersionId = resolvedArgs.mode() == ExecutionMode.JAVA
                    ? menuVersionDao.createNewMenuVersionInJava(resolvedArgs.menuId(), resolvedArgs.userId())
                    : menuVersionDao.createNewMenuVersion(resolvedArgs.menuId(), resolvedArgs.userId());

            // Verify the created version
            logger.info("Verifying created version...");
            MenuVersionDto createdVersion = menuVersionDao.getMenuVersion(newVersionId);

            // Print results
            printResults(createdVersion);

            logger.info("=== Application completed successfully ===");

        } catch (Exception e) {
            logger.error("Application error: {}", e.getMessage());
            logger.error("Full stack trace:", e);
            System.exit(1);
        }
    }

    private static ResolvedArgs parseArguments(String[] args) {
        ExecutionMode mode = ExecutionMode.DATABASE;
        int offset = 0;

        if (args.length > 0) {
            ExecutionMode parsedMode = parseMode(args[0]);
            if (parsedMode != null) {
                mode = parsedMode;
                offset = 1;
            }
        }

        long menuId = parseLongArgument(args, offset, 1, "menu ID");
        long userId = parseLongArgument(args, offset + 1, 1, "user ID");
        return new ResolvedArgs(mode, menuId, userId);
    }

    private static ExecutionMode parseMode(String value) {
        if (value == null) {
            return null;
        }

        return switch (value.toLowerCase()) {
            case "java" -> ExecutionMode.JAVA;
            case "database", "db", "stored", "stored-function", "function" -> ExecutionMode.DATABASE;
            default -> null;
        };
    }

    private static long parseLongArgument(String[] args, int index, long defaultValue, String label) {
        if (index < args.length) {
            try {
                return Long.parseLong(args[index]);
            } catch (NumberFormatException e) {
                logger.warn("Invalid {} argument: {}, using default", label, args[index]);
            }
        }

        logger.info("Using default {}: {}", label, defaultValue);
        return defaultValue;
    }

    /**
     * Prints the results of the operation in a formatted way.
     *
     * @param version The created menu version
     */
    private static void printResults(MenuVersionDto version) {
        if (version == null) {
            System.out.println("\n❌ ERROR: Created version was not found in database!");
            return;
        }

        System.out.println("\n" + "=".repeat(50));
        System.out.println("✅ New Menu Version Created Successfully!");
        System.out.println("=".repeat(50));
        System.out.println("Version ID:      " + version.getVersionId());
        System.out.println("Menu ID:         " + version.getMenuId());
        System.out.println("Version Number:  " + version.getVersionNumber());
        System.out.println("With Prices:     " + (version.isWithPrices() ? "Yes" : "No"));
        System.out.println("Template ID:     " + (version.getTemplateId() != null ? version.getTemplateId() : "None"));
        System.out.println("=".repeat(50));
        System.out.println("Details: " + version);
        System.out.println("=".repeat(50) + "\n");
    }
}


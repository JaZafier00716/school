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
        run(args);
    }

    private static void run(String[] args) {
        logger.info("=== Menu System Console Application ===");
        logger.info("Starting application...");

        try {
            ResolvedArgs resolvedArgs = parseArguments(args);
            MenuVersionDaoInterface menuVersionDao = new MenuVersionDao();
            MenuVersionDto createdVersion = createAndVerifyVersion(menuVersionDao, resolvedArgs);
            printResults(createdVersion);

            logger.info("=== Application completed successfully ===");

        } catch (Exception e) {
            handleError(e);
        }
    }

    private static MenuVersionDto createAndVerifyVersion(MenuVersionDaoInterface menuVersionDao, ResolvedArgs resolvedArgs) {
        long newVersionId = createNewVersion(menuVersionDao, resolvedArgs);

        logger.info("Verifying created version...");
        return menuVersionDao.getMenuVersion(newVersionId);
    }

    private static long createNewVersion(MenuVersionDaoInterface menuVersionDao, ResolvedArgs resolvedArgs) {
        logger.info("Creating new menu version using {} implementation...", resolvedArgs.mode());
        return resolvedArgs.mode() == ExecutionMode.JAVA
                ? menuVersionDao.createNewMenuVersionInJava(resolvedArgs.menuId(), resolvedArgs.userId())
                : menuVersionDao.createNewMenuVersionDatabase(resolvedArgs.menuId(), resolvedArgs.userId());
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

        long menuId = parseLongArgument(args, offset, "menu ID");
        long userId = parseLongArgument(args, offset + 1, "user ID");
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

    private static long parseLongArgument(String[] args, int index, String label) {
        if (index < args.length) {
            try {
                return Long.parseLong(args[index]);
            } catch (NumberFormatException e) {
                logger.warn("Invalid {} argument: {}, using default", label, args[index]);
            }
        }

        long defaultValue = 1;
        logger.info("Using default {}: {}", label, defaultValue);
        return defaultValue;
    }

    private static void handleError(Exception e) {
        logger.error("Application error: {}", e.getMessage());
        logger.error("Full stack trace:", e);
        System.exit(1);
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

        printHeader();
        printVersionField("Version ID:      ", version.getVersionId());
        printVersionField("Menu ID:         ", version.getMenuId());
        printVersionField("Version Number:  ", version.getVersionNumber());
        printVersionField("With Prices:     ", version.isWithPrices() ? "Yes" : "No");
        printVersionField("Template ID:     ", version.getTemplateId() != null ? version.getTemplateId() : "None");
        printFooter(version);
    }

    private static void printHeader() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("✅ New Menu Version Created Successfully!");
        System.out.println("=".repeat(50));
    }

    private static void printVersionField(String prefix, Object value) {
        System.out.println(prefix + value);
    }

    private static void printFooter(MenuVersionDto version) {
        System.out.println("=".repeat(50));
        System.out.println("Details: " + version);
        System.out.println("=".repeat(50) + "\n");
    }
}


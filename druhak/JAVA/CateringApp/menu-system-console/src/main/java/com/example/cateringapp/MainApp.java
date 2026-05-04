package com.example.cateringapp;

import com.example.cateringapp.dao.MenuVersionDao;
import com.example.cateringapp.dao.MenuVersionDaoInterface;
import com.example.cateringapp.dto.MenuVersionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main application entry point.
 * Demonstrates calling the CreateNewMenuVersion stored function and verifying results.
 */
public class MainApp {
    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) {
        logger.info("=== Menu System Console Application ===");
        logger.info("Starting application...");

        try {
            // Parse command line arguments or use defaults
            long menuId = parseMenuId(args);
            long userId = parseUserId(args);

            // Initialize DAO
            MenuVersionDaoInterface menuVersionDao = new MenuVersionDao();

            // Call the stored function to create new menu version
            logger.info("Creating new menu version...");
            long newVersionId = menuVersionDao.createNewMenuVersion(menuId, userId);

            // Verify the created version
            logger.info("Verifying created version...");
            MenuVersionDto createdVersion = menuVersionDao.getMenuVersion(newVersionId);

            // Print results
            printResults(createdVersion);

            logger.info("=== Application completed successfully ===");

        } catch (Exception e) {
            logger.error("Application error: {}", e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Parses menu ID from command line arguments or returns default.
     *
     * @param args Command line arguments
     * @return Menu ID
     */
    private static long parseMenuId(String[] args) {
        if (args.length > 0) {
            try {
                return Long.parseLong(args[0]);
            } catch (NumberFormatException e) {
                logger.warn("Invalid menu ID argument: {}, using default", args[0]);
            }
        }
        logger.info("Using default menu ID: 1");
        return 1;
    }

    /**
     * Parses user ID from command line arguments or returns default.
     *
     * @param args Command line arguments
     * @return User ID
     */
    private static long parseUserId(String[] args) {
        if (args.length > 1) {
            try {
                return Long.parseLong(args[1]);
            } catch (NumberFormatException e) {
                logger.warn("Invalid user ID argument: {}, using default", args[1]);
            }
        }
        logger.info("Using default user ID: 1");
        return 1;
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


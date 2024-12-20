package nl.bioinf.alpruis;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * The ErrorThrower class provides a method for handling and logging critical IOExceptions and Exceptions.
 * It logs the error message and cause at the fatal level and then terminates the application.
 */
public class ErrorThrower {
    private static final Logger logger = LogManager.getLogger(ErrorThrower.class.getName());

    /**
     * Logs a fatal error message and the cause of an IOException.
     * This method should be called when a critical error occurs that requires the application to exit.
     *
     * @param ex The IOException that triggered the error.
     */
    public static void throwError(IOException ex) {
        logger.fatal("Something went wrong: IOException. Exiting.\n" +
                "Error information: " + ex.getMessage() + ": " + ex.getCause());
        System.exit(1);
    }

    /**
     * Logs a fatal error message and the cause of an Exception.
     * This method should be called when a critical error occurs that requires the application to exit.
     *
     * @param e The Exception that triggered the error.
     */
    public static void throwError(Exception e) {
        logger.fatal("Something went wrong: Exception. Exiting.\n" +
                "Error information: " + e.getMessage() + ": " + e.getCause());
        System.exit(1);
    }

    /**
     * Logs a fatal error message and the cause of an Exception.
     * This method should be called when a critical error occurs that requires the application to exit.
     *
     * @param message The invalid input of filter that triggered the error.
     */
    public static void throwError(String message) {
        logger.fatal("Something went wrong: input filter invalid. Exiting.\n" +
                "Error information: "+ message);
        System.exit(1);
    }
}

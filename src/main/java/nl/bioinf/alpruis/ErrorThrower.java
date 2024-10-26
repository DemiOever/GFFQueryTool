package nl.bioinf.alpruis;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * The ErrorThrower class provides a method for handling and logging critical IOExceptions.
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
        logger.fatal("Something went wrong. Exiting.\n" +
                "Error information: " + ex.getMessage() + ": " + ex.getCause());
    }

    public static void throwErrorE(Exception e) {
        logger.fatal("Something went wrong. Exiting.\n" +
                "Error information: " + e.getMessage() + ": " + e.getCause());
    }
} // TODO make the errors better and add more of them

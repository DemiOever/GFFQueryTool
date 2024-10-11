package nl.bioinf.alpruis;

import java.io.IOException;

import static nl.bioinf.alpruis.Main.logger;

public class ErrorThrower {
    public static void throwError(IOException ex) {
        logger.error("Something went wrong. Exiting.\n" +
                "Error information: " + ex.getMessage() + ": " +
                ex.getCause());
    }
}

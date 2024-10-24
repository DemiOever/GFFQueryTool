package nl.bioinf.alpruis;

import picocli.CommandLine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new CommandLineParser()).execute(args);
        System.exit(exitCode);
    }
}

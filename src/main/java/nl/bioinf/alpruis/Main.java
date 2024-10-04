package nl.bioinf.alpruis;

import nl.bioinf.alpruis.operations.AbstractTree;
import picocli.CommandLine;

import java.io.File;
import java.lang.reflect.Array;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class.getName());

    public static boolean isValid() {
        // first line == to "##gff-version 3"
        /*
        lees de eerste line van de file
        vergelijk met "##gff-version 3"
        if gelijk: set validate to True;
        else: set validate to False
        the same for fasta
        im just hoping there is a java package to this for us
         */
        return true;
    }

    public static Object fileParser() {

        if (isValid()){
            // the data gff is seperated by /tab
            // per line through the file
            boolean validated = isValid();
            AbstractTree tree = new AbstractTree();
            System.out.println("valid and continuing");
        }
        else {
            System.out.println("not valid and warning");
        }
        // skip lines with ## at the beginning
        // sends every line to become a feature and get added to an array might need to be a TREE???!?!?!?!?
        return Array.newInstance(File.class, 0);
    }

    public String makeSequence(String inputFastaFile) {
        // reading the fasta file and making one long sequence?
        return "ATCGCGGATAGC";
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new CommandLineParser()).execute(args);
        System.exit(exitCode);
    }
}

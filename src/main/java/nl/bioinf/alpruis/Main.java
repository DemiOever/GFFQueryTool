package nl.bioinf.alpruis;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;

import java.io.File;
import java.lang.reflect.Array;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Command(name = "GffCommandLine", mixinStandardHelpOptions = true, version = "1.0",
        description = "A command-line tool to parse and query GFF3 files.")

public class Main implements Runnable {
    private static final Logger logger = LogManager.getLogger(Main.class.getName());

    // Define the GFF3 input file as a positional argument
    @Parameters(index = "0", description = "The input GFF3 file.")
    private String inputGffFile;

    // Define the FASTA file
    @Parameters(index = "1", description = "The input FASTA file.")
    private String inputFastaFile;

   @Option(names = {"-s", "--summary"}, description = "Gives back a summary of the files and includes: Creates a textual summary of the parsed file: length of the sequence, gc-percentage, feature types with the amount present, number and types of annotations/features?. Average lengths of the genes, number on forward strand, number on reverse strand. Usage is a boolean.")
    private boolean summary;

    @Option(names = {"-i", "--fetch_id"}, description = "Returns the nucleotide sequence of the element with this ID, in Fasta format.")
    private String fetch_id;

    @Option(names = {"-f", "--fetch_feature"}, description = "Returns the nucleotide sequence of all the elements of this type, in Fasta format.")
    private String fetch_feature;

    @Option(names = {"-t", "--fetch_type"}, description = "Returns the nucleotide sequence of the element with this feature type, in Fasta format.")
    private String fetch_type;

    @Option(names = {"-a", "--fetch_attribute"}, description = "Returns the nucleotide sequence of the element with this attribute name, in Fasta format.")
    private String fetch_attribute;

    @Option(names = {"-r", "--fetch_region"}, description = "Returns all features between the given coordinates, in gff format.")
    private String fetch_region;

    @Option(names = {"--validate"}, description = "Validates the file if it has the right gff3 format.")
    private boolean validate;

    // The help option is automatically included by Picocli due to mixinStandardHelpOptions = true

    public boolean isValid() {
        // first line == to "##gff-version 3"
        /*
        lees de eerste line van de file
        vergelijk met "##gff-version 3"
        if gelijk: set validate to True;
        else: set validate to False
         */
        validate = true;
        return validate;
    }

    public Object fileParser() {
        if (isValid()){
            System.out.println("valid and continuing");
        }
        else {
            System.out.println("not valid and warning");
        }
        // skip lines with ## at the beginning
        // sends every line to become a feature and get added to an array
        return Array.newInstance(File.class, 0);
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        if (summary) {
            logger.info("Generating summary...");
            // Implement summary logic here
        }

        else if (validate) {
            logger.info("Validating GFF3 file...");
            boolean validated = isValid();
            if (validated) {
                logger.info("Validated GFF3 file. This file can be used for the purpose.");
            }
            else{
                logger.warn("The GFF file isn't a valid GFF3 format.");
            }
        }

        else if (fetch_id != null) {
            logger.info("Fetching by ID: " + fetch_id);
            // Implement ID fetching logic here
        }

        else if (fetch_type != null) {
            logger.info("Fetching by type: " + fetch_type);
            // Implement type fetching logic here
        }

        else if (fetch_attribute != null) {
            logger.info("Fetching by attribute: " + fetch_attribute);
            // Implement attribute fetching logic here
        }

        else if (fetch_region != null) {
            logger.info("Fetching by region: " + fetch_region);
            // Implement region fetching logic here
        }

        else if (fetch_feature != null) {
            logger.info("Fetching by feature: " + fetch_feature);
            // Implement feature fetching logic here
        }

        else {
            logger.warn("You may have gave the command a null which is not accepted as an option.");
        }
    }
}

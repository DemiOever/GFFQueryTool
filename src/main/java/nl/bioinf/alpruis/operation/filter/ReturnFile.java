package nl.bioinf.alpruis.operation.filter;

import nl.bioinf.alpruis.ErrorThrower;
import nl.bioinf.alpruis.Feature;
import nl.bioinf.alpruis.OptionsProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * The ReturnFile class provides methods to write features to different file formats including FASTA, GFF, CSV, and plain text.
 * The file format is determined based on the file extension, and appropriate methods are used to output the data.
 */

public class ReturnFile {
    private static final Logger logger = LogManager.getLogger(ReturnFile.class.getName());

    /**
     * Writes a single feature to a file in GFF format.
     */
    private static void returnGff(Path outFile, Feature feature) {
        try (BufferedWriter writer = Files.newBufferedWriter(outFile, StandardOpenOption.APPEND)) {
            writer.write(feature.toGffFormat()); // Writes feature in GFF format
            writer.newLine();
        } catch (IOException ex) {
            ErrorThrower.throwError(ex);
        }
    }

    /**
     * Writes a single feature to a file in plain text format.
     */
    private static void returnTxt(Path outFile, Feature feature) {
        try (BufferedWriter writer = Files.newBufferedWriter(outFile, StandardOpenOption.APPEND)) {
            writer.write(feature.toString()); // Writes feature using its toString method
            writer.newLine();
        } catch (IOException ex) {
            ErrorThrower.throwError(ex);
        }
    }

    /**
     * Writes a single feature to a file in CSV format.
     */
    private static void returnCsv(Path outFile, Feature feature) {
        try (BufferedWriter writer = Files.newBufferedWriter(outFile, StandardOpenOption.APPEND)) {
            writer.write(feature.toCsvFormat()); // Writes feature in CSV format
            writer.newLine();
        } catch (IOException ex) {
            ErrorThrower.throwError(ex);
        }
    }

    /**
     * Determines the file type based on the file extension and writes the feature to the appropriate format.
     */
    public static void chooseTypeFile(Feature feature, OptionsProcessor options) {
        Path outputFile = options.getOutputFile();
        String fileName = outputFile.getFileName().toString().toLowerCase();

        // Handle different output formats based on file extension
        if (fileName.endsWith(".gff")) {
            returnGff(outputFile, feature);
        } else if (fileName.endsWith(".csv")) {
            returnCsv(outputFile, feature);
        } else if (fileName.endsWith(".txt")) {
            returnTxt(outputFile, feature);
        } else {
            //TODO doesn't work yet
            outputFile = Paths.get("./output/standard_gff_outfile.gff");
            options.setOutputFile(outputFile);
            logger.warn("Given file was invalid so writing to:" + outputFile);
            checkFileDir(options);
            returnGff(outputFile, feature);
            // Unsupported file format, default to GFF output
                /*logger.error("Unsupported file format: {}. Writing to default GFF file: output_GFQueryTool.gff", fileName);
                Path defaultOutFile = outputFile.getParent() != null ?
                        outputFile.getParent().resolve("output_GFQueryTool.gff") :
                        Path.of("output_GFQueryTool.gff");

                returnGff(defaultOutFile, feature);*/
        }
    }

    public static void checkOutputfileVariable(OptionsProcessor options) {
        Path outputFile = options.getOutputFile();

        // Case 1: No output file specified, set to default
        if (outputFile == null || outputFile.toString().isEmpty()) {
            outputFile = Paths.get("./output/standard_gff_outfile.gff");
            options.setOutputFile(outputFile);
            logger.debug("No file or path provided, using default: " + outputFile);
        }
        // Case 2: Only a directory is specified (e.g., ends with "\")
        else if (Files.isDirectory(outputFile) || outputFile.toString().endsWith(File.separator)) {
            outputFile = outputFile.resolve("standard_gff_outfile.gff");
            options.setOutputFile(outputFile);
            logger.debug("Directory provided, adding default filename: " + outputFile);
        }
        // Case 3: Only filename specified, add default directory
        else if (outputFile.getParent() == null) {
            if (outputFile.toString().contains(".")) {
                outputFile = Paths.get("./output", outputFile.toString());
            } else {
                outputFile = Paths.get("./output", outputFile + ".gff");
            }
            options.setOutputFile(outputFile);
            logger.debug("Filename provided without path, using default path: " + outputFile);
        }
    }

    public static void checkFileDir(OptionsProcessor options) {
        // Ensure outputFile variable is checked and set
        Path outputFile = options.getOutputFile();

        // Ensure the parent directory exists or create it
        Path parentDir = outputFile.getParent();
        logger.debug("Output file path: " + outputFile);
        logger.debug("Parent directory: " + parentDir);

        if (parentDir != null && !Files.exists(parentDir)) {
            try {
                Files.createDirectories(parentDir);
            } catch (IOException ex) {
                ErrorThrower.throwError(ex);
                return;
            }
        }

        // Create or overwrite the output file
        try {
            if (Files.exists(outputFile)) {
                Files.delete(outputFile); // Overwrite if file exists
            }
            Files.createFile(outputFile);
        } catch (IOException ex) {
            ErrorThrower.throwError(ex);
        }
    }




    public static void writeHeader(String line, OptionsProcessor options) {
        Path outFile = options.getOutputFile();
        try (BufferedWriter writer = Files.newBufferedWriter(outFile, StandardOpenOption.APPEND)) {
            writer.write(line);
            writer.newLine();
        } catch (IOException ex) {
            ErrorThrower.throwError(ex);
        }
    }
}
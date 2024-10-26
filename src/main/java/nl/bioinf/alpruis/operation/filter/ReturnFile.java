package nl.bioinf.alpruis.operation.filter;

import nl.bioinf.alpruis.ErrorThrower;
import nl.bioinf.alpruis.Feature;
import nl.bioinf.alpruis.OptionsProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;

/**
 * The ReturnFile class provides methods to write features to different file formats including FASTA, GFF, CSV, and plain text.
 * The file format is determined based on the file extension, and appropriate methods are used to output the data.
 */

public class ReturnFile {
    private static final Logger logger = LogManager.getLogger(ReturnFile.class.getName());
        /**
         * Writes a single feature to a file in FASTA format.
         */
        private static void returnFasta(Path outFile, Feature feature, Map<String, String> seq) {
            try (BufferedWriter writer = Files.newBufferedWriter(outFile, StandardOpenOption.APPEND)) {
                writer.write(">Feature " + feature.getID()); // Writes feature ID as FASTA header
                writer.newLine();
                writer.write(feature.toString()); // Writes feature data
                writer.newLine();
            } catch (IOException ex) {
                ErrorThrower.throwError(ex);
            }
        }

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
                writer.newLine();
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
            if (fileName.endsWith(".fasta")) {
                returnFasta(outputFile, feature, options.getSequence());
            } else if (fileName.endsWith(".gff")) {
                returnGff(outputFile, feature);
            } else if (fileName.endsWith(".csv")) {
                returnCsv(outputFile, feature);
            } else if (fileName.endsWith(".txt")) {
                returnTxt(outputFile, feature);
            } else {
                // Unsupported file format, default to GFF output
                logger.error("Unsupported file format: {}. Writing to default GFF file: output_GFQueryTool.gff", fileName);
                Path defaultOutFile = outputFile.getParent() != null ?
                        outputFile.getParent().resolve("output_GFQueryTool.gff") :
                        Path.of("output_GFQueryTool.gff");

                returnGff(defaultOutFile, feature);
            }
        }

    public static void checkFileDir(Path outputFile) {

        // Create directory if it doesn't exist
        Path parentDir = outputFile.getParent();
        if (parentDir != null && !Files.exists(parentDir)) {
            try {
                Files.createDirectories(parentDir);
            } catch (IOException ex) {
                ErrorThrower.throwError(ex);
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

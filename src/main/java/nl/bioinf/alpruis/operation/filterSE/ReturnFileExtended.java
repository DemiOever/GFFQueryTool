package nl.bioinf.alpruis.operation.filterSE;

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
import java.util.LinkedList;
import java.util.Map;

/**
 * The ReturnFile class provides methods to write features to different file formats including FASTA, GFF, CSV, and plain text.
 * The file format is determined based on the file extension, and appropriate methods are used to output the data.
 */
public class ReturnFileExtended {
    // TODO update the code and make it extended
    private static final Logger logger = LogManager.getLogger(ReturnFileExtended.class);
    /**
     * Writes the features to a file in FASTA format.
     *
     * @param outFile The output file path.
     * @param result The list of features to write.
     * @param seq A map containing sequences, typically chromosome sequences, that relate to the features.
     */
    private static void returnFasta(Path outFile, LinkedList<Feature> result, Map<String, String> seq) { //TODO make the method work (lvl 3)
        try (BufferedWriter writer = Files.newBufferedWriter(outFile, StandardOpenOption.APPEND)) {
            for (Feature feature : result) {
                writer.write(">Feature " + feature.getID()); // Writes feature ID as FASTA header
                writer.newLine();
                writer.write(feature.toString()); // Writes feature data
                writer.newLine();
            }
        } catch (IOException ex) {
            ErrorThrower.throwError(ex);
        }
    }

    /**
     * Writes the features to a file in GFF format.
     *
     * @param outFile The output file path.
     * @param result The list of features to write.
     */
    private static void returnGff(Path outFile, LinkedList<Feature> result) {
        System.out.println(outFile);
        System.out.println(outFile.getParent());
        try (BufferedWriter writer = Files.newBufferedWriter(outFile, StandardOpenOption.APPEND)) {
            for (Feature feature : result) {
                writer.write(feature.toGffFormat()); // Writes feature in GFF format
                writer.newLine();
            }
        } catch (IOException ex) {
            ErrorThrower.throwError(ex);
        }
    }

    /**
     * Writes the features to a file in plain text format.
     *
     * @param outFile The output file path.
     * @param result The list of features to write.
     */
    private static void returnTxt(Path outFile, LinkedList<Feature> result) {
        try (BufferedWriter writer = Files.newBufferedWriter(outFile, StandardOpenOption.APPEND)) {
            for (Feature feature : result) {
                writer.write(feature.toString()); // Writes feature using its toString method
                writer.newLine();
            }
        } catch (IOException ex) {
            ErrorThrower.throwError(ex);
        }
    }

    /**
     * Writes the features to a file in CSV format.
     *
     * @param outFile The output file path.
     * @param result The list of features to write.
     */
    private static void returnCsv(Path outFile, LinkedList<Feature> result) {
        try (BufferedWriter writer = Files.newBufferedWriter(outFile, StandardOpenOption.APPEND)) {
            writer.write("ID,Source,Type,Start,End,Score,Strand,Phase,Attributes"); // Writes CSV header
            writer.newLine();
            for (Feature feature : result) {
                writer.write(feature.toCsvFormat()); // Writes feature in CSV format
                writer.newLine();
            }
        } catch (IOException ex) {
            ErrorThrower.throwError(ex);
        }
    }

    /**
     * Determines the file type based on the file extension and writes the features to the appropriate format.
     * Supports FASTA, GFF, CSV, and plain text formats. For unsupported formats, writes to a default GFF file.
     *
     * @param outputFile The output file path.
     * @param result The list of features to write.
     * @param seq A map containing sequences, used for the FASTA output.
     */
    public static void chooseTypeFile(OptionsProcessor options, LinkedList<Feature> result) {
        Path outputFile = options.getOutputFile();
        String fileName = outputFile.getFileName().toString().toLowerCase();

        // Handle different output formats based on file extension
        if (fileName.endsWith(".fasta")) {
            checkFileDir(outputFile);
            returnFasta(outputFile, result, options.getSequence());
        } else if (fileName.endsWith(".gff")) {
            checkFileDir(outputFile);
            returnGff(outputFile, result);
        } else if (fileName.endsWith(".csv")) {
            checkFileDir(outputFile);
            returnCsv(outputFile, result);
        } else if (fileName.endsWith(".txt")) {
            checkFileDir(outputFile);
            returnTxt(outputFile, result);
        } else {
            // Unsupported file format, default to GFF output
            logger.error("Unsupported file format: {}. Writing to default GFF file: output_GFQueryTool.gff", fileName);
            // TODO figure out how to write to a default type aka output_GFQueryTool.gff(lvl 1) - Demi

            // Create a default path for the output file
            
            Path defaultOutFile = outputFile.getParent() != null ?
                    outputFile.getParent().resolve("output_GFQueryTool.gff") :
                    Path.of("output_GFQueryTool.gff");

            // Write to the default GFF file
            returnGff(defaultOutFile, result);
        }
    }

    private static void checkFileDir(Path outputFile) {

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
}

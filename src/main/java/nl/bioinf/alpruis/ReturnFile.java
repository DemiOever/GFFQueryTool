package nl.bioinf.alpruis;

import static nl.bioinf.alpruis.Main.logger;

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
public class ReturnFile {

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
     * Supports FASTA, GFF, CSV, and plain text formats.
     *
     * @param outputFile The output file path.
     * @param result The list of features to write.
     * @param seq A map containing sequences, used for the FASTA output.
     */
    public static void chooseTypeFile(Path outputFile, LinkedList<Feature> result, Map<String, String> seq) {
        String fileName = outputFile.getFileName().toString().toLowerCase();

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

        // Handle different output formats based on file extension
        if (fileName.endsWith(".fasta")) {
            returnFasta(outputFile, result, seq);
        } else if (fileName.endsWith(".gff")) {
            returnGff(outputFile, result);
        } else if (fileName.endsWith(".csv")) {
            returnCsv(outputFile, result);
        } else if (fileName.endsWith(".txt")) {
            returnTxt(outputFile, result);
        } else {
            logger.error("Unsupported file format: {}. Writing to default type of file.", fileName); // TODO figure out how to write to a default type aka output_GFQueryTool.gff(lvl 1)
            //Path outFile = (Path) "./output_GFQueryTool.gff";
            returnGff(outputFile, result);

        }
    }
}

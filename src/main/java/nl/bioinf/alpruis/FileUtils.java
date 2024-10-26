package nl.bioinf.alpruis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import static nl.bioinf.alpruis.ErrorThrower.throwError;

/**
 * Utility class providing functions for validating and processing files, specifically GFF3 and FASTA files.
 */
public class FileUtils {
    private static final Logger logger = LogManager.getLogger(FileUtils.class.getName());
    /**
     * Validates a GFF3 or FASTA file based on its extension and content.
     * - GFF3 files are validated by checking if they start with "##gff-version 3"
     *   and ensuring that they have the correct number of columns.
     * - FASTA files are validated by ensuring that the first line starts with ">" and
     *   that the subsequent sequence contains valid nucleotides (A, T, C, G, N).
     *
     * @param inputFile_path the path to the file to be validated.
     * @return true if the file is valid, false otherwise.
     */
    public static Boolean fileValidator(Path inputFile_path) {
        boolean isValid = false;

        try (BufferedReader reader = Files.newBufferedReader(inputFile_path)) {
            String firstLine = reader.readLine();
            String inputFile = inputFile_path.getFileName().toString();

            if (inputFile.endsWith(".gff") || inputFile.endsWith(".gff3")) {
                // Check if the first line is "##gff-version 3"
                if (firstLine != null && firstLine.equals("##gff-version 3") && contentGFFValid(reader)) {
                    isValid = true;
                }
            } else if (inputFile.endsWith(".fasta") || inputFile.endsWith(".txt") || inputFile.endsWith(".fna") || inputFile.endsWith(".fas") || inputFile.endsWith(".fa") || inputFile.endsWith(".fnn") || inputFile.endsWith(".faa") || inputFile.endsWith(".mpfa") || inputFile.endsWith(".frn")) {
                // Check if the first line starts with ">" and contains only ATCG characters in the sequence
                if (firstLine != null && firstLine.startsWith(">")) {
                    isValid = fastaSequenceValidator(reader);
                }
            }
        } catch (IOException ex) {
            throwError(ex);
        }
        return isValid;
    }

    /**
     * Checks the first 100 lines of a GFF3 file to ensure each line (excluding comment lines) has exactly 9 columns.
     *
     * @param br the BufferedReader for reading the GFF3 file.
     * @return true if the file content is valid, false otherwise.
     */
    private static boolean contentGFFValid(BufferedReader br) {
        boolean isValid = true;
        try {
            for (int i = 0; i <= 100; i++) {
                String line = br.readLine();
                if (!line.startsWith("#")) {
                    String[] columns = line.split("\t");
                    if (columns.length != 9) {
                        logger.warn("Invalid amount of columns({}) found on row {}/100 of given GFF file", i, columns.length);
                        isValid = false;
                    }
                }
            }
        }
        catch (IOException ex) {
            throwError(ex);
        }
        return isValid;
    }

    /**
     * Validates the nucleotide sequence of a FASTA file. It ensures that the sequence
     * only contains valid nucleotide characters (A, T, C, G, N).
     *
     * @param br the BufferedReader for reading the FASTA file.
     * @return true if the sequence is valid, false otherwise.
     * @throws IOException if an I/O error occurs while reading the file.
     */
    static boolean fastaSequenceValidator(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            line = line.toUpperCase();
            // Ensure that the sequence only contains A, T, C, G (ignoring line breaks and comments starting with ">")
            if (!line.startsWith(">") && !line.matches("[ATCGN]*")) {
                return false;
            }
        }
        return true;
    }

    /**
     * Parses a FASTA file and constructs a map where each sequence is stored with its corresponding header.
     * The header starts with ">" and the sequence consists of the nucleotide characters.
     *
     * @param inputFastaFile the path to the input FASTA file.
     * @return a map where keys are FASTA headers and values are the corresponding sequences.
     */
    public static Map<String, String> sequenceMaker(Path inputFastaFile) {
        Map<String, String> sequence = new LinkedHashMap<>();
        String header = "";
        StringBuilder seq = new StringBuilder(); // Use StringBuilder for better performance

        try (BufferedReader reader = Files.newBufferedReader(inputFastaFile)) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Handle header lines starting with '>'
                if (line.startsWith(">")) {
                    // If there's already a header, save the previous sequence before moving to the new one
                    if (!header.isEmpty()) {
                        sequence.put(header, seq.toString());
                        seq.setLength(0); // Reset the sequence for the next header
                    }
                    header = line; // Update header with the current line (FASTA header)
                } else {
                    // Append sequence lines after the header
                    seq.append(line);
                }
            }

            // Put the last header and sequence after the loop finishes
            if (!header.isEmpty()) {
                sequence.put(header, seq.toString());
            }
        } catch (IOException ex) {
            ErrorThrower.throwError(ex);
        }

        return sequence;
    }
}
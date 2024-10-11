package nl.bioinf.alpruis;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static nl.bioinf.alpruis.ErrorThrower.throwError;
import static nl.bioinf.alpruis.Main.logger;

public class FileUtils {
    public static Boolean fileValidator(Path inputFile_path) {
        boolean isValid = false;

        try (BufferedReader reader = Files.newBufferedReader(inputFile_path)) {
            String firstLine = reader.readLine();
            String inputFile = inputFile_path.getFileName().toString();

            if (inputFile.endsWith(".gff") || inputFile.endsWith(".gff3")) {
                // Check if the first line is "##gff-version 3"
                if (firstLine != null && firstLine.equals("##gff-version 3") && contentGFFValid(reader)) {
                    isValid = true;
                    //TODO check gff3 file also on having 9 columns(lvl 1)
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

    private static boolean contentGFFValid(BufferedReader br) throws IOException {
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

    public static Map<String, String> sequenceMaker(Path inputFastaFile) {
        Map<String, String> sequence = new HashMap<>();
        String header = "";
        StringBuilder seq = new StringBuilder(); // Use StringBuilder for better performance

        if (FileUtils.fileValidator(inputFastaFile)) {
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
            } catch (IOException e) {
                logger.error("Error reading the FASTA file: {}", e.getMessage());
            }
        } else {
            logger.warn("Invalid FASTA file.");
        }

        return sequence;
    }
}
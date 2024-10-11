package nl.bioinf.alpruis;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nl.bioinf.alpruis.Main.logger;

public class FileUtils {
    public static boolean fileValidator(Path inputFile_path) {
        boolean isValid = false;

        try (BufferedReader reader = Files.newBufferedReader(inputFile_path)) {
            String firstLine = reader.readLine();
            String inputFile = inputFile_path.getFileName().toString();

            if (inputFile.endsWith(".gff") || inputFile.endsWith(".gff3")) {
                // Check if the first line is "##gff-version 3"
                if (firstLine != null && firstLine.equals("##gff-version 3") ) {
                    isValid = true;
                    //TODO check gff3 file also on having 9 columns(lvl 1)
                }
            } else if (inputFile.endsWith(".fasta") || inputFile.endsWith(".txt") || inputFile.endsWith(".fna") || inputFile.endsWith(".fas") || inputFile.endsWith(".fa") || inputFile.endsWith(".fnn") || inputFile.endsWith(".faa") || inputFile.endsWith(".mpfa") || inputFile.endsWith(".frn")) {
                // Check if the first line starts with ">" and contains only ATCG characters in the sequence
                if (firstLine != null && firstLine.startsWith(">")) {
                    isValid = fastaSequenceValidator(reader);
                }
            }
        } catch (IOException e) {
            //TODO create new logger(lvl 2)
            //Main.logger.error("Error reading file: " + e.getMessage());

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

    public static String sequenceMaker(Path inputFastaFile) {
        StringBuilder sequence = new StringBuilder();

        if (FileUtils.fileValidator(inputFastaFile)) {
            try (BufferedReader reader = Files.newBufferedReader(inputFastaFile)) {
                String line;
                boolean isHeader = true;
                //TODO use a hashmap to save the header and sequence(lvl 2)

                while ((line = reader.readLine()) != null) {
                    // Skip the header lines starting with '>'
                    if (line.startsWith(">")) { //TODO save the header for when they want a fasta/txt back(lvl 2)
                        isHeader = false;
                        continue;
                    }

                    // Append sequence lines after the header
                    if (!isHeader) {
                        sequence.append(line.trim().toUpperCase());
                    }
                }
            } catch (IOException e) {
                //TODO create new logger(lvl 2)
                // e.printStackTrace();
            }
        } else {
            //TODO create new logger(lvl 2)
            logger.warn("Invalid FASTA file.");
        }

        // Return the parsed sequence as a string
        return sequence.toString();
    }

}
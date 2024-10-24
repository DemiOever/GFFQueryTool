package nl.bioinf.alpruis.operation.filter;

import nl.bioinf.alpruis.ErrorThrower;
import nl.bioinf.alpruis.Feature;
import nl.bioinf.alpruis.OptionsProcessor;
import nl.bioinf.alpruis.operation.filterSE.GffParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static nl.bioinf.alpruis.Main.logger;

/**
 * The GffParser class is responsible for parsing GFF3 files, creating Feature objects,
 * and storing them in a linked list. It processes each line of the GFF3 file, handling
 * features and their parent-child relationships.
 */
public class GffProcessor {
// TODO logger.warn for when the given list with options includes one that simply doesn't exist in the gff file (lvl 1)
// TODO exit condition when the list is empty only use ID and Chromosome. Not for Source, Type and Attributes.
// TODO For region maybe when a feature is not in that region anymore exit condition.
    /**
     * Parses the provided GFF3 file and returns a LinkedList of Feature objects.
     * Each feature is parsed line by line, and parent-child relationships are handled.
     * Features are stored in a LinkedList, and a map is used to store features by their ID for fast lookup.
     *
     * @param options the path   to the GFF3 file to be parsed.
     */
    public static void gffParser(OptionsProcessor options) {
        try (BufferedReader reader = Files.newBufferedReader(options.getInputGffFile())) {
            String line;

            for (Map.Entry<String, List<String>> entry : options.getListFilter().entrySet()) {
                // Process each line of the GFF3 file
                while ((line = reader.readLine()) != null) {
                    boolean filter;

                    if (line.startsWith("#")) {
                        ReturnFile.writeHeader(line, options);  // Add header to the list
                    } else {
                        Feature feature = parseLine(line);
                        filter = GFFFeatureFunctions.filteringLine(feature, entry.getKey(), entry.getValue(), options.isDelete(), options.getContains());

                        if (filter) {
                            ReturnFile.chooseTypeFile(feature, options);
                        } // else keep going
                    }
                }
            }
        } catch (IOException ex) {
            ErrorThrower.throwError(ex);
        }
        logger.info("done parsing and writing");
    }

    /**
     * Processes a single line from the GFF3 file, creating a Feature object from the line data,
     * and adding it to the linked list. It also handles parent-child relationships between features.
     *
     * @param line the line from the GFF3 file to be processed.
     */
    private static Feature parseLine(String line) {
        String[] columns = line.split("\t"); // Parse the feature details from the columns
        String seqID = columns[0];
        String source = columns[1];
        String type = columns[2];
        int start = Integer.parseInt(columns[3]);
        int end = Integer.parseInt(columns[4]);
        String score = columns[5];
        String strand = columns[6];
        String phase = columns[7];
        String[] attrPairs = columns[8].split(";");
        Map<String, String> attributes = GffParser.parseAttributes(attrPairs);

        return new Feature(seqID, source, type, start, end, score, strand, phase, attributes);
    }
}

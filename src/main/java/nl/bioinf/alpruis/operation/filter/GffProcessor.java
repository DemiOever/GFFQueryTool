package nl.bioinf.alpruis.operation.filter;

import nl.bioinf.alpruis.Feature;
import nl.bioinf.alpruis.OptionsProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static nl.bioinf.alpruis.Main.logger;

/**
 * The GffParser class is responsible for parsing GFF3 files, creating Feature objects,
 * and storing them in a linked list. It processes each line of the GFF3 file, handling
 * features and their parent-child relationships.
 */
public class GffProcessor {

    /**
     * Parses the provided GFF3 file and returns a LinkedList of Feature objects.
     * Each feature is parsed line by line, and parent-child relationships are handled.
     * Features are stored in a LinkedList, and a map is used to store features by their ID for fast lookup.
     *
     * @param inputGffFile the path to the GFF3 file to be parsed.
     * @return a LinkedList of Feature objects representing the parsed GFF3 data.
     */
    public static void gffParser(OptionsProcessor options) {
        List<String> headers = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(options.getInputGffFile())) {
            String line;
            int count = 0;
            for (Map.Entry<String, List<String>> entry : options.getListFilter().entrySet()) {
                // Process each line of the GFF3 file
                while ((line = reader.readLine()) != null) {
                    boolean filter = false;
                    if (line.startsWith("#")) {
                        headers.add(line);  // Add header to the list
                    } else {
                        Feature feature = parseLine(line);
                        filter = GFFFeatureFunctions.filteringLine(feature, entry.getKey(), entry.getValue(), options.isDelete());
                        if (filter) {
                            ReturnFile.chooseTypeFile(feature, options);
                        } // else keep going
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("done parsing and writing");
    }

    /**
     * Processes a single line from the GFF3 file, creating a Feature object from the line data,
     * and adding it to the linked list. It also handles parent-child relationships between features.
     *
     * @param line the line from the GFF3 file to be processed.
     * @param headers the List that stores all parsed Feature objects.
     * @param map a map to store features by their ID for efficient lookup.
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
        Map<String, String> attributes = parseAttributes(attrPairs);

        Feature feature = new Feature(seqID, source, type, start, end, score, strand, phase, attributes);
        return feature;
    }

    /**
     * Parses the attribute column of a GFF3 file, converting it into a map of key-value pairs.
     * The attribute column contains key-value pairs separated by semicolons (";"), and each
     * key-value pair is separated by an equals sign ("=").
     *
     * @param attrPairs an array of attribute strings, each representing a key-value pair.
     * @return a map of attributes as key-value pairs.
     */
    static Map<String, String> parseAttributes(String[] attrPairs) {
        Map<String, String> attributes = new HashMap<>();
        for (String attr : attrPairs) {
            String[] keyValue = attr.split("=");
            if (keyValue.length == 2) {
                attributes.put(keyValue[0].trim(), keyValue[1].trim());
            } else if (keyValue.length >= 3) {
                logger.error("The attribute ({}) cannot be parsed correctly because there are one or more extra '=' signs.", attr);
            } else {
                logger.error("The attribute ({}) cannot be parsed correctly because there is no '=' sign in the attribute.", attr);
            }
        }
        return attributes;
    }
}

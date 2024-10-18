package nl.bioinf.alpruis.operation.filter_ex_sum;

import nl.bioinf.alpruis.ErrorThrower;
import nl.bioinf.alpruis.Feature;

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
public class GffParser {
// TODO if the list given is used all of them like with ID then stop parsing and if its not empty at the end of parsing then give back with left over
    /**
     * Parses the provided GFF3 file and returns a LinkedList of Feature objects.
     * Each feature is parsed line by line, and parent-child relationships are handled.
     * Features are stored in a LinkedList, and a map is used to store features by their ID for fast lookup.
     *
     * @param inputGffFile the path to the GFF3 file to be parsed.
     * @return a LinkedList of Feature objects representing the parsed GFF3 data.
     */
    public static LinkedList<Feature> gffParser(Path inputGffFile) {
        LinkedList<Feature> gffFeatures = new LinkedList<>();
        Map<String, Feature> map = new HashMap<>();
        List<String> headers = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(inputGffFile)) {
            String line;

            // Process each line of the GFF3 file
            while ((line = reader.readLine()) != null) {
                processLine(line, gffFeatures, map, headers);
            }
        } catch (IOException e) {
            ErrorThrower.throwErrorE(e);
        }
        logger.info("done parsing");
        return gffFeatures;
    }

    /**
     * Processes a single line from the GFF3 file, creating a Feature object from the line data,
     * and adding it to the linked list. It also handles parent-child relationships between features.
     *
     * @param line the line from the GFF3 file to be processed.
     * @param gffFeatures the LinkedList that stores all parsed Feature objects.
     * @param map a map to store features by their ID for efficient lookup.
     */
    private static void processLine(String line, LinkedList<Feature> gffFeatures, Map<String, Feature> map, List<String> headers) {
        if (line.startsWith("#")) {
            headers.add(line);  // Add header to the list
            return;
        }

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
        gffFeatures.add(feature);

        // Store feature if it has an ID
        if (feature.getID() != null) {
            map.put(feature.getID(), feature);
        }

        // Check if the feature has a Parent attribute
        String parentID = feature.getParentID();
        if (parentID != null) {
            if (map.containsKey(parentID)) {
                // Parent exists, add this feature as its child
                map.get(parentID).addChild(feature.getID());
            }
        } else {
            // No explicit parent; check if it belongs to the feature type region based on coordinates
            linkToRegion(feature, gffFeatures);
        }
    }

    /**
     * Links a feature to a region if the feature is within the boundaries of a region in the GFF file.
     * This method searches through the GFF features to find a matching region based on coordinates and sequence ID.
     *
     * @param feature the feature to be linked to a region.
     * @param gffFeatures the LinkedList containing all parsed features, including regions.
     */
    private static void linkToRegion(Feature feature, LinkedList<Feature> gffFeatures) {
        // Search for a region that contains this feature
        for (Feature parentFeature : gffFeatures) {
            if (parentFeature.getType().equals("region") &&
                    parentFeature.getSeqId().equals(feature.getSeqId()) &&
                    parentFeature.getStart() <= feature.getStart() &&
                    parentFeature.getEnd() >= feature.getEnd() &&
                    !parentFeature.getID().equals(feature.getID()) &&
                    feature.getID().startsWith("gene")) {
                // Link feature to the region as a child
                parentFeature.addChild(feature.getID());
                break;
            }
        }
    }

    /**
     * Parses the attribute column of a GFF3 file, converting it into a map of key-value pairs.
     * The attribute column contains key-value pairs separated by semicolons (";"), and each
     * key-value pair is separated by an equals sign ("=").
     *
     * @param attrPairs an array of attribute strings, each representing a key-value pair.
     * @return a map of attributes as key-value pairs.
     */
    public static Map<String, String> parseAttributes(String[] attrPairs) {
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

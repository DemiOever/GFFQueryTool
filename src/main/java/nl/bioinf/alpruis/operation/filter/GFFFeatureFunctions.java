package nl.bioinf.alpruis.operation.filter;

import nl.bioinf.alpruis.Feature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * This class provides various functions to manipulate and filter GFF features, including deleting
 * and fetching based on different criteria such as attributes, IDs, types, regions, chromosomes, and sources.
 */
public class GFFFeatureFunctions {
    private static final Logger logger = LogManager.getLogger(GFFFeatureFunctions.class.getName());

    private static Map<String, List<String>> parseList(List<String> listInput) {
        Map<String, List<String>> mapInput = new LinkedHashMap<>();

        for (String attr : listInput) {
            int equalIndex = attr.indexOf("=");
            if (equalIndex > 0) {
                String key = attr.substring(0, equalIndex).trim();
                String value = attr.substring(equalIndex + 1).trim();
                List<String> valuesList = Arrays.asList(value.split(","));  // Split the value into a list
                mapInput.put(key, valuesList);
            } else {
                logger.error("The attribute ({}) cannot be parsed correctly because there is no '=' sign in the attribute.", attr);
            }
        }
        return mapInput;
    }

    private static boolean filterLine(String filter, List<String> listInput, boolean delete, boolean useContains) {
        boolean contains = false;
        if (useContains) {
            for (String attr : listInput) {
                contains = filter.contains(attr) != delete;
                if (contains) {
                    break;
                }
            }
        } else {
            if (delete) {
                return !listInput.contains(filter);
            } else {
                return listInput.contains(filter);
            }
        }
        return contains;
    }

    private static boolean filterRegion(Feature feature, List<String> listInput, boolean delete, boolean useContains) {
        for (int i = 0; i < listInput.size(); i += 2) {
            int regionStart = Integer.parseInt(listInput.get(i));
            int regionEnd = Integer.parseInt(listInput.get(i + 1));

            // Check if feature partially or fully overlaps with the region
            boolean overlaps = feature.getStart() <= regionEnd && feature.getEnd() >= regionStart;
            boolean inside = regionStart < feature.getStart() && regionEnd > feature.getEnd();

            // If the region contains the feature (or overlaps), act based on the delete flag
            if (useContains && overlaps) {
                return !delete;
            } else if (!useContains && inside) {
                return !delete;
            }
        }
        return delete; // If no overlap is found and we are deleting, return true
    }


    public static boolean filteringLine(Feature feature, String column, List<String> inputValues, boolean delete, boolean useContains) {
        return switch (column) {
            case "ID" -> filterLine(feature.getID(), inputValues, delete, useContains);
            case "TYPE" -> filterLine(feature.getType(), inputValues, delete, useContains);
            case "CHROMOSOME" -> filterLine(feature.getSeqId(), inputValues, delete, useContains);
            case "REGION" -> filterRegion(feature, inputValues, delete, useContains);
            case "ATTRIBUTES" -> filterAttributes(feature.getAttributes(), inputValues, delete, useContains);
            case "SOURCE" -> filterLine(feature.getSource(), inputValues, delete, useContains);
            default -> false;
        };
    }

    /**
     * Deletes features from the list that match specific attribute key-value pairs.
     *
     * @param featureAttributes  LinkedList of GFF features to be filtered.
     * @param listInput Map containing attribute key-value pairs to match for deletion.
     * @return The filtered LinkedList of GFF features.
     */
    private static boolean filterAttributes(Map<String, String> featureAttributes, List<String> listInput, boolean delete, boolean useContains) {
        Map<String, List<String>> mapInput = parseList(listInput);  // Parse the input into key -> list of values
        boolean foundMatch = false;  // Track if we found any match across all entries

        for (Map.Entry<String, List<String>> entry : mapInput.entrySet()) {
            String key = entry.getKey();  // e.g., ID
            List<String> inputValues = entry.getValue();  // List of values e.g., ["NC_000067.7:1..195154279", "id-GeneID:131295014"]

            // Check if the feature has the attribute and if it matches any of the provided values
            if (featureAttributes.containsKey(key)) {
                String featureValue = featureAttributes.get(key);

                // Check if this feature's value matches any of the input values
                for (String value : inputValues) {
                    if (useContains ? featureValue.contains(value) : featureValue.equals(value)) {
                        foundMatch = true;  // A match is found, but we don't exit yet
                        break;  // Exit inner loop if one match for this key-value pair is found
                    }
                }
            }
        }

        // Now decide based on whether we're deleting or fetching
        if (delete) {
            // If deleting: return false if any match is found (i.e., we want to delete matched items)
            return !foundMatch;  // Invert the match flag for deletion
        } else {
            // If fetching: return true only if any match is found (i.e., we want to keep matched items)
            return foundMatch;
        }
    }
}
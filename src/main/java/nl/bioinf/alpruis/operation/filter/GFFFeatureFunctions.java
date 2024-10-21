package nl.bioinf.alpruis.operation.filter;

import nl.bioinf.alpruis.Feature;

import java.util.*;

import static nl.bioinf.alpruis.Main.logger;
/**
 * This class provides various functions to manipulate and filter GFF features, including deleting
 * and fetching based on different criteria such as attributes, IDs, types, regions, chromosomes, and sources.
 */
public class GFFFeatureFunctions {
    private static Map<String, List<String>> parseList(List<String> listInput) {
        Map<String, List<String>> mapInput = new HashMap<>();

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

    public static boolean filterLine(String filter, List<String> listInput, boolean delete) {
        if (delete) {
            return !listInput.contains(filter);
        } else {
            return listInput.contains(filter);
        }
    }

    public static boolean filterRegion(Feature feature, List<String> listInput, boolean delete) {

        for (int i = 0; i < listInput.size(); i += 2) {
            int regionStart = Integer.parseInt(listInput.get(i));
            int regionEnd = Integer.parseInt(listInput.get(i + 1));
            if (regionStart < feature.getStart() && regionEnd > feature.getEnd()) {
                return true;
            }
        }
        return false;
    }

    public static boolean filteringLine(Feature feature, String column, List<String> inputValues, boolean delete, boolean useContains) {
        return switch (column) {
            case "ID" -> filterLine(feature.getID(), inputValues, delete);
            case "Type" -> filterLine(feature.getType(), inputValues, delete);
            case "Chromosome" -> filterChromosome(feature.getSeqId(), inputValues, delete);
            case "Region" -> filterRegion(feature, inputValues, delete);
            case "Attributes" -> filterAttributes(feature.getAttributes(), inputValues, delete, useContains);
            case "Source" -> filterLine(feature.getSource(), inputValues, delete);
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
    public static boolean filterAttributes(Map<String, String> featureAttributes, List<String> listInput, boolean delete, boolean useContains) {
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



    private static boolean filterChromosome(String chromosome, List<String> listInput, boolean delete) {
        // Check if the feature's chromosome matches any in the input list
        boolean matches = listInput.contains(chromosome);

        // Return based on whether delete is true or false
        return delete != matches;
    }
}
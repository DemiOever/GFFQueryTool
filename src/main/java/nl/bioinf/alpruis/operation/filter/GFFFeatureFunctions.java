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

            if (delete) {
                if (feature.getStart() <= regionStart && feature.getEnd() >= regionEnd) {
                    return false;
                }
            } else {
                if (feature.getStart() < regionStart || feature.getEnd() > regionEnd) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean filteringLine(Feature feature, String column, List<String> inputValues, boolean delete) {
        return switch (column) {
            case "ID" -> filterLine(feature.getID(), inputValues, delete);
            case "Type" -> filterLine(feature.getType(), inputValues, delete);
            case "Chromosome" -> filterLine(feature.getChromosome(), inputValues, delete);
            case "Region" -> filterRegion(feature, inputValues, delete);
            case "Attributes" -> filterAttributes(feature, inputValues, delete);
            case "Source" -> filterLine(feature.getSource(), inputValues, delete);
            default -> false;
        };
    }

    /**
     * Deletes features from the list that match specific attribute key-value pairs.
     *
     * @param feature  LinkedList of GFF features to be filtered.
     * @param listInput Map containing attribute key-value pairs to match for deletion.
     * @return The filtered LinkedList of GFF features.
     */
    public static boolean filterAttributes(Feature feature, List<String> listInput, boolean delete) {
        Map<String, List<String>> mapInput = parseList(listInput);  // Parse the input into key -> list of values
        Map<String, String> featureAttributes = feature.getAttributes();

        for (Map.Entry<String, List<String>> entry : mapInput.entrySet()) {
            String key = entry.getKey();  // e.g., ID
            List<String> inputValues = entry.getValue();  // List of values e.g., ["NC_000067.7:1..195154279", "id-GeneID:131295014"]

            // Check if the feature has the attribute and if it matches any of the provided values
            if (featureAttributes.containsKey(key)) {
                String featureValue = featureAttributes.get(key);

                boolean matched = false;
                for (String value : inputValues) {
                    if (featureValue.contains(value)) {  // Use contains for partial matching
                        // TODO make option for contains or equals
                        matched = true;
                        break;  // If any value matches, we stop checking further
                    }
                }

                // If deleting, return false if there's a match; if fetching, return false if there's no match
                if (delete && matched) {
                    return false; // Mark for deletion if matches
                } else if (!delete && !matched) {
                    return false; // Exclude from result if none of the values match
                }
            } else if (!delete) {
                return false; // Exclude from result if attribute doesn't exist and we're fetching
            }
        }
        return true; // Feature passes the filter
    }
}
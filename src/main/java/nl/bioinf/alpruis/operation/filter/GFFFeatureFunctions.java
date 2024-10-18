package nl.bioinf.alpruis.operation.filter;

import nl.bioinf.alpruis.Feature;

import java.util.*;

import static nl.bioinf.alpruis.Main.logger;
// TODO logger.warn for when the given list with options includes one that simply doesn't exist (lvl 1)
// TODO rewrite in a more efficient way for less methods and less code repeating (lvl 2)
/**
 * This class provides various functions to manipulate and filter GFF features, including deleting
 * and fetching based on different criteria such as attributes, IDs, types, regions, chromosomes, and sources.
 */
public class GFFFeatureFunctions {
    private static Map<String, String> parseList(List<String> listInput) {
        Map<String, String> mapInput = new HashMap<String, String>();
        for (String attr : listInput) {
            String[] keyValue = attr.split("=");
            if (keyValue.length == 2) {
                mapInput.put(keyValue[0].trim(), keyValue[1].trim());
            } else if (keyValue.length >= 3) {
                logger.error("The attribute ({}) cannot be parsed correctly because there are one or more extra '=' signs.", attr);
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
        if (column.equals("ID")) {
            return filterLine(feature.getID(), inputValues, delete);
        } else if (column.equals("Type")) {
            return filterLine(feature.getType(), inputValues, delete);
        } else if (column.equals("Chromosome")) {
            return filterLine(feature.getChromosome(), inputValues, delete);
        } else if (column.equals("Region")) {
            return filterRegion(feature, inputValues, delete);
        } else if (column.equals("Attributes")) {
            return filterAttributes(feature, inputValues, delete);
        } else if (column.equals("Source")) {
            return filterLine(feature.getSource(), inputValues, delete);
        }
        return false;
    }

    /**
     * Deletes features from the list that match specific attribute key-value pairs.
     *
     * @param feature  LinkedList of GFF features to be filtered.
     * @param listInput Map containing attribute key-value pairs to match for deletion.
     * @return The filtered LinkedList of GFF features.
     */
    public static boolean filterAttributes(Feature feature, List<String> listInput, boolean delete) {
        Map<String, String> mapInput = parseList(listInput);
        Map<String, String> featureAttributes = feature.getAttributes();

        for (Map.Entry<String, String> entry : mapInput.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            // Check if the feature has the attribute and if it matches the provided value
            if (featureAttributes.containsKey(key)) {
                boolean match = featureAttributes.get(key).matches(value);
                if (delete && match) {
                    return false; // Mark for deletion if matches
                } else if (!delete && !match) {
                    return false; // Exclude from result if it doesn't match
                }
            } else if (!delete) {
                return false; // Exclude from result if attribute doesn't exist and we're fetching
            }
        }
        return true; // Feature passes the filter
    }
}
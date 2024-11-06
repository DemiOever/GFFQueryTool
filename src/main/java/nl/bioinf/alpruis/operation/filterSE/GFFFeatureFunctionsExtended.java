package nl.bioinf.alpruis.operation.filterSE;

import nl.bioinf.alpruis.Feature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
// TODO logger.warn for when the given list with options includes one that simply doesn't exist (lvl 1)
// TODO rewrite in a more efficient way for less methods and less code repeating (lvl 2)
/**
 * This class provides various functions to manipulate and filter GFF features, including deleting
 * and fetching based on different criteria such as attributes, IDs, types, regions, chromosomes, and sources.
 */
public class GFFFeatureFunctionsExtended {
    private static final Logger logger = LogManager.getLogger(GFFFeatureFunctionsExtended.class.getName());
    /**
     * Deletes features from the list that match specific attribute key-value pairs.
     *
     * @param gffFeatures_del LinkedList of GFF features to be filtered.
     * @param mapInput Map containing attribute key-value pairs to match for deletion.
     * @return The filtered LinkedList of GFF features.
     */
    // TODO update the code and make it for the extended option and otherwise a if and else in the other filter file
    public static LinkedList<Feature> deleteAttributes(LinkedList<Feature> gffFeatures_del, Map<String, String> mapInput) {
        Iterator<Feature> iterator = gffFeatures_del.iterator();

        while (iterator.hasNext()) {
            Feature feature = iterator.next();
            Map<String, String> featureAttributes = feature.getAttributes();

            boolean shouldRemove = false;

            for (Map.Entry<String, String> entry : mapInput.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (featureAttributes.containsKey(key) && featureAttributes.get(key).matches(value)) {
                    shouldRemove = true;
                }
            }

            if (shouldRemove) {
                iterator.remove();
            }
        }
        return gffFeatures_del;
    }

    /**
     * Deletes features from the list that match specific IDs.
     *
     * @param gffFeatures_del LinkedList of GFF features to be filtered.
     * @param listInput List of IDs to match for deletion.
     * @return The filtered LinkedList of GFF features.
     */
    public static LinkedList<Feature> deleteId(LinkedList<Feature> gffFeatures_del, List<String> listInput) {
        gffFeatures_del.removeIf(feature -> listInput.contains(feature.getID()));
        return gffFeatures_del;
    }

    /**
     * Deletes features from the list that match specific types.
     *
     * @param gffFeatures_del LinkedList of GFF features to be filtered.
     * @param listInput List of types to match for deletion.
     * @return The filtered LinkedList of GFF features.
     */
    public static LinkedList<Feature> deleteType(LinkedList<Feature> gffFeatures_del, List<String> listInput) {
        gffFeatures_del.removeIf(feature -> listInput.contains(feature.getType()));
        return gffFeatures_del;
    }

    /**
     * Deletes features from the list that fall outside the specified regions.
     *
     * @param gffFeatures_del LinkedList of GFF features to be filtered.
     * @param listInput List of start and end positions defining the regions.
     * @return The filtered LinkedList of GFF features.
     */
    public static LinkedList<Feature> deleteRegion(LinkedList<Feature> gffFeatures_del, List<Integer> listInput) {
        for (int i = 0; i < listInput.size(); i += 2) {
            int regionStart = listInput.get(i);
            int regionEnd = listInput.get(i + 1);

            gffFeatures_del.removeIf(feature -> feature.getStart() > regionStart || feature.getEnd() < regionEnd);
        }
        return gffFeatures_del;
    }

    /**
     * Deletes features from the list that match specific chromosomes.
     *
     * @param gffFeatures_del LinkedList of GFF features to be filtered.
     * @param listInput List of chromosomes to match for deletion.
     * @return The filtered LinkedList of GFF features.
     */
    public static LinkedList<Feature> deleteChromosome(LinkedList<Feature> gffFeatures_del, List<String> listInput) {
        gffFeatures_del.removeIf(feature -> listInput.contains(feature.getSeqId()));
        return gffFeatures_del;
    }

    /**
     * Deletes features from the list that match specific sources.
     *
     * @param gffFeatures_del LinkedList of GFF features to be filtered.
     * @param listInput List of sources to match for deletion.
     * @return The filtered LinkedList of GFF features.
     */
    public static LinkedList<Feature> deleteSource(LinkedList<Feature> gffFeatures_del, List<String> listInput) {
        gffFeatures_del.removeIf(feature -> listInput.contains(feature.getSource()));
        return gffFeatures_del;
    }

    /**
     * Fetches features from the list that match specific attribute key-value pairs.
     *
     * @param gffFeatures_fetch LinkedList of GFF features to be filtered.
     * @param mapInput Map containing attribute key-value pairs to match for fetching.
     * @return The filtered LinkedList of GFF features.
     */
    public static LinkedList<Feature> FetchAttributes(LinkedList<Feature> gffFeatures_fetch, Map<String, String> mapInput) {
        Iterator<Feature> iterator = gffFeatures_fetch.iterator();

        while (iterator.hasNext()) {
            Feature feature = iterator.next();
            Map<String, String> featureAttributes = feature.getAttributes();

            boolean shouldRemove = false;

            for (Map.Entry<String, String> entry : mapInput.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (!featureAttributes.containsKey(key) && !featureAttributes.get(key).matches(value)) {
                    shouldRemove = true;
                }
            }

            if (shouldRemove) {
                iterator.remove();
            }
        }
        return gffFeatures_fetch;
    }

    /**
     * Fetches features from the list that match specific IDs.
     *
     * @param gffFeatures_fetch LinkedList of GFF features to be filtered.
     * @param listInput List of IDs to match for fetching.
     * @return The filtered LinkedList of GFF features.
     */
    public static LinkedList<Feature> fetchId(LinkedList<Feature> gffFeatures_fetch, List<String> listInput) {
        gffFeatures_fetch.removeIf(feature -> !listInput.contains(feature.getID()));
        return gffFeatures_fetch;
    }

    /**
     * Fetches features from the list that match specific types.
     *
     * @param gffFeatures_fetch LinkedList of GFF features to be filtered.
     * @param listInput List of types to match for fetching.
     * @return The filtered LinkedList of GFF features.
     */
    public static LinkedList<Feature> fetchType(LinkedList<Feature> gffFeatures_fetch, List<String> listInput) {
        gffFeatures_fetch.removeIf(feature -> !listInput.contains(feature.getType()));
        return gffFeatures_fetch;
    }

    /**
     * Fetches features from the list that are located within the specified regions.
     *
     * @param gffFeatures_fetch LinkedList of GFF features to be filtered.
     * @param listInput List of start and end positions defining the regions.
     * @return The filtered LinkedList of GFF features.
     */
    public static LinkedList<Feature> fetchRegion(LinkedList<Feature> gffFeatures_fetch, List<Integer> listInput) {
        for (int i = 0; i < listInput.size(); i += 2) {
            int regionStart = listInput.get(i);
            int regionEnd = listInput.get(i + 1);

            gffFeatures_fetch.removeIf(feature -> feature.getStart() < regionStart || feature.getEnd() > regionEnd);
        }
        return gffFeatures_fetch;
    }

    /**
     * Fetches features from the list that match specific chromosomes.
     *
     * @param gffFeatures_fetch LinkedList of GFF features to be filtered.
     * @param listInput List of chromosomes to match for fetching.
     * @return The filtered LinkedList of GFF features.
     */
    public static LinkedList<Feature> fetchChromosome(LinkedList<Feature> gffFeatures_fetch, List<String> listInput) {
        gffFeatures_fetch.removeIf(feature -> !listInput.contains(feature.getSeqId()));
        return gffFeatures_fetch;
    }

    /**
     * Fetches features from the list that match specific sources.
     *
     * @param gffFeatures_fetch LinkedList of GFF features to be filtered.
     * @param listInput List of sources to match for fetching.
     * @return The filtered LinkedList of GFF features.
     */
    public static LinkedList<Feature> fetchSource(LinkedList<Feature> gffFeatures_fetch, List<String> listInput) {
        gffFeatures_fetch.removeIf(feature -> !listInput.contains(feature.getSource()));
        return gffFeatures_fetch;
    }

    private static boolean filterLine(String filter, List<String> listInput, boolean extended) {
        if (extended) {
            return !listInput.contains(filter);
        } else {
            return listInput.contains(filter);
        }
    }

    private static boolean filterRegion(Feature feature, List<String> listInput, boolean extended) {
        for (int i = 0; i < listInput.size(); i += 2) {
            int regionStart = Integer.parseInt(listInput.get(i));
            int regionEnd = Integer.parseInt(listInput.get(i + 1));
            if (regionStart < feature.getStart() && regionEnd > feature.getEnd()) {
                return !extended;
            }
        }
        return extended;
    }

    private static boolean filteringLine(Feature feature, String column, List<String> inputValues, boolean extended, boolean useContains) {
        return switch (column) {
            case "ID" -> filterLine(feature.getID(), inputValues, extended);
            case "Type" -> filterLine(feature.getType(), inputValues, extended);
            //case "Chromosome" -> filterChromosome(feature.getSeqId(), inputValues, delete);
            case "Region" -> filterRegion(feature, inputValues, extended);
            //case "Attributes" -> filterAttributes(feature.getAttributes(), inputValues, delete, useContains);
            case "Source" -> filterLine(feature.getSource(), inputValues, extended);
            default -> false;
        };
    }
}

package nl.bioinf.alpruis.operation.filter_ex_sum;

import nl.bioinf.alpruis.Feature;

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
public class GFFFeatureFunctionsEXS {
    /**
     * Deletes features from the list that match specific attribute key-value pairs.
     *
     * @param gffFeatures_del LinkedList of GFF features to be filtered.
     * @param mapInput Map containing attribute key-value pairs to match for deletion.
     * @return The filtered LinkedList of GFF features.
     */
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
     * Deletes features from the list that fall outside of the specified regions.
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
        gffFeatures_del.removeIf(feature -> listInput.contains(feature.getChromosome()));
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
        gffFeatures_fetch.removeIf(feature -> !listInput.contains(feature.getChromosome()));
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
}

package nl.bioinf.alpruis.operations;
import static nl.bioinf.alpruis.Main.logger;


import nl.bioinf.alpruis.CommandLineParser;
import nl.bioinf.alpruis.Feature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

//TODO can this be better like 1 default method with calls to others (lvl2)
//TODO the method below if it is continued will turn into a long if else statement, can it be better (lvl2)
public class GFFFeatureFunctions {
    private static final Logger logger = LogManager.getLogger(GFFFeatureFunctions.class.getName());

    public static LinkedList<Feature> decideMethod(LinkedList<Feature> gffFeatures, String sequence, String filter, boolean delete, List<String> list_input, boolean extended) {
        //LinkedList<Feature> gffFeatures_del1 = GFFFeatureFunctions.attributes(gffFeatures_del);
        if (delete) {
            if (extended) {
                logger.info("You chose to delete: {}. And chose to add the children and parent.", list_input);
                logger.info(list_input);

            } else {
                logger.info("You chose to delete: {}. ", list_input);
                deleteId(gffFeatures, list_input);
            }
        }
        else {
            if (extended) {
                logger.info("You chose to select: {}. And chose to not add the children and parent.", list_input);
            } else {
                logger.info("You chose to select: {}. ", list_input);
                fetchId(gffFeatures, list_input);
            }
        }
        return gffFeatures;
    }
//delete
    public static LinkedList<Feature> deleteAttributes(LinkedList<Feature> gffFeatures_del, Map<String, String> listInput) {
    // Use an iterator to safely remove elements during iteration
    Iterator<Feature> iterator = gffFeatures_del.iterator();

    while (iterator.hasNext()) {
        Feature feature = iterator.next();
        Map<String, String> featureAttributes = feature.getAttributes(); // Assuming this returns the feature's attributes map

        boolean shouldRemove = false;

        // Iterate over the provided attribute filter map (listInput)
        for (Map.Entry<String, String> entry : listInput.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            // Check if the feature contains the attribute and if its value matches
            if (featureAttributes.containsKey(key) && featureAttributes.get(key).matches(value)) {
                shouldRemove = true; // Mark for removal if it matches
            }
        }

        // Remove the feature if a matching attribute was found
        if (shouldRemove) {
            iterator.remove();
        }
    }
        return gffFeatures_del;

    }

    public static LinkedList<Feature> deleteId(LinkedList<Feature> gffFeatures_del, List<String> listInput) {
        // going into hashmap getting all the features where the sequence_id is the same as id given
        gffFeatures_del.removeIf(feature -> listInput.contains(feature.getID()));
        return gffFeatures_del;
    }

    public static LinkedList<Feature> deleteType(LinkedList<Feature> gffFeatures_del, List<String> listInput) {
        // going into hashmap getting all the features where the type is the same as the type given
        gffFeatures_del.removeIf(feature -> listInput.contains(feature.getType()));
        return gffFeatures_del;
    }

    public static LinkedList<Feature> deleteRegion(LinkedList<Feature> gffFeatures_del, List<Integer> listInput) {

        for (int i = 0; i < listInput.size(); i += 2) {
            int regionStart = listInput.get(i);    // Start of the region
            int regionEnd = listInput.get(i + 1);  // End of the region

            gffFeatures_del.removeIf(feature -> feature.getStart() > regionStart || feature.getEnd() < regionEnd);
        }
        return gffFeatures_del;
    }


    public static LinkedList<Feature> deleteChromosome(LinkedList<Feature> gffFeatures_del, List<String> listInput) {
        gffFeatures_del.removeIf(feature -> listInput.contains(feature.getChromosome()));
        return gffFeatures_del;
    }
    public static LinkedList<Feature> deleteSource(LinkedList<Feature> gffFeatures_del, List<String> listInput) {
        gffFeatures_del.removeIf(feature -> listInput.contains(feature.getSource()));
        return gffFeatures_del;
    }
// fetch
    public static LinkedList<Feature> FetchAttributes(LinkedList<Feature> gffFeatures_fetch, Map<String,String> mapInput) { //TODO check this method(lvl 1)
        Iterator<Feature> iterator = gffFeatures_fetch.iterator();

        while (iterator.hasNext()) {
            Feature feature = iterator.next();
            Map<String, String> featureAttributes = feature.getAttributes(); // Assuming this returns the feature's attributes map

            boolean shouldRemove = false;

            // Iterate over the provided attribute filter map (listInput)
            for (Map.Entry<String, String> entry : mapInput.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                // Check if the feature contains the attribute and if its value matches
                if (!featureAttributes.containsKey(key) && !featureAttributes.get(key).matches(value)) {
                    shouldRemove = true; // Mark for removal if it matches
                }
            }

            // Remove the feature if a matching attribute was found
            if (shouldRemove) {
                iterator.remove();
            }
        }
        return gffFeatures_fetch;

    }

    public static LinkedList<Feature> fetchId(LinkedList<Feature> gffFeatures_fetch, List<String> listInput) {//TODO check this method(lvl 1)
        // going into hashmap getting all the features where the sequence_id is the same as id given
        gffFeatures_fetch.removeIf(feature -> !listInput.contains(feature.getID()));
        return gffFeatures_fetch;
    }

    public static LinkedList<Feature> fetchType(LinkedList<Feature> gffFeatures_fetch, List<String> listInput) {//TODO check this method(lvl 1)
        // going into hashmap getting all the features where the type is the same as the type given
        gffFeatures_fetch.removeIf(feature -> !listInput.contains(feature.getType()));
        return gffFeatures_fetch;
    }

    public static LinkedList<Feature> fetchRegion(LinkedList<Feature> gffFeatures_fetch, List<Integer> listInput) {//TODO check this method(lvl 1)
        // going into hashmap getting all the features where start to stop are between the beginning and end of the region
        for (int i = 0; i < listInput.size(); i += 2) {
            int regionStart = listInput.get(i);    // Start of the region
            int regionEnd = listInput.get(i + 1);  // End of the region

            gffFeatures_fetch.removeIf(feature -> feature.getStart() < regionStart || feature.getEnd() > regionEnd);
        }
        return gffFeatures_fetch;
    }

    public static LinkedList<Feature> fetchChromosome(LinkedList<Feature> gffFeatures_fetch, List<String> listInput) {//TODO check this method(lvl 1)
        gffFeatures_fetch.removeIf(feature -> !listInput.contains(feature.getChromosome()));
        return gffFeatures_fetch;
    }
    public static LinkedList<Feature> fetchSource(LinkedList<Feature> gffFeatures_fetch, List<String> listInput) {//TODO check this method(lvl 1)
        gffFeatures_fetch.removeIf(feature -> !listInput.contains(feature.getSource()));
                //contains(feature.getSource()));
        return gffFeatures_fetch;
    }

/*    public static LinkedList<Feature> fetch(LinkedList<Feature> gffFeatures_del, String filter, List<String> input) {
        // i have a filter and a list of features in those feature have a part for where filter goes to filter i need to see if that is the input list

        for (Feature feature : gffFeatures_del) {
            if (*//*feature.getFilter() in input*//*) {
                continue;
            }
            else {
                //delete
            }
        }
        return gffFeatures_del;
    }

    public static LinkedList<Feature> delete(LinkedList<Feature> gffFeatures_del, String filter, List<String> input) {
        // i have a filter and a list of features in those feature have a part for where filter goes to filter i need to see if that is the input list

        for (Feature feature : gffFeatures_del) {
            if (*//*feature.getFilter() in input*//*) {
                //delete
            }
            else {
                //continue
            }
        }
        return gffFeatures_del;
    }*/

}

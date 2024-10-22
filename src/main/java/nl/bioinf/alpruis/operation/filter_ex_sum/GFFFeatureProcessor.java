package nl.bioinf.alpruis.operation.filter_ex_sum;

import nl.bioinf.alpruis.Feature;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class GFFFeatureProcessor {

    /**
     * Processes a GFF file and writes the target feature and its children to an output file.
     *
     * @param gffFeatures the LinkedList that stores all parsed Feature objects..
     * @param featureID    the ID of the feature.
     * @param outputFile   path to output file.
     * @throws IOException iff an error occurs while it is reading and/or writing files.
     */
    public void processGFFFile(LinkedList<Feature> gffFeatures, String featureID, Path outputFile) throws IOException {
        List<Feature> gffeatures = gffFeatures;

        Extended manager = new Extended();
        manager.buildFeatureMap(gffeatures);

        // Get the target feature and its children
        List<String> result = new LinkedList<>();
        String targetFeatureString = manager.getFeatureString(featureID);

        if (targetFeatureString != null) {
            result.add(targetFeatureString);
            // Add the children of the feature to the result
            List<String> childFeatures = manager.getChildren(featureID);
            result.addAll(childFeatures);
        } else {
            System.err.println("Feature ID not found: " + featureID); // TODO make logger
            System.exit(1);
        }


        Map<String, String> seqMap = new HashMap<>();
        ReturnFileExtended.chooseTypeFile(outputFile, result, seqMap);
    }
}


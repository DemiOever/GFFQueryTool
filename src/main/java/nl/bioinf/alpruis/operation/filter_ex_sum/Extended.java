package nl.bioinf.alpruis.operation.filter_ex_sum;

import nl.bioinf.alpruis.Feature;

import java.util.*;

public class Extended {
    private Map<String, String> featureMap;
    private Map<String, List<String>> parentChildMap;

    public Extended() {
        featureMap = new HashMap<>();
        parentChildMap = new HashMap<>();
    }

    /**
     * Builds the featureMap by storing the full feature string for each feature.
     *
     * @param features is a list of features from a GFF file.
     */
    public void buildFeatureMap(List<Feature> features) {
        for (Feature feature : features) {
            String featureID = feature.getID();
            String featureString = feature.toString();

            if (featureID != null && !featureID.isEmpty() && featureString != null) {
                featureMap.put(featureID, featureString);
            }
        }

        for (Feature feature : features) {
            String parentID = feature.getParentID();
            String featureID = feature.getID();
            if (parentID != null && featureID != null) {
                parentChildMap.putIfAbsent(parentID, new ArrayList<>());
                parentChildMap.get(parentID).add(featureID);  // Add child ID to the parent
            }
        }
    }

    /**
     * Returns the complete feature string for the feature ID.
     *
     * @param featureID ID of the feature.
     * @return string corresponding to the ID.
     */
    public String getFeatureString(String featureID) {
        return featureMap.get(featureID);
    }

    /**
     * Returns the list of strings with childs for the parent ID(input).
     *
     * @param parentID ID of the parent feature.
     * @return list with List of full feature strings of children.
     */
    public List<String> getChildren(String parentID) {
        List<String> childrenStrings = new ArrayList<>();
        if (parentChildMap.containsKey(parentID)) {
            List<String> childrenIDs = parentChildMap.get(parentID);
            for (String childID : childrenIDs) {
                childrenStrings.add(featureMap.get(childID));
            }
        }
        return childrenStrings;
    }

    /**
     * Filters the list of features based on criteria such as ID, type, chromosome, region, and source.
     *
     * @param filterCriteria criteria like ID, type, chromosome, region, etc.
     * @return filtered list of full feature strings.
     */
    public List<String> filterFeatures(Map<String, List<String>> filterCriteria) {
        List<String> filteredFeatures = new ArrayList<>(featureMap.values());

        if (filterCriteria.containsKey("id")) {
            List<String> ids = filterCriteria.get("id");
            filteredFeatures.removeIf(featureString -> {
                String featureID = extractIDFromString(featureString);
                return !ids.contains(featureID);
            });
        }

        if (filterCriteria.containsKey("type")) {
            List<String> types = filterCriteria.get("type");
            filteredFeatures.removeIf(featureString -> {
                String featureType = extractTypeFromString(featureString);
                return !types.contains(featureType);
            });
        }

        if (filterCriteria.containsKey("chromosome")) {
            List<String> chromosomes = filterCriteria.get("chromosome");
            filteredFeatures.removeIf(featureString -> {
                String featureChromosome = extractChromosomeFromString(featureString);
                return !chromosomes.contains(featureChromosome);
            });
        }

        if (filterCriteria.containsKey("region")) {
            List<String> regions = filterCriteria.get("region");
            for (String region : regions) {
                String[] parts = region.split("-");
                int start = Integer.parseInt(parts[0]);
                int end = Integer.parseInt(parts[1]);
                filteredFeatures.removeIf(featureString -> {
                    int featureStart = extractStartFromString(featureString);
                    int featureEnd = extractEndFromString(featureString);
                    return featureStart < start || featureEnd > end;
                });
            }
        }

        if (filterCriteria.containsKey("source")) {
            List<String> sources = filterCriteria.get("source");
            filteredFeatures.removeIf(featureString -> {
                String featureSource = extractSourceFromString(featureString);
                return !sources.contains(featureSource);
            });
        }
        return filteredFeatures;
    }

    // Methods to extract specific fields from the full feature string (didn't work with the fetches from GFFFeatureFunctionsExtended???)
    private String extractIDFromString(String featureString) {
        return featureString.split("\t")[8].split(";")[0];
        // TODO, this isn't correct yet
    }

    private String extractTypeFromString(String featureString) {
        return featureString.split("\t")[2];
    }

    private String extractChromosomeFromString(String featureString) {
        return featureString.split("\t")[0];
        // TODO, this isn't correct yet
    }

    private int extractStartFromString(String featureString) {
        return Integer.parseInt(featureString.split("\t")[3]);
    }

    private int extractEndFromString(String featureString) {
        return Integer.parseInt(featureString.split("\t")[4]);
    }

    private String extractSourceFromString(String featureString) {
        return featureString.split("\t")[1];
    }
}

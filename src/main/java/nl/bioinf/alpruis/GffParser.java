package nl.bioinf.alpruis;

import static nl.bioinf.alpruis.Main.logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class GffParser {
    public static LinkedList<Feature> gffParser(Path inputGffFile) {
        LinkedList<Feature> gffFeatures = new LinkedList<Feature>();
        Map<String, Feature> map = new HashMap<String, Feature>();
        try (BufferedReader reader = Files.newBufferedReader(inputGffFile)) {
            String line;

            while ((line = reader.readLine()) != null) {
                processLine(line, gffFeatures, map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("done parsing");
        return gffFeatures;
    }

    private static void processLine(String line, LinkedList<Feature> gffFeatures, Map<String, Feature> map) {
        if (line.startsWith("#")) return; //TODO save for when they want a gff3 file back(lvl 1)
        String[] columns = line.split("\t");
        if (columns.length != 9) return; //TODO add a logger warn

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
        String featureID = feature.getID();
        if (featureID != null) {
            map.put(featureID, feature);
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

    private static void linkToRegion(Feature feature, LinkedList<Feature> gffFeatures) {
        // Search for a region that contains this feature
        for (Feature parentFeature : gffFeatures) {
            if (parentFeature.getType().equals("region") &&
                    parentFeature.getSeqId().equals(feature.getSeqId()) &&
                    parentFeature.getStart() <= feature.getStart() &&
                    parentFeature.getEnd() >= feature.getEnd() &&
                    !parentFeature.getID().equals(feature.getID()) &&
                    feature.getID().startsWith("gene") ) {// This feature is within the region's boundaries
                parentFeature.addChild(feature.getID());
                break;
            }
        }
    }


    static Map<String, String> parseAttributes(String[] attrPairs) {
        Map<String, String> attributes = new HashMap<String, String>();
        for (String attr : attrPairs) {
            String[] keyValue = attr.split("=");
            if (keyValue.length == 2) {
                attributes.put(keyValue[0].trim(), keyValue[1].trim());
            }
            else if (keyValue.length >= 3) {
                logger.warn("The attribute({}) can not be parsed correctly, because there are one or more extra = sign(s) in an attribute", attr);
            }
            else {
                logger.warn("The attribute({}) can not be parsed correctly, because there is no = sign in the attribute.", attr);
            }
        }
        return attributes;
    }
}
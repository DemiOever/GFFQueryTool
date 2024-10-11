package nl.bioinf.alpruis.operations;

import nl.bioinf.alpruis.Feature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSummarizer {
    private static final Logger logger = LogManager.getLogger(FileSummarizer.class.getName());

    private static double gettingGcPercentage(String seq) {
        // gc-percentage
        // input: String
        // return: percentage = int
        Pattern pattern = Pattern.compile("[*C|G]");
        Matcher matcher = pattern.matcher(seq);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count / (double) seq.length() * 100;
    }

    public FeatureSummary summarizeFeatures(List<Feature> features, String sequence) {
        Map<String, Integer> countingFeatures = new HashMap<>();
        List<String> regions = new ArrayList<>();

        int countGenes = 0;
        long lengthGenes = 0;
        int forwardStrands = 0;
        int reverseStrands = 0;

        for (Feature feature : features) {
            countingFeatures.put(feature.getType(), countingFeatures.getOrDefault(feature.getType(), 0) + 1);

            if (Objects.equals(feature.getType(), "gene")) {
                countGenes++;
                long length = feature.getEnd() - feature.getStart();
                lengthGenes += length;
            }

            String strand = feature.getStrand();
            if (Objects.equals(strand, "+")) {
                forwardStrands++;
            } else if (Objects.equals(strand, "-")) {
                reverseStrands++;
            } else {
                logger.warn("Unknown stand direction: " + strand);
            }

            if (Objects.equals(feature.getType(), "region")) {
                regions.add(feature.getChromosome());
            }
        }

        long avgLengthGenes = countGenes > 0 ? lengthGenes / countGenes : 0;

        return new FeatureSummary(sequence.length(), gettingGcPercentage(sequence), countingFeatures, regions, countGenes, avgLengthGenes, forwardStrands, reverseStrands);
    }
}

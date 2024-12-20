package nl.bioinf.alpruis.operation.filter;

import nl.bioinf.alpruis.ErrorThrower;
import nl.bioinf.alpruis.Feature;
import nl.bioinf.alpruis.OptionsProcessor;
import nl.bioinf.alpruis.operation.filterSE.GffParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * The GffParser class is responsible for parsing GFF3 files, creating Feature objects,
 * and storing them in a linked list. It processes each line of the GFF3 file, handling
 * features and their parent-child relationships.
 */
public class GffProcessor {
    private static final Logger logger = LogManager.getLogger(GffProcessor.class.getName());

    /**
     * Parses the provided GFF3 file and returns a LinkedList of Feature objects.
     * Each feature is parsed line by line, and parent-child relationships are handled.
     * Features are stored in a LinkedList, and a map is used to store features by their ID for fast lookup.
     *
     * @param options contains the path to the GFF3 file to be parsed.
     */
    public static void gffParser(OptionsProcessor options) {
        try (BufferedReader reader = Files.newBufferedReader(options.getInputGffFile())) {
            String line;
            String filename = options.getOutputFile().getFileName().toString().toLowerCase();
            if (filename.endsWith(".csv")) {
                ReturnFile.writeHeader("sequence_id,source,feature_type,feature_start,feature_end,score,strand,phase,attributes", options);
            } else if (filename.endsWith(".txt")) {
                ReturnFile.writeHeader("Feature{Sequence Id, Source, Feature type, Feature start, Feature end, score, strand, phase, attributes={}", options);
            }

            for (Map.Entry<String, List<String>> entry : options.getListFilter().entrySet()) {
                // Process each line of the GFF3 file
                while ((line = reader.readLine()) != null) {
                    boolean filter;

                    if (line.startsWith("#")) {
                        if (filename.endsWith(".gff")) {
                            ReturnFile.writeHeader(line, options);  // Add header to the list
                        }
                    } else {
                        Feature feature = parseLine(line);
                        filter = GFFFeatureFunctions.filteringLine(feature, entry.getKey(), entry.getValue(), options.isDelete(), options.getContains());

                        if (filter) {
                            ReturnFile.chooseTypeFile(feature, options);
                        } // else keep going
                    }
                }
            }
        } catch (IOException ex) {
            ErrorThrower.throwError(ex);
        }
        logger.info("done parsing and writing");
    }

    /**
     * Processes a single line from the GFF3 file, creating a Feature object from the line data,
     * and adding it to the linked list. It also handles parent-child relationships between features.
     *
     * @param line the line from the GFF3 file to be processed.
     */
    private static Feature parseLine(String line) {
        String[] columns = line.split("\t"); // Parse the feature details from the columns
        String seqID = columns[0];
        String source = columns[1];
        String type = columns[2];
        int start = Integer.parseInt(columns[3]);
        int end = Integer.parseInt(columns[4]);
        String score = columns[5];
        String strand = columns[6];
        String phase = columns[7];
        String[] attrPairs = columns[8].split(";");
        Map<String, String> attributes = GffParser.parseAttributes(attrPairs);

        return new Feature(seqID, source, type, start, end, score, strand, phase, attributes);
    }
}

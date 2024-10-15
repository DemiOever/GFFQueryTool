/**
 * This class is responsible for parsing command-line arguments using Picocli and executing
 * operations related to GFF3 and FASTA file processing. It validates input files, extracts feature sequences,
 * generates summaries, and performs delete or fetch operations on specific elements.
 *
 * <p>
 * Supported operations include file validation, feature extraction by ID, type, region, chromosome,
 * attribute, and source, with output formats like GFF, FASTA, CSV, or TXT.
 * </p>
 */

package nl.bioinf.alpruis;

import static nl.bioinf.alpruis.FileUtils.fileValidator;
import static nl.bioinf.alpruis.Main.logger;

import nl.bioinf.alpruis.operations.FeatureSummary;
import nl.bioinf.alpruis.operations.GFFFeatureFunctions;
import nl.bioinf.alpruis.operations.FileSummarizer;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Command(name = "GffCommandLine", mixinStandardHelpOptions = true, version = "1.0",
        description = "A command-line tool to parse and query GFF3 files.")
public class CommandLineParser implements Runnable {

    // Define input files
    @Parameters(index = "0", description = "The input GFF3 file.")
    private Path inputGffFile;

    @Parameters(index = "1", description = "The input FASTA file.")
    private Path inputFastaFile;

    // Define options
    @Option(names = {"-vf","--validate"}, description = "Validates the GFF3 and FASTA files.")
    private boolean validate;

    @Option(names = {"-o", "--output_file"}, description = "The output file location. If not specified, a default file will be created.")
    private Path output_file;

    @Option(names = {"-sum", "--summary"}, description = "Generates a summary of the file contents including feature types, GC percentage, and more.")
    private boolean summary;

    @Option(names = {"-d","--delete"}, description = "Deletes specified feature(s). If not specified, features will be fetched.")
    private boolean delete;

    @Option(names = {"-e","--extended"}, description = "Includes parent and child features in the results.") // TODO rethink to use might to be too much work (lvl 2)
    private boolean extended; // TODO make this work somehow (lvl 3)

    // Define query options
    @Option(names = {"-i", "--id"}, description = "Fetches or deletes features by ID(s). Use a comma-separated list.", split = ",")
    private List<String> listId;

    @Option(names = {"-t", "--type"}, description = "Fetches or deletes features by type(s). Use a comma-separated list.", split = ",")
    private List<String> listType;

    @Option(names = {"-a", "--attribute"}, description = "Fetches or deletes features by attribute(s). Use a comma-separated list (e.g., name=LOC,id=15).", split = ",")
    private String[] listAttribute;

    @Option(names = {"-r", "--region"}, description = "Fetches or deletes features within the specified regions (start, end). Use a comma-separated list.", split = ",")
    private List<Integer> listRegion;

    @Option(names = {"-c", "--chromosome"}, description = "Fetches or deletes features by chromosome(s). Use a comma-separated list.", split = ",")
    private List<String> listChromosomes;

    @Option(names = {"-s", "--source"}, description = "Fetches or deletes features by source(s). Use a comma-separated list.", split = ",")
    private List<String> listSource;

    //TODO there is a way to make it more efficient and the list of options shorter (lvl2)

    // @Option(names="-j", description = "column name = list with things to fetch or delete")

    /**
     * Executes the command-line options and performs the corresponding file processing tasks such as validation, feature extraction, or summary generation.
     */
    @Override
    public void run() {
        if (validate) {
            validateFiles();
        } else {
            validateFiles(); // Always validate before processing
            processFiles();
        }
    }

    /**
     * Validates GFF3 and FASTA files and logs the result.
     */
    private void validateFiles() {
        logger.info("Validating GFF3 file...");
        boolean gffValid = fileValidator(inputGffFile);

        logger.info("Validating FASTA file...");
        boolean fastaValid = fileValidator(inputFastaFile);

        if (gffValid && fastaValid) {
            logger.info("Both files are valid.");
        } else if (!gffValid) {
            logger.fatal("Invalid GFF3 file.");
            System.exit(1);
        } else if (!fastaValid) {
            logger.fatal("Invalid FASTA file.");
            System.exit(1);
        } else {
            logger.info("Both files are invalid.");
            System.exit(1);
        }
    }

    /**
     * Processes GFF3 and FASTA files based on the command-line options.
     */
    private void processFiles() {//TODO if not summary or they want a GFF back then no sequence making for time saving(lvl 4)
        logger.info("Making sequence...");
        Map<String, String> sequence = FileUtils.sequenceMaker(inputFastaFile);
        logger.info("Sequence has been made...");
        logger.info("Getting ready to parse GFF3 file...");
        LinkedList<Feature> gffFeatures = GffParser.gffParser(inputGffFile);

        if (summary) {
            generateSummary(gffFeatures, sequence);
        } else {
            filterFeatures(gffFeatures, sequence);
        }
    }

    /**
     * Generates a summary of the GFF3 and FASTA files.
     */
    private void generateSummary(LinkedList<Feature> gffFeatures, Map<String, String> sequence) {
        logger.info("Generating summary...");
        FeatureSummary summary = new FileSummarizer().summarizeFeatures(gffFeatures, sequence);
        System.out.print(summary);
        //ReturnFile.chooseTypeFile(output_file, gffFeatures, sequence);
    }

    /**
     * Filters features based on command-line options.
     */
    private void filterFeatures(LinkedList<Feature> gffFeatures, Map<String, String> sequence) { //TODO if the GFFFeatureFunctions change this will need to change aswell (lvl 2)
        if (listId != null) {
            gffFeatures = delete ? GFFFeatureFunctions.deleteId(gffFeatures, listId) : GFFFeatureFunctions.fetchId(gffFeatures, listId);
        }
        if (listType != null) {
            gffFeatures = delete ? GFFFeatureFunctions.deleteType(gffFeatures, listType) : GFFFeatureFunctions.fetchType(gffFeatures, listType);
        }
        if (listAttribute != null) {
            Map<String, String> attributes = GffParser.parseAttributes(listAttribute);
            gffFeatures = delete ? GFFFeatureFunctions.deleteAttributes(gffFeatures, attributes) : GFFFeatureFunctions.FetchAttributes(gffFeatures, attributes);
        }
        if (listRegion != null) {
            gffFeatures = delete ? GFFFeatureFunctions.deleteRegion(gffFeatures, listRegion) : GFFFeatureFunctions.fetchRegion(gffFeatures, listRegion);
        }
        if (listChromosomes != null) {
            gffFeatures = delete ? GFFFeatureFunctions.deleteChromosome(gffFeatures, listChromosomes) : GFFFeatureFunctions.fetchChromosome(gffFeatures, listChromosomes);
        }
        if (listSource != null) {
            gffFeatures = delete ? GFFFeatureFunctions.deleteSource(gffFeatures, listSource) : GFFFeatureFunctions.fetchSource(gffFeatures, listSource);
        }

        ReturnFile.chooseTypeFile(output_file, gffFeatures, sequence);
    }
}

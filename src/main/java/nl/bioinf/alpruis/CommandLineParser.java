package nl.bioinf.alpruis;

import nl.bioinf.alpruis.operation.filter.GffProcessor;
import nl.bioinf.alpruis.operation.filter.ReturnFile;
import nl.bioinf.alpruis.operation.filter.StringToMapListConverter;
import nl.bioinf.alpruis.operation.filter_ex_sum.FeatureSummary;
import nl.bioinf.alpruis.operation.filter_ex_sum.FileSummarizer;
import nl.bioinf.alpruis.operation.filter_ex_sum.GffParser;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

import static nl.bioinf.alpruis.FileUtils.fileValidator;
import static nl.bioinf.alpruis.Main.logger;

@CommandLine.Command(name = "GffCommandLine", mixinStandardHelpOptions = true, version = "1.0",
        description = "A command-line tool to parse and query GFF3 files.")
public class CommandLineParser implements Runnable {

    // Define input files
    @CommandLine.Parameters(index = "0", description = "The input GFF3 file.")
    private Path inputGffFile;

    @CommandLine.Parameters(index = "1", description = "The input FASTA file.")
    private Path inputFastaFile;

    // Define options
    @CommandLine.Option(names = {"-vf","--validate"}, description = "Validates the GFF3 and FASTA files.")
    private boolean validate;

    @CommandLine.Option(names = {"-o", "--output_file"}, description = "The output file location. If not specified, a default file will be created.")
    private Path output_file;

    @CommandLine.Option(names = {"-sum", "--summary"}, description = "Generates a summary of the file contents including feature types, GC percentage, and more.")
    private boolean summary;

    @CommandLine.Option(names = {"-d","--delete"}, description = "Deletes specified feature(s). If not specified, features will be fetched.")
    private boolean delete;

    @CommandLine.Option(names = {"-e","--extended"}, description = "Includes parent and child features in the results.") // TODO rethink to use might to be too much work (lvl 2)
    private boolean extended; // TODO make this work somehow (lvl 3)
/*
    // Define query options
    @CommandLine.Option(names = {"-i", "--id"}, description = "Fetches or deletes features by ID(s). Use a comma-separated list.", split = ",")
    private List<String> listId;

    @CommandLine.Option(names = {"-t", "--type"}, description = "Fetches or deletes features by type(s). Use a comma-separated list.", split = ",")
    private List<String> listType;

    @CommandLine.Option(names = {"-r", "--region"}, description = "Fetches or deletes features within the specified regions (start, end). Use a comma-separated list.", split = ",")
    private List<Integer> listRegion;

    @CommandLine.Option(names = {"-c", "--chromosome"}, description = "Fetches or deletes features by chromosome(s). Use a comma-separated list.", split = ",")
    private List<String> listChromosomes;

    @CommandLine.Option(names = {"-s", "--source"}, description = "Fetches or deletes features by source(s). Use a comma-separated list.", split = ",")
    private List<String> listSource;*/

    //TODO there is a way to make it more efficient and the list of options shorter (lvl2)

    @CommandLine.Option(names = {"-f", "--filter"}, description = "column name = list with things to fetch or delete")
    private String listFilter;

    @CommandLine.Option(names = {"-a", "--attribute"}, description = "Fetches or deletes features by attribute(s). Use a comma-separated list (e.g., name=LOC,id=15).", split = ",")
    private List<String> listAttribute;

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

        if (summary) {
            logger.info("Getting ready to parse GFF3 file...");
            LinkedList<Feature> gffFeatures = GffParser.gffParser(inputGffFile);
            generateSummary(gffFeatures, sequence);
        } else {
            filterFeatures(sequence);
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
    private void filterFeatures(Map<String, String> sequence) { //TODO if the GFFFeatureFunctions change this will need to change aswell (lvl 2)
        StringToMapListConverter converter = new StringToMapListConverter();
        Map<String, List<String>> finalListFilter = converter.convert(listFilter);

        OptionsProcessor options = new OptionsProcessor(inputGffFile, sequence, validate, summary,
                delete, extended, output_file, finalListFilter, listAttribute);

            if (listFilter != null) {
                //logger.info("Getting ready to parse and filter GFF3 file...");
                ReturnFile.checkFileDir(output_file);
                GffProcessor.gffParser(options);
            } //TODO figure out what to do with Attributes and Extended
            if (listAttribute != null) {
                ReturnFile.checkFileDir(output_file);
                GffProcessor.gffParser(options);
            }
//            gffFeatures = delete ? GFFFeatureFunctions.deleteId(gffFeatures, listId) : GFFFeatureFunctions.fetchId(gffFeatures, listId);

    }
}

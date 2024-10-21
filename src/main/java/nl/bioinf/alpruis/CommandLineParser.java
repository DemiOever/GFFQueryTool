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

import static nl.bioinf.alpruis.FileUtils.fileValidator;
import static nl.bioinf.alpruis.Main.logger;

@CommandLine.Command(name = "GffCommandLine", mixinStandardHelpOptions = true, version = "1.0",
        description = "A command-line tool to parse and query GFF3 files.")
public class CommandLineParser implements Runnable {

    // Define input files
    @CommandLine.Parameters(index = "0", description = "The path to the input GFF3 file.")
    private Path inputGffFile;

    @CommandLine.Parameters(index = "1", description = "The path to the input FASTA file.")
    private Path inputFastaFile;

    // Define options
    @CommandLine.Option(names = {"-v","--validate"}, description = "Validates the files(both fasta and gff) if it has the right gff3 and fasta format.")
    private boolean validate;

    @CommandLine.Option(names = {"-o", "--output_file"}, description = "Put here the location of the output file with filename, preferably with a extension(.gff/.fasta/.csv/.txt). If this is left empty one will be created for you with default name and extension.")
    private Path output_file;

    @CommandLine.Option(names = {"-s", "--summary"}, description = "Gives back a summary of the files and includes: length of the sequence, gc-percentage, feature types with the amount present, different sources and amount present, amount of genes, average length of genes, amount of forward and reverse strands and names of all the regions.")
    private boolean summary;

    @CommandLine.Option(names = {"-d","--delete"}, description = "Deletes given feature part/parts, default is false which means it will fetch given element. To use this simply type the -d or --delete. Needs to be combined with --filter.")
    private boolean delete;

    @CommandLine.Option(names = {"-e","--extended"}, description = "This allows the parent and children of the feature to be included. Default is false but when used turned to true.")
    private boolean extended; // TODO make this work somehow (lvl 2)

    @CommandLine.Option(names = {"-f", "--filter"}, description = "column name(ID, Chromosome, Type, Source, Region, Attributes) == list with things seperated by a comma to fetch or delete. Example: Type==gene,exon")
    private String listFilter;
//TODO maybe able to specify filter multiple times
//TODO add the contains/equals option
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
        System.out.println(listFilter);
        StringToMapListConverter converter = new StringToMapListConverter();
        Map<String, List<String>> finalListFilter = converter.convert(listFilter);
        System.out.println(finalListFilter);
        OptionsProcessor options = new OptionsProcessor(inputGffFile, sequence, validate, summary,
                delete, extended, output_file, finalListFilter);

        if (listFilter != null) {
            //logger.info("Getting ready to parse and filter GFF3 file...");
            ReturnFile.checkFileDir(output_file);
            GffProcessor.gffParser(options);
        }
        //TODO put the extended option here

    }
}

package nl.bioinf.alpruis;

import static nl.bioinf.alpruis.Main.logger;

import nl.bioinf.alpruis.operations.FeatureSummary;
import nl.bioinf.alpruis.operations.GFFFeatureFunctions;
import nl.bioinf.alpruis.operations.FileSummarizer;
import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Command(name = "GffCommandLine", mixinStandardHelpOptions = true, version = "1.0",
        description = "A command-line tool to parse and query GFF3 files.")
// The help option is automatically included by Picocli due to mixinStandardHelpOptions = true
// TODO there is a split and other thing look into it and us it (lvl 1)
public class CommandLineParser implements Runnable {
    // Define the GFF3 input file as a positional argument
    @Parameters(index = "0", description = "The input GFF3 file.")
    private Path inputGffFile;

    // Define the FASTA file
    @Parameters(index = "1", description = "The input FASTA file.")
    private Path inputFastaFile;

    @Option(names = {"-vf","--validate"}, description = "Validates the files(both fasta and gff) if it has the right gff3 and fasta format.")
    private boolean validate;

    @Option(names = {"-o", "--output_file"}, description = "Put here the location of the output file with filename, preferably with a extension(.gff/.fasta/.csv/.txt). If this is left empty one will be created for you with default name and extension.")
    private Path output_file;

    @Option(names = {"-sum", "--summary"}, description = "Gives back a summary of the files and includes: Creates a textual summary of the parsed file: length of the sequence, gc-percentage, feature types with the amount present, number and types of annotations/features?. Average lengths of the genes, number on forward strand, number on reverse strand. Usage is a boolean.")
    private boolean summary;

    @Option(names = {"-d","--delete"}, description = "Deletes given feature part/parts, default is false which means it will fetch given element. To use this simply type the -d or --delete. Needs to be combined at least with one of the following: --id, --type, --source, --chromosome, --region, --atribute.")
    private boolean delete;

    @Option(names = {"-e","--extended"}, description = "This allows the parent and children of the feature to be included. Default is false but when used turned to true.")
    private boolean extended;

    @Option(names = {"-i", "--id"}, description = "Returns the nucleotide sequence of the element with this ID/ID's or without, default in Fasta format. To use this option please write it without spaces within ID's and a comma separating them instead, example: -i ID12345,ID67890", split = ",")
    private List<String> listId;

    @Option(names = {"-t", "--type"}, description = "Returns the nucleotide sequence of the element with this feature type or without, default in Fasta format. If chosen can also be returned as gff/csv/txt, use output. To use this option please write it without spaces within types and a comma separating them instead, example: -t mRNA,gene.", split = ",")
    private List<String> listType;

    @Option(names = {"-a", "--attribute"}, description = "Returns the nucleotide sequence of the element with this attribute name or without, default in Fasta format. If chosen can also be returned as gff/csv/txt, use output. To use this option please write it without spaces within attribute part(e.g. name=LOC) and a comma separating them instead, example: -attr name=LOC,id=15.", split = ",")
    private String[] listAttribute; // vb name=1 and that it's regex on the items in the attribute or not who knows

    @Option(names = {"-r", "--region"}, description = "Returns all features between the given coordinates or without, default in gff format. If chosen can also be returned as fasta/csv/txt, use output. To use this option please write it without spaces within locations and a comma separating them instead, example: -r 1,200,300,400.", split = ",")
    private List<Integer> listRegion; // maybe integer instead but it not work rn

    @Option(names = {"-c", "--chromosome"}, description = "Returns nucleotide sequence of the chromosome(s) given or without, default in fasta format. If chosen can also be returned as gff/csv/txt, use output. To use this option please write it without spaces within chromosomes(only numbers no written) and a comma separating them instead, example: 1,2", split = ",")
    private List<String> listChromosomes;

    @Option(names = {"-s", "--source"}, description = "Fetches or deletes the elements with this source, returns by default a gff file. If chosen can also be returned as fasta/csv/txt, use output. To use this option, write the source name/names without spaces and separate them with commas, e.g., refSeq,cDNA_match", split = ",")
    private List<String> listSource;
// TODO strand en score??

/*    @Option(names = {"-fp", "--feature_part"}, description = "Choose the feature parts you want to filter on: id, type, source, chromosome, region, atribute.")
    private List<String> listFeatureParts;

    @Option(names = {"-fp", "--feature_part"}, description = "")
    private List<String> listParts;*/


    @Override
    public void run() {
        if (validate) {
            validateFile();
        }
        else {
            //TODO if not summary or they want a GFF back then no sequence making for time saving(lvl 3)
            validateFile();
            logger.info("Making sequence...");
            String sequence = FileUtils.sequenceMaker(inputFastaFile);
            logger.info("Sequence has been made...");
            logger.info("Getting ready to parse GFF3 file...");
            LinkedList<Feature> gffFeatures = GffParser.gffParser(inputGffFile);
            //logger.info(gffFeatures.getFirst());

            if (summary) {
                logger.info("Generating summary...");
                FileSummarizer fileSummarizer = new FileSummarizer();
                FeatureSummary summary = fileSummarizer.summarizeFeatures(gffFeatures, sequence);
                logger.info(summary);
                ReturnFile.main(output_file, gffFeatures);
            } else if (listId != null || listType != null || listAttribute != null || listRegion != null || listChromosomes != null || listSource != null) {
                // Assuming gffFeatures is defined before this block
                if (listId != null) {
                    LinkedList<Feature> filteredFeatures = GFFFeatureFunctions.deleteId(gffFeatures, listId);
                    System.out.println(filteredFeatures);
                }
                if (listType != null) {
                    String filter = "Type";
                    LinkedList<Feature> filteredFeatures = GFFFeatureFunctions.deleteType(gffFeatures, listType);
                    //LinkedList<Feature> filteredFeatures = GFFFeatureFunctions.decideMethod(gffFeatures, sequence, filter, delete, listType, extended);
                    System.out.println(filteredFeatures);
                }
                if (listAttribute != null) {
                    String filter = "Attribute";
                    Map<String,String> mapje = GffParser.parseAttributes(listAttribute);
                    System.out.println(mapje);
                    LinkedList<Feature> filteredFeatures = GFFFeatureFunctions.deleteAttributes(gffFeatures, mapje);
                    //gffFeatures = GFFFeatureFunctions.decideMethod(gffFeatures, sequence, filter, delete, listAttribute, extended);
                    System.out.println(filteredFeatures);
                }
                if (listRegion != null) {
                    String filter = "Region";
                    System.out.println(listRegion);
                    LinkedList<Feature> filteredFeatures = GFFFeatureFunctions.deleteRegion(gffFeatures, listRegion);
                    // gffFeatures = GFFFeatureFunctions.decideMethod(gffFeatures, sequence, filter, delete, listRegion, extended);
                    System.out.println(filteredFeatures);
                }
                if (listChromosomes != null) {
                    String filter = "Chromosome";
                    LinkedList<Feature> filteredFeatures = GFFFeatureFunctions.deleteChromosome(gffFeatures, listChromosomes);
                    //gffFeatures = GFFFeatureFunctions.decideMethod(gffFeatures, sequence, filter, delete, listChromosomes, extended);
                    System.out.println(filteredFeatures);
                }
                if (listSource != null) {
                    String filter = "Source";
                    LinkedList<Feature> filteredFeatures = GFFFeatureFunctions.deleteSource(gffFeatures, listSource);
                    //gffFeatures = GFFFeatureFunctions.decideMethod(gffFeatures, sequence, filter, delete, listSource, extended);
                    System.out.println(filteredFeatures);
                }
            }
            else {
                logger.warn("You may have gave the command a null which is not accepted as an option."); //TODO create new logger(lvl 2)
            }
            //ReturnFile returned = new ReturnFile();
            //logger.info("first: {} \n last: {}", gffFeatures.getFirst(), gffFeatures.getLast());
        }
    }

    private void validateFile() {
        logger.info("Validating GFF3 file...");
        boolean validatedGff = FileUtils.fileValidator(inputGffFile);
        logger.info("Validating Fasta file...");
        boolean validatedFasta = FileUtils.fileValidator(inputFastaFile);
        if (validatedGff && validatedFasta) {
            logger.info("Both files content are valid to use.");
        }
        else if (validatedGff) {
            logger.warn("Only the Gff file is valid, Fasta file isnt up to standard."); //TODO create new logger(lvl 2)
        }
        else if (validatedFasta) {
            logger.warn("Only the Fasta file is valid, Gff file isnt up to standard."); //TODO create new logger(lvl 2)
        }
        else{
            logger.warn("Both files content are not valid to use."); //TODO create new logger(lvl 2)

        }
    }
}



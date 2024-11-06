package nl.bioinf.alpruis;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * A processor class for handling various options, which are related to file processing and validation.
 * The OptionsProcessor manages configuration options and file paths needed for processing
 * and provides methods to access these settings.
 */

public class OptionsProcessor {

    private final Path inputGffFile;
    private final boolean validate;
    private final boolean summary;
    private final boolean delete;
    private final boolean extended;
    private Path outputFile;
    private final Map<String, List<String>> finalListFilter;
    private final boolean useContains;

    /**
     * Constructs an OptionsProcessor with the specified configuration parameters.
     *
     * @param inputGffFile The path to the input GFF file.
     * @param validate Specifies if validation should be performed.
     * @param summary Specifies if a summary output is requested.
     * @param delete Specifies if delete should be performed.
     * @param extended Specifies if extended functionality should be enabled.
     * @param outputFile The path to the output file.
     * @param finalListFilter A map of filters to be applied, where each key has a list of filter criteria.
     * @param useContains Specifies if a "contains" check should be used in filtering.
     */
    public OptionsProcessor(Path inputGffFile, boolean validate, boolean summary,
                                  boolean delete, boolean extended, Path outputFile, Map<String, List<String>> finalListFilter, boolean useContains) {
        this.inputGffFile = inputGffFile;
        this.validate = validate;
        this.summary = summary;
        this.delete = delete;
        this.extended = extended;
        this.outputFile = outputFile;
        this.finalListFilter = finalListFilter;
        this.useContains = useContains;
    }

    // Getters and setters for each field

    /**
     * Gives the path to the input GFF file.
     *
     * @return The path to the input GFF file.
     */
    public Path getInputGffFile() {
        return inputGffFile;
    }

    /**
     * Checks if validation is requested.
     *
     * @return true if validation is requested; false otherwise.
     */
    public boolean isValidate() {
        return validate;
    }

    /**
     * Checks if a summary is requested.
     *
     * @return true if summary is requested; false otherwise.
     */
    public boolean isSummary() {
        return summary;
    }

    /**
     * Checks if delete is requested.
     *
     * @return true if delete are requested; false otherwise.
     */
    public boolean isDelete() {
        return delete;
    }

    /**
     * Checks if extended is requested.
     *
     * @return true if extended is requested; false otherwise.
     */
    public boolean isExtended() {
        return extended;
    }

    /**
     * Gives the path to the output file.
     *
     * @return The path to the output file.
     */
    public Path getOutputFile() {
        return outputFile;
    }

    /**
     * Gives the filter criteria map, where each key maps to a list of filtering strings.
     *
     * @return finalListFilter A map of filter criteria.
     */
    public Map<String, List<String>> getListFilter() {
        return finalListFilter;
    }

    /**
     * Checks if contains is requested.
     *
     * @return true if contains is requested; false otherwise.
     */
    public boolean getContains() {return useContains;}

    /**
     * Sets the output file path.
     *
     * @param outputFile the path to the output file.
     */
    public void setOutputFile(Path outputFile) {
        this.outputFile = outputFile;
    }

}

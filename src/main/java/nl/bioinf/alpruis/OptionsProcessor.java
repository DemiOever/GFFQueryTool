package nl.bioinf.alpruis;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class OptionsProcessor {

    private final Path inputGffFile;
    private final boolean validate;
    private final boolean summary;
    private final boolean delete;
    private final boolean extended;
    private Path outputFile;
    private final Map<String, List<String>> finalListFilter;
    private final boolean useContains;

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

    public Path getInputGffFile() {
        return inputGffFile;
    }

    public boolean isValidate() {
        return validate;
    }

    public boolean isSummary() {
        return summary;
    }

    public boolean isDelete() {
        return delete;
    }

    public boolean isExtended() {
        return extended;
    }

    public Path getOutputFile() {
        return outputFile;
    }

    public Map<String, List<String>> getListFilter() {
        return finalListFilter;
    }

    public boolean getContains() {return useContains;}

    public void setOutputFile(Path outputFile) {
        this.outputFile = outputFile;
    }

}

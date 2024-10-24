
# GFF Query Tool

---

The **GFF Query Tool** is a powerful command-line utility designed for parsing, querying, and manipulating GFF3 and FASTA files. With its robust features, users can efficiently search for specific attributes, chromosomes, IDs, regions, sources, and feature types within GFF3 files, returning corresponding nucleotide sequences in various formats. Currently, the tool is optimized for use on Linux-based command lines.

---

## Installation

To set up the GFF Query Tool, follow these steps:

1. **Clone the Repository**:
   Download the source code from [GitHub](https://github.com/DemiOever/GFFQueryTool.git).

2. **Java Requirement**:
   Ensure you have Java installed (Java 11 or higher).

3. **Build the Executable JAR**:
   Navigate to the `gffquerytool` directory in your terminal and execute:
   ```bash
   ./gradlew build
   ```

---

## Usage

The GFF Query Tool provides a variety of options for querying GFF3 and FASTA files. The basic syntax is as follows:

```bash
java -jar GFFQueryTool.jar [-dehVs] [-vf] [-o=<output_file>] 
                            [-f=<filter>[,listFilter]] 
                            <inputGffFile> <inputFastaFile>
```

### Input Parameters

- **`<inputGffFile>`**: Path to the input GFF3 file.
- **`<inputFastaFile>`**: Path to the input FASTA file.

### Command Options

| Option                | Description                                                                                                                                                                                                                         |
|-----------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **-d, --delete**      | Deletes specified feature(s) when used with other options; if not used, it fetches instead. Must be combined with `--filter`.                                                                                                   |
| **-e, --extended**    | Includes parent and child features in the results. Default is `false`.                                                                                                                                                           |
| **-h, --help**        | Displays help information.                                                                                                                                                                                                          |
| **-o, --output_file** | Specifies the output file path. Supported extensions: `.fasta`, `.gff`, `.txt`, `.csv`. Example: `-o output.fasta`                                                                                                                |
| **-s, --summary**     | Generates a summary of the GFF and FASTA files, including sequence length, GC percentage, feature types, sources, gene statistics, and strand counts.                                                                             |
| **-f, --filter**      | Filters based on specified criteria for columns: [ID, Type, Chromosome, Region, Attributes, Source]. Example: `ID==123,456`.                                                                                                   |
| **-V, --version**     | Displays the tool's version information.                                                                                                                                                                                          |
| **-vf, --validate**   | Validates the input GFF and FASTA files for the correct format.                                                                                                                                                                   |
| **-c, --contains**    | Allows attribute filtering using a "contains" approach rather than an exact match.                                                                                                                                               |

### Filter Usage Examples

#### For ID/Type/Source
**Usage**: `[ID|Type|Source]==num1,num2,num3`

#### For Chromosome
**Usage**: `Chromosome==NC_1.1,NC_2.1`  
*Matches with the `seq_id` in the GFF file.*

#### For Region
**Usage**: `Region==Start1,End1,Start2,End2`  
*Works in pairs.*

#### For Attributes
**Usage**: `Attributes==Name=XP_001.1,gene=Rb1`

### Sample Data
Example datasets can be found in the `src/main/resources/data` directory. These include both short and medium-length versions of the *Mus musculus* organism, commonly known as the house mouse.

---

## Example Usage

1. **Fetch Features by Attribute**:
   ```bash
   java -jar .\build\libs\gffquerytool-1.0-SNAPSHOT.jar -f Attributes==Note=C1 .\src\main\resources\data\medium_genomic.gff .\src\main\resources\data\medium_genomic.fna -o .\src\main\resources\data\output\output.gff -c
   ```
   *Replace `input.gff` and `input.fasta` with your own file paths; `output.gff` is your desired output path.*  <br><br>

2. **Fetch Sequences by Chromosome**:
   ```bash
   java -jar .\build\libs\gffquerytool-1.0-SNAPSHOT.jar -f Chromosome==NC_000067.7,NC_000070.7 input.gff input.fasta -o output.csv
   ```

3. **Delete Features by ID**:
   ```bash
   java -jar .\build\libs\gffquerytool-1.0-SNAPSHOT.jar -d -f ID==ID12345 input.gff input.fasta -o output.txt
   ```

4. **Generate a Summary**:
   ```bash
   java -jar .\build\libs\gffquerytool-1.0-SNAPSHOT.jar -s input.gff input.fasta
   ```

---

## Output Formats

### FASTA
The output will be a FASTA file containing original headers with only the retained features after filtering.

### GFF (Default)
Example output:
```
NC_088708.1	RefSeq	region	1	79350317	.	+	.	genome=chromosome;chromosome=1;ID=NC_088708.1:1..79350317;gbkey=Src;Dbxref=taxon:2589382;Name=1;mol_type=genomic DNA;
NC_088708.1	Gnomon	gene	12802	14331	.	-	.	gene=LOC137385494;description=uncharacterized LOC137385494;ID=gene-LOC137385494;gene_biotype=protein_coding;gbkey=Gene;Dbxref=GeneID:137385494;Name=LOC137385494;
...
```

### CSV
Example output:
```
ID,Source,Type,Start,End,Score,Strand,Phase,Attributes
NC_000067.7,cmsearch,gene,3172239,3172348,.,+,.,gene=Gm26206;description=predicted gene%2C 26206;ID=gene-Gm26206;gene_biotype=snRNA;gbkey=Gene;Dbxref=GeneID:115487594,MGI:MGI:5455983;Name=Gm26206
...
```

### TXT
Example output:
```
Feature{seqID='NC_000067.7', source='RefSeq', type='region', start=1, end=195154279, score='.', strand='+', phase='.', attributes={strain=C57BL/6J, genome=chromosome, chromosome=1, ID=NC_000067.7:1..195154279, gbkey=Src, Dbxref=taxon:10090, Name=1, mol_type=genomic DNA}}
...
```

---

## Ongoing Development

- Implementing the extended option to include child features.
- Writing output in FASTA format when selected.
- Optimizing performance by addressing time-consuming classes/methods.
- Completing tests for all methods and classes.
- Ensuring default GFF output when no valid output file is specified.

---

## FAQ

For questions or further support, feel free to reach out:

- **A.L. Pruis**: [a.l.pruis@st.hanze.nl](mailto:a.l.pruis@st.hanze.nl)
- **D. van 't Oever**: [d.van.t.oever@st.hanze.nl](mailto:d.van.t.oever@st.hanze.nl)

---
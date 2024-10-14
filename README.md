# GFF Query Tool

The **GFF Query Tool** is a command-line utility designed to parse, query, and manipulate GFF3 and FASTA files. It provides powerful features to search for specific attributes, chromosomes, IDs, regions, sources, and feature types within GFF3 files and return corresponding nucleotide sequences in various formats.

---

## Installation

1. Clone the repository or download the source code. From https://github.com/DemiOever/GFFQueryTool.git.
2. Ensure you have Java installed (Java 11 or higher).
3. Create the executable JAR file:
   Go to the terminal and make sure you are standing in the gffquerytool directory and type the following:
   ```
   ./gradlew build
   ```
   

---

## Usage

The GFF Query Tool supports various options for querying GFF3 and FASTA files. The syntax for using the tool is as follows:

```
java -jar GFFQueryTool.jar [-dehV] [-sum] [-vf] [-o=<output_file>] 
                           [-a=<listAttribute>[,<listAttribute>...]] 
                           [-c=<listChromosomes>[,<listChromosomes>...]] 
                           [-i=<listId>[,<listId>...]] 
                           [-r=<listRegion>[,<listRegion>...]] 
                           [-s=<listSource>[,<listSource>...]] 
                           [-t=<listType>[,<listType>...]] 
                           <inputGffFile> <inputFastaFile>
```

- **`<inputGffFile>`**: Path to the input GFF3 file.
- **`<inputFastaFile>`**: Path to the input FASTA file.

### Command Options

| Option | Description |
|--------|-------------|
| **-a, --attribute** | Returns the nucleotide sequence of the element(s) with specified attributes. Example: `-a name=LOC,id=15` |
| **-c, --chromosome** | Fetch sequences from specific chromosomes. Example: `-c 1,2` |
| **-d, --delete** | Deletes specified feature(s). Must be combined with `--id`, `--type`, `--source`, `--chromosome`, `--region`, or `--attribute`. |
| **-e, --extended** | Includes parent and child features in the result. Default is `false`. |
| **-h, --help** | Displays help information. |
| **-i, --id** | Fetch sequences by feature IDs. Example: `-i ID12345,ID67890` |
| **-o, --output_file** | Specifies the output file path. Example: `-o output.fasta` |
| **-r, --region** | Fetches features within specified regions. Example: `-r 1,200,300,400` |
| **-s, --source** | Fetches elements by source(s). Example: `-s refSeq,cDNA_match` |
| **-sum, --summary** | Generates a textual summary of the GFF and FASTA files, including sequence length, GC content, feature counts, and more. |
| **-t, --type** | Fetches features by type. Example: `-t mRNA,gene` |
| **-V, --version** | Displays the tool's version information. |
| **-vf, --validate** | Validates the input GFF and FASTA files for correct format. |

---

## Example Usage

1. **Fetch Features by Attribute:**
   ```
   java -jar GFFQueryTool.jar -a name=LOC,id=15 input.gff input.fasta
   ```

2. **Fetch Sequences by Chromosome:**
   ```
   java -jar GFFQueryTool.jar -c 1,2 input.gff input.fasta
   ```

3. **Delete Features by ID:**
   ```
   java -jar GFFQueryTool.jar -d -i ID12345 input.gff input.fasta
   ```

4. **Generate a Summary:**
   ```
   java -jar GFFQueryTool.jar -sum input.gff input.fasta
   ```

---

## FAQ

For questions or further support, feel free to reach out:

- **A.L. Pruis**: a.l.pruis@st.hanze.nl
- **D. van 't Oever**: d.van.t.oever@st.hanze.nl

---

## License

This tool is distributed under the MIT License.
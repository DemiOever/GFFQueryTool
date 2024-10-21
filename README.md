# GFF Query Tool

---

The **GFF Query Tool** is a command-line utility designed to parse, query, and manipulate GFF3 and FASTA files. It provides powerful features to search for specific attributes, chromosomes, IDs, regions, sources, and feature types within GFF3 files and return corresponding nucleotide sequences in various formats.
At the moment only able to run in a linux based commandline.  

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
java -jar GFFQueryTool.jar [-dehVs] [-vf] [-o=<output_file>] 
                           [-f=<filter>[,listFilter]] 
                           <inputGffFile> <inputFastaFile>
```

- **`<inputGffFile>`**: Path to the input GFF3 file.
- **`<inputFastaFile>`**: Path to the input FASTA file.

### Command Options

| Option                | Description                                                                                                                                                                                                                                                                                         |
|-----------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **-d, --delete**      | Deletes specified feature(s) used in combination with other options and if not used then it fetches. Must be combined with `--filter`.                                                                                                                                                              |
| **-e, --extended**    | Includes parent and child features in the result. Default is `false`.                                                                                                                                                                                                                               |
| **-h, --help**        | Displays help information.                                                                                                                                                                                                                                                                          |
| **-o, --output_file** | Specifies the output file path. You can chose one of the following 4 extensions: .fasta, .gff, .txt, .csv. Example: `-o output.fasta`                                                                                                                                                               |
| **-s, --summary**     | Generates a textual summary of the GFF and FASTA files, including length of the sequence, gc-percentage, feature types with the amount present, different sources and amount present, amount of genes, average length of genes, amount of forward and reverse strands and names of all the regions. |
| **-f, --filter**      | Filters the file on given input. The filters work per column: [ID, Type, Chromosome, Region, Attributes, Source] Example: `ID==123,456`.                                                                                                                                                            |
| **-V, --version**     | Displays the tool's version information.                                                                                                                                                                                                                                                            |
| **-vf, --validate**   | Validates the input GFF and FASTA files for correct format.                                                                                                                                                                                                                                         |


### Data
Data examples can be found in the following directory: src/main/resources/data. There are 4 files a short and medium length
version of the Mus Muscullus organism commonly known as house mouse.

---

## Example Usage

1. **Fetch Features by Attribute:**
   ```
   java -jar gffquerytool-1.0-SNAPSHOT.jar -f Attributes==name=LOC input.gff input.fasta -o output.gff
   ```
input.gff needs to be replaced by your own gff3 file<br>
input.fasta needs to be replaced by your own fasta file<br>
output.gff needs to be replaced by your path to your output file
2. **Fetch Sequences by Chromosome:**
   ```
   java -jar gffquerytool-1.0-SNAPSHOT.jar -f Chromosome==NC_000067.7,NC_000070.7 input.gff input.fasta -o output.csv
   ```

3. **Delete Features by ID:**
   ```
   java -jar gffquerytool-1.0-SNAPSHOT.jar -d -f ID==ID12345 input.gff input.fasta -o output.txt
   ```

4. **Generate a Summary:**
   ```
   java -jar gffquerytool-1.0-SNAPSHOT.jar -s input.gff input.fasta
   ```
---

## Output options

### Fasta
We want a fasta file with original headers and only with kept features after filtering

### GFF (default)
example:
headers
NC_088708.1	RefSeq	region	1	79350317	.	+	.	genome=chromosome;chromosome=1;ID=NC_088708.1:1..79350317;gbkey=Src;Dbxref=taxon:2589382;Name=1;mol_type=genomic DNA;
NC_088708.1	Gnomon	gene	12802	14331	.	-	.	gene=LOC137385494;description=uncharacterized LOC137385494;ID=gene-LOC137385494;gene_biotype=protein_coding;gbkey=Gene;Dbxref=GeneID:137385494;Name=LOC137385494;
NC_088708.1	Gnomon	mRNA	12802	14331	.	-	.	product=uncharacterized protein;transcript_id=XM_068071963.1;Parent=gene-LOC137385494;gene=LOC137385494;ID=rna-XM_068071963.1;gbkey=mRNA;Dbxref=GeneID:137385494,GenBank:XM_068071963.1;Name=XM_068071963.1;model_evidence=Supporting evidence includes similarity to: 3 Proteins;
etc.

### CSV
ID,Source,Type,Start,End,Score,Strand,Phase,Attributes
NC_000067.7,cmsearch,gene,3172239,3172348,.,+,.,gene=Gm26206;description=predicted gene%2C 26206;ID=gene-Gm26206;gene_biotype=snRNA;gbkey=Gene;Dbxref=GeneID:115487594,MGI:MGI:5455983;Name=Gm26206
NC_000067.7,BestRefSeq%2CGnomon,gene,3269956,3741733,.,-,.,gene=Xkr4;description=X-linked Kx blood group related 4;gene_synonym=Gm210,mKIAA1889,XRG4;ID=gene-Xkr4;gene_biotype=protein_coding;gbkey=Gene;Dbxref=GeneID:497097,MGI:MGI:3528744;Name=Xkr4
NC_000067.7,Gnomon,gene,3363896,3448035,.,+,.,gene=Gm53491;description=predicted gene%2C 53491;ID=gene-Gm53491;gene_biotype=lncRNA;gbkey=Gene;Dbxref=GeneID:118567655,MGI:MGI:6722034;Name=Gm53491


### TXT
Feature{seqID='NC_000067.7', source='RefSeq', type='region', start=1, end=195154279, score='.', strand='+', phase='.', attributes={strain=C57BL/6J, genome=chromosome, chromosome=1, ID=NC_000067.7:1..195154279, gbkey=Src, Dbxref=taxon:10090, Name=1, mol_type=genomic DNA}}
Feature{seqID='NC_000067.7', source='RefSeqFE', type='enhancer', start=3132446, end=3133539, score='.', strand='.', phase='.', attributes={experiment=EXISTENCE:reporter gene assay evidence [ECO:0000049][PMID:32912294], Note=C1 STARR-seq + active chromatin enhancer starr_00001, function=activates a minimal SCP1 promoter by STARR-seq in metastable (SL) mouse embryonic stem cells {active_cell/tissue: mESC(E14 +serum+LIF)}, ID=id-GeneID:131295014, regulatory_class=enhancer, gbkey=regulatory, Dbxref=GeneID:131295014}}
Feature{seqID='NC_000067.7', source='RefSeqFE', type='biological_region', start=3132446, end=3133539, score='.', strand='+', phase='.', attributes={standard_name=STARR-seq mESC enhancer starr_00001, ID=id-GeneID:131295014-2, gbkey=Region, Dbxref=GeneID:131295014, Name=biological region}}

---

## Ongoing processes
* Use the extended option to include Children 
* Writing in fasta format to a fasta file when chosen
* Look at classes and/of methods that are time-consuming and implement a better way to do that task. 
* Making all the tests for the methods/classes
* If no outfile has been given or an invalid one then it should be written to a default gff file.

---

## FAQ

For questions or further support, feel free to reach out:

- **A.L. Pruis**: a.l.pruis@st.hanze.nl
- **D. van 't Oever**: d.van.t.oever@st.hanze.nl

---
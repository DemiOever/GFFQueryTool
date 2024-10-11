package nl.bioinf.alpruis;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;

public class ReturnFile {
    // fasta return
    private static void returnFasta(){ //TODO finish this method(lvl 1)
    }

    private static void returnGff(){ //TODO finish this method(lvl 1)
    }

    private static void returnTxt(Path outFile, LinkedList<Feature> result){ //TODO finish this method(lvl 2)
        try (BufferedWriter writer = Files.newBufferedWriter(outFile, StandardOpenOption.APPEND)) {
            /*writer.write("Hi there");
            writer.newLine();
            writer.write("Bye now");
            writer.newLine();*/
            for (Feature feature : result){
                writer.write(String.valueOf(feature));
                writer.newLine();
            }
            } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void returnCsv(){ //TODO finish this method(lvl 2)
    }

    public static void main(Path outputFile, LinkedList<Feature> result) {
        //TODO make a if, if else, else for what type of outfile they want.(lvl 2)
        //in the else saying "ohh the file ya want isn't supported by this package"
        // and set defaults for certain options aka:
        // fetch_id, fetch_type, fetch_attribute  = fasta
        // fetch_region = ggf

        if(Files.exists(outputFile)) { //TODO check if this is right(lvl 2)
            //TODO make dir if dir not exist(lvl 2)
            try {
                Path copy = Paths.get(outputFile.toUri());
                //delete if it already exists
                Files.deleteIfExists(copy);
                Files.createFile(outputFile);
            } catch (IOException e) {
                e.printStackTrace(); //TODO catch this error correctly
            }
        }
        else {
            try {
                Files.createFile(outputFile);
            } catch (IOException e) {
                e.printStackTrace();//TODO catch this error correctly
            }
        }
        // returnFasta();
        // returnGff();
        returnTxt(outputFile, result);
        // returnCsv();
    }
}

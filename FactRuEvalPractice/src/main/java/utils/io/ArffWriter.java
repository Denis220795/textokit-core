package utils.io;

import ml.weka.CharacteristicVector;
import utils.extractors.FeatureExtractor;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Денис on 18.07.2016.
 */
public class ArffWriter {

    public void writeVectorsToFile(ArrayList<CharacteristicVector> vectors, File file) throws FileNotFoundException {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(file), Charset.forName("UTF-8")));
            BufferedWriter finalWriter = writer;
            FeatureExtractor.modifyLabelsWithBILOU(vectors);
            FeatureExtractor.setNextGrammems(vectors);
            FeatureExtractor.setPrevGrammems(vectors);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            writer.write("@relation ner");
            writer.newLine();
            writer.newLine();

            // with java reflection
            CharacteristicVector temp = new CharacteristicVector();
            Class vector = temp.getClass();
            Field[] publicFields = vector.getFields();
            for (Field field : publicFields) {
                writer.write("@attribute " + field.getName() + " string");
                writer.newLine();
            }
            writer.newLine();
            writer.write("@attribute class " +
                    "{'O', 'B_Person', 'I_Person', 'L_Person', 'U_Person', 'B_Location', 'I_Location', 'L_Location', " +
                    "'U_Location', 'B_Org', 'I_Org', 'L_Org', 'U_Org', 'B_LocOrg', 'I_LocOrg', 'L_LocOrg', 'U_LocOrg', " +
                    "'B_Project', 'I_Project', 'L_Project'}");
            writer.newLine();
            writer.newLine();
            writer.write("@data");
            writer.newLine();
            vectors.forEach(a -> {
                try {
                    if (!a.getBilouLabel().equals("O")) {
                        finalWriter.write(a.toString());
                        finalWriter.newLine();
                    } else {
                        if (Math.random() > 0.8) {
                            finalWriter.write(a.toString());
                            finalWriter.newLine();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

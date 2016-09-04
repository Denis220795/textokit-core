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

    private BufferedWriter writer;
    private boolean isTestSet;

    public ArffWriter(File file) {
        if (file.getName().toLowerCase().contains("test")) {
            isTestSet = true;
        }
        try {
            writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(file), Charset.forName("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeVectorsToFile(ArrayList<CharacteristicVector> vectors) throws IOException {
        buildHeader();
        buildBody(vectors, 0.8);
        closeWriter();
    }

    private void buildHeader() throws IOException {
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
                "'B_Project', 'I_Project', 'L_Project', 'U_Project', 'U_Facility'}");

        writer.newLine();
        writer.newLine();

        writer.write("@data");

        writer.newLine();
    }

    private void buildBody(ArrayList<CharacteristicVector> vectors, double probabilityOfOLabel) {
        BufferedWriter finalWriter = writer;
        vectors.forEach(a -> {
            try {
                if (!"O".equals(a.getBilouLabel())) {
                    if (!FeatureExtractor.isNumeric(a.getCoveredText())) {
                        finalWriter.write(a.toString());
                        finalWriter.newLine();
                    }
                } else {
                    if (Math.random() > probabilityOfOLabel) {
                        finalWriter.write(a.toString());
                        finalWriter.newLine();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void closeWriter() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

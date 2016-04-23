package com.textocat.lemma.predictor.utils.io;

import com.textocat.lemma.predictor.model.LemmaPredictionModel;

import java.io.*;

/**
 * Created by Денис on 07.04.2016.
 */
public class IOModelUtil {

    private IOModelUtil() {
    }

    public static void writeModel(LemmaPredictionModel model) throws IOException {
        FileOutputStream fos = null;
        try {
            File f = new File("model.ser");
            if (!f.exists()) {
                f.createNewFile();
            }
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(model);
        oos.flush();
        oos.close();
        fos.close();
    }

    public static LemmaPredictionModel readModel() throws IOException, ClassNotFoundException {
        FileInputStream fis = null;
        try {
            File f = new File("model.ser");
            fis = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectInputStream ois = new ObjectInputStream(fis);
        LemmaPredictionModel lemmaPredictionModel =  (LemmaPredictionModel) ois.readObject();
        ois.close();
        fis.close();
        return lemmaPredictionModel;
    }
}

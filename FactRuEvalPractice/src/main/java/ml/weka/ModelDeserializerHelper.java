package ml.weka;

import weka.classifiers.Classifier;

import java.io.File;

/**
 * Created by Денис on 30.08.2016.
 */
public class ModelDeserializerHelper {

    private ModelDeserializerHelper() {
    }

    public static Classifier deserializeModelFromFile(String filePath) {
        Classifier classifier = null;
        try {
            classifier = (Classifier) weka.core.SerializationHelper.read(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classifier;
    }
}

package realtests;

import com.textocat.lemma.predictor.model.LemmaPredictionModel;
import com.textocat.lemma.predictor.model.Transformation;
import com.textocat.lemma.predictor.model.utils.ModelWordsExtractor;
import com.textocat.lemma.predictor.model.utils.WordsTransformationUtil;
import com.textocat.lemma.predictor.utils.csv.CSVWriter;
import com.textocat.lemma.predictor.utils.csv.raw.IRecord;
import com.textocat.lemma.predictor.utils.csv.raw.Record;
import com.textocat.lemma.predictor.utils.io.IOModelUtil;
import com.textocat.lemma.predictor.utils.io.SystemResources;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Денис on 08.04.2016.
 */
public class ModelPredictionTest {
    ArrayList<String[]> records;

    public ModelPredictionTest() {
        records = new ArrayList<>();
    }

    @Test
    public static void main(String[] args) throws IOException, ClassNotFoundException, URISyntaxException {
        System.out.println("Initializing LemmaPredictionModel from source: "
                + SystemResources.resourcePath() + "models/model-stem-based.ser ...");
        LemmaPredictionModel model = IOModelUtil.readModel();
        Scanner s = new Scanner(new File(SystemResources.resourcePath() + "test-for-lpm.txt"));
        String sourceWord, goldWord, predictedWord, status, posTag;
        int total = 0, correct = 0;
        double accuracy;
        Transformation transformation;
        ArrayList<IRecord> records = new ArrayList<>();
        Record record;
        while (s.hasNext()) {
            sourceWord = s.next();
            posTag = s.next();
            goldWord = s.next();
            transformation = ModelWordsExtractor.getMostPossibleTransformation(sourceWord, posTag, model);
            predictedWord = WordsTransformationUtil.getTransformedWord(transformation, sourceWord);
            if (predictedWord.equals(goldWord)) {
                correct++;
                status = "true";
            } else {
                status = "false";
            }
            total++;
            System.out.println("Sorce word: " + sourceWord);
            System.out.println("Gold word: " + goldWord);
            System.out.println("Predicted word: " + predictedWord);
            System.out.println("Status: " + status);
            System.out.println("-------------------------");
            record = new Record(new String[]{sourceWord, goldWord, predictedWord, status});
            records.add(record);
        }
        accuracy = (double) correct / (double) total;
        System.out.println("Accuracy: " + accuracy);
        s.close();
        CSVWriter writer = new CSVWriter();
        String[] header = {"Source", "Golden", "Predicted by model", "Is correct"};
        File file = new File(SystemResources.resourcePath() + "results/result.csv");
        System.out.println("Writing results to file: " + file.getPath());
        writer.writeToCSV(header, records, file, accuracy);
    }
}

package realtests;

import com.textocat.morph.predictor.model.model.LemmaPredictionModel;
import com.textocat.morph.predictor.model.utils.ioutils.IOModelUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Денис on 08.04.2016.
 */
public class ModelPredictionTest {
    ArrayList<String> originalWords;
    ArrayList<String> correctWords;

    public ModelPredictionTest() {
        originalWords = new ArrayList<>();
        correctWords = new ArrayList<>();
    }

    @Test
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        LemmaPredictionModel model = IOModelUtil.readModel();
        System.out.println(">>" + model.getMostPossibleTransformation("красивая", "ADJF").toString());
        model.getAllPossibleTransformations("красивая", "ADJF").forEach(a -> System.out.println(a.toString()));
        System.out.println(">>" + model.getMostPossibleTransformation("человеком", "NOUN").toString());
        model.getAllPossibleTransformations("человеком", "NOUN").forEach(a -> System.out.println(a.toString()));
        System.out.println(">>" + model.getMostPossibleTransformation("телеграмме", "NOUN").toString());
        model.getAllPossibleTransformations("телеграмме", "NOUN").forEach(a -> System.out.println(a.toString()));
        System.out.println(">>" + model.getMostPossibleTransformation("смержили", "VERB").toString());
        model.getAllPossibleTransformations("смержили", "VERB").forEach(a -> System.out.println(a.toString()));
        System.out.println(">>" + model.getMostPossibleTransformation("гастарбайтеров", "NOUN").toString());
        model.getAllPossibleTransformations("гастарбайтеров", "NOUN").forEach(a -> System.out.println(a.toString()));
    }
}

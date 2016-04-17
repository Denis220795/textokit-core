package realtests;

import com.textocat.lemma.predictor.model.LemmaPredictionModel;
import com.textocat.lemma.predictor.model.utils.ModelWordsExtractor;
import com.textocat.lemma.predictor.utils.ioutils.IOModelUtil;
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
        System.out.println(">>" + ModelWordsExtractor.getMostPossibleTransformation("красивая", "ADJF", model).toString());
        ModelWordsExtractor.getAllPossibleTransformations("красивая", "ADJF", model).forEach(a -> System.out.println(a.toString()));
        System.out.println(">>" + ModelWordsExtractor.getMostPossibleTransformation("человеком", "NOUN", model).toString());
        ModelWordsExtractor.getAllPossibleTransformations("человеком", "NOUN", model).forEach(a -> System.out.println(a.toString()));
        System.out.println(">>" + ModelWordsExtractor.getMostPossibleTransformation("телеграмме", "NOUN", model).toString());
        ModelWordsExtractor.getAllPossibleTransformations("телеграмме", "NOUN", model).forEach(a -> System.out.println(a.toString()));
        System.out.println(">>" + ModelWordsExtractor.getMostPossibleTransformation("смержили", "VERB", model).toString());
        ModelWordsExtractor.getAllPossibleTransformations("смержили", "VERB", model).forEach(a -> System.out.println(a.toString()));
        System.out.println(">>" + ModelWordsExtractor.getMostPossibleTransformation("гастарбайтеров", "NOUN", model).toString());
        ModelWordsExtractor.getAllPossibleTransformations("гастарбайтеров", "NOUN", model).forEach(a -> System.out.println(a.toString()));
//        System.out.println(">>" + model.getMostPossibleTransformation("красиво", "ADVB").toString());
//        model.getAllPossibleTransformations("красиво", "ADVB").forEach(a -> System.out.println(a.toString()));
        System.out.println(">>" + ModelWordsExtractor.getMostPossibleTransformation("энергоресурсах", "NOUN", model).toString());
        ModelWordsExtractor.getAllPossibleTransformations("энергоресурсах", "NOUN", model).forEach(a -> System.out.println(a.toString()));
        System.out.println(">>" + ModelWordsExtractor.getMostPossibleTransformation("энергосzстеме", "NOUN", model).toString());
        ModelWordsExtractor.getAllPossibleTransformations("энергосzстеме", "NOUN", model).forEach(a -> System.out.println(a.toString()));
    }
}

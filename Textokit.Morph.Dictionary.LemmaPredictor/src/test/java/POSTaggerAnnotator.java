import com.textocat.lemma.predictor.model.LemmaPredictionModel;
import com.textocat.lemma.predictor.model.utils.ModelWordsExtractor;
import com.textocat.lemma.predictor.model.Transformation;
import com.textocat.lemma.predictor.utils.ioutils.IOModelUtil;
import com.textocat.textokit.morph.fs.SimplyWord;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Денис on 07.04.2016.
 */
    public class POSTaggerAnnotator  extends JCasAnnotator_ImplBase {
    LemmaPredictionModel model = null;
    @Override
        public void initialize(UimaContext aContext) throws ResourceInitializationException {
        try {
            model = IOModelUtil.readModel();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        }

        @Override
        public void process(JCas jCas) throws AnalysisEngineProcessException {
            Collection<SimplyWord> simplyWords = JCasUtil.select(jCas, SimplyWord.class);

            ArrayList<SimplyWord> lemmatizedWords = new ArrayList<>();

            ArrayList<SimplyWord> testWords = new ArrayList<>();
            final int[] i = {1};

            simplyWords.forEach(a -> {
                if (i[0] % 2 == 0) {
                    lemmatizedWords.add(a);
                    i[0]++;
                } else {
                    testWords.add(a);
                    i[0]++;
                }
            });

            for (int j = 0; j < lemmatizedWords.size(); j++) {
                SimplyWord testedWord = testWords.get(j);
                System.out.println(testedWord.getCoveredText() + " ^ ");
                ArrayList<Transformation> transformations = ModelWordsExtractor.getAllPossibleTransformations(
                        testWords.get(j), model);
                transformations.forEach(a -> {
                    System.out.println(" $ " + a.toString());
                });

            }
        }
    }
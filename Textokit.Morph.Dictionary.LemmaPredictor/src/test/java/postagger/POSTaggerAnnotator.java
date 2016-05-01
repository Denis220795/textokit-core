package postagger;

import com.textocat.lemma.predictor.model.LemmaPredictionModel;
import com.textocat.lemma.predictor.model.utils.ModelWordsExtractor;
import com.textocat.lemma.predictor.utils.io.IOModelUtil;
import com.textocat.textokit.morph.fs.SimplyWord;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import java.io.IOException;
import java.net.URISyntaxException;
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
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

        @Override
        public void process(JCas jCas) throws AnalysisEngineProcessException {
            Collection<SimplyWord> simplyWords = JCasUtil.select(jCas, SimplyWord.class);
            simplyWords.forEach(a -> {
                if (a.getLemma() == null) {
                    System.out.println(a.getCoveredText() + " -> " + ModelWordsExtractor.getMostPossibleTransformation(a,
                            model));
                }
            });
        }
    }
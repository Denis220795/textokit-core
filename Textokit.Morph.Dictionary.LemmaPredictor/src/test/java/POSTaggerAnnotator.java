import com.textocat.morph.predictor.model.model.LemmaPredictionModel;
import com.textocat.morph.predictor.model.utils.ioutils.IOModelUtil;
import com.textocat.textokit.morph.fs.SimplyWord;
import com.textocat.textokit.segmentation.fstype.Sentence;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import java.io.IOException;
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
            System.out.println("Inside 1");
            Collection<Sentence> sent = JCasUtil.select(jCas, Sentence.class);
            sent.forEach(a -> System.out.println(a.getCoveredText()));
            Collection<SimplyWord> simplyWords = JCasUtil.select(jCas, SimplyWord.class);
            final LemmaPredictionModel finalModel = model;
            simplyWords.forEach(a -> {
                System.out.println(a.getCoveredText()+" "+a.getPosTag());
//                if (a.getLemma()==null) {
//                    System.out.println(finalModel.getMostPossibleTransformation(a));
//                }
            });
        }
    }
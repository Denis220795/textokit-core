
import com.textocat.textokit.commons.cpe.FileDirectoryCollectionReader;
import com.textocat.textokit.postagger.PosTaggerAPI;
import com.textocat.textokit.tokenizer.TokenizerAPI;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;


import java.io.File;
import java.io.IOException;

/**
 * Created by Денис on 07.04.2016.
 */
public class POSTaggerTest {
    public static void main(String[] args) throws UIMAException, IOException, ClassNotFoundException {
        String inputDirPath = args[0];
        File inputF = new File(inputDirPath);
        CollectionReaderDescription readerDesc = FileDirectoryCollectionReader.createDescription(inputF);
        AnalysisEngineDescription aePOSTagger = AnalysisEngineFactory.createEngineDescription(PosTaggerAPI.AE_POSTAGGER);
        AnalysisEngineDescription aeTokenizer = AnalysisEngineFactory.createEngineDescription(TokenizerAPI.AE_TOKENIZER);
        AnalysisEngineDescription posa = AnalysisEngineFactory.createEngineDescription(POSTaggerAnnotator.class);
        SimplePipeline.runPipeline(readerDesc,aeTokenizer, aePOSTagger, posa);
    }
}
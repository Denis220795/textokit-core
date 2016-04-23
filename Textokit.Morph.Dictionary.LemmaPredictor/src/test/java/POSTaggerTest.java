
import com.textocat.textokit.commons.cpe.FileDirectoryCollectionReader;
import com.textocat.textokit.commons.util.PipelineDescriptorUtils;
import com.textocat.textokit.morph.commons.SimplyWordAnnotator;
import com.textocat.textokit.morph.lemmatizer.LemmatizerAPI;
import com.textocat.textokit.morph.opencorpora.resource.ClasspathMorphDictionaryResource;
import com.textocat.textokit.postagger.PosTaggerAPI;
import com.textocat.textokit.segmentation.SentenceSplitterAPI;
import com.textocat.textokit.tokenizer.TokenizerAPI;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.ExternalResourceFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ExternalResourceDescription;


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

        AnalysisEngineDescription aeTokenizer = AnalysisEngineFactory.createEngineDescription(TokenizerAPI.AE_TOKENIZER);

        AnalysisEngineDescription aeLemmaizer = AnalysisEngineFactory.createEngineDescription(LemmatizerAPI.AE_LEMMATIZER);

        AnalysisEngineDescription aeSimpWord = AnalysisEngineFactory.createEngineDescription(SimplyWordAnnotator.class);

        AnalysisEngineDescription aeSentS = AnalysisEngineFactory.createEngineDescription(SentenceSplitterAPI.AE_SENTENCE_SPLITTER);

        AnalysisEngineDescription aePOSTagger = AnalysisEngineFactory.createEngineDescription(PosTaggerAPI.AE_POSTAGGER);

        AnalysisEngineDescription posa = AnalysisEngineFactory.createEngineDescription(POSTaggerAnnotator.class);

        AnalysisEngineDescription aeDesc = AnalysisEngineFactory.createEngineDescription(aeTokenizer, aeSentS, aePOSTagger, aeLemmaizer, aeSimpWord, posa);

        ExternalResourceDescription morphDictDesc = ExternalResourceFactory.createExternalResourceDescription(ClasspathMorphDictionaryResource.class);

        morphDictDesc.setName("MorphDictionary");

        PipelineDescriptorUtils.getResourceManagerConfiguration(aeDesc).addExternalResource(morphDictDesc);

        SimplePipeline.runPipeline(readerDesc, aeDesc);
    }
}
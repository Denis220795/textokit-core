package model;

import com.textocat.textokit.morph.fs.SimplyWord;
import com.textocat.textokit.segmentation.fstype.Sentence;
import ml.weka.CharacteristicVector;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import utils.extractors.FeatureExtractor;
import utils.io.ArffWriter;
import utils.io.SystemResources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static utils.extractors.FeatureExtractor.*;
import static utils.extractors.FeatureExtractor.mkStringFromList;

/**
 * Created by Денис on 30.08.2016.
 */
public class EvalauationAnnotator extends JCasAnnotator_ImplBase {

    private ArrayList<CharacteristicVector> vectors;
    int pos;
    int length;
    int begin;
    String text;
    String lemma;
    String posTag;
    String suffixL1;
    String suffixL2;
    String suffixL3;
    String affixL1;
    String affixL2;
    String affixL3;
    String label;
    boolean isCW;
    boolean isNumeric;
    List<SimplyWord> left1Token;
    List<SimplyWord> left2Tokens;
    List<SimplyWord> left3Tokens;
    List<SimplyWord> right1Token;
    List<SimplyWord> right2Tokens;
    List<SimplyWord> right3Tokens;
    File textDirectory;
    File objectsDirectory;
    File spansDirectory;
    private int numOfDocs = 0;


    @Override
    public void initialize(UimaContext aContext) throws ResourceInitializationException {
        vectors = new ArrayList<>();
        textDirectory = new File(SystemResources.resourceTestSetPath() + "/texts/");
        objectsDirectory = new File(SystemResources.resourceTestSetPath() + "/objects/");
        spansDirectory = new File(SystemResources.resourceTestSetPath() + "/spans");
    }

    // fill local grammar info
    @Override
    public void collectionProcessComplete() throws AnalysisEngineProcessException {
        File file = new File("D:\\vectorsTest.arff");
        ArffWriter arffWriter = new ArffWriter(file);
        try {
            arffWriter.writeVectorsToFile(vectors);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // set word grammar info
    @Override
    public void process(JCas jCas) throws AnalysisEngineProcessException {
        String txtFIleName = FeatureExtractor.getFileName(jCas.getDocumentText(), textDirectory);
        String objectFileName;
        String spanFileName = null;
        if (txtFIleName != null) {
            numOfDocs++;
            objectFileName = txtFIleName.replaceAll(".txt", ".objects");
            spanFileName = txtFIleName.replaceAll(".txt", ".spans");
            System.out.println("Obj: " + objectFileName + " Spans: " + spanFileName);
            Collection<Sentence> sents = JCasUtil.select(jCas, Sentence.class);
            for (Sentence s : sents) {
                final int[] position = {0};
                Collection<SimplyWord> simplyWords = JCasUtil.selectCovered(SimplyWord.class, s);
                simplyWords.forEach(a -> {
                    position[0]++;
                    pos = position[0];
                    text = a.getCoveredText();
                    begin = a.getBegin();
                    text = text.replaceAll("'", "");
                    text = text.replaceAll(",", "");
                    lemma = a.getLemma();
                    posTag = a.getPosTag();
                    suffixL1 = getSuffixWithLength(1, a.getCoveredText());
                    suffixL2 = getSuffixWithLength(2, a.getCoveredText());
                    suffixL3 = getSuffixWithLength(3, a.getCoveredText());
                    affixL1 = getAffixWithLength(1, a.getCoveredText());
                    affixL2 = getAffixWithLength(2, a.getCoveredText());
                    affixL3 = getAffixWithLength(3, a.getCoveredText());
                    left1Token = getKNearestNeighbourWords(simplyWords, a, 1, "<-");
                    left2Tokens = getKNearestNeighbourWords(simplyWords, a, 2, "<-");
                    left3Tokens = getKNearestNeighbourWords(simplyWords, a, 3, "<-");
                    right1Token = getKNearestNeighbourWords(simplyWords, a, 1, "->");
                    right2Tokens = getKNearestNeighbourWords(simplyWords, a, 2, "->");
                    right3Tokens = getKNearestNeighbourWords(simplyWords, a, 3, "->");
                    length = a.getCoveredText().length();
                    isCW = isCapitalizedWord(a);
                    isNumeric = isNumeric(a);
                    try {
                        label = getLabel(a, objectFileName, objectsDirectory);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    zipToVector();
                });
                position[0] = 0;
            }
        }
        System.out.println("Num of docs processed: " + numOfDocs);
    }

    private void zipToVector() {
        CharacteristicVector vector = new CharacteristicVector();
        vector.setCoveredText(text);
        vector.setLemma(lemma);
        vector.setBegin(begin);
        vector.setPosition(pos);
        vector.setPosTag(posTag);
        vector.setAffixL1(affixL1);
        vector.setAffixL2(affixL2);
        vector.setAffixL3(affixL3);
        vector.setSuffixL1(suffixL1);
        vector.setSuffixL2(suffixL2);
        vector.setSuffixL3(suffixL3);
        vector.setLeft1Token(mkStringFromList(left1Token));
        vector.setLeft2Tokens(mkStringFromList(left2Tokens));
        vector.setLeft3Tokens(mkStringFromList(left3Tokens));
        vector.setRight1Token(mkStringFromList(right1Token));
        vector.setRight2Tokens(mkStringFromList(right2Tokens));
        vector.setRight3Tokens(mkStringFromList(right3Tokens));
        vector.setLength(length);
        vector.setLabel(label);
        vector.setIsCW(isCW);
        vector.setNumeric(isNumeric);
        vectors.add(vector);
    }
}

package postagger;

import com.textocat.textokit.morph.fs.SimplyWord;
import com.textocat.textokit.segmentation.fstype.Sentence;
import ml.weka.CharacteristicVector;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import java.util.*;

/**
 * Created by Денис on 07.04.2016.
 */
public class FeatureExtractorAnnotator extends JCasAnnotator_ImplBase {

    private Collection<CharacteristicVector> vectors;
    int pos;
    String text;
    String lemma;
    String posTag;
    String suffixL1;
    String suffixL2;
    String suffixL3;
    String affixL1;
    String affixL2;
    String affixL3;
    List<SimplyWord> left1Token;
    List<SimplyWord> left2Tokens;
    List<SimplyWord> left3Tokens;
    List<SimplyWord> right1Token;
    List<SimplyWord> right2Tokens;
    List<SimplyWord> right3Tokens;

    @Override
    public void initialize(UimaContext aContext) throws ResourceInitializationException {
        vectors = new ArrayList<>();

    }

    @Override
    public void process(JCas jCas) throws AnalysisEngineProcessException {
        Collection<Sentence> sents = JCasUtil.select(jCas, Sentence.class);
        for (Sentence s : sents) {
            final int[] position = {0};
            Collection<SimplyWord> simplyWords = JCasUtil.selectCovered(SimplyWord.class, s);
            simplyWords.forEach(a -> {
                position[0]++;
                pos = position[0];
                text = a.getCoveredText();
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
                zipToVector();
            });
            position[0] = 0;
        }
    }

    private String getSuffixWithLength(int length, String token) {
        String result;
        if (length >= token.length()) {
            return token;
        } else {
            result = token.substring(token.length() - length);
            return result;
        }
    }

    private String getAffixWithLength(int length, String token) {
        String result;
        if (length >= token.length()) {
            return token;
        } else {
            result = token.substring(0, length - 1);
            return result;
        }
    }

    private List<SimplyWord> getKNearestNeighbourWords(Collection<SimplyWord> simplyWords, SimplyWord word, int k, String direction) {
        List words = (List) simplyWords;
        ListIterator<SimplyWord> iterator = words.listIterator();
        int index = words.indexOf(word);
        for (int j = 0; j <= index; j++)
            iterator.next();

        List<SimplyWord> neighbours = new ArrayList<>();
        SimplyWord temp;
        if (direction.equals("->")) {
            for (int i = 0; i < k; i++) {
                if (iterator.hasNext()) {
                    temp = iterator.next();
                    neighbours.add(temp);
                } else return neighbours;
            }
            return neighbours;
        } else {
            if (direction.equals("<-")) {
                if (iterator.hasPrevious())
                    iterator.previous();
                for (int i = 0; i < k; i++) {
                    if (iterator.hasPrevious()) {
                        temp = iterator.previous();
                        neighbours.add(temp);
                    } else {
                        Collections.reverse(neighbours);
                        return neighbours;
                    }
                }
                Collections.reverse(neighbours);
                return neighbours;
            } else {
                return neighbours;
            }
        }
    }

    private String getLabel(SimplyWord simplyWord) {
        return "";
    }

    private void zipToVector() {
        CharacteristicVector vector = new CharacteristicVector();
        vector.setCoveredText(text);
        vector.setLemma(lemma);
        vector.setPosition(pos);
        vector.setPosTag(posTag);
        vector.setAffixL1(affixL1);
        vector.setAffixL2(affixL2);
        vector.setAffixL3(affixL3);
        vector.setSuffixL1(suffixL1);
        vector.setSuffixL2(suffixL2);
        vector.setSuffixL3(suffixL3);
        vector.setLeft1Token(left1Token);
        vector.setLeft2Tokens(left2Tokens);
        vector.setLeft3Tokens(left3Tokens);
        vector.setRight1Token(right1Token);
        vector.setRight2Tokens(right2Tokens);
        vector.setRight3Tokens(right3Tokens);
        vectors.add(vector);
        System.out.println(vector.toString());
    }
}
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
import utils.io.SystemResources;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by Денис on 07.04.2016.
 */
public class FeatureExtractorAnnotator extends JCasAnnotator_ImplBase {

    private Collection<String> vectors;
    int pos;
    int length;
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
    List<SimplyWord> left1Token;
    List<SimplyWord> left2Tokens;
    List<SimplyWord> left3Tokens;
    List<SimplyWord> right1Token;
    List<SimplyWord> right2Tokens;
    List<SimplyWord> right3Tokens;
    File textDirectory;
    File spanDirectory;
    private int numOfDocs = 0;
    private int noneCounter = 0;


    @Override
    public void initialize(UimaContext aContext) throws ResourceInitializationException {
        vectors = new ArrayList<>();
        textDirectory = new File(SystemResources.resourcePath() + "/texts/");
        spanDirectory = new File(SystemResources.resourcePath() + "/objects/");
    }

    @Override
    public void collectionProcessComplete() throws AnalysisEngineProcessException {
        File file = new File("D:\\vectors.txt");
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(file), Charset.forName("UTF-8")));
            BufferedWriter finalWriter = writer;
            vectors.forEach(a -> {
                try {
                    finalWriter.write(a.toString());
                    finalWriter.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        showAllVectors();
    }

    @Override
    public void process(JCas jCas) throws AnalysisEngineProcessException {
        String spanFileName = getFileName(jCas.getDocumentText());
        if (spanFileName != null) {
            numOfDocs++;
            spanFileName = spanFileName.replaceAll(".txt", "");
            spanFileName += ".objects";
            Collection<Sentence> sents = JCasUtil.select(jCas, Sentence.class);
            for (Sentence s : sents) {
                final int[] position = {0};
                Collection<SimplyWord> simplyWords = JCasUtil.selectCovered(SimplyWord.class, s);
                String finalSpanFileName = spanFileName;
                simplyWords.forEach(a -> {
                    position[0]++;
                    pos = position[0];
                    text = a.getCoveredText();
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
                    try {
                        label = getLabel(a, finalSpanFileName);
                        ;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (label.equals("none")) {
                        noneCounter++;
                        if (noneCounter < 4000) {
                            zipToVector();
                            noneCounter++;
                        }
                    } else
                    zipToVector();
                });
                position[0] = 0;
            }
        }
        System.out.println("Num of docs processed: " + numOfDocs);
    }

    private String getSuffixWithLength(int length, String token) {
        String result;
        if (length >= token.length()) {
            return token;
        } else {
            result = token.substring(token.length() - length);
            result = result.replaceAll("'", "");
            result = result.replaceAll("'", "");
            return result;
        }
    }

    private String getAffixWithLength(int length, String token) {
        String result;
        if (length >= token.length()) {
            return token;
        } else {
            result = token.substring(0, length);
            result = result.replaceAll("'", "");
            result = result.replaceAll("'", "");
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

    private String getLabel(SimplyWord simplyWord, String spanFileName) throws IOException {
        BufferedReader reader = null;
        final String[] label = {"none"};

        File[] spansFiles = spanDirectory.listFiles();
        for (File f : spansFiles) {
            if (f.getName().equals(spanFileName))
                reader = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(f), Charset.forName("UTF-8")));
        }
        if (reader != null) {
            reader.lines().forEach(a -> {
                String[] labels = a.split(" ");
                if (label[0].equals("none"))
                    for (int i = labels.length - 1; i >= 0; i--) {
                        if (!labels[i].equals("#")) {
                            if (labels[i].equals(simplyWord.getCoveredText()))
                                label[0] = labels[1];
                        }
                    }
            });
        }
        return label[0];
    }

    private String getFileName(String docText) {
        BufferedReader reader;
        File[] spansFiles = textDirectory.listFiles();
        for (File f : spansFiles) {
            try {
                reader = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(f), Charset.forName("UTF-8")));
                final String[] text = {""};
                reader.lines().forEach(a -> {
                    text[0] += a;
                });
                if (text[0].substring(0, 20).equals(docText.substring(0, 20))) {
                    String name = f.getName();
                    return name;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
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
        vector.setLength(length);
        vector.setLabel(label);
        vectors.add(vector.toString());
//        System.out.println(vector.toString());
    }

    private void showAllVectors() {
        vectors.forEach(a -> {
            System.out.println(a.toString());
        });
    }
}
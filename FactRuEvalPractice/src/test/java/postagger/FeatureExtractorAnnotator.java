package postagger;

import com.textocat.textokit.morph.fs.SimplyWord;
import com.textocat.textokit.segmentation.fstype.Sentence;
import ml.weka.CharacteristicVector;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import utils.io.SystemResources;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Денис on 07.04.2016.
 */
public class FeatureExtractorAnnotator extends JCasAnnotator_ImplBase {

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
    String prevBilouLabel;
    String nextBilouLabel;
    String nextGrammems;
    String prevGrammems;
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
        textDirectory = new File(SystemResources.resourcePath() + "/texts/");
        objectsDirectory = new File(SystemResources.resourcePath() + "/objects/");
        spansDirectory = new File(SystemResources.resourcePath() + "/spans");
    }

    @Override
    public void collectionProcessComplete() throws AnalysisEngineProcessException {
        File file = new File("D:\\vectors.arff");
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(file), Charset.forName("UTF-8")));
            BufferedWriter finalWriter = writer;
            modifyLabelsWithBILOU(vectors);
            writer.write("@relation ner");
            writer.newLine();
            writer.newLine();

            // with java reflection
            CharacteristicVector temp = new CharacteristicVector();
            Class vector = temp.getClass();
            Field[] publicFields = vector.getFields();
            for (Field field : publicFields) {
                writer.write("@attribute " + field.getName() + " string");
                writer.newLine();
            }
            writer.newLine();
            writer.write("@attribute class " +
                    "{'O', 'B_Person', 'I_Person', 'L_Person', 'U_Person', 'B_Location', 'I_Location', 'L_Location', " +
                    "'U_Location', 'B_Org', 'I_Org', 'L_Org', 'U_Org', 'B_LocOrg', 'I_LocOrg', 'L_LocOrg', 'U_LocOrg', " +
                    "'B_Project', 'I_Project', 'L_Project'}");
            writer.newLine();
            writer.newLine();
            writer.write("@data");
            writer.newLine();
            vectors.forEach(a -> {
                try {
                    if (!a.getBilouLabel().equals("O")) {
                        finalWriter.write(a.toString());
                        finalWriter.newLine();
                    } else {
                        if (Math.random() > 0.8) {
                            finalWriter.write(a.toString());
                            finalWriter.newLine();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        showAllVectors();
    }

    @Override
    public void process(JCas jCas) throws AnalysisEngineProcessException {
        String txtFIleName = getFileName(jCas.getDocumentText());
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
                String finalobjectFileName = objectFileName;
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

                    try {
                        label = getLabel(a, finalobjectFileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    if (label.equals("none")) {
//                        noneCounter++;
//                        if (noneCounter < 4000) {
//                            zipToVector();
//                            noneCounter++;
//                        }
//                    } else
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

    private String getLabel(SimplyWord simplyWord, String objectFileName) throws IOException {
        BufferedReader objectsReader = null;

        final String[] label = {"none"};

        File[] objectsFiles = objectsDirectory.listFiles();

        for (File f : objectsFiles) {
            if (f.getName().equals(objectFileName))
                objectsReader = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(f), Charset.forName("UTF-8")));
        }

        if (objectsReader != null) {
            objectsReader.lines().forEach(a -> {
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
        vectors.add(vector);
//        System.out.println(">>>"+vector.toString());
    }

    private void showAllVectors() {
        for (CharacteristicVector vector : vectors) {
            System.out.println(vector.toString());
        }
    }

    private String mkStringFromList(List<SimplyWord> words) {
        String result = "";
        for (SimplyWord sw : words)
            result += sw.getCoveredText() + " ";
        return result;
    }

    private boolean isCapitalizedWord(SimplyWord simplyWord) {
        String rusRegEx = "[А-Я]";
        String engRegEx = "[A-Z]";
        Pattern patternRus = Pattern.compile(rusRegEx);
        Pattern patternEng = Pattern.compile(engRegEx);
        boolean result;
        result = patternRus.matcher(new String(String.valueOf(simplyWord.getCoveredText().charAt(0)))).matches();
        if (!result) {
            result = patternEng.matcher(new String(String.valueOf(simplyWord.getCoveredText().charAt(0)))).matches();
        }
        return result;
    }

    private boolean isNumeric(SimplyWord simplyWord) {
        String digitalRegEx = "[0-9]";
        Pattern patternDig = Pattern.compile(digitalRegEx);
        return patternDig.matcher(simplyWord.getCoveredText()).matches();
    }

    private void modifyLabelsWithBILOU(ArrayList<CharacteristicVector> vectors) {
        for (int i = 0; i < vectors.size(); i++) {
            // choose only ner tokens, miss none
            if (!vectors.get(i).getLabel().equals("none")) {
                // see only not first and end tokens
                if (i != 0 && i != vectors.size() - 1) {
                    CharacteristicVector currentVector = vectors.get(i);
                    CharacteristicVector previousVector = vectors.get(i - 1);
                    CharacteristicVector nextVector = vectors.get(i + 1);
                    // atomic anno
                    if (!nextVector.getLabel().equals(currentVector.getLabel()) && !previousVector.getLabel().equals(currentVector.getLabel()))
                        currentVector.setBilouLabel("U_" + currentVector.getLabel());
                    else {
                        // begin anno
                        if (nextVector.getLabel().equals(vectors.get(i).getLabel()) && !previousVector.getLabel().equals(vectors.get(i).getLabel()))
                            vectors.get(i).setBilouLabel("B_" + vectors.get(i).getLabel());
                        else {
                            // end anno
                            if (!nextVector.getLabel().equals(vectors.get(i).getLabel()) && previousVector.getLabel().equals(vectors.get(i).getLabel()))
                                vectors.get(i).setBilouLabel("L_" + vectors.get(i).getLabel());
                            else {
                                // inside anno
                                if (nextVector.getLabel().equals(vectors.get(i).getLabel()) && previousVector.getLabel().equals(vectors.get(i).getLabel()))
                                    vectors.get(i).setBilouLabel("I_" + vectors.get(i).getLabel());
                            }
                        }
                    }
                }
            } else {
                vectors.get(i).setBilouLabel("O");
            }
        }
    }
}
package utils.extractors;

import com.textocat.textokit.morph.fs.SimplyWord;
import ml.weka.CharacteristicVector;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Денис on 28.08.2016.
 */
public class FeatureExtractor {

    private FeatureExtractor() {
    }

    public static String getSuffixWithLength(int length, String token) {
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

    public static String getAffixWithLength(int length, String token) {
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

    public static List<SimplyWord> getKNearestNeighbourWords(Collection<SimplyWord> simplyWords, SimplyWord word, int k, String direction) {
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

    public static String mkStringFromList(List<SimplyWord> words) {
        String result = "";
        for (SimplyWord sw : words) {
            result += sw.getLemma() != null ? sw.getLemma().replaceAll("'", "") : sw.getCoveredText().replaceAll("'", "");
            result += " ";
        }
        return result;
    }

    public static boolean isCapitalizedWord(SimplyWord simplyWord) {
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

    public static boolean isNumeric(SimplyWord simplyWord) {
        String digitalRegEx = "[0-9]+";
        Pattern patternDig = Pattern.compile(digitalRegEx);
        return patternDig.matcher(simplyWord.getCoveredText()).matches();
    }

    public static void modifyLabelsWithBILOU(ArrayList<CharacteristicVector> vectors) {
        for (int i = 0; i < vectors.size(); i++) {
            // choose only ner tokens, miss none
            if (!"none".equals(vectors.get(i).getLabel())) {
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

    public static void modifyLabelsWithQuestion(ArrayList<CharacteristicVector> vectors) {
        vectors.forEach(f -> f.setBilouLabel("?"));
    }

    public static void setPrevGrammems(ArrayList<CharacteristicVector> vectors) {
        for (int i = 1; i < vectors.size(); i++) {
            vectors.get(i).setprevGrammems(vectors.get(i - 1).getPosTag());
        }
    }

    public static void setNextGrammems(ArrayList<CharacteristicVector> vectors) {
        for (int i = vectors.size() - 2; i > 0; i--) {
            vectors.get(i).setNextGrammems(vectors.get(i + 1).getPosTag());
        }
    }

    public static String getLabel(SimplyWord simplyWord, String objectFileName, File objectsDirectory) throws IOException {
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

    public static String getFileName(String docText, File textDirectory) {
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

    public static boolean isNumeric(String word) {
        String digitalRegEx = "[0-9]+";
        Pattern patternDig = Pattern.compile(digitalRegEx);
        return patternDig.matcher(word).matches();
    }
}

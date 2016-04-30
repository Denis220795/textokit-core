package com.textocat.lemma.predictor.model.utils;

import com.textocat.lemma.predictor.model.Transformation;
import com.textocat.lemma.predictor.model.LemmaPredictionModel;
import com.textocat.lemma.predictor.utils.comparators.CriteriaBasedComparator;
import com.textocat.lemma.predictor.utils.comparators.LengthBasedComparator;
import com.textocat.lemma.predictor.utils.comparators.PossibilityBasedComparator;
import com.textocat.textokit.morph.fs.SimplyWord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Created by Денис on 17.04.2016.
 */
public class ModelWordsExtractor {

    private ModelWordsExtractor() {
    }

    // возвращает самое длинное вероятное преобразование для слова - точность выше
    public static Transformation getMostPossibleLongestTransformation(SimplyWord word, LemmaPredictionModel lpmodel) {
        ArrayList<Transformation> possibleTransformations = new ArrayList<>();
        HashMap<String, ArrayList<Transformation>> model = lpmodel.getModel();
        ArrayList<Transformation> transformations;
        if (word.getGrammems() == null)
            return null;
        transformations = model.get(word.getGrammems(0));
        if (transformations == null) return null;
        String text = word.getCoveredText();
        for (Transformation temp : transformations) {
            if (temp.getFrom().length() < text.length() && temp.getFrom().length() != 0) {
                if (text.substring(text.length() - temp.getFrom().length()).equals(temp.getFrom())) {
                    possibleTransformations.add(temp);
                }
            }
        }
        // сглаживание
        possibleTransformations.forEach(a -> a.setCriteria(a.getPossibility() * Math.log(a.getNum())));
        if (possibleTransformations.size() != 0) {
            possibleTransformations.sort(new LengthBasedComparator());
        }
        ArrayList<Transformation> mostLengthTrans = new ArrayList<>();
        for (Transformation tr : possibleTransformations) {
            if (mostLengthTrans.size() == 0) mostLengthTrans.add(tr);
            else {
                if (tr.getFrom().length() >= mostLengthTrans.get(0).getFrom().length())
                    mostLengthTrans.add(tr);
            }
        }
        mostLengthTrans.sort(new CriteriaBasedComparator());
        if (mostLengthTrans.size() != 0)
            return mostLengthTrans.get(0);
        else return null;
    }

    // возвращает наиболее вероятное преобразование вне зависимости от длины для слова - точность меньше
    public static Transformation getMostPossibleTransformation(SimplyWord word, LemmaPredictionModel lpmodel) {
        ArrayList<Transformation> possibleTransformations = new ArrayList<>();
        HashMap<String, ArrayList<Transformation>> model = lpmodel.getModel();
        if (word.getGrammems() == null) return null;
        ArrayList<Transformation> transformations = model.get(word.getGrammems(0));
        String text = word.getCoveredText();
        if (transformations == null) return null;
        for (Transformation temp : transformations) {
            if (temp.getFrom().length() < text.length() && temp.getFrom().length() != 0) {
                if (text.substring(text.length() - temp.getFrom().length()).equals(temp.getFrom())) {
                    possibleTransformations.add(temp);
                }
            }
        }
        // сглаживание
        possibleTransformations.forEach(a -> a.setCriteria(a.getPossibility() * Math.log(a.getNum())));
        possibleTransformations.sort(new CriteriaBasedComparator());
        if (possibleTransformations.size() != 0) {
            return possibleTransformations.get(0);
        } else return null;
    }

    // возвращает наиболее вероятное преобразование вне зависимости от длины для слова
    public static Transformation getMostPossibleTransformation(String word, String partOfSpeech, LemmaPredictionModel lpmodel) {
        ArrayList<Transformation> possibleTransformations = new ArrayList<>();
        HashMap<String, ArrayList<Transformation>> model = lpmodel.getModel();
        ArrayList<Transformation> transformations = model.get(partOfSpeech);
        for (Transformation temp : transformations) {
            if (temp.getFrom().length() < word.length() & !temp.getFrom().equals("")) {
                if (word.substring(word.length() - temp.getFrom().length()).equals(temp.getFrom())) {
                    possibleTransformations.add(temp);
                }
            }
        }
        // сглаживание
        possibleTransformations.forEach(a -> a.setCriteria(a.getPossibility() * Math.getExponent(a.getNum())));
        if (possibleTransformations.size() != 0) {
            possibleTransformations.sort(new CriteriaBasedComparator());
            return possibleTransformations.get(0);
        } else return null;
    }

    // возвращает все вероятные преобразования для слова
    public static ArrayList<Transformation> getAllPossibleTransformations(String word, String partOfSpeech, LemmaPredictionModel lpmodel) {
        ArrayList<Transformation> possibleTransformations = new ArrayList<>();
        HashMap<String, ArrayList<Transformation>> model = lpmodel.getModel();
        ArrayList<Transformation> transformations = model.get(partOfSpeech);
        for (Transformation temp : transformations) {
            if (temp.getFrom().length() < word.length() & !temp.getFrom().equals("")) {
                if (word.substring(word.length() - temp.getFrom().length()).equals(temp.getFrom())) {
                    possibleTransformations.add(temp);
                }
            }
        }
        possibleTransformations.sort(new PossibilityBasedComparator());
        if (possibleTransformations.size() != 0) {
            return possibleTransformations;
        } else return null;
    }

    // возвращает все вероятные преобразования для слова
    public static ArrayList<Transformation> getAllPossibleTransformations(SimplyWord simplyWord, LemmaPredictionModel lpmodel) {
        ArrayList<Transformation> possibleTransformations = new ArrayList<>();
        HashMap<String, ArrayList<Transformation>> model = lpmodel.getModel();
        ArrayList<Transformation> transformations = model.get(simplyWord.getGrammems(0));
        possibleTransformations.addAll(transformations.stream().filter(temp -> temp.getFrom().length() < simplyWord.getCoveredText().length() & !temp.getFrom().equals("")).filter(temp -> simplyWord.getCoveredText().substring(simplyWord.getCoveredText().length()
                - temp.getFrom().length()).equals(temp.getFrom())).collect(Collectors.toList()));
        possibleTransformations.forEach(a -> a.setCriteria(a.getPossibility() * Math.log(a.getNum())));
        possibleTransformations.sort(new CriteriaBasedComparator());
        if (possibleTransformations.size() != 0) {
            return possibleTransformations;
        } else return null;
    }

    // печатает слова и преобразования с очень малой вероятностьью
    public static void printLeastPossibleTransformations(LemmaPredictionModel lpmodel) {
        HashMap<String, ArrayList<Transformation>> model = lpmodel.getModel();
        for (String s : model.keySet()) {
            model.get(s).forEach(a -> {
                if (a.getPossibility() < 0.001)
                    System.out.println(a.toString() + " Words:" + a.getSourceWord() + " -> " + a.getTransformedWord());
            });
        }
    }
}
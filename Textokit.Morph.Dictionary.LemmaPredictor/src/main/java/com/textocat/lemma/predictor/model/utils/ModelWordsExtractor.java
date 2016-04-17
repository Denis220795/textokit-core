package com.textocat.lemma.predictor.model.utils;

import com.textocat.lemma.predictor.model.Transformation;
import com.textocat.lemma.predictor.model.LemmaPredictionModel;
import com.textocat.lemma.predictor.utils.comparators.CriteriaBasedComparator;
import com.textocat.lemma.predictor.utils.comparators.PossibilityBasedComparator;
import com.textocat.textokit.morph.fs.SimplyWord;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Денис on 17.04.2016.
 */
public class ModelWordsExtractor {

    private ModelWordsExtractor() {
    }

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
        possibleTransformations.forEach(a -> {
            a.setCriteria(a.getPossibility() * Math.getExponent(a.getNum()));
        });
        if (possibleTransformations.size() != 0) {
            possibleTransformations.sort(new CriteriaBasedComparator());
            return possibleTransformations.get(0);
        } else return null;
    }

    public static Transformation getMostPossibleTransformation(SimplyWord word, LemmaPredictionModel lpmodel) {
        ArrayList<Transformation> possibleTransformations = new ArrayList<>();
        HashMap<String, ArrayList<Transformation>> model = lpmodel.getModel();
        ArrayList<Transformation> transformations = model.get(word.getGrammems(0));
        String text = word.getCoveredText();
        for (Transformation temp : transformations) {
            if (temp.getFrom().length() < text.length() && temp.getFrom().length() != 0) {
                if (text.substring(text.length() - temp.getFrom().length()).equals(temp.getFrom())) {
                    possibleTransformations.add(temp);
                }
            }
        }
        possibleTransformations.forEach(a -> {
            a.setCriteria(a.getPossibility() * Math.getExponent(a.getNum()));
        });
        possibleTransformations.sort(new CriteriaBasedComparator());
        if (possibleTransformations.size() != 0) {
            return possibleTransformations.get(0);
        } else return null;
    }

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

    public static ArrayList<Transformation> getAllPossibleTransformations(SimplyWord simplyWord, LemmaPredictionModel lpmodel) {
        ArrayList<Transformation> possibleTransformations = new ArrayList<>();
        HashMap<String, ArrayList<Transformation>> model = lpmodel.getModel();
        ArrayList<Transformation> transformations = model.get(simplyWord.getGrammems(0));
        for (Transformation temp : transformations) {
            if (temp.getFrom().length() < simplyWord.getCoveredText().length() & !temp.getFrom().equals("")) {
                if (simplyWord.getCoveredText().substring(simplyWord.getCoveredText().length()
                        - temp.getFrom().length()).equals(temp.getFrom())) {
                    possibleTransformations.add(temp);
                }
            }
        }
        possibleTransformations.forEach(a -> {
            a.setCriteria(a.getPossibility() * Math.getExponent(a.getNum()));
        });
        possibleTransformations.sort(new CriteriaBasedComparator());
        if (possibleTransformations.size() != 0) {
            return possibleTransformations;
        } else return null;
    }

    public static void printLeastPossibleWords(LemmaPredictionModel lpmodel) {
        HashMap<String, ArrayList<Transformation>> model = lpmodel.getModel();
        for (String s : model.keySet()) {
            model.get(s).forEach(a -> {
                if (a.getPossibility() < 0.001)
                    System.out.println(a.toString() + " Words:" + a.getSourceWord() + " -> " + a.getTransformedWord());
            });
        }
    }
}

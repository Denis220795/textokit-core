package com.textocat.morph.predictor.model.model;

import com.textocat.morph.predictor.model.utils.Transformation;
import com.textocat.morph.predictor.model.utils.comparators.NumberBasedComparator;
import com.textocat.morph.predictor.model.utils.comparators.PossibilityBasedComparator;
import com.textocat.textokit.morph.fs.SimplyWord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Денис on 02.04.2016.
 */
public class LemmaPredictionModel implements Serializable, ILemmaPredcitionModel {
    private static final long serialVersionUID = -2920304581672529098L;

    public int id;

    private HashMap<String, ArrayList<Transformation>> model;

    public LemmaPredictionModel(HashMap<String, ArrayList<Transformation>> model) {
        this.model = model;
    }

    public LemmaPredictionModel() {
    }

    public HashMap<String, ArrayList<Transformation>> getModel() {
        return model;
    }

    public void setModel(HashMap<String, ArrayList<Transformation>> model) {
        this.model = model;
    }

    public ArrayList<Transformation> getAllTransformationsFor(String partOfSpeech) {
        return model.get(partOfSpeech);
    }

    public void printModel() {
        for (String s : model.keySet()) {
            model.get(s).forEach(a -> {
                System.out.println(a.toString());
            });
        }
    }

    public Transformation getMostPossibleTransformation(String word, String partOfSpeech) {
        ArrayList<Transformation> possibleTransformations = new ArrayList<>();
        ArrayList<Transformation> transformations = model.get(partOfSpeech);
        for (Transformation temp : transformations) {
            if (temp.getFrom().length() < word.length()) {
                if (word.substring(word.length() - temp.getFrom().length()).equals(temp.getFrom())) {
                    possibleTransformations.add(temp);
                }
            }
        }
        possibleTransformations.sort(new PossibilityBasedComparator());
        if (possibleTransformations.size() != 0) {
            return possibleTransformations.get(0);
        } else return null;
    }

    public Transformation getMostPossibleTransformation(SimplyWord word) {
        ArrayList<Transformation> possibleTransformations = new ArrayList<>();
        ArrayList<Transformation> transformations = model.get(word.getPosTag());
        String text = word.getCoveredText();
        for (Transformation temp : transformations) {
            if (temp.getFrom().length() < text.length()) {
                if (text.substring(text.length() - temp.getFrom().length()).equals(temp.getFrom())) {
                    possibleTransformations.add(temp);
                }
            }
        }
        possibleTransformations.sort(new PossibilityBasedComparator());
        if (possibleTransformations.size() != 0) {
            return possibleTransformations.get(0);
        } else return null;
    }

    public ArrayList<Transformation> getAllPossibleTransformations(String word, String partOfSpeech) {
        ArrayList<Transformation> transformations = model.get(partOfSpeech);
        ArrayList<Transformation> result = new ArrayList<>();
        for (Transformation temp : transformations) {
            if (word.contains(temp.getFrom())) {
                result.add(temp);
            }
        }
        result.sort(new PossibilityBasedComparator());
        return result;
    }

    public void sortTransformations(Comparator comparator) {
        for (String partOfSpeech : model.keySet())
            model.get(partOfSpeech).sort(comparator);
    }

    public void laplaceSmoothing() {
        for (String s : model.keySet()) {
            model.get(s).forEach(a ->
            {
                if (a.getPossibility() != 1)
                    a.setPossibility(a.getPossibility() + 0.001);
            });
        }
    }

    public void goodTuringSmoothing() {

    }

    public void buildPossibilities() {
        for (String partOfSpeech : model.keySet()) {
            ArrayList<Transformation> transformations = model.get(partOfSpeech);
            for (Transformation transformation : transformations) {
                int total = 0;
                total += transformation.getNum();
                for (Transformation temp : transformations) {
                    if (!temp.equals(transformation) & temp.getFrom().equals(transformation.getFrom()) & !temp.getTo().equals(transformation.getTo())) {
                        total += temp.getNum();
                    }
                }
                double possibility;
                // округление до сотых
                //possibility = Math.rint(1000.0 * (double) transformation.getNum() / (double) total) / 1000.0;
                possibility = (double) transformation.getNum() / (double) total;
                transformation.setPossibility(possibility);
            }
        }
    }

    public void printLeastPossibleWords() {
        for (String s : model.keySet()) {
            model.get(s).forEach(a -> {
                if (a.getPossibility() < 0.001)
                    System.out.println(a.toString() + " Words:" + a.getSourceWord() + " -> " + a.getTransformedWord());
            });
        }
    }
}
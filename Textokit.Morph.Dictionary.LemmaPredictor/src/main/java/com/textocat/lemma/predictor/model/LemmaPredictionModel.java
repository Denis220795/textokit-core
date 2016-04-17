package com.textocat.lemma.predictor.model;

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
                possibility = (double) transformation.getNum() / (double) total;
                transformation.setPossibility(possibility);
            }
        }
    }
}
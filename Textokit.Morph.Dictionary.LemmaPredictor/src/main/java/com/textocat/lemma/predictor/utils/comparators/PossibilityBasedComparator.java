package com.textocat.lemma.predictor.utils.comparators;

import com.textocat.lemma.predictor.model.Transformation;

import java.util.Comparator;

/**
 * Created by Денис on 02.04.2016.
 */
public class PossibilityBasedComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        Transformation transformation1 = (Transformation) o1;
        Transformation transformation2 = (Transformation) o2;
        if (!transformation1.equals(transformation2)) {
            double dif = transformation1.getPossibility() - transformation2.getPossibility();
            //noinspection Duplicates
            if (dif < 0) return 1;
            else if (dif == 0) return 0;
            else return -1;
        } else return 0;
    }
}
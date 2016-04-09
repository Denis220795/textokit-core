package com.textocat.morph.predictor.model.utils.comparators;

import com.textocat.morph.predictor.model.utils.Transformation;

import java.util.Comparator;

/**
 * Created by Денис on 02.04.2016.
 */
public class NumberBasedComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        Transformation transformation1 = (Transformation) o1;
        Transformation transformation2 = (Transformation) o2;
        if (!transformation1.equals(transformation2)) {
            int dif = transformation1.getNum() - transformation2.getNum();
            if (dif == 0) return 0;
            else if (dif < 0) return 1;
            else return -1;
        } else return 0;
    }
}

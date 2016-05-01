package com.textocat.lemma.predictor.model.utils;

import com.textocat.lemma.predictor.model.Transformation;

/**
 * Created by Денис on 01.05.2016.
 */
public class WordsTransformationUtil {
    private WordsTransformationUtil() {
    }

    public static String getTransformedWord(Transformation transformation, String sourceWord) {
        String result;
        for (int i = sourceWord.length() - 1; i > 0; i--) {
            if (sourceWord.substring(i).equals(transformation.getFrom())) {
                result = sourceWord.substring(0, i) + transformation.getTo();
                return result;
            } else continue;
        }
        return null;
    }
}

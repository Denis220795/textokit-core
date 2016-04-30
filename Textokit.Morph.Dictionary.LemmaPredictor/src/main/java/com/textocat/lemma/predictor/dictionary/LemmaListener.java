package com.textocat.lemma.predictor.dictionary;

import com.google.common.collect.Multimap;
import com.textocat.lemma.predictor.model.LemmaPredictionModel;
import com.textocat.lemma.predictor.model.Transformation;
import com.textocat.lemma.predictor.utils.comparators.PossibilityBasedComparator;
import com.textocat.lemma.predictor.utils.io.IOModelUtil;
import com.textocat.textokit.morph.dictionary.resource.MorphDictionary;
import com.textocat.textokit.morph.model.Lemma;
import com.textocat.textokit.morph.model.Wordform;
import com.textocat.textokit.morph.opencorpora.resource.LemmaPostProcessor;

import java.io.IOException;
import java.util.*;

/**
 * Created by Денис on 24.02.2016.
 */
public class LemmaListener implements LemmaPostProcessor {

    private int i;
    private LemmaPredictionModel lemmaPredictionModel;
    private HashMap<String, ArrayList<Transformation>> model;

    public LemmaListener() {
        model = new HashMap<>();
        lemmaPredictionModel = new LemmaPredictionModel();
    }

    public boolean process(MorphDictionary dict, Lemma.Builder lemmaBuilder, Multimap<String, Wordform> wfMap) {
        i++;
        Transformation transformation;
        String lemma = lemmaBuilder.build().getString();
        String partOfSpeech = dict.getGramModel().toGramSet(lemmaBuilder.getGrammems()).get(0);
        if (!model.containsKey(partOfSpeech)) {
            model.put(partOfSpeech, new ArrayList<>());
        }
        lemma = lemma.replaceAll("ё", "е");
        String longestStem = getLongestStem(wfMap.asMap().keySet(), lemma);
        for (String s : wfMap.asMap().keySet()) {
//          s - формы леммы с разными окончаниями
            String from;
            String to;
            s = s.replaceAll("ё", "е");
            to = lemma.replace(longestStem, "");
            from = s.replace(longestStem, "");
            transformation = new Transformation(from, to, partOfSpeech);
            if (transformation != null) {
                transformation.setSourceWord(s);
                transformation.setTransformedWord(lemma);
                ArrayList<Transformation> concreteEndings = model.get(partOfSpeech);
                if (!concreteEndings.contains(transformation)) {
                    transformation.incrNum();
                    model.get(partOfSpeech).add(transformation);
                } else {
                    model.get(partOfSpeech).get(model.get(partOfSpeech).indexOf(transformation)).incrNum();
                }
            }

        }
        if (i % 10000 == 0) {
            System.out.println("Processed " + i);
        }
        return true;
    }

    public void dictionaryParsed(MorphDictionary dict) {
        System.out.println("Setting model to LemmaPredictionModel...");
        lemmaPredictionModel.setModel(model);
        System.out.println("Building possibilities for each transformation...");
        lemmaPredictionModel.buildPossibilities();
        System.out.println("Sorting transformations based on built possibilities ...");
        lemmaPredictionModel.sortTransformations(new PossibilityBasedComparator());
        try {
            IOModelUtil.writeModel(lemmaPredictionModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getLongestStem(Collection<String> words, String lemma) {
        lemma = lemma.replaceAll("ё", "е");
        String result;
        int i;
        for (i = 0; i < lemma.length(); i++) {
            boolean isEqual = true;
            for (String w : words) {
                w = w.replaceAll("ё", "е");
                if (i < w.length()) {
                    if (w.charAt(i) != lemma.charAt(i)) {
                        isEqual = false;
                        break;
                    } else continue;
                } else {
                    i = i - 1;
                    isEqual = false;
                    break;
                }
            }
            if (isEqual == false) break;
            else continue;
        }
        result = lemma.substring(0, i);
        return result;
    }
}
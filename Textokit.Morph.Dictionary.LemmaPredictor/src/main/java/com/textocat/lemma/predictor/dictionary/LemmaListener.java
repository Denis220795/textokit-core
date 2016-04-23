package com.textocat.lemma.predictor.dictionary;

import com.google.common.collect.Multimap;
import com.textocat.lemma.predictor.model.LemmaPredictionModel;
import com.textocat.lemma.predictor.model.Transformation;
import com.textocat.lemma.predictor.utils.comparators.NumberBasedComparator;
import com.textocat.lemma.predictor.utils.comparators.PossibilityBasedComparator;
import com.textocat.lemma.predictor.utils.io.IOModelUtil;
import com.textocat.textokit.morph.dictionary.resource.MorphDictionary;
import com.textocat.textokit.morph.model.Lemma;
import com.textocat.textokit.morph.model.Wordform;
import com.textocat.textokit.morph.opencorpora.resource.LemmaPostProcessor;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

import java.io.IOException;
import java.util.*;

/**
 * Created by Денис on 24.02.2016.
 */
public class LemmaListener implements LemmaPostProcessor {

    private int i;
    private SnowballStemmer stemmer;
    private LemmaPredictionModel lemmaPredictionModel;
    private HashMap<String, ArrayList<Transformation>> model;

    public LemmaListener() {
        model = new HashMap<>();
        lemmaPredictionModel = new LemmaPredictionModel();
        stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.RUSSIAN);
    }

    public boolean process(MorphDictionary dict, Lemma.Builder lemmaBuilder, Multimap<String, Wordform> wfMap) {
        // лемма
        i++;
        Transformation transformation = null;
        String lemma = lemmaBuilder.build().getString();
        String partOfSpeech = dict.getGramModel().toGramSet(lemmaBuilder.getGrammems()).get(0);
        if (!model.containsKey(partOfSpeech)) {
            model.put(partOfSpeech, new ArrayList<>());
        }
        lemma = lemma.replaceAll("ё", "е");

        for (String s : wfMap.asMap().keySet()) {
//          s - формы леммы с разными окончаниями
            String from;
            String to;
            s = s.replaceAll("ё", "е");
            int i = 0;
            while (i<s.length() && i<lemma.length() && lemma.charAt(i)==s.charAt(i)) {
                i++;
            }
                if (i > 0) {
                    to = lemma.substring(i);
                    from = s.substring(i);
                } else {
                    from = s.replace(stemmer.stem(s), "");
                    to = lemma.replace(stemmer.stem(lemma), "");
                }

            if (!lemma.equals(s))
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
        lemmaPredictionModel.setModel(model);
        lemmaPredictionModel.sortTransformations(new NumberBasedComparator());
        lemmaPredictionModel.buildPossibilities();
        lemmaPredictionModel.sortTransformations(new PossibilityBasedComparator());
        lemmaPredictionModel.printModel();
        try {
            IOModelUtil.writeModel(lemmaPredictionModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
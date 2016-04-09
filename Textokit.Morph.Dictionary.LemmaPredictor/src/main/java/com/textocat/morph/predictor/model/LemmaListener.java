package com.textocat.morph.predictor.model;

import com.google.common.collect.Multimap;
import com.textocat.morph.predictor.model.model.LemmaPredictionModel;
import com.textocat.morph.predictor.model.utils.Transformation;
import com.textocat.morph.predictor.model.utils.comparators.NumberBasedComparator;
import com.textocat.morph.predictor.model.utils.comparators.PossibilityBasedComparator;
import com.textocat.morph.predictor.model.utils.ioutils.IOModelUtil;
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
    SnowballStemmer stemmer;
    LemmaPredictionModel lemmaPredictionModel;
    private HashMap<String, ArrayList<Transformation>> model;

    public LemmaListener() {
        model = new HashMap();
        lemmaPredictionModel = new LemmaPredictionModel();
        stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.RUSSIAN);
    }

    public boolean process(MorphDictionary dict, Lemma.Builder lemmaBuilder, Multimap<String, Wordform> wfMap) {
        // лемма
        i++;
        Transformation transformation;
        String lemma = lemmaBuilder.build().getString();
        String partOfSpeech = dict.getGramModel().toGramSet(lemmaBuilder.getGrammems()).get(0);
        if (!model.containsKey(partOfSpeech)) {
            model.put(partOfSpeech, new ArrayList<>());
        }
        lemma.replaceAll("ё", "е");

        for (String s : wfMap.asMap().keySet()) {
//          s - формы леммы с разными окончаниями
            String from = "";
            String to = "";
            s.replaceAll("ё", "е");
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
            transformation = new Transformation(from, to, partOfSpeech);
            transformation.setSourceWord(s);
            transformation.setTransformedWord(lemma);
            ArrayList<Transformation> concreteEndings = model.get(partOfSpeech);
            if (!concreteEndings.contains(transformation)) {
                transformation.incrNum();
                model.get(partOfSpeech).add(transformation);
            } else {
                model.get(partOfSpeech).get(model.get(partOfSpeech).indexOf(transformation)).incrNum();
            }
//
//            Collection<Wordform> ll = wfMap.asMap().get(s);
//
//            for (Wordform wordform : ll) {
//                BitSet ww = wordform.getGrammems();
//                System.out.println(s + "^GramSet^"+dict.getGramModel().toGramSet(ww)); // выводит падежи
//                //System.out.println(">>>"+dict.getGramModel().toGramSet(gmPos));
//                //System.out.println(">>>"+dict.getGramModel().toGramSet(lemmaBuilder.getGrammems()));
//
//                // и прочую инфу, не выводит часть речи
//            }
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
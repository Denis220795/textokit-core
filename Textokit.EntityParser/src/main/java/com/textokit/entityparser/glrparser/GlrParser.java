package com.textokit.entityparser.glrparser;

import com.textocat.textokit.morph.fs.SimplyWord;
import com.textocat.textokit.segmentation.fstype.Sentence;
import com.textokit.entityparser.cfg.Grammatica;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import sun.security.jca.JCAUtil;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Денис on 25.06.2016.
 */
public class GlrParser {
    ArrayList<Grammatica> grammatica;
    private JCas jCas;
    private Collection<Sentence> sentences;

    public GlrParser(ArrayList<Grammatica> grammatica, JCas jCas, Collection<Sentence> sentences) {
        this.grammatica = grammatica;
        this.jCas = jCas;
        this.sentences = sentences;
    }

    public void process() {
        for (Sentence sentence : sentences) {
            Collection<SimplyWord> words = JCasUtil.selectCovered(SimplyWord.class, sentence);
            addAnnotationsIfFounded(words);
        }
    }

    private void addAnnotationsIfFounded(Collection<SimplyWord> simplyWords) {

    }
}

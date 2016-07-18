package com.textokit.entityparser.annotators;

import com.textocat.textokit.morph.fs.SimplyWord;
import com.textokit.entityparser.systempaths.SystemPaths;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

/**
 * Created by Денис on 25.06.2016.
 */
public class PhoneFeaturesMentionAnnotator extends JCasAnnotator_ImplBase {

    private ArrayList<String> features;
    private ArrayList<String> eval;
    private Scanner reader;

    @Override
    public void initialize(UimaContext aContext) throws ResourceInitializationException {
        features = new ArrayList<>();
        eval = new ArrayList<>();
        try {
            File featuresFile = new File(SystemPaths.resourcePath() + "features/features.txt");
            File evalFile = new File(SystemPaths.resourcePath() + "eval/eval.txt");
            reader = new Scanner(featuresFile);
            while (reader.hasNext()) {
                features.add(reader.next());
            }
            reader.close();
            reader = new Scanner(evalFile);
            while (reader.hasNext()) {
                eval.add(reader.next());
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(JCas jCas) throws AnalysisEngineProcessException {
        Collection<SimplyWord> words = JCasUtil.select(jCas, SimplyWord.class);
        for (SimplyWord sw : words) {
           // если принадлежит какой либо аннотации, то добавляем её
        }

    }
}
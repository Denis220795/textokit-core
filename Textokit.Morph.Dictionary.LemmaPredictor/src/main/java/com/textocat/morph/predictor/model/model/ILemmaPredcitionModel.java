package com.textocat.morph.predictor.model.model;

import com.textocat.morph.predictor.model.utils.Transformation;
import com.textocat.textokit.morph.fs.SimplyWord;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Денис on 06.04.2016.
 */
interface ILemmaPredcitionModel {

    HashMap<String, ArrayList<Transformation>> getModel();

    void setModel(HashMap<String, ArrayList<Transformation>> model);

    ArrayList<Transformation> getAllTransformationsFor(String partOfSpeech);

    void printModel();

    Transformation getMostPossibleTransformation(String word, String partOfSpeech);

    Transformation getMostPossibleTransformation(SimplyWord word);

    ArrayList<Transformation> getAllPossibleTransformations(String word, String partOfSpeech);

    void sortTransformations(Comparator comparator);

    void laplaceSmoothing();

    void goodTuringSmoothing();

    void buildPossibilities();
}

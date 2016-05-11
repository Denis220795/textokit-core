package com.textocat.lemma.predictor.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Денис on 06.04.2016.
 */
interface ILemmaPredictionModel {

    HashMap<String, ArrayList<Transformation>> getModel();

    void setModel(HashMap<String, ArrayList<Transformation>> model);

    void printModel();

    void sortTransformations(Comparator comparator);

    void buildPossibilities();
}

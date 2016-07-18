package com.textokit.entityparser.cfg;

import org.apache.uima.jcas.tcas.Annotation;

import java.util.ArrayList;

/**
 * Created by Денис on 25.06.2016.
 */
public class Grammatica {

    private Annotation leftContext;

    private ArrayList<Annotation> rightContext;

    public Grammatica(Annotation leftContext, ArrayList<Annotation> rightContext) {
        this.leftContext = leftContext;
        this.rightContext = rightContext;
    }

    public Annotation getLeftContext() {
        return leftContext;
    }

    public void setLeftContext(Annotation leftContext) {
        this.leftContext = leftContext;
    }

    public ArrayList<Annotation> getRightContext() {
        return rightContext;
    }

    public void setRightContext(ArrayList<Annotation> rightContext) {
        this.rightContext = rightContext;
    }
}
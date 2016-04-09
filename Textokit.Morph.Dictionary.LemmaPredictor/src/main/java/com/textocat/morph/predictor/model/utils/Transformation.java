package com.textocat.morph.predictor.model.utils;

import java.io.Serializable;

/**
 * Created by Денис on 24.02.2016.
 */
public class Transformation implements Serializable{
    private static final long serialVersionUID = 5227824473238329563L;

    private String from, to, type, sourceWord, transformedWord;
    private int num;
    private double possibility;

    public Transformation(String from, String to, String type) {
        this.from = from;
        this.to = to;
        this.type = type;
    }

    public Transformation(String from, String to) {
        this.from = from;
        this.to = to;
        this.type = "UNKNOWN";
    }

    public Transformation() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void incrNum () {
        this.num++;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPossibility() {
        return possibility;
    }

    public void setPossibility(double possibility) {
        this.possibility = possibility;
    }

    public String getSourceWord() {
        return sourceWord;
    }

    public void setSourceWord(String sourceWord) {
        this.sourceWord = sourceWord;
    }

    public String getTransformedWord() {
        return transformedWord;
    }

    public void setTransformedWord(String transformedWord) {
        this.transformedWord = transformedWord;
    }

    @Override
    public int hashCode() {
        return to.hashCode()+from.hashCode()+type.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
        {
            return false;
        }
        if (obj == this)
        {
            return true;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        Transformation ending = (Transformation) obj;
        return (this.from.equals(ending.from) && this.to.equals(ending.to) && this.type.equals(ending.type));
    }

    @Override
    public String toString() {
        return "Type: " + type + " " + from + " -> " + to + " Num: " + num + " Possibility: " + possibility;
    }
}

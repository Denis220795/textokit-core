package com.textocat.lemma.predictor.utils.csv.raw;

/**
 * Created by Денис on 02.05.2016.
 */
public class Record implements IRecord {
    String[] values;

    public Record() {
    }

    public Record(String[] values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return values;
    }

    @Override
    public void setValues(String[] values) {
        this.values = values;
    }

}

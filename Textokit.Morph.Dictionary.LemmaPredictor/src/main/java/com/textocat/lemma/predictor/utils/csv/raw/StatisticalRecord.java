package com.textocat.lemma.predictor.utils.csv.raw;

/**
 * Created by Денис on 30.04.2016.
 */
public class StatisticalRecord {
    String wordform, gold, predicted, status;

    public StatisticalRecord(String wordform, String gold, String predicted, String status) {
        this.wordform = wordform;
        this.gold = gold;
        this.predicted = predicted;
        this.status = status;
    }

    public String getWordform() {
        return wordform;
    }

    public String getGold() {
        return gold;
    }

    public String getPredicted() {
        return predicted;
    }

    public String getStatus() {
        return status;
    }
}

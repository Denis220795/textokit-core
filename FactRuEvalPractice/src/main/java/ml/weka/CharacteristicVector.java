package ml.weka;

import com.textocat.textokit.commons.io.StreamGobblerBase;
import com.textocat.textokit.morph.fs.SimplyWord;

import java.util.List;

/**
 * Created by Денис on 18.07.2016.
 */
public class CharacteristicVector {

    private String coveredText;
    private String lemma;
    private String posTag;
    private int position;
    private String suffixL1;
    private String suffixL2;
    private String suffixL3;
    private String affixL1;
    private String affixL2;
    private String affixL3;
    private String label;
    private int length;
    List<SimplyWord> left1Token;
    List<SimplyWord> left2Tokens;
    List<SimplyWord> left3Tokens;
    List<SimplyWord> right1Token;
    List<SimplyWord> right2Tokens;
    List<SimplyWord> right3Tokens;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getCoveredText() {
        return coveredText;
    }

    public void setCoveredText(String coveredText) {
        this.coveredText = coveredText;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public String getPosTag() {
        return posTag;
    }

    public void setPosTag(String posTag) {
        this.posTag = posTag;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getSuffixL1() {
        return suffixL1;
    }

    public void setSuffixL1(String suffixL1) {
        this.suffixL1 = suffixL1;
    }

    public String getSuffixL2() {
        return suffixL2;
    }

    public void setSuffixL2(String suffixL2) {
        this.suffixL2 = suffixL2;
    }

    public String getSuffixL3() {
        return suffixL3;
    }

    public void setSuffixL3(String suffixL3) {
        this.suffixL3 = suffixL3;
    }

    public String getAffixL1() {
        return affixL1;
    }

    public void setAffixL1(String affixL1) {
        this.affixL1 = affixL1;
    }

    public String getAffixL2() {
        return affixL2;
    }

    public void setAffixL2(String affixL2) {
        this.affixL2 = affixL2;
    }

    public String getAffixL3() {
        return affixL3;
    }

    public void setAffixL3(String affixL3) {
        this.affixL3 = affixL3;
    }

    public List<SimplyWord> getLeft1Token() {
        return left1Token;
    }

    public void setLeft1Token(List<SimplyWord> left1Token) {
        this.left1Token = left1Token;
    }

    public List<SimplyWord> getLeft2Tokens() {
        return left2Tokens;
    }

    public void setLeft2Tokens(List<SimplyWord> left2Tokens) {
        this.left2Tokens = left2Tokens;
    }

    public List<SimplyWord> getLeft3Tokens() {
        return left3Tokens;
    }

    public void setLeft3Tokens(List<SimplyWord> left3Tokens) {
        this.left3Tokens = left3Tokens;
    }

    public List<SimplyWord> getRight1Token() {
        return right1Token;
    }

    public void setRight1Token(List<SimplyWord> right1Token) {
        this.right1Token = right1Token;
    }

    public List<SimplyWord> getRight2Tokens() {
        return right2Tokens;
    }

    public void setRight2Tokens(List<SimplyWord> right2Tokens) {
        this.right2Tokens = right2Tokens;
    }

    public List<SimplyWord> getRight3Tokens() {
        return right3Tokens;
    }

    public void setRight3Tokens(List<SimplyWord> right3Tokens) {
        this.right3Tokens = right3Tokens;
    }

    @Override
    public String toString() {
        return "'" + coveredText + "'" + "," + "'" + lemma + "'" + "," + "'" + posTag + "'" + "," + position + "," +
                suffixL1 + "," + suffixL2 + "," + suffixL3 + "," + affixL1 + "," + affixL2 + "," + affixL3 + "," +
                "'" + mkStringFromList(left1Token) + "'" + "," + "'" + mkStringFromList(left2Tokens) + "'" + ","
                + "'" + mkStringFromList(left3Tokens) + "'" + "," + "'" + mkStringFromList(right1Token) + "'" + ","
                + "'" + mkStringFromList(right2Tokens) + "'" + "," + "'" + mkStringFromList(right3Tokens) + "'" + ","
                + length + "," + label;
    }

    private String mkStringFromList(List<SimplyWord> words) {
        String result = "";
        for (SimplyWord sw : words)
            result += sw.getCoveredText() + " ";
        return result;
    }
}

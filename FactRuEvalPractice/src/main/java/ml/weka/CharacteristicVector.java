package ml.weka;

import com.textocat.textokit.commons.io.StreamGobblerBase;
import com.textocat.textokit.morph.fs.SimplyWord;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Денис on 18.07.2016.
 * NOTE: all fields are public for using it in reflection Java API!!!
 */
public class CharacteristicVector {

    private int begin;
    public String coveredText;
    public String lemma;
    public String posTag;
    public int position;
    public String suffixL1;
    public String suffixL2;
    public String suffixL3;
    public String affixL1;
    public String affixL2;
    public String affixL3;
    public String left1Token;
    public String left2Tokens;
    public String left3Tokens;
    public String right1Token;
    public String right2Tokens;
    public String right3Tokens;
    public int length;
    private String bilouLabel;
    private String label;
    public boolean isCW;
    public boolean isNumeric;
    private String prevBilouLabel;
    private String nextBilouLabel;
    public String nextGrammems;
    public String prevGrammems;


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

    public String getLeft1Token() {
        return left1Token;
    }

    public void setLeft1Token(String left1Token) {
        this.left1Token = left1Token;
    }

    public String getLeft2Tokens() {
        return left2Tokens;
    }

    public void setLeft2Tokens(String left2Tokens) {
        this.left2Tokens = left2Tokens;
    }

    public String getLeft3Tokens() {
        return left3Tokens;
    }

    public void setLeft3Tokens(String left3Tokens) {
        this.left3Tokens = left3Tokens;
    }

    public String getRight1Token() {
        return right1Token;
    }

    public void setRight1Token(String right1Token) {
        this.right1Token = right1Token;
    }

    public String getRight2Tokens() {
        return right2Tokens;
    }

    public void setRight2Tokens(String right2Tokens) {
        this.right2Tokens = right2Tokens;
    }

    public String getRight3Tokens() {
        return right3Tokens;
    }

    public void setRight3Tokens(String right3Tokens) {
        this.right3Tokens = right3Tokens;
    }

    public boolean getIsCW() {
        return isCW;
    }

    public void setIsCW(boolean isCW) {
        this.isCW = isCW;
    }

    public boolean isCW() {
        return isCW;
    }

    public void setCW(boolean CW) {
        isCW = CW;
    }

    public boolean isNumeric() {
        return isNumeric;
    }

    public void setNumeric(boolean numeric) {
        isNumeric = numeric;
    }

    public String getPrevBilouLabel() {
        return prevBilouLabel;
    }

    public void setPrevBilouLabel(String prevBilouLabel) {
        this.prevBilouLabel = prevBilouLabel;
    }

    public String getNextBilouLabel() {
        return nextBilouLabel;
    }

    public void setNextBilouLabel(String nextBilouLabel) {
        this.nextBilouLabel = nextBilouLabel;
    }

    public String getprevGrammems() {
        return prevGrammems;
    }

    public void setprevGrammems(String prevGrammems) {
        this.prevGrammems = prevGrammems;
    }

    public String getNextGrammems() {
        return nextGrammems;
    }

    public void setNextGrammems(String nextGrammems) {
        this.nextGrammems = nextGrammems;
    }

    @Override
    public String toString() {
        String result = "";
        Class vector = this.getClass();
        Field[] publicFields = vector.getFields();
        for (Field field : publicFields) {
            try {
                result += "'" + String.valueOf(field.get(this)) + "'" + ",";
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        result += "'" + bilouLabel + "'";
        return result;
    }

    public String getBilouLabel() {
        return bilouLabel;
    }

    public void setBilouLabel(String bilouLabel) {
        this.bilouLabel = bilouLabel;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }
}

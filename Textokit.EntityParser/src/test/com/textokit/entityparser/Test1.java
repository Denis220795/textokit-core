package com.textokit.entityparser;

import com.textocat.textokit.morph.fs.SimplyWord;
import com.textocat.textokit.segmentation.fstype.Sentence;
import com.textokit.entityparser.cfg.Grammatica;

import java.lang.annotation.Annotation;
import java.util.ArrayList;

/**
 * Created by Денис on 30.06.2016.
 */
public class Test1 {
    public static void main(String[] args) {
        ArrayList<Grammatica> grammaticas = new ArrayList<>();
        ArrayList<Class> right_context = new ArrayList<>();
        Class left_context = SimplyWord.class;
        Class right_context_1 = Sentence.class;
        System.out.println(left_context.getTypeName());
        //    Grammatica g1 = new Grammatica(SimplyWord.class, annotations);
    }
}

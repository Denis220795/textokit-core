package com.textocat.lemma.predictor.utils.io;

/**
 * Created by Денис on 01.05.2016.
 */
public class SystemResources {
    public static String resourcePath() {
        return System.getProperty("user.dir") + "/src/main/resources/";
    }

    public static String sourcePath() {
        return System.getProperty("user.dir") + "/src/main/java/";
    }
}

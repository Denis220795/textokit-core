package com.textokit.entityparser.systempaths;

/**
 * Created by Денис on 25.06.2016.
 */
public class SystemPaths {
    public static String resourcePath() {
        return System.getProperty("user.dir") + "/src/main/resources/";
    }

    public static String sourcePath() {
        return System.getProperty("user.dir") + "/src/main/java/";
    }
}

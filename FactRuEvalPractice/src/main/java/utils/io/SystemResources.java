package utils.io;

/**
 * Created by Денис on 01.05.2016.
 */
public class SystemResources {
    public static String resourceDevSetPath() {
        return System.getProperty("user.dir") + "/FactRuEvalPractice/src/main/resources/devset";
    }

    public static String resourceTestSetPath() {
        return System.getProperty("user.dir") + "/FactRuEvalPractice/src/main/resources/testset";
    }

    public static String sourcePath() {
        return System.getProperty("user.dir") + "/FactRuEvalPractice/src/main/java/";
    }
}

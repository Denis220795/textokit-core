package com.textocat.morph.predictor.model;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.textocat.textokit.commons.cli.FileValueValidator;
import com.textocat.textokit.morph.opencorpora.resource.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * Created by Денис on 24.02.2016.
 */
public class DictionaryParserLauncher {

    @Parameter(names = {"-i", "--input-xml"}, required = true, validateValueWith = FileValueValidator.class)
    private File dictXmlFile;

    private DictionaryParserLauncher() {
    }

    public static void main(String[] args) throws Exception {
        DictionaryParserLauncher cfg = new DictionaryParserLauncher();
        new JCommander(cfg, args);

        MorphDictionaryImpl dict = new MorphDictionaryImpl();
        FileInputStream fis = FileUtils.openInputStream(cfg.dictXmlFile);
        try {
            DictionaryParser.parse(fis,new LemmaListener());
        } finally {
            IOUtils.closeQuietly(fis);
        }
    }
}
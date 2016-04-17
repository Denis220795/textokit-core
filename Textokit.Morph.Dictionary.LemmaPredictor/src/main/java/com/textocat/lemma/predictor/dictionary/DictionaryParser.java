package com.textocat.lemma.predictor.dictionary;

import com.textocat.textokit.morph.opencorpora.resource.*;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Денис on 24.02.2016.
 */
public class DictionaryParser {

    private MorphDictionaryImpl dict;
    private DictionaryExtension ext;
    private InputStream in;

    public DictionaryParser(MorphDictionaryImpl dict, DictionaryExtension ext, InputStream in) {
        this.dict = dict;
        this.ext = ext;
        this.in = in;
    }

    public void run() throws SAXException, IOException {
        SAXParser xmlParser;
        try {
            xmlParser = SAXParserFactory.newInstance().newSAXParser();
        } catch (ParserConfigurationException e) {
            // should never happen
            throw new IllegalStateException(e);
        }
        XMLReader xmlReader = xmlParser.getXMLReader();

        DictionaryXmlHandler dictHandler = new DictionaryXmlHandler(dict);

        dictHandler.addLemmaPostProcessor(new LemmaListener());

        xmlReader.setContentHandler(dictHandler);
    }

    public static MorphDictionaryImpl parse(InputStream in,
                                            final LemmaPostProcessor... lemmaPostProcessors)
            throws IOException, SAXException {
        return parse(in, new DictionaryExtensionBase() {

            @Override
            public List<LemmaPostProcessor> getLexemePostprocessors() {
                return Arrays.asList(lemmaPostProcessors);
            }
        });
    }

    public static MorphDictionaryImpl parse(InputStream in, DictionaryExtension ext)
            throws IOException, SAXException {
        MorphDictionaryImpl dict = new MorphDictionaryImpl();
        parse(dict, in, ext);
        return dict;
    }

    public static void parse(MorphDictionaryImpl dict, InputStream in, DictionaryExtension ext)
            throws IOException, SAXException {
        new XmlDictionaryParser(dict, ext, in).run();
    }
}
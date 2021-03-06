/*
 *    Copyright 2015 Textocat
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */


package com.textocat.textokit.commons.consumer;

import org.apache.commons.io.IOUtils;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.OperationalProperties;
import org.apache.uima.fit.descriptor.TypeCapability;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.XMLSerializer;
import org.xml.sax.SAXException;
import com.textocat.textokit.commons.DocumentMetadata;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A template class for annotator implementations that write XMI representation of an input CAS.
 *
 * @author Rinat Gareev (Textocat)
 */
@TypeCapability(inputs = "com.textocat.textokit.commons.DocumentMetadata")
@OperationalProperties(modifiesCas = false)
public abstract class XmiWriterBase extends JCasAnnotator_ImplBase {

    public static final String PARAM_XML_FORMATTED = "XmlFormatted";

    @ConfigurationParameter(name = PARAM_XML_FORMATTED, defaultValue = "true", mandatory = false)
    private boolean xmlFormatted;

    @Override
    public void process(JCas jCas) throws AnalysisEngineProcessException {
        DocumentMetadata meta = JCasUtil.selectSingle(jCas, DocumentMetadata.class);
        OutputStream out = null;
        try {
            out = getOutputStream(meta);
            writeXmi(jCas.getCas(), out);
        } catch (IOException | SAXException e) {
            throw new AnalysisEngineProcessException(e);
        } finally {
            afterXMIWritten(out);
        }
    }

    protected abstract OutputStream getOutputStream(DocumentMetadata meta) throws IOException;

    /**
     * Subclasses may override this to prevent closing the stream.
     *
     * @param out an output stream
     */
    protected void afterXMIWritten(OutputStream out) {
        IOUtils.closeQuietly(out);
    }

    private void writeXmi(CAS aCas, OutputStream out) throws IOException,
            SAXException {
        // seems like it is not necessary to buffer outputStream for SAX TransformationHandler
        // out = new BufferedOutputStream(out);
        XmiCasSerializer ser = new XmiCasSerializer(aCas.getTypeSystem());
        XMLSerializer xmlSer = new XMLSerializer(out, xmlFormatted);
        ser.serialize(aCas, xmlSer.getContentHandler());
    }
}
/**
 *
 */
package ru.kfu.itis.cll.uima.util;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.CasCreationUtils;
import org.junit.Test;
import ru.kfu.itis.cll.uima.commons.DocumentMetadata;

import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Rinat Gareev
 */
public class DocumentUtilsTest {

    private TypeSystemDescription inputTS = TypeSystemDescriptionFactory
            .createTypeSystemDescription(
                    "ru.kfu.itis.cll.uima.commons.Commons-TypeSystem");

    @Test
    public void setUriShouldFailIfThereIsNoMeta() throws ResourceInitializationException {
        CAS cas = CasCreationUtils.createCas(inputTS, null, null);
        cas.setDocumentText("Some text");
        assertEquals(null, DocumentUtils.getDocumentUri(cas));
        try {
            DocumentUtils.setDocumentUri(cas, "uri1", false);
        } catch (IllegalStateException e) {
            return;
        }
        fail();
    }

    @Test
    public void setUriShouldCreateIfThereIsNoMeta() throws ResourceInitializationException {
        CAS cas = CasCreationUtils.createCas(inputTS, null, null);
        cas.setDocumentText("Some text");
        assertEquals(null, DocumentUtils.getDocumentUri(cas));
        DocumentUtils.setDocumentUri(cas, "uri1", true);
        assertEquals("uri1", DocumentUtils.getDocumentUri(cas));
    }

    @Test
    public void setUriShouldChangeUri() throws ResourceInitializationException, CASException {
        CAS cas = CasCreationUtils.createCas(inputTS, null, null);
        {
            JCas jCas = cas.getJCas();
            DocumentMetadata dm = new DocumentMetadata(jCas, 0, 0);
            dm.setSourceUri("old_uri");
            dm.addToIndexes();
        }
        cas.setDocumentText("Some text");
        assertEquals("old_uri", DocumentUtils.getDocumentUri(cas));
        DocumentUtils.setDocumentUri(cas, "uri1", false);
        assertEquals("uri1", DocumentUtils.getDocumentUri(cas));
    }

    @Test
    public void setUriShouldFailIfNoTypeDefined() throws ResourceInitializationException {
        TypeSystemDescription inputTS = TypeSystemDescriptionFactory.createTypeSystemDescription();
        CAS cas = CasCreationUtils.createCas(inputTS, null, null);
        cas.setDocumentText("Some text");
        assertEquals(null, DocumentUtils.getDocumentUri(cas));
        try {
            DocumentUtils.setDocumentUri(cas, "uri1", true);
        } catch (IllegalStateException e) {
            return;
        }
        fail();
    }

    @Test
    public void testGetFilename() throws URISyntaxException {
        assertEquals("1.txt", DocumentUtils.getFilename("file:corpus/1.txt"));
        assertEquals("1.txt", DocumentUtils.getFilename("corpus/1.txt"));
        assertEquals("1.txt", DocumentUtils.getFilename("file:/corpus/1.txt"));
    }
}

/**
 *
 */
package ru.kfu.itis.cll.uima.io.axml;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.uima.UIMAException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.CasCreationUtils;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

/**
 * An application. Reads all {@code *.xml} files in the specified input
 * directory by {@link AXMLReader} and serialize resulting CASes in XMI format
 * into the specified output directory.
 * <p/>
 * If an output directory is not specified then it is assumed to be the same as
 * the input directory.
 *
 * @author Rinat Gareev
 */
public class AXML2XMI {

    @Parameter(names = {"-i", "--input-dir"}, required = true)
    private File inputDir;
    @Parameter(names = {"-ts"}, required = true,
            description = "Typesystem description name. "
                    + "UIMA mechanics assumes that it must be in the classpath or the UIMA data path")
    private String typeSystemDescName;
    @Parameter(names = {"-o", "--output-dir"}, required = false)
    private File outputDir;

    private AXML2XMI() {
    }

    public static void main(String[] args) throws UIMAException, IOException, SAXException {
        AXML2XMI launcher = new AXML2XMI();
        new JCommander(launcher, args);
        if (launcher.outputDir == null) {
            launcher.outputDir = launcher.inputDir;
        }
        launcher.run();
    }

    private void run() throws ResourceInitializationException, IOException, SAXException {
        Collection<File> inputFiles =
                FileUtils.listFiles(inputDir, FileFilterUtils.suffixFileFilter(".xml"), null);
        if (inputFiles.isEmpty()) {
            return;
        }
        TypeSystemDescription tsd = TypeSystemDescriptionFactory
                .createTypeSystemDescription(typeSystemDescName);
        CAS cas = CasCreationUtils.createCas(tsd, null, null);
        for (File inputFile : inputFiles) {
            AXMLReader.read(inputFile, cas);
            File outFile = getOutputFile(inputFile);
            OutputStream out = FileUtils.openOutputStream(outFile);
            try {
                XmiCasSerializer.serialize(cas, null, out, true, null);
            } finally {
                out.close();
                cas.reset();
            }
        }
    }

    private File getOutputFile(File inputFile) {
        String baseName = FilenameUtils.getBaseName(inputFile.getPath());
        return new File(outputDir, baseName + ".xmi");
    }
}

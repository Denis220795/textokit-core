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

package com.textocat.textokit.corpus.statistics.cpe;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.textocat.textokit.corpus.statistics.dao.corpus.XmiFileTreeCorpusDAO;
import com.textocat.textokit.segmentation.SentenceSplitterAPI;
import com.textocat.textokit.tokenizer.TokenizerAPI;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.factory.ExternalResourceFactory;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.fit.pipeline.JCasIterable;
import org.apache.uima.fit.util.CasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.CasCreationUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class UnitClassifierTest {
    String corpusPathString = Thread.currentThread().getContextClassLoader()
            .getResource("corpus_example").getPath();
    Set<String> unitTypes = Sets
            .newHashSet("com.textocat.textokit.tokenizer.fstype.W");
    Set<String> classTypes = Sets.newHashSet("ru.kfu.itis.issst.evex.Person",
            "ru.kfu.itis.issst.evex.Organization",
            "ru.kfu.itis.issst.evex.Weapon");
    ExternalResourceDescription daoDesc;
    TypeSystemDescription tsd;
    CollectionReaderDescription reader;
    AnalysisEngineDescription tokenizerSentenceSplitter;
    AnalysisEngineDescription unitAnnotator;
    AnalysisEngineDescription unitClassifier;

    @Before
    public void setUp() throws Exception {
        daoDesc = ExternalResourceFactory.createExternalResourceDescription(
                XmiFileTreeCorpusDAOResource.class, corpusPathString);
        tsd = CasCreationUtils
                .mergeTypeSystems(Sets.newHashSet(
                        XmiFileTreeCorpusDAO.getTypeSystem(corpusPathString),
                        TypeSystemDescriptionFactory
                                .createTypeSystemDescription(),
                        TokenizerAPI.getTypeSystemDescription(),
                        SentenceSplitterAPI.getTypeSystemDescription()));
        reader = CollectionReaderFactory.createReaderDescription(
                CorpusDAOCollectionReader.class, tsd,
                CorpusDAOCollectionReader.CORPUS_DAO_KEY, daoDesc);
        CAS aCAS = CasCreationUtils.createCas(tsd, null, null, null);
        tokenizerSentenceSplitter = Unitizer.createTokenizerSentenceSplitterAED();
        unitAnnotator = AnalysisEngineFactory.createEngineDescription(
                UnitAnnotator.class, UnitAnnotator.PARAM_UNIT_TYPE_NAMES,
                unitTypes);
        unitClassifier = AnalysisEngineFactory.createEngineDescription(
                UnitClassifier.class, UnitClassifier.PARAM_CLASS_TYPE_NAMES,
                classTypes);
    }

    @Test
    public void test() throws UIMAException, IOException {
        SetMultimap<String, String> unitsByClass = HashMultimap.create();
        for (JCas jCas : new JCasIterable(reader, tokenizerSentenceSplitter,
                unitAnnotator, unitClassifier)) {
            CAS aCas = jCas.getCas();
            Type unitType = aCas.getTypeSystem().getType(
                    UnitAnnotator.UNIT_TYPE_NAME);
            Feature classFeature = unitType
                    .getFeatureByBaseName(UnitClassifier.CLASS_FEAT_NAME);
            for (AnnotationFS unitAnnotation : CasUtil.select(aCas, unitType)) {
                if (unitAnnotation.getStringValue(classFeature) != null) {
                    unitsByClass.put(
                            unitAnnotation.getStringValue(classFeature),
                            unitAnnotation.getCoveredText());
                }
            }
        }
        assertEquals(
                Sets.newHashSet("Вагнер", "двое", "боевиков", "пособница"),
                unitsByClass.get("ru.kfu.itis.issst.evex.Person"));
        assertEquals(Sets.newHashSet("ЦСКА"),
                unitsByClass.get("ru.kfu.itis.issst.evex.Organization"));
        assertEquals(Sets.newHashSet("штурма", "квартиры"),
                unitsByClass.get("ru.kfu.itis.issst.evex.Weapon"));
    }

}

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

package com.textocat.textokit.postagger.opennlp;

import com.textocat.textokit.segmentation.fstype.Sentence;
import com.textocat.textokit.tokenizer.fstype.Token;
import opennlp.tools.cmdline.CmdLineUtil;
import opennlp.tools.ml.EventTrainer;
import opennlp.tools.ml.TrainerFactory;
import opennlp.tools.ml.model.Event;
import opennlp.tools.ml.model.MaxentModel;
import opennlp.tools.util.BeamSearchContextGenerator;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.TrainingParameters;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Rinat Gareev
 */
public class OpenNLPPosTaggerTrainer {

    // config fields
    private String languageCode;
    private File modelOutFile;
    private TrainingParameters trainParams;
    // derived
    private ObjectStream<Sentence> sentenceStream;
    private POSTaggerFactory taggerFactory;

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public File getModelOutFile() {
        return modelOutFile;
    }

    public void setModelOutFile(File modelOutFile) {
        this.modelOutFile = modelOutFile;
    }

    public TrainingParameters getTrainingParameters() {
        return trainParams;
    }

    public void setTrainingParameters(TrainingParameters trainParams) {
        this.trainParams = trainParams;
    }

    public POSTaggerFactory getTaggerFactory() {
        return taggerFactory;
    }

    public void setTaggerFactory(POSTaggerFactory taggerFactory) {
        this.taggerFactory = taggerFactory;
    }

    public ObjectStream<Sentence> getSentenceStream() {
        return sentenceStream;
    }

    public void setSentenceStream(ObjectStream<Sentence> sentenceStream) {
        this.sentenceStream = sentenceStream;
    }

    public void train() throws IOException {
        if (languageCode == null) {
            throw new IllegalStateException("languageCode is not provided");
        }
        if (modelOutFile == null) {
            throw new IllegalStateException("model output path is not provided");
        }
        if (trainParams == null) {
            throw new IllegalStateException("training parameters are not set");
        }
        if (sentenceStream == null) {
            throw new IllegalStateException("sentence stream is not configured");
        }
        if (taggerFactory == null) {
            throw new IllegalStateException("tagger factory is not configured");
        }
        Map<String, String> manifestInfoEntries = new HashMap<>();
        BeamSearchContextGenerator<Token> contextGenerator = taggerFactory.getContextGenerator();

        MaxentModel posModel;
        try {
            if (TrainerFactory.TrainerType.EVENT_MODEL_TRAINER.equals(
                    TrainerFactory.getTrainerType(trainParams.getSettings()))) {

                ObjectStream<Event> es = new POSTokenEventStream<>(sentenceStream, contextGenerator);
                EventTrainer trainer = TrainerFactory.getEventTrainer(trainParams.getSettings(), manifestInfoEntries);
                posModel = trainer.train(es);
            } else {
                throw new UnsupportedOperationException("Sequence training");
                //POSSampleSequenceStream ss = new POSSampleSequenceStream(samples, contextGenerator);
                // posModel = TrainUtil.train(ss, trainParams.getSettings(), manifestInfoEntries);
            }
        } finally {
            sentenceStream.close();
        }
        POSModel modelAggregate = new POSModel(languageCode,
                posModel, manifestInfoEntries, taggerFactory);
        CmdLineUtil.writeModel("PoS-tagger", modelOutFile, modelAggregate);
    }
}

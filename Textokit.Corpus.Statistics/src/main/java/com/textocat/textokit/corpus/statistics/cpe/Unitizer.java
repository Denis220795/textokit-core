package com.textocat.textokit.corpus.statistics.cpe;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;

import com.textocat.textokit.segmentation.SentenceSplitterAPI;
import com.textocat.textokit.tokenizer.TokenizerAPI;

public class Unitizer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static AnalysisEngineDescription createTokenizerSentenceSplitterAED()
			throws UIMAException, IOException {

		AnalysisEngineDescription tokenizerDesc = TokenizerAPI.getAEDescription();

		AnalysisEngineDescription ssDesc = SentenceSplitterAPI.getAEDescription();

		return AnalysisEngineFactory.createEngineDescription(tokenizerDesc, ssDesc);
	}
}

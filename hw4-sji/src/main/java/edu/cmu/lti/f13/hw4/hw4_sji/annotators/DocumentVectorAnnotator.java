package edu.cmu.lti.f13.hw4.hw4_sji.annotators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import edu.cmu.lti.f13.hw4.hw4_sji.typesystems.Document;
import edu.cmu.lti.f13.hw4.hw4_sji.typesystems.Token;
import edu.cmu.lti.f13.hw4.hw4_sji.utils.Utils;

public class DocumentVectorAnnotator extends JCasAnnotator_ImplBase {

    private Pattern tokenPattern = Pattern.compile("[\\w']+");
    private boolean removeStopWords;

    public void initialize(UimaContext aContext)
	    throws ResourceInitializationException {
	super.initialize(aContext);
	removeStopWords = (Boolean) aContext.getConfigParameterValue("removeStopWords");
    }

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {

	FSIterator<Annotation> iter = jcas.getAnnotationIndex().iterator();
	if (iter.isValid()) {
	    iter.moveToNext();
	    Document doc = (Document) iter.get();
	    createTermFreqVector(jcas, doc);
	}

    }

    /**
     * create term frequent vector
     * @param jcas
     * @param doc
     */

    private void createTermFreqVector(JCas jcas, Document doc) {

	String docText = doc.getText().toLowerCase();

	// TO DO: construct a vector of tokens and update the tokenList in CAS

	// save token and its frequency in a hashmap
	Map<String, Integer> vocab = new HashMap<String, Integer>();
	Matcher matcher = tokenPattern.matcher(docText);

	while (matcher.find()) {
	    String tok = matcher.group();
	    // check if the token is a stop word
	    if (!removeStopWords || !Utils.stopwords.contains(tok)) {
		if (!vocab.containsKey(tok)) {
		    vocab.put(tok, 1);
		} else {
		    int freq = vocab.get(tok);
		    vocab.put(tok, freq + 1);
		}
	    }
	}
	ArrayList<Token> tokenList = new ArrayList<Token>();
	for (String tok : vocab.keySet()) {
	    Token token = new Token(jcas);
	    token.setText(tok);
	    token.setFrequency(vocab.get(tok));
	    tokenList.add(token);
	}
	doc.setTokenList(Utils.fromCollectionToFSList(jcas, tokenList));
    }

}

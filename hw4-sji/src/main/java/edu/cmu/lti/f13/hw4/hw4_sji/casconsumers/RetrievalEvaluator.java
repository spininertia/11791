package edu.cmu.lti.f13.hw4.hw4_sji.casconsumers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.uima.UimaContext;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.util.ProcessTrace;

import edu.cmu.lti.f13.hw4.hw4_sji.typesystems.Document;
import edu.cmu.lti.f13.hw4.hw4_sji.typesystems.Token;
import edu.cmu.lti.f13.hw4.hw4_sji.utils.Utils;

public class RetrievalEvaluator extends CasConsumer_ImplBase {

    // document list
    public ArrayList<Sentence> docList = new ArrayList<RetrievalEvaluator.Sentence>();

    // query list
    public ArrayList<Sentence> queryList = new ArrayList<RetrievalEvaluator.Sentence>();
    
    private String similarityMeasure;

    /**
     * sentence - store relevant information of a sentence
     * 
     * @author Siping Ji <sipingji@cmu.edu>
     * 
     */
    public static class Sentence implements Comparable<Sentence> {
	int qId;
	int rel;
	Map<String, Integer> docVec;
	double score;
	String text;

	public Sentence(int qId, int rel, String text,
		Map<String, Integer> docVec, double score) {
	    this.qId = qId;
	    this.rel = rel;
	    this.docVec = docVec;
	    this.score = score;
	    this.text = text;
	}

	@Override
	public String toString() {
	    return String.format("qId = %d rel = %d %.4f %s", qId, rel, score,
		    text);
	}

	@Override
	public int compareTo(Sentence o) {
	    if (o.score - score > 0)
		return 1;
	    if (o.score - score < 0)
		return -1;
	    return 0;
	}

    }

    @Override
    public void initialize() throws ResourceInitializationException {
	super.initialize();
	UimaContext aContext = getUimaContext();

	similarityMeasure = (String) aContext.getConfigParameterValue("SimilarityMeasure");
    }

    /**
     * 1. construct the global word dictionary 2. keep the word
     * frequency for each sentence
     */
    @Override
    public void processCas(CAS aCas) throws ResourceProcessException {

	JCas jcas;
	try {
	    jcas = aCas.getJCas();
	} catch (CASException e) {
	    throw new ResourceProcessException(e);
	}

	FSIterator it = jcas.getAnnotationIndex(Document.type).iterator();

	if (it.hasNext()) {
	    Document doc = (Document) it.next();

	    // Make sure that your previous annotators have populated this in
	    // CAS
	    FSList fsTokenList = doc.getTokenList();
	    ArrayList<Token> tokenList = Utils.fromFSListToCollection(
		    fsTokenList, Token.class);

	    Map<String, Integer> docVec = new HashMap<String, Integer>();
	    for (Token token : tokenList) {
		docVec.put(token.getText(), token.getFrequency());
	    }
	    
	    // sotre relevant information about a sentence in the class Sentence
	    Sentence sentence = new Sentence(doc.getQueryID(),
		    doc.getRelevanceValue(), doc.getText(), docVec, 0);
	    if (sentence.rel == 99) {
		queryList.add(sentence);
	    } else
		docList.add(sentence);
	}

    }

    /**
     * 1. Compute Cosine Similarity and rank the retrieved sentences 2.
     * Compute the MRR metric
     */
    @Override
    public void collectionProcessComplete(ProcessTrace arg0)
	    throws ResourceProcessException, IOException {

	super.collectionProcessComplete(arg0);
	ArrayList<ArrayList<Sentence>> rankLists = new ArrayList<ArrayList<Sentence>>();

	// compute the cosine similarity measure
	// for each query
	for (Sentence query : queryList) {
	    ArrayList<Sentence> rankList = new ArrayList<Sentence>();
	    for (Sentence doc : docList) {
		if (query.qId == doc.qId) {
		    //compute similarity using different measure
		    if (similarityMeasure.equals("JaccardCoefficient"))
			doc.score = computeJaccardCoefficient(query.docVec,
			    doc.docVec); 
		    else if (similarityMeasure.equals("DiceCoefficient"))
			doc.score = computeDiceCoeficcient(query.docVec, doc.docVec);
		    else 
			doc.score = computeCosineSimilarity(query.docVec, doc.docVec);
		    rankList.add(doc);
		}
	    }
	    rankLists.add(rankList);
	}

	// compute the rank of retrieved sentences
	for (ArrayList<Sentence> list : rankLists) {
	    Collections.sort(list);
	}
	System.out.println("Similarity Measure:" + similarityMeasure);
	// compute the metric:: mean reciprocal rank
	double metric_mrr = compute_mrr(rankLists);
	for (ArrayList<Sentence> list : rankLists) {
	    for (Sentence sentence : list)
		System.out.println(sentence);
	    System.out.println();
	}
	System.out.println(" (MRR) Mean Reciprocal Rank ::" + metric_mrr);
    }

    /**
     * compute cosine similarity for two vectors
     * @return cosine_similarity
     */
    private double computeCosineSimilarity(Map<String, Integer> queryVector,
	    Map<String, Integer> docVector) {
	double cosine_similarity = 0.0;
	double length_q, length_d;
	length_q = vecLength(queryVector);
	length_d = vecLength(docVector);

	for (String tok : queryVector.keySet()) {
	    if (docVector.containsKey(tok)) {
		cosine_similarity += queryVector.get(tok) * docVector.get(tok);
	    }
	}
	cosine_similarity /= length_d * length_q;
	return cosine_similarity;
    }

    /**
     * compute Jacard coefficient as a similarity measure
     * 
     * @param queryVector
     * @param docVector
     * @return
     */
    private double computeJaccardCoefficient(Map<String, Integer> queryVector,
	    Map<String, Integer> docVector) {
	Set<String> uniqueToken = new HashSet<String>();
	int count = 0;
	for (String token : queryVector.keySet()) {
	    if (docVector.containsKey(token))
		count++;
	    uniqueToken.add(token);
	}
	for (String token : docVector.keySet()) {
	    uniqueToken.add(token);
	}

	return 1.0d * count / uniqueToken.size();
    }

    private double computeDiceCoeficcient(Map<String, Integer> queryVector,
	    Map<String, Integer> docVector) {
	int count = 0;
	for (String token : queryVector.keySet()) {
	    if (docVector.containsKey(token))
		count++;
	}

	return 2.0d * count / (queryVector.size() + docVector.size());

    }

    /**
     * compute vector length
     * 
     * @param vec
     * @return vector length
     */
    private double vecLength(Map<String, Integer> vec) {
	double length = 0;
	for (String tok : vec.keySet()) {
	    int freq = vec.get(tok);
	    length += freq * freq;
	}
	return Math.sqrt(length);
    }

    /**
     * compute mean reciprocal rank measure
     * 
     * @return mrr
     */
    private double compute_mrr(ArrayList<ArrayList<Sentence>> rankLists) {
	double metric_mrr = 0.0;
	for (ArrayList<Sentence> rankList : rankLists) {
	    int rank = 1;
	    for (Sentence sentence : rankList) {
		if (sentence.rel == 1)
		    break;
		rank++;
	    }
	    metric_mrr += 1.0d * 1 / rank;
	}
	metric_mrr /= rankLists.size();
	return metric_mrr;
    }

}

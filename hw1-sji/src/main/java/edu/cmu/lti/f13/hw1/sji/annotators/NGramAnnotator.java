package edu.cmu.lti.f13.hw1.sji.annotators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import edu.cmu.lti.f13.hw1.sji.NGram;
import edu.cmu.lti.f13.hw1.sji.Sentence;
import edu.cmu.lti.f13.hw1.sji.Token;

public class NGramAnnotator extends JCasAnnotator_ImplBase {

  private Pattern wordPattern = Pattern.compile("\\w");

  private int numberOfGrams;

  @Override
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);
    numberOfGrams = (Integer) aContext.getConfigParameterValue("NumberOfGrams");
  }

  @Override
  public void process(JCas jCas) throws AnalysisEngineProcessException {
    FSIndex sentenceIndex = jCas.getAnnotationIndex(Sentence.type);
    FSIndex tokenIndex = jCas.getAnnotationIndex(Token.type);
    Iterator sentenceIterator = sentenceIndex.iterator();
    while (sentenceIterator.hasNext()) {
      FSIterator tokenIterator = tokenIndex.iterator();
      Sentence sentence = (Sentence) sentenceIterator.next();
      Token token = null;
      while (tokenIterator.hasNext()) {
        token = (Token) tokenIterator.next();
        if (inSentecne(token, sentence)) {
          break;
        }
      }
      if (token != null && inSentecne(token, sentence)) {
        tokenIterator.moveToPrevious();
        ArrayList<Token> tokenArray = new ArrayList<Token>();
        // tokenArray.add(token);
        for (int i = 0; i < numberOfGrams - 1; i++) {
          if (tokenIterator.hasNext()) {
            token = (Token) tokenIterator.next();
            if (inSentecne(token, sentence))
              tokenArray.add(token);
          }
        }
        if (tokenArray.size() == numberOfGrams - 1) {
          int begin = 0;
          int end = tokenArray.size();
          while (tokenIterator.hasNext()) {
            token = (Token) tokenIterator.next();
            if (!inSentecne(token, sentence))
              break;
            tokenArray.add(token);
            NGram nGram = new NGram(jCas);
            nGram.setBegin(tokenArray.get(begin).getBegin());
            nGram.setEnd(tokenArray.get(end).getEnd());
            nGram.setCasProcessorId(getClass().getName());
            nGram.setConfidence(1.0f);
            FSArray elements = new FSArray(jCas, numberOfGrams);
            for (int i = 0; i < numberOfGrams; i++) {
              elements.set(i, tokenArray.get(begin + i));
            }
            nGram.setElements(elements);
            nGram.addToIndexes();
            begin++;
            end++;
          }
        }
      }
    }

  }

  public boolean inSentecne(Token token, Annotation sentence) {
    return (token.getBegin() >= sentence.getBegin() && token.getEnd() <= sentence.getEnd());
  }
}

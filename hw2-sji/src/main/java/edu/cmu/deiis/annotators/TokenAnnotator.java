package edu.cmu.deiis.annotators;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;

import edu.cmu.deiis.types.Sentence;
import edu.cmu.deiis.types.Token;

/**
 * This class annotates tokens inside question and answer annotations
 * 
 * @author Siping Ji <sipingji@cmu.edu>
 *
 */
public class TokenAnnotator extends JCasAnnotator_ImplBase {

  private Pattern tokenPattern = Pattern.compile("\\w+");

  @Override
  /*
   * (non-Javadoc)
   * @see org.apache.uima.analysis_component.JCasAnnotator_ImplBase#process(org.apache.uima.jcas.JCas)
   */
  public void process(JCas jCas) throws AnalysisEngineProcessException {
    FSIndex sentenceIndex = jCas.getAnnotationIndex(Sentence.type);
    Iterator sentenceIterator = sentenceIndex.iterator();
    while (sentenceIterator.hasNext()) {
      Sentence sentence = (Sentence) sentenceIterator.next();
      Matcher matcher = tokenPattern.matcher(sentence.getCoveredText());
      while (matcher.find()) {
        Token token = new Token(jCas);
        token.setBegin(sentence.getBegin() + matcher.start());
        token.setEnd(sentence.getBegin() + matcher.end());
        token.setCasProcessorId(this.getClass().getName());
        token.setConfidence(1.0f);
        token.addToIndexes();
      }
    }
  }

}

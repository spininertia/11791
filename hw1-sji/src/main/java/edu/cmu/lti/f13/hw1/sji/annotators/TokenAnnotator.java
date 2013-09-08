package edu.cmu.lti.f13.hw1.sji.annotators;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;

import edu.cmu.lti.f13.hw1.sji.Answer;
import edu.cmu.lti.f13.hw1.sji.Question;
import edu.cmu.lti.f13.hw1.sji.Sentence;
import edu.cmu.lti.f13.hw1.sji.Token;

public class TokenAnnotator extends JCasAnnotator_ImplBase {

  private Pattern tokenPattern = Pattern.compile("\\w+");

  @Override
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
//    FSIndex answerIndex = jCas.getAnnotationIndex(Answer.type);
//    Iterator answerIterator = answerIndex.iterator();
//    while (answerIterator.hasNext()) {
//      Answer answer = (Answer) answerIterator.next();
//      Matcher matcher = tokenPattern.matcher(answer.getCoveredText());
//      while (matcher.find()) {
//        Token token = new Token(jCas);
//        token.setBegin(answer.getBegin() + matcher.start());
//        token.setEnd(answer.getBegin() + matcher.end());
//        token.setCasProcessorId(this.getClass().getName());
//        token.setConfidence(1.0f);
//        token.addToIndexes();
//      }
//    }
  }

}

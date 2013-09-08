package edu.cmu.lti.f13.hw1.sji.annotators;

import java.util.Iterator;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import edu.cmu.lti.f13.hw1.sji.Answer;
import edu.cmu.lti.f13.hw1.sji.AnswerScore;
import edu.cmu.lti.f13.hw1.sji.NGram;
import edu.cmu.lti.f13.hw1.sji.Question;
import edu.cmu.lti.f13.hw1.sji.Token;

public class AnswerScoreAnnotator extends JCasAnnotator_ImplBase {

  @Override
  public void process(JCas jCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    nGramOverlapScorer(jCas);
  }

  public void tokenOverlapScorer(JCas jCas) {
    overlapScorer(jCas, Token.type);
  }

  public void nGramOverlapScorer(JCas jCas) {
    overlapScorer(jCas, NGram.type);
  }
  
  public void overlapScorer(JCas jCas, int type) {
    FSIndex questionIndex = jCas.getAnnotationIndex(Question.type);
    FSIndex answerIndex = jCas.getAnnotationIndex(Answer.type);
    FSIndex spanIndex = jCas.getAnnotationIndex(type);
    Iterator answerIterator = answerIndex.iterator();
    while (answerIterator.hasNext()) {
      Answer answer = (Answer) answerIterator.next();
      Iterator answerSpanIterator = spanIndex.iterator();
      int match, total;
      match = total = 0;
      while (answerSpanIterator.hasNext()) {
        Annotation answerToken = (Annotation) answerSpanIterator.next();
        if (inSentence(answerToken, answer)) {
          total++;
          Iterator questionIterator = questionIndex.iterator();
          while (questionIterator.hasNext()) {
            Question question = (Question) questionIterator.next();
            Iterator questionSpanIterator = spanIndex.iterator();
            while (questionSpanIterator.hasNext()) {
              Annotation questionSpan = (Annotation) questionSpanIterator.next();
              if (inSentence(questionSpan, question)
                      && answerToken.getCoveredText().equals(questionSpan.getCoveredText())) {
                match++;
              }
            }
          }
        }
      }
      AnswerScore answerScore = new AnswerScore(jCas);
      answerScore.setAnswer(answer);
      answerScore.setConfidence(1.0f);
      answerScore.setBegin(answer.getBegin());
      answerScore.setEnd(answer.getEnd());
      answerScore.setCasProcessorId(getClass().getName());
      answerScore.setScore(1.0f * match / total);
      answerScore.addToIndexes();
    }

  }
  
  public boolean inSentence(Annotation span, Annotation sentence) {
    return (span.getBegin() >= sentence.getBegin() && span.getEnd() <= sentence.getEnd());
  }

}

package edu.cmu.deiis.annotators;

import java.util.Iterator;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import edu.cmu.deiis.types.Answer;
import edu.cmu.deiis.types.AnswerScore;
import edu.cmu.deiis.types.NGram;
import edu.cmu.deiis.types.Question;
import edu.cmu.deiis.types.Token;

/**
 * AnswerScoreAnnotator - generate AnswerScore annotation. Assign Score to answer using NGram
 * overlap strategy or Token overlap strategy
 * 
 * @author Siping Ji
 * 
 */
public class AnswerScoreAnnotator extends JCasAnnotator_ImplBase {

  private String strategy; // strategy to score an answer

  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);
    strategy = (String) aContext.getConfigParameterValue("Strategy");
  }

  @Override
  public void process(JCas jCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    if (strategy.equals("NGramOverlap"))
      nGramOverlapScorer(jCas);
    else
      tokenOverlapScorer(jCas);
  }

  /**
   * token overlap scorer - the score is assigned according to the proportion that tokens in answer
   * overlaps with token in question
   * 
   * @param jCas
   */
  public void tokenOverlapScorer(JCas jCas) {
    overlapScorer(jCas, Token.type);
  }

  /**
   * nGram overlap scorer - the score is assigned according to the proportion that NGram in answer
   * overlaps with NGram in question
   * 
   * @param jCas
   */
  public void nGramOverlapScorer(JCas jCas) {
    overlapScorer(jCas, NGram.type);
  }

  /**
   * overlap scorer can be used by either token overlap answer scorer or NGram overlap scorer
   * 
   * @param jCas
   * @param type
   */
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

  /**
   * determine whether span is within the scope of sentence
   * @param span
   * @param sentence
   * @return true if span is within the scope of sentence, false otherwise
   */
  public boolean inSentence(Annotation span, Annotation sentence) {
    return (span.getBegin() >= sentence.getBegin() && span.getEnd() <= sentence.getEnd());
  }

}

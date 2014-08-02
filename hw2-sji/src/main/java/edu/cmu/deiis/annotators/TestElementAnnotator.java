package edu.cmu.deiis.annotators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import edu.cmu.deiis.types.Answer;
import edu.cmu.deiis.types.Question;
import edu.cmu.deiis.types.Sentence;

/**
 * This class annotates question, answer and sentences for a given document
 * 
 * @author Siping Ji <sipingji@cmu.edu>
 *
 */
public class TestElementAnnotator extends JCasAnnotator_ImplBase {

  //regular expression that match answer and questions
  private Pattern linePattern = Pattern.compile("([QA])\\s?([01]?)\\s(.*)\\r\\n");

  @Override
  /*
   * (non-Javadoc)
   * @see org.apache.uima.analysis_component.JCasAnnotator_ImplBase#process(org.apache.uima.jcas.JCas)
   */
  public void process(JCas jCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub

    String docTest = jCas.getDocumentText();
    // search for line of question or answer
    Matcher matcher = linePattern.matcher(docTest);
    while (matcher.find()) {
      if (matcher.group(1).equals("Q")) { //annotating questions
        Question question = new Question(jCas);
        question.setBegin(matcher.start(3));
        question.setEnd(matcher.end(3));
        question.setConfidence(1.0f);
        question.setCasProcessorId(this.getClass().getName());
        question.addToIndexes();
      } else if (matcher.group(1).equals("A")) { //annotating answers
        Answer answer = new Answer(jCas);
        answer.setBegin(matcher.start(3));
        answer.setEnd(matcher.end(3));
        answer.setConfidence(1.0f);
        answer.setCasProcessorId(this.getClass().getName());
        if (matcher.group(2).equals("1")) {
          answer.setIsCorrect(true);
        } else
          answer.setIsCorrect(false);
        answer.addToIndexes();
      }
      //annotating sentences
      Sentence sentence = new Sentence(jCas);
      sentence.setBegin(matcher.start(3));
      sentence.setEnd(matcher.end(3));
      sentence.setConfidence(1.0f);
      sentence.setCasProcessorId(this.getClass().getName());
      sentence.addToIndexes();

    }
  }

}

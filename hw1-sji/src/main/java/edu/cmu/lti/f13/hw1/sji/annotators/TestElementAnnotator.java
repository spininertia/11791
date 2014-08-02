package edu.cmu.lti.f13.hw1.sji.annotators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import edu.cmu.lti.f13.hw1.sji.Answer;
import edu.cmu.lti.f13.hw1.sji.Question;
import edu.cmu.lti.f13.hw1.sji.Sentence;

public class TestElementAnnotator extends JCasAnnotator_ImplBase {

   private Pattern linePattern = Pattern.compile("([QA])\\s?([01]?)\\s(.*)\\r\\n");
  
  @Override
  public void process(JCas jCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
   
    String docTest = jCas.getDocumentText();
    // search for line of question or answer
    Matcher matcher = linePattern.matcher(docTest);
    while(matcher.find()) {
      if(matcher.group(1).equals("Q")) {
        Question question = new Question(jCas);
        question.setBegin(matcher.start(3));
        question.setEnd(matcher.end(3));
        question.setConfidence(1.0f);
        question.setCasProcessorId(this.getClass().getName());
        question.addToIndexes();
      }
      else if(matcher.group(1).equals("A")) {
        Answer answer = new Answer(jCas);
        answer.setBegin(matcher.start(3));
        answer.setEnd(matcher.end(3));
        answer.setConfidence(1.0f);
        answer.setCasProcessorId(this.getClass().getName());
        if (matcher.group(2).equals("1")) {
          answer.setIsCorrect(true);
        }
        else 
          answer.setIsCorrect(false);
        answer.addToIndexes();
      }
      Sentence sentence = new Sentence(jCas);
      sentence.setBegin(matcher.start(3));
      sentence.setEnd(matcher.end(3));
      sentence.setConfidence(1.0f);
      sentence.setCasProcessorId(this.getClass().getName());
      sentence.addToIndexes();

    }
  }

}

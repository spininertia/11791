package edu.cmu.lti.f13.hw1.sji.annotators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import edu.cmu.lti.f13.hw1.sji.AnswerScore;
import edu.cmu.lti.f13.hw1.sji.Question;

public class Evaluator extends JCasAnnotator_ImplBase {

  private int evaluationPoint; // precision at N

  private String outputFileName;

  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);
    evaluationPoint = (Integer) aContext.getConfigParameterValue("EvaluationPoint");
    outputFileName = (String) aContext.getConfigParameterValue("OutputFileName");
  }

  @Override
  public void process(JCas jCas) throws AnalysisEngineProcessException {
    FSIndex answerScoreIndex = jCas.getAnnotationIndex(AnswerScore.type);
    FSIndex questionIndex = jCas.getAnnotationIndex(Question.type);

    Iterator questionIterator = questionIndex.iterator();
    Question question = null;
    if (questionIterator.hasNext())
      question = (Question) questionIterator.next();
    Iterator answerScoreIterator = answerScoreIndex.iterator();
    ArrayList<AnswerScore> answerScoreList = new ArrayList<AnswerScore>();
    while (answerScoreIterator.hasNext()) {
      answerScoreList.add((AnswerScore) answerScoreIterator.next());
    }
    Collections.sort(answerScoreList, new Comparator<AnswerScore>() {

      @Override
      public int compare(AnswerScore o1, AnswerScore o2) {
        if (o2.getScore() - o1.getScore() > 0)
          return 1;
        else if (o2.getScore() - o1.getScore() < 0)
          return -1;
        return 0;
      }

    });
    outputResult(answerScoreList, question);
  }

  public void outputResult(ArrayList<AnswerScore> resultList, Question question) {
    try {
      PrintStream printStream;
      if (outputFileName.equals("console"))
        printStream = System.out;
      else
        printStream = new PrintStream(new File(outputFileName));
      printStream.printf("Question: %s\n", question.getCoveredText());
      int matchN = 0;
      int i = 0;
      for (AnswerScore answerScore : resultList) {
        i++;
        if (answerScore.getAnswer().getIsCorrect()) {
          if (i <= evaluationPoint)
            matchN++;
        }
        printStream.printf("%s %.2f %s\n", answerScore.getAnswer().getIsCorrect() ? "+" : "-",
                answerScore.getScore(), answerScore.getAnswer().getCoveredText());
      }
      printStream.printf("Precision at %d: %f\n\n", evaluationPoint,
              getPrecision(matchN, evaluationPoint));
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public float getPrecision(int match, int total) {
    return 1.0f * match / total;
  }
}

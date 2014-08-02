package edu.cmu.deiis.annotators;

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

import edu.cmu.deiis.types.AnswerScore;
import edu.cmu.deiis.types.Question;

/**
 * This class rank answers according to score assigned by answerScorer and then evaluate the result
 * using precision metric
 * 
 * @author Siping Ji <sipingji@cmu.edu>
 * 
 */
public class Evaluator extends JCasAnnotator_ImplBase {

  private String outputFileName; // output file name

  private static int numDoc = 0; // number of Docs processed

  private static float totalScore = 0; //total precision

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.uima.analysis_component.AnalysisComponent_ImplBase#initialize(org.apache.uima.
   * UimaContext)
   */
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);
    outputFileName = (String) aContext.getConfigParameterValue("OutputFileName");
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see
   * org.apache.uima.analysis_component.JCasAnnotator_ImplBase#process(org.apache.uima.jcas.JCas)
   */
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
    numDoc++;
    outputResult(answerScoreList, question);
  }

  @SuppressWarnings("resource")
  /**
   * compute precision and print the result to output stream
   * @param resultList
   * @param question
   */
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
      int numCorrect = 0;
      for (AnswerScore answerScore : resultList) {
        if (answerScore.getAnswer().getIsCorrect())
          numCorrect++;
        printStream.printf("%s %.2f %s\n", answerScore.getAnswer().getIsCorrect() ? "+" : "-",
                answerScore.getScore(), answerScore.getAnswer().getCoveredText());
      }
      for (AnswerScore answerScore : resultList) {
        i++;
        if (answerScore.getAnswer().getIsCorrect()) {
          matchN++;
        }
        if (i == numCorrect)
          break;
      }
      float precision = getPrecision(matchN, numCorrect);
      totalScore += precision;
      printStream.printf("Precision at %d: %.2f\n\n", numCorrect, getPrecision(matchN, numCorrect));
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * calculate precision
   * 
   * @param match
   * @param total
   * @return precision
   */
  public float getPrecision(int match, int total) {
    return 1.0f * match / total;
  }

  @Override
  /*
   * (non-Javadoc)
   * @see org.apache.uima.analysis_component.AnalysisComponent_ImplBase#destroy()
   */
  public void destroy() {
    float ave_precision = totalScore / numDoc;
    PrintStream printStream = null;
    if (outputFileName.equals("console"))
      printStream = System.out;
    else
      try {
        printStream = new PrintStream(new File(outputFileName));
      } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    printStream.printf("Average Precision: %.2f", ave_precision);

  }
}

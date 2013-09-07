package edu.cmu.lti.f13.hw1.sji.annotators;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

public class TestElementAnnotator extends JCasAnnotator_ImplBase {

  @Override
  public void process(JCas cas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    String docTest = cas.getDocumentText();
    
  }

}

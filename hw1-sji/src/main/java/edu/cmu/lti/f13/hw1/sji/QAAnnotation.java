
/* First created by JCasGen Fri Sep 06 20:34:47 EDT 2013 */
package edu.cmu.lti.f13.hw1.sji;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;

/**
 * Base class for all other annotation types in this type system. All other annotation classes
 * inherit this class so that all types contain confidence and casprocessorId features Updated by
 * JCasGen Tue Sep 10 20:42:20 EDT 2013 XML source:
 * /Users/Chris/git/hw1-sji/hw1-sji/src/main/resources/hw1-sji-typesystem.xml
 * 
 * @generated
 */
public class QAAnnotation extends Annotation {
  /**
   * @generated
   * @ordered
   */
  @SuppressWarnings("hiding")
  public final static int typeIndexID = JCasRegistry.register(QAAnnotation.class);

  /**
   * @generated
   * @ordered
   */
  @SuppressWarnings("hiding")
  public final static int type = typeIndexID;

  /** @generated */
  @Override
  public int getTypeIndexID() {return typeIndexID;}
 
  /**
   * Never called. Disable default constructor
   * 
   * @generated
   */
  protected QAAnnotation() {/* intentionally empty block */}
    
  /**
   * Internal - constructor used by generator
   * 
   * @generated
   */
  public QAAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public QAAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */
  public QAAnnotation(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc --> Write your own initialization here <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/* default - does nothing empty block */
  }

  // *--------------*
  // * Feature: confidence

  /**
   * getter for confidence - gets the confidence score assigned to the annotation by the component
   * 
   * @generated
   */
  public float getConfidence() {
    if (QAAnnotation_Type.featOkTst && ((QAAnnotation_Type)jcasType).casFeat_confidence == null)
      jcasType.jcas.throwFeatMissing("confidence", "edu.cmu.lti.f13.hw1.sji.QAAnnotation");
    return jcasType.ll_cas.ll_getFloatValue(addr, ((QAAnnotation_Type)jcasType).casFeatCode_confidence);}
    
  /**
   * setter for confidence - sets the confidence score assigned to the annotation by the component
   * 
   * @generated
   */
  public void setConfidence(float v) {
    if (QAAnnotation_Type.featOkTst && ((QAAnnotation_Type)jcasType).casFeat_confidence == null)
      jcasType.jcas.throwFeatMissing("confidence", "edu.cmu.lti.f13.hw1.sji.QAAnnotation");
    jcasType.ll_cas.ll_setFloatValue(addr, ((QAAnnotation_Type)jcasType).casFeatCode_confidence, v);}    
   
    
  // *--------------*
  // * Feature: casProcessorId

  /**
   * getter for casProcessorId - gets the name of the component that produced the annotation
   * 
   * @generated
   */
  public String getCasProcessorId() {
    if (QAAnnotation_Type.featOkTst && ((QAAnnotation_Type)jcasType).casFeat_casProcessorId == null)
      jcasType.jcas.throwFeatMissing("casProcessorId", "edu.cmu.lti.f13.hw1.sji.QAAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((QAAnnotation_Type)jcasType).casFeatCode_casProcessorId);}
    
  /**
   * setter for casProcessorId - sets the name of the component that produced the annotation
   * 
   * @generated
   */
  public void setCasProcessorId(String v) {
    if (QAAnnotation_Type.featOkTst && ((QAAnnotation_Type)jcasType).casFeat_casProcessorId == null)
      jcasType.jcas.throwFeatMissing("casProcessorId", "edu.cmu.lti.f13.hw1.sji.QAAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((QAAnnotation_Type)jcasType).casFeatCode_casProcessorId, v);}    
  }

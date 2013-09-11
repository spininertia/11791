/* First created by JCasGen Fri Sep 06 20:34:47 EDT 2013 */
package edu.cmu.lti.f13.hw1.sji;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/**
 * Base class for all other annotation types in this type system. All other annotation classes
 * inherit this class so that all types contain confidence and casprocessorId features Updated by
 * JCasGen Tue Sep 10 20:42:20 EDT 2013
 * 
 * @generated
 */
public class QAAnnotation_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (QAAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = QAAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new QAAnnotation(addr, QAAnnotation_Type.this);
  			   QAAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new QAAnnotation(addr, QAAnnotation_Type.this);
  	  }
    };

  /** @generated */
  @SuppressWarnings("hiding")
  public final static int typeIndexID = QAAnnotation.typeIndexID;

  /**
   * @generated
   * @modifiable
   */
  @SuppressWarnings("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("edu.cmu.lti.f13.hw1.sji.QAAnnotation");

  /** @generated */
  final Feature casFeat_confidence;

  /** @generated */
  final int casFeatCode_confidence;

  /** @generated */
  public float getConfidence(int addr) {
        if (featOkTst && casFeat_confidence == null)
      jcas.throwFeatMissing("confidence", "edu.cmu.lti.f13.hw1.sji.QAAnnotation");
    return ll_cas.ll_getFloatValue(addr, casFeatCode_confidence);
  }
  /** @generated */
  public void setConfidence(int addr, float v) {
        if (featOkTst && casFeat_confidence == null)
      jcas.throwFeatMissing("confidence", "edu.cmu.lti.f13.hw1.sji.QAAnnotation");
    ll_cas.ll_setFloatValue(addr, casFeatCode_confidence, v);}
    
  
 
  /** @generated */
  final Feature casFeat_casProcessorId;

  /** @generated */
  final int casFeatCode_casProcessorId;

  /** @generated */
  public String getCasProcessorId(int addr) {
        if (featOkTst && casFeat_casProcessorId == null)
      jcas.throwFeatMissing("casProcessorId", "edu.cmu.lti.f13.hw1.sji.QAAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_casProcessorId);
  }
  /** @generated */
  public void setCasProcessorId(int addr, String v) {
        if (featOkTst && casFeat_casProcessorId == null)
      jcas.throwFeatMissing("casProcessorId", "edu.cmu.lti.f13.hw1.sji.QAAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_casProcessorId, v);}
    
  



  /**
   * initialize variables to correspond with Cas Type and Features
   * 
   * @generated
   */
  public QAAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_confidence = jcas.getRequiredFeatureDE(casType, "confidence", "uima.cas.Float", featOkTst);
    casFeatCode_confidence  = (null == casFeat_confidence) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_confidence).getCode();

 
    casFeat_casProcessorId = jcas.getRequiredFeatureDE(casType, "casProcessorId", "uima.cas.String", featOkTst);
    casFeatCode_casProcessorId  = (null == casFeat_casProcessorId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_casProcessorId).getCode();

  }
}

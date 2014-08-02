
/* First created by JCasGen Sat Sep 07 22:16:04 EDT 2013 */
package edu.cmu.lti.f13.hw1.sji;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

/**
 * This type refer to a sentence annotated in the user input. It's actually the joint of question
 * and answer annotation. This type makes it easy for annotators like token and ngram annotators to
 * treat question and answers uniformally. Updated by JCasGen Tue Sep 10 20:42:20 EDT 2013 XML
 * source: /Users/Chris/git/hw1-sji/hw1-sji/src/main/resources/hw1-sji-typesystem.xml
 * 
 * @generated
 */
public class Sentence extends QAAnnotation {
  /**
   * @generated
   * @ordered
   */
  @SuppressWarnings("hiding")
  public final static int typeIndexID = JCasRegistry.register(Sentence.class);

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
  protected Sentence() {/* intentionally empty block */}
    
  /**
   * Internal - constructor used by generator
   * 
   * @generated
   */
  public Sentence(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Sentence(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */
  public Sentence(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc --> Write your own initialization here <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/* default - does nothing empty block */
  }

}

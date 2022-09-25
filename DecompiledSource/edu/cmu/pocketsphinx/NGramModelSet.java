package edu.cmu.pocketsphinx;

import java.util.Iterator;

public class NGramModelSet implements Iterable<NGramModel> {
  protected transient boolean swigCMemOwn;
  
  private transient long swigCPtr;
  
  protected NGramModelSet(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  public NGramModelSet(Config paramConfig, LogMath paramLogMath, String paramString) {
    this(SphinxBaseJNI.new_NGramModelSet(Config.getCPtr(paramConfig), paramConfig, LogMath.getCPtr(paramLogMath), paramLogMath, paramString), true);
  }
  
  protected static long getCPtr(NGramModelSet paramNGramModelSet) {
    long l;
    if (paramNGramModelSet == null) {
      l = 0L;
    } else {
      l = paramNGramModelSet.swigCPtr;
    } 
    return l;
  }
  
  public NGramModel add(NGramModel paramNGramModel, String paramString, float paramFloat, boolean paramBoolean) {
    long l = SphinxBaseJNI.NGramModelSet_add(this.swigCPtr, this, NGramModel.getCPtr(paramNGramModel), paramNGramModel, paramString, paramFloat, paramBoolean);
    if (l == 0L) {
      paramNGramModel = null;
    } else {
      paramNGramModel = new NGramModel(l, false);
    } 
    return paramNGramModel;
  }
  
  public int count() {
    return SphinxBaseJNI.NGramModelSet_count(this.swigCPtr, this);
  }
  
  public String current() {
    return SphinxBaseJNI.NGramModelSet_current(this.swigCPtr, this);
  }
  
  public void delete() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield swigCPtr : J
    //   6: lconst_0
    //   7: lcmp
    //   8: ifeq -> 35
    //   11: aload_0
    //   12: getfield swigCMemOwn : Z
    //   15: ifeq -> 30
    //   18: aload_0
    //   19: iconst_0
    //   20: putfield swigCMemOwn : Z
    //   23: aload_0
    //   24: getfield swigCPtr : J
    //   27: invokestatic delete_NGramModelSet : (J)V
    //   30: aload_0
    //   31: lconst_0
    //   32: putfield swigCPtr : J
    //   35: aload_0
    //   36: monitorexit
    //   37: return
    //   38: astore_1
    //   39: aload_0
    //   40: monitorexit
    //   41: aload_1
    //   42: athrow
    // Exception table:
    //   from	to	target	type
    //   2	30	38	finally
    //   30	35	38	finally
  }
  
  protected void finalize() {
    delete();
  }
  
  public NGramModelSetIterator iterator() {
    NGramModelSetIterator nGramModelSetIterator;
    long l = SphinxBaseJNI.NGramModelSet_iterator(this.swigCPtr, this);
    if (l == 0L) {
      nGramModelSetIterator = null;
    } else {
      nGramModelSetIterator = new NGramModelSetIterator(l, true);
    } 
    return nGramModelSetIterator;
  }
  
  public NGramModel lookup(String paramString) {
    NGramModel nGramModel;
    long l = SphinxBaseJNI.NGramModelSet_lookup(this.swigCPtr, this, paramString);
    if (l == 0L) {
      paramString = null;
    } else {
      nGramModel = new NGramModel(l, false);
    } 
    return nGramModel;
  }
  
  public NGramModel select(String paramString) {
    NGramModel nGramModel;
    long l = SphinxBaseJNI.NGramModelSet_select(this.swigCPtr, this, paramString);
    if (l == 0L) {
      paramString = null;
    } else {
      nGramModel = new NGramModel(l, false);
    } 
    return nGramModel;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/edu/cmu/pocketsphinx/NGramModelSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package edu.cmu.pocketsphinx;

public class NGramModel {
  protected transient boolean swigCMemOwn;
  
  private transient long swigCPtr;
  
  protected NGramModel(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  public NGramModel(Config paramConfig, LogMath paramLogMath, String paramString) {
    this(SphinxBaseJNI.new_NGramModel__SWIG_1(Config.getCPtr(paramConfig), paramConfig, LogMath.getCPtr(paramLogMath), paramLogMath, paramString), true);
  }
  
  public NGramModel(String paramString) {
    this(SphinxBaseJNI.new_NGramModel__SWIG_0(paramString), true);
  }
  
  public static NGramModel fromIter(SWIGTYPE_p_void paramSWIGTYPE_p_void) {
    NGramModel nGramModel;
    long l = SphinxBaseJNI.NGramModel_fromIter(SWIGTYPE_p_void.getCPtr(paramSWIGTYPE_p_void));
    if (l == 0L) {
      paramSWIGTYPE_p_void = null;
    } else {
      nGramModel = new NGramModel(l, false);
    } 
    return nGramModel;
  }
  
  protected static long getCPtr(NGramModel paramNGramModel) {
    long l;
    if (paramNGramModel == null) {
      l = 0L;
    } else {
      l = paramNGramModel.swigCPtr;
    } 
    return l;
  }
  
  public int addWord(String paramString, float paramFloat) {
    return SphinxBaseJNI.NGramModel_addWord(this.swigCPtr, this, paramString, paramFloat);
  }
  
  public void casefold(int paramInt) {
    SphinxBaseJNI.NGramModel_casefold(this.swigCPtr, this, paramInt);
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
    //   27: invokestatic delete_NGramModel : (J)V
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
  
  public int prob(String[] paramArrayOfString) {
    return SphinxBaseJNI.NGramModel_prob(this.swigCPtr, this, paramArrayOfString);
  }
  
  public int size() {
    return SphinxBaseJNI.NGramModel_size(this.swigCPtr, this);
  }
  
  public int strToType(String paramString) {
    return SphinxBaseJNI.NGramModel_strToType(this.swigCPtr, this, paramString);
  }
  
  public String typeToStr(int paramInt) {
    return SphinxBaseJNI.NGramModel_typeToStr(this.swigCPtr, this, paramInt);
  }
  
  public void write(String paramString, int paramInt) {
    SphinxBaseJNI.NGramModel_write(this.swigCPtr, this, paramString, paramInt);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/edu/cmu/pocketsphinx/NGramModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
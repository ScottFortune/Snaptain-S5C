package edu.cmu.pocketsphinx;

public class FsgModel {
  protected transient boolean swigCMemOwn;
  
  private transient long swigCPtr;
  
  protected FsgModel(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  public FsgModel(String paramString, LogMath paramLogMath, float paramFloat) {
    this(SphinxBaseJNI.new_FsgModel__SWIG_1(paramString, LogMath.getCPtr(paramLogMath), paramLogMath, paramFloat), true);
  }
  
  public FsgModel(String paramString, LogMath paramLogMath, float paramFloat, int paramInt) {
    this(SphinxBaseJNI.new_FsgModel__SWIG_0(paramString, LogMath.getCPtr(paramLogMath), paramLogMath, paramFloat, paramInt), true);
  }
  
  protected static long getCPtr(FsgModel paramFsgModel) {
    long l;
    if (paramFsgModel == null) {
      l = 0L;
    } else {
      l = paramFsgModel.swigCPtr;
    } 
    return l;
  }
  
  public int addAlt(String paramString1, String paramString2) {
    return SphinxBaseJNI.FsgModel_addAlt(this.swigCPtr, this, paramString1, paramString2);
  }
  
  public int addSilence(String paramString, int paramInt, float paramFloat) {
    return SphinxBaseJNI.FsgModel_addSilence(this.swigCPtr, this, paramString, paramInt, paramFloat);
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
    //   27: invokestatic delete_FsgModel : (J)V
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
  
  public int nullTransAdd(int paramInt1, int paramInt2, int paramInt3) {
    return SphinxBaseJNI.FsgModel_nullTransAdd(this.swigCPtr, this, paramInt1, paramInt2, paramInt3);
  }
  
  public int tagTransAdd(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    return SphinxBaseJNI.FsgModel_tagTransAdd(this.swigCPtr, this, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public void transAdd(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    SphinxBaseJNI.FsgModel_transAdd(this.swigCPtr, this, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public int wordAdd(String paramString) {
    return SphinxBaseJNI.FsgModel_wordAdd(this.swigCPtr, this, paramString);
  }
  
  public int wordId(String paramString) {
    return SphinxBaseJNI.FsgModel_wordId(this.swigCPtr, this, paramString);
  }
  
  public void writefile(String paramString) {
    SphinxBaseJNI.FsgModel_writefile(this.swigCPtr, this, paramString);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/edu/cmu/pocketsphinx/FsgModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package edu.cmu.pocketsphinx;

public class Hypothesis {
  protected transient boolean swigCMemOwn;
  
  private transient long swigCPtr;
  
  protected Hypothesis(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  public Hypothesis(String paramString, int paramInt1, int paramInt2) {
    this(PocketSphinxJNI.new_Hypothesis(paramString, paramInt1, paramInt2), true);
  }
  
  protected static long getCPtr(Hypothesis paramHypothesis) {
    long l;
    if (paramHypothesis == null) {
      l = 0L;
    } else {
      l = paramHypothesis.swigCPtr;
    } 
    return l;
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
    //   27: invokestatic delete_Hypothesis : (J)V
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
  
  public int getBestScore() {
    return PocketSphinxJNI.Hypothesis_bestScore_get(this.swigCPtr, this);
  }
  
  public String getHypstr() {
    return PocketSphinxJNI.Hypothesis_hypstr_get(this.swigCPtr, this);
  }
  
  public int getProb() {
    return PocketSphinxJNI.Hypothesis_prob_get(this.swigCPtr, this);
  }
  
  public void setBestScore(int paramInt) {
    PocketSphinxJNI.Hypothesis_bestScore_set(this.swigCPtr, this, paramInt);
  }
  
  public void setHypstr(String paramString) {
    PocketSphinxJNI.Hypothesis_hypstr_set(this.swigCPtr, this, paramString);
  }
  
  public void setProb(int paramInt) {
    PocketSphinxJNI.Hypothesis_prob_set(this.swigCPtr, this, paramInt);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/edu/cmu/pocketsphinx/Hypothesis.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
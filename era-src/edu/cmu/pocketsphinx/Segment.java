package edu.cmu.pocketsphinx;

public class Segment {
  protected transient boolean swigCMemOwn;
  
  private transient long swigCPtr;
  
  public Segment() {
    this(PocketSphinxJNI.new_segment(), true);
  }
  
  protected Segment(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  public static Segment fromIter(SWIGTYPE_p_void paramSWIGTYPE_p_void) {
    Segment segment;
    long l = PocketSphinxJNI.Segment_fromIter(SWIGTYPE_p_void.getCPtr(paramSWIGTYPE_p_void));
    if (l == 0L) {
      paramSWIGTYPE_p_void = null;
    } else {
      segment = new Segment(l, false);
    } 
    return segment;
  }
  
  protected static long getCPtr(Segment paramSegment) {
    long l;
    if (paramSegment == null) {
      l = 0L;
    } else {
      l = paramSegment.swigCPtr;
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
    //   27: invokestatic delete_Segment : (J)V
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
  
  public int getAscore() {
    return PocketSphinxJNI.Segment_ascore_get(this.swigCPtr, this);
  }
  
  public int getEndFrame() {
    return PocketSphinxJNI.Segment_endFrame_get(this.swigCPtr, this);
  }
  
  public int getLback() {
    return PocketSphinxJNI.Segment_lback_get(this.swigCPtr, this);
  }
  
  public int getLscore() {
    return PocketSphinxJNI.Segment_lscore_get(this.swigCPtr, this);
  }
  
  public int getProb() {
    return PocketSphinxJNI.Segment_prob_get(this.swigCPtr, this);
  }
  
  public int getStartFrame() {
    return PocketSphinxJNI.Segment_startFrame_get(this.swigCPtr, this);
  }
  
  public String getWord() {
    return PocketSphinxJNI.Segment_word_get(this.swigCPtr, this);
  }
  
  public void setAscore(int paramInt) {
    PocketSphinxJNI.Segment_ascore_set(this.swigCPtr, this, paramInt);
  }
  
  public void setEndFrame(int paramInt) {
    PocketSphinxJNI.Segment_endFrame_set(this.swigCPtr, this, paramInt);
  }
  
  public void setLback(int paramInt) {
    PocketSphinxJNI.Segment_lback_set(this.swigCPtr, this, paramInt);
  }
  
  public void setLscore(int paramInt) {
    PocketSphinxJNI.Segment_lscore_set(this.swigCPtr, this, paramInt);
  }
  
  public void setProb(int paramInt) {
    PocketSphinxJNI.Segment_prob_set(this.swigCPtr, this, paramInt);
  }
  
  public void setStartFrame(int paramInt) {
    PocketSphinxJNI.Segment_startFrame_set(this.swigCPtr, this, paramInt);
  }
  
  public void setWord(String paramString) {
    PocketSphinxJNI.Segment_word_set(this.swigCPtr, this, paramString);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/edu/cmu/pocketsphinx/Segment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
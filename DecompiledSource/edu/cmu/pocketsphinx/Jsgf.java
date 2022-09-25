package edu.cmu.pocketsphinx;

import java.util.Iterator;

public class Jsgf implements Iterable<JsgfRule> {
  protected transient boolean swigCMemOwn;
  
  private transient long swigCPtr;
  
  protected Jsgf(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  public Jsgf(String paramString) {
    this(SphinxBaseJNI.new_Jsgf(paramString), true);
  }
  
  protected static long getCPtr(Jsgf paramJsgf) {
    long l;
    if (paramJsgf == null) {
      l = 0L;
    } else {
      l = paramJsgf.swigCPtr;
    } 
    return l;
  }
  
  public FsgModel buildFsg(JsgfRule paramJsgfRule, LogMath paramLogMath, float paramFloat) {
    FsgModel fsgModel;
    long l = SphinxBaseJNI.Jsgf_buildFsg(this.swigCPtr, this, JsgfRule.getCPtr(paramJsgfRule), paramJsgfRule, LogMath.getCPtr(paramLogMath), paramLogMath, paramFloat);
    if (l == 0L) {
      paramJsgfRule = null;
    } else {
      fsgModel = new FsgModel(l, false);
    } 
    return fsgModel;
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
    //   27: invokestatic delete_Jsgf : (J)V
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
  
  public String getName() {
    return SphinxBaseJNI.Jsgf_getName(this.swigCPtr, this);
  }
  
  public JsgfRule getRule(String paramString) {
    JsgfRule jsgfRule;
    long l = SphinxBaseJNI.Jsgf_getRule(this.swigCPtr, this, paramString);
    if (l == 0L) {
      paramString = null;
    } else {
      jsgfRule = new JsgfRule(l, false);
    } 
    return jsgfRule;
  }
  
  public JsgfIterator iterator() {
    JsgfIterator jsgfIterator;
    long l = SphinxBaseJNI.Jsgf_iterator(this.swigCPtr, this);
    if (l == 0L) {
      jsgfIterator = null;
    } else {
      jsgfIterator = new JsgfIterator(l, true);
    } 
    return jsgfIterator;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/edu/cmu/pocketsphinx/Jsgf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
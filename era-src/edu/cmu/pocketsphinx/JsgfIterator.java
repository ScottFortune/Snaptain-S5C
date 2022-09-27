package edu.cmu.pocketsphinx;

import java.util.Iterator;

public class JsgfIterator implements Iterator<JsgfRule> {
  protected transient boolean swigCMemOwn;
  
  private transient long swigCPtr;
  
  protected JsgfIterator(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  public JsgfIterator(SWIGTYPE_p_void paramSWIGTYPE_p_void) {
    this(SphinxBaseJNI.new_JsgfIterator(SWIGTYPE_p_void.getCPtr(paramSWIGTYPE_p_void)), true);
  }
  
  protected static long getCPtr(JsgfIterator paramJsgfIterator) {
    long l;
    if (paramJsgfIterator == null) {
      l = 0L;
    } else {
      l = paramJsgfIterator.swigCPtr;
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
    //   27: invokestatic delete_JsgfIterator : (J)V
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
  
  public boolean hasNext() {
    return SphinxBaseJNI.JsgfIterator_hasNext(this.swigCPtr, this);
  }
  
  public JsgfRule next() {
    JsgfRule jsgfRule;
    long l = SphinxBaseJNI.JsgfIterator_next(this.swigCPtr, this);
    if (l == 0L) {
      jsgfRule = null;
    } else {
      jsgfRule = new JsgfRule(l, true);
    } 
    return jsgfRule;
  }
  
  public void remove() {
    throw new UnsupportedOperationException();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/edu/cmu/pocketsphinx/JsgfIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
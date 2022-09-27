package edu.cmu.pocketsphinx;

public class Lattice {
  protected transient boolean swigCMemOwn;
  
  private transient long swigCPtr;
  
  protected Lattice(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  public Lattice(Decoder paramDecoder, String paramString) {
    this(PocketSphinxJNI.new_Lattice__SWIG_1(Decoder.getCPtr(paramDecoder), paramDecoder, paramString), true);
  }
  
  public Lattice(String paramString) {
    this(PocketSphinxJNI.new_Lattice__SWIG_0(paramString), true);
  }
  
  protected static long getCPtr(Lattice paramLattice) {
    long l;
    if (paramLattice == null) {
      l = 0L;
    } else {
      l = paramLattice.swigCPtr;
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
    //   27: invokestatic delete_Lattice : (J)V
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
  
  public void write(String paramString) {
    PocketSphinxJNI.Lattice_write(this.swigCPtr, this, paramString);
  }
  
  public void writeHtk(String paramString) {
    PocketSphinxJNI.Lattice_writeHtk(this.swigCPtr, this, paramString);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/edu/cmu/pocketsphinx/Lattice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
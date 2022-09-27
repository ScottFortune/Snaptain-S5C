package edu.cmu.pocketsphinx;

public class Config {
  protected transient boolean swigCMemOwn;
  
  private transient long swigCPtr;
  
  protected Config(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  protected static long getCPtr(Config paramConfig) {
    long l;
    if (paramConfig == null) {
      l = 0L;
    } else {
      l = paramConfig.swigCPtr;
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
    //   27: invokestatic delete_Config : (J)V
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
  
  public boolean exists(String paramString) {
    return SphinxBaseJNI.Config_exists(this.swigCPtr, this, paramString);
  }
  
  protected void finalize() {
    delete();
  }
  
  public boolean getBoolean(String paramString) {
    return SphinxBaseJNI.Config_getBoolean(this.swigCPtr, this, paramString);
  }
  
  public double getFloat(String paramString) {
    return SphinxBaseJNI.Config_getFloat(this.swigCPtr, this, paramString);
  }
  
  public int getInt(String paramString) {
    return SphinxBaseJNI.Config_getInt(this.swigCPtr, this, paramString);
  }
  
  public String getString(String paramString) {
    return SphinxBaseJNI.Config_getString(this.swigCPtr, this, paramString);
  }
  
  public void setBoolean(String paramString, boolean paramBoolean) {
    SphinxBaseJNI.Config_setBoolean(this.swigCPtr, this, paramString, paramBoolean);
  }
  
  public void setFloat(String paramString, double paramDouble) {
    SphinxBaseJNI.Config_setFloat(this.swigCPtr, this, paramString, paramDouble);
  }
  
  public void setInt(String paramString, int paramInt) {
    SphinxBaseJNI.Config_setInt(this.swigCPtr, this, paramString, paramInt);
  }
  
  public void setString(String paramString1, String paramString2) {
    SphinxBaseJNI.Config_setString(this.swigCPtr, this, paramString1, paramString2);
  }
  
  public void setStringExtra(String paramString1, String paramString2) {
    SphinxBaseJNI.Config_setStringExtra(this.swigCPtr, this, paramString1, paramString2);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/edu/cmu/pocketsphinx/Config.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
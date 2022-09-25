package com.bumptech.glide.util.pool;

public abstract class StateVerifier {
  private static final boolean DEBUG = false;
  
  private StateVerifier() {}
  
  public static StateVerifier newInstance() {
    return new DefaultStateVerifier();
  }
  
  abstract void setRecycled(boolean paramBoolean);
  
  public abstract void throwIfRecycled();
  
  private static class DebugStateVerifier extends StateVerifier {
    private volatile RuntimeException recycledAtStackTraceException;
    
    void setRecycled(boolean param1Boolean) {
      if (param1Boolean) {
        this.recycledAtStackTraceException = new RuntimeException("Released");
      } else {
        this.recycledAtStackTraceException = null;
      } 
    }
    
    public void throwIfRecycled() {
      if (this.recycledAtStackTraceException == null)
        return; 
      throw new IllegalStateException("Already released", this.recycledAtStackTraceException);
    }
  }
  
  private static class DefaultStateVerifier extends StateVerifier {
    private volatile boolean isReleased;
    
    public void setRecycled(boolean param1Boolean) {
      this.isReleased = param1Boolean;
    }
    
    public void throwIfRecycled() {
      if (!this.isReleased)
        return; 
      throw new IllegalStateException("Already released");
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/util/pool/StateVerifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
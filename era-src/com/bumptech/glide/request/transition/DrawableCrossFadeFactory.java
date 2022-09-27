package com.bumptech.glide.request.transition;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.DataSource;

public class DrawableCrossFadeFactory implements TransitionFactory<Drawable> {
  private final int duration;
  
  private final boolean isCrossFadeEnabled;
  
  private DrawableCrossFadeTransition resourceTransition;
  
  protected DrawableCrossFadeFactory(int paramInt, boolean paramBoolean) {
    this.duration = paramInt;
    this.isCrossFadeEnabled = paramBoolean;
  }
  
  private Transition<Drawable> getResourceTransition() {
    if (this.resourceTransition == null)
      this.resourceTransition = new DrawableCrossFadeTransition(this.duration, this.isCrossFadeEnabled); 
    return this.resourceTransition;
  }
  
  public Transition<Drawable> build(DataSource paramDataSource, boolean paramBoolean) {
    Transition<Drawable> transition;
    if (paramDataSource == DataSource.MEMORY_CACHE) {
      transition = NoTransition.get();
    } else {
      transition = getResourceTransition();
    } 
    return transition;
  }
  
  public static class Builder {
    private static final int DEFAULT_DURATION_MS = 300;
    
    private final int durationMillis;
    
    private boolean isCrossFadeEnabled;
    
    public Builder() {
      this(300);
    }
    
    public Builder(int param1Int) {
      this.durationMillis = param1Int;
    }
    
    public DrawableCrossFadeFactory build() {
      return new DrawableCrossFadeFactory(this.durationMillis, this.isCrossFadeEnabled);
    }
    
    public Builder setCrossFadeEnabled(boolean param1Boolean) {
      this.isCrossFadeEnabled = param1Boolean;
      return this;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/transition/DrawableCrossFadeFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
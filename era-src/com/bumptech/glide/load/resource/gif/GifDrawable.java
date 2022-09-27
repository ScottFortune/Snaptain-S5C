package com.bumptech.glide.load.resource.gif;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class GifDrawable extends Drawable implements GifFrameLoader.FrameCallback, Animatable, Animatable2Compat {
  private static final int GRAVITY = 119;
  
  public static final int LOOP_FOREVER = -1;
  
  public static final int LOOP_INTRINSIC = 0;
  
  private List<Animatable2Compat.AnimationCallback> animationCallbacks;
  
  private boolean applyGravity;
  
  private Rect destRect;
  
  private boolean isRecycled;
  
  private boolean isRunning;
  
  private boolean isStarted;
  
  private boolean isVisible = true;
  
  private int loopCount;
  
  private int maxLoopCount = -1;
  
  private Paint paint;
  
  private final GifState state;
  
  public GifDrawable(Context paramContext, GifDecoder paramGifDecoder, Transformation<Bitmap> paramTransformation, int paramInt1, int paramInt2, Bitmap paramBitmap) {
    this(new GifState(new GifFrameLoader(Glide.get(paramContext), paramGifDecoder, paramInt1, paramInt2, paramTransformation, paramBitmap)));
  }
  
  @Deprecated
  public GifDrawable(Context paramContext, GifDecoder paramGifDecoder, BitmapPool paramBitmapPool, Transformation<Bitmap> paramTransformation, int paramInt1, int paramInt2, Bitmap paramBitmap) {
    this(paramContext, paramGifDecoder, paramTransformation, paramInt1, paramInt2, paramBitmap);
  }
  
  GifDrawable(GifState paramGifState) {
    this.state = (GifState)Preconditions.checkNotNull(paramGifState);
  }
  
  GifDrawable(GifFrameLoader paramGifFrameLoader, Paint paramPaint) {
    this(new GifState(paramGifFrameLoader));
    this.paint = paramPaint;
  }
  
  private Drawable.Callback findCallback() {
    Drawable.Callback callback;
    for (callback = getCallback(); callback instanceof Drawable; callback = ((Drawable)callback).getCallback());
    return callback;
  }
  
  private Rect getDestRect() {
    if (this.destRect == null)
      this.destRect = new Rect(); 
    return this.destRect;
  }
  
  private Paint getPaint() {
    if (this.paint == null)
      this.paint = new Paint(2); 
    return this.paint;
  }
  
  private void notifyAnimationEndToListeners() {
    List<Animatable2Compat.AnimationCallback> list = this.animationCallbacks;
    if (list != null) {
      byte b = 0;
      int i = list.size();
      while (b < i) {
        ((Animatable2Compat.AnimationCallback)this.animationCallbacks.get(b)).onAnimationEnd(this);
        b++;
      } 
    } 
  }
  
  private void resetLoopCount() {
    this.loopCount = 0;
  }
  
  private void startRunning() {
    Preconditions.checkArgument(this.isRecycled ^ true, "You cannot start a recycled Drawable. Ensure thatyou clear any references to the Drawable when clearing the corresponding request.");
    if (this.state.frameLoader.getFrameCount() == 1) {
      invalidateSelf();
    } else if (!this.isRunning) {
      this.isRunning = true;
      this.state.frameLoader.subscribe(this);
      invalidateSelf();
    } 
  }
  
  private void stopRunning() {
    this.isRunning = false;
    this.state.frameLoader.unsubscribe(this);
  }
  
  public void clearAnimationCallbacks() {
    List<Animatable2Compat.AnimationCallback> list = this.animationCallbacks;
    if (list != null)
      list.clear(); 
  }
  
  public void draw(Canvas paramCanvas) {
    if (this.isRecycled)
      return; 
    if (this.applyGravity) {
      Gravity.apply(119, getIntrinsicWidth(), getIntrinsicHeight(), getBounds(), getDestRect());
      this.applyGravity = false;
    } 
    paramCanvas.drawBitmap(this.state.frameLoader.getCurrentFrame(), null, getDestRect(), getPaint());
  }
  
  public ByteBuffer getBuffer() {
    return this.state.frameLoader.getBuffer();
  }
  
  public Drawable.ConstantState getConstantState() {
    return this.state;
  }
  
  public Bitmap getFirstFrame() {
    return this.state.frameLoader.getFirstFrame();
  }
  
  public int getFrameCount() {
    return this.state.frameLoader.getFrameCount();
  }
  
  public int getFrameIndex() {
    return this.state.frameLoader.getCurrentIndex();
  }
  
  public Transformation<Bitmap> getFrameTransformation() {
    return this.state.frameLoader.getFrameTransformation();
  }
  
  public int getIntrinsicHeight() {
    return this.state.frameLoader.getHeight();
  }
  
  public int getIntrinsicWidth() {
    return this.state.frameLoader.getWidth();
  }
  
  public int getOpacity() {
    return -2;
  }
  
  public int getSize() {
    return this.state.frameLoader.getSize();
  }
  
  boolean isRecycled() {
    return this.isRecycled;
  }
  
  public boolean isRunning() {
    return this.isRunning;
  }
  
  protected void onBoundsChange(Rect paramRect) {
    super.onBoundsChange(paramRect);
    this.applyGravity = true;
  }
  
  public void onFrameReady() {
    if (findCallback() == null) {
      stop();
      invalidateSelf();
      return;
    } 
    invalidateSelf();
    if (getFrameIndex() == getFrameCount() - 1)
      this.loopCount++; 
    int i = this.maxLoopCount;
    if (i != -1 && this.loopCount >= i) {
      notifyAnimationEndToListeners();
      stop();
    } 
  }
  
  public void recycle() {
    this.isRecycled = true;
    this.state.frameLoader.clear();
  }
  
  public void registerAnimationCallback(Animatable2Compat.AnimationCallback paramAnimationCallback) {
    if (paramAnimationCallback == null)
      return; 
    if (this.animationCallbacks == null)
      this.animationCallbacks = new ArrayList<Animatable2Compat.AnimationCallback>(); 
    this.animationCallbacks.add(paramAnimationCallback);
  }
  
  public void setAlpha(int paramInt) {
    getPaint().setAlpha(paramInt);
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {
    getPaint().setColorFilter(paramColorFilter);
  }
  
  public void setFrameTransformation(Transformation<Bitmap> paramTransformation, Bitmap paramBitmap) {
    this.state.frameLoader.setFrameTransformation(paramTransformation, paramBitmap);
  }
  
  void setIsRunning(boolean paramBoolean) {
    this.isRunning = paramBoolean;
  }
  
  public void setLoopCount(int paramInt) {
    if (paramInt > 0 || paramInt == -1 || paramInt == 0) {
      if (paramInt == 0) {
        int i = this.state.frameLoader.getLoopCount();
        paramInt = i;
        if (i == 0)
          paramInt = -1; 
        this.maxLoopCount = paramInt;
      } else {
        this.maxLoopCount = paramInt;
      } 
      return;
    } 
    throw new IllegalArgumentException("Loop count must be greater than 0, or equal to GlideDrawable.LOOP_FOREVER, or equal to GlideDrawable.LOOP_INTRINSIC");
  }
  
  public boolean setVisible(boolean paramBoolean1, boolean paramBoolean2) {
    Preconditions.checkArgument(this.isRecycled ^ true, "Cannot change the visibility of a recycled resource. Ensure that you unset the Drawable from your View before changing the View's visibility.");
    this.isVisible = paramBoolean1;
    if (!paramBoolean1) {
      stopRunning();
    } else if (this.isStarted) {
      startRunning();
    } 
    return super.setVisible(paramBoolean1, paramBoolean2);
  }
  
  public void start() {
    this.isStarted = true;
    resetLoopCount();
    if (this.isVisible)
      startRunning(); 
  }
  
  public void startFromFirstFrame() {
    Preconditions.checkArgument(this.isRunning ^ true, "You cannot restart a currently running animation.");
    this.state.frameLoader.setNextStartFromFirstFrame();
    start();
  }
  
  public void stop() {
    this.isStarted = false;
    stopRunning();
  }
  
  public boolean unregisterAnimationCallback(Animatable2Compat.AnimationCallback paramAnimationCallback) {
    List<Animatable2Compat.AnimationCallback> list = this.animationCallbacks;
    return (list == null || paramAnimationCallback == null) ? false : list.remove(paramAnimationCallback);
  }
  
  static final class GifState extends Drawable.ConstantState {
    final GifFrameLoader frameLoader;
    
    GifState(GifFrameLoader param1GifFrameLoader) {
      this.frameLoader = param1GifFrameLoader;
    }
    
    public int getChangingConfigurations() {
      return 0;
    }
    
    public Drawable newDrawable() {
      return new GifDrawable(this);
    }
    
    public Drawable newDrawable(Resources param1Resources) {
      return newDrawable();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/gif/GifDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
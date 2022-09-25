package com.bumptech.glide.load.resource.gif;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

class GifFrameLoader {
  private final BitmapPool bitmapPool;
  
  private final List<FrameCallback> callbacks = new ArrayList<FrameCallback>();
  
  private DelayTarget current;
  
  private Bitmap firstFrame;
  
  private int firstFrameSize;
  
  private final GifDecoder gifDecoder;
  
  private final Handler handler;
  
  private int height;
  
  private boolean isCleared;
  
  private boolean isLoadPending;
  
  private boolean isRunning;
  
  private DelayTarget next;
  
  private OnEveryFrameListener onEveryFrameListener;
  
  private DelayTarget pendingTarget;
  
  private RequestBuilder<Bitmap> requestBuilder;
  
  final RequestManager requestManager;
  
  private boolean startFromFirstFrame;
  
  private Transformation<Bitmap> transformation;
  
  private int width;
  
  GifFrameLoader(Glide paramGlide, GifDecoder paramGifDecoder, int paramInt1, int paramInt2, Transformation<Bitmap> paramTransformation, Bitmap paramBitmap) {
    this(paramGlide.getBitmapPool(), Glide.with(paramGlide.getContext()), paramGifDecoder, null, getRequestBuilder(Glide.with(paramGlide.getContext()), paramInt1, paramInt2), paramTransformation, paramBitmap);
  }
  
  GifFrameLoader(BitmapPool paramBitmapPool, RequestManager paramRequestManager, GifDecoder paramGifDecoder, Handler paramHandler, RequestBuilder<Bitmap> paramRequestBuilder, Transformation<Bitmap> paramTransformation, Bitmap paramBitmap) {
    this.requestManager = paramRequestManager;
    Handler handler = paramHandler;
    if (paramHandler == null)
      handler = new Handler(Looper.getMainLooper(), new FrameLoaderCallback()); 
    this.bitmapPool = paramBitmapPool;
    this.handler = handler;
    this.requestBuilder = paramRequestBuilder;
    this.gifDecoder = paramGifDecoder;
    setFrameTransformation(paramTransformation, paramBitmap);
  }
  
  private static Key getFrameSignature() {
    return (Key)new ObjectKey(Double.valueOf(Math.random()));
  }
  
  private static RequestBuilder<Bitmap> getRequestBuilder(RequestManager paramRequestManager, int paramInt1, int paramInt2) {
    return paramRequestManager.asBitmap().apply(((RequestOptions)((RequestOptions)RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).useAnimationPool(true)).skipMemoryCache(true)).override(paramInt1, paramInt2));
  }
  
  private void loadNextFrame() {
    if (this.isRunning && !this.isLoadPending) {
      if (this.startFromFirstFrame) {
        boolean bool;
        if (this.pendingTarget == null) {
          bool = true;
        } else {
          bool = false;
        } 
        Preconditions.checkArgument(bool, "Pending target must be null when starting from the first frame");
        this.gifDecoder.resetFrameIndex();
        this.startFromFirstFrame = false;
      } 
      DelayTarget delayTarget = this.pendingTarget;
      if (delayTarget != null) {
        this.pendingTarget = null;
        onFrameReady(delayTarget);
        return;
      } 
      this.isLoadPending = true;
      int i = this.gifDecoder.getNextDelay();
      long l1 = SystemClock.uptimeMillis();
      long l2 = i;
      this.gifDecoder.advance();
      this.next = new DelayTarget(this.handler, this.gifDecoder.getCurrentFrameIndex(), l1 + l2);
      this.requestBuilder.apply((BaseRequestOptions)RequestOptions.signatureOf(getFrameSignature())).load(this.gifDecoder).into((Target)this.next);
    } 
  }
  
  private void recycleFirstFrame() {
    Bitmap bitmap = this.firstFrame;
    if (bitmap != null) {
      this.bitmapPool.put(bitmap);
      this.firstFrame = null;
    } 
  }
  
  private void start() {
    if (this.isRunning)
      return; 
    this.isRunning = true;
    this.isCleared = false;
    loadNextFrame();
  }
  
  private void stop() {
    this.isRunning = false;
  }
  
  void clear() {
    this.callbacks.clear();
    recycleFirstFrame();
    stop();
    DelayTarget delayTarget = this.current;
    if (delayTarget != null) {
      this.requestManager.clear((Target)delayTarget);
      this.current = null;
    } 
    delayTarget = this.next;
    if (delayTarget != null) {
      this.requestManager.clear((Target)delayTarget);
      this.next = null;
    } 
    delayTarget = this.pendingTarget;
    if (delayTarget != null) {
      this.requestManager.clear((Target)delayTarget);
      this.pendingTarget = null;
    } 
    this.gifDecoder.clear();
    this.isCleared = true;
  }
  
  ByteBuffer getBuffer() {
    return this.gifDecoder.getData().asReadOnlyBuffer();
  }
  
  Bitmap getCurrentFrame() {
    Bitmap bitmap;
    DelayTarget delayTarget = this.current;
    if (delayTarget != null) {
      bitmap = delayTarget.getResource();
    } else {
      bitmap = this.firstFrame;
    } 
    return bitmap;
  }
  
  int getCurrentIndex() {
    byte b;
    DelayTarget delayTarget = this.current;
    if (delayTarget != null) {
      b = delayTarget.index;
    } else {
      b = -1;
    } 
    return b;
  }
  
  Bitmap getFirstFrame() {
    return this.firstFrame;
  }
  
  int getFrameCount() {
    return this.gifDecoder.getFrameCount();
  }
  
  Transformation<Bitmap> getFrameTransformation() {
    return this.transformation;
  }
  
  int getHeight() {
    return this.height;
  }
  
  int getLoopCount() {
    return this.gifDecoder.getTotalIterationCount();
  }
  
  int getSize() {
    return this.gifDecoder.getByteSize() + this.firstFrameSize;
  }
  
  int getWidth() {
    return this.width;
  }
  
  void onFrameReady(DelayTarget paramDelayTarget) {
    OnEveryFrameListener onEveryFrameListener = this.onEveryFrameListener;
    if (onEveryFrameListener != null)
      onEveryFrameListener.onFrameReady(); 
    this.isLoadPending = false;
    if (this.isCleared) {
      this.handler.obtainMessage(2, paramDelayTarget).sendToTarget();
      return;
    } 
    if (!this.isRunning) {
      this.pendingTarget = paramDelayTarget;
      return;
    } 
    if (paramDelayTarget.getResource() != null) {
      recycleFirstFrame();
      DelayTarget delayTarget = this.current;
      this.current = paramDelayTarget;
      for (int i = this.callbacks.size() - 1; i >= 0; i--)
        ((FrameCallback)this.callbacks.get(i)).onFrameReady(); 
      if (delayTarget != null)
        this.handler.obtainMessage(2, delayTarget).sendToTarget(); 
    } 
    loadNextFrame();
  }
  
  void setFrameTransformation(Transformation<Bitmap> paramTransformation, Bitmap paramBitmap) {
    this.transformation = (Transformation<Bitmap>)Preconditions.checkNotNull(paramTransformation);
    this.firstFrame = (Bitmap)Preconditions.checkNotNull(paramBitmap);
    this.requestBuilder = this.requestBuilder.apply((new RequestOptions()).transform(paramTransformation));
    this.firstFrameSize = Util.getBitmapByteSize(paramBitmap);
    this.width = paramBitmap.getWidth();
    this.height = paramBitmap.getHeight();
  }
  
  void setNextStartFromFirstFrame() {
    Preconditions.checkArgument(this.isRunning ^ true, "Can't restart a running animation");
    this.startFromFirstFrame = true;
    DelayTarget delayTarget = this.pendingTarget;
    if (delayTarget != null) {
      this.requestManager.clear((Target)delayTarget);
      this.pendingTarget = null;
    } 
  }
  
  void setOnEveryFrameReadyListener(OnEveryFrameListener paramOnEveryFrameListener) {
    this.onEveryFrameListener = paramOnEveryFrameListener;
  }
  
  void subscribe(FrameCallback paramFrameCallback) {
    if (!this.isCleared) {
      if (!this.callbacks.contains(paramFrameCallback)) {
        boolean bool = this.callbacks.isEmpty();
        this.callbacks.add(paramFrameCallback);
        if (bool)
          start(); 
        return;
      } 
      throw new IllegalStateException("Cannot subscribe twice in a row");
    } 
    throw new IllegalStateException("Cannot subscribe to a cleared frame loader");
  }
  
  void unsubscribe(FrameCallback paramFrameCallback) {
    this.callbacks.remove(paramFrameCallback);
    if (this.callbacks.isEmpty())
      stop(); 
  }
  
  static class DelayTarget extends CustomTarget<Bitmap> {
    private final Handler handler;
    
    final int index;
    
    private Bitmap resource;
    
    private final long targetTime;
    
    DelayTarget(Handler param1Handler, int param1Int, long param1Long) {
      this.handler = param1Handler;
      this.index = param1Int;
      this.targetTime = param1Long;
    }
    
    Bitmap getResource() {
      return this.resource;
    }
    
    public void onLoadCleared(Drawable param1Drawable) {
      this.resource = null;
    }
    
    public void onResourceReady(Bitmap param1Bitmap, Transition<? super Bitmap> param1Transition) {
      this.resource = param1Bitmap;
      Message message = this.handler.obtainMessage(1, this);
      this.handler.sendMessageAtTime(message, this.targetTime);
    }
  }
  
  public static interface FrameCallback {
    void onFrameReady();
  }
  
  private class FrameLoaderCallback implements Handler.Callback {
    static final int MSG_CLEAR = 2;
    
    static final int MSG_DELAY = 1;
    
    public boolean handleMessage(Message param1Message) {
      GifFrameLoader.DelayTarget delayTarget;
      if (param1Message.what == 1) {
        delayTarget = (GifFrameLoader.DelayTarget)param1Message.obj;
        GifFrameLoader.this.onFrameReady(delayTarget);
        return true;
      } 
      if (((Message)delayTarget).what == 2) {
        delayTarget = (GifFrameLoader.DelayTarget)((Message)delayTarget).obj;
        GifFrameLoader.this.requestManager.clear((Target)delayTarget);
      } 
      return false;
    }
  }
  
  static interface OnEveryFrameListener {
    void onFrameReady();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/gif/GifFrameLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
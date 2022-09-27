package com.bumptech.glide;

import android.graphics.drawable.Drawable;
import android.widget.AbsListView;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.util.Util;
import java.util.List;
import java.util.Queue;

public class ListPreloader<T> implements AbsListView.OnScrollListener {
  private boolean isIncreasing = true;
  
  private int lastEnd;
  
  private int lastFirstVisible = -1;
  
  private int lastStart;
  
  private final int maxPreload;
  
  private final PreloadSizeProvider<T> preloadDimensionProvider;
  
  private final PreloadModelProvider<T> preloadModelProvider;
  
  private final PreloadTargetQueue preloadTargetQueue;
  
  private final RequestManager requestManager;
  
  private int totalItemCount;
  
  public ListPreloader(RequestManager paramRequestManager, PreloadModelProvider<T> paramPreloadModelProvider, PreloadSizeProvider<T> paramPreloadSizeProvider, int paramInt) {
    this.requestManager = paramRequestManager;
    this.preloadModelProvider = paramPreloadModelProvider;
    this.preloadDimensionProvider = paramPreloadSizeProvider;
    this.maxPreload = paramInt;
    this.preloadTargetQueue = new PreloadTargetQueue(paramInt + 1);
  }
  
  private void cancelAll() {
    for (byte b = 0; b < this.preloadTargetQueue.queue.size(); b++)
      this.requestManager.clear(this.preloadTargetQueue.next(0, 0)); 
  }
  
  private void preload(int paramInt1, int paramInt2) {
    if (paramInt1 < paramInt2) {
      i = Math.max(this.lastEnd, paramInt1);
      j = paramInt2;
    } else {
      j = Math.min(this.lastStart, paramInt1);
      i = paramInt2;
    } 
    int j = Math.min(this.totalItemCount, j);
    int i = Math.min(this.totalItemCount, Math.max(0, i));
    if (paramInt1 < paramInt2) {
      for (paramInt1 = i; paramInt1 < j; paramInt1++)
        preloadAdapterPosition(this.preloadModelProvider.getPreloadItems(paramInt1), paramInt1, true); 
    } else {
      for (paramInt1 = j - 1; paramInt1 >= i; paramInt1--)
        preloadAdapterPosition(this.preloadModelProvider.getPreloadItems(paramInt1), paramInt1, false); 
    } 
    this.lastStart = i;
    this.lastEnd = j;
  }
  
  private void preload(int paramInt, boolean paramBoolean) {
    int i;
    if (this.isIncreasing != paramBoolean) {
      this.isIncreasing = paramBoolean;
      cancelAll();
    } 
    if (paramBoolean) {
      i = this.maxPreload;
    } else {
      i = -this.maxPreload;
    } 
    preload(paramInt, i + paramInt);
  }
  
  private void preloadAdapterPosition(List<T> paramList, int paramInt, boolean paramBoolean) {
    int i = paramList.size();
    if (paramBoolean) {
      for (byte b = 0; b < i; b++)
        preloadItem(paramList.get(b), paramInt, b); 
    } else {
      for (int j = i - 1; j >= 0; j--)
        preloadItem(paramList.get(j), paramInt, j); 
    } 
  }
  
  private void preloadItem(T paramT, int paramInt1, int paramInt2) {
    if (paramT == null)
      return; 
    int[] arrayOfInt = this.preloadDimensionProvider.getPreloadSize(paramT, paramInt1, paramInt2);
    if (arrayOfInt == null)
      return; 
    RequestBuilder<?> requestBuilder = this.preloadModelProvider.getPreloadRequestBuilder(paramT);
    if (requestBuilder == null)
      return; 
    requestBuilder.into(this.preloadTargetQueue.next(arrayOfInt[0], arrayOfInt[1]));
  }
  
  public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3) {
    this.totalItemCount = paramInt3;
    paramInt3 = this.lastFirstVisible;
    if (paramInt1 > paramInt3) {
      preload(paramInt2 + paramInt1, true);
    } else if (paramInt1 < paramInt3) {
      preload(paramInt1, false);
    } 
    this.lastFirstVisible = paramInt1;
  }
  
  public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt) {}
  
  public static interface PreloadModelProvider<U> {
    List<U> getPreloadItems(int param1Int);
    
    RequestBuilder<?> getPreloadRequestBuilder(U param1U);
  }
  
  public static interface PreloadSizeProvider<T> {
    int[] getPreloadSize(T param1T, int param1Int1, int param1Int2);
  }
  
  private static final class PreloadTarget implements Target<Object> {
    int photoHeight;
    
    int photoWidth;
    
    private Request request;
    
    public Request getRequest() {
      return this.request;
    }
    
    public void getSize(SizeReadyCallback param1SizeReadyCallback) {
      param1SizeReadyCallback.onSizeReady(this.photoWidth, this.photoHeight);
    }
    
    public void onDestroy() {}
    
    public void onLoadCleared(Drawable param1Drawable) {}
    
    public void onLoadFailed(Drawable param1Drawable) {}
    
    public void onLoadStarted(Drawable param1Drawable) {}
    
    public void onResourceReady(Object param1Object, Transition<? super Object> param1Transition) {}
    
    public void onStart() {}
    
    public void onStop() {}
    
    public void removeCallback(SizeReadyCallback param1SizeReadyCallback) {}
    
    public void setRequest(Request param1Request) {
      this.request = param1Request;
    }
  }
  
  private static final class PreloadTargetQueue {
    final Queue<ListPreloader.PreloadTarget> queue;
    
    PreloadTargetQueue(int param1Int) {
      this.queue = Util.createQueue(param1Int);
      for (byte b = 0; b < param1Int; b++)
        this.queue.offer(new ListPreloader.PreloadTarget()); 
    }
    
    public ListPreloader.PreloadTarget next(int param1Int1, int param1Int2) {
      ListPreloader.PreloadTarget preloadTarget = this.queue.poll();
      this.queue.offer(preloadTarget);
      preloadTarget.photoWidth = param1Int1;
      preloadTarget.photoHeight = param1Int2;
      return preloadTarget;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/ListPreloader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
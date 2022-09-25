package com.bumptech.glide;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.manager.ConnectivityMonitor;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.LifecycleListener;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import com.bumptech.glide.manager.RequestTracker;
import com.bumptech.glide.manager.TargetTracker;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RequestManager implements ComponentCallbacks2, LifecycleListener, ModelTypes<RequestBuilder<Drawable>> {
  private static final RequestOptions DECODE_TYPE_BITMAP = (RequestOptions)RequestOptions.decodeTypeOf(Bitmap.class).lock();
  
  private static final RequestOptions DECODE_TYPE_GIF = (RequestOptions)RequestOptions.decodeTypeOf(GifDrawable.class).lock();
  
  private static final RequestOptions DOWNLOAD_ONLY_OPTIONS = (RequestOptions)((RequestOptions)RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA).priority(Priority.LOW)).skipMemoryCache(true);
  
  private final Runnable addSelfToLifecycle = new Runnable() {
      public void run() {
        RequestManager.this.lifecycle.addListener(RequestManager.this);
      }
    };
  
  private final ConnectivityMonitor connectivityMonitor;
  
  protected final Context context;
  
  private final CopyOnWriteArrayList<RequestListener<Object>> defaultRequestListeners;
  
  protected final Glide glide;
  
  final Lifecycle lifecycle;
  
  private final Handler mainHandler = new Handler(Looper.getMainLooper());
  
  private boolean pauseAllRequestsOnTrimMemoryModerate;
  
  private RequestOptions requestOptions;
  
  private final RequestTracker requestTracker;
  
  private final TargetTracker targetTracker = new TargetTracker();
  
  private final RequestManagerTreeNode treeNode;
  
  public RequestManager(Glide paramGlide, Lifecycle paramLifecycle, RequestManagerTreeNode paramRequestManagerTreeNode, Context paramContext) {
    this(paramGlide, paramLifecycle, paramRequestManagerTreeNode, new RequestTracker(), paramGlide.getConnectivityMonitorFactory(), paramContext);
  }
  
  RequestManager(Glide paramGlide, Lifecycle paramLifecycle, RequestManagerTreeNode paramRequestManagerTreeNode, RequestTracker paramRequestTracker, ConnectivityMonitorFactory paramConnectivityMonitorFactory, Context paramContext) {
    this.glide = paramGlide;
    this.lifecycle = paramLifecycle;
    this.treeNode = paramRequestManagerTreeNode;
    this.requestTracker = paramRequestTracker;
    this.context = paramContext;
    this.connectivityMonitor = paramConnectivityMonitorFactory.build(paramContext.getApplicationContext(), new RequestManagerConnectivityListener(paramRequestTracker));
    if (Util.isOnBackgroundThread()) {
      this.mainHandler.post(this.addSelfToLifecycle);
    } else {
      paramLifecycle.addListener(this);
    } 
    paramLifecycle.addListener((LifecycleListener)this.connectivityMonitor);
    this.defaultRequestListeners = new CopyOnWriteArrayList<RequestListener<Object>>(paramGlide.getGlideContext().getDefaultRequestListeners());
    setRequestOptions(paramGlide.getGlideContext().getDefaultRequestOptions());
    paramGlide.registerRequestManager(this);
  }
  
  private void untrackOrDelegate(Target<?> paramTarget) {
    boolean bool = untrack(paramTarget);
    Request request = paramTarget.getRequest();
    if (!bool && !this.glide.removeFromManagers(paramTarget) && request != null) {
      paramTarget.setRequest(null);
      request.clear();
    } 
  }
  
  private void updateRequestOptions(RequestOptions paramRequestOptions) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield requestOptions : Lcom/bumptech/glide/request/RequestOptions;
    //   7: aload_1
    //   8: invokevirtual apply : (Lcom/bumptech/glide/request/BaseRequestOptions;)Lcom/bumptech/glide/request/BaseRequestOptions;
    //   11: checkcast com/bumptech/glide/request/RequestOptions
    //   14: putfield requestOptions : Lcom/bumptech/glide/request/RequestOptions;
    //   17: aload_0
    //   18: monitorexit
    //   19: return
    //   20: astore_1
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_1
    //   24: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	20	finally
  }
  
  public RequestManager addDefaultRequestListener(RequestListener<Object> paramRequestListener) {
    this.defaultRequestListeners.add(paramRequestListener);
    return this;
  }
  
  public RequestManager applyDefaultRequestOptions(RequestOptions paramRequestOptions) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: invokespecial updateRequestOptions : (Lcom/bumptech/glide/request/RequestOptions;)V
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_0
    //   10: areturn
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public <ResourceType> RequestBuilder<ResourceType> as(Class<ResourceType> paramClass) {
    return new RequestBuilder<ResourceType>(this.glide, this, paramClass, this.context);
  }
  
  public RequestBuilder<Bitmap> asBitmap() {
    return as(Bitmap.class).apply((BaseRequestOptions<?>)DECODE_TYPE_BITMAP);
  }
  
  public RequestBuilder<Drawable> asDrawable() {
    return as(Drawable.class);
  }
  
  public RequestBuilder<File> asFile() {
    return as(File.class).apply((BaseRequestOptions<?>)RequestOptions.skipMemoryCacheOf(true));
  }
  
  public RequestBuilder<GifDrawable> asGif() {
    return as(GifDrawable.class).apply((BaseRequestOptions<?>)DECODE_TYPE_GIF);
  }
  
  public void clear(View paramView) {
    clear((Target<?>)new ClearTarget(paramView));
  }
  
  public void clear(Target<?> paramTarget) {
    if (paramTarget == null)
      return; 
    untrackOrDelegate(paramTarget);
  }
  
  public RequestBuilder<File> download(Object paramObject) {
    return downloadOnly().load(paramObject);
  }
  
  public RequestBuilder<File> downloadOnly() {
    return as(File.class).apply((BaseRequestOptions<?>)DOWNLOAD_ONLY_OPTIONS);
  }
  
  List<RequestListener<Object>> getDefaultRequestListeners() {
    return this.defaultRequestListeners;
  }
  
  RequestOptions getDefaultRequestOptions() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield requestOptions : Lcom/bumptech/glide/request/RequestOptions;
    //   6: astore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_1
    //   10: areturn
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  <T> TransitionOptions<?, T> getDefaultTransitionOptions(Class<T> paramClass) {
    return this.glide.getGlideContext().getDefaultTransitionOptions(paramClass);
  }
  
  public boolean isPaused() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield requestTracker : Lcom/bumptech/glide/manager/RequestTracker;
    //   6: invokevirtual isPaused : ()Z
    //   9: istore_1
    //   10: aload_0
    //   11: monitorexit
    //   12: iload_1
    //   13: ireturn
    //   14: astore_2
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_2
    //   18: athrow
    // Exception table:
    //   from	to	target	type
    //   2	10	14	finally
  }
  
  public RequestBuilder<Drawable> load(Bitmap paramBitmap) {
    return asDrawable().load(paramBitmap);
  }
  
  public RequestBuilder<Drawable> load(Drawable paramDrawable) {
    return asDrawable().load(paramDrawable);
  }
  
  public RequestBuilder<Drawable> load(Uri paramUri) {
    return asDrawable().load(paramUri);
  }
  
  public RequestBuilder<Drawable> load(File paramFile) {
    return asDrawable().load(paramFile);
  }
  
  public RequestBuilder<Drawable> load(Integer paramInteger) {
    return asDrawable().load(paramInteger);
  }
  
  public RequestBuilder<Drawable> load(Object paramObject) {
    return asDrawable().load(paramObject);
  }
  
  public RequestBuilder<Drawable> load(String paramString) {
    return asDrawable().load(paramString);
  }
  
  @Deprecated
  public RequestBuilder<Drawable> load(URL paramURL) {
    return asDrawable().load(paramURL);
  }
  
  public RequestBuilder<Drawable> load(byte[] paramArrayOfbyte) {
    return asDrawable().load(paramArrayOfbyte);
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {}
  
  public void onDestroy() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield targetTracker : Lcom/bumptech/glide/manager/TargetTracker;
    //   6: invokevirtual onDestroy : ()V
    //   9: aload_0
    //   10: getfield targetTracker : Lcom/bumptech/glide/manager/TargetTracker;
    //   13: invokevirtual getAll : ()Ljava/util/List;
    //   16: invokeinterface iterator : ()Ljava/util/Iterator;
    //   21: astore_1
    //   22: aload_1
    //   23: invokeinterface hasNext : ()Z
    //   28: ifeq -> 47
    //   31: aload_0
    //   32: aload_1
    //   33: invokeinterface next : ()Ljava/lang/Object;
    //   38: checkcast com/bumptech/glide/request/target/Target
    //   41: invokevirtual clear : (Lcom/bumptech/glide/request/target/Target;)V
    //   44: goto -> 22
    //   47: aload_0
    //   48: getfield targetTracker : Lcom/bumptech/glide/manager/TargetTracker;
    //   51: invokevirtual clear : ()V
    //   54: aload_0
    //   55: getfield requestTracker : Lcom/bumptech/glide/manager/RequestTracker;
    //   58: invokevirtual clearRequests : ()V
    //   61: aload_0
    //   62: getfield lifecycle : Lcom/bumptech/glide/manager/Lifecycle;
    //   65: aload_0
    //   66: invokeinterface removeListener : (Lcom/bumptech/glide/manager/LifecycleListener;)V
    //   71: aload_0
    //   72: getfield lifecycle : Lcom/bumptech/glide/manager/Lifecycle;
    //   75: aload_0
    //   76: getfield connectivityMonitor : Lcom/bumptech/glide/manager/ConnectivityMonitor;
    //   79: invokeinterface removeListener : (Lcom/bumptech/glide/manager/LifecycleListener;)V
    //   84: aload_0
    //   85: getfield mainHandler : Landroid/os/Handler;
    //   88: aload_0
    //   89: getfield addSelfToLifecycle : Ljava/lang/Runnable;
    //   92: invokevirtual removeCallbacks : (Ljava/lang/Runnable;)V
    //   95: aload_0
    //   96: getfield glide : Lcom/bumptech/glide/Glide;
    //   99: aload_0
    //   100: invokevirtual unregisterRequestManager : (Lcom/bumptech/glide/RequestManager;)V
    //   103: aload_0
    //   104: monitorexit
    //   105: return
    //   106: astore_1
    //   107: aload_0
    //   108: monitorexit
    //   109: goto -> 114
    //   112: aload_1
    //   113: athrow
    //   114: goto -> 112
    // Exception table:
    //   from	to	target	type
    //   2	22	106	finally
    //   22	44	106	finally
    //   47	103	106	finally
  }
  
  public void onLowMemory() {}
  
  public void onStart() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual resumeRequests : ()V
    //   6: aload_0
    //   7: getfield targetTracker : Lcom/bumptech/glide/manager/TargetTracker;
    //   10: invokevirtual onStart : ()V
    //   13: aload_0
    //   14: monitorexit
    //   15: return
    //   16: astore_1
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_1
    //   20: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	16	finally
  }
  
  public void onStop() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual pauseRequests : ()V
    //   6: aload_0
    //   7: getfield targetTracker : Lcom/bumptech/glide/manager/TargetTracker;
    //   10: invokevirtual onStop : ()V
    //   13: aload_0
    //   14: monitorexit
    //   15: return
    //   16: astore_1
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_1
    //   20: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	16	finally
  }
  
  public void onTrimMemory(int paramInt) {
    if (paramInt == 60 && this.pauseAllRequestsOnTrimMemoryModerate)
      pauseAllRequestsRecursive(); 
  }
  
  public void pauseAllRequests() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield requestTracker : Lcom/bumptech/glide/manager/RequestTracker;
    //   6: invokevirtual pauseAllRequests : ()V
    //   9: aload_0
    //   10: monitorexit
    //   11: return
    //   12: astore_1
    //   13: aload_0
    //   14: monitorexit
    //   15: aload_1
    //   16: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	12	finally
  }
  
  public void pauseAllRequestsRecursive() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual pauseAllRequests : ()V
    //   6: aload_0
    //   7: getfield treeNode : Lcom/bumptech/glide/manager/RequestManagerTreeNode;
    //   10: invokeinterface getDescendants : ()Ljava/util/Set;
    //   15: invokeinterface iterator : ()Ljava/util/Iterator;
    //   20: astore_1
    //   21: aload_1
    //   22: invokeinterface hasNext : ()Z
    //   27: ifeq -> 45
    //   30: aload_1
    //   31: invokeinterface next : ()Ljava/lang/Object;
    //   36: checkcast com/bumptech/glide/RequestManager
    //   39: invokevirtual pauseAllRequests : ()V
    //   42: goto -> 21
    //   45: aload_0
    //   46: monitorexit
    //   47: return
    //   48: astore_1
    //   49: aload_0
    //   50: monitorexit
    //   51: goto -> 56
    //   54: aload_1
    //   55: athrow
    //   56: goto -> 54
    // Exception table:
    //   from	to	target	type
    //   2	21	48	finally
    //   21	42	48	finally
  }
  
  public void pauseRequests() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield requestTracker : Lcom/bumptech/glide/manager/RequestTracker;
    //   6: invokevirtual pauseRequests : ()V
    //   9: aload_0
    //   10: monitorexit
    //   11: return
    //   12: astore_1
    //   13: aload_0
    //   14: monitorexit
    //   15: aload_1
    //   16: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	12	finally
  }
  
  public void pauseRequestsRecursive() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual pauseRequests : ()V
    //   6: aload_0
    //   7: getfield treeNode : Lcom/bumptech/glide/manager/RequestManagerTreeNode;
    //   10: invokeinterface getDescendants : ()Ljava/util/Set;
    //   15: invokeinterface iterator : ()Ljava/util/Iterator;
    //   20: astore_1
    //   21: aload_1
    //   22: invokeinterface hasNext : ()Z
    //   27: ifeq -> 45
    //   30: aload_1
    //   31: invokeinterface next : ()Ljava/lang/Object;
    //   36: checkcast com/bumptech/glide/RequestManager
    //   39: invokevirtual pauseRequests : ()V
    //   42: goto -> 21
    //   45: aload_0
    //   46: monitorexit
    //   47: return
    //   48: astore_1
    //   49: aload_0
    //   50: monitorexit
    //   51: goto -> 56
    //   54: aload_1
    //   55: athrow
    //   56: goto -> 54
    // Exception table:
    //   from	to	target	type
    //   2	21	48	finally
    //   21	42	48	finally
  }
  
  public void resumeRequests() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield requestTracker : Lcom/bumptech/glide/manager/RequestTracker;
    //   6: invokevirtual resumeRequests : ()V
    //   9: aload_0
    //   10: monitorexit
    //   11: return
    //   12: astore_1
    //   13: aload_0
    //   14: monitorexit
    //   15: aload_1
    //   16: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	12	finally
  }
  
  public void resumeRequestsRecursive() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: invokestatic assertMainThread : ()V
    //   5: aload_0
    //   6: invokevirtual resumeRequests : ()V
    //   9: aload_0
    //   10: getfield treeNode : Lcom/bumptech/glide/manager/RequestManagerTreeNode;
    //   13: invokeinterface getDescendants : ()Ljava/util/Set;
    //   18: invokeinterface iterator : ()Ljava/util/Iterator;
    //   23: astore_1
    //   24: aload_1
    //   25: invokeinterface hasNext : ()Z
    //   30: ifeq -> 48
    //   33: aload_1
    //   34: invokeinterface next : ()Ljava/lang/Object;
    //   39: checkcast com/bumptech/glide/RequestManager
    //   42: invokevirtual resumeRequests : ()V
    //   45: goto -> 24
    //   48: aload_0
    //   49: monitorexit
    //   50: return
    //   51: astore_1
    //   52: aload_0
    //   53: monitorexit
    //   54: goto -> 59
    //   57: aload_1
    //   58: athrow
    //   59: goto -> 57
    // Exception table:
    //   from	to	target	type
    //   2	24	51	finally
    //   24	45	51	finally
  }
  
  public RequestManager setDefaultRequestOptions(RequestOptions paramRequestOptions) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: invokevirtual setRequestOptions : (Lcom/bumptech/glide/request/RequestOptions;)V
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_0
    //   10: areturn
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public void setPauseAllRequestsOnTrimMemoryModerate(boolean paramBoolean) {
    this.pauseAllRequestsOnTrimMemoryModerate = paramBoolean;
  }
  
  protected void setRequestOptions(RequestOptions paramRequestOptions) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: invokevirtual clone : ()Lcom/bumptech/glide/request/BaseRequestOptions;
    //   7: checkcast com/bumptech/glide/request/RequestOptions
    //   10: invokevirtual autoClone : ()Lcom/bumptech/glide/request/BaseRequestOptions;
    //   13: checkcast com/bumptech/glide/request/RequestOptions
    //   16: putfield requestOptions : Lcom/bumptech/glide/request/RequestOptions;
    //   19: aload_0
    //   20: monitorexit
    //   21: return
    //   22: astore_1
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_1
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	22	finally
  }
  
  public String toString() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/lang/StringBuilder
    //   5: astore_1
    //   6: aload_1
    //   7: invokespecial <init> : ()V
    //   10: aload_1
    //   11: aload_0
    //   12: invokespecial toString : ()Ljava/lang/String;
    //   15: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   18: pop
    //   19: aload_1
    //   20: ldc_w '{tracker='
    //   23: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   26: pop
    //   27: aload_1
    //   28: aload_0
    //   29: getfield requestTracker : Lcom/bumptech/glide/manager/RequestTracker;
    //   32: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   35: pop
    //   36: aload_1
    //   37: ldc_w ', treeNode='
    //   40: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   43: pop
    //   44: aload_1
    //   45: aload_0
    //   46: getfield treeNode : Lcom/bumptech/glide/manager/RequestManagerTreeNode;
    //   49: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   52: pop
    //   53: aload_1
    //   54: ldc_w '}'
    //   57: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   60: pop
    //   61: aload_1
    //   62: invokevirtual toString : ()Ljava/lang/String;
    //   65: astore_1
    //   66: aload_0
    //   67: monitorexit
    //   68: aload_1
    //   69: areturn
    //   70: astore_1
    //   71: aload_0
    //   72: monitorexit
    //   73: aload_1
    //   74: athrow
    // Exception table:
    //   from	to	target	type
    //   2	66	70	finally
  }
  
  void track(Target<?> paramTarget, Request paramRequest) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield targetTracker : Lcom/bumptech/glide/manager/TargetTracker;
    //   6: aload_1
    //   7: invokevirtual track : (Lcom/bumptech/glide/request/target/Target;)V
    //   10: aload_0
    //   11: getfield requestTracker : Lcom/bumptech/glide/manager/RequestTracker;
    //   14: aload_2
    //   15: invokevirtual runRequest : (Lcom/bumptech/glide/request/Request;)V
    //   18: aload_0
    //   19: monitorexit
    //   20: return
    //   21: astore_1
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_1
    //   25: athrow
    // Exception table:
    //   from	to	target	type
    //   2	18	21	finally
  }
  
  boolean untrack(Target<?> paramTarget) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokeinterface getRequest : ()Lcom/bumptech/glide/request/Request;
    //   8: astore_2
    //   9: aload_2
    //   10: ifnonnull -> 17
    //   13: aload_0
    //   14: monitorexit
    //   15: iconst_1
    //   16: ireturn
    //   17: aload_0
    //   18: getfield requestTracker : Lcom/bumptech/glide/manager/RequestTracker;
    //   21: aload_2
    //   22: invokevirtual clearAndRemove : (Lcom/bumptech/glide/request/Request;)Z
    //   25: ifeq -> 47
    //   28: aload_0
    //   29: getfield targetTracker : Lcom/bumptech/glide/manager/TargetTracker;
    //   32: aload_1
    //   33: invokevirtual untrack : (Lcom/bumptech/glide/request/target/Target;)V
    //   36: aload_1
    //   37: aconst_null
    //   38: invokeinterface setRequest : (Lcom/bumptech/glide/request/Request;)V
    //   43: aload_0
    //   44: monitorexit
    //   45: iconst_1
    //   46: ireturn
    //   47: aload_0
    //   48: monitorexit
    //   49: iconst_0
    //   50: ireturn
    //   51: astore_1
    //   52: aload_0
    //   53: monitorexit
    //   54: aload_1
    //   55: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	51	finally
    //   17	43	51	finally
  }
  
  private static class ClearTarget extends CustomViewTarget<View, Object> {
    ClearTarget(View param1View) {
      super(param1View);
    }
    
    public void onLoadFailed(Drawable param1Drawable) {}
    
    protected void onResourceCleared(Drawable param1Drawable) {}
    
    public void onResourceReady(Object param1Object, Transition<? super Object> param1Transition) {}
  }
  
  private class RequestManagerConnectivityListener implements ConnectivityMonitor.ConnectivityListener {
    private final RequestTracker requestTracker;
    
    RequestManagerConnectivityListener(RequestTracker param1RequestTracker) {
      this.requestTracker = param1RequestTracker;
    }
    
    public void onConnectivityChanged(boolean param1Boolean) {
      if (param1Boolean)
        synchronized (RequestManager.this) {
          this.requestTracker.restartRequests();
        }  
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/RequestManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
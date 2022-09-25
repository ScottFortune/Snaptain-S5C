package com.bumptech.glide.request;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.drawable.DrawableDecoderCompat;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.request.transition.TransitionFactory;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.StateVerifier;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public final class SingleRequest<R> implements Request, SizeReadyCallback, ResourceCallback {
  private static final String GLIDE_TAG = "Glide";
  
  private static final boolean IS_VERBOSE_LOGGABLE = Log.isLoggable("Request", 2);
  
  private static final String TAG = "Request";
  
  private final TransitionFactory<? super R> animationFactory;
  
  private final Executor callbackExecutor;
  
  private final Context context;
  
  private volatile Engine engine;
  
  private Drawable errorDrawable;
  
  private Drawable fallbackDrawable;
  
  private final GlideContext glideContext;
  
  private int height;
  
  private boolean isCallingCallbacks;
  
  private Engine.LoadStatus loadStatus;
  
  private final Object model;
  
  private final int overrideHeight;
  
  private final int overrideWidth;
  
  private Drawable placeholderDrawable;
  
  private final Priority priority;
  
  private final RequestCoordinator requestCoordinator;
  
  private final List<RequestListener<R>> requestListeners;
  
  private final Object requestLock;
  
  private final BaseRequestOptions<?> requestOptions;
  
  private RuntimeException requestOrigin;
  
  private Resource<R> resource;
  
  private long startTime;
  
  private final StateVerifier stateVerifier;
  
  private Status status;
  
  private final String tag;
  
  private final Target<R> target;
  
  private final RequestListener<R> targetListener;
  
  private final Class<R> transcodeClass;
  
  private int width;
  
  private SingleRequest(Context paramContext, GlideContext paramGlideContext, Object paramObject1, Object paramObject2, Class<R> paramClass, BaseRequestOptions<?> paramBaseRequestOptions, int paramInt1, int paramInt2, Priority paramPriority, Target<R> paramTarget, RequestListener<R> paramRequestListener, List<RequestListener<R>> paramList, RequestCoordinator paramRequestCoordinator, Engine paramEngine, TransitionFactory<? super R> paramTransitionFactory, Executor paramExecutor) {
    String str;
    if (IS_VERBOSE_LOGGABLE) {
      str = String.valueOf(hashCode());
    } else {
      str = null;
    } 
    this.tag = str;
    this.stateVerifier = StateVerifier.newInstance();
    this.requestLock = paramObject1;
    this.context = paramContext;
    this.glideContext = paramGlideContext;
    this.model = paramObject2;
    this.transcodeClass = paramClass;
    this.requestOptions = paramBaseRequestOptions;
    this.overrideWidth = paramInt1;
    this.overrideHeight = paramInt2;
    this.priority = paramPriority;
    this.target = paramTarget;
    this.targetListener = paramRequestListener;
    this.requestListeners = paramList;
    this.requestCoordinator = paramRequestCoordinator;
    this.engine = paramEngine;
    this.animationFactory = paramTransitionFactory;
    this.callbackExecutor = paramExecutor;
    this.status = Status.PENDING;
    if (this.requestOrigin == null && paramGlideContext.isLoggingRequestOriginsEnabled())
      this.requestOrigin = new RuntimeException("Glide request origin trace"); 
  }
  
  private void assertNotCallingCallbacks() {
    if (!this.isCallingCallbacks)
      return; 
    throw new IllegalStateException("You can't start or clear loads in RequestListener or Target callbacks. If you're trying to start a fallback request when a load fails, use RequestBuilder#error(RequestBuilder). Otherwise consider posting your into() or clear() calls to the main thread using a Handler instead.");
  }
  
  private boolean canNotifyCleared() {
    RequestCoordinator requestCoordinator = this.requestCoordinator;
    return (requestCoordinator == null || requestCoordinator.canNotifyCleared(this));
  }
  
  private boolean canNotifyStatusChanged() {
    RequestCoordinator requestCoordinator = this.requestCoordinator;
    return (requestCoordinator == null || requestCoordinator.canNotifyStatusChanged(this));
  }
  
  private boolean canSetResource() {
    RequestCoordinator requestCoordinator = this.requestCoordinator;
    return (requestCoordinator == null || requestCoordinator.canSetImage(this));
  }
  
  private void cancel() {
    assertNotCallingCallbacks();
    this.stateVerifier.throwIfRecycled();
    this.target.removeCallback(this);
    Engine.LoadStatus loadStatus = this.loadStatus;
    if (loadStatus != null) {
      loadStatus.cancel();
      this.loadStatus = null;
    } 
  }
  
  private Drawable getErrorDrawable() {
    if (this.errorDrawable == null) {
      this.errorDrawable = this.requestOptions.getErrorPlaceholder();
      if (this.errorDrawable == null && this.requestOptions.getErrorId() > 0)
        this.errorDrawable = loadDrawable(this.requestOptions.getErrorId()); 
    } 
    return this.errorDrawable;
  }
  
  private Drawable getFallbackDrawable() {
    if (this.fallbackDrawable == null) {
      this.fallbackDrawable = this.requestOptions.getFallbackDrawable();
      if (this.fallbackDrawable == null && this.requestOptions.getFallbackId() > 0)
        this.fallbackDrawable = loadDrawable(this.requestOptions.getFallbackId()); 
    } 
    return this.fallbackDrawable;
  }
  
  private Drawable getPlaceholderDrawable() {
    if (this.placeholderDrawable == null) {
      this.placeholderDrawable = this.requestOptions.getPlaceholderDrawable();
      if (this.placeholderDrawable == null && this.requestOptions.getPlaceholderId() > 0)
        this.placeholderDrawable = loadDrawable(this.requestOptions.getPlaceholderId()); 
    } 
    return this.placeholderDrawable;
  }
  
  private boolean isFirstReadyResource() {
    RequestCoordinator requestCoordinator = this.requestCoordinator;
    return (requestCoordinator == null || !requestCoordinator.isAnyResourceSet());
  }
  
  private Drawable loadDrawable(int paramInt) {
    Resources.Theme theme;
    if (this.requestOptions.getTheme() != null) {
      theme = this.requestOptions.getTheme();
    } else {
      theme = this.context.getTheme();
    } 
    return DrawableDecoderCompat.getDrawable((Context)this.glideContext, paramInt, theme);
  }
  
  private void logV(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString);
    stringBuilder.append(" this: ");
    stringBuilder.append(this.tag);
    Log.v("Request", stringBuilder.toString());
  }
  
  private static int maybeApplySizeMultiplier(int paramInt, float paramFloat) {
    if (paramInt != Integer.MIN_VALUE)
      paramInt = Math.round(paramFloat * paramInt); 
    return paramInt;
  }
  
  private void notifyLoadFailed() {
    RequestCoordinator requestCoordinator = this.requestCoordinator;
    if (requestCoordinator != null)
      requestCoordinator.onRequestFailed(this); 
  }
  
  private void notifyLoadSuccess() {
    RequestCoordinator requestCoordinator = this.requestCoordinator;
    if (requestCoordinator != null)
      requestCoordinator.onRequestSuccess(this); 
  }
  
  public static <R> SingleRequest<R> obtain(Context paramContext, GlideContext paramGlideContext, Object paramObject1, Object paramObject2, Class<R> paramClass, BaseRequestOptions<?> paramBaseRequestOptions, int paramInt1, int paramInt2, Priority paramPriority, Target<R> paramTarget, RequestListener<R> paramRequestListener, List<RequestListener<R>> paramList, RequestCoordinator paramRequestCoordinator, Engine paramEngine, TransitionFactory<? super R> paramTransitionFactory, Executor paramExecutor) {
    return new SingleRequest<R>(paramContext, paramGlideContext, paramObject1, paramObject2, paramClass, paramBaseRequestOptions, paramInt1, paramInt2, paramPriority, paramTarget, paramRequestListener, paramList, paramRequestCoordinator, paramEngine, paramTransitionFactory, paramExecutor);
  }
  
  private void onLoadFailed(GlideException paramGlideException, int paramInt) {
    this.stateVerifier.throwIfRecycled();
    synchronized (this.requestLock) {
      paramGlideException.setOrigin(this.requestOrigin);
      int i = this.glideContext.getLogLevel();
      if (i <= paramInt) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append("Load failed for ");
        stringBuilder.append(this.model);
        stringBuilder.append(" with size [");
        stringBuilder.append(this.width);
        stringBuilder.append("x");
        stringBuilder.append(this.height);
        stringBuilder.append("]");
        Log.w("Glide", stringBuilder.toString(), (Throwable)paramGlideException);
        if (i <= 4)
          paramGlideException.logRootCauses("Glide"); 
      } 
      this.loadStatus = null;
      this.status = Status.FAILED;
      boolean bool = true;
      this.isCallingCallbacks = true;
      try {
        if (this.requestListeners != null) {
          Iterator<RequestListener<R>> iterator = this.requestListeners.iterator();
          paramInt = 0;
          while (true) {
            i = paramInt;
            if (iterator.hasNext()) {
              boolean bool1 = paramInt | ((RequestListener<R>)iterator.next()).onLoadFailed(paramGlideException, this.model, this.target, isFirstReadyResource());
              continue;
            } 
            break;
          } 
        } else {
          i = 0;
        } 
        if (this.targetListener != null && this.targetListener.onLoadFailed(paramGlideException, this.model, this.target, isFirstReadyResource())) {
          paramInt = bool;
        } else {
          paramInt = 0;
        } 
        if ((i | paramInt) == 0)
          setErrorPlaceholder(); 
        this.isCallingCallbacks = false;
        return;
      } finally {
        this.isCallingCallbacks = false;
      } 
    } 
  }
  
  private void onResourceReady(Resource<R> paramResource, R paramR, DataSource paramDataSource) {
    boolean bool = isFirstReadyResource();
    this.status = Status.COMPLETE;
    this.resource = paramResource;
    if (this.glideContext.getLogLevel() <= 3) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Finished loading ");
      stringBuilder.append(paramR.getClass().getSimpleName());
      stringBuilder.append(" from ");
      stringBuilder.append(paramDataSource);
      stringBuilder.append(" for ");
      stringBuilder.append(this.model);
      stringBuilder.append(" with size [");
      stringBuilder.append(this.width);
      stringBuilder.append("x");
      stringBuilder.append(this.height);
      stringBuilder.append("] in ");
      stringBuilder.append(LogTime.getElapsedMillis(this.startTime));
      stringBuilder.append(" ms");
      Log.d("Glide", stringBuilder.toString());
    } 
    boolean bool1 = true;
    this.isCallingCallbacks = true;
    try {
      boolean bool2;
      boolean bool3;
      if (this.requestListeners != null) {
        Iterator<RequestListener<R>> iterator = this.requestListeners.iterator();
        bool2 = false;
        while (true) {
          bool3 = bool2;
          if (iterator.hasNext()) {
            bool2 |= ((RequestListener<R>)iterator.next()).onResourceReady(paramR, this.model, this.target, paramDataSource, bool);
            continue;
          } 
          break;
        } 
      } else {
        bool3 = false;
      } 
      if (this.targetListener != null && this.targetListener.onResourceReady(paramR, this.model, this.target, paramDataSource, bool)) {
        bool2 = bool1;
      } else {
        bool2 = false;
      } 
      if ((bool2 | bool3) == 0) {
        Transition transition = this.animationFactory.build(paramDataSource, bool);
        this.target.onResourceReady(paramR, transition);
      } 
      this.isCallingCallbacks = false;
      return;
    } finally {
      this.isCallingCallbacks = false;
    } 
  }
  
  private void setErrorPlaceholder() {
    if (!canNotifyStatusChanged())
      return; 
    Drawable drawable1 = null;
    if (this.model == null)
      drawable1 = getFallbackDrawable(); 
    Drawable drawable2 = drawable1;
    if (drawable1 == null)
      drawable2 = getErrorDrawable(); 
    drawable1 = drawable2;
    if (drawable2 == null)
      drawable1 = getPlaceholderDrawable(); 
    this.target.onLoadFailed(drawable1);
  }
  
  public void begin() {
    synchronized (this.requestLock) {
      assertNotCallingCallbacks();
      this.stateVerifier.throwIfRecycled();
      this.startTime = LogTime.getLogTime();
      if (this.model == null) {
        byte b;
        if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight)) {
          this.width = this.overrideWidth;
          this.height = this.overrideHeight;
        } 
        if (getFallbackDrawable() == null) {
          b = 5;
        } else {
          b = 3;
        } 
        GlideException glideException = new GlideException();
        this("Received null model");
        onLoadFailed(glideException, b);
        return;
      } 
      if (this.status != Status.RUNNING) {
        if (this.status == Status.COMPLETE) {
          onResourceReady(this.resource, DataSource.MEMORY_CACHE);
          return;
        } 
        this.status = Status.WAITING_FOR_SIZE;
        if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight)) {
          onSizeReady(this.overrideWidth, this.overrideHeight);
        } else {
          this.target.getSize(this);
        } 
        if ((this.status == Status.RUNNING || this.status == Status.WAITING_FOR_SIZE) && canNotifyStatusChanged())
          this.target.onLoadStarted(getPlaceholderDrawable()); 
        if (IS_VERBOSE_LOGGABLE) {
          StringBuilder stringBuilder = new StringBuilder();
          this();
          stringBuilder.append("finished run method in ");
          stringBuilder.append(LogTime.getElapsedMillis(this.startTime));
          logV(stringBuilder.toString());
        } 
        return;
      } 
      IllegalArgumentException illegalArgumentException = new IllegalArgumentException();
      this("Cannot restart a running request");
      throw illegalArgumentException;
    } 
  }
  
  public void clear() {
    synchronized (this.requestLock) {
      Resource resource;
      assertNotCallingCallbacks();
      this.stateVerifier.throwIfRecycled();
      if (this.status == Status.CLEARED)
        return; 
      cancel();
      if (this.resource != null) {
        resource = this.resource;
        this.resource = null;
      } else {
        resource = null;
      } 
      if (canNotifyCleared())
        this.target.onLoadCleared(getPlaceholderDrawable()); 
      this.status = Status.CLEARED;
      if (resource != null)
        this.engine.release(resource); 
      return;
    } 
  }
  
  public Object getLock() {
    this.stateVerifier.throwIfRecycled();
    return this.requestLock;
  }
  
  public boolean isCleared() {
    synchronized (this.requestLock) {
      boolean bool;
      if (this.status == Status.CLEARED) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
  }
  
  public boolean isComplete() {
    synchronized (this.requestLock) {
      boolean bool;
      if (this.status == Status.COMPLETE) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
  }
  
  public boolean isEquivalentTo(Request paramRequest) {
    if (!(paramRequest instanceof SingleRequest))
      return false; 
    synchronized (this.requestLock) {
      boolean bool;
      int i = this.overrideWidth;
      int j = this.overrideHeight;
      Object object = this.model;
      null = this.transcodeClass;
      BaseRequestOptions<?> baseRequestOptions = this.requestOptions;
      Priority priority = this.priority;
      if (this.requestListeners != null) {
        bool = this.requestListeners.size();
      } else {
        bool = false;
      } 
      SingleRequest singleRequest = (SingleRequest)paramRequest;
      synchronized (singleRequest.requestLock) {
        boolean bool1;
        boolean bool2;
        int k = singleRequest.overrideWidth;
        int m = singleRequest.overrideHeight;
        null = singleRequest.model;
        Class<R> clazz = singleRequest.transcodeClass;
        BaseRequestOptions<?> baseRequestOptions1 = singleRequest.requestOptions;
        Priority priority1 = singleRequest.priority;
        if (singleRequest.requestListeners != null) {
          bool1 = singleRequest.requestListeners.size();
        } else {
          bool1 = false;
        } 
        if (i == k && j == m && Util.bothModelsNullEquivalentOrEquals(object, null) && null.equals(clazz) && baseRequestOptions.equals(baseRequestOptions1) && priority == priority1 && bool == bool1) {
          bool2 = true;
        } else {
          bool2 = false;
        } 
        return bool2;
      } 
    } 
  }
  
  public boolean isRunning() {
    synchronized (this.requestLock) {
      if (this.status == Status.RUNNING || this.status == Status.WAITING_FOR_SIZE)
        return true; 
      return false;
    } 
  }
  
  public void onLoadFailed(GlideException paramGlideException) {
    onLoadFailed(paramGlideException, 5);
  }
  
  public void onResourceReady(Resource<?> paramResource, DataSource paramDataSource) {
    Class<?> clazz1;
    this.stateVerifier.throwIfRecycled();
    Class<?> clazz2 = null;
    try {
      Object object = this.requestLock;
      /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
      try {
        GlideException glideException1;
        GlideException glideException2;
        this.loadStatus = null;
        if (paramResource == null) {
          glideException1 = new GlideException();
          StringBuilder stringBuilder = new StringBuilder();
          this();
          stringBuilder.append("Expected to receive a Resource<R> with an object of ");
          stringBuilder.append(this.transcodeClass);
          stringBuilder.append(" inside, but instead got null.");
          this(stringBuilder.toString());
          return;
        } 
        Object object1 = glideException1.get();
        if (object1 == null || !this.transcodeClass.isAssignableFrom(object1.getClass())) {
          String str;
          glideException2 = glideException1;
          this.resource = null;
          glideException2 = glideException1;
          GlideException glideException = new GlideException();
          glideException2 = glideException1;
          StringBuilder stringBuilder = new StringBuilder();
          glideException2 = glideException1;
          this();
          glideException2 = glideException1;
          stringBuilder.append("Expected to receive an object of ");
          glideException2 = glideException1;
          stringBuilder.append(this.transcodeClass);
          glideException2 = glideException1;
          stringBuilder.append(" but instead got ");
          if (object1 != null) {
            glideException2 = glideException1;
            clazz2 = object1.getClass();
          } else {
            str = "";
          } 
          glideException2 = glideException1;
          stringBuilder.append(str);
          glideException2 = glideException1;
          stringBuilder.append("{");
          glideException2 = glideException1;
          stringBuilder.append(object1);
          glideException2 = glideException1;
          stringBuilder.append("} inside Resource{");
          glideException2 = glideException1;
          stringBuilder.append(glideException1);
          glideException2 = glideException1;
          stringBuilder.append("}.");
          if (object1 != null) {
            str = "";
          } else {
            str = " To indicate failure return a null Resource object, rather than a Resource object containing null data.";
          } 
          glideException2 = glideException1;
          stringBuilder.append(str);
          glideException2 = glideException1;
          this(stringBuilder.toString());
          glideException2 = glideException1;
          onLoadFailed(glideException);
          glideException2 = glideException1;
          return;
        } 
        boolean bool = canSetResource();
      } finally {
        clazz2 = null;
      } 
      Resource<?> resource = paramResource;
      /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
    } finally {
      paramDataSource = null;
    } 
    if (clazz1 != null)
      this.engine.release((Resource)clazz1); 
    throw paramDataSource;
  }
  
  public void onSizeReady(int paramInt1, int paramInt2) {
    Exception exception;
    this.stateVerifier.throwIfRecycled();
    Object object1 = this.requestLock;
    /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
    try {
      if (IS_VERBOSE_LOGGABLE) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append("Got onSizeReady in ");
        stringBuilder.append(LogTime.getElapsedMillis(this.startTime));
        logV(stringBuilder.toString());
      } 
      if (this.status != Status.WAITING_FOR_SIZE) {
        /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
        return;
      } 
      this.status = Status.RUNNING;
      float f = this.requestOptions.getSizeMultiplier();
      this.width = maybeApplySizeMultiplier(paramInt1, f);
      this.height = maybeApplySizeMultiplier(paramInt2, f);
      if (IS_VERBOSE_LOGGABLE) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append("finished setup for calling load in ");
        stringBuilder.append(LogTime.getElapsedMillis(this.startTime));
        logV(stringBuilder.toString());
      } 
      Engine engine = this.engine;
      GlideContext glideContext = this.glideContext;
      Object object = this.model;
      Key key = this.requestOptions.getSignature();
      paramInt2 = this.width;
      paramInt1 = this.height;
      Class<?> clazz = this.requestOptions.getResourceClass();
      Class<R> clazz1 = this.transcodeClass;
      Priority priority = this.priority;
      DiskCacheStrategy diskCacheStrategy = this.requestOptions.getDiskCacheStrategy();
      Map<Class<?>, Transformation<?>> map = this.requestOptions.getTransformations();
      boolean bool1 = this.requestOptions.isTransformationRequired();
      boolean bool2 = this.requestOptions.isScaleOnlyOrNoTransform();
      Options options = this.requestOptions.getOptions();
      boolean bool3 = this.requestOptions.isMemoryCacheable();
      boolean bool4 = this.requestOptions.getUseUnlimitedSourceGeneratorsPool();
      boolean bool5 = this.requestOptions.getUseAnimationPool();
      boolean bool6 = this.requestOptions.getOnlyRetrieveFromCache();
      Executor executor = this.callbackExecutor;
      try {
        Engine.LoadStatus loadStatus = engine.load(glideContext, object, key, paramInt2, paramInt1, clazz, clazz1, priority, diskCacheStrategy, map, bool1, bool2, options, bool3, bool4, bool5, bool6, this, executor);
        Object object3 = object1;
        try {
          this.loadStatus = loadStatus;
          object3 = object1;
          if (this.status != Status.RUNNING) {
            object3 = object1;
            this.loadStatus = null;
          } 
          object3 = object1;
          if (IS_VERBOSE_LOGGABLE) {
            object3 = object1;
            StringBuilder stringBuilder = new StringBuilder();
            object3 = object1;
            this();
            object3 = object1;
            stringBuilder.append("finished onSizeReady in ");
            object3 = object1;
            stringBuilder.append(LogTime.getElapsedMillis(this.startTime));
            object3 = object1;
            logV(stringBuilder.toString());
          } 
          object3 = object1;
          return;
        } finally {
          loadStatus = null;
        } 
      } finally {}
    } finally {}
    Object object2 = object1;
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
    throw exception;
  }
  
  public void pause() {
    synchronized (this.requestLock) {
      if (isRunning())
        clear(); 
      return;
    } 
  }
  
  private enum Status {
    CLEARED, COMPLETE, FAILED, PENDING, RUNNING, WAITING_FOR_SIZE;
    
    static {
      CLEARED = new Status("CLEARED", 5);
      $VALUES = new Status[] { PENDING, RUNNING, WAITING_FOR_SIZE, COMPLETE, FAILED, CLEARED };
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/SingleRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.bumptech.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.ErrorRequestCoordinator;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestCoordinator;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.SingleRequest;
import com.bumptech.glide.request.ThumbnailRequestCoordinator;
import com.bumptech.glide.request.target.PreloadTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.signature.AndroidResourceSignature;
import com.bumptech.glide.util.Executors;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

public class RequestBuilder<TranscodeType> extends BaseRequestOptions<RequestBuilder<TranscodeType>> implements Cloneable, ModelTypes<RequestBuilder<TranscodeType>> {
  protected static final RequestOptions DOWNLOAD_ONLY_OPTIONS = (RequestOptions)((RequestOptions)((RequestOptions)(new RequestOptions()).diskCacheStrategy(DiskCacheStrategy.DATA)).priority(Priority.LOW)).skipMemoryCache(true);
  
  private final Context context;
  
  private RequestBuilder<TranscodeType> errorBuilder;
  
  private final Glide glide;
  
  private final GlideContext glideContext;
  
  private boolean isDefaultTransitionOptionsSet = true;
  
  private boolean isModelSet;
  
  private boolean isThumbnailBuilt;
  
  private Object model;
  
  private List<RequestListener<TranscodeType>> requestListeners;
  
  private final RequestManager requestManager;
  
  private Float thumbSizeMultiplier;
  
  private RequestBuilder<TranscodeType> thumbnailBuilder;
  
  private final Class<TranscodeType> transcodeClass;
  
  private TransitionOptions<?, ? super TranscodeType> transitionOptions;
  
  protected RequestBuilder(Glide paramGlide, RequestManager paramRequestManager, Class<TranscodeType> paramClass, Context paramContext) {
    this.glide = paramGlide;
    this.requestManager = paramRequestManager;
    this.transcodeClass = paramClass;
    this.context = paramContext;
    this.transitionOptions = paramRequestManager.getDefaultTransitionOptions(paramClass);
    this.glideContext = paramGlide.getGlideContext();
    initRequestListeners(paramRequestManager.getDefaultRequestListeners());
    apply((BaseRequestOptions<?>)paramRequestManager.getDefaultRequestOptions());
  }
  
  protected RequestBuilder(Class<TranscodeType> paramClass, RequestBuilder<?> paramRequestBuilder) {
    this(paramRequestBuilder.glide, paramRequestBuilder.requestManager, paramClass, paramRequestBuilder.context);
    this.model = paramRequestBuilder.model;
    this.isModelSet = paramRequestBuilder.isModelSet;
    apply(paramRequestBuilder);
  }
  
  private Request buildRequest(Target<TranscodeType> paramTarget, RequestListener<TranscodeType> paramRequestListener, BaseRequestOptions<?> paramBaseRequestOptions, Executor paramExecutor) {
    return buildRequestRecursive(new Object(), paramTarget, paramRequestListener, (RequestCoordinator)null, this.transitionOptions, paramBaseRequestOptions.getPriority(), paramBaseRequestOptions.getOverrideWidth(), paramBaseRequestOptions.getOverrideHeight(), paramBaseRequestOptions, paramExecutor);
  }
  
  private Request buildRequestRecursive(Object paramObject, Target<TranscodeType> paramTarget, RequestListener<TranscodeType> paramRequestListener, RequestCoordinator paramRequestCoordinator, TransitionOptions<?, ? super TranscodeType> paramTransitionOptions, Priority paramPriority, int paramInt1, int paramInt2, BaseRequestOptions<?> paramBaseRequestOptions, Executor paramExecutor) {
    ErrorRequestCoordinator errorRequestCoordinator1;
    ErrorRequestCoordinator errorRequestCoordinator2;
    if (this.errorBuilder != null) {
      errorRequestCoordinator2 = new ErrorRequestCoordinator(paramObject, paramRequestCoordinator);
      errorRequestCoordinator1 = errorRequestCoordinator2;
    } else {
      ErrorRequestCoordinator errorRequestCoordinator = null;
      errorRequestCoordinator2 = errorRequestCoordinator1;
      errorRequestCoordinator1 = errorRequestCoordinator;
    } 
    Request request = buildThumbnailRequestRecursive(paramObject, paramTarget, paramRequestListener, (RequestCoordinator)errorRequestCoordinator2, paramTransitionOptions, paramPriority, paramInt1, paramInt2, paramBaseRequestOptions, paramExecutor);
    if (errorRequestCoordinator1 == null)
      return request; 
    int i = this.errorBuilder.getOverrideWidth();
    int j = this.errorBuilder.getOverrideHeight();
    int k = i;
    int m = j;
    if (Util.isValidDimensions(paramInt1, paramInt2)) {
      k = i;
      m = j;
      if (!this.errorBuilder.isValidOverride()) {
        k = paramBaseRequestOptions.getOverrideWidth();
        m = paramBaseRequestOptions.getOverrideHeight();
      } 
    } 
    RequestBuilder<TranscodeType> requestBuilder = this.errorBuilder;
    errorRequestCoordinator1.setRequests(request, requestBuilder.buildRequestRecursive(paramObject, paramTarget, paramRequestListener, (RequestCoordinator)errorRequestCoordinator1, requestBuilder.transitionOptions, requestBuilder.getPriority(), k, m, this.errorBuilder, paramExecutor));
    return (Request)errorRequestCoordinator1;
  }
  
  private Request buildThumbnailRequestRecursive(Object paramObject, Target<TranscodeType> paramTarget, RequestListener<TranscodeType> paramRequestListener, RequestCoordinator paramRequestCoordinator, TransitionOptions<?, ? super TranscodeType> paramTransitionOptions, Priority paramPriority, int paramInt1, int paramInt2, BaseRequestOptions<?> paramBaseRequestOptions, Executor paramExecutor) {
    ThumbnailRequestCoordinator thumbnailRequestCoordinator;
    Request request;
    RequestBuilder<TranscodeType> requestBuilder1;
    RequestBuilder<TranscodeType> requestBuilder2 = this.thumbnailBuilder;
    if (requestBuilder2 != null) {
      if (!this.isThumbnailBuilt) {
        Priority priority;
        TransitionOptions<?, ? super TranscodeType> transitionOptions = requestBuilder2.transitionOptions;
        if (requestBuilder2.isDefaultTransitionOptionsSet)
          transitionOptions = paramTransitionOptions; 
        if (this.thumbnailBuilder.isPrioritySet()) {
          priority = this.thumbnailBuilder.getPriority();
        } else {
          priority = getThumbnailPriority(paramPriority);
        } 
        int i = this.thumbnailBuilder.getOverrideWidth();
        int j = this.thumbnailBuilder.getOverrideHeight();
        int k = i;
        int m = j;
        if (Util.isValidDimensions(paramInt1, paramInt2)) {
          k = i;
          m = j;
          if (!this.thumbnailBuilder.isValidOverride()) {
            k = paramBaseRequestOptions.getOverrideWidth();
            m = paramBaseRequestOptions.getOverrideHeight();
          } 
        } 
        thumbnailRequestCoordinator = new ThumbnailRequestCoordinator(paramObject, paramRequestCoordinator);
        request = obtainRequest(paramObject, paramTarget, paramRequestListener, paramBaseRequestOptions, (RequestCoordinator)thumbnailRequestCoordinator, paramTransitionOptions, paramPriority, paramInt1, paramInt2, paramExecutor);
        this.isThumbnailBuilt = true;
        requestBuilder1 = this.thumbnailBuilder;
        paramObject = requestBuilder1.buildRequestRecursive(paramObject, paramTarget, paramRequestListener, (RequestCoordinator)thumbnailRequestCoordinator, transitionOptions, priority, k, m, requestBuilder1, paramExecutor);
        this.isThumbnailBuilt = false;
        thumbnailRequestCoordinator.setRequests(request, (Request)paramObject);
        return (Request)thumbnailRequestCoordinator;
      } 
      throw new IllegalStateException("You cannot use a request as both the main request and a thumbnail, consider using clone() on the request(s) passed to thumbnail()");
    } 
    if (this.thumbSizeMultiplier != null) {
      thumbnailRequestCoordinator = new ThumbnailRequestCoordinator(paramObject, (RequestCoordinator)thumbnailRequestCoordinator);
      thumbnailRequestCoordinator.setRequests(obtainRequest(paramObject, paramTarget, paramRequestListener, paramBaseRequestOptions, (RequestCoordinator)thumbnailRequestCoordinator, (TransitionOptions<?, ? super TranscodeType>)request, (Priority)requestBuilder1, paramInt1, paramInt2, paramExecutor), obtainRequest(paramObject, paramTarget, paramRequestListener, paramBaseRequestOptions.clone().sizeMultiplier(this.thumbSizeMultiplier.floatValue()), (RequestCoordinator)thumbnailRequestCoordinator, (TransitionOptions<?, ? super TranscodeType>)request, getThumbnailPriority((Priority)requestBuilder1), paramInt1, paramInt2, paramExecutor));
      return (Request)thumbnailRequestCoordinator;
    } 
    return obtainRequest(paramObject, paramTarget, paramRequestListener, paramBaseRequestOptions, (RequestCoordinator)thumbnailRequestCoordinator, (TransitionOptions<?, ? super TranscodeType>)request, (Priority)requestBuilder1, paramInt1, paramInt2, paramExecutor);
  }
  
  private Priority getThumbnailPriority(Priority paramPriority) {
    int i = null.$SwitchMap$com$bumptech$glide$Priority[paramPriority.ordinal()];
    if (i != 1) {
      if (i != 2) {
        if (i == 3 || i == 4)
          return Priority.IMMEDIATE; 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("unknown priority: ");
        stringBuilder.append(getPriority());
        throw new IllegalArgumentException(stringBuilder.toString());
      } 
      return Priority.HIGH;
    } 
    return Priority.NORMAL;
  }
  
  private void initRequestListeners(List<RequestListener<Object>> paramList) {
    Iterator<RequestListener<Object>> iterator = paramList.iterator();
    while (iterator.hasNext())
      addListener((RequestListener<TranscodeType>)iterator.next()); 
  }
  
  private <Y extends Target<TranscodeType>> Y into(Y paramY, RequestListener<TranscodeType> paramRequestListener, BaseRequestOptions<?> paramBaseRequestOptions, Executor paramExecutor) {
    Preconditions.checkNotNull(paramY);
    if (this.isModelSet) {
      Request request2 = buildRequest((Target<TranscodeType>)paramY, paramRequestListener, paramBaseRequestOptions, paramExecutor);
      Request request1 = paramY.getRequest();
      if (request2.isEquivalentTo(request1) && !isSkipMemoryCacheWithCompletePreviousRequest(paramBaseRequestOptions, request1)) {
        if (!((Request)Preconditions.checkNotNull(request1)).isRunning())
          request1.begin(); 
        return paramY;
      } 
      this.requestManager.clear((Target<?>)paramY);
      paramY.setRequest(request2);
      this.requestManager.track((Target<?>)paramY, request2);
      return paramY;
    } 
    throw new IllegalArgumentException("You must call #load() before calling #into()");
  }
  
  private boolean isSkipMemoryCacheWithCompletePreviousRequest(BaseRequestOptions<?> paramBaseRequestOptions, Request paramRequest) {
    boolean bool;
    if (!paramBaseRequestOptions.isMemoryCacheable() && paramRequest.isComplete()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private RequestBuilder<TranscodeType> loadGeneric(Object paramObject) {
    this.model = paramObject;
    this.isModelSet = true;
    return this;
  }
  
  private Request obtainRequest(Object paramObject, Target<TranscodeType> paramTarget, RequestListener<TranscodeType> paramRequestListener, BaseRequestOptions<?> paramBaseRequestOptions, RequestCoordinator paramRequestCoordinator, TransitionOptions<?, ? super TranscodeType> paramTransitionOptions, Priority paramPriority, int paramInt1, int paramInt2, Executor paramExecutor) {
    Context context = this.context;
    GlideContext glideContext = this.glideContext;
    return (Request)SingleRequest.obtain(context, glideContext, paramObject, this.model, this.transcodeClass, paramBaseRequestOptions, paramInt1, paramInt2, paramPriority, paramTarget, paramRequestListener, this.requestListeners, paramRequestCoordinator, glideContext.getEngine(), paramTransitionOptions.getTransitionFactory(), paramExecutor);
  }
  
  public RequestBuilder<TranscodeType> addListener(RequestListener<TranscodeType> paramRequestListener) {
    if (paramRequestListener != null) {
      if (this.requestListeners == null)
        this.requestListeners = new ArrayList<RequestListener<TranscodeType>>(); 
      this.requestListeners.add(paramRequestListener);
    } 
    return this;
  }
  
  public RequestBuilder<TranscodeType> apply(BaseRequestOptions<?> paramBaseRequestOptions) {
    Preconditions.checkNotNull(paramBaseRequestOptions);
    return (RequestBuilder<TranscodeType>)super.apply(paramBaseRequestOptions);
  }
  
  public RequestBuilder<TranscodeType> clone() {
    RequestBuilder<TranscodeType> requestBuilder = (RequestBuilder)super.clone();
    requestBuilder.transitionOptions = (TransitionOptions<?, ? super TranscodeType>)requestBuilder.transitionOptions.clone();
    return requestBuilder;
  }
  
  @Deprecated
  public FutureTarget<File> downloadOnly(int paramInt1, int paramInt2) {
    return getDownloadOnlyRequest().submit(paramInt1, paramInt2);
  }
  
  @Deprecated
  public <Y extends Target<File>> Y downloadOnly(Y paramY) {
    return (Y)getDownloadOnlyRequest().into((Target<File>)paramY);
  }
  
  public RequestBuilder<TranscodeType> error(RequestBuilder<TranscodeType> paramRequestBuilder) {
    this.errorBuilder = paramRequestBuilder;
    return this;
  }
  
  protected RequestBuilder<File> getDownloadOnlyRequest() {
    return (new RequestBuilder((Class)File.class, this)).apply((BaseRequestOptions<?>)DOWNLOAD_ONLY_OPTIONS);
  }
  
  @Deprecated
  public FutureTarget<TranscodeType> into(int paramInt1, int paramInt2) {
    return submit(paramInt1, paramInt2);
  }
  
  public <Y extends Target<TranscodeType>> Y into(Y paramY) {
    return into(paramY, (RequestListener<?>)null, Executors.mainThreadExecutor());
  }
  
  <Y extends Target<TranscodeType>> Y into(Y paramY, RequestListener<TranscodeType> paramRequestListener, Executor paramExecutor) {
    return into(paramY, paramRequestListener, this, paramExecutor);
  }
  
  public ViewTarget<ImageView, TranscodeType> into(ImageView paramImageView) {
    Util.assertMainThread();
    Preconditions.checkNotNull(paramImageView);
    if (!isTransformationSet() && isTransformationAllowed() && paramImageView.getScaleType() != null) {
      BaseRequestOptions<?> baseRequestOptions;
      switch (paramImageView.getScaleType()) {
        case null:
          baseRequestOptions = super.clone().optionalCenterInside();
          return into(this.glideContext.buildImageViewTarget(paramImageView, this.transcodeClass), (RequestListener<?>)null, baseRequestOptions, Executors.mainThreadExecutor());
        case HIGH:
        case IMMEDIATE:
        case null:
          baseRequestOptions = super.clone().optionalFitCenter();
          return into(this.glideContext.buildImageViewTarget(paramImageView, this.transcodeClass), (RequestListener<?>)null, baseRequestOptions, Executors.mainThreadExecutor());
        case NORMAL:
          baseRequestOptions = super.clone().optionalCenterInside();
          return into(this.glideContext.buildImageViewTarget(paramImageView, this.transcodeClass), (RequestListener<?>)null, baseRequestOptions, Executors.mainThreadExecutor());
        case LOW:
          baseRequestOptions = super.clone().optionalCenterCrop();
          return into(this.glideContext.buildImageViewTarget(paramImageView, this.transcodeClass), (RequestListener<?>)null, baseRequestOptions, Executors.mainThreadExecutor());
      } 
    } 
    RequestBuilder<?> requestBuilder = this;
    return into(this.glideContext.buildImageViewTarget(paramImageView, this.transcodeClass), (RequestListener<?>)null, requestBuilder, Executors.mainThreadExecutor());
  }
  
  public RequestBuilder<TranscodeType> listener(RequestListener<TranscodeType> paramRequestListener) {
    this.requestListeners = null;
    return addListener(paramRequestListener);
  }
  
  public RequestBuilder<TranscodeType> load(Bitmap paramBitmap) {
    return loadGeneric(paramBitmap).apply((BaseRequestOptions<?>)RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE));
  }
  
  public RequestBuilder<TranscodeType> load(Drawable paramDrawable) {
    return loadGeneric(paramDrawable).apply((BaseRequestOptions<?>)RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE));
  }
  
  public RequestBuilder<TranscodeType> load(Uri paramUri) {
    return loadGeneric(paramUri);
  }
  
  public RequestBuilder<TranscodeType> load(File paramFile) {
    return loadGeneric(paramFile);
  }
  
  public RequestBuilder<TranscodeType> load(Integer paramInteger) {
    return loadGeneric(paramInteger).apply((BaseRequestOptions<?>)RequestOptions.signatureOf(AndroidResourceSignature.obtain(this.context)));
  }
  
  public RequestBuilder<TranscodeType> load(Object paramObject) {
    return loadGeneric(paramObject);
  }
  
  public RequestBuilder<TranscodeType> load(String paramString) {
    return loadGeneric(paramString);
  }
  
  @Deprecated
  public RequestBuilder<TranscodeType> load(URL paramURL) {
    return loadGeneric(paramURL);
  }
  
  public RequestBuilder<TranscodeType> load(byte[] paramArrayOfbyte) {
    RequestBuilder<TranscodeType> requestBuilder2 = loadGeneric(paramArrayOfbyte);
    RequestBuilder<TranscodeType> requestBuilder1 = requestBuilder2;
    if (!requestBuilder2.isDiskCacheStrategySet())
      requestBuilder1 = requestBuilder2.apply((BaseRequestOptions<?>)RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)); 
    requestBuilder2 = requestBuilder1;
    if (!requestBuilder1.isSkipMemoryCacheSet())
      requestBuilder2 = requestBuilder1.apply((BaseRequestOptions<?>)RequestOptions.skipMemoryCacheOf(true)); 
    return requestBuilder2;
  }
  
  public Target<TranscodeType> preload() {
    return preload(-2147483648, -2147483648);
  }
  
  public Target<TranscodeType> preload(int paramInt1, int paramInt2) {
    return (Target<TranscodeType>)into(PreloadTarget.obtain(this.requestManager, paramInt1, paramInt2));
  }
  
  public FutureTarget<TranscodeType> submit() {
    return submit(-2147483648, -2147483648);
  }
  
  public FutureTarget<TranscodeType> submit(int paramInt1, int paramInt2) {
    RequestFutureTarget requestFutureTarget = new RequestFutureTarget(paramInt1, paramInt2);
    return (FutureTarget<TranscodeType>)into(requestFutureTarget, (RequestListener<?>)requestFutureTarget, Executors.directExecutor());
  }
  
  public RequestBuilder<TranscodeType> thumbnail(float paramFloat) {
    if (paramFloat >= 0.0F && paramFloat <= 1.0F) {
      this.thumbSizeMultiplier = Float.valueOf(paramFloat);
      return this;
    } 
    throw new IllegalArgumentException("sizeMultiplier must be between 0 and 1");
  }
  
  public RequestBuilder<TranscodeType> thumbnail(RequestBuilder<TranscodeType> paramRequestBuilder) {
    this.thumbnailBuilder = paramRequestBuilder;
    return this;
  }
  
  public RequestBuilder<TranscodeType> thumbnail(RequestBuilder<TranscodeType>... paramVarArgs) {
    RequestBuilder<TranscodeType> requestBuilder = null;
    if (paramVarArgs == null || paramVarArgs.length == 0)
      return thumbnail((RequestBuilder<TranscodeType>)null); 
    for (int i = paramVarArgs.length - 1; i >= 0; i--) {
      RequestBuilder<TranscodeType> requestBuilder1 = paramVarArgs[i];
      if (requestBuilder1 != null)
        if (requestBuilder == null) {
          requestBuilder = requestBuilder1;
        } else {
          requestBuilder = requestBuilder1.thumbnail(requestBuilder);
        }  
    } 
    return thumbnail(requestBuilder);
  }
  
  public RequestBuilder<TranscodeType> transition(TransitionOptions<?, ? super TranscodeType> paramTransitionOptions) {
    this.transitionOptions = (TransitionOptions<?, ? super TranscodeType>)Preconditions.checkNotNull(paramTransitionOptions);
    this.isDefaultTransitionOptionsSet = false;
    return this;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/RequestBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
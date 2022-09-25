package com.bumptech.glide;

import android.content.Context;
import android.content.ContextWrapper;
import android.widget.ImageView;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.bumptech.glide.request.target.ViewTarget;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GlideContext extends ContextWrapper {
  static final TransitionOptions<?, ?> DEFAULT_TRANSITION_OPTIONS = new GenericTransitionOptions();
  
  private final ArrayPool arrayPool;
  
  private final List<RequestListener<Object>> defaultRequestListeners;
  
  private RequestOptions defaultRequestOptions;
  
  private final Glide.RequestOptionsFactory defaultRequestOptionsFactory;
  
  private final Map<Class<?>, TransitionOptions<?, ?>> defaultTransitionOptions;
  
  private final Engine engine;
  
  private final ImageViewTargetFactory imageViewTargetFactory;
  
  private final boolean isLoggingRequestOriginsEnabled;
  
  private final int logLevel;
  
  private final Registry registry;
  
  public GlideContext(Context paramContext, ArrayPool paramArrayPool, Registry paramRegistry, ImageViewTargetFactory paramImageViewTargetFactory, Glide.RequestOptionsFactory paramRequestOptionsFactory, Map<Class<?>, TransitionOptions<?, ?>> paramMap, List<RequestListener<Object>> paramList, Engine paramEngine, boolean paramBoolean, int paramInt) {
    super(paramContext.getApplicationContext());
    this.arrayPool = paramArrayPool;
    this.registry = paramRegistry;
    this.imageViewTargetFactory = paramImageViewTargetFactory;
    this.defaultRequestOptionsFactory = paramRequestOptionsFactory;
    this.defaultRequestListeners = paramList;
    this.defaultTransitionOptions = paramMap;
    this.engine = paramEngine;
    this.isLoggingRequestOriginsEnabled = paramBoolean;
    this.logLevel = paramInt;
  }
  
  public <X> ViewTarget<ImageView, X> buildImageViewTarget(ImageView paramImageView, Class<X> paramClass) {
    return this.imageViewTargetFactory.buildTarget(paramImageView, paramClass);
  }
  
  public ArrayPool getArrayPool() {
    return this.arrayPool;
  }
  
  public List<RequestListener<Object>> getDefaultRequestListeners() {
    return this.defaultRequestListeners;
  }
  
  public RequestOptions getDefaultRequestOptions() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield defaultRequestOptions : Lcom/bumptech/glide/request/RequestOptions;
    //   6: ifnonnull -> 28
    //   9: aload_0
    //   10: aload_0
    //   11: getfield defaultRequestOptionsFactory : Lcom/bumptech/glide/Glide$RequestOptionsFactory;
    //   14: invokeinterface build : ()Lcom/bumptech/glide/request/RequestOptions;
    //   19: invokevirtual lock : ()Lcom/bumptech/glide/request/BaseRequestOptions;
    //   22: checkcast com/bumptech/glide/request/RequestOptions
    //   25: putfield defaultRequestOptions : Lcom/bumptech/glide/request/RequestOptions;
    //   28: aload_0
    //   29: getfield defaultRequestOptions : Lcom/bumptech/glide/request/RequestOptions;
    //   32: astore_1
    //   33: aload_0
    //   34: monitorexit
    //   35: aload_1
    //   36: areturn
    //   37: astore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_1
    //   41: athrow
    // Exception table:
    //   from	to	target	type
    //   2	28	37	finally
    //   28	33	37	finally
  }
  
  public <T> TransitionOptions<?, T> getDefaultTransitionOptions(Class<T> paramClass) {
    TransitionOptions<?, ?> transitionOptions;
    Map.Entry entry2;
    TransitionOptions transitionOptions1 = this.defaultTransitionOptions.get(paramClass);
    TransitionOptions transitionOptions2 = transitionOptions1;
    if (transitionOptions1 == null) {
      Iterator<Map.Entry> iterator = this.defaultTransitionOptions.entrySet().iterator();
      while (true) {
        transitionOptions2 = transitionOptions1;
        if (iterator.hasNext()) {
          entry2 = iterator.next();
          if (((Class)entry2.getKey()).isAssignableFrom(paramClass))
            transitionOptions1 = (TransitionOptions)entry2.getValue(); 
          continue;
        } 
        break;
      } 
    } 
    Map.Entry entry1 = entry2;
    if (entry2 == null)
      transitionOptions = DEFAULT_TRANSITION_OPTIONS; 
    return (TransitionOptions)transitionOptions;
  }
  
  public Engine getEngine() {
    return this.engine;
  }
  
  public int getLogLevel() {
    return this.logLevel;
  }
  
  public Registry getRegistry() {
    return this.registry;
  }
  
  public boolean isLoggingRequestOriginsEnabled() {
    return this.isLoggingRequestOriginsEnabled;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/GlideContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
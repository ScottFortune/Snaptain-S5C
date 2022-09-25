package com.bumptech.glide.request;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.bitmap.DrawableTransformation;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.VideoDecoder;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawableTransformation;
import com.bumptech.glide.load.resource.gif.GifOptions;
import com.bumptech.glide.signature.EmptySignature;
import com.bumptech.glide.util.CachedHashCodeArrayMap;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.util.Map;

public abstract class BaseRequestOptions<T extends BaseRequestOptions<T>> implements Cloneable {
  private static final int DISK_CACHE_STRATEGY = 4;
  
  private static final int ERROR_ID = 32;
  
  private static final int ERROR_PLACEHOLDER = 16;
  
  private static final int FALLBACK = 8192;
  
  private static final int FALLBACK_ID = 16384;
  
  private static final int IS_CACHEABLE = 256;
  
  private static final int ONLY_RETRIEVE_FROM_CACHE = 524288;
  
  private static final int OVERRIDE = 512;
  
  private static final int PLACEHOLDER = 64;
  
  private static final int PLACEHOLDER_ID = 128;
  
  private static final int PRIORITY = 8;
  
  private static final int RESOURCE_CLASS = 4096;
  
  private static final int SIGNATURE = 1024;
  
  private static final int SIZE_MULTIPLIER = 2;
  
  private static final int THEME = 32768;
  
  private static final int TRANSFORMATION = 2048;
  
  private static final int TRANSFORMATION_ALLOWED = 65536;
  
  private static final int TRANSFORMATION_REQUIRED = 131072;
  
  private static final int UNSET = -1;
  
  private static final int USE_ANIMATION_POOL = 1048576;
  
  private static final int USE_UNLIMITED_SOURCE_GENERATORS_POOL = 262144;
  
  private DiskCacheStrategy diskCacheStrategy = DiskCacheStrategy.AUTOMATIC;
  
  private int errorId;
  
  private Drawable errorPlaceholder;
  
  private Drawable fallbackDrawable;
  
  private int fallbackId;
  
  private int fields;
  
  private boolean isAutoCloneEnabled;
  
  private boolean isCacheable = true;
  
  private boolean isLocked;
  
  private boolean isScaleOnlyOrNoTransform = true;
  
  private boolean isTransformationAllowed = true;
  
  private boolean isTransformationRequired;
  
  private boolean onlyRetrieveFromCache;
  
  private Options options = new Options();
  
  private int overrideHeight = -1;
  
  private int overrideWidth = -1;
  
  private Drawable placeholderDrawable;
  
  private int placeholderId;
  
  private Priority priority = Priority.NORMAL;
  
  private Class<?> resourceClass = Object.class;
  
  private Key signature = (Key)EmptySignature.obtain();
  
  private float sizeMultiplier = 1.0F;
  
  private Resources.Theme theme;
  
  private Map<Class<?>, Transformation<?>> transformations = (Map<Class<?>, Transformation<?>>)new CachedHashCodeArrayMap();
  
  private boolean useAnimationPool;
  
  private boolean useUnlimitedSourceGeneratorsPool;
  
  private boolean isSet(int paramInt) {
    return isSet(this.fields, paramInt);
  }
  
  private static boolean isSet(int paramInt1, int paramInt2) {
    boolean bool;
    if ((paramInt1 & paramInt2) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private T optionalScaleOnlyTransform(DownsampleStrategy paramDownsampleStrategy, Transformation<Bitmap> paramTransformation) {
    return scaleOnlyTransform(paramDownsampleStrategy, paramTransformation, false);
  }
  
  private T scaleOnlyTransform(DownsampleStrategy paramDownsampleStrategy, Transformation<Bitmap> paramTransformation) {
    return scaleOnlyTransform(paramDownsampleStrategy, paramTransformation, true);
  }
  
  private T scaleOnlyTransform(DownsampleStrategy paramDownsampleStrategy, Transformation<Bitmap> paramTransformation, boolean paramBoolean) {
    if (paramBoolean) {
      paramDownsampleStrategy = (DownsampleStrategy)transform(paramDownsampleStrategy, paramTransformation);
    } else {
      paramDownsampleStrategy = (DownsampleStrategy)optionalTransform(paramDownsampleStrategy, paramTransformation);
    } 
    ((BaseRequestOptions)paramDownsampleStrategy).isScaleOnlyOrNoTransform = true;
    return (T)paramDownsampleStrategy;
  }
  
  private T self() {
    return (T)this;
  }
  
  private T selfOrThrowIfLocked() {
    if (!this.isLocked)
      return self(); 
    throw new IllegalStateException("You cannot modify locked T, consider clone()");
  }
  
  public T apply(BaseRequestOptions<?> paramBaseRequestOptions) {
    if (this.isAutoCloneEnabled)
      return clone().apply(paramBaseRequestOptions); 
    if (isSet(paramBaseRequestOptions.fields, 2))
      this.sizeMultiplier = paramBaseRequestOptions.sizeMultiplier; 
    if (isSet(paramBaseRequestOptions.fields, 262144))
      this.useUnlimitedSourceGeneratorsPool = paramBaseRequestOptions.useUnlimitedSourceGeneratorsPool; 
    if (isSet(paramBaseRequestOptions.fields, 1048576))
      this.useAnimationPool = paramBaseRequestOptions.useAnimationPool; 
    if (isSet(paramBaseRequestOptions.fields, 4))
      this.diskCacheStrategy = paramBaseRequestOptions.diskCacheStrategy; 
    if (isSet(paramBaseRequestOptions.fields, 8))
      this.priority = paramBaseRequestOptions.priority; 
    if (isSet(paramBaseRequestOptions.fields, 16)) {
      this.errorPlaceholder = paramBaseRequestOptions.errorPlaceholder;
      this.errorId = 0;
      this.fields &= 0xFFFFFFDF;
    } 
    if (isSet(paramBaseRequestOptions.fields, 32)) {
      this.errorId = paramBaseRequestOptions.errorId;
      this.errorPlaceholder = null;
      this.fields &= 0xFFFFFFEF;
    } 
    if (isSet(paramBaseRequestOptions.fields, 64)) {
      this.placeholderDrawable = paramBaseRequestOptions.placeholderDrawable;
      this.placeholderId = 0;
      this.fields &= 0xFFFFFF7F;
    } 
    if (isSet(paramBaseRequestOptions.fields, 128)) {
      this.placeholderId = paramBaseRequestOptions.placeholderId;
      this.placeholderDrawable = null;
      this.fields &= 0xFFFFFFBF;
    } 
    if (isSet(paramBaseRequestOptions.fields, 256))
      this.isCacheable = paramBaseRequestOptions.isCacheable; 
    if (isSet(paramBaseRequestOptions.fields, 512)) {
      this.overrideWidth = paramBaseRequestOptions.overrideWidth;
      this.overrideHeight = paramBaseRequestOptions.overrideHeight;
    } 
    if (isSet(paramBaseRequestOptions.fields, 1024))
      this.signature = paramBaseRequestOptions.signature; 
    if (isSet(paramBaseRequestOptions.fields, 4096))
      this.resourceClass = paramBaseRequestOptions.resourceClass; 
    if (isSet(paramBaseRequestOptions.fields, 8192)) {
      this.fallbackDrawable = paramBaseRequestOptions.fallbackDrawable;
      this.fallbackId = 0;
      this.fields &= 0xFFFFBFFF;
    } 
    if (isSet(paramBaseRequestOptions.fields, 16384)) {
      this.fallbackId = paramBaseRequestOptions.fallbackId;
      this.fallbackDrawable = null;
      this.fields &= 0xFFFFDFFF;
    } 
    if (isSet(paramBaseRequestOptions.fields, 32768))
      this.theme = paramBaseRequestOptions.theme; 
    if (isSet(paramBaseRequestOptions.fields, 65536))
      this.isTransformationAllowed = paramBaseRequestOptions.isTransformationAllowed; 
    if (isSet(paramBaseRequestOptions.fields, 131072))
      this.isTransformationRequired = paramBaseRequestOptions.isTransformationRequired; 
    if (isSet(paramBaseRequestOptions.fields, 2048)) {
      this.transformations.putAll(paramBaseRequestOptions.transformations);
      this.isScaleOnlyOrNoTransform = paramBaseRequestOptions.isScaleOnlyOrNoTransform;
    } 
    if (isSet(paramBaseRequestOptions.fields, 524288))
      this.onlyRetrieveFromCache = paramBaseRequestOptions.onlyRetrieveFromCache; 
    if (!this.isTransformationAllowed) {
      this.transformations.clear();
      this.fields &= 0xFFFFF7FF;
      this.isTransformationRequired = false;
      this.fields &= 0xFFFDFFFF;
      this.isScaleOnlyOrNoTransform = true;
    } 
    this.fields |= paramBaseRequestOptions.fields;
    this.options.putAll(paramBaseRequestOptions.options);
    return selfOrThrowIfLocked();
  }
  
  public T autoClone() {
    if (!this.isLocked || this.isAutoCloneEnabled) {
      this.isAutoCloneEnabled = true;
      return lock();
    } 
    throw new IllegalStateException("You cannot auto lock an already locked options object, try clone() first");
  }
  
  public T centerCrop() {
    return transform(DownsampleStrategy.CENTER_OUTSIDE, (Transformation<Bitmap>)new CenterCrop());
  }
  
  public T centerInside() {
    return scaleOnlyTransform(DownsampleStrategy.CENTER_INSIDE, (Transformation<Bitmap>)new CenterInside());
  }
  
  public T circleCrop() {
    return transform(DownsampleStrategy.CENTER_INSIDE, (Transformation<Bitmap>)new CircleCrop());
  }
  
  public T clone() {
    try {
      BaseRequestOptions baseRequestOptions = (BaseRequestOptions)super.clone();
      Options options = new Options();
      this();
      baseRequestOptions.options = options;
      baseRequestOptions.options.putAll(this.options);
      CachedHashCodeArrayMap cachedHashCodeArrayMap = new CachedHashCodeArrayMap();
      this();
      baseRequestOptions.transformations = (Map<Class<?>, Transformation<?>>)cachedHashCodeArrayMap;
      baseRequestOptions.transformations.putAll(this.transformations);
      baseRequestOptions.isLocked = false;
      baseRequestOptions.isAutoCloneEnabled = false;
      return (T)baseRequestOptions;
    } catch (CloneNotSupportedException cloneNotSupportedException) {
      throw new RuntimeException(cloneNotSupportedException);
    } 
  }
  
  public T decode(Class<?> paramClass) {
    if (this.isAutoCloneEnabled)
      return clone().decode(paramClass); 
    this.resourceClass = (Class)Preconditions.checkNotNull(paramClass);
    this.fields |= 0x1000;
    return selfOrThrowIfLocked();
  }
  
  public T disallowHardwareConfig() {
    return set(Downsampler.ALLOW_HARDWARE_CONFIG, Boolean.valueOf(false));
  }
  
  public T diskCacheStrategy(DiskCacheStrategy paramDiskCacheStrategy) {
    if (this.isAutoCloneEnabled)
      return clone().diskCacheStrategy(paramDiskCacheStrategy); 
    this.diskCacheStrategy = (DiskCacheStrategy)Preconditions.checkNotNull(paramDiskCacheStrategy);
    this.fields |= 0x4;
    return selfOrThrowIfLocked();
  }
  
  public T dontAnimate() {
    return set(GifOptions.DISABLE_ANIMATION, Boolean.valueOf(true));
  }
  
  public T dontTransform() {
    if (this.isAutoCloneEnabled)
      return clone().dontTransform(); 
    this.transformations.clear();
    this.fields &= 0xFFFFF7FF;
    this.isTransformationRequired = false;
    this.fields &= 0xFFFDFFFF;
    this.isTransformationAllowed = false;
    this.fields |= 0x10000;
    this.isScaleOnlyOrNoTransform = true;
    return selfOrThrowIfLocked();
  }
  
  public T downsample(DownsampleStrategy paramDownsampleStrategy) {
    return set(DownsampleStrategy.OPTION, Preconditions.checkNotNull(paramDownsampleStrategy));
  }
  
  public T encodeFormat(Bitmap.CompressFormat paramCompressFormat) {
    return set(BitmapEncoder.COMPRESSION_FORMAT, Preconditions.checkNotNull(paramCompressFormat));
  }
  
  public T encodeQuality(int paramInt) {
    return set(BitmapEncoder.COMPRESSION_QUALITY, Integer.valueOf(paramInt));
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof BaseRequestOptions;
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (bool) {
      paramObject = paramObject;
      bool2 = bool1;
      if (Float.compare(((BaseRequestOptions)paramObject).sizeMultiplier, this.sizeMultiplier) == 0) {
        bool2 = bool1;
        if (this.errorId == ((BaseRequestOptions)paramObject).errorId) {
          bool2 = bool1;
          if (Util.bothNullOrEqual(this.errorPlaceholder, ((BaseRequestOptions)paramObject).errorPlaceholder)) {
            bool2 = bool1;
            if (this.placeholderId == ((BaseRequestOptions)paramObject).placeholderId) {
              bool2 = bool1;
              if (Util.bothNullOrEqual(this.placeholderDrawable, ((BaseRequestOptions)paramObject).placeholderDrawable)) {
                bool2 = bool1;
                if (this.fallbackId == ((BaseRequestOptions)paramObject).fallbackId) {
                  bool2 = bool1;
                  if (Util.bothNullOrEqual(this.fallbackDrawable, ((BaseRequestOptions)paramObject).fallbackDrawable)) {
                    bool2 = bool1;
                    if (this.isCacheable == ((BaseRequestOptions)paramObject).isCacheable) {
                      bool2 = bool1;
                      if (this.overrideHeight == ((BaseRequestOptions)paramObject).overrideHeight) {
                        bool2 = bool1;
                        if (this.overrideWidth == ((BaseRequestOptions)paramObject).overrideWidth) {
                          bool2 = bool1;
                          if (this.isTransformationRequired == ((BaseRequestOptions)paramObject).isTransformationRequired) {
                            bool2 = bool1;
                            if (this.isTransformationAllowed == ((BaseRequestOptions)paramObject).isTransformationAllowed) {
                              bool2 = bool1;
                              if (this.useUnlimitedSourceGeneratorsPool == ((BaseRequestOptions)paramObject).useUnlimitedSourceGeneratorsPool) {
                                bool2 = bool1;
                                if (this.onlyRetrieveFromCache == ((BaseRequestOptions)paramObject).onlyRetrieveFromCache) {
                                  bool2 = bool1;
                                  if (this.diskCacheStrategy.equals(((BaseRequestOptions)paramObject).diskCacheStrategy)) {
                                    bool2 = bool1;
                                    if (this.priority == ((BaseRequestOptions)paramObject).priority) {
                                      bool2 = bool1;
                                      if (this.options.equals(((BaseRequestOptions)paramObject).options)) {
                                        bool2 = bool1;
                                        if (this.transformations.equals(((BaseRequestOptions)paramObject).transformations)) {
                                          bool2 = bool1;
                                          if (this.resourceClass.equals(((BaseRequestOptions)paramObject).resourceClass)) {
                                            bool2 = bool1;
                                            if (Util.bothNullOrEqual(this.signature, ((BaseRequestOptions)paramObject).signature)) {
                                              bool2 = bool1;
                                              if (Util.bothNullOrEqual(this.theme, ((BaseRequestOptions)paramObject).theme))
                                                bool2 = true; 
                                            } 
                                          } 
                                        } 
                                      } 
                                    } 
                                  } 
                                } 
                              } 
                            } 
                          } 
                        } 
                      } 
                    } 
                  } 
                } 
              } 
            } 
          } 
        } 
      } 
    } 
    return bool2;
  }
  
  public T error(int paramInt) {
    if (this.isAutoCloneEnabled)
      return clone().error(paramInt); 
    this.errorId = paramInt;
    this.fields |= 0x20;
    this.errorPlaceholder = null;
    this.fields &= 0xFFFFFFEF;
    return selfOrThrowIfLocked();
  }
  
  public T error(Drawable paramDrawable) {
    if (this.isAutoCloneEnabled)
      return clone().error(paramDrawable); 
    this.errorPlaceholder = paramDrawable;
    this.fields |= 0x10;
    this.errorId = 0;
    this.fields &= 0xFFFFFFDF;
    return selfOrThrowIfLocked();
  }
  
  public T fallback(int paramInt) {
    if (this.isAutoCloneEnabled)
      return clone().fallback(paramInt); 
    this.fallbackId = paramInt;
    this.fields |= 0x4000;
    this.fallbackDrawable = null;
    this.fields &= 0xFFFFDFFF;
    return selfOrThrowIfLocked();
  }
  
  public T fallback(Drawable paramDrawable) {
    if (this.isAutoCloneEnabled)
      return clone().fallback(paramDrawable); 
    this.fallbackDrawable = paramDrawable;
    this.fields |= 0x2000;
    this.fallbackId = 0;
    this.fields &= 0xFFFFBFFF;
    return selfOrThrowIfLocked();
  }
  
  public T fitCenter() {
    return scaleOnlyTransform(DownsampleStrategy.FIT_CENTER, (Transformation<Bitmap>)new FitCenter());
  }
  
  public T format(DecodeFormat paramDecodeFormat) {
    Preconditions.checkNotNull(paramDecodeFormat);
    return set(Downsampler.DECODE_FORMAT, paramDecodeFormat).set(GifOptions.DECODE_FORMAT, paramDecodeFormat);
  }
  
  public T frame(long paramLong) {
    return set(VideoDecoder.TARGET_FRAME, Long.valueOf(paramLong));
  }
  
  public final DiskCacheStrategy getDiskCacheStrategy() {
    return this.diskCacheStrategy;
  }
  
  public final int getErrorId() {
    return this.errorId;
  }
  
  public final Drawable getErrorPlaceholder() {
    return this.errorPlaceholder;
  }
  
  public final Drawable getFallbackDrawable() {
    return this.fallbackDrawable;
  }
  
  public final int getFallbackId() {
    return this.fallbackId;
  }
  
  public final boolean getOnlyRetrieveFromCache() {
    return this.onlyRetrieveFromCache;
  }
  
  public final Options getOptions() {
    return this.options;
  }
  
  public final int getOverrideHeight() {
    return this.overrideHeight;
  }
  
  public final int getOverrideWidth() {
    return this.overrideWidth;
  }
  
  public final Drawable getPlaceholderDrawable() {
    return this.placeholderDrawable;
  }
  
  public final int getPlaceholderId() {
    return this.placeholderId;
  }
  
  public final Priority getPriority() {
    return this.priority;
  }
  
  public final Class<?> getResourceClass() {
    return this.resourceClass;
  }
  
  public final Key getSignature() {
    return this.signature;
  }
  
  public final float getSizeMultiplier() {
    return this.sizeMultiplier;
  }
  
  public final Resources.Theme getTheme() {
    return this.theme;
  }
  
  public final Map<Class<?>, Transformation<?>> getTransformations() {
    return this.transformations;
  }
  
  public final boolean getUseAnimationPool() {
    return this.useAnimationPool;
  }
  
  public final boolean getUseUnlimitedSourceGeneratorsPool() {
    return this.useUnlimitedSourceGeneratorsPool;
  }
  
  public int hashCode() {
    int i = Util.hashCode(this.sizeMultiplier);
    i = Util.hashCode(this.errorId, i);
    i = Util.hashCode(this.errorPlaceholder, i);
    i = Util.hashCode(this.placeholderId, i);
    i = Util.hashCode(this.placeholderDrawable, i);
    i = Util.hashCode(this.fallbackId, i);
    i = Util.hashCode(this.fallbackDrawable, i);
    i = Util.hashCode(this.isCacheable, i);
    i = Util.hashCode(this.overrideHeight, i);
    i = Util.hashCode(this.overrideWidth, i);
    i = Util.hashCode(this.isTransformationRequired, i);
    i = Util.hashCode(this.isTransformationAllowed, i);
    i = Util.hashCode(this.useUnlimitedSourceGeneratorsPool, i);
    i = Util.hashCode(this.onlyRetrieveFromCache, i);
    i = Util.hashCode(this.diskCacheStrategy, i);
    i = Util.hashCode(this.priority, i);
    i = Util.hashCode(this.options, i);
    i = Util.hashCode(this.transformations, i);
    i = Util.hashCode(this.resourceClass, i);
    i = Util.hashCode(this.signature, i);
    return Util.hashCode(this.theme, i);
  }
  
  protected boolean isAutoCloneEnabled() {
    return this.isAutoCloneEnabled;
  }
  
  public final boolean isDiskCacheStrategySet() {
    return isSet(4);
  }
  
  public final boolean isLocked() {
    return this.isLocked;
  }
  
  public final boolean isMemoryCacheable() {
    return this.isCacheable;
  }
  
  public final boolean isPrioritySet() {
    return isSet(8);
  }
  
  boolean isScaleOnlyOrNoTransform() {
    return this.isScaleOnlyOrNoTransform;
  }
  
  public final boolean isSkipMemoryCacheSet() {
    return isSet(256);
  }
  
  public final boolean isTransformationAllowed() {
    return this.isTransformationAllowed;
  }
  
  public final boolean isTransformationRequired() {
    return this.isTransformationRequired;
  }
  
  public final boolean isTransformationSet() {
    return isSet(2048);
  }
  
  public final boolean isValidOverride() {
    return Util.isValidDimensions(this.overrideWidth, this.overrideHeight);
  }
  
  public T lock() {
    this.isLocked = true;
    return self();
  }
  
  public T onlyRetrieveFromCache(boolean paramBoolean) {
    if (this.isAutoCloneEnabled)
      return clone().onlyRetrieveFromCache(paramBoolean); 
    this.onlyRetrieveFromCache = paramBoolean;
    this.fields |= 0x80000;
    return selfOrThrowIfLocked();
  }
  
  public T optionalCenterCrop() {
    return optionalTransform(DownsampleStrategy.CENTER_OUTSIDE, (Transformation<Bitmap>)new CenterCrop());
  }
  
  public T optionalCenterInside() {
    return optionalScaleOnlyTransform(DownsampleStrategy.CENTER_INSIDE, (Transformation<Bitmap>)new CenterInside());
  }
  
  public T optionalCircleCrop() {
    return optionalTransform(DownsampleStrategy.CENTER_OUTSIDE, (Transformation<Bitmap>)new CircleCrop());
  }
  
  public T optionalFitCenter() {
    return optionalScaleOnlyTransform(DownsampleStrategy.FIT_CENTER, (Transformation<Bitmap>)new FitCenter());
  }
  
  public T optionalTransform(Transformation<Bitmap> paramTransformation) {
    return transform(paramTransformation, false);
  }
  
  final T optionalTransform(DownsampleStrategy paramDownsampleStrategy, Transformation<Bitmap> paramTransformation) {
    if (this.isAutoCloneEnabled)
      return clone().optionalTransform(paramDownsampleStrategy, paramTransformation); 
    downsample(paramDownsampleStrategy);
    return transform(paramTransformation, false);
  }
  
  public <Y> T optionalTransform(Class<Y> paramClass, Transformation<Y> paramTransformation) {
    return transform(paramClass, paramTransformation, false);
  }
  
  public T override(int paramInt) {
    return override(paramInt, paramInt);
  }
  
  public T override(int paramInt1, int paramInt2) {
    if (this.isAutoCloneEnabled)
      return clone().override(paramInt1, paramInt2); 
    this.overrideWidth = paramInt1;
    this.overrideHeight = paramInt2;
    this.fields |= 0x200;
    return selfOrThrowIfLocked();
  }
  
  public T placeholder(int paramInt) {
    if (this.isAutoCloneEnabled)
      return clone().placeholder(paramInt); 
    this.placeholderId = paramInt;
    this.fields |= 0x80;
    this.placeholderDrawable = null;
    this.fields &= 0xFFFFFFBF;
    return selfOrThrowIfLocked();
  }
  
  public T placeholder(Drawable paramDrawable) {
    if (this.isAutoCloneEnabled)
      return clone().placeholder(paramDrawable); 
    this.placeholderDrawable = paramDrawable;
    this.fields |= 0x40;
    this.placeholderId = 0;
    this.fields &= 0xFFFFFF7F;
    return selfOrThrowIfLocked();
  }
  
  public T priority(Priority paramPriority) {
    if (this.isAutoCloneEnabled)
      return clone().priority(paramPriority); 
    this.priority = (Priority)Preconditions.checkNotNull(paramPriority);
    this.fields |= 0x8;
    return selfOrThrowIfLocked();
  }
  
  public <Y> T set(Option<Y> paramOption, Y paramY) {
    if (this.isAutoCloneEnabled)
      return clone().set(paramOption, paramY); 
    Preconditions.checkNotNull(paramOption);
    Preconditions.checkNotNull(paramY);
    this.options.set(paramOption, paramY);
    return selfOrThrowIfLocked();
  }
  
  public T signature(Key paramKey) {
    if (this.isAutoCloneEnabled)
      return clone().signature(paramKey); 
    this.signature = (Key)Preconditions.checkNotNull(paramKey);
    this.fields |= 0x400;
    return selfOrThrowIfLocked();
  }
  
  public T sizeMultiplier(float paramFloat) {
    if (this.isAutoCloneEnabled)
      return clone().sizeMultiplier(paramFloat); 
    if (paramFloat >= 0.0F && paramFloat <= 1.0F) {
      this.sizeMultiplier = paramFloat;
      this.fields |= 0x2;
      return selfOrThrowIfLocked();
    } 
    throw new IllegalArgumentException("sizeMultiplier must be between 0 and 1");
  }
  
  public T skipMemoryCache(boolean paramBoolean) {
    if (this.isAutoCloneEnabled)
      return clone().skipMemoryCache(true); 
    this.isCacheable = paramBoolean ^ true;
    this.fields |= 0x100;
    return selfOrThrowIfLocked();
  }
  
  public T theme(Resources.Theme paramTheme) {
    if (this.isAutoCloneEnabled)
      return clone().theme(paramTheme); 
    this.theme = paramTheme;
    this.fields |= 0x8000;
    return selfOrThrowIfLocked();
  }
  
  public T timeout(int paramInt) {
    return set(HttpGlideUrlLoader.TIMEOUT, Integer.valueOf(paramInt));
  }
  
  public T transform(Transformation<Bitmap> paramTransformation) {
    return transform(paramTransformation, true);
  }
  
  T transform(Transformation<Bitmap> paramTransformation, boolean paramBoolean) {
    if (this.isAutoCloneEnabled)
      return clone().transform(paramTransformation, paramBoolean); 
    DrawableTransformation drawableTransformation = new DrawableTransformation(paramTransformation, paramBoolean);
    transform(Bitmap.class, paramTransformation, paramBoolean);
    transform(Drawable.class, (Transformation<Drawable>)drawableTransformation, paramBoolean);
    transform(BitmapDrawable.class, drawableTransformation.asBitmapDrawable(), paramBoolean);
    transform(GifDrawable.class, (Transformation<GifDrawable>)new GifDrawableTransformation(paramTransformation), paramBoolean);
    return selfOrThrowIfLocked();
  }
  
  final T transform(DownsampleStrategy paramDownsampleStrategy, Transformation<Bitmap> paramTransformation) {
    if (this.isAutoCloneEnabled)
      return clone().transform(paramDownsampleStrategy, paramTransformation); 
    downsample(paramDownsampleStrategy);
    return transform(paramTransformation);
  }
  
  public <Y> T transform(Class<Y> paramClass, Transformation<Y> paramTransformation) {
    return transform(paramClass, paramTransformation, true);
  }
  
  <Y> T transform(Class<Y> paramClass, Transformation<Y> paramTransformation, boolean paramBoolean) {
    if (this.isAutoCloneEnabled)
      return clone().transform(paramClass, paramTransformation, paramBoolean); 
    Preconditions.checkNotNull(paramClass);
    Preconditions.checkNotNull(paramTransformation);
    this.transformations.put(paramClass, paramTransformation);
    this.fields |= 0x800;
    this.isTransformationAllowed = true;
    this.fields |= 0x10000;
    this.isScaleOnlyOrNoTransform = false;
    if (paramBoolean) {
      this.fields |= 0x20000;
      this.isTransformationRequired = true;
    } 
    return selfOrThrowIfLocked();
  }
  
  public T transform(Transformation<Bitmap>... paramVarArgs) {
    return (paramVarArgs.length > 1) ? transform((Transformation<Bitmap>)new MultiTransformation((Transformation[])paramVarArgs), true) : ((paramVarArgs.length == 1) ? transform(paramVarArgs[0]) : selfOrThrowIfLocked());
  }
  
  @Deprecated
  public T transforms(Transformation<Bitmap>... paramVarArgs) {
    return transform((Transformation<Bitmap>)new MultiTransformation((Transformation[])paramVarArgs), true);
  }
  
  public T useAnimationPool(boolean paramBoolean) {
    if (this.isAutoCloneEnabled)
      return clone().useAnimationPool(paramBoolean); 
    this.useAnimationPool = paramBoolean;
    this.fields |= 0x100000;
    return selfOrThrowIfLocked();
  }
  
  public T useUnlimitedSourceGeneratorsPool(boolean paramBoolean) {
    if (this.isAutoCloneEnabled)
      return clone().useUnlimitedSourceGeneratorsPool(paramBoolean); 
    this.useUnlimitedSourceGeneratorsPool = paramBoolean;
    this.fields |= 0x40000;
    return selfOrThrowIfLocked();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/BaseRequestOptions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
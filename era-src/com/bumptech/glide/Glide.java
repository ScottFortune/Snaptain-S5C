package com.bumptech.glide;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentCallbacks;
import android.content.ComponentCallbacks2;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.data.InputStreamRewinder;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.prefill.BitmapPreFiller;
import com.bumptech.glide.load.engine.prefill.PreFillType;
import com.bumptech.glide.load.model.AssetUriLoader;
import com.bumptech.glide.load.model.ByteArrayLoader;
import com.bumptech.glide.load.model.ByteBufferEncoder;
import com.bumptech.glide.load.model.ByteBufferFileLoader;
import com.bumptech.glide.load.model.DataUrlLoader;
import com.bumptech.glide.load.model.FileLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.MediaStoreFileLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.ResourceLoader;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.model.StringLoader;
import com.bumptech.glide.load.model.UnitModelLoader;
import com.bumptech.glide.load.model.UriLoader;
import com.bumptech.glide.load.model.UrlUriLoader;
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader;
import com.bumptech.glide.load.model.stream.HttpUriLoader;
import com.bumptech.glide.load.model.stream.MediaStoreImageThumbLoader;
import com.bumptech.glide.load.model.stream.MediaStoreVideoThumbLoader;
import com.bumptech.glide.load.model.stream.UrlLoader;
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableDecoder;
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableEncoder;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.bumptech.glide.load.resource.bitmap.ByteBufferBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.ByteBufferBitmapImageDecoderResourceDecoder;
import com.bumptech.glide.load.resource.bitmap.DefaultImageHeaderParser;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.bitmap.ExifInterfaceImageHeaderParser;
import com.bumptech.glide.load.resource.bitmap.InputStreamBitmapImageDecoderResourceDecoder;
import com.bumptech.glide.load.resource.bitmap.ResourceBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.UnitBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.VideoDecoder;
import com.bumptech.glide.load.resource.bytes.ByteBufferRewinder;
import com.bumptech.glide.load.resource.drawable.ResourceDrawableDecoder;
import com.bumptech.glide.load.resource.drawable.UnitDrawableDecoder;
import com.bumptech.glide.load.resource.file.FileDecoder;
import com.bumptech.glide.load.resource.gif.ByteBufferGifDecoder;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawableEncoder;
import com.bumptech.glide.load.resource.gif.GifFrameResourceDecoder;
import com.bumptech.glide.load.resource.gif.StreamGifDecoder;
import com.bumptech.glide.load.resource.transcode.BitmapBytesTranscoder;
import com.bumptech.glide.load.resource.transcode.BitmapDrawableTranscoder;
import com.bumptech.glide.load.resource.transcode.DrawableBytesTranscoder;
import com.bumptech.glide.load.resource.transcode.GifDrawableBytesTranscoder;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.bumptech.glide.manager.RequestManagerRetriever;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.module.ManifestParser;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Glide implements ComponentCallbacks2 {
  private static final String DEFAULT_DISK_CACHE_DIR = "image_manager_disk_cache";
  
  private static final String TAG = "Glide";
  
  private static volatile Glide glide;
  
  private static volatile boolean isInitializing;
  
  private final ArrayPool arrayPool;
  
  private final BitmapPool bitmapPool;
  
  private BitmapPreFiller bitmapPreFiller;
  
  private final ConnectivityMonitorFactory connectivityMonitorFactory;
  
  private final RequestOptionsFactory defaultRequestOptionsFactory;
  
  private final Engine engine;
  
  private final GlideContext glideContext;
  
  private final List<RequestManager> managers;
  
  private final MemoryCache memoryCache;
  
  private MemoryCategory memoryCategory;
  
  private final Registry registry;
  
  private final RequestManagerRetriever requestManagerRetriever;
  
  Glide(Context paramContext, Engine paramEngine, MemoryCache paramMemoryCache, BitmapPool paramBitmapPool, ArrayPool paramArrayPool, RequestManagerRetriever paramRequestManagerRetriever, ConnectivityMonitorFactory paramConnectivityMonitorFactory, int paramInt1, RequestOptionsFactory paramRequestOptionsFactory, Map<Class<?>, TransitionOptions<?, ?>> paramMap, List<RequestListener<Object>> paramList, boolean paramBoolean1, boolean paramBoolean2, int paramInt2, int paramInt3) {
    ByteBufferBitmapDecoder byteBufferBitmapDecoder;
    StreamBitmapDecoder streamBitmapDecoder;
    this.managers = new ArrayList<RequestManager>();
    this.memoryCategory = MemoryCategory.NORMAL;
    this.engine = paramEngine;
    this.bitmapPool = paramBitmapPool;
    this.arrayPool = paramArrayPool;
    this.memoryCache = paramMemoryCache;
    this.requestManagerRetriever = paramRequestManagerRetriever;
    this.connectivityMonitorFactory = paramConnectivityMonitorFactory;
    this.defaultRequestOptionsFactory = paramRequestOptionsFactory;
    Resources resources = paramContext.getResources();
    this.registry = new Registry();
    this.registry.register((ImageHeaderParser)new DefaultImageHeaderParser());
    if (Build.VERSION.SDK_INT >= 27)
      this.registry.register((ImageHeaderParser)new ExifInterfaceImageHeaderParser()); 
    List<ImageHeaderParser> list = this.registry.getImageHeaderParsers();
    ByteBufferGifDecoder byteBufferGifDecoder = new ByteBufferGifDecoder(paramContext, list, paramBitmapPool, paramArrayPool);
    ResourceDecoder<ParcelFileDescriptor, Bitmap> resourceDecoder = VideoDecoder.parcel(paramBitmapPool);
    if (paramBoolean2 && Build.VERSION.SDK_INT >= 28) {
      InputStreamBitmapImageDecoderResourceDecoder inputStreamBitmapImageDecoderResourceDecoder = new InputStreamBitmapImageDecoderResourceDecoder();
      ByteBufferBitmapImageDecoderResourceDecoder byteBufferBitmapImageDecoderResourceDecoder = new ByteBufferBitmapImageDecoderResourceDecoder();
    } else {
      Downsampler downsampler = new Downsampler(this.registry.getImageHeaderParsers(), resources.getDisplayMetrics(), paramBitmapPool, paramArrayPool);
      byteBufferBitmapDecoder = new ByteBufferBitmapDecoder(downsampler);
      streamBitmapDecoder = new StreamBitmapDecoder(downsampler, paramArrayPool);
    } 
    ResourceDrawableDecoder resourceDrawableDecoder = new ResourceDrawableDecoder(paramContext);
    ResourceLoader.StreamFactory streamFactory = new ResourceLoader.StreamFactory(resources);
    ResourceLoader.UriFactory uriFactory = new ResourceLoader.UriFactory(resources);
    ResourceLoader.FileDescriptorFactory fileDescriptorFactory = new ResourceLoader.FileDescriptorFactory(resources);
    ResourceLoader.AssetFileDescriptorFactory assetFileDescriptorFactory = new ResourceLoader.AssetFileDescriptorFactory(resources);
    BitmapEncoder bitmapEncoder = new BitmapEncoder(paramArrayPool);
    BitmapBytesTranscoder bitmapBytesTranscoder = new BitmapBytesTranscoder();
    GifDrawableBytesTranscoder gifDrawableBytesTranscoder = new GifDrawableBytesTranscoder();
    ContentResolver contentResolver = paramContext.getContentResolver();
    this.registry.<ByteBuffer>append(ByteBuffer.class, (Encoder<ByteBuffer>)new ByteBufferEncoder()).<InputStream>append(InputStream.class, (Encoder<InputStream>)new StreamEncoder(paramArrayPool)).<ByteBuffer, Bitmap>append("Bitmap", ByteBuffer.class, Bitmap.class, (ResourceDecoder<ByteBuffer, Bitmap>)byteBufferBitmapDecoder).<InputStream, Bitmap>append("Bitmap", InputStream.class, Bitmap.class, (ResourceDecoder<InputStream, Bitmap>)streamBitmapDecoder).<ParcelFileDescriptor, Bitmap>append("Bitmap", ParcelFileDescriptor.class, Bitmap.class, resourceDecoder).<AssetFileDescriptor, Bitmap>append("Bitmap", AssetFileDescriptor.class, Bitmap.class, VideoDecoder.asset(paramBitmapPool)).<Bitmap, Bitmap>append(Bitmap.class, Bitmap.class, (ModelLoaderFactory<Bitmap, Bitmap>)UnitModelLoader.Factory.getInstance()).<Bitmap, Bitmap>append("Bitmap", Bitmap.class, Bitmap.class, (ResourceDecoder<Bitmap, Bitmap>)new UnitBitmapDecoder()).<Bitmap>append(Bitmap.class, (ResourceEncoder<Bitmap>)bitmapEncoder).<ByteBuffer, BitmapDrawable>append("BitmapDrawable", ByteBuffer.class, BitmapDrawable.class, (ResourceDecoder<ByteBuffer, BitmapDrawable>)new BitmapDrawableDecoder(resources, (ResourceDecoder)byteBufferBitmapDecoder)).<InputStream, BitmapDrawable>append("BitmapDrawable", InputStream.class, BitmapDrawable.class, (ResourceDecoder<InputStream, BitmapDrawable>)new BitmapDrawableDecoder(resources, (ResourceDecoder)streamBitmapDecoder)).<ParcelFileDescriptor, BitmapDrawable>append("BitmapDrawable", ParcelFileDescriptor.class, BitmapDrawable.class, (ResourceDecoder<ParcelFileDescriptor, BitmapDrawable>)new BitmapDrawableDecoder(resources, resourceDecoder)).<BitmapDrawable>append(BitmapDrawable.class, (ResourceEncoder<BitmapDrawable>)new BitmapDrawableEncoder(paramBitmapPool, (ResourceEncoder)bitmapEncoder)).<InputStream, GifDrawable>append("Gif", InputStream.class, GifDrawable.class, (ResourceDecoder<InputStream, GifDrawable>)new StreamGifDecoder(list, (ResourceDecoder)byteBufferGifDecoder, paramArrayPool)).<ByteBuffer, GifDrawable>append("Gif", ByteBuffer.class, GifDrawable.class, (ResourceDecoder<ByteBuffer, GifDrawable>)byteBufferGifDecoder).<GifDrawable>append(GifDrawable.class, (ResourceEncoder<GifDrawable>)new GifDrawableEncoder()).<GifDecoder, GifDecoder>append(GifDecoder.class, GifDecoder.class, (ModelLoaderFactory<GifDecoder, GifDecoder>)UnitModelLoader.Factory.getInstance()).<GifDecoder, Bitmap>append("Bitmap", GifDecoder.class, Bitmap.class, (ResourceDecoder<GifDecoder, Bitmap>)new GifFrameResourceDecoder(paramBitmapPool)).<Uri, Drawable>append(Uri.class, Drawable.class, (ResourceDecoder<Uri, Drawable>)resourceDrawableDecoder).<Uri, Bitmap>append(Uri.class, Bitmap.class, (ResourceDecoder<Uri, Bitmap>)new ResourceBitmapDecoder(resourceDrawableDecoder, paramBitmapPool)).register((DataRewinder.Factory<?>)new ByteBufferRewinder.Factory()).<File, ByteBuffer>append(File.class, ByteBuffer.class, (ModelLoaderFactory<File, ByteBuffer>)new ByteBufferFileLoader.Factory()).<File, InputStream>append(File.class, InputStream.class, (ModelLoaderFactory<File, InputStream>)new FileLoader.StreamFactory()).<File, File>append(File.class, File.class, (ResourceDecoder<File, File>)new FileDecoder()).<File, ParcelFileDescriptor>append(File.class, ParcelFileDescriptor.class, (ModelLoaderFactory<File, ParcelFileDescriptor>)new FileLoader.FileDescriptorFactory()).<File, File>append(File.class, File.class, (ModelLoaderFactory<File, File>)UnitModelLoader.Factory.getInstance()).register((DataRewinder.Factory<?>)new InputStreamRewinder.Factory(paramArrayPool)).<int, InputStream>append(int.class, InputStream.class, (ModelLoaderFactory<int, InputStream>)streamFactory).<int, ParcelFileDescriptor>append(int.class, ParcelFileDescriptor.class, (ModelLoaderFactory<int, ParcelFileDescriptor>)fileDescriptorFactory).<Integer, InputStream>append(Integer.class, InputStream.class, (ModelLoaderFactory<Integer, InputStream>)streamFactory).<Integer, ParcelFileDescriptor>append(Integer.class, ParcelFileDescriptor.class, (ModelLoaderFactory<Integer, ParcelFileDescriptor>)fileDescriptorFactory).<Integer, Uri>append(Integer.class, Uri.class, (ModelLoaderFactory<Integer, Uri>)uriFactory).<int, AssetFileDescriptor>append(int.class, AssetFileDescriptor.class, (ModelLoaderFactory<int, AssetFileDescriptor>)assetFileDescriptorFactory).<Integer, AssetFileDescriptor>append(Integer.class, AssetFileDescriptor.class, (ModelLoaderFactory<Integer, AssetFileDescriptor>)assetFileDescriptorFactory).<int, Uri>append(int.class, Uri.class, (ModelLoaderFactory<int, Uri>)uriFactory).<String, InputStream>append(String.class, InputStream.class, (ModelLoaderFactory<String, InputStream>)new DataUrlLoader.StreamFactory()).<Uri, InputStream>append(Uri.class, InputStream.class, (ModelLoaderFactory<Uri, InputStream>)new DataUrlLoader.StreamFactory()).<String, InputStream>append(String.class, InputStream.class, (ModelLoaderFactory<String, InputStream>)new StringLoader.StreamFactory()).<String, ParcelFileDescriptor>append(String.class, ParcelFileDescriptor.class, (ModelLoaderFactory<String, ParcelFileDescriptor>)new StringLoader.FileDescriptorFactory()).<String, AssetFileDescriptor>append(String.class, AssetFileDescriptor.class, (ModelLoaderFactory<String, AssetFileDescriptor>)new StringLoader.AssetFileDescriptorFactory()).<Uri, InputStream>append(Uri.class, InputStream.class, (ModelLoaderFactory<Uri, InputStream>)new HttpUriLoader.Factory()).<Uri, InputStream>append(Uri.class, InputStream.class, (ModelLoaderFactory<Uri, InputStream>)new AssetUriLoader.StreamFactory(paramContext.getAssets())).<Uri, ParcelFileDescriptor>append(Uri.class, ParcelFileDescriptor.class, (ModelLoaderFactory<Uri, ParcelFileDescriptor>)new AssetUriLoader.FileDescriptorFactory(paramContext.getAssets())).<Uri, InputStream>append(Uri.class, InputStream.class, (ModelLoaderFactory<Uri, InputStream>)new MediaStoreImageThumbLoader.Factory(paramContext)).<Uri, InputStream>append(Uri.class, InputStream.class, (ModelLoaderFactory<Uri, InputStream>)new MediaStoreVideoThumbLoader.Factory(paramContext)).<Uri, InputStream>append(Uri.class, InputStream.class, (ModelLoaderFactory<Uri, InputStream>)new UriLoader.StreamFactory(contentResolver)).<Uri, ParcelFileDescriptor>append(Uri.class, ParcelFileDescriptor.class, (ModelLoaderFactory<Uri, ParcelFileDescriptor>)new UriLoader.FileDescriptorFactory(contentResolver)).<Uri, AssetFileDescriptor>append(Uri.class, AssetFileDescriptor.class, (ModelLoaderFactory<Uri, AssetFileDescriptor>)new UriLoader.AssetFileDescriptorFactory(contentResolver)).<Uri, InputStream>append(Uri.class, InputStream.class, (ModelLoaderFactory<Uri, InputStream>)new UrlUriLoader.StreamFactory()).<URL, InputStream>append(URL.class, InputStream.class, (ModelLoaderFactory<URL, InputStream>)new UrlLoader.StreamFactory()).<Uri, File>append(Uri.class, File.class, (ModelLoaderFactory<Uri, File>)new MediaStoreFileLoader.Factory(paramContext)).<GlideUrl, InputStream>append(GlideUrl.class, InputStream.class, (ModelLoaderFactory<GlideUrl, InputStream>)new HttpGlideUrlLoader.Factory()).<byte[], ByteBuffer>append((Class)byte[].class, ByteBuffer.class, (ModelLoaderFactory<byte, ByteBuffer>)new ByteArrayLoader.ByteBufferFactory()).<byte[], InputStream>append((Class)byte[].class, InputStream.class, (ModelLoaderFactory<byte, InputStream>)new ByteArrayLoader.StreamFactory()).<Uri, Uri>append(Uri.class, Uri.class, (ModelLoaderFactory<Uri, Uri>)UnitModelLoader.Factory.getInstance()).<Drawable, Drawable>append(Drawable.class, Drawable.class, (ModelLoaderFactory<Drawable, Drawable>)UnitModelLoader.Factory.getInstance()).<Drawable, Drawable>append(Drawable.class, Drawable.class, (ResourceDecoder<Drawable, Drawable>)new UnitDrawableDecoder()).<Bitmap, BitmapDrawable>register(Bitmap.class, BitmapDrawable.class, (ResourceTranscoder<Bitmap, BitmapDrawable>)new BitmapDrawableTranscoder(resources)).<Bitmap, byte[]>register(Bitmap.class, (Class)byte[].class, (ResourceTranscoder<Bitmap, byte>)bitmapBytesTranscoder).<Drawable, byte[]>register(Drawable.class, (Class)byte[].class, (ResourceTranscoder<Drawable, byte>)new DrawableBytesTranscoder(paramBitmapPool, (ResourceTranscoder)bitmapBytesTranscoder, (ResourceTranscoder)gifDrawableBytesTranscoder)).register(GifDrawable.class, (Class)byte[].class, (ResourceTranscoder<GifDrawable, byte>)gifDrawableBytesTranscoder);
    ImageViewTargetFactory imageViewTargetFactory = new ImageViewTargetFactory();
    this.glideContext = new GlideContext(paramContext, paramArrayPool, this.registry, imageViewTargetFactory, paramRequestOptionsFactory, paramMap, paramList, paramEngine, paramBoolean1, paramInt1);
  }
  
  private static void checkAndInitializeGlide(Context paramContext, GeneratedAppGlideModule paramGeneratedAppGlideModule) {
    if (!isInitializing) {
      isInitializing = true;
      initializeGlide(paramContext, paramGeneratedAppGlideModule);
      isInitializing = false;
      return;
    } 
    throw new IllegalStateException("You cannot call Glide.get() in registerComponents(), use the provided Glide instance instead");
  }
  
  public static Glide get(Context paramContext) {
    // Byte code:
    //   0: getstatic com/bumptech/glide/Glide.glide : Lcom/bumptech/glide/Glide;
    //   3: ifnonnull -> 40
    //   6: aload_0
    //   7: invokevirtual getApplicationContext : ()Landroid/content/Context;
    //   10: invokestatic getAnnotationGeneratedGlideModules : (Landroid/content/Context;)Lcom/bumptech/glide/GeneratedAppGlideModule;
    //   13: astore_1
    //   14: ldc com/bumptech/glide/Glide
    //   16: monitorenter
    //   17: getstatic com/bumptech/glide/Glide.glide : Lcom/bumptech/glide/Glide;
    //   20: ifnonnull -> 28
    //   23: aload_0
    //   24: aload_1
    //   25: invokestatic checkAndInitializeGlide : (Landroid/content/Context;Lcom/bumptech/glide/GeneratedAppGlideModule;)V
    //   28: ldc com/bumptech/glide/Glide
    //   30: monitorexit
    //   31: goto -> 40
    //   34: astore_0
    //   35: ldc com/bumptech/glide/Glide
    //   37: monitorexit
    //   38: aload_0
    //   39: athrow
    //   40: getstatic com/bumptech/glide/Glide.glide : Lcom/bumptech/glide/Glide;
    //   43: areturn
    // Exception table:
    //   from	to	target	type
    //   17	28	34	finally
    //   28	31	34	finally
    //   35	38	34	finally
  }
  
  private static GeneratedAppGlideModule getAnnotationGeneratedGlideModules(Context paramContext) {
    try {
      GeneratedAppGlideModule generatedAppGlideModule = Class.forName("com.bumptech.glide.GeneratedAppGlideModuleImpl").getDeclaredConstructor(new Class[] { Context.class }).newInstance(new Object[] { paramContext.getApplicationContext() });
    } catch (ClassNotFoundException classNotFoundException) {
      if (Log.isLoggable("Glide", 5))
        Log.w("Glide", "Failed to find GeneratedAppGlideModule. You should include an annotationProcessor compile dependency on com.github.bumptech.glide:compiler in your application and a @GlideModule annotated AppGlideModule implementation or LibraryGlideModules will be silently ignored"); 
      classNotFoundException = null;
    } catch (InstantiationException instantiationException) {
      throwIncorrectGlideModule(instantiationException);
    } catch (IllegalAccessException illegalAccessException) {
      throwIncorrectGlideModule(illegalAccessException);
    } catch (NoSuchMethodException noSuchMethodException) {
      throwIncorrectGlideModule(noSuchMethodException);
    } catch (InvocationTargetException invocationTargetException) {
      throwIncorrectGlideModule(invocationTargetException);
    } 
    return (GeneratedAppGlideModule)invocationTargetException;
  }
  
  public static File getPhotoCacheDir(Context paramContext) {
    return getPhotoCacheDir(paramContext, "image_manager_disk_cache");
  }
  
  public static File getPhotoCacheDir(Context paramContext, String paramString) {
    File file = paramContext.getCacheDir();
    if (file != null) {
      file = new File(file, paramString);
      return (!file.mkdirs() && (!file.exists() || !file.isDirectory())) ? null : file;
    } 
    if (Log.isLoggable("Glide", 6))
      Log.e("Glide", "default disk cache dir is null"); 
    return null;
  }
  
  private static RequestManagerRetriever getRetriever(Context paramContext) {
    Preconditions.checkNotNull(paramContext, "You cannot start a load on a not yet attached View or a Fragment where getActivity() returns null (which usually occurs when getActivity() is called before the Fragment is attached or after the Fragment is destroyed).");
    return get(paramContext).getRequestManagerRetriever();
  }
  
  public static void init(Context paramContext, GlideBuilder paramGlideBuilder) {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic getAnnotationGeneratedGlideModules : (Landroid/content/Context;)Lcom/bumptech/glide/GeneratedAppGlideModule;
    //   4: astore_2
    //   5: ldc com/bumptech/glide/Glide
    //   7: monitorenter
    //   8: getstatic com/bumptech/glide/Glide.glide : Lcom/bumptech/glide/Glide;
    //   11: ifnull -> 17
    //   14: invokestatic tearDown : ()V
    //   17: aload_0
    //   18: aload_1
    //   19: aload_2
    //   20: invokestatic initializeGlide : (Landroid/content/Context;Lcom/bumptech/glide/GlideBuilder;Lcom/bumptech/glide/GeneratedAppGlideModule;)V
    //   23: ldc com/bumptech/glide/Glide
    //   25: monitorexit
    //   26: return
    //   27: astore_0
    //   28: ldc com/bumptech/glide/Glide
    //   30: monitorexit
    //   31: aload_0
    //   32: athrow
    // Exception table:
    //   from	to	target	type
    //   8	17	27	finally
    //   17	26	27	finally
    //   28	31	27	finally
  }
  
  @Deprecated
  public static void init(Glide paramGlide) {
    // Byte code:
    //   0: ldc com/bumptech/glide/Glide
    //   2: monitorenter
    //   3: getstatic com/bumptech/glide/Glide.glide : Lcom/bumptech/glide/Glide;
    //   6: ifnull -> 12
    //   9: invokestatic tearDown : ()V
    //   12: aload_0
    //   13: putstatic com/bumptech/glide/Glide.glide : Lcom/bumptech/glide/Glide;
    //   16: ldc com/bumptech/glide/Glide
    //   18: monitorexit
    //   19: return
    //   20: astore_0
    //   21: ldc com/bumptech/glide/Glide
    //   23: monitorexit
    //   24: aload_0
    //   25: athrow
    // Exception table:
    //   from	to	target	type
    //   3	12	20	finally
    //   12	16	20	finally
  }
  
  private static void initializeGlide(Context paramContext, GeneratedAppGlideModule paramGeneratedAppGlideModule) {
    initializeGlide(paramContext, new GlideBuilder(), paramGeneratedAppGlideModule);
  }
  
  private static void initializeGlide(Context paramContext, GlideBuilder paramGlideBuilder, GeneratedAppGlideModule paramGeneratedAppGlideModule) {
    StringBuilder stringBuilder;
    RequestManagerRetriever.RequestManagerFactory requestManagerFactory;
    Context context = paramContext.getApplicationContext();
    List<?> list = Collections.emptyList();
    if (paramGeneratedAppGlideModule == null || paramGeneratedAppGlideModule.isManifestParsingEnabled())
      list = (new ManifestParser(context)).parse(); 
    if (paramGeneratedAppGlideModule != null && !paramGeneratedAppGlideModule.getExcludedModuleClasses().isEmpty()) {
      Set<Class<?>> set = paramGeneratedAppGlideModule.getExcludedModuleClasses();
      requestManagerFactory = (RequestManagerRetriever.RequestManagerFactory)list.iterator();
      while (requestManagerFactory.hasNext()) {
        GlideModule glideModule = (GlideModule)requestManagerFactory.next();
        if (!set.contains(glideModule.getClass()))
          continue; 
        if (Log.isLoggable("Glide", 3)) {
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("AppGlideModule excludes manifest GlideModule: ");
          stringBuilder1.append(glideModule);
          Log.d("Glide", stringBuilder1.toString());
        } 
        requestManagerFactory.remove();
      } 
    } 
    if (Log.isLoggable("Glide", 3))
      for (GlideModule glideModule : list) {
        requestManagerFactory = (RequestManagerRetriever.RequestManagerFactory)new StringBuilder();
        requestManagerFactory.append("Discovered GlideModule from manifest: ");
        requestManagerFactory.append(glideModule.getClass());
        Log.d("Glide", requestManagerFactory.toString());
      }  
    if (paramGeneratedAppGlideModule != null) {
      requestManagerFactory = paramGeneratedAppGlideModule.getRequestManagerFactory();
    } else {
      requestManagerFactory = null;
    } 
    paramGlideBuilder.setRequestManagerFactory(requestManagerFactory);
    null = list.iterator();
    while (null.hasNext())
      ((GlideModule)null.next()).applyOptions(context, paramGlideBuilder); 
    if (paramGeneratedAppGlideModule != null)
      paramGeneratedAppGlideModule.applyOptions(context, paramGlideBuilder); 
    Glide glide = paramGlideBuilder.build(context);
    for (GlideModule glideModule : list) {
      try {
        glideModule.registerComponents(context, glide, glide.registry);
      } catch (AbstractMethodError abstractMethodError) {
        stringBuilder = new StringBuilder();
        stringBuilder.append("Attempting to register a Glide v3 module. If you see this, you or one of your dependencies may be including Glide v3 even though you're using Glide v4. You'll need to find and remove (or update) the offending dependency. The v3 module name is: ");
        stringBuilder.append(glideModule.getClass().getName());
        throw new IllegalStateException(stringBuilder.toString(), abstractMethodError);
      } 
    } 
    if (abstractMethodError != null)
      abstractMethodError.registerComponents(context, (Glide)stringBuilder, ((Glide)stringBuilder).registry); 
    context.registerComponentCallbacks((ComponentCallbacks)stringBuilder);
    glide = (Glide)stringBuilder;
  }
  
  public static void tearDown() {
    // Byte code:
    //   0: ldc com/bumptech/glide/Glide
    //   2: monitorenter
    //   3: getstatic com/bumptech/glide/Glide.glide : Lcom/bumptech/glide/Glide;
    //   6: ifnull -> 33
    //   9: getstatic com/bumptech/glide/Glide.glide : Lcom/bumptech/glide/Glide;
    //   12: invokevirtual getContext : ()Landroid/content/Context;
    //   15: invokevirtual getApplicationContext : ()Landroid/content/Context;
    //   18: getstatic com/bumptech/glide/Glide.glide : Lcom/bumptech/glide/Glide;
    //   21: invokevirtual unregisterComponentCallbacks : (Landroid/content/ComponentCallbacks;)V
    //   24: getstatic com/bumptech/glide/Glide.glide : Lcom/bumptech/glide/Glide;
    //   27: getfield engine : Lcom/bumptech/glide/load/engine/Engine;
    //   30: invokevirtual shutdown : ()V
    //   33: aconst_null
    //   34: putstatic com/bumptech/glide/Glide.glide : Lcom/bumptech/glide/Glide;
    //   37: ldc com/bumptech/glide/Glide
    //   39: monitorexit
    //   40: return
    //   41: astore_0
    //   42: ldc com/bumptech/glide/Glide
    //   44: monitorexit
    //   45: aload_0
    //   46: athrow
    // Exception table:
    //   from	to	target	type
    //   3	33	41	finally
    //   33	37	41	finally
  }
  
  private static void throwIncorrectGlideModule(Exception paramException) {
    throw new IllegalStateException("GeneratedAppGlideModuleImpl is implemented incorrectly. If you've manually implemented this class, remove your implementation. The Annotation processor will generate a correct implementation.", paramException);
  }
  
  public static RequestManager with(Activity paramActivity) {
    return getRetriever((Context)paramActivity).get(paramActivity);
  }
  
  @Deprecated
  public static RequestManager with(Fragment paramFragment) {
    return getRetriever((Context)paramFragment.getActivity()).get(paramFragment);
  }
  
  public static RequestManager with(Context paramContext) {
    return getRetriever(paramContext).get(paramContext);
  }
  
  public static RequestManager with(View paramView) {
    return getRetriever(paramView.getContext()).get(paramView);
  }
  
  public static RequestManager with(Fragment paramFragment) {
    return getRetriever(paramFragment.getContext()).get(paramFragment);
  }
  
  public static RequestManager with(FragmentActivity paramFragmentActivity) {
    return getRetriever((Context)paramFragmentActivity).get(paramFragmentActivity);
  }
  
  public void clearDiskCache() {
    Util.assertBackgroundThread();
    this.engine.clearDiskCache();
  }
  
  public void clearMemory() {
    Util.assertMainThread();
    this.memoryCache.clearMemory();
    this.bitmapPool.clearMemory();
    this.arrayPool.clearMemory();
  }
  
  public ArrayPool getArrayPool() {
    return this.arrayPool;
  }
  
  public BitmapPool getBitmapPool() {
    return this.bitmapPool;
  }
  
  ConnectivityMonitorFactory getConnectivityMonitorFactory() {
    return this.connectivityMonitorFactory;
  }
  
  public Context getContext() {
    return this.glideContext.getBaseContext();
  }
  
  GlideContext getGlideContext() {
    return this.glideContext;
  }
  
  public Registry getRegistry() {
    return this.registry;
  }
  
  public RequestManagerRetriever getRequestManagerRetriever() {
    return this.requestManagerRetriever;
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {}
  
  public void onLowMemory() {
    clearMemory();
  }
  
  public void onTrimMemory(int paramInt) {
    trimMemory(paramInt);
  }
  
  public void preFillBitmapPool(PreFillType.Builder... paramVarArgs) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield bitmapPreFiller : Lcom/bumptech/glide/load/engine/prefill/BitmapPreFiller;
    //   6: ifnonnull -> 53
    //   9: aload_0
    //   10: getfield defaultRequestOptionsFactory : Lcom/bumptech/glide/Glide$RequestOptionsFactory;
    //   13: invokeinterface build : ()Lcom/bumptech/glide/request/RequestOptions;
    //   18: invokevirtual getOptions : ()Lcom/bumptech/glide/load/Options;
    //   21: getstatic com/bumptech/glide/load/resource/bitmap/Downsampler.DECODE_FORMAT : Lcom/bumptech/glide/load/Option;
    //   24: invokevirtual get : (Lcom/bumptech/glide/load/Option;)Ljava/lang/Object;
    //   27: checkcast com/bumptech/glide/load/DecodeFormat
    //   30: astore_2
    //   31: new com/bumptech/glide/load/engine/prefill/BitmapPreFiller
    //   34: astore_3
    //   35: aload_3
    //   36: aload_0
    //   37: getfield memoryCache : Lcom/bumptech/glide/load/engine/cache/MemoryCache;
    //   40: aload_0
    //   41: getfield bitmapPool : Lcom/bumptech/glide/load/engine/bitmap_recycle/BitmapPool;
    //   44: aload_2
    //   45: invokespecial <init> : (Lcom/bumptech/glide/load/engine/cache/MemoryCache;Lcom/bumptech/glide/load/engine/bitmap_recycle/BitmapPool;Lcom/bumptech/glide/load/DecodeFormat;)V
    //   48: aload_0
    //   49: aload_3
    //   50: putfield bitmapPreFiller : Lcom/bumptech/glide/load/engine/prefill/BitmapPreFiller;
    //   53: aload_0
    //   54: getfield bitmapPreFiller : Lcom/bumptech/glide/load/engine/prefill/BitmapPreFiller;
    //   57: aload_1
    //   58: invokevirtual preFill : ([Lcom/bumptech/glide/load/engine/prefill/PreFillType$Builder;)V
    //   61: aload_0
    //   62: monitorexit
    //   63: return
    //   64: astore_1
    //   65: aload_0
    //   66: monitorexit
    //   67: aload_1
    //   68: athrow
    // Exception table:
    //   from	to	target	type
    //   2	53	64	finally
    //   53	61	64	finally
  }
  
  void registerRequestManager(RequestManager paramRequestManager) {
    synchronized (this.managers) {
      if (!this.managers.contains(paramRequestManager)) {
        this.managers.add(paramRequestManager);
        return;
      } 
      IllegalStateException illegalStateException = new IllegalStateException();
      this("Cannot register already registered manager");
      throw illegalStateException;
    } 
  }
  
  boolean removeFromManagers(Target<?> paramTarget) {
    synchronized (this.managers) {
      Iterator<RequestManager> iterator = this.managers.iterator();
      while (iterator.hasNext()) {
        if (((RequestManager)iterator.next()).untrack(paramTarget))
          return true; 
      } 
      return false;
    } 
  }
  
  public MemoryCategory setMemoryCategory(MemoryCategory paramMemoryCategory) {
    Util.assertMainThread();
    this.memoryCache.setSizeMultiplier(paramMemoryCategory.getMultiplier());
    this.bitmapPool.setSizeMultiplier(paramMemoryCategory.getMultiplier());
    MemoryCategory memoryCategory = this.memoryCategory;
    this.memoryCategory = paramMemoryCategory;
    return memoryCategory;
  }
  
  public void trimMemory(int paramInt) {
    Util.assertMainThread();
    Iterator<RequestManager> iterator = this.managers.iterator();
    while (iterator.hasNext())
      ((RequestManager)iterator.next()).onTrimMemory(paramInt); 
    this.memoryCache.trimMemory(paramInt);
    this.bitmapPool.trimMemory(paramInt);
    this.arrayPool.trimMemory(paramInt);
  }
  
  void unregisterRequestManager(RequestManager paramRequestManager) {
    synchronized (this.managers) {
      if (this.managers.contains(paramRequestManager)) {
        this.managers.remove(paramRequestManager);
        return;
      } 
      IllegalStateException illegalStateException = new IllegalStateException();
      this("Cannot unregister not yet registered manager");
      throw illegalStateException;
    } 
  }
  
  public static interface RequestOptionsFactory {
    RequestOptions build();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/Glide.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
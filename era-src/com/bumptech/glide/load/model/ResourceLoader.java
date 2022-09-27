package com.bumptech.glide.load.model;

import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.bumptech.glide.load.Options;
import java.io.InputStream;

public class ResourceLoader<Data> implements ModelLoader<Integer, Data> {
  private static final String TAG = "ResourceLoader";
  
  private final Resources resources;
  
  private final ModelLoader<Uri, Data> uriLoader;
  
  public ResourceLoader(Resources paramResources, ModelLoader<Uri, Data> paramModelLoader) {
    this.resources = paramResources;
    this.uriLoader = paramModelLoader;
  }
  
  private Uri getResourceUri(Integer paramInteger) {
    try {
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("android.resource://");
      stringBuilder.append(this.resources.getResourcePackageName(paramInteger.intValue()));
      stringBuilder.append('/');
      stringBuilder.append(this.resources.getResourceTypeName(paramInteger.intValue()));
      stringBuilder.append('/');
      stringBuilder.append(this.resources.getResourceEntryName(paramInteger.intValue()));
      return Uri.parse(stringBuilder.toString());
    } catch (android.content.res.Resources.NotFoundException notFoundException) {
      if (Log.isLoggable("ResourceLoader", 5)) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Received invalid resource id: ");
        stringBuilder.append(paramInteger);
        Log.w("ResourceLoader", stringBuilder.toString(), (Throwable)notFoundException);
      } 
      return null;
    } 
  }
  
  public ModelLoader.LoadData<Data> buildLoadData(Integer paramInteger, int paramInt1, int paramInt2, Options paramOptions) {
    ModelLoader.LoadData<Data> loadData;
    Uri uri = getResourceUri(paramInteger);
    if (uri == null) {
      uri = null;
    } else {
      loadData = this.uriLoader.buildLoadData(uri, paramInt1, paramInt2, paramOptions);
    } 
    return loadData;
  }
  
  public boolean handles(Integer paramInteger) {
    return true;
  }
  
  public static final class AssetFileDescriptorFactory implements ModelLoaderFactory<Integer, AssetFileDescriptor> {
    private final Resources resources;
    
    public AssetFileDescriptorFactory(Resources param1Resources) {
      this.resources = param1Resources;
    }
    
    public ModelLoader<Integer, AssetFileDescriptor> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new ResourceLoader<AssetFileDescriptor>(this.resources, param1MultiModelLoaderFactory.build(Uri.class, AssetFileDescriptor.class));
    }
    
    public void teardown() {}
  }
  
  public static class FileDescriptorFactory implements ModelLoaderFactory<Integer, ParcelFileDescriptor> {
    private final Resources resources;
    
    public FileDescriptorFactory(Resources param1Resources) {
      this.resources = param1Resources;
    }
    
    public ModelLoader<Integer, ParcelFileDescriptor> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new ResourceLoader<ParcelFileDescriptor>(this.resources, param1MultiModelLoaderFactory.build(Uri.class, ParcelFileDescriptor.class));
    }
    
    public void teardown() {}
  }
  
  public static class StreamFactory implements ModelLoaderFactory<Integer, InputStream> {
    private final Resources resources;
    
    public StreamFactory(Resources param1Resources) {
      this.resources = param1Resources;
    }
    
    public ModelLoader<Integer, InputStream> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new ResourceLoader<InputStream>(this.resources, param1MultiModelLoaderFactory.build(Uri.class, InputStream.class));
    }
    
    public void teardown() {}
  }
  
  public static class UriFactory implements ModelLoaderFactory<Integer, Uri> {
    private final Resources resources;
    
    public UriFactory(Resources param1Resources) {
      this.resources = param1Resources;
    }
    
    public ModelLoader<Integer, Uri> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new ResourceLoader<Uri>(this.resources, UnitModelLoader.getInstance());
    }
    
    public void teardown() {}
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/model/ResourceLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
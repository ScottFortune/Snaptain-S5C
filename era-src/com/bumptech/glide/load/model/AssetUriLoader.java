package com.bumptech.glide.load.model;

import android.content.res.AssetManager;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.FileDescriptorAssetPathFetcher;
import com.bumptech.glide.load.data.StreamAssetPathFetcher;
import com.bumptech.glide.signature.ObjectKey;
import java.io.InputStream;

public class AssetUriLoader<Data> implements ModelLoader<Uri, Data> {
  private static final String ASSET_PATH_SEGMENT = "android_asset";
  
  private static final String ASSET_PREFIX = "file:///android_asset/";
  
  private static final int ASSET_PREFIX_LENGTH = 22;
  
  private final AssetManager assetManager;
  
  private final AssetFetcherFactory<Data> factory;
  
  public AssetUriLoader(AssetManager paramAssetManager, AssetFetcherFactory<Data> paramAssetFetcherFactory) {
    this.assetManager = paramAssetManager;
    this.factory = paramAssetFetcherFactory;
  }
  
  public ModelLoader.LoadData<Data> buildLoadData(Uri paramUri, int paramInt1, int paramInt2, Options paramOptions) {
    String str = paramUri.toString().substring(ASSET_PREFIX_LENGTH);
    return new ModelLoader.LoadData<Data>((Key)new ObjectKey(paramUri), this.factory.buildFetcher(this.assetManager, str));
  }
  
  public boolean handles(Uri paramUri) {
    boolean bool = "file".equals(paramUri.getScheme());
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (bool) {
      bool2 = bool1;
      if (!paramUri.getPathSegments().isEmpty()) {
        bool2 = bool1;
        if ("android_asset".equals(paramUri.getPathSegments().get(0)))
          bool2 = true; 
      } 
    } 
    return bool2;
  }
  
  public static interface AssetFetcherFactory<Data> {
    DataFetcher<Data> buildFetcher(AssetManager param1AssetManager, String param1String);
  }
  
  public static class FileDescriptorFactory implements ModelLoaderFactory<Uri, ParcelFileDescriptor>, AssetFetcherFactory<ParcelFileDescriptor> {
    private final AssetManager assetManager;
    
    public FileDescriptorFactory(AssetManager param1AssetManager) {
      this.assetManager = param1AssetManager;
    }
    
    public ModelLoader<Uri, ParcelFileDescriptor> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new AssetUriLoader<ParcelFileDescriptor>(this.assetManager, this);
    }
    
    public DataFetcher<ParcelFileDescriptor> buildFetcher(AssetManager param1AssetManager, String param1String) {
      return (DataFetcher<ParcelFileDescriptor>)new FileDescriptorAssetPathFetcher(param1AssetManager, param1String);
    }
    
    public void teardown() {}
  }
  
  public static class StreamFactory implements ModelLoaderFactory<Uri, InputStream>, AssetFetcherFactory<InputStream> {
    private final AssetManager assetManager;
    
    public StreamFactory(AssetManager param1AssetManager) {
      this.assetManager = param1AssetManager;
    }
    
    public ModelLoader<Uri, InputStream> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new AssetUriLoader<InputStream>(this.assetManager, this);
    }
    
    public DataFetcher<InputStream> buildFetcher(AssetManager param1AssetManager, String param1String) {
      return (DataFetcher<InputStream>)new StreamAssetPathFetcher(param1AssetManager, param1String);
    }
    
    public void teardown() {}
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/model/AssetUriLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
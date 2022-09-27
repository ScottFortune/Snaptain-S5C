package com.bumptech.glide.load.model;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.AssetFileDescriptorLocalUriFetcher;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.FileDescriptorLocalUriFetcher;
import com.bumptech.glide.load.data.StreamLocalUriFetcher;
import com.bumptech.glide.signature.ObjectKey;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UriLoader<Data> implements ModelLoader<Uri, Data> {
  private static final Set<String> SCHEMES = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(new String[] { "file", "android.resource", "content" })));
  
  private final LocalUriFetcherFactory<Data> factory;
  
  public UriLoader(LocalUriFetcherFactory<Data> paramLocalUriFetcherFactory) {
    this.factory = paramLocalUriFetcherFactory;
  }
  
  public ModelLoader.LoadData<Data> buildLoadData(Uri paramUri, int paramInt1, int paramInt2, Options paramOptions) {
    return new ModelLoader.LoadData<Data>((Key)new ObjectKey(paramUri), this.factory.build(paramUri));
  }
  
  public boolean handles(Uri paramUri) {
    return SCHEMES.contains(paramUri.getScheme());
  }
  
  public static final class AssetFileDescriptorFactory implements ModelLoaderFactory<Uri, AssetFileDescriptor>, LocalUriFetcherFactory<AssetFileDescriptor> {
    private final ContentResolver contentResolver;
    
    public AssetFileDescriptorFactory(ContentResolver param1ContentResolver) {
      this.contentResolver = param1ContentResolver;
    }
    
    public DataFetcher<AssetFileDescriptor> build(Uri param1Uri) {
      return (DataFetcher<AssetFileDescriptor>)new AssetFileDescriptorLocalUriFetcher(this.contentResolver, param1Uri);
    }
    
    public ModelLoader<Uri, AssetFileDescriptor> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new UriLoader<AssetFileDescriptor>(this);
    }
    
    public void teardown() {}
  }
  
  public static class FileDescriptorFactory implements ModelLoaderFactory<Uri, ParcelFileDescriptor>, LocalUriFetcherFactory<ParcelFileDescriptor> {
    private final ContentResolver contentResolver;
    
    public FileDescriptorFactory(ContentResolver param1ContentResolver) {
      this.contentResolver = param1ContentResolver;
    }
    
    public DataFetcher<ParcelFileDescriptor> build(Uri param1Uri) {
      return (DataFetcher<ParcelFileDescriptor>)new FileDescriptorLocalUriFetcher(this.contentResolver, param1Uri);
    }
    
    public ModelLoader<Uri, ParcelFileDescriptor> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new UriLoader<ParcelFileDescriptor>(this);
    }
    
    public void teardown() {}
  }
  
  public static interface LocalUriFetcherFactory<Data> {
    DataFetcher<Data> build(Uri param1Uri);
  }
  
  public static class StreamFactory implements ModelLoaderFactory<Uri, InputStream>, LocalUriFetcherFactory<InputStream> {
    private final ContentResolver contentResolver;
    
    public StreamFactory(ContentResolver param1ContentResolver) {
      this.contentResolver = param1ContentResolver;
    }
    
    public DataFetcher<InputStream> build(Uri param1Uri) {
      return (DataFetcher<InputStream>)new StreamLocalUriFetcher(this.contentResolver, param1Uri);
    }
    
    public ModelLoader<Uri, InputStream> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new UriLoader<InputStream>(this);
    }
    
    public void teardown() {}
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/model/UriLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
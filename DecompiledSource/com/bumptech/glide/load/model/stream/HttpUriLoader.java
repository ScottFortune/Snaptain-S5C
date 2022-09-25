package com.bumptech.glide.load.model.stream;

import android.net.Uri;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class HttpUriLoader implements ModelLoader<Uri, InputStream> {
  private static final Set<String> SCHEMES = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(new String[] { "http", "https" })));
  
  private final ModelLoader<GlideUrl, InputStream> urlLoader;
  
  public HttpUriLoader(ModelLoader<GlideUrl, InputStream> paramModelLoader) {
    this.urlLoader = paramModelLoader;
  }
  
  public ModelLoader.LoadData<InputStream> buildLoadData(Uri paramUri, int paramInt1, int paramInt2, Options paramOptions) {
    return this.urlLoader.buildLoadData(new GlideUrl(paramUri.toString()), paramInt1, paramInt2, paramOptions);
  }
  
  public boolean handles(Uri paramUri) {
    return SCHEMES.contains(paramUri.getScheme());
  }
  
  public static class Factory implements ModelLoaderFactory<Uri, InputStream> {
    public ModelLoader<Uri, InputStream> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new HttpUriLoader(param1MultiModelLoaderFactory.build(GlideUrl.class, InputStream.class));
    }
    
    public void teardown() {}
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/model/stream/HttpUriLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
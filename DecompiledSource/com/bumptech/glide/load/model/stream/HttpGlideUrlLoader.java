package com.bumptech.glide.load.model.stream;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.HttpUrlFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import java.io.InputStream;

public class HttpGlideUrlLoader implements ModelLoader<GlideUrl, InputStream> {
  public static final Option<Integer> TIMEOUT = Option.memory("com.bumptech.glide.load.model.stream.HttpGlideUrlLoader.Timeout", Integer.valueOf(2500));
  
  private final ModelCache<GlideUrl, GlideUrl> modelCache;
  
  public HttpGlideUrlLoader() {
    this(null);
  }
  
  public HttpGlideUrlLoader(ModelCache<GlideUrl, GlideUrl> paramModelCache) {
    this.modelCache = paramModelCache;
  }
  
  public ModelLoader.LoadData<InputStream> buildLoadData(GlideUrl paramGlideUrl, int paramInt1, int paramInt2, Options paramOptions) {
    ModelCache<GlideUrl, GlideUrl> modelCache = this.modelCache;
    GlideUrl glideUrl = paramGlideUrl;
    if (modelCache != null) {
      glideUrl = (GlideUrl)modelCache.get(paramGlideUrl, 0, 0);
      if (glideUrl == null) {
        this.modelCache.put(paramGlideUrl, 0, 0, paramGlideUrl);
        glideUrl = paramGlideUrl;
      } 
    } 
    return new ModelLoader.LoadData((Key)glideUrl, (DataFetcher)new HttpUrlFetcher(glideUrl, ((Integer)paramOptions.get(TIMEOUT)).intValue()));
  }
  
  public boolean handles(GlideUrl paramGlideUrl) {
    return true;
  }
  
  public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {
    private final ModelCache<GlideUrl, GlideUrl> modelCache = new ModelCache(500L);
    
    public ModelLoader<GlideUrl, InputStream> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new HttpGlideUrlLoader(this.modelCache);
    }
    
    public void teardown() {}
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/model/stream/HttpGlideUrlLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
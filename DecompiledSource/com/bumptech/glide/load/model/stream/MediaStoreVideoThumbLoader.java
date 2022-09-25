package com.bumptech.glide.load.model.stream;

import android.content.Context;
import android.net.Uri;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.mediastore.MediaStoreUtil;
import com.bumptech.glide.load.data.mediastore.ThumbFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.load.resource.bitmap.VideoDecoder;
import com.bumptech.glide.signature.ObjectKey;
import java.io.InputStream;

public class MediaStoreVideoThumbLoader implements ModelLoader<Uri, InputStream> {
  private final Context context;
  
  public MediaStoreVideoThumbLoader(Context paramContext) {
    this.context = paramContext.getApplicationContext();
  }
  
  private boolean isRequestingDefaultFrame(Options paramOptions) {
    boolean bool;
    Long long_ = (Long)paramOptions.get(VideoDecoder.TARGET_FRAME);
    if (long_ != null && long_.longValue() == -1L) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public ModelLoader.LoadData<InputStream> buildLoadData(Uri paramUri, int paramInt1, int paramInt2, Options paramOptions) {
    return (MediaStoreUtil.isThumbnailSize(paramInt1, paramInt2) && isRequestingDefaultFrame(paramOptions)) ? new ModelLoader.LoadData((Key)new ObjectKey(paramUri), (DataFetcher)ThumbFetcher.buildVideoFetcher(this.context, paramUri)) : null;
  }
  
  public boolean handles(Uri paramUri) {
    return MediaStoreUtil.isMediaStoreVideoUri(paramUri);
  }
  
  public static class Factory implements ModelLoaderFactory<Uri, InputStream> {
    private final Context context;
    
    public Factory(Context param1Context) {
      this.context = param1Context;
    }
    
    public ModelLoader<Uri, InputStream> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new MediaStoreVideoThumbLoader(this.context);
    }
    
    public void teardown() {}
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/model/stream/MediaStoreVideoThumbLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
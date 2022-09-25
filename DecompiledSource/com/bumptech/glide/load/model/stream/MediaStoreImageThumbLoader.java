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
import com.bumptech.glide.signature.ObjectKey;
import java.io.InputStream;

public class MediaStoreImageThumbLoader implements ModelLoader<Uri, InputStream> {
  private final Context context;
  
  public MediaStoreImageThumbLoader(Context paramContext) {
    this.context = paramContext.getApplicationContext();
  }
  
  public ModelLoader.LoadData<InputStream> buildLoadData(Uri paramUri, int paramInt1, int paramInt2, Options paramOptions) {
    return MediaStoreUtil.isThumbnailSize(paramInt1, paramInt2) ? new ModelLoader.LoadData((Key)new ObjectKey(paramUri), (DataFetcher)ThumbFetcher.buildImageFetcher(this.context, paramUri)) : null;
  }
  
  public boolean handles(Uri paramUri) {
    return MediaStoreUtil.isMediaStoreImageUri(paramUri);
  }
  
  public static class Factory implements ModelLoaderFactory<Uri, InputStream> {
    private final Context context;
    
    public Factory(Context param1Context) {
      this.context = param1Context;
    }
    
    public ModelLoader<Uri, InputStream> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new MediaStoreImageThumbLoader(this.context);
    }
    
    public void teardown() {}
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/model/stream/MediaStoreImageThumbLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
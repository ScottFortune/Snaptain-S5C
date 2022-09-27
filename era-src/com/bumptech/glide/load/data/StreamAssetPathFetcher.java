package com.bumptech.glide.load.data;

import android.content.res.AssetManager;
import java.io.IOException;
import java.io.InputStream;

public class StreamAssetPathFetcher extends AssetPathFetcher<InputStream> {
  public StreamAssetPathFetcher(AssetManager paramAssetManager, String paramString) {
    super(paramAssetManager, paramString);
  }
  
  protected void close(InputStream paramInputStream) throws IOException {
    paramInputStream.close();
  }
  
  public Class<InputStream> getDataClass() {
    return InputStream.class;
  }
  
  protected InputStream loadResource(AssetManager paramAssetManager, String paramString) throws IOException {
    return paramAssetManager.open(paramString);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/data/StreamAssetPathFetcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
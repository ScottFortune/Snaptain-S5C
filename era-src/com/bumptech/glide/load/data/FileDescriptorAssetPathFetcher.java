package com.bumptech.glide.load.data;

import android.content.res.AssetManager;
import android.os.ParcelFileDescriptor;
import java.io.IOException;

public class FileDescriptorAssetPathFetcher extends AssetPathFetcher<ParcelFileDescriptor> {
  public FileDescriptorAssetPathFetcher(AssetManager paramAssetManager, String paramString) {
    super(paramAssetManager, paramString);
  }
  
  protected void close(ParcelFileDescriptor paramParcelFileDescriptor) throws IOException {
    paramParcelFileDescriptor.close();
  }
  
  public Class<ParcelFileDescriptor> getDataClass() {
    return ParcelFileDescriptor.class;
  }
  
  protected ParcelFileDescriptor loadResource(AssetManager paramAssetManager, String paramString) throws IOException {
    return paramAssetManager.openFd(paramString).getParcelFileDescriptor();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/data/FileDescriptorAssetPathFetcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
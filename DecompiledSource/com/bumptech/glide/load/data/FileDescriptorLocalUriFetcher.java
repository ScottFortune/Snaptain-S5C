package com.bumptech.glide.load.data;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileDescriptorLocalUriFetcher extends LocalUriFetcher<ParcelFileDescriptor> {
  public FileDescriptorLocalUriFetcher(ContentResolver paramContentResolver, Uri paramUri) {
    super(paramContentResolver, paramUri);
  }
  
  protected void close(ParcelFileDescriptor paramParcelFileDescriptor) throws IOException {
    paramParcelFileDescriptor.close();
  }
  
  public Class<ParcelFileDescriptor> getDataClass() {
    return ParcelFileDescriptor.class;
  }
  
  protected ParcelFileDescriptor loadResource(Uri paramUri, ContentResolver paramContentResolver) throws FileNotFoundException {
    AssetFileDescriptor assetFileDescriptor = paramContentResolver.openAssetFileDescriptor(paramUri, "r");
    if (assetFileDescriptor != null)
      return assetFileDescriptor.getParcelFileDescriptor(); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("FileDescriptor is null for: ");
    stringBuilder.append(paramUri);
    throw new FileNotFoundException(stringBuilder.toString());
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/data/FileDescriptorLocalUriFetcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
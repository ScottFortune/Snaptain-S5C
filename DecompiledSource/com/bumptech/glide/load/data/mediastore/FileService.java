package com.bumptech.glide.load.data.mediastore;

import java.io.File;

class FileService {
  public boolean exists(File paramFile) {
    return paramFile.exists();
  }
  
  public File get(String paramString) {
    return new File(paramString);
  }
  
  public long length(File paramFile) {
    return paramFile.length();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/data/mediastore/FileService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
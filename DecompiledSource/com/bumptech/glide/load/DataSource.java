package com.bumptech.glide.load;

public enum DataSource {
  DATA_DISK_CACHE, LOCAL, MEMORY_CACHE, REMOTE, RESOURCE_DISK_CACHE;
  
  static {
    DATA_DISK_CACHE = new DataSource("DATA_DISK_CACHE", 2);
    RESOURCE_DISK_CACHE = new DataSource("RESOURCE_DISK_CACHE", 3);
    MEMORY_CACHE = new DataSource("MEMORY_CACHE", 4);
    $VALUES = new DataSource[] { LOCAL, REMOTE, DATA_DISK_CACHE, RESOURCE_DISK_CACHE, MEMORY_CACHE };
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/DataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
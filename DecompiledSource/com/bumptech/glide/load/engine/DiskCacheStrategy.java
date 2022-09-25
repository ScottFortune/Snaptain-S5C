package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.EncodeStrategy;

public abstract class DiskCacheStrategy {
  public static final DiskCacheStrategy ALL = new DiskCacheStrategy() {
      public boolean decodeCachedData() {
        return true;
      }
      
      public boolean decodeCachedResource() {
        return true;
      }
      
      public boolean isDataCacheable(DataSource param1DataSource) {
        boolean bool;
        if (param1DataSource == DataSource.REMOTE) {
          bool = true;
        } else {
          bool = false;
        } 
        return bool;
      }
      
      public boolean isResourceCacheable(boolean param1Boolean, DataSource param1DataSource, EncodeStrategy param1EncodeStrategy) {
        if (param1DataSource != DataSource.RESOURCE_DISK_CACHE && param1DataSource != DataSource.MEMORY_CACHE) {
          param1Boolean = true;
        } else {
          param1Boolean = false;
        } 
        return param1Boolean;
      }
    };
  
  public static final DiskCacheStrategy AUTOMATIC;
  
  public static final DiskCacheStrategy DATA;
  
  public static final DiskCacheStrategy NONE = new DiskCacheStrategy() {
      public boolean decodeCachedData() {
        return false;
      }
      
      public boolean decodeCachedResource() {
        return false;
      }
      
      public boolean isDataCacheable(DataSource param1DataSource) {
        return false;
      }
      
      public boolean isResourceCacheable(boolean param1Boolean, DataSource param1DataSource, EncodeStrategy param1EncodeStrategy) {
        return false;
      }
    };
  
  public static final DiskCacheStrategy RESOURCE;
  
  static {
    DATA = new DiskCacheStrategy() {
        public boolean decodeCachedData() {
          return true;
        }
        
        public boolean decodeCachedResource() {
          return false;
        }
        
        public boolean isDataCacheable(DataSource param1DataSource) {
          boolean bool;
          if (param1DataSource != DataSource.DATA_DISK_CACHE && param1DataSource != DataSource.MEMORY_CACHE) {
            bool = true;
          } else {
            bool = false;
          } 
          return bool;
        }
        
        public boolean isResourceCacheable(boolean param1Boolean, DataSource param1DataSource, EncodeStrategy param1EncodeStrategy) {
          return false;
        }
      };
    RESOURCE = new DiskCacheStrategy() {
        public boolean decodeCachedData() {
          return false;
        }
        
        public boolean decodeCachedResource() {
          return true;
        }
        
        public boolean isDataCacheable(DataSource param1DataSource) {
          return false;
        }
        
        public boolean isResourceCacheable(boolean param1Boolean, DataSource param1DataSource, EncodeStrategy param1EncodeStrategy) {
          if (param1DataSource != DataSource.RESOURCE_DISK_CACHE && param1DataSource != DataSource.MEMORY_CACHE) {
            param1Boolean = true;
          } else {
            param1Boolean = false;
          } 
          return param1Boolean;
        }
      };
    AUTOMATIC = new DiskCacheStrategy() {
        public boolean decodeCachedData() {
          return true;
        }
        
        public boolean decodeCachedResource() {
          return true;
        }
        
        public boolean isDataCacheable(DataSource param1DataSource) {
          boolean bool;
          if (param1DataSource == DataSource.REMOTE) {
            bool = true;
          } else {
            bool = false;
          } 
          return bool;
        }
        
        public boolean isResourceCacheable(boolean param1Boolean, DataSource param1DataSource, EncodeStrategy param1EncodeStrategy) {
          if (((param1Boolean && param1DataSource == DataSource.DATA_DISK_CACHE) || param1DataSource == DataSource.LOCAL) && param1EncodeStrategy == EncodeStrategy.TRANSFORMED) {
            param1Boolean = true;
          } else {
            param1Boolean = false;
          } 
          return param1Boolean;
        }
      };
  }
  
  public abstract boolean decodeCachedData();
  
  public abstract boolean decodeCachedResource();
  
  public abstract boolean isDataCacheable(DataSource paramDataSource);
  
  public abstract boolean isResourceCacheable(boolean paramBoolean, DataSource paramDataSource, EncodeStrategy paramEncodeStrategy);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/DiskCacheStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
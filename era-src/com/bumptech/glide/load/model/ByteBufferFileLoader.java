package com.bumptech.glide.load.model;

import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.signature.ObjectKey;
import com.bumptech.glide.util.ByteBufferUtil;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ByteBufferFileLoader implements ModelLoader<File, ByteBuffer> {
  private static final String TAG = "ByteBufferFileLoader";
  
  public ModelLoader.LoadData<ByteBuffer> buildLoadData(File paramFile, int paramInt1, int paramInt2, Options paramOptions) {
    return new ModelLoader.LoadData<ByteBuffer>((Key)new ObjectKey(paramFile), new ByteBufferFetcher(paramFile));
  }
  
  public boolean handles(File paramFile) {
    return true;
  }
  
  private static final class ByteBufferFetcher implements DataFetcher<ByteBuffer> {
    private final File file;
    
    ByteBufferFetcher(File param1File) {
      this.file = param1File;
    }
    
    public void cancel() {}
    
    public void cleanup() {}
    
    public Class<ByteBuffer> getDataClass() {
      return ByteBuffer.class;
    }
    
    public DataSource getDataSource() {
      return DataSource.LOCAL;
    }
    
    public void loadData(Priority param1Priority, DataFetcher.DataCallback<? super ByteBuffer> param1DataCallback) {
      try {
        ByteBuffer byteBuffer = ByteBufferUtil.fromFile(this.file);
        param1DataCallback.onDataReady(byteBuffer);
        return;
      } catch (IOException iOException) {
        if (Log.isLoggable("ByteBufferFileLoader", 3))
          Log.d("ByteBufferFileLoader", "Failed to obtain ByteBuffer for file", iOException); 
        param1DataCallback.onLoadFailed(iOException);
        return;
      } 
    }
  }
  
  public static class Factory implements ModelLoaderFactory<File, ByteBuffer> {
    public ModelLoader<File, ByteBuffer> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new ByteBufferFileLoader();
    }
    
    public void teardown() {}
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/model/ByteBufferFileLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
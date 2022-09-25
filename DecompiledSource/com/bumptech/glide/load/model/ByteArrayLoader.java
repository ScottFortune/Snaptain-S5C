package com.bumptech.glide.load.model;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.signature.ObjectKey;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteArrayLoader<Data> implements ModelLoader<byte[], Data> {
  private final Converter<Data> converter;
  
  public ByteArrayLoader(Converter<Data> paramConverter) {
    this.converter = paramConverter;
  }
  
  public ModelLoader.LoadData<Data> buildLoadData(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, Options paramOptions) {
    return new ModelLoader.LoadData<Data>((Key)new ObjectKey(paramArrayOfbyte), new Fetcher<Data>(paramArrayOfbyte, this.converter));
  }
  
  public boolean handles(byte[] paramArrayOfbyte) {
    return true;
  }
  
  public static class ByteBufferFactory implements ModelLoaderFactory<byte[], ByteBuffer> {
    public ModelLoader<byte[], ByteBuffer> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new ByteArrayLoader<ByteBuffer>(new ByteArrayLoader.Converter<ByteBuffer>() {
            public ByteBuffer convert(byte[] param2ArrayOfbyte) {
              return ByteBuffer.wrap(param2ArrayOfbyte);
            }
            
            public Class<ByteBuffer> getDataClass() {
              return ByteBuffer.class;
            }
          });
    }
    
    public void teardown() {}
  }
  
  class null implements Converter<ByteBuffer> {
    public ByteBuffer convert(byte[] param1ArrayOfbyte) {
      return ByteBuffer.wrap(param1ArrayOfbyte);
    }
    
    public Class<ByteBuffer> getDataClass() {
      return ByteBuffer.class;
    }
  }
  
  public static interface Converter<Data> {
    Data convert(byte[] param1ArrayOfbyte);
    
    Class<Data> getDataClass();
  }
  
  private static class Fetcher<Data> implements DataFetcher<Data> {
    private final ByteArrayLoader.Converter<Data> converter;
    
    private final byte[] model;
    
    Fetcher(byte[] param1ArrayOfbyte, ByteArrayLoader.Converter<Data> param1Converter) {
      this.model = param1ArrayOfbyte;
      this.converter = param1Converter;
    }
    
    public void cancel() {}
    
    public void cleanup() {}
    
    public Class<Data> getDataClass() {
      return this.converter.getDataClass();
    }
    
    public DataSource getDataSource() {
      return DataSource.LOCAL;
    }
    
    public void loadData(Priority param1Priority, DataFetcher.DataCallback<? super Data> param1DataCallback) {
      param1DataCallback.onDataReady(this.converter.convert(this.model));
    }
  }
  
  public static class StreamFactory implements ModelLoaderFactory<byte[], InputStream> {
    public ModelLoader<byte[], InputStream> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new ByteArrayLoader<InputStream>(new ByteArrayLoader.Converter<InputStream>() {
            public InputStream convert(byte[] param2ArrayOfbyte) {
              return new ByteArrayInputStream(param2ArrayOfbyte);
            }
            
            public Class<InputStream> getDataClass() {
              return InputStream.class;
            }
          });
    }
    
    public void teardown() {}
  }
  
  class null implements Converter<InputStream> {
    public InputStream convert(byte[] param1ArrayOfbyte) {
      return new ByteArrayInputStream(param1ArrayOfbyte);
    }
    
    public Class<InputStream> getDataClass() {
      return InputStream.class;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/model/ByteArrayLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
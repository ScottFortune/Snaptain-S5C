package com.bumptech.glide.load.model;

import android.util.Base64;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.signature.ObjectKey;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class DataUrlLoader<Model, Data> implements ModelLoader<Model, Data> {
  private static final String BASE64_TAG = ";base64";
  
  private static final String DATA_SCHEME_IMAGE = "data:image";
  
  private final DataDecoder<Data> dataDecoder;
  
  public DataUrlLoader(DataDecoder<Data> paramDataDecoder) {
    this.dataDecoder = paramDataDecoder;
  }
  
  public ModelLoader.LoadData<Data> buildLoadData(Model paramModel, int paramInt1, int paramInt2, Options paramOptions) {
    return new ModelLoader.LoadData<Data>((Key)new ObjectKey(paramModel), new DataUriFetcher<Data>(paramModel.toString(), this.dataDecoder));
  }
  
  public boolean handles(Model paramModel) {
    return paramModel.toString().startsWith("data:image");
  }
  
  public static interface DataDecoder<Data> {
    void close(Data param1Data) throws IOException;
    
    Data decode(String param1String) throws IllegalArgumentException;
    
    Class<Data> getDataClass();
  }
  
  private static final class DataUriFetcher<Data> implements DataFetcher<Data> {
    private Data data;
    
    private final String dataUri;
    
    private final DataUrlLoader.DataDecoder<Data> reader;
    
    DataUriFetcher(String param1String, DataUrlLoader.DataDecoder<Data> param1DataDecoder) {
      this.dataUri = param1String;
      this.reader = param1DataDecoder;
    }
    
    public void cancel() {}
    
    public void cleanup() {
      try {
        this.reader.close(this.data);
      } catch (IOException iOException) {}
    }
    
    public Class<Data> getDataClass() {
      return this.reader.getDataClass();
    }
    
    public DataSource getDataSource() {
      return DataSource.LOCAL;
    }
    
    public void loadData(Priority param1Priority, DataFetcher.DataCallback<? super Data> param1DataCallback) {
      try {
        this.data = this.reader.decode(this.dataUri);
        param1DataCallback.onDataReady(this.data);
      } catch (IllegalArgumentException illegalArgumentException) {
        param1DataCallback.onLoadFailed(illegalArgumentException);
      } 
    }
  }
  
  public static final class StreamFactory<Model> implements ModelLoaderFactory<Model, InputStream> {
    private final DataUrlLoader.DataDecoder<InputStream> opener = new DataUrlLoader.DataDecoder<InputStream>() {
        public void close(InputStream param2InputStream) throws IOException {
          param2InputStream.close();
        }
        
        public InputStream decode(String param2String) {
          if (param2String.startsWith("data:image")) {
            int i = param2String.indexOf(',');
            if (i != -1) {
              if (param2String.substring(0, i).endsWith(";base64"))
                return new ByteArrayInputStream(Base64.decode(param2String.substring(i + 1), 0)); 
              throw new IllegalArgumentException("Not a base64 image data URL.");
            } 
            throw new IllegalArgumentException("Missing comma in data URL.");
          } 
          throw new IllegalArgumentException("Not a valid image data URL.");
        }
        
        public Class<InputStream> getDataClass() {
          return InputStream.class;
        }
      };
    
    public ModelLoader<Model, InputStream> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new DataUrlLoader<Model, InputStream>(this.opener);
    }
    
    public void teardown() {}
  }
  
  class null implements DataDecoder<InputStream> {
    public void close(InputStream param1InputStream) throws IOException {
      param1InputStream.close();
    }
    
    public InputStream decode(String param1String) {
      if (param1String.startsWith("data:image")) {
        int i = param1String.indexOf(',');
        if (i != -1) {
          if (param1String.substring(0, i).endsWith(";base64"))
            return new ByteArrayInputStream(Base64.decode(param1String.substring(i + 1), 0)); 
          throw new IllegalArgumentException("Not a base64 image data URL.");
        } 
        throw new IllegalArgumentException("Missing comma in data URL.");
      } 
      throw new IllegalArgumentException("Not a valid image data URL.");
    }
    
    public Class<InputStream> getDataClass() {
      return InputStream.class;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/model/DataUrlLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
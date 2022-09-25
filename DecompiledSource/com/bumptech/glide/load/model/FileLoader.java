package com.bumptech.glide.load.model;

import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.signature.ObjectKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileLoader<Data> implements ModelLoader<File, Data> {
  private static final String TAG = "FileLoader";
  
  private final FileOpener<Data> fileOpener;
  
  public FileLoader(FileOpener<Data> paramFileOpener) {
    this.fileOpener = paramFileOpener;
  }
  
  public ModelLoader.LoadData<Data> buildLoadData(File paramFile, int paramInt1, int paramInt2, Options paramOptions) {
    return new ModelLoader.LoadData<Data>((Key)new ObjectKey(paramFile), new FileFetcher<Data>(paramFile, this.fileOpener));
  }
  
  public boolean handles(File paramFile) {
    return true;
  }
  
  public static class Factory<Data> implements ModelLoaderFactory<File, Data> {
    private final FileLoader.FileOpener<Data> opener;
    
    public Factory(FileLoader.FileOpener<Data> param1FileOpener) {
      this.opener = param1FileOpener;
    }
    
    public final ModelLoader<File, Data> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new FileLoader<Data>(this.opener);
    }
    
    public final void teardown() {}
  }
  
  public static class FileDescriptorFactory extends Factory<ParcelFileDescriptor> {
    public FileDescriptorFactory() {
      super(new FileLoader.FileOpener<ParcelFileDescriptor>() {
            public void close(ParcelFileDescriptor param2ParcelFileDescriptor) throws IOException {
              param2ParcelFileDescriptor.close();
            }
            
            public Class<ParcelFileDescriptor> getDataClass() {
              return ParcelFileDescriptor.class;
            }
            
            public ParcelFileDescriptor open(File param2File) throws FileNotFoundException {
              return ParcelFileDescriptor.open(param2File, 268435456);
            }
          });
    }
  }
  
  class null implements FileOpener<ParcelFileDescriptor> {
    public void close(ParcelFileDescriptor param1ParcelFileDescriptor) throws IOException {
      param1ParcelFileDescriptor.close();
    }
    
    public Class<ParcelFileDescriptor> getDataClass() {
      return ParcelFileDescriptor.class;
    }
    
    public ParcelFileDescriptor open(File param1File) throws FileNotFoundException {
      return ParcelFileDescriptor.open(param1File, 268435456);
    }
  }
  
  private static final class FileFetcher<Data> implements DataFetcher<Data> {
    private Data data;
    
    private final File file;
    
    private final FileLoader.FileOpener<Data> opener;
    
    FileFetcher(File param1File, FileLoader.FileOpener<Data> param1FileOpener) {
      this.file = param1File;
      this.opener = param1FileOpener;
    }
    
    public void cancel() {}
    
    public void cleanup() {
      Data data = this.data;
      if (data != null)
        try {
          this.opener.close(data);
        } catch (IOException iOException) {} 
    }
    
    public Class<Data> getDataClass() {
      return this.opener.getDataClass();
    }
    
    public DataSource getDataSource() {
      return DataSource.LOCAL;
    }
    
    public void loadData(Priority param1Priority, DataFetcher.DataCallback<? super Data> param1DataCallback) {
      try {
        this.data = this.opener.open(this.file);
        param1DataCallback.onDataReady(this.data);
        return;
      } catch (FileNotFoundException fileNotFoundException) {
        if (Log.isLoggable("FileLoader", 3))
          Log.d("FileLoader", "Failed to open file", fileNotFoundException); 
        param1DataCallback.onLoadFailed(fileNotFoundException);
        return;
      } 
    }
  }
  
  public static interface FileOpener<Data> {
    void close(Data param1Data) throws IOException;
    
    Class<Data> getDataClass();
    
    Data open(File param1File) throws FileNotFoundException;
  }
  
  public static class StreamFactory extends Factory<InputStream> {
    public StreamFactory() {
      super(new FileLoader.FileOpener<InputStream>() {
            public void close(InputStream param2InputStream) throws IOException {
              param2InputStream.close();
            }
            
            public Class<InputStream> getDataClass() {
              return InputStream.class;
            }
            
            public InputStream open(File param2File) throws FileNotFoundException {
              return new FileInputStream(param2File);
            }
          });
    }
  }
  
  class null implements FileOpener<InputStream> {
    public void close(InputStream param1InputStream) throws IOException {
      param1InputStream.close();
    }
    
    public Class<InputStream> getDataClass() {
      return InputStream.class;
    }
    
    public InputStream open(File param1File) throws FileNotFoundException {
      return new FileInputStream(param1File);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/model/FileLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
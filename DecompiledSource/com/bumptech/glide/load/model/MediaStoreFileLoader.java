package com.bumptech.glide.load.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.mediastore.MediaStoreUtil;
import com.bumptech.glide.signature.ObjectKey;
import java.io.File;
import java.io.FileNotFoundException;

public final class MediaStoreFileLoader implements ModelLoader<Uri, File> {
  private final Context context;
  
  public MediaStoreFileLoader(Context paramContext) {
    this.context = paramContext;
  }
  
  public ModelLoader.LoadData<File> buildLoadData(Uri paramUri, int paramInt1, int paramInt2, Options paramOptions) {
    return new ModelLoader.LoadData<File>((Key)new ObjectKey(paramUri), new FilePathFetcher(this.context, paramUri));
  }
  
  public boolean handles(Uri paramUri) {
    return MediaStoreUtil.isMediaStoreUri(paramUri);
  }
  
  public static final class Factory implements ModelLoaderFactory<Uri, File> {
    private final Context context;
    
    public Factory(Context param1Context) {
      this.context = param1Context;
    }
    
    public ModelLoader<Uri, File> build(MultiModelLoaderFactory param1MultiModelLoaderFactory) {
      return new MediaStoreFileLoader(this.context);
    }
    
    public void teardown() {}
  }
  
  private static class FilePathFetcher implements DataFetcher<File> {
    private static final String[] PROJECTION = new String[] { "_data" };
    
    private final Context context;
    
    private final Uri uri;
    
    FilePathFetcher(Context param1Context, Uri param1Uri) {
      this.context = param1Context;
      this.uri = param1Uri;
    }
    
    public void cancel() {}
    
    public void cleanup() {}
    
    public Class<File> getDataClass() {
      return File.class;
    }
    
    public DataSource getDataSource() {
      return DataSource.LOCAL;
    }
    
    public void loadData(Priority param1Priority, DataFetcher.DataCallback<? super File> param1DataCallback) {
      StringBuilder stringBuilder;
      Cursor cursor = this.context.getContentResolver().query(this.uri, PROJECTION, null, null, null);
      param1Priority = null;
      Priority priority = null;
      if (cursor != null) {
        param1Priority = priority;
        try {
          if (cursor.moveToFirst())
            String str = cursor.getString(cursor.getColumnIndexOrThrow("_data")); 
        } finally {
          cursor.close();
        } 
      } 
      if (TextUtils.isEmpty((CharSequence)param1Priority)) {
        stringBuilder = new StringBuilder();
        stringBuilder.append("Failed to find file path for: ");
        stringBuilder.append(this.uri);
        param1DataCallback.onLoadFailed(new FileNotFoundException(stringBuilder.toString()));
      } else {
        param1DataCallback.onDataReady(new File((String)stringBuilder));
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/model/MediaStoreFileLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
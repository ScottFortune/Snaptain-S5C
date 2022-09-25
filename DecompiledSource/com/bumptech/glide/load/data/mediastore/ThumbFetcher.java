package com.bumptech.glide.load.data.mediastore;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.ExifOrientationStream;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ThumbFetcher implements DataFetcher<InputStream> {
  private static final String TAG = "MediaStoreThumbFetcher";
  
  private InputStream inputStream;
  
  private final Uri mediaStoreImageUri;
  
  private final ThumbnailStreamOpener opener;
  
  ThumbFetcher(Uri paramUri, ThumbnailStreamOpener paramThumbnailStreamOpener) {
    this.mediaStoreImageUri = paramUri;
    this.opener = paramThumbnailStreamOpener;
  }
  
  private static ThumbFetcher build(Context paramContext, Uri paramUri, ThumbnailQuery paramThumbnailQuery) {
    ArrayPool arrayPool = Glide.get(paramContext).getArrayPool();
    return new ThumbFetcher(paramUri, new ThumbnailStreamOpener(Glide.get(paramContext).getRegistry().getImageHeaderParsers(), paramThumbnailQuery, arrayPool, paramContext.getContentResolver()));
  }
  
  public static ThumbFetcher buildImageFetcher(Context paramContext, Uri paramUri) {
    return build(paramContext, paramUri, new ImageThumbnailQuery(paramContext.getContentResolver()));
  }
  
  public static ThumbFetcher buildVideoFetcher(Context paramContext, Uri paramUri) {
    return build(paramContext, paramUri, new VideoThumbnailQuery(paramContext.getContentResolver()));
  }
  
  private InputStream openThumbInputStream() throws FileNotFoundException {
    byte b;
    ExifOrientationStream exifOrientationStream;
    InputStream inputStream1 = this.opener.open(this.mediaStoreImageUri);
    if (inputStream1 != null) {
      b = this.opener.getOrientation(this.mediaStoreImageUri);
    } else {
      b = -1;
    } 
    InputStream inputStream2 = inputStream1;
    if (b != -1)
      exifOrientationStream = new ExifOrientationStream(inputStream1, b); 
    return (InputStream)exifOrientationStream;
  }
  
  public void cancel() {}
  
  public void cleanup() {
    InputStream inputStream = this.inputStream;
    if (inputStream != null)
      try {
        inputStream.close();
      } catch (IOException iOException) {} 
  }
  
  public Class<InputStream> getDataClass() {
    return InputStream.class;
  }
  
  public DataSource getDataSource() {
    return DataSource.LOCAL;
  }
  
  public void loadData(Priority paramPriority, DataFetcher.DataCallback<? super InputStream> paramDataCallback) {
    try {
      this.inputStream = openThumbInputStream();
      paramDataCallback.onDataReady(this.inputStream);
      return;
    } catch (FileNotFoundException fileNotFoundException) {
      if (Log.isLoggable("MediaStoreThumbFetcher", 3))
        Log.d("MediaStoreThumbFetcher", "Failed to find thumbnail file", fileNotFoundException); 
      paramDataCallback.onLoadFailed(fileNotFoundException);
      return;
    } 
  }
  
  static class ImageThumbnailQuery implements ThumbnailQuery {
    private static final String[] PATH_PROJECTION = new String[] { "_data" };
    
    private static final String PATH_SELECTION = "kind = 1 AND image_id = ?";
    
    private final ContentResolver contentResolver;
    
    ImageThumbnailQuery(ContentResolver param1ContentResolver) {
      this.contentResolver = param1ContentResolver;
    }
    
    public Cursor query(Uri param1Uri) {
      String str = param1Uri.getLastPathSegment();
      return this.contentResolver.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, PATH_PROJECTION, "kind = 1 AND image_id = ?", new String[] { str }, null);
    }
  }
  
  static class VideoThumbnailQuery implements ThumbnailQuery {
    private static final String[] PATH_PROJECTION = new String[] { "_data" };
    
    private static final String PATH_SELECTION = "kind = 1 AND video_id = ?";
    
    private final ContentResolver contentResolver;
    
    VideoThumbnailQuery(ContentResolver param1ContentResolver) {
      this.contentResolver = param1ContentResolver;
    }
    
    public Cursor query(Uri param1Uri) {
      String str = param1Uri.getLastPathSegment();
      return this.contentResolver.query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, PATH_PROJECTION, "kind = 1 AND video_id = ?", new String[] { str }, null);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/data/mediastore/ThumbFetcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.bumptech.glide.load.data.mediastore;

import android.content.ContentResolver;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

class ThumbnailStreamOpener {
  private static final FileService DEFAULT_SERVICE = new FileService();
  
  private static final String TAG = "ThumbStreamOpener";
  
  private final ArrayPool byteArrayPool;
  
  private final ContentResolver contentResolver;
  
  private final List<ImageHeaderParser> parsers;
  
  private final ThumbnailQuery query;
  
  private final FileService service;
  
  ThumbnailStreamOpener(List<ImageHeaderParser> paramList, FileService paramFileService, ThumbnailQuery paramThumbnailQuery, ArrayPool paramArrayPool, ContentResolver paramContentResolver) {
    this.service = paramFileService;
    this.query = paramThumbnailQuery;
    this.byteArrayPool = paramArrayPool;
    this.contentResolver = paramContentResolver;
    this.parsers = paramList;
  }
  
  ThumbnailStreamOpener(List<ImageHeaderParser> paramList, ThumbnailQuery paramThumbnailQuery, ArrayPool paramArrayPool, ContentResolver paramContentResolver) {
    this(paramList, DEFAULT_SERVICE, paramThumbnailQuery, paramArrayPool, paramContentResolver);
  }
  
  private String getPath(Uri paramUri) {
    // Byte code:
    //   0: aload_0
    //   1: getfield query : Lcom/bumptech/glide/load/data/mediastore/ThumbnailQuery;
    //   4: aload_1
    //   5: invokeinterface query : (Landroid/net/Uri;)Landroid/database/Cursor;
    //   10: astore_2
    //   11: aload_2
    //   12: ifnull -> 55
    //   15: aload_2
    //   16: astore_3
    //   17: aload_2
    //   18: invokeinterface moveToFirst : ()Z
    //   23: ifeq -> 55
    //   26: aload_2
    //   27: astore_3
    //   28: aload_2
    //   29: iconst_0
    //   30: invokeinterface getString : (I)Ljava/lang/String;
    //   35: astore #4
    //   37: aload_2
    //   38: ifnull -> 47
    //   41: aload_2
    //   42: invokeinterface close : ()V
    //   47: aload #4
    //   49: areturn
    //   50: astore #4
    //   52: goto -> 77
    //   55: aload_2
    //   56: ifnull -> 65
    //   59: aload_2
    //   60: invokeinterface close : ()V
    //   65: aconst_null
    //   66: areturn
    //   67: astore_1
    //   68: aconst_null
    //   69: astore_3
    //   70: goto -> 149
    //   73: astore #4
    //   75: aconst_null
    //   76: astore_2
    //   77: aload_2
    //   78: astore_3
    //   79: ldc 'ThumbStreamOpener'
    //   81: iconst_3
    //   82: invokestatic isLoggable : (Ljava/lang/String;I)Z
    //   85: ifeq -> 136
    //   88: aload_2
    //   89: astore_3
    //   90: new java/lang/StringBuilder
    //   93: astore #5
    //   95: aload_2
    //   96: astore_3
    //   97: aload #5
    //   99: invokespecial <init> : ()V
    //   102: aload_2
    //   103: astore_3
    //   104: aload #5
    //   106: ldc 'Failed to query for thumbnail for Uri: '
    //   108: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   111: pop
    //   112: aload_2
    //   113: astore_3
    //   114: aload #5
    //   116: aload_1
    //   117: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   120: pop
    //   121: aload_2
    //   122: astore_3
    //   123: ldc 'ThumbStreamOpener'
    //   125: aload #5
    //   127: invokevirtual toString : ()Ljava/lang/String;
    //   130: aload #4
    //   132: invokestatic d : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   135: pop
    //   136: aload_2
    //   137: ifnull -> 146
    //   140: aload_2
    //   141: invokeinterface close : ()V
    //   146: aconst_null
    //   147: areturn
    //   148: astore_1
    //   149: aload_3
    //   150: ifnull -> 159
    //   153: aload_3
    //   154: invokeinterface close : ()V
    //   159: aload_1
    //   160: athrow
    // Exception table:
    //   from	to	target	type
    //   0	11	73	java/lang/SecurityException
    //   0	11	67	finally
    //   17	26	50	java/lang/SecurityException
    //   17	26	148	finally
    //   28	37	50	java/lang/SecurityException
    //   28	37	148	finally
    //   79	88	148	finally
    //   90	95	148	finally
    //   97	102	148	finally
    //   104	112	148	finally
    //   114	121	148	finally
    //   123	136	148	finally
  }
  
  private boolean isValid(File paramFile) {
    boolean bool;
    if (this.service.exists(paramFile) && 0L < this.service.length(paramFile)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  int getOrientation(Uri paramUri) {
    NullPointerException nullPointerException;
    IOException iOException2;
    InputStream inputStream1 = null;
    InputStream inputStream2 = null;
    InputStream inputStream3 = null;
    try {
      InputStream inputStream = this.contentResolver.openInputStream(paramUri);
      inputStream3 = inputStream;
      inputStream1 = inputStream;
      inputStream2 = inputStream;
      int i = ImageHeaderParserUtils.getOrientation(this.parsers, inputStream, this.byteArrayPool);
      if (inputStream != null)
        try {
          inputStream.close();
        } catch (IOException iOException) {} 
      return i;
    } catch (IOException iOException1) {
    
    } catch (NullPointerException nullPointerException1) {
      iOException2 = iOException1;
      nullPointerException = nullPointerException1;
    } finally {}
    IOException iOException3 = iOException2;
    if (Log.isLoggable("ThumbStreamOpener", 3)) {
      iOException3 = iOException2;
      StringBuilder stringBuilder = new StringBuilder();
      iOException3 = iOException2;
      this();
      iOException3 = iOException2;
      stringBuilder.append("Failed to open uri: ");
      iOException3 = iOException2;
      stringBuilder.append(paramUri);
      iOException3 = iOException2;
      Log.d("ThumbStreamOpener", stringBuilder.toString(), nullPointerException);
    } 
    if (iOException2 != null)
      try {
        iOException2.close();
      } catch (IOException iOException) {} 
    return -1;
  }
  
  public InputStream open(Uri paramUri) throws FileNotFoundException {
    String str = getPath(paramUri);
    if (TextUtils.isEmpty(str))
      return null; 
    File file = this.service.get(str);
    if (!isValid(file))
      return null; 
    Uri uri = Uri.fromFile(file);
    try {
      return this.contentResolver.openInputStream(uri);
    } catch (NullPointerException nullPointerException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("NPE opening uri: ");
      stringBuilder.append(paramUri);
      stringBuilder.append(" -> ");
      stringBuilder.append(uri);
      throw (FileNotFoundException)(new FileNotFoundException(stringBuilder.toString())).initCause(nullPointerException);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/data/mediastore/ThumbnailStreamOpener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
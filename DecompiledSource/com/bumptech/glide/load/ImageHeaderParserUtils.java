package com.bumptech.glide.load;

import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

public final class ImageHeaderParserUtils {
  private static final int MARK_POSITION = 5242880;
  
  public static int getOrientation(List<ImageHeaderParser> paramList, InputStream paramInputStream, ArrayPool paramArrayPool) throws IOException {
    RecyclableBufferedInputStream recyclableBufferedInputStream;
    if (paramInputStream == null)
      return -1; 
    InputStream inputStream = paramInputStream;
    if (!paramInputStream.markSupported())
      recyclableBufferedInputStream = new RecyclableBufferedInputStream(paramInputStream, paramArrayPool); 
    recyclableBufferedInputStream.mark(5242880);
    byte b = 0;
    int i = paramList.size();
    while (b < i) {
      ImageHeaderParser imageHeaderParser = paramList.get(b);
      try {
        int j = imageHeaderParser.getOrientation((InputStream)recyclableBufferedInputStream, paramArrayPool);
        if (j != -1)
          return j; 
        recyclableBufferedInputStream.reset();
      } finally {
        recyclableBufferedInputStream.reset();
      } 
    } 
    return -1;
  }
  
  public static ImageHeaderParser.ImageType getType(List<ImageHeaderParser> paramList, InputStream paramInputStream, ArrayPool paramArrayPool) throws IOException {
    RecyclableBufferedInputStream recyclableBufferedInputStream;
    if (paramInputStream == null)
      return ImageHeaderParser.ImageType.UNKNOWN; 
    InputStream inputStream = paramInputStream;
    if (!paramInputStream.markSupported())
      recyclableBufferedInputStream = new RecyclableBufferedInputStream(paramInputStream, paramArrayPool); 
    recyclableBufferedInputStream.mark(5242880);
    byte b = 0;
    int i = paramList.size();
    while (b < i) {
      ImageHeaderParser imageHeaderParser = paramList.get(b);
      try {
        ImageHeaderParser.ImageType imageType2 = imageHeaderParser.getType((InputStream)recyclableBufferedInputStream);
        ImageHeaderParser.ImageType imageType1 = ImageHeaderParser.ImageType.UNKNOWN;
        if (imageType2 != imageType1)
          return imageType2; 
        recyclableBufferedInputStream.reset();
      } finally {
        recyclableBufferedInputStream.reset();
      } 
    } 
    return ImageHeaderParser.ImageType.UNKNOWN;
  }
  
  public static ImageHeaderParser.ImageType getType(List<ImageHeaderParser> paramList, ByteBuffer paramByteBuffer) throws IOException {
    if (paramByteBuffer == null)
      return ImageHeaderParser.ImageType.UNKNOWN; 
    byte b = 0;
    int i = paramList.size();
    while (b < i) {
      ImageHeaderParser.ImageType imageType = ((ImageHeaderParser)paramList.get(b)).getType(paramByteBuffer);
      if (imageType != ImageHeaderParser.ImageType.UNKNOWN)
        return imageType; 
      b++;
    } 
    return ImageHeaderParser.ImageType.UNKNOWN;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/ImageHeaderParserUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
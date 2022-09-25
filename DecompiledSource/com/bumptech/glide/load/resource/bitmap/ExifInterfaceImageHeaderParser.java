package com.bumptech.glide.load.resource.bitmap;

import android.media.ExifInterface;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.ByteBufferUtil;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class ExifInterfaceImageHeaderParser implements ImageHeaderParser {
  public int getOrientation(InputStream paramInputStream, ArrayPool paramArrayPool) throws IOException {
    int i = (new ExifInterface(paramInputStream)).getAttributeInt("Orientation", 1);
    int j = i;
    if (i == 0)
      j = -1; 
    return j;
  }
  
  public int getOrientation(ByteBuffer paramByteBuffer, ArrayPool paramArrayPool) throws IOException {
    return getOrientation(ByteBufferUtil.toStream(paramByteBuffer), paramArrayPool);
  }
  
  public ImageHeaderParser.ImageType getType(InputStream paramInputStream) throws IOException {
    return ImageHeaderParser.ImageType.UNKNOWN;
  }
  
  public ImageHeaderParser.ImageType getType(ByteBuffer paramByteBuffer) throws IOException {
    return ImageHeaderParser.ImageType.UNKNOWN;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/bitmap/ExifInterfaceImageHeaderParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
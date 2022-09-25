package com.bumptech.glide.load;

import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public interface ImageHeaderParser {
  public static final int UNKNOWN_ORIENTATION = -1;
  
  int getOrientation(InputStream paramInputStream, ArrayPool paramArrayPool) throws IOException;
  
  int getOrientation(ByteBuffer paramByteBuffer, ArrayPool paramArrayPool) throws IOException;
  
  ImageType getType(InputStream paramInputStream) throws IOException;
  
  ImageType getType(ByteBuffer paramByteBuffer) throws IOException;
  
  public enum ImageType {
    GIF(true),
    JPEG(false),
    PNG(false),
    PNG_A(false),
    RAW(false),
    UNKNOWN(false),
    WEBP(false),
    WEBP_A(false);
    
    private final boolean hasAlpha;
    
    static {
      PNG = new ImageType("PNG", 4, false);
      WEBP_A = new ImageType("WEBP_A", 5, true);
      WEBP = new ImageType("WEBP", 6, false);
      UNKNOWN = new ImageType("UNKNOWN", 7, false);
      $VALUES = new ImageType[] { GIF, JPEG, RAW, PNG_A, PNG, WEBP_A, WEBP, UNKNOWN };
    }
    
    ImageType(boolean param1Boolean) {
      this.hasAlpha = param1Boolean;
    }
    
    public boolean hasAlpha() {
      return this.hasAlpha;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/ImageHeaderParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
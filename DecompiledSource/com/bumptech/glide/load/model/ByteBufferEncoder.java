package com.bumptech.glide.load.model;

import android.util.Log;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.util.ByteBufferUtil;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ByteBufferEncoder implements Encoder<ByteBuffer> {
  private static final String TAG = "ByteBufferEncoder";
  
  public boolean encode(ByteBuffer paramByteBuffer, File paramFile, Options paramOptions) {
    boolean bool;
    try {
      ByteBufferUtil.toFile(paramByteBuffer, paramFile);
      bool = true;
    } catch (IOException iOException) {
      if (Log.isLoggable("ByteBufferEncoder", 3))
        Log.d("ByteBufferEncoder", "Failed to write data", iOException); 
      bool = false;
    } 
    return bool;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/model/ByteBufferEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.bumptech.glide.load.resource.gif;

import android.util.Log;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

public class StreamGifDecoder implements ResourceDecoder<InputStream, GifDrawable> {
  private static final String TAG = "StreamGifDecoder";
  
  private final ArrayPool byteArrayPool;
  
  private final ResourceDecoder<ByteBuffer, GifDrawable> byteBufferDecoder;
  
  private final List<ImageHeaderParser> parsers;
  
  public StreamGifDecoder(List<ImageHeaderParser> paramList, ResourceDecoder<ByteBuffer, GifDrawable> paramResourceDecoder, ArrayPool paramArrayPool) {
    this.parsers = paramList;
    this.byteBufferDecoder = paramResourceDecoder;
    this.byteArrayPool = paramArrayPool;
  }
  
  private static byte[] inputStreamToBytes(InputStream paramInputStream) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(16384);
    try {
      byte[] arrayOfByte = new byte[16384];
      while (true) {
        int i = paramInputStream.read(arrayOfByte);
        if (i != -1) {
          byteArrayOutputStream.write(arrayOfByte, 0, i);
          continue;
        } 
        byteArrayOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
      } 
    } catch (IOException iOException) {
      if (Log.isLoggable("StreamGifDecoder", 5))
        Log.w("StreamGifDecoder", "Error reading data from stream", iOException); 
      return null;
    } 
  }
  
  public Resource<GifDrawable> decode(InputStream paramInputStream, int paramInt1, int paramInt2, Options paramOptions) throws IOException {
    byte[] arrayOfByte = inputStreamToBytes(paramInputStream);
    if (arrayOfByte == null)
      return null; 
    ByteBuffer byteBuffer = ByteBuffer.wrap(arrayOfByte);
    return this.byteBufferDecoder.decode(byteBuffer, paramInt1, paramInt2, paramOptions);
  }
  
  public boolean handles(InputStream paramInputStream, Options paramOptions) throws IOException {
    boolean bool;
    if (!((Boolean)paramOptions.get(GifOptions.DISABLE_ANIMATION)).booleanValue() && ImageHeaderParserUtils.getType(this.parsers, paramInputStream, this.byteArrayPool) == ImageHeaderParser.ImageType.GIF) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/gif/StreamGifDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
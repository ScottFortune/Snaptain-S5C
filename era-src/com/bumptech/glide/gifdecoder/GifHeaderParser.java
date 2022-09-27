package com.bumptech.glide.gifdecoder;

import android.util.Log;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class GifHeaderParser {
  static final int DEFAULT_FRAME_DELAY = 10;
  
  private static final int DESCRIPTOR_MASK_INTERLACE_FLAG = 64;
  
  private static final int DESCRIPTOR_MASK_LCT_FLAG = 128;
  
  private static final int DESCRIPTOR_MASK_LCT_SIZE = 7;
  
  private static final int EXTENSION_INTRODUCER = 33;
  
  private static final int GCE_DISPOSAL_METHOD_SHIFT = 2;
  
  private static final int GCE_MASK_DISPOSAL_METHOD = 28;
  
  private static final int GCE_MASK_TRANSPARENT_COLOR_FLAG = 1;
  
  private static final int IMAGE_SEPARATOR = 44;
  
  private static final int LABEL_APPLICATION_EXTENSION = 255;
  
  private static final int LABEL_COMMENT_EXTENSION = 254;
  
  private static final int LABEL_GRAPHIC_CONTROL_EXTENSION = 249;
  
  private static final int LABEL_PLAIN_TEXT_EXTENSION = 1;
  
  private static final int LSD_MASK_GCT_FLAG = 128;
  
  private static final int LSD_MASK_GCT_SIZE = 7;
  
  private static final int MASK_INT_LOWEST_BYTE = 255;
  
  private static final int MAX_BLOCK_SIZE = 256;
  
  static final int MIN_FRAME_DELAY = 2;
  
  private static final String TAG = "GifHeaderParser";
  
  private static final int TRAILER = 59;
  
  private final byte[] block = new byte[256];
  
  private int blockSize = 0;
  
  private GifHeader header;
  
  private ByteBuffer rawData;
  
  private boolean err() {
    boolean bool;
    if (this.header.status != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private int read() {
    boolean bool;
    try {
      byte b = this.rawData.get();
      bool = b & 0xFF;
    } catch (Exception exception) {
      this.header.status = 1;
      bool = false;
    } 
    return bool;
  }
  
  private void readBitmap() {
    boolean bool2;
    this.header.currentFrame.ix = readShort();
    this.header.currentFrame.iy = readShort();
    this.header.currentFrame.iw = readShort();
    this.header.currentFrame.ih = readShort();
    int i = read();
    boolean bool1 = false;
    if ((i & 0x80) != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    int j = (int)Math.pow(2.0D, ((i & 0x7) + 1));
    GifFrame gifFrame = this.header.currentFrame;
    if ((i & 0x40) != 0)
      bool1 = true; 
    gifFrame.interlace = bool1;
    if (bool2) {
      this.header.currentFrame.lct = readColorTable(j);
    } else {
      this.header.currentFrame.lct = null;
    } 
    this.header.currentFrame.bufferFrameStart = this.rawData.position();
    skipImageData();
    if (err())
      return; 
    GifHeader gifHeader = this.header;
    gifHeader.frameCount++;
    this.header.frames.add(this.header.currentFrame);
  }
  
  private void readBlock() {
    this.blockSize = read();
    if (this.blockSize > 0) {
      int i = 0;
      int j = 0;
      while (true) {
        int k = j;
        try {
          if (i < this.blockSize) {
            k = j;
            j = this.blockSize - i;
            k = j;
            this.rawData.get(this.block, i, j);
            i += j;
            continue;
          } 
          break;
        } catch (Exception exception) {
          if (Log.isLoggable("GifHeaderParser", 3)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error Reading Block n: ");
            stringBuilder.append(i);
            stringBuilder.append(" count: ");
            stringBuilder.append(k);
            stringBuilder.append(" blockSize: ");
            stringBuilder.append(this.blockSize);
            Log.d("GifHeaderParser", stringBuilder.toString(), exception);
          } 
          this.header.status = 1;
        } 
        return;
      } 
    } 
  }
  
  private int[] readColorTable(int paramInt) {
    byte[] arrayOfByte = new byte[paramInt * 3];
    int[] arrayOfInt1 = null;
    int[] arrayOfInt2 = arrayOfInt1;
    try {
      this.rawData.get(arrayOfByte);
      arrayOfInt2 = arrayOfInt1;
      arrayOfInt1 = new int[256];
      byte b = 0;
      int i = 0;
      while (true) {
        arrayOfInt2 = arrayOfInt1;
        if (b < paramInt) {
          int j = i + 1;
          byte b1 = arrayOfByte[i];
          i = j + 1;
          arrayOfInt1[b] = (b1 & 0xFF) << 16 | 0xFF000000 | (arrayOfByte[j] & 0xFF) << 8 | arrayOfByte[i] & 0xFF;
          i++;
          b++;
          continue;
        } 
        break;
      } 
    } catch (BufferUnderflowException bufferUnderflowException) {
      if (Log.isLoggable("GifHeaderParser", 3))
        Log.d("GifHeaderParser", "Format Error Reading Color Table", bufferUnderflowException); 
      this.header.status = 1;
    } 
    return arrayOfInt2;
  }
  
  private void readContents() {
    readContents(2147483647);
  }
  
  private void readContents(int paramInt) {
    boolean bool = false;
    while (!bool && !err() && this.header.frameCount <= paramInt) {
      int i = read();
      if (i != 33) {
        if (i != 44) {
          if (i != 59) {
            this.header.status = 1;
            continue;
          } 
          bool = true;
          continue;
        } 
        if (this.header.currentFrame == null)
          this.header.currentFrame = new GifFrame(); 
        readBitmap();
        continue;
      } 
      i = read();
      if (i != 1) {
        if (i != 249) {
          if (i != 254) {
            if (i != 255) {
              skip();
              continue;
            } 
            readBlock();
            StringBuilder stringBuilder = new StringBuilder();
            for (i = 0; i < 11; i++)
              stringBuilder.append((char)this.block[i]); 
            if (stringBuilder.toString().equals("NETSCAPE2.0")) {
              readNetscapeExt();
              continue;
            } 
            skip();
            continue;
          } 
          skip();
          continue;
        } 
        this.header.currentFrame = new GifFrame();
        readGraphicControlExt();
        continue;
      } 
      skip();
    } 
  }
  
  private void readGraphicControlExt() {
    read();
    int i = read();
    this.header.currentFrame.dispose = (i & 0x1C) >> 2;
    int j = this.header.currentFrame.dispose;
    boolean bool = true;
    if (j == 0)
      this.header.currentFrame.dispose = 1; 
    GifFrame gifFrame = this.header.currentFrame;
    if ((i & 0x1) == 0)
      bool = false; 
    gifFrame.transparency = bool;
    i = readShort();
    j = i;
    if (i < 2)
      j = 10; 
    this.header.currentFrame.delay = j * 10;
    this.header.currentFrame.transIndex = read();
    read();
  }
  
  private void readHeader() {
    StringBuilder stringBuilder = new StringBuilder();
    for (byte b = 0; b < 6; b++)
      stringBuilder.append((char)read()); 
    if (!stringBuilder.toString().startsWith("GIF")) {
      this.header.status = 1;
      return;
    } 
    readLSD();
    if (this.header.gctFlag && !err()) {
      GifHeader gifHeader = this.header;
      gifHeader.gct = readColorTable(gifHeader.gctSize);
      gifHeader = this.header;
      gifHeader.bgColor = gifHeader.gct[this.header.bgIndex];
    } 
  }
  
  private void readLSD() {
    boolean bool;
    this.header.width = readShort();
    this.header.height = readShort();
    int i = read();
    GifHeader gifHeader = this.header;
    if ((i & 0x80) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    gifHeader.gctFlag = bool;
    this.header.gctSize = (int)Math.pow(2.0D, ((i & 0x7) + 1));
    this.header.bgIndex = read();
    this.header.pixelAspect = read();
  }
  
  private void readNetscapeExt() {
    do {
      readBlock();
      byte[] arrayOfByte = this.block;
      if (arrayOfByte[0] != 1)
        continue; 
      byte b1 = arrayOfByte[1];
      byte b2 = arrayOfByte[2];
      this.header.loopCount = (b2 & 0xFF) << 8 | b1 & 0xFF;
    } while (this.blockSize > 0 && !err());
  }
  
  private int readShort() {
    return this.rawData.getShort();
  }
  
  private void reset() {
    this.rawData = null;
    Arrays.fill(this.block, (byte)0);
    this.header = new GifHeader();
    this.blockSize = 0;
  }
  
  private void skip() {
    int i;
    do {
      i = read();
      int j = Math.min(this.rawData.position() + i, this.rawData.limit());
      this.rawData.position(j);
    } while (i > 0);
  }
  
  private void skipImageData() {
    read();
    skip();
  }
  
  public void clear() {
    this.rawData = null;
    this.header = null;
  }
  
  public boolean isAnimated() {
    readHeader();
    if (!err())
      readContents(2); 
    int i = this.header.frameCount;
    boolean bool = true;
    if (i <= 1)
      bool = false; 
    return bool;
  }
  
  public GifHeader parseHeader() {
    if (this.rawData != null) {
      if (err())
        return this.header; 
      readHeader();
      if (!err()) {
        readContents();
        if (this.header.frameCount < 0)
          this.header.status = 1; 
      } 
      return this.header;
    } 
    throw new IllegalStateException("You must call setData() before parseHeader()");
  }
  
  public GifHeaderParser setData(ByteBuffer paramByteBuffer) {
    reset();
    this.rawData = paramByteBuffer.asReadOnlyBuffer();
    this.rawData.position(0);
    this.rawData.order(ByteOrder.LITTLE_ENDIAN);
    return this;
  }
  
  public GifHeaderParser setData(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte != null) {
      setData(ByteBuffer.wrap(paramArrayOfbyte));
    } else {
      this.rawData = null;
      this.header.status = 2;
    } 
    return this;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/gifdecoder/GifHeaderParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.bumptech.glide.load.resource.bitmap;

import android.util.Log;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public final class DefaultImageHeaderParser implements ImageHeaderParser {
  private static final int[] BYTES_PER_FORMAT;
  
  static final int EXIF_MAGIC_NUMBER = 65496;
  
  static final int EXIF_SEGMENT_TYPE = 225;
  
  private static final int GIF_HEADER = 4671814;
  
  private static final int INTEL_TIFF_MAGIC_NUMBER = 18761;
  
  private static final String JPEG_EXIF_SEGMENT_PREAMBLE = "Exif\000\000";
  
  static final byte[] JPEG_EXIF_SEGMENT_PREAMBLE_BYTES = "Exif\000\000".getBytes(Charset.forName("UTF-8"));
  
  private static final int MARKER_EOI = 217;
  
  private static final int MOTOROLA_TIFF_MAGIC_NUMBER = 19789;
  
  private static final int ORIENTATION_TAG_TYPE = 274;
  
  private static final int PNG_HEADER = -1991225785;
  
  private static final int RIFF_HEADER = 1380533830;
  
  private static final int SEGMENT_SOS = 218;
  
  static final int SEGMENT_START_ID = 255;
  
  private static final String TAG = "DfltImageHeaderParser";
  
  private static final int VP8_HEADER = 1448097792;
  
  private static final int VP8_HEADER_MASK = -256;
  
  private static final int VP8_HEADER_TYPE_EXTENDED = 88;
  
  private static final int VP8_HEADER_TYPE_LOSSLESS = 76;
  
  private static final int VP8_HEADER_TYPE_MASK = 255;
  
  private static final int WEBP_EXTENDED_ALPHA_FLAG = 16;
  
  private static final int WEBP_HEADER = 1464156752;
  
  private static final int WEBP_LOSSLESS_ALPHA_FLAG = 8;
  
  static {
    BYTES_PER_FORMAT = new int[] { 
        0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 
        8, 4, 8 };
  }
  
  private static int calcTagOffset(int paramInt1, int paramInt2) {
    return paramInt1 + 2 + paramInt2 * 12;
  }
  
  private int getOrientation(Reader paramReader, ArrayPool paramArrayPool) throws IOException {
    int i = paramReader.getUInt16();
    if (!handles(i)) {
      if (Log.isLoggable("DfltImageHeaderParser", 3)) {
        null = new StringBuilder();
        null.append("Parser doesn't handle magic number: ");
        null.append(i);
        Log.d("DfltImageHeaderParser", null.toString());
      } 
      return -1;
    } 
    i = moveToExifSegmentAndGetLength((Reader)null);
    if (i == -1) {
      if (Log.isLoggable("DfltImageHeaderParser", 3))
        Log.d("DfltImageHeaderParser", "Failed to parse exif segment length, or exif segment not found"); 
      return -1;
    } 
    byte[] arrayOfByte = (byte[])paramArrayPool.get(i, byte[].class);
    try {
      i = parseExifSegment((Reader)null, arrayOfByte, i);
      return i;
    } finally {
      paramArrayPool.put(arrayOfByte);
    } 
  }
  
  private ImageHeaderParser.ImageType getType(Reader paramReader) throws IOException {
    ImageHeaderParser.ImageType imageType;
    int i = paramReader.getUInt16();
    if (i == 65496)
      return ImageHeaderParser.ImageType.JPEG; 
    i = i << 16 & 0xFFFF0000 | paramReader.getUInt16() & 0xFFFF;
    if (i == -1991225785) {
      paramReader.skip(21L);
      if (paramReader.getByte() >= 3) {
        imageType = ImageHeaderParser.ImageType.PNG_A;
      } else {
        imageType = ImageHeaderParser.ImageType.PNG;
      } 
      return imageType;
    } 
    if (i >> 8 == 4671814)
      return ImageHeaderParser.ImageType.GIF; 
    if (i != 1380533830)
      return ImageHeaderParser.ImageType.UNKNOWN; 
    imageType.skip(4L);
    if ((imageType.getUInt16() << 16 & 0xFFFF0000 | imageType.getUInt16() & 0xFFFF) != 1464156752)
      return ImageHeaderParser.ImageType.UNKNOWN; 
    i = imageType.getUInt16() << 16 & 0xFFFF0000 | imageType.getUInt16() & 0xFFFF;
    if ((i & 0xFFFFFF00) != 1448097792)
      return ImageHeaderParser.ImageType.UNKNOWN; 
    i &= 0xFF;
    if (i == 88) {
      imageType.skip(4L);
      if ((imageType.getByte() & 0x10) != 0) {
        imageType = ImageHeaderParser.ImageType.WEBP_A;
      } else {
        imageType = ImageHeaderParser.ImageType.WEBP;
      } 
      return imageType;
    } 
    if (i == 76) {
      imageType.skip(4L);
      if ((imageType.getByte() & 0x8) != 0) {
        imageType = ImageHeaderParser.ImageType.WEBP_A;
      } else {
        imageType = ImageHeaderParser.ImageType.WEBP;
      } 
      return imageType;
    } 
    return ImageHeaderParser.ImageType.WEBP;
  }
  
  private static boolean handles(int paramInt) {
    return ((paramInt & 0xFFD8) == 65496 || paramInt == 19789 || paramInt == 18761);
  }
  
  private boolean hasJpegExifPreamble(byte[] paramArrayOfbyte, int paramInt) {
    boolean bool1;
    if (paramArrayOfbyte != null && paramInt > JPEG_EXIF_SEGMENT_PREAMBLE_BYTES.length) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    boolean bool2 = bool1;
    if (bool1) {
      paramInt = 0;
      while (true) {
        byte[] arrayOfByte = JPEG_EXIF_SEGMENT_PREAMBLE_BYTES;
        bool2 = bool1;
        if (paramInt < arrayOfByte.length) {
          if (paramArrayOfbyte[paramInt] != arrayOfByte[paramInt]) {
            bool2 = false;
            break;
          } 
          paramInt++;
          continue;
        } 
        break;
      } 
    } 
    return bool2;
  }
  
  private int moveToExifSegmentAndGetLength(Reader paramReader) throws IOException {
    while (true) {
      StringBuilder stringBuilder;
      short s = paramReader.getUInt8();
      if (s != 255) {
        if (Log.isLoggable("DfltImageHeaderParser", 3)) {
          stringBuilder = new StringBuilder();
          stringBuilder.append("Unknown segmentId=");
          stringBuilder.append(s);
          Log.d("DfltImageHeaderParser", stringBuilder.toString());
        } 
        return -1;
      } 
      s = stringBuilder.getUInt8();
      if (s == 218)
        return -1; 
      if (s == 217) {
        if (Log.isLoggable("DfltImageHeaderParser", 3))
          Log.d("DfltImageHeaderParser", "Found MARKER_EOI in exif segment"); 
        return -1;
      } 
      int i = stringBuilder.getUInt16() - 2;
      if (s != 225) {
        long l1 = i;
        long l2 = stringBuilder.skip(l1);
        if (l2 != l1) {
          if (Log.isLoggable("DfltImageHeaderParser", 3)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to skip enough data, type: ");
            stringBuilder.append(s);
            stringBuilder.append(", wanted to skip: ");
            stringBuilder.append(i);
            stringBuilder.append(", but actually skipped: ");
            stringBuilder.append(l2);
            Log.d("DfltImageHeaderParser", stringBuilder.toString());
          } 
          return -1;
        } 
        continue;
      } 
      return i;
    } 
  }
  
  private static int parseExifSegment(RandomAccessReader paramRandomAccessReader) {
    ByteOrder byteOrder;
    short s1 = paramRandomAccessReader.getInt16(6);
    if (s1 != 18761) {
      if (s1 != 19789) {
        if (Log.isLoggable("DfltImageHeaderParser", 3)) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Unknown endianness = ");
          stringBuilder.append(s1);
          Log.d("DfltImageHeaderParser", stringBuilder.toString());
        } 
        byteOrder = ByteOrder.BIG_ENDIAN;
      } else {
        byteOrder = ByteOrder.BIG_ENDIAN;
      } 
    } else {
      byteOrder = ByteOrder.LITTLE_ENDIAN;
    } 
    paramRandomAccessReader.order(byteOrder);
    int i = paramRandomAccessReader.getInt32(10) + 6;
    short s2 = paramRandomAccessReader.getInt16(i);
    for (s1 = 0; s1 < s2; s1++) {
      int j = calcTagOffset(i, s1);
      short s = paramRandomAccessReader.getInt16(j);
      if (s == 274) {
        short s3 = paramRandomAccessReader.getInt16(j + 2);
        if (s3 < 1 || s3 > 12) {
          if (Log.isLoggable("DfltImageHeaderParser", 3)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Got invalid format code = ");
            stringBuilder.append(s3);
            Log.d("DfltImageHeaderParser", stringBuilder.toString());
          } 
        } else {
          int k = paramRandomAccessReader.getInt32(j + 4);
          if (k < 0) {
            if (Log.isLoggable("DfltImageHeaderParser", 3))
              Log.d("DfltImageHeaderParser", "Negative tiff component count"); 
          } else {
            if (Log.isLoggable("DfltImageHeaderParser", 3)) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("Got tagIndex=");
              stringBuilder.append(s1);
              stringBuilder.append(" tagType=");
              stringBuilder.append(s);
              stringBuilder.append(" formatCode=");
              stringBuilder.append(s3);
              stringBuilder.append(" componentCount=");
              stringBuilder.append(k);
              Log.d("DfltImageHeaderParser", stringBuilder.toString());
            } 
            k += BYTES_PER_FORMAT[s3];
            if (k > 4) {
              if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Got byte count > 4, not orientation, continuing, formatCode=");
                stringBuilder.append(s3);
                Log.d("DfltImageHeaderParser", stringBuilder.toString());
              } 
            } else {
              int m = j + 8;
              if (m < 0 || m > paramRandomAccessReader.length()) {
                if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                  StringBuilder stringBuilder = new StringBuilder();
                  stringBuilder.append("Illegal tagValueOffset=");
                  stringBuilder.append(m);
                  stringBuilder.append(" tagType=");
                  stringBuilder.append(s);
                  Log.d("DfltImageHeaderParser", stringBuilder.toString());
                } 
              } else if (k < 0 || k + m > paramRandomAccessReader.length()) {
                if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                  StringBuilder stringBuilder = new StringBuilder();
                  stringBuilder.append("Illegal number of bytes for TI tag data tagType=");
                  stringBuilder.append(s);
                  Log.d("DfltImageHeaderParser", stringBuilder.toString());
                } 
              } else {
                return paramRandomAccessReader.getInt16(m);
              } 
            } 
          } 
        } 
      } 
    } 
    return -1;
  }
  
  private int parseExifSegment(Reader paramReader, byte[] paramArrayOfbyte, int paramInt) throws IOException {
    int i = paramReader.read(paramArrayOfbyte, paramInt);
    if (i != paramInt) {
      if (Log.isLoggable("DfltImageHeaderParser", 3)) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to read exif segment data, length: ");
        stringBuilder.append(paramInt);
        stringBuilder.append(", actually read: ");
        stringBuilder.append(i);
        Log.d("DfltImageHeaderParser", stringBuilder.toString());
      } 
      return -1;
    } 
    if (hasJpegExifPreamble(paramArrayOfbyte, paramInt))
      return parseExifSegment(new RandomAccessReader(paramArrayOfbyte, paramInt)); 
    if (Log.isLoggable("DfltImageHeaderParser", 3))
      Log.d("DfltImageHeaderParser", "Missing jpeg exif preamble"); 
    return -1;
  }
  
  public int getOrientation(InputStream paramInputStream, ArrayPool paramArrayPool) throws IOException {
    return getOrientation(new StreamReader((InputStream)Preconditions.checkNotNull(paramInputStream)), (ArrayPool)Preconditions.checkNotNull(paramArrayPool));
  }
  
  public int getOrientation(ByteBuffer paramByteBuffer, ArrayPool paramArrayPool) throws IOException {
    return getOrientation(new ByteBufferReader((ByteBuffer)Preconditions.checkNotNull(paramByteBuffer)), (ArrayPool)Preconditions.checkNotNull(paramArrayPool));
  }
  
  public ImageHeaderParser.ImageType getType(InputStream paramInputStream) throws IOException {
    return getType(new StreamReader((InputStream)Preconditions.checkNotNull(paramInputStream)));
  }
  
  public ImageHeaderParser.ImageType getType(ByteBuffer paramByteBuffer) throws IOException {
    return getType(new ByteBufferReader((ByteBuffer)Preconditions.checkNotNull(paramByteBuffer)));
  }
  
  private static final class ByteBufferReader implements Reader {
    private final ByteBuffer byteBuffer;
    
    ByteBufferReader(ByteBuffer param1ByteBuffer) {
      this.byteBuffer = param1ByteBuffer;
      param1ByteBuffer.order(ByteOrder.BIG_ENDIAN);
    }
    
    public int getByte() {
      return (this.byteBuffer.remaining() < 1) ? -1 : this.byteBuffer.get();
    }
    
    public int getUInt16() {
      return getByte() << 8 & 0xFF00 | getByte() & 0xFF;
    }
    
    public short getUInt8() {
      return (short)(getByte() & 0xFF);
    }
    
    public int read(byte[] param1ArrayOfbyte, int param1Int) {
      param1Int = Math.min(param1Int, this.byteBuffer.remaining());
      if (param1Int == 0)
        return -1; 
      this.byteBuffer.get(param1ArrayOfbyte, 0, param1Int);
      return param1Int;
    }
    
    public long skip(long param1Long) {
      int i = (int)Math.min(this.byteBuffer.remaining(), param1Long);
      ByteBuffer byteBuffer = this.byteBuffer;
      byteBuffer.position(byteBuffer.position() + i);
      return i;
    }
  }
  
  private static final class RandomAccessReader {
    private final ByteBuffer data;
    
    RandomAccessReader(byte[] param1ArrayOfbyte, int param1Int) {
      this.data = (ByteBuffer)ByteBuffer.wrap(param1ArrayOfbyte).order(ByteOrder.BIG_ENDIAN).limit(param1Int);
    }
    
    private boolean isAvailable(int param1Int1, int param1Int2) {
      boolean bool;
      if (this.data.remaining() - param1Int1 >= param1Int2) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    short getInt16(int param1Int) {
      int i;
      if (isAvailable(param1Int, 2)) {
        param1Int = this.data.getShort(param1Int);
        i = param1Int;
      } else {
        param1Int = -1;
        i = param1Int;
      } 
      return i;
    }
    
    int getInt32(int param1Int) {
      if (isAvailable(param1Int, 4)) {
        param1Int = this.data.getInt(param1Int);
      } else {
        param1Int = -1;
      } 
      return param1Int;
    }
    
    int length() {
      return this.data.remaining();
    }
    
    void order(ByteOrder param1ByteOrder) {
      this.data.order(param1ByteOrder);
    }
  }
  
  private static interface Reader {
    int getByte() throws IOException;
    
    int getUInt16() throws IOException;
    
    short getUInt8() throws IOException;
    
    int read(byte[] param1ArrayOfbyte, int param1Int) throws IOException;
    
    long skip(long param1Long) throws IOException;
  }
  
  private static final class StreamReader implements Reader {
    private final InputStream is;
    
    StreamReader(InputStream param1InputStream) {
      this.is = param1InputStream;
    }
    
    public int getByte() throws IOException {
      return this.is.read();
    }
    
    public int getUInt16() throws IOException {
      return this.is.read() << 8 & 0xFF00 | this.is.read() & 0xFF;
    }
    
    public short getUInt8() throws IOException {
      return (short)(this.is.read() & 0xFF);
    }
    
    public int read(byte[] param1ArrayOfbyte, int param1Int) throws IOException {
      int i = param1Int;
      while (i > 0) {
        int j = this.is.read(param1ArrayOfbyte, param1Int - i, i);
        if (j != -1)
          i -= j; 
      } 
      return param1Int - i;
    }
    
    public long skip(long param1Long) throws IOException {
      if (param1Long < 0L)
        return 0L; 
      long l;
      for (l = param1Long; l > 0L; l -= l1) {
        long l1 = this.is.skip(l);
        if (l1 <= 0L) {
          if (this.is.read() == -1)
            break; 
          l1 = 1L;
        } 
      } 
      return param1Long - l;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/bitmap/DefaultImageHeaderParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
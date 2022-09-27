package tv.danmaku.ijk.media.player_gx.javaprotocols;

import java.nio.ByteBuffer;

public class JavaUrlContext {
  static final int SIZE = 28;
  
  private volatile ByteBuffer dataConduit = ByteBuffer.allocateDirect(524288);
  
  public final ByteBuffer entityDirectBuffer = ByteBuffer.allocateDirect(28);
  
  private boolean lock = true;
  
  final Operator operator = new Operator();
  
  private Object privateObject = null;
  
  private static int byteArrayToInt(byte[] paramArrayOfbyte, int paramInt) {
    byte b1 = paramArrayOfbyte[paramInt + 3];
    byte b2 = paramArrayOfbyte[paramInt + 2];
    byte b3 = paramArrayOfbyte[paramInt + 1];
    return (paramArrayOfbyte[paramInt] & 0xFF) << 24 | b1 & 0xFF | (b2 & 0xFF) << 8 | (b3 & 0xFF) << 16;
  }
  
  private static void intToByteArray(int paramInt1, byte[] paramArrayOfbyte, int paramInt2) {
    paramArrayOfbyte[paramInt2] = (byte)(byte)(paramInt1 >> 24 & 0xFF);
    paramArrayOfbyte[paramInt2 + 1] = (byte)(byte)(paramInt1 >> 16 & 0xFF);
    paramArrayOfbyte[paramInt2 + 2] = (byte)(byte)(paramInt1 >> 8 & 0xFF);
    paramArrayOfbyte[paramInt2 + 3] = (byte)(byte)(paramInt1 & 0xFF);
  }
  
  private static void longToByteArray(long paramLong, byte[] paramArrayOfbyte, int paramInt) {
    paramArrayOfbyte[paramInt] = (byte)(byte)(int)(paramLong >> 56L & 0xFFL);
    paramArrayOfbyte[paramInt + 1] = (byte)(byte)(int)(paramLong >> 48L & 0xFFL);
    paramArrayOfbyte[paramInt + 2] = (byte)(byte)(int)(paramLong >> 40L & 0xFFL);
    paramArrayOfbyte[paramInt + 3] = (byte)(byte)(int)(paramLong >> 32L & 0xFFL);
    paramArrayOfbyte[paramInt + 4] = (byte)(byte)(int)(paramLong >> 24L & 0xFFL);
    paramArrayOfbyte[paramInt + 5] = (byte)(byte)(int)(paramLong >> 16L & 0xFFL);
    paramArrayOfbyte[paramInt + 6] = (byte)(byte)(int)(paramLong >> 8L & 0xFFL);
    paramArrayOfbyte[paramInt + 7] = (byte)(byte)(int)(paramLong & 0xFFL);
  }
  
  public ByteBuffer getDataConduit() {
    return this.dataConduit;
  }
  
  public Object getPrivateObject() {
    return this.privateObject;
  }
  
  public void lock() {
    this.lock = true;
  }
  
  public Operator operator() {
    this.entityDirectBuffer.rewind();
    this.entityDirectBuffer.get(this.operator.entity);
    return this.operator;
  }
  
  public void setPrivateObject(Object paramObject) {
    this.privateObject = paramObject;
  }
  
  public void unLock() {
    this.lock = false;
  }
  
  public class Operator {
    private static final int FLAGS_INDEX = 0;
    
    private static final int FORCE_FPS_INDEX = 24;
    
    private static final int IS_CONNECTED_INDEX = 12;
    
    private static final int IS_STREAMED_INDEX = 8;
    
    private static final int MAX_PACKET_SIZE_INDEX = 4;
    
    private static final int RW_TIMEOUT_INDEX = 16;
    
    public final byte[] entity = new byte[28];
    
    private Operator() {}
    
    public boolean commit() {
      JavaUrlContext.this.entityDirectBuffer.clear();
      if (JavaUrlContext.this.lock)
        return false; 
      ByteBuffer byteBuffer = JavaUrlContext.this.entityDirectBuffer;
      byte[] arrayOfByte = this.entity;
      byteBuffer.put(arrayOfByte, 0, arrayOfByte.length);
      return true;
    }
    
    public int getFlags() {
      return JavaUrlContext.byteArrayToInt(this.entity, 0);
    }
    
    public int getForceFPS() {
      return JavaUrlContext.byteArrayToInt(this.entity, 24);
    }
    
    public int getIsConnected() {
      return JavaUrlContext.byteArrayToInt(this.entity, 12);
    }
    
    public int getIsStreamed() {
      return JavaUrlContext.byteArrayToInt(this.entity, 8);
    }
    
    public int getMaxPacketSize() {
      return JavaUrlContext.byteArrayToInt(this.entity, 4);
    }
    
    public int getRwTimeout() {
      return JavaUrlContext.byteArrayToInt(this.entity, 16);
    }
    
    public Operator setFlags(int param1Int) {
      JavaUrlContext.intToByteArray(param1Int, this.entity, 0);
      return this;
    }
    
    public Operator setForceFPS(int param1Int) {
      JavaUrlContext.intToByteArray(param1Int, this.entity, 24);
      return this;
    }
    
    public Operator setIsConnected(int param1Int) {
      JavaUrlContext.intToByteArray(param1Int, this.entity, 12);
      return this;
    }
    
    public Operator setIsStreamed(int param1Int) {
      JavaUrlContext.intToByteArray(param1Int, this.entity, 8);
      return this;
    }
    
    public Operator setMaxPacketSize(int param1Int) {
      JavaUrlContext.access$302(JavaUrlContext.this, ByteBuffer.allocateDirect(param1Int));
      JavaUrlContext.intToByteArray(param1Int, this.entity, 4);
      return this;
    }
    
    public Operator setRwTimeout(int param1Int) {
      JavaUrlContext.intToByteArray(param1Int, this.entity, 16);
      return this;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/tv/danmaku/ijk/media/player_gx/javaprotocols/JavaUrlContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
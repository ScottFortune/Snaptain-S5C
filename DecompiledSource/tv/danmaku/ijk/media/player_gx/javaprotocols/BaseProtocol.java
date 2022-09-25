package tv.danmaku.ijk.media.player_gx.javaprotocols;

import android.util.Log;
import java.nio.ByteBuffer;
import tv.danmaku.ijk.media.player_gx.IjkMediaPlayer;

public abstract class BaseProtocol {
  public static final int AVSEEK_SIZE = 65536;
  
  public static final int EAGAIN = -11;
  
  public static final int EOF = FFERRORTAG('E', 'O', 'F', ' ');
  
  public static final int INPUT_OUTPUT_ERROR = -5;
  
  public static final int NETWORK_IS_UNREACHABLE = -101;
  
  public final String default_whitelist;
  
  public final String name;
  
  public BaseProtocol(String paramString1, String paramString2) {
    this.name = paramString1;
    this.default_whitelist = paramString2;
  }
  
  protected static int FFERRORTAG(char paramChar1, char paramChar2, char paramChar3, char paramChar4) {
    return -((byte)paramChar1 | (byte)paramChar2 << 8 | (byte)paramChar3 << 16 | (byte)paramChar4 << 24);
  }
  
  public int jni_close(JavaUrlContext paramJavaUrlContext) {
    paramJavaUrlContext.unLock();
    int i = url_close(paramJavaUrlContext);
    paramJavaUrlContext.getDataConduit().clear();
    paramJavaUrlContext.lock();
    return i;
  }
  
  public int jni_open(JavaUrlContext paramJavaUrlContext, String paramString, int paramInt) {
    paramJavaUrlContext.unLock();
    paramInt = url_open(paramJavaUrlContext, paramString, paramInt);
    paramJavaUrlContext.lock();
    return paramInt;
  }
  
  public int jni_read(JavaUrlContext paramJavaUrlContext, long paramLong, int paramInt) {
    paramJavaUrlContext.unLock();
    int i = url_read(paramJavaUrlContext, paramJavaUrlContext.getDataConduit(), paramInt);
    if (i > 0 && paramInt >= i)
      IjkMediaPlayer._writeBuf(paramJavaUrlContext.getDataConduit(), paramLong, i); 
    paramJavaUrlContext.lock();
    if (paramInt < i) {
      Log.e("Error", "read length is greater than size");
      return -11;
    } 
    return i;
  }
  
  public long jni_seek(JavaUrlContext paramJavaUrlContext, long paramLong, int paramInt) {
    paramJavaUrlContext.unLock();
    paramLong = url_seek(paramJavaUrlContext, paramLong, paramInt);
    paramJavaUrlContext.lock();
    return paramLong;
  }
  
  protected abstract int url_close(JavaUrlContext paramJavaUrlContext);
  
  protected abstract int url_open(JavaUrlContext paramJavaUrlContext, String paramString, int paramInt);
  
  protected abstract int url_read(JavaUrlContext paramJavaUrlContext, ByteBuffer paramByteBuffer, int paramInt);
  
  protected abstract long url_seek(JavaUrlContext paramJavaUrlContext, long paramLong, int paramInt);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/tv/danmaku/ijk/media/player_gx/javaprotocols/BaseProtocol.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
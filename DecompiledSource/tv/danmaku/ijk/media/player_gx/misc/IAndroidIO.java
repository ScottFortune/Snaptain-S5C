package tv.danmaku.ijk.media.player_gx.misc;

import java.io.IOException;

public interface IAndroidIO {
  int close() throws IOException;
  
  int open(String paramString) throws IOException;
  
  int read(byte[] paramArrayOfbyte, int paramInt) throws IOException;
  
  long seek(long paramLong, int paramInt) throws IOException;
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/tv/danmaku/ijk/media/player_gx/misc/IAndroidIO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
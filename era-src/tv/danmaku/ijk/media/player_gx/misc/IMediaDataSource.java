package tv.danmaku.ijk.media.player_gx.misc;

import java.io.IOException;

public interface IMediaDataSource {
  void close() throws IOException;
  
  long getSize() throws IOException;
  
  int readAt(long paramLong, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException;
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/tv/danmaku/ijk/media/player_gx/misc/IMediaDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
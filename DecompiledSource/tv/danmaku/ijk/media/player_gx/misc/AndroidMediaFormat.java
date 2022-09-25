package tv.danmaku.ijk.media.player_gx.misc;

import android.media.MediaFormat;

public class AndroidMediaFormat implements IMediaFormat {
  private final MediaFormat mMediaFormat;
  
  public AndroidMediaFormat(MediaFormat paramMediaFormat) {
    this.mMediaFormat = paramMediaFormat;
  }
  
  public int getInteger(String paramString) {
    MediaFormat mediaFormat = this.mMediaFormat;
    return (mediaFormat == null) ? 0 : mediaFormat.getInteger(paramString);
  }
  
  public String getString(String paramString) {
    MediaFormat mediaFormat = this.mMediaFormat;
    return (mediaFormat == null) ? null : mediaFormat.getString(paramString);
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder(128);
    stringBuilder.append(getClass().getName());
    stringBuilder.append('{');
    MediaFormat mediaFormat = this.mMediaFormat;
    if (mediaFormat != null) {
      stringBuilder.append(mediaFormat.toString());
    } else {
      stringBuilder.append("null");
    } 
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/tv/danmaku/ijk/media/player_gx/misc/AndroidMediaFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
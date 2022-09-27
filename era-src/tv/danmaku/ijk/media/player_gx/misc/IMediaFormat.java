package tv.danmaku.ijk.media.player_gx.misc;

public interface IMediaFormat {
  public static final String KEY_HEIGHT = "height";
  
  public static final String KEY_MIME = "mime";
  
  public static final String KEY_WIDTH = "width";
  
  int getInteger(String paramString);
  
  String getString(String paramString);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/tv/danmaku/ijk/media/player_gx/misc/IMediaFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
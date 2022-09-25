package tv.danmaku.ijk.media.player_gx;

import android.graphics.Rect;

public final class IjkTimedText {
  private Rect mTextBounds = null;
  
  private String mTextChars = null;
  
  public IjkTimedText(Rect paramRect, String paramString) {
    this.mTextBounds = paramRect;
    this.mTextChars = paramString;
  }
  
  public Rect getBounds() {
    return this.mTextBounds;
  }
  
  public String getText() {
    return this.mTextChars;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/tv/danmaku/ijk/media/player_gx/IjkTimedText.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
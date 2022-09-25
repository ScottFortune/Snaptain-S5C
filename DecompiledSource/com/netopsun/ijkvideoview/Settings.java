package com.netopsun.ijkvideoview;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {
  public static final int PV_PLAYER__AndroidMediaPlayer = 1;
  
  public static final int PV_PLAYER__Auto = 0;
  
  public static final int PV_PLAYER__IjkExoMediaPlayer = 3;
  
  public static final int PV_PLAYER__IjkMediaPlayer = 2;
  
  private Context mAppContext;
  
  private SharedPreferences mSharedPreferences;
  
  public Settings(Context paramContext) {
    this.mAppContext = paramContext.getApplicationContext();
    this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mAppContext);
  }
  
  public boolean getEnableBackgroundPlay() {
    String str = this.mAppContext.getString(R.string.pref_key_enable_background_play);
    return this.mSharedPreferences.getBoolean(str, true);
  }
  
  public boolean getEnableDetachedSurfaceTextureView() {
    String str = this.mAppContext.getString(R.string.pref_key_enable_detached_surface_texture);
    return this.mSharedPreferences.getBoolean(str, false);
  }
  
  public boolean getEnableNoView() {
    String str = this.mAppContext.getString(R.string.pref_key_enable_no_view);
    return this.mSharedPreferences.getBoolean(str, true);
  }
  
  public boolean getEnableSurfaceView() {
    String str = this.mAppContext.getString(R.string.pref_key_enable_surface_view);
    return this.mSharedPreferences.getBoolean(str, false);
  }
  
  public boolean getEnableTextureView() {
    String str = this.mAppContext.getString(R.string.pref_key_enable_texture_view);
    return this.mSharedPreferences.getBoolean(str, false);
  }
  
  public String getLastDirectory() {
    String str = this.mAppContext.getString(R.string.pref_key_last_directory);
    return this.mSharedPreferences.getString(str, "/");
  }
  
  public boolean getMediaCodecHandleResolutionChange() {
    String str = this.mAppContext.getString(R.string.pref_key_media_codec_handle_resolution_change);
    return this.mSharedPreferences.getBoolean(str, true);
  }
  
  public String getPixelFormat() {
    String str = this.mAppContext.getString(R.string.pref_key_pixel_format);
    return this.mSharedPreferences.getString(str, "");
  }
  
  public int getPlayer() {
    String str = this.mAppContext.getString(R.string.pref_key_player);
    str = this.mSharedPreferences.getString(str, "");
    try {
      return Integer.valueOf(str).intValue();
    } catch (NumberFormatException numberFormatException) {
      return 0;
    } 
  }
  
  public boolean getUsingMediaCodec() {
    String str = this.mAppContext.getString(R.string.pref_key_using_media_codec);
    return this.mSharedPreferences.getBoolean(str, true);
  }
  
  public boolean getUsingMediaCodecAutoRotate() {
    String str = this.mAppContext.getString(R.string.pref_key_using_media_codec_auto_rotate);
    return this.mSharedPreferences.getBoolean(str, true);
  }
  
  public boolean getUsingMediaDataSource() {
    String str = this.mAppContext.getString(R.string.pref_key_using_mediadatasource);
    return this.mSharedPreferences.getBoolean(str, false);
  }
  
  public boolean getUsingOpenSLES() {
    String str = this.mAppContext.getString(R.string.pref_key_using_opensl_es);
    return this.mSharedPreferences.getBoolean(str, false);
  }
  
  public void setLastDirectory(String paramString) {
    String str = this.mAppContext.getString(R.string.pref_key_last_directory);
    this.mSharedPreferences.edit().putString(str, paramString).apply();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/Settings.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
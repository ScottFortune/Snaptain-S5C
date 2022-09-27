package com.guanxukeji.videobackgroundmusicpicker;

import android.content.Context;
import android.os.Environment;
import java.io.File;

public class SharedPreferenceHelper {
  private static final String TABLENAME = "video_background_music_saveting";
  
  private static final String backgroundMusicFileMD5Tag = "backgroundMusicFileMD5Tag";
  
  private static final String backgroundMusicNameTag = "backgroundMusicNameTag";
  
  private static final String endTimeTag = "endTimeTag";
  
  private static final String isOpenBackgroundMusicTag = "isOpenBackgroundMusicTag";
  
  private static final String startTimeTag = "startTimeTag";
  
  public static String getAssetMusicCopyBasePath(Context paramContext) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(Environment.getExternalStorageDirectory());
    stringBuilder.append(File.separator);
    stringBuilder.append(paramContext.getPackageName());
    stringBuilder.append(File.separator);
    return stringBuilder.toString();
  }
  
  public static String getBackgroundMusicFileMD5(Context paramContext) {
    return paramContext.getSharedPreferences("video_background_music_saveting", 0).getString("backgroundMusicFileMD5Tag", "");
  }
  
  public static String getBackgroundMusicFilePath(Context paramContext) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(Environment.getExternalStorageDirectory());
    stringBuilder.append(File.separator);
    stringBuilder.append(paramContext.getPackageName());
    stringBuilder.append("/bgm/rec_video_bgm.m4a");
    return stringBuilder.toString();
  }
  
  public static String getBackgroundMusicName(Context paramContext) {
    return paramContext.getSharedPreferences("video_background_music_saveting", 0).getString("backgroundMusicNameTag", "");
  }
  
  public static String getEndTime(Context paramContext) {
    return paramContext.getSharedPreferences("video_background_music_saveting", 0).getString("endTimeTag", "00:00");
  }
  
  public static String getStartTime(Context paramContext) {
    return paramContext.getSharedPreferences("video_background_music_saveting", 0).getString("startTimeTag", "00:00");
  }
  
  public static boolean isOpenBackgroundMusic(Context paramContext) {
    return paramContext.getSharedPreferences("video_background_music_saveting", 0).getBoolean("isOpenBackgroundMusicTag", false);
  }
  
  public static void saveBackgroundMusicFileMD5(Context paramContext, String paramString) {
    paramContext.getSharedPreferences("video_background_music_saveting", 0).edit().putString("backgroundMusicFileMD5Tag", paramString).commit();
  }
  
  public static void saveBackgroundMusicName(Context paramContext, String paramString) {
    paramContext.getSharedPreferences("video_background_music_saveting", 0).edit().putString("backgroundMusicNameTag", paramString).commit();
  }
  
  public static void saveEndTime(Context paramContext, String paramString) {
    paramContext.getSharedPreferences("video_background_music_saveting", 0).edit().putString("endTimeTag", paramString).commit();
  }
  
  public static void saveOpenBackgroundMusic(Context paramContext, boolean paramBoolean) {
    paramContext.getSharedPreferences("video_background_music_saveting", 0).edit().putBoolean("isOpenBackgroundMusicTag", paramBoolean).commit();
  }
  
  public static void saveStartTime(Context paramContext, String paramString) {
    paramContext.getSharedPreferences("video_background_music_saveting", 0).edit().putString("startTimeTag", paramString).commit();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/guanxukeji/videobackgroundmusicpicker/SharedPreferenceHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
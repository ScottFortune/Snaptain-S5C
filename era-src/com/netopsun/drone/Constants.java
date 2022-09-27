package com.netopsun.drone;

import android.content.Context;
import android.os.Environment;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Constants {
  public static String generatePicName(String paramString) {
    File file = new File(paramString);
    if (!file.exists())
      file.mkdirs(); 
    Date date = new Date();
    int i = (new Random()).nextInt(10000);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString);
    stringBuilder.append("pic_");
    stringBuilder.append((new SimpleDateFormat("yyyyMMdd_HHmmss")).format(date));
    stringBuilder.append("_");
    stringBuilder.append(i);
    stringBuilder.append(".jpg");
    return stringBuilder.toString();
  }
  
  public static String generateVideoName(String paramString) {
    File file = new File(paramString);
    if (!file.exists())
      file.mkdirs(); 
    Date date = new Date();
    int i = (new Random()).nextInt(10000);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString);
    stringBuilder.append("video_");
    stringBuilder.append((new SimpleDateFormat("yyyyMMdd_HHmmss")).format(date));
    stringBuilder.append("_");
    stringBuilder.append(i);
    stringBuilder.append(".mp4");
    return stringBuilder.toString();
  }
  
  public static boolean getIs1080PPreview(Context paramContext) {
    return paramContext.getSharedPreferences("table", 0).getBoolean("is1080p", false);
  }
  
  public static boolean getIs720PPreview(Context paramContext) {
    return paramContext.getSharedPreferences("table", 0).getBoolean("is720p", true);
  }
  
  public static boolean getIsAutoSaveParams(Context paramContext) {
    return paramContext.getSharedPreferences("table", 0).getBoolean("is_auto_save_params", true);
  }
  
  public static boolean getIsGravitySensorRotatingScreen(Context paramContext) {
    return paramContext.getSharedPreferences("table", 0).getBoolean("is_gravity_sensor_rotating_screen", false);
  }
  
  public static boolean getIsRightHandMode(Context paramContext) {
    return paramContext.getSharedPreferences("table", 0).getBoolean("is_right_hand_mode", false);
  }
  
  public static boolean getIsRotateDegree180(Context paramContext) {
    return paramContext.getSharedPreferences("table", 0).getBoolean("rotate_180", true);
  }
  
  public static boolean getIsRotateVideo(Context paramContext) {
    return paramContext.getSharedPreferences("table", 0).getBoolean("is_rotate_video", false);
  }
  
  public static String getPhotoPath(Context paramContext) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(Environment.getExternalStorageDirectory());
    stringBuilder.append(File.separator);
    stringBuilder.append("Snaptain_Era");
    stringBuilder.append(File.separator);
    stringBuilder.append("UFO_Photo");
    stringBuilder.append(File.separator);
    return stringBuilder.toString();
  }
  
  public static int getPitchSliderLevel(Context paramContext) {
    return paramContext.getSharedPreferences("table", 0).getInt("pitch_slider_level", 0);
  }
  
  public static int getRollSliderLevel(Context paramContext) {
    return paramContext.getSharedPreferences("table", 0).getInt("roll_slider_level", 0);
  }
  
  public static String getSelectedModel(Context paramContext) {
    return paramContext.getSharedPreferences("table", 0).getString("selected_model", "");
  }
  
  public static String getVideoPath(Context paramContext) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(Environment.getExternalStorageDirectory());
    stringBuilder.append(File.separator);
    stringBuilder.append("Snaptain_Era");
    stringBuilder.append(File.separator);
    stringBuilder.append("UFO_Video");
    stringBuilder.append(File.separator);
    return stringBuilder.toString();
  }
  
  public static int getYawSliderLevel(Context paramContext) {
    return paramContext.getSharedPreferences("table", 0).getInt("yaw_slider_level", 0);
  }
  
  public static void setIs1080PPreview(Context paramContext, boolean paramBoolean) {
    paramContext.getSharedPreferences("table", 0).edit().putBoolean("is1080p", paramBoolean).commit();
  }
  
  public static void setIs720PPreview(Context paramContext, boolean paramBoolean) {
    paramContext.getSharedPreferences("table", 0).edit().putBoolean("is720p", paramBoolean).commit();
  }
  
  public static void setIsAutoSaveParams(Context paramContext, boolean paramBoolean) {
    paramContext.getSharedPreferences("table", 0).edit().putBoolean("is_auto_save_params", paramBoolean).commit();
  }
  
  public static void setIsGravitySensorRotatingScreen(Context paramContext, boolean paramBoolean) {
    paramContext.getSharedPreferences("table", 0).edit().putBoolean("is_gravity_sensor_rotating_screen", paramBoolean).commit();
  }
  
  public static void setIsRightHandMode(Context paramContext, boolean paramBoolean) {
    paramContext.getSharedPreferences("table", 0).edit().putBoolean("is_right_hand_mode", paramBoolean).commit();
  }
  
  public static void setIsRotateDegree180(Context paramContext, boolean paramBoolean) {
    paramContext.getSharedPreferences("table", 0).edit().putBoolean("rotate_180", paramBoolean).commit();
  }
  
  public static void setIsRotateVideo(Context paramContext, boolean paramBoolean) {
    paramContext.getSharedPreferences("table", 0).edit().putBoolean("is_rotate_video", paramBoolean).commit();
  }
  
  public static void setPitchSliderLevel(Context paramContext, int paramInt) {
    paramContext.getSharedPreferences("table", 0).edit().putInt("pitch_slider_level", paramInt).commit();
  }
  
  public static void setRollSliderLevel(Context paramContext, int paramInt) {
    paramContext.getSharedPreferences("table", 0).edit().putInt("roll_slider_level", paramInt).commit();
  }
  
  public static void setSelectedModel(Context paramContext, String paramString) {
    paramContext.getSharedPreferences("table", 0).edit().putString("selected_model", paramString).commit();
  }
  
  public static void setYawSliderLevel(Context paramContext, int paramInt) {
    paramContext.getSharedPreferences("table", 0).edit().putInt("yaw_slider_level", paramInt).commit();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/drone/Constants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
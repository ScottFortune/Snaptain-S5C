package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import java.io.File;

public final class HardwareConfigState {
  public static final int DEFAULT_MAXIMUM_FDS_FOR_HARDWARE_CONFIGS = 700;
  
  public static final int DEFAULT_MIN_HARDWARE_DIMENSION = 128;
  
  private static final File FD_SIZE_LIST = new File("/proc/self/fd");
  
  private static final int MINIMUM_DECODES_BETWEEN_FD_CHECKS = 50;
  
  private static volatile int fdSizeLimit = 700;
  
  private static volatile HardwareConfigState instance;
  
  private static volatile int minHardwareDimension = 128;
  
  private int decodesSinceLastFdCheck;
  
  private boolean isFdSizeBelowHardwareLimit = true;
  
  private final boolean isHardwareConfigAllowedByDeviceModel = isHardwareConfigAllowedByDeviceModel();
  
  public static HardwareConfigState getInstance() {
    // Byte code:
    //   0: getstatic com/bumptech/glide/load/resource/bitmap/HardwareConfigState.instance : Lcom/bumptech/glide/load/resource/bitmap/HardwareConfigState;
    //   3: ifnonnull -> 39
    //   6: ldc com/bumptech/glide/load/resource/bitmap/HardwareConfigState
    //   8: monitorenter
    //   9: getstatic com/bumptech/glide/load/resource/bitmap/HardwareConfigState.instance : Lcom/bumptech/glide/load/resource/bitmap/HardwareConfigState;
    //   12: ifnonnull -> 27
    //   15: new com/bumptech/glide/load/resource/bitmap/HardwareConfigState
    //   18: astore_0
    //   19: aload_0
    //   20: invokespecial <init> : ()V
    //   23: aload_0
    //   24: putstatic com/bumptech/glide/load/resource/bitmap/HardwareConfigState.instance : Lcom/bumptech/glide/load/resource/bitmap/HardwareConfigState;
    //   27: ldc com/bumptech/glide/load/resource/bitmap/HardwareConfigState
    //   29: monitorexit
    //   30: goto -> 39
    //   33: astore_0
    //   34: ldc com/bumptech/glide/load/resource/bitmap/HardwareConfigState
    //   36: monitorexit
    //   37: aload_0
    //   38: athrow
    //   39: getstatic com/bumptech/glide/load/resource/bitmap/HardwareConfigState.instance : Lcom/bumptech/glide/load/resource/bitmap/HardwareConfigState;
    //   42: areturn
    // Exception table:
    //   from	to	target	type
    //   9	27	33	finally
    //   27	30	33	finally
    //   34	37	33	finally
  }
  
  private boolean isFdSizeBelowHardwareLimit() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield decodesSinceLastFdCheck : I
    //   6: iconst_1
    //   7: iadd
    //   8: istore_1
    //   9: aload_0
    //   10: iload_1
    //   11: putfield decodesSinceLastFdCheck : I
    //   14: iload_1
    //   15: bipush #50
    //   17: if_icmplt -> 111
    //   20: iconst_0
    //   21: istore_2
    //   22: aload_0
    //   23: iconst_0
    //   24: putfield decodesSinceLastFdCheck : I
    //   27: getstatic com/bumptech/glide/load/resource/bitmap/HardwareConfigState.FD_SIZE_LIST : Ljava/io/File;
    //   30: invokevirtual list : ()[Ljava/lang/String;
    //   33: arraylength
    //   34: istore_1
    //   35: iload_1
    //   36: getstatic com/bumptech/glide/load/resource/bitmap/HardwareConfigState.fdSizeLimit : I
    //   39: if_icmpge -> 44
    //   42: iconst_1
    //   43: istore_2
    //   44: aload_0
    //   45: iload_2
    //   46: putfield isFdSizeBelowHardwareLimit : Z
    //   49: aload_0
    //   50: getfield isFdSizeBelowHardwareLimit : Z
    //   53: ifne -> 111
    //   56: ldc 'Downsampler'
    //   58: iconst_5
    //   59: invokestatic isLoggable : (Ljava/lang/String;I)Z
    //   62: ifeq -> 111
    //   65: new java/lang/StringBuilder
    //   68: astore_3
    //   69: aload_3
    //   70: invokespecial <init> : ()V
    //   73: aload_3
    //   74: ldc 'Excluding HARDWARE bitmap config because we're over the file descriptor limit, file descriptors '
    //   76: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   79: pop
    //   80: aload_3
    //   81: iload_1
    //   82: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   85: pop
    //   86: aload_3
    //   87: ldc ', limit '
    //   89: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   92: pop
    //   93: aload_3
    //   94: getstatic com/bumptech/glide/load/resource/bitmap/HardwareConfigState.fdSizeLimit : I
    //   97: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   100: pop
    //   101: ldc 'Downsampler'
    //   103: aload_3
    //   104: invokevirtual toString : ()Ljava/lang/String;
    //   107: invokestatic w : (Ljava/lang/String;Ljava/lang/String;)I
    //   110: pop
    //   111: aload_0
    //   112: getfield isFdSizeBelowHardwareLimit : Z
    //   115: istore_2
    //   116: aload_0
    //   117: monitorexit
    //   118: iload_2
    //   119: ireturn
    //   120: astore_3
    //   121: aload_0
    //   122: monitorexit
    //   123: aload_3
    //   124: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	120	finally
    //   22	35	120	finally
    //   35	42	120	finally
    //   44	111	120	finally
    //   111	116	120	finally
  }
  
  private static boolean isHardwareConfigAllowedByDeviceModel() {
    String str = Build.MODEL;
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (str != null)
      if (Build.MODEL.length() < 7) {
        bool2 = bool1;
      } else {
        byte b;
        str = Build.MODEL.substring(0, 7);
        switch (str.hashCode()) {
          default:
            b = -1;
            break;
          case -1398222624:
            if (str.equals("SM-N935")) {
              b = 0;
              break;
            } 
          case -1398343746:
            if (str.equals("SM-J720")) {
              b = 1;
              break;
            } 
          case -1398431068:
            if (str.equals("SM-G965")) {
              b = 3;
              break;
            } 
          case -1398431073:
            if (str.equals("SM-G960")) {
              b = 2;
              break;
            } 
          case -1398431161:
            if (str.equals("SM-G935")) {
              b = 4;
              break;
            } 
          case -1398431166:
            if (str.equals("SM-G930")) {
              b = 5;
              break;
            } 
          case -1398613787:
            if (str.equals("SM-A520")) {
              b = 6;
              break;
            } 
        } 
        switch (b) {
          default:
            return true;
          case 0:
          case 1:
          case 2:
          case 3:
          case 4:
          case 5:
          case 6:
            break;
        } 
        if (Build.VERSION.SDK_INT != 26) {
          bool2 = bool1;
        } else {
          bool2 = false;
        } 
      }  
    return bool2;
  }
  
  public boolean isHardwareConfigAllowed(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2) {
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (paramBoolean1) {
      bool2 = bool1;
      if (this.isHardwareConfigAllowedByDeviceModel) {
        bool2 = bool1;
        if (Build.VERSION.SDK_INT >= 26)
          if (paramBoolean2) {
            bool2 = bool1;
          } else {
            bool2 = bool1;
            if (paramInt1 >= minHardwareDimension) {
              bool2 = bool1;
              if (paramInt2 >= minHardwareDimension) {
                bool2 = bool1;
                if (isFdSizeBelowHardwareLimit())
                  bool2 = true; 
              } 
            } 
          }  
      } 
    } 
    return bool2;
  }
  
  boolean setHardwareConfigIfAllowed(int paramInt1, int paramInt2, BitmapFactory.Options paramOptions, boolean paramBoolean1, boolean paramBoolean2) {
    paramBoolean1 = isHardwareConfigAllowed(paramInt1, paramInt2, paramBoolean1, paramBoolean2);
    if (paramBoolean1) {
      paramOptions.inPreferredConfig = Bitmap.Config.HARDWARE;
      paramOptions.inMutable = false;
    } 
    return paramBoolean1;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/bitmap/HardwareConfigState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
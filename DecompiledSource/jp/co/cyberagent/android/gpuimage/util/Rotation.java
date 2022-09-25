package jp.co.cyberagent.android.gpuimage.util;

public enum Rotation {
  NORMAL, ROTATION_180, ROTATION_270, ROTATION_90;
  
  static {
    ROTATION_180 = new Rotation("ROTATION_180", 2);
    ROTATION_270 = new Rotation("ROTATION_270", 3);
    $VALUES = new Rotation[] { NORMAL, ROTATION_90, ROTATION_180, ROTATION_270 };
  }
  
  public static Rotation fromInt(int paramInt) {
    if (paramInt != 0) {
      if (paramInt != 90) {
        if (paramInt != 180) {
          if (paramInt != 270) {
            if (paramInt == 360)
              return NORMAL; 
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(paramInt);
            stringBuilder.append(" is an unknown rotation. Needs to be either 0, 90, 180 or 270!");
            throw new IllegalStateException(stringBuilder.toString());
          } 
          return ROTATION_270;
        } 
        return ROTATION_180;
      } 
      return ROTATION_90;
    } 
    return NORMAL;
  }
  
  public int asInt() {
    int i = null.$SwitchMap$jp$co$cyberagent$android$gpuimage$util$Rotation[ordinal()];
    if (i != 1) {
      if (i != 2) {
        if (i != 3) {
          if (i == 4)
            return 270; 
          throw new IllegalStateException("Unknown Rotation!");
        } 
        return 180;
      } 
      return 90;
    } 
    return 0;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/util/Rotation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
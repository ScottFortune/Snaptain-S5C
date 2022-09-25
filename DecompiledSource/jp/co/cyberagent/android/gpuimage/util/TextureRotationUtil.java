package jp.co.cyberagent.android.gpuimage.util;

public class TextureRotationUtil {
  public static final float[] TEXTURE_NO_ROTATION = new float[] { 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F };
  
  public static final float[] TEXTURE_ROTATED_180;
  
  public static final float[] TEXTURE_ROTATED_270;
  
  public static final float[] TEXTURE_ROTATED_90 = new float[] { 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F };
  
  static {
    TEXTURE_ROTATED_180 = new float[] { 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F };
    TEXTURE_ROTATED_270 = new float[] { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F, 1.0F };
  }
  
  private static float flip(float paramFloat) {
    return (paramFloat == 0.0F) ? 1.0F : 0.0F;
  }
  
  public static float[] getRotation(Rotation paramRotation, boolean paramBoolean1, boolean paramBoolean2) {
    int i = null.$SwitchMap$jp$co$cyberagent$android$gpuimage$util$Rotation[paramRotation.ordinal()];
    if (i != 1) {
      if (i != 2) {
        if (i != 3) {
          arrayOfFloat1 = TEXTURE_NO_ROTATION;
        } else {
          arrayOfFloat1 = TEXTURE_ROTATED_270;
        } 
      } else {
        arrayOfFloat1 = TEXTURE_ROTATED_180;
      } 
    } else {
      arrayOfFloat1 = TEXTURE_ROTATED_90;
    } 
    float[] arrayOfFloat2 = arrayOfFloat1;
    if (paramBoolean1)
      arrayOfFloat2 = new float[] { flip(arrayOfFloat1[0]), arrayOfFloat1[1], flip(arrayOfFloat1[2]), arrayOfFloat1[3], flip(arrayOfFloat1[4]), arrayOfFloat1[5], flip(arrayOfFloat1[6]), arrayOfFloat1[7] }; 
    float[] arrayOfFloat1 = arrayOfFloat2;
    if (paramBoolean2)
      arrayOfFloat1 = new float[] { arrayOfFloat2[0], flip(arrayOfFloat2[1]), arrayOfFloat2[2], flip(arrayOfFloat2[3]), arrayOfFloat2[4], flip(arrayOfFloat2[5]), arrayOfFloat2[6], flip(arrayOfFloat2[7]) }; 
    return arrayOfFloat1;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/util/TextureRotationUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
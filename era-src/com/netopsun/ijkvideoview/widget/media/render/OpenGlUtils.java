package com.netopsun.ijkvideoview.widget.media.render;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class OpenGlUtils {
  public static final int NO_TEXTURE = -1;
  
  public static void deleteTexture(int paramInt) {
    GLES20.glDeleteTextures(1, new int[] { paramInt }, 0);
  }
  
  public static int loadProgram(String paramString1, String paramString2) {
    int i = loadShader(paramString1, 35633);
    if (i == 0) {
      Log.d("Load Program", "Vertex Shader Failed");
      return 0;
    } 
    int j = loadShader(paramString2, 35632);
    if (j == 0) {
      Log.d("Load Program", "Fragment Shader Failed");
      return 0;
    } 
    int k = GLES20.glCreateProgram();
    GLES20.glAttachShader(k, i);
    GLES20.glAttachShader(k, j);
    GLES20.glLinkProgram(k);
    int[] arrayOfInt = new int[1];
    GLES20.glGetProgramiv(k, 35714, arrayOfInt, 0);
    if (arrayOfInt[0] <= 0) {
      Log.d("Load Program", "Linking Failed");
      return 0;
    } 
    GLES20.glDeleteShader(i);
    GLES20.glDeleteShader(j);
    return k;
  }
  
  public static int loadShader(String paramString, int paramInt) {
    int[] arrayOfInt = new int[1];
    paramInt = GLES20.glCreateShader(paramInt);
    GLES20.glShaderSource(paramInt, paramString);
    GLES20.glCompileShader(paramInt);
    GLES20.glGetShaderiv(paramInt, 35713, arrayOfInt, 0);
    if (arrayOfInt[0] == 0) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Compilation\n");
      stringBuilder.append(GLES20.glGetShaderInfoLog(paramInt));
      Log.d("Load Shader Failed", stringBuilder.toString());
      return 0;
    } 
    return paramInt;
  }
  
  public static int loadTexture(Bitmap paramBitmap, int paramInt) {
    return loadTexture(paramBitmap, paramInt, true);
  }
  
  public static int loadTexture(Bitmap paramBitmap, int paramInt, boolean paramBoolean) {
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = -1;
    if (paramInt == -1) {
      GLES20.glGenTextures(1, arrayOfInt, 0);
      GLES20.glBindTexture(3553, arrayOfInt[0]);
      GLES20.glTexParameterf(3553, 10240, 9729.0F);
      GLES20.glTexParameterf(3553, 10241, 9729.0F);
      GLES20.glTexParameterf(3553, 10242, 10497.0F);
      GLES20.glTexParameterf(3553, 10243, 10497.0F);
      GLUtils.texImage2D(3553, 0, paramBitmap, 0);
    } else {
      GLES20.glBindTexture(3553, paramInt);
      GLUtils.texSubImage2D(3553, 0, 0, 0, paramBitmap);
      arrayOfInt[0] = paramInt;
    } 
    if (paramBoolean)
      paramBitmap.recycle(); 
    return arrayOfInt[0];
  }
  
  public static int loadTexture(IntBuffer paramIntBuffer, Camera.Size paramSize, int paramInt) {
    int[] arrayOfInt = new int[1];
    if (paramInt == -1) {
      GLES20.glGenTextures(1, arrayOfInt, 0);
      GLES20.glBindTexture(3553, arrayOfInt[0]);
      GLES20.glTexParameterf(3553, 10240, 9729.0F);
      GLES20.glTexParameterf(3553, 10241, 9729.0F);
      GLES20.glTexParameterf(3553, 10242, 33071.0F);
      GLES20.glTexParameterf(3553, 10243, 33071.0F);
      GLES20.glTexImage2D(3553, 0, 6408, paramSize.width, paramSize.height, 0, 6408, 5121, paramIntBuffer);
    } else {
      GLES20.glBindTexture(3553, paramInt);
      GLES20.glTexSubImage2D(3553, 0, 0, 0, paramSize.width, paramSize.height, 6408, 5121, paramIntBuffer);
      arrayOfInt[0] = paramInt;
    } 
    return arrayOfInt[0];
  }
  
  public static int loadTextureAsBitmap(IntBuffer paramIntBuffer, Camera.Size paramSize, int paramInt) {
    return loadTexture(Bitmap.createBitmap(paramIntBuffer.array(), paramSize.width, paramSize.height, Bitmap.Config.ARGB_8888), paramInt);
  }
  
  public static void loadYUVTextures(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, int[] paramArrayOfint) {
    paramByteBuffer.clear();
    int i = paramInt1 * paramInt2;
    paramByteBuffer.limit(i);
    ByteBuffer byteBuffer1 = paramByteBuffer.slice();
    paramByteBuffer.position(i);
    int j = i * 5 / 4;
    paramByteBuffer.limit(j);
    ByteBuffer byteBuffer2 = paramByteBuffer.slice();
    paramByteBuffer.position(j);
    paramByteBuffer.limit(i * 6 / 4);
    paramByteBuffer = paramByteBuffer.slice();
    if (paramArrayOfint[0] == -1) {
      GLES20.glGenTextures(3, paramArrayOfint, 0);
      GLES20.glActiveTexture(33984);
      GLES20.glBindTexture(3553, paramArrayOfint[0]);
      GLES20.glTexParameterf(3553, 10240, 9729.0F);
      GLES20.glTexParameterf(3553, 10241, 9729.0F);
      GLES20.glTexParameterf(3553, 10242, 10497.0F);
      GLES20.glTexParameterf(3553, 10243, 10497.0F);
      GLES20.glActiveTexture(33985);
      GLES20.glBindTexture(3553, paramArrayOfint[1]);
      GLES20.glTexParameterf(3553, 10240, 9729.0F);
      GLES20.glTexParameterf(3553, 10241, 9729.0F);
      GLES20.glTexParameterf(3553, 10242, 10497.0F);
      GLES20.glTexParameterf(3553, 10243, 10497.0F);
      GLES20.glActiveTexture(33986);
      GLES20.glBindTexture(3553, paramArrayOfint[2]);
      GLES20.glTexParameterf(3553, 10240, 9729.0F);
      GLES20.glTexParameterf(3553, 10241, 9729.0F);
      GLES20.glTexParameterf(3553, 10242, 10497.0F);
      GLES20.glTexParameterf(3553, 10243, 10497.0F);
      GLES20.glBindTexture(3553, paramArrayOfint[0]);
      GLES20.glTexImage2D(3553, 0, 6409, paramInt1, paramInt2, 0, 6409, 5121, byteBuffer1);
      GLES20.glBindTexture(3553, paramArrayOfint[1]);
      paramInt1 /= 2;
      paramInt2 /= 2;
      GLES20.glTexImage2D(3553, 0, 6409, paramInt1, paramInt2, 0, 6409, 5121, byteBuffer2);
      GLES20.glBindTexture(3553, paramArrayOfint[2]);
      GLES20.glTexImage2D(3553, 0, 6409, paramInt1, paramInt2, 0, 6409, 5121, paramByteBuffer);
    } else {
      GLES20.glBindTexture(3553, paramArrayOfint[0]);
      GLES20.glTexImage2D(3553, 0, 6409, paramInt1, paramInt2, 0, 6409, 5121, byteBuffer1);
      GLES20.glBindTexture(3553, paramArrayOfint[1]);
      paramInt1 /= 2;
      paramInt2 /= 2;
      GLES20.glTexImage2D(3553, 0, 6409, paramInt1, paramInt2, 0, 6409, 5121, byteBuffer2);
      GLES20.glBindTexture(3553, paramArrayOfint[2]);
      GLES20.glTexImage2D(3553, 0, 6409, paramInt1, paramInt2, 0, 6409, 5121, paramByteBuffer);
    } 
  }
  
  public static float rnd(float paramFloat1, float paramFloat2) {
    return paramFloat1 + (paramFloat2 - paramFloat1) * (float)Math.random();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/widget/media/render/OpenGlUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
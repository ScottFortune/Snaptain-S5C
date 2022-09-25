package com.yang.firework;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import com.yang.firework.utils.ESShader;
import com.yang.firework.utils.RawResourceReader;
import com.yang.firework.utils.TextureHelper;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class TextureLayer {
  private final ShortBuffer indiceBuffer;
  
  private final short[] indicesData = new short[] { 0, 1, 2, 3, 2, 1 };
  
  private final int mPositionHandle;
  
  private final int mProgrammerHandle;
  
  private final int mTexCoordHandle;
  
  private int mTextureHandle;
  
  private final int textureUniformHandle;
  
  private final FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(this.vertexsData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
  
  private final float[] vertexsData = new float[] { 
      -1.0F, 1.0F, 0.0F, 0.0F, -1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 
      1.0F, 0.0F, 1.0F, -1.0F, 1.0F, 1.0F };
  
  public TextureLayer(Context paramContext, Bitmap paramBitmap) {
    this.vertexBuffer.put(this.vertexsData);
    this.vertexBuffer.position(0);
    this.indiceBuffer = ByteBuffer.allocateDirect(this.indicesData.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
    this.indiceBuffer.put(this.indicesData).position(0);
    this.mProgrammerHandle = ESShader.loadProgram(RawResourceReader.readTextFileFromRawResource(paramContext, R.raw.simple_vertex_shader), RawResourceReader.readTextFileFromRawResource(paramContext, R.raw.simpe_fragment_shader));
    this.mPositionHandle = GLES20.glGetAttribLocation(this.mProgrammerHandle, "a_position");
    this.mTexCoordHandle = GLES20.glGetAttribLocation(this.mProgrammerHandle, "a_texCoord");
    this.mTextureHandle = TextureHelper.loadTexture(paramBitmap);
    this.textureUniformHandle = GLES20.glGetUniformLocation(this.mProgrammerHandle, "u_texture");
  }
  
  public void draw() {
    GLES20.glUseProgram(this.mProgrammerHandle);
    this.vertexBuffer.position(0);
    GLES20.glVertexAttribPointer(this.mPositionHandle, 2, 5126, false, 16, this.vertexBuffer);
    this.vertexBuffer.position(2);
    GLES20.glVertexAttribPointer(this.mTexCoordHandle, 2, 5126, false, 16, this.vertexBuffer);
    GLES20.glEnableVertexAttribArray(this.mPositionHandle);
    GLES20.glEnableVertexAttribArray(this.mTexCoordHandle);
    GLES20.glActiveTexture(33984);
    GLES20.glBindTexture(3553, this.mTextureHandle);
    GLES20.glUniform1i(this.textureUniformHandle, 0);
    GLES20.glDrawElements(4, 6, 5123, this.indiceBuffer);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/yang/firework/TextureLayer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
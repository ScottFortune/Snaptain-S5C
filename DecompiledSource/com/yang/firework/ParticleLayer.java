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
import java.util.Arrays;

public class ParticleLayer extends ParticleSystem {
  public static final int BL = 1;
  
  public static final int BR = 3;
  
  private static final int COLOR_COMPONENT_COUNT = 4;
  
  private static final int COORD_COMPONENT_COUNT = 2;
  
  private static final int POSITION_COMPONENT_COUNT = 2;
  
  private static final int QUAD_COMPONENT_COUNT = 32;
  
  public static final int TL = 0;
  
  public static final int TR = 2;
  
  private static final int VERTEX_COMPONENT_COUNT = 8;
  
  private static final int VERTEX_PER_QUAD = 4;
  
  short[] _indices;
  
  ShortBuffer _indicesBuffer;
  
  int[] _vboIds = new int[] { 0, 0 };
  
  float[] _vertexs;
  
  FloatBuffer _vertexsBuffer;
  
  private int mColorHandle;
  
  private int mMatrixUniformHandle;
  
  private int mPositionHandle;
  
  private int mProgrammerHandle;
  
  private int mTexCoordHandle;
  
  private int mTextureHandle;
  
  private float[] projectionMatrix;
  
  private int textureUniformHandle;
  
  ParticleLayer(Context paramContext, int paramInt) {
    super(paramContext, paramInt);
  }
  
  public ParticleLayer(Context paramContext, int paramInt1, int paramInt2) {
    super(paramContext, paramInt1, paramInt2);
  }
  
  private void createOrthographicOffCenter(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float[] paramArrayOffloat) {
    paramArrayOffloat[0] = 2.0F / (paramFloat2 - paramFloat1);
    paramArrayOffloat[5] = 2.0F / (paramFloat4 - paramFloat3);
    float f = paramFloat5 - paramFloat6;
    paramArrayOffloat[10] = 2.0F / f;
    paramArrayOffloat[12] = (paramFloat1 + paramFloat2) / (paramFloat1 - paramFloat2);
    paramArrayOffloat[13] = (paramFloat4 + paramFloat3) / (paramFloat3 - paramFloat4);
    paramArrayOffloat[14] = (paramFloat5 + paramFloat6) / f;
    paramArrayOffloat[15] = 1.0F;
  }
  
  private void initIndices() {
    for (short s = 0; s < this._totalParticles; s = (short)(s + 1)) {
      short s1 = (short)(s * 6);
      short s2 = (short)(s * 4);
      short[] arrayOfShort = this._indices;
      arrayOfShort[s1 + 0] = (short)(short)(s2 + 0);
      short s3 = (short)(s2 + 1);
      arrayOfShort[s1 + 1] = (short)s3;
      short s4 = (short)(s2 + 2);
      arrayOfShort[s1 + 2] = (short)s4;
      arrayOfShort[s1 + 5] = (short)s3;
      arrayOfShort[s1 + 4] = (short)s4;
      arrayOfShort[s1 + 3] = (short)(short)(s2 + 3);
    } 
  }
  
  private void initTexCoord() {
    for (byte b = 0; b < this._totalParticles; b++) {
      float[] arrayOfFloat = this._vertexs;
      int i = b * 32;
      int j = i + 8 + 2 + 4;
      arrayOfFloat[j + 0] = 0.0F;
      arrayOfFloat[j + 1] = 1.0F;
      j = i + 24 + 2 + 4;
      arrayOfFloat[j + 0] = 1.0F;
      arrayOfFloat[j + 1] = 1.0F;
      j = i + 0 + 2 + 4;
      arrayOfFloat[j + 0] = 0.0F;
      arrayOfFloat[j + 1] = 0.0F;
      i = i + 16 + 2 + 4;
      arrayOfFloat[i + 0] = 1.0F;
      arrayOfFloat[i + 1] = 0.0F;
    } 
  }
  
  private void updateColor(Particle paramParticle) {
    this._vertexs[this._particleIdx * 32 + 8 + 2 + 0] = paramParticle.color.r;
    this._vertexs[this._particleIdx * 32 + 8 + 2 + 1] = paramParticle.color.g;
    this._vertexs[this._particleIdx * 32 + 8 + 2 + 2] = paramParticle.color.b;
    this._vertexs[this._particleIdx * 32 + 8 + 2 + 3] = paramParticle.color.a;
    this._vertexs[this._particleIdx * 32 + 24 + 2 + 0] = paramParticle.color.r;
    this._vertexs[this._particleIdx * 32 + 24 + 2 + 1] = paramParticle.color.g;
    this._vertexs[this._particleIdx * 32 + 24 + 2 + 2] = paramParticle.color.b;
    this._vertexs[this._particleIdx * 32 + 24 + 2 + 3] = paramParticle.color.a;
    this._vertexs[this._particleIdx * 32 + 0 + 2 + 0] = paramParticle.color.r;
    this._vertexs[this._particleIdx * 32 + 0 + 2 + 1] = paramParticle.color.g;
    this._vertexs[this._particleIdx * 32 + 0 + 2 + 2] = paramParticle.color.b;
    this._vertexs[this._particleIdx * 32 + 0 + 2 + 3] = paramParticle.color.a;
    this._vertexs[this._particleIdx * 32 + 16 + 2 + 0] = paramParticle.color.r;
    this._vertexs[this._particleIdx * 32 + 16 + 2 + 1] = paramParticle.color.g;
    this._vertexs[this._particleIdx * 32 + 16 + 2 + 2] = paramParticle.color.b;
    this._vertexs[this._particleIdx * 32 + 16 + 2 + 3] = paramParticle.color.a;
  }
  
  private void updatePos(Particle paramParticle, Vec2 paramVec2) {
    float f = paramParticle.size / 2.0F;
    if (paramParticle.rotation != 0.0F) {
      float f1 = -f;
      float f2 = paramVec2.x;
      float f3 = paramVec2.y;
      double d = -CC_DEGREES_TO_RADIANS(paramParticle.rotation);
      float f4 = (float)Math.cos(d);
      float f5 = (float)Math.sin(d);
      float f6 = f1 * f4;
      f1 *= f5;
      f4 *= f;
      float f7 = f * f5;
      float f8 = this._density;
      float f9 = this._density;
      float f10 = this._density;
      float f11 = this._density;
      f5 = this._density;
      float f12 = this._density;
      float f13 = this._density;
      f = this._density;
      this._vertexs[this._particleIdx * 32 + 8 + 0] = (f6 - f1 + f2) * f8;
      this._vertexs[this._particleIdx * 32 + 8 + 1] = (f1 + f6 + f3) * f9;
      this._vertexs[this._particleIdx * 32 + 24 + 0] = (f4 - f1 + f2) * f10;
      this._vertexs[this._particleIdx * 32 + 24 + 1] = (f7 + f6 + f3) * f11;
      this._vertexs[this._particleIdx * 32 + 0 + 0] = (f6 - f7 + f2) * f13;
      this._vertexs[this._particleIdx * 32 + 0 + 1] = (f1 + f4 + f3) * f;
      this._vertexs[this._particleIdx * 32 + 16 + 0] = (f4 - f7 + f2) * f5;
      this._vertexs[this._particleIdx * 32 + 16 + 1] = (f7 + f4 + f3) * f12;
    } else {
      this._vertexs[this._particleIdx * 32 + 8 + 0] = (paramVec2.x - f) * this._density;
      this._vertexs[this._particleIdx * 32 + 8 + 1] = (paramVec2.y - f) * this._density;
      this._vertexs[this._particleIdx * 32 + 24 + 0] = (paramVec2.x + f) * this._density;
      this._vertexs[this._particleIdx * 32 + 24 + 1] = (paramVec2.y - f) * this._density;
      this._vertexs[this._particleIdx * 32 + 0 + 0] = (paramVec2.x - f) * this._density;
      this._vertexs[this._particleIdx * 32 + 0 + 1] = (paramVec2.y + f) * this._density;
      this._vertexs[this._particleIdx * 32 + 16 + 0] = (paramVec2.x + f) * this._density;
      this._vertexs[this._particleIdx * 32 + 16 + 1] = (paramVec2.y + f) * this._density;
    } 
  }
  
  void clearData() {
    Arrays.fill(this._vertexs, 0.0F);
    initTexCoord();
  }
  
  public void destory() {
    GLES20.glDeleteProgram(this.mProgrammerHandle);
    GLES20.glDeleteTextures(1, new int[] { this.mTextureHandle }, 0);
  }
  
  public void draw() {
    GLES20.glUseProgram(this.mProgrammerHandle);
    GLES20.glEnableVertexAttribArray(this.mPositionHandle);
    GLES20.glEnableVertexAttribArray(this.mColorHandle);
    GLES20.glEnableVertexAttribArray(this.mTexCoordHandle);
    this._vertexsBuffer.position(0);
    GLES20.glVertexAttribPointer(this.mPositionHandle, 2, 5126, false, 32, this._vertexsBuffer);
    this._vertexsBuffer.position(2);
    GLES20.glVertexAttribPointer(this.mColorHandle, 4, 5126, false, 32, this._vertexsBuffer);
    this._vertexsBuffer.position(6);
    GLES20.glVertexAttribPointer(this.mTexCoordHandle, 2, 5126, false, 32, this._vertexsBuffer);
    GLES20.glUniformMatrix4fv(this.mMatrixUniformHandle, 1, false, this.projectionMatrix, 0);
    GLES20.glActiveTexture(33984);
    GLES20.glBindTexture(3553, this.mTextureHandle);
    GLES20.glUniform1i(this.textureUniformHandle, 0);
    GLES20.glDrawElements(4, this._indices.length, 5123, this._indicesBuffer);
    GLES20.glDisableVertexAttribArray(this.mPositionHandle);
    GLES20.glDisableVertexAttribArray(this.mColorHandle);
    GLES20.glDisableVertexAttribArray(this.mTexCoordHandle);
  }
  
  boolean initWithTotalParticles(int paramInt) {
    if (super.initWithTotalParticles(paramInt)) {
      this._vertexs = new float[this._totalParticles * 32];
      this._vertexsBuffer = ByteBuffer.allocateDirect(this._vertexs.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(this._vertexs);
      this._vertexsBuffer.position(0);
      this._indices = new short[this._totalParticles * 6];
      initIndices();
      this._indicesBuffer = ByteBuffer.allocateDirect(this._indices.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer().put(this._indices);
      this._indicesBuffer.position(0);
      this.projectionMatrix = new float[16];
      createOrthographicOffCenter(0.0F, this._width, this._height, 0.0F, -1024.0F, 1024.0F, this.projectionMatrix);
      this.mProgrammerHandle = ESShader.loadProgram(RawResourceReader.readTextFileFromRawResource(this.context, R.raw.particle_vert), RawResourceReader.readTextFileFromRawResource(this.context, R.raw.particle_frag));
      this.mPositionHandle = GLES20.glGetAttribLocation(this.mProgrammerHandle, "a_position");
      this.mColorHandle = GLES20.glGetAttribLocation(this.mProgrammerHandle, "a_color");
      this.mTexCoordHandle = GLES20.glGetAttribLocation(this.mProgrammerHandle, "a_texCoord");
      this.mMatrixUniformHandle = GLES20.glGetUniformLocation(this.mProgrammerHandle, "u_matrix");
      this.textureUniformHandle = GLES20.glGetUniformLocation(this.mProgrammerHandle, "u_texture");
      return true;
    } 
    return false;
  }
  
  void setTexture(int paramInt) {
    this.mTextureHandle = TextureHelper.loadTexture(this.context, paramInt);
    initTexCoord();
  }
  
  void setTexture(Bitmap paramBitmap) {
    this.mTextureHandle = TextureHelper.loadTexture(paramBitmap);
    initTexCoord();
  }
  
  public void updateBuffer() {
    this._vertexsBuffer.position(0);
    this._vertexsBuffer.put(this._vertexs);
    this._vertexsBuffer.position(0);
    this._indicesBuffer.position(0);
    this._indicesBuffer.put(this._indices);
    this._indicesBuffer.position(0);
  }
  
  void updateQuadWithParticle(Particle paramParticle, Vec2 paramVec2) {
    updatePos(paramParticle, paramVec2);
    updateColor(paramParticle);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/yang/firework/ParticleLayer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
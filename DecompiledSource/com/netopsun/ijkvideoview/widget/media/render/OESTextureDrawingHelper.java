package com.netopsun.ijkvideoview.widget.media.render;

import android.opengl.GLES10;
import android.opengl.GLES20;
import com.yang.firework.ParticleLayer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class OESTextureDrawingHelper {
  private static final float[] CUBE = new float[] { -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F };
  
  private static final String NO_FILTER_FRAGMENT_SHADER = "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying highp vec2 textureCoordinate;\n \nuniform samplerExternalOES inputImageTexture;\n \nvoid main()\n{\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}";
  
  private static final String NO_FILTER_VERTEX_SHADER = "attribute vec4 position;\nuniform mat4 uTexMatrix;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = (uTexMatrix *inputTextureCoordinate).xy;\n}";
  
  private static final float[] TEXTURE_NO_ROTATION = new float[] { 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F };
  
  private static final String mFragmentShader = "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying highp vec2 textureCoordinate;\n \nuniform samplerExternalOES inputImageTexture;\n \nvoid main()\n{\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}";
  
  private static final String mVertexShader = "attribute vec4 position;\nuniform mat4 uTexMatrix;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = (uTexMatrix *inputTextureCoordinate).xy;\n}";
  
  private final int[] fbo = new int[] { -1 };
  
  private int mGLAttribPosition;
  
  private int mGLAttribTextureCoordinate;
  
  private FloatBuffer mGLCubeBuffer = ByteBuffer.allocateDirect(CUBE.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
  
  private int mGLProgId;
  
  private FloatBuffer mGLTextureBuffer;
  
  private int mGLUniformTexture;
  
  private int muTexMatrixLoc;
  
  private int rgbaTextureBufferSize = 0;
  
  public OESTextureDrawingHelper() {
    this.mGLCubeBuffer.put(CUBE).position(0);
    this.mGLTextureBuffer = ByteBuffer.allocateDirect(TEXTURE_NO_ROTATION.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    this.mGLTextureBuffer.put(TEXTURE_NO_ROTATION).position(0);
    this.mGLProgId = OpenGlUtils.loadProgram("attribute vec4 position;\nuniform mat4 uTexMatrix;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = (uTexMatrix *inputTextureCoordinate).xy;\n}", "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying highp vec2 textureCoordinate;\n \nuniform samplerExternalOES inputImageTexture;\n \nvoid main()\n{\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}");
    this.mGLAttribPosition = GLES20.glGetAttribLocation(this.mGLProgId, "position");
    this.mGLUniformTexture = GLES20.glGetUniformLocation(this.mGLProgId, "inputImageTexture");
    this.mGLAttribTextureCoordinate = GLES20.glGetAttribLocation(this.mGLProgId, "inputTextureCoordinate");
    this.muTexMatrixLoc = GLES20.glGetUniformLocation(this.mGLProgId, "uTexMatrix");
  }
  
  public void drawOESTexture(float[] paramArrayOffloat, int paramInt1, int paramInt2, int paramInt3) {
    GLES20.glUseProgram(this.mGLProgId);
    this.mGLCubeBuffer.position(0);
    GLES20.glVertexAttribPointer(this.mGLAttribPosition, 2, 5126, false, 0, this.mGLCubeBuffer);
    GLES20.glEnableVertexAttribArray(this.mGLAttribPosition);
    GLES20.glUniformMatrix4fv(this.muTexMatrixLoc, 1, false, paramArrayOffloat, 0);
    this.mGLTextureBuffer.position(0);
    GLES20.glVertexAttribPointer(this.mGLAttribTextureCoordinate, 2, 5126, false, 0, this.mGLTextureBuffer);
    GLES20.glEnableVertexAttribArray(this.mGLAttribTextureCoordinate);
    GLES20.glViewport(0, 0, paramInt2, paramInt3);
    GLES20.glActiveTexture(33984);
    GLES20.glBindTexture(36197, paramInt1);
    GLES20.glUniform1i(this.mGLUniformTexture, 0);
    GLES20.glDrawArrays(5, 0, 4);
    GLES20.glDisableVertexAttribArray(this.mGLAttribPosition);
    GLES20.glDisableVertexAttribArray(this.mGLAttribTextureCoordinate);
    GLES20.glBindTexture(3553, 0);
  }
  
  public void drawOESTexture2RGBTexture(float[] paramArrayOffloat, int paramInt1, int paramInt2, ParticleLayer paramParticleLayer, int paramInt3, int paramInt4, float paramFloat) {
    GLES20.glUseProgram(this.mGLProgId);
    float f = -1.0F * paramFloat;
    paramFloat = 1.0F * paramFloat;
    this.mGLCubeBuffer.clear();
    this.mGLCubeBuffer.put(new float[] { f, f, paramFloat, f, f, paramFloat, paramFloat, paramFloat });
    this.mGLCubeBuffer.position(0);
    GLES20.glVertexAttribPointer(this.mGLAttribPosition, 2, 5126, false, 0, this.mGLCubeBuffer);
    GLES20.glEnableVertexAttribArray(this.mGLAttribPosition);
    GLES20.glUniformMatrix4fv(this.muTexMatrixLoc, 1, false, paramArrayOffloat, 0);
    this.mGLTextureBuffer.position(0);
    GLES20.glVertexAttribPointer(this.mGLAttribTextureCoordinate, 2, 5126, false, 0, this.mGLTextureBuffer);
    GLES20.glEnableVertexAttribArray(this.mGLAttribTextureCoordinate);
    int[] arrayOfInt = this.fbo;
    if (arrayOfInt[0] == -1)
      GLES20.glGenFramebuffers(1, arrayOfInt, 0); 
    GLES20.glBindFramebuffer(36160, this.fbo[0]);
    int i = this.rgbaTextureBufferSize;
    int j = paramInt3 * paramInt4 * 4;
    if (i != j) {
      GLES20.glBindTexture(3553, paramInt2);
      GLES20.glTexImage2D(3553, 0, 6408, paramInt3, paramInt4, 0, 6408, 5121, null);
      GLES20.glTexParameterf(3553, 10240, 9729.0F);
      GLES20.glTexParameterf(3553, 10241, 9729.0F);
      GLES20.glTexParameterf(3553, 10242, 33071.0F);
      GLES20.glTexParameterf(3553, 10243, 33071.0F);
      GLES20.glFramebufferTexture2D(36160, 36064, 3553, paramInt2, 0);
      this.rgbaTextureBufferSize = j;
    } 
    GLES20.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
    GLES20.glClear(16384);
    GLES20.glViewport(0, 0, paramInt3, paramInt4);
    GLES20.glActiveTexture(33984);
    GLES20.glBindTexture(36197, paramInt1);
    GLES20.glUniform1i(this.mGLUniformTexture, 0);
    GLES20.glDrawArrays(5, 0, 4);
    GLES20.glDisableVertexAttribArray(this.mGLAttribPosition);
    GLES20.glDisableVertexAttribArray(this.mGLAttribTextureCoordinate);
    if (paramParticleLayer != null) {
      GLES20.glEnable(3042);
      GLES10.glBlendFunc(770, 1);
      paramParticleLayer.update(0.04F);
      paramParticleLayer.updateBuffer();
      paramParticleLayer.draw();
      GLES20.glDisable(3042);
    } 
    GLES20.glBindTexture(3553, 0);
    GLES20.glBindFramebuffer(36160, 0);
  }
  
  public void drawOESTextureVRMode(int paramInt1, int paramInt2, int paramInt3) {
    GLES20.glUseProgram(this.mGLProgId);
    this.mGLCubeBuffer.position(0);
    GLES20.glVertexAttribPointer(this.mGLAttribPosition, 2, 5126, false, 0, this.mGLCubeBuffer);
    GLES20.glEnableVertexAttribArray(this.mGLAttribPosition);
    this.mGLTextureBuffer.position(0);
    GLES20.glVertexAttribPointer(this.mGLAttribTextureCoordinate, 2, 5126, false, 0, this.mGLTextureBuffer);
    GLES20.glEnableVertexAttribArray(this.mGLAttribTextureCoordinate);
    GLES20.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
    GLES20.glClear(16384);
    int i = paramInt3 / 4;
    paramInt2 /= 2;
    paramInt3 /= 2;
    GLES20.glViewport(0, i, paramInt2, paramInt3);
    GLES20.glActiveTexture(33984);
    GLES20.glBindTexture(36197, paramInt1);
    GLES20.glUniform1i(this.mGLUniformTexture, 0);
    GLES20.glDrawArrays(5, 0, 4);
    GLES20.glViewport(paramInt2, i, paramInt2, paramInt3);
    GLES20.glActiveTexture(33984);
    GLES20.glBindTexture(36197, paramInt1);
    GLES20.glUniform1i(this.mGLUniformTexture, 0);
    GLES20.glDrawArrays(5, 0, 4);
    GLES20.glDisableVertexAttribArray(this.mGLAttribPosition);
    GLES20.glDisableVertexAttribArray(this.mGLAttribTextureCoordinate);
    GLES20.glBindTexture(3553, 0);
  }
  
  public void onDestory() {
    GLES20.glDeleteProgram(this.mGLProgId);
    GLES20.glDeleteFramebuffers(1, this.fbo, 0);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/widget/media/render/OESTextureDrawingHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
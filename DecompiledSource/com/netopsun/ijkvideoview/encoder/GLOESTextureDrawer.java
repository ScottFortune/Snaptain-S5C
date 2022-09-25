package com.netopsun.ijkvideoview.encoder;

import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GLOESTextureDrawer {
  static final float[] CUBE = new float[] { -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F };
  
  public static final String NO_FILTER_FRAGMENT_SHADER = "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying highp vec2 textureCoordinate;\n \nuniform samplerExternalOES inputImageTexture;\n \nvoid main()\n{\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}";
  
  public static final String NO_FILTER_VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}";
  
  public static final float[] TEXTURE_NO_ROTATION = new float[] { 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F };
  
  private static int[] fbo;
  
  private static final String mFragmentShader = "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying highp vec2 textureCoordinate;\n \nuniform samplerExternalOES inputImageTexture;\n \nvoid main()\n{\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}";
  
  protected static int mGLAttribPosition = 0;
  
  protected static int mGLAttribTextureCoordinate = 0;
  
  private static FloatBuffer mGLCubeBuffer;
  
  protected static int mGLProgId = 0;
  
  private static FloatBuffer mGLTextureBuffer;
  
  protected static int mGLUniformTexture = 0;
  
  private static final String mVertexShader = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}";
  
  public static void draw(int paramInt) {
    GLES20.glUseProgram(mGLProgId);
    mGLCubeBuffer.position(0);
    GLES20.glVertexAttribPointer(mGLAttribPosition, 2, 5126, false, 0, mGLCubeBuffer);
    GLES20.glEnableVertexAttribArray(mGLAttribPosition);
    mGLTextureBuffer.position(0);
    GLES20.glVertexAttribPointer(mGLAttribTextureCoordinate, 2, 5126, false, 0, mGLTextureBuffer);
    GLES20.glEnableVertexAttribArray(mGLAttribTextureCoordinate);
    GLES20.glActiveTexture(33984);
    GLES20.glBindTexture(36197, paramInt);
    GLES20.glUniform1i(mGLUniformTexture, 0);
    GLES20.glDrawArrays(5, 0, 4);
    GLES20.glDisableVertexAttribArray(mGLAttribPosition);
    GLES20.glDisableVertexAttribArray(mGLAttribTextureCoordinate);
    GLES20.glBindTexture(3553, 0);
  }
  
  public static void init() {
    fbo = new int[] { -1 };
    mGLCubeBuffer = ByteBuffer.allocateDirect(CUBE.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    mGLCubeBuffer.put(CUBE).position(0);
    mGLTextureBuffer = ByteBuffer.allocateDirect(TEXTURE_NO_ROTATION.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    mGLTextureBuffer.put(TEXTURE_NO_ROTATION).position(0);
    mGLProgId = OpenGlUtils.loadProgram("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying highp vec2 textureCoordinate;\n \nuniform samplerExternalOES inputImageTexture;\n \nvoid main()\n{\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}");
    mGLAttribPosition = GLES20.glGetAttribLocation(mGLProgId, "position");
    mGLUniformTexture = GLES20.glGetUniformLocation(mGLProgId, "inputImageTexture");
    mGLAttribTextureCoordinate = GLES20.glGetAttribLocation(mGLProgId, "inputTextureCoordinate");
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/encoder/GLOESTextureDrawer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
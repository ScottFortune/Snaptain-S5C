package com.netopsun.ijkvideoview.widget.media.render;

import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class TextureDrawingHelper {
  private static final float[] CUBE = new float[] { -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F };
  
  private static final String NO_FILTER_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n \nuniform sampler2D inputImageTexture;\n \nvoid main()\n{\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}";
  
  private static final String NO_FILTER_VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}";
  
  private static final float[] TEXTURE_ROTATION = new float[] { 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F };
  
  private static final String mFragmentShader = "varying highp vec2 textureCoordinate;\n \nuniform sampler2D inputImageTexture;\n \nvoid main()\n{\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}";
  
  private static final String mVertexShader = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}";
  
  private int mGLAttribPosition;
  
  private int mGLAttribTextureCoordinate;
  
  private FloatBuffer mGLCubeBuffer = ByteBuffer.allocateDirect(CUBE.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
  
  private int mGLProgId;
  
  private FloatBuffer mGLTextureBuffer;
  
  private int mGLUniformTexture;
  
  public TextureDrawingHelper() {
    this.mGLCubeBuffer.put(CUBE).position(0);
    this.mGLTextureBuffer = ByteBuffer.allocateDirect(TEXTURE_ROTATION.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    this.mGLTextureBuffer.put(TEXTURE_ROTATION).position(0);
    this.mGLProgId = OpenGlUtils.loadProgram("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "varying highp vec2 textureCoordinate;\n \nuniform sampler2D inputImageTexture;\n \nvoid main()\n{\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}");
    this.mGLAttribPosition = GLES20.glGetAttribLocation(this.mGLProgId, "position");
    this.mGLUniformTexture = GLES20.glGetUniformLocation(this.mGLProgId, "inputImageTexture");
    this.mGLAttribTextureCoordinate = GLES20.glGetAttribLocation(this.mGLProgId, "inputTextureCoordinate");
  }
  
  public void draw(int paramInt) {
    GLES20.glUseProgram(this.mGLProgId);
    this.mGLCubeBuffer.position(0);
    GLES20.glVertexAttribPointer(this.mGLAttribPosition, 2, 5126, false, 0, this.mGLCubeBuffer);
    GLES20.glEnableVertexAttribArray(this.mGLAttribPosition);
    this.mGLTextureBuffer.position(0);
    GLES20.glVertexAttribPointer(this.mGLAttribTextureCoordinate, 2, 5126, false, 0, this.mGLTextureBuffer);
    GLES20.glEnableVertexAttribArray(this.mGLAttribTextureCoordinate);
    GLES20.glActiveTexture(33984);
    GLES20.glBindTexture(3553, paramInt);
    GLES20.glUniform1i(this.mGLUniformTexture, 0);
    GLES20.glDrawArrays(5, 0, 4);
    GLES20.glDisableVertexAttribArray(this.mGLAttribPosition);
    GLES20.glDisableVertexAttribArray(this.mGLAttribTextureCoordinate);
    GLES20.glBindTexture(3553, 0);
  }
  
  public void onDestory() {
    GLES20.glDeleteProgram(this.mGLProgId);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/widget/media/render/TextureDrawingHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package jp.co.cyberagent.android.gpuimage.filter;

import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLES20;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.Scanner;
import jp.co.cyberagent.android.gpuimage.util.OpenGlUtils;

public class GPUImageFilter {
  public static final String NO_FILTER_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n \nuniform sampler2D inputImageTexture;\n \nvoid main()\n{\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}";
  
  public static final String NO_FILTER_VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}";
  
  private final String fragmentShader;
  
  private int glAttribPosition;
  
  private int glAttribTextureCoordinate;
  
  private int glProgId;
  
  private int glUniformTexture;
  
  private boolean isInitialized;
  
  private int outputHeight;
  
  private int outputWidth;
  
  private final LinkedList<Runnable> runOnDraw = new LinkedList<Runnable>();
  
  private final String vertexShader;
  
  public GPUImageFilter() {
    this("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "varying highp vec2 textureCoordinate;\n \nuniform sampler2D inputImageTexture;\n \nvoid main()\n{\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}");
  }
  
  public GPUImageFilter(String paramString1, String paramString2) {
    this.vertexShader = paramString1;
    this.fragmentShader = paramString2;
  }
  
  public static String convertStreamToString(InputStream paramInputStream) {
    String str;
    Scanner scanner = (new Scanner(paramInputStream)).useDelimiter("\\A");
    if (scanner.hasNext()) {
      str = scanner.next();
    } else {
      str = "";
    } 
    return str;
  }
  
  private final void init() {
    onInit();
    onInitialized();
  }
  
  public static String loadShader(String paramString, Context paramContext) {
    try {
      InputStream inputStream = paramContext.getAssets().open(paramString);
      paramString = convertStreamToString(inputStream);
      inputStream.close();
      return paramString;
    } catch (Exception exception) {
      exception.printStackTrace();
      return "";
    } 
  }
  
  public final void destroy() {
    this.isInitialized = false;
    GLES20.glDeleteProgram(this.glProgId);
    onDestroy();
  }
  
  public int getAttribPosition() {
    return this.glAttribPosition;
  }
  
  public int getAttribTextureCoordinate() {
    return this.glAttribTextureCoordinate;
  }
  
  public int getOutputHeight() {
    return this.outputHeight;
  }
  
  public int getOutputWidth() {
    return this.outputWidth;
  }
  
  public int getProgram() {
    return this.glProgId;
  }
  
  public int getUniformTexture() {
    return this.glUniformTexture;
  }
  
  public void ifNeedInit() {
    if (!this.isInitialized)
      init(); 
  }
  
  public boolean isInitialized() {
    return this.isInitialized;
  }
  
  public void onDestroy() {}
  
  public void onDraw(int paramInt, FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2) {
    GLES20.glUseProgram(this.glProgId);
    runPendingOnDrawTasks();
    if (!this.isInitialized)
      return; 
    paramFloatBuffer1.position(0);
    GLES20.glVertexAttribPointer(this.glAttribPosition, 2, 5126, false, 0, paramFloatBuffer1);
    GLES20.glEnableVertexAttribArray(this.glAttribPosition);
    paramFloatBuffer2.position(0);
    GLES20.glVertexAttribPointer(this.glAttribTextureCoordinate, 2, 5126, false, 0, paramFloatBuffer2);
    GLES20.glEnableVertexAttribArray(this.glAttribTextureCoordinate);
    if (paramInt != -1) {
      GLES20.glActiveTexture(33984);
      GLES20.glBindTexture(3553, paramInt);
      GLES20.glUniform1i(this.glUniformTexture, 0);
    } 
    onDrawArraysPre();
    GLES20.glDrawArrays(5, 0, 4);
    GLES20.glDisableVertexAttribArray(this.glAttribPosition);
    GLES20.glDisableVertexAttribArray(this.glAttribTextureCoordinate);
    GLES20.glBindTexture(3553, 0);
  }
  
  protected void onDrawArraysPre() {}
  
  public void onInit() {
    this.glProgId = OpenGlUtils.loadProgram(this.vertexShader, this.fragmentShader);
    this.glAttribPosition = GLES20.glGetAttribLocation(this.glProgId, "position");
    this.glUniformTexture = GLES20.glGetUniformLocation(this.glProgId, "inputImageTexture");
    this.glAttribTextureCoordinate = GLES20.glGetAttribLocation(this.glProgId, "inputTextureCoordinate");
    this.isInitialized = true;
  }
  
  public void onInitialized() {}
  
  public void onOutputSizeChanged(int paramInt1, int paramInt2) {
    this.outputWidth = paramInt1;
    this.outputHeight = paramInt2;
  }
  
  protected void runOnDraw(Runnable paramRunnable) {
    synchronized (this.runOnDraw) {
      this.runOnDraw.addLast(paramRunnable);
      return;
    } 
  }
  
  protected void runPendingOnDrawTasks() {
    while (!this.runOnDraw.isEmpty())
      ((Runnable)this.runOnDraw.removeFirst()).run(); 
  }
  
  protected void setFloat(final int location, final float floatValue) {
    runOnDraw(new Runnable() {
          public void run() {
            GPUImageFilter.this.ifNeedInit();
            GLES20.glUniform1f(location, floatValue);
          }
        });
  }
  
  protected void setFloatArray(final int location, final float[] arrayValue) {
    runOnDraw(new Runnable() {
          public void run() {
            GPUImageFilter.this.ifNeedInit();
            int i = location;
            float[] arrayOfFloat = arrayValue;
            GLES20.glUniform1fv(i, arrayOfFloat.length, FloatBuffer.wrap(arrayOfFloat));
          }
        });
  }
  
  protected void setFloatVec2(final int location, final float[] arrayValue) {
    runOnDraw(new Runnable() {
          public void run() {
            GPUImageFilter.this.ifNeedInit();
            GLES20.glUniform2fv(location, 1, FloatBuffer.wrap(arrayValue));
          }
        });
  }
  
  protected void setFloatVec3(final int location, final float[] arrayValue) {
    runOnDraw(new Runnable() {
          public void run() {
            GPUImageFilter.this.ifNeedInit();
            GLES20.glUniform3fv(location, 1, FloatBuffer.wrap(arrayValue));
          }
        });
  }
  
  protected void setFloatVec4(final int location, final float[] arrayValue) {
    runOnDraw(new Runnable() {
          public void run() {
            GPUImageFilter.this.ifNeedInit();
            GLES20.glUniform4fv(location, 1, FloatBuffer.wrap(arrayValue));
          }
        });
  }
  
  protected void setInteger(final int location, final int intValue) {
    runOnDraw(new Runnable() {
          public void run() {
            GPUImageFilter.this.ifNeedInit();
            GLES20.glUniform1i(location, intValue);
          }
        });
  }
  
  protected void setPoint(final int location, final PointF point) {
    runOnDraw(new Runnable() {
          public void run() {
            GPUImageFilter.this.ifNeedInit();
            float f1 = point.x;
            float f2 = point.y;
            GLES20.glUniform2fv(location, 1, new float[] { f1, f2 }, 0);
          }
        });
  }
  
  protected void setUniformMatrix3f(final int location, final float[] matrix) {
    runOnDraw(new Runnable() {
          public void run() {
            GPUImageFilter.this.ifNeedInit();
            GLES20.glUniformMatrix3fv(location, 1, false, matrix, 0);
          }
        });
  }
  
  protected void setUniformMatrix4f(final int location, final float[] matrix) {
    runOnDraw(new Runnable() {
          public void run() {
            GPUImageFilter.this.ifNeedInit();
            GLES20.glUniformMatrix4fv(location, 1, false, matrix, 0);
          }
        });
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
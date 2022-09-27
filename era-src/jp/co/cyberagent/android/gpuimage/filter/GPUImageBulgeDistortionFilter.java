package jp.co.cyberagent.android.gpuimage.filter;

import android.graphics.PointF;
import android.opengl.GLES20;

public class GPUImageBulgeDistortionFilter extends GPUImageFilter {
  public static final String BULGE_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nuniform highp float aspectRatio;\nuniform highp vec2 center;\nuniform highp float radius;\nuniform highp float scale;\n\nvoid main()\n{\nhighp vec2 textureCoordinateToUse = vec2(textureCoordinate.x, (textureCoordinate.y * aspectRatio + 0.5 - 0.5 * aspectRatio));\nhighp float dist = distance(center, textureCoordinateToUse);\ntextureCoordinateToUse = textureCoordinate;\n\nif (dist < radius)\n{\ntextureCoordinateToUse -= center;\nhighp float percent = 1.0 - ((radius - dist) / radius) * scale;\npercent = percent * percent;\n\ntextureCoordinateToUse = textureCoordinateToUse * percent;\ntextureCoordinateToUse += center;\n}\n\ngl_FragColor = texture2D(inputImageTexture, textureCoordinateToUse );    \n}\n";
  
  private float aspectRatio;
  
  private int aspectRatioLocation;
  
  private PointF center;
  
  private int centerLocation;
  
  private float radius;
  
  private int radiusLocation;
  
  private float scale;
  
  private int scaleLocation;
  
  public GPUImageBulgeDistortionFilter() {
    this(0.25F, 0.5F, new PointF(0.5F, 0.5F));
  }
  
  public GPUImageBulgeDistortionFilter(float paramFloat1, float paramFloat2, PointF paramPointF) {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nuniform highp float aspectRatio;\nuniform highp vec2 center;\nuniform highp float radius;\nuniform highp float scale;\n\nvoid main()\n{\nhighp vec2 textureCoordinateToUse = vec2(textureCoordinate.x, (textureCoordinate.y * aspectRatio + 0.5 - 0.5 * aspectRatio));\nhighp float dist = distance(center, textureCoordinateToUse);\ntextureCoordinateToUse = textureCoordinate;\n\nif (dist < radius)\n{\ntextureCoordinateToUse -= center;\nhighp float percent = 1.0 - ((radius - dist) / radius) * scale;\npercent = percent * percent;\n\ntextureCoordinateToUse = textureCoordinateToUse * percent;\ntextureCoordinateToUse += center;\n}\n\ngl_FragColor = texture2D(inputImageTexture, textureCoordinateToUse );    \n}\n");
    this.radius = paramFloat1;
    this.scale = paramFloat2;
    this.center = paramPointF;
  }
  
  private void setAspectRatio(float paramFloat) {
    this.aspectRatio = paramFloat;
    setFloat(this.aspectRatioLocation, paramFloat);
  }
  
  public void onInit() {
    super.onInit();
    this.scaleLocation = GLES20.glGetUniformLocation(getProgram(), "scale");
    this.radiusLocation = GLES20.glGetUniformLocation(getProgram(), "radius");
    this.centerLocation = GLES20.glGetUniformLocation(getProgram(), "center");
    this.aspectRatioLocation = GLES20.glGetUniformLocation(getProgram(), "aspectRatio");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setAspectRatio(this.aspectRatio);
    setRadius(this.radius);
    setScale(this.scale);
    setCenter(this.center);
  }
  
  public void onOutputSizeChanged(int paramInt1, int paramInt2) {
    this.aspectRatio = paramInt2 / paramInt1;
    setAspectRatio(this.aspectRatio);
    super.onOutputSizeChanged(paramInt1, paramInt2);
  }
  
  public void setCenter(PointF paramPointF) {
    this.center = paramPointF;
    setPoint(this.centerLocation, paramPointF);
  }
  
  public void setRadius(float paramFloat) {
    this.radius = paramFloat;
    setFloat(this.radiusLocation, paramFloat);
  }
  
  public void setScale(float paramFloat) {
    this.scale = paramFloat;
    setFloat(this.scaleLocation, paramFloat);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageBulgeDistortionFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
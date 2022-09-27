package jp.co.cyberagent.android.gpuimage.filter;

import android.graphics.PointF;
import android.opengl.GLES20;

public class GPUImageSwirlFilter extends GPUImageFilter {
  public static final String SWIRL_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nuniform highp vec2 center;\nuniform highp float radius;\nuniform highp float angle;\n\nvoid main()\n{\nhighp vec2 textureCoordinateToUse = textureCoordinate;\nhighp float dist = distance(center, textureCoordinate);\nif (dist < radius)\n{\ntextureCoordinateToUse -= center;\nhighp float percent = (radius - dist) / radius;\nhighp float theta = percent * percent * angle * 8.0;\nhighp float s = sin(theta);\nhighp float c = cos(theta);\ntextureCoordinateToUse = vec2(dot(textureCoordinateToUse, vec2(c, -s)), dot(textureCoordinateToUse, vec2(s, c)));\ntextureCoordinateToUse += center;\n}\n\ngl_FragColor = texture2D(inputImageTexture, textureCoordinateToUse );\n\n}\n";
  
  private float angle;
  
  private int angleLocation;
  
  private PointF center;
  
  private int centerLocation;
  
  private float radius;
  
  private int radiusLocation;
  
  public GPUImageSwirlFilter() {
    this(0.5F, 1.0F, new PointF(0.5F, 0.5F));
  }
  
  public GPUImageSwirlFilter(float paramFloat1, float paramFloat2, PointF paramPointF) {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nuniform highp vec2 center;\nuniform highp float radius;\nuniform highp float angle;\n\nvoid main()\n{\nhighp vec2 textureCoordinateToUse = textureCoordinate;\nhighp float dist = distance(center, textureCoordinate);\nif (dist < radius)\n{\ntextureCoordinateToUse -= center;\nhighp float percent = (radius - dist) / radius;\nhighp float theta = percent * percent * angle * 8.0;\nhighp float s = sin(theta);\nhighp float c = cos(theta);\ntextureCoordinateToUse = vec2(dot(textureCoordinateToUse, vec2(c, -s)), dot(textureCoordinateToUse, vec2(s, c)));\ntextureCoordinateToUse += center;\n}\n\ngl_FragColor = texture2D(inputImageTexture, textureCoordinateToUse );\n\n}\n");
    this.radius = paramFloat1;
    this.angle = paramFloat2;
    this.center = paramPointF;
  }
  
  public void onInit() {
    super.onInit();
    this.angleLocation = GLES20.glGetUniformLocation(getProgram(), "angle");
    this.radiusLocation = GLES20.glGetUniformLocation(getProgram(), "radius");
    this.centerLocation = GLES20.glGetUniformLocation(getProgram(), "center");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setRadius(this.radius);
    setAngle(this.angle);
    setCenter(this.center);
  }
  
  public void setAngle(float paramFloat) {
    this.angle = paramFloat;
    setFloat(this.angleLocation, paramFloat);
  }
  
  public void setCenter(PointF paramPointF) {
    this.center = paramPointF;
    setPoint(this.centerLocation, paramPointF);
  }
  
  public void setRadius(float paramFloat) {
    this.radius = paramFloat;
    setFloat(this.radiusLocation, paramFloat);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageSwirlFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
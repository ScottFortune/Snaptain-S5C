package jp.co.cyberagent.android.gpuimage.filter;

import android.graphics.PointF;
import android.opengl.GLES20;

public class GPUImageSphereRefractionFilter extends GPUImageFilter {
  public static final String SPHERE_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nuniform highp vec2 center;\nuniform highp float radius;\nuniform highp float aspectRatio;\nuniform highp float refractiveIndex;\n\nvoid main()\n{\nhighp vec2 textureCoordinateToUse = vec2(textureCoordinate.x, (textureCoordinate.y * aspectRatio + 0.5 - 0.5 * aspectRatio));\nhighp float distanceFromCenter = distance(center, textureCoordinateToUse);\nlowp float checkForPresenceWithinSphere = step(distanceFromCenter, radius);\n\ndistanceFromCenter = distanceFromCenter / radius;\n\nhighp float normalizedDepth = radius * sqrt(1.0 - distanceFromCenter * distanceFromCenter);\nhighp vec3 sphereNormal = normalize(vec3(textureCoordinateToUse - center, normalizedDepth));\n\nhighp vec3 refractedVector = refract(vec3(0.0, 0.0, -1.0), sphereNormal, refractiveIndex);\n\ngl_FragColor = texture2D(inputImageTexture, (refractedVector.xy + 1.0) * 0.5) * checkForPresenceWithinSphere;     \n}\n";
  
  private float aspectRatio;
  
  private int aspectRatioLocation;
  
  private PointF center;
  
  private int centerLocation;
  
  private float radius;
  
  private int radiusLocation;
  
  private float refractiveIndex;
  
  private int refractiveIndexLocation;
  
  public GPUImageSphereRefractionFilter() {
    this(new PointF(0.5F, 0.5F), 0.25F, 0.71F);
  }
  
  public GPUImageSphereRefractionFilter(PointF paramPointF, float paramFloat1, float paramFloat2) {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nuniform highp vec2 center;\nuniform highp float radius;\nuniform highp float aspectRatio;\nuniform highp float refractiveIndex;\n\nvoid main()\n{\nhighp vec2 textureCoordinateToUse = vec2(textureCoordinate.x, (textureCoordinate.y * aspectRatio + 0.5 - 0.5 * aspectRatio));\nhighp float distanceFromCenter = distance(center, textureCoordinateToUse);\nlowp float checkForPresenceWithinSphere = step(distanceFromCenter, radius);\n\ndistanceFromCenter = distanceFromCenter / radius;\n\nhighp float normalizedDepth = radius * sqrt(1.0 - distanceFromCenter * distanceFromCenter);\nhighp vec3 sphereNormal = normalize(vec3(textureCoordinateToUse - center, normalizedDepth));\n\nhighp vec3 refractedVector = refract(vec3(0.0, 0.0, -1.0), sphereNormal, refractiveIndex);\n\ngl_FragColor = texture2D(inputImageTexture, (refractedVector.xy + 1.0) * 0.5) * checkForPresenceWithinSphere;     \n}\n");
    this.center = paramPointF;
    this.radius = paramFloat1;
    this.refractiveIndex = paramFloat2;
  }
  
  private void setAspectRatio(float paramFloat) {
    this.aspectRatio = paramFloat;
    setFloat(this.aspectRatioLocation, paramFloat);
  }
  
  public void onInit() {
    super.onInit();
    this.centerLocation = GLES20.glGetUniformLocation(getProgram(), "center");
    this.radiusLocation = GLES20.glGetUniformLocation(getProgram(), "radius");
    this.aspectRatioLocation = GLES20.glGetUniformLocation(getProgram(), "aspectRatio");
    this.refractiveIndexLocation = GLES20.glGetUniformLocation(getProgram(), "refractiveIndex");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setAspectRatio(this.aspectRatio);
    setRadius(this.radius);
    setCenter(this.center);
    setRefractiveIndex(this.refractiveIndex);
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
  
  public void setRefractiveIndex(float paramFloat) {
    this.refractiveIndex = paramFloat;
    setFloat(this.refractiveIndexLocation, paramFloat);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageSphereRefractionFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
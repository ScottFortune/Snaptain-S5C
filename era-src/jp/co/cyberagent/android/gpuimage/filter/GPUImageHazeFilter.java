package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImageHazeFilter extends GPUImageFilter {
  public static final String HAZE_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nuniform lowp float distance;\nuniform highp float slope;\n\nvoid main()\n{\n\t//todo reconsider precision modifiers\t \n\t highp vec4 color = vec4(1.0);//todo reimplement as a parameter\n\n\t highp float  d = textureCoordinate.y * slope  +  distance; \n\n\t highp vec4 c = texture2D(inputImageTexture, textureCoordinate) ; // consider using unpremultiply\n\n\t c = (c - d * color) / (1.0 -d);\n\n\t gl_FragColor = c; //consider using premultiply(c);\n}\n";
  
  private float distance;
  
  private int distanceLocation;
  
  private float slope;
  
  private int slopeLocation;
  
  public GPUImageHazeFilter() {
    this(0.2F, 0.0F);
  }
  
  public GPUImageHazeFilter(float paramFloat1, float paramFloat2) {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nuniform lowp float distance;\nuniform highp float slope;\n\nvoid main()\n{\n\t//todo reconsider precision modifiers\t \n\t highp vec4 color = vec4(1.0);//todo reimplement as a parameter\n\n\t highp float  d = textureCoordinate.y * slope  +  distance; \n\n\t highp vec4 c = texture2D(inputImageTexture, textureCoordinate) ; // consider using unpremultiply\n\n\t c = (c - d * color) / (1.0 -d);\n\n\t gl_FragColor = c; //consider using premultiply(c);\n}\n");
    this.distance = paramFloat1;
    this.slope = paramFloat2;
  }
  
  public void onInit() {
    super.onInit();
    this.distanceLocation = GLES20.glGetUniformLocation(getProgram(), "distance");
    this.slopeLocation = GLES20.glGetUniformLocation(getProgram(), "slope");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setDistance(this.distance);
    setSlope(this.slope);
  }
  
  public void setDistance(float paramFloat) {
    this.distance = paramFloat;
    setFloat(this.distanceLocation, paramFloat);
  }
  
  public void setSlope(float paramFloat) {
    this.slope = paramFloat;
    setFloat(this.slopeLocation, paramFloat);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageHazeFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImageCrosshatchFilter extends GPUImageFilter {
  public static final String CROSSHATCH_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\nuniform sampler2D inputImageTexture;\nuniform highp float crossHatchSpacing;\nuniform highp float lineWidth;\nconst highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\nvoid main()\n{\nhighp float luminance = dot(texture2D(inputImageTexture, textureCoordinate).rgb, W);\nlowp vec4 colorToDisplay = vec4(1.0, 1.0, 1.0, 1.0);\nif (luminance < 1.00)\n{\nif (mod(textureCoordinate.x + textureCoordinate.y, crossHatchSpacing) <= lineWidth)\n{\ncolorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n}\n}\nif (luminance < 0.75)\n{\nif (mod(textureCoordinate.x - textureCoordinate.y, crossHatchSpacing) <= lineWidth)\n{\ncolorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n}\n}\nif (luminance < 0.50)\n{\nif (mod(textureCoordinate.x + textureCoordinate.y - (crossHatchSpacing / 2.0), crossHatchSpacing) <= lineWidth)\n{\ncolorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n}\n}\nif (luminance < 0.3)\n{\nif (mod(textureCoordinate.x - textureCoordinate.y - (crossHatchSpacing / 2.0), crossHatchSpacing) <= lineWidth)\n{\ncolorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n}\n}\ngl_FragColor = colorToDisplay;\n}\n";
  
  private float crossHatchSpacing;
  
  private int crossHatchSpacingLocation;
  
  private float lineWidth;
  
  private int lineWidthLocation;
  
  public GPUImageCrosshatchFilter() {
    this(0.03F, 0.003F);
  }
  
  public GPUImageCrosshatchFilter(float paramFloat1, float paramFloat2) {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "varying highp vec2 textureCoordinate;\nuniform sampler2D inputImageTexture;\nuniform highp float crossHatchSpacing;\nuniform highp float lineWidth;\nconst highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\nvoid main()\n{\nhighp float luminance = dot(texture2D(inputImageTexture, textureCoordinate).rgb, W);\nlowp vec4 colorToDisplay = vec4(1.0, 1.0, 1.0, 1.0);\nif (luminance < 1.00)\n{\nif (mod(textureCoordinate.x + textureCoordinate.y, crossHatchSpacing) <= lineWidth)\n{\ncolorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n}\n}\nif (luminance < 0.75)\n{\nif (mod(textureCoordinate.x - textureCoordinate.y, crossHatchSpacing) <= lineWidth)\n{\ncolorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n}\n}\nif (luminance < 0.50)\n{\nif (mod(textureCoordinate.x + textureCoordinate.y - (crossHatchSpacing / 2.0), crossHatchSpacing) <= lineWidth)\n{\ncolorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n}\n}\nif (luminance < 0.3)\n{\nif (mod(textureCoordinate.x - textureCoordinate.y - (crossHatchSpacing / 2.0), crossHatchSpacing) <= lineWidth)\n{\ncolorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);\n}\n}\ngl_FragColor = colorToDisplay;\n}\n");
    this.crossHatchSpacing = paramFloat1;
    this.lineWidth = paramFloat2;
  }
  
  public void onInit() {
    super.onInit();
    this.crossHatchSpacingLocation = GLES20.glGetUniformLocation(getProgram(), "crossHatchSpacing");
    this.lineWidthLocation = GLES20.glGetUniformLocation(getProgram(), "lineWidth");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setCrossHatchSpacing(this.crossHatchSpacing);
    setLineWidth(this.lineWidth);
  }
  
  public void setCrossHatchSpacing(float paramFloat) {
    float f;
    if (getOutputWidth() != 0) {
      f = 1.0F / getOutputWidth();
    } else {
      f = 4.8828125E-4F;
    } 
    if (paramFloat < f) {
      this.crossHatchSpacing = f;
    } else {
      this.crossHatchSpacing = paramFloat;
    } 
    setFloat(this.crossHatchSpacingLocation, this.crossHatchSpacing);
  }
  
  public void setLineWidth(float paramFloat) {
    this.lineWidth = paramFloat;
    setFloat(this.lineWidthLocation, this.lineWidth);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageCrosshatchFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
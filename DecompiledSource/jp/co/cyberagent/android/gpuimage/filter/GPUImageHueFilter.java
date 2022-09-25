package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImageHueFilter extends GPUImageFilter {
  public static final String HUE_FRAGMENT_SHADER = "precision highp float;\nvarying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\nuniform mediump float hueAdjust;\nconst highp vec4 kRGBToYPrime = vec4 (0.299, 0.587, 0.114, 0.0);\nconst highp vec4 kRGBToI = vec4 (0.595716, -0.274453, -0.321263, 0.0);\nconst highp vec4 kRGBToQ = vec4 (0.211456, -0.522591, 0.31135, 0.0);\n\nconst highp vec4 kYIQToR = vec4 (1.0, 0.9563, 0.6210, 0.0);\nconst highp vec4 kYIQToG = vec4 (1.0, -0.2721, -0.6474, 0.0);\nconst highp vec4 kYIQToB = vec4 (1.0, -1.1070, 1.7046, 0.0);\n\nvoid main ()\n{\n    // Sample the input pixel\n    highp vec4 color = texture2D(inputImageTexture, textureCoordinate);\n\n    // Convert to YIQ\n    highp float YPrime = dot (color, kRGBToYPrime);\n    highp float I = dot (color, kRGBToI);\n    highp float Q = dot (color, kRGBToQ);\n\n    // Calculate the hue and chroma\n    highp float hue = atan (Q, I);\n    highp float chroma = sqrt (I * I + Q * Q);\n\n    // Make the user's adjustments\n    hue += (-hueAdjust); //why negative rotation?\n\n    // Convert back to YIQ\n    Q = chroma * sin (hue);\n    I = chroma * cos (hue);\n\n    // Convert back to RGB\n    highp vec4 yIQ = vec4 (YPrime, I, Q, 0.0);\n    color.r = dot (yIQ, kYIQToR);\n    color.g = dot (yIQ, kYIQToG);\n    color.b = dot (yIQ, kYIQToB);\n\n    // Save the result\n    gl_FragColor = color;\n}\n";
  
  private float hue;
  
  private int hueLocation;
  
  public GPUImageHueFilter() {
    this(90.0F);
  }
  
  public GPUImageHueFilter(float paramFloat) {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "precision highp float;\nvarying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\nuniform mediump float hueAdjust;\nconst highp vec4 kRGBToYPrime = vec4 (0.299, 0.587, 0.114, 0.0);\nconst highp vec4 kRGBToI = vec4 (0.595716, -0.274453, -0.321263, 0.0);\nconst highp vec4 kRGBToQ = vec4 (0.211456, -0.522591, 0.31135, 0.0);\n\nconst highp vec4 kYIQToR = vec4 (1.0, 0.9563, 0.6210, 0.0);\nconst highp vec4 kYIQToG = vec4 (1.0, -0.2721, -0.6474, 0.0);\nconst highp vec4 kYIQToB = vec4 (1.0, -1.1070, 1.7046, 0.0);\n\nvoid main ()\n{\n    // Sample the input pixel\n    highp vec4 color = texture2D(inputImageTexture, textureCoordinate);\n\n    // Convert to YIQ\n    highp float YPrime = dot (color, kRGBToYPrime);\n    highp float I = dot (color, kRGBToI);\n    highp float Q = dot (color, kRGBToQ);\n\n    // Calculate the hue and chroma\n    highp float hue = atan (Q, I);\n    highp float chroma = sqrt (I * I + Q * Q);\n\n    // Make the user's adjustments\n    hue += (-hueAdjust); //why negative rotation?\n\n    // Convert back to YIQ\n    Q = chroma * sin (hue);\n    I = chroma * cos (hue);\n\n    // Convert back to RGB\n    highp vec4 yIQ = vec4 (YPrime, I, Q, 0.0);\n    color.r = dot (yIQ, kYIQToR);\n    color.g = dot (yIQ, kYIQToG);\n    color.b = dot (yIQ, kYIQToB);\n\n    // Save the result\n    gl_FragColor = color;\n}\n");
    this.hue = paramFloat;
  }
  
  public void onInit() {
    super.onInit();
    this.hueLocation = GLES20.glGetUniformLocation(getProgram(), "hueAdjust");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setHue(this.hue);
  }
  
  public void setHue(float paramFloat) {
    this.hue = paramFloat;
    paramFloat = this.hue % 360.0F * 3.1415927F / 180.0F;
    setFloat(this.hueLocation, paramFloat);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageHueFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
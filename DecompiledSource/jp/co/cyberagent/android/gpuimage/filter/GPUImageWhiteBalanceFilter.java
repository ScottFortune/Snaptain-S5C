package jp.co.cyberagent.android.gpuimage.filter;

import android.opengl.GLES20;

public class GPUImageWhiteBalanceFilter extends GPUImageFilter {
  public static final String WHITE_BALANCE_FRAGMENT_SHADER = "uniform sampler2D inputImageTexture;\nvarying highp vec2 textureCoordinate;\n \nuniform lowp float temperature;\nuniform lowp float tint;\n\nconst lowp vec3 warmFilter = vec3(0.93, 0.54, 0.0);\n\nconst mediump mat3 RGBtoYIQ = mat3(0.299, 0.587, 0.114, 0.596, -0.274, -0.322, 0.212, -0.523, 0.311);\nconst mediump mat3 YIQtoRGB = mat3(1.0, 0.956, 0.621, 1.0, -0.272, -0.647, 1.0, -1.105, 1.702);\n\nvoid main()\n{\n\tlowp vec4 source = texture2D(inputImageTexture, textureCoordinate);\n\t\n\tmediump vec3 yiq = RGBtoYIQ * source.rgb; //adjusting tint\n\tyiq.b = clamp(yiq.b + tint*0.5226*0.1, -0.5226, 0.5226);\n\tlowp vec3 rgb = YIQtoRGB * yiq;\n\n\tlowp vec3 processed = vec3(\n\t\t(rgb.r < 0.5 ? (2.0 * rgb.r * warmFilter.r) : (1.0 - 2.0 * (1.0 - rgb.r) * (1.0 - warmFilter.r))), //adjusting temperature\n\t\t(rgb.g < 0.5 ? (2.0 * rgb.g * warmFilter.g) : (1.0 - 2.0 * (1.0 - rgb.g) * (1.0 - warmFilter.g))), \n\t\t(rgb.b < 0.5 ? (2.0 * rgb.b * warmFilter.b) : (1.0 - 2.0 * (1.0 - rgb.b) * (1.0 - warmFilter.b))));\n\n\tgl_FragColor = vec4(mix(rgb, processed, temperature), source.a);\n}";
  
  private float temperature;
  
  private int temperatureLocation;
  
  private float tint;
  
  private int tintLocation;
  
  public GPUImageWhiteBalanceFilter() {
    this(5000.0F, 0.0F);
  }
  
  public GPUImageWhiteBalanceFilter(float paramFloat1, float paramFloat2) {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "uniform sampler2D inputImageTexture;\nvarying highp vec2 textureCoordinate;\n \nuniform lowp float temperature;\nuniform lowp float tint;\n\nconst lowp vec3 warmFilter = vec3(0.93, 0.54, 0.0);\n\nconst mediump mat3 RGBtoYIQ = mat3(0.299, 0.587, 0.114, 0.596, -0.274, -0.322, 0.212, -0.523, 0.311);\nconst mediump mat3 YIQtoRGB = mat3(1.0, 0.956, 0.621, 1.0, -0.272, -0.647, 1.0, -1.105, 1.702);\n\nvoid main()\n{\n\tlowp vec4 source = texture2D(inputImageTexture, textureCoordinate);\n\t\n\tmediump vec3 yiq = RGBtoYIQ * source.rgb; //adjusting tint\n\tyiq.b = clamp(yiq.b + tint*0.5226*0.1, -0.5226, 0.5226);\n\tlowp vec3 rgb = YIQtoRGB * yiq;\n\n\tlowp vec3 processed = vec3(\n\t\t(rgb.r < 0.5 ? (2.0 * rgb.r * warmFilter.r) : (1.0 - 2.0 * (1.0 - rgb.r) * (1.0 - warmFilter.r))), //adjusting temperature\n\t\t(rgb.g < 0.5 ? (2.0 * rgb.g * warmFilter.g) : (1.0 - 2.0 * (1.0 - rgb.g) * (1.0 - warmFilter.g))), \n\t\t(rgb.b < 0.5 ? (2.0 * rgb.b * warmFilter.b) : (1.0 - 2.0 * (1.0 - rgb.b) * (1.0 - warmFilter.b))));\n\n\tgl_FragColor = vec4(mix(rgb, processed, temperature), source.a);\n}");
    this.temperature = paramFloat1;
    this.tint = paramFloat2;
  }
  
  public void onInit() {
    super.onInit();
    this.temperatureLocation = GLES20.glGetUniformLocation(getProgram(), "temperature");
    this.tintLocation = GLES20.glGetUniformLocation(getProgram(), "tint");
  }
  
  public void onInitialized() {
    super.onInitialized();
    setTemperature(this.temperature);
    setTint(this.tint);
  }
  
  public void setTemperature(float paramFloat) {
    double d1;
    this.temperature = paramFloat;
    int i = this.temperatureLocation;
    paramFloat = this.temperature;
    if (paramFloat < 5000.0F) {
      d1 = 4.0E-4D;
    } else {
      d1 = 6.0E-5D;
    } 
    double d2 = paramFloat;
    Double.isNaN(d2);
    setFloat(i, (float)((d2 - 5000.0D) * d1));
  }
  
  public void setTint(float paramFloat) {
    this.tint = paramFloat;
    int i = this.tintLocation;
    double d = this.tint;
    Double.isNaN(d);
    setFloat(i, (float)(d / 100.0D));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageWhiteBalanceFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package jp.co.cyberagent.android.gpuimage.filter;

public class GPUImageLuminanceFilter extends GPUImageFilter {
  public static final String LUMINANCE_FRAGMENT_SHADER = "precision highp float;\n\nvarying vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\n// Values from \"Graphics Shaders: Theory and Practice\" by Bailey and Cunningham\nconst highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\n\nvoid main()\n{\n    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n    float luminance = dot(textureColor.rgb, W);\n    \n    gl_FragColor = vec4(vec3(luminance), textureColor.a);\n}";
  
  public GPUImageLuminanceFilter() {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "precision highp float;\n\nvarying vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\n// Values from \"Graphics Shaders: Theory and Practice\" by Bailey and Cunningham\nconst highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\n\nvoid main()\n{\n    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n    float luminance = dot(textureColor.rgb, W);\n    \n    gl_FragColor = vec4(vec3(luminance), textureColor.a);\n}");
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageLuminanceFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
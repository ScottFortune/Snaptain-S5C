package com.netopsun.ijkvideoview.extra.filter_choose_popupwindows.filter;

import jp.co.cyberagent.android.gpuimage.filter.GPUImage3x3TextureSamplingFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter;

public class MyGPUImageSobelEdgeDetectionFilter extends MyGPUImageFilterGroup {
  public static final String SOBEL_EDGE_DETECTION = "precision mediump float;\n\nvarying vec2 textureCoordinate;\nvarying vec2 leftTextureCoordinate;\nvarying vec2 rightTextureCoordinate;\n\nvarying vec2 topTextureCoordinate;\nvarying vec2 topLeftTextureCoordinate;\nvarying vec2 topRightTextureCoordinate;\n\nvarying vec2 bottomTextureCoordinate;\nvarying vec2 bottomLeftTextureCoordinate;\nvarying vec2 bottomRightTextureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nvoid main()\n{\n    float bottomLeftIntensity = texture2D(inputImageTexture, bottomLeftTextureCoordinate).r;\n    float topRightIntensity = texture2D(inputImageTexture, topRightTextureCoordinate).r;\n    float topLeftIntensity = texture2D(inputImageTexture, topLeftTextureCoordinate).r;\n    float bottomRightIntensity = texture2D(inputImageTexture, bottomRightTextureCoordinate).r;\n    float leftIntensity = texture2D(inputImageTexture, leftTextureCoordinate).r;\n    float rightIntensity = texture2D(inputImageTexture, rightTextureCoordinate).r;\n    float bottomIntensity = texture2D(inputImageTexture, bottomTextureCoordinate).r;\n    float topIntensity = texture2D(inputImageTexture, topTextureCoordinate).r;\n    float h = -topLeftIntensity - 2.0 * topIntensity - topRightIntensity + bottomLeftIntensity + 2.0 * bottomIntensity + bottomRightIntensity;\n    float v = -bottomLeftIntensity - 2.0 * leftIntensity - topLeftIntensity + bottomRightIntensity + 2.0 * rightIntensity + topRightIntensity;\n\n    float mag = length(vec2(h, v));\n\n    gl_FragColor = vec4(vec3(mag), 1.0);\n}";
  
  public MyGPUImageSobelEdgeDetectionFilter() {
    addFilter((GPUImageFilter)new GPUImageGrayscaleFilter());
    addFilter((GPUImageFilter)new GPUImage3x3TextureSamplingFilter("precision mediump float;\n\nvarying vec2 textureCoordinate;\nvarying vec2 leftTextureCoordinate;\nvarying vec2 rightTextureCoordinate;\n\nvarying vec2 topTextureCoordinate;\nvarying vec2 topLeftTextureCoordinate;\nvarying vec2 topRightTextureCoordinate;\n\nvarying vec2 bottomTextureCoordinate;\nvarying vec2 bottomLeftTextureCoordinate;\nvarying vec2 bottomRightTextureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nvoid main()\n{\n    float bottomLeftIntensity = texture2D(inputImageTexture, bottomLeftTextureCoordinate).r;\n    float topRightIntensity = texture2D(inputImageTexture, topRightTextureCoordinate).r;\n    float topLeftIntensity = texture2D(inputImageTexture, topLeftTextureCoordinate).r;\n    float bottomRightIntensity = texture2D(inputImageTexture, bottomRightTextureCoordinate).r;\n    float leftIntensity = texture2D(inputImageTexture, leftTextureCoordinate).r;\n    float rightIntensity = texture2D(inputImageTexture, rightTextureCoordinate).r;\n    float bottomIntensity = texture2D(inputImageTexture, bottomTextureCoordinate).r;\n    float topIntensity = texture2D(inputImageTexture, topTextureCoordinate).r;\n    float h = -topLeftIntensity - 2.0 * topIntensity - topRightIntensity + bottomLeftIntensity + 2.0 * bottomIntensity + bottomRightIntensity;\n    float v = -bottomLeftIntensity - 2.0 * leftIntensity - topLeftIntensity + bottomRightIntensity + 2.0 * rightIntensity + topRightIntensity;\n\n    float mag = length(vec2(h, v));\n\n    gl_FragColor = vec4(vec3(mag), 1.0);\n}"));
  }
  
  public void setLineSize(float paramFloat) {
    ((GPUImage3x3TextureSamplingFilter)getFilters().get(1)).setLineSize(paramFloat);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/extra/filter_choose_popupwindows/filter/MyGPUImageSobelEdgeDetectionFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
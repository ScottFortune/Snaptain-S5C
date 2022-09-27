package com.netopsun.ijkvideoview.extra.filter_choose_popupwindows.filter;

import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jp.co.cyberagent.android.gpuimage.GPUImageRenderer;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.util.Rotation;
import jp.co.cyberagent.android.gpuimage.util.TextureRotationUtil;

public class MyGPUImageFilterGroup extends GPUImageFilter {
  private int fboFrameBuffer = 0;
  
  private List<GPUImageFilter> filters;
  
  private int[] frameBufferTextures;
  
  private int[] frameBuffers;
  
  private final FloatBuffer glCubeBuffer;
  
  private final FloatBuffer glTextureBuffer;
  
  private final FloatBuffer glTextureFlipBuffer;
  
  private List<GPUImageFilter> mergedFilters;
  
  public MyGPUImageFilterGroup() {
    this((List<GPUImageFilter>)null);
  }
  
  public MyGPUImageFilterGroup(List<GPUImageFilter> paramList) {
    this.filters = paramList;
    if (this.filters == null) {
      this.filters = new ArrayList<GPUImageFilter>();
    } else {
      updateMergedFilters();
    } 
    this.glCubeBuffer = ByteBuffer.allocateDirect(GPUImageRenderer.CUBE.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    this.glCubeBuffer.put(GPUImageRenderer.CUBE).position(0);
    this.glTextureBuffer = ByteBuffer.allocateDirect(TextureRotationUtil.TEXTURE_NO_ROTATION.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    this.glTextureBuffer.put(TextureRotationUtil.TEXTURE_NO_ROTATION).position(0);
    float[] arrayOfFloat = TextureRotationUtil.getRotation(Rotation.NORMAL, false, true);
    this.glTextureFlipBuffer = ByteBuffer.allocateDirect(arrayOfFloat.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    this.glTextureFlipBuffer.put(arrayOfFloat).position(0);
  }
  
  private void destroyFramebuffers() {
    int[] arrayOfInt = this.frameBufferTextures;
    if (arrayOfInt != null) {
      GLES20.glDeleteTextures(arrayOfInt.length, arrayOfInt, 0);
      this.frameBufferTextures = null;
    } 
    arrayOfInt = this.frameBuffers;
    if (arrayOfInt != null) {
      GLES20.glDeleteFramebuffers(arrayOfInt.length, arrayOfInt, 0);
      this.frameBuffers = null;
    } 
  }
  
  public void addFilter(GPUImageFilter paramGPUImageFilter) {
    if (paramGPUImageFilter == null)
      return; 
    this.filters.add(paramGPUImageFilter);
    updateMergedFilters();
  }
  
  public List<GPUImageFilter> getFilters() {
    return this.filters;
  }
  
  public List<GPUImageFilter> getMergedFilters() {
    return this.mergedFilters;
  }
  
  public void onDestroy() {
    destroyFramebuffers();
    Iterator<GPUImageFilter> iterator = this.filters.iterator();
    while (iterator.hasNext())
      ((GPUImageFilter)iterator.next()).destroy(); 
    super.onDestroy();
  }
  
  public void onDraw(int paramInt, FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2) {
    runPendingOnDrawTasks();
    if (isInitialized() && this.frameBuffers != null && this.frameBufferTextures != null) {
      List<GPUImageFilter> list = this.mergedFilters;
      if (list != null) {
        int i = list.size();
        int j = paramInt;
        for (paramInt = 0; paramInt < i; paramInt++) {
          boolean bool;
          GPUImageFilter gPUImageFilter = this.mergedFilters.get(paramInt);
          int k = i - 1;
          if (paramInt < k) {
            bool = true;
          } else {
            bool = false;
          } 
          if (bool) {
            GLES20.glBindFramebuffer(36160, this.frameBuffers[paramInt]);
            GLES20.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
          } 
          if (paramInt == 0) {
            gPUImageFilter.onDraw(j, paramFloatBuffer1, paramFloatBuffer2);
          } else if (paramInt == k) {
            FloatBuffer floatBuffer1;
            FloatBuffer floatBuffer2 = this.glCubeBuffer;
            if (i % 2 == 0) {
              floatBuffer1 = this.glTextureFlipBuffer;
            } else {
              floatBuffer1 = this.glTextureBuffer;
            } 
            gPUImageFilter.onDraw(j, floatBuffer2, floatBuffer1);
          } else {
            gPUImageFilter.onDraw(j, this.glCubeBuffer, this.glTextureBuffer);
          } 
          if (bool) {
            GLES20.glBindFramebuffer(36160, this.fboFrameBuffer);
            j = this.frameBufferTextures[paramInt];
          } 
        } 
      } 
    } 
  }
  
  public void onInit() {
    super.onInit();
    Iterator<GPUImageFilter> iterator = this.filters.iterator();
    while (iterator.hasNext())
      ((GPUImageFilter)iterator.next()).ifNeedInit(); 
  }
  
  public void onOutputSizeChanged(int paramInt1, int paramInt2) {
    super.onOutputSizeChanged(paramInt1, paramInt2);
    if (this.frameBuffers != null)
      destroyFramebuffers(); 
    int i = this.filters.size();
    byte b;
    for (b = 0; b < i; b++)
      ((GPUImageFilter)this.filters.get(b)).onOutputSizeChanged(paramInt1, paramInt2); 
    List<GPUImageFilter> list = this.mergedFilters;
    if (list != null && list.size() > 0) {
      i = this.mergedFilters.size() - 1;
      this.frameBuffers = new int[i];
      this.frameBufferTextures = new int[i];
      for (b = 0; b < i; b++) {
        GLES20.glGenFramebuffers(1, this.frameBuffers, b);
        GLES20.glGenTextures(1, this.frameBufferTextures, b);
        GLES20.glBindTexture(3553, this.frameBufferTextures[b]);
        GLES20.glTexImage2D(3553, 0, 6408, paramInt1, paramInt2, 0, 6408, 5121, null);
        GLES20.glTexParameterf(3553, 10240, 9729.0F);
        GLES20.glTexParameterf(3553, 10241, 9729.0F);
        GLES20.glTexParameterf(3553, 10242, 33071.0F);
        GLES20.glTexParameterf(3553, 10243, 33071.0F);
        GLES20.glBindFramebuffer(36160, this.frameBuffers[b]);
        GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.frameBufferTextures[b], 0);
        GLES20.glBindTexture(3553, 0);
        GLES20.glBindFramebuffer(36160, this.fboFrameBuffer);
      } 
    } 
  }
  
  public void setFboFrameBuffer(int paramInt) {
    this.fboFrameBuffer = paramInt;
  }
  
  public void updateMergedFilters() {
    if (this.filters == null)
      return; 
    List<GPUImageFilter> list = this.mergedFilters;
    if (list == null) {
      this.mergedFilters = new ArrayList<GPUImageFilter>();
    } else {
      list.clear();
    } 
    for (GPUImageFilter gPUImageFilter : this.filters) {
      List<? extends GPUImageFilter> list1;
      if (gPUImageFilter instanceof GPUImageFilterGroup) {
        GPUImageFilterGroup gPUImageFilterGroup = (GPUImageFilterGroup)gPUImageFilter;
        gPUImageFilterGroup.updateMergedFilters();
        list1 = gPUImageFilterGroup.getMergedFilters();
        if (list1 == null || list1.isEmpty())
          continue; 
        this.mergedFilters.addAll(list1);
        continue;
      } 
      this.mergedFilters.add(list1);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/extra/filter_choose_popupwindows/filter/MyGPUImageFilterGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.yang.firework;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class FireworkView extends GLSurfaceView {
  private Bitmap mBackground;
  
  private Context mContext;
  
  private ParticleLayer mParticleSystem;
  
  private int mPlistFileResourceId;
  
  private TextureLayer mTextureLayer;
  
  private int mTextureResourceId;
  
  public FireworkView(Context paramContext) {
    super(paramContext);
    init(paramContext, (AttributeSet)null);
  }
  
  public FireworkView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramContext, paramAttributeSet);
  }
  
  private void handleTypedArray(Context paramContext, AttributeSet paramAttributeSet) {
    if (paramAttributeSet == null)
      return; 
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.FireworkView);
    this.mTextureResourceId = typedArray.getResourceId(R.styleable.FireworkView_texture, 0);
    this.mPlistFileResourceId = typedArray.getResourceId(R.styleable.FireworkView_plistFile, 0);
    int i = typedArray.getResourceId(R.styleable.FireworkView_backgroundImage, 0);
    this.mBackground = BitmapFactory.decodeResource(paramContext.getResources(), i);
  }
  
  private void init(Context paramContext, AttributeSet paramAttributeSet) {
    handleTypedArray(paramContext, paramAttributeSet);
    this.mContext = paramContext;
    setEGLContextClientVersion(2);
    setRenderer(new FireworkRenderer());
  }
  
  public ParticleLayer getParticleSystem() {
    return this.mParticleSystem;
  }
  
  public TextureLayer getTextureLayer() {
    return this.mTextureLayer;
  }
  
  public void setParticleSystem(ParticleLayer paramParticleLayer) {
    this.mParticleSystem = paramParticleLayer;
  }
  
  public void setTextureLayer(TextureLayer paramTextureLayer) {
    this.mTextureLayer = paramTextureLayer;
  }
  
  public class FireworkRenderer implements GLSurfaceView.Renderer {
    public void onDrawFrame(GL10 param1GL10) {
      GLES20.glViewport(0, 0, FireworkView.this.getWidth(), FireworkView.this.getHeight());
      GLES20.glClear(16384);
      if (FireworkView.this.mBackground != null)
        FireworkView.this.mTextureLayer.draw(); 
      GLES20.glDepthMask(false);
      GLES20.glEnable(3042);
      GLES10.glBlendFunc(770, 1);
      FireworkView.this.mParticleSystem.update(0.016666668F);
      FireworkView.this.mParticleSystem.updateBuffer();
      FireworkView.this.mParticleSystem.draw();
    }
    
    public void onSurfaceChanged(GL10 param1GL10, int param1Int1, int param1Int2) {
      GLES20.glViewport(0, 0, param1Int1, param1Int2);
    }
    
    public void onSurfaceCreated(GL10 param1GL10, EGLConfig param1EGLConfig) {
      GLES20.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
      FireworkView fireworkView = FireworkView.this;
      FireworkView.access$002(fireworkView, new ParticleLayer(fireworkView.mContext, FireworkView.this.mPlistFileResourceId, FireworkView.this.mTextureResourceId));
      if (FireworkView.this.mBackground != null) {
        fireworkView = FireworkView.this;
        FireworkView.access$502(fireworkView, new TextureLayer(fireworkView.mContext, FireworkView.this.mBackground));
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/yang/firework/FireworkView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
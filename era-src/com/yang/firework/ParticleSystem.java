package com.yang.firework;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSNumber;
import com.dd.plist.PropertyListParser;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class ParticleSystem {
  static final int DURATION_INFINITY = -1;
  
  static final int START_RADIUS_EQUAL_TO_END_RADIUS = -1;
  
  static final int START_SIZE_EQUAL_TO_END_SIZE = -1;
  
  static Random _random = new Random();
  
  int _allocatedParticles;
  
  float _angle;
  
  float _angleVar;
  
  int _atlasIndex;
  
  private boolean _batchNode = true;
  
  BlendFunc _blendFunc;
  
  String _configName = "";
  
  float _density;
  
  float _duration;
  
  float _elapsed;
  
  float _emissionRate;
  
  float _emitCounter;
  
  Mode _emitterMode = Mode.GRAVITY;
  
  Color4F _endColor = new Color4F();
  
  Color4F _endColorVar = new Color4F();
  
  float _endSize;
  
  float _endSizeVar;
  
  float _endSpin;
  
  float _endSpinVar;
  
  int _height;
  
  boolean _isActive = true;
  
  boolean _isAutoRemoveOnFinish = false;
  
  boolean _isBlendAdditive = false;
  
  float _life;
  
  float _lifeVar;
  
  boolean _opacityModifyRGB = false;
  
  int _particleCount = 0;
  
  int _particleIdx;
  
  List<Particle> _particles = new ArrayList<Particle>();
  
  String _plistFile = "";
  
  Vec2 _posVar = new Vec2(0.0F, 0.0F);
  
  Vec2 _position = new Vec2(0.0F, 0.0F);
  
  PositionType _positionType;
  
  Vec2 _sourcePosition = new Vec2(0.0F, 0.0F);
  
  Color4F _startColor = new Color4F();
  
  Color4F _startColorVar = new Color4F();
  
  float _startSize;
  
  float _startSizeVar;
  
  float _startSpin;
  
  float _startSpinVar;
  
  int _textureResourceId;
  
  int _totalParticles;
  
  boolean _transformSystemDirty = false;
  
  private boolean _unschedule = false;
  
  boolean _visible = true;
  
  int _width;
  
  int _yCoordFlipped = 1;
  
  Context context;
  
  float gravityX;
  
  float gravityY;
  
  ModeA modeA = new ModeA();
  
  ModeB modeB = new ModeB();
  
  ParticleSystem(Context paramContext, int paramInt) {
    this.context = paramContext;
    initWithTotalParticles(paramInt);
  }
  
  ParticleSystem(Context paramContext, int paramInt1, int paramInt2) {
    this.context = paramContext;
    initWithFile(paramInt1, paramInt2);
  }
  
  public static float CCRANDOM_MINUS1_1() {
    return _random.nextFloat() * 2.0F - 1.0F;
  }
  
  public static float CC_DEGREES_TO_RADIANS(float paramFloat) {
    return paramFloat * 0.017453292F;
  }
  
  public static float CC_RADIANS_TO_DEGREES(float paramFloat) {
    return paramFloat * 57.29578F;
  }
  
  public static float clampf(float paramFloat1, float paramFloat2, float paramFloat3) {
    return Math.max(paramFloat2, Math.min(paramFloat3, paramFloat1));
  }
  
  private void initWithFile(int paramInt1, int paramInt2) {
    InputStream inputStream = this.context.getResources().openRawResource(paramInt1);
    try {
      NSDictionary nSDictionary = (NSDictionary)PropertyListParser.parse(inputStream);
      paramInt1 = ((NSNumber)nSDictionary.get("maxParticles")).intValue();
      setTexture(paramInt2);
      if (initWithTotalParticles(paramInt1)) {
        this._angle = ((NSNumber)nSDictionary.get("angle")).floatValue();
        this._angleVar = ((NSNumber)nSDictionary.get("angleVariance")).floatValue();
        this._duration = ((NSNumber)nSDictionary.get("duration")).floatValue();
        this._blendFunc.src = ((NSNumber)nSDictionary.get("blendFuncSource")).intValue();
        this._blendFunc.dst = ((NSNumber)nSDictionary.get("blendFuncDestination")).intValue();
        this._startColor.r = ((NSNumber)nSDictionary.get("startColorRed")).floatValue();
        this._startColor.g = ((NSNumber)nSDictionary.get("startColorGreen")).floatValue();
        this._startColor.b = ((NSNumber)nSDictionary.get("startColorBlue")).floatValue();
        this._startColor.a = ((NSNumber)nSDictionary.get("startColorAlpha")).floatValue();
        this._startColorVar.r = ((NSNumber)nSDictionary.get("startColorVarianceRed")).floatValue();
        this._startColorVar.g = ((NSNumber)nSDictionary.get("startColorVarianceGreen")).floatValue();
        this._startColorVar.b = ((NSNumber)nSDictionary.get("startColorVarianceBlue")).floatValue();
        this._startColorVar.a = ((NSNumber)nSDictionary.get("startColorVarianceAlpha")).floatValue();
        this._endColor.r = ((NSNumber)nSDictionary.get("finishColorRed")).floatValue();
        this._endColor.g = ((NSNumber)nSDictionary.get("finishColorGreen")).floatValue();
        this._endColor.b = ((NSNumber)nSDictionary.get("finishColorBlue")).floatValue();
        this._endColor.a = ((NSNumber)nSDictionary.get("finishColorAlpha")).floatValue();
        this._endColorVar.r = ((NSNumber)nSDictionary.get("finishColorVarianceRed")).floatValue();
        this._endColorVar.g = ((NSNumber)nSDictionary.get("finishColorVarianceGreen")).floatValue();
        this._endColorVar.b = ((NSNumber)nSDictionary.get("finishColorVarianceBlue")).floatValue();
        this._endColorVar.a = ((NSNumber)nSDictionary.get("finishColorVarianceAlpha")).floatValue();
        this._startSize = ((NSNumber)nSDictionary.get("startParticleSize")).floatValue();
        this._startSizeVar = ((NSNumber)nSDictionary.get("startParticleSizeVariance")).floatValue();
        this._endSize = ((NSNumber)nSDictionary.get("finishParticleSize")).floatValue();
        this._endSizeVar = ((NSNumber)nSDictionary.get("finishParticleSizeVariance")).floatValue();
        float f1 = ((NSNumber)nSDictionary.get("sourcePositionx")).floatValue();
        float f2 = ((NSNumber)nSDictionary.get("sourcePositiony")).floatValue();
        Vec2 vec2 = new Vec2();
        this(f1, f2);
        this._position = vec2;
        this._posVar.x = ((NSNumber)nSDictionary.get("sourcePositionVariancex")).floatValue();
        this._posVar.y = ((NSNumber)nSDictionary.get("sourcePositionVariancey")).floatValue();
        this._startSpin = ((NSNumber)nSDictionary.get("rotationStart")).floatValue();
        this._startSpinVar = ((NSNumber)nSDictionary.get("rotationStartVariance")).floatValue();
        this._endSpin = ((NSNumber)nSDictionary.get("rotationEnd")).floatValue();
        this._endSpinVar = ((NSNumber)nSDictionary.get("rotationEndVariance")).floatValue();
        if (((NSNumber)nSDictionary.get("emitterType")).intValue() == 0) {
          this._emitterMode = Mode.GRAVITY;
        } else if (((NSNumber)nSDictionary.get("emitterType")).intValue() == 1) {
          this._emitterMode = Mode.RADIUS;
        } 
        if (this._emitterMode == Mode.GRAVITY) {
          this.modeA.gravity.x = ((NSNumber)nSDictionary.get("gravityx")).floatValue();
          this.modeA.gravity.y = ((NSNumber)nSDictionary.get("gravityy")).floatValue();
          this.gravityX = ((NSNumber)nSDictionary.get("gravityx")).floatValue();
          this.gravityY = ((NSNumber)nSDictionary.get("gravityy")).floatValue();
          this.modeA.speed = ((NSNumber)nSDictionary.get("speed")).floatValue();
          this.modeA.speedVar = ((NSNumber)nSDictionary.get("speedVariance")).floatValue();
          this.modeA.radialAccel = ((NSNumber)nSDictionary.get("radialAcceleration")).floatValue();
          this.modeA.radialAccelVar = ((NSNumber)nSDictionary.get("radialAccelVariance")).floatValue();
          this.modeA.tangentialAccel = ((NSNumber)nSDictionary.get("tangentialAcceleration")).floatValue();
          this.modeA.tangentialAccelVar = ((NSNumber)nSDictionary.get("tangentialAccelVariance")).floatValue();
        } else if (this._emitterMode == Mode.RADIUS) {
          this.modeB.startRadius = ((NSNumber)nSDictionary.get("maxRadius")).floatValue();
          this.modeB.startRadiusVar = ((NSNumber)nSDictionary.get("maxRadiusVariance")).floatValue();
          this.modeB.endRadius = ((NSNumber)nSDictionary.get("minRadius")).floatValue();
          if (nSDictionary.get("minRadiusVariance") != null) {
            this.modeB.endRadiusVar = ((NSNumber)nSDictionary.get("minRadiusVariance")).floatValue();
          } else {
            this.modeB.endRadiusVar = 0.0F;
          } 
          this.modeB.rotatePerSecond = ((NSNumber)nSDictionary.get("rotatePerSecond")).floatValue();
          this.modeB.rotatePerSecondVar = ((NSNumber)nSDictionary.get("rotatePerSecondVariance")).floatValue();
        } 
        this._life = ((NSNumber)nSDictionary.get("particleLifespan")).floatValue();
        this._lifeVar = ((NSNumber)nSDictionary.get("particleLifespanVariance")).floatValue();
        this._emissionRate = this._totalParticles / this._life;
      } 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  boolean addParticle() {
    if (isFull())
      return false; 
    initParticle(this._particles.get(this._particleCount));
    this._particleCount++;
    return true;
  }
  
  abstract void clearData();
  
  public abstract void draw();
  
  void initParticle(Particle paramParticle) {
    paramParticle.timeToLive = this._life + this._lifeVar * CCRANDOM_MINUS1_1();
    paramParticle.timeToLive = Math.abs(paramParticle.timeToLive);
    paramParticle.timeToLive = Math.max(0.1F, paramParticle.timeToLive);
    this._sourcePosition.x += this._posVar.x * CCRANDOM_MINUS1_1();
    this._sourcePosition.y += this._posVar.y * CCRANDOM_MINUS1_1();
    Color4F color4F1 = new Color4F();
    color4F1.r = clampf(this._startColor.r + this._startColorVar.r * CCRANDOM_MINUS1_1(), 0.0F, 1.0F);
    color4F1.g = clampf(this._startColor.g + this._startColorVar.g * CCRANDOM_MINUS1_1(), 0.0F, 1.0F);
    color4F1.b = clampf(this._startColor.b + this._startColorVar.b * CCRANDOM_MINUS1_1(), 0.0F, 1.0F);
    color4F1.a = clampf(this._startColor.a + this._startColorVar.a * CCRANDOM_MINUS1_1(), 0.0F, 1.0F);
    Color4F color4F2 = new Color4F();
    color4F2.r = clampf(this._endColor.r + this._endColorVar.r * CCRANDOM_MINUS1_1(), 0.0F, 1.0F);
    color4F2.g = clampf(this._endColor.g + this._endColorVar.g * CCRANDOM_MINUS1_1(), 0.0F, 1.0F);
    color4F2.b = clampf(this._endColor.b + this._endColorVar.b * CCRANDOM_MINUS1_1(), 0.0F, 1.0F);
    color4F2.a = clampf(this._endColor.a + this._endColorVar.a * CCRANDOM_MINUS1_1(), 0.0F, 1.0F);
    paramParticle.color = color4F1;
    paramParticle.deltaColor.r = (color4F2.r - color4F1.r) / paramParticle.timeToLive;
    paramParticle.deltaColor.g = (color4F2.g - color4F1.g) / paramParticle.timeToLive;
    paramParticle.deltaColor.b = (color4F2.b - color4F1.b) / paramParticle.timeToLive;
    paramParticle.deltaColor.a = (color4F2.a - color4F1.a) / paramParticle.timeToLive;
    float f1 = Math.max(0.0F, this._startSize + this._startSizeVar * CCRANDOM_MINUS1_1());
    paramParticle.size = f1;
    float f2 = this._endSize;
    if (f2 == -1.0F) {
      paramParticle.deltaSize = 0.0F;
    } else {
      paramParticle.deltaSize = (Math.max(0.0F, f2 + this._endSizeVar * CCRANDOM_MINUS1_1()) - f1) / paramParticle.timeToLive;
    } 
    float f3 = this._startSpin + this._startSpinVar * CCRANDOM_MINUS1_1();
    f2 = this._endSpin;
    f1 = this._endSpinVar;
    float f4 = CCRANDOM_MINUS1_1();
    paramParticle.rotation = f3;
    paramParticle.deltaRotation = (f2 + f1 * f4 - f3) / paramParticle.timeToLive;
    if (this._positionType == PositionType.FREE) {
      paramParticle.startPos = new Vec2(0.0F, 0.0F);
    } else if (this._positionType == PositionType.RELATIVE) {
      paramParticle.startPos = this._position;
    } 
    float f5 = CC_DEGREES_TO_RADIANS(this._angle + this._angleVar * CCRANDOM_MINUS1_1());
    if (this._emitterMode == Mode.GRAVITY) {
      double d = f5;
      Vec2 vec2 = new Vec2((float)Math.cos(d), (float)Math.sin(d));
      f4 = this.modeA.speed;
      f1 = this.modeA.speedVar;
      f2 = CCRANDOM_MINUS1_1();
      paramParticle.modeA.dir = vec2.scale(f4 + f1 * f2);
      paramParticle.modeA.radialAccel = this.modeA.radialAccel + this.modeA.radialAccelVar * CCRANDOM_MINUS1_1();
      paramParticle.modeA.tangentialAccel = this.modeA.tangentialAccel + this.modeA.tangentialAccelVar * CCRANDOM_MINUS1_1();
      if (this.modeA.rotationIsDir)
        paramParticle.rotation = -CC_RADIANS_TO_DEGREES(paramParticle.modeA.dir.getAngle()); 
    } else {
      f4 = this.modeB.startRadius + this.modeB.startRadiusVar * CCRANDOM_MINUS1_1();
      f3 = this.modeB.endRadius;
      f1 = this.modeB.endRadiusVar;
      f2 = CCRANDOM_MINUS1_1();
      paramParticle.modeB.radius = f4;
      if (this.modeB.endRadius == -1.0F) {
        paramParticle.modeB.deltaRadius = 0.0F;
      } else {
        paramParticle.modeB.deltaRadius = (f3 + f1 * f2 - f4) / paramParticle.timeToLive;
      } 
      paramParticle.modeB.angle = f5;
      paramParticle.modeB.degreesPerSecond = CC_DEGREES_TO_RADIANS(this.modeB.rotatePerSecond + this.modeB.rotatePerSecondVar * CCRANDOM_MINUS1_1());
    } 
  }
  
  boolean initWithTotalParticles(int paramInt) {
    this._totalParticles = paramInt;
    this._particles = new ArrayList<Particle>(paramInt);
    for (byte b = 0; b < paramInt; b++)
      this._particles.add(new Particle()); 
    DisplayMetrics displayMetrics = this.context.getResources().getDisplayMetrics();
    this._density = displayMetrics.density;
    this._width = displayMetrics.widthPixels;
    this._height = displayMetrics.heightPixels;
    this._isActive = true;
    this._blendFunc = new BlendFunc(1, 771);
    this._positionType = PositionType.FREE;
    this._emitterMode = Mode.GRAVITY;
    this._isAutoRemoveOnFinish = false;
    this._transformSystemDirty = false;
    return true;
  }
  
  boolean isFull() {
    boolean bool;
    if (this._particleCount == this._totalParticles) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  void postStep() {}
  
  void resetSystem() {
    this._isActive = true;
    this._elapsed = 0.0F;
    int i = 0;
    while (true) {
      this._particleIdx = i;
      i = this._particleIdx;
      if (i < this._particleCount) {
        ((Particle)this._particles.get(i)).timeToLive = 0.0F;
        i = this._particleIdx + 1;
        continue;
      } 
      break;
    } 
  }
  
  abstract void setTexture(int paramInt);
  
  abstract void setTexture(Bitmap paramBitmap);
  
  public void setTotalParticles(int paramInt) {
    this._totalParticles = paramInt;
  }
  
  void stopSystem() {
    this._isActive = false;
    this._elapsed = this._duration;
    this._emitCounter = 0.0F;
  }
  
  public void update(float paramFloat) {
    if (this._isActive && this._emissionRate != 0.0F) {
      if (this._particleCount < this._totalParticles)
        this._emitCounter += paramFloat; 
      while (this._particleCount < this._totalParticles && this._emitCounter > 0.033333335F) {
        addParticle();
        this._emitCounter -= 0.033333335F;
      } 
      this._elapsed += paramFloat;
      if (this._duration != -1.0F)
        float f = this._elapsed; 
    } 
    this._particleIdx = 0;
    Vec2 vec21 = new Vec2(0.0F, 0.0F);
    if (this._positionType == PositionType.FREE) {
      vec21 = new Vec2(0.0F, 0.0F);
    } else if (this._positionType == PositionType.RELATIVE) {
      vec21 = this._position;
    } 
    clearData();
    Vec2 vec22 = new Vec2(0.0F, 0.0F);
    Vec2 vec23 = new Vec2(0.0F, 0.0F);
    Vec2 vec24 = new Vec2(0.0F, 0.0F);
    while (true) {
      int i = this._particleIdx;
      if (i < this._particleCount) {
        Particle particle = this._particles.get(i);
        particle.timeToLive -= paramFloat;
        if (particle.timeToLive > 0.0F) {
          if (this._emitterMode == Mode.GRAVITY) {
            vec23.x = 0.0F;
            vec23.y = 0.0F;
            vec24.x = 0.0F;
            vec24.y = 0.0F;
            if (particle.pos.x != 0.0F || particle.pos.y != 0.0F) {
              particle.pos.x /= (float)Math.sqrt((particle.pos.x * particle.pos.x + particle.pos.y * particle.pos.y));
              particle.pos.y /= (float)Math.sqrt((particle.pos.x * particle.pos.x + particle.pos.y * particle.pos.y));
            } 
            vec23.x *= particle.modeA.radialAccel;
            vec23.y *= particle.modeA.radialAccel;
            float f = vec23.x;
            vec23.x = -vec23.y;
            vec23.y = f;
            vec23.x *= particle.modeA.tangentialAccel;
            vec23.y *= particle.modeA.tangentialAccel;
            particle.modeA.dir.x += (vec23.x + vec23.x + this.gravityX) * paramFloat;
            particle.modeA.dir.y += (vec23.y + vec23.y + this.gravityY) * paramFloat;
            particle.pos.x += particle.modeA.dir.x * paramFloat * this._yCoordFlipped;
            particle.pos.y += particle.modeA.dir.y * paramFloat * this._yCoordFlipped;
            vec24 = vec23;
          } else {
            ModeB modeB = particle.modeB;
            modeB.angle += particle.modeB.degreesPerSecond * paramFloat;
            modeB = particle.modeB;
            modeB.radius += particle.modeB.deltaRadius * paramFloat;
            particle.pos.x = -((float)Math.cos(particle.modeB.angle)) * particle.modeB.radius;
            particle.pos.y = -((float)Math.sin(particle.modeB.angle)) * particle.modeB.radius;
            Vec2 vec2 = particle.pos;
            vec2.y *= this._yCoordFlipped;
          } 
          Color4F color4F = particle.color;
          color4F.r += particle.deltaColor.r * paramFloat;
          color4F = particle.color;
          color4F.g += particle.deltaColor.g * paramFloat;
          color4F = particle.color;
          color4F.b += particle.deltaColor.b * paramFloat;
          color4F = particle.color;
          color4F.a += particle.deltaColor.a * paramFloat;
          particle.size += particle.deltaSize * paramFloat;
          particle.size = Math.max(0.0F, particle.size);
          particle.rotation += particle.deltaRotation * paramFloat;
          if (this._positionType == PositionType.FREE) {
            vec22.x = particle.pos.x - vec21.x - particle.startPos.x + this._position.x;
            vec22.y = particle.pos.y - vec21.y - particle.startPos.y + this._position.y;
          } else if (this._positionType == PositionType.RELATIVE) {
            particle.pos.x -= vec21.x - particle.startPos.x;
            particle.pos.y -= vec21.y - particle.startPos.y;
          } else {
            vec22 = particle.pos;
          } 
          boolean bool1 = this._batchNode;
          updateQuadWithParticle(particle, vec22);
          this._particleIdx++;
          continue;
        } 
        i = particle.atlasIndex;
        i = this._particleIdx;
        int j = this._particleCount;
        if (i != j - 1)
          Collections.rotate(this._particles.subList(i, j), -1); 
        boolean bool = this._batchNode;
        this._particleCount--;
        if (this._particleCount == 0 && this._isAutoRemoveOnFinish) {
          this._unschedule = true;
          return;
        } 
        continue;
      } 
      this._transformSystemDirty = false;
      if (this._visible)
        postStep(); 
      return;
    } 
  }
  
  public abstract void updateBuffer();
  
  abstract void updateQuadWithParticle(Particle paramParticle, Vec2 paramVec2);
  
  void updateQuadWithParticle(List<Particle> paramList, Vec2 paramVec2) {}
  
  void updateWithNoTime() {}
  
  enum Mode {
    GRAVITY, RADIUS;
    
    static {
    
    }
  }
  
  class ModeA {
    Vec2 gravity = new Vec2(0.0F, 0.0F);
    
    float radialAccel;
    
    float radialAccelVar;
    
    boolean rotationIsDir;
    
    float speed;
    
    float speedVar;
    
    float tangentialAccel;
    
    float tangentialAccelVar;
  }
  
  class ModeB {
    float endRadius;
    
    float endRadiusVar;
    
    float rotatePerSecond;
    
    float rotatePerSecondVar;
    
    float startRadius;
    
    float startRadiusVar;
  }
  
  enum PositionType {
    FREE, GROUPED, RELATIVE;
    
    static {
      $VALUES = new PositionType[] { FREE, RELATIVE, GROUPED };
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/yang/firework/ParticleSystem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.yang.firework;

public class Vec2 {
  public float x;
  
  public float y;
  
  public Vec2(float paramFloat1, float paramFloat2) {
    this.x = paramFloat1;
    this.y = paramFloat2;
  }
  
  public Vec2(Vec2 paramVec2) {
    this.x = paramVec2.x;
    this.y = paramVec2.y;
  }
  
  public Vec2 add(Vec2 paramVec2) {
    return new Vec2(this.x + paramVec2.x, this.y + paramVec2.y);
  }
  
  public float getAngle() {
    return (float)Math.atan2(this.y, this.x);
  }
  
  public Vec2 getNormalized() {
    return scale(1.0F / length());
  }
  
  public float length() {
    float f1 = this.x;
    float f2 = this.y;
    return (float)Math.sqrt((f1 * f1 + f2 * f2));
  }
  
  public Vec2 scale(float paramFloat) {
    return new Vec2(this.x * paramFloat, this.y * paramFloat);
  }
  
  public Vec2 subtract(Vec2 paramVec2) {
    return new Vec2(this.x - paramVec2.x, this.y - paramVec2.y);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/yang/firework/Vec2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
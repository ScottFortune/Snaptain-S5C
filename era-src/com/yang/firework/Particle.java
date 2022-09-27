package com.yang.firework;

public class Particle {
  int atlasIndex;
  
  Color4F color = new Color4F(0.0F, 0.0F, 0.0F, 1.0F);
  
  Color4F deltaColor = new Color4F(0.0F, 0.0F, 0.0F, 1.0F);
  
  float deltaRotation;
  
  float deltaSize;
  
  ModeA modeA = new ModeA();
  
  ModeB modeB = new ModeB();
  
  Vec2 pos = new Vec2(0.0F, 0.0F);
  
  float rotation;
  
  float size;
  
  Vec2 startPos = new Vec2(0.0F, 0.0F);
  
  float timeToLive;
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/yang/firework/Particle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
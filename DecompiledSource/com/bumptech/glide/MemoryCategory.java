package com.bumptech.glide;

public enum MemoryCategory {
  HIGH,
  LOW(0.5F),
  NORMAL(1.0F);
  
  private final float multiplier;
  
  static {
    HIGH = new MemoryCategory("HIGH", 2, 1.5F);
    $VALUES = new MemoryCategory[] { LOW, NORMAL, HIGH };
  }
  
  MemoryCategory(float paramFloat) {
    this.multiplier = paramFloat;
  }
  
  public float getMultiplier() {
    return this.multiplier;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/MemoryCategory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
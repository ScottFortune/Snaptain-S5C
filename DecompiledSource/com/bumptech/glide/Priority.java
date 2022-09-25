package com.bumptech.glide;

public enum Priority {
  HIGH, IMMEDIATE, LOW, NORMAL;
  
  static {
    HIGH = new Priority("HIGH", 1);
    NORMAL = new Priority("NORMAL", 2);
    LOW = new Priority("LOW", 3);
    $VALUES = new Priority[] { IMMEDIATE, HIGH, NORMAL, LOW };
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/Priority.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.bumptech.glide.gifdecoder;

class GifFrame {
  static final int DISPOSAL_BACKGROUND = 2;
  
  static final int DISPOSAL_NONE = 1;
  
  static final int DISPOSAL_PREVIOUS = 3;
  
  static final int DISPOSAL_UNSPECIFIED = 0;
  
  int bufferFrameStart;
  
  int delay;
  
  int dispose;
  
  int ih;
  
  boolean interlace;
  
  int iw;
  
  int ix;
  
  int iy;
  
  int[] lct;
  
  int transIndex;
  
  boolean transparency;
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/gifdecoder/GifFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.stx.xhb.xbanner.transformers;

public enum Transformer {
  Accordion, Alpha, Cube, Default, Depth, Flip, Rotate, Scale, Stack, Zoom, ZoomCenter, ZoomFade, ZoomStack;
  
  static {
    Alpha = new Transformer("Alpha", 1);
    Rotate = new Transformer("Rotate", 2);
    Cube = new Transformer("Cube", 3);
    Flip = new Transformer("Flip", 4);
    Accordion = new Transformer("Accordion", 5);
    ZoomFade = new Transformer("ZoomFade", 6);
    ZoomCenter = new Transformer("ZoomCenter", 7);
    ZoomStack = new Transformer("ZoomStack", 8);
    Stack = new Transformer("Stack", 9);
    Depth = new Transformer("Depth", 10);
    Zoom = new Transformer("Zoom", 11);
    Scale = new Transformer("Scale", 12);
    $VALUES = new Transformer[] { 
        Default, Alpha, Rotate, Cube, Flip, Accordion, ZoomFade, ZoomCenter, ZoomStack, Stack, 
        Depth, Zoom, Scale };
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/stx/xhb/xbanner/transformers/Transformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.stx.xhb.xbanner.transformers;

import android.view.View;
import androidx.viewpager.widget.ViewPager;

public abstract class BasePageTransformer implements ViewPager.PageTransformer {
  public static BasePageTransformer getPageTransformer(Transformer paramTransformer) {
    switch (paramTransformer) {
      default:
        return new DefaultPageTransformer();
      case Scale:
        return new ScalePageTransformer();
      case Zoom:
        return new ZoomPageTransformer();
      case Depth:
        return new DepthPageTransformer();
      case Stack:
        return new StackPageTransformer();
      case ZoomStack:
        return new ZoomStackPageTransformer();
      case ZoomCenter:
        return new ZoomCenterPageTransformer();
      case ZoomFade:
        return new ZoomFadePageTransformer();
      case Accordion:
        return new AccordionPageTransformer();
      case Flip:
        return new FlipPageTransformer();
      case Cube:
        return new CubePageTransformer();
      case Rotate:
        return new RotatePageTransformer();
      case Alpha:
        return new AlphaPageTransformer();
      case Default:
        break;
    } 
    return new DefaultPageTransformer();
  }
  
  public abstract void handleInvisiblePage(View paramView, float paramFloat);
  
  public abstract void handleLeftPage(View paramView, float paramFloat);
  
  public abstract void handleRightPage(View paramView, float paramFloat);
  
  public void transformPage(View paramView, float paramFloat) {
    if (paramFloat < -1.0F) {
      handleInvisiblePage(paramView, paramFloat);
    } else if (paramFloat <= 0.0F) {
      handleLeftPage(paramView, paramFloat);
    } else if (paramFloat <= 1.0F) {
      handleRightPage(paramView, paramFloat);
    } else {
      handleInvisiblePage(paramView, paramFloat);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/stx/xhb/xbanner/transformers/BasePageTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
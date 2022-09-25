package com.netopsun.gesturerecognition;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import java.util.Timer;
import java.util.TimerTask;

public class PopupCountdownView {
  private View anchor;
  
  private final AnimationSet as;
  
  private View contentView;
  
  private Context context;
  
  private ImageView countdownImageView;
  
  private int[] countdownImages;
  
  private Timer timer = new Timer();
  
  private TimerTask timerTask = new TimerTask() {
      public void run() {}
    };
  
  private PopupWindow window;
  
  public PopupCountdownView(Context paramContext, View paramView, int[] paramArrayOfint) {
    this.context = paramContext;
    this.anchor = paramView;
    this.countdownImages = paramArrayOfint;
    this.contentView = LayoutInflater.from(paramContext).inflate(R.layout.popup_countdown_view, null);
    this.countdownImageView = (ImageView)this.contentView.findViewById(R.id.count_down_img);
    this.window = new PopupWindow(this.contentView, -1, -1, true);
    this.window.setBackgroundDrawable((Drawable)new ColorDrawable(0));
    this.window.setTouchable(false);
    this.window.setFocusable(false);
    ScaleAnimation scaleAnimation = new ScaleAnimation(3.0F, 1.0F, 3.0F, 1.0F, 1, 0.5F, 1, 0.5F);
    scaleAnimation.setDuration(600L);
    scaleAnimation.setRepeatCount(0);
    scaleAnimation.setFillAfter(true);
    AlphaAnimation alphaAnimation = new AlphaAnimation(0.1F, 1.0F);
    alphaAnimation.setDuration(600L);
    alphaAnimation.setRepeatCount(0);
    alphaAnimation.setFillAfter(true);
    this.as = new AnimationSet(false);
    this.as.addAnimation((Animation)scaleAnimation);
    this.as.addAnimation((Animation)alphaAnimation);
  }
  
  public void show(final int index) {
    this.anchor.post(new Runnable() {
          public void run() {
            PopupCountdownView.this.timerTask.cancel();
            PopupCountdownView.this.timer.cancel();
            PopupCountdownView.access$102(PopupCountdownView.this, new Timer());
            PopupCountdownView.access$002(PopupCountdownView.this, new TimerTask() {
                  public void run() {
                    PopupCountdownView.this.anchor.post(new Runnable() {
                          public void run() {
                            if (PopupCountdownView.this.window != null && PopupCountdownView.this.window.isShowing())
                              PopupCountdownView.this.window.dismiss(); 
                          }
                        });
                  }
                });
            if (PopupCountdownView.this.window.isShowing())
              PopupCountdownView.this.window.dismiss(); 
            PopupCountdownView.this.countdownImageView.setImageResource(PopupCountdownView.this.countdownImages[index]);
            PopupCountdownView.this.window.showAtLocation(PopupCountdownView.this.anchor, 17, 0, 0);
            PopupCountdownView.this.timer.schedule(PopupCountdownView.this.timerTask, 900L);
            PopupCountdownView.this.countdownImageView.startAnimation((Animation)PopupCountdownView.this.as);
          }
        });
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/gesturerecognition/PopupCountdownView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
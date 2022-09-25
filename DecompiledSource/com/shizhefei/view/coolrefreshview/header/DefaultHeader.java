package com.shizhefei.view.coolrefreshview.header;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.shizhefei.view.coolrefreshview.CoolRefreshView;
import com.shizhefei.view.coolrefreshview.PullHeader;
import com.shizhefei.view.coolrefreshview.R;

public class DefaultHeader implements PullHeader {
  private int backgroundColor = Color.parseColor("#989898");
  
  private PullHeader.DefaultConfig config = new PullHeader.DefaultConfig() {
      public int offsetToKeepHeaderWhileLoading(CoolRefreshView param1CoolRefreshView, View param1View) {
        return param1View.getMeasuredHeight() / 3;
      }
      
      public int offsetToRefresh(CoolRefreshView param1CoolRefreshView, View param1View) {
        return (int)((param1View.getMeasuredHeight() / 3) * 1.2F);
      }
      
      public int totalDistance(CoolRefreshView param1CoolRefreshView, View param1View) {
        return param1View.getMeasuredHeight();
      }
    };
  
  private View headerView;
  
  private ImageView imageView;
  
  private boolean isDownArrow = true;
  
  private RotateAnimation mFlipAnimation;
  
  private RotateAnimation mReverseFlipAnimation;
  
  private int mRotateAniTime = 150;
  
  private MaterialProgressDrawable materialProgressDrawable;
  
  private ImageView progressImageView;
  
  private TextView textView;
  
  private Resources getResources() {
    return this.headerView.getResources();
  }
  
  public View createHeaderView(CoolRefreshView paramCoolRefreshView) {
    Context context = paramCoolRefreshView.getContext();
    this.headerView = LayoutInflater.from(context).inflate(R.layout.coolrefreshview_defaultheader, (ViewGroup)paramCoolRefreshView, false);
    this.imageView = (ImageView)this.headerView.findViewById(R.id.coolrefresh_defaultheader_imageView);
    this.textView = (TextView)this.headerView.findViewById(R.id.coolrefresh_defaultheader_textView);
    this.progressImageView = (ImageView)this.headerView.findViewById(R.id.coolrefresh_defaultheader_progress_imageView);
    this.mFlipAnimation = new RotateAnimation(0.0F, -180.0F, 1, 0.5F, 1, 0.5F);
    this.mFlipAnimation.setInterpolator((Interpolator)new LinearInterpolator());
    this.mFlipAnimation.setDuration(this.mRotateAniTime);
    this.mFlipAnimation.setFillAfter(true);
    this.mReverseFlipAnimation = new RotateAnimation(-180.0F, 0.0F, 1, 0.5F, 1, 0.5F);
    this.mReverseFlipAnimation.setInterpolator((Interpolator)new LinearInterpolator());
    this.mReverseFlipAnimation.setDuration(this.mRotateAniTime);
    this.mReverseFlipAnimation.setFillAfter(true);
    this.imageView.setAnimation((Animation)this.mFlipAnimation);
    this.materialProgressDrawable = new MaterialProgressDrawable(context, (View)this.progressImageView);
    this.materialProgressDrawable.setColorSchemeColors(new int[] { -1 });
    this.materialProgressDrawable.setAlpha(255);
    this.progressImageView.setImageDrawable(this.materialProgressDrawable);
    this.headerView.setBackgroundColor(this.backgroundColor);
    return this.headerView;
  }
  
  public PullHeader.Config getConfig() {
    return (PullHeader.Config)this.config;
  }
  
  public void onPositionChange(CoolRefreshView paramCoolRefreshView, int paramInt1, int paramInt2, int paramInt3) {
    paramInt2 = getConfig().offsetToRefresh(paramCoolRefreshView, this.headerView);
    if (paramInt1 == 2)
      if (paramInt3 < paramInt2) {
        if (!this.isDownArrow) {
          this.textView.setText(getResources().getString(R.string.coolrefreshview_pull_down_to_refresh));
          this.imageView.clearAnimation();
          this.imageView.startAnimation((Animation)this.mReverseFlipAnimation);
          this.isDownArrow = true;
        } 
      } else if (this.isDownArrow) {
        this.textView.setText(getResources().getString(R.string.coolrefreshview_release_to_refresh));
        this.imageView.clearAnimation();
        this.imageView.startAnimation((Animation)this.mFlipAnimation);
        this.isDownArrow = false;
      }  
  }
  
  public void onPullBegin(CoolRefreshView paramCoolRefreshView) {
    this.progressImageView.setVisibility(8);
    this.imageView.setVisibility(0);
    this.textView.setText(getResources().getString(R.string.coolrefreshview_pull_down_to_refresh));
    this.isDownArrow = true;
  }
  
  public void onPullRefreshComplete(CoolRefreshView paramCoolRefreshView) {
    this.textView.setText(getResources().getString(R.string.coolrefreshview_complete));
    this.materialProgressDrawable.stop();
    this.imageView.setVisibility(8);
    this.progressImageView.setVisibility(8);
    this.imageView.clearAnimation();
  }
  
  public void onRefreshing(CoolRefreshView paramCoolRefreshView) {
    this.textView.setText(getResources().getString(R.string.coolrefreshview_refreshing));
    this.imageView.clearAnimation();
    this.materialProgressDrawable.start();
    this.imageView.setVisibility(8);
    this.progressImageView.setVisibility(0);
  }
  
  public void onReset(CoolRefreshView paramCoolRefreshView, boolean paramBoolean) {
    this.textView.setText(getResources().getString(R.string.coolrefreshview_pull_down_to_refresh));
    this.materialProgressDrawable.stop();
    this.imageView.setVisibility(8);
    this.progressImageView.setVisibility(8);
    this.imageView.clearAnimation();
  }
  
  public void setBackgroundColor(int paramInt) {
    this.backgroundColor = paramInt;
    View view = this.headerView;
    if (view != null)
      view.setBackgroundColor(paramInt); 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/view/coolrefreshview/header/DefaultHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
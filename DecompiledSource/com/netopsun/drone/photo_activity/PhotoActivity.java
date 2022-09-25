package com.netopsun.drone.photo_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.netopsun.drone.BaseSensorLandscapeActivity;
import java.util.LinkedList;
import java.util.List;

public class PhotoActivity extends BaseSensorLandscapeActivity implements View.OnClickListener {
  private static int tempCurrentPhotoPosition;
  
  private static List<String> tempPhotoPaths;
  
  private Button backBtn;
  
  int currentPhotoPosition;
  
  private Button nextPhoto;
  
  private ViewPager pager;
  
  List<String> photoPaths;
  
  private Button previousPhoto;
  
  private void initView() {
    this.backBtn = (Button)findViewById(2131230803);
    this.backBtn.setOnClickListener(this);
    this.previousPhoto = (Button)findViewById(2131230971);
    this.previousPhoto.setOnClickListener(this);
    this.nextPhoto = (Button)findViewById(2131230949);
    this.nextPhoto.setOnClickListener(this);
    this.pager = (ViewPager)findViewById(2131230960);
  }
  
  public static void launch(Context paramContext, List<String> paramList, int paramInt) {
    tempPhotoPaths = paramList;
    tempCurrentPhotoPosition = paramInt;
    paramContext.startActivity(new Intent(paramContext, PhotoActivity.class));
  }
  
  public void onClick(View paramView) {
    int i = paramView.getId();
    if (i != 2131230803) {
      if (i != 2131230949) {
        if (i == 2131230971) {
          i = this.currentPhotoPosition;
          if (i - 1 >= 0) {
            ViewPager viewPager = this.pager;
            this.currentPhotoPosition = --i;
            viewPager.setCurrentItem(i, true);
          } 
        } 
      } else if (this.currentPhotoPosition + 1 < this.photoPaths.size()) {
        ViewPager viewPager = this.pager;
        i = this.currentPhotoPosition + 1;
        this.currentPhotoPosition = i;
        viewPager.setCurrentItem(i, true);
      } 
    } else {
      finish();
    } 
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    getWindow().setFlags(1024, 1024);
    setContentView(2131427365);
    initView();
    this.photoPaths = tempPhotoPaths;
    this.currentPhotoPosition = tempCurrentPhotoPosition;
    tempCurrentPhotoPosition = 0;
    tempPhotoPaths = null;
    if (this.photoPaths == null) {
      finish();
      return;
    } 
    final LinkedList viewCache = new LinkedList();
    this.pager.setOffscreenPageLimit(3);
    this.pager.setAdapter(new PagerAdapter() {
          public void destroyItem(ViewGroup param1ViewGroup, int param1Int, Object param1Object) {
            param1Object = param1Object;
            param1ViewGroup.removeView((View)param1Object);
            viewCache.add(param1Object);
          }
          
          public int getCount() {
            return PhotoActivity.this.photoPaths.size();
          }
          
          public Object instantiateItem(ViewGroup param1ViewGroup, int param1Int) {
            PinchImageView pinchImageView;
            if (viewCache.size() > 0) {
              pinchImageView = viewCache.remove();
              pinchImageView.reset();
            } else {
              pinchImageView = new PinchImageView((Context)PhotoActivity.this);
              pinchImageView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View param2View) {
                      boolean bool;
                      if (PhotoActivity.this.backBtn.getVisibility() == 0) {
                        bool = true;
                      } else {
                        bool = false;
                      } 
                      PhotoActivity.this.backBtn.setVisibility(bool);
                      PhotoActivity.this.previousPhoto.setVisibility(bool);
                      PhotoActivity.this.nextPhoto.setVisibility(bool);
                    }
                  });
            } 
            Glide.with((FragmentActivity)PhotoActivity.this).load(PhotoActivity.this.photoPaths.get(param1Int)).into(pinchImageView);
            param1ViewGroup.addView((View)pinchImageView);
            return pinchImageView;
          }
          
          public boolean isViewFromObject(View param1View, Object param1Object) {
            boolean bool;
            if (param1View == param1Object) {
              bool = true;
            } else {
              bool = false;
            } 
            return bool;
          }
          
          public void setPrimaryItem(ViewGroup param1ViewGroup, int param1Int, Object param1Object) {
            PinchImageView pinchImageView = (PinchImageView)param1Object;
            Glide.with((FragmentActivity)PhotoActivity.this).load(PhotoActivity.this.photoPaths.get(param1Int)).into(pinchImageView);
          }
        });
    this.pager.setCurrentItem(this.currentPhotoPosition);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/drone/photo_activity/PhotoActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
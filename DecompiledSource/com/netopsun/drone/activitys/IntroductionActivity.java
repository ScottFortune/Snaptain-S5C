package com.netopsun.drone.activitys;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.netopsun.drone.BaseSensorLandscapeActivity;
import com.netopsun.drone.Constants;
import com.stx.xhb.xbanner.XBanner;
import com.stx.xhb.xbanner.entity.LocalImageInfo;
import java.util.ArrayList;

public class IntroductionActivity extends BaseSensorLandscapeActivity implements View.OnClickListener {
  private Button mBackBtn;
  
  private XBanner mXbanner;
  
  private void initView() {
    this.mXbanner = (XBanner)findViewById(2131231090);
    this.mBackBtn = (Button)findViewById(2131230803);
    this.mBackBtn.setOnClickListener(this);
  }
  
  public void onClick(View paramView) {
    if (paramView.getId() == 2131230803)
      finish(); 
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    getWindow().setFlags(1024, 1024);
    setContentView(2131427359);
    initView();
    this.mXbanner.loadImage(new XBanner.XBannerAdapter() {
          public void loadBanner(XBanner param1XBanner, Object param1Object, View param1View, int param1Int) {
            ((RequestBuilder)((RequestBuilder)Glide.with((FragmentActivity)IntroductionActivity.this).load(((LocalImageInfo)param1Object).getXBannerUrl()).skipMemoryCache(true)).diskCacheStrategy(DiskCacheStrategy.NONE)).into((ImageView)param1View);
          }
        });
    ArrayList<LocalImageInfo> arrayList = new ArrayList();
    arrayList.add(new LocalImageInfo(2131165411));
    arrayList.add(new LocalImageInfo(2131165412));
    arrayList.add(new LocalImageInfo(2131165413));
    arrayList.add(new LocalImageInfo(2131165414));
    if (Constants.getSelectedModel((Context)this).contains("sp600")) {
      arrayList.add(new LocalImageInfo(2131165416));
    } else if (Constants.getSelectedModel((Context)this).contains("sp660")) {
      arrayList.add(new LocalImageInfo(2131165417));
    } else {
      arrayList.add(new LocalImageInfo(2131165415));
    } 
    this.mXbanner.setBannerData(arrayList);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/drone/activitys/IntroductionActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.netopsun.drone.activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.netopsun.drone.BaseSensorLandscapeActivity;
import com.netopsun.drone.Constants;
import com.netopsun.drone.DevicesUtil;
import com.stx.xhb.xbanner.XBanner;
import com.stx.xhb.xbanner.entity.LocalImageInfo;
import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends BaseSensorLandscapeActivity implements View.OnClickListener {
  private XBanner mDroneLogoBanner;
  
  private View mDroneLogoText;
  
  private Button mMeidaBtn;
  
  private Button mPlayBtn;
  
  private Button mSettingBtn;
  
  private Button mSupportBtn;
  
  private Button mTutorialsBtn;
  
  private void initView() {
    this.mMeidaBtn = (Button)findViewById(2131230929);
    this.mMeidaBtn.setOnClickListener(this);
    this.mPlayBtn = (Button)findViewById(2131230968);
    this.mPlayBtn.setOnClickListener(this);
    this.mSupportBtn = (Button)findViewById(2131231045);
    this.mSupportBtn.setOnClickListener(this);
    this.mSettingBtn = (Button)findViewById(2131231013);
    this.mSettingBtn.setOnClickListener(this);
    this.mDroneLogoBanner = (XBanner)findViewById(2131230886);
    this.mDroneLogoText = findViewById(2131230887);
    this.mTutorialsBtn = (Button)findViewById(2131231075);
    this.mTutorialsBtn.setOnClickListener(this);
  }
  
  public static boolean isLocServiceEnable(Context paramContext) {
    LocationManager locationManager = (LocationManager)paramContext.getSystemService("location");
    boolean bool1 = locationManager.isProviderEnabled("gps");
    boolean bool2 = locationManager.isProviderEnabled("network");
    return (bool1 || bool2);
  }
  
  private void showOpenGPSDialog() {
    (new AlertDialog.Builder((Context)this)).setIcon(17301659).setTitle(getString(2131624078)).setMessage(getString(2131624153)).setNegativeButton(getString(2131624020), null).setPositiveButton(getString(2131624076), new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            Intent intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
            PlayActivity.this.startActivity(intent);
          }
        }).show();
  }
  
  private void startControlActivity() {
    Acp.getInstance((Context)this).request((new AcpOptions.Builder((Context)this)).setPermissions(new String[] { "android.permission.RECORD_AUDIO", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE" }, ).build(), new AcpListener() {
          public void onDenied(List<String> param1List) {}
          
          public void onGranted() {
            if (!PlayActivity.isLocServiceEnable(PlayActivity.this.getApplicationContext())) {
              PlayActivity.this.showOpenGPSDialog();
              return;
            } 
            DevicesUtil.initDevices(PlayActivity.this.getApplicationContext());
            Intent intent = new Intent((Context)PlayActivity.this, ControlActivity.class);
            PlayActivity.this.startActivity(intent);
          }
        });
  }
  
  private void startMediaActivity() {
    Acp.getInstance((Context)this).request((new AcpOptions.Builder((Context)this)).setPermissions(new String[] { "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE" }, ).build(), new AcpListener() {
          public void onDenied(List<String> param1List) {}
          
          public void onGranted() {
            DevicesUtil.initDevices(PlayActivity.this.getApplicationContext());
            Intent intent = new Intent((Context)PlayActivity.this, MediaSelectActivity.class);
            PlayActivity.this.startActivity(intent);
          }
        });
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    setConfigOrientation();
  }
  
  public void onClick(View paramView) {
    switch (paramView.getId()) {
      default:
        return;
      case 2131231075:
        startActivity(new Intent((Context)this, InstructionVideoActivity.class));
      case 2131231045:
        startActivity(new Intent((Context)this, ContactUsActivity.class));
      case 2131231013:
        startActivityForResult(new Intent((Context)this, SettingActivity.class), 0);
      case 2131230968:
        startControlActivity();
      case 2131230929:
        break;
    } 
    startMediaActivity();
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    getWindow().setFlags(1024, 1024);
    setContentView(2131427366);
    initView();
    this.mDroneLogoBanner.loadImage(new XBanner.XBannerAdapter() {
          public void loadBanner(XBanner param1XBanner, Object param1Object, View param1View, int param1Int) {
            ImageView imageView = (ImageView)param1View;
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with((FragmentActivity)PlayActivity.this).load(((LocalImageInfo)param1Object).getXBannerUrl()).into(imageView);
          }
        });
    ArrayList<LocalImageInfo> arrayList = new ArrayList();
    arrayList.add(new LocalImageInfo(2131165457));
    arrayList.add(new LocalImageInfo(2131165458));
    arrayList.add(new LocalImageInfo(2131165459));
    this.mDroneLogoBanner.setBannerData(arrayList);
    this.mDroneLogoBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
          public void onPageScrollStateChanged(int param1Int) {}
          
          public void onPageScrolled(int param1Int1, float param1Float, int param1Int2) {}
          
          public void onPageSelected(int param1Int) {
            if (param1Int == 0) {
              PlayActivity.this.mDroneLogoText.setBackgroundResource(2131165462);
              Constants.setSelectedModel(PlayActivity.this.getApplicationContext(), "s5c");
            } else if (param1Int == 1) {
              PlayActivity.this.mDroneLogoText.setBackgroundResource(2131165463);
              Constants.setSelectedModel(PlayActivity.this.getApplicationContext(), "sp600");
            } else {
              PlayActivity.this.mDroneLogoText.setBackgroundResource(2131165464);
              Constants.setSelectedModel(PlayActivity.this.getApplicationContext(), "sp660");
            } 
          }
        });
  }
  
  protected void onDestroy() {
    super.onDestroy();
    DevicesUtil.releaseDevices();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/drone/activitys/PlayActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
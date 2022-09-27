package com.netopsun.drone.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.SwitchCompat;
import com.netopsun.deviceshub.base.Devices;
import com.netopsun.drone.BaseSensorLandscapeActivity;
import com.netopsun.drone.Constants;
import com.netopsun.drone.DevicesUtil;
import com.netopsun.fhdevices.FHDevices;

public class SettingActivity extends BaseSensorLandscapeActivity implements View.OnClickListener {
  private SwitchCompat mAutoSaveParamsSwitch;
  
  private SwitchCompat mGravitySensorRotatingScreenSwitch;
  
  private SwitchCompat mHighDefinitionSwitch;
  
  private TextView mHighDefinitionText;
  
  private View mNewGuidelinesBtn;
  
  private SwitchCompat mResetParamsSwitch;
  
  private SwitchCompat mRightHandModeSwitch;
  
  private SwitchCompat mRotateVideoSwitch;
  
  private TextView mSettingAppVersion;
  
  private ImageView mSettingsBack;
  
  public static String getVersionName(Context paramContext) {
    PackageManager packageManager = paramContext.getPackageManager();
    try {
      String str = (packageManager.getPackageInfo(paramContext.getPackageName(), 0)).versionName;
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      nameNotFoundException.printStackTrace();
      nameNotFoundException = null;
    } 
    return (String)nameNotFoundException;
  }
  
  private void initView() {
    this.mHighDefinitionSwitch = (SwitchCompat)findViewById(2131230905);
    this.mRightHandModeSwitch = (SwitchCompat)findViewById(2131230986);
    this.mRotateVideoSwitch = (SwitchCompat)findViewById(2131230993);
    this.mGravitySensorRotatingScreenSwitch = (SwitchCompat)findViewById(2131230901);
    this.mAutoSaveParamsSwitch = (SwitchCompat)findViewById(2131230801);
    this.mResetParamsSwitch = (SwitchCompat)findViewById(2131230984);
    this.mSettingsBack = (ImageView)findViewById(2131231015);
    this.mSettingsBack.setOnClickListener(this);
    this.mSettingAppVersion = (TextView)findViewById(2131231012);
    this.mNewGuidelinesBtn = findViewById(2131230948);
    this.mNewGuidelinesBtn.setOnClickListener(this);
    this.mHighDefinitionText = (TextView)findViewById(2131230906);
  }
  
  public void onClick(View paramView) {
    int i = paramView.getId();
    if (i != 2131230948) {
      if (i == 2131231015)
        finish(); 
    } else {
      startActivity(new Intent((Context)this, IntroductionActivity.class));
    } 
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    getWindow().setFlags(1024, 1024);
    setContentView(2131427369);
    initView();
    this.mAutoSaveParamsSwitch.setChecked(Constants.getIsAutoSaveParams((Context)this));
    this.mAutoSaveParamsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
            Constants.setIsAutoSaveParams(SettingActivity.this.getApplicationContext(), param1Boolean);
          }
        });
    this.mResetParamsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
            if (param1Boolean)
              SettingActivity.this.mResetParamsSwitch.postDelayed(new Runnable() {
                    public void run() {
                      SettingActivity.this.mResetParamsSwitch.setChecked(false);
                      Toast.makeText(SettingActivity.this.getApplicationContext(), "success", 0).show();
                    }
                  }200L); 
            Constants.setPitchSliderLevel(SettingActivity.this.getApplicationContext(), 0);
            Constants.setRollSliderLevel(SettingActivity.this.getApplicationContext(), 0);
            Constants.setYawSliderLevel(SettingActivity.this.getApplicationContext(), 0);
          }
        });
    this.mHighDefinitionSwitch.setChecked(Constants.getIs720PPreview((Context)this));
    this.mHighDefinitionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
            Constants.setIs720PPreview(SettingActivity.this.getApplicationContext(), param1Boolean);
          }
        });
    this.mRotateVideoSwitch.setChecked(Constants.getIsRotateVideo((Context)this));
    this.mRotateVideoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
            Constants.setIsRotateVideo(SettingActivity.this.getApplicationContext(), param1Boolean);
          }
        });
    this.mRightHandModeSwitch.setChecked(Constants.getIsRightHandMode((Context)this));
    this.mRightHandModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
            Constants.setIsRightHandMode(SettingActivity.this.getApplicationContext(), param1Boolean);
          }
        });
    this.mGravitySensorRotatingScreenSwitch.setChecked(Constants.getIsGravitySensorRotatingScreen((Context)this));
    this.mGravitySensorRotatingScreenSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
            Constants.setIsGravitySensorRotatingScreen(SettingActivity.this.getApplicationContext(), param1Boolean);
            SettingActivity.this.setConfigOrientation();
          }
        });
    TextView textView = this.mSettingAppVersion;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("APP Version:");
    stringBuilder.append(getVersionName((Context)this));
    textView.setText(stringBuilder.toString());
    Devices devices = DevicesUtil.getCurrentConnectDevices();
    if (devices instanceof FHDevices && ((FHDevices)devices).getDevicesIP().equals("172.19.10.1")) {
      this.mHighDefinitionText.setText(getString(2131624001));
      this.mHighDefinitionSwitch.setChecked(Constants.getIs1080PPreview((Context)this));
      this.mHighDefinitionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
              Constants.setIs1080PPreview(SettingActivity.this.getApplicationContext(), param1Boolean);
            }
          });
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/drone/activitys/SettingActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
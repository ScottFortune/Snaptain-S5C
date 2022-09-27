package com.netopsun.drone.activitys;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;
import com.netopsun.drone.BaseSensorLandscapeActivity;

public class InstructionVideoActivity extends BaseSensorLandscapeActivity implements View.OnClickListener {
  private Button mBackBtn;
  
  private VideoView mVideoView;
  
  private void autoHideBackBtn() {
    this.mBackBtn.postDelayed(new Runnable() {
          public void run() {
            InstructionVideoActivity.this.mBackBtn.setVisibility(4);
          }
        },  2000L);
  }
  
  private void initView() {
    this.mVideoView = (VideoView)findViewById(2131231084);
    this.mVideoView.setOnClickListener(this);
    this.mBackBtn = (Button)findViewById(2131230803);
    this.mBackBtn.setOnClickListener(this);
  }
  
  public void onClick(View paramView) {
    int i = paramView.getId();
    if (i != 2131230803) {
      if (i == 2131231084)
        if (this.mBackBtn.getVisibility() == 0) {
          this.mBackBtn.setVisibility(4);
        } else {
          this.mBackBtn.setVisibility(0);
          autoHideBackBtn();
        }  
    } else {
      finish();
    } 
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    getWindow().setFlags(1152, 1152);
    setContentView(2131427358);
    initView();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("android.resource://");
    stringBuilder.append(getPackageName());
    stringBuilder.append("/");
    stringBuilder.append(2131558405);
    String str = stringBuilder.toString();
    this.mVideoView.setVideoURI(Uri.parse(str));
    this.mVideoView.start();
  }
  
  protected void onStart() {
    super.onStart();
    autoHideBackBtn();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/drone/activitys/InstructionVideoActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
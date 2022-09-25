package com.netopsun.drone.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.netopsun.deviceshub.base.CMDCommunicator;
import com.netopsun.deviceshub.base.Devices;
import com.netopsun.deviceshub.interfaces.ConnectResultCallback;
import com.netopsun.drone.BaseSensorLandscapeActivity;
import com.netopsun.drone.DevicesUtil;
import com.netopsun.drone.media_file_list_activity.MediaFileListActivity;
import com.netopsun.drone.remote_media_file_list_activity.RemoteVideoListActivity;

public class MediaSelectActivity extends BaseSensorLandscapeActivity implements View.OnClickListener {
  private Devices devices;
  
  private ImageView mBackBtn;
  
  private ImageView mRemoteVideoBtn;
  
  private ImageView photoBtn;
  
  private ImageView videoBtn;
  
  private void initView() {
    this.photoBtn = (ImageView)findViewById(2131230966);
    this.photoBtn.setOnClickListener(this);
    this.videoBtn = (ImageView)findViewById(2131231083);
    this.videoBtn.setOnClickListener(this);
    this.mBackBtn = (ImageView)findViewById(2131230803);
    this.mBackBtn.setOnClickListener(this);
    this.mRemoteVideoBtn = (ImageView)findViewById(2131230982);
    this.mRemoteVideoBtn.setOnClickListener(this);
  }
  
  public void onClick(View paramView) {
    Intent intent;
    switch (paramView.getId()) {
      default:
        return;
      case 2131231083:
        intent = new Intent((Context)this, MediaFileListActivity.class);
        intent.putExtra("is_video", true);
        startActivity(intent);
      case 2131230982:
        startActivity(new Intent((Context)this, RemoteVideoListActivity.class));
      case 2131230966:
        startActivity(new Intent((Context)this, MediaFileListActivity.class));
      case 2131230803:
        break;
    } 
    finish();
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    getWindow().setFlags(1024, 1024);
    setContentView(2131427361);
    initView();
  }
  
  protected void onPause() {
    super.onPause();
    Devices devices = this.devices;
    if (devices != null)
      devices.getCMDCommunicator().setConnectResultCallback(null); 
  }
  
  protected void onResume() {
    super.onResume();
    this.devices = DevicesUtil.getCurrentConnectDevices();
    Devices devices = this.devices;
    if (devices != null) {
      if ((devices.getCMDCommunicator()).SDCardState == 1) {
        this.mRemoteVideoBtn.setVisibility(0);
      } else {
        this.mRemoteVideoBtn.setVisibility(8);
      } 
      this.devices.getCMDCommunicator().setConnectResultCallback(new ConnectResultCallback() {
            public void onConnectFail(int param1Int, String param1String) {}
            
            public void onConnectSuccess(int param1Int, String param1String) {
              MediaSelectActivity.this.devices.getCMDCommunicator().getRemoteSDCardStatus(true, 3, new CMDCommunicator.OnExecuteCMDResult() {
                    public void onResult(int param2Int, String param2String) {
                      MediaSelectActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                              if ((MediaSelectActivity.this.devices.getCMDCommunicator()).SDCardState == 1) {
                                MediaSelectActivity.this.mRemoteVideoBtn.setVisibility(0);
                              } else {
                                MediaSelectActivity.this.mRemoteVideoBtn.setVisibility(8);
                              } 
                            }
                          });
                    }
                  });
            }
          });
      this.devices.getCMDCommunicator().connect();
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/drone/activitys/MediaSelectActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
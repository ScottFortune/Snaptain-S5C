package com.netopsun.drone.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.netopsun.drone.BaseSensorLandscapeActivity;

public class ContactUsActivity extends BaseSensorLandscapeActivity implements View.OnClickListener {
  private ImageView mBackBtn;
  
  private void initView() {
    this.mBackBtn = (ImageView)findViewById(2131230803);
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
    setContentView(2131427356);
    initView();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/drone/activitys/ContactUsActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
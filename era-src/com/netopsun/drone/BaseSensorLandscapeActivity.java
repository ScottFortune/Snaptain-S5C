package com.netopsun.drone;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class BaseSensorLandscapeActivity extends AppCompatActivity {
  protected void onCreate(Bundle paramBundle) {
    setConfigOrientation();
    super.onCreate(paramBundle);
  }
  
  protected void setConfigOrientation() {
    if (Constants.getIsGravitySensorRotatingScreen((Context)this)) {
      setRequestedOrientation(6);
    } else if (getWindowManager().getDefaultDisplay().getRotation() == 3) {
      setRequestedOrientation(8);
    } else {
      setRequestedOrientation(0);
    } 
  }
  
  protected void setFixedCurrentOrientation() {
    if (getWindowManager().getDefaultDisplay().getRotation() == 3) {
      setRequestedOrientation(8);
    } else {
      setRequestedOrientation(0);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/drone/BaseSensorLandscapeActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
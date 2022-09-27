package com.netopsun.drone;

import android.app.Application;
import android.content.IntentFilter;

public class MyApplication extends Application {
  private static final String TAG = "MyApplication";
  
  public void onCreate() {
    super.onCreate();
    (new IntentFilter()).addAction("android.net.conn.CONNECTIVITY_CHANGE");
  }
  
  public void onTerminate() {
    super.onTerminate();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/drone/MyApplication.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
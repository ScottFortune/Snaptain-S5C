package com.mylhyl.acp;

import java.util.List;

public interface AcpListener {
  void onDenied(List<String> paramList);
  
  void onGranted();
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/mylhyl/acp/AcpListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
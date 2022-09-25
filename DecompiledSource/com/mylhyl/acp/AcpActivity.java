package com.mylhyl.acp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class AcpActivity extends AppCompatActivity {
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    Acp.getInstance((Context)this).getAcpManager().onActivityResult(paramInt1, paramInt2, paramIntent);
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    getWindow().addFlags(16);
    if (paramBundle == null)
      Acp.getInstance((Context)this).getAcpManager().checkRequestPermissionRationale((Activity)this); 
  }
  
  protected void onNewIntent(Intent paramIntent) {
    super.onNewIntent(paramIntent);
    Acp.getInstance((Context)this).getAcpManager().checkRequestPermissionRationale((Activity)this);
  }
  
  public void onRequestPermissionsResult(int paramInt, String[] paramArrayOfString, int[] paramArrayOfint) {
    super.onRequestPermissionsResult(paramInt, paramArrayOfString, paramArrayOfint);
    Acp.getInstance((Context)this).getAcpManager().onRequestPermissionsResult(paramInt, paramArrayOfString, paramArrayOfint);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/mylhyl/acp/AcpActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
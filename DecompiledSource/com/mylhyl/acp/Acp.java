package com.mylhyl.acp;

import android.content.Context;

public class Acp {
  private static Acp mInstance;
  
  private AcpManager mAcpManager;
  
  private Acp(Context paramContext) {
    this.mAcpManager = new AcpManager(paramContext.getApplicationContext());
  }
  
  public static Acp getInstance(Context paramContext) {
    // Byte code:
    //   0: getstatic com/mylhyl/acp/Acp.mInstance : Lcom/mylhyl/acp/Acp;
    //   3: ifnonnull -> 40
    //   6: ldc com/mylhyl/acp/Acp
    //   8: monitorenter
    //   9: getstatic com/mylhyl/acp/Acp.mInstance : Lcom/mylhyl/acp/Acp;
    //   12: ifnonnull -> 28
    //   15: new com/mylhyl/acp/Acp
    //   18: astore_1
    //   19: aload_1
    //   20: aload_0
    //   21: invokespecial <init> : (Landroid/content/Context;)V
    //   24: aload_1
    //   25: putstatic com/mylhyl/acp/Acp.mInstance : Lcom/mylhyl/acp/Acp;
    //   28: ldc com/mylhyl/acp/Acp
    //   30: monitorexit
    //   31: goto -> 40
    //   34: astore_0
    //   35: ldc com/mylhyl/acp/Acp
    //   37: monitorexit
    //   38: aload_0
    //   39: athrow
    //   40: getstatic com/mylhyl/acp/Acp.mInstance : Lcom/mylhyl/acp/Acp;
    //   43: areturn
    // Exception table:
    //   from	to	target	type
    //   9	28	34	finally
    //   28	31	34	finally
    //   35	38	34	finally
  }
  
  AcpManager getAcpManager() {
    return this.mAcpManager;
  }
  
  public void request(AcpOptions paramAcpOptions, AcpListener paramAcpListener) {
    if (paramAcpOptions == null)
      new NullPointerException("AcpOptions is null..."); 
    if (paramAcpListener == null)
      new NullPointerException("AcpListener is null..."); 
    this.mAcpManager.request(paramAcpOptions, paramAcpListener);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/mylhyl/acp/Acp.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.bumptech.glide.load.engine;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

class ResourceRecycler {
  private final Handler handler = new Handler(Looper.getMainLooper(), new ResourceRecyclerCallback());
  
  private boolean isRecycling;
  
  void recycle(Resource<?> paramResource) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isRecycling : Z
    //   6: ifeq -> 24
    //   9: aload_0
    //   10: getfield handler : Landroid/os/Handler;
    //   13: iconst_1
    //   14: aload_1
    //   15: invokevirtual obtainMessage : (ILjava/lang/Object;)Landroid/os/Message;
    //   18: invokevirtual sendToTarget : ()V
    //   21: goto -> 40
    //   24: aload_0
    //   25: iconst_1
    //   26: putfield isRecycling : Z
    //   29: aload_1
    //   30: invokeinterface recycle : ()V
    //   35: aload_0
    //   36: iconst_0
    //   37: putfield isRecycling : Z
    //   40: aload_0
    //   41: monitorexit
    //   42: return
    //   43: astore_1
    //   44: aload_0
    //   45: monitorexit
    //   46: aload_1
    //   47: athrow
    // Exception table:
    //   from	to	target	type
    //   2	21	43	finally
    //   24	40	43	finally
  }
  
  private static final class ResourceRecyclerCallback implements Handler.Callback {
    static final int RECYCLE_RESOURCE = 1;
    
    public boolean handleMessage(Message param1Message) {
      if (param1Message.what == 1) {
        ((Resource)param1Message.obj).recycle();
        return true;
      } 
      return false;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/ResourceRecycler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
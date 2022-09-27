package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.transition.Transition;

public final class PreloadTarget<Z> extends CustomTarget<Z> {
  private static final Handler HANDLER = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        public boolean handleMessage(Message param1Message) {
          if (param1Message.what == 1) {
            ((PreloadTarget)param1Message.obj).clear();
            return true;
          } 
          return false;
        }
      });
  
  private static final int MESSAGE_CLEAR = 1;
  
  private final RequestManager requestManager;
  
  private PreloadTarget(RequestManager paramRequestManager, int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
    this.requestManager = paramRequestManager;
  }
  
  public static <Z> PreloadTarget<Z> obtain(RequestManager paramRequestManager, int paramInt1, int paramInt2) {
    return new PreloadTarget<Z>(paramRequestManager, paramInt1, paramInt2);
  }
  
  void clear() {
    this.requestManager.clear(this);
  }
  
  public void onLoadCleared(Drawable paramDrawable) {}
  
  public void onResourceReady(Z paramZ, Transition<? super Z> paramTransition) {
    HANDLER.obtainMessage(1, this).sendToTarget();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/target/PreloadTarget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
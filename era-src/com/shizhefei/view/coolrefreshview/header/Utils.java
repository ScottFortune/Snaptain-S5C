package com.shizhefei.view.coolrefreshview.header;

import android.content.Context;
import android.util.TypedValue;

class Utils {
  public static int dipToPix(Context paramContext, float paramFloat) {
    return (int)TypedValue.applyDimension(1, paramFloat, paramContext.getResources().getDisplayMetrics());
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/view/coolrefreshview/header/Utils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
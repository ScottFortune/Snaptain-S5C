package androidx.fragment.app;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

public abstract class FragmentContainer {
  @Deprecated
  public Fragment instantiate(Context paramContext, String paramString, Bundle paramBundle) {
    return Fragment.instantiate(paramContext, paramString, paramBundle);
  }
  
  public abstract View onFindViewById(int paramInt);
  
  public abstract boolean onHasView();
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/fragment/app/FragmentContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
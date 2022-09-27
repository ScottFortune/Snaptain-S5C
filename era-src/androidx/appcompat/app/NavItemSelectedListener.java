package androidx.appcompat.app;

import android.view.View;
import android.widget.AdapterView;

class NavItemSelectedListener implements AdapterView.OnItemSelectedListener {
  private final ActionBar.OnNavigationListener mListener;
  
  public NavItemSelectedListener(ActionBar.OnNavigationListener paramOnNavigationListener) {
    this.mListener = paramOnNavigationListener;
  }
  
  public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
    ActionBar.OnNavigationListener onNavigationListener = this.mListener;
    if (onNavigationListener != null)
      onNavigationListener.onNavigationItemSelected(paramInt, paramLong); 
  }
  
  public void onNothingSelected(AdapterView<?> paramAdapterView) {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/appcompat/app/NavItemSelectedListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
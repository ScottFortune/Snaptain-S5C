package androidx.lifecycle;

import android.app.Application;

public class AndroidViewModel extends ViewModel {
  private Application mApplication;
  
  public AndroidViewModel(Application paramApplication) {
    this.mApplication = paramApplication;
  }
  
  public <T extends Application> T getApplication() {
    return (T)this.mApplication;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/lifecycle/AndroidViewModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
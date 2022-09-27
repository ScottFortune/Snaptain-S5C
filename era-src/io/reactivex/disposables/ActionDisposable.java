package io.reactivex.disposables;

import io.reactivex.functions.Action;

final class ActionDisposable extends ReferenceDisposable<Action> {
  private static final long serialVersionUID = -8219729196779211169L;
  
  ActionDisposable(Action paramAction) {
    super(paramAction);
  }
  
  protected void onDisposed(Action paramAction) {
    try {
      return;
    } finally {
      paramAction = null;
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/disposables/ActionDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
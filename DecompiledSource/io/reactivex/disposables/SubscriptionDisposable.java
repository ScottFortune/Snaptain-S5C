package io.reactivex.disposables;

import org.reactivestreams.Subscription;

final class SubscriptionDisposable extends ReferenceDisposable<Subscription> {
  private static final long serialVersionUID = -707001650852963139L;
  
  SubscriptionDisposable(Subscription paramSubscription) {
    super(paramSubscription);
  }
  
  protected void onDisposed(Subscription paramSubscription) {
    paramSubscription.cancel();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/disposables/SubscriptionDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
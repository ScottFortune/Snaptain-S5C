package com.bumptech.glide.manager;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import androidx.collection.ArrayMap;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RequestManagerRetriever implements Handler.Callback {
  private static final RequestManagerFactory DEFAULT_FACTORY = new RequestManagerFactory() {
      public RequestManager build(Glide param1Glide, Lifecycle param1Lifecycle, RequestManagerTreeNode param1RequestManagerTreeNode, Context param1Context) {
        return new RequestManager(param1Glide, param1Lifecycle, param1RequestManagerTreeNode, param1Context);
      }
    };
  
  private static final String FRAGMENT_INDEX_KEY = "key";
  
  static final String FRAGMENT_TAG = "com.bumptech.glide.manager";
  
  private static final int ID_REMOVE_FRAGMENT_MANAGER = 1;
  
  private static final int ID_REMOVE_SUPPORT_FRAGMENT_MANAGER = 2;
  
  private static final String TAG = "RMRetriever";
  
  private volatile RequestManager applicationManager;
  
  private final RequestManagerFactory factory;
  
  private final Handler handler;
  
  final Map<FragmentManager, RequestManagerFragment> pendingRequestManagerFragments = new HashMap<FragmentManager, RequestManagerFragment>();
  
  final Map<FragmentManager, SupportRequestManagerFragment> pendingSupportRequestManagerFragments = new HashMap<FragmentManager, SupportRequestManagerFragment>();
  
  private final Bundle tempBundle = new Bundle();
  
  private final ArrayMap<View, Fragment> tempViewToFragment = new ArrayMap();
  
  private final ArrayMap<View, Fragment> tempViewToSupportFragment = new ArrayMap();
  
  public RequestManagerRetriever(RequestManagerFactory paramRequestManagerFactory) {
    if (paramRequestManagerFactory == null)
      paramRequestManagerFactory = DEFAULT_FACTORY; 
    this.factory = paramRequestManagerFactory;
    this.handler = new Handler(Looper.getMainLooper(), this);
  }
  
  private static void assertNotDestroyed(Activity paramActivity) {
    if (Build.VERSION.SDK_INT < 17 || !paramActivity.isDestroyed())
      return; 
    throw new IllegalArgumentException("You cannot start a load for a destroyed activity");
  }
  
  private static Activity findActivity(Context paramContext) {
    return (paramContext instanceof Activity) ? (Activity)paramContext : ((paramContext instanceof ContextWrapper) ? findActivity(((ContextWrapper)paramContext).getBaseContext()) : null);
  }
  
  @Deprecated
  private void findAllFragmentsWithViews(FragmentManager paramFragmentManager, ArrayMap<View, Fragment> paramArrayMap) {
    if (Build.VERSION.SDK_INT >= 26) {
      for (Fragment fragment : paramFragmentManager.getFragments()) {
        if (fragment.getView() != null) {
          paramArrayMap.put(fragment.getView(), fragment);
          findAllFragmentsWithViews(fragment.getChildFragmentManager(), paramArrayMap);
        } 
      } 
    } else {
      findAllFragmentsWithViewsPreO(paramFragmentManager, paramArrayMap);
    } 
  }
  
  @Deprecated
  private void findAllFragmentsWithViewsPreO(FragmentManager paramFragmentManager, ArrayMap<View, Fragment> paramArrayMap) {
    for (byte b = 0;; b++) {
      this.tempBundle.putInt("key", b);
      Fragment fragment = null;
      try {
        Fragment fragment1 = paramFragmentManager.getFragment(this.tempBundle, "key");
        fragment = fragment1;
      } catch (Exception exception) {}
      if (fragment == null)
        return; 
      if (fragment.getView() != null) {
        paramArrayMap.put(fragment.getView(), fragment);
        if (Build.VERSION.SDK_INT >= 17)
          findAllFragmentsWithViews(fragment.getChildFragmentManager(), paramArrayMap); 
      } 
    } 
  }
  
  private static void findAllSupportFragmentsWithViews(Collection<Fragment> paramCollection, Map<View, Fragment> paramMap) {
    if (paramCollection == null)
      return; 
    for (Fragment fragment : paramCollection) {
      if (fragment == null || fragment.getView() == null)
        continue; 
      paramMap.put(fragment.getView(), fragment);
      findAllSupportFragmentsWithViews(fragment.getChildFragmentManager().getFragments(), paramMap);
    } 
  }
  
  @Deprecated
  private Fragment findFragment(View paramView, Activity paramActivity) {
    Fragment fragment;
    this.tempViewToFragment.clear();
    findAllFragmentsWithViews(paramActivity.getFragmentManager(), this.tempViewToFragment);
    View view2 = paramActivity.findViewById(16908290);
    View view3 = null;
    View view1 = paramView;
    paramView = view3;
    while (true) {
      view3 = paramView;
      if (!view1.equals(view2)) {
        Fragment fragment1 = (Fragment)this.tempViewToFragment.get(view1);
        if (fragment1 != null) {
          Fragment fragment2 = fragment1;
          break;
        } 
        fragment = fragment1;
        if (view1.getParent() instanceof View) {
          view1 = (View)view1.getParent();
          continue;
        } 
      } 
      break;
    } 
    this.tempViewToFragment.clear();
    return fragment;
  }
  
  private Fragment findSupportFragment(View paramView, FragmentActivity paramFragmentActivity) {
    Fragment fragment;
    this.tempViewToSupportFragment.clear();
    findAllSupportFragmentsWithViews(paramFragmentActivity.getSupportFragmentManager().getFragments(), (Map<View, Fragment>)this.tempViewToSupportFragment);
    View view2 = paramFragmentActivity.findViewById(16908290);
    View view3 = null;
    View view1 = paramView;
    paramView = view3;
    while (true) {
      view3 = paramView;
      if (!view1.equals(view2)) {
        Fragment fragment1 = (Fragment)this.tempViewToSupportFragment.get(view1);
        if (fragment1 != null) {
          Fragment fragment2 = fragment1;
          break;
        } 
        fragment = fragment1;
        if (view1.getParent() instanceof View) {
          view1 = (View)view1.getParent();
          continue;
        } 
      } 
      break;
    } 
    this.tempViewToSupportFragment.clear();
    return fragment;
  }
  
  @Deprecated
  private RequestManager fragmentGet(Context paramContext, FragmentManager paramFragmentManager, Fragment paramFragment, boolean paramBoolean) {
    RequestManagerFragment requestManagerFragment = getRequestManagerFragment(paramFragmentManager, paramFragment, paramBoolean);
    RequestManager requestManager2 = requestManagerFragment.getRequestManager();
    RequestManager requestManager1 = requestManager2;
    if (requestManager2 == null) {
      Glide glide = Glide.get(paramContext);
      requestManager1 = this.factory.build(glide, requestManagerFragment.getGlideLifecycle(), requestManagerFragment.getRequestManagerTreeNode(), paramContext);
      requestManagerFragment.setRequestManager(requestManager1);
    } 
    return requestManager1;
  }
  
  private RequestManager getApplicationManager(Context paramContext) {
    // Byte code:
    //   0: aload_0
    //   1: getfield applicationManager : Lcom/bumptech/glide/RequestManager;
    //   4: ifnonnull -> 78
    //   7: aload_0
    //   8: monitorenter
    //   9: aload_0
    //   10: getfield applicationManager : Lcom/bumptech/glide/RequestManager;
    //   13: ifnonnull -> 68
    //   16: aload_1
    //   17: invokevirtual getApplicationContext : ()Landroid/content/Context;
    //   20: invokestatic get : (Landroid/content/Context;)Lcom/bumptech/glide/Glide;
    //   23: astore_2
    //   24: aload_0
    //   25: getfield factory : Lcom/bumptech/glide/manager/RequestManagerRetriever$RequestManagerFactory;
    //   28: astore_3
    //   29: new com/bumptech/glide/manager/ApplicationLifecycle
    //   32: astore #4
    //   34: aload #4
    //   36: invokespecial <init> : ()V
    //   39: new com/bumptech/glide/manager/EmptyRequestManagerTreeNode
    //   42: astore #5
    //   44: aload #5
    //   46: invokespecial <init> : ()V
    //   49: aload_0
    //   50: aload_3
    //   51: aload_2
    //   52: aload #4
    //   54: aload #5
    //   56: aload_1
    //   57: invokevirtual getApplicationContext : ()Landroid/content/Context;
    //   60: invokeinterface build : (Lcom/bumptech/glide/Glide;Lcom/bumptech/glide/manager/Lifecycle;Lcom/bumptech/glide/manager/RequestManagerTreeNode;Landroid/content/Context;)Lcom/bumptech/glide/RequestManager;
    //   65: putfield applicationManager : Lcom/bumptech/glide/RequestManager;
    //   68: aload_0
    //   69: monitorexit
    //   70: goto -> 78
    //   73: astore_1
    //   74: aload_0
    //   75: monitorexit
    //   76: aload_1
    //   77: athrow
    //   78: aload_0
    //   79: getfield applicationManager : Lcom/bumptech/glide/RequestManager;
    //   82: areturn
    // Exception table:
    //   from	to	target	type
    //   9	68	73	finally
    //   68	70	73	finally
    //   74	76	73	finally
  }
  
  private RequestManagerFragment getRequestManagerFragment(FragmentManager paramFragmentManager, Fragment paramFragment, boolean paramBoolean) {
    RequestManagerFragment requestManagerFragment1 = (RequestManagerFragment)paramFragmentManager.findFragmentByTag("com.bumptech.glide.manager");
    RequestManagerFragment requestManagerFragment2 = requestManagerFragment1;
    if (requestManagerFragment1 == null) {
      requestManagerFragment1 = this.pendingRequestManagerFragments.get(paramFragmentManager);
      requestManagerFragment2 = requestManagerFragment1;
      if (requestManagerFragment1 == null) {
        requestManagerFragment2 = new RequestManagerFragment();
        requestManagerFragment2.setParentFragmentHint(paramFragment);
        if (paramBoolean)
          requestManagerFragment2.getGlideLifecycle().onStart(); 
        this.pendingRequestManagerFragments.put(paramFragmentManager, requestManagerFragment2);
        paramFragmentManager.beginTransaction().add(requestManagerFragment2, "com.bumptech.glide.manager").commitAllowingStateLoss();
        this.handler.obtainMessage(1, paramFragmentManager).sendToTarget();
      } 
    } 
    return requestManagerFragment2;
  }
  
  private SupportRequestManagerFragment getSupportRequestManagerFragment(FragmentManager paramFragmentManager, Fragment paramFragment, boolean paramBoolean) {
    SupportRequestManagerFragment supportRequestManagerFragment1 = (SupportRequestManagerFragment)paramFragmentManager.findFragmentByTag("com.bumptech.glide.manager");
    SupportRequestManagerFragment supportRequestManagerFragment2 = supportRequestManagerFragment1;
    if (supportRequestManagerFragment1 == null) {
      supportRequestManagerFragment1 = this.pendingSupportRequestManagerFragments.get(paramFragmentManager);
      supportRequestManagerFragment2 = supportRequestManagerFragment1;
      if (supportRequestManagerFragment1 == null) {
        supportRequestManagerFragment2 = new SupportRequestManagerFragment();
        supportRequestManagerFragment2.setParentFragmentHint(paramFragment);
        if (paramBoolean)
          supportRequestManagerFragment2.getGlideLifecycle().onStart(); 
        this.pendingSupportRequestManagerFragments.put(paramFragmentManager, supportRequestManagerFragment2);
        paramFragmentManager.beginTransaction().add(supportRequestManagerFragment2, "com.bumptech.glide.manager").commitAllowingStateLoss();
        this.handler.obtainMessage(2, paramFragmentManager).sendToTarget();
      } 
    } 
    return supportRequestManagerFragment2;
  }
  
  private static boolean isActivityVisible(Context paramContext) {
    Activity activity = findActivity(paramContext);
    return (activity == null || !activity.isFinishing());
  }
  
  private RequestManager supportFragmentGet(Context paramContext, FragmentManager paramFragmentManager, Fragment paramFragment, boolean paramBoolean) {
    SupportRequestManagerFragment supportRequestManagerFragment = getSupportRequestManagerFragment(paramFragmentManager, paramFragment, paramBoolean);
    RequestManager requestManager2 = supportRequestManagerFragment.getRequestManager();
    RequestManager requestManager1 = requestManager2;
    if (requestManager2 == null) {
      Glide glide = Glide.get(paramContext);
      requestManager1 = this.factory.build(glide, supportRequestManagerFragment.getGlideLifecycle(), supportRequestManagerFragment.getRequestManagerTreeNode(), paramContext);
      supportRequestManagerFragment.setRequestManager(requestManager1);
    } 
    return requestManager1;
  }
  
  public RequestManager get(Activity paramActivity) {
    if (Util.isOnBackgroundThread())
      return get(paramActivity.getApplicationContext()); 
    assertNotDestroyed(paramActivity);
    return fragmentGet((Context)paramActivity, paramActivity.getFragmentManager(), null, isActivityVisible((Context)paramActivity));
  }
  
  @Deprecated
  public RequestManager get(Fragment paramFragment) {
    if (paramFragment.getActivity() != null) {
      if (Util.isOnBackgroundThread() || Build.VERSION.SDK_INT < 17)
        return get(paramFragment.getActivity().getApplicationContext()); 
      FragmentManager fragmentManager = paramFragment.getChildFragmentManager();
      return fragmentGet((Context)paramFragment.getActivity(), fragmentManager, paramFragment, paramFragment.isVisible());
    } 
    throw new IllegalArgumentException("You cannot start a load on a fragment before it is attached");
  }
  
  public RequestManager get(Context paramContext) {
    if (paramContext != null) {
      if (Util.isOnMainThread() && !(paramContext instanceof android.app.Application)) {
        if (paramContext instanceof FragmentActivity)
          return get((FragmentActivity)paramContext); 
        if (paramContext instanceof Activity)
          return get((Activity)paramContext); 
        if (paramContext instanceof ContextWrapper) {
          ContextWrapper contextWrapper = (ContextWrapper)paramContext;
          if (contextWrapper.getBaseContext().getApplicationContext() != null)
            return get(contextWrapper.getBaseContext()); 
        } 
      } 
      return getApplicationManager(paramContext);
    } 
    throw new IllegalArgumentException("You cannot start a load on a null Context");
  }
  
  public RequestManager get(View paramView) {
    RequestManager requestManager;
    FragmentActivity fragmentActivity;
    if (Util.isOnBackgroundThread())
      return get(paramView.getContext().getApplicationContext()); 
    Preconditions.checkNotNull(paramView);
    Preconditions.checkNotNull(paramView.getContext(), "Unable to obtain a request manager for a view without a Context");
    Activity activity = findActivity(paramView.getContext());
    if (activity == null)
      return get(paramView.getContext().getApplicationContext()); 
    if (activity instanceof FragmentActivity) {
      fragmentActivity = (FragmentActivity)activity;
      Fragment fragment1 = findSupportFragment(paramView, fragmentActivity);
      if (fragment1 != null) {
        requestManager = get(fragment1);
      } else {
        requestManager = get(fragmentActivity);
      } 
      return requestManager;
    } 
    Fragment fragment = findFragment((View)requestManager, (Activity)fragmentActivity);
    return (fragment == null) ? get((Activity)fragmentActivity) : get(fragment);
  }
  
  public RequestManager get(Fragment paramFragment) {
    Preconditions.checkNotNull(paramFragment.getContext(), "You cannot start a load on a fragment before it is attached or after it is destroyed");
    if (Util.isOnBackgroundThread())
      return get(paramFragment.getContext().getApplicationContext()); 
    FragmentManager fragmentManager = paramFragment.getChildFragmentManager();
    return supportFragmentGet(paramFragment.getContext(), fragmentManager, paramFragment, paramFragment.isVisible());
  }
  
  public RequestManager get(FragmentActivity paramFragmentActivity) {
    if (Util.isOnBackgroundThread())
      return get(paramFragmentActivity.getApplicationContext()); 
    assertNotDestroyed((Activity)paramFragmentActivity);
    return supportFragmentGet((Context)paramFragmentActivity, paramFragmentActivity.getSupportFragmentManager(), null, isActivityVisible((Context)paramFragmentActivity));
  }
  
  @Deprecated
  RequestManagerFragment getRequestManagerFragment(Activity paramActivity) {
    return getRequestManagerFragment(paramActivity.getFragmentManager(), null, isActivityVisible((Context)paramActivity));
  }
  
  SupportRequestManagerFragment getSupportRequestManagerFragment(Context paramContext, FragmentManager paramFragmentManager) {
    return getSupportRequestManagerFragment(paramFragmentManager, null, isActivityVisible(paramContext));
  }
  
  public boolean handleMessage(Message paramMessage) {
    FragmentManager fragmentManager1;
    FragmentManager fragmentManager;
    int i = paramMessage.what;
    Message message = null;
    boolean bool = true;
    if (i != 1) {
      if (i != 2) {
        bool = false;
        Message message1 = null;
        paramMessage = message;
        message = message1;
      } else {
        fragmentManager1 = (FragmentManager)paramMessage.obj;
        message = (Message)this.pendingSupportRequestManagerFragments.remove(fragmentManager1);
      } 
    } else {
      fragmentManager = (FragmentManager)((Message)fragmentManager1).obj;
      message = (Message)this.pendingRequestManagerFragments.remove(fragmentManager);
    } 
    if (bool && message == null && Log.isLoggable("RMRetriever", 5)) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Failed to remove expected request manager fragment, manager: ");
      stringBuilder.append(fragmentManager);
      Log.w("RMRetriever", stringBuilder.toString());
    } 
    return bool;
  }
  
  public static interface RequestManagerFactory {
    RequestManager build(Glide param1Glide, Lifecycle param1Lifecycle, RequestManagerTreeNode param1RequestManagerTreeNode, Context param1Context);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/manager/RequestManagerRetriever.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
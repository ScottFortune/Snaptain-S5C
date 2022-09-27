package com.bumptech.glide.manager;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Deprecated
public class RequestManagerFragment extends Fragment {
  private static final String TAG = "RMFragment";
  
  private final Set<RequestManagerFragment> childRequestManagerFragments = new HashSet<RequestManagerFragment>();
  
  private final ActivityFragmentLifecycle lifecycle;
  
  private Fragment parentFragmentHint;
  
  private RequestManager requestManager;
  
  private final RequestManagerTreeNode requestManagerTreeNode = new FragmentRequestManagerTreeNode();
  
  private RequestManagerFragment rootRequestManagerFragment;
  
  public RequestManagerFragment() {
    this(new ActivityFragmentLifecycle());
  }
  
  RequestManagerFragment(ActivityFragmentLifecycle paramActivityFragmentLifecycle) {
    this.lifecycle = paramActivityFragmentLifecycle;
  }
  
  private void addChildRequestManagerFragment(RequestManagerFragment paramRequestManagerFragment) {
    this.childRequestManagerFragments.add(paramRequestManagerFragment);
  }
  
  private Fragment getParentFragmentUsingHint() {
    Fragment fragment;
    if (Build.VERSION.SDK_INT >= 17) {
      fragment = getParentFragment();
    } else {
      fragment = null;
    } 
    if (fragment == null)
      fragment = this.parentFragmentHint; 
    return fragment;
  }
  
  private boolean isDescendant(Fragment paramFragment) {
    Fragment fragment = getParentFragment();
    while (true) {
      Fragment fragment1 = paramFragment.getParentFragment();
      if (fragment1 != null) {
        if (fragment1.equals(fragment))
          return true; 
        paramFragment = paramFragment.getParentFragment();
        continue;
      } 
      return false;
    } 
  }
  
  private void registerFragmentWithRoot(Activity paramActivity) {
    unregisterFragmentWithRoot();
    this.rootRequestManagerFragment = Glide.get((Context)paramActivity).getRequestManagerRetriever().getRequestManagerFragment(paramActivity);
    if (!equals(this.rootRequestManagerFragment))
      this.rootRequestManagerFragment.addChildRequestManagerFragment(this); 
  }
  
  private void removeChildRequestManagerFragment(RequestManagerFragment paramRequestManagerFragment) {
    this.childRequestManagerFragments.remove(paramRequestManagerFragment);
  }
  
  private void unregisterFragmentWithRoot() {
    RequestManagerFragment requestManagerFragment = this.rootRequestManagerFragment;
    if (requestManagerFragment != null) {
      requestManagerFragment.removeChildRequestManagerFragment(this);
      this.rootRequestManagerFragment = null;
    } 
  }
  
  Set<RequestManagerFragment> getDescendantRequestManagerFragments() {
    if (equals(this.rootRequestManagerFragment))
      return Collections.unmodifiableSet(this.childRequestManagerFragments); 
    if (this.rootRequestManagerFragment == null || Build.VERSION.SDK_INT < 17)
      return Collections.emptySet(); 
    HashSet<RequestManagerFragment> hashSet = new HashSet();
    for (RequestManagerFragment requestManagerFragment : this.rootRequestManagerFragment.getDescendantRequestManagerFragments()) {
      if (isDescendant(requestManagerFragment.getParentFragment()))
        hashSet.add(requestManagerFragment); 
    } 
    return Collections.unmodifiableSet(hashSet);
  }
  
  ActivityFragmentLifecycle getGlideLifecycle() {
    return this.lifecycle;
  }
  
  public RequestManager getRequestManager() {
    return this.requestManager;
  }
  
  public RequestManagerTreeNode getRequestManagerTreeNode() {
    return this.requestManagerTreeNode;
  }
  
  public void onAttach(Activity paramActivity) {
    super.onAttach(paramActivity);
    try {
      registerFragmentWithRoot(paramActivity);
    } catch (IllegalStateException illegalStateException) {
      if (Log.isLoggable("RMFragment", 5))
        Log.w("RMFragment", "Unable to register fragment with root", illegalStateException); 
    } 
  }
  
  public void onDestroy() {
    super.onDestroy();
    this.lifecycle.onDestroy();
    unregisterFragmentWithRoot();
  }
  
  public void onDetach() {
    super.onDetach();
    unregisterFragmentWithRoot();
  }
  
  public void onStart() {
    super.onStart();
    this.lifecycle.onStart();
  }
  
  public void onStop() {
    super.onStop();
    this.lifecycle.onStop();
  }
  
  void setParentFragmentHint(Fragment paramFragment) {
    this.parentFragmentHint = paramFragment;
    if (paramFragment != null && paramFragment.getActivity() != null)
      registerFragmentWithRoot(paramFragment.getActivity()); 
  }
  
  public void setRequestManager(RequestManager paramRequestManager) {
    this.requestManager = paramRequestManager;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(super.toString());
    stringBuilder.append("{parent=");
    stringBuilder.append(getParentFragmentUsingHint());
    stringBuilder.append("}");
    return stringBuilder.toString();
  }
  
  private class FragmentRequestManagerTreeNode implements RequestManagerTreeNode {
    public Set<RequestManager> getDescendants() {
      Set<RequestManagerFragment> set = RequestManagerFragment.this.getDescendantRequestManagerFragments();
      HashSet<RequestManager> hashSet = new HashSet(set.size());
      for (RequestManagerFragment requestManagerFragment : set) {
        if (requestManagerFragment.getRequestManager() != null)
          hashSet.add(requestManagerFragment.getRequestManager()); 
      } 
      return hashSet;
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(super.toString());
      stringBuilder.append("{fragment=");
      stringBuilder.append(RequestManagerFragment.this);
      stringBuilder.append("}");
      return stringBuilder.toString();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/manager/RequestManagerFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
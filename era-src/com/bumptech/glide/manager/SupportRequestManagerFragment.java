package com.bumptech.glide.manager;

import android.content.Context;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SupportRequestManagerFragment extends Fragment {
  private static final String TAG = "SupportRMFragment";
  
  private final Set<SupportRequestManagerFragment> childRequestManagerFragments = new HashSet<SupportRequestManagerFragment>();
  
  private final ActivityFragmentLifecycle lifecycle;
  
  private Fragment parentFragmentHint;
  
  private RequestManager requestManager;
  
  private final RequestManagerTreeNode requestManagerTreeNode = new SupportFragmentRequestManagerTreeNode();
  
  private SupportRequestManagerFragment rootRequestManagerFragment;
  
  public SupportRequestManagerFragment() {
    this(new ActivityFragmentLifecycle());
  }
  
  public SupportRequestManagerFragment(ActivityFragmentLifecycle paramActivityFragmentLifecycle) {
    this.lifecycle = paramActivityFragmentLifecycle;
  }
  
  private void addChildRequestManagerFragment(SupportRequestManagerFragment paramSupportRequestManagerFragment) {
    this.childRequestManagerFragments.add(paramSupportRequestManagerFragment);
  }
  
  private Fragment getParentFragmentUsingHint() {
    Fragment fragment = getParentFragment();
    if (fragment == null)
      fragment = this.parentFragmentHint; 
    return fragment;
  }
  
  private static FragmentManager getRootFragmentManager(Fragment paramFragment) {
    while (paramFragment.getParentFragment() != null)
      paramFragment = paramFragment.getParentFragment(); 
    return paramFragment.getFragmentManager();
  }
  
  private boolean isDescendant(Fragment paramFragment) {
    Fragment fragment = getParentFragmentUsingHint();
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
  
  private void registerFragmentWithRoot(Context paramContext, FragmentManager paramFragmentManager) {
    unregisterFragmentWithRoot();
    this.rootRequestManagerFragment = Glide.get(paramContext).getRequestManagerRetriever().getSupportRequestManagerFragment(paramContext, paramFragmentManager);
    if (!equals(this.rootRequestManagerFragment))
      this.rootRequestManagerFragment.addChildRequestManagerFragment(this); 
  }
  
  private void removeChildRequestManagerFragment(SupportRequestManagerFragment paramSupportRequestManagerFragment) {
    this.childRequestManagerFragments.remove(paramSupportRequestManagerFragment);
  }
  
  private void unregisterFragmentWithRoot() {
    SupportRequestManagerFragment supportRequestManagerFragment = this.rootRequestManagerFragment;
    if (supportRequestManagerFragment != null) {
      supportRequestManagerFragment.removeChildRequestManagerFragment(this);
      this.rootRequestManagerFragment = null;
    } 
  }
  
  Set<SupportRequestManagerFragment> getDescendantRequestManagerFragments() {
    SupportRequestManagerFragment supportRequestManagerFragment = this.rootRequestManagerFragment;
    if (supportRequestManagerFragment == null)
      return Collections.emptySet(); 
    if (equals(supportRequestManagerFragment))
      return Collections.unmodifiableSet(this.childRequestManagerFragments); 
    HashSet<SupportRequestManagerFragment> hashSet = new HashSet();
    for (SupportRequestManagerFragment supportRequestManagerFragment1 : this.rootRequestManagerFragment.getDescendantRequestManagerFragments()) {
      if (isDescendant(supportRequestManagerFragment1.getParentFragmentUsingHint()))
        hashSet.add(supportRequestManagerFragment1); 
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
  
  public void onAttach(Context paramContext) {
    super.onAttach(paramContext);
    FragmentManager fragmentManager = getRootFragmentManager(this);
    if (fragmentManager == null) {
      if (Log.isLoggable("SupportRMFragment", 5))
        Log.w("SupportRMFragment", "Unable to register fragment with root, ancestor detached"); 
      return;
    } 
    try {
      registerFragmentWithRoot(getContext(), fragmentManager);
    } catch (IllegalStateException illegalStateException) {
      if (Log.isLoggable("SupportRMFragment", 5))
        Log.w("SupportRMFragment", "Unable to register fragment with root", illegalStateException); 
    } 
  }
  
  public void onDestroy() {
    super.onDestroy();
    this.lifecycle.onDestroy();
    unregisterFragmentWithRoot();
  }
  
  public void onDetach() {
    super.onDetach();
    this.parentFragmentHint = null;
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
    if (paramFragment != null && paramFragment.getContext() != null) {
      FragmentManager fragmentManager = getRootFragmentManager(paramFragment);
      if (fragmentManager == null)
        return; 
      registerFragmentWithRoot(paramFragment.getContext(), fragmentManager);
    } 
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
  
  private class SupportFragmentRequestManagerTreeNode implements RequestManagerTreeNode {
    public Set<RequestManager> getDescendants() {
      Set<SupportRequestManagerFragment> set = SupportRequestManagerFragment.this.getDescendantRequestManagerFragments();
      HashSet<RequestManager> hashSet = new HashSet(set.size());
      for (SupportRequestManagerFragment supportRequestManagerFragment : set) {
        if (supportRequestManagerFragment.getRequestManager() != null)
          hashSet.add(supportRequestManagerFragment.getRequestManager()); 
      } 
      return hashSet;
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(super.toString());
      stringBuilder.append("{fragment=");
      stringBuilder.append(SupportRequestManagerFragment.this);
      stringBuilder.append("}");
      return stringBuilder.toString();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/manager/SupportRequestManagerFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
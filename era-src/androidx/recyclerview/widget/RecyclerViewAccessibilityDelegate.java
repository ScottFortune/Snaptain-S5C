package androidx.recyclerview.widget;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import java.util.Map;
import java.util.WeakHashMap;

public class RecyclerViewAccessibilityDelegate extends AccessibilityDelegateCompat {
  private final ItemDelegate mItemDelegate;
  
  final RecyclerView mRecyclerView;
  
  public RecyclerViewAccessibilityDelegate(RecyclerView paramRecyclerView) {
    this.mRecyclerView = paramRecyclerView;
    AccessibilityDelegateCompat accessibilityDelegateCompat = getItemDelegate();
    if (accessibilityDelegateCompat != null && accessibilityDelegateCompat instanceof ItemDelegate) {
      this.mItemDelegate = (ItemDelegate)accessibilityDelegateCompat;
    } else {
      this.mItemDelegate = new ItemDelegate(this);
    } 
  }
  
  public AccessibilityDelegateCompat getItemDelegate() {
    return this.mItemDelegate;
  }
  
  public void onInitializeAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent) {
    super.onInitializeAccessibilityEvent(paramView, paramAccessibilityEvent);
    if (paramView instanceof RecyclerView && !shouldIgnore()) {
      RecyclerView recyclerView = (RecyclerView)paramView;
      if (recyclerView.getLayoutManager() != null)
        recyclerView.getLayoutManager().onInitializeAccessibilityEvent(paramAccessibilityEvent); 
    } 
  }
  
  public void onInitializeAccessibilityNodeInfo(View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat) {
    super.onInitializeAccessibilityNodeInfo(paramView, paramAccessibilityNodeInfoCompat);
    if (!shouldIgnore() && this.mRecyclerView.getLayoutManager() != null)
      this.mRecyclerView.getLayoutManager().onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfoCompat); 
  }
  
  public boolean performAccessibilityAction(View paramView, int paramInt, Bundle paramBundle) {
    return super.performAccessibilityAction(paramView, paramInt, paramBundle) ? true : ((!shouldIgnore() && this.mRecyclerView.getLayoutManager() != null) ? this.mRecyclerView.getLayoutManager().performAccessibilityAction(paramInt, paramBundle) : false);
  }
  
  boolean shouldIgnore() {
    return this.mRecyclerView.hasPendingAdapterUpdates();
  }
  
  public static class ItemDelegate extends AccessibilityDelegateCompat {
    private Map<View, AccessibilityDelegateCompat> mOriginalItemDelegates = new WeakHashMap<View, AccessibilityDelegateCompat>();
    
    final RecyclerViewAccessibilityDelegate mRecyclerViewDelegate;
    
    public ItemDelegate(RecyclerViewAccessibilityDelegate param1RecyclerViewAccessibilityDelegate) {
      this.mRecyclerViewDelegate = param1RecyclerViewAccessibilityDelegate;
    }
    
    public boolean dispatchPopulateAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
      AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(param1View);
      return (accessibilityDelegateCompat != null) ? accessibilityDelegateCompat.dispatchPopulateAccessibilityEvent(param1View, param1AccessibilityEvent) : super.dispatchPopulateAccessibilityEvent(param1View, param1AccessibilityEvent);
    }
    
    public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View param1View) {
      AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(param1View);
      return (accessibilityDelegateCompat != null) ? accessibilityDelegateCompat.getAccessibilityNodeProvider(param1View) : super.getAccessibilityNodeProvider(param1View);
    }
    
    AccessibilityDelegateCompat getAndRemoveOriginalDelegateForItem(View param1View) {
      return this.mOriginalItemDelegates.remove(param1View);
    }
    
    public void onInitializeAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
      AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(param1View);
      if (accessibilityDelegateCompat != null) {
        accessibilityDelegateCompat.onInitializeAccessibilityEvent(param1View, param1AccessibilityEvent);
      } else {
        super.onInitializeAccessibilityEvent(param1View, param1AccessibilityEvent);
      } 
    }
    
    public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      if (!this.mRecyclerViewDelegate.shouldIgnore() && this.mRecyclerViewDelegate.mRecyclerView.getLayoutManager() != null) {
        this.mRecyclerViewDelegate.mRecyclerView.getLayoutManager().onInitializeAccessibilityNodeInfoForItem(param1View, param1AccessibilityNodeInfoCompat);
        AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(param1View);
        if (accessibilityDelegateCompat != null) {
          accessibilityDelegateCompat.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
        } else {
          super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
        } 
      } else {
        super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
      } 
    }
    
    public void onPopulateAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
      AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(param1View);
      if (accessibilityDelegateCompat != null) {
        accessibilityDelegateCompat.onPopulateAccessibilityEvent(param1View, param1AccessibilityEvent);
      } else {
        super.onPopulateAccessibilityEvent(param1View, param1AccessibilityEvent);
      } 
    }
    
    public boolean onRequestSendAccessibilityEvent(ViewGroup param1ViewGroup, View param1View, AccessibilityEvent param1AccessibilityEvent) {
      AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(param1ViewGroup);
      return (accessibilityDelegateCompat != null) ? accessibilityDelegateCompat.onRequestSendAccessibilityEvent(param1ViewGroup, param1View, param1AccessibilityEvent) : super.onRequestSendAccessibilityEvent(param1ViewGroup, param1View, param1AccessibilityEvent);
    }
    
    public boolean performAccessibilityAction(View param1View, int param1Int, Bundle param1Bundle) {
      if (!this.mRecyclerViewDelegate.shouldIgnore() && this.mRecyclerViewDelegate.mRecyclerView.getLayoutManager() != null) {
        AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(param1View);
        if (accessibilityDelegateCompat != null) {
          if (accessibilityDelegateCompat.performAccessibilityAction(param1View, param1Int, param1Bundle))
            return true; 
        } else if (super.performAccessibilityAction(param1View, param1Int, param1Bundle)) {
          return true;
        } 
        return this.mRecyclerViewDelegate.mRecyclerView.getLayoutManager().performAccessibilityActionForItem(param1View, param1Int, param1Bundle);
      } 
      return super.performAccessibilityAction(param1View, param1Int, param1Bundle);
    }
    
    void saveOriginalDelegate(View param1View) {
      AccessibilityDelegateCompat accessibilityDelegateCompat = ViewCompat.getAccessibilityDelegate(param1View);
      if (accessibilityDelegateCompat != null && accessibilityDelegateCompat != this)
        this.mOriginalItemDelegates.put(param1View, accessibilityDelegateCompat); 
    }
    
    public void sendAccessibilityEvent(View param1View, int param1Int) {
      AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(param1View);
      if (accessibilityDelegateCompat != null) {
        accessibilityDelegateCompat.sendAccessibilityEvent(param1View, param1Int);
      } else {
        super.sendAccessibilityEvent(param1View, param1Int);
      } 
    }
    
    public void sendAccessibilityEventUnchecked(View param1View, AccessibilityEvent param1AccessibilityEvent) {
      AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(param1View);
      if (accessibilityDelegateCompat != null) {
        accessibilityDelegateCompat.sendAccessibilityEventUnchecked(param1View, param1AccessibilityEvent);
      } else {
        super.sendAccessibilityEventUnchecked(param1View, param1AccessibilityEvent);
      } 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/recyclerview/widget/RecyclerViewAccessibilityDelegate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
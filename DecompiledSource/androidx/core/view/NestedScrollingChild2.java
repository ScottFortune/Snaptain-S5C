package androidx.core.view;

public interface NestedScrollingChild2 extends NestedScrollingChild {
  boolean dispatchNestedPreScroll(int paramInt1, int paramInt2, int[] paramArrayOfint1, int[] paramArrayOfint2, int paramInt3);
  
  boolean dispatchNestedScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint, int paramInt5);
  
  boolean hasNestedScrollingParent(int paramInt);
  
  boolean startNestedScroll(int paramInt1, int paramInt2);
  
  void stopNestedScroll(int paramInt);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/core/view/NestedScrollingChild2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package androidx.core.widget;

import android.view.View;
import android.widget.ListView;

public class ListViewAutoScrollHelper extends AutoScrollHelper {
  private final ListView mTarget;
  
  public ListViewAutoScrollHelper(ListView paramListView) {
    super((View)paramListView);
    this.mTarget = paramListView;
  }
  
  public boolean canTargetScrollHorizontally(int paramInt) {
    return false;
  }
  
  public boolean canTargetScrollVertically(int paramInt) {
    ListView listView = this.mTarget;
    int i = listView.getCount();
    if (i == 0)
      return false; 
    int j = listView.getChildCount();
    int k = listView.getFirstVisiblePosition();
    if (paramInt > 0) {
      if (k + j >= i && listView.getChildAt(j - 1).getBottom() <= listView.getHeight())
        return false; 
    } else {
      return (paramInt < 0) ? (!(k <= 0 && listView.getChildAt(0).getTop() >= 0)) : false;
    } 
    return true;
  }
  
  public void scrollTargetBy(int paramInt1, int paramInt2) {
    ListViewCompat.scrollListBy(this.mTarget, paramInt2);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/core/widget/ListViewAutoScrollHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
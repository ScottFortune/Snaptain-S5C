package androidx.recyclerview.widget;

import java.util.List;

public abstract class ListAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
  final AsyncListDiffer<T> mDiffer;
  
  private final AsyncListDiffer.ListListener<T> mListener = new AsyncListDiffer.ListListener<T>() {
      public void onCurrentListChanged(List<T> param1List1, List<T> param1List2) {
        ListAdapter.this.onCurrentListChanged(param1List1, param1List2);
      }
    };
  
  protected ListAdapter(AsyncDifferConfig<T> paramAsyncDifferConfig) {
    this.mDiffer = new AsyncListDiffer<T>(new AdapterListUpdateCallback(this), paramAsyncDifferConfig);
    this.mDiffer.addListListener(this.mListener);
  }
  
  protected ListAdapter(DiffUtil.ItemCallback<T> paramItemCallback) {
    this.mDiffer = new AsyncListDiffer<T>(new AdapterListUpdateCallback(this), (new AsyncDifferConfig.Builder<T>(paramItemCallback)).build());
    this.mDiffer.addListListener(this.mListener);
  }
  
  public List<T> getCurrentList() {
    return this.mDiffer.getCurrentList();
  }
  
  protected T getItem(int paramInt) {
    return this.mDiffer.getCurrentList().get(paramInt);
  }
  
  public int getItemCount() {
    return this.mDiffer.getCurrentList().size();
  }
  
  public void onCurrentListChanged(List<T> paramList1, List<T> paramList2) {}
  
  public void submitList(List<T> paramList) {
    this.mDiffer.submitList(paramList);
  }
  
  public void submitList(List<T> paramList, Runnable paramRunnable) {
    this.mDiffer.submitList(paramList, paramRunnable);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/recyclerview/widget/ListAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
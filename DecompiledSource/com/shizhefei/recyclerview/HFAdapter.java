package com.shizhefei.recyclerview;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import java.util.ArrayList;
import java.util.List;

public abstract class HFAdapter extends RecyclerView.Adapter {
  public static final int TYPE_FOOTER = 7899;
  
  public static final int TYPE_HEADER = 7898;
  
  public static final int TYPE_MANAGER_GRID = 2;
  
  public static final int TYPE_MANAGER_LINEAR = 1;
  
  public static final int TYPE_MANAGER_OTHER = 0;
  
  public static final int TYPE_MANAGER_STAGGERED_GRID = 3;
  
  private List<View> mFooters = new ArrayList<View>();
  
  private List<View> mHeaders = new ArrayList<View>();
  
  private int mManagerType;
  
  private boolean needSetClickListener = true;
  
  private OnItemClickListener onItemClickListener;
  
  private OnItemLongClickListener onItemLongClickListener;
  
  public HFAdapter() {
    this(true);
  }
  
  public HFAdapter(boolean paramBoolean) {
    this.needSetClickListener = paramBoolean;
  }
  
  private boolean isFooter(int paramInt) {
    boolean bool;
    if (paramInt >= this.mHeaders.size() + getItemCountHF()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean isHeader(int paramInt) {
    boolean bool;
    if (paramInt < this.mHeaders.size()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void prepareHeaderFooter(HeaderFooterViewHolder paramHeaderFooterViewHolder, View paramView) {
    if (this.mManagerType == 3) {
      StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(-1, -2);
      layoutParams.setFullSpan(true);
      paramHeaderFooterViewHolder.itemView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    } 
    if (paramView.getParent() != null)
      ((ViewGroup)paramView.getParent()).removeView(paramView); 
    paramHeaderFooterViewHolder.base.removeAllViews();
    paramHeaderFooterViewHolder.base.addView(paramView);
  }
  
  public void addFooter(View paramView) {
    if (!this.mFooters.contains(paramView)) {
      this.mFooters.add(paramView);
      notifyItemInserted(this.mHeaders.size() + getItemCountHF() + this.mFooters.size() - 1);
    } 
  }
  
  public void addHeader(View paramView) {
    if (!this.mHeaders.contains(paramView)) {
      this.mHeaders.add(paramView);
      notifyItemInserted(this.mHeaders.size() - 1);
    } 
  }
  
  public int getFootSize() {
    return this.mFooters.size();
  }
  
  public int getHeadSize() {
    return this.mHeaders.size();
  }
  
  public final int getItemCount() {
    return this.mHeaders.size() + getItemCountHF() + this.mFooters.size();
  }
  
  public abstract int getItemCountHF();
  
  public final long getItemId(int paramInt) {
    return getItemIdHF(getRealPosition(paramInt));
  }
  
  public long getItemIdHF(int paramInt) {
    return super.getItemId(paramInt);
  }
  
  public final int getItemViewType(int paramInt) {
    if (isHeader(paramInt))
      return 7898; 
    if (isFooter(paramInt))
      return 7899; 
    paramInt = getItemViewTypeHF(getRealPosition(paramInt));
    if (paramInt != 7898 && paramInt != 7899)
      return paramInt; 
    throw new IllegalArgumentException("Item type cannot equal 7898 or 7899");
  }
  
  public int getItemViewTypeHF(int paramInt) {
    return super.getItemViewType(paramInt);
  }
  
  public int getManagerType() {
    return this.mManagerType;
  }
  
  public OnItemClickListener getOnItemClickListener() {
    return this.onItemClickListener;
  }
  
  public OnItemLongClickListener getOnItemLongClickListener() {
    return this.onItemLongClickListener;
  }
  
  public int getRealPosition(int paramInt) {
    return paramInt - this.mHeaders.size();
  }
  
  public boolean isFooter(RecyclerView.ViewHolder paramViewHolder) {
    return isFooter(paramViewHolder.getAdapterPosition());
  }
  
  public boolean isHeader(RecyclerView.ViewHolder paramViewHolder) {
    return isHeader(paramViewHolder.getAdapterPosition());
  }
  
  public void notifyDataSetChangedHF() {
    notifyDataSetChanged();
  }
  
  public void notifyItemChangedHF(int paramInt) {
    notifyItemChanged(getRealPosition(paramInt));
  }
  
  public void notifyItemInsertedHF(int paramInt) {
    notifyItemInserted(getRealPosition(paramInt));
  }
  
  public void notifyItemMovedHF(int paramInt1, int paramInt2) {
    notifyItemMoved(getRealPosition(paramInt1), getRealPosition(paramInt2));
  }
  
  public void notifyItemRangeChangedHF(int paramInt1, int paramInt2) {
    notifyItemRangeChanged(getRealPosition(paramInt1), paramInt2);
  }
  
  public void notifyItemRangeInsertedHF(int paramInt1, int paramInt2) {
    notifyItemRangeInserted(getRealPosition(paramInt1), paramInt2);
  }
  
  public void notifyItemRangeRemovedHF(int paramInt1, int paramInt2) {
    notifyItemRangeRemoved(getRealPosition(paramInt1), paramInt2);
  }
  
  public void notifyItemRemovedHF(int paramInt) {
    notifyItemRemoved(getRealPosition(paramInt));
  }
  
  public final void onBindViewHolder(RecyclerView.ViewHolder paramViewHolder, int paramInt) {
    if (isHeader(paramInt)) {
      View view = this.mHeaders.get(paramInt);
      prepareHeaderFooter((HeaderFooterViewHolder)paramViewHolder, view);
    } else if (isFooter(paramInt)) {
      View view = this.mFooters.get(paramInt - getItemCountHF() - this.mHeaders.size());
      prepareHeaderFooter((HeaderFooterViewHolder)paramViewHolder, view);
    } else {
      if (this.needSetClickListener) {
        paramViewHolder.itemView.setOnClickListener(new MyOnClickListener(paramViewHolder));
        paramViewHolder.itemView.setOnLongClickListener(new MyOnLongClickListener(paramViewHolder));
      } 
      onBindViewHolderHF(paramViewHolder, getRealPosition(paramInt));
    } 
  }
  
  public final void onBindViewHolder(RecyclerView.ViewHolder paramViewHolder, int paramInt, List paramList) {
    super.onBindViewHolder(paramViewHolder, paramInt, paramList);
  }
  
  public abstract void onBindViewHolderHF(RecyclerView.ViewHolder paramViewHolder, int paramInt);
  
  public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
    if (paramInt != 7898 && paramInt != 7899)
      return onCreateViewHolderHF(paramViewGroup, paramInt); 
    FrameLayout frameLayout = new FrameLayout(paramViewGroup.getContext());
    frameLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
    return new HeaderFooterViewHolder((View)frameLayout);
  }
  
  public abstract RecyclerView.ViewHolder onCreateViewHolderHF(ViewGroup paramViewGroup, int paramInt);
  
  protected boolean onItemClick(RecyclerView.ViewHolder paramViewHolder, int paramInt) {
    return false;
  }
  
  protected boolean onItemLongClick(RecyclerView.ViewHolder paramViewHolder, int paramInt) {
    return false;
  }
  
  public void removeFooter(View paramView) {
    if (this.mFooters.contains(paramView)) {
      notifyItemRemoved(this.mHeaders.size() + getItemCountHF() + this.mFooters.indexOf(paramView));
      this.mFooters.remove(paramView);
    } 
  }
  
  public void removeHeader(View paramView) {
    if (this.mHeaders.contains(paramView)) {
      notifyItemRemoved(this.mHeaders.indexOf(paramView));
      this.mHeaders.remove(paramView);
    } 
  }
  
  public void setOnItemClickListener(OnItemClickListener paramOnItemClickListener) {
    this.onItemClickListener = paramOnItemClickListener;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("setOnItemClickListener ");
    stringBuilder.append(this.onItemClickListener);
    Log.d("eeee", stringBuilder.toString());
  }
  
  public void setOnItemLongClickListener(OnItemLongClickListener paramOnItemLongClickListener) {
    this.onItemLongClickListener = paramOnItemLongClickListener;
  }
  
  public void setmManagerType(int paramInt) {
    this.mManagerType = paramInt;
  }
  
  public static class HeaderFooterViewHolder extends RecyclerView.ViewHolder {
    FrameLayout base;
    
    public HeaderFooterViewHolder(View param1View) {
      super(param1View);
      this.base = (FrameLayout)param1View;
    }
  }
  
  private class MyOnClickListener implements View.OnClickListener {
    private RecyclerView.ViewHolder vh;
    
    public MyOnClickListener(RecyclerView.ViewHolder param1ViewHolder) {
      this.vh = param1ViewHolder;
    }
    
    public void onClick(View param1View) {
      int i = HFAdapter.this.getRealPosition(this.vh.getLayoutPosition());
      if (!HFAdapter.this.onItemClick(this.vh, i) && HFAdapter.this.onItemClickListener != null)
        HFAdapter.this.onItemClickListener.onItemClick(HFAdapter.this, this.vh, i); 
    }
  }
  
  private class MyOnLongClickListener implements View.OnLongClickListener {
    private RecyclerView.ViewHolder vh;
    
    public MyOnLongClickListener(RecyclerView.ViewHolder param1ViewHolder) {
      this.vh = param1ViewHolder;
    }
    
    public boolean onLongClick(View param1View) {
      int i = HFAdapter.this.getRealPosition(this.vh.getLayoutPosition());
      if (!HFAdapter.this.onItemLongClick(this.vh, i) && HFAdapter.this.onItemLongClickListener != null)
        HFAdapter.this.onItemLongClickListener.onItemLongClick(HFAdapter.this, this.vh, i); 
      return true;
    }
  }
  
  public static interface OnItemClickListener {
    void onItemClick(HFAdapter param1HFAdapter, RecyclerView.ViewHolder param1ViewHolder, int param1Int);
  }
  
  public static interface OnItemLongClickListener {
    void onItemLongClick(HFAdapter param1HFAdapter, RecyclerView.ViewHolder param1ViewHolder, int param1Int);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/recyclerview/HFAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.netopsun.ijkvideoview.widget.media;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.netopsun.ijkvideoview.R;

public class TableLayoutBinder {
  private Context mContext;
  
  public TableLayout mTableLayout;
  
  public ViewGroup mTableView;
  
  public TableLayoutBinder(Context paramContext) {
    this(paramContext, R.layout.table_media_info);
  }
  
  public TableLayoutBinder(Context paramContext, int paramInt) {
    this.mContext = paramContext;
    this.mTableView = (ViewGroup)LayoutInflater.from(this.mContext).inflate(paramInt, null);
    this.mTableLayout = (TableLayout)this.mTableView.findViewById(R.id.table);
  }
  
  public TableLayoutBinder(Context paramContext, TableLayout paramTableLayout) {
    this.mContext = paramContext;
    this.mTableView = (ViewGroup)paramTableLayout;
    this.mTableLayout = paramTableLayout;
  }
  
  public View appendRow(int paramInt, String paramString1, String paramString2) {
    ViewGroup viewGroup = (ViewGroup)LayoutInflater.from(this.mContext).inflate(paramInt, (ViewGroup)this.mTableLayout, false);
    setNameValueText((View)viewGroup, paramString1, paramString2);
    this.mTableLayout.addView((View)viewGroup);
    return (View)viewGroup;
  }
  
  public View appendRow1(int paramInt, String paramString) {
    return appendRow1(this.mContext.getString(paramInt), paramString);
  }
  
  public View appendRow1(String paramString1, String paramString2) {
    return appendRow(R.layout.table_media_info_row1, paramString1, paramString2);
  }
  
  public View appendRow2(int paramInt, String paramString) {
    return appendRow2(this.mContext.getString(paramInt), paramString);
  }
  
  public View appendRow2(String paramString1, String paramString2) {
    return appendRow(R.layout.table_media_info_row2, paramString1, paramString2);
  }
  
  public View appendSection(int paramInt) {
    return appendSection(this.mContext.getString(paramInt));
  }
  
  public View appendSection(String paramString) {
    return appendRow(R.layout.table_media_info_section, paramString, null);
  }
  
  public AlertDialog.Builder buildAlertDialogBuilder() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
    builder.setView((View)buildLayout());
    return builder;
  }
  
  public ViewGroup buildLayout() {
    return this.mTableView;
  }
  
  public ViewHolder obtainViewHolder(View paramView) {
    ViewHolder viewHolder1 = (ViewHolder)paramView.getTag();
    ViewHolder viewHolder2 = viewHolder1;
    if (viewHolder1 == null) {
      viewHolder2 = new ViewHolder();
      viewHolder2.mNameTextView = (TextView)paramView.findViewById(R.id.name);
      viewHolder2.mValueTextView = (TextView)paramView.findViewById(R.id.value);
      paramView.setTag(viewHolder2);
    } 
    return viewHolder2;
  }
  
  public void setNameValueText(View paramView, String paramString1, String paramString2) {
    ViewHolder viewHolder = obtainViewHolder(paramView);
    viewHolder.setName(paramString1);
    viewHolder.setValue(paramString2);
  }
  
  public void setValueText(View paramView, String paramString) {
    obtainViewHolder(paramView).setValue(paramString);
  }
  
  private static class ViewHolder {
    public TextView mNameTextView;
    
    public TextView mValueTextView;
    
    private ViewHolder() {}
    
    public void setName(String param1String) {
      TextView textView = this.mNameTextView;
      if (textView != null)
        textView.setText(param1String); 
    }
    
    public void setValue(String param1String) {
      TextView textView = this.mValueTextView;
      if (textView != null)
        textView.setText(param1String); 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/widget/media/TableLayoutBinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
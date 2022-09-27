package com.shizhefei.view.coolrefreshview.header;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.shizhefei.view.coolrefreshview.CoolRefreshView;
import com.shizhefei.view.coolrefreshview.PullHeader;
import com.shizhefei.view.coolrefreshview.R;

public class TestHeader implements PullHeader {
  private PullHeader.DefaultConfig config = new PullHeader.DefaultConfig();
  
  private TextView mTextView;
  
  private TextView mTextView2;
  
  private View view;
  
  public View createHeaderView(final CoolRefreshView refreshView) {
    this.view = LayoutInflater.from(refreshView.getContext()).inflate(R.layout.coolrecyclerview_testhead, (ViewGroup)refreshView, false);
    this.mTextView = (TextView)this.view.findViewById(R.id.coolrecyclerview_testhead_text1_textView);
    this.mTextView2 = (TextView)this.view.findViewById(R.id.coolrecyclerview_testhead_text2_textView);
    this.mTextView2.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            refreshView.setRefreshing(false);
          }
        });
    return this.view;
  }
  
  public PullHeader.Config getConfig() {
    return (PullHeader.Config)this.config;
  }
  
  public void onPositionChange(CoolRefreshView paramCoolRefreshView, int paramInt1, int paramInt2, int paramInt3) {
    TextView textView = this.mTextView2;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("onPositionChange status:");
    stringBuilder.append(paramInt1);
    stringBuilder.append(" dy:");
    stringBuilder.append(paramInt2);
    stringBuilder.append(" currentDistance:");
    stringBuilder.append(paramInt3);
    textView.setText(stringBuilder.toString());
  }
  
  public void onPullBegin(CoolRefreshView paramCoolRefreshView) {
    this.mTextView.setText("onPullBegin :");
  }
  
  public void onPullRefreshComplete(CoolRefreshView paramCoolRefreshView) {
    this.mTextView.setText("onPullRefreshComplete");
  }
  
  public void onRefreshing(CoolRefreshView paramCoolRefreshView) {
    this.mTextView.setText("onRefreshing");
  }
  
  public void onReset(CoolRefreshView paramCoolRefreshView, boolean paramBoolean) {
    TextView textView = this.mTextView;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("onReset pullRelease:");
    stringBuilder.append(paramBoolean);
    textView.setText(stringBuilder.toString());
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/view/coolrefreshview/header/TestHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
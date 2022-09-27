package com.shizhefei.mvc.imp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.shizhefei.mvc.ILoadViewFactory;
import com.shizhefei.view.vary.VaryViewHelper;

public class DefaultLoadViewFactory implements ILoadViewFactory {
  public static int dip2px(Context paramContext, float paramFloat) {
    return (int)(paramFloat * (paramContext.getResources().getDisplayMetrics()).density + 0.5F);
  }
  
  public ILoadViewFactory.ILoadMoreView madeLoadMoreView() {
    return new LoadMoreHelper();
  }
  
  public ILoadViewFactory.ILoadView madeLoadView() {
    return new LoadViewHelper();
  }
  
  private static class LoadMoreHelper implements ILoadViewFactory.ILoadMoreView {
    protected TextView footView;
    
    protected View.OnClickListener onClickRefreshListener;
    
    private LoadMoreHelper() {}
    
    public void init(ILoadViewFactory.FootViewAdder param1FootViewAdder, View.OnClickListener param1OnClickListener) {
      Context context = param1FootViewAdder.getContentView().getContext();
      TextView textView = new TextView(context);
      textView.setTextColor(-7829368);
      textView.setPadding(0, DefaultLoadViewFactory.dip2px(context, 16.0F), 0, DefaultLoadViewFactory.dip2px(context, 16.0F));
      textView.setGravity(17);
      param1FootViewAdder.addFootView((View)textView);
      this.footView = textView;
      this.onClickRefreshListener = param1OnClickListener;
      showNormal();
    }
    
    public void showFail(Exception param1Exception) {
      this.footView.setText("加载失败，点击重新加载");
      this.footView.setOnClickListener(this.onClickRefreshListener);
    }
    
    public void showLoading() {
      this.footView.setText("正在加载中..");
      this.footView.setOnClickListener(null);
    }
    
    public void showNomore() {
      this.footView.setText("已经加载完毕");
      this.footView.setOnClickListener(null);
    }
    
    public void showNormal() {
      this.footView.setText("点击加载更多");
      this.footView.setOnClickListener(this.onClickRefreshListener);
    }
  }
  
  private static class LoadViewHelper implements ILoadViewFactory.ILoadView {
    private Context context;
    
    private VaryViewHelper helper;
    
    private View.OnClickListener onClickRefreshListener;
    
    private LoadViewHelper() {}
    
    public void init(View param1View, View.OnClickListener param1OnClickListener) {
      this.context = param1View.getContext().getApplicationContext();
      this.onClickRefreshListener = param1OnClickListener;
      this.helper = new VaryViewHelper(param1View);
    }
    
    public void restore() {
      this.helper.restoreView();
    }
    
    public void showEmpty() {
      Context context = this.helper.getContext();
      LinearLayout linearLayout = new LinearLayout(context);
      linearLayout.setOrientation(1);
      linearLayout.setGravity(17);
      TextView textView = new TextView(context);
      textView.setText("暂无数据");
      textView.setGravity(17);
      linearLayout.addView((View)textView);
      Button button = new Button(context);
      button.setText("重试");
      button.setOnClickListener(this.onClickRefreshListener);
      LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
      layoutParams.setMargins(0, DefaultLoadViewFactory.dip2px(context, 12.0F), 0, 0);
      linearLayout.addView((View)button, (ViewGroup.LayoutParams)layoutParams);
      this.helper.showLayout((View)linearLayout);
    }
    
    public void showFail(Exception param1Exception) {
      Context context = this.helper.getContext();
      LinearLayout linearLayout = new LinearLayout(context);
      linearLayout.setOrientation(1);
      linearLayout.setGravity(17);
      TextView textView = new TextView(context);
      textView.setText("网络加载失败");
      textView.setGravity(17);
      linearLayout.addView((View)textView);
      Button button = new Button(context);
      button.setText("重试");
      button.setOnClickListener(this.onClickRefreshListener);
      LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
      layoutParams.setMargins(0, DefaultLoadViewFactory.dip2px(context, 12.0F), 0, 0);
      linearLayout.addView((View)button, (ViewGroup.LayoutParams)layoutParams);
      this.helper.showLayout((View)linearLayout);
    }
    
    public void showLoading() {
      Context context = this.helper.getContext();
      LinearLayout linearLayout = new LinearLayout(context);
      linearLayout.setOrientation(1);
      linearLayout.setGravity(17);
      linearLayout.addView((View)new ProgressBar(context));
      TextView textView = new TextView(context);
      textView.setText("加载中...");
      textView.setGravity(17);
      LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
      layoutParams.setMargins(0, DefaultLoadViewFactory.dip2px(context, 12.0F), 0, 0);
      linearLayout.addView((View)textView, (ViewGroup.LayoutParams)layoutParams);
      this.helper.showLayout((View)linearLayout);
    }
    
    public void tipFail(Exception param1Exception) {
      Toast.makeText(this.context, "网络加载失败", 0).show();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/mvc/imp/DefaultLoadViewFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
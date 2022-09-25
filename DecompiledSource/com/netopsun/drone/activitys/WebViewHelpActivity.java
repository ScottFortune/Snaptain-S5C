package com.netopsun.drone.activitys;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import com.netopsun.drone.BaseSensorLandscapeActivity;
import java.util.Locale;

public class WebViewHelpActivity extends BaseSensorLandscapeActivity implements View.OnClickListener {
  private ImageView mBackBtn;
  
  private WebView mWebView;
  
  private void initView() {
    this.mBackBtn = (ImageView)findViewById(2131230803);
    this.mBackBtn.setOnClickListener(this);
    this.mWebView = (WebView)findViewById(2131231086);
  }
  
  public void onClick(View paramView) {
    if (paramView.getId() == 2131230803)
      finish(); 
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    getWindow().setFlags(1024, 1024);
    setContentView(2131427364);
    initView();
    if (Locale.getDefault().getLanguage().contains("zh")) {
      this.mWebView.loadUrl("file:///android_asset/help_web/help_zh.pdf.html");
    } else {
      this.mWebView.loadUrl("file:///android_asset/help_web/help.pdf.html");
    } 
    WebSettings webSettings = this.mWebView.getSettings();
    webSettings.setUseWideViewPort(true);
    webSettings.setDefaultFontSize(20);
    webSettings.setLoadWithOverviewMode(true);
    this.mWebView.setWebViewClient(new WebViewClient() {
          public void onPageFinished(WebView param1WebView, String param1String) {}
        });
  }
  
  protected void onDestroy() {
    WebView webView = this.mWebView;
    if (webView != null) {
      webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
      this.mWebView.clearHistory();
      ((ViewGroup)this.mWebView.getParent()).removeView((View)this.mWebView);
      this.mWebView.destroy();
      this.mWebView = null;
    } 
    super.onDestroy();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/drone/activitys/WebViewHelpActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
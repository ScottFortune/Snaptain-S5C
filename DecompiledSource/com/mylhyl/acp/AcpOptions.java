package com.mylhyl.acp;

import android.content.Context;

public final class AcpOptions {
  private String mDeniedCloseBtn;
  
  private String mDeniedMessage;
  
  private String mDeniedSettingBtn;
  
  private boolean mDialogCancelable;
  
  private boolean mDialogCanceledOnTouchOutside;
  
  private String[] mPermissions;
  
  private String mRationalBtn;
  
  private String mRationalMessage;
  
  private AcpOptions(Builder paramBuilder) {
    this.mRationalMessage = paramBuilder.mRationalMessage;
    this.mDeniedMessage = paramBuilder.mDeniedMessage;
    this.mDeniedCloseBtn = paramBuilder.mDeniedCloseBtn;
    this.mDeniedSettingBtn = paramBuilder.mDeniedSettingBtn;
    this.mRationalBtn = paramBuilder.mRationalBtn;
    this.mPermissions = paramBuilder.mPermissions;
    this.mDialogCancelable = paramBuilder.dialogCancelable;
    this.mDialogCanceledOnTouchOutside = paramBuilder.dialogCanceledOnTouchOutside;
  }
  
  public String getDeniedCloseBtn() {
    return this.mDeniedCloseBtn;
  }
  
  public String getDeniedMessage() {
    return this.mDeniedMessage;
  }
  
  public String getDeniedSettingBtn() {
    return this.mDeniedSettingBtn;
  }
  
  public String[] getPermissions() {
    return this.mPermissions;
  }
  
  public String getRationalBtnText() {
    return this.mRationalBtn;
  }
  
  public String getRationalMessage() {
    return this.mRationalMessage;
  }
  
  public boolean isDialogCancelable() {
    return this.mDialogCancelable;
  }
  
  public boolean isDialogCanceledOnTouchOutside() {
    return this.mDialogCanceledOnTouchOutside;
  }
  
  public static class Builder {
    private static final String DEF_DENIED_CLOSE_BTN_TEXT = "关闭";
    
    private static final String DEF_DENIED_MESSAGE = "您拒绝权限申请，此功能将不能正常使用，您可以去设置页面重新授权";
    
    private static final String DEF_DENIED_SETTINGS_BTN_TEXT = "设置权限";
    
    private static final String DEF_RATIONAL_BTN_TEXT = "我知道了";
    
    private static final String DEF_RATIONAL_MESSAGE = "此功能需要您授权，否则将不能正常使用";
    
    private boolean dialogCancelable = false;
    
    private boolean dialogCanceledOnTouchOutside = false;
    
    private String mDeniedCloseBtn = "关闭";
    
    private String mDeniedMessage = "您拒绝权限申请，此功能将不能正常使用，您可以去设置页面重新授权";
    
    private String mDeniedSettingBtn = "设置权限";
    
    private String[] mPermissions;
    
    private String mRationalBtn = "我知道了";
    
    private String mRationalMessage = "此功能需要您授权，否则将不能正常使用";
    
    public Builder(Context param1Context) {
      setRationalMessage(param1Context.getString(R.string.def_rational_message));
      setDeniedMessage(param1Context.getString(R.string.def_denied_message));
      setDeniedCloseBtn(param1Context.getString(R.string.def_denied_close_btn_text));
      setDeniedSettingBtn(param1Context.getString(R.string.def_denied_settings_btn_text));
      setRationalBtn(param1Context.getString(R.string.def_rational_btn_text));
    }
    
    public AcpOptions build() {
      String[] arrayOfString = this.mPermissions;
      if (arrayOfString != null && arrayOfString.length != 0)
        return new AcpOptions(this); 
      throw new IllegalArgumentException("mPermissions no found...");
    }
    
    public Builder setDeniedCloseBtn(String param1String) {
      this.mDeniedCloseBtn = param1String;
      return this;
    }
    
    public Builder setDeniedMessage(String param1String) {
      this.mDeniedMessage = param1String;
      return this;
    }
    
    public Builder setDeniedSettingBtn(String param1String) {
      this.mDeniedSettingBtn = param1String;
      return this;
    }
    
    public Builder setDialogCancelable(boolean param1Boolean) {
      this.dialogCancelable = param1Boolean;
      return this;
    }
    
    public Builder setDialogCanceledOnTouchOutside(boolean param1Boolean) {
      this.dialogCanceledOnTouchOutside = param1Boolean;
      return this;
    }
    
    public Builder setPermissions(String... param1VarArgs) {
      this.mPermissions = param1VarArgs;
      return this;
    }
    
    public Builder setRationalBtn(String param1String) {
      this.mRationalBtn = param1String;
      return this;
    }
    
    public Builder setRationalMessage(String param1String) {
      this.mRationalMessage = param1String;
      return this;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/mylhyl/acp/AcpOptions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
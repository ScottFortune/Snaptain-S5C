package com.mylhyl.acp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

class AcpManager {
  private static final int REQUEST_CODE_PERMISSION = 56;
  
  private static final int REQUEST_CODE_SETTING = 57;
  
  private static final String TAG = "AcpManager";
  
  private Activity mActivity;
  
  private AcpListener mCallback;
  
  private Context mContext;
  
  private final List<String> mDeniedPermissions = new LinkedList<String>();
  
  private final Set<String> mManifestPermissions = new HashSet<String>(1);
  
  private AcpOptions mOptions;
  
  private AcpService mService;
  
  AcpManager(Context paramContext) {
    this.mContext = paramContext;
    this.mService = new AcpService();
    getManifestPermissions();
  }
  
  private void checkSelfPermission() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mDeniedPermissions : Ljava/util/List;
    //   6: invokeinterface clear : ()V
    //   11: getstatic android/os/Build$VERSION.SDK_INT : I
    //   14: bipush #23
    //   16: if_icmpge -> 50
    //   19: ldc 'AcpManager'
    //   21: ldc 'Build.VERSION.SDK_INT < Build.VERSION_CODES.M'
    //   23: invokestatic i : (Ljava/lang/String;Ljava/lang/String;)I
    //   26: pop
    //   27: aload_0
    //   28: getfield mCallback : Lcom/mylhyl/acp/AcpListener;
    //   31: ifnull -> 43
    //   34: aload_0
    //   35: getfield mCallback : Lcom/mylhyl/acp/AcpListener;
    //   38: invokeinterface onGranted : ()V
    //   43: aload_0
    //   44: invokespecial onDestroy : ()V
    //   47: aload_0
    //   48: monitorexit
    //   49: return
    //   50: aload_0
    //   51: getfield mOptions : Lcom/mylhyl/acp/AcpOptions;
    //   54: invokevirtual getPermissions : ()[Ljava/lang/String;
    //   57: astore_1
    //   58: aload_1
    //   59: arraylength
    //   60: istore_2
    //   61: iconst_0
    //   62: istore_3
    //   63: iload_3
    //   64: iload_2
    //   65: if_icmpge -> 163
    //   68: aload_1
    //   69: iload_3
    //   70: aaload
    //   71: astore #4
    //   73: aload_0
    //   74: getfield mManifestPermissions : Ljava/util/Set;
    //   77: aload #4
    //   79: invokeinterface contains : (Ljava/lang/Object;)Z
    //   84: ifeq -> 157
    //   87: aload_0
    //   88: getfield mService : Lcom/mylhyl/acp/AcpService;
    //   91: aload_0
    //   92: getfield mContext : Landroid/content/Context;
    //   95: aload #4
    //   97: invokevirtual checkSelfPermission : (Landroid/content/Context;Ljava/lang/String;)I
    //   100: istore #5
    //   102: new java/lang/StringBuilder
    //   105: astore #6
    //   107: aload #6
    //   109: invokespecial <init> : ()V
    //   112: aload #6
    //   114: ldc 'checkSelfPermission = '
    //   116: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   119: pop
    //   120: aload #6
    //   122: iload #5
    //   124: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   127: pop
    //   128: ldc 'AcpManager'
    //   130: aload #6
    //   132: invokevirtual toString : ()Ljava/lang/String;
    //   135: invokestatic i : (Ljava/lang/String;Ljava/lang/String;)I
    //   138: pop
    //   139: iload #5
    //   141: iconst_m1
    //   142: if_icmpne -> 157
    //   145: aload_0
    //   146: getfield mDeniedPermissions : Ljava/util/List;
    //   149: aload #4
    //   151: invokeinterface add : (Ljava/lang/Object;)Z
    //   156: pop
    //   157: iinc #3, 1
    //   160: goto -> 63
    //   163: aload_0
    //   164: getfield mDeniedPermissions : Ljava/util/List;
    //   167: invokeinterface isEmpty : ()Z
    //   172: ifeq -> 206
    //   175: ldc 'AcpManager'
    //   177: ldc 'mDeniedPermissions.isEmpty()'
    //   179: invokestatic i : (Ljava/lang/String;Ljava/lang/String;)I
    //   182: pop
    //   183: aload_0
    //   184: getfield mCallback : Lcom/mylhyl/acp/AcpListener;
    //   187: ifnull -> 199
    //   190: aload_0
    //   191: getfield mCallback : Lcom/mylhyl/acp/AcpListener;
    //   194: invokeinterface onGranted : ()V
    //   199: aload_0
    //   200: invokespecial onDestroy : ()V
    //   203: aload_0
    //   204: monitorexit
    //   205: return
    //   206: aload_0
    //   207: invokespecial startAcpActivity : ()V
    //   210: aload_0
    //   211: monitorexit
    //   212: return
    //   213: astore #6
    //   215: aload_0
    //   216: monitorexit
    //   217: goto -> 223
    //   220: aload #6
    //   222: athrow
    //   223: goto -> 220
    // Exception table:
    //   from	to	target	type
    //   2	43	213	finally
    //   43	47	213	finally
    //   50	61	213	finally
    //   73	139	213	finally
    //   145	157	213	finally
    //   163	199	213	finally
    //   199	203	213	finally
    //   206	210	213	finally
  }
  
  private void getManifestPermissions() {
    /* monitor enter ThisExpression{ObjectType{com/mylhyl/acp/AcpManager}} */
    PackageInfo packageInfo = null;
    try {
      PackageInfo packageInfo1 = this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 4096);
      packageInfo = packageInfo1;
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      nameNotFoundException.printStackTrace();
    } finally {}
    if (packageInfo != null) {
      String[] arrayOfString = packageInfo.requestedPermissions;
      if (arrayOfString != null) {
        int i = arrayOfString.length;
        for (byte b = 0; b < i; b++) {
          String str = arrayOfString[b];
          this.mManifestPermissions.add(str);
        } 
      } 
    } 
    /* monitor exit ThisExpression{ObjectType{com/mylhyl/acp/AcpManager}} */
  }
  
  private void onDestroy() {
    Activity activity = this.mActivity;
    if (activity != null) {
      activity.finish();
      this.mActivity = null;
    } 
    this.mCallback = null;
  }
  
  private void requestPermissions(String[] paramArrayOfString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mService : Lcom/mylhyl/acp/AcpService;
    //   6: aload_0
    //   7: getfield mActivity : Landroid/app/Activity;
    //   10: aload_1
    //   11: bipush #56
    //   13: invokevirtual requestPermissions : (Landroid/app/Activity;[Ljava/lang/String;I)V
    //   16: aload_0
    //   17: monitorexit
    //   18: return
    //   19: astore_1
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_1
    //   23: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	19	finally
  }
  
  private void showDeniedDialog(List<String> paramList) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new android/app/AlertDialog$Builder
    //   5: astore_2
    //   6: aload_2
    //   7: aload_0
    //   8: getfield mActivity : Landroid/app/Activity;
    //   11: invokespecial <init> : (Landroid/content/Context;)V
    //   14: aload_2
    //   15: aload_0
    //   16: getfield mOptions : Lcom/mylhyl/acp/AcpOptions;
    //   19: invokevirtual getDeniedMessage : ()Ljava/lang/String;
    //   22: invokevirtual setMessage : (Ljava/lang/CharSequence;)Landroid/app/AlertDialog$Builder;
    //   25: astore_3
    //   26: aload_0
    //   27: getfield mOptions : Lcom/mylhyl/acp/AcpOptions;
    //   30: invokevirtual getDeniedCloseBtn : ()Ljava/lang/String;
    //   33: astore #4
    //   35: new com/mylhyl/acp/AcpManager$3
    //   38: astore_2
    //   39: aload_2
    //   40: aload_0
    //   41: aload_1
    //   42: invokespecial <init> : (Lcom/mylhyl/acp/AcpManager;Ljava/util/List;)V
    //   45: aload_3
    //   46: aload #4
    //   48: aload_2
    //   49: invokevirtual setNegativeButton : (Ljava/lang/CharSequence;Landroid/content/DialogInterface$OnClickListener;)Landroid/app/AlertDialog$Builder;
    //   52: astore_1
    //   53: aload_0
    //   54: getfield mOptions : Lcom/mylhyl/acp/AcpOptions;
    //   57: invokevirtual getDeniedSettingBtn : ()Ljava/lang/String;
    //   60: astore_2
    //   61: new com/mylhyl/acp/AcpManager$2
    //   64: astore_3
    //   65: aload_3
    //   66: aload_0
    //   67: invokespecial <init> : (Lcom/mylhyl/acp/AcpManager;)V
    //   70: aload_1
    //   71: aload_2
    //   72: aload_3
    //   73: invokevirtual setPositiveButton : (Ljava/lang/CharSequence;Landroid/content/DialogInterface$OnClickListener;)Landroid/app/AlertDialog$Builder;
    //   76: invokevirtual create : ()Landroid/app/AlertDialog;
    //   79: astore_1
    //   80: aload_1
    //   81: aload_0
    //   82: getfield mOptions : Lcom/mylhyl/acp/AcpOptions;
    //   85: invokevirtual isDialogCancelable : ()Z
    //   88: invokevirtual setCancelable : (Z)V
    //   91: aload_1
    //   92: aload_0
    //   93: getfield mOptions : Lcom/mylhyl/acp/AcpOptions;
    //   96: invokevirtual isDialogCanceledOnTouchOutside : ()Z
    //   99: invokevirtual setCanceledOnTouchOutside : (Z)V
    //   102: aload_1
    //   103: invokevirtual show : ()V
    //   106: aload_0
    //   107: monitorexit
    //   108: return
    //   109: astore_1
    //   110: aload_0
    //   111: monitorexit
    //   112: aload_1
    //   113: athrow
    // Exception table:
    //   from	to	target	type
    //   2	106	109	finally
  }
  
  private void showRationalDialog(String[] paramArrayOfString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new android/app/AlertDialog$Builder
    //   5: astore_2
    //   6: aload_2
    //   7: aload_0
    //   8: getfield mActivity : Landroid/app/Activity;
    //   11: invokespecial <init> : (Landroid/content/Context;)V
    //   14: aload_2
    //   15: aload_0
    //   16: getfield mOptions : Lcom/mylhyl/acp/AcpOptions;
    //   19: invokevirtual getRationalMessage : ()Ljava/lang/String;
    //   22: invokevirtual setMessage : (Ljava/lang/CharSequence;)Landroid/app/AlertDialog$Builder;
    //   25: astore_3
    //   26: aload_0
    //   27: getfield mOptions : Lcom/mylhyl/acp/AcpOptions;
    //   30: invokevirtual getRationalBtnText : ()Ljava/lang/String;
    //   33: astore #4
    //   35: new com/mylhyl/acp/AcpManager$1
    //   38: astore_2
    //   39: aload_2
    //   40: aload_0
    //   41: aload_1
    //   42: invokespecial <init> : (Lcom/mylhyl/acp/AcpManager;[Ljava/lang/String;)V
    //   45: aload_3
    //   46: aload #4
    //   48: aload_2
    //   49: invokevirtual setPositiveButton : (Ljava/lang/CharSequence;Landroid/content/DialogInterface$OnClickListener;)Landroid/app/AlertDialog$Builder;
    //   52: invokevirtual create : ()Landroid/app/AlertDialog;
    //   55: astore_1
    //   56: aload_1
    //   57: aload_0
    //   58: getfield mOptions : Lcom/mylhyl/acp/AcpOptions;
    //   61: invokevirtual isDialogCancelable : ()Z
    //   64: invokevirtual setCancelable : (Z)V
    //   67: aload_1
    //   68: aload_0
    //   69: getfield mOptions : Lcom/mylhyl/acp/AcpOptions;
    //   72: invokevirtual isDialogCanceledOnTouchOutside : ()Z
    //   75: invokevirtual setCanceledOnTouchOutside : (Z)V
    //   78: aload_1
    //   79: invokevirtual show : ()V
    //   82: aload_0
    //   83: monitorexit
    //   84: return
    //   85: astore_1
    //   86: aload_0
    //   87: monitorexit
    //   88: aload_1
    //   89: athrow
    // Exception table:
    //   from	to	target	type
    //   2	82	85	finally
  }
  
  private void startAcpActivity() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new android/content/Intent
    //   5: astore_1
    //   6: aload_1
    //   7: aload_0
    //   8: getfield mContext : Landroid/content/Context;
    //   11: ldc_w com/mylhyl/acp/AcpActivity
    //   14: invokespecial <init> : (Landroid/content/Context;Ljava/lang/Class;)V
    //   17: aload_1
    //   18: ldc_w 268435456
    //   21: invokevirtual addFlags : (I)Landroid/content/Intent;
    //   24: pop
    //   25: aload_0
    //   26: getfield mContext : Landroid/content/Context;
    //   29: aload_1
    //   30: invokevirtual startActivity : (Landroid/content/Intent;)V
    //   33: aload_0
    //   34: monitorexit
    //   35: return
    //   36: astore_1
    //   37: aload_0
    //   38: monitorexit
    //   39: aload_1
    //   40: athrow
    // Exception table:
    //   from	to	target	type
    //   2	33	36	finally
  }
  
  private void startSetting() {
    if (MiuiOs.isMIUI()) {
      Intent intent = MiuiOs.getSettingIntent((Context)this.mActivity);
      if (MiuiOs.isIntentAvailable((Context)this.mActivity, intent)) {
        this.mActivity.startActivityForResult(intent, 57);
        return;
      } 
    } 
    try {
      Intent intent = new Intent();
      this("android.settings.APPLICATION_DETAILS_SETTINGS");
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("package:");
      stringBuilder.append(this.mActivity.getPackageName());
      intent = intent.setData(Uri.parse(stringBuilder.toString()));
      this.mActivity.startActivityForResult(intent, 57);
    } catch (ActivityNotFoundException activityNotFoundException) {
      activityNotFoundException.printStackTrace();
      try {
        Intent intent = new Intent();
        this("android.settings.MANAGE_APPLICATIONS_SETTINGS");
        this.mActivity.startActivityForResult(intent, 57);
      } catch (Exception exception) {
        exception.printStackTrace();
      } 
    } 
  }
  
  void checkRequestPermissionRationale(Activity paramActivity) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield mActivity : Landroid/app/Activity;
    //   7: aload_0
    //   8: getfield mDeniedPermissions : Ljava/util/List;
    //   11: invokeinterface iterator : ()Ljava/util/Iterator;
    //   16: astore_1
    //   17: iconst_0
    //   18: istore_2
    //   19: aload_1
    //   20: invokeinterface hasNext : ()Z
    //   25: ifeq -> 62
    //   28: aload_1
    //   29: invokeinterface next : ()Ljava/lang/Object;
    //   34: checkcast java/lang/String
    //   37: astore_3
    //   38: iload_2
    //   39: ifne -> 57
    //   42: aload_0
    //   43: getfield mService : Lcom/mylhyl/acp/AcpService;
    //   46: aload_0
    //   47: getfield mActivity : Landroid/app/Activity;
    //   50: aload_3
    //   51: invokevirtual shouldShowRequestPermissionRationale : (Landroid/app/Activity;Ljava/lang/String;)Z
    //   54: ifeq -> 17
    //   57: iconst_1
    //   58: istore_2
    //   59: goto -> 19
    //   62: new java/lang/StringBuilder
    //   65: astore_1
    //   66: aload_1
    //   67: invokespecial <init> : ()V
    //   70: aload_1
    //   71: ldc_w 'rationale = '
    //   74: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   77: pop
    //   78: aload_1
    //   79: iload_2
    //   80: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   83: pop
    //   84: ldc 'AcpManager'
    //   86: aload_1
    //   87: invokevirtual toString : ()Ljava/lang/String;
    //   90: invokestatic i : (Ljava/lang/String;Ljava/lang/String;)I
    //   93: pop
    //   94: aload_0
    //   95: getfield mDeniedPermissions : Ljava/util/List;
    //   98: aload_0
    //   99: getfield mDeniedPermissions : Ljava/util/List;
    //   102: invokeinterface size : ()I
    //   107: anewarray java/lang/String
    //   110: invokeinterface toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   115: checkcast [Ljava/lang/String;
    //   118: astore_1
    //   119: iload_2
    //   120: ifeq -> 131
    //   123: aload_0
    //   124: aload_1
    //   125: invokespecial showRationalDialog : ([Ljava/lang/String;)V
    //   128: goto -> 136
    //   131: aload_0
    //   132: aload_1
    //   133: invokespecial requestPermissions : ([Ljava/lang/String;)V
    //   136: aload_0
    //   137: monitorexit
    //   138: return
    //   139: astore_1
    //   140: aload_0
    //   141: monitorexit
    //   142: goto -> 147
    //   145: aload_1
    //   146: athrow
    //   147: goto -> 145
    // Exception table:
    //   from	to	target	type
    //   2	17	139	finally
    //   19	38	139	finally
    //   42	57	139	finally
    //   62	119	139	finally
    //   123	128	139	finally
    //   131	136	139	finally
  }
  
  void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mCallback : Lcom/mylhyl/acp/AcpListener;
    //   6: ifnull -> 32
    //   9: aload_0
    //   10: getfield mOptions : Lcom/mylhyl/acp/AcpOptions;
    //   13: ifnull -> 32
    //   16: iload_1
    //   17: bipush #57
    //   19: if_icmpeq -> 25
    //   22: goto -> 32
    //   25: aload_0
    //   26: invokespecial checkSelfPermission : ()V
    //   29: aload_0
    //   30: monitorexit
    //   31: return
    //   32: aload_0
    //   33: invokespecial onDestroy : ()V
    //   36: aload_0
    //   37: monitorexit
    //   38: return
    //   39: astore_3
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_3
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	39	finally
    //   25	29	39	finally
    //   32	36	39	finally
  }
  
  void onRequestPermissionsResult(int paramInt, String[] paramArrayOfString, int[] paramArrayOfint) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iload_1
    //   3: bipush #56
    //   5: if_icmpeq -> 11
    //   8: goto -> 128
    //   11: new java/util/LinkedList
    //   14: astore #4
    //   16: aload #4
    //   18: invokespecial <init> : ()V
    //   21: new java/util/LinkedList
    //   24: astore #5
    //   26: aload #5
    //   28: invokespecial <init> : ()V
    //   31: iconst_0
    //   32: istore_1
    //   33: iload_1
    //   34: aload_2
    //   35: arraylength
    //   36: if_icmpge -> 75
    //   39: aload_2
    //   40: iload_1
    //   41: aaload
    //   42: astore #6
    //   44: aload_3
    //   45: iload_1
    //   46: iaload
    //   47: ifne -> 61
    //   50: aload #4
    //   52: aload #6
    //   54: invokevirtual add : (Ljava/lang/Object;)Z
    //   57: pop
    //   58: goto -> 69
    //   61: aload #5
    //   63: aload #6
    //   65: invokevirtual add : (Ljava/lang/Object;)Z
    //   68: pop
    //   69: iinc #1, 1
    //   72: goto -> 33
    //   75: aload #4
    //   77: invokevirtual isEmpty : ()Z
    //   80: ifne -> 114
    //   83: aload #5
    //   85: invokevirtual isEmpty : ()Z
    //   88: ifeq -> 114
    //   91: aload_0
    //   92: getfield mCallback : Lcom/mylhyl/acp/AcpListener;
    //   95: ifnull -> 107
    //   98: aload_0
    //   99: getfield mCallback : Lcom/mylhyl/acp/AcpListener;
    //   102: invokeinterface onGranted : ()V
    //   107: aload_0
    //   108: invokespecial onDestroy : ()V
    //   111: goto -> 128
    //   114: aload #5
    //   116: invokevirtual isEmpty : ()Z
    //   119: ifne -> 128
    //   122: aload_0
    //   123: aload #5
    //   125: invokespecial showDeniedDialog : (Ljava/util/List;)V
    //   128: aload_0
    //   129: monitorexit
    //   130: return
    //   131: astore_2
    //   132: aload_0
    //   133: monitorexit
    //   134: goto -> 139
    //   137: aload_2
    //   138: athrow
    //   139: goto -> 137
    // Exception table:
    //   from	to	target	type
    //   11	31	131	finally
    //   33	39	131	finally
    //   50	58	131	finally
    //   61	69	131	finally
    //   75	107	131	finally
    //   107	111	131	finally
    //   114	128	131	finally
  }
  
  void request(AcpOptions paramAcpOptions, AcpListener paramAcpListener) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_2
    //   4: putfield mCallback : Lcom/mylhyl/acp/AcpListener;
    //   7: aload_0
    //   8: aload_1
    //   9: putfield mOptions : Lcom/mylhyl/acp/AcpOptions;
    //   12: aload_0
    //   13: invokespecial checkSelfPermission : ()V
    //   16: aload_0
    //   17: monitorexit
    //   18: return
    //   19: astore_1
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_1
    //   23: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	19	finally
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/mylhyl/acp/AcpManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
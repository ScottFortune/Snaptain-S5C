package androidx.appcompat.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.ThemedSpinnerAdapter;
import androidx.appcompat.R;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.menu.ShowableListMenu;
import androidx.core.view.TintableBackgroundView;
import androidx.core.view.ViewCompat;

public class AppCompatSpinner extends Spinner implements TintableBackgroundView {
  private static final int[] ATTRS_ANDROID_SPINNERMODE = new int[] { 16843505 };
  
  private static final int MAX_ITEMS_MEASURED = 15;
  
  private static final int MODE_DIALOG = 0;
  
  private static final int MODE_DROPDOWN = 1;
  
  private static final int MODE_THEME = -1;
  
  private static final String TAG = "AppCompatSpinner";
  
  private final AppCompatBackgroundHelper mBackgroundTintHelper;
  
  int mDropDownWidth;
  
  private ForwardingListener mForwardingListener;
  
  private SpinnerPopup mPopup;
  
  private final Context mPopupContext;
  
  private final boolean mPopupSet;
  
  private SpinnerAdapter mTempAdapter;
  
  final Rect mTempRect;
  
  public AppCompatSpinner(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public AppCompatSpinner(Context paramContext, int paramInt) {
    this(paramContext, (AttributeSet)null, R.attr.spinnerStyle, paramInt);
  }
  
  public AppCompatSpinner(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.spinnerStyle);
  }
  
  public AppCompatSpinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    this(paramContext, paramAttributeSet, paramInt, -1);
  }
  
  public AppCompatSpinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    this(paramContext, paramAttributeSet, paramInt1, paramInt2, (Resources.Theme)null);
  }
  
  public AppCompatSpinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2, Resources.Theme paramTheme) {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: aload_2
    //   3: iload_3
    //   4: invokespecial <init> : (Landroid/content/Context;Landroid/util/AttributeSet;I)V
    //   7: aload_0
    //   8: new android/graphics/Rect
    //   11: dup
    //   12: invokespecial <init> : ()V
    //   15: putfield mTempRect : Landroid/graphics/Rect;
    //   18: aload_1
    //   19: aload_2
    //   20: getstatic androidx/appcompat/R$styleable.Spinner : [I
    //   23: iload_3
    //   24: iconst_0
    //   25: invokestatic obtainStyledAttributes : (Landroid/content/Context;Landroid/util/AttributeSet;[III)Landroidx/appcompat/widget/TintTypedArray;
    //   28: astore #6
    //   30: aload_0
    //   31: new androidx/appcompat/widget/AppCompatBackgroundHelper
    //   34: dup
    //   35: aload_0
    //   36: invokespecial <init> : (Landroid/view/View;)V
    //   39: putfield mBackgroundTintHelper : Landroidx/appcompat/widget/AppCompatBackgroundHelper;
    //   42: aload #5
    //   44: ifnull -> 64
    //   47: aload_0
    //   48: new androidx/appcompat/view/ContextThemeWrapper
    //   51: dup
    //   52: aload_1
    //   53: aload #5
    //   55: invokespecial <init> : (Landroid/content/Context;Landroid/content/res/Resources$Theme;)V
    //   58: putfield mPopupContext : Landroid/content/Context;
    //   61: goto -> 102
    //   64: aload #6
    //   66: getstatic androidx/appcompat/R$styleable.Spinner_popupTheme : I
    //   69: iconst_0
    //   70: invokevirtual getResourceId : (II)I
    //   73: istore #7
    //   75: iload #7
    //   77: ifeq -> 97
    //   80: aload_0
    //   81: new androidx/appcompat/view/ContextThemeWrapper
    //   84: dup
    //   85: aload_1
    //   86: iload #7
    //   88: invokespecial <init> : (Landroid/content/Context;I)V
    //   91: putfield mPopupContext : Landroid/content/Context;
    //   94: goto -> 102
    //   97: aload_0
    //   98: aload_1
    //   99: putfield mPopupContext : Landroid/content/Context;
    //   102: iload #4
    //   104: istore #8
    //   106: iload #4
    //   108: iconst_m1
    //   109: if_icmpne -> 235
    //   112: aload_1
    //   113: aload_2
    //   114: getstatic androidx/appcompat/widget/AppCompatSpinner.ATTRS_ANDROID_SPINNERMODE : [I
    //   117: iload_3
    //   118: iconst_0
    //   119: invokevirtual obtainStyledAttributes : (Landroid/util/AttributeSet;[III)Landroid/content/res/TypedArray;
    //   122: astore #5
    //   124: iload #4
    //   126: istore #7
    //   128: aload #5
    //   130: astore #9
    //   132: aload #5
    //   134: iconst_0
    //   135: invokevirtual hasValue : (I)Z
    //   138: ifeq -> 154
    //   141: aload #5
    //   143: astore #9
    //   145: aload #5
    //   147: iconst_0
    //   148: iconst_0
    //   149: invokevirtual getInt : (II)I
    //   152: istore #7
    //   154: iload #7
    //   156: istore #8
    //   158: aload #5
    //   160: ifnull -> 235
    //   163: iload #7
    //   165: istore #4
    //   167: aload #5
    //   169: invokevirtual recycle : ()V
    //   172: iload #4
    //   174: istore #8
    //   176: goto -> 235
    //   179: astore #10
    //   181: goto -> 196
    //   184: astore_1
    //   185: aconst_null
    //   186: astore #9
    //   188: goto -> 223
    //   191: astore #10
    //   193: aconst_null
    //   194: astore #5
    //   196: aload #5
    //   198: astore #9
    //   200: ldc 'AppCompatSpinner'
    //   202: ldc 'Could not read android:spinnerMode'
    //   204: aload #10
    //   206: invokestatic i : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   209: pop
    //   210: iload #4
    //   212: istore #8
    //   214: aload #5
    //   216: ifnull -> 235
    //   219: goto -> 167
    //   222: astore_1
    //   223: aload #9
    //   225: ifnull -> 233
    //   228: aload #9
    //   230: invokevirtual recycle : ()V
    //   233: aload_1
    //   234: athrow
    //   235: iload #8
    //   237: ifeq -> 349
    //   240: iload #8
    //   242: iconst_1
    //   243: if_icmpeq -> 249
    //   246: goto -> 378
    //   249: new androidx/appcompat/widget/AppCompatSpinner$DropdownPopup
    //   252: dup
    //   253: aload_0
    //   254: aload_0
    //   255: getfield mPopupContext : Landroid/content/Context;
    //   258: aload_2
    //   259: iload_3
    //   260: invokespecial <init> : (Landroidx/appcompat/widget/AppCompatSpinner;Landroid/content/Context;Landroid/util/AttributeSet;I)V
    //   263: astore #9
    //   265: aload_0
    //   266: getfield mPopupContext : Landroid/content/Context;
    //   269: aload_2
    //   270: getstatic androidx/appcompat/R$styleable.Spinner : [I
    //   273: iload_3
    //   274: iconst_0
    //   275: invokestatic obtainStyledAttributes : (Landroid/content/Context;Landroid/util/AttributeSet;[III)Landroidx/appcompat/widget/TintTypedArray;
    //   278: astore #5
    //   280: aload_0
    //   281: aload #5
    //   283: getstatic androidx/appcompat/R$styleable.Spinner_android_dropDownWidth : I
    //   286: bipush #-2
    //   288: invokevirtual getLayoutDimension : (II)I
    //   291: putfield mDropDownWidth : I
    //   294: aload #9
    //   296: aload #5
    //   298: getstatic androidx/appcompat/R$styleable.Spinner_android_popupBackground : I
    //   301: invokevirtual getDrawable : (I)Landroid/graphics/drawable/Drawable;
    //   304: invokevirtual setBackgroundDrawable : (Landroid/graphics/drawable/Drawable;)V
    //   307: aload #9
    //   309: aload #6
    //   311: getstatic androidx/appcompat/R$styleable.Spinner_android_prompt : I
    //   314: invokevirtual getString : (I)Ljava/lang/String;
    //   317: invokevirtual setPromptText : (Ljava/lang/CharSequence;)V
    //   320: aload #5
    //   322: invokevirtual recycle : ()V
    //   325: aload_0
    //   326: aload #9
    //   328: putfield mPopup : Landroidx/appcompat/widget/AppCompatSpinner$SpinnerPopup;
    //   331: aload_0
    //   332: new androidx/appcompat/widget/AppCompatSpinner$1
    //   335: dup
    //   336: aload_0
    //   337: aload_0
    //   338: aload #9
    //   340: invokespecial <init> : (Landroidx/appcompat/widget/AppCompatSpinner;Landroid/view/View;Landroidx/appcompat/widget/AppCompatSpinner$DropdownPopup;)V
    //   343: putfield mForwardingListener : Landroidx/appcompat/widget/ForwardingListener;
    //   346: goto -> 378
    //   349: aload_0
    //   350: new androidx/appcompat/widget/AppCompatSpinner$DialogPopup
    //   353: dup
    //   354: aload_0
    //   355: invokespecial <init> : (Landroidx/appcompat/widget/AppCompatSpinner;)V
    //   358: putfield mPopup : Landroidx/appcompat/widget/AppCompatSpinner$SpinnerPopup;
    //   361: aload_0
    //   362: getfield mPopup : Landroidx/appcompat/widget/AppCompatSpinner$SpinnerPopup;
    //   365: aload #6
    //   367: getstatic androidx/appcompat/R$styleable.Spinner_android_prompt : I
    //   370: invokevirtual getString : (I)Ljava/lang/String;
    //   373: invokeinterface setPromptText : (Ljava/lang/CharSequence;)V
    //   378: aload #6
    //   380: getstatic androidx/appcompat/R$styleable.Spinner_android_entries : I
    //   383: invokevirtual getTextArray : (I)[Ljava/lang/CharSequence;
    //   386: astore #5
    //   388: aload #5
    //   390: ifnull -> 418
    //   393: new android/widget/ArrayAdapter
    //   396: dup
    //   397: aload_1
    //   398: ldc 17367048
    //   400: aload #5
    //   402: invokespecial <init> : (Landroid/content/Context;I[Ljava/lang/Object;)V
    //   405: astore_1
    //   406: aload_1
    //   407: getstatic androidx/appcompat/R$layout.support_simple_spinner_dropdown_item : I
    //   410: invokevirtual setDropDownViewResource : (I)V
    //   413: aload_0
    //   414: aload_1
    //   415: invokevirtual setAdapter : (Landroid/widget/SpinnerAdapter;)V
    //   418: aload #6
    //   420: invokevirtual recycle : ()V
    //   423: aload_0
    //   424: iconst_1
    //   425: putfield mPopupSet : Z
    //   428: aload_0
    //   429: getfield mTempAdapter : Landroid/widget/SpinnerAdapter;
    //   432: astore_1
    //   433: aload_1
    //   434: ifnull -> 447
    //   437: aload_0
    //   438: aload_1
    //   439: invokevirtual setAdapter : (Landroid/widget/SpinnerAdapter;)V
    //   442: aload_0
    //   443: aconst_null
    //   444: putfield mTempAdapter : Landroid/widget/SpinnerAdapter;
    //   447: aload_0
    //   448: getfield mBackgroundTintHelper : Landroidx/appcompat/widget/AppCompatBackgroundHelper;
    //   451: aload_2
    //   452: iload_3
    //   453: invokevirtual loadFromAttributes : (Landroid/util/AttributeSet;I)V
    //   456: return
    // Exception table:
    //   from	to	target	type
    //   112	124	191	java/lang/Exception
    //   112	124	184	finally
    //   132	141	179	java/lang/Exception
    //   132	141	222	finally
    //   145	154	179	java/lang/Exception
    //   145	154	222	finally
    //   200	210	222	finally
  }
  
  int compatMeasureContentWidth(SpinnerAdapter paramSpinnerAdapter, Drawable paramDrawable) {
    int i = 0;
    if (paramSpinnerAdapter == null)
      return 0; 
    int j = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 0);
    int k = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 0);
    int m = Math.max(0, getSelectedItemPosition());
    int n = Math.min(paramSpinnerAdapter.getCount(), m + 15);
    int i1 = Math.max(0, m - 15 - n - m);
    View view = null;
    m = 0;
    while (i1 < n) {
      int i2 = paramSpinnerAdapter.getItemViewType(i1);
      int i3 = i;
      if (i2 != i) {
        view = null;
        i3 = i2;
      } 
      view = paramSpinnerAdapter.getView(i1, view, (ViewGroup)this);
      if (view.getLayoutParams() == null)
        view.setLayoutParams(new ViewGroup.LayoutParams(-2, -2)); 
      view.measure(j, k);
      m = Math.max(m, view.getMeasuredWidth());
      i1++;
      i = i3;
    } 
    i1 = m;
    if (paramDrawable != null) {
      paramDrawable.getPadding(this.mTempRect);
      i1 = m + this.mTempRect.left + this.mTempRect.right;
    } 
    return i1;
  }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null)
      appCompatBackgroundHelper.applySupportBackgroundTint(); 
  }
  
  public int getDropDownHorizontalOffset() {
    SpinnerPopup spinnerPopup = this.mPopup;
    return (spinnerPopup != null) ? spinnerPopup.getHorizontalOffset() : ((Build.VERSION.SDK_INT >= 16) ? super.getDropDownHorizontalOffset() : 0);
  }
  
  public int getDropDownVerticalOffset() {
    SpinnerPopup spinnerPopup = this.mPopup;
    return (spinnerPopup != null) ? spinnerPopup.getVerticalOffset() : ((Build.VERSION.SDK_INT >= 16) ? super.getDropDownVerticalOffset() : 0);
  }
  
  public int getDropDownWidth() {
    return (this.mPopup != null) ? this.mDropDownWidth : ((Build.VERSION.SDK_INT >= 16) ? super.getDropDownWidth() : 0);
  }
  
  final SpinnerPopup getInternalPopup() {
    return this.mPopup;
  }
  
  public Drawable getPopupBackground() {
    SpinnerPopup spinnerPopup = this.mPopup;
    return (spinnerPopup != null) ? spinnerPopup.getBackground() : ((Build.VERSION.SDK_INT >= 16) ? super.getPopupBackground() : null);
  }
  
  public Context getPopupContext() {
    return this.mPopupContext;
  }
  
  public CharSequence getPrompt() {
    CharSequence charSequence;
    SpinnerPopup spinnerPopup = this.mPopup;
    if (spinnerPopup != null) {
      charSequence = spinnerPopup.getHintText();
    } else {
      charSequence = super.getPrompt();
    } 
    return charSequence;
  }
  
  public ColorStateList getSupportBackgroundTintList() {
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null) {
      ColorStateList colorStateList = appCompatBackgroundHelper.getSupportBackgroundTintList();
    } else {
      appCompatBackgroundHelper = null;
    } 
    return (ColorStateList)appCompatBackgroundHelper;
  }
  
  public PorterDuff.Mode getSupportBackgroundTintMode() {
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null) {
      PorterDuff.Mode mode = appCompatBackgroundHelper.getSupportBackgroundTintMode();
    } else {
      appCompatBackgroundHelper = null;
    } 
    return (PorterDuff.Mode)appCompatBackgroundHelper;
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    SpinnerPopup spinnerPopup = this.mPopup;
    if (spinnerPopup != null && spinnerPopup.isShowing())
      this.mPopup.dismiss(); 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, paramInt2);
    if (this.mPopup != null && View.MeasureSpec.getMode(paramInt1) == Integer.MIN_VALUE)
      setMeasuredDimension(Math.min(Math.max(getMeasuredWidth(), compatMeasureContentWidth(getAdapter(), getBackground())), View.MeasureSpec.getSize(paramInt1)), getMeasuredHeight()); 
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    if (savedState.mShowDropdown) {
      ViewTreeObserver viewTreeObserver = getViewTreeObserver();
      if (viewTreeObserver != null)
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
              public void onGlobalLayout() {
                if (!AppCompatSpinner.this.getInternalPopup().isShowing())
                  AppCompatSpinner.this.showPopup(); 
                ViewTreeObserver viewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
                if (viewTreeObserver != null)
                  if (Build.VERSION.SDK_INT >= 16) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this);
                  } else {
                    viewTreeObserver.removeGlobalOnLayoutListener(this);
                  }  
              }
            }); 
    } 
  }
  
  public Parcelable onSaveInstanceState() {
    boolean bool;
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    SpinnerPopup spinnerPopup = this.mPopup;
    if (spinnerPopup != null && spinnerPopup.isShowing()) {
      bool = true;
    } else {
      bool = false;
    } 
    savedState.mShowDropdown = bool;
    return (Parcelable)savedState;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    ForwardingListener forwardingListener = this.mForwardingListener;
    return (forwardingListener != null && forwardingListener.onTouch((View)this, paramMotionEvent)) ? true : super.onTouchEvent(paramMotionEvent);
  }
  
  public boolean performClick() {
    SpinnerPopup spinnerPopup = this.mPopup;
    if (spinnerPopup != null) {
      if (!spinnerPopup.isShowing())
        showPopup(); 
      return true;
    } 
    return super.performClick();
  }
  
  public void setAdapter(SpinnerAdapter paramSpinnerAdapter) {
    if (!this.mPopupSet) {
      this.mTempAdapter = paramSpinnerAdapter;
      return;
    } 
    super.setAdapter(paramSpinnerAdapter);
    if (this.mPopup != null) {
      Context context1 = this.mPopupContext;
      Context context2 = context1;
      if (context1 == null)
        context2 = getContext(); 
      this.mPopup.setAdapter(new DropDownAdapter(paramSpinnerAdapter, context2.getTheme()));
    } 
  }
  
  public void setBackgroundDrawable(Drawable paramDrawable) {
    super.setBackgroundDrawable(paramDrawable);
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null)
      appCompatBackgroundHelper.onSetBackgroundDrawable(paramDrawable); 
  }
  
  public void setBackgroundResource(int paramInt) {
    super.setBackgroundResource(paramInt);
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null)
      appCompatBackgroundHelper.onSetBackgroundResource(paramInt); 
  }
  
  public void setDropDownHorizontalOffset(int paramInt) {
    SpinnerPopup spinnerPopup = this.mPopup;
    if (spinnerPopup != null) {
      spinnerPopup.setHorizontalOriginalOffset(paramInt);
      this.mPopup.setHorizontalOffset(paramInt);
    } else if (Build.VERSION.SDK_INT >= 16) {
      super.setDropDownHorizontalOffset(paramInt);
    } 
  }
  
  public void setDropDownVerticalOffset(int paramInt) {
    SpinnerPopup spinnerPopup = this.mPopup;
    if (spinnerPopup != null) {
      spinnerPopup.setVerticalOffset(paramInt);
    } else if (Build.VERSION.SDK_INT >= 16) {
      super.setDropDownVerticalOffset(paramInt);
    } 
  }
  
  public void setDropDownWidth(int paramInt) {
    if (this.mPopup != null) {
      this.mDropDownWidth = paramInt;
    } else if (Build.VERSION.SDK_INT >= 16) {
      super.setDropDownWidth(paramInt);
    } 
  }
  
  public void setPopupBackgroundDrawable(Drawable paramDrawable) {
    SpinnerPopup spinnerPopup = this.mPopup;
    if (spinnerPopup != null) {
      spinnerPopup.setBackgroundDrawable(paramDrawable);
    } else if (Build.VERSION.SDK_INT >= 16) {
      super.setPopupBackgroundDrawable(paramDrawable);
    } 
  }
  
  public void setPopupBackgroundResource(int paramInt) {
    setPopupBackgroundDrawable(AppCompatResources.getDrawable(getPopupContext(), paramInt));
  }
  
  public void setPrompt(CharSequence paramCharSequence) {
    SpinnerPopup spinnerPopup = this.mPopup;
    if (spinnerPopup != null) {
      spinnerPopup.setPromptText(paramCharSequence);
    } else {
      super.setPrompt(paramCharSequence);
    } 
  }
  
  public void setSupportBackgroundTintList(ColorStateList paramColorStateList) {
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null)
      appCompatBackgroundHelper.setSupportBackgroundTintList(paramColorStateList); 
  }
  
  public void setSupportBackgroundTintMode(PorterDuff.Mode paramMode) {
    AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
    if (appCompatBackgroundHelper != null)
      appCompatBackgroundHelper.setSupportBackgroundTintMode(paramMode); 
  }
  
  void showPopup() {
    if (Build.VERSION.SDK_INT >= 17) {
      this.mPopup.show(getTextDirection(), getTextAlignment());
    } else {
      this.mPopup.show(-1, -1);
    } 
  }
  
  class DialogPopup implements SpinnerPopup, DialogInterface.OnClickListener {
    private ListAdapter mListAdapter;
    
    AlertDialog mPopup;
    
    private CharSequence mPrompt;
    
    public void dismiss() {
      AlertDialog alertDialog = this.mPopup;
      if (alertDialog != null) {
        alertDialog.dismiss();
        this.mPopup = null;
      } 
    }
    
    public Drawable getBackground() {
      return null;
    }
    
    public CharSequence getHintText() {
      return this.mPrompt;
    }
    
    public int getHorizontalOffset() {
      return 0;
    }
    
    public int getHorizontalOriginalOffset() {
      return 0;
    }
    
    public int getVerticalOffset() {
      return 0;
    }
    
    public boolean isShowing() {
      boolean bool;
      AlertDialog alertDialog = this.mPopup;
      if (alertDialog != null) {
        bool = alertDialog.isShowing();
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onClick(DialogInterface param1DialogInterface, int param1Int) {
      AppCompatSpinner.this.setSelection(param1Int);
      if (AppCompatSpinner.this.getOnItemClickListener() != null)
        AppCompatSpinner.this.performItemClick(null, param1Int, this.mListAdapter.getItemId(param1Int)); 
      dismiss();
    }
    
    public void setAdapter(ListAdapter param1ListAdapter) {
      this.mListAdapter = param1ListAdapter;
    }
    
    public void setBackgroundDrawable(Drawable param1Drawable) {
      Log.e("AppCompatSpinner", "Cannot set popup background for MODE_DIALOG, ignoring");
    }
    
    public void setHorizontalOffset(int param1Int) {
      Log.e("AppCompatSpinner", "Cannot set horizontal offset for MODE_DIALOG, ignoring");
    }
    
    public void setHorizontalOriginalOffset(int param1Int) {
      Log.e("AppCompatSpinner", "Cannot set horizontal (original) offset for MODE_DIALOG, ignoring");
    }
    
    public void setPromptText(CharSequence param1CharSequence) {
      this.mPrompt = param1CharSequence;
    }
    
    public void setVerticalOffset(int param1Int) {
      Log.e("AppCompatSpinner", "Cannot set vertical offset for MODE_DIALOG, ignoring");
    }
    
    public void show(int param1Int1, int param1Int2) {
      if (this.mListAdapter == null)
        return; 
      AlertDialog.Builder builder = new AlertDialog.Builder(AppCompatSpinner.this.getPopupContext());
      CharSequence charSequence = this.mPrompt;
      if (charSequence != null)
        builder.setTitle(charSequence); 
      this.mPopup = builder.setSingleChoiceItems(this.mListAdapter, AppCompatSpinner.this.getSelectedItemPosition(), this).create();
      ListView listView = this.mPopup.getListView();
      if (Build.VERSION.SDK_INT >= 17) {
        listView.setTextDirection(param1Int1);
        listView.setTextAlignment(param1Int2);
      } 
      this.mPopup.show();
    }
  }
  
  private static class DropDownAdapter implements ListAdapter, SpinnerAdapter {
    private SpinnerAdapter mAdapter;
    
    private ListAdapter mListAdapter;
    
    public DropDownAdapter(SpinnerAdapter param1SpinnerAdapter, Resources.Theme param1Theme) {
      this.mAdapter = param1SpinnerAdapter;
      if (param1SpinnerAdapter instanceof ListAdapter)
        this.mListAdapter = (ListAdapter)param1SpinnerAdapter; 
      if (param1Theme != null) {
        ThemedSpinnerAdapter themedSpinnerAdapter;
        if (Build.VERSION.SDK_INT >= 23 && param1SpinnerAdapter instanceof ThemedSpinnerAdapter) {
          themedSpinnerAdapter = (ThemedSpinnerAdapter)param1SpinnerAdapter;
          if (themedSpinnerAdapter.getDropDownViewTheme() != param1Theme)
            themedSpinnerAdapter.setDropDownViewTheme(param1Theme); 
        } else if (themedSpinnerAdapter instanceof ThemedSpinnerAdapter) {
          ThemedSpinnerAdapter themedSpinnerAdapter1 = (ThemedSpinnerAdapter)themedSpinnerAdapter;
          if (themedSpinnerAdapter1.getDropDownViewTheme() == null)
            themedSpinnerAdapter1.setDropDownViewTheme(param1Theme); 
        } 
      } 
    }
    
    public boolean areAllItemsEnabled() {
      ListAdapter listAdapter = this.mListAdapter;
      return (listAdapter != null) ? listAdapter.areAllItemsEnabled() : true;
    }
    
    public int getCount() {
      int i;
      SpinnerAdapter spinnerAdapter = this.mAdapter;
      if (spinnerAdapter == null) {
        i = 0;
      } else {
        i = spinnerAdapter.getCount();
      } 
      return i;
    }
    
    public View getDropDownView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
      SpinnerAdapter spinnerAdapter = this.mAdapter;
      if (spinnerAdapter == null) {
        param1View = null;
      } else {
        param1View = spinnerAdapter.getDropDownView(param1Int, param1View, param1ViewGroup);
      } 
      return param1View;
    }
    
    public Object getItem(int param1Int) {
      Object object = this.mAdapter;
      if (object == null) {
        object = null;
      } else {
        object = object.getItem(param1Int);
      } 
      return object;
    }
    
    public long getItemId(int param1Int) {
      long l;
      SpinnerAdapter spinnerAdapter = this.mAdapter;
      if (spinnerAdapter == null) {
        l = -1L;
      } else {
        l = spinnerAdapter.getItemId(param1Int);
      } 
      return l;
    }
    
    public int getItemViewType(int param1Int) {
      return 0;
    }
    
    public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
      return getDropDownView(param1Int, param1View, param1ViewGroup);
    }
    
    public int getViewTypeCount() {
      return 1;
    }
    
    public boolean hasStableIds() {
      boolean bool;
      SpinnerAdapter spinnerAdapter = this.mAdapter;
      if (spinnerAdapter != null && spinnerAdapter.hasStableIds()) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public boolean isEmpty() {
      boolean bool;
      if (getCount() == 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public boolean isEnabled(int param1Int) {
      ListAdapter listAdapter = this.mListAdapter;
      return (listAdapter != null) ? listAdapter.isEnabled(param1Int) : true;
    }
    
    public void registerDataSetObserver(DataSetObserver param1DataSetObserver) {
      SpinnerAdapter spinnerAdapter = this.mAdapter;
      if (spinnerAdapter != null)
        spinnerAdapter.registerDataSetObserver(param1DataSetObserver); 
    }
    
    public void unregisterDataSetObserver(DataSetObserver param1DataSetObserver) {
      SpinnerAdapter spinnerAdapter = this.mAdapter;
      if (spinnerAdapter != null)
        spinnerAdapter.unregisterDataSetObserver(param1DataSetObserver); 
    }
  }
  
  class DropdownPopup extends ListPopupWindow implements SpinnerPopup {
    ListAdapter mAdapter;
    
    private CharSequence mHintText;
    
    private int mOriginalHorizontalOffset;
    
    private final Rect mVisibleRect = new Rect();
    
    public DropdownPopup(Context param1Context, AttributeSet param1AttributeSet, int param1Int) {
      super(param1Context, param1AttributeSet, param1Int);
      setAnchorView((View)AppCompatSpinner.this);
      setModal(true);
      setPromptPosition(0);
      setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> param2AdapterView, View param2View, int param2Int, long param2Long) {
              AppCompatSpinner.this.setSelection(param2Int);
              if (AppCompatSpinner.this.getOnItemClickListener() != null)
                AppCompatSpinner.this.performItemClick(param2View, param2Int, AppCompatSpinner.DropdownPopup.this.mAdapter.getItemId(param2Int)); 
              AppCompatSpinner.DropdownPopup.this.dismiss();
            }
          });
    }
    
    void computeContentWidth() {
      Drawable drawable = getBackground();
      int i = 0;
      if (drawable != null) {
        drawable.getPadding(AppCompatSpinner.this.mTempRect);
        if (ViewUtils.isLayoutRtl((View)AppCompatSpinner.this)) {
          i = AppCompatSpinner.this.mTempRect.right;
        } else {
          i = -AppCompatSpinner.this.mTempRect.left;
        } 
      } else {
        Rect rect = AppCompatSpinner.this.mTempRect;
        AppCompatSpinner.this.mTempRect.right = 0;
        rect.left = 0;
      } 
      int j = AppCompatSpinner.this.getPaddingLeft();
      int k = AppCompatSpinner.this.getPaddingRight();
      int m = AppCompatSpinner.this.getWidth();
      if (AppCompatSpinner.this.mDropDownWidth == -2) {
        int n = AppCompatSpinner.this.compatMeasureContentWidth((SpinnerAdapter)this.mAdapter, getBackground());
        int i1 = (AppCompatSpinner.this.getContext().getResources().getDisplayMetrics()).widthPixels - AppCompatSpinner.this.mTempRect.left - AppCompatSpinner.this.mTempRect.right;
        int i2 = n;
        if (n > i1)
          i2 = i1; 
        setContentWidth(Math.max(i2, m - j - k));
      } else if (AppCompatSpinner.this.mDropDownWidth == -1) {
        setContentWidth(m - j - k);
      } else {
        setContentWidth(AppCompatSpinner.this.mDropDownWidth);
      } 
      if (ViewUtils.isLayoutRtl((View)AppCompatSpinner.this)) {
        i += m - k - getWidth() - getHorizontalOriginalOffset();
      } else {
        i += j + getHorizontalOriginalOffset();
      } 
      setHorizontalOffset(i);
    }
    
    public CharSequence getHintText() {
      return this.mHintText;
    }
    
    public int getHorizontalOriginalOffset() {
      return this.mOriginalHorizontalOffset;
    }
    
    boolean isVisibleToUser(View param1View) {
      boolean bool;
      if (ViewCompat.isAttachedToWindow(param1View) && param1View.getGlobalVisibleRect(this.mVisibleRect)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void setAdapter(ListAdapter param1ListAdapter) {
      super.setAdapter(param1ListAdapter);
      this.mAdapter = param1ListAdapter;
    }
    
    public void setHorizontalOriginalOffset(int param1Int) {
      this.mOriginalHorizontalOffset = param1Int;
    }
    
    public void setPromptText(CharSequence param1CharSequence) {
      this.mHintText = param1CharSequence;
    }
    
    public void show(int param1Int1, int param1Int2) {
      boolean bool = isShowing();
      computeContentWidth();
      setInputMethodMode(2);
      show();
      ListView listView = getListView();
      listView.setChoiceMode(1);
      if (Build.VERSION.SDK_INT >= 17) {
        listView.setTextDirection(param1Int1);
        listView.setTextAlignment(param1Int2);
      } 
      setSelection(AppCompatSpinner.this.getSelectedItemPosition());
      if (bool)
        return; 
      ViewTreeObserver viewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
      if (viewTreeObserver != null) {
        final ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
              AppCompatSpinner.DropdownPopup dropdownPopup = AppCompatSpinner.DropdownPopup.this;
              if (!dropdownPopup.isVisibleToUser((View)AppCompatSpinner.this)) {
                AppCompatSpinner.DropdownPopup.this.dismiss();
              } else {
                AppCompatSpinner.DropdownPopup.this.computeContentWidth();
                AppCompatSpinner.DropdownPopup.this.show();
              } 
            }
          };
        viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener);
        setOnDismissListener(new PopupWindow.OnDismissListener() {
              public void onDismiss() {
                ViewTreeObserver viewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
                if (viewTreeObserver != null)
                  viewTreeObserver.removeGlobalOnLayoutListener(layoutListener); 
              }
            });
      } 
    }
  }
  
  class null implements AdapterView.OnItemClickListener {
    public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
      AppCompatSpinner.this.setSelection(param1Int);
      if (AppCompatSpinner.this.getOnItemClickListener() != null)
        AppCompatSpinner.this.performItemClick(param1View, param1Int, this.this$1.mAdapter.getItemId(param1Int)); 
      this.this$1.dismiss();
    }
  }
  
  class null implements ViewTreeObserver.OnGlobalLayoutListener {
    public void onGlobalLayout() {
      AppCompatSpinner.DropdownPopup dropdownPopup = this.this$1;
      if (!dropdownPopup.isVisibleToUser((View)AppCompatSpinner.this)) {
        this.this$1.dismiss();
      } else {
        this.this$1.computeContentWidth();
        this.this$1.show();
      } 
    }
  }
  
  class null implements PopupWindow.OnDismissListener {
    public void onDismiss() {
      ViewTreeObserver viewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
      if (viewTreeObserver != null)
        viewTreeObserver.removeGlobalOnLayoutListener(layoutListener); 
    }
  }
  
  static class SavedState extends View.BaseSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public AppCompatSpinner.SavedState createFromParcel(Parcel param2Parcel) {
          return new AppCompatSpinner.SavedState(param2Parcel);
        }
        
        public AppCompatSpinner.SavedState[] newArray(int param2Int) {
          return new AppCompatSpinner.SavedState[param2Int];
        }
      };
    
    boolean mShowDropdown;
    
    SavedState(Parcel param1Parcel) {
      super(param1Parcel);
      boolean bool;
      if (param1Parcel.readByte() != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      this.mShowDropdown = bool;
    }
    
    SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeByte((byte)this.mShowDropdown);
    }
  }
  
  static final class null implements Parcelable.Creator<SavedState> {
    public AppCompatSpinner.SavedState createFromParcel(Parcel param1Parcel) {
      return new AppCompatSpinner.SavedState(param1Parcel);
    }
    
    public AppCompatSpinner.SavedState[] newArray(int param1Int) {
      return new AppCompatSpinner.SavedState[param1Int];
    }
  }
  
  static interface SpinnerPopup {
    void dismiss();
    
    Drawable getBackground();
    
    CharSequence getHintText();
    
    int getHorizontalOffset();
    
    int getHorizontalOriginalOffset();
    
    int getVerticalOffset();
    
    boolean isShowing();
    
    void setAdapter(ListAdapter param1ListAdapter);
    
    void setBackgroundDrawable(Drawable param1Drawable);
    
    void setHorizontalOffset(int param1Int);
    
    void setHorizontalOriginalOffset(int param1Int);
    
    void setPromptText(CharSequence param1CharSequence);
    
    void setVerticalOffset(int param1Int);
    
    void show(int param1Int1, int param1Int2);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/appcompat/widget/AppCompatSpinner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
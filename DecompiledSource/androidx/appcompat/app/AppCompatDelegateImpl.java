package androidx.appcompat.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.view.SupportActionModeWrapper;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.WindowCallbackWrapper;
import androidx.appcompat.view.menu.ListMenuPresenter;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.ActionBarContextView;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.appcompat.widget.DecorContentParent;
import androidx.appcompat.widget.FitWindowsViewGroup;
import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.VectorEnabledTintResources;
import androidx.appcompat.widget.ViewUtils;
import androidx.collection.ArrayMap;
import androidx.core.app.NavUtils;
import androidx.core.view.KeyEventDispatcher;
import androidx.core.view.LayoutInflaterCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;

class AppCompatDelegateImpl extends AppCompatDelegate implements MenuBuilder.Callback, LayoutInflater.Factory2 {
  private static final boolean DEBUG = false;
  
  static final String EXCEPTION_HANDLER_MESSAGE_SUFFIX = ". If the resource you are trying to use is a vector resource, you may be referencing it in an unsupported way. See AppCompatDelegate.setCompatVectorFromResourcesEnabled() for more info.";
  
  private static final boolean IS_PRE_LOLLIPOP;
  
  private static final boolean sAlwaysOverrideConfiguration;
  
  private static boolean sInstalledExceptionHandler;
  
  private static final Map<Class<?>, Integer> sLocalNightModes = (Map<Class<?>, Integer>)new ArrayMap();
  
  private static final int[] sWindowBackgroundStyleable;
  
  ActionBar mActionBar;
  
  private ActionMenuPresenterCallback mActionMenuPresenterCallback;
  
  ActionMode mActionMode;
  
  PopupWindow mActionModePopup;
  
  ActionBarContextView mActionModeView;
  
  private boolean mActivityHandlesUiMode;
  
  private boolean mActivityHandlesUiModeChecked;
  
  final AppCompatCallback mAppCompatCallback;
  
  private AppCompatViewInflater mAppCompatViewInflater;
  
  private AppCompatWindowCallback mAppCompatWindowCallback;
  
  private AutoNightModeManager mAutoBatteryNightModeManager;
  
  private AutoNightModeManager mAutoTimeNightModeManager;
  
  private boolean mBaseContextAttached;
  
  private boolean mClosingActionMenu;
  
  final Context mContext;
  
  private boolean mCreated;
  
  private DecorContentParent mDecorContentParent;
  
  private boolean mEnableDefaultActionBarUp;
  
  ViewPropertyAnimatorCompat mFadeAnim = null;
  
  private boolean mFeatureIndeterminateProgress;
  
  private boolean mFeatureProgress;
  
  private boolean mHandleNativeActionModes = true;
  
  boolean mHasActionBar;
  
  final Object mHost;
  
  int mInvalidatePanelMenuFeatures;
  
  boolean mInvalidatePanelMenuPosted;
  
  private final Runnable mInvalidatePanelMenuRunnable = new Runnable() {
      public void run() {
        if ((AppCompatDelegateImpl.this.mInvalidatePanelMenuFeatures & 0x1) != 0)
          AppCompatDelegateImpl.this.doInvalidatePanelMenu(0); 
        if ((AppCompatDelegateImpl.this.mInvalidatePanelMenuFeatures & 0x1000) != 0)
          AppCompatDelegateImpl.this.doInvalidatePanelMenu(108); 
        AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
        appCompatDelegateImpl.mInvalidatePanelMenuPosted = false;
        appCompatDelegateImpl.mInvalidatePanelMenuFeatures = 0;
      }
    };
  
  boolean mIsDestroyed;
  
  boolean mIsFloating;
  
  private int mLocalNightMode = -100;
  
  private boolean mLongPressBackDown;
  
  MenuInflater mMenuInflater;
  
  boolean mOverlayActionBar;
  
  boolean mOverlayActionMode;
  
  private PanelMenuPresenterCallback mPanelMenuPresenterCallback;
  
  private PanelFeatureState[] mPanels;
  
  private PanelFeatureState mPreparedPanel;
  
  Runnable mShowActionModePopup;
  
  private boolean mStarted;
  
  private View mStatusGuard;
  
  private ViewGroup mSubDecor;
  
  private boolean mSubDecorInstalled;
  
  private Rect mTempRect1;
  
  private Rect mTempRect2;
  
  private int mThemeResId;
  
  private CharSequence mTitle;
  
  private TextView mTitleView;
  
  Window mWindow;
  
  boolean mWindowNoTitle;
  
  static {
    int i = Build.VERSION.SDK_INT;
    boolean bool1 = false;
    if (i < 21) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    IS_PRE_LOLLIPOP = bool2;
    sWindowBackgroundStyleable = new int[] { 16842836 };
    boolean bool2 = bool1;
    if (Build.VERSION.SDK_INT >= 21) {
      bool2 = bool1;
      if (Build.VERSION.SDK_INT <= 25)
        bool2 = true; 
    } 
    sAlwaysOverrideConfiguration = bool2;
    if (IS_PRE_LOLLIPOP && !sInstalledExceptionHandler) {
      Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler()) {
            private boolean shouldWrapException(Throwable param1Throwable) {
              boolean bool = param1Throwable instanceof Resources.NotFoundException;
              boolean bool1 = false;
              null = bool1;
              if (bool) {
                String str = param1Throwable.getMessage();
                null = bool1;
                if (str != null) {
                  if (!str.contains("drawable")) {
                    null = bool1;
                    return str.contains("Drawable") ? true : null;
                  } 
                } else {
                  return null;
                } 
              } else {
                return null;
              } 
              return true;
            }
            
            public void uncaughtException(Thread param1Thread, Throwable param1Throwable) {
              if (shouldWrapException(param1Throwable)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(param1Throwable.getMessage());
                stringBuilder.append(". If the resource you are trying to use is a vector resource, you may be referencing it in an unsupported way. See AppCompatDelegate.setCompatVectorFromResourcesEnabled() for more info.");
                Resources.NotFoundException notFoundException = new Resources.NotFoundException(stringBuilder.toString());
                notFoundException.initCause(param1Throwable.getCause());
                notFoundException.setStackTrace(param1Throwable.getStackTrace());
                defHandler.uncaughtException(param1Thread, (Throwable)notFoundException);
              } else {
                defHandler.uncaughtException(param1Thread, param1Throwable);
              } 
            }
          });
      sInstalledExceptionHandler = true;
    } 
  }
  
  AppCompatDelegateImpl(Activity paramActivity, AppCompatCallback paramAppCompatCallback) {
    this((Context)paramActivity, (Window)null, paramAppCompatCallback, paramActivity);
  }
  
  AppCompatDelegateImpl(Dialog paramDialog, AppCompatCallback paramAppCompatCallback) {
    this(paramDialog.getContext(), paramDialog.getWindow(), paramAppCompatCallback, paramDialog);
  }
  
  AppCompatDelegateImpl(Context paramContext, Activity paramActivity, AppCompatCallback paramAppCompatCallback) {
    this(paramContext, (Window)null, paramAppCompatCallback, paramActivity);
  }
  
  AppCompatDelegateImpl(Context paramContext, Window paramWindow, AppCompatCallback paramAppCompatCallback) {
    this(paramContext, paramWindow, paramAppCompatCallback, paramContext);
  }
  
  private AppCompatDelegateImpl(Context paramContext, Window paramWindow, AppCompatCallback paramAppCompatCallback, Object paramObject) {
    this.mContext = paramContext;
    this.mAppCompatCallback = paramAppCompatCallback;
    this.mHost = paramObject;
    if (this.mLocalNightMode == -100 && this.mHost instanceof Dialog) {
      AppCompatActivity appCompatActivity = tryUnwrapContext();
      if (appCompatActivity != null)
        this.mLocalNightMode = appCompatActivity.getDelegate().getLocalNightMode(); 
    } 
    if (this.mLocalNightMode == -100) {
      Integer integer = sLocalNightModes.get(this.mHost.getClass());
      if (integer != null) {
        this.mLocalNightMode = integer.intValue();
        sLocalNightModes.remove(this.mHost.getClass());
      } 
    } 
    if (paramWindow != null)
      attachToWindow(paramWindow); 
    AppCompatDrawableManager.preload();
  }
  
  private boolean applyDayNight(boolean paramBoolean) {
    if (this.mIsDestroyed)
      return false; 
    int i = calculateNightMode();
    paramBoolean = updateForNightMode(mapNightMode(i), paramBoolean);
    if (i == 0) {
      getAutoTimeNightModeManager().setup();
    } else {
      AutoNightModeManager autoNightModeManager = this.mAutoTimeNightModeManager;
      if (autoNightModeManager != null)
        autoNightModeManager.cleanup(); 
    } 
    if (i == 3) {
      getAutoBatteryNightModeManager().setup();
    } else {
      AutoNightModeManager autoNightModeManager = this.mAutoBatteryNightModeManager;
      if (autoNightModeManager != null)
        autoNightModeManager.cleanup(); 
    } 
    return paramBoolean;
  }
  
  private void applyFixedSizeWindow() {
    ContentFrameLayout contentFrameLayout = (ContentFrameLayout)this.mSubDecor.findViewById(16908290);
    View view = this.mWindow.getDecorView();
    contentFrameLayout.setDecorPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    TypedArray typedArray = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme);
    typedArray.getValue(R.styleable.AppCompatTheme_windowMinWidthMajor, contentFrameLayout.getMinWidthMajor());
    typedArray.getValue(R.styleable.AppCompatTheme_windowMinWidthMinor, contentFrameLayout.getMinWidthMinor());
    if (typedArray.hasValue(R.styleable.AppCompatTheme_windowFixedWidthMajor))
      typedArray.getValue(R.styleable.AppCompatTheme_windowFixedWidthMajor, contentFrameLayout.getFixedWidthMajor()); 
    if (typedArray.hasValue(R.styleable.AppCompatTheme_windowFixedWidthMinor))
      typedArray.getValue(R.styleable.AppCompatTheme_windowFixedWidthMinor, contentFrameLayout.getFixedWidthMinor()); 
    if (typedArray.hasValue(R.styleable.AppCompatTheme_windowFixedHeightMajor))
      typedArray.getValue(R.styleable.AppCompatTheme_windowFixedHeightMajor, contentFrameLayout.getFixedHeightMajor()); 
    if (typedArray.hasValue(R.styleable.AppCompatTheme_windowFixedHeightMinor))
      typedArray.getValue(R.styleable.AppCompatTheme_windowFixedHeightMinor, contentFrameLayout.getFixedHeightMinor()); 
    typedArray.recycle();
    contentFrameLayout.requestLayout();
  }
  
  private void attachToWindow(Window paramWindow) {
    if (this.mWindow == null) {
      Window.Callback callback = paramWindow.getCallback();
      if (!(callback instanceof AppCompatWindowCallback)) {
        this.mAppCompatWindowCallback = new AppCompatWindowCallback(callback);
        paramWindow.setCallback((Window.Callback)this.mAppCompatWindowCallback);
        TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(this.mContext, null, sWindowBackgroundStyleable);
        Drawable drawable = tintTypedArray.getDrawableIfKnown(0);
        if (drawable != null)
          paramWindow.setBackgroundDrawable(drawable); 
        tintTypedArray.recycle();
        this.mWindow = paramWindow;
        return;
      } 
      throw new IllegalStateException("AppCompat has already installed itself into the Window");
    } 
    throw new IllegalStateException("AppCompat has already installed itself into the Window");
  }
  
  private int calculateNightMode() {
    int i = this.mLocalNightMode;
    if (i == -100)
      i = getDefaultNightMode(); 
    return i;
  }
  
  private void cleanupAutoManagers() {
    AutoNightModeManager autoNightModeManager = this.mAutoTimeNightModeManager;
    if (autoNightModeManager != null)
      autoNightModeManager.cleanup(); 
    autoNightModeManager = this.mAutoBatteryNightModeManager;
    if (autoNightModeManager != null)
      autoNightModeManager.cleanup(); 
  }
  
  private ViewGroup createSubDecor() {
    StringBuilder stringBuilder;
    TypedArray typedArray = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme);
    if (typedArray.hasValue(R.styleable.AppCompatTheme_windowActionBar)) {
      ViewGroup viewGroup;
      if (typedArray.getBoolean(R.styleable.AppCompatTheme_windowNoTitle, false)) {
        requestWindowFeature(1);
      } else if (typedArray.getBoolean(R.styleable.AppCompatTheme_windowActionBar, false)) {
        requestWindowFeature(108);
      } 
      if (typedArray.getBoolean(R.styleable.AppCompatTheme_windowActionBarOverlay, false))
        requestWindowFeature(109); 
      if (typedArray.getBoolean(R.styleable.AppCompatTheme_windowActionModeOverlay, false))
        requestWindowFeature(10); 
      this.mIsFloating = typedArray.getBoolean(R.styleable.AppCompatTheme_android_windowIsFloating, false);
      typedArray.recycle();
      ensureWindow();
      this.mWindow.getDecorView();
      LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
      if (!this.mWindowNoTitle) {
        if (this.mIsFloating) {
          viewGroup = (ViewGroup)layoutInflater.inflate(R.layout.abc_dialog_title_material, null);
          this.mOverlayActionBar = false;
          this.mHasActionBar = false;
        } else if (this.mHasActionBar) {
          Context context;
          TypedValue typedValue = new TypedValue();
          this.mContext.getTheme().resolveAttribute(R.attr.actionBarTheme, typedValue, true);
          if (typedValue.resourceId != 0) {
            ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(this.mContext, typedValue.resourceId);
          } else {
            context = this.mContext;
          } 
          ViewGroup viewGroup1 = (ViewGroup)LayoutInflater.from(context).inflate(R.layout.abc_screen_toolbar, null);
          this.mDecorContentParent = (DecorContentParent)viewGroup1.findViewById(R.id.decor_content_parent);
          this.mDecorContentParent.setWindowCallback(getWindowCallback());
          if (this.mOverlayActionBar)
            this.mDecorContentParent.initFeature(109); 
          if (this.mFeatureProgress)
            this.mDecorContentParent.initFeature(2); 
          viewGroup = viewGroup1;
          if (this.mFeatureIndeterminateProgress) {
            this.mDecorContentParent.initFeature(5);
            viewGroup = viewGroup1;
          } 
        } else {
          layoutInflater = null;
        } 
      } else {
        if (this.mOverlayActionMode) {
          viewGroup = (ViewGroup)layoutInflater.inflate(R.layout.abc_screen_simple_overlay_action_mode, null);
        } else {
          viewGroup = (ViewGroup)viewGroup.inflate(R.layout.abc_screen_simple, null);
        } 
        if (Build.VERSION.SDK_INT >= 21) {
          ViewCompat.setOnApplyWindowInsetsListener((View)viewGroup, new OnApplyWindowInsetsListener() {
                public WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat) {
                  int i = param1WindowInsetsCompat.getSystemWindowInsetTop();
                  int j = AppCompatDelegateImpl.this.updateStatusGuard(i);
                  WindowInsetsCompat windowInsetsCompat = param1WindowInsetsCompat;
                  if (i != j)
                    windowInsetsCompat = param1WindowInsetsCompat.replaceSystemWindowInsets(param1WindowInsetsCompat.getSystemWindowInsetLeft(), j, param1WindowInsetsCompat.getSystemWindowInsetRight(), param1WindowInsetsCompat.getSystemWindowInsetBottom()); 
                  return ViewCompat.onApplyWindowInsets(param1View, windowInsetsCompat);
                }
              });
        } else {
          ((FitWindowsViewGroup)viewGroup).setOnFitSystemWindowsListener(new FitWindowsViewGroup.OnFitSystemWindowsListener() {
                public void onFitSystemWindows(Rect param1Rect) {
                  param1Rect.top = AppCompatDelegateImpl.this.updateStatusGuard(param1Rect.top);
                }
              });
        } 
      } 
      if (viewGroup != null) {
        if (this.mDecorContentParent == null)
          this.mTitleView = (TextView)viewGroup.findViewById(R.id.title); 
        ViewUtils.makeOptionalFitsSystemWindows((View)viewGroup);
        ContentFrameLayout contentFrameLayout = (ContentFrameLayout)viewGroup.findViewById(R.id.action_bar_activity_content);
        ViewGroup viewGroup1 = (ViewGroup)this.mWindow.findViewById(16908290);
        if (viewGroup1 != null) {
          while (viewGroup1.getChildCount() > 0) {
            View view = viewGroup1.getChildAt(0);
            viewGroup1.removeViewAt(0);
            contentFrameLayout.addView(view);
          } 
          viewGroup1.setId(-1);
          contentFrameLayout.setId(16908290);
          if (viewGroup1 instanceof FrameLayout)
            ((FrameLayout)viewGroup1).setForeground(null); 
        } 
        this.mWindow.setContentView((View)viewGroup);
        contentFrameLayout.setAttachListener(new ContentFrameLayout.OnAttachListener() {
              public void onAttachedFromWindow() {}
              
              public void onDetachedFromWindow() {
                AppCompatDelegateImpl.this.dismissPopups();
              }
            });
        return viewGroup;
      } 
      stringBuilder = new StringBuilder();
      stringBuilder.append("AppCompat does not support the current theme features: { windowActionBar: ");
      stringBuilder.append(this.mHasActionBar);
      stringBuilder.append(", windowActionBarOverlay: ");
      stringBuilder.append(this.mOverlayActionBar);
      stringBuilder.append(", android:windowIsFloating: ");
      stringBuilder.append(this.mIsFloating);
      stringBuilder.append(", windowActionModeOverlay: ");
      stringBuilder.append(this.mOverlayActionMode);
      stringBuilder.append(", windowNoTitle: ");
      stringBuilder.append(this.mWindowNoTitle);
      stringBuilder.append(" }");
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    stringBuilder.recycle();
    IllegalStateException illegalStateException = new IllegalStateException("You need to use a Theme.AppCompat theme (or descendant) with this activity.");
    throw illegalStateException;
  }
  
  private void ensureSubDecor() {
    if (!this.mSubDecorInstalled) {
      this.mSubDecor = createSubDecor();
      CharSequence charSequence = getTitle();
      if (!TextUtils.isEmpty(charSequence)) {
        DecorContentParent decorContentParent = this.mDecorContentParent;
        if (decorContentParent != null) {
          decorContentParent.setWindowTitle(charSequence);
        } else if (peekSupportActionBar() != null) {
          peekSupportActionBar().setWindowTitle(charSequence);
        } else {
          TextView textView = this.mTitleView;
          if (textView != null)
            textView.setText(charSequence); 
        } 
      } 
      applyFixedSizeWindow();
      onSubDecorInstalled(this.mSubDecor);
      this.mSubDecorInstalled = true;
      PanelFeatureState panelFeatureState = getPanelState(0, false);
      if (!this.mIsDestroyed && (panelFeatureState == null || panelFeatureState.menu == null))
        invalidatePanelMenu(108); 
    } 
  }
  
  private void ensureWindow() {
    if (this.mWindow == null) {
      Object object = this.mHost;
      if (object instanceof Activity)
        attachToWindow(((Activity)object).getWindow()); 
    } 
    if (this.mWindow != null)
      return; 
    throw new IllegalStateException("We have not been given a Window");
  }
  
  private AutoNightModeManager getAutoBatteryNightModeManager() {
    if (this.mAutoBatteryNightModeManager == null)
      this.mAutoBatteryNightModeManager = new AutoBatteryNightModeManager(this.mContext); 
    return this.mAutoBatteryNightModeManager;
  }
  
  private void initWindowDecorActionBar() {
    ensureSubDecor();
    if (this.mHasActionBar && this.mActionBar == null) {
      Object object = this.mHost;
      if (object instanceof Activity) {
        this.mActionBar = new WindowDecorActionBar((Activity)object, this.mOverlayActionBar);
      } else if (object instanceof Dialog) {
        this.mActionBar = new WindowDecorActionBar((Dialog)object);
      } 
      object = this.mActionBar;
      if (object != null)
        object.setDefaultDisplayHomeAsUpEnabled(this.mEnableDefaultActionBarUp); 
    } 
  }
  
  private boolean initializePanelContent(PanelFeatureState paramPanelFeatureState) {
    View view = paramPanelFeatureState.createdPanelView;
    boolean bool = true;
    if (view != null) {
      paramPanelFeatureState.shownPanelView = paramPanelFeatureState.createdPanelView;
      return true;
    } 
    if (paramPanelFeatureState.menu == null)
      return false; 
    if (this.mPanelMenuPresenterCallback == null)
      this.mPanelMenuPresenterCallback = new PanelMenuPresenterCallback(); 
    paramPanelFeatureState.shownPanelView = (View)paramPanelFeatureState.getListMenuView(this.mPanelMenuPresenterCallback);
    if (paramPanelFeatureState.shownPanelView == null)
      bool = false; 
    return bool;
  }
  
  private boolean initializePanelDecor(PanelFeatureState paramPanelFeatureState) {
    paramPanelFeatureState.setStyle(getActionBarThemedContext());
    paramPanelFeatureState.decorView = (ViewGroup)new ListMenuDecorView(paramPanelFeatureState.listPresenterContext);
    paramPanelFeatureState.gravity = 81;
    return true;
  }
  
  private boolean initializePanelMenu(PanelFeatureState paramPanelFeatureState) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mContext : Landroid/content/Context;
    //   4: astore_2
    //   5: aload_1
    //   6: getfield featureId : I
    //   9: ifeq -> 23
    //   12: aload_2
    //   13: astore_3
    //   14: aload_1
    //   15: getfield featureId : I
    //   18: bipush #108
    //   20: if_icmpne -> 190
    //   23: aload_2
    //   24: astore_3
    //   25: aload_0
    //   26: getfield mDecorContentParent : Landroidx/appcompat/widget/DecorContentParent;
    //   29: ifnull -> 190
    //   32: new android/util/TypedValue
    //   35: dup
    //   36: invokespecial <init> : ()V
    //   39: astore #4
    //   41: aload_2
    //   42: invokevirtual getTheme : ()Landroid/content/res/Resources$Theme;
    //   45: astore #5
    //   47: aload #5
    //   49: getstatic androidx/appcompat/R$attr.actionBarTheme : I
    //   52: aload #4
    //   54: iconst_1
    //   55: invokevirtual resolveAttribute : (ILandroid/util/TypedValue;Z)Z
    //   58: pop
    //   59: aconst_null
    //   60: astore_3
    //   61: aload #4
    //   63: getfield resourceId : I
    //   66: ifeq -> 107
    //   69: aload_2
    //   70: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   73: invokevirtual newTheme : ()Landroid/content/res/Resources$Theme;
    //   76: astore_3
    //   77: aload_3
    //   78: aload #5
    //   80: invokevirtual setTo : (Landroid/content/res/Resources$Theme;)V
    //   83: aload_3
    //   84: aload #4
    //   86: getfield resourceId : I
    //   89: iconst_1
    //   90: invokevirtual applyStyle : (IZ)V
    //   93: aload_3
    //   94: getstatic androidx/appcompat/R$attr.actionBarWidgetTheme : I
    //   97: aload #4
    //   99: iconst_1
    //   100: invokevirtual resolveAttribute : (ILandroid/util/TypedValue;Z)Z
    //   103: pop
    //   104: goto -> 119
    //   107: aload #5
    //   109: getstatic androidx/appcompat/R$attr.actionBarWidgetTheme : I
    //   112: aload #4
    //   114: iconst_1
    //   115: invokevirtual resolveAttribute : (ILandroid/util/TypedValue;Z)Z
    //   118: pop
    //   119: aload_3
    //   120: astore #6
    //   122: aload #4
    //   124: getfield resourceId : I
    //   127: ifeq -> 164
    //   130: aload_3
    //   131: astore #6
    //   133: aload_3
    //   134: ifnonnull -> 153
    //   137: aload_2
    //   138: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   141: invokevirtual newTheme : ()Landroid/content/res/Resources$Theme;
    //   144: astore #6
    //   146: aload #6
    //   148: aload #5
    //   150: invokevirtual setTo : (Landroid/content/res/Resources$Theme;)V
    //   153: aload #6
    //   155: aload #4
    //   157: getfield resourceId : I
    //   160: iconst_1
    //   161: invokevirtual applyStyle : (IZ)V
    //   164: aload_2
    //   165: astore_3
    //   166: aload #6
    //   168: ifnull -> 190
    //   171: new androidx/appcompat/view/ContextThemeWrapper
    //   174: dup
    //   175: aload_2
    //   176: iconst_0
    //   177: invokespecial <init> : (Landroid/content/Context;I)V
    //   180: astore_3
    //   181: aload_3
    //   182: invokevirtual getTheme : ()Landroid/content/res/Resources$Theme;
    //   185: aload #6
    //   187: invokevirtual setTo : (Landroid/content/res/Resources$Theme;)V
    //   190: new androidx/appcompat/view/menu/MenuBuilder
    //   193: dup
    //   194: aload_3
    //   195: invokespecial <init> : (Landroid/content/Context;)V
    //   198: astore_3
    //   199: aload_3
    //   200: aload_0
    //   201: invokevirtual setCallback : (Landroidx/appcompat/view/menu/MenuBuilder$Callback;)V
    //   204: aload_1
    //   205: aload_3
    //   206: invokevirtual setMenu : (Landroidx/appcompat/view/menu/MenuBuilder;)V
    //   209: iconst_1
    //   210: ireturn
  }
  
  private void invalidatePanelMenu(int paramInt) {
    this.mInvalidatePanelMenuFeatures = 1 << paramInt | this.mInvalidatePanelMenuFeatures;
    if (!this.mInvalidatePanelMenuPosted) {
      ViewCompat.postOnAnimation(this.mWindow.getDecorView(), this.mInvalidatePanelMenuRunnable);
      this.mInvalidatePanelMenuPosted = true;
    } 
  }
  
  private boolean isActivityManifestHandlingUiMode() {
    if (!this.mActivityHandlesUiModeChecked && this.mHost instanceof Activity) {
      PackageManager packageManager = this.mContext.getPackageManager();
      if (packageManager == null)
        return false; 
      try {
        boolean bool;
        ComponentName componentName = new ComponentName();
        this(this.mContext, this.mHost.getClass());
        ActivityInfo activityInfo = packageManager.getActivityInfo(componentName, 0);
        if (activityInfo != null && (activityInfo.configChanges & 0x200) != 0) {
          bool = true;
        } else {
          bool = false;
        } 
        this.mActivityHandlesUiMode = bool;
      } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
        Log.d("AppCompatDelegate", "Exception while getting ActivityInfo", (Throwable)nameNotFoundException);
        this.mActivityHandlesUiMode = false;
      } 
    } 
    this.mActivityHandlesUiModeChecked = true;
    return this.mActivityHandlesUiMode;
  }
  
  private boolean onKeyDownPanel(int paramInt, KeyEvent paramKeyEvent) {
    if (paramKeyEvent.getRepeatCount() == 0) {
      PanelFeatureState panelFeatureState = getPanelState(paramInt, true);
      if (!panelFeatureState.isOpen)
        return preparePanel(panelFeatureState, paramKeyEvent); 
    } 
    return false;
  }
  
  private boolean onKeyUpPanel(int paramInt, KeyEvent paramKeyEvent) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mActionMode : Landroidx/appcompat/view/ActionMode;
    //   4: ifnull -> 9
    //   7: iconst_0
    //   8: ireturn
    //   9: aload_0
    //   10: iload_1
    //   11: iconst_1
    //   12: invokevirtual getPanelState : (IZ)Landroidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState;
    //   15: astore_3
    //   16: iload_1
    //   17: ifne -> 110
    //   20: aload_0
    //   21: getfield mDecorContentParent : Landroidx/appcompat/widget/DecorContentParent;
    //   24: astore #4
    //   26: aload #4
    //   28: ifnull -> 110
    //   31: aload #4
    //   33: invokeinterface canShowOverflowMenu : ()Z
    //   38: ifeq -> 110
    //   41: aload_0
    //   42: getfield mContext : Landroid/content/Context;
    //   45: invokestatic get : (Landroid/content/Context;)Landroid/view/ViewConfiguration;
    //   48: invokevirtual hasPermanentMenuKey : ()Z
    //   51: ifne -> 110
    //   54: aload_0
    //   55: getfield mDecorContentParent : Landroidx/appcompat/widget/DecorContentParent;
    //   58: invokeinterface isOverflowMenuShowing : ()Z
    //   63: ifne -> 96
    //   66: aload_0
    //   67: getfield mIsDestroyed : Z
    //   70: ifne -> 177
    //   73: aload_0
    //   74: aload_3
    //   75: aload_2
    //   76: invokespecial preparePanel : (Landroidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState;Landroid/view/KeyEvent;)Z
    //   79: ifeq -> 177
    //   82: aload_0
    //   83: getfield mDecorContentParent : Landroidx/appcompat/widget/DecorContentParent;
    //   86: invokeinterface showOverflowMenu : ()Z
    //   91: istore #5
    //   93: goto -> 195
    //   96: aload_0
    //   97: getfield mDecorContentParent : Landroidx/appcompat/widget/DecorContentParent;
    //   100: invokeinterface hideOverflowMenu : ()Z
    //   105: istore #5
    //   107: goto -> 195
    //   110: aload_3
    //   111: getfield isOpen : Z
    //   114: ifne -> 183
    //   117: aload_3
    //   118: getfield isHandled : Z
    //   121: ifeq -> 127
    //   124: goto -> 183
    //   127: aload_3
    //   128: getfield isPrepared : Z
    //   131: ifeq -> 177
    //   134: aload_3
    //   135: getfield refreshMenuContent : Z
    //   138: ifeq -> 157
    //   141: aload_3
    //   142: iconst_0
    //   143: putfield isPrepared : Z
    //   146: aload_0
    //   147: aload_3
    //   148: aload_2
    //   149: invokespecial preparePanel : (Landroidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState;Landroid/view/KeyEvent;)Z
    //   152: istore #5
    //   154: goto -> 160
    //   157: iconst_1
    //   158: istore #5
    //   160: iload #5
    //   162: ifeq -> 177
    //   165: aload_0
    //   166: aload_3
    //   167: aload_2
    //   168: invokespecial openPanel : (Landroidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState;Landroid/view/KeyEvent;)V
    //   171: iconst_1
    //   172: istore #5
    //   174: goto -> 195
    //   177: iconst_0
    //   178: istore #5
    //   180: goto -> 195
    //   183: aload_3
    //   184: getfield isOpen : Z
    //   187: istore #5
    //   189: aload_0
    //   190: aload_3
    //   191: iconst_1
    //   192: invokevirtual closePanel : (Landroidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState;Z)V
    //   195: iload #5
    //   197: ifeq -> 236
    //   200: aload_0
    //   201: getfield mContext : Landroid/content/Context;
    //   204: ldc_w 'audio'
    //   207: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
    //   210: checkcast android/media/AudioManager
    //   213: astore_2
    //   214: aload_2
    //   215: ifnull -> 226
    //   218: aload_2
    //   219: iconst_0
    //   220: invokevirtual playSoundEffect : (I)V
    //   223: goto -> 236
    //   226: ldc_w 'AppCompatDelegate'
    //   229: ldc_w 'Couldn't get audio manager'
    //   232: invokestatic w : (Ljava/lang/String;Ljava/lang/String;)I
    //   235: pop
    //   236: iload #5
    //   238: ireturn
  }
  
  private void openPanel(PanelFeatureState paramPanelFeatureState, KeyEvent paramKeyEvent) {
    // Byte code:
    //   0: aload_1
    //   1: getfield isOpen : Z
    //   4: ifne -> 406
    //   7: aload_0
    //   8: getfield mIsDestroyed : Z
    //   11: ifeq -> 17
    //   14: goto -> 406
    //   17: aload_1
    //   18: getfield featureId : I
    //   21: ifne -> 56
    //   24: aload_0
    //   25: getfield mContext : Landroid/content/Context;
    //   28: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   31: invokevirtual getConfiguration : ()Landroid/content/res/Configuration;
    //   34: getfield screenLayout : I
    //   37: bipush #15
    //   39: iand
    //   40: iconst_4
    //   41: if_icmpne -> 49
    //   44: iconst_1
    //   45: istore_3
    //   46: goto -> 51
    //   49: iconst_0
    //   50: istore_3
    //   51: iload_3
    //   52: ifeq -> 56
    //   55: return
    //   56: aload_0
    //   57: invokevirtual getWindowCallback : ()Landroid/view/Window$Callback;
    //   60: astore #4
    //   62: aload #4
    //   64: ifnull -> 92
    //   67: aload #4
    //   69: aload_1
    //   70: getfield featureId : I
    //   73: aload_1
    //   74: getfield menu : Landroidx/appcompat/view/menu/MenuBuilder;
    //   77: invokeinterface onMenuOpened : (ILandroid/view/Menu;)Z
    //   82: ifne -> 92
    //   85: aload_0
    //   86: aload_1
    //   87: iconst_1
    //   88: invokevirtual closePanel : (Landroidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState;Z)V
    //   91: return
    //   92: aload_0
    //   93: getfield mContext : Landroid/content/Context;
    //   96: ldc_w 'window'
    //   99: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
    //   102: checkcast android/view/WindowManager
    //   105: astore #5
    //   107: aload #5
    //   109: ifnonnull -> 113
    //   112: return
    //   113: aload_0
    //   114: aload_1
    //   115: aload_2
    //   116: invokespecial preparePanel : (Landroidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState;Landroid/view/KeyEvent;)Z
    //   119: ifne -> 123
    //   122: return
    //   123: aload_1
    //   124: getfield decorView : Landroid/view/ViewGroup;
    //   127: ifnull -> 172
    //   130: aload_1
    //   131: getfield refreshDecorView : Z
    //   134: ifeq -> 140
    //   137: goto -> 172
    //   140: aload_1
    //   141: getfield createdPanelView : Landroid/view/View;
    //   144: ifnull -> 338
    //   147: aload_1
    //   148: getfield createdPanelView : Landroid/view/View;
    //   151: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   154: astore_2
    //   155: aload_2
    //   156: ifnull -> 338
    //   159: aload_2
    //   160: getfield width : I
    //   163: iconst_m1
    //   164: if_icmpne -> 338
    //   167: iconst_m1
    //   168: istore_3
    //   169: goto -> 341
    //   172: aload_1
    //   173: getfield decorView : Landroid/view/ViewGroup;
    //   176: ifnonnull -> 195
    //   179: aload_0
    //   180: aload_1
    //   181: invokespecial initializePanelDecor : (Landroidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState;)Z
    //   184: ifeq -> 194
    //   187: aload_1
    //   188: getfield decorView : Landroid/view/ViewGroup;
    //   191: ifnonnull -> 219
    //   194: return
    //   195: aload_1
    //   196: getfield refreshDecorView : Z
    //   199: ifeq -> 219
    //   202: aload_1
    //   203: getfield decorView : Landroid/view/ViewGroup;
    //   206: invokevirtual getChildCount : ()I
    //   209: ifle -> 219
    //   212: aload_1
    //   213: getfield decorView : Landroid/view/ViewGroup;
    //   216: invokevirtual removeAllViews : ()V
    //   219: aload_0
    //   220: aload_1
    //   221: invokespecial initializePanelContent : (Landroidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState;)Z
    //   224: ifeq -> 406
    //   227: aload_1
    //   228: invokevirtual hasPanelItems : ()Z
    //   231: ifne -> 237
    //   234: goto -> 406
    //   237: aload_1
    //   238: getfield shownPanelView : Landroid/view/View;
    //   241: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   244: astore #4
    //   246: aload #4
    //   248: astore_2
    //   249: aload #4
    //   251: ifnonnull -> 266
    //   254: new android/view/ViewGroup$LayoutParams
    //   257: dup
    //   258: bipush #-2
    //   260: bipush #-2
    //   262: invokespecial <init> : (II)V
    //   265: astore_2
    //   266: aload_1
    //   267: getfield background : I
    //   270: istore_3
    //   271: aload_1
    //   272: getfield decorView : Landroid/view/ViewGroup;
    //   275: iload_3
    //   276: invokevirtual setBackgroundResource : (I)V
    //   279: aload_1
    //   280: getfield shownPanelView : Landroid/view/View;
    //   283: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   286: astore #4
    //   288: aload #4
    //   290: instanceof android/view/ViewGroup
    //   293: ifeq -> 308
    //   296: aload #4
    //   298: checkcast android/view/ViewGroup
    //   301: aload_1
    //   302: getfield shownPanelView : Landroid/view/View;
    //   305: invokevirtual removeView : (Landroid/view/View;)V
    //   308: aload_1
    //   309: getfield decorView : Landroid/view/ViewGroup;
    //   312: aload_1
    //   313: getfield shownPanelView : Landroid/view/View;
    //   316: aload_2
    //   317: invokevirtual addView : (Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V
    //   320: aload_1
    //   321: getfield shownPanelView : Landroid/view/View;
    //   324: invokevirtual hasFocus : ()Z
    //   327: ifne -> 338
    //   330: aload_1
    //   331: getfield shownPanelView : Landroid/view/View;
    //   334: invokevirtual requestFocus : ()Z
    //   337: pop
    //   338: bipush #-2
    //   340: istore_3
    //   341: aload_1
    //   342: iconst_0
    //   343: putfield isHandled : Z
    //   346: new android/view/WindowManager$LayoutParams
    //   349: dup
    //   350: iload_3
    //   351: bipush #-2
    //   353: aload_1
    //   354: getfield x : I
    //   357: aload_1
    //   358: getfield y : I
    //   361: sipush #1002
    //   364: ldc_w 8519680
    //   367: bipush #-3
    //   369: invokespecial <init> : (IIIIIII)V
    //   372: astore_2
    //   373: aload_2
    //   374: aload_1
    //   375: getfield gravity : I
    //   378: putfield gravity : I
    //   381: aload_2
    //   382: aload_1
    //   383: getfield windowAnimations : I
    //   386: putfield windowAnimations : I
    //   389: aload #5
    //   391: aload_1
    //   392: getfield decorView : Landroid/view/ViewGroup;
    //   395: aload_2
    //   396: invokeinterface addView : (Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V
    //   401: aload_1
    //   402: iconst_1
    //   403: putfield isOpen : Z
    //   406: return
  }
  
  private boolean performPanelShortcut(PanelFeatureState paramPanelFeatureState, int paramInt1, KeyEvent paramKeyEvent, int paramInt2) {
    // Byte code:
    //   0: aload_3
    //   1: invokevirtual isSystem : ()Z
    //   4: istore #5
    //   6: iconst_0
    //   7: istore #6
    //   9: iload #5
    //   11: ifeq -> 16
    //   14: iconst_0
    //   15: ireturn
    //   16: aload_1
    //   17: getfield isPrepared : Z
    //   20: ifne -> 36
    //   23: iload #6
    //   25: istore #5
    //   27: aload_0
    //   28: aload_1
    //   29: aload_3
    //   30: invokespecial preparePanel : (Landroidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState;Landroid/view/KeyEvent;)Z
    //   33: ifeq -> 60
    //   36: iload #6
    //   38: istore #5
    //   40: aload_1
    //   41: getfield menu : Landroidx/appcompat/view/menu/MenuBuilder;
    //   44: ifnull -> 60
    //   47: aload_1
    //   48: getfield menu : Landroidx/appcompat/view/menu/MenuBuilder;
    //   51: iload_2
    //   52: aload_3
    //   53: iload #4
    //   55: invokevirtual performShortcut : (ILandroid/view/KeyEvent;I)Z
    //   58: istore #5
    //   60: iload #5
    //   62: ifeq -> 85
    //   65: iload #4
    //   67: iconst_1
    //   68: iand
    //   69: ifne -> 85
    //   72: aload_0
    //   73: getfield mDecorContentParent : Landroidx/appcompat/widget/DecorContentParent;
    //   76: ifnonnull -> 85
    //   79: aload_0
    //   80: aload_1
    //   81: iconst_1
    //   82: invokevirtual closePanel : (Landroidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState;Z)V
    //   85: iload #5
    //   87: ireturn
  }
  
  private boolean preparePanel(PanelFeatureState paramPanelFeatureState, KeyEvent paramKeyEvent) {
    DecorContentParent decorContentParent;
    int i;
    if (this.mIsDestroyed)
      return false; 
    if (paramPanelFeatureState.isPrepared)
      return true; 
    PanelFeatureState panelFeatureState = this.mPreparedPanel;
    if (panelFeatureState != null && panelFeatureState != paramPanelFeatureState)
      closePanel(panelFeatureState, false); 
    Window.Callback callback = getWindowCallback();
    if (callback != null)
      paramPanelFeatureState.createdPanelView = callback.onCreatePanelView(paramPanelFeatureState.featureId); 
    if (paramPanelFeatureState.featureId == 0 || paramPanelFeatureState.featureId == 108) {
      i = 1;
    } else {
      i = 0;
    } 
    if (i) {
      DecorContentParent decorContentParent1 = this.mDecorContentParent;
      if (decorContentParent1 != null)
        decorContentParent1.setMenuPrepared(); 
    } 
    if (paramPanelFeatureState.createdPanelView == null && (!i || !(peekSupportActionBar() instanceof ToolbarActionBar))) {
      DecorContentParent decorContentParent1;
      boolean bool;
      if (paramPanelFeatureState.menu == null || paramPanelFeatureState.refreshMenuContent) {
        if (paramPanelFeatureState.menu == null && (!initializePanelMenu(paramPanelFeatureState) || paramPanelFeatureState.menu == null))
          return false; 
        if (i && this.mDecorContentParent != null) {
          if (this.mActionMenuPresenterCallback == null)
            this.mActionMenuPresenterCallback = new ActionMenuPresenterCallback(); 
          this.mDecorContentParent.setMenu((Menu)paramPanelFeatureState.menu, this.mActionMenuPresenterCallback);
        } 
        paramPanelFeatureState.menu.stopDispatchingItemsChanged();
        if (!callback.onCreatePanelMenu(paramPanelFeatureState.featureId, (Menu)paramPanelFeatureState.menu)) {
          paramPanelFeatureState.setMenu(null);
          if (i) {
            decorContentParent = this.mDecorContentParent;
            if (decorContentParent != null)
              decorContentParent.setMenu(null, this.mActionMenuPresenterCallback); 
          } 
          return false;
        } 
        ((PanelFeatureState)decorContentParent).refreshMenuContent = false;
      } 
      ((PanelFeatureState)decorContentParent).menu.stopDispatchingItemsChanged();
      if (((PanelFeatureState)decorContentParent).frozenActionViewState != null) {
        ((PanelFeatureState)decorContentParent).menu.restoreActionViewStates(((PanelFeatureState)decorContentParent).frozenActionViewState);
        ((PanelFeatureState)decorContentParent).frozenActionViewState = null;
      } 
      if (!callback.onPreparePanel(0, ((PanelFeatureState)decorContentParent).createdPanelView, (Menu)((PanelFeatureState)decorContentParent).menu)) {
        if (i) {
          decorContentParent1 = this.mDecorContentParent;
          if (decorContentParent1 != null)
            decorContentParent1.setMenu(null, this.mActionMenuPresenterCallback); 
        } 
        ((PanelFeatureState)decorContentParent).menu.startDispatchingItemsChanged();
        return false;
      } 
      if (decorContentParent1 != null) {
        i = decorContentParent1.getDeviceId();
      } else {
        i = -1;
      } 
      if (KeyCharacterMap.load(i).getKeyboardType() != 1) {
        bool = true;
      } else {
        bool = false;
      } 
      ((PanelFeatureState)decorContentParent).qwertyMode = bool;
      ((PanelFeatureState)decorContentParent).menu.setQwertyMode(((PanelFeatureState)decorContentParent).qwertyMode);
      ((PanelFeatureState)decorContentParent).menu.startDispatchingItemsChanged();
    } 
    ((PanelFeatureState)decorContentParent).isPrepared = true;
    ((PanelFeatureState)decorContentParent).isHandled = false;
    this.mPreparedPanel = (PanelFeatureState)decorContentParent;
    return true;
  }
  
  private void reopenMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean) {
    DecorContentParent decorContentParent = this.mDecorContentParent;
    if (decorContentParent != null && decorContentParent.canShowOverflowMenu() && (!ViewConfiguration.get(this.mContext).hasPermanentMenuKey() || this.mDecorContentParent.isOverflowMenuShowPending())) {
      Window.Callback callback = getWindowCallback();
      if (!this.mDecorContentParent.isOverflowMenuShowing() || !paramBoolean) {
        if (callback != null && !this.mIsDestroyed) {
          if (this.mInvalidatePanelMenuPosted && (this.mInvalidatePanelMenuFeatures & 0x1) != 0) {
            this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
            this.mInvalidatePanelMenuRunnable.run();
          } 
          PanelFeatureState panelFeatureState1 = getPanelState(0, true);
          if (panelFeatureState1.menu != null && !panelFeatureState1.refreshMenuContent && callback.onPreparePanel(0, panelFeatureState1.createdPanelView, (Menu)panelFeatureState1.menu)) {
            callback.onMenuOpened(108, (Menu)panelFeatureState1.menu);
            this.mDecorContentParent.showOverflowMenu();
          } 
        } 
        return;
      } 
      this.mDecorContentParent.hideOverflowMenu();
      if (!this.mIsDestroyed)
        callback.onPanelClosed(108, (Menu)(getPanelState(0, true)).menu); 
      return;
    } 
    PanelFeatureState panelFeatureState = getPanelState(0, true);
    panelFeatureState.refreshDecorView = true;
    closePanel(panelFeatureState, false);
    openPanel(panelFeatureState, (KeyEvent)null);
  }
  
  private int sanitizeWindowFeatureId(int paramInt) {
    if (paramInt == 8) {
      Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR id when requesting this feature.");
      return 108;
    } 
    int i = paramInt;
    if (paramInt == 9) {
      Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY id when requesting this feature.");
      i = 109;
    } 
    return i;
  }
  
  private boolean shouldInheritContext(ViewParent paramViewParent) {
    if (paramViewParent == null)
      return false; 
    View view = this.mWindow.getDecorView();
    while (true) {
      if (paramViewParent == null)
        return true; 
      if (paramViewParent == view || !(paramViewParent instanceof View) || ViewCompat.isAttachedToWindow((View)paramViewParent))
        break; 
      paramViewParent = paramViewParent.getParent();
    } 
    return false;
  }
  
  private void throwFeatureRequestIfSubDecorInstalled() {
    if (!this.mSubDecorInstalled)
      return; 
    throw new AndroidRuntimeException("Window feature must be requested before adding content");
  }
  
  private AppCompatActivity tryUnwrapContext() {
    Context context = this.mContext;
    while (context != null) {
      if (context instanceof AppCompatActivity)
        return (AppCompatActivity)context; 
      if (context instanceof ContextWrapper)
        context = ((ContextWrapper)context).getBaseContext(); 
    } 
    return null;
  }
  
  private boolean updateForNightMode(int paramInt, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mContext : Landroid/content/Context;
    //   4: invokevirtual getApplicationContext : ()Landroid/content/Context;
    //   7: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   10: invokevirtual getConfiguration : ()Landroid/content/res/Configuration;
    //   13: getfield uiMode : I
    //   16: bipush #48
    //   18: iand
    //   19: istore_3
    //   20: iconst_1
    //   21: istore #4
    //   23: iload_1
    //   24: iconst_1
    //   25: if_icmpeq -> 46
    //   28: iload_1
    //   29: iconst_2
    //   30: if_icmpeq -> 39
    //   33: iload_3
    //   34: istore #5
    //   36: goto -> 50
    //   39: bipush #32
    //   41: istore #5
    //   43: goto -> 50
    //   46: bipush #16
    //   48: istore #5
    //   50: aload_0
    //   51: invokespecial isActivityManifestHandlingUiMode : ()Z
    //   54: istore #6
    //   56: getstatic androidx/appcompat/app/AppCompatDelegateImpl.sAlwaysOverrideConfiguration : Z
    //   59: istore #7
    //   61: iconst_0
    //   62: istore #8
    //   64: iload #7
    //   66: ifne -> 79
    //   69: iload #8
    //   71: istore #7
    //   73: iload #5
    //   75: iload_3
    //   76: if_icmpeq -> 186
    //   79: iload #8
    //   81: istore #7
    //   83: iload #6
    //   85: ifne -> 186
    //   88: iload #8
    //   90: istore #7
    //   92: getstatic android/os/Build$VERSION.SDK_INT : I
    //   95: bipush #17
    //   97: if_icmplt -> 186
    //   100: iload #8
    //   102: istore #7
    //   104: aload_0
    //   105: getfield mBaseContextAttached : Z
    //   108: ifne -> 186
    //   111: iload #8
    //   113: istore #7
    //   115: aload_0
    //   116: getfield mHost : Ljava/lang/Object;
    //   119: instanceof android/view/ContextThemeWrapper
    //   122: ifeq -> 186
    //   125: new android/content/res/Configuration
    //   128: dup
    //   129: invokespecial <init> : ()V
    //   132: astore #9
    //   134: aload #9
    //   136: aload #9
    //   138: getfield uiMode : I
    //   141: bipush #-49
    //   143: iand
    //   144: iload #5
    //   146: ior
    //   147: putfield uiMode : I
    //   150: aload_0
    //   151: getfield mHost : Ljava/lang/Object;
    //   154: checkcast android/view/ContextThemeWrapper
    //   157: aload #9
    //   159: invokevirtual applyOverrideConfiguration : (Landroid/content/res/Configuration;)V
    //   162: iconst_1
    //   163: istore #7
    //   165: goto -> 186
    //   168: astore #9
    //   170: ldc_w 'AppCompatDelegate'
    //   173: ldc_w 'updateForNightMode. Calling applyOverrideConfiguration() failed with an exception. Will fall back to using Resources.updateConfiguration()'
    //   176: aload #9
    //   178: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   181: pop
    //   182: iload #8
    //   184: istore #7
    //   186: aload_0
    //   187: getfield mContext : Landroid/content/Context;
    //   190: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   193: invokevirtual getConfiguration : ()Landroid/content/res/Configuration;
    //   196: getfield uiMode : I
    //   199: bipush #48
    //   201: iand
    //   202: istore_3
    //   203: iload #7
    //   205: istore #8
    //   207: iload #7
    //   209: ifne -> 298
    //   212: iload #7
    //   214: istore #8
    //   216: iload_3
    //   217: iload #5
    //   219: if_icmpeq -> 298
    //   222: iload #7
    //   224: istore #8
    //   226: iload_2
    //   227: ifeq -> 298
    //   230: iload #7
    //   232: istore #8
    //   234: iload #6
    //   236: ifne -> 298
    //   239: iload #7
    //   241: istore #8
    //   243: aload_0
    //   244: getfield mBaseContextAttached : Z
    //   247: ifeq -> 298
    //   250: getstatic android/os/Build$VERSION.SDK_INT : I
    //   253: bipush #17
    //   255: if_icmpge -> 269
    //   258: iload #7
    //   260: istore #8
    //   262: aload_0
    //   263: getfield mCreated : Z
    //   266: ifeq -> 298
    //   269: aload_0
    //   270: getfield mHost : Ljava/lang/Object;
    //   273: astore #9
    //   275: iload #7
    //   277: istore #8
    //   279: aload #9
    //   281: instanceof android/app/Activity
    //   284: ifeq -> 298
    //   287: aload #9
    //   289: checkcast android/app/Activity
    //   292: invokestatic recreate : (Landroid/app/Activity;)V
    //   295: iconst_1
    //   296: istore #8
    //   298: iload #8
    //   300: ifne -> 323
    //   303: iload_3
    //   304: iload #5
    //   306: if_icmpeq -> 323
    //   309: aload_0
    //   310: iload #5
    //   312: iload #6
    //   314: invokespecial updateResourcesConfigurationForNightMode : (IZ)V
    //   317: iload #4
    //   319: istore_2
    //   320: goto -> 326
    //   323: iload #8
    //   325: istore_2
    //   326: iload_2
    //   327: ifeq -> 353
    //   330: aload_0
    //   331: getfield mHost : Ljava/lang/Object;
    //   334: astore #9
    //   336: aload #9
    //   338: instanceof androidx/appcompat/app/AppCompatActivity
    //   341: ifeq -> 353
    //   344: aload #9
    //   346: checkcast androidx/appcompat/app/AppCompatActivity
    //   349: iload_1
    //   350: invokevirtual onNightModeChanged : (I)V
    //   353: iload_2
    //   354: ireturn
    // Exception table:
    //   from	to	target	type
    //   150	162	168	java/lang/IllegalStateException
  }
  
  private void updateResourcesConfigurationForNightMode(int paramInt, boolean paramBoolean) {
    Resources resources = this.mContext.getResources();
    Configuration configuration = new Configuration(resources.getConfiguration());
    configuration.uiMode = paramInt | (resources.getConfiguration()).uiMode & 0xFFFFFFCF;
    resources.updateConfiguration(configuration, null);
    if (Build.VERSION.SDK_INT < 26)
      ResourcesFlusher.flush(resources); 
    paramInt = this.mThemeResId;
    if (paramInt != 0) {
      this.mContext.setTheme(paramInt);
      if (Build.VERSION.SDK_INT >= 23)
        this.mContext.getTheme().applyStyle(this.mThemeResId, true); 
    } 
    if (paramBoolean) {
      Object object = this.mHost;
      if (object instanceof Activity) {
        object = object;
        if (object instanceof LifecycleOwner) {
          if (((LifecycleOwner)object).getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED))
            object.onConfigurationChanged(configuration); 
        } else if (this.mStarted) {
          object.onConfigurationChanged(configuration);
        } 
      } 
    } 
  }
  
  public void addContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    ensureSubDecor();
    ((ViewGroup)this.mSubDecor.findViewById(16908290)).addView(paramView, paramLayoutParams);
    this.mAppCompatWindowCallback.getWrapped().onContentChanged();
  }
  
  public boolean applyDayNight() {
    return applyDayNight(true);
  }
  
  public void attachBaseContext(Context paramContext) {
    applyDayNight(false);
    this.mBaseContextAttached = true;
  }
  
  void callOnPanelClosed(int paramInt, PanelFeatureState paramPanelFeatureState, Menu paramMenu) {
    MenuBuilder menuBuilder;
    PanelFeatureState panelFeatureState = paramPanelFeatureState;
    Menu menu = paramMenu;
    if (paramMenu == null) {
      PanelFeatureState panelFeatureState1 = paramPanelFeatureState;
      if (paramPanelFeatureState == null) {
        panelFeatureState1 = paramPanelFeatureState;
        if (paramInt >= 0) {
          PanelFeatureState[] arrayOfPanelFeatureState = this.mPanels;
          panelFeatureState1 = paramPanelFeatureState;
          if (paramInt < arrayOfPanelFeatureState.length)
            panelFeatureState1 = arrayOfPanelFeatureState[paramInt]; 
        } 
      } 
      panelFeatureState = panelFeatureState1;
      menu = paramMenu;
      if (panelFeatureState1 != null) {
        menuBuilder = panelFeatureState1.menu;
        panelFeatureState = panelFeatureState1;
      } 
    } 
    if (panelFeatureState != null && !panelFeatureState.isOpen)
      return; 
    if (!this.mIsDestroyed)
      this.mAppCompatWindowCallback.getWrapped().onPanelClosed(paramInt, (Menu)menuBuilder); 
  }
  
  void checkCloseActionMenu(MenuBuilder paramMenuBuilder) {
    if (this.mClosingActionMenu)
      return; 
    this.mClosingActionMenu = true;
    this.mDecorContentParent.dismissPopups();
    Window.Callback callback = getWindowCallback();
    if (callback != null && !this.mIsDestroyed)
      callback.onPanelClosed(108, (Menu)paramMenuBuilder); 
    this.mClosingActionMenu = false;
  }
  
  void closePanel(int paramInt) {
    closePanel(getPanelState(paramInt, true), true);
  }
  
  void closePanel(PanelFeatureState paramPanelFeatureState, boolean paramBoolean) {
    if (paramBoolean && paramPanelFeatureState.featureId == 0) {
      DecorContentParent decorContentParent = this.mDecorContentParent;
      if (decorContentParent != null && decorContentParent.isOverflowMenuShowing()) {
        checkCloseActionMenu(paramPanelFeatureState.menu);
        return;
      } 
    } 
    WindowManager windowManager = (WindowManager)this.mContext.getSystemService("window");
    if (windowManager != null && paramPanelFeatureState.isOpen && paramPanelFeatureState.decorView != null) {
      windowManager.removeView((View)paramPanelFeatureState.decorView);
      if (paramBoolean)
        callOnPanelClosed(paramPanelFeatureState.featureId, paramPanelFeatureState, (Menu)null); 
    } 
    paramPanelFeatureState.isPrepared = false;
    paramPanelFeatureState.isHandled = false;
    paramPanelFeatureState.isOpen = false;
    paramPanelFeatureState.shownPanelView = null;
    paramPanelFeatureState.refreshDecorView = true;
    if (this.mPreparedPanel == paramPanelFeatureState)
      this.mPreparedPanel = null; 
  }
  
  public View createView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet) {
    AppCompatViewInflater appCompatViewInflater = this.mAppCompatViewInflater;
    boolean bool = false;
    if (appCompatViewInflater == null) {
      String str = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme).getString(R.styleable.AppCompatTheme_viewInflaterClass);
      if (str == null || AppCompatViewInflater.class.getName().equals(str)) {
        this.mAppCompatViewInflater = new AppCompatViewInflater();
      } else {
        try {
          this.mAppCompatViewInflater = Class.forName(str).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } finally {
          appCompatViewInflater = null;
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Failed to instantiate custom view inflater ");
          stringBuilder.append(str);
          stringBuilder.append(". Falling back to default.");
          Log.i("AppCompatDelegate", stringBuilder.toString(), (Throwable)appCompatViewInflater);
        } 
      } 
    } 
    if (IS_PRE_LOLLIPOP) {
      if (paramAttributeSet instanceof XmlPullParser) {
        if (((XmlPullParser)paramAttributeSet).getDepth() > 1)
          bool = true; 
      } else {
        bool = shouldInheritContext((ViewParent)paramView);
      } 
    } else {
      bool = false;
    } 
    return this.mAppCompatViewInflater.createView(paramView, paramString, paramContext, paramAttributeSet, bool, IS_PRE_LOLLIPOP, true, VectorEnabledTintResources.shouldBeUsed());
  }
  
  void dismissPopups() {
    DecorContentParent decorContentParent = this.mDecorContentParent;
    if (decorContentParent != null)
      decorContentParent.dismissPopups(); 
    if (this.mActionModePopup != null) {
      this.mWindow.getDecorView().removeCallbacks(this.mShowActionModePopup);
      if (this.mActionModePopup.isShowing())
        try {
          this.mActionModePopup.dismiss();
        } catch (IllegalArgumentException illegalArgumentException) {} 
      this.mActionModePopup = null;
    } 
    endOnGoingFadeAnimation();
    PanelFeatureState panelFeatureState = getPanelState(0, false);
    if (panelFeatureState != null && panelFeatureState.menu != null)
      panelFeatureState.menu.close(); 
  }
  
  boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
    Object object = this.mHost;
    boolean bool = object instanceof KeyEventDispatcher.Component;
    boolean bool1 = true;
    if (bool || object instanceof AppCompatDialog) {
      object = this.mWindow.getDecorView();
      if (object != null && KeyEventDispatcher.dispatchBeforeHierarchy((View)object, paramKeyEvent))
        return true; 
    } 
    if (paramKeyEvent.getKeyCode() == 82 && this.mAppCompatWindowCallback.getWrapped().dispatchKeyEvent(paramKeyEvent))
      return true; 
    int i = paramKeyEvent.getKeyCode();
    if (paramKeyEvent.getAction() != 0)
      bool1 = false; 
    if (bool1) {
      bool = onKeyDown(i, paramKeyEvent);
    } else {
      bool = onKeyUp(i, paramKeyEvent);
    } 
    return bool;
  }
  
  void doInvalidatePanelMenu(int paramInt) {
    PanelFeatureState panelFeatureState = getPanelState(paramInt, true);
    if (panelFeatureState.menu != null) {
      Bundle bundle = new Bundle();
      panelFeatureState.menu.saveActionViewStates(bundle);
      if (bundle.size() > 0)
        panelFeatureState.frozenActionViewState = bundle; 
      panelFeatureState.menu.stopDispatchingItemsChanged();
      panelFeatureState.menu.clear();
    } 
    panelFeatureState.refreshMenuContent = true;
    panelFeatureState.refreshDecorView = true;
    if ((paramInt == 108 || paramInt == 0) && this.mDecorContentParent != null) {
      PanelFeatureState panelFeatureState1 = getPanelState(0, false);
      if (panelFeatureState1 != null) {
        panelFeatureState1.isPrepared = false;
        preparePanel(panelFeatureState1, (KeyEvent)null);
      } 
    } 
  }
  
  void endOnGoingFadeAnimation() {
    ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = this.mFadeAnim;
    if (viewPropertyAnimatorCompat != null)
      viewPropertyAnimatorCompat.cancel(); 
  }
  
  PanelFeatureState findMenuPanel(Menu paramMenu) {
    byte b2;
    PanelFeatureState[] arrayOfPanelFeatureState = this.mPanels;
    byte b1 = 0;
    if (arrayOfPanelFeatureState != null) {
      b2 = arrayOfPanelFeatureState.length;
    } else {
      b2 = 0;
    } 
    while (b1 < b2) {
      PanelFeatureState panelFeatureState = arrayOfPanelFeatureState[b1];
      if (panelFeatureState != null && panelFeatureState.menu == paramMenu)
        return panelFeatureState; 
      b1++;
    } 
    return null;
  }
  
  public <T extends View> T findViewById(int paramInt) {
    ensureSubDecor();
    return (T)this.mWindow.findViewById(paramInt);
  }
  
  final Context getActionBarThemedContext() {
    Context context;
    ActionBar actionBar1 = getSupportActionBar();
    if (actionBar1 != null) {
      Context context1 = actionBar1.getThemedContext();
    } else {
      actionBar1 = null;
    } 
    ActionBar actionBar2 = actionBar1;
    if (actionBar1 == null)
      context = this.mContext; 
    return context;
  }
  
  final AutoNightModeManager getAutoTimeNightModeManager() {
    if (this.mAutoTimeNightModeManager == null)
      this.mAutoTimeNightModeManager = new AutoTimeNightModeManager(TwilightManager.getInstance(this.mContext)); 
    return this.mAutoTimeNightModeManager;
  }
  
  public final ActionBarDrawerToggle.Delegate getDrawerToggleDelegate() {
    return new ActionBarDrawableToggleImpl();
  }
  
  public int getLocalNightMode() {
    return this.mLocalNightMode;
  }
  
  public MenuInflater getMenuInflater() {
    if (this.mMenuInflater == null) {
      Context context;
      initWindowDecorActionBar();
      ActionBar actionBar = this.mActionBar;
      if (actionBar != null) {
        context = actionBar.getThemedContext();
      } else {
        context = this.mContext;
      } 
      this.mMenuInflater = (MenuInflater)new SupportMenuInflater(context);
    } 
    return this.mMenuInflater;
  }
  
  protected PanelFeatureState getPanelState(int paramInt, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mPanels : [Landroidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState;
    //   4: astore_3
    //   5: aload_3
    //   6: ifnull -> 18
    //   9: aload_3
    //   10: astore #4
    //   12: aload_3
    //   13: arraylength
    //   14: iload_1
    //   15: if_icmpgt -> 46
    //   18: iload_1
    //   19: iconst_1
    //   20: iadd
    //   21: anewarray androidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState
    //   24: astore #4
    //   26: aload_3
    //   27: ifnull -> 40
    //   30: aload_3
    //   31: iconst_0
    //   32: aload #4
    //   34: iconst_0
    //   35: aload_3
    //   36: arraylength
    //   37: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   40: aload_0
    //   41: aload #4
    //   43: putfield mPanels : [Landroidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState;
    //   46: aload #4
    //   48: iload_1
    //   49: aaload
    //   50: astore #5
    //   52: aload #5
    //   54: astore_3
    //   55: aload #5
    //   57: ifnonnull -> 74
    //   60: new androidx/appcompat/app/AppCompatDelegateImpl$PanelFeatureState
    //   63: dup
    //   64: iload_1
    //   65: invokespecial <init> : (I)V
    //   68: astore_3
    //   69: aload #4
    //   71: iload_1
    //   72: aload_3
    //   73: aastore
    //   74: aload_3
    //   75: areturn
  }
  
  ViewGroup getSubDecor() {
    return this.mSubDecor;
  }
  
  public ActionBar getSupportActionBar() {
    initWindowDecorActionBar();
    return this.mActionBar;
  }
  
  final CharSequence getTitle() {
    Object object = this.mHost;
    return (object instanceof Activity) ? ((Activity)object).getTitle() : this.mTitle;
  }
  
  final Window.Callback getWindowCallback() {
    return this.mWindow.getCallback();
  }
  
  public boolean hasWindowFeature(int paramInt) {
    boolean bool;
    int i = sanitizeWindowFeatureId(paramInt);
    boolean bool1 = true;
    if (i != 1) {
      if (i != 2) {
        if (i != 5) {
          if (i != 10) {
            if (i != 108) {
              if (i != 109) {
                bool = false;
              } else {
                bool = this.mOverlayActionBar;
              } 
            } else {
              bool = this.mHasActionBar;
            } 
          } else {
            bool = this.mOverlayActionMode;
          } 
        } else {
          bool = this.mFeatureIndeterminateProgress;
        } 
      } else {
        bool = this.mFeatureProgress;
      } 
    } else {
      bool = this.mWindowNoTitle;
    } 
    boolean bool2 = bool1;
    if (!bool)
      if (this.mWindow.hasFeature(paramInt)) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }  
    return bool2;
  }
  
  public void installViewFactory() {
    LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
    if (layoutInflater.getFactory() == null) {
      LayoutInflaterCompat.setFactory2(layoutInflater, this);
    } else if (!(layoutInflater.getFactory2() instanceof AppCompatDelegateImpl)) {
      Log.i("AppCompatDelegate", "The Activity's LayoutInflater already has a Factory installed so we can not install AppCompat's");
    } 
  }
  
  public void invalidateOptionsMenu() {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null && actionBar.invalidateOptionsMenu())
      return; 
    invalidatePanelMenu(0);
  }
  
  public boolean isHandleNativeActionModesEnabled() {
    return this.mHandleNativeActionModes;
  }
  
  int mapNightMode(int paramInt) {
    if (paramInt != -100) {
      int i = paramInt;
      if (paramInt != -1)
        if (paramInt != 0) {
          i = paramInt;
          if (paramInt != 1) {
            i = paramInt;
            if (paramInt != 2) {
              if (paramInt == 3)
                return getAutoBatteryNightModeManager().getApplyableNightMode(); 
              throw new IllegalStateException("Unknown value set for night mode. Please use one of the MODE_NIGHT values from AppCompatDelegate.");
            } 
          } 
        } else {
          if (Build.VERSION.SDK_INT >= 23 && ((UiModeManager)this.mContext.getSystemService(UiModeManager.class)).getNightMode() == 0)
            return -1; 
          i = getAutoTimeNightModeManager().getApplyableNightMode();
        }  
      return i;
    } 
    return -1;
  }
  
  boolean onBackPressed() {
    ActionMode actionMode = this.mActionMode;
    if (actionMode != null) {
      actionMode.finish();
      return true;
    } 
    ActionBar actionBar = getSupportActionBar();
    return (actionBar != null && actionBar.collapseActionView());
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    if (this.mHasActionBar && this.mSubDecorInstalled) {
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null)
        actionBar.onConfigurationChanged(paramConfiguration); 
    } 
    AppCompatDrawableManager.get().onConfigurationChanged(this.mContext);
    applyDayNight(false);
  }
  
  public void onCreate(Bundle paramBundle) {
    this.mBaseContextAttached = true;
    applyDayNight(false);
    ensureWindow();
    Object object = this.mHost;
    if (object instanceof Activity) {
      Object object1;
      paramBundle = null;
      try {
        object = NavUtils.getParentActivityName((Activity)object);
        object1 = object;
      } catch (IllegalArgumentException illegalArgumentException) {}
      if (object1 != null) {
        object1 = peekSupportActionBar();
        if (object1 == null) {
          this.mEnableDefaultActionBarUp = true;
        } else {
          object1.setDefaultDisplayHomeAsUpEnabled(true);
        } 
      } 
    } 
    this.mCreated = true;
  }
  
  public final View onCreateView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet) {
    return createView(paramView, paramString, paramContext, paramAttributeSet);
  }
  
  public View onCreateView(String paramString, Context paramContext, AttributeSet paramAttributeSet) {
    return onCreateView((View)null, paramString, paramContext, paramAttributeSet);
  }
  
  public void onDestroy() {
    markStopped(this);
    if (this.mInvalidatePanelMenuPosted)
      this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable); 
    this.mStarted = false;
    this.mIsDestroyed = true;
    ActionBar actionBar = this.mActionBar;
    if (actionBar != null)
      actionBar.onDestroy(); 
    cleanupAutoManagers();
  }
  
  boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    boolean bool = true;
    if (paramInt != 4) {
      if (paramInt == 82) {
        onKeyDownPanel(0, paramKeyEvent);
        return true;
      } 
    } else {
      if ((paramKeyEvent.getFlags() & 0x80) == 0)
        bool = false; 
      this.mLongPressBackDown = bool;
    } 
    return false;
  }
  
  boolean onKeyShortcut(int paramInt, KeyEvent paramKeyEvent) {
    PanelFeatureState panelFeatureState1;
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null && actionBar.onKeyShortcut(paramInt, paramKeyEvent))
      return true; 
    PanelFeatureState panelFeatureState2 = this.mPreparedPanel;
    if (panelFeatureState2 != null && performPanelShortcut(panelFeatureState2, paramKeyEvent.getKeyCode(), paramKeyEvent, 1)) {
      panelFeatureState1 = this.mPreparedPanel;
      if (panelFeatureState1 != null)
        panelFeatureState1.isHandled = true; 
      return true;
    } 
    if (this.mPreparedPanel == null) {
      panelFeatureState2 = getPanelState(0, true);
      preparePanel(panelFeatureState2, (KeyEvent)panelFeatureState1);
      boolean bool = performPanelShortcut(panelFeatureState2, panelFeatureState1.getKeyCode(), (KeyEvent)panelFeatureState1, 1);
      panelFeatureState2.isPrepared = false;
      if (bool)
        return true; 
    } 
    return false;
  }
  
  boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
    if (paramInt != 4) {
      if (paramInt == 82) {
        onKeyUpPanel(0, paramKeyEvent);
        return true;
      } 
    } else {
      boolean bool = this.mLongPressBackDown;
      this.mLongPressBackDown = false;
      PanelFeatureState panelFeatureState = getPanelState(0, false);
      if (panelFeatureState != null && panelFeatureState.isOpen) {
        if (!bool)
          closePanel(panelFeatureState, true); 
        return true;
      } 
      if (onBackPressed())
        return true; 
    } 
    return false;
  }
  
  public boolean onMenuItemSelected(MenuBuilder paramMenuBuilder, MenuItem paramMenuItem) {
    Window.Callback callback = getWindowCallback();
    if (callback != null && !this.mIsDestroyed) {
      PanelFeatureState panelFeatureState = findMenuPanel((Menu)paramMenuBuilder.getRootMenu());
      if (panelFeatureState != null)
        return callback.onMenuItemSelected(panelFeatureState.featureId, paramMenuItem); 
    } 
    return false;
  }
  
  public void onMenuModeChange(MenuBuilder paramMenuBuilder) {
    reopenMenu(paramMenuBuilder, true);
  }
  
  void onMenuOpened(int paramInt) {
    if (paramInt == 108) {
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null)
        actionBar.dispatchMenuVisibilityChanged(true); 
    } 
  }
  
  void onPanelClosed(int paramInt) {
    if (paramInt == 108) {
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null)
        actionBar.dispatchMenuVisibilityChanged(false); 
    } else if (paramInt == 0) {
      PanelFeatureState panelFeatureState = getPanelState(paramInt, true);
      if (panelFeatureState.isOpen)
        closePanel(panelFeatureState, false); 
    } 
  }
  
  public void onPostCreate(Bundle paramBundle) {
    ensureSubDecor();
  }
  
  public void onPostResume() {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null)
      actionBar.setShowHideAnimationEnabled(true); 
  }
  
  public void onSaveInstanceState(Bundle paramBundle) {
    if (this.mLocalNightMode != -100)
      sLocalNightModes.put(this.mHost.getClass(), Integer.valueOf(this.mLocalNightMode)); 
  }
  
  public void onStart() {
    this.mStarted = true;
    applyDayNight();
    markStarted(this);
  }
  
  public void onStop() {
    this.mStarted = false;
    markStopped(this);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null)
      actionBar.setShowHideAnimationEnabled(false); 
    if (this.mHost instanceof Dialog)
      cleanupAutoManagers(); 
  }
  
  void onSubDecorInstalled(ViewGroup paramViewGroup) {}
  
  final ActionBar peekSupportActionBar() {
    return this.mActionBar;
  }
  
  public boolean requestWindowFeature(int paramInt) {
    paramInt = sanitizeWindowFeatureId(paramInt);
    if (this.mWindowNoTitle && paramInt == 108)
      return false; 
    if (this.mHasActionBar && paramInt == 1)
      this.mHasActionBar = false; 
    if (paramInt != 1) {
      if (paramInt != 2) {
        if (paramInt != 5) {
          if (paramInt != 10) {
            if (paramInt != 108) {
              if (paramInt != 109)
                return this.mWindow.requestFeature(paramInt); 
              throwFeatureRequestIfSubDecorInstalled();
              this.mOverlayActionBar = true;
              return true;
            } 
            throwFeatureRequestIfSubDecorInstalled();
            this.mHasActionBar = true;
            return true;
          } 
          throwFeatureRequestIfSubDecorInstalled();
          this.mOverlayActionMode = true;
          return true;
        } 
        throwFeatureRequestIfSubDecorInstalled();
        this.mFeatureIndeterminateProgress = true;
        return true;
      } 
      throwFeatureRequestIfSubDecorInstalled();
      this.mFeatureProgress = true;
      return true;
    } 
    throwFeatureRequestIfSubDecorInstalled();
    this.mWindowNoTitle = true;
    return true;
  }
  
  public void setContentView(int paramInt) {
    ensureSubDecor();
    ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(16908290);
    viewGroup.removeAllViews();
    LayoutInflater.from(this.mContext).inflate(paramInt, viewGroup);
    this.mAppCompatWindowCallback.getWrapped().onContentChanged();
  }
  
  public void setContentView(View paramView) {
    ensureSubDecor();
    ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(16908290);
    viewGroup.removeAllViews();
    viewGroup.addView(paramView);
    this.mAppCompatWindowCallback.getWrapped().onContentChanged();
  }
  
  public void setContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    ensureSubDecor();
    ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(16908290);
    viewGroup.removeAllViews();
    viewGroup.addView(paramView, paramLayoutParams);
    this.mAppCompatWindowCallback.getWrapped().onContentChanged();
  }
  
  public void setHandleNativeActionModesEnabled(boolean paramBoolean) {
    this.mHandleNativeActionModes = paramBoolean;
  }
  
  public void setLocalNightMode(int paramInt) {
    if (this.mLocalNightMode != paramInt) {
      this.mLocalNightMode = paramInt;
      applyDayNight();
    } 
  }
  
  public void setSupportActionBar(Toolbar paramToolbar) {
    if (!(this.mHost instanceof Activity))
      return; 
    ActionBar actionBar = getSupportActionBar();
    if (!(actionBar instanceof WindowDecorActionBar)) {
      this.mMenuInflater = null;
      if (actionBar != null)
        actionBar.onDestroy(); 
      if (paramToolbar != null) {
        ToolbarActionBar toolbarActionBar = new ToolbarActionBar(paramToolbar, getTitle(), (Window.Callback)this.mAppCompatWindowCallback);
        this.mActionBar = toolbarActionBar;
        this.mWindow.setCallback(toolbarActionBar.getWrappedWindowCallback());
      } else {
        this.mActionBar = null;
        this.mWindow.setCallback((Window.Callback)this.mAppCompatWindowCallback);
      } 
      invalidateOptionsMenu();
      return;
    } 
    throw new IllegalStateException("This Activity already has an action bar supplied by the window decor. Do not request Window.FEATURE_SUPPORT_ACTION_BAR and set windowActionBar to false in your theme to use a Toolbar instead.");
  }
  
  public void setTheme(int paramInt) {
    this.mThemeResId = paramInt;
  }
  
  public final void setTitle(CharSequence paramCharSequence) {
    this.mTitle = paramCharSequence;
    DecorContentParent decorContentParent = this.mDecorContentParent;
    if (decorContentParent != null) {
      decorContentParent.setWindowTitle(paramCharSequence);
    } else if (peekSupportActionBar() != null) {
      peekSupportActionBar().setWindowTitle(paramCharSequence);
    } else {
      TextView textView = this.mTitleView;
      if (textView != null)
        textView.setText(paramCharSequence); 
    } 
  }
  
  final boolean shouldAnimateActionModeView() {
    if (this.mSubDecorInstalled) {
      ViewGroup viewGroup = this.mSubDecor;
      if (viewGroup != null && ViewCompat.isLaidOut((View)viewGroup))
        return true; 
    } 
    return false;
  }
  
  public ActionMode startSupportActionMode(ActionMode.Callback paramCallback) {
    if (paramCallback != null) {
      ActionMode actionMode = this.mActionMode;
      if (actionMode != null)
        actionMode.finish(); 
      paramCallback = new ActionModeCallbackWrapperV9(paramCallback);
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null) {
        this.mActionMode = actionBar.startActionMode(paramCallback);
        ActionMode actionMode1 = this.mActionMode;
        if (actionMode1 != null) {
          AppCompatCallback appCompatCallback = this.mAppCompatCallback;
          if (appCompatCallback != null)
            appCompatCallback.onSupportActionModeStarted(actionMode1); 
        } 
      } 
      if (this.mActionMode == null)
        this.mActionMode = startSupportActionModeFromWindow(paramCallback); 
      return this.mActionMode;
    } 
    throw new IllegalArgumentException("ActionMode callback can not be null.");
  }
  
  ActionMode startSupportActionModeFromWindow(ActionMode.Callback paramCallback) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual endOnGoingFadeAnimation : ()V
    //   4: aload_0
    //   5: getfield mActionMode : Landroidx/appcompat/view/ActionMode;
    //   8: astore_2
    //   9: aload_2
    //   10: ifnull -> 17
    //   13: aload_2
    //   14: invokevirtual finish : ()V
    //   17: aload_1
    //   18: astore_2
    //   19: aload_1
    //   20: instanceof androidx/appcompat/app/AppCompatDelegateImpl$ActionModeCallbackWrapperV9
    //   23: ifne -> 36
    //   26: new androidx/appcompat/app/AppCompatDelegateImpl$ActionModeCallbackWrapperV9
    //   29: dup
    //   30: aload_0
    //   31: aload_1
    //   32: invokespecial <init> : (Landroidx/appcompat/app/AppCompatDelegateImpl;Landroidx/appcompat/view/ActionMode$Callback;)V
    //   35: astore_2
    //   36: aload_0
    //   37: getfield mAppCompatCallback : Landroidx/appcompat/app/AppCompatCallback;
    //   40: astore_1
    //   41: aload_1
    //   42: ifnull -> 63
    //   45: aload_0
    //   46: getfield mIsDestroyed : Z
    //   49: ifne -> 63
    //   52: aload_1
    //   53: aload_2
    //   54: invokeinterface onWindowStartingSupportActionMode : (Landroidx/appcompat/view/ActionMode$Callback;)Landroidx/appcompat/view/ActionMode;
    //   59: astore_1
    //   60: goto -> 65
    //   63: aconst_null
    //   64: astore_1
    //   65: aload_1
    //   66: ifnull -> 77
    //   69: aload_0
    //   70: aload_1
    //   71: putfield mActionMode : Landroidx/appcompat/view/ActionMode;
    //   74: goto -> 567
    //   77: aload_0
    //   78: getfield mActionModeView : Landroidx/appcompat/widget/ActionBarContextView;
    //   81: astore_1
    //   82: iconst_1
    //   83: istore_3
    //   84: aload_1
    //   85: ifnonnull -> 349
    //   88: aload_0
    //   89: getfield mIsFloating : Z
    //   92: ifeq -> 309
    //   95: new android/util/TypedValue
    //   98: dup
    //   99: invokespecial <init> : ()V
    //   102: astore #4
    //   104: aload_0
    //   105: getfield mContext : Landroid/content/Context;
    //   108: invokevirtual getTheme : ()Landroid/content/res/Resources$Theme;
    //   111: astore_1
    //   112: aload_1
    //   113: getstatic androidx/appcompat/R$attr.actionBarTheme : I
    //   116: aload #4
    //   118: iconst_1
    //   119: invokevirtual resolveAttribute : (ILandroid/util/TypedValue;Z)Z
    //   122: pop
    //   123: aload #4
    //   125: getfield resourceId : I
    //   128: ifeq -> 185
    //   131: aload_0
    //   132: getfield mContext : Landroid/content/Context;
    //   135: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   138: invokevirtual newTheme : ()Landroid/content/res/Resources$Theme;
    //   141: astore #5
    //   143: aload #5
    //   145: aload_1
    //   146: invokevirtual setTo : (Landroid/content/res/Resources$Theme;)V
    //   149: aload #5
    //   151: aload #4
    //   153: getfield resourceId : I
    //   156: iconst_1
    //   157: invokevirtual applyStyle : (IZ)V
    //   160: new androidx/appcompat/view/ContextThemeWrapper
    //   163: dup
    //   164: aload_0
    //   165: getfield mContext : Landroid/content/Context;
    //   168: iconst_0
    //   169: invokespecial <init> : (Landroid/content/Context;I)V
    //   172: astore_1
    //   173: aload_1
    //   174: invokevirtual getTheme : ()Landroid/content/res/Resources$Theme;
    //   177: aload #5
    //   179: invokevirtual setTo : (Landroid/content/res/Resources$Theme;)V
    //   182: goto -> 190
    //   185: aload_0
    //   186: getfield mContext : Landroid/content/Context;
    //   189: astore_1
    //   190: aload_0
    //   191: new androidx/appcompat/widget/ActionBarContextView
    //   194: dup
    //   195: aload_1
    //   196: invokespecial <init> : (Landroid/content/Context;)V
    //   199: putfield mActionModeView : Landroidx/appcompat/widget/ActionBarContextView;
    //   202: aload_0
    //   203: new android/widget/PopupWindow
    //   206: dup
    //   207: aload_1
    //   208: aconst_null
    //   209: getstatic androidx/appcompat/R$attr.actionModePopupWindowStyle : I
    //   212: invokespecial <init> : (Landroid/content/Context;Landroid/util/AttributeSet;I)V
    //   215: putfield mActionModePopup : Landroid/widget/PopupWindow;
    //   218: aload_0
    //   219: getfield mActionModePopup : Landroid/widget/PopupWindow;
    //   222: iconst_2
    //   223: invokestatic setWindowLayoutType : (Landroid/widget/PopupWindow;I)V
    //   226: aload_0
    //   227: getfield mActionModePopup : Landroid/widget/PopupWindow;
    //   230: aload_0
    //   231: getfield mActionModeView : Landroidx/appcompat/widget/ActionBarContextView;
    //   234: invokevirtual setContentView : (Landroid/view/View;)V
    //   237: aload_0
    //   238: getfield mActionModePopup : Landroid/widget/PopupWindow;
    //   241: iconst_m1
    //   242: invokevirtual setWidth : (I)V
    //   245: aload_1
    //   246: invokevirtual getTheme : ()Landroid/content/res/Resources$Theme;
    //   249: getstatic androidx/appcompat/R$attr.actionBarSize : I
    //   252: aload #4
    //   254: iconst_1
    //   255: invokevirtual resolveAttribute : (ILandroid/util/TypedValue;Z)Z
    //   258: pop
    //   259: aload #4
    //   261: getfield data : I
    //   264: aload_1
    //   265: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   268: invokevirtual getDisplayMetrics : ()Landroid/util/DisplayMetrics;
    //   271: invokestatic complexToDimensionPixelSize : (ILandroid/util/DisplayMetrics;)I
    //   274: istore #6
    //   276: aload_0
    //   277: getfield mActionModeView : Landroidx/appcompat/widget/ActionBarContextView;
    //   280: iload #6
    //   282: invokevirtual setContentHeight : (I)V
    //   285: aload_0
    //   286: getfield mActionModePopup : Landroid/widget/PopupWindow;
    //   289: bipush #-2
    //   291: invokevirtual setHeight : (I)V
    //   294: aload_0
    //   295: new androidx/appcompat/app/AppCompatDelegateImpl$6
    //   298: dup
    //   299: aload_0
    //   300: invokespecial <init> : (Landroidx/appcompat/app/AppCompatDelegateImpl;)V
    //   303: putfield mShowActionModePopup : Ljava/lang/Runnable;
    //   306: goto -> 349
    //   309: aload_0
    //   310: getfield mSubDecor : Landroid/view/ViewGroup;
    //   313: getstatic androidx/appcompat/R$id.action_mode_bar_stub : I
    //   316: invokevirtual findViewById : (I)Landroid/view/View;
    //   319: checkcast androidx/appcompat/widget/ViewStubCompat
    //   322: astore_1
    //   323: aload_1
    //   324: ifnull -> 349
    //   327: aload_1
    //   328: aload_0
    //   329: invokevirtual getActionBarThemedContext : ()Landroid/content/Context;
    //   332: invokestatic from : (Landroid/content/Context;)Landroid/view/LayoutInflater;
    //   335: invokevirtual setLayoutInflater : (Landroid/view/LayoutInflater;)V
    //   338: aload_0
    //   339: aload_1
    //   340: invokevirtual inflate : ()Landroid/view/View;
    //   343: checkcast androidx/appcompat/widget/ActionBarContextView
    //   346: putfield mActionModeView : Landroidx/appcompat/widget/ActionBarContextView;
    //   349: aload_0
    //   350: getfield mActionModeView : Landroidx/appcompat/widget/ActionBarContextView;
    //   353: ifnull -> 567
    //   356: aload_0
    //   357: invokevirtual endOnGoingFadeAnimation : ()V
    //   360: aload_0
    //   361: getfield mActionModeView : Landroidx/appcompat/widget/ActionBarContextView;
    //   364: invokevirtual killMode : ()V
    //   367: aload_0
    //   368: getfield mActionModeView : Landroidx/appcompat/widget/ActionBarContextView;
    //   371: invokevirtual getContext : ()Landroid/content/Context;
    //   374: astore_1
    //   375: aload_0
    //   376: getfield mActionModeView : Landroidx/appcompat/widget/ActionBarContextView;
    //   379: astore #4
    //   381: aload_0
    //   382: getfield mActionModePopup : Landroid/widget/PopupWindow;
    //   385: ifnonnull -> 391
    //   388: goto -> 393
    //   391: iconst_0
    //   392: istore_3
    //   393: new androidx/appcompat/view/StandaloneActionMode
    //   396: dup
    //   397: aload_1
    //   398: aload #4
    //   400: aload_2
    //   401: iload_3
    //   402: invokespecial <init> : (Landroid/content/Context;Landroidx/appcompat/widget/ActionBarContextView;Landroidx/appcompat/view/ActionMode$Callback;Z)V
    //   405: astore_1
    //   406: aload_2
    //   407: aload_1
    //   408: aload_1
    //   409: invokevirtual getMenu : ()Landroid/view/Menu;
    //   412: invokeinterface onCreateActionMode : (Landroidx/appcompat/view/ActionMode;Landroid/view/Menu;)Z
    //   417: ifeq -> 562
    //   420: aload_1
    //   421: invokevirtual invalidate : ()V
    //   424: aload_0
    //   425: getfield mActionModeView : Landroidx/appcompat/widget/ActionBarContextView;
    //   428: aload_1
    //   429: invokevirtual initForMode : (Landroidx/appcompat/view/ActionMode;)V
    //   432: aload_0
    //   433: aload_1
    //   434: putfield mActionMode : Landroidx/appcompat/view/ActionMode;
    //   437: aload_0
    //   438: invokevirtual shouldAnimateActionModeView : ()Z
    //   441: ifeq -> 486
    //   444: aload_0
    //   445: getfield mActionModeView : Landroidx/appcompat/widget/ActionBarContextView;
    //   448: fconst_0
    //   449: invokevirtual setAlpha : (F)V
    //   452: aload_0
    //   453: aload_0
    //   454: getfield mActionModeView : Landroidx/appcompat/widget/ActionBarContextView;
    //   457: invokestatic animate : (Landroid/view/View;)Landroidx/core/view/ViewPropertyAnimatorCompat;
    //   460: fconst_1
    //   461: invokevirtual alpha : (F)Landroidx/core/view/ViewPropertyAnimatorCompat;
    //   464: putfield mFadeAnim : Landroidx/core/view/ViewPropertyAnimatorCompat;
    //   467: aload_0
    //   468: getfield mFadeAnim : Landroidx/core/view/ViewPropertyAnimatorCompat;
    //   471: new androidx/appcompat/app/AppCompatDelegateImpl$7
    //   474: dup
    //   475: aload_0
    //   476: invokespecial <init> : (Landroidx/appcompat/app/AppCompatDelegateImpl;)V
    //   479: invokevirtual setListener : (Landroidx/core/view/ViewPropertyAnimatorListener;)Landroidx/core/view/ViewPropertyAnimatorCompat;
    //   482: pop
    //   483: goto -> 537
    //   486: aload_0
    //   487: getfield mActionModeView : Landroidx/appcompat/widget/ActionBarContextView;
    //   490: fconst_1
    //   491: invokevirtual setAlpha : (F)V
    //   494: aload_0
    //   495: getfield mActionModeView : Landroidx/appcompat/widget/ActionBarContextView;
    //   498: iconst_0
    //   499: invokevirtual setVisibility : (I)V
    //   502: aload_0
    //   503: getfield mActionModeView : Landroidx/appcompat/widget/ActionBarContextView;
    //   506: bipush #32
    //   508: invokevirtual sendAccessibilityEvent : (I)V
    //   511: aload_0
    //   512: getfield mActionModeView : Landroidx/appcompat/widget/ActionBarContextView;
    //   515: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   518: instanceof android/view/View
    //   521: ifeq -> 537
    //   524: aload_0
    //   525: getfield mActionModeView : Landroidx/appcompat/widget/ActionBarContextView;
    //   528: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   531: checkcast android/view/View
    //   534: invokestatic requestApplyInsets : (Landroid/view/View;)V
    //   537: aload_0
    //   538: getfield mActionModePopup : Landroid/widget/PopupWindow;
    //   541: ifnull -> 567
    //   544: aload_0
    //   545: getfield mWindow : Landroid/view/Window;
    //   548: invokevirtual getDecorView : ()Landroid/view/View;
    //   551: aload_0
    //   552: getfield mShowActionModePopup : Ljava/lang/Runnable;
    //   555: invokevirtual post : (Ljava/lang/Runnable;)Z
    //   558: pop
    //   559: goto -> 567
    //   562: aload_0
    //   563: aconst_null
    //   564: putfield mActionMode : Landroidx/appcompat/view/ActionMode;
    //   567: aload_0
    //   568: getfield mActionMode : Landroidx/appcompat/view/ActionMode;
    //   571: astore_2
    //   572: aload_2
    //   573: ifnull -> 592
    //   576: aload_0
    //   577: getfield mAppCompatCallback : Landroidx/appcompat/app/AppCompatCallback;
    //   580: astore_1
    //   581: aload_1
    //   582: ifnull -> 592
    //   585: aload_1
    //   586: aload_2
    //   587: invokeinterface onSupportActionModeStarted : (Landroidx/appcompat/view/ActionMode;)V
    //   592: aload_0
    //   593: getfield mActionMode : Landroidx/appcompat/view/ActionMode;
    //   596: areturn
    //   597: astore_1
    //   598: goto -> 63
    // Exception table:
    //   from	to	target	type
    //   52	60	597	java/lang/AbstractMethodError
  }
  
  int updateStatusGuard(int paramInt) {
    boolean bool2;
    ActionBarContextView actionBarContextView = this.mActionModeView;
    boolean bool1 = false;
    if (actionBarContextView != null && actionBarContextView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
      boolean bool4;
      boolean bool5;
      int i;
      ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)this.mActionModeView.getLayoutParams();
      boolean bool = this.mActionModeView.isShown();
      boolean bool3 = true;
      if (bool) {
        boolean bool6;
        if (this.mTempRect1 == null) {
          this.mTempRect1 = new Rect();
          this.mTempRect2 = new Rect();
        } 
        Rect rect1 = this.mTempRect1;
        Rect rect2 = this.mTempRect2;
        rect1.set(0, paramInt, 0, 0);
        ViewUtils.computeFitSystemWindows((View)this.mSubDecor, rect1, rect2);
        if (rect2.top == 0) {
          bool4 = paramInt;
        } else {
          bool4 = false;
        } 
        if (marginLayoutParams.topMargin != bool4) {
          marginLayoutParams.topMargin = paramInt;
          View view1 = this.mStatusGuard;
          if (view1 == null) {
            this.mStatusGuard = new View(this.mContext);
            this.mStatusGuard.setBackgroundColor(this.mContext.getResources().getColor(R.color.abc_input_method_navigation_guard));
            this.mSubDecor.addView(this.mStatusGuard, -1, new ViewGroup.LayoutParams(-1, paramInt));
          } else {
            ViewGroup.LayoutParams layoutParams = view1.getLayoutParams();
            if (layoutParams.height != paramInt) {
              layoutParams.height = paramInt;
              this.mStatusGuard.setLayoutParams(layoutParams);
            } 
          } 
          bool6 = true;
        } else {
          bool6 = false;
        } 
        if (this.mStatusGuard == null)
          bool3 = false; 
        bool5 = bool6;
        bool4 = bool3;
        i = paramInt;
        if (!this.mOverlayActionMode) {
          bool5 = bool6;
          bool4 = bool3;
          i = paramInt;
          if (bool3) {
            i = 0;
            bool5 = bool6;
            bool4 = bool3;
          } 
        } 
      } else {
        boolean bool6;
        if (marginLayoutParams.topMargin != 0) {
          marginLayoutParams.topMargin = 0;
          bool6 = true;
        } else {
          bool6 = false;
        } 
        bool4 = false;
        i = paramInt;
        bool5 = bool6;
      } 
      bool2 = bool4;
      paramInt = i;
      if (bool5) {
        this.mActionModeView.setLayoutParams((ViewGroup.LayoutParams)marginLayoutParams);
        bool2 = bool4;
        paramInt = i;
      } 
    } else {
      bool2 = false;
    } 
    View view = this.mStatusGuard;
    if (view != null) {
      byte b;
      if (bool2) {
        b = bool1;
      } else {
        b = 8;
      } 
      view.setVisibility(b);
    } 
    return paramInt;
  }
  
  private class ActionBarDrawableToggleImpl implements ActionBarDrawerToggle.Delegate {
    public Context getActionBarThemedContext() {
      return AppCompatDelegateImpl.this.getActionBarThemedContext();
    }
    
    public Drawable getThemeUpIndicator() {
      TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(getActionBarThemedContext(), null, new int[] { R.attr.homeAsUpIndicator });
      Drawable drawable = tintTypedArray.getDrawable(0);
      tintTypedArray.recycle();
      return drawable;
    }
    
    public boolean isNavigationVisible() {
      boolean bool;
      ActionBar actionBar = AppCompatDelegateImpl.this.getSupportActionBar();
      if (actionBar != null && (actionBar.getDisplayOptions() & 0x4) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void setActionBarDescription(int param1Int) {
      ActionBar actionBar = AppCompatDelegateImpl.this.getSupportActionBar();
      if (actionBar != null)
        actionBar.setHomeActionContentDescription(param1Int); 
    }
    
    public void setActionBarUpIndicator(Drawable param1Drawable, int param1Int) {
      ActionBar actionBar = AppCompatDelegateImpl.this.getSupportActionBar();
      if (actionBar != null) {
        actionBar.setHomeAsUpIndicator(param1Drawable);
        actionBar.setHomeActionContentDescription(param1Int);
      } 
    }
  }
  
  private final class ActionMenuPresenterCallback implements MenuPresenter.Callback {
    public void onCloseMenu(MenuBuilder param1MenuBuilder, boolean param1Boolean) {
      AppCompatDelegateImpl.this.checkCloseActionMenu(param1MenuBuilder);
    }
    
    public boolean onOpenSubMenu(MenuBuilder param1MenuBuilder) {
      Window.Callback callback = AppCompatDelegateImpl.this.getWindowCallback();
      if (callback != null)
        callback.onMenuOpened(108, (Menu)param1MenuBuilder); 
      return true;
    }
  }
  
  class ActionModeCallbackWrapperV9 implements ActionMode.Callback {
    private ActionMode.Callback mWrapped;
    
    public ActionModeCallbackWrapperV9(ActionMode.Callback param1Callback) {
      this.mWrapped = param1Callback;
    }
    
    public boolean onActionItemClicked(ActionMode param1ActionMode, MenuItem param1MenuItem) {
      return this.mWrapped.onActionItemClicked(param1ActionMode, param1MenuItem);
    }
    
    public boolean onCreateActionMode(ActionMode param1ActionMode, Menu param1Menu) {
      return this.mWrapped.onCreateActionMode(param1ActionMode, param1Menu);
    }
    
    public void onDestroyActionMode(ActionMode param1ActionMode) {
      this.mWrapped.onDestroyActionMode(param1ActionMode);
      if (AppCompatDelegateImpl.this.mActionModePopup != null)
        AppCompatDelegateImpl.this.mWindow.getDecorView().removeCallbacks(AppCompatDelegateImpl.this.mShowActionModePopup); 
      if (AppCompatDelegateImpl.this.mActionModeView != null) {
        AppCompatDelegateImpl.this.endOnGoingFadeAnimation();
        AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
        appCompatDelegateImpl.mFadeAnim = ViewCompat.animate((View)appCompatDelegateImpl.mActionModeView).alpha(0.0F);
        AppCompatDelegateImpl.this.mFadeAnim.setListener((ViewPropertyAnimatorListener)new ViewPropertyAnimatorListenerAdapter() {
              public void onAnimationEnd(View param2View) {
                AppCompatDelegateImpl.this.mActionModeView.setVisibility(8);
                if (AppCompatDelegateImpl.this.mActionModePopup != null) {
                  AppCompatDelegateImpl.this.mActionModePopup.dismiss();
                } else if (AppCompatDelegateImpl.this.mActionModeView.getParent() instanceof View) {
                  ViewCompat.requestApplyInsets((View)AppCompatDelegateImpl.this.mActionModeView.getParent());
                } 
                AppCompatDelegateImpl.this.mActionModeView.removeAllViews();
                AppCompatDelegateImpl.this.mFadeAnim.setListener(null);
                AppCompatDelegateImpl.this.mFadeAnim = null;
              }
            });
      } 
      if (AppCompatDelegateImpl.this.mAppCompatCallback != null)
        AppCompatDelegateImpl.this.mAppCompatCallback.onSupportActionModeFinished(AppCompatDelegateImpl.this.mActionMode); 
      AppCompatDelegateImpl.this.mActionMode = null;
    }
    
    public boolean onPrepareActionMode(ActionMode param1ActionMode, Menu param1Menu) {
      return this.mWrapped.onPrepareActionMode(param1ActionMode, param1Menu);
    }
  }
  
  class null extends ViewPropertyAnimatorListenerAdapter {
    public void onAnimationEnd(View param1View) {
      AppCompatDelegateImpl.this.mActionModeView.setVisibility(8);
      if (AppCompatDelegateImpl.this.mActionModePopup != null) {
        AppCompatDelegateImpl.this.mActionModePopup.dismiss();
      } else if (AppCompatDelegateImpl.this.mActionModeView.getParent() instanceof View) {
        ViewCompat.requestApplyInsets((View)AppCompatDelegateImpl.this.mActionModeView.getParent());
      } 
      AppCompatDelegateImpl.this.mActionModeView.removeAllViews();
      AppCompatDelegateImpl.this.mFadeAnim.setListener(null);
      AppCompatDelegateImpl.this.mFadeAnim = null;
    }
  }
  
  class AppCompatWindowCallback extends WindowCallbackWrapper {
    AppCompatWindowCallback(Window.Callback param1Callback) {
      super(param1Callback);
    }
    
    public boolean dispatchKeyEvent(KeyEvent param1KeyEvent) {
      return (AppCompatDelegateImpl.this.dispatchKeyEvent(param1KeyEvent) || super.dispatchKeyEvent(param1KeyEvent));
    }
    
    public boolean dispatchKeyShortcutEvent(KeyEvent param1KeyEvent) {
      return (super.dispatchKeyShortcutEvent(param1KeyEvent) || AppCompatDelegateImpl.this.onKeyShortcut(param1KeyEvent.getKeyCode(), param1KeyEvent));
    }
    
    public void onContentChanged() {}
    
    public boolean onCreatePanelMenu(int param1Int, Menu param1Menu) {
      return (param1Int == 0 && !(param1Menu instanceof MenuBuilder)) ? false : super.onCreatePanelMenu(param1Int, param1Menu);
    }
    
    public boolean onMenuOpened(int param1Int, Menu param1Menu) {
      super.onMenuOpened(param1Int, param1Menu);
      AppCompatDelegateImpl.this.onMenuOpened(param1Int);
      return true;
    }
    
    public void onPanelClosed(int param1Int, Menu param1Menu) {
      super.onPanelClosed(param1Int, param1Menu);
      AppCompatDelegateImpl.this.onPanelClosed(param1Int);
    }
    
    public boolean onPreparePanel(int param1Int, View param1View, Menu param1Menu) {
      MenuBuilder menuBuilder;
      if (param1Menu instanceof MenuBuilder) {
        menuBuilder = (MenuBuilder)param1Menu;
      } else {
        menuBuilder = null;
      } 
      if (param1Int == 0 && menuBuilder == null)
        return false; 
      if (menuBuilder != null)
        menuBuilder.setOverrideVisibleItems(true); 
      boolean bool = super.onPreparePanel(param1Int, param1View, param1Menu);
      if (menuBuilder != null)
        menuBuilder.setOverrideVisibleItems(false); 
      return bool;
    }
    
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> param1List, Menu param1Menu, int param1Int) {
      AppCompatDelegateImpl.PanelFeatureState panelFeatureState = AppCompatDelegateImpl.this.getPanelState(0, true);
      if (panelFeatureState != null && panelFeatureState.menu != null) {
        super.onProvideKeyboardShortcuts(param1List, (Menu)panelFeatureState.menu, param1Int);
      } else {
        super.onProvideKeyboardShortcuts(param1List, param1Menu, param1Int);
      } 
    }
    
    public ActionMode onWindowStartingActionMode(ActionMode.Callback param1Callback) {
      return (Build.VERSION.SDK_INT >= 23) ? null : (AppCompatDelegateImpl.this.isHandleNativeActionModesEnabled() ? startAsSupportActionMode(param1Callback) : super.onWindowStartingActionMode(param1Callback));
    }
    
    public ActionMode onWindowStartingActionMode(ActionMode.Callback param1Callback, int param1Int) {
      return (!AppCompatDelegateImpl.this.isHandleNativeActionModesEnabled() || param1Int != 0) ? super.onWindowStartingActionMode(param1Callback, param1Int) : startAsSupportActionMode(param1Callback);
    }
    
    final ActionMode startAsSupportActionMode(ActionMode.Callback param1Callback) {
      SupportActionModeWrapper.CallbackWrapper callbackWrapper = new SupportActionModeWrapper.CallbackWrapper(AppCompatDelegateImpl.this.mContext, param1Callback);
      ActionMode actionMode = AppCompatDelegateImpl.this.startSupportActionMode((ActionMode.Callback)callbackWrapper);
      return (actionMode != null) ? callbackWrapper.getActionModeWrapper(actionMode) : null;
    }
  }
  
  private class AutoBatteryNightModeManager extends AutoNightModeManager {
    private final PowerManager mPowerManager;
    
    AutoBatteryNightModeManager(Context param1Context) {
      this.mPowerManager = (PowerManager)param1Context.getSystemService("power");
    }
    
    IntentFilter createIntentFilterForBroadcastReceiver() {
      if (Build.VERSION.SDK_INT >= 21) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.os.action.POWER_SAVE_MODE_CHANGED");
        return intentFilter;
      } 
      return null;
    }
    
    public int getApplyableNightMode() {
      int i = Build.VERSION.SDK_INT;
      byte b1 = 1;
      byte b2 = b1;
      if (i >= 21) {
        b2 = b1;
        if (this.mPowerManager.isPowerSaveMode())
          b2 = 2; 
      } 
      return b2;
    }
    
    public void onChange() {
      AppCompatDelegateImpl.this.applyDayNight();
    }
  }
  
  abstract class AutoNightModeManager {
    private BroadcastReceiver mReceiver;
    
    void cleanup() {
      if (this.mReceiver != null) {
        try {
          AppCompatDelegateImpl.this.mContext.unregisterReceiver(this.mReceiver);
        } catch (IllegalArgumentException illegalArgumentException) {}
        this.mReceiver = null;
      } 
    }
    
    abstract IntentFilter createIntentFilterForBroadcastReceiver();
    
    abstract int getApplyableNightMode();
    
    boolean isListening() {
      boolean bool;
      if (this.mReceiver != null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    abstract void onChange();
    
    void setup() {
      cleanup();
      IntentFilter intentFilter = createIntentFilterForBroadcastReceiver();
      if (intentFilter != null && intentFilter.countActions() != 0) {
        if (this.mReceiver == null)
          this.mReceiver = new BroadcastReceiver() {
              public void onReceive(Context param2Context, Intent param2Intent) {
                AppCompatDelegateImpl.AutoNightModeManager.this.onChange();
              }
            }; 
        AppCompatDelegateImpl.this.mContext.registerReceiver(this.mReceiver, intentFilter);
      } 
    }
  }
  
  class null extends BroadcastReceiver {
    public void onReceive(Context param1Context, Intent param1Intent) {
      this.this$1.onChange();
    }
  }
  
  private class AutoTimeNightModeManager extends AutoNightModeManager {
    private final TwilightManager mTwilightManager;
    
    AutoTimeNightModeManager(TwilightManager param1TwilightManager) {
      this.mTwilightManager = param1TwilightManager;
    }
    
    IntentFilter createIntentFilterForBroadcastReceiver() {
      IntentFilter intentFilter = new IntentFilter();
      intentFilter.addAction("android.intent.action.TIME_SET");
      intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
      intentFilter.addAction("android.intent.action.TIME_TICK");
      return intentFilter;
    }
    
    public int getApplyableNightMode() {
      boolean bool;
      if (this.mTwilightManager.isNight()) {
        bool = true;
      } else {
        bool = true;
      } 
      return bool;
    }
    
    public void onChange() {
      AppCompatDelegateImpl.this.applyDayNight();
    }
  }
  
  private class ListMenuDecorView extends ContentFrameLayout {
    public ListMenuDecorView(Context param1Context) {
      super(param1Context);
    }
    
    private boolean isOutOfBounds(int param1Int1, int param1Int2) {
      return (param1Int1 < -5 || param1Int2 < -5 || param1Int1 > getWidth() + 5 || param1Int2 > getHeight() + 5);
    }
    
    public boolean dispatchKeyEvent(KeyEvent param1KeyEvent) {
      return (AppCompatDelegateImpl.this.dispatchKeyEvent(param1KeyEvent) || super.dispatchKeyEvent(param1KeyEvent));
    }
    
    public boolean onInterceptTouchEvent(MotionEvent param1MotionEvent) {
      if (param1MotionEvent.getAction() == 0 && isOutOfBounds((int)param1MotionEvent.getX(), (int)param1MotionEvent.getY())) {
        AppCompatDelegateImpl.this.closePanel(0);
        return true;
      } 
      return super.onInterceptTouchEvent(param1MotionEvent);
    }
    
    public void setBackgroundResource(int param1Int) {
      setBackgroundDrawable(AppCompatResources.getDrawable(getContext(), param1Int));
    }
  }
  
  protected static final class PanelFeatureState {
    int background;
    
    View createdPanelView;
    
    ViewGroup decorView;
    
    int featureId;
    
    Bundle frozenActionViewState;
    
    Bundle frozenMenuState;
    
    int gravity;
    
    boolean isHandled;
    
    boolean isOpen;
    
    boolean isPrepared;
    
    ListMenuPresenter listMenuPresenter;
    
    Context listPresenterContext;
    
    MenuBuilder menu;
    
    public boolean qwertyMode;
    
    boolean refreshDecorView;
    
    boolean refreshMenuContent;
    
    View shownPanelView;
    
    boolean wasLastOpen;
    
    int windowAnimations;
    
    int x;
    
    int y;
    
    PanelFeatureState(int param1Int) {
      this.featureId = param1Int;
      this.refreshDecorView = false;
    }
    
    void applyFrozenState() {
      MenuBuilder menuBuilder = this.menu;
      if (menuBuilder != null) {
        Bundle bundle = this.frozenMenuState;
        if (bundle != null) {
          menuBuilder.restorePresenterStates(bundle);
          this.frozenMenuState = null;
        } 
      } 
    }
    
    public void clearMenuPresenters() {
      MenuBuilder menuBuilder = this.menu;
      if (menuBuilder != null)
        menuBuilder.removeMenuPresenter((MenuPresenter)this.listMenuPresenter); 
      this.listMenuPresenter = null;
    }
    
    MenuView getListMenuView(MenuPresenter.Callback param1Callback) {
      if (this.menu == null)
        return null; 
      if (this.listMenuPresenter == null) {
        this.listMenuPresenter = new ListMenuPresenter(this.listPresenterContext, R.layout.abc_list_menu_item_layout);
        this.listMenuPresenter.setCallback(param1Callback);
        this.menu.addMenuPresenter((MenuPresenter)this.listMenuPresenter);
      } 
      return this.listMenuPresenter.getMenuView(this.decorView);
    }
    
    public boolean hasPanelItems() {
      View view = this.shownPanelView;
      boolean bool = false;
      if (view == null)
        return false; 
      if (this.createdPanelView != null)
        return true; 
      if (this.listMenuPresenter.getAdapter().getCount() > 0)
        bool = true; 
      return bool;
    }
    
    void onRestoreInstanceState(Parcelable param1Parcelable) {
      param1Parcelable = param1Parcelable;
      this.featureId = ((SavedState)param1Parcelable).featureId;
      this.wasLastOpen = ((SavedState)param1Parcelable).isOpen;
      this.frozenMenuState = ((SavedState)param1Parcelable).menuState;
      this.shownPanelView = null;
      this.decorView = null;
    }
    
    Parcelable onSaveInstanceState() {
      SavedState savedState = new SavedState();
      savedState.featureId = this.featureId;
      savedState.isOpen = this.isOpen;
      if (this.menu != null) {
        savedState.menuState = new Bundle();
        this.menu.savePresenterStates(savedState.menuState);
      } 
      return savedState;
    }
    
    void setMenu(MenuBuilder param1MenuBuilder) {
      MenuBuilder menuBuilder = this.menu;
      if (param1MenuBuilder == menuBuilder)
        return; 
      if (menuBuilder != null)
        menuBuilder.removeMenuPresenter((MenuPresenter)this.listMenuPresenter); 
      this.menu = param1MenuBuilder;
      if (param1MenuBuilder != null) {
        ListMenuPresenter listMenuPresenter = this.listMenuPresenter;
        if (listMenuPresenter != null)
          param1MenuBuilder.addMenuPresenter((MenuPresenter)listMenuPresenter); 
      } 
    }
    
    void setStyle(Context param1Context) {
      TypedValue typedValue = new TypedValue();
      Resources.Theme theme = param1Context.getResources().newTheme();
      theme.setTo(param1Context.getTheme());
      theme.resolveAttribute(R.attr.actionBarPopupTheme, typedValue, true);
      if (typedValue.resourceId != 0)
        theme.applyStyle(typedValue.resourceId, true); 
      theme.resolveAttribute(R.attr.panelMenuListTheme, typedValue, true);
      if (typedValue.resourceId != 0) {
        theme.applyStyle(typedValue.resourceId, true);
      } else {
        theme.applyStyle(R.style.Theme_AppCompat_CompactMenu, true);
      } 
      ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(param1Context, 0);
      contextThemeWrapper.getTheme().setTo(theme);
      this.listPresenterContext = (Context)contextThemeWrapper;
      TypedArray typedArray = contextThemeWrapper.obtainStyledAttributes(R.styleable.AppCompatTheme);
      this.background = typedArray.getResourceId(R.styleable.AppCompatTheme_panelBackground, 0);
      this.windowAnimations = typedArray.getResourceId(R.styleable.AppCompatTheme_android_windowAnimationStyle, 0);
      typedArray.recycle();
    }
    
    private static class SavedState implements Parcelable {
      public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
          public AppCompatDelegateImpl.PanelFeatureState.SavedState createFromParcel(Parcel param3Parcel) {
            return AppCompatDelegateImpl.PanelFeatureState.SavedState.readFromParcel(param3Parcel, null);
          }
          
          public AppCompatDelegateImpl.PanelFeatureState.SavedState createFromParcel(Parcel param3Parcel, ClassLoader param3ClassLoader) {
            return AppCompatDelegateImpl.PanelFeatureState.SavedState.readFromParcel(param3Parcel, param3ClassLoader);
          }
          
          public AppCompatDelegateImpl.PanelFeatureState.SavedState[] newArray(int param3Int) {
            return new AppCompatDelegateImpl.PanelFeatureState.SavedState[param3Int];
          }
        };
      
      int featureId;
      
      boolean isOpen;
      
      Bundle menuState;
      
      static SavedState readFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
        SavedState savedState = new SavedState();
        savedState.featureId = param2Parcel.readInt();
        int i = param2Parcel.readInt();
        boolean bool = true;
        if (i != 1)
          bool = false; 
        savedState.isOpen = bool;
        if (savedState.isOpen)
          savedState.menuState = param2Parcel.readBundle(param2ClassLoader); 
        return savedState;
      }
      
      public int describeContents() {
        return 0;
      }
      
      public void writeToParcel(Parcel param2Parcel, int param2Int) {
        param2Parcel.writeInt(this.featureId);
        param2Parcel.writeInt(this.isOpen);
        if (this.isOpen)
          param2Parcel.writeBundle(this.menuState); 
      }
    }
    
    static final class null implements Parcelable.ClassLoaderCreator<SavedState> {
      public AppCompatDelegateImpl.PanelFeatureState.SavedState createFromParcel(Parcel param2Parcel) {
        return AppCompatDelegateImpl.PanelFeatureState.SavedState.readFromParcel(param2Parcel, null);
      }
      
      public AppCompatDelegateImpl.PanelFeatureState.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
        return AppCompatDelegateImpl.PanelFeatureState.SavedState.readFromParcel(param2Parcel, param2ClassLoader);
      }
      
      public AppCompatDelegateImpl.PanelFeatureState.SavedState[] newArray(int param2Int) {
        return new AppCompatDelegateImpl.PanelFeatureState.SavedState[param2Int];
      }
    }
  }
  
  private static class SavedState implements Parcelable {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public AppCompatDelegateImpl.PanelFeatureState.SavedState createFromParcel(Parcel param3Parcel) {
          return AppCompatDelegateImpl.PanelFeatureState.SavedState.readFromParcel(param3Parcel, null);
        }
        
        public AppCompatDelegateImpl.PanelFeatureState.SavedState createFromParcel(Parcel param3Parcel, ClassLoader param3ClassLoader) {
          return AppCompatDelegateImpl.PanelFeatureState.SavedState.readFromParcel(param3Parcel, param3ClassLoader);
        }
        
        public AppCompatDelegateImpl.PanelFeatureState.SavedState[] newArray(int param3Int) {
          return new AppCompatDelegateImpl.PanelFeatureState.SavedState[param3Int];
        }
      };
    
    int featureId;
    
    boolean isOpen;
    
    Bundle menuState;
    
    static SavedState readFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      SavedState savedState = new SavedState();
      savedState.featureId = param1Parcel.readInt();
      int i = param1Parcel.readInt();
      boolean bool = true;
      if (i != 1)
        bool = false; 
      savedState.isOpen = bool;
      if (savedState.isOpen)
        savedState.menuState = param1Parcel.readBundle(param1ClassLoader); 
      return savedState;
    }
    
    public int describeContents() {
      return 0;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      param1Parcel.writeInt(this.featureId);
      param1Parcel.writeInt(this.isOpen);
      if (this.isOpen)
        param1Parcel.writeBundle(this.menuState); 
    }
  }
  
  static final class null implements Parcelable.ClassLoaderCreator<PanelFeatureState.SavedState> {
    public AppCompatDelegateImpl.PanelFeatureState.SavedState createFromParcel(Parcel param1Parcel) {
      return AppCompatDelegateImpl.PanelFeatureState.SavedState.readFromParcel(param1Parcel, null);
    }
    
    public AppCompatDelegateImpl.PanelFeatureState.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return AppCompatDelegateImpl.PanelFeatureState.SavedState.readFromParcel(param1Parcel, param1ClassLoader);
    }
    
    public AppCompatDelegateImpl.PanelFeatureState.SavedState[] newArray(int param1Int) {
      return new AppCompatDelegateImpl.PanelFeatureState.SavedState[param1Int];
    }
  }
  
  private final class PanelMenuPresenterCallback implements MenuPresenter.Callback {
    public void onCloseMenu(MenuBuilder param1MenuBuilder, boolean param1Boolean) {
      boolean bool;
      MenuBuilder menuBuilder = param1MenuBuilder.getRootMenu();
      if (menuBuilder != param1MenuBuilder) {
        bool = true;
      } else {
        bool = false;
      } 
      AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
      if (bool)
        param1MenuBuilder = menuBuilder; 
      AppCompatDelegateImpl.PanelFeatureState panelFeatureState = appCompatDelegateImpl.findMenuPanel((Menu)param1MenuBuilder);
      if (panelFeatureState != null)
        if (bool) {
          AppCompatDelegateImpl.this.callOnPanelClosed(panelFeatureState.featureId, panelFeatureState, (Menu)menuBuilder);
          AppCompatDelegateImpl.this.closePanel(panelFeatureState, true);
        } else {
          AppCompatDelegateImpl.this.closePanel(panelFeatureState, param1Boolean);
        }  
    }
    
    public boolean onOpenSubMenu(MenuBuilder param1MenuBuilder) {
      if (param1MenuBuilder == null && AppCompatDelegateImpl.this.mHasActionBar) {
        Window.Callback callback = AppCompatDelegateImpl.this.getWindowCallback();
        if (callback != null && !AppCompatDelegateImpl.this.mIsDestroyed)
          callback.onMenuOpened(108, (Menu)param1MenuBuilder); 
      } 
      return true;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/appcompat/app/AppCompatDelegateImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
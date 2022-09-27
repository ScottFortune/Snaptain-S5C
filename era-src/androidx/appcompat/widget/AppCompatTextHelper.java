package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.AutoSizeableTextView;
import java.lang.ref.WeakReference;

class AppCompatTextHelper {
  private static final int MONOSPACE = 3;
  
  private static final int SANS = 1;
  
  private static final int SERIF = 2;
  
  private static final int TEXT_FONT_WEIGHT_UNSPECIFIED = -1;
  
  private boolean mAsyncFontPending;
  
  private final AppCompatTextViewAutoSizeHelper mAutoSizeTextHelper;
  
  private TintInfo mDrawableBottomTint;
  
  private TintInfo mDrawableEndTint;
  
  private TintInfo mDrawableLeftTint;
  
  private TintInfo mDrawableRightTint;
  
  private TintInfo mDrawableStartTint;
  
  private TintInfo mDrawableTint;
  
  private TintInfo mDrawableTopTint;
  
  private Typeface mFontTypeface;
  
  private int mFontWeight = -1;
  
  private int mStyle = 0;
  
  private final TextView mView;
  
  AppCompatTextHelper(TextView paramTextView) {
    this.mView = paramTextView;
    this.mAutoSizeTextHelper = new AppCompatTextViewAutoSizeHelper(this.mView);
  }
  
  private void applyCompoundDrawableTint(Drawable paramDrawable, TintInfo paramTintInfo) {
    if (paramDrawable != null && paramTintInfo != null)
      AppCompatDrawableManager.tintDrawable(paramDrawable, paramTintInfo, this.mView.getDrawableState()); 
  }
  
  private static TintInfo createTintInfo(Context paramContext, AppCompatDrawableManager paramAppCompatDrawableManager, int paramInt) {
    ColorStateList colorStateList = paramAppCompatDrawableManager.getTintList(paramContext, paramInt);
    if (colorStateList != null) {
      TintInfo tintInfo = new TintInfo();
      tintInfo.mHasTintList = true;
      tintInfo.mTintList = colorStateList;
      return tintInfo;
    } 
    return null;
  }
  
  private void setCompoundDrawables(Drawable paramDrawable1, Drawable paramDrawable2, Drawable paramDrawable3, Drawable paramDrawable4, Drawable paramDrawable5, Drawable paramDrawable6) {
    TextView textView;
    Drawable[] arrayOfDrawable;
    if (Build.VERSION.SDK_INT >= 17 && (paramDrawable5 != null || paramDrawable6 != null)) {
      arrayOfDrawable = this.mView.getCompoundDrawablesRelative();
      textView = this.mView;
      if (paramDrawable5 == null)
        paramDrawable5 = arrayOfDrawable[0]; 
      if (paramDrawable2 == null)
        paramDrawable2 = arrayOfDrawable[1]; 
      if (paramDrawable6 == null)
        paramDrawable6 = arrayOfDrawable[2]; 
      if (paramDrawable4 == null)
        paramDrawable4 = arrayOfDrawable[3]; 
      textView.setCompoundDrawablesRelativeWithIntrinsicBounds(paramDrawable5, paramDrawable2, paramDrawable6, paramDrawable4);
    } else if (textView != null || paramDrawable2 != null || arrayOfDrawable != null || paramDrawable4 != null) {
      Drawable drawable1;
      Drawable drawable2;
      if (Build.VERSION.SDK_INT >= 17) {
        Drawable[] arrayOfDrawable2 = this.mView.getCompoundDrawablesRelative();
        if (arrayOfDrawable2[0] != null || arrayOfDrawable2[2] != null) {
          textView = this.mView;
          drawable2 = arrayOfDrawable2[0];
          if (paramDrawable2 == null)
            paramDrawable2 = arrayOfDrawable2[1]; 
          paramDrawable6 = arrayOfDrawable2[2];
          if (paramDrawable4 == null)
            paramDrawable4 = arrayOfDrawable2[3]; 
          textView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable2, paramDrawable2, paramDrawable6, paramDrawable4);
          return;
        } 
      } 
      Drawable[] arrayOfDrawable1 = this.mView.getCompoundDrawables();
      TextView textView1 = this.mView;
      if (textView == null)
        drawable1 = arrayOfDrawable1[0]; 
      if (paramDrawable2 == null)
        paramDrawable2 = arrayOfDrawable1[1]; 
      if (drawable2 == null)
        drawable2 = arrayOfDrawable1[2]; 
      if (paramDrawable4 == null)
        paramDrawable4 = arrayOfDrawable1[3]; 
      textView1.setCompoundDrawablesWithIntrinsicBounds(drawable1, paramDrawable2, drawable2, paramDrawable4);
    } 
  }
  
  private void setCompoundTints() {
    TintInfo tintInfo = this.mDrawableTint;
    this.mDrawableLeftTint = tintInfo;
    this.mDrawableTopTint = tintInfo;
    this.mDrawableRightTint = tintInfo;
    this.mDrawableBottomTint = tintInfo;
    this.mDrawableStartTint = tintInfo;
    this.mDrawableEndTint = tintInfo;
  }
  
  private void setTextSizeInternal(int paramInt, float paramFloat) {
    this.mAutoSizeTextHelper.setTextSizeInternal(paramInt, paramFloat);
  }
  
  private void updateTypefaceAndStyle(Context paramContext, TintTypedArray paramTintTypedArray) {
    this.mStyle = paramTintTypedArray.getInt(R.styleable.TextAppearance_android_textStyle, this.mStyle);
    int i = Build.VERSION.SDK_INT;
    boolean bool = false;
    if (i >= 28) {
      this.mFontWeight = paramTintTypedArray.getInt(R.styleable.TextAppearance_android_textFontWeight, -1);
      if (this.mFontWeight != -1)
        this.mStyle = this.mStyle & 0x2 | 0x0; 
    } 
    if (paramTintTypedArray.hasValue(R.styleable.TextAppearance_android_fontFamily) || paramTintTypedArray.hasValue(R.styleable.TextAppearance_fontFamily)) {
      this.mFontTypeface = null;
      if (paramTintTypedArray.hasValue(R.styleable.TextAppearance_fontFamily)) {
        i = R.styleable.TextAppearance_fontFamily;
      } else {
        i = R.styleable.TextAppearance_android_fontFamily;
      } 
      int j = this.mFontWeight;
      int k = this.mStyle;
      if (!paramContext.isRestricted()) {
        ApplyTextViewCallback applyTextViewCallback = new ApplyTextViewCallback(this, j, k);
        try {
          boolean bool1;
          Typeface typeface = paramTintTypedArray.getFont(i, this.mStyle, applyTextViewCallback);
          if (typeface != null)
            if (Build.VERSION.SDK_INT >= 28 && this.mFontWeight != -1) {
              typeface = Typeface.create(typeface, 0);
              k = this.mFontWeight;
              if ((this.mStyle & 0x2) != 0) {
                bool1 = true;
              } else {
                bool1 = false;
              } 
              this.mFontTypeface = Typeface.create(typeface, k, bool1);
            } else {
              this.mFontTypeface = typeface;
            }  
          if (this.mFontTypeface == null) {
            bool1 = true;
          } else {
            bool1 = false;
          } 
          this.mAsyncFontPending = bool1;
        } catch (UnsupportedOperationException|android.content.res.Resources.NotFoundException unsupportedOperationException) {}
      } 
      if (this.mFontTypeface == null) {
        String str = paramTintTypedArray.getString(i);
        if (str != null) {
          Typeface typeface;
          if (Build.VERSION.SDK_INT >= 28 && this.mFontWeight != -1) {
            typeface = Typeface.create(str, 0);
            i = this.mFontWeight;
            boolean bool1 = bool;
            if ((this.mStyle & 0x2) != 0)
              bool1 = true; 
            this.mFontTypeface = Typeface.create(typeface, i, bool1);
          } else {
            this.mFontTypeface = Typeface.create((String)typeface, this.mStyle);
          } 
        } 
      } 
      return;
    } 
    if (paramTintTypedArray.hasValue(R.styleable.TextAppearance_android_typeface)) {
      this.mAsyncFontPending = false;
      i = paramTintTypedArray.getInt(R.styleable.TextAppearance_android_typeface, 1);
      if (i != 1) {
        if (i != 2) {
          if (i == 3)
            this.mFontTypeface = Typeface.MONOSPACE; 
        } else {
          this.mFontTypeface = Typeface.SERIF;
        } 
      } else {
        this.mFontTypeface = Typeface.SANS_SERIF;
      } 
    } 
  }
  
  void applyCompoundDrawablesTints() {
    if (this.mDrawableLeftTint != null || this.mDrawableTopTint != null || this.mDrawableRightTint != null || this.mDrawableBottomTint != null) {
      Drawable[] arrayOfDrawable = this.mView.getCompoundDrawables();
      applyCompoundDrawableTint(arrayOfDrawable[0], this.mDrawableLeftTint);
      applyCompoundDrawableTint(arrayOfDrawable[1], this.mDrawableTopTint);
      applyCompoundDrawableTint(arrayOfDrawable[2], this.mDrawableRightTint);
      applyCompoundDrawableTint(arrayOfDrawable[3], this.mDrawableBottomTint);
    } 
    if (Build.VERSION.SDK_INT >= 17 && (this.mDrawableStartTint != null || this.mDrawableEndTint != null)) {
      Drawable[] arrayOfDrawable = this.mView.getCompoundDrawablesRelative();
      applyCompoundDrawableTint(arrayOfDrawable[0], this.mDrawableStartTint);
      applyCompoundDrawableTint(arrayOfDrawable[2], this.mDrawableEndTint);
    } 
  }
  
  void autoSizeText() {
    this.mAutoSizeTextHelper.autoSizeText();
  }
  
  int getAutoSizeMaxTextSize() {
    return this.mAutoSizeTextHelper.getAutoSizeMaxTextSize();
  }
  
  int getAutoSizeMinTextSize() {
    return this.mAutoSizeTextHelper.getAutoSizeMinTextSize();
  }
  
  int getAutoSizeStepGranularity() {
    return this.mAutoSizeTextHelper.getAutoSizeStepGranularity();
  }
  
  int[] getAutoSizeTextAvailableSizes() {
    return this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
  }
  
  int getAutoSizeTextType() {
    return this.mAutoSizeTextHelper.getAutoSizeTextType();
  }
  
  ColorStateList getCompoundDrawableTintList() {
    TintInfo tintInfo = this.mDrawableTint;
    if (tintInfo != null) {
      ColorStateList colorStateList = tintInfo.mTintList;
    } else {
      tintInfo = null;
    } 
    return (ColorStateList)tintInfo;
  }
  
  PorterDuff.Mode getCompoundDrawableTintMode() {
    TintInfo tintInfo = this.mDrawableTint;
    if (tintInfo != null) {
      PorterDuff.Mode mode = tintInfo.mTintMode;
    } else {
      tintInfo = null;
    } 
    return (PorterDuff.Mode)tintInfo;
  }
  
  boolean isAutoSizeEnabled() {
    return this.mAutoSizeTextHelper.isAutoSizeEnabled();
  }
  
  void loadFromAttributes(AttributeSet paramAttributeSet, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mView : Landroid/widget/TextView;
    //   4: invokevirtual getContext : ()Landroid/content/Context;
    //   7: astore_3
    //   8: invokestatic get : ()Landroidx/appcompat/widget/AppCompatDrawableManager;
    //   11: astore #4
    //   13: aload_3
    //   14: aload_1
    //   15: getstatic androidx/appcompat/R$styleable.AppCompatTextHelper : [I
    //   18: iload_2
    //   19: iconst_0
    //   20: invokestatic obtainStyledAttributes : (Landroid/content/Context;Landroid/util/AttributeSet;[III)Landroidx/appcompat/widget/TintTypedArray;
    //   23: astore #5
    //   25: aload #5
    //   27: getstatic androidx/appcompat/R$styleable.AppCompatTextHelper_android_textAppearance : I
    //   30: iconst_m1
    //   31: invokevirtual getResourceId : (II)I
    //   34: istore #6
    //   36: aload #5
    //   38: getstatic androidx/appcompat/R$styleable.AppCompatTextHelper_android_drawableLeft : I
    //   41: invokevirtual hasValue : (I)Z
    //   44: ifeq -> 66
    //   47: aload_0
    //   48: aload_3
    //   49: aload #4
    //   51: aload #5
    //   53: getstatic androidx/appcompat/R$styleable.AppCompatTextHelper_android_drawableLeft : I
    //   56: iconst_0
    //   57: invokevirtual getResourceId : (II)I
    //   60: invokestatic createTintInfo : (Landroid/content/Context;Landroidx/appcompat/widget/AppCompatDrawableManager;I)Landroidx/appcompat/widget/TintInfo;
    //   63: putfield mDrawableLeftTint : Landroidx/appcompat/widget/TintInfo;
    //   66: aload #5
    //   68: getstatic androidx/appcompat/R$styleable.AppCompatTextHelper_android_drawableTop : I
    //   71: invokevirtual hasValue : (I)Z
    //   74: ifeq -> 96
    //   77: aload_0
    //   78: aload_3
    //   79: aload #4
    //   81: aload #5
    //   83: getstatic androidx/appcompat/R$styleable.AppCompatTextHelper_android_drawableTop : I
    //   86: iconst_0
    //   87: invokevirtual getResourceId : (II)I
    //   90: invokestatic createTintInfo : (Landroid/content/Context;Landroidx/appcompat/widget/AppCompatDrawableManager;I)Landroidx/appcompat/widget/TintInfo;
    //   93: putfield mDrawableTopTint : Landroidx/appcompat/widget/TintInfo;
    //   96: aload #5
    //   98: getstatic androidx/appcompat/R$styleable.AppCompatTextHelper_android_drawableRight : I
    //   101: invokevirtual hasValue : (I)Z
    //   104: ifeq -> 126
    //   107: aload_0
    //   108: aload_3
    //   109: aload #4
    //   111: aload #5
    //   113: getstatic androidx/appcompat/R$styleable.AppCompatTextHelper_android_drawableRight : I
    //   116: iconst_0
    //   117: invokevirtual getResourceId : (II)I
    //   120: invokestatic createTintInfo : (Landroid/content/Context;Landroidx/appcompat/widget/AppCompatDrawableManager;I)Landroidx/appcompat/widget/TintInfo;
    //   123: putfield mDrawableRightTint : Landroidx/appcompat/widget/TintInfo;
    //   126: aload #5
    //   128: getstatic androidx/appcompat/R$styleable.AppCompatTextHelper_android_drawableBottom : I
    //   131: invokevirtual hasValue : (I)Z
    //   134: ifeq -> 156
    //   137: aload_0
    //   138: aload_3
    //   139: aload #4
    //   141: aload #5
    //   143: getstatic androidx/appcompat/R$styleable.AppCompatTextHelper_android_drawableBottom : I
    //   146: iconst_0
    //   147: invokevirtual getResourceId : (II)I
    //   150: invokestatic createTintInfo : (Landroid/content/Context;Landroidx/appcompat/widget/AppCompatDrawableManager;I)Landroidx/appcompat/widget/TintInfo;
    //   153: putfield mDrawableBottomTint : Landroidx/appcompat/widget/TintInfo;
    //   156: getstatic android/os/Build$VERSION.SDK_INT : I
    //   159: bipush #17
    //   161: if_icmplt -> 224
    //   164: aload #5
    //   166: getstatic androidx/appcompat/R$styleable.AppCompatTextHelper_android_drawableStart : I
    //   169: invokevirtual hasValue : (I)Z
    //   172: ifeq -> 194
    //   175: aload_0
    //   176: aload_3
    //   177: aload #4
    //   179: aload #5
    //   181: getstatic androidx/appcompat/R$styleable.AppCompatTextHelper_android_drawableStart : I
    //   184: iconst_0
    //   185: invokevirtual getResourceId : (II)I
    //   188: invokestatic createTintInfo : (Landroid/content/Context;Landroidx/appcompat/widget/AppCompatDrawableManager;I)Landroidx/appcompat/widget/TintInfo;
    //   191: putfield mDrawableStartTint : Landroidx/appcompat/widget/TintInfo;
    //   194: aload #5
    //   196: getstatic androidx/appcompat/R$styleable.AppCompatTextHelper_android_drawableEnd : I
    //   199: invokevirtual hasValue : (I)Z
    //   202: ifeq -> 224
    //   205: aload_0
    //   206: aload_3
    //   207: aload #4
    //   209: aload #5
    //   211: getstatic androidx/appcompat/R$styleable.AppCompatTextHelper_android_drawableEnd : I
    //   214: iconst_0
    //   215: invokevirtual getResourceId : (II)I
    //   218: invokestatic createTintInfo : (Landroid/content/Context;Landroidx/appcompat/widget/AppCompatDrawableManager;I)Landroidx/appcompat/widget/TintInfo;
    //   221: putfield mDrawableEndTint : Landroidx/appcompat/widget/TintInfo;
    //   224: aload #5
    //   226: invokevirtual recycle : ()V
    //   229: aload_0
    //   230: getfield mView : Landroid/widget/TextView;
    //   233: invokevirtual getTransformationMethod : ()Landroid/text/method/TransformationMethod;
    //   236: instanceof android/text/method/PasswordTransformationMethod
    //   239: istore #7
    //   241: iload #6
    //   243: iconst_m1
    //   244: if_icmpeq -> 472
    //   247: aload_3
    //   248: iload #6
    //   250: getstatic androidx/appcompat/R$styleable.TextAppearance : [I
    //   253: invokestatic obtainStyledAttributes : (Landroid/content/Context;I[I)Landroidx/appcompat/widget/TintTypedArray;
    //   256: astore #8
    //   258: iload #7
    //   260: ifne -> 291
    //   263: aload #8
    //   265: getstatic androidx/appcompat/R$styleable.TextAppearance_textAllCaps : I
    //   268: invokevirtual hasValue : (I)Z
    //   271: ifeq -> 291
    //   274: aload #8
    //   276: getstatic androidx/appcompat/R$styleable.TextAppearance_textAllCaps : I
    //   279: iconst_0
    //   280: invokevirtual getBoolean : (IZ)Z
    //   283: istore #9
    //   285: iconst_1
    //   286: istore #6
    //   288: goto -> 297
    //   291: iconst_0
    //   292: istore #6
    //   294: iconst_0
    //   295: istore #9
    //   297: aload_0
    //   298: aload_3
    //   299: aload #8
    //   301: invokespecial updateTypefaceAndStyle : (Landroid/content/Context;Landroidx/appcompat/widget/TintTypedArray;)V
    //   304: getstatic android/os/Build$VERSION.SDK_INT : I
    //   307: bipush #23
    //   309: if_icmpge -> 393
    //   312: aload #8
    //   314: getstatic androidx/appcompat/R$styleable.TextAppearance_android_textColor : I
    //   317: invokevirtual hasValue : (I)Z
    //   320: ifeq -> 336
    //   323: aload #8
    //   325: getstatic androidx/appcompat/R$styleable.TextAppearance_android_textColor : I
    //   328: invokevirtual getColorStateList : (I)Landroid/content/res/ColorStateList;
    //   331: astore #10
    //   333: goto -> 339
    //   336: aconst_null
    //   337: astore #10
    //   339: aload #8
    //   341: getstatic androidx/appcompat/R$styleable.TextAppearance_android_textColorHint : I
    //   344: invokevirtual hasValue : (I)Z
    //   347: ifeq -> 363
    //   350: aload #8
    //   352: getstatic androidx/appcompat/R$styleable.TextAppearance_android_textColorHint : I
    //   355: invokevirtual getColorStateList : (I)Landroid/content/res/ColorStateList;
    //   358: astore #5
    //   360: goto -> 366
    //   363: aconst_null
    //   364: astore #5
    //   366: aload #8
    //   368: getstatic androidx/appcompat/R$styleable.TextAppearance_android_textColorLink : I
    //   371: invokevirtual hasValue : (I)Z
    //   374: ifeq -> 390
    //   377: aload #8
    //   379: getstatic androidx/appcompat/R$styleable.TextAppearance_android_textColorLink : I
    //   382: invokevirtual getColorStateList : (I)Landroid/content/res/ColorStateList;
    //   385: astore #11
    //   387: goto -> 402
    //   390: goto -> 399
    //   393: aconst_null
    //   394: astore #5
    //   396: aconst_null
    //   397: astore #10
    //   399: aconst_null
    //   400: astore #11
    //   402: aload #8
    //   404: getstatic androidx/appcompat/R$styleable.TextAppearance_textLocale : I
    //   407: invokevirtual hasValue : (I)Z
    //   410: ifeq -> 426
    //   413: aload #8
    //   415: getstatic androidx/appcompat/R$styleable.TextAppearance_textLocale : I
    //   418: invokevirtual getString : (I)Ljava/lang/String;
    //   421: astore #12
    //   423: goto -> 429
    //   426: aconst_null
    //   427: astore #12
    //   429: getstatic android/os/Build$VERSION.SDK_INT : I
    //   432: bipush #26
    //   434: if_icmplt -> 461
    //   437: aload #8
    //   439: getstatic androidx/appcompat/R$styleable.TextAppearance_fontVariationSettings : I
    //   442: invokevirtual hasValue : (I)Z
    //   445: ifeq -> 461
    //   448: aload #8
    //   450: getstatic androidx/appcompat/R$styleable.TextAppearance_fontVariationSettings : I
    //   453: invokevirtual getString : (I)Ljava/lang/String;
    //   456: astore #13
    //   458: goto -> 464
    //   461: aconst_null
    //   462: astore #13
    //   464: aload #8
    //   466: invokevirtual recycle : ()V
    //   469: goto -> 493
    //   472: aconst_null
    //   473: astore #12
    //   475: aconst_null
    //   476: astore #5
    //   478: aconst_null
    //   479: astore #13
    //   481: aconst_null
    //   482: astore #10
    //   484: iconst_0
    //   485: istore #6
    //   487: iconst_0
    //   488: istore #9
    //   490: aconst_null
    //   491: astore #11
    //   493: aload_3
    //   494: aload_1
    //   495: getstatic androidx/appcompat/R$styleable.TextAppearance : [I
    //   498: iload_2
    //   499: iconst_0
    //   500: invokestatic obtainStyledAttributes : (Landroid/content/Context;Landroid/util/AttributeSet;[III)Landroidx/appcompat/widget/TintTypedArray;
    //   503: astore #14
    //   505: iload #6
    //   507: istore #15
    //   509: iload #9
    //   511: istore #16
    //   513: iload #7
    //   515: ifne -> 551
    //   518: iload #6
    //   520: istore #15
    //   522: iload #9
    //   524: istore #16
    //   526: aload #14
    //   528: getstatic androidx/appcompat/R$styleable.TextAppearance_textAllCaps : I
    //   531: invokevirtual hasValue : (I)Z
    //   534: ifeq -> 551
    //   537: aload #14
    //   539: getstatic androidx/appcompat/R$styleable.TextAppearance_textAllCaps : I
    //   542: iconst_0
    //   543: invokevirtual getBoolean : (IZ)Z
    //   546: istore #16
    //   548: iconst_1
    //   549: istore #15
    //   551: aload #5
    //   553: astore #17
    //   555: aload #10
    //   557: astore #8
    //   559: aload #11
    //   561: astore #18
    //   563: getstatic android/os/Build$VERSION.SDK_INT : I
    //   566: bipush #23
    //   568: if_icmpge -> 654
    //   571: aload #14
    //   573: getstatic androidx/appcompat/R$styleable.TextAppearance_android_textColor : I
    //   576: invokevirtual hasValue : (I)Z
    //   579: ifeq -> 592
    //   582: aload #14
    //   584: getstatic androidx/appcompat/R$styleable.TextAppearance_android_textColor : I
    //   587: invokevirtual getColorStateList : (I)Landroid/content/res/ColorStateList;
    //   590: astore #10
    //   592: aload #14
    //   594: getstatic androidx/appcompat/R$styleable.TextAppearance_android_textColorHint : I
    //   597: invokevirtual hasValue : (I)Z
    //   600: ifeq -> 613
    //   603: aload #14
    //   605: getstatic androidx/appcompat/R$styleable.TextAppearance_android_textColorHint : I
    //   608: invokevirtual getColorStateList : (I)Landroid/content/res/ColorStateList;
    //   611: astore #5
    //   613: aload #5
    //   615: astore #17
    //   617: aload #10
    //   619: astore #8
    //   621: aload #11
    //   623: astore #18
    //   625: aload #14
    //   627: getstatic androidx/appcompat/R$styleable.TextAppearance_android_textColorLink : I
    //   630: invokevirtual hasValue : (I)Z
    //   633: ifeq -> 654
    //   636: aload #14
    //   638: getstatic androidx/appcompat/R$styleable.TextAppearance_android_textColorLink : I
    //   641: invokevirtual getColorStateList : (I)Landroid/content/res/ColorStateList;
    //   644: astore #18
    //   646: aload #10
    //   648: astore #8
    //   650: aload #5
    //   652: astore #17
    //   654: aload #14
    //   656: getstatic androidx/appcompat/R$styleable.TextAppearance_textLocale : I
    //   659: invokevirtual hasValue : (I)Z
    //   662: ifeq -> 675
    //   665: aload #14
    //   667: getstatic androidx/appcompat/R$styleable.TextAppearance_textLocale : I
    //   670: invokevirtual getString : (I)Ljava/lang/String;
    //   673: astore #12
    //   675: aload #13
    //   677: astore #5
    //   679: getstatic android/os/Build$VERSION.SDK_INT : I
    //   682: bipush #26
    //   684: if_icmplt -> 712
    //   687: aload #13
    //   689: astore #5
    //   691: aload #14
    //   693: getstatic androidx/appcompat/R$styleable.TextAppearance_fontVariationSettings : I
    //   696: invokevirtual hasValue : (I)Z
    //   699: ifeq -> 712
    //   702: aload #14
    //   704: getstatic androidx/appcompat/R$styleable.TextAppearance_fontVariationSettings : I
    //   707: invokevirtual getString : (I)Ljava/lang/String;
    //   710: astore #5
    //   712: getstatic android/os/Build$VERSION.SDK_INT : I
    //   715: bipush #28
    //   717: if_icmplt -> 755
    //   720: aload #14
    //   722: getstatic androidx/appcompat/R$styleable.TextAppearance_android_textSize : I
    //   725: invokevirtual hasValue : (I)Z
    //   728: ifeq -> 755
    //   731: aload #14
    //   733: getstatic androidx/appcompat/R$styleable.TextAppearance_android_textSize : I
    //   736: iconst_m1
    //   737: invokevirtual getDimensionPixelSize : (II)I
    //   740: ifne -> 755
    //   743: aload_0
    //   744: getfield mView : Landroid/widget/TextView;
    //   747: iconst_0
    //   748: fconst_0
    //   749: invokevirtual setTextSize : (IF)V
    //   752: goto -> 755
    //   755: aload_0
    //   756: aload_3
    //   757: aload #14
    //   759: invokespecial updateTypefaceAndStyle : (Landroid/content/Context;Landroidx/appcompat/widget/TintTypedArray;)V
    //   762: aload #14
    //   764: invokevirtual recycle : ()V
    //   767: aload #8
    //   769: ifnull -> 781
    //   772: aload_0
    //   773: getfield mView : Landroid/widget/TextView;
    //   776: aload #8
    //   778: invokevirtual setTextColor : (Landroid/content/res/ColorStateList;)V
    //   781: aload #17
    //   783: ifnull -> 795
    //   786: aload_0
    //   787: getfield mView : Landroid/widget/TextView;
    //   790: aload #17
    //   792: invokevirtual setHintTextColor : (Landroid/content/res/ColorStateList;)V
    //   795: aload #18
    //   797: ifnull -> 809
    //   800: aload_0
    //   801: getfield mView : Landroid/widget/TextView;
    //   804: aload #18
    //   806: invokevirtual setLinkTextColor : (Landroid/content/res/ColorStateList;)V
    //   809: iload #7
    //   811: ifne -> 825
    //   814: iload #15
    //   816: ifeq -> 825
    //   819: aload_0
    //   820: iload #16
    //   822: invokevirtual setAllCaps : (Z)V
    //   825: aload_0
    //   826: getfield mFontTypeface : Landroid/graphics/Typeface;
    //   829: astore #10
    //   831: aload #10
    //   833: ifnull -> 869
    //   836: aload_0
    //   837: getfield mFontWeight : I
    //   840: iconst_m1
    //   841: if_icmpne -> 860
    //   844: aload_0
    //   845: getfield mView : Landroid/widget/TextView;
    //   848: aload #10
    //   850: aload_0
    //   851: getfield mStyle : I
    //   854: invokevirtual setTypeface : (Landroid/graphics/Typeface;I)V
    //   857: goto -> 869
    //   860: aload_0
    //   861: getfield mView : Landroid/widget/TextView;
    //   864: aload #10
    //   866: invokevirtual setTypeface : (Landroid/graphics/Typeface;)V
    //   869: aload #5
    //   871: ifnull -> 884
    //   874: aload_0
    //   875: getfield mView : Landroid/widget/TextView;
    //   878: aload #5
    //   880: invokevirtual setFontVariationSettings : (Ljava/lang/String;)Z
    //   883: pop
    //   884: aload #12
    //   886: ifnull -> 947
    //   889: getstatic android/os/Build$VERSION.SDK_INT : I
    //   892: bipush #24
    //   894: if_icmplt -> 912
    //   897: aload_0
    //   898: getfield mView : Landroid/widget/TextView;
    //   901: aload #12
    //   903: invokestatic forLanguageTags : (Ljava/lang/String;)Landroid/os/LocaleList;
    //   906: invokevirtual setTextLocales : (Landroid/os/LocaleList;)V
    //   909: goto -> 947
    //   912: getstatic android/os/Build$VERSION.SDK_INT : I
    //   915: bipush #21
    //   917: if_icmplt -> 947
    //   920: aload #12
    //   922: iconst_0
    //   923: aload #12
    //   925: bipush #44
    //   927: invokevirtual indexOf : (I)I
    //   930: invokevirtual substring : (II)Ljava/lang/String;
    //   933: astore #5
    //   935: aload_0
    //   936: getfield mView : Landroid/widget/TextView;
    //   939: aload #5
    //   941: invokestatic forLanguageTag : (Ljava/lang/String;)Ljava/util/Locale;
    //   944: invokevirtual setTextLocale : (Ljava/util/Locale;)V
    //   947: aload_0
    //   948: getfield mAutoSizeTextHelper : Landroidx/appcompat/widget/AppCompatTextViewAutoSizeHelper;
    //   951: aload_1
    //   952: iload_2
    //   953: invokevirtual loadFromAttributes : (Landroid/util/AttributeSet;I)V
    //   956: getstatic androidx/core/widget/AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE : Z
    //   959: ifeq -> 1044
    //   962: aload_0
    //   963: getfield mAutoSizeTextHelper : Landroidx/appcompat/widget/AppCompatTextViewAutoSizeHelper;
    //   966: invokevirtual getAutoSizeTextType : ()I
    //   969: ifeq -> 1044
    //   972: aload_0
    //   973: getfield mAutoSizeTextHelper : Landroidx/appcompat/widget/AppCompatTextViewAutoSizeHelper;
    //   976: invokevirtual getAutoSizeTextAvailableSizes : ()[I
    //   979: astore #5
    //   981: aload #5
    //   983: arraylength
    //   984: ifle -> 1044
    //   987: aload_0
    //   988: getfield mView : Landroid/widget/TextView;
    //   991: invokevirtual getAutoSizeStepGranularity : ()I
    //   994: i2f
    //   995: ldc_w -1.0
    //   998: fcmpl
    //   999: ifeq -> 1034
    //   1002: aload_0
    //   1003: getfield mView : Landroid/widget/TextView;
    //   1006: aload_0
    //   1007: getfield mAutoSizeTextHelper : Landroidx/appcompat/widget/AppCompatTextViewAutoSizeHelper;
    //   1010: invokevirtual getAutoSizeMinTextSize : ()I
    //   1013: aload_0
    //   1014: getfield mAutoSizeTextHelper : Landroidx/appcompat/widget/AppCompatTextViewAutoSizeHelper;
    //   1017: invokevirtual getAutoSizeMaxTextSize : ()I
    //   1020: aload_0
    //   1021: getfield mAutoSizeTextHelper : Landroidx/appcompat/widget/AppCompatTextViewAutoSizeHelper;
    //   1024: invokevirtual getAutoSizeStepGranularity : ()I
    //   1027: iconst_0
    //   1028: invokevirtual setAutoSizeTextTypeUniformWithConfiguration : (IIII)V
    //   1031: goto -> 1044
    //   1034: aload_0
    //   1035: getfield mView : Landroid/widget/TextView;
    //   1038: aload #5
    //   1040: iconst_0
    //   1041: invokevirtual setAutoSizeTextTypeUniformWithPresetSizes : ([II)V
    //   1044: aload_3
    //   1045: aload_1
    //   1046: getstatic androidx/appcompat/R$styleable.AppCompatTextView : [I
    //   1049: invokestatic obtainStyledAttributes : (Landroid/content/Context;Landroid/util/AttributeSet;[I)Landroidx/appcompat/widget/TintTypedArray;
    //   1052: astore #8
    //   1054: aload #8
    //   1056: getstatic androidx/appcompat/R$styleable.AppCompatTextView_drawableLeftCompat : I
    //   1059: iconst_m1
    //   1060: invokevirtual getResourceId : (II)I
    //   1063: istore_2
    //   1064: iload_2
    //   1065: iconst_m1
    //   1066: if_icmpeq -> 1080
    //   1069: aload #4
    //   1071: aload_3
    //   1072: iload_2
    //   1073: invokevirtual getDrawable : (Landroid/content/Context;I)Landroid/graphics/drawable/Drawable;
    //   1076: astore_1
    //   1077: goto -> 1082
    //   1080: aconst_null
    //   1081: astore_1
    //   1082: aload #8
    //   1084: getstatic androidx/appcompat/R$styleable.AppCompatTextView_drawableTopCompat : I
    //   1087: iconst_m1
    //   1088: invokevirtual getResourceId : (II)I
    //   1091: istore_2
    //   1092: iload_2
    //   1093: iconst_m1
    //   1094: if_icmpeq -> 1109
    //   1097: aload #4
    //   1099: aload_3
    //   1100: iload_2
    //   1101: invokevirtual getDrawable : (Landroid/content/Context;I)Landroid/graphics/drawable/Drawable;
    //   1104: astore #5
    //   1106: goto -> 1112
    //   1109: aconst_null
    //   1110: astore #5
    //   1112: aload #8
    //   1114: getstatic androidx/appcompat/R$styleable.AppCompatTextView_drawableRightCompat : I
    //   1117: iconst_m1
    //   1118: invokevirtual getResourceId : (II)I
    //   1121: istore_2
    //   1122: iload_2
    //   1123: iconst_m1
    //   1124: if_icmpeq -> 1139
    //   1127: aload #4
    //   1129: aload_3
    //   1130: iload_2
    //   1131: invokevirtual getDrawable : (Landroid/content/Context;I)Landroid/graphics/drawable/Drawable;
    //   1134: astore #10
    //   1136: goto -> 1142
    //   1139: aconst_null
    //   1140: astore #10
    //   1142: aload #8
    //   1144: getstatic androidx/appcompat/R$styleable.AppCompatTextView_drawableBottomCompat : I
    //   1147: iconst_m1
    //   1148: invokevirtual getResourceId : (II)I
    //   1151: istore_2
    //   1152: iload_2
    //   1153: iconst_m1
    //   1154: if_icmpeq -> 1169
    //   1157: aload #4
    //   1159: aload_3
    //   1160: iload_2
    //   1161: invokevirtual getDrawable : (Landroid/content/Context;I)Landroid/graphics/drawable/Drawable;
    //   1164: astore #13
    //   1166: goto -> 1172
    //   1169: aconst_null
    //   1170: astore #13
    //   1172: aload #8
    //   1174: getstatic androidx/appcompat/R$styleable.AppCompatTextView_drawableStartCompat : I
    //   1177: iconst_m1
    //   1178: invokevirtual getResourceId : (II)I
    //   1181: istore_2
    //   1182: iload_2
    //   1183: iconst_m1
    //   1184: if_icmpeq -> 1199
    //   1187: aload #4
    //   1189: aload_3
    //   1190: iload_2
    //   1191: invokevirtual getDrawable : (Landroid/content/Context;I)Landroid/graphics/drawable/Drawable;
    //   1194: astore #12
    //   1196: goto -> 1202
    //   1199: aconst_null
    //   1200: astore #12
    //   1202: aload #8
    //   1204: getstatic androidx/appcompat/R$styleable.AppCompatTextView_drawableEndCompat : I
    //   1207: iconst_m1
    //   1208: invokevirtual getResourceId : (II)I
    //   1211: istore_2
    //   1212: iload_2
    //   1213: iconst_m1
    //   1214: if_icmpeq -> 1229
    //   1217: aload #4
    //   1219: aload_3
    //   1220: iload_2
    //   1221: invokevirtual getDrawable : (Landroid/content/Context;I)Landroid/graphics/drawable/Drawable;
    //   1224: astore #11
    //   1226: goto -> 1232
    //   1229: aconst_null
    //   1230: astore #11
    //   1232: aload_0
    //   1233: aload_1
    //   1234: aload #5
    //   1236: aload #10
    //   1238: aload #13
    //   1240: aload #12
    //   1242: aload #11
    //   1244: invokespecial setCompoundDrawables : (Landroid/graphics/drawable/Drawable;Landroid/graphics/drawable/Drawable;Landroid/graphics/drawable/Drawable;Landroid/graphics/drawable/Drawable;Landroid/graphics/drawable/Drawable;Landroid/graphics/drawable/Drawable;)V
    //   1247: aload #8
    //   1249: getstatic androidx/appcompat/R$styleable.AppCompatTextView_drawableTint : I
    //   1252: invokevirtual hasValue : (I)Z
    //   1255: ifeq -> 1275
    //   1258: aload #8
    //   1260: getstatic androidx/appcompat/R$styleable.AppCompatTextView_drawableTint : I
    //   1263: invokevirtual getColorStateList : (I)Landroid/content/res/ColorStateList;
    //   1266: astore_1
    //   1267: aload_0
    //   1268: getfield mView : Landroid/widget/TextView;
    //   1271: aload_1
    //   1272: invokestatic setCompoundDrawableTintList : (Landroid/widget/TextView;Landroid/content/res/ColorStateList;)V
    //   1275: aload #8
    //   1277: getstatic androidx/appcompat/R$styleable.AppCompatTextView_drawableTintMode : I
    //   1280: invokevirtual hasValue : (I)Z
    //   1283: ifeq -> 1311
    //   1286: aload #8
    //   1288: getstatic androidx/appcompat/R$styleable.AppCompatTextView_drawableTintMode : I
    //   1291: iconst_m1
    //   1292: invokevirtual getInt : (II)I
    //   1295: aconst_null
    //   1296: invokestatic parseTintMode : (ILandroid/graphics/PorterDuff$Mode;)Landroid/graphics/PorterDuff$Mode;
    //   1299: astore_1
    //   1300: aload_0
    //   1301: getfield mView : Landroid/widget/TextView;
    //   1304: aload_1
    //   1305: invokestatic setCompoundDrawableTintMode : (Landroid/widget/TextView;Landroid/graphics/PorterDuff$Mode;)V
    //   1308: goto -> 1311
    //   1311: aload #8
    //   1313: getstatic androidx/appcompat/R$styleable.AppCompatTextView_firstBaselineToTopHeight : I
    //   1316: iconst_m1
    //   1317: invokevirtual getDimensionPixelSize : (II)I
    //   1320: istore #6
    //   1322: aload #8
    //   1324: getstatic androidx/appcompat/R$styleable.AppCompatTextView_lastBaselineToBottomHeight : I
    //   1327: iconst_m1
    //   1328: invokevirtual getDimensionPixelSize : (II)I
    //   1331: istore #15
    //   1333: aload #8
    //   1335: getstatic androidx/appcompat/R$styleable.AppCompatTextView_lineHeight : I
    //   1338: iconst_m1
    //   1339: invokevirtual getDimensionPixelSize : (II)I
    //   1342: istore_2
    //   1343: aload #8
    //   1345: invokevirtual recycle : ()V
    //   1348: iload #6
    //   1350: iconst_m1
    //   1351: if_icmpeq -> 1363
    //   1354: aload_0
    //   1355: getfield mView : Landroid/widget/TextView;
    //   1358: iload #6
    //   1360: invokestatic setFirstBaselineToTopHeight : (Landroid/widget/TextView;I)V
    //   1363: iload #15
    //   1365: iconst_m1
    //   1366: if_icmpeq -> 1378
    //   1369: aload_0
    //   1370: getfield mView : Landroid/widget/TextView;
    //   1373: iload #15
    //   1375: invokestatic setLastBaselineToBottomHeight : (Landroid/widget/TextView;I)V
    //   1378: iload_2
    //   1379: iconst_m1
    //   1380: if_icmpeq -> 1391
    //   1383: aload_0
    //   1384: getfield mView : Landroid/widget/TextView;
    //   1387: iload_2
    //   1388: invokestatic setLineHeight : (Landroid/widget/TextView;I)V
    //   1391: return
  }
  
  void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (!AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE)
      autoSizeText(); 
  }
  
  void onSetCompoundDrawables() {
    applyCompoundDrawablesTints();
  }
  
  void onSetTextAppearance(Context paramContext, int paramInt) {
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramInt, R.styleable.TextAppearance);
    if (tintTypedArray.hasValue(R.styleable.TextAppearance_textAllCaps))
      setAllCaps(tintTypedArray.getBoolean(R.styleable.TextAppearance_textAllCaps, false)); 
    if (Build.VERSION.SDK_INT < 23 && tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColor)) {
      ColorStateList colorStateList = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColor);
      if (colorStateList != null)
        this.mView.setTextColor(colorStateList); 
    } 
    if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textSize) && tintTypedArray.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, -1) == 0)
      this.mView.setTextSize(0, 0.0F); 
    updateTypefaceAndStyle(paramContext, tintTypedArray);
    if (Build.VERSION.SDK_INT >= 26 && tintTypedArray.hasValue(R.styleable.TextAppearance_fontVariationSettings)) {
      String str = tintTypedArray.getString(R.styleable.TextAppearance_fontVariationSettings);
      if (str != null)
        this.mView.setFontVariationSettings(str); 
    } 
    tintTypedArray.recycle();
    Typeface typeface = this.mFontTypeface;
    if (typeface != null)
      this.mView.setTypeface(typeface, this.mStyle); 
  }
  
  public void runOnUiThread(Runnable paramRunnable) {
    this.mView.post(paramRunnable);
  }
  
  void setAllCaps(boolean paramBoolean) {
    this.mView.setAllCaps(paramBoolean);
  }
  
  void setAutoSizeTextTypeUniformWithConfiguration(int paramInt1, int paramInt2, int paramInt3, int paramInt4) throws IllegalArgumentException {
    this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithConfiguration(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  void setAutoSizeTextTypeUniformWithPresetSizes(int[] paramArrayOfint, int paramInt) throws IllegalArgumentException {
    this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(paramArrayOfint, paramInt);
  }
  
  void setAutoSizeTextTypeWithDefaults(int paramInt) {
    this.mAutoSizeTextHelper.setAutoSizeTextTypeWithDefaults(paramInt);
  }
  
  void setCompoundDrawableTintList(ColorStateList paramColorStateList) {
    boolean bool;
    if (this.mDrawableTint == null)
      this.mDrawableTint = new TintInfo(); 
    TintInfo tintInfo = this.mDrawableTint;
    tintInfo.mTintList = paramColorStateList;
    if (paramColorStateList != null) {
      bool = true;
    } else {
      bool = false;
    } 
    tintInfo.mHasTintList = bool;
    setCompoundTints();
  }
  
  void setCompoundDrawableTintMode(PorterDuff.Mode paramMode) {
    boolean bool;
    if (this.mDrawableTint == null)
      this.mDrawableTint = new TintInfo(); 
    TintInfo tintInfo = this.mDrawableTint;
    tintInfo.mTintMode = paramMode;
    if (paramMode != null) {
      bool = true;
    } else {
      bool = false;
    } 
    tintInfo.mHasTintMode = bool;
    setCompoundTints();
  }
  
  void setTextSize(int paramInt, float paramFloat) {
    if (!AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE && !isAutoSizeEnabled())
      setTextSizeInternal(paramInt, paramFloat); 
  }
  
  public void setTypefaceByCallback(Typeface paramTypeface) {
    if (this.mAsyncFontPending) {
      this.mView.setTypeface(paramTypeface);
      this.mFontTypeface = paramTypeface;
    } 
  }
  
  private static class ApplyTextViewCallback extends ResourcesCompat.FontCallback {
    private final int mFontWeight;
    
    private final WeakReference<AppCompatTextHelper> mParent;
    
    private final int mStyle;
    
    ApplyTextViewCallback(AppCompatTextHelper param1AppCompatTextHelper, int param1Int1, int param1Int2) {
      this.mParent = new WeakReference<AppCompatTextHelper>(param1AppCompatTextHelper);
      this.mFontWeight = param1Int1;
      this.mStyle = param1Int2;
    }
    
    public void onFontRetrievalFailed(int param1Int) {}
    
    public void onFontRetrieved(Typeface param1Typeface) {
      AppCompatTextHelper appCompatTextHelper = this.mParent.get();
      if (appCompatTextHelper == null)
        return; 
      Typeface typeface = param1Typeface;
      if (Build.VERSION.SDK_INT >= 28) {
        int i = this.mFontWeight;
        typeface = param1Typeface;
        if (i != -1) {
          boolean bool;
          if ((this.mStyle & 0x2) != 0) {
            bool = true;
          } else {
            bool = false;
          } 
          typeface = Typeface.create(param1Typeface, i, bool);
        } 
      } 
      appCompatTextHelper.runOnUiThread(new TypefaceApplyCallback(this.mParent, typeface));
    }
    
    private class TypefaceApplyCallback implements Runnable {
      private final WeakReference<AppCompatTextHelper> mParent;
      
      private final Typeface mTypeface;
      
      TypefaceApplyCallback(WeakReference<AppCompatTextHelper> param2WeakReference, Typeface param2Typeface) {
        this.mParent = param2WeakReference;
        this.mTypeface = param2Typeface;
      }
      
      public void run() {
        AppCompatTextHelper appCompatTextHelper = this.mParent.get();
        if (appCompatTextHelper == null)
          return; 
        appCompatTextHelper.setTypefaceByCallback(this.mTypeface);
      }
    }
  }
  
  private class TypefaceApplyCallback implements Runnable {
    private final WeakReference<AppCompatTextHelper> mParent;
    
    private final Typeface mTypeface;
    
    TypefaceApplyCallback(WeakReference<AppCompatTextHelper> param1WeakReference, Typeface param1Typeface) {
      this.mParent = param1WeakReference;
      this.mTypeface = param1Typeface;
    }
    
    public void run() {
      AppCompatTextHelper appCompatTextHelper = this.mParent.get();
      if (appCompatTextHelper == null)
        return; 
      appCompatTextHelper.setTypefaceByCallback(this.mTypeface);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/appcompat/widget/AppCompatTextHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.CancellationSignal;
import android.util.Log;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

class TypefaceCompatBaseImpl {
  private static final int INVALID_KEY = 0;
  
  private static final String TAG = "TypefaceCompatBaseImpl";
  
  private ConcurrentHashMap<Long, FontResourcesParserCompat.FontFamilyFilesResourceEntry> mFontFamilies = new ConcurrentHashMap<Long, FontResourcesParserCompat.FontFamilyFilesResourceEntry>();
  
  private void addFontFamily(Typeface paramTypeface, FontResourcesParserCompat.FontFamilyFilesResourceEntry paramFontFamilyFilesResourceEntry) {
    long l = getUniqueKey(paramTypeface);
    if (l != 0L)
      this.mFontFamilies.put(Long.valueOf(l), paramFontFamilyFilesResourceEntry); 
  }
  
  private FontResourcesParserCompat.FontFileResourceEntry findBestEntry(FontResourcesParserCompat.FontFamilyFilesResourceEntry paramFontFamilyFilesResourceEntry, int paramInt) {
    return findBestFont(paramFontFamilyFilesResourceEntry.getEntries(), paramInt, new StyleExtractor<FontResourcesParserCompat.FontFileResourceEntry>() {
          public int getWeight(FontResourcesParserCompat.FontFileResourceEntry param1FontFileResourceEntry) {
            return param1FontFileResourceEntry.getWeight();
          }
          
          public boolean isItalic(FontResourcesParserCompat.FontFileResourceEntry param1FontFileResourceEntry) {
            return param1FontFileResourceEntry.isItalic();
          }
        });
  }
  
  private static <T> T findBestFont(T[] paramArrayOfT, int paramInt, StyleExtractor<T> paramStyleExtractor) {
    // Byte code:
    //   0: iload_1
    //   1: iconst_1
    //   2: iand
    //   3: ifne -> 13
    //   6: sipush #400
    //   9: istore_3
    //   10: goto -> 17
    //   13: sipush #700
    //   16: istore_3
    //   17: iload_1
    //   18: iconst_2
    //   19: iand
    //   20: ifeq -> 29
    //   23: iconst_1
    //   24: istore #4
    //   26: goto -> 32
    //   29: iconst_0
    //   30: istore #4
    //   32: aload_0
    //   33: arraylength
    //   34: istore #5
    //   36: aconst_null
    //   37: astore #6
    //   39: iconst_0
    //   40: istore #7
    //   42: ldc 2147483647
    //   44: istore_1
    //   45: iload #7
    //   47: iload #5
    //   49: if_icmpge -> 135
    //   52: aload_0
    //   53: iload #7
    //   55: aaload
    //   56: astore #8
    //   58: aload_2
    //   59: aload #8
    //   61: invokeinterface getWeight : (Ljava/lang/Object;)I
    //   66: iload_3
    //   67: isub
    //   68: invokestatic abs : (I)I
    //   71: istore #9
    //   73: aload_2
    //   74: aload #8
    //   76: invokeinterface isItalic : (Ljava/lang/Object;)Z
    //   81: iload #4
    //   83: if_icmpne -> 92
    //   86: iconst_0
    //   87: istore #10
    //   89: goto -> 95
    //   92: iconst_1
    //   93: istore #10
    //   95: iload #9
    //   97: iconst_2
    //   98: imul
    //   99: iload #10
    //   101: iadd
    //   102: istore #9
    //   104: aload #6
    //   106: ifnull -> 118
    //   109: iload_1
    //   110: istore #10
    //   112: iload_1
    //   113: iload #9
    //   115: if_icmple -> 126
    //   118: aload #8
    //   120: astore #6
    //   122: iload #9
    //   124: istore #10
    //   126: iinc #7, 1
    //   129: iload #10
    //   131: istore_1
    //   132: goto -> 45
    //   135: aload #6
    //   137: areturn
  }
  
  private static long getUniqueKey(Typeface paramTypeface) {
    if (paramTypeface == null)
      return 0L; 
    try {
      Field field = Typeface.class.getDeclaredField("native_instance");
      field.setAccessible(true);
      return ((Number)field.get(paramTypeface)).longValue();
    } catch (NoSuchFieldException noSuchFieldException) {
      Log.e("TypefaceCompatBaseImpl", "Could not retrieve font from family.", noSuchFieldException);
      return 0L;
    } catch (IllegalAccessException illegalAccessException) {
      Log.e("TypefaceCompatBaseImpl", "Could not retrieve font from family.", illegalAccessException);
      return 0L;
    } 
  }
  
  public Typeface createFromFontFamilyFilesResourceEntry(Context paramContext, FontResourcesParserCompat.FontFamilyFilesResourceEntry paramFontFamilyFilesResourceEntry, Resources paramResources, int paramInt) {
    FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry = findBestEntry(paramFontFamilyFilesResourceEntry, paramInt);
    if (fontFileResourceEntry == null)
      return null; 
    Typeface typeface = TypefaceCompat.createFromResourcesFontFile(paramContext, paramResources, fontFileResourceEntry.getResourceId(), fontFileResourceEntry.getFileName(), paramInt);
    addFontFamily(typeface, paramFontFamilyFilesResourceEntry);
    return typeface;
  }
  
  public Typeface createFromFontInfo(Context paramContext, CancellationSignal paramCancellationSignal, FontsContractCompat.FontInfo[] paramArrayOfFontInfo, int paramInt) {
    int i = paramArrayOfFontInfo.length;
    FontsContractCompat.FontInfo fontInfo2 = null;
    if (i < 1)
      return null; 
    FontsContractCompat.FontInfo fontInfo1 = findBestInfo(paramArrayOfFontInfo, paramInt);
    try {
      InputStream inputStream = paramContext.getContentResolver().openInputStream(fontInfo1.getUri());
      try {
        return typeface;
      } catch (IOException iOException) {
      
      } finally {}
      TypefaceCompatUtil.closeQuietly(inputStream);
      throw paramContext;
    } catch (IOException iOException) {
    
    } finally {
      fontInfo1 = fontInfo2;
      TypefaceCompatUtil.closeQuietly((Closeable)fontInfo1);
    } 
    TypefaceCompatUtil.closeQuietly((Closeable)fontInfo1);
    return null;
  }
  
  protected Typeface createFromInputStream(Context paramContext, InputStream paramInputStream) {
    File file = TypefaceCompatUtil.getTempFile(paramContext);
    if (file == null)
      return null; 
    try {
      boolean bool = TypefaceCompatUtil.copyToFile(file, paramInputStream);
      if (!bool)
        return null; 
      return Typeface.createFromFile(file.getPath());
    } catch (RuntimeException runtimeException) {
      return null;
    } finally {
      file.delete();
    } 
  }
  
  public Typeface createFromResourcesFontFile(Context paramContext, Resources paramResources, int paramInt1, String paramString, int paramInt2) {
    File file = TypefaceCompatUtil.getTempFile(paramContext);
    if (file == null)
      return null; 
    try {
      boolean bool = TypefaceCompatUtil.copyToFile(file, paramResources, paramInt1);
      if (!bool)
        return null; 
      return Typeface.createFromFile(file.getPath());
    } catch (RuntimeException runtimeException) {
      return null;
    } finally {
      file.delete();
    } 
  }
  
  protected FontsContractCompat.FontInfo findBestInfo(FontsContractCompat.FontInfo[] paramArrayOfFontInfo, int paramInt) {
    return findBestFont(paramArrayOfFontInfo, paramInt, new StyleExtractor<FontsContractCompat.FontInfo>() {
          public int getWeight(FontsContractCompat.FontInfo param1FontInfo) {
            return param1FontInfo.getWeight();
          }
          
          public boolean isItalic(FontsContractCompat.FontInfo param1FontInfo) {
            return param1FontInfo.isItalic();
          }
        });
  }
  
  FontResourcesParserCompat.FontFamilyFilesResourceEntry getFontFamily(Typeface paramTypeface) {
    long l = getUniqueKey(paramTypeface);
    return (l == 0L) ? null : this.mFontFamilies.get(Long.valueOf(l));
  }
  
  private static interface StyleExtractor<T> {
    int getWeight(T param1T);
    
    boolean isItalic(T param1T);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/core/graphics/TypefaceCompatBaseImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
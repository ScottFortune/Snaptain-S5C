package androidx.core.view.inputmethod;

import android.content.ClipDescription;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputContentInfo;

public final class InputConnectionCompat {
  private static final String COMMIT_CONTENT_ACTION = "androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
  
  private static final String COMMIT_CONTENT_CONTENT_URI_INTEROP_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI";
  
  private static final String COMMIT_CONTENT_CONTENT_URI_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI";
  
  private static final String COMMIT_CONTENT_DESCRIPTION_INTEROP_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
  
  private static final String COMMIT_CONTENT_DESCRIPTION_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
  
  private static final String COMMIT_CONTENT_FLAGS_INTEROP_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
  
  private static final String COMMIT_CONTENT_FLAGS_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
  
  private static final String COMMIT_CONTENT_INTEROP_ACTION = "androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
  
  private static final String COMMIT_CONTENT_LINK_URI_INTEROP_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
  
  private static final String COMMIT_CONTENT_LINK_URI_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
  
  private static final String COMMIT_CONTENT_OPTS_INTEROP_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
  
  private static final String COMMIT_CONTENT_OPTS_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
  
  private static final String COMMIT_CONTENT_RESULT_INTEROP_RECEIVER_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
  
  private static final String COMMIT_CONTENT_RESULT_RECEIVER_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
  
  public static final int INPUT_CONTENT_GRANT_READ_URI_PERMISSION = 1;
  
  public static boolean commitContent(InputConnection paramInputConnection, EditorInfo paramEditorInfo, InputContentInfoCompat paramInputContentInfoCompat, int paramInt, Bundle paramBundle) {
    ClipDescription clipDescription = paramInputContentInfoCompat.getDescription();
    String[] arrayOfString = EditorInfoCompat.getContentMimeTypes(paramEditorInfo);
    int i = arrayOfString.length;
    int j = 0;
    while (true) {
      if (j < i) {
        if (clipDescription.hasMimeType(arrayOfString[j])) {
          j = 1;
          break;
        } 
        j++;
        continue;
      } 
      j = 0;
      break;
    } 
    if (j == 0)
      return false; 
    if (Build.VERSION.SDK_INT >= 25)
      return paramInputConnection.commitContent((InputContentInfo)paramInputContentInfoCompat.unwrap(), paramInt, paramBundle); 
    j = EditorInfoCompat.getProtocol(paramEditorInfo);
    if (j != 2 && j != 3 && j != 4)
      return false; 
    Bundle bundle = new Bundle();
    bundle.putParcelable("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI", (Parcelable)paramInputContentInfoCompat.getContentUri());
    bundle.putParcelable("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION", (Parcelable)paramInputContentInfoCompat.getDescription());
    bundle.putParcelable("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI", (Parcelable)paramInputContentInfoCompat.getLinkUri());
    bundle.putInt("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS", paramInt);
    bundle.putParcelable("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS", (Parcelable)paramBundle);
    return paramInputConnection.performPrivateCommand("androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT", bundle);
  }
  
  public static InputConnection createWrapper(InputConnection paramInputConnection, EditorInfo paramEditorInfo, final OnCommitContentListener listener) {
    if (paramInputConnection != null) {
      if (paramEditorInfo != null) {
        if (listener != null)
          return (InputConnection)((Build.VERSION.SDK_INT >= 25) ? new InputConnectionWrapper(paramInputConnection, false) {
              public boolean commitContent(InputContentInfo param1InputContentInfo, int param1Int, Bundle param1Bundle) {
                return listener.onCommitContent(InputContentInfoCompat.wrap(param1InputContentInfo), param1Int, param1Bundle) ? true : super.commitContent(param1InputContentInfo, param1Int, param1Bundle);
              }
            } : (((EditorInfoCompat.getContentMimeTypes(paramEditorInfo)).length == 0) ? paramInputConnection : new InputConnectionWrapper(paramInputConnection, false) {
              public boolean performPrivateCommand(String param1String, Bundle param1Bundle) {
                return InputConnectionCompat.handlePerformPrivateCommand(param1String, param1Bundle, listener) ? true : super.performPrivateCommand(param1String, param1Bundle);
              }
            })); 
        throw new IllegalArgumentException("onCommitContentListener must be non-null");
      } 
      throw new IllegalArgumentException("editorInfo must be non-null");
    } 
    throw new IllegalArgumentException("inputConnection must be non-null");
  }
  
  static boolean handlePerformPrivateCommand(String paramString, Bundle paramBundle, OnCommitContentListener paramOnCommitContentListener) {
    boolean bool = false;
    if (paramBundle == null)
      return false; 
    if (TextUtils.equals("androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT", paramString) || TextUtils.equals("androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT", paramString)) {
      try {
        ResultReceiver resultReceiver = (ResultReceiver)paramBundle.getParcelable("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER");
      } finally {
        paramString = null;
      } 
      if (paramBundle != null)
        paramBundle.send(0, null); 
      throw paramString;
    } 
    return false;
  }
  
  public static interface OnCommitContentListener {
    boolean onCommitContent(InputContentInfoCompat param1InputContentInfoCompat, int param1Int, Bundle param1Bundle);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/core/view/inputmethod/InputConnectionCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
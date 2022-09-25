package androidx.core.database.sqlite;

import android.database.sqlite.SQLiteCursor;
import android.os.Build;

public final class SQLiteCursorCompat {
  public static void setFillWindowForwardOnly(SQLiteCursor paramSQLiteCursor, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 28)
      paramSQLiteCursor.setFillWindowForwardOnly(paramBoolean); 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/core/database/sqlite/SQLiteCursorCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
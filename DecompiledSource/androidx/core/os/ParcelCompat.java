package androidx.core.os;

import android.os.Parcel;

public final class ParcelCompat {
  public static boolean readBoolean(Parcel paramParcel) {
    boolean bool;
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static void writeBoolean(Parcel paramParcel, boolean paramBoolean) {
    paramParcel.writeInt(paramBoolean);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/core/os/ParcelCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package androidx.core.os;

import android.os.Parcel;

@Deprecated
public interface ParcelableCompatCreatorCallbacks<T> {
  T createFromParcel(Parcel paramParcel, ClassLoader paramClassLoader);
  
  T[] newArray(int paramInt);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/core/os/ParcelableCompatCreatorCallbacks.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
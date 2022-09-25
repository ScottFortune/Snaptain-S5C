package com.jaygoo.widget;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

public class SavedState extends View.BaseSavedState {
  public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
      public SavedState createFromParcel(Parcel param1Parcel) {
        return new SavedState(param1Parcel);
      }
      
      public SavedState[] newArray(int param1Int) {
        return new SavedState[param1Int];
      }
    };
  
  public float currSelectedMax;
  
  public float currSelectedMin;
  
  public float maxValue;
  
  public float minValue;
  
  public float rangeInterval;
  
  public int tickNumber;
  
  private SavedState(Parcel paramParcel) {
    super(paramParcel);
    this.minValue = paramParcel.readFloat();
    this.maxValue = paramParcel.readFloat();
    this.rangeInterval = paramParcel.readFloat();
    this.tickNumber = paramParcel.readInt();
    this.currSelectedMin = paramParcel.readFloat();
    this.currSelectedMax = paramParcel.readFloat();
  }
  
  public SavedState(Parcelable paramParcelable) {
    super(paramParcelable);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    super.writeToParcel(paramParcel, paramInt);
    paramParcel.writeFloat(this.minValue);
    paramParcel.writeFloat(this.maxValue);
    paramParcel.writeFloat(this.rangeInterval);
    paramParcel.writeInt(this.tickNumber);
    paramParcel.writeFloat(this.currSelectedMin);
    paramParcel.writeFloat(this.currSelectedMax);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/jaygoo/widget/SavedState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
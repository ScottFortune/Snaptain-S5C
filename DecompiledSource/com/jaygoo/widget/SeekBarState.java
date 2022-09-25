package com.jaygoo.widget;

public class SeekBarState {
  public String indicatorText;
  
  public boolean isMax;
  
  public boolean isMin;
  
  public float value;
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("indicatorText: ");
    stringBuilder.append(this.indicatorText);
    stringBuilder.append(" ,isMin: ");
    stringBuilder.append(this.isMin);
    stringBuilder.append(" ,isMax: ");
    stringBuilder.append(this.isMax);
    return stringBuilder.toString();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/jaygoo/widget/SeekBarState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
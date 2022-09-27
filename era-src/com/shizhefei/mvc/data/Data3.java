package com.shizhefei.mvc.data;

public class Data3<VALUE1, VALUE2, VALUE3> {
  private VALUE1 value1;
  
  private VALUE2 value2;
  
  private VALUE3 value3;
  
  public Data3() {}
  
  public Data3(VALUE1 paramVALUE1, VALUE2 paramVALUE2, VALUE3 paramVALUE3) {
    this.value1 = paramVALUE1;
    this.value2 = paramVALUE2;
    this.value3 = paramVALUE3;
  }
  
  public VALUE1 getValue1() {
    return this.value1;
  }
  
  public VALUE2 getValue2() {
    return this.value2;
  }
  
  public VALUE3 getValue3() {
    return this.value3;
  }
  
  public void setValue1(VALUE1 paramVALUE1) {
    this.value1 = paramVALUE1;
  }
  
  public void setValue2(VALUE2 paramVALUE2) {
    this.value2 = paramVALUE2;
  }
  
  public void setValue3(VALUE3 paramVALUE3) {
    this.value3 = paramVALUE3;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/mvc/data/Data3.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
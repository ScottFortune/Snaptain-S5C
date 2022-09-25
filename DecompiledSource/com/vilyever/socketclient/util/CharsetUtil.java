package com.vilyever.socketclient.util;

import java.io.UnsupportedEncodingException;

public class CharsetUtil {
  public static final String UTF_8 = "UTF-8";
  
  final CharsetUtil self = this;
  
  public static String dataToString(byte[] paramArrayOfbyte, String paramString) {
    if (paramArrayOfbyte != null)
      try {
        return new String(paramArrayOfbyte, paramString);
      } catch (UnsupportedEncodingException unsupportedEncodingException) {
        unsupportedEncodingException.printStackTrace();
      }  
    return null;
  }
  
  public static byte[] stringToData(String paramString1, String paramString2) {
    if (paramString1 != null)
      try {
        return paramString1.getBytes(paramString2);
      } catch (UnsupportedEncodingException unsupportedEncodingException) {
        unsupportedEncodingException.printStackTrace();
      }  
    return null;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/vilyever/socketclient/util/CharsetUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
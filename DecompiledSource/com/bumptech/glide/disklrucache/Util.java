package com.bumptech.glide.disklrucache;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;

final class Util {
  static final Charset US_ASCII = Charset.forName("US-ASCII");
  
  static final Charset UTF_8 = Charset.forName("UTF-8");
  
  static void closeQuietly(Closeable paramCloseable) {
    if (paramCloseable != null)
      try {
        paramCloseable.close();
      } catch (RuntimeException runtimeException) {
        throw runtimeException;
      } catch (Exception exception) {} 
  }
  
  static void deleteContents(File paramFile) throws IOException {
    File[] arrayOfFile = paramFile.listFiles();
    if (arrayOfFile != null) {
      int i = arrayOfFile.length;
      byte b = 0;
      while (b < i) {
        paramFile = arrayOfFile[b];
        if (paramFile.isDirectory())
          deleteContents(paramFile); 
        if (paramFile.delete()) {
          b++;
          continue;
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("failed to delete file: ");
        stringBuilder1.append(paramFile);
        throw new IOException(stringBuilder1.toString());
      } 
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("not a readable directory: ");
    stringBuilder.append(paramFile);
    IOException iOException = new IOException(stringBuilder.toString());
    throw iOException;
  }
  
  static String readFully(Reader paramReader) throws IOException {
    try {
      StringWriter stringWriter = new StringWriter();
      this();
      char[] arrayOfChar = new char[1024];
      while (true) {
        int i = paramReader.read(arrayOfChar);
        if (i != -1) {
          stringWriter.write(arrayOfChar, 0, i);
          continue;
        } 
        return stringWriter.toString();
      } 
    } finally {
      paramReader.close();
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/disklrucache/Util.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
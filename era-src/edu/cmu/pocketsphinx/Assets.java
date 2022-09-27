package edu.cmu.pocketsphinx;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Assets {
  public static final String ASSET_LIST_NAME = "assets.lst";
  
  public static final String HASH_EXT = ".md5";
  
  public static final String SYNC_DIR = "sync";
  
  protected static final String TAG = Assets.class.getSimpleName();
  
  private final AssetManager assetManager;
  
  private final File externalDir;
  
  public Assets(Context paramContext) throws IOException {
    File file = paramContext.getExternalFilesDir(null);
    if (file != null) {
      this.externalDir = new File(file, "sync");
      this.assetManager = paramContext.getAssets();
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("cannot get external files dir, external storage state is ");
    stringBuilder.append(Environment.getExternalStorageState());
    throw new IOException(stringBuilder.toString());
  }
  
  public Assets(Context paramContext, String paramString) {
    this.externalDir = new File(paramString);
    this.assetManager = paramContext.getAssets();
  }
  
  private InputStream openAsset(String paramString) throws IOException {
    return this.assetManager.open((new File("sync", paramString)).getPath());
  }
  
  private List<String> readLines(InputStream paramInputStream) throws IOException {
    ArrayList<String> arrayList = new ArrayList();
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(paramInputStream));
    while (true) {
      String str = bufferedReader.readLine();
      if (str != null) {
        arrayList.add(str);
        continue;
      } 
      return arrayList;
    } 
  }
  
  public File copy(String paramString) throws IOException {
    InputStream inputStream = openAsset(paramString);
    File file = new File(this.externalDir, paramString);
    file.getParentFile().mkdirs();
    FileOutputStream fileOutputStream = new FileOutputStream(file);
    byte[] arrayOfByte = new byte[1024];
    while (true) {
      int i = inputStream.read(arrayOfByte);
      if (i != -1) {
        if (i == 0) {
          i = inputStream.read();
          if (i < 0)
            break; 
          fileOutputStream.write(i);
          continue;
        } 
        fileOutputStream.write(arrayOfByte, 0, i);
        continue;
      } 
      break;
    } 
    fileOutputStream.close();
    return file;
  }
  
  public File getExternalDir() {
    return this.externalDir;
  }
  
  public Map<String, String> getExternalItems() {
    try {
      HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
      this();
      File file = new File();
      this(this.externalDir, "assets.lst");
      FileInputStream fileInputStream = new FileInputStream();
      this(file);
      Iterator<String> iterator = readLines(fileInputStream).iterator();
      while (iterator.hasNext()) {
        String[] arrayOfString = ((String)iterator.next()).split(" ");
        hashMap.put(arrayOfString[0], arrayOfString[1]);
      } 
      return (Map)hashMap;
    } catch (IOException iOException) {
      return Collections.emptyMap();
    } 
  }
  
  public Map<String, String> getItems() throws IOException {
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    for (String str : readLines(openAsset("assets.lst"))) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str);
      stringBuilder.append(".md5");
      hashMap.put(str, (new BufferedReader(new InputStreamReader(openAsset(stringBuilder.toString())))).readLine());
    } 
    return (Map)hashMap;
  }
  
  public Collection<String> getItemsToCopy(String paramString) throws IOException {
    ArrayList<String> arrayList = new ArrayList();
    ArrayDeque<String> arrayDeque = new ArrayDeque();
    arrayDeque.offer(paramString);
    while (!arrayDeque.isEmpty()) {
      paramString = arrayDeque.poll();
      String[] arrayOfString = this.assetManager.list(paramString);
      int i = arrayOfString.length;
      for (byte b = 0; b < i; b++)
        arrayDeque.offer(arrayOfString[b]); 
      if (arrayOfString.length == 0)
        arrayList.add(paramString); 
    } 
    return arrayList;
  }
  
  public File syncAssets() throws IOException {
    ArrayList<String> arrayList = new ArrayList();
    ArrayList arrayList1 = new ArrayList();
    Map<String, String> map1 = getItems();
    Map<String, String> map2 = getExternalItems();
    for (String str : map1.keySet()) {
      if (!((String)map1.get(str)).equals(map2.get(str)) || !(new File(this.externalDir, str)).exists()) {
        arrayList.add(str);
        continue;
      } 
      Log.i(TAG, String.format("Skipping asset %s: checksums are equal", new Object[] { str }));
    } 
    arrayList1.addAll(map2.keySet());
    arrayList1.removeAll(map1.keySet());
    for (String str : arrayList) {
      File file = copy(str);
      Log.i(TAG, String.format("Copying asset %s to %s", new Object[] { str, file }));
    } 
    for (String str : arrayList1) {
      File file = new File(this.externalDir, str);
      file.delete();
      Log.i(TAG, String.format("Removing asset %s", new Object[] { file }));
    } 
    updateItemList(map1);
    return this.externalDir;
  }
  
  public void updateItemList(Map<String, String> paramMap) throws IOException {
    PrintWriter printWriter = new PrintWriter(new FileOutputStream(new File(this.externalDir, "assets.lst")));
    for (Map.Entry<String, String> entry : paramMap.entrySet()) {
      printWriter.format("%s %s\n", new Object[] { entry.getKey(), entry.getValue() });
    } 
    printWriter.close();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/edu/cmu/pocketsphinx/Assets.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.bumptech.glide.disklrucache;

import android.os.Build;
import android.os.StrictMode;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class DiskLruCache implements Closeable {
  static final long ANY_SEQUENCE_NUMBER = -1L;
  
  private static final String CLEAN = "CLEAN";
  
  private static final String DIRTY = "DIRTY";
  
  static final String JOURNAL_FILE = "journal";
  
  static final String JOURNAL_FILE_BACKUP = "journal.bkp";
  
  static final String JOURNAL_FILE_TEMP = "journal.tmp";
  
  static final String MAGIC = "libcore.io.DiskLruCache";
  
  private static final String READ = "READ";
  
  private static final String REMOVE = "REMOVE";
  
  static final String VERSION_1 = "1";
  
  private final int appVersion;
  
  private final Callable<Void> cleanupCallable = new Callable<Void>() {
      public Void call() throws Exception {
        synchronized (DiskLruCache.this) {
          if (DiskLruCache.this.journalWriter == null)
            return null; 
          DiskLruCache.this.trimToSize();
          if (DiskLruCache.this.journalRebuildRequired()) {
            DiskLruCache.this.rebuildJournal();
            DiskLruCache.access$502(DiskLruCache.this, 0);
          } 
          return null;
        } 
      }
    };
  
  private final File directory;
  
  final ThreadPoolExecutor executorService = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new DiskLruCacheThreadFactory());
  
  private final File journalFile;
  
  private final File journalFileBackup;
  
  private final File journalFileTmp;
  
  private Writer journalWriter;
  
  private final LinkedHashMap<String, Entry> lruEntries = new LinkedHashMap<String, Entry>(0, 0.75F, true);
  
  private long maxSize;
  
  private long nextSequenceNumber = 0L;
  
  private int redundantOpCount;
  
  private long size = 0L;
  
  private final int valueCount;
  
  private DiskLruCache(File paramFile, int paramInt1, int paramInt2, long paramLong) {
    this.directory = paramFile;
    this.appVersion = paramInt1;
    this.journalFile = new File(paramFile, "journal");
    this.journalFileTmp = new File(paramFile, "journal.tmp");
    this.journalFileBackup = new File(paramFile, "journal.bkp");
    this.valueCount = paramInt2;
    this.maxSize = paramLong;
  }
  
  private void checkNotClosed() {
    if (this.journalWriter != null)
      return; 
    throw new IllegalStateException("cache is closed");
  }
  
  private static void closeWriter(Writer paramWriter) throws IOException {
    if (Build.VERSION.SDK_INT < 26) {
      paramWriter.close();
      return;
    } 
    StrictMode.ThreadPolicy threadPolicy = StrictMode.getThreadPolicy();
    StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder(threadPolicy)).permitUnbufferedIo().build());
    try {
      paramWriter.close();
      return;
    } finally {
      StrictMode.setThreadPolicy(threadPolicy);
    } 
  }
  
  private void completeEdit(Editor paramEditor, boolean paramBoolean) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokestatic access$1500 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Editor;)Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;
    //   6: astore_3
    //   7: aload_3
    //   8: invokestatic access$800 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Lcom/bumptech/glide/disklrucache/DiskLruCache$Editor;
    //   11: aload_1
    //   12: if_acmpne -> 430
    //   15: iconst_0
    //   16: istore #4
    //   18: iload #4
    //   20: istore #5
    //   22: iload_2
    //   23: ifeq -> 128
    //   26: iload #4
    //   28: istore #5
    //   30: aload_3
    //   31: invokestatic access$700 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Z
    //   34: ifne -> 128
    //   37: iconst_0
    //   38: istore #6
    //   40: iload #4
    //   42: istore #5
    //   44: iload #6
    //   46: aload_0
    //   47: getfield valueCount : I
    //   50: if_icmpge -> 128
    //   53: aload_1
    //   54: invokestatic access$1600 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Editor;)[Z
    //   57: iload #6
    //   59: baload
    //   60: ifeq -> 88
    //   63: aload_3
    //   64: iload #6
    //   66: invokevirtual getDirtyFile : (I)Ljava/io/File;
    //   69: invokevirtual exists : ()Z
    //   72: ifne -> 82
    //   75: aload_1
    //   76: invokevirtual abort : ()V
    //   79: aload_0
    //   80: monitorexit
    //   81: return
    //   82: iinc #6, 1
    //   85: goto -> 40
    //   88: aload_1
    //   89: invokevirtual abort : ()V
    //   92: new java/lang/IllegalStateException
    //   95: astore_3
    //   96: new java/lang/StringBuilder
    //   99: astore_1
    //   100: aload_1
    //   101: invokespecial <init> : ()V
    //   104: aload_1
    //   105: ldc 'Newly created entry didn't create value for index '
    //   107: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   110: pop
    //   111: aload_1
    //   112: iload #6
    //   114: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   117: pop
    //   118: aload_3
    //   119: aload_1
    //   120: invokevirtual toString : ()Ljava/lang/String;
    //   123: invokespecial <init> : (Ljava/lang/String;)V
    //   126: aload_3
    //   127: athrow
    //   128: iload #5
    //   130: aload_0
    //   131: getfield valueCount : I
    //   134: if_icmpge -> 222
    //   137: aload_3
    //   138: iload #5
    //   140: invokevirtual getDirtyFile : (I)Ljava/io/File;
    //   143: astore_1
    //   144: iload_2
    //   145: ifeq -> 212
    //   148: aload_1
    //   149: invokevirtual exists : ()Z
    //   152: ifeq -> 216
    //   155: aload_3
    //   156: iload #5
    //   158: invokevirtual getCleanFile : (I)Ljava/io/File;
    //   161: astore #7
    //   163: aload_1
    //   164: aload #7
    //   166: invokevirtual renameTo : (Ljava/io/File;)Z
    //   169: pop
    //   170: aload_3
    //   171: invokestatic access$1100 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)[J
    //   174: iload #5
    //   176: laload
    //   177: lstore #8
    //   179: aload #7
    //   181: invokevirtual length : ()J
    //   184: lstore #10
    //   186: aload_3
    //   187: invokestatic access$1100 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)[J
    //   190: iload #5
    //   192: lload #10
    //   194: lastore
    //   195: aload_0
    //   196: aload_0
    //   197: getfield size : J
    //   200: lload #8
    //   202: lsub
    //   203: lload #10
    //   205: ladd
    //   206: putfield size : J
    //   209: goto -> 216
    //   212: aload_1
    //   213: invokestatic deleteIfExists : (Ljava/io/File;)V
    //   216: iinc #5, 1
    //   219: goto -> 128
    //   222: aload_0
    //   223: aload_0
    //   224: getfield redundantOpCount : I
    //   227: iconst_1
    //   228: iadd
    //   229: putfield redundantOpCount : I
    //   232: aload_3
    //   233: aconst_null
    //   234: invokestatic access$802 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;Lcom/bumptech/glide/disklrucache/DiskLruCache$Editor;)Lcom/bumptech/glide/disklrucache/DiskLruCache$Editor;
    //   237: pop
    //   238: aload_3
    //   239: invokestatic access$700 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Z
    //   242: iload_2
    //   243: ior
    //   244: ifeq -> 335
    //   247: aload_3
    //   248: iconst_1
    //   249: invokestatic access$702 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;Z)Z
    //   252: pop
    //   253: aload_0
    //   254: getfield journalWriter : Ljava/io/Writer;
    //   257: ldc 'CLEAN'
    //   259: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/io/Writer;
    //   262: pop
    //   263: aload_0
    //   264: getfield journalWriter : Ljava/io/Writer;
    //   267: bipush #32
    //   269: invokevirtual append : (C)Ljava/io/Writer;
    //   272: pop
    //   273: aload_0
    //   274: getfield journalWriter : Ljava/io/Writer;
    //   277: aload_3
    //   278: invokestatic access$1200 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Ljava/lang/String;
    //   281: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/io/Writer;
    //   284: pop
    //   285: aload_0
    //   286: getfield journalWriter : Ljava/io/Writer;
    //   289: aload_3
    //   290: invokevirtual getLengths : ()Ljava/lang/String;
    //   293: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/io/Writer;
    //   296: pop
    //   297: aload_0
    //   298: getfield journalWriter : Ljava/io/Writer;
    //   301: bipush #10
    //   303: invokevirtual append : (C)Ljava/io/Writer;
    //   306: pop
    //   307: iload_2
    //   308: ifeq -> 389
    //   311: aload_0
    //   312: getfield nextSequenceNumber : J
    //   315: lstore #10
    //   317: aload_0
    //   318: lconst_1
    //   319: lload #10
    //   321: ladd
    //   322: putfield nextSequenceNumber : J
    //   325: aload_3
    //   326: lload #10
    //   328: invokestatic access$1302 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;J)J
    //   331: pop2
    //   332: goto -> 389
    //   335: aload_0
    //   336: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   339: aload_3
    //   340: invokestatic access$1200 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Ljava/lang/String;
    //   343: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   346: pop
    //   347: aload_0
    //   348: getfield journalWriter : Ljava/io/Writer;
    //   351: ldc 'REMOVE'
    //   353: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/io/Writer;
    //   356: pop
    //   357: aload_0
    //   358: getfield journalWriter : Ljava/io/Writer;
    //   361: bipush #32
    //   363: invokevirtual append : (C)Ljava/io/Writer;
    //   366: pop
    //   367: aload_0
    //   368: getfield journalWriter : Ljava/io/Writer;
    //   371: aload_3
    //   372: invokestatic access$1200 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Ljava/lang/String;
    //   375: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/io/Writer;
    //   378: pop
    //   379: aload_0
    //   380: getfield journalWriter : Ljava/io/Writer;
    //   383: bipush #10
    //   385: invokevirtual append : (C)Ljava/io/Writer;
    //   388: pop
    //   389: aload_0
    //   390: getfield journalWriter : Ljava/io/Writer;
    //   393: invokestatic flushWriter : (Ljava/io/Writer;)V
    //   396: aload_0
    //   397: getfield size : J
    //   400: aload_0
    //   401: getfield maxSize : J
    //   404: lcmp
    //   405: ifgt -> 415
    //   408: aload_0
    //   409: invokespecial journalRebuildRequired : ()Z
    //   412: ifeq -> 427
    //   415: aload_0
    //   416: getfield executorService : Ljava/util/concurrent/ThreadPoolExecutor;
    //   419: aload_0
    //   420: getfield cleanupCallable : Ljava/util/concurrent/Callable;
    //   423: invokevirtual submit : (Ljava/util/concurrent/Callable;)Ljava/util/concurrent/Future;
    //   426: pop
    //   427: aload_0
    //   428: monitorexit
    //   429: return
    //   430: new java/lang/IllegalStateException
    //   433: astore_1
    //   434: aload_1
    //   435: invokespecial <init> : ()V
    //   438: aload_1
    //   439: athrow
    //   440: astore_1
    //   441: aload_0
    //   442: monitorexit
    //   443: goto -> 448
    //   446: aload_1
    //   447: athrow
    //   448: goto -> 446
    // Exception table:
    //   from	to	target	type
    //   2	15	440	finally
    //   30	37	440	finally
    //   44	79	440	finally
    //   88	128	440	finally
    //   128	144	440	finally
    //   148	209	440	finally
    //   212	216	440	finally
    //   222	307	440	finally
    //   311	332	440	finally
    //   335	389	440	finally
    //   389	415	440	finally
    //   415	427	440	finally
    //   430	440	440	finally
  }
  
  private static void deleteIfExists(File paramFile) throws IOException {
    if (!paramFile.exists() || paramFile.delete())
      return; 
    throw new IOException();
  }
  
  private Editor edit(String paramString, long paramLong) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkNotClosed : ()V
    //   6: aload_0
    //   7: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   10: aload_1
    //   11: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   14: checkcast com/bumptech/glide/disklrucache/DiskLruCache$Entry
    //   17: astore #4
    //   19: lload_2
    //   20: ldc2_w -1
    //   23: lcmp
    //   24: ifeq -> 50
    //   27: aload #4
    //   29: ifnull -> 46
    //   32: aload #4
    //   34: invokestatic access$1300 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)J
    //   37: lstore #5
    //   39: lload #5
    //   41: lload_2
    //   42: lcmp
    //   43: ifeq -> 50
    //   46: aload_0
    //   47: monitorexit
    //   48: aconst_null
    //   49: areturn
    //   50: aload #4
    //   52: ifnonnull -> 82
    //   55: new com/bumptech/glide/disklrucache/DiskLruCache$Entry
    //   58: astore #4
    //   60: aload #4
    //   62: aload_0
    //   63: aload_1
    //   64: aconst_null
    //   65: invokespecial <init> : (Lcom/bumptech/glide/disklrucache/DiskLruCache;Ljava/lang/String;Lcom/bumptech/glide/disklrucache/DiskLruCache$1;)V
    //   68: aload_0
    //   69: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   72: aload_1
    //   73: aload #4
    //   75: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   78: pop
    //   79: goto -> 98
    //   82: aload #4
    //   84: invokestatic access$800 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Lcom/bumptech/glide/disklrucache/DiskLruCache$Editor;
    //   87: astore #7
    //   89: aload #7
    //   91: ifnull -> 98
    //   94: aload_0
    //   95: monitorexit
    //   96: aconst_null
    //   97: areturn
    //   98: new com/bumptech/glide/disklrucache/DiskLruCache$Editor
    //   101: astore #7
    //   103: aload #7
    //   105: aload_0
    //   106: aload #4
    //   108: aconst_null
    //   109: invokespecial <init> : (Lcom/bumptech/glide/disklrucache/DiskLruCache;Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;Lcom/bumptech/glide/disklrucache/DiskLruCache$1;)V
    //   112: aload #4
    //   114: aload #7
    //   116: invokestatic access$802 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;Lcom/bumptech/glide/disklrucache/DiskLruCache$Editor;)Lcom/bumptech/glide/disklrucache/DiskLruCache$Editor;
    //   119: pop
    //   120: aload_0
    //   121: getfield journalWriter : Ljava/io/Writer;
    //   124: ldc 'DIRTY'
    //   126: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/io/Writer;
    //   129: pop
    //   130: aload_0
    //   131: getfield journalWriter : Ljava/io/Writer;
    //   134: bipush #32
    //   136: invokevirtual append : (C)Ljava/io/Writer;
    //   139: pop
    //   140: aload_0
    //   141: getfield journalWriter : Ljava/io/Writer;
    //   144: aload_1
    //   145: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/io/Writer;
    //   148: pop
    //   149: aload_0
    //   150: getfield journalWriter : Ljava/io/Writer;
    //   153: bipush #10
    //   155: invokevirtual append : (C)Ljava/io/Writer;
    //   158: pop
    //   159: aload_0
    //   160: getfield journalWriter : Ljava/io/Writer;
    //   163: invokestatic flushWriter : (Ljava/io/Writer;)V
    //   166: aload_0
    //   167: monitorexit
    //   168: aload #7
    //   170: areturn
    //   171: astore_1
    //   172: aload_0
    //   173: monitorexit
    //   174: aload_1
    //   175: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	171	finally
    //   32	39	171	finally
    //   55	79	171	finally
    //   82	89	171	finally
    //   98	166	171	finally
  }
  
  private static void flushWriter(Writer paramWriter) throws IOException {
    if (Build.VERSION.SDK_INT < 26) {
      paramWriter.flush();
      return;
    } 
    StrictMode.ThreadPolicy threadPolicy = StrictMode.getThreadPolicy();
    StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder(threadPolicy)).permitUnbufferedIo().build());
    try {
      paramWriter.flush();
      return;
    } finally {
      StrictMode.setThreadPolicy(threadPolicy);
    } 
  }
  
  private static String inputStreamToString(InputStream paramInputStream) throws IOException {
    return Util.readFully(new InputStreamReader(paramInputStream, Util.UTF_8));
  }
  
  private boolean journalRebuildRequired() {
    boolean bool;
    int i = this.redundantOpCount;
    if (i >= 2000 && i >= this.lruEntries.size()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static DiskLruCache open(File paramFile, int paramInt1, int paramInt2, long paramLong) throws IOException {
    if (paramLong > 0L) {
      if (paramInt2 > 0) {
        File file = new File(paramFile, "journal.bkp");
        if (file.exists()) {
          File file1 = new File(paramFile, "journal");
          if (file1.exists()) {
            file.delete();
          } else {
            renameTo(file, file1, false);
          } 
        } 
        DiskLruCache diskLruCache2 = new DiskLruCache(paramFile, paramInt1, paramInt2, paramLong);
        if (diskLruCache2.journalFile.exists())
          try {
            diskLruCache2.readJournal();
            diskLruCache2.processJournal();
            return diskLruCache2;
          } catch (IOException iOException) {
            PrintStream printStream = System.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("DiskLruCache ");
            stringBuilder.append(paramFile);
            stringBuilder.append(" is corrupt: ");
            stringBuilder.append(iOException.getMessage());
            stringBuilder.append(", removing");
            printStream.println(stringBuilder.toString());
            diskLruCache2.delete();
          }  
        paramFile.mkdirs();
        DiskLruCache diskLruCache1 = new DiskLruCache(paramFile, paramInt1, paramInt2, paramLong);
        diskLruCache1.rebuildJournal();
        return diskLruCache1;
      } 
      throw new IllegalArgumentException("valueCount <= 0");
    } 
    throw new IllegalArgumentException("maxSize <= 0");
  }
  
  private void processJournal() throws IOException {
    deleteIfExists(this.journalFileTmp);
    Iterator<Entry> iterator = this.lruEntries.values().iterator();
    while (iterator.hasNext()) {
      Entry entry = iterator.next();
      Editor editor = entry.currentEditor;
      boolean bool = false;
      byte b = 0;
      if (editor == null) {
        while (b < this.valueCount) {
          this.size += entry.lengths[b];
          b++;
        } 
        continue;
      } 
      Entry.access$802(entry, null);
      for (b = bool; b < this.valueCount; b++) {
        deleteIfExists(entry.getCleanFile(b));
        deleteIfExists(entry.getDirtyFile(b));
      } 
      iterator.remove();
    } 
  }
  
  private void readJournal() throws IOException {
    StrictLineReader strictLineReader = new StrictLineReader(new FileInputStream(this.journalFile), Util.US_ASCII);
    try {
      BufferedWriter bufferedWriter;
      OutputStreamWriter outputStreamWriter;
      FileOutputStream fileOutputStream;
      String str1 = strictLineReader.readLine();
      String str2 = strictLineReader.readLine();
      String str3 = strictLineReader.readLine();
      String str4 = strictLineReader.readLine();
      String str5 = strictLineReader.readLine();
      if ("libcore.io.DiskLruCache".equals(str1) && "1".equals(str2) && Integer.toString(this.appVersion).equals(str3) && Integer.toString(this.valueCount).equals(str4)) {
        boolean bool = "".equals(str5);
        if (bool) {
          byte b = 0;
          try {
            while (true) {
              readJournalLine(strictLineReader.readLine());
              b++;
            } 
          } catch (EOFException eOFException) {
            this.redundantOpCount = b - this.lruEntries.size();
            if (strictLineReader.hasUnterminatedLine()) {
              rebuildJournal();
            } else {
              bufferedWriter = new BufferedWriter();
              outputStreamWriter = new OutputStreamWriter();
              fileOutputStream = new FileOutputStream();
              this(this.journalFile, true);
              this(fileOutputStream, Util.US_ASCII);
              this(outputStreamWriter);
              this.journalWriter = bufferedWriter;
            } 
            return;
          } 
        } 
      } 
      IOException iOException = new IOException();
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("unexpected journal header: [");
      stringBuilder.append((String)bufferedWriter);
      stringBuilder.append(", ");
      stringBuilder.append((String)outputStreamWriter);
      stringBuilder.append(", ");
      stringBuilder.append((String)fileOutputStream);
      stringBuilder.append(", ");
      stringBuilder.append(str5);
      stringBuilder.append("]");
      this(stringBuilder.toString());
      throw iOException;
    } finally {
      Util.closeQuietly(strictLineReader);
    } 
  }
  
  private void readJournalLine(String paramString) throws IOException {
    String[] arrayOfString;
    int i = paramString.indexOf(' ');
    if (i != -1) {
      String str;
      int j = i + 1;
      int k = paramString.indexOf(' ', j);
      if (k == -1) {
        String str1 = paramString.substring(j);
        str = str1;
        if (i == 6) {
          str = str1;
          if (paramString.startsWith("REMOVE")) {
            this.lruEntries.remove(str1);
            return;
          } 
        } 
      } else {
        str = paramString.substring(j, k);
      } 
      Entry entry2 = this.lruEntries.get(str);
      Entry entry1 = entry2;
      if (entry2 == null) {
        entry1 = new Entry(str);
        this.lruEntries.put(str, entry1);
      } 
      if (k != -1 && i == 5 && paramString.startsWith("CLEAN")) {
        arrayOfString = paramString.substring(k + 1).split(" ");
        Entry.access$702(entry1, true);
        Entry.access$802(entry1, null);
        entry1.setLengths(arrayOfString);
      } else if (k == -1 && i == 5 && arrayOfString.startsWith("DIRTY")) {
        Entry.access$802(entry1, new Editor(entry1));
      } else if (k != -1 || i != 4 || !arrayOfString.startsWith("READ")) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("unexpected journal line: ");
        stringBuilder1.append((String)arrayOfString);
        throw new IOException(stringBuilder1.toString());
      } 
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("unexpected journal line: ");
    stringBuilder.append((String)arrayOfString);
    throw new IOException(stringBuilder.toString());
  }
  
  private void rebuildJournal() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield journalWriter : Ljava/io/Writer;
    //   6: ifnull -> 16
    //   9: aload_0
    //   10: getfield journalWriter : Ljava/io/Writer;
    //   13: invokestatic closeWriter : (Ljava/io/Writer;)V
    //   16: new java/io/BufferedWriter
    //   19: astore_1
    //   20: new java/io/OutputStreamWriter
    //   23: astore_2
    //   24: new java/io/FileOutputStream
    //   27: astore_3
    //   28: aload_3
    //   29: aload_0
    //   30: getfield journalFileTmp : Ljava/io/File;
    //   33: invokespecial <init> : (Ljava/io/File;)V
    //   36: aload_2
    //   37: aload_3
    //   38: getstatic com/bumptech/glide/disklrucache/Util.US_ASCII : Ljava/nio/charset/Charset;
    //   41: invokespecial <init> : (Ljava/io/OutputStream;Ljava/nio/charset/Charset;)V
    //   44: aload_1
    //   45: aload_2
    //   46: invokespecial <init> : (Ljava/io/Writer;)V
    //   49: aload_1
    //   50: ldc 'libcore.io.DiskLruCache'
    //   52: invokevirtual write : (Ljava/lang/String;)V
    //   55: aload_1
    //   56: ldc_w '\\n'
    //   59: invokevirtual write : (Ljava/lang/String;)V
    //   62: aload_1
    //   63: ldc '1'
    //   65: invokevirtual write : (Ljava/lang/String;)V
    //   68: aload_1
    //   69: ldc_w '\\n'
    //   72: invokevirtual write : (Ljava/lang/String;)V
    //   75: aload_1
    //   76: aload_0
    //   77: getfield appVersion : I
    //   80: invokestatic toString : (I)Ljava/lang/String;
    //   83: invokevirtual write : (Ljava/lang/String;)V
    //   86: aload_1
    //   87: ldc_w '\\n'
    //   90: invokevirtual write : (Ljava/lang/String;)V
    //   93: aload_1
    //   94: aload_0
    //   95: getfield valueCount : I
    //   98: invokestatic toString : (I)Ljava/lang/String;
    //   101: invokevirtual write : (Ljava/lang/String;)V
    //   104: aload_1
    //   105: ldc_w '\\n'
    //   108: invokevirtual write : (Ljava/lang/String;)V
    //   111: aload_1
    //   112: ldc_w '\\n'
    //   115: invokevirtual write : (Ljava/lang/String;)V
    //   118: aload_0
    //   119: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   122: invokevirtual values : ()Ljava/util/Collection;
    //   125: invokeinterface iterator : ()Ljava/util/Iterator;
    //   130: astore_3
    //   131: aload_3
    //   132: invokeinterface hasNext : ()Z
    //   137: ifeq -> 265
    //   140: aload_3
    //   141: invokeinterface next : ()Ljava/lang/Object;
    //   146: checkcast com/bumptech/glide/disklrucache/DiskLruCache$Entry
    //   149: astore_2
    //   150: aload_2
    //   151: invokestatic access$800 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Lcom/bumptech/glide/disklrucache/DiskLruCache$Editor;
    //   154: ifnull -> 206
    //   157: new java/lang/StringBuilder
    //   160: astore #4
    //   162: aload #4
    //   164: invokespecial <init> : ()V
    //   167: aload #4
    //   169: ldc_w 'DIRTY '
    //   172: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   175: pop
    //   176: aload #4
    //   178: aload_2
    //   179: invokestatic access$1200 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Ljava/lang/String;
    //   182: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   185: pop
    //   186: aload #4
    //   188: bipush #10
    //   190: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   193: pop
    //   194: aload_1
    //   195: aload #4
    //   197: invokevirtual toString : ()Ljava/lang/String;
    //   200: invokevirtual write : (Ljava/lang/String;)V
    //   203: goto -> 131
    //   206: new java/lang/StringBuilder
    //   209: astore #4
    //   211: aload #4
    //   213: invokespecial <init> : ()V
    //   216: aload #4
    //   218: ldc_w 'CLEAN '
    //   221: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   224: pop
    //   225: aload #4
    //   227: aload_2
    //   228: invokestatic access$1200 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Ljava/lang/String;
    //   231: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   234: pop
    //   235: aload #4
    //   237: aload_2
    //   238: invokevirtual getLengths : ()Ljava/lang/String;
    //   241: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   244: pop
    //   245: aload #4
    //   247: bipush #10
    //   249: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   252: pop
    //   253: aload_1
    //   254: aload #4
    //   256: invokevirtual toString : ()Ljava/lang/String;
    //   259: invokevirtual write : (Ljava/lang/String;)V
    //   262: goto -> 131
    //   265: aload_1
    //   266: invokestatic closeWriter : (Ljava/io/Writer;)V
    //   269: aload_0
    //   270: getfield journalFile : Ljava/io/File;
    //   273: invokevirtual exists : ()Z
    //   276: ifeq -> 291
    //   279: aload_0
    //   280: getfield journalFile : Ljava/io/File;
    //   283: aload_0
    //   284: getfield journalFileBackup : Ljava/io/File;
    //   287: iconst_1
    //   288: invokestatic renameTo : (Ljava/io/File;Ljava/io/File;Z)V
    //   291: aload_0
    //   292: getfield journalFileTmp : Ljava/io/File;
    //   295: aload_0
    //   296: getfield journalFile : Ljava/io/File;
    //   299: iconst_0
    //   300: invokestatic renameTo : (Ljava/io/File;Ljava/io/File;Z)V
    //   303: aload_0
    //   304: getfield journalFileBackup : Ljava/io/File;
    //   307: invokevirtual delete : ()Z
    //   310: pop
    //   311: new java/io/BufferedWriter
    //   314: astore_1
    //   315: new java/io/OutputStreamWriter
    //   318: astore_3
    //   319: new java/io/FileOutputStream
    //   322: astore_2
    //   323: aload_2
    //   324: aload_0
    //   325: getfield journalFile : Ljava/io/File;
    //   328: iconst_1
    //   329: invokespecial <init> : (Ljava/io/File;Z)V
    //   332: aload_3
    //   333: aload_2
    //   334: getstatic com/bumptech/glide/disklrucache/Util.US_ASCII : Ljava/nio/charset/Charset;
    //   337: invokespecial <init> : (Ljava/io/OutputStream;Ljava/nio/charset/Charset;)V
    //   340: aload_1
    //   341: aload_3
    //   342: invokespecial <init> : (Ljava/io/Writer;)V
    //   345: aload_0
    //   346: aload_1
    //   347: putfield journalWriter : Ljava/io/Writer;
    //   350: aload_0
    //   351: monitorexit
    //   352: return
    //   353: astore_3
    //   354: aload_1
    //   355: invokestatic closeWriter : (Ljava/io/Writer;)V
    //   358: aload_3
    //   359: athrow
    //   360: astore_1
    //   361: aload_0
    //   362: monitorexit
    //   363: goto -> 368
    //   366: aload_1
    //   367: athrow
    //   368: goto -> 366
    // Exception table:
    //   from	to	target	type
    //   2	16	360	finally
    //   16	49	360	finally
    //   49	131	353	finally
    //   131	203	353	finally
    //   206	262	353	finally
    //   265	291	360	finally
    //   291	350	360	finally
    //   354	360	360	finally
  }
  
  private static void renameTo(File paramFile1, File paramFile2, boolean paramBoolean) throws IOException {
    if (paramBoolean)
      deleteIfExists(paramFile2); 
    if (paramFile1.renameTo(paramFile2))
      return; 
    throw new IOException();
  }
  
  private void trimToSize() throws IOException {
    while (this.size > this.maxSize)
      remove((String)((Map.Entry)this.lruEntries.entrySet().iterator().next()).getKey()); 
  }
  
  public void close() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield journalWriter : Ljava/io/Writer;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnonnull -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: new java/util/ArrayList
    //   17: astore_1
    //   18: aload_1
    //   19: aload_0
    //   20: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   23: invokevirtual values : ()Ljava/util/Collection;
    //   26: invokespecial <init> : (Ljava/util/Collection;)V
    //   29: aload_1
    //   30: invokevirtual iterator : ()Ljava/util/Iterator;
    //   33: astore_2
    //   34: aload_2
    //   35: invokeinterface hasNext : ()Z
    //   40: ifeq -> 70
    //   43: aload_2
    //   44: invokeinterface next : ()Ljava/lang/Object;
    //   49: checkcast com/bumptech/glide/disklrucache/DiskLruCache$Entry
    //   52: astore_1
    //   53: aload_1
    //   54: invokestatic access$800 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Lcom/bumptech/glide/disklrucache/DiskLruCache$Editor;
    //   57: ifnull -> 34
    //   60: aload_1
    //   61: invokestatic access$800 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Lcom/bumptech/glide/disklrucache/DiskLruCache$Editor;
    //   64: invokevirtual abort : ()V
    //   67: goto -> 34
    //   70: aload_0
    //   71: invokespecial trimToSize : ()V
    //   74: aload_0
    //   75: getfield journalWriter : Ljava/io/Writer;
    //   78: invokestatic closeWriter : (Ljava/io/Writer;)V
    //   81: aload_0
    //   82: aconst_null
    //   83: putfield journalWriter : Ljava/io/Writer;
    //   86: aload_0
    //   87: monitorexit
    //   88: return
    //   89: astore_1
    //   90: aload_0
    //   91: monitorexit
    //   92: goto -> 97
    //   95: aload_1
    //   96: athrow
    //   97: goto -> 95
    // Exception table:
    //   from	to	target	type
    //   2	7	89	finally
    //   14	34	89	finally
    //   34	67	89	finally
    //   70	86	89	finally
  }
  
  public void delete() throws IOException {
    close();
    Util.deleteContents(this.directory);
  }
  
  public Editor edit(String paramString) throws IOException {
    return edit(paramString, -1L);
  }
  
  public void flush() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkNotClosed : ()V
    //   6: aload_0
    //   7: invokespecial trimToSize : ()V
    //   10: aload_0
    //   11: getfield journalWriter : Ljava/io/Writer;
    //   14: invokestatic flushWriter : (Ljava/io/Writer;)V
    //   17: aload_0
    //   18: monitorexit
    //   19: return
    //   20: astore_1
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_1
    //   24: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	20	finally
  }
  
  public Value get(String paramString) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkNotClosed : ()V
    //   6: aload_0
    //   7: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   10: aload_1
    //   11: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   14: checkcast com/bumptech/glide/disklrucache/DiskLruCache$Entry
    //   17: astore_2
    //   18: aload_2
    //   19: ifnonnull -> 26
    //   22: aload_0
    //   23: monitorexit
    //   24: aconst_null
    //   25: areturn
    //   26: aload_2
    //   27: invokestatic access$700 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Z
    //   30: istore_3
    //   31: iload_3
    //   32: ifne -> 39
    //   35: aload_0
    //   36: monitorexit
    //   37: aconst_null
    //   38: areturn
    //   39: aload_2
    //   40: getfield cleanFiles : [Ljava/io/File;
    //   43: astore #4
    //   45: aload #4
    //   47: arraylength
    //   48: istore #5
    //   50: iconst_0
    //   51: istore #6
    //   53: iload #6
    //   55: iload #5
    //   57: if_icmpge -> 83
    //   60: aload #4
    //   62: iload #6
    //   64: aaload
    //   65: invokevirtual exists : ()Z
    //   68: istore_3
    //   69: iload_3
    //   70: ifne -> 77
    //   73: aload_0
    //   74: monitorexit
    //   75: aconst_null
    //   76: areturn
    //   77: iinc #6, 1
    //   80: goto -> 53
    //   83: aload_0
    //   84: aload_0
    //   85: getfield redundantOpCount : I
    //   88: iconst_1
    //   89: iadd
    //   90: putfield redundantOpCount : I
    //   93: aload_0
    //   94: getfield journalWriter : Ljava/io/Writer;
    //   97: ldc 'READ'
    //   99: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/io/Writer;
    //   102: pop
    //   103: aload_0
    //   104: getfield journalWriter : Ljava/io/Writer;
    //   107: bipush #32
    //   109: invokevirtual append : (C)Ljava/io/Writer;
    //   112: pop
    //   113: aload_0
    //   114: getfield journalWriter : Ljava/io/Writer;
    //   117: aload_1
    //   118: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/io/Writer;
    //   121: pop
    //   122: aload_0
    //   123: getfield journalWriter : Ljava/io/Writer;
    //   126: bipush #10
    //   128: invokevirtual append : (C)Ljava/io/Writer;
    //   131: pop
    //   132: aload_0
    //   133: invokespecial journalRebuildRequired : ()Z
    //   136: ifeq -> 151
    //   139: aload_0
    //   140: getfield executorService : Ljava/util/concurrent/ThreadPoolExecutor;
    //   143: aload_0
    //   144: getfield cleanupCallable : Ljava/util/concurrent/Callable;
    //   147: invokevirtual submit : (Ljava/util/concurrent/Callable;)Ljava/util/concurrent/Future;
    //   150: pop
    //   151: new com/bumptech/glide/disklrucache/DiskLruCache$Value
    //   154: dup
    //   155: aload_0
    //   156: aload_1
    //   157: aload_2
    //   158: invokestatic access$1300 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)J
    //   161: aload_2
    //   162: getfield cleanFiles : [Ljava/io/File;
    //   165: aload_2
    //   166: invokestatic access$1100 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)[J
    //   169: aconst_null
    //   170: invokespecial <init> : (Lcom/bumptech/glide/disklrucache/DiskLruCache;Ljava/lang/String;J[Ljava/io/File;[JLcom/bumptech/glide/disklrucache/DiskLruCache$1;)V
    //   173: astore_1
    //   174: aload_0
    //   175: monitorexit
    //   176: aload_1
    //   177: areturn
    //   178: astore_1
    //   179: aload_0
    //   180: monitorexit
    //   181: goto -> 186
    //   184: aload_1
    //   185: athrow
    //   186: goto -> 184
    // Exception table:
    //   from	to	target	type
    //   2	18	178	finally
    //   26	31	178	finally
    //   39	50	178	finally
    //   60	69	178	finally
    //   83	151	178	finally
    //   151	174	178	finally
  }
  
  public File getDirectory() {
    return this.directory;
  }
  
  public long getMaxSize() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield maxSize : J
    //   6: lstore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: lload_1
    //   10: lreturn
    //   11: astore_3
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_3
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public boolean isClosed() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield journalWriter : Ljava/io/Writer;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnonnull -> 16
    //   11: iconst_1
    //   12: istore_2
    //   13: goto -> 18
    //   16: iconst_0
    //   17: istore_2
    //   18: aload_0
    //   19: monitorexit
    //   20: iload_2
    //   21: ireturn
    //   22: astore_1
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_1
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	22	finally
  }
  
  public boolean remove(String paramString) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial checkNotClosed : ()V
    //   6: aload_0
    //   7: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   10: aload_1
    //   11: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   14: checkcast com/bumptech/glide/disklrucache/DiskLruCache$Entry
    //   17: astore_2
    //   18: iconst_0
    //   19: istore_3
    //   20: aload_2
    //   21: ifnull -> 214
    //   24: aload_2
    //   25: invokestatic access$800 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)Lcom/bumptech/glide/disklrucache/DiskLruCache$Editor;
    //   28: ifnull -> 34
    //   31: goto -> 214
    //   34: iload_3
    //   35: aload_0
    //   36: getfield valueCount : I
    //   39: if_icmpge -> 133
    //   42: aload_2
    //   43: iload_3
    //   44: invokevirtual getCleanFile : (I)Ljava/io/File;
    //   47: astore #4
    //   49: aload #4
    //   51: invokevirtual exists : ()Z
    //   54: ifeq -> 105
    //   57: aload #4
    //   59: invokevirtual delete : ()Z
    //   62: ifeq -> 68
    //   65: goto -> 105
    //   68: new java/io/IOException
    //   71: astore_1
    //   72: new java/lang/StringBuilder
    //   75: astore_2
    //   76: aload_2
    //   77: invokespecial <init> : ()V
    //   80: aload_2
    //   81: ldc_w 'failed to delete '
    //   84: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   87: pop
    //   88: aload_2
    //   89: aload #4
    //   91: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   94: pop
    //   95: aload_1
    //   96: aload_2
    //   97: invokevirtual toString : ()Ljava/lang/String;
    //   100: invokespecial <init> : (Ljava/lang/String;)V
    //   103: aload_1
    //   104: athrow
    //   105: aload_0
    //   106: aload_0
    //   107: getfield size : J
    //   110: aload_2
    //   111: invokestatic access$1100 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)[J
    //   114: iload_3
    //   115: laload
    //   116: lsub
    //   117: putfield size : J
    //   120: aload_2
    //   121: invokestatic access$1100 : (Lcom/bumptech/glide/disklrucache/DiskLruCache$Entry;)[J
    //   124: iload_3
    //   125: lconst_0
    //   126: lastore
    //   127: iinc #3, 1
    //   130: goto -> 34
    //   133: aload_0
    //   134: aload_0
    //   135: getfield redundantOpCount : I
    //   138: iconst_1
    //   139: iadd
    //   140: putfield redundantOpCount : I
    //   143: aload_0
    //   144: getfield journalWriter : Ljava/io/Writer;
    //   147: ldc 'REMOVE'
    //   149: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/io/Writer;
    //   152: pop
    //   153: aload_0
    //   154: getfield journalWriter : Ljava/io/Writer;
    //   157: bipush #32
    //   159: invokevirtual append : (C)Ljava/io/Writer;
    //   162: pop
    //   163: aload_0
    //   164: getfield journalWriter : Ljava/io/Writer;
    //   167: aload_1
    //   168: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/io/Writer;
    //   171: pop
    //   172: aload_0
    //   173: getfield journalWriter : Ljava/io/Writer;
    //   176: bipush #10
    //   178: invokevirtual append : (C)Ljava/io/Writer;
    //   181: pop
    //   182: aload_0
    //   183: getfield lruEntries : Ljava/util/LinkedHashMap;
    //   186: aload_1
    //   187: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   190: pop
    //   191: aload_0
    //   192: invokespecial journalRebuildRequired : ()Z
    //   195: ifeq -> 210
    //   198: aload_0
    //   199: getfield executorService : Ljava/util/concurrent/ThreadPoolExecutor;
    //   202: aload_0
    //   203: getfield cleanupCallable : Ljava/util/concurrent/Callable;
    //   206: invokevirtual submit : (Ljava/util/concurrent/Callable;)Ljava/util/concurrent/Future;
    //   209: pop
    //   210: aload_0
    //   211: monitorexit
    //   212: iconst_1
    //   213: ireturn
    //   214: aload_0
    //   215: monitorexit
    //   216: iconst_0
    //   217: ireturn
    //   218: astore_1
    //   219: aload_0
    //   220: monitorexit
    //   221: goto -> 226
    //   224: aload_1
    //   225: athrow
    //   226: goto -> 224
    // Exception table:
    //   from	to	target	type
    //   2	18	218	finally
    //   24	31	218	finally
    //   34	65	218	finally
    //   68	105	218	finally
    //   105	127	218	finally
    //   133	210	218	finally
  }
  
  public void setMaxSize(long paramLong) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: lload_1
    //   4: putfield maxSize : J
    //   7: aload_0
    //   8: getfield executorService : Ljava/util/concurrent/ThreadPoolExecutor;
    //   11: aload_0
    //   12: getfield cleanupCallable : Ljava/util/concurrent/Callable;
    //   15: invokevirtual submit : (Ljava/util/concurrent/Callable;)Ljava/util/concurrent/Future;
    //   18: pop
    //   19: aload_0
    //   20: monitorexit
    //   21: return
    //   22: astore_3
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_3
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	22	finally
  }
  
  public long size() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield size : J
    //   6: lstore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: lload_1
    //   10: lreturn
    //   11: astore_3
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_3
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  private static final class DiskLruCacheThreadFactory implements ThreadFactory {
    private DiskLruCacheThreadFactory() {}
    
    public Thread newThread(Runnable param1Runnable) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: new java/lang/Thread
      //   5: astore_2
      //   6: aload_2
      //   7: aload_1
      //   8: ldc 'glide-disk-lru-cache-thread'
      //   10: invokespecial <init> : (Ljava/lang/Runnable;Ljava/lang/String;)V
      //   13: aload_2
      //   14: iconst_1
      //   15: invokevirtual setPriority : (I)V
      //   18: aload_0
      //   19: monitorexit
      //   20: aload_2
      //   21: areturn
      //   22: astore_1
      //   23: aload_0
      //   24: monitorexit
      //   25: aload_1
      //   26: athrow
      // Exception table:
      //   from	to	target	type
      //   2	18	22	finally
    }
  }
  
  public final class Editor {
    private boolean committed;
    
    private final DiskLruCache.Entry entry;
    
    private final boolean[] written;
    
    private Editor(DiskLruCache.Entry param1Entry) {
      boolean[] arrayOfBoolean;
      this.entry = param1Entry;
      if (param1Entry.readable) {
        DiskLruCache.this = null;
      } else {
        arrayOfBoolean = new boolean[DiskLruCache.this.valueCount];
      } 
      this.written = arrayOfBoolean;
    }
    
    private InputStream newInputStream(int param1Int) throws IOException {
      synchronized (DiskLruCache.this) {
        if (this.entry.currentEditor == this) {
          if (!this.entry.readable)
            return null; 
          try {
            FileInputStream fileInputStream = new FileInputStream();
            this(this.entry.getCleanFile(param1Int));
            return fileInputStream;
          } catch (FileNotFoundException fileNotFoundException) {
            return null;
          } 
        } 
        IllegalStateException illegalStateException = new IllegalStateException();
        this();
        throw illegalStateException;
      } 
    }
    
    public void abort() throws IOException {
      DiskLruCache.this.completeEdit(this, false);
    }
    
    public void abortUnlessCommitted() {
      if (!this.committed)
        try {
          abort();
        } catch (IOException iOException) {} 
    }
    
    public void commit() throws IOException {
      DiskLruCache.this.completeEdit(this, true);
      this.committed = true;
    }
    
    public File getFile(int param1Int) throws IOException {
      synchronized (DiskLruCache.this) {
        if (this.entry.currentEditor == this) {
          if (!this.entry.readable)
            this.written[param1Int] = true; 
          File file = this.entry.getDirtyFile(param1Int);
          if (!DiskLruCache.this.directory.exists())
            DiskLruCache.this.directory.mkdirs(); 
          return file;
        } 
        IllegalStateException illegalStateException = new IllegalStateException();
        this();
        throw illegalStateException;
      } 
    }
    
    public String getString(int param1Int) throws IOException {
      InputStream inputStream = newInputStream(param1Int);
      if (inputStream != null) {
        String str = DiskLruCache.inputStreamToString(inputStream);
      } else {
        inputStream = null;
      } 
      return (String)inputStream;
    }
    
    public void set(int param1Int, String param1String) throws IOException {
      OutputStreamWriter outputStreamWriter = null;
      try {
        FileOutputStream fileOutputStream = new FileOutputStream();
        this(getFile(param1Int));
        OutputStreamWriter outputStreamWriter1 = new OutputStreamWriter();
        this(fileOutputStream, Util.UTF_8);
        try {
          outputStreamWriter1.write(param1String);
          return;
        } finally {
          param1String = null;
        } 
      } finally {}
      Util.closeQuietly(outputStreamWriter);
      throw param1String;
    }
  }
  
  private final class Entry {
    File[] cleanFiles;
    
    private DiskLruCache.Editor currentEditor;
    
    File[] dirtyFiles;
    
    private final String key;
    
    private final long[] lengths;
    
    private boolean readable;
    
    private long sequenceNumber;
    
    private Entry(String param1String) {
      this.key = param1String;
      this.lengths = new long[DiskLruCache.this.valueCount];
      this.cleanFiles = new File[DiskLruCache.this.valueCount];
      this.dirtyFiles = new File[DiskLruCache.this.valueCount];
      StringBuilder stringBuilder = new StringBuilder(param1String);
      stringBuilder.append('.');
      int i = stringBuilder.length();
      for (byte b = 0; b < DiskLruCache.this.valueCount; b++) {
        stringBuilder.append(b);
        this.cleanFiles[b] = new File(DiskLruCache.this.directory, stringBuilder.toString());
        stringBuilder.append(".tmp");
        this.dirtyFiles[b] = new File(DiskLruCache.this.directory, stringBuilder.toString());
        stringBuilder.setLength(i);
      } 
    }
    
    private IOException invalidLengths(String[] param1ArrayOfString) throws IOException {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("unexpected journal line: ");
      stringBuilder.append(Arrays.toString((Object[])param1ArrayOfString));
      throw new IOException(stringBuilder.toString());
    }
    
    private void setLengths(String[] param1ArrayOfString) throws IOException {
      if (param1ArrayOfString.length == DiskLruCache.this.valueCount) {
        byte b = 0;
        try {
          while (b < param1ArrayOfString.length) {
            this.lengths[b] = Long.parseLong(param1ArrayOfString[b]);
            b++;
          } 
          return;
        } catch (NumberFormatException numberFormatException) {
          throw invalidLengths(param1ArrayOfString);
        } 
      } 
      IOException iOException = invalidLengths(param1ArrayOfString);
      throw iOException;
    }
    
    public File getCleanFile(int param1Int) {
      return this.cleanFiles[param1Int];
    }
    
    public File getDirtyFile(int param1Int) {
      return this.dirtyFiles[param1Int];
    }
    
    public String getLengths() throws IOException {
      StringBuilder stringBuilder = new StringBuilder();
      for (long l : this.lengths) {
        stringBuilder.append(' ');
        stringBuilder.append(l);
      } 
      return stringBuilder.toString();
    }
  }
  
  public final class Value {
    private final File[] files;
    
    private final String key;
    
    private final long[] lengths;
    
    private final long sequenceNumber;
    
    private Value(String param1String, long param1Long, File[] param1ArrayOfFile, long[] param1ArrayOflong) {
      this.key = param1String;
      this.sequenceNumber = param1Long;
      this.files = param1ArrayOfFile;
      this.lengths = param1ArrayOflong;
    }
    
    public DiskLruCache.Editor edit() throws IOException {
      return DiskLruCache.this.edit(this.key, this.sequenceNumber);
    }
    
    public File getFile(int param1Int) {
      return this.files[param1Int];
    }
    
    public long getLength(int param1Int) {
      return this.lengths[param1Int];
    }
    
    public String getString(int param1Int) throws IOException {
      return DiskLruCache.inputStreamToString(new FileInputStream(this.files[param1Int]));
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/disklrucache/DiskLruCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.sun.jna;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Platform {
  public static final int AIX = 7;
  
  public static final int ANDROID = 8;
  
  public static final String ARCH;
  
  public static final String C_LIBRARY_NAME;
  
  public static final int FREEBSD = 4;
  
  public static final int GNU = 9;
  
  public static final boolean HAS_AWT;
  
  public static final boolean HAS_BUFFERS;
  
  public static final boolean HAS_DLL_CALLBACKS;
  
  public static final boolean HAS_JAWT;
  
  public static final int KFREEBSD = 10;
  
  public static final int LINUX = 1;
  
  public static final int MAC = 0;
  
  public static final String MATH_LIBRARY_NAME;
  
  public static final int NETBSD = 11;
  
  public static final int OPENBSD = 5;
  
  public static final String RESOURCE_PREFIX;
  
  public static final boolean RO_FIELDS;
  
  public static final int SOLARIS = 3;
  
  public static final int UNSPECIFIED = -1;
  
  public static final int WINDOWS = 2;
  
  public static final int WINDOWSCE = 6;
  
  private static final int osType;
  
  static {
    boolean bool2;
    String str1 = System.getProperty("os.name");
    boolean bool = str1.startsWith("Linux");
    boolean bool1 = false;
    if (bool) {
      if ("dalvik".equals(System.getProperty("java.vm.name").toLowerCase())) {
        osType = 8;
        System.setProperty("jna.nounpack", "true");
      } else {
        osType = 1;
      } 
    } else if (str1.startsWith("AIX")) {
      osType = 7;
    } else if (str1.startsWith("Mac") || str1.startsWith("Darwin")) {
      osType = 0;
    } else if (str1.startsWith("Windows CE")) {
      osType = 6;
    } else if (str1.startsWith("Windows")) {
      osType = 2;
    } else if (str1.startsWith("Solaris") || str1.startsWith("SunOS")) {
      osType = 3;
    } else if (str1.startsWith("FreeBSD")) {
      osType = 4;
    } else if (str1.startsWith("OpenBSD")) {
      osType = 5;
    } else if (str1.equalsIgnoreCase("gnu")) {
      osType = 9;
    } else if (str1.equalsIgnoreCase("gnu/kfreebsd")) {
      osType = 10;
    } else if (str1.equalsIgnoreCase("netbsd")) {
      osType = 11;
    } else {
      osType = -1;
    } 
    try {
      Class.forName("java.nio.Buffer");
      bool = true;
    } catch (ClassNotFoundException classNotFoundException) {
      bool = false;
    } 
    int i = osType;
    if (i != 6 && i != 8 && i != 7) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    HAS_AWT = bool2;
    if (HAS_AWT && osType != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    HAS_JAWT = bool2;
    HAS_BUFFERS = bool;
    if (osType != 6) {
      bool = true;
    } else {
      bool = false;
    } 
    RO_FIELDS = bool;
    i = osType;
    String str2 = "coredll";
    if (i == 2) {
      str1 = "msvcrt";
    } else if (i == 6) {
      str1 = "coredll";
    } else {
      str1 = "c";
    } 
    C_LIBRARY_NAME = str1;
    i = osType;
    if (i == 2) {
      str1 = "msvcrt";
    } else if (i == 6) {
      str1 = str2;
    } else {
      str1 = "m";
    } 
    MATH_LIBRARY_NAME = str1;
    bool = bool1;
    if (osType == 2)
      bool = true; 
    HAS_DLL_CALLBACKS = bool;
    ARCH = getCanonicalArchitecture(System.getProperty("os.arch"), osType);
    RESOURCE_PREFIX = getNativeLibraryResourcePrefix();
  }
  
  static String getCanonicalArchitecture(String paramString, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   4: invokevirtual trim : ()Ljava/lang/String;
    //   7: astore_2
    //   8: ldc 'powerpc'
    //   10: aload_2
    //   11: invokevirtual equals : (Ljava/lang/Object;)Z
    //   14: ifeq -> 23
    //   17: ldc 'ppc'
    //   19: astore_0
    //   20: goto -> 88
    //   23: ldc 'powerpc64'
    //   25: aload_2
    //   26: invokevirtual equals : (Ljava/lang/Object;)Z
    //   29: ifeq -> 38
    //   32: ldc 'ppc64'
    //   34: astore_0
    //   35: goto -> 88
    //   38: ldc 'i386'
    //   40: aload_2
    //   41: invokevirtual equals : (Ljava/lang/Object;)Z
    //   44: ifne -> 85
    //   47: ldc 'i686'
    //   49: aload_2
    //   50: invokevirtual equals : (Ljava/lang/Object;)Z
    //   53: ifeq -> 59
    //   56: goto -> 85
    //   59: ldc 'x86_64'
    //   61: aload_2
    //   62: invokevirtual equals : (Ljava/lang/Object;)Z
    //   65: ifne -> 79
    //   68: aload_2
    //   69: astore_0
    //   70: ldc 'amd64'
    //   72: aload_2
    //   73: invokevirtual equals : (Ljava/lang/Object;)Z
    //   76: ifeq -> 88
    //   79: ldc 'x86-64'
    //   81: astore_0
    //   82: goto -> 88
    //   85: ldc 'x86'
    //   87: astore_0
    //   88: aload_0
    //   89: astore_2
    //   90: ldc 'ppc64'
    //   92: aload_0
    //   93: invokevirtual equals : (Ljava/lang/Object;)Z
    //   96: ifeq -> 117
    //   99: aload_0
    //   100: astore_2
    //   101: ldc 'little'
    //   103: ldc 'sun.cpu.endian'
    //   105: invokestatic getProperty : (Ljava/lang/String;)Ljava/lang/String;
    //   108: invokevirtual equals : (Ljava/lang/Object;)Z
    //   111: ifeq -> 117
    //   114: ldc 'ppc64le'
    //   116: astore_2
    //   117: aload_2
    //   118: astore_0
    //   119: ldc 'arm'
    //   121: aload_2
    //   122: invokevirtual equals : (Ljava/lang/Object;)Z
    //   125: ifeq -> 146
    //   128: aload_2
    //   129: astore_0
    //   130: iload_1
    //   131: iconst_1
    //   132: if_icmpne -> 146
    //   135: aload_2
    //   136: astore_0
    //   137: invokestatic isSoftFloat : ()Z
    //   140: ifeq -> 146
    //   143: ldc 'armel'
    //   145: astore_0
    //   146: aload_0
    //   147: areturn
  }
  
  static String getNativeLibraryResourcePrefix() {
    String str = System.getProperty("jna.prefix");
    return (str != null) ? str : getNativeLibraryResourcePrefix(getOSType(), System.getProperty("os.arch"), System.getProperty("os.name"));
  }
  
  static String getNativeLibraryResourcePrefix(int paramInt, String paramString1, String paramString2) {
    StringBuilder stringBuilder1;
    String str = getCanonicalArchitecture(paramString1, paramInt);
    switch (paramInt) {
      default:
        paramString2 = paramString2.toLowerCase();
        paramInt = paramString2.indexOf(" ");
        paramString1 = paramString2;
        if (paramInt != -1)
          paramString1 = paramString2.substring(0, paramInt); 
        break;
      case 11:
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("netbsd-");
        stringBuilder1.append(str);
        return stringBuilder1.toString();
      case 10:
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("kfreebsd-");
        stringBuilder1.append(str);
        return stringBuilder1.toString();
      case 8:
        null = "arm";
        if (!str.startsWith("arm"))
          null = str; 
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("android-");
        stringBuilder2.append(null);
        return stringBuilder2.toString();
      case 6:
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("w32ce-");
        stringBuilder1.append(str);
        return stringBuilder1.toString();
      case 5:
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("openbsd-");
        stringBuilder1.append(str);
        return stringBuilder1.toString();
      case 4:
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("freebsd-");
        stringBuilder1.append(str);
        return stringBuilder1.toString();
      case 3:
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("sunos-");
        stringBuilder1.append(str);
        return stringBuilder1.toString();
      case 2:
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("win32-");
        stringBuilder1.append(str);
        return stringBuilder1.toString();
      case 1:
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("linux-");
        stringBuilder1.append(str);
        return stringBuilder1.toString();
      case 0:
        return "darwin";
    } 
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append((String)stringBuilder1);
    stringBuilder2.append("-");
    stringBuilder2.append(str);
    return stringBuilder2.toString();
  }
  
  public static final int getOSType() {
    return osType;
  }
  
  public static final boolean hasRuntimeExec() {
    return !(isWindowsCE() && "J9".equals(System.getProperty("java.vm.name")));
  }
  
  public static final boolean is64Bit() {
    String str = System.getProperty("sun.arch.data.model", System.getProperty("com.ibm.vm.bitmode"));
    if (str != null)
      return "64".equals(str); 
    boolean bool = "x86-64".equals(ARCH);
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (!bool) {
      bool2 = bool1;
      if (!"ia64".equals(ARCH)) {
        bool2 = bool1;
        if (!"ppc64".equals(ARCH)) {
          bool2 = bool1;
          if (!"ppc64le".equals(ARCH)) {
            bool2 = bool1;
            if (!"sparcv9".equals(ARCH)) {
              bool2 = bool1;
              if (!"mips64".equals(ARCH)) {
                bool2 = bool1;
                if (!"mips64el".equals(ARCH))
                  if ("amd64".equals(ARCH)) {
                    bool2 = bool1;
                  } else if (Native.POINTER_SIZE == 8) {
                    bool2 = bool1;
                  } else {
                    bool2 = false;
                  }  
              } 
            } 
          } 
        } 
      } 
    } 
    return bool2;
  }
  
  public static final boolean isAIX() {
    boolean bool;
    if (osType == 7) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static final boolean isARM() {
    return ARCH.startsWith("arm");
  }
  
  public static final boolean isAndroid() {
    boolean bool;
    if (osType == 8) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static final boolean isFreeBSD() {
    boolean bool;
    if (osType == 4) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static final boolean isGNU() {
    boolean bool;
    if (osType == 9) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static final boolean isIntel() {
    return ARCH.startsWith("x86");
  }
  
  public static final boolean isLinux() {
    int i = osType;
    boolean bool = true;
    if (i != 1)
      bool = false; 
    return bool;
  }
  
  public static final boolean isMIPS() {
    return (ARCH.equals("mips") || ARCH.equals("mips64") || ARCH.equals("mipsel") || ARCH.equals("mips64el"));
  }
  
  public static final boolean isMac() {
    boolean bool;
    if (osType == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static final boolean isNetBSD() {
    boolean bool;
    if (osType == 11) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static final boolean isOpenBSD() {
    boolean bool;
    if (osType == 5) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static final boolean isPPC() {
    return ARCH.startsWith("ppc");
  }
  
  public static final boolean isSPARC() {
    return ARCH.startsWith("sparc");
  }
  
  static boolean isSoftFloat() {
    try {
      File file = new File();
      this("/proc/self/exe");
      if (file.exists()) {
        boolean bool = ELFAnalyser.analyse(file.getCanonicalPath()).isArmHardFloat();
        return bool ^ true;
      } 
    } catch (IOException iOException) {
      Logger.getLogger(Platform.class.getName()).log(Level.INFO, "Failed to read '/proc/self/exe' or the target binary.", iOException);
    } catch (SecurityException securityException) {
      Logger.getLogger(Platform.class.getName()).log(Level.INFO, "SecurityException while analysing '/proc/self/exe' or the target binary.", securityException);
    } 
    return false;
  }
  
  public static final boolean isSolaris() {
    boolean bool;
    if (osType == 3) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static final boolean isWindows() {
    int i = osType;
    return (i == 2 || i == 6);
  }
  
  public static final boolean isWindowsCE() {
    boolean bool;
    if (osType == 6) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static final boolean isX11() {
    boolean bool;
    if (!isWindows() && !isMac()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static final boolean iskFreeBSD() {
    boolean bool;
    if (osType == 10) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/Platform.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
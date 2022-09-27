package com.sun.jna;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class ELFAnalyser {
  private static final int EF_ARM_ABI_FLOAT_HARD = 1024;
  
  private static final int EF_ARM_ABI_FLOAT_SOFT = 512;
  
  private static final int EI_CLASS_64BIT = 2;
  
  private static final int EI_DATA_BIG_ENDIAN = 2;
  
  private static final byte[] ELF_MAGIC = new byte[] { Byte.MAX_VALUE, 69, 76, 70 };
  
  private static final int E_MACHINE_ARM = 40;
  
  private boolean ELF = false;
  
  private boolean _64Bit = false;
  
  private boolean arm = false;
  
  private boolean armEabiAapcsVfp = false;
  
  private boolean armHardFloatFlag = false;
  
  private boolean armSoftFloatFlag = false;
  
  private boolean bigEndian = false;
  
  private final String filename;
  
  private ELFAnalyser(String paramString) {
    this.filename = paramString;
  }
  
  public static ELFAnalyser analyse(String paramString) throws IOException {
    ELFAnalyser eLFAnalyser = new ELFAnalyser(paramString);
    eLFAnalyser.runDetection();
    return eLFAnalyser;
  }
  
  private static Map<Integer, Map<ArmAeabiAttributesTag, Object>> parseAEABI(ByteBuffer paramByteBuffer) {
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    while (paramByteBuffer.position() < paramByteBuffer.limit()) {
      int i = paramByteBuffer.position();
      int j = readULEB128(paramByteBuffer).intValue();
      int k = paramByteBuffer.getInt();
      if (j == 1)
        hashMap.put(Integer.valueOf(j), parseFileAttribute(paramByteBuffer)); 
      paramByteBuffer.position(i + k);
    } 
    return (Map)hashMap;
  }
  
  private static Map<Integer, Map<ArmAeabiAttributesTag, Object>> parseArmAttributes(ByteBuffer paramByteBuffer) {
    if (paramByteBuffer.get() != 65)
      return Collections.EMPTY_MAP; 
    while (paramByteBuffer.position() < paramByteBuffer.limit()) {
      int i = paramByteBuffer.position();
      int j = paramByteBuffer.getInt();
      if (j <= 0)
        break; 
      if ("aeabi".equals(readNTBS(paramByteBuffer, null)))
        return parseAEABI(paramByteBuffer); 
      paramByteBuffer.position(i + j);
    } 
    return Collections.EMPTY_MAP;
  }
  
  private void parseEabiAapcsVfp(ByteBuffer paramByteBuffer, RandomAccessFile paramRandomAccessFile) throws IOException {
    for (ELFSectionHeaderEntry eLFSectionHeaderEntry : (new ELFSectionHeaders(this._64Bit, this.bigEndian, paramByteBuffer, paramRandomAccessFile)).getEntries()) {
      if (".ARM.attributes".equals(eLFSectionHeaderEntry.getName())) {
        ByteOrder byteOrder;
        ByteBuffer byteBuffer = ByteBuffer.allocate(eLFSectionHeaderEntry.getSize());
        if (this.bigEndian) {
          byteOrder = ByteOrder.BIG_ENDIAN;
        } else {
          byteOrder = ByteOrder.LITTLE_ENDIAN;
        } 
        byteBuffer.order(byteOrder);
        paramRandomAccessFile.getChannel().read(byteBuffer, eLFSectionHeaderEntry.getOffset());
        byteBuffer.rewind();
        Map map = parseArmAttributes(byteBuffer).get(Integer.valueOf(1));
        if (map == null)
          continue; 
        map = (Map)map.get(ArmAeabiAttributesTag.ABI_VFP_args);
        if (map instanceof Integer && ((Integer)map).equals(Integer.valueOf(1))) {
          this.armEabiAapcsVfp = true;
          continue;
        } 
        if (map instanceof BigInteger && ((BigInteger)map).intValue() == 1)
          this.armEabiAapcsVfp = true; 
      } 
    } 
  }
  
  private static Map<ArmAeabiAttributesTag, Object> parseFileAttribute(ByteBuffer paramByteBuffer) {
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    while (paramByteBuffer.position() < paramByteBuffer.limit()) {
      ArmAeabiAttributesTag armAeabiAttributesTag = ArmAeabiAttributesTag.getByValue(readULEB128(paramByteBuffer).intValue());
      int i = null.$SwitchMap$com$sun$jna$ELFAnalyser$ArmAeabiAttributesTag$ParameterType[armAeabiAttributesTag.getParameterType().ordinal()];
      if (i != 1) {
        if (i != 2) {
          if (i != 3)
            continue; 
          hashMap.put(armAeabiAttributesTag, readULEB128(paramByteBuffer));
          continue;
        } 
        hashMap.put(armAeabiAttributesTag, readNTBS(paramByteBuffer, null));
        continue;
      } 
      hashMap.put(armAeabiAttributesTag, Integer.valueOf(paramByteBuffer.getInt()));
    } 
    return (Map)hashMap;
  }
  
  private static String readNTBS(ByteBuffer paramByteBuffer, Integer paramInteger) {
    if (paramInteger != null)
      paramByteBuffer.position(paramInteger.intValue()); 
    int i = paramByteBuffer.position();
    do {
    
    } while (paramByteBuffer.get() != 0 && paramByteBuffer.position() <= paramByteBuffer.limit());
    byte[] arrayOfByte = new byte[paramByteBuffer.position() - i - 1];
    paramByteBuffer.position(i);
    paramByteBuffer.get(arrayOfByte);
    paramByteBuffer.position(paramByteBuffer.position() + 1);
    try {
      return new String(arrayOfByte, "ASCII");
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      RuntimeException runtimeException = new RuntimeException(unsupportedEncodingException);
      throw runtimeException;
    } 
  }
  
  private static BigInteger readULEB128(ByteBuffer paramByteBuffer) {
    BigInteger bigInteger = BigInteger.ZERO;
    for (boolean bool = false;; bool += true) {
      byte b = paramByteBuffer.get();
      bigInteger = bigInteger.or(BigInteger.valueOf((b & Byte.MAX_VALUE)).shiftLeft(bool));
      if ((b & 0x80) == 0)
        return bigInteger; 
    } 
  }
  
  private void runDetection() throws IOException {
    RandomAccessFile randomAccessFile = new RandomAccessFile(this.filename, "r");
    try {
      ByteOrder byteOrder;
      if (randomAccessFile.length() > 4L) {
        byte[] arrayOfByte = new byte[4];
        randomAccessFile.seek(0L);
        randomAccessFile.read(arrayOfByte);
        if (Arrays.equals(arrayOfByte, ELF_MAGIC))
          this.ELF = true; 
      } 
      boolean bool = this.ELF;
      if (!bool)
        return; 
      randomAccessFile.seek(4L);
      byte b1 = randomAccessFile.readByte();
      byte b2 = randomAccessFile.readByte();
      boolean bool1 = false;
      if (b1 == 2) {
        bool = true;
      } else {
        bool = false;
      } 
      this._64Bit = bool;
      if (b2 == 2) {
        bool = true;
      } else {
        bool = false;
      } 
      this.bigEndian = bool;
      randomAccessFile.seek(0L);
      if (this._64Bit) {
        b1 = 64;
      } else {
        b1 = 52;
      } 
      ByteBuffer byteBuffer = ByteBuffer.allocate(b1);
      randomAccessFile.getChannel().read(byteBuffer, 0L);
      if (this.bigEndian) {
        byteOrder = ByteOrder.BIG_ENDIAN;
      } else {
        byteOrder = ByteOrder.LITTLE_ENDIAN;
      } 
      byteBuffer.order(byteOrder);
      if (byteBuffer.get(18) == 40) {
        bool = true;
      } else {
        bool = false;
      } 
      this.arm = bool;
      if (this.arm) {
        if (this._64Bit) {
          b1 = 48;
        } else {
          b1 = 36;
        } 
        int i = byteBuffer.getInt(b1);
        if ((i & 0x400) == 1024) {
          bool = true;
        } else {
          bool = false;
        } 
        this.armHardFloatFlag = bool;
        bool = bool1;
        if ((i & 0x200) == 512)
          bool = true; 
        this.armSoftFloatFlag = bool;
        parseEabiAapcsVfp(byteBuffer, randomAccessFile);
      } 
    } finally {
      try {
        randomAccessFile.close();
      } catch (IOException iOException) {}
    } 
  }
  
  public String getFilename() {
    return this.filename;
  }
  
  public boolean is64Bit() {
    return this._64Bit;
  }
  
  public boolean isArm() {
    return this.arm;
  }
  
  public boolean isArmEabiAapcsVfp() {
    return this.armEabiAapcsVfp;
  }
  
  public boolean isArmHardFloat() {
    return (isArmEabiAapcsVfp() || isArmHardFloatFlag());
  }
  
  public boolean isArmHardFloatFlag() {
    return this.armHardFloatFlag;
  }
  
  public boolean isArmSoftFloatFlag() {
    return this.armSoftFloatFlag;
  }
  
  public boolean isBigEndian() {
    return this.bigEndian;
  }
  
  public boolean isELF() {
    return this.ELF;
  }
  
  static class ArmAeabiAttributesTag {
    public static final ArmAeabiAttributesTag ABI_FP_16bit_format;
    
    public static final ArmAeabiAttributesTag ABI_FP_denormal;
    
    public static final ArmAeabiAttributesTag ABI_FP_exceptions;
    
    public static final ArmAeabiAttributesTag ABI_FP_number_model;
    
    public static final ArmAeabiAttributesTag ABI_FP_optimization_goals;
    
    public static final ArmAeabiAttributesTag ABI_FP_rounding;
    
    public static final ArmAeabiAttributesTag ABI_FP_user_exceptions;
    
    public static final ArmAeabiAttributesTag ABI_HardFP_use;
    
    public static final ArmAeabiAttributesTag ABI_PCS_GOT_use;
    
    public static final ArmAeabiAttributesTag ABI_PCS_R9_use;
    
    public static final ArmAeabiAttributesTag ABI_PCS_RO_data;
    
    public static final ArmAeabiAttributesTag ABI_PCS_RW_data;
    
    public static final ArmAeabiAttributesTag ABI_PCS_wchar_t;
    
    public static final ArmAeabiAttributesTag ABI_VFP_args;
    
    public static final ArmAeabiAttributesTag ABI_WMMX_args;
    
    public static final ArmAeabiAttributesTag ABI_align8_preserved;
    
    public static final ArmAeabiAttributesTag ABI_align_needed;
    
    public static final ArmAeabiAttributesTag ABI_enum_size;
    
    public static final ArmAeabiAttributesTag ABI_optimization_goals;
    
    public static final ArmAeabiAttributesTag ARM_ISA_use;
    
    public static final ArmAeabiAttributesTag Advanced_SIMD_arch;
    
    public static final ArmAeabiAttributesTag CPU_arch;
    
    public static final ArmAeabiAttributesTag CPU_arch_profile;
    
    public static final ArmAeabiAttributesTag CPU_name;
    
    public static final ArmAeabiAttributesTag CPU_raw_name;
    
    public static final ArmAeabiAttributesTag CPU_unaligned_access;
    
    public static final ArmAeabiAttributesTag DIV_use;
    
    public static final ArmAeabiAttributesTag FP_HP_extension;
    
    public static final ArmAeabiAttributesTag FP_arch;
    
    public static final ArmAeabiAttributesTag File;
    
    public static final ArmAeabiAttributesTag MPextension_use;
    
    public static final ArmAeabiAttributesTag MPextension_use2;
    
    public static final ArmAeabiAttributesTag PCS_config;
    
    public static final ArmAeabiAttributesTag Section;
    
    public static final ArmAeabiAttributesTag Symbol;
    
    public static final ArmAeabiAttributesTag T2EE_use;
    
    public static final ArmAeabiAttributesTag THUMB_ISA_use;
    
    public static final ArmAeabiAttributesTag Virtualization_use;
    
    public static final ArmAeabiAttributesTag WMMX_arch;
    
    public static final ArmAeabiAttributesTag also_compatible_with;
    
    public static final ArmAeabiAttributesTag compatibility;
    
    public static final ArmAeabiAttributesTag conformance;
    
    private static final Map<String, ArmAeabiAttributesTag> nameMap = new HashMap<String, ArmAeabiAttributesTag>();
    
    public static final ArmAeabiAttributesTag nodefaults;
    
    private static final List<ArmAeabiAttributesTag> tags = new LinkedList<ArmAeabiAttributesTag>();
    
    private static final Map<Integer, ArmAeabiAttributesTag> valueMap = new HashMap<Integer, ArmAeabiAttributesTag>();
    
    private final String name;
    
    private final ParameterType parameterType;
    
    private final int value;
    
    static {
      File = addTag(1, "File", ParameterType.UINT32);
      Section = addTag(2, "Section", ParameterType.UINT32);
      Symbol = addTag(3, "Symbol", ParameterType.UINT32);
      CPU_raw_name = addTag(4, "CPU_raw_name", ParameterType.NTBS);
      CPU_name = addTag(5, "CPU_name", ParameterType.NTBS);
      CPU_arch = addTag(6, "CPU_arch", ParameterType.ULEB128);
      CPU_arch_profile = addTag(7, "CPU_arch_profile", ParameterType.ULEB128);
      ARM_ISA_use = addTag(8, "ARM_ISA_use", ParameterType.ULEB128);
      THUMB_ISA_use = addTag(9, "THUMB_ISA_use", ParameterType.ULEB128);
      FP_arch = addTag(10, "FP_arch", ParameterType.ULEB128);
      WMMX_arch = addTag(11, "WMMX_arch", ParameterType.ULEB128);
      Advanced_SIMD_arch = addTag(12, "Advanced_SIMD_arch", ParameterType.ULEB128);
      PCS_config = addTag(13, "PCS_config", ParameterType.ULEB128);
      ABI_PCS_R9_use = addTag(14, "ABI_PCS_R9_use", ParameterType.ULEB128);
      ABI_PCS_RW_data = addTag(15, "ABI_PCS_RW_data", ParameterType.ULEB128);
      ABI_PCS_RO_data = addTag(16, "ABI_PCS_RO_data", ParameterType.ULEB128);
      ABI_PCS_GOT_use = addTag(17, "ABI_PCS_GOT_use", ParameterType.ULEB128);
      ABI_PCS_wchar_t = addTag(18, "ABI_PCS_wchar_t", ParameterType.ULEB128);
      ABI_FP_rounding = addTag(19, "ABI_FP_rounding", ParameterType.ULEB128);
      ABI_FP_denormal = addTag(20, "ABI_FP_denormal", ParameterType.ULEB128);
      ABI_FP_exceptions = addTag(21, "ABI_FP_exceptions", ParameterType.ULEB128);
      ABI_FP_user_exceptions = addTag(22, "ABI_FP_user_exceptions", ParameterType.ULEB128);
      ABI_FP_number_model = addTag(23, "ABI_FP_number_model", ParameterType.ULEB128);
      ABI_align_needed = addTag(24, "ABI_align_needed", ParameterType.ULEB128);
      ABI_align8_preserved = addTag(25, "ABI_align8_preserved", ParameterType.ULEB128);
      ABI_enum_size = addTag(26, "ABI_enum_size", ParameterType.ULEB128);
      ABI_HardFP_use = addTag(27, "ABI_HardFP_use", ParameterType.ULEB128);
      ABI_VFP_args = addTag(28, "ABI_VFP_args", ParameterType.ULEB128);
      ABI_WMMX_args = addTag(29, "ABI_WMMX_args", ParameterType.ULEB128);
      ABI_optimization_goals = addTag(30, "ABI_optimization_goals", ParameterType.ULEB128);
      ABI_FP_optimization_goals = addTag(31, "ABI_FP_optimization_goals", ParameterType.ULEB128);
      compatibility = addTag(32, "compatibility", ParameterType.NTBS);
      CPU_unaligned_access = addTag(34, "CPU_unaligned_access", ParameterType.ULEB128);
      FP_HP_extension = addTag(36, "FP_HP_extension", ParameterType.ULEB128);
      ABI_FP_16bit_format = addTag(38, "ABI_FP_16bit_format", ParameterType.ULEB128);
      MPextension_use = addTag(42, "MPextension_use", ParameterType.ULEB128);
      DIV_use = addTag(44, "DIV_use", ParameterType.ULEB128);
      nodefaults = addTag(64, "nodefaults", ParameterType.ULEB128);
      also_compatible_with = addTag(65, "also_compatible_with", ParameterType.NTBS);
      conformance = addTag(67, "conformance", ParameterType.NTBS);
      T2EE_use = addTag(66, "T2EE_use", ParameterType.ULEB128);
      Virtualization_use = addTag(68, "Virtualization_use", ParameterType.ULEB128);
      MPextension_use2 = addTag(70, "MPextension_use", ParameterType.ULEB128);
    }
    
    public ArmAeabiAttributesTag(int param1Int, String param1String, ParameterType param1ParameterType) {
      this.value = param1Int;
      this.name = param1String;
      this.parameterType = param1ParameterType;
    }
    
    private static ArmAeabiAttributesTag addTag(int param1Int, String param1String, ParameterType param1ParameterType) {
      ArmAeabiAttributesTag armAeabiAttributesTag = new ArmAeabiAttributesTag(param1Int, param1String, param1ParameterType);
      if (!valueMap.containsKey(Integer.valueOf(armAeabiAttributesTag.getValue())))
        valueMap.put(Integer.valueOf(armAeabiAttributesTag.getValue()), armAeabiAttributesTag); 
      if (!nameMap.containsKey(armAeabiAttributesTag.getName()))
        nameMap.put(armAeabiAttributesTag.getName(), armAeabiAttributesTag); 
      tags.add(armAeabiAttributesTag);
      return armAeabiAttributesTag;
    }
    
    public static ArmAeabiAttributesTag getByName(String param1String) {
      return nameMap.get(param1String);
    }
    
    public static ArmAeabiAttributesTag getByValue(int param1Int) {
      if (valueMap.containsKey(Integer.valueOf(param1Int)))
        return valueMap.get(Integer.valueOf(param1Int)); 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unknown ");
      stringBuilder.append(param1Int);
      return new ArmAeabiAttributesTag(param1Int, stringBuilder.toString(), getParameterType(param1Int));
    }
    
    private static ParameterType getParameterType(int param1Int) {
      ArmAeabiAttributesTag armAeabiAttributesTag = getByValue(param1Int);
      return (armAeabiAttributesTag == null) ? ((param1Int % 2 == 0) ? ParameterType.ULEB128 : ParameterType.NTBS) : armAeabiAttributesTag.getParameterType();
    }
    
    public static List<ArmAeabiAttributesTag> getTags() {
      return Collections.unmodifiableList(tags);
    }
    
    public boolean equals(Object param1Object) {
      if (this == param1Object)
        return true; 
      if (param1Object == null)
        return false; 
      if (getClass() != param1Object.getClass())
        return false; 
      param1Object = param1Object;
      return !(this.value != ((ArmAeabiAttributesTag)param1Object).value);
    }
    
    public String getName() {
      return this.name;
    }
    
    public ParameterType getParameterType() {
      return this.parameterType;
    }
    
    public int getValue() {
      return this.value;
    }
    
    public int hashCode() {
      return 469 + this.value;
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.name);
      stringBuilder.append(" (");
      stringBuilder.append(this.value);
      stringBuilder.append(")");
      return stringBuilder.toString();
    }
    
    public enum ParameterType {
      NTBS, UINT32, ULEB128;
      
      static {
      
      }
    }
  }
  
  public enum ParameterType {
    NTBS, UINT32, ULEB128;
    
    static {
      $VALUES = new ParameterType[] { UINT32, NTBS, ULEB128 };
    }
  }
  
  static class ELFSectionHeaderEntry {
    private final int flags;
    
    private String name;
    
    private final int nameOffset;
    
    private final int offset;
    
    private final int size;
    
    private final int type;
    
    public ELFSectionHeaderEntry(boolean param1Boolean, ByteBuffer param1ByteBuffer) {
      long l;
      this.nameOffset = param1ByteBuffer.getInt(0);
      this.type = param1ByteBuffer.getInt(4);
      if (param1Boolean) {
        l = param1ByteBuffer.getLong(8);
      } else {
        l = param1ByteBuffer.getInt(8);
      } 
      this.flags = (int)l;
      if (param1Boolean) {
        l = param1ByteBuffer.getLong(24);
      } else {
        l = param1ByteBuffer.getInt(16);
      } 
      this.offset = (int)l;
      if (param1Boolean) {
        l = param1ByteBuffer.getLong(32);
      } else {
        l = param1ByteBuffer.getInt(20);
      } 
      this.size = (int)l;
    }
    
    public int getFlags() {
      return this.flags;
    }
    
    public String getName() {
      return this.name;
    }
    
    public int getNameOffset() {
      return this.nameOffset;
    }
    
    public int getOffset() {
      return this.offset;
    }
    
    public int getSize() {
      return this.size;
    }
    
    public int getType() {
      return this.type;
    }
    
    public void setName(String param1String) {
      this.name = param1String;
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("ELFSectionHeaderEntry{nameIdx=");
      stringBuilder.append(this.nameOffset);
      stringBuilder.append(", name=");
      stringBuilder.append(this.name);
      stringBuilder.append(", type=");
      stringBuilder.append(this.type);
      stringBuilder.append(", flags=");
      stringBuilder.append(this.flags);
      stringBuilder.append(", offset=");
      stringBuilder.append(this.offset);
      stringBuilder.append(", size=");
      stringBuilder.append(this.size);
      stringBuilder.append('}');
      return stringBuilder.toString();
    }
  }
  
  static class ELFSectionHeaders {
    private final List<ELFAnalyser.ELFSectionHeaderEntry> entries;
    
    public ELFSectionHeaders(boolean param1Boolean1, boolean param1Boolean2, ByteBuffer param1ByteBuffer, RandomAccessFile param1RandomAccessFile) throws IOException {
      ByteOrder byteOrder;
      long l;
      short s1;
      short s2;
      short s3;
      this.entries = new ArrayList<ELFAnalyser.ELFSectionHeaderEntry>();
      if (param1Boolean1) {
        l = param1ByteBuffer.getLong(40);
        s1 = param1ByteBuffer.getShort(58);
        s2 = param1ByteBuffer.getShort(60);
        s3 = param1ByteBuffer.getShort(62);
      } else {
        l = param1ByteBuffer.getInt(32);
        s1 = param1ByteBuffer.getShort(46);
        s2 = param1ByteBuffer.getShort(48);
        s3 = param1ByteBuffer.getShort(50);
      } 
      ByteBuffer byteBuffer = ByteBuffer.allocate(s2 * s1);
      if (param1Boolean2) {
        byteOrder = ByteOrder.BIG_ENDIAN;
      } else {
        byteOrder = ByteOrder.LITTLE_ENDIAN;
      } 
      byteBuffer.order(byteOrder);
      param1RandomAccessFile.getChannel().read(byteBuffer, l);
      for (byte b = 0; b < s2; b++) {
        byteBuffer.position(b * s1);
        ByteBuffer byteBuffer1 = byteBuffer.slice();
        byteBuffer1.order(byteBuffer.order());
        byteBuffer1.limit(s1);
        this.entries.add(new ELFAnalyser.ELFSectionHeaderEntry(param1Boolean1, byteBuffer1));
      } 
      eLFSectionHeaderEntry = this.entries.get(s3);
      byteBuffer = ByteBuffer.allocate(eLFSectionHeaderEntry.getSize());
      if (param1Boolean2) {
        byteOrder = ByteOrder.BIG_ENDIAN;
      } else {
        byteOrder = ByteOrder.LITTLE_ENDIAN;
      } 
      byteBuffer.order(byteOrder);
      param1RandomAccessFile.getChannel().read(byteBuffer, eLFSectionHeaderEntry.getOffset());
      byteBuffer.rewind();
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(20);
      for (ELFAnalyser.ELFSectionHeaderEntry eLFSectionHeaderEntry : this.entries) {
        byteArrayOutputStream.reset();
        byteBuffer.position(eLFSectionHeaderEntry.getNameOffset());
        while (byteBuffer.position() < byteBuffer.limit()) {
          s1 = byteBuffer.get();
          if (s1 == 0)
            break; 
          byteArrayOutputStream.write(s1);
        } 
        eLFSectionHeaderEntry.setName(byteArrayOutputStream.toString("ASCII"));
      } 
    }
    
    public List<ELFAnalyser.ELFSectionHeaderEntry> getEntries() {
      return this.entries;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/ELFAnalyser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
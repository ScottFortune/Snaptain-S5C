package edu.cmu.pocketsphinx;

public class Decoder {
  protected transient boolean swigCMemOwn;
  
  private transient long swigCPtr;
  
  public Decoder() {
    this(PocketSphinxJNI.new_Decoder__SWIG_0(), true);
  }
  
  protected Decoder(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  public Decoder(Config paramConfig) {
    this(PocketSphinxJNI.new_Decoder__SWIG_1(Config.getCPtr(paramConfig), paramConfig), true);
  }
  
  public static Config defaultConfig() {
    Config config;
    long l = PocketSphinxJNI.Decoder_defaultConfig();
    if (l == 0L) {
      config = null;
    } else {
      config = new Config(l, true);
    } 
    return config;
  }
  
  public static Config fileConfig(String paramString) {
    Config config;
    long l = PocketSphinxJNI.Decoder_fileConfig(paramString);
    if (l == 0L) {
      paramString = null;
    } else {
      config = new Config(l, true);
    } 
    return config;
  }
  
  protected static long getCPtr(Decoder paramDecoder) {
    long l;
    if (paramDecoder == null) {
      l = 0L;
    } else {
      l = paramDecoder.swigCPtr;
    } 
    return l;
  }
  
  public void addWord(String paramString1, String paramString2, int paramInt) {
    PocketSphinxJNI.Decoder_addWord(this.swigCPtr, this, paramString1, paramString2, paramInt);
  }
  
  public void delete() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield swigCPtr : J
    //   6: lconst_0
    //   7: lcmp
    //   8: ifeq -> 35
    //   11: aload_0
    //   12: getfield swigCMemOwn : Z
    //   15: ifeq -> 30
    //   18: aload_0
    //   19: iconst_0
    //   20: putfield swigCMemOwn : Z
    //   23: aload_0
    //   24: getfield swigCPtr : J
    //   27: invokestatic delete_Decoder : (J)V
    //   30: aload_0
    //   31: lconst_0
    //   32: putfield swigCPtr : J
    //   35: aload_0
    //   36: monitorexit
    //   37: return
    //   38: astore_1
    //   39: aload_0
    //   40: monitorexit
    //   41: aload_1
    //   42: athrow
    // Exception table:
    //   from	to	target	type
    //   2	30	38	finally
    //   30	35	38	finally
  }
  
  public void endUtt() {
    PocketSphinxJNI.Decoder_endUtt(this.swigCPtr, this);
  }
  
  protected void finalize() {
    delete();
  }
  
  public Config getConfig() {
    Config config;
    long l = PocketSphinxJNI.Decoder_getConfig(this.swigCPtr, this);
    if (l == 0L) {
      config = null;
    } else {
      config = new Config(l, true);
    } 
    return config;
  }
  
  public FrontEnd getFe() {
    FrontEnd frontEnd;
    long l = PocketSphinxJNI.Decoder_getFe(this.swigCPtr, this);
    if (l == 0L) {
      frontEnd = null;
    } else {
      frontEnd = new FrontEnd(l, false);
    } 
    return frontEnd;
  }
  
  public Feature getFeat() {
    Feature feature;
    long l = PocketSphinxJNI.Decoder_getFeat(this.swigCPtr, this);
    if (l == 0L) {
      feature = null;
    } else {
      feature = new Feature(l, false);
    } 
    return feature;
  }
  
  public FsgModel getFsg(String paramString) {
    FsgModel fsgModel;
    long l = PocketSphinxJNI.Decoder_getFsg(this.swigCPtr, this, paramString);
    if (l == 0L) {
      paramString = null;
    } else {
      fsgModel = new FsgModel(l, false);
    } 
    return fsgModel;
  }
  
  public boolean getInSpeech() {
    return PocketSphinxJNI.Decoder_getInSpeech(this.swigCPtr, this);
  }
  
  public String getKws(String paramString) {
    return PocketSphinxJNI.Decoder_getKws(this.swigCPtr, this, paramString);
  }
  
  public Lattice getLattice() {
    Lattice lattice;
    long l = PocketSphinxJNI.Decoder_getLattice(this.swigCPtr, this);
    if (l == 0L) {
      lattice = null;
    } else {
      lattice = new Lattice(l, false);
    } 
    return lattice;
  }
  
  public NGramModel getLm(String paramString) {
    NGramModel nGramModel;
    long l = PocketSphinxJNI.Decoder_getLm(this.swigCPtr, this, paramString);
    if (l == 0L) {
      paramString = null;
    } else {
      nGramModel = new NGramModel(l, true);
    } 
    return nGramModel;
  }
  
  public LogMath getLogmath() {
    LogMath logMath;
    long l = PocketSphinxJNI.Decoder_getLogmath(this.swigCPtr, this);
    if (l == 0L) {
      logMath = null;
    } else {
      logMath = new LogMath(l, true);
    } 
    return logMath;
  }
  
  public short[] getRawdata() {
    return PocketSphinxJNI.Decoder_getRawdata(this.swigCPtr, this);
  }
  
  public String getSearch() {
    return PocketSphinxJNI.Decoder_getSearch(this.swigCPtr, this);
  }
  
  public Hypothesis hyp() {
    Hypothesis hypothesis;
    long l = PocketSphinxJNI.Decoder_hyp(this.swigCPtr, this);
    if (l == 0L) {
      hypothesis = null;
    } else {
      hypothesis = new Hypothesis(l, true);
    } 
    return hypothesis;
  }
  
  public void loadDict(String paramString1, String paramString2, String paramString3) {
    PocketSphinxJNI.Decoder_loadDict(this.swigCPtr, this, paramString1, paramString2, paramString3);
  }
  
  public String lookupWord(String paramString) {
    return PocketSphinxJNI.Decoder_lookupWord(this.swigCPtr, this, paramString);
  }
  
  public int nFrames() {
    return PocketSphinxJNI.Decoder_nFrames(this.swigCPtr, this);
  }
  
  public NBestList nbest() {
    NBestList nBestList;
    long l = PocketSphinxJNI.Decoder_nbest(this.swigCPtr, this);
    if (l == 0L) {
      nBestList = null;
    } else {
      nBestList = new NBestList(l, false);
    } 
    return nBestList;
  }
  
  public int processRaw(short[] paramArrayOfshort, long paramLong, boolean paramBoolean1, boolean paramBoolean2) {
    return PocketSphinxJNI.Decoder_processRaw(this.swigCPtr, this, paramArrayOfshort, paramLong, paramBoolean1, paramBoolean2);
  }
  
  public void reinit(Config paramConfig) {
    PocketSphinxJNI.Decoder_reinit(this.swigCPtr, this, Config.getCPtr(paramConfig), paramConfig);
  }
  
  public void saveDict(String paramString1, String paramString2) {
    PocketSphinxJNI.Decoder_saveDict(this.swigCPtr, this, paramString1, paramString2);
  }
  
  public SegmentList seg() {
    SegmentList segmentList;
    long l = PocketSphinxJNI.Decoder_seg(this.swigCPtr, this);
    if (l == 0L) {
      segmentList = null;
    } else {
      segmentList = new SegmentList(l, false);
    } 
    return segmentList;
  }
  
  public void setAllphoneFile(String paramString1, String paramString2) {
    PocketSphinxJNI.Decoder_setAllphoneFile(this.swigCPtr, this, paramString1, paramString2);
  }
  
  public void setFsg(String paramString, FsgModel paramFsgModel) {
    PocketSphinxJNI.Decoder_setFsg(this.swigCPtr, this, paramString, FsgModel.getCPtr(paramFsgModel), paramFsgModel);
  }
  
  public void setJsgfFile(String paramString1, String paramString2) {
    PocketSphinxJNI.Decoder_setJsgfFile(this.swigCPtr, this, paramString1, paramString2);
  }
  
  public void setJsgfString(String paramString1, String paramString2) {
    PocketSphinxJNI.Decoder_setJsgfString(this.swigCPtr, this, paramString1, paramString2);
  }
  
  public void setKeyphrase(String paramString1, String paramString2) {
    PocketSphinxJNI.Decoder_setKeyphrase(this.swigCPtr, this, paramString1, paramString2);
  }
  
  public void setKws(String paramString1, String paramString2) {
    PocketSphinxJNI.Decoder_setKws(this.swigCPtr, this, paramString1, paramString2);
  }
  
  public void setLm(String paramString, NGramModel paramNGramModel) {
    PocketSphinxJNI.Decoder_setLm(this.swigCPtr, this, paramString, NGramModel.getCPtr(paramNGramModel), paramNGramModel);
  }
  
  public void setLmFile(String paramString1, String paramString2) {
    PocketSphinxJNI.Decoder_setLmFile(this.swigCPtr, this, paramString1, paramString2);
  }
  
  public void setRawdataSize(long paramLong) {
    PocketSphinxJNI.Decoder_setRawdataSize(this.swigCPtr, this, paramLong);
  }
  
  public void setSearch(String paramString) {
    PocketSphinxJNI.Decoder_setSearch(this.swigCPtr, this, paramString);
  }
  
  public void startStream() {
    PocketSphinxJNI.Decoder_startStream(this.swigCPtr, this);
  }
  
  public void startUtt() {
    PocketSphinxJNI.Decoder_startUtt(this.swigCPtr, this);
  }
  
  public void unsetSearch(String paramString) {
    PocketSphinxJNI.Decoder_unsetSearch(this.swigCPtr, this, paramString);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/edu/cmu/pocketsphinx/Decoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
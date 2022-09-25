package edu.cmu.pocketsphinx;

import java.io.File;
import java.io.IOException;

public class SpeechRecognizerSetup {
  private final Config config;
  
  static {
    System.loadLibrary("pocketsphinx_jni");
  }
  
  private SpeechRecognizerSetup(Config paramConfig) {
    this.config = paramConfig;
  }
  
  public static SpeechRecognizerSetup defaultSetup() {
    return new SpeechRecognizerSetup(Decoder.defaultConfig());
  }
  
  public static SpeechRecognizerSetup setupFromFile(File paramFile) {
    return new SpeechRecognizerSetup(Decoder.fileConfig(paramFile.getPath()));
  }
  
  public SpeechRecognizer getRecognizer() throws IOException {
    return new SpeechRecognizer(this.config);
  }
  
  public SpeechRecognizerSetup setAcousticModel(File paramFile) {
    return setString("-hmm", paramFile.getPath());
  }
  
  public SpeechRecognizerSetup setBoolean(String paramString, boolean paramBoolean) {
    this.config.setBoolean(paramString, paramBoolean);
    return this;
  }
  
  public SpeechRecognizerSetup setDictionary(File paramFile) {
    return setString("-dict", paramFile.getPath());
  }
  
  public SpeechRecognizerSetup setFloat(String paramString, double paramDouble) {
    this.config.setFloat(paramString, paramDouble);
    return this;
  }
  
  public SpeechRecognizerSetup setInteger(String paramString, int paramInt) {
    this.config.setInt(paramString, paramInt);
    return this;
  }
  
  public SpeechRecognizerSetup setKeywordThreshold(float paramFloat) {
    return setFloat("-kws_threshold", paramFloat);
  }
  
  public SpeechRecognizerSetup setRawLogDir(File paramFile) {
    return setString("-rawlogdir", paramFile.getPath());
  }
  
  public SpeechRecognizerSetup setSampleRate(int paramInt) {
    return setFloat("-samprate", paramInt);
  }
  
  public SpeechRecognizerSetup setString(String paramString1, String paramString2) {
    this.config.setString(paramString1, paramString2);
    return this;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/edu/cmu/pocketsphinx/SpeechRecognizerSetup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
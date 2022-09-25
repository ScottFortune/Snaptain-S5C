package edu.cmu.pocketsphinx;

public interface RecognitionListener {
  void onBeginningOfSpeech();
  
  void onEndOfSpeech();
  
  void onError(Exception paramException);
  
  void onPartialResult(Hypothesis paramHypothesis);
  
  void onResult(Hypothesis paramHypothesis);
  
  void onTimeout();
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/edu/cmu/pocketsphinx/RecognitionListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
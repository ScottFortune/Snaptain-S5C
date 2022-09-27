package edu.cmu.pocketsphinx;

import android.media.AudioRecord;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

public class SpeechRecognizer {
  private static final float BUFFER_SIZE_SECONDS = 0.4F;
  
  protected static final String TAG = SpeechRecognizer.class.getSimpleName();
  
  private int bufferSize;
  
  private final Decoder decoder;
  
  private final Collection<RecognitionListener> listeners = new HashSet<RecognitionListener>();
  
  private final Handler mainHandler = new Handler(Looper.getMainLooper());
  
  private Thread recognizerThread;
  
  private final AudioRecord recorder;
  
  private final int sampleRate;
  
  protected SpeechRecognizer(Config paramConfig) throws IOException {
    this.decoder = new Decoder(paramConfig);
    this.sampleRate = (int)this.decoder.getConfig().getFloat("-samprate");
    this.bufferSize = Math.round(this.sampleRate * 0.4F);
    this.recorder = new AudioRecord(6, this.sampleRate, 16, 2, this.bufferSize * 2);
    if (this.recorder.getState() != 0)
      return; 
    this.recorder.release();
    throw new IOException("Failed to initialize recorder. Microphone might be already in use.");
  }
  
  private boolean stopRecognizerThread() {
    Thread thread = this.recognizerThread;
    if (thread == null)
      return false; 
    try {
      thread.interrupt();
      this.recognizerThread.join();
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
    } 
    this.recognizerThread = null;
    return true;
  }
  
  public void addAllphoneSearch(String paramString, File paramFile) {
    this.decoder.setAllphoneFile(paramString, paramFile.getPath());
  }
  
  public void addFsgSearch(String paramString, FsgModel paramFsgModel) {
    this.decoder.setFsg(paramString, paramFsgModel);
  }
  
  public void addGrammarSearch(String paramString, File paramFile) {
    Log.i(TAG, String.format("Load JSGF %s", new Object[] { paramFile }));
    this.decoder.setJsgfFile(paramString, paramFile.getPath());
  }
  
  public void addGrammarSearch(String paramString1, String paramString2) {
    this.decoder.setJsgfString(paramString1, paramString2);
  }
  
  public void addKeyphraseSearch(String paramString1, String paramString2) {
    this.decoder.setKeyphrase(paramString1, paramString2);
  }
  
  public void addKeywordSearch(String paramString, File paramFile) {
    this.decoder.setKws(paramString, paramFile.getPath());
  }
  
  public void addListener(RecognitionListener paramRecognitionListener) {
    synchronized (this.listeners) {
      this.listeners.add(paramRecognitionListener);
      return;
    } 
  }
  
  public void addNgramSearch(String paramString, File paramFile) {
    Log.i(TAG, String.format("Load N-gram model %s", new Object[] { paramFile }));
    this.decoder.setLmFile(paramString, paramFile.getPath());
  }
  
  public boolean cancel() {
    boolean bool = stopRecognizerThread();
    if (bool)
      Log.i(TAG, "Cancel recognition"); 
    return bool;
  }
  
  public Decoder getDecoder() {
    return this.decoder;
  }
  
  public String getSearchName() {
    return this.decoder.getSearch();
  }
  
  public void removeListener(RecognitionListener paramRecognitionListener) {
    synchronized (this.listeners) {
      this.listeners.remove(paramRecognitionListener);
      return;
    } 
  }
  
  public void shutdown() {
    this.recorder.release();
  }
  
  public boolean startListening(String paramString) {
    if (this.recognizerThread != null)
      return false; 
    Log.i(TAG, String.format("Start recognition \"%s\"", new Object[] { paramString }));
    this.decoder.setSearch(paramString);
    this.recognizerThread = new RecognizerThread();
    this.recognizerThread.start();
    return true;
  }
  
  public boolean startListening(String paramString, int paramInt) {
    if (this.recognizerThread != null)
      return false; 
    Log.i(TAG, String.format("Start recognition \"%s\"", new Object[] { paramString }));
    this.decoder.setSearch(paramString);
    this.recognizerThread = new RecognizerThread(paramInt);
    this.recognizerThread.start();
    return true;
  }
  
  public boolean stop() {
    boolean bool = stopRecognizerThread();
    if (bool) {
      Log.i(TAG, "Stop recognition");
      Hypothesis hypothesis = this.decoder.hyp();
      this.mainHandler.post(new ResultEvent(hypothesis, true));
    } 
    return bool;
  }
  
  private class InSpeechChangeEvent extends RecognitionEvent {
    private final boolean state;
    
    InSpeechChangeEvent(boolean param1Boolean) {
      this.state = param1Boolean;
    }
    
    protected void execute(RecognitionListener param1RecognitionListener) {
      if (this.state) {
        param1RecognitionListener.onBeginningOfSpeech();
      } else {
        param1RecognitionListener.onEndOfSpeech();
      } 
    }
  }
  
  private class OnErrorEvent extends RecognitionEvent {
    private final Exception exception;
    
    OnErrorEvent(Exception param1Exception) {
      this.exception = param1Exception;
    }
    
    protected void execute(RecognitionListener param1RecognitionListener) {
      param1RecognitionListener.onError(this.exception);
    }
  }
  
  private abstract class RecognitionEvent implements Runnable {
    private RecognitionEvent() {}
    
    protected abstract void execute(RecognitionListener param1RecognitionListener);
    
    public void run() {
      byte b = 0;
      RecognitionListener[] arrayOfRecognitionListener = (RecognitionListener[])SpeechRecognizer.this.listeners.toArray((Object[])new RecognitionListener[0]);
      int i = arrayOfRecognitionListener.length;
      while (b < i) {
        execute(arrayOfRecognitionListener[b]);
        b++;
      } 
    }
  }
  
  private final class RecognizerThread extends Thread {
    private static final int NO_TIMEOUT = -1;
    
    private int remainingSamples;
    
    private int timeoutSamples;
    
    public RecognizerThread() {
      this(-1);
    }
    
    public RecognizerThread(int param1Int) {
      if (param1Int != -1) {
        this.timeoutSamples = param1Int * SpeechRecognizer.this.sampleRate / 1000;
      } else {
        this.timeoutSamples = -1;
      } 
      this.remainingSamples = this.timeoutSamples;
    }
    
    public void run() {
      SpeechRecognizer.this.recorder.startRecording();
      if (SpeechRecognizer.this.recorder.getRecordingState() == 1) {
        SpeechRecognizer.this.recorder.stop();
        IOException iOException = new IOException("Failed to start recording. Microphone might be already in use.");
        SpeechRecognizer.this.mainHandler.post(new SpeechRecognizer.OnErrorEvent(iOException));
        return;
      } 
      Log.d(SpeechRecognizer.TAG, "Starting decoding");
      SpeechRecognizer.this.decoder.startUtt();
      short[] arrayOfShort = new short[SpeechRecognizer.this.bufferSize];
      boolean bool = SpeechRecognizer.this.decoder.getInSpeech();
      SpeechRecognizer.this.recorder.read(arrayOfShort, 0, arrayOfShort.length);
      while (!interrupted() && (this.timeoutSamples == -1 || this.remainingSamples > 0)) {
        int i = SpeechRecognizer.this.recorder.read(arrayOfShort, 0, arrayOfShort.length);
        if (-1 != i) {
          boolean bool1 = bool;
          if (i > 0) {
            SpeechRecognizer.this.decoder.processRaw(arrayOfShort, i, false, false);
            bool1 = bool;
            if (SpeechRecognizer.this.decoder.getInSpeech() != bool) {
              bool1 = SpeechRecognizer.this.decoder.getInSpeech();
              SpeechRecognizer.this.mainHandler.post(new SpeechRecognizer.InSpeechChangeEvent(bool1));
            } 
            if (bool1)
              this.remainingSamples = this.timeoutSamples; 
            Hypothesis hypothesis = SpeechRecognizer.this.decoder.hyp();
            SpeechRecognizer.this.mainHandler.post(new SpeechRecognizer.ResultEvent(hypothesis, false));
          } 
          bool = bool1;
          if (this.timeoutSamples != -1) {
            this.remainingSamples -= i;
            bool = bool1;
          } 
          continue;
        } 
        throw new RuntimeException("error reading audio buffer");
      } 
      SpeechRecognizer.this.recorder.stop();
      SpeechRecognizer.this.decoder.endUtt();
      SpeechRecognizer.this.mainHandler.removeCallbacksAndMessages(null);
      if (this.timeoutSamples != -1 && this.remainingSamples <= 0)
        SpeechRecognizer.this.mainHandler.post(new SpeechRecognizer.TimeoutEvent()); 
    }
  }
  
  private class ResultEvent extends RecognitionEvent {
    private final boolean finalResult;
    
    protected final Hypothesis hypothesis;
    
    ResultEvent(Hypothesis param1Hypothesis, boolean param1Boolean) {
      this.hypothesis = param1Hypothesis;
      this.finalResult = param1Boolean;
    }
    
    protected void execute(RecognitionListener param1RecognitionListener) {
      if (this.finalResult) {
        param1RecognitionListener.onResult(this.hypothesis);
      } else {
        param1RecognitionListener.onPartialResult(this.hypothesis);
      } 
    }
  }
  
  private class TimeoutEvent extends RecognitionEvent {
    private TimeoutEvent() {}
    
    protected void execute(RecognitionListener param1RecognitionListener) {
      param1RecognitionListener.onTimeout();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/edu/cmu/pocketsphinx/SpeechRecognizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.netopsun.voicecommandrecognizer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import androidx.core.content.ContextCompat;
import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class VoiceRecognizer implements RecognitionListener {
  public static final int COMMAND_BACKWARD = 5;
  
  public static final int COMMAND_FALLING = 3;
  
  public static final int COMMAND_FORWARD = 4;
  
  public static final int COMMAND_LAND = 1;
  
  public static final int COMMAND_LEFT = 6;
  
  public static final int COMMAND_NOVIDEO = 10;
  
  public static final int COMMAND_RIGHT = 7;
  
  public static final int COMMAND_RISE = 2;
  
  public static final int COMMAND_SHOOT = 8;
  
  public static final int COMMAND_TAKEOFF = 0;
  
  public static final int COMMAND_VIDEO = 9;
  
  private final String COMMANDS = "COMMANDS";
  
  private String[] commandArray;
  
  private OnCommandCallback commandCallback;
  
  private File commandPhrasefile;
  
  private final Context context;
  
  private volatile boolean isStart = false;
  
  private SpeechRecognizer recognizer;
  
  public VoiceRecognizer(Context paramContext) {
    this.context = paramContext;
    if (ContextCompat.checkSelfPermission(paramContext.getApplicationContext(), "android.permission.RECORD_AUDIO") != 0) {
      Log.e("VoiceRecognizer", "缺少权限");
      return;
    } 
    if (ContextCompat.checkSelfPermission(paramContext.getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
      Log.e("VoiceRecognizer", "缺少权限");
      return;
    } 
    if (ContextCompat.checkSelfPermission(paramContext.getApplicationContext(), "android.permission.READ_EXTERNAL_STORAGE") != 0) {
      Log.e("VoiceRecognizer", "缺少权限");
      return;
    } 
    (new SetupTask(this)).execute((Object[])new Void[0]);
  }
  
  private int getCommandPosition(String paramString) {
    if (this.commandArray == null)
      return -1; 
    byte b = 0;
    while (true) {
      String[] arrayOfString = this.commandArray;
      if (b < arrayOfString.length) {
        if (arrayOfString[b].contains(paramString))
          return b; 
        b++;
        continue;
      } 
      return -1;
    } 
  }
  
  private Context getContext() {
    return this.context;
  }
  
  private static String readString(File paramFile) {
    try {
      FileInputStream fileInputStream = new FileInputStream();
      this(paramFile);
      byte[] arrayOfByte = new byte[fileInputStream.available()];
      fileInputStream.read(arrayOfByte);
      fileInputStream.close();
      return new String(arrayOfByte, "utf-8");
    } catch (IOException iOException) {
      iOException.printStackTrace();
      return null;
    } 
  }
  
  private void setupRecognizer(File paramFile) throws IOException {
    this.commandPhrasefile = new File(paramFile, this.context.getString(R.string.command_file));
    this.recognizer = SpeechRecognizerSetup.defaultSetup().setAcousticModel(new File(paramFile, this.context.getString(R.string.ptm_directory))).setDictionary(new File(paramFile, this.context.getString(R.string.dict_file))).setRawLogDir(paramFile).getRecognizer();
    this.recognizer.addListener(this);
    this.commandArray = readString(this.commandPhrasefile).split("\\|");
    this.recognizer.addGrammarSearch("COMMANDS", this.commandPhrasefile);
    if (this.isStart)
      this.recognizer.startListening("COMMANDS"); 
  }
  
  public void onBeginningOfSpeech() {}
  
  public void onEndOfSpeech() {}
  
  public void onError(Exception paramException) {}
  
  public void onPartialResult(Hypothesis paramHypothesis) {
    if (paramHypothesis == null)
      return; 
    SpeechRecognizer speechRecognizer = this.recognizer;
    if (speechRecognizer != null) {
      speechRecognizer.stop();
      this.recognizer.startListening("COMMANDS");
    } 
  }
  
  public void onResult(Hypothesis paramHypothesis) {
    if (paramHypothesis != null) {
      String str = paramHypothesis.getHypstr();
      OnCommandCallback onCommandCallback = this.commandCallback;
      if (onCommandCallback != null)
        onCommandCallback.onCommand(getCommandPosition(str), str); 
    } 
  }
  
  public void onTimeout() {}
  
  public void release() {
    SpeechRecognizer speechRecognizer = this.recognizer;
    if (speechRecognizer != null) {
      speechRecognizer.cancel();
      this.recognizer.shutdown();
    } 
    this.isStart = false;
  }
  
  public void start(OnCommandCallback paramOnCommandCallback) {
    this.commandCallback = paramOnCommandCallback;
    SpeechRecognizer speechRecognizer = this.recognizer;
    if (speechRecognizer != null) {
      speechRecognizer.stop();
      this.recognizer.startListening("COMMANDS");
    } 
    this.isStart = true;
  }
  
  public void stop() {
    SpeechRecognizer speechRecognizer = this.recognizer;
    if (speechRecognizer != null)
      speechRecognizer.cancel(); 
    this.isStart = false;
  }
  
  public static interface OnCommandCallback {
    void onCommand(int param1Int, String param1String);
  }
  
  private static class SetupTask extends AsyncTask<Void, Void, Exception> {
    VoiceRecognizer voiceRecognizer;
    
    SetupTask(VoiceRecognizer param1VoiceRecognizer) {
      this.voiceRecognizer = param1VoiceRecognizer;
    }
    
    protected Exception doInBackground(Void... param1VarArgs) {
      try {
        Assets assets = new Assets();
        this(this.voiceRecognizer.getContext());
        File file = assets.syncAssets();
        this.voiceRecognizer.setupRecognizer(file);
        return null;
      } catch (IOException null) {
        return null;
      } 
    }
    
    protected void onPostExecute(Exception param1Exception) {}
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/voicecommandrecognizer/VoiceRecognizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
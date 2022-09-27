package com.guanxukeji.videobackgroundmusicpicker.activity;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guanxukeji.videobackgroundmusicpicker.R;
import com.guanxukeji.videobackgroundmusicpicker.SharedPreferenceHelper;
import com.guanxukeji.videobackgroundmusicpicker.adapter.MusicListAdapter;
import com.guanxukeji.videobackgroundmusicpicker.bean.MusicBean;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;

public class MusicChooseActivity extends AppCompatActivity {
  private TextView backBtn;
  
  private ToggleButton backgroundMusicToogle;
  
  private RadioButton buildInMusic;
  
  private TextView currentBgMusicText;
  
  private Disposable gettingPhoneMusicDisposable;
  
  private RadioGroup musicGroup;
  
  private RecyclerView musicRecycleList;
  
  private RadioButton phoneMusic;
  
  public static final String getStringFormAsset(Context paramContext, String paramString) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: aload_3
    //   5: astore #4
    //   7: new java/io/BufferedReader
    //   10: astore #5
    //   12: aload_3
    //   13: astore #4
    //   15: new java/io/InputStreamReader
    //   18: astore #6
    //   20: aload_3
    //   21: astore #4
    //   23: aload #6
    //   25: aload_0
    //   26: invokevirtual getAssets : ()Landroid/content/res/AssetManager;
    //   29: aload_1
    //   30: invokevirtual open : (Ljava/lang/String;)Ljava/io/InputStream;
    //   33: invokespecial <init> : (Ljava/io/InputStream;)V
    //   36: aload_3
    //   37: astore #4
    //   39: aload #5
    //   41: aload #6
    //   43: invokespecial <init> : (Ljava/io/Reader;)V
    //   46: new java/lang/StringBuilder
    //   49: astore_1
    //   50: aload_1
    //   51: invokespecial <init> : ()V
    //   54: aload #5
    //   56: invokevirtual readLine : ()Ljava/lang/String;
    //   59: astore_0
    //   60: aload_0
    //   61: ifnull -> 80
    //   64: aload_1
    //   65: aload_0
    //   66: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   69: pop
    //   70: aload_1
    //   71: bipush #10
    //   73: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   76: pop
    //   77: goto -> 54
    //   80: aload_1
    //   81: invokevirtual toString : ()Ljava/lang/String;
    //   84: astore_0
    //   85: aload #5
    //   87: invokevirtual close : ()V
    //   90: goto -> 98
    //   93: astore_1
    //   94: aload_1
    //   95: invokevirtual printStackTrace : ()V
    //   98: aload_0
    //   99: areturn
    //   100: astore_0
    //   101: aload #5
    //   103: astore #4
    //   105: goto -> 148
    //   108: astore_1
    //   109: aload #5
    //   111: astore_0
    //   112: goto -> 122
    //   115: astore_0
    //   116: goto -> 148
    //   119: astore_1
    //   120: aload_2
    //   121: astore_0
    //   122: aload_0
    //   123: astore #4
    //   125: aload_1
    //   126: invokevirtual printStackTrace : ()V
    //   129: aload_0
    //   130: ifnull -> 145
    //   133: aload_0
    //   134: invokevirtual close : ()V
    //   137: goto -> 145
    //   140: astore_0
    //   141: aload_0
    //   142: invokevirtual printStackTrace : ()V
    //   145: ldc ''
    //   147: areturn
    //   148: aload #4
    //   150: ifnull -> 166
    //   153: aload #4
    //   155: invokevirtual close : ()V
    //   158: goto -> 166
    //   161: astore_1
    //   162: aload_1
    //   163: invokevirtual printStackTrace : ()V
    //   166: goto -> 171
    //   169: aload_0
    //   170: athrow
    //   171: goto -> 169
    // Exception table:
    //   from	to	target	type
    //   7	12	119	java/io/IOException
    //   7	12	115	finally
    //   15	20	119	java/io/IOException
    //   15	20	115	finally
    //   23	36	119	java/io/IOException
    //   23	36	115	finally
    //   39	46	119	java/io/IOException
    //   39	46	115	finally
    //   46	54	108	java/io/IOException
    //   46	54	100	finally
    //   54	60	108	java/io/IOException
    //   54	60	100	finally
    //   64	77	108	java/io/IOException
    //   64	77	100	finally
    //   80	85	108	java/io/IOException
    //   80	85	100	finally
    //   85	90	93	java/io/IOException
    //   125	129	115	finally
    //   133	137	140	java/io/IOException
    //   153	158	161	java/io/IOException
  }
  
  private static String resourceIdToUri(Context paramContext, int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("android.resource://");
    stringBuilder.append(paramContext.getPackageName());
    stringBuilder.append("/drawable/");
    stringBuilder.append(paramInt);
    return stringBuilder.toString();
  }
  
  private void showPhoneMusicList() {
    Disposable disposable = this.gettingPhoneMusicDisposable;
    if (disposable != null)
      disposable.dispose(); 
    this.gettingPhoneMusicDisposable = Observable.create(new ObservableOnSubscribe<List<MusicBean>>() {
          public void subscribe(ObservableEmitter<List<MusicBean>> param1ObservableEmitter) throws Exception {
            ArrayList<MusicBean> arrayList = new ArrayList();
            Cursor cursor = MusicChooseActivity.this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, "title_key");
            if (cursor != null) {
              int i = 0;
              while (true) {
                int j = 0;
                while (cursor.moveToNext() && !param1ObservableEmitter.isDisposed()) {
                  MusicBean musicBean = new MusicBean();
                  musicBean.setSinger(cursor.getString(cursor.getColumnIndex("artist")));
                  musicBean.setName(cursor.getString(cursor.getColumnIndex("title")));
                  musicBean.setFilename(cursor.getString(cursor.getColumnIndex("_data")));
                  int k = i % 3;
                  if (k == 0)
                    musicBean.setIcon(MusicChooseActivity.resourceIdToUri(MusicChooseActivity.this.getBaseContext(), R.drawable.default_music_icon_1)); 
                  if (k == 1)
                    musicBean.setIcon(MusicChooseActivity.resourceIdToUri(MusicChooseActivity.this.getBaseContext(), R.drawable.default_music_icon_2)); 
                  if (k == 2)
                    musicBean.setIcon(MusicChooseActivity.resourceIdToUri(MusicChooseActivity.this.getBaseContext(), R.drawable.default_music_icon_3)); 
                  k = i + 1;
                  arrayList.add(musicBean);
                  int m = j + 1;
                  i = k;
                  j = m;
                  if (m == 10) {
                    ArrayList<MusicBean> arrayList1 = new ArrayList<MusicBean>(arrayList);
                    arrayList.clear();
                    param1ObservableEmitter.onNext(arrayList1);
                    i = k;
                  } 
                } 
                break;
              } 
            } 
            if (cursor != null)
              cursor.close(); 
            if (!param1ObservableEmitter.isDisposed())
              param1ObservableEmitter.onNext(arrayList); 
            param1ObservableEmitter.onComplete();
          }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<MusicBean>>() {
          ArrayList<MusicBean> musicBeans = new ArrayList<MusicBean>();
          
          MusicListAdapter musicListAdapter = new MusicListAdapter((Context)MusicChooseActivity.this, this.musicBeans);
          
          public void accept(List<MusicBean> param1List) throws Exception {
            if (this.musicBeans.size() == 0) {
              MusicChooseActivity.this.musicRecycleList.setAdapter((RecyclerView.Adapter)this.musicListAdapter);
              this.musicListAdapter.setItemClickListener(new MusicListAdapter.OnItemClickListener() {
                    public void onItemClick(View param2View, int param2Int) {
                      MusicClipActivity.launch((Activity)MusicChooseActivity.this, MusicChooseActivity.null.this.musicBeans, param2Int);
                    }
                  });
            } 
            this.musicBeans.addAll(param1List);
            this.musicListAdapter.notifyDataSetChanged();
          }
        });
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    getWindow().setFlags(1024, 1024);
    setContentView(R.layout.activity_music_choose);
    this.musicGroup = (RadioGroup)findViewById(R.id.music_group);
    this.backgroundMusicToogle = (ToggleButton)findViewById(R.id.background_music_toogle);
    this.buildInMusic = (RadioButton)findViewById(R.id.builtIn_music);
    this.phoneMusic = (RadioButton)findViewById(R.id.phone_music);
    this.currentBgMusicText = (TextView)findViewById(R.id.current_bg_music_text);
    this.backBtn = (TextView)findViewById(R.id.back_btn);
    this.musicRecycleList = (RecyclerView)findViewById(R.id.music_recycle_list);
    this.backBtn.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            MusicChooseActivity.this.finish();
          }
        });
    this.backgroundMusicToogle.setChecked(SharedPreferenceHelper.isOpenBackgroundMusic(getApplicationContext()));
    this.backgroundMusicToogle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
            SharedPreferenceHelper.saveOpenBackgroundMusic((Context)MusicChooseActivity.this.getApplication(), param1Boolean);
          }
        });
    this.musicRecycleList.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager((Context)this, 0, false));
    final List musicBeans = (List)(new Gson()).fromJson(getStringFormAsset((Context)this, "buildin_music/Musics.json"), (new TypeToken<List<MusicBean>>() {
        
        }).getType());
    for (MusicBean musicBean : list) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("file:///android_asset/buildin_music/Images/");
      stringBuilder.append(musicBean.getIcon());
      musicBean.setIcon(stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("file:///android_asset/buildin_music/MP3s/");
      stringBuilder.append(musicBean.getFilename());
      musicBean.setFilename(stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("file:///android_asset/buildin_music/Lrcs/");
      stringBuilder.append(musicBean.getLrcname());
      musicBean.setLrcname(stringBuilder.toString());
    } 
    final MusicListAdapter buildInMusicListAdapter = new MusicListAdapter((Context)this, list);
    this.musicRecycleList.setAdapter((RecyclerView.Adapter)musicListAdapter);
    musicListAdapter.setItemClickListener(new MusicListAdapter.OnItemClickListener() {
          public void onItemClick(View param1View, int param1Int) {
            MusicClipActivity.launch((Activity)MusicChooseActivity.this, musicBeans, param1Int);
          }
        });
    this.musicGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
          public void onCheckedChanged(RadioGroup param1RadioGroup, int param1Int) {
            if (param1Int == MusicChooseActivity.this.buildInMusic.getId()) {
              if (MusicChooseActivity.this.gettingPhoneMusicDisposable != null)
                MusicChooseActivity.this.gettingPhoneMusicDisposable.dispose(); 
              MusicChooseActivity.this.musicRecycleList.setAdapter((RecyclerView.Adapter)buildInMusicListAdapter);
            } else if (param1Int == MusicChooseActivity.this.phoneMusic.getId()) {
              MusicChooseActivity.this.showPhoneMusicList();
            } 
          }
        });
  }
  
  protected void onDestroy() {
    super.onDestroy();
    Disposable disposable = this.gettingPhoneMusicDisposable;
    if (disposable != null)
      disposable.dispose(); 
  }
  
  protected void onStart() {
    super.onStart();
    TextView textView = this.currentBgMusicText;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getString(R.string.current_bgm));
    stringBuilder.append(SharedPreferenceHelper.getBackgroundMusicName((Context)this));
    textView.setText(stringBuilder.toString());
    this.backgroundMusicToogle.setChecked(SharedPreferenceHelper.isOpenBackgroundMusic(getApplicationContext()));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/guanxukeji/videobackgroundmusicpicker/activity/MusicChooseActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
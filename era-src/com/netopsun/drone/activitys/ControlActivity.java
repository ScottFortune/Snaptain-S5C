package com.netopsun.drone.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.media.SoundPool;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.appcompat.app.AlertDialog;
import com.guanxukeji.drone_rocker.ControlListener;
import com.guanxukeji.drone_rocker.ControlPanel;
import com.guanxukeji.videobackgroundmusicpicker.BGMPicker;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.jaygoo.widget.VerticalRangeSeekBar;
import com.netopsun.anykadevices.AnykaRxTxCommunicator;
import com.netopsun.anykadevices.AnykaVideoCommunicator;
import com.netopsun.anykadevices.ConnectInternalCallback;
import com.netopsun.deviceshub.base.CMDCommunicator;
import com.netopsun.deviceshub.base.Devices;
import com.netopsun.deviceshub.base.RxTxCommunicator;
import com.netopsun.deviceshub.base.VideoCommunicator;
import com.netopsun.deviceshub.interfaces.ConnectResultCallback;
import com.netopsun.drone.BaseSensorLandscapeActivity;
import com.netopsun.drone.Constants;
import com.netopsun.drone.DevicesUtil;
import com.netopsun.drone.NetworkUtils;
import com.netopsun.fhdevices.FHCMDCommunicator;
import com.netopsun.fhdevices.FHDevices;
import com.netopsun.gesturerecognition.GestureReconizerHelper;
import com.netopsun.ijkvideoview.encoder.EncodeBitmapAndMux2Mp4;
import com.netopsun.ijkvideoview.extra.filter_choose_popupwindows.FilterChoosePopupWindows;
import com.netopsun.ijkvideoview.extra.filter_choose_popupwindows.FilterListAdapter;
import com.netopsun.ijkvideoview.extra.particle_effects_choose_popupwindows.ParticleEffectsBean;
import com.netopsun.ijkvideoview.extra.particle_effects_choose_popupwindows.ParticleEffectsChoosePopupWindows;
import com.netopsun.ijkvideoview.extra.particle_effects_choose_popupwindows.ParticleEffectsListAdapter;
import com.netopsun.ijkvideoview.widget.media.IjkVideoView;
import com.netopsun.ijkvideoview.widget.media.render.MultiFunctionRender;
import com.netopsun.jrdevices.JRRxTxCommunicator;
import com.netopsun.rxtxprotocol.RxTxProtocolFactory;
import com.netopsun.rxtxprotocol.base.RxTxProtocol;
import com.netopsun.rxtxprotocol.base.simple_receiver.SimpleDroneMsgCallback;
import com.netopsun.rxtxprotocol.simple_drone_protocol.SimpleDroneProtocol;
import com.netopsun.voicecommandrecognizer.VoiceRecognizer;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.TimeUnit;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import tv.danmaku.ijk.media.player_gx.IMediaPlayer;

public class ControlActivity extends BaseSensorLandscapeActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
  private static final String TAG = "ControlActivity";
  
  private boolean autoSaveParams;
  
  private ImageView controlBackground;
  
  private Button controlBarAlbum;
  
  private Button controlBarBackButton;
  
  private ToggleButton controlBarClose;
  
  private ToggleButton controlBarCoolVideo;
  
  private ViewGroup controlBarDefault;
  
  private ToggleButton controlBarFilter;
  
  private ToggleButton controlBarGesture;
  
  private ToggleButton controlBarHeadless;
  
  private ToggleButton controlBarHumanoidFollower;
  
  private ViewGroup controlBarMVGroup;
  
  private ToggleButton controlBarMenu;
  
  private ViewGroup controlBarMenuLayout;
  
  private Button controlBarMusicList;
  
  private TextView controlBarMusicName;
  
  private Button controlBarMvAlbum;
  
  private ToggleButton controlBarMvFilter;
  
  private ToggleButton controlBarMvRecordVideo;
  
  private ToggleButton controlBarMvScale;
  
  private Button controlBarMvTakePicture;
  
  private ToggleButton controlBarRecordVideo;
  
  private Button controlBarRev;
  
  private ToggleButton controlBarRocker;
  
  private ToggleButton controlBarRockerExtra;
  
  private ImageButton controlBarSpeed;
  
  private Button controlBarSwitchCamera;
  
  private Button controlBarTakePicture;
  
  private ToggleButton controlBarTakeoff;
  
  private ToggleButton controlBarTrack;
  
  private ToggleButton controlBarVrMode;
  
  private ToggleButton controlBarVrModeExtra;
  
  private ControlListener controlListener;
  
  private Disposable countRecordTimeTask;
  
  private Devices devices;
  
  private FilterChoosePopupWindows filterChoosePopupWindows;
  
  private GestureReconizerHelper gestureReconizerHelper;
  
  private IjkVideoView ijkVideoView;
  
  private boolean isMainCamera = true;
  
  private boolean isReversal;
  
  private volatile boolean isSendRockerData;
  
  private volatile long lastCaptureRecordTime;
  
  private volatile long lastSendRockerDataStatusChangeTime;
  
  private ImageView mControlBarBattery;
  
  private Button mControlBarCalibrate;
  
  private TextView mControlBarMVRecordTimeText;
  
  private TextView mControlBarRecordTimeText;
  
  private ToggleButton mControlBarSensorControl;
  
  private ToggleButton mControlBarShowTunningSlider;
  
  private Button mControlBarStop;
  
  private View mControlBarVoiceCommandTips;
  
  private ToggleButton mControlBarVoiceControl;
  
  private ImageView mControlBarWifiSignal;
  
  private VerticalRangeSeekBar mMagnifySeekbar;
  
  private TextView mSignalWeakTips;
  
  private SoundPool mSoundPoll;
  
  private final CMDCommunicator.OnRemoteCMDListener onRemoteCMDListener = new CMDCommunicator.OnRemoteCMDListener() {
      public void onRemoteStartRecord() {
        if (ControlActivity.this.devices instanceof com.netopsun.anykadevices.AnykaDevices && System.currentTimeMillis() - ControlActivity.this.lastCaptureRecordTime < 1000L)
          return; 
        ControlActivity.access$102(ControlActivity.this, System.currentTimeMillis());
        ControlActivity.this.runOnUiThread(new Runnable() {
              public void run() {
                boolean bool;
                ControlActivity.this.controlBarRecordVideo.setChecked(true);
                ControlActivity.this.controlBarMvRecordVideo.setChecked(true);
                ControlActivity controlActivity = ControlActivity.this;
                if (BGMPicker.isOpenBGMMode(ControlActivity.this.getApplicationContext()) && ControlActivity.this.controlBarMVGroup.getVisibility() == 0) {
                  bool = true;
                } else {
                  bool = false;
                } 
                controlActivity.toggleRecord(true, bool, false);
              }
            });
      }
      
      public void onRemoteStopRecord() {
        if (ControlActivity.this.devices instanceof com.netopsun.anykadevices.AnykaDevices && System.currentTimeMillis() - ControlActivity.this.lastCaptureRecordTime < 1000L)
          return; 
        ControlActivity.access$102(ControlActivity.this, System.currentTimeMillis());
        ControlActivity.this.runOnUiThread(new Runnable() {
              public void run() {
                ControlActivity.this.controlBarRecordVideo.setChecked(false);
                ControlActivity.this.controlBarMvRecordVideo.setChecked(false);
                ControlActivity.this.toggleRecord(false, false);
              }
            });
      }
      
      public void onRemoteTakePhoto() {
        if (ControlActivity.this.devices instanceof com.netopsun.anykadevices.AnykaDevices && System.currentTimeMillis() - ControlActivity.this.lastCaptureRecordTime < 1000L)
          return; 
        ControlActivity.access$102(ControlActivity.this, System.currentTimeMillis());
        ControlActivity.this.runOnUiThread(new Runnable() {
              public void run() {
                ControlActivity.this.onReceiveRemoteTakePhoto(false);
              }
            });
      }
    };
  
  private ParticleEffectsChoosePopupWindows particleEffectsChoosePopupWindows;
  
  private int pitchSliderLevel;
  
  private ImageView receiveImg;
  
  private FrameLayout receiveImgContainer;
  
  private ProgressBar receiveImgProgress;
  
  private Disposable refreshSignalTask;
  
  private ControlPanel rockerControlPanel;
  
  private int rollSliderLevel;
  
  private RxTxProtocol rxTxProtocol;
  
  private int shutterSoundID;
  
  private final SimpleDroneMsgCallback simpleDroneMsgCallback = new SimpleDroneMsgCallback() {
      public void didRecvRecordStartCmd() {
        if (ControlActivity.this.devices instanceof com.netopsun.anykadevices.AnykaDevices && System.currentTimeMillis() - ControlActivity.this.lastCaptureRecordTime < 1000L)
          return; 
        ControlActivity.access$102(ControlActivity.this, System.currentTimeMillis());
        ControlActivity.this.runOnUiThread(new Runnable() {
              public void run() {
                boolean bool;
                ControlActivity.this.controlBarRecordVideo.setChecked(true);
                ControlActivity.this.controlBarMvRecordVideo.setChecked(true);
                ControlActivity controlActivity = ControlActivity.this;
                if (BGMPicker.isOpenBGMMode(ControlActivity.this.getApplicationContext()) && ControlActivity.this.controlBarMVGroup.getVisibility() == 0) {
                  bool = true;
                } else {
                  bool = false;
                } 
                controlActivity.toggleRecord(true, bool, false);
              }
            });
      }
      
      public void didRecvRecordStopCmd() {
        if (ControlActivity.this.devices instanceof com.netopsun.anykadevices.AnykaDevices && System.currentTimeMillis() - ControlActivity.this.lastCaptureRecordTime < 1000L)
          return; 
        ControlActivity.access$102(ControlActivity.this, System.currentTimeMillis());
        ControlActivity.this.runOnUiThread(new Runnable() {
              public void run() {
                ControlActivity.this.controlBarRecordVideo.setChecked(false);
                ControlActivity.this.controlBarMvRecordVideo.setChecked(false);
                ControlActivity.this.toggleRecord(false, false, false);
              }
            });
      }
      
      public void didRecvTakePhotoCmd() {
        if (ControlActivity.this.devices instanceof com.netopsun.anykadevices.AnykaDevices && System.currentTimeMillis() - ControlActivity.this.lastCaptureRecordTime < 1000L)
          return; 
        ControlActivity.access$102(ControlActivity.this, System.currentTimeMillis());
        ControlActivity.this.runOnUiThread(new Runnable() {
              public void run() {
                ControlActivity.this.onReceiveRemoteTakePhoto(true);
              }
            });
      }
      
      public void onBatteryLevel(final int level, float param1Float) {
        ControlActivity.this.runOnUiThread(new Runnable() {
              public void run() {
                if (ControlActivity.this.wifiName.toLowerCase().contains("s5c")) {
                  ControlActivity.this.mControlBarBattery.getDrawable().setLevel(level);
                } else {
                  int i = level;
                  if (i > 40) {
                    ControlActivity.this.mControlBarBattery.getDrawable().setLevel(100);
                  } else if (i > 20) {
                    ControlActivity.this.mControlBarBattery.getDrawable().setLevel(75);
                  } else if (i > 0) {
                    ControlActivity.this.mControlBarBattery.getDrawable().setLevel(50);
                  } else if (i == 0) {
                    ControlActivity.this.mControlBarBattery.getDrawable().setLevel(25);
                  } 
                } 
              }
            });
      }
    };
  
  private int speedState;
  
  private String videoFileName;
  
  private VoiceRecognizer voiceRecognizer;
  
  private String wifiName = "";
  
  private int yawSliderLevel;
  
  private void checkIfNeedSendControlData() {
    if (this.rxTxProtocol == null)
      return; 
    this.lastSendRockerDataStatusChangeTime = System.currentTimeMillis();
    if (this.controlBarRocker.isChecked() || this.controlBarRockerExtra.isChecked() || this.controlBarHumanoidFollower.isChecked()) {
      this.rxTxProtocol.startAutomaticTimingSend(70);
      this.isSendRockerData = true;
      return;
    } 
    this.rxTxProtocol.stopAutomaticTimingSend();
    this.isSendRockerData = false;
  }
  
  private void finishRecord(final String videoFileName, boolean paramBoolean) {
    this.videoFileName = videoFileName;
    if (!paramBoolean) {
      MediaScannerConnection.scanFile((Context)this, new String[] { videoFileName }, null, null);
      return;
    } 
    int i = videoFileName.lastIndexOf(".");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(videoFileName.substring(0, i));
    stringBuilder.append("_bgm");
    stringBuilder.append(videoFileName.substring(i));
    final String coverbgmVideoOutputPath = stringBuilder.toString();
    BGMPicker.coverVideoBGM((Context)getApplication(), videoFileName, str, new BGMPicker.CoverBGMCallback() {
          public void onCancel() {
            (new File(coverbgmVideoOutputPath)).delete();
            MediaScannerConnection.scanFile((Context)ControlActivity.this.getApplication(), new String[] { this.val$videoFileName }, null, null);
          }
          
          public void onResult(boolean param1Boolean, String param1String) {
            if (param1Boolean) {
              File file = new File(videoFileName);
              file.delete();
              (new File(coverbgmVideoOutputPath)).renameTo(file);
            } else {
              (new File(coverbgmVideoOutputPath)).delete();
            } 
            MediaScannerConnection.scanFile(ControlActivity.this.getApplicationContext(), new String[] { this.val$videoFileName }, null, null);
          }
        });
  }
  
  private void hideVirtualButtons() {
    getWindow().getDecorView().setSystemUiVisibility(3846);
  }
  
  private void initFilterPopupWindows() {
    this.filterChoosePopupWindows = FilterChoosePopupWindows.getDefaultFilterPopupWindows((Context)this);
    this.filterChoosePopupWindows.setOnItemClickListener(new FilterListAdapter.OnItemClickListener() {
          public void onItemClick(View param1View, GPUImageFilter param1GPUImageFilter, int param1Int) {
            ControlActivity.this.ijkVideoView.setGPUFilter(param1GPUImageFilter);
          }
        });
  }
  
  private void initParticleSystemPopupWindows() {
    this.particleEffectsChoosePopupWindows = ParticleEffectsChoosePopupWindows.getDefaultParticleEffectsPopupWindows((Context)this);
    this.particleEffectsChoosePopupWindows.setOnItemClickListener(new ParticleEffectsListAdapter.OnItemClickListener() {
          int lastPosition = -1;
          
          public void onItemClick(View param1View, List<ParticleEffectsBean> param1List, int param1Int) {
            if (param1Int == this.lastPosition) {
              this.lastPosition = -1;
              ControlActivity.this.ijkVideoView.setNoneParticleSystem();
              return;
            } 
            this.lastPosition = param1Int;
            ControlActivity.this.ijkVideoView.setParticleSystem((Context)ControlActivity.this, ((ParticleEffectsBean)param1List.get(param1Int)).getPlistFile(), ((ParticleEffectsBean)param1List.get(param1Int)).getTextureResourceID());
          }
        });
  }
  
  private void initView() {
    this.controlBarDefault = (ViewGroup)findViewById(2131230833);
    this.controlBarMVGroup = (ViewGroup)findViewById(2131230844);
    this.controlBarMenuLayout = (ViewGroup)findViewById(2131230839);
    this.controlBarBackButton = (Button)findViewById(2131230828);
    this.controlBarBackButton.setOnClickListener(this);
    this.controlBarSwitchCamera = (Button)findViewById(2131230858);
    this.controlBarSwitchCamera.setOnClickListener(this);
    this.controlBarAlbum = (Button)findViewById(2131230827);
    this.controlBarAlbum.setOnClickListener(this);
    this.controlBarTakeoff = (ToggleButton)findViewById(2131230860);
    this.controlBarTakeoff.setOnCheckedChangeListener(this);
    this.controlBarMenu = (ToggleButton)findViewById(2131230838);
    this.controlBarMenu.setOnCheckedChangeListener(this);
    this.controlBarSpeed = (ImageButton)findViewById(2131230856);
    this.controlBarSpeed.setOnClickListener(this);
    this.controlBarTakePicture = (Button)findViewById(2131230859);
    this.controlBarTakePicture.setOnClickListener(this);
    this.controlBarRecordVideo = (ToggleButton)findViewById(2131230850);
    this.controlBarRecordVideo.setOnCheckedChangeListener(this);
    this.controlBarRocker = (ToggleButton)findViewById(2131230852);
    this.controlBarRocker.setOnCheckedChangeListener(this);
    this.controlBarFilter = (ToggleButton)findViewById(2131230834);
    this.controlBarFilter.setOnCheckedChangeListener(this);
    this.controlBarGesture = (ToggleButton)findViewById(2131230835);
    this.controlBarGesture.setOnCheckedChangeListener(this);
    this.controlBarTrack = (ToggleButton)findViewById(2131230861);
    this.controlBarTrack.setOnCheckedChangeListener(this);
    this.controlBarVrMode = (ToggleButton)findViewById(2131230864);
    this.controlBarVrMode.setOnCheckedChangeListener(this);
    this.controlBarVrModeExtra = (ToggleButton)findViewById(2131230865);
    this.controlBarVrModeExtra.setOnCheckedChangeListener(this);
    this.controlBarRev = (Button)findViewById(2131230851);
    this.controlBarRev.setOnClickListener(this);
    this.controlBarHeadless = (ToggleButton)findViewById(2131230836);
    this.controlBarHeadless.setOnCheckedChangeListener(this);
    this.controlBarCoolVideo = (ToggleButton)findViewById(2131230832);
    this.controlBarCoolVideo.setOnCheckedChangeListener(this);
    this.controlBarMvScale = (ToggleButton)findViewById(2131230847);
    this.controlBarMvScale.setOnCheckedChangeListener(this);
    this.controlBarMvFilter = (ToggleButton)findViewById(2131230843);
    this.controlBarMvFilter.setOnCheckedChangeListener(this);
    this.controlBarMvAlbum = (Button)findViewById(2131230842);
    this.controlBarMvAlbum.setOnClickListener(this);
    this.controlBarRockerExtra = (ToggleButton)findViewById(2131230853);
    this.controlBarRockerExtra.setOnCheckedChangeListener(this);
    this.controlBarMusicList = (Button)findViewById(2131230840);
    this.controlBarMusicList.setOnClickListener(this);
    this.controlBarMusicName = (TextView)findViewById(2131230841);
    this.controlBarMusicName.setOnClickListener(this);
    this.controlBarClose = (ToggleButton)findViewById(2131230831);
    this.controlBarClose.setOnClickListener(this);
    this.controlBarMvTakePicture = (Button)findViewById(2131230848);
    this.controlBarMvTakePicture.setOnClickListener(this);
    this.controlBarMvRecordVideo = (ToggleButton)findViewById(2131230846);
    this.controlBarMvRecordVideo.setOnCheckedChangeListener(this);
    this.ijkVideoView = (IjkVideoView)findViewById(2131230914);
    this.controlBackground = (ImageView)findViewById(2131230826);
    this.rockerControlPanel = (ControlPanel)findViewById(2131230990);
    this.mControlBarCalibrate = (Button)findViewById(2131230830);
    this.mControlBarCalibrate.setOnClickListener(this);
    this.mControlBarStop = (Button)findViewById(2131230857);
    this.mControlBarStop.setOnClickListener(this);
    this.mMagnifySeekbar = (VerticalRangeSeekBar)findViewById(2131230928);
    this.controlBarHumanoidFollower = (ToggleButton)findViewById(2131230837);
    this.controlBarHumanoidFollower.setOnCheckedChangeListener(this);
    this.receiveImg = (ImageView)findViewById(2131230977);
    this.receiveImgProgress = (ProgressBar)findViewById(2131230979);
    this.receiveImgContainer = (FrameLayout)findViewById(2131230978);
    this.mControlBarRecordTimeText = (TextView)findViewById(2131230849);
    this.mControlBarMVRecordTimeText = (TextView)findViewById(2131230845);
    this.mControlBarSensorControl = (ToggleButton)findViewById(2131230854);
    this.mControlBarSensorControl.setOnCheckedChangeListener(this);
    this.mControlBarVoiceControl = (ToggleButton)findViewById(2131230863);
    this.mControlBarVoiceControl.setOnCheckedChangeListener(this);
    this.mControlBarVoiceCommandTips = findViewById(2131230862);
    this.mControlBarVoiceCommandTips.setOnClickListener(this);
    this.mControlBarBattery = (ImageView)findViewById(2131230829);
    this.mControlBarWifiSignal = (ImageView)findViewById(2131230866);
    this.mSignalWeakTips = (TextView)findViewById(2131231022);
    this.mControlBarShowTunningSlider = (ToggleButton)findViewById(2131230855);
    this.mControlBarShowTunningSlider.setOnCheckedChangeListener(this);
  }
  
  private void onReceiveRemoteTakePhoto(boolean paramBoolean) {
    this.controlBarTakePicture.setBackgroundResource(2131165367);
    this.controlBarTakePicture.postDelayed(new Runnable() {
          public void run() {
            ControlActivity.this.controlBarTakePicture.setBackgroundResource(2131165365);
          }
        },  500L);
    if (this.devices instanceof FHDevices) {
      receivePhoto(paramBoolean);
    } else {
      takePhoto();
    } 
    this.mSoundPoll.play(this.shutterSoundID, 1.0F, 1.0F, 0, 0, 1.0F);
  }
  
  private void receivePhoto(boolean paramBoolean) {
    Devices devices = this.devices;
    if (devices != null) {
      CMDCommunicator cMDCommunicator = devices.getCMDCommunicator();
      cMDCommunicator.setReceiveRemotePhotoCallback(new CMDCommunicator.OnReceiveRemotePhotoCallback() {
            public String generatePhotoFileName() {
              return Constants.generatePicName(Constants.getPhotoPath(ControlActivity.this.getApplicationContext()));
            }
            
            public void onReceive(final int progress, final boolean isFinish, final String filePath) {
              ControlActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                      if (isFinish) {
                        int i = progress;
                        if (i == 100) {
                          Toast.makeText(ControlActivity.this.getApplicationContext(), ControlActivity.this.getString(2131624141), 0).show();
                        } else if (i == 0) {
                          Toast.makeText(ControlActivity.this.getApplicationContext(), ControlActivity.this.getString(2131624140), 0).show();
                        } 
                        MediaScannerConnection.scanFile((Context)ControlActivity.this, new String[] { this.val$filePath }, null, null);
                      } 
                    }
                  });
            }
          });
      if (cMDCommunicator instanceof FHCMDCommunicator) {
        ((FHCMDCommunicator)cMDCommunicator).sendReceiveRemotePhotoCMD(paramBoolean);
      } else {
        cMDCommunicator.sendReceiveRemotePhotoCMD();
      } 
    } 
  }
  
  private void resizeJPG(String paramString, int paramInt1, int paramInt2) {
    Bitmap bitmap = BitmapFactory.decodeFile(paramString);
    if (bitmap != null) {
      float f1 = paramInt1 / bitmap.getWidth();
      float f2 = paramInt2 / bitmap.getHeight();
      Matrix matrix = new Matrix();
      matrix.postScale(f1, f2);
      Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
      File file = new File(paramString);
      file.delete();
      try {
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream();
        this(file);
        bitmap1.compress(Bitmap.CompressFormat.JPEG, 95, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        bitmap.recycle();
        bitmap1.recycle();
      } catch (FileNotFoundException fileNotFoundException) {
        fileNotFoundException.printStackTrace();
      } catch (IOException iOException) {
        iOException.printStackTrace();
      } 
    } 
  }
  
  private static String secondToTime(long paramLong) {
    StringBuilder stringBuilder = new StringBuilder();
    long l = paramLong / 60L;
    paramLong %= 60L;
    if (l < 10L)
      stringBuilder.append("0"); 
    stringBuilder.append(l);
    stringBuilder.append(":");
    if (paramLong < 10L)
      stringBuilder.append("0"); 
    stringBuilder.append(paramLong);
    return stringBuilder.toString();
  }
  
  private void showFilterPopupWindows(boolean paramBoolean) {
    if (!paramBoolean) {
      this.filterChoosePopupWindows.dismiss();
    } else if (!this.filterChoosePopupWindows.isShowing()) {
      this.filterChoosePopupWindows.show((View)this.controlBarFilter);
    } 
  }
  
  private void showParticleSystemPopupWindows(boolean paramBoolean) {
    if (!paramBoolean) {
      this.particleEffectsChoosePopupWindows.dismiss();
    } else if (!this.particleEffectsChoosePopupWindows.isShowing()) {
      this.particleEffectsChoosePopupWindows.show((View)this.controlBarFilter);
    } 
  }
  
  private void showToast(final String msg) {
    (new Handler(getMainLooper())).post(new Runnable() {
          public void run() {
            Toast toast = new Toast(ControlActivity.this.getApplicationContext());
            TextView textView = new TextView(ControlActivity.this.getApplicationContext());
            textView.setTextColor(-256);
            textView.setText(msg);
            textView.setBackgroundColor(-16777216);
            textView.setPadding(20, 30, 20, 30);
            textView.setLayoutParams((ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-2, -2));
            toast.setGravity(17, 0, 0);
            toast.setView((View)textView);
            toast.show();
          }
        });
  }
  
  private void startCountRecordTime() {
    Disposable disposable = this.countRecordTimeTask;
    if (disposable != null && !disposable.isDisposed())
      this.countRecordTimeTask.dispose(); 
    this.mControlBarRecordTimeText.setText("00:00");
    this.mControlBarMVRecordTimeText.setText("00:00");
    this.mControlBarRecordTimeText.setVisibility(0);
    this.mControlBarMVRecordTimeText.setVisibility(0);
    this.countRecordTimeTask = Observable.interval(1L, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            ControlActivity.this.mControlBarRecordTimeText.setText(ControlActivity.secondToTime(param1Long.longValue() + 1L));
            ControlActivity.this.mControlBarMVRecordTimeText.setText(ControlActivity.secondToTime(param1Long.longValue() + 1L));
          }
        });
  }
  
  private void stopCountRecordTime() {
    Disposable disposable = this.countRecordTimeTask;
    if (disposable != null && !disposable.isDisposed())
      this.countRecordTimeTask.dispose(); 
    this.mControlBarRecordTimeText.setVisibility(4);
    this.mControlBarMVRecordTimeText.setVisibility(4);
  }
  
  private void switchSpeed() {
    this.speedState++;
    if (this.speedState >= 3)
      this.speedState = 0; 
    this.controlBarSpeed.setImageLevel(this.speedState);
  }
  
  private void takePhoto() {
    if (!this.ijkVideoView.isPlaying())
      return; 
    if (this.devices != null)
      this.rxTxProtocol.setOpenDroneTakePhotoLight(true); 
    int i = 1280;
    int j = 720;
    if (this.ijkVideoView.getmVideoWidth() > 0) {
      i = this.ijkVideoView.getmVideoWidth();
      j = this.ijkVideoView.getmVideoHeight();
    } 
    String str = Constants.generatePicName(Constants.getPhotoPath((Context)this));
    this.ijkVideoView.capture(str, i, j);
    MediaScannerConnection.scanFile((Context)this, new String[] { str }, null, null);
    Toast.makeText(getApplicationContext(), getString(2131624141), 0).show();
  }
  
  private void toggleGesture(boolean paramBoolean) {
    if (paramBoolean) {
      showToast(getString(2131624044));
      if (this.gestureReconizerHelper == null)
        this.gestureReconizerHelper = new GestureReconizerHelper((Context)this, new GestureReconizerHelper.ReconizeResultCallback() {
              public void photo() {
                ControlActivity.this.runOnUiThread(new Runnable() {
                      public void run() {
                        ControlActivity.this.controlBarTakePicture.performClick();
                      }
                    });
              }
              
              public void video() {
                ControlActivity.this.runOnUiThread(new Runnable() {
                      public void run() {
                        ControlActivity.this.controlBarRecordVideo.performClick();
                        ControlActivity.this.toggleRecord(ControlActivity.this.controlBarRecordVideo.isChecked(), BGMPicker.isOpenBGMMode(ControlActivity.this.getApplicationContext()));
                      }
                    });
              }
            }); 
      this.ijkVideoView.setOnFrameByteBufferCallBack(new MultiFunctionRender.OnFrameBufferCallback() {
            public int onFrameBuffer(ByteBuffer param1ByteBuffer) {
              if (ControlActivity.this.gestureReconizerHelper != null && ControlActivity.this.controlBarGesture.isChecked()) {
                Bitmap bitmap2 = Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_8888);
                bitmap2.copyPixelsFromBuffer(param1ByteBuffer);
                Matrix matrix = new Matrix();
                matrix.postScale(-1.0F, 1.0F);
                matrix.postRotate(-180.0F);
                Bitmap bitmap1 = Bitmap.createBitmap(bitmap2, 0, 0, 640, 480, matrix, true);
                ControlActivity.this.gestureReconizerHelper.process(ControlActivity.this.getApplicationContext(), bitmap1, 0.2F);
              } 
              return 20;
            }
          }640, 480);
    } else {
      this.ijkVideoView.setOnFrameByteBufferCallBack(null, 0, 0);
    } 
  }
  
  private void toggleRecord(boolean paramBoolean1, boolean paramBoolean2) {
    toggleRecord(paramBoolean1, paramBoolean2, true);
  }
  
  private void toggleRecord(boolean paramBoolean1, final boolean openBGM, boolean paramBoolean3) {
    if (!paramBoolean1) {
      this.ijkVideoView.stopRecord();
      stopCountRecordTime();
      BGMPicker.stopBGMPlaying((Context)this);
      if (this.devices != null) {
        this.rxTxProtocol.setOpenDroneRecordLight(false);
        if (paramBoolean3)
          this.devices.getCMDCommunicator().remoteStopRecord(false, 5, null); 
      } 
    } else {
      char c1;
      char c2;
      int i;
      byte b;
      if (!this.ijkVideoView.isPlaying()) {
        this.controlBarRecordVideo.setChecked(false);
        this.controlBarMvRecordVideo.setChecked(false);
        return;
      } 
      if (openBGM && !BGMPicker.playBGM((Context)this))
        Toast.makeText((Context)this, getString(2131624081), 0).show(); 
      if (this.devices != null) {
        this.rxTxProtocol.setOpenDroneRecordLight(true);
        if (paramBoolean3)
          this.devices.getCMDCommunicator().remoteStartRecord(false, 5, null); 
      } 
      startCountRecordTime();
      if (this.ijkVideoView.getmVideoWidth() > 0) {
        c1 = this.ijkVideoView.getmVideoWidth();
        c2 = this.ijkVideoView.getmVideoHeight();
      } else {
        c1 = 'ހ';
        c2 = 'и';
      } 
      if (c2 <= 'ː') {
        i = 3145728;
        b = 25;
      } else {
        i = 7340032;
        b = 30;
      } 
      final String videoFileName = Constants.generateVideoName(Constants.getVideoPath((Context)this));
      this.ijkVideoView.startRecord(c1, c2, i, b, str, new EncodeBitmapAndMux2Mp4.EncodeStatusCallback() {
            public void onEncoderErro(String param1String) {
              ControlActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                      ControlActivity.this.controlBarRecordVideo.setChecked(false);
                      ControlActivity.this.controlBarMvRecordVideo.setChecked(false);
                      Toast.makeText((Context)ControlActivity.this, ControlActivity.this.getString(2131624130), 0).show();
                    }
                  });
            }
            
            public void onEncodingProcessNotFinishErro() {
              ControlActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                      ControlActivity.this.controlBarRecordVideo.setChecked(false);
                      ControlActivity.this.controlBarMvRecordVideo.setChecked(false);
                    }
                  });
            }
            
            public void onFinish() {
              ControlActivity.this.finishRecord(videoFileName, openBGM);
            }
          });
    } 
  }
  
  private void toggleVoiceControl(boolean paramBoolean) {
    if (this.voiceRecognizer == null && paramBoolean)
      this.voiceRecognizer = new VoiceRecognizer((Context)this); 
    if (paramBoolean) {
      this.voiceRecognizer.start(new VoiceRecognizer.OnCommandCallback() {
            long lastOnCommandTime = 0L;
            
            public void onCommand(int param1Int, String param1String) {
              if (System.currentTimeMillis() - this.lastOnCommandTime < 2000L)
                return; 
              this.lastOnCommandTime = System.currentTimeMillis();
              Toast.makeText((Context)ControlActivity.this, param1String, 0).show();
              switch (param1Int) {
                default:
                  return;
                case 10:
                  if (ControlActivity.this.controlBarRecordVideo.isChecked())
                    ControlActivity.this.controlBarRecordVideo.performClick(); 
                case 9:
                  if (!ControlActivity.this.controlBarRecordVideo.isChecked())
                    ControlActivity.this.controlBarRecordVideo.performClick(); 
                case 8:
                  ControlActivity.this.controlBarTakePicture.performClick();
                case 7:
                  ControlActivity.this.rockerControlPanel.simulateTouchRocker(100.0F, 0.0F, 0.0F, 0.0F);
                  ControlActivity.this.rockerControlPanel.postDelayed(new Runnable() {
                        public void run() {
                          ControlActivity.this.rockerControlPanel.cancelSimulateTouchRocker();
                        }
                      },  2000L);
                case 6:
                  ControlActivity.this.rockerControlPanel.simulateTouchRocker(-100.0F, 0.0F, 0.0F, 0.0F);
                  ControlActivity.this.rockerControlPanel.postDelayed(new Runnable() {
                        public void run() {
                          ControlActivity.this.rockerControlPanel.cancelSimulateTouchRocker();
                        }
                      },  2000L);
                case 5:
                  ControlActivity.this.rockerControlPanel.simulateTouchRocker(0.0F, -100.0F, 0.0F, 0.0F);
                  ControlActivity.this.rockerControlPanel.postDelayed(new Runnable() {
                        public void run() {
                          ControlActivity.this.rockerControlPanel.cancelSimulateTouchRocker();
                        }
                      },  2000L);
                case 4:
                  ControlActivity.this.rockerControlPanel.simulateTouchRocker(0.0F, 100.0F, 0.0F, 0.0F);
                  ControlActivity.this.rockerControlPanel.postDelayed(new Runnable() {
                        public void run() {
                          ControlActivity.this.rockerControlPanel.cancelSimulateTouchRocker();
                        }
                      },  2000L);
                case 3:
                  ControlActivity.this.rockerControlPanel.simulateTouchRocker(0.0F, 0.0F, 0.0F, -50.0F);
                  ControlActivity.this.rockerControlPanel.postDelayed(new Runnable() {
                        public void run() {
                          ControlActivity.this.rockerControlPanel.cancelSimulateTouchRocker();
                        }
                      },  1500L);
                case 2:
                  ControlActivity.this.rockerControlPanel.simulateTouchRocker(0.0F, 0.0F, 0.0F, 50.0F);
                  ControlActivity.this.rockerControlPanel.postDelayed(new Runnable() {
                        public void run() {
                          ControlActivity.this.rockerControlPanel.cancelSimulateTouchRocker();
                        }
                      },  1500L);
                case 1:
                  if (ControlActivity.this.controlBarTakeoff.isChecked())
                    ControlActivity.this.controlBarTakeoff.performClick(); 
                case 0:
                  break;
              } 
              if (!ControlActivity.this.controlBarTakeoff.isChecked())
                ControlActivity.this.controlBarTakeoff.performClick(); 
            }
          });
    } else {
      VoiceRecognizer voiceRecognizer = this.voiceRecognizer;
      if (voiceRecognizer != null)
        voiceRecognizer.stop(); 
    } 
  }
  
  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent) {
    if (paramMotionEvent.getAction() == 0)
      hideVirtualButtons(); 
    return super.dispatchTouchEvent(paramMotionEvent);
  }
  
  public void finish() {
    super.finish();
    if (this.devices != null) {
      this.rxTxProtocol.stopAutomaticTimingSend();
      this.devices.getRxTxCommunicator().stopSendHeartBeatPackage();
      this.devices.getRxTxCommunicator().disconnect();
      VoiceRecognizer voiceRecognizer = this.voiceRecognizer;
      if (voiceRecognizer != null)
        voiceRecognizer.release(); 
    } 
  }
  
  public void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (this.devices != null)
      NetworkUtils.checkIfNeedShowDialog((Context)this); 
  }
  
  public void onCheckedChanged(CompoundButton paramCompoundButton, final boolean isChecked) {
    View view;
    ControlPanel controlPanel;
    VerticalRangeSeekBar verticalRangeSeekBar;
    int i = paramCompoundButton.getId();
    byte b = 4;
    switch (i) {
      default:
        return;
      case 2131230864:
      case 2131230865:
        if (paramCompoundButton.isPressed()) {
          ViewGroup viewGroup = this.controlBarDefault;
          if (isChecked) {
            i = 4;
          } else {
            i = 0;
          } 
          viewGroup.setVisibility(i);
          ToggleButton toggleButton = this.controlBarVrModeExtra;
          if (isChecked)
            b = 0; 
          toggleButton.setVisibility(b);
          this.ijkVideoView.setVRMode(isChecked);
        } 
        if (this.controlBarVrMode.isChecked() != isChecked)
          this.controlBarVrMode.setChecked(isChecked); 
        if (this.controlBarVrModeExtra.isChecked() != isChecked)
          this.controlBarVrModeExtra.setChecked(isChecked); 
      case 2131230863:
        view = this.mControlBarVoiceCommandTips;
        if (isChecked)
          b = 0; 
        view.setVisibility(b);
        toggleVoiceControl(isChecked);
      case 2131230861:
        this.mControlBarShowTunningSlider.setChecked(false);
        this.rockerControlPanel.showTrajectorySketchpad(isChecked, null);
      case 2131230860:
        if (this.devices != null)
          if (isChecked) {
            this.rxTxProtocol.setTakeOff(true);
          } else {
            this.rxTxProtocol.setLanding(true);
          }  
      case 2131230855:
        if (this.controlBarTrack.isChecked() && isChecked) {
          this.mControlBarShowTunningSlider.setChecked(false);
          return;
        } 
        controlPanel = this.rockerControlPanel;
        if (isChecked)
          b = 0; 
        controlPanel.setSliderVisibility(b);
      case 2131230854:
        if (isChecked) {
          setFixedCurrentOrientation();
        } else {
          setConfigOrientation();
        } 
        this.rockerControlPanel.turnOnTheAccelerometerController(isChecked, null);
      case 2131230853:
        if (controlPanel.isPressed()) {
          controlPanel = this.rockerControlPanel;
          if (isChecked)
            b = 0; 
          controlPanel.setVisibility(b);
          this.controlBarRocker.setChecked(isChecked);
          checkIfNeedSendControlData();
        } 
      case 2131230852:
        if (controlPanel.isPressed()) {
          controlPanel = this.rockerControlPanel;
          if (isChecked)
            b = 0; 
          controlPanel.setVisibility(b);
          this.controlBarRockerExtra.setChecked(isChecked);
          checkIfNeedSendControlData();
        } 
      case 2131230850:
        if (controlPanel.isPressed()) {
          toggleRecord(isChecked, BGMPicker.isOpenBGMMode((Context)this));
          this.controlBarMvRecordVideo.setChecked(isChecked);
        } 
      case 2131230847:
        if (controlPanel.isPressed()) {
          verticalRangeSeekBar = this.mMagnifySeekbar;
          if (isChecked)
            b = 0; 
          verticalRangeSeekBar.setVisibility(b);
        } 
      case 2131230846:
        if (verticalRangeSeekBar.isPressed()) {
          toggleRecord(isChecked, BGMPicker.isOpenBGMMode((Context)this));
          this.controlBarRecordVideo.setChecked(isChecked);
        } 
      case 2131230843:
        if (verticalRangeSeekBar.isPressed()) {
          this.controlBarFilter.setChecked(isChecked);
          showFilterPopupWindows(isChecked);
          if (this.controlBarCoolVideo.isChecked()) {
            this.controlBarCoolVideo.setChecked(false);
            showParticleSystemPopupWindows(false);
          } 
        } 
      case 2131230838:
        if (isChecked) {
          this.controlBarMenuLayout.setVisibility(0);
        } else {
          this.controlBarMenuLayout.setVisibility(4);
        } 
      case 2131230837:
        if (isChecked && verticalRangeSeekBar.isPressed() && this.controlBarGesture.isChecked()) {
          Toast.makeText((Context)this, getString(2131624083), 0).show();
          this.controlBarHumanoidFollower.setChecked(false);
          return;
        } 
        if (verticalRangeSeekBar.isPressed())
          if (isChecked) {
            this.controlBarHumanoidFollower.setChecked(false);
            (new AlertDialog.Builder((Context)this)).setTitle(getString(2131624144)).setMessage(getString(2131624046)).setPositiveButton(getString(2131624076), new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                    ControlActivity.this.controlBarHumanoidFollower.setChecked(isChecked);
                  }
                }).setNegativeButton(getString(2131624020), new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface param1DialogInterface, int param1Int) {}
                }).show();
          } else {
            Toast.makeText((Context)this, getString(2131624040), 0).show();
          }  
      case 2131230836:
        if (this.devices != null)
          this.rxTxProtocol.setHeadless(isChecked); 
      case 2131230835:
        if (isChecked && verticalRangeSeekBar.isPressed() && this.controlBarHumanoidFollower.isChecked()) {
          Toast.makeText((Context)this, getString(2131624084), 0).show();
          this.controlBarGesture.setChecked(false);
          return;
        } 
        toggleGesture(isChecked);
      case 2131230834:
        if (verticalRangeSeekBar.isPressed()) {
          this.controlBarMvFilter.setChecked(isChecked);
          showFilterPopupWindows(isChecked);
          if (this.controlBarCoolVideo.isChecked()) {
            this.controlBarCoolVideo.setChecked(false);
            showParticleSystemPopupWindows(false);
          } 
        } 
      case 2131230832:
        break;
    } 
    if (verticalRangeSeekBar.isPressed()) {
      showParticleSystemPopupWindows(isChecked);
      if (this.controlBarMvFilter.isChecked()) {
        this.controlBarMvFilter.setChecked(false);
        showFilterPopupWindows(false);
      } 
      if (this.controlBarFilter.isChecked()) {
        this.controlBarFilter.setChecked(false);
        showFilterPopupWindows(false);
      } 
    } 
  }
  
  public void onClick(View paramView) {
    Devices devices;
    switch (paramView.getId()) {
      default:
        return;
      case 2131230862:
        this.mControlBarVoiceCommandTips.setVisibility(4);
      case 2131230858:
        devices = this.devices;
        if (devices != null) {
          this.isMainCamera ^= 0x1;
          devices.getCMDCommunicator().switchCamera(true, 5, this.isMainCamera, null);
        } 
      case 2131230857:
        if (this.devices != null)
          this.rxTxProtocol.setEmergencyStop(true); 
      case 2131230856:
        switchSpeed();
      case 2131230851:
        devices = this.devices;
        if (devices != null) {
          this.isReversal ^= 0x1;
          devices.getCMDCommunicator().rotateVideo(true, 5, this.isReversal, new CMDCommunicator.OnExecuteCMDResult() {
                public void onResult(int param1Int, String param1String) {
                  StringBuilder stringBuilder = new StringBuilder();
                  stringBuilder.append("onResult: ");
                  stringBuilder.append(param1Int);
                  Log.e("ControlActivity", stringBuilder.toString());
                }
              });
        } 
      case 2131230848:
      case 2131230859:
        if (this.devices instanceof FHDevices) {
          receivePhoto(true);
        } else {
          takePhoto();
          devices = this.devices;
          if (devices != null)
            devices.getCMDCommunicator().remoteTakePhoto(false, 3, null); 
        } 
        this.mSoundPoll.play(this.shutterSoundID, 1.0F, 1.0F, 0, 0, 1.0F);
      case 2131230840:
      case 2131230841:
        BGMPicker.launchMusicChooseActivity((Activity)this);
      case 2131230831:
        this.controlBarMVGroup.setVisibility(4);
        this.controlBarDefault.setVisibility(0);
        if (this.controlBarCoolVideo.isChecked()) {
          this.controlBarCoolVideo.setChecked(false);
          showParticleSystemPopupWindows(false);
        } 
        if (this.controlBarMvFilter.isChecked()) {
          this.controlBarMvFilter.setChecked(false);
          showFilterPopupWindows(false);
        } 
        if (this.controlBarFilter.isChecked()) {
          this.controlBarFilter.setChecked(false);
          showFilterPopupWindows(false);
        } 
      case 2131230830:
        this.mControlBarCalibrate.setBackgroundResource(2131165301);
        this.mControlBarCalibrate.postDelayed(new Runnable() {
              public void run() {
                ControlActivity.this.mControlBarCalibrate.setBackgroundResource(2131165299);
              }
            },  1000L);
        if (this.devices != null)
          this.rxTxProtocol.setCalibration(true); 
      case 2131230828:
        finish();
      case 2131230827:
      case 2131230842:
        break;
    } 
    startActivity(new Intent((Context)this, MediaSelectActivity.class));
  }
  
  protected void onCreate(Bundle paramBundle) {
    byte b2;
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    getWindow().setFlags(1152, 1152);
    hideVirtualButtons();
    setContentView(2131427357);
    initView();
    this.rockerControlPanel.getPitchSlider().setSlideEndOffset(-10);
    this.rockerControlPanel.getRollSlider().setSlideEndOffset(-10);
    this.rockerControlPanel.getYawSlider().setSlideEndOffset(-10);
    this.rockerControlPanel.getPitchSlider().setMaxSliderLevel(12);
    this.rockerControlPanel.getRollSlider().setMaxSliderLevel(12);
    this.rockerControlPanel.getYawSlider().setMaxSliderLevel(12);
    this.pitchSliderLevel = Constants.getPitchSliderLevel((Context)this);
    this.yawSliderLevel = Constants.getYawSliderLevel((Context)this);
    this.rollSliderLevel = Constants.getRollSliderLevel((Context)this);
    this.autoSaveParams = Constants.getIsAutoSaveParams((Context)this);
    this.rockerControlPanel.getPitchSlider().setCurrentSliderLevel(this.pitchSliderLevel);
    this.rockerControlPanel.getRollSlider().setCurrentSliderLevel(this.rollSliderLevel);
    this.rockerControlPanel.getYawSlider().setCurrentSliderLevel(this.yawSliderLevel);
    this.rockerControlPanel.setSliderVisibility(4);
    this.rockerControlPanel.setRocketMovable(true);
    if (Constants.getIsRightHandMode((Context)this))
      this.rockerControlPanel.setRightHandMode(true); 
    initFilterPopupWindows();
    initParticleSystemPopupWindows();
    this.mMagnifySeekbar.setProgress(1.0F);
    this.mMagnifySeekbar.setOnRangeChangedListener(new OnRangeChangedListener() {
          public void onRangeChanged(RangeSeekBar param1RangeSeekBar, float param1Float1, float param1Float2, boolean param1Boolean) {
            ControlActivity.this.ijkVideoView.setMagnification((param1Float1 / 10.0F + 1.0F));
          }
          
          public void onStartTrackingTouch(RangeSeekBar param1RangeSeekBar, boolean param1Boolean) {}
          
          public void onStopTrackingTouch(RangeSeekBar param1RangeSeekBar, boolean param1Boolean) {}
        });
    this.mSoundPoll = new SoundPool(100, 3, 0);
    this.shutterSoundID = this.mSoundPoll.load((Context)this, 2131558408, 0);
    this.devices = DevicesUtil.getCurrentConnectDevices();
    Devices devices2 = this.devices;
    if (devices2 == null) {
      Toast.makeText((Context)this, getString(2131624037), 0).show();
      return;
    } 
    VideoCommunicator videoCommunicator = devices2.getVideoCommunicator();
    boolean bool = Constants.getIs720PPreview((Context)this);
    byte b1 = 2;
    if (bool) {
      b2 = 1;
    } else {
      b2 = 2;
    } 
    videoCommunicator.setVideoDefaultQuality(b2);
    Devices devices1 = this.devices;
    if (devices1 instanceof FHDevices && ((FHDevices)devices1).getDevicesIP().equals("172.19.10.1")) {
      VideoCommunicator videoCommunicator1 = this.devices.getVideoCommunicator();
      b2 = b1;
      if (Constants.getIs1080PPreview((Context)this))
        b2 = 1; 
      videoCommunicator1.setVideoDefaultQuality(b2);
    } 
    this.ijkVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
          public void onPrepared(IMediaPlayer param1IMediaPlayer) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0F, 0.0F);
            alphaAnimation.setDuration(1500L);
            alphaAnimation.setFillAfter(true);
            alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                  public void onAnimationEnd(Animation param2Animation) {
                    ControlActivity.this.controlBackground.setVisibility(8);
                  }
                  
                  public void onAnimationRepeat(Animation param2Animation) {}
                  
                  public void onAnimationStart(Animation param2Animation) {}
                });
            ControlActivity.this.controlBackground.startAnimation((Animation)alphaAnimation);
          }
        });
    this.rxTxProtocol = RxTxProtocolFactory.createByName("SimpleDroneProtocol", new RxTxCommunicator(this.devices) {
          public int connectInternal() {
            return 0;
          }
          
          public int disconnectInternal() {
            return 0;
          }
          
          public int interruptSend() {
            return 0;
          }
          
          public boolean isConnected() {
            return true;
          }
          
          public int send(byte[] param1ArrayOfbyte) {
            return ControlActivity.this.devices.getRxTxCommunicator().send(param1ArrayOfbyte);
          }
          
          public void setOnReceiveCallback(final RxTxCommunicator.OnReceiveCallback onReceiveCallback) {
            super.setOnReceiveCallback(onReceiveCallback);
            ControlActivity.this.devices.getRxTxCommunicator().setOnReceiveCallback(new RxTxCommunicator.OnReceiveCallback() {
                  public void onReceive(byte[] param2ArrayOfbyte) {
                    onReceiveCallback.onReceive(param2ArrayOfbyte);
                  }
                });
          }
        });
    RxTxProtocol rxTxProtocol = this.rxTxProtocol;
    if (rxTxProtocol instanceof SimpleDroneProtocol)
      ((SimpleDroneProtocol)rxTxProtocol).setModelFlag("HD_UFO"); 
    this.controlListener = new ControlListener() {
        public void onReplayTrackAction(float param1Float, float[] param1ArrayOffloat, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
          float f1 = (float)Math.sqrt(Math.pow(75.0F, 2.0D) / (Math.pow(param1ArrayOffloat[1], 2.0D) / Math.pow(param1ArrayOffloat[0], 2.0D) + 1.0D));
          float f2 = Math.abs(param1ArrayOffloat[1] * f1 / param1ArrayOffloat[0]);
          param1Float = param1ArrayOffloat[0];
          float f3 = -1.0F;
          if (param1Float < 0.0F) {
            param1Float = -1.0F;
          } else {
            param1Float = 1.0F;
          } 
          if (param1ArrayOffloat[1] < 0.0F)
            f3 = 1.0F; 
          ControlActivity.this.rxTxProtocol.setRoll(f1 * param1Float + param1Int1);
          ControlActivity.this.rxTxProtocol.setPitch(f2 * f3 + param1Int3);
        }
        
        public void onReplayTrackActionCancel() {
          ControlActivity.this.rxTxProtocol.setRoll(getRollControlSliderCurrentLevel());
          ControlActivity.this.rxTxProtocol.setPitch(getPitchControlSliderCurrentLevel());
        }
        
        public void onRockerAction(float param1Float1, float param1Float2, int param1Int1, int param1Int2, float param1Float3, float param1Float4, int param1Int3, int param1Int4, int param1Int5, int param1Int6) {
          float f;
          if (ControlActivity.this.speedState == 1) {
            f = 0.6F;
          } else if (ControlActivity.this.speedState == 2) {
            f = 0.8F;
          } else {
            f = 0.4F;
          } 
          ControlActivity.this.rxTxProtocol.setYaw(param1Float1 * f + param1Int1);
          ControlActivity.this.rxTxProtocol.setAccelerator(param1Float2);
          ControlActivity.this.rxTxProtocol.setRoll(param1Float3 * f + param1Int3);
          ControlActivity.this.rxTxProtocol.setPitch(param1Float4 * f + param1Int5);
          if (ControlActivity.this.autoSaveParams) {
            if (ControlActivity.this.pitchSliderLevel != param1Int5)
              Constants.setPitchSliderLevel(ControlActivity.this.getApplicationContext(), param1Int5); 
            if (ControlActivity.this.rollSliderLevel != param1Int3)
              Constants.setRollSliderLevel(ControlActivity.this.getApplicationContext(), param1Int3); 
            if (ControlActivity.this.yawSliderLevel != param1Int1)
              Constants.setYawSliderLevel(ControlActivity.this.getApplicationContext(), param1Int1); 
          } 
        }
      };
    this.rockerControlPanel.setControlListener(this.controlListener);
    WifiInfo wifiInfo = ((WifiManager)getApplicationContext().getSystemService("wifi")).getConnectionInfo();
    this.wifiName = wifiInfo.getSSID();
    if (this.devices.getRxTxCommunicator() instanceof JRRxTxCommunicator) {
      ((JRRxTxCommunicator)this.devices.getRxTxCommunicator()).setMac(wifiInfo.getBSSID());
      ((JRRxTxCommunicator)this.devices.getRxTxCommunicator()).setEncryptData(true);
    } 
  }
  
  protected void onPause() {
    super.onPause();
    this.ijkVideoView.stopPlayback();
    Devices devices = this.devices;
    if (devices != null) {
      devices.getCMDCommunicator().disconnect();
      this.controlBarRecordVideo.setChecked(false);
      this.controlBarMvRecordVideo.setChecked(false);
      toggleRecord(false, false);
      this.rxTxProtocol.setSimpleDroneMsgCallback(null);
      this.devices.getCMDCommunicator().setConnectResultCallback(null);
    } 
    Disposable disposable = this.refreshSignalTask;
    if (disposable != null)
      disposable.dispose(); 
    this.mControlBarVoiceControl.setChecked(false);
    toggleVoiceControl(false);
    this.controlBarHumanoidFollower.setChecked(false);
  }
  
  protected void onResume() {
    super.onResume();
    hideVirtualButtons();
    if (BGMPicker.isOpenBGMMode(getApplicationContext())) {
      this.controlBarMusicName.setText(BGMPicker.getBGMName(getApplicationContext()));
    } else {
      this.controlBarMusicName.setText(2131624082);
    } 
    Devices devices = this.devices;
    if (devices != null) {
      devices.getCMDCommunicator().setConnectResultCallback(new ConnectResultCallback() {
            public void onConnectFail(int param1Int, String param1String) {}
            
            public void onConnectSuccess(int param1Int, String param1String) {
              if (Constants.getIsRotateVideo((Context)ControlActivity.this))
                ControlActivity.this.controlBackground.postDelayed(new Runnable() {
                      public void run() {
                        ControlActivity.this.devices.getCMDCommunicator().rotateVideo(true, 5, Constants.getIsRotateDegree180(ControlActivity.this.getApplicationContext()), new CMDCommunicator.OnExecuteCMDResult() {
                              public void onResult(int param3Int, String param3String) {
                                if (param3Int >= 0) {
                                  Constants.setIsRotateVideo(ControlActivity.this.getApplicationContext(), false);
                                  Constants.setIsRotateDegree180(ControlActivity.this.getApplicationContext(), Constants.getIsRotateDegree180(ControlActivity.this.getApplicationContext()) ^ true);
                                } 
                              }
                            });
                      }
                    }500L); 
              ControlActivity.this.devices.getCMDCommunicator().getRemoteSDCardStatus(false, 5, new CMDCommunicator.OnExecuteCMDResult() {
                    public void onResult(int param2Int, String param2String) {
                      ControlActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                              if ((ControlActivity.this.devices.getCMDCommunicator()).SDCardState == 1) {
                                ControlActivity.this.controlBarRecordVideo.setBackgroundResource(2131165333);
                              } else {
                                ControlActivity.this.controlBarRecordVideo.setBackgroundResource(2131165330);
                              } 
                            }
                          });
                    }
                  });
            }
          });
      this.devices.getCMDCommunicator().setShouldReconnectTimes(-1);
      this.devices.getCMDCommunicator().setAutoReconnect(true, 3);
      this.devices.getCMDCommunicator().connect();
      this.devices.getVideoCommunicator().setShouldReconnectTimes(-1);
      this.devices.getVideoCommunicator().setReadFrameTimeOut(3000);
      this.ijkVideoView.setRender(0);
      this.devices.getVideoCommunicator().setPlaybackUrl(null);
      this.ijkVideoView.setVideoCommunicator(this.devices.getVideoCommunicator());
      if (!Constants.getIs1080PPreview((Context)this))
        this.ijkVideoView.setUsingMediaCodec(false); 
      this.ijkVideoView.start();
      this.devices.getRxTxCommunicator().setShouldReconnectTimes(-1);
      this.devices.getRxTxCommunicator().setAutoReconnect(true, -1);
      this.devices.getRxTxCommunicator().connect();
      if (this.devices.getRxTxCommunicator() instanceof AnykaRxTxCommunicator)
        ((AnykaRxTxCommunicator)this.devices.getRxTxCommunicator()).setConnectInternalCallback(new ConnectInternalCallback() {
              public void connectSuccess() {
                ControlActivity.access$1902(ControlActivity.this, System.currentTimeMillis());
              }
            }); 
      if (this.devices.getVideoCommunicator() instanceof AnykaVideoCommunicator)
        ((AnykaVideoCommunicator)this.devices.getVideoCommunicator()).setConnectInternalCallback(new ConnectInternalCallback() {
              public void connectSuccess() {
                ControlActivity.access$1902(ControlActivity.this, System.currentTimeMillis());
              }
            }); 
      this.devices.getRxTxCommunicator().startSendHeartBeatPackage(1000, new byte[] { 0 });
      this.devices.getCMDCommunicator().setOnRemoteCMDListener(this.onRemoteCMDListener);
      this.rxTxProtocol.setSimpleDroneMsgCallback(this.simpleDroneMsgCallback);
      this.refreshSignalTask = Observable.interval(1L, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            public void accept(Long param1Long) throws Exception {
              int i = ((WifiManager)ControlActivity.this.getApplicationContext().getSystemService("wifi")).getConnectionInfo().getRssi();
              int j = WifiManager.calculateSignalLevel(i, 4);
              ControlActivity.this.mControlBarWifiSignal.getDrawable().setLevel(j);
              if (i < -80) {
                if (ControlActivity.this.mSignalWeakTips.getVisibility() != 0)
                  ControlActivity.this.mSignalWeakTips.setVisibility(0); 
              } else if (ControlActivity.this.mSignalWeakTips.getVisibility() == 0) {
                ControlActivity.this.mSignalWeakTips.setVisibility(4);
              } 
            }
          });
    } 
    this.receiveImgContainer.setVisibility(4);
  }
  
  public void onWindowFocusChanged(boolean paramBoolean) {
    super.onWindowFocusChanged(paramBoolean);
    if (paramBoolean)
      hideVirtualButtons(); 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/drone/activitys/ControlActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
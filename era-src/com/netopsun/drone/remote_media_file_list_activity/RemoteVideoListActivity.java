package com.netopsun.drone.remote_media_file_list_activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.netopsun.deviceshub.base.CMDCommunicator;
import com.netopsun.deviceshub.base.Devices;
import com.netopsun.deviceshub.base.bean.RemoteVideoFiles;
import com.netopsun.deviceshub.interfaces.Cancelable;
import com.netopsun.drone.Constants;
import com.netopsun.drone.DevicesUtil;
import com.netopsun.drone.activitys.RemoteVideoActivity;
import com.shizhefei.mvc.IAsyncDataSource;
import com.shizhefei.mvc.MVCCoolHelper;
import com.shizhefei.mvc.MVCHelper;
import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;
import com.shizhefei.view.coolrefreshview.CoolRefreshView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RemoteVideoListActivity extends AppCompatActivity implements View.OnClickListener {
  private ListView fileListView;
  
  private View mBtnClose;
  
  private MVCHelper mvcHelper;
  
  private CoolRefreshView refreshView;
  
  private void deleteRemoteFile(final int position, Devices paramDevices, final PlaybackVideoDataAdapter listAdapter) {
    final ProgressDialog progressDialog = ProgressDialog.show((Context)this, "wait", "");
    String str = ((RemoteVideoFiles.RemoteVideoFilesBean)listAdapter.getData().get(position)).getVideo_name();
    paramDevices.getCMDCommunicator().deleteRemoteRecordFile(true, 6, str, new CMDCommunicator.OnExecuteCMDResult() {
          public void onResult(final int errorCode, String param1String) {
            progressDialog.dismiss();
            RemoteVideoListActivity.this.runOnUiThread(new Runnable() {
                  public void run() {
                    if (errorCode < 0) {
                      Toast.makeText((Context)RemoteVideoListActivity.this, "fail", 0).show();
                    } else {
                      Toast.makeText((Context)RemoteVideoListActivity.this, "success", 0).show();
                      listAdapter.getData().remove(listAdapter.getData().get(position));
                    } 
                    listAdapter.notifyDataSetChanged();
                  }
                });
          }
        });
  }
  
  private void downloadRemoteFile(int paramInt, Devices paramDevices, PlaybackVideoDataAdapter paramPlaybackVideoDataAdapter) {
    Context context;
    String str2 = ((RemoteVideoFiles.RemoteVideoFilesBean)paramPlaybackVideoDataAdapter.getData().get(paramInt)).getVideo_name();
    paramInt = str2.lastIndexOf("/");
    if (paramInt > 0) {
      paramInt++;
    } else {
      paramInt = 0;
    } 
    String str3 = str2.substring(paramInt);
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append(Constants.getVideoPath(getApplicationContext()));
    stringBuilder1.append(str3);
    String str4 = stringBuilder1.toString();
    final String finalPathname = str4;
    if (!str4.contains("mp4")) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str4);
      stringBuilder.append(".mp4");
      str1 = stringBuilder.toString();
    } 
    if ((new File(str1)).exists()) {
      context = getApplicationContext();
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("already download:");
      stringBuilder.append(str1);
      Toast.makeText(context, stringBuilder.toString(), 0).show();
      return;
    } 
    final ProgressDialog progressDialog = new ProgressDialog((Context)this);
    progressDialog.setTitle("wait");
    CMDCommunicator cMDCommunicator = context.getCMDCommunicator();
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append(Constants.getVideoPath(getApplicationContext()));
    stringBuilder2.append(str3);
    final Cancelable cancelable = cMDCommunicator.downloadRemoteRecordFile(true, 6, str2, stringBuilder2.toString(), new CMDCommunicator.OnExecuteCMDResult() {
          public void onResult(final int errorCode, String param1String) {
            progressDialog.dismiss();
            RemoteVideoListActivity.this.runOnUiThread(new Runnable() {
                  public void run() {
                    if (errorCode < 0) {
                      Toast.makeText((Context)RemoteVideoListActivity.this, "fail", 0).show();
                    } else {
                      MediaScannerConnection.scanFile(RemoteVideoListActivity.this.getApplicationContext(), new String[] { this.this$1.val$finalPathname }, null, null);
                      Toast.makeText((Context)RemoteVideoListActivity.this, "success", 0).show();
                    } 
                  }
                });
          }
        });
    progressDialog.setButton(-2, getString(2131624020), new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            cancelable.cancel();
          }
        });
    progressDialog.show();
  }
  
  private void initView() {
    this.fileListView = (ListView)findViewById(2131230894);
    this.refreshView = (CoolRefreshView)findViewById(2131230980);
    this.mBtnClose = findViewById(2131230810);
    this.mBtnClose.setOnClickListener(this);
  }
  
  private void showDeleteDialog(final int position, final Devices devices, final PlaybackVideoDataAdapter listAdapter) {
    (new AlertDialog.Builder((Context)this)).setMessage(getString(2131624050)).setPositiveButton(getString(2131624076), new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            RemoteVideoListActivity.this.deleteRemoteFile(position, devices, listAdapter);
          }
        }).setNegativeButton(getString(2131624020), new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {}
        }).show();
  }
  
  public void finish() {
    super.finish();
    this.mvcHelper.destory();
  }
  
  public void onClick(View paramView) {
    if (paramView.getId() == 2131230810)
      finish(); 
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    getWindow().setFlags(1024, 1024);
    setContentView(2131427368);
    initView();
    final Devices devices = DevicesUtil.getCurrentConnectDevices();
    if (devices != null) {
      CMDCommunicator cMDCommunicator = devices.getCMDCommunicator();
      cMDCommunicator.connect();
    } else {
      paramBundle = null;
    } 
    final PlaybackVideoDataAdapter listAdapter = new PlaybackVideoDataAdapter((Context)this);
    playbackVideoDataAdapter.setOnDeleteClickListener(new PlaybackVideoDataAdapter.OnItemButtonClickListener() {
          public void onDeleteButtonClick(View param1View, int param1Int) {
            Devices devices = devices;
            if (devices == null)
              return; 
            RemoteVideoListActivity.this.showDeleteDialog(param1Int, devices, listAdapter);
          }
          
          public void onDownloadButtonClick(View param1View, int param1Int) {
            Devices devices = devices;
            if (devices == null)
              return; 
            RemoteVideoListActivity.this.downloadRemoteFile(param1Int, devices, listAdapter);
          }
        });
    this.fileListView.setAdapter((ListAdapter)playbackVideoDataAdapter);
    this.fileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
            if (devices == null)
              return; 
            String str = ((RemoteVideoFiles.RemoteVideoFilesBean)listAdapter.getData().get(param1Int)).getVideo_name();
            devices.getVideoCommunicator().setPlaybackUrl(str);
            devices.getVideoCommunicator().setPlaybackStartTime(((RemoteVideoFiles.RemoteVideoFilesBean)listAdapter.getData().get(param1Int)).getRecord_start_time());
            devices.getVideoCommunicator().setPlaybackDuration(((RemoteVideoFiles.RemoteVideoFilesBean)listAdapter.getData().get(param1Int)).getDuration());
            Intent intent = new Intent((Context)RemoteVideoListActivity.this, RemoteVideoActivity.class);
            RemoteVideoListActivity.this.startActivity(intent);
          }
        });
    this.mvcHelper = (MVCHelper)new MVCCoolHelper(this.refreshView);
    this.mvcHelper.setDataSource(new PlayBackVideoDataSource((CMDCommunicator)paramBundle));
    this.mvcHelper.setAdapter(playbackVideoDataAdapter);
    this.mvcHelper.refresh();
  }
  
  private static class PlayBackVideoDataSource implements IAsyncDataSource<List<RemoteVideoFiles.RemoteVideoFilesBean>> {
    final CMDCommunicator cmdCommunicator;
    
    boolean hasMore = true;
    
    String lastFileName = "";
    
    PlayBackVideoDataSource(CMDCommunicator param1CMDCommunicator) {
      this.cmdCommunicator = param1CMDCommunicator;
    }
    
    private RequestHandle Cancelable2RequestHandle(final Cancelable cancelable) {
      return new RequestHandle() {
          boolean isRunning = true;
          
          public void cancle() {
            Cancelable cancelable = cancelable;
            if (cancelable != null)
              cancelable.cancel(); 
            this.isRunning = false;
          }
          
          public boolean isRunning() {
            return this.isRunning;
          }
        };
    }
    
    private Cancelable getIpcamRecordFileList(final ResponseSender<List<RemoteVideoFiles.RemoteVideoFilesBean>> sender) {
      CMDCommunicator cMDCommunicator = this.cmdCommunicator;
      if (cMDCommunicator == null) {
        sender.sendError(new Exception("未连接"));
        return null;
      } 
      cMDCommunicator.connect();
      return this.cmdCommunicator.getRemoteRecordFileList(true, 15, 10, this.lastFileName, new CMDCommunicator.OnExecuteCMDResult() {
            public void onResult(int param2Int, String param2String) {
              StringBuilder stringBuilder;
              if (param2Int != 0) {
                ResponseSender responseSender = sender;
                stringBuilder = new StringBuilder();
                stringBuilder.append("error:");
                stringBuilder.append(param2Int);
                responseSender.sendError(new Exception(stringBuilder.toString()));
              } else {
                RemoteVideoFiles remoteVideoFiles = RemoteVideoListActivity.PlayBackVideoDataSource.this.cmdCommunicator.string2RemoteVideoFiles((String)stringBuilder);
                ArrayList<String> arrayList = new ArrayList();
                if (remoteVideoFiles == null || remoteVideoFiles.getRemote_video_files().size() == 0) {
                  RemoteVideoListActivity.PlayBackVideoDataSource.this.hasMore = false;
                } else {
                  for (param2Int = 0; param2Int < remoteVideoFiles.getRemote_video_files().size(); param2Int++)
                    arrayList.add(((RemoteVideoFiles.RemoteVideoFilesBean)remoteVideoFiles.getRemote_video_files().get(param2Int)).getVideo_name()); 
                  if (remoteVideoFiles.getRemote_video_files().size() < 10)
                    RemoteVideoListActivity.PlayBackVideoDataSource.this.hasMore = false; 
                  RemoteVideoListActivity.PlayBackVideoDataSource.this.lastFileName = arrayList.get(arrayList.size() - 1);
                } 
                if (RemoteVideoListActivity.PlayBackVideoDataSource.this.cmdCommunicator instanceof com.netopsun.anykadevices.AnykaCMDCommunicator)
                  RemoteVideoListActivity.PlayBackVideoDataSource.this.hasMore = false; 
                sender.sendData(remoteVideoFiles.getRemote_video_files());
              } 
            }
          });
    }
    
    public boolean hasMore() {
      return this.hasMore;
    }
    
    public RequestHandle loadMore(ResponseSender<List<RemoteVideoFiles.RemoteVideoFilesBean>> param1ResponseSender) throws Exception {
      return Cancelable2RequestHandle(getIpcamRecordFileList(param1ResponseSender));
    }
    
    public RequestHandle refresh(ResponseSender<List<RemoteVideoFiles.RemoteVideoFilesBean>> param1ResponseSender) throws Exception {
      this.lastFileName = "";
      this.hasMore = true;
      return Cancelable2RequestHandle(getIpcamRecordFileList(param1ResponseSender));
    }
  }
  
  class null extends CMDCommunicator.OnExecuteCMDResult {
    public void onResult(int param1Int, String param1String) {
      StringBuilder stringBuilder;
      if (param1Int != 0) {
        ResponseSender responseSender = sender;
        stringBuilder = new StringBuilder();
        stringBuilder.append("error:");
        stringBuilder.append(param1Int);
        responseSender.sendError(new Exception(stringBuilder.toString()));
      } else {
        RemoteVideoFiles remoteVideoFiles = this.this$0.cmdCommunicator.string2RemoteVideoFiles((String)stringBuilder);
        ArrayList<String> arrayList = new ArrayList();
        if (remoteVideoFiles == null || remoteVideoFiles.getRemote_video_files().size() == 0) {
          this.this$0.hasMore = false;
        } else {
          for (param1Int = 0; param1Int < remoteVideoFiles.getRemote_video_files().size(); param1Int++)
            arrayList.add(((RemoteVideoFiles.RemoteVideoFilesBean)remoteVideoFiles.getRemote_video_files().get(param1Int)).getVideo_name()); 
          if (remoteVideoFiles.getRemote_video_files().size() < 10)
            this.this$0.hasMore = false; 
          this.this$0.lastFileName = arrayList.get(arrayList.size() - 1);
        } 
        if (this.this$0.cmdCommunicator instanceof com.netopsun.anykadevices.AnykaCMDCommunicator)
          this.this$0.hasMore = false; 
        sender.sendData(remoteVideoFiles.getRemote_video_files());
      } 
    }
  }
  
  class null implements RequestHandle {
    boolean isRunning = true;
    
    public void cancle() {
      Cancelable cancelable = cancelable;
      if (cancelable != null)
        cancelable.cancel(); 
      this.isRunning = false;
    }
    
    public boolean isRunning() {
      return this.isRunning;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/drone/remote_media_file_list_activity/RemoteVideoListActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
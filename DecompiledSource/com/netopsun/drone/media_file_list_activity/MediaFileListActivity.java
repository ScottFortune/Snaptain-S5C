package com.netopsun.drone.media_file_list_activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.netopsun.drone.BaseSensorLandscapeActivity;
import com.netopsun.drone.Constants;
import com.netopsun.drone.activitys.VideoActivity;
import com.netopsun.drone.media_file_list_activity.recycleview.MediaFileBean;
import com.netopsun.drone.media_file_list_activity.recycleview.MediaFileListAdapter;
import com.netopsun.drone.photo_activity.PhotoActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class MediaFileListActivity extends BaseSensorLandscapeActivity implements View.OnClickListener {
  public static final String IS_VIDEO = "is_video";
  
  private boolean alreadyChooseAll;
  
  private Disposable discoverFileTask;
  
  private boolean isVideo = false;
  
  private TextView mBtnBack;
  
  private TextView mBtnDelete;
  
  private TextView mBtnEdit;
  
  private TextView mBtnSelectAll;
  
  private RecyclerView mFileListView;
  
  private final List<MediaFileBean> mediaFileBeanList = new ArrayList<MediaFileBean>();
  
  private MediaFileListAdapter mediaFileListAdapter;
  
  private void initView() {
    this.mBtnBack = (TextView)findViewById(2131230809);
    this.mBtnBack.setOnClickListener(this);
    this.mBtnDelete = (TextView)findViewById(2131230811);
    this.mBtnDelete.setOnClickListener(this);
    this.mBtnSelectAll = (TextView)findViewById(2131230813);
    this.mBtnSelectAll.setOnClickListener(this);
    this.mBtnEdit = (TextView)findViewById(2131230812);
    this.mBtnEdit.setOnClickListener(this);
    this.mFileListView = (RecyclerView)findViewById(2131230894);
  }
  
  private void launchPhotoActivity(List<MediaFileBean> paramList, int paramInt) {
    ArrayList<String> arrayList = new ArrayList();
    Iterator<MediaFileBean> iterator = paramList.iterator();
    while (iterator.hasNext())
      arrayList.add(((MediaFileBean)iterator.next()).getFilePath()); 
    PhotoActivity.launch((Context)this, arrayList, paramInt);
  }
  
  private void playMedia(List<MediaFileBean> paramList, int paramInt, String paramString) {
    Uri uri;
    Intent intent = new Intent("android.intent.action.VIEW");
    File file = new File(((MediaFileBean)paramList.get(paramInt)).getFilePath());
    if (Build.VERSION.SDK_INT >= 24) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append((getApplicationInfo()).processName);
      stringBuilder.append(".fileprovider");
      uri = FileProvider.getUriForFile((Context)this, stringBuilder.toString(), file);
    } else {
      uri = Uri.fromFile((File)uri);
    } 
    intent.addFlags(268468224);
    intent.addFlags(1);
    intent.setDataAndType(uri, paramString);
    startActivity(intent);
  }
  
  public void onClick(View paramView) {
    switch (paramView.getId()) {
      default:
        return;
      case 2131230813:
        if (this.alreadyChooseAll) {
          this.mediaFileListAdapter.chooseNone();
        } else {
          this.mediaFileListAdapter.chooseAll();
        } 
        this.alreadyChooseAll ^= 0x1;
      case 2131230812:
        if (this.mBtnDelete.getVisibility() == 0) {
          this.mediaFileListAdapter.setChooseMode(false);
          this.mBtnDelete.setVisibility(4);
          this.mBtnSelectAll.setVisibility(4);
        } else {
          this.mediaFileListAdapter.setChooseMode(true);
          this.mBtnDelete.setVisibility(0);
          this.mBtnSelectAll.setVisibility(0);
        } 
      case 2131230811:
        (new AlertDialog.Builder((Context)this)).setMessage(getString(2131624050)).setPositiveButton(getString(2131624076), new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                MediaFileListActivity.this.mediaFileListAdapter.deleteChooseFile();
              }
            }).setNegativeButton(getString(2131624020), new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {}
            }).show();
      case 2131230809:
        break;
    } 
    finish();
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    getWindow().setFlags(1024, 1024);
    setContentView(2131427360);
    initView();
    this.isVideo = getIntent().getBooleanExtra("is_video", false);
    this.mFileListView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager((Context)this));
    this.mediaFileListAdapter = new MediaFileListAdapter((Context)this, this.mediaFileBeanList);
    this.mFileListView.setAdapter((RecyclerView.Adapter)this.mediaFileListAdapter);
    this.mediaFileListAdapter.setItemClickListener(new MediaFileListAdapter.OnItemClickListener() {
          public void onItemClick(View param1View, List<MediaFileBean> param1List, int param1Int) {
            String str = ((MediaFileBean)param1List.get(param1Int)).getFileName().toLowerCase();
            if (str.endsWith("mp4") || str.endsWith("avi")) {
              VideoActivity.launch((Context)MediaFileListActivity.this, ((MediaFileBean)param1List.get(param1Int)).getFilePath());
              return;
            } 
            MediaFileListActivity.this.launchPhotoActivity(param1List, param1Int);
          }
        });
    this.discoverFileTask = Observable.create(new ObservableOnSubscribe<List<MediaFileBean>>() {
          public void subscribe(ObservableEmitter<List<MediaFileBean>> param1ObservableEmitter) throws Exception {
            File[] arrayOfFile;
            if (MediaFileListActivity.this.isVideo) {
              arrayOfFile = (new File(Constants.getVideoPath(MediaFileListActivity.this.getApplicationContext()))).listFiles(new FileFilter() {
                    public boolean accept(File param2File) {
                      return (param2File.getName().toLowerCase().endsWith(".mp4") || param2File.getName().toLowerCase().endsWith(".avi"));
                    }
                  });
            } else {
              arrayOfFile = (new File(Constants.getPhotoPath(MediaFileListActivity.this.getApplicationContext()))).listFiles(new FileFilter() {
                    public boolean accept(File param2File) {
                      return (param2File.getName().toLowerCase().endsWith(".jpg") || param2File.getName().toLowerCase().endsWith(".png"));
                    }
                  });
            } 
            if (arrayOfFile == null || arrayOfFile.length == 0) {
              param1ObservableEmitter.onComplete();
              return;
            } 
            List<File> list = Arrays.asList(arrayOfFile);
            Collections.sort(list, new Comparator<File>() {
                  public int compare(File param2File1, File param2File2) {
                    long l = param2File1.lastModified() - param2File2.lastModified();
                    return (l > 0L) ? -1 : ((l < 0L) ? 1 : 0);
                  }
                });
            long l = 0L;
            ArrayList<MediaFileBean> arrayList = new ArrayList();
            for (File file : list) {
              String str = (new SimpleDateFormat("yyyy-MM-dd")).format(Long.valueOf(file.lastModified()));
              long l1 = l;
              if (file.lastModified() / 86400000L != l) {
                MediaFileBean mediaFileBean1 = new MediaFileBean();
                mediaFileBean1.setGroupTitle(str);
                arrayList.add(mediaFileBean1);
                l1 = file.lastModified() / 86400000L;
              } 
              MediaFileBean mediaFileBean = new MediaFileBean();
              mediaFileBean.setFileName(file.getName());
              mediaFileBean.setFilePath(file.getAbsolutePath());
              mediaFileBean.setPhoto(file.getName().toLowerCase().endsWith(".jpg"));
              mediaFileBean.setFileModified(str);
              float f = (float)file.length();
              if (f >= 1048576.0F) {
                f /= 1048576.0F;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append((new DecimalFormat("#.00")).format(f));
                stringBuilder.append("MB");
                String str1 = stringBuilder.toString();
              } else if (f >= 1024.0F) {
                f /= 1024.0F;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append((new DecimalFormat("#.00")).format(f));
                stringBuilder.append("KB");
                String str1 = stringBuilder.toString();
              } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(f);
                stringBuilder.append("B");
                str = stringBuilder.toString();
              } 
              mediaFileBean.setFileSize(str);
              arrayList.add(mediaFileBean);
              l = l1;
            } 
            param1ObservableEmitter.onNext(arrayList);
            param1ObservableEmitter.onComplete();
          }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<MediaFileBean>>() {
          public void accept(List<MediaFileBean> param1List) throws Exception {
            MediaFileListActivity.this.mediaFileBeanList.addAll(param1List);
            MediaFileListActivity.this.mediaFileListAdapter.notifyDataSetChanged();
          }
        });
  }
  
  protected void onDestroy() {
    this.discoverFileTask.dispose();
    super.onDestroy();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/drone/media_file_list_activity/MediaFileListActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
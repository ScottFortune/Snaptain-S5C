package com.shizhefei.mvc;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import com.shizhefei.mvc.imp.DefaultLoadViewFactory;
import com.shizhefei.mvc.viewhandler.ListViewHandler;
import com.shizhefei.mvc.viewhandler.RecyclerViewHandler;
import com.shizhefei.mvc.viewhandler.ViewHandler;
import com.shizhefei.task.Code;
import com.shizhefei.task.IAsyncTask;
import com.shizhefei.task.ICallback;
import com.shizhefei.task.ISuperTask;
import com.shizhefei.task.ITask;
import com.shizhefei.task.TaskHelper;
import com.shizhefei.task.imp.SimpleCallback;
import com.shizhefei.utils.NetworkUtils;

public class MVCHelper<DATA> {
  public static ILoadViewFactory loadViewFactory = (ILoadViewFactory)new DefaultLoadViewFactory();
  
  private boolean autoLoadMore = true;
  
  private View contentView;
  
  private Context context;
  
  private IDataAdapter<DATA> dataAdapter;
  
  private ISuperTask<DATA> dataSource;
  
  private Handler handler;
  
  private boolean hasInitLoadMoreView = false;
  
  private boolean hasMoreData = true;
  
  private ListViewHandler listViewHandler = new ListViewHandler();
  
  private long loadDataTime = -1L;
  
  private SimpleCallback<DATA> loadMoreCallback = new SimpleCallback<DATA>() {
      public void onPostExecute(Object param1Object, Code param1Code, Exception param1Exception, DATA param1DATA) {
        Code code = param1Code;
        Exception exception = param1Exception;
        if (param1Code == Code.SUCCESS) {
          code = param1Code;
          exception = param1Exception;
          if (param1DATA == null) {
            code = Code.EXCEPTION;
            exception = new Exception("数据不能返回null");
          } 
        } 
        int i = MVCHelper.null.$SwitchMap$com$shizhefei$task$Code[code.ordinal()];
        if (i != 1) {
          if (i == 2) {
            MVCHelper.access$802(MVCHelper.this, null);
            MVCHelper.this.mLoadView.tipFail(exception);
            if (MVCHelper.this.hasInitLoadMoreView && MVCHelper.this.mLoadMoreView != null)
              MVCHelper.this.mLoadMoreView.showFail(exception); 
          } 
        } else {
          MVCHelper.access$802(MVCHelper.this, null);
          MVCHelper.this.dataAdapter.notifyDataChanged(param1DATA, false);
          if (MVCHelper.this.dataAdapter.isEmpty()) {
            MVCHelper.this.mLoadView.showEmpty();
          } else {
            MVCHelper.this.mLoadView.restore();
          } 
          MVCHelper mVCHelper = MVCHelper.this;
          MVCHelper.access$1002(mVCHelper, mVCHelper.hasMore(param1Object));
          if (MVCHelper.this.hasInitLoadMoreView && MVCHelper.this.mLoadMoreView != null)
            if (MVCHelper.this.hasMoreData) {
              MVCHelper.this.mLoadMoreView.showNormal();
            } else {
              MVCHelper.this.mLoadMoreView.showNomore();
            }  
        } 
        if (MVCHelper.this.onStateChangeListener != null)
          MVCHelper.this.onStateChangeListener.onEndLoadMore(MVCHelper.this.dataAdapter, param1DATA); 
      }
      
      public void onPreExecute(Object param1Object) {
        super.onPreExecute(param1Object);
        if (MVCHelper.this.onStateChangeListener != null)
          MVCHelper.this.onStateChangeListener.onStartLoadMore(MVCHelper.this.dataAdapter); 
        if (MVCHelper.this.hasInitLoadMoreView && MVCHelper.this.mLoadMoreView != null)
          MVCHelper.this.mLoadMoreView.showLoading(); 
      }
    };
  
  private ILoadViewFactory.ILoadMoreView mLoadMoreView;
  
  private ILoadViewFactory.ILoadView mLoadView;
  
  private boolean needCheckNetwork = true;
  
  private View.OnClickListener onClickLoadMoreListener = new View.OnClickListener() {
      public void onClick(View param1View) {
        MVCHelper.this.loadMore();
      }
    };
  
  private View.OnClickListener onClickRefresListener = new View.OnClickListener() {
      public void onClick(View param1View) {
        MVCHelper.this.refresh();
      }
    };
  
  private IRefreshView.OnRefreshListener onRefreshListener = new IRefreshView.OnRefreshListener() {
      public void onRefresh() {
        MVCHelper.this.refresh();
      }
    };
  
  private OnScrollBottomListener onScrollBottomListener = new OnScrollBottomListener() {
      public void onScorllBootom() {
        if (MVCHelper.this.autoLoadMore && MVCHelper.this.hasMoreData && !MVCHelper.this.isLoading())
          if (MVCHelper.this.needCheckNetwork && !NetworkUtils.hasNetwork(MVCHelper.this.context)) {
            MVCHelper.this.mLoadMoreView.showFail(new Exception("网络不可用"));
          } else {
            MVCHelper.this.loadMore();
          }  
      }
    };
  
  private MOnStateChangeListener<DATA> onStateChangeListener = new MOnStateChangeListener<DATA>();
  
  private RecyclerViewHandler recyclerViewHandler = new RecyclerViewHandler();
  
  private SimpleCallback<DATA> refreshCallback = new SimpleCallback<DATA>() {
      public Runnable showRefreshing;
      
      public void onPostExecute(Object param1Object, Code param1Code, Exception param1Exception, DATA param1DATA) {
        MVCHelper.this.handler.removeCallbacks(this.showRefreshing);
        MVCHelper.this.refreshView.showRefreshComplete();
        Code code = param1Code;
        Exception exception = param1Exception;
        if (param1Code == Code.SUCCESS) {
          code = param1Code;
          exception = param1Exception;
          if (param1DATA == null) {
            code = Code.EXCEPTION;
            exception = new Exception("数据不能返回null");
          } 
        } 
        int i = MVCHelper.null.$SwitchMap$com$shizhefei$task$Code[code.ordinal()];
        if (i != 1) {
          if (i == 2) {
            MVCHelper.access$802(MVCHelper.this, null);
            if (MVCHelper.this.dataAdapter.isEmpty()) {
              MVCHelper.this.mLoadView.showFail(exception);
            } else {
              MVCHelper.this.mLoadView.tipFail(exception);
            } 
          } 
        } else {
          MVCHelper.access$802(MVCHelper.this, null);
          MVCHelper.access$902(MVCHelper.this, System.currentTimeMillis());
          MVCHelper.this.dataAdapter.notifyDataChanged(param1DATA, true);
          if (MVCHelper.this.dataAdapter.isEmpty()) {
            MVCHelper.this.mLoadView.showEmpty();
          } else {
            MVCHelper.this.mLoadView.restore();
          } 
          MVCHelper mVCHelper = MVCHelper.this;
          MVCHelper.access$1002(mVCHelper, mVCHelper.hasMore(param1Object));
          if (MVCHelper.this.hasInitLoadMoreView && MVCHelper.this.mLoadMoreView != null)
            if (MVCHelper.this.hasMoreData) {
              MVCHelper.this.mLoadMoreView.showNormal();
            } else {
              MVCHelper.this.mLoadMoreView.showNomore();
            }  
        } 
        if (MVCHelper.this.onStateChangeListener != null)
          MVCHelper.this.onStateChangeListener.onEndRefresh(MVCHelper.this.dataAdapter, param1DATA); 
      }
      
      public void onPreExecute(Object param1Object) {
        if (MVCHelper.this.hasInitLoadMoreView && MVCHelper.this.mLoadMoreView != null)
          MVCHelper.this.mLoadMoreView.showNormal(); 
        if (param1Object instanceof IDataCacheLoader) {
          param1Object = ((IDataCacheLoader)param1Object).loadCache(MVCHelper.this.dataAdapter.isEmpty());
          if (param1Object != null)
            MVCHelper.this.dataAdapter.notifyDataChanged(param1Object, true); 
        } 
        if (MVCHelper.this.dataAdapter.isEmpty()) {
          MVCHelper.this.mLoadView.showLoading();
        } else {
          MVCHelper.this.mLoadView.restore();
        } 
        Handler handler = MVCHelper.this.handler;
        param1Object = new Runnable() {
            public void run() {
              if (MVCHelper.this.dataAdapter.isEmpty()) {
                MVCHelper.this.refreshView.showRefreshComplete();
              } else {
                MVCHelper.this.refreshView.showRefreshing();
              } 
            }
          };
        this.showRefreshing = (Runnable)param1Object;
        handler.post((Runnable)param1Object);
        if (MVCHelper.this.onStateChangeListener != null)
          MVCHelper.this.onStateChangeListener.onStartRefresh(MVCHelper.this.dataAdapter); 
      }
    };
  
  private IRefreshView refreshView;
  
  private RequestHandle requestHandle;
  
  private TaskHelper<DATA> taskHelper;
  
  public MVCHelper(IRefreshView paramIRefreshView) {
    this(paramIRefreshView, loadViewFactory.madeLoadView(), loadViewFactory.madeLoadMoreView());
  }
  
  public MVCHelper(IRefreshView paramIRefreshView, ILoadViewFactory.ILoadView paramILoadView) {
    this(paramIRefreshView, paramILoadView, null);
  }
  
  public MVCHelper(IRefreshView paramIRefreshView, ILoadViewFactory.ILoadView paramILoadView, ILoadViewFactory.ILoadMoreView paramILoadMoreView) {
    this.context = paramIRefreshView.getContentView().getContext().getApplicationContext();
    this.autoLoadMore = true;
    this.refreshView = paramIRefreshView;
    this.contentView = paramIRefreshView.getContentView();
    this.contentView.setOverScrollMode(2);
    paramIRefreshView.setOnRefreshListener(this.onRefreshListener);
    this.mLoadView = paramILoadView;
    this.mLoadMoreView = paramILoadMoreView;
    this.taskHelper = new TaskHelper();
    this.mLoadView.init(paramIRefreshView.getSwitchView(), this.onClickRefresListener);
    this.handler = new Handler();
  }
  
  private boolean hasMore(Object paramObject) {
    return (paramObject instanceof IAsyncDataSource) ? ((IAsyncDataSource)paramObject).hasMore() : ((paramObject instanceof IDataSource) ? ((IDataSource)paramObject).hasMore() : false);
  }
  
  public static void setLoadViewFractory(ILoadViewFactory paramILoadViewFactory) {
    loadViewFactory = paramILoadViewFactory;
  }
  
  public void cancel() {
    RequestHandle requestHandle = this.requestHandle;
    if (requestHandle != null) {
      requestHandle.cancle();
      this.requestHandle = null;
    } 
    this.handler.removeCallbacksAndMessages(null);
  }
  
  public void destory() {
    cancel();
    this.taskHelper.destroy();
  }
  
  public IDataAdapter<DATA> getAdapter() {
    return this.dataAdapter;
  }
  
  public <T extends View> T getContentView() {
    return (T)this.refreshView.getContentView();
  }
  
  public ISuperTask<DATA> getDataSource() {
    return this.dataSource;
  }
  
  public long getLoadDataTime() {
    return this.loadDataTime;
  }
  
  public ILoadViewFactory.ILoadMoreView getLoadMoreView() {
    return this.mLoadMoreView;
  }
  
  public ILoadViewFactory.ILoadView getLoadView() {
    return this.mLoadView;
  }
  
  protected IRefreshView getRefreshView() {
    return this.refreshView;
  }
  
  public boolean isAutoLoadMore() {
    return this.autoLoadMore;
  }
  
  public boolean isLoading() {
    boolean bool;
    if (this.requestHandle != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void loadMore() {
    if (isLoading())
      return; 
    if (this.dataAdapter.isEmpty()) {
      refresh();
      return;
    } 
    if (this.dataAdapter == null || this.dataSource == null) {
      IRefreshView iRefreshView = this.refreshView;
      if (iRefreshView != null)
        iRefreshView.showRefreshComplete(); 
      return;
    } 
    RequestHandle requestHandle = this.requestHandle;
    if (requestHandle != null) {
      requestHandle.cancle();
      this.requestHandle = null;
    } 
    ISuperTask<DATA> iSuperTask = this.dataSource;
    if (iSuperTask instanceof IDataSource) {
      this.requestHandle = TaskHelper.createExecutor((IDataSource)iSuperTask, false, (ICallback)this.loadMoreCallback).execute();
    } else if (iSuperTask instanceof IAsyncDataSource) {
      this.requestHandle = TaskHelper.createExecutor((IAsyncDataSource)iSuperTask, false, (ICallback)this.loadMoreCallback).execute();
    } 
  }
  
  public void refresh() {
    if (this.dataAdapter == null || this.dataSource == null) {
      IRefreshView iRefreshView = this.refreshView;
      if (iRefreshView != null)
        iRefreshView.showRefreshComplete(); 
      return;
    } 
    RequestHandle requestHandle = this.requestHandle;
    if (requestHandle != null) {
      requestHandle.cancle();
      this.requestHandle = null;
    } 
    ISuperTask<DATA> iSuperTask = this.dataSource;
    if (iSuperTask instanceof IDataSource) {
      this.requestHandle = TaskHelper.createExecutor((IDataSource)iSuperTask, true, (ICallback)this.refreshCallback).execute();
    } else if (iSuperTask instanceof IAsyncDataSource) {
      this.requestHandle = TaskHelper.createExecutor((IAsyncDataSource)iSuperTask, true, (ICallback)this.refreshCallback).execute();
    } else if (iSuperTask instanceof ITask) {
      this.requestHandle = TaskHelper.createExecutor((ITask)iSuperTask, (ICallback)this.refreshCallback).execute();
    } else {
      this.requestHandle = TaskHelper.createExecutor((IAsyncTask)iSuperTask, (ICallback)this.refreshCallback).execute();
    } 
  }
  
  public void setAdapter(IDataAdapter<DATA> paramIDataAdapter) {
    setAdapter2(paramIDataAdapter, paramIDataAdapter);
  }
  
  public void setAdapter(IDataAdapter<DATA> paramIDataAdapter, ViewHandler paramViewHandler) {
    setAdapter2(paramIDataAdapter, paramIDataAdapter, paramViewHandler);
  }
  
  public void setAdapter2(Object paramObject, IDataAdapter<DATA> paramIDataAdapter) {
    View view = this.contentView;
    if (view instanceof android.widget.ListView) {
      setAdapter2(paramObject, paramIDataAdapter, (ViewHandler)this.listViewHandler);
    } else if (view instanceof androidx.recyclerview.widget.RecyclerView) {
      setAdapter2(paramObject, paramIDataAdapter, (ViewHandler)this.recyclerViewHandler);
    } else {
      setAdapter2(paramObject, paramIDataAdapter, null);
    } 
  }
  
  public void setAdapter2(Object paramObject, IDataAdapter<DATA> paramIDataAdapter, ViewHandler paramViewHandler) {
    this.hasInitLoadMoreView = false;
    if (paramViewHandler != null) {
      View view = (View)getContentView();
      this.hasInitLoadMoreView = paramViewHandler.handleSetAdapter(view, paramObject, this.mLoadMoreView, this.onClickLoadMoreListener);
      paramViewHandler.setOnScrollBottomListener(view, this.onScrollBottomListener);
    } 
    this.dataAdapter = paramIDataAdapter;
  }
  
  public void setAutoLoadMore(boolean paramBoolean) {
    this.autoLoadMore = paramBoolean;
  }
  
  public void setDataSource(IAsyncDataSource<DATA> paramIAsyncDataSource) {
    this.dataSource = paramIAsyncDataSource;
  }
  
  public void setDataSource(IDataSource<DATA> paramIDataSource) {
    this.dataSource = paramIDataSource;
  }
  
  public void setDataSource(IAsyncTask<DATA> paramIAsyncTask) {
    this.dataSource = (ISuperTask<DATA>)paramIAsyncTask;
  }
  
  public void setDataSource(ITask<DATA> paramITask) {
    this.dataSource = (ISuperTask<DATA>)paramITask;
  }
  
  public void setNeedCheckNetwork(boolean paramBoolean) {
    this.needCheckNetwork = paramBoolean;
  }
  
  public void setOnStateChangeListener(OnLoadMoreStateChangeListener<DATA> paramOnLoadMoreStateChangeListener) {
    this.onStateChangeListener.setOnLoadMoreStateChangeListener(paramOnLoadMoreStateChangeListener);
  }
  
  public void setOnStateChangeListener(OnRefreshStateChangeListener<DATA> paramOnRefreshStateChangeListener) {
    this.onStateChangeListener.setOnRefreshStateChangeListener(paramOnRefreshStateChangeListener);
  }
  
  public void setOnStateChangeListener(OnStateChangeListener<DATA> paramOnStateChangeListener) {
    this.onStateChangeListener.setOnStateChangeListener(paramOnStateChangeListener);
  }
  
  private static class MOnStateChangeListener<DATA> implements OnStateChangeListener<DATA> {
    private OnLoadMoreStateChangeListener<DATA> onLoadMoreStateChangeListener;
    
    private OnRefreshStateChangeListener<DATA> onRefreshStateChangeListener;
    
    private OnStateChangeListener<DATA> onStateChangeListener;
    
    private MOnStateChangeListener() {}
    
    public void onEndLoadMore(IDataAdapter<DATA> param1IDataAdapter, DATA param1DATA) {
      OnStateChangeListener<DATA> onStateChangeListener = this.onStateChangeListener;
      if (onStateChangeListener != null) {
        onStateChangeListener.onEndLoadMore(param1IDataAdapter, param1DATA);
      } else {
        OnLoadMoreStateChangeListener<DATA> onLoadMoreStateChangeListener = this.onLoadMoreStateChangeListener;
        if (onLoadMoreStateChangeListener != null)
          onLoadMoreStateChangeListener.onEndLoadMore(param1IDataAdapter, param1DATA); 
      } 
    }
    
    public void onEndRefresh(IDataAdapter<DATA> param1IDataAdapter, DATA param1DATA) {
      OnStateChangeListener<DATA> onStateChangeListener = this.onStateChangeListener;
      if (onStateChangeListener != null) {
        onStateChangeListener.onEndRefresh(param1IDataAdapter, param1DATA);
      } else {
        OnRefreshStateChangeListener<DATA> onRefreshStateChangeListener = this.onRefreshStateChangeListener;
        if (onRefreshStateChangeListener != null)
          onRefreshStateChangeListener.onEndRefresh(param1IDataAdapter, param1DATA); 
      } 
    }
    
    public void onStartLoadMore(IDataAdapter<DATA> param1IDataAdapter) {
      OnStateChangeListener<DATA> onStateChangeListener = this.onStateChangeListener;
      if (onStateChangeListener != null) {
        onStateChangeListener.onStartLoadMore(param1IDataAdapter);
      } else {
        OnLoadMoreStateChangeListener<DATA> onLoadMoreStateChangeListener = this.onLoadMoreStateChangeListener;
        if (onLoadMoreStateChangeListener != null)
          onLoadMoreStateChangeListener.onStartLoadMore(param1IDataAdapter); 
      } 
    }
    
    public void onStartRefresh(IDataAdapter<DATA> param1IDataAdapter) {
      OnStateChangeListener<DATA> onStateChangeListener = this.onStateChangeListener;
      if (onStateChangeListener != null) {
        onStateChangeListener.onStartRefresh(param1IDataAdapter);
      } else {
        OnRefreshStateChangeListener<DATA> onRefreshStateChangeListener = this.onRefreshStateChangeListener;
        if (onRefreshStateChangeListener != null)
          onRefreshStateChangeListener.onStartRefresh(param1IDataAdapter); 
      } 
    }
    
    public void setOnLoadMoreStateChangeListener(OnLoadMoreStateChangeListener<DATA> param1OnLoadMoreStateChangeListener) {
      this.onLoadMoreStateChangeListener = param1OnLoadMoreStateChangeListener;
    }
    
    public void setOnRefreshStateChangeListener(OnRefreshStateChangeListener<DATA> param1OnRefreshStateChangeListener) {
      this.onRefreshStateChangeListener = param1OnRefreshStateChangeListener;
    }
    
    public void setOnStateChangeListener(OnStateChangeListener<DATA> param1OnStateChangeListener) {
      this.onStateChangeListener = param1OnStateChangeListener;
    }
  }
  
  public static interface OnScrollBottomListener {
    void onScorllBootom();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/mvc/MVCHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
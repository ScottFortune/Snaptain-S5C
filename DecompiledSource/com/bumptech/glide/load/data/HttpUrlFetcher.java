package com.bumptech.glide.load.data;

import android.text.TextUtils;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.HttpException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.ContentLengthInputStream;
import com.bumptech.glide.util.LogTime;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

public class HttpUrlFetcher implements DataFetcher<InputStream> {
  static final HttpUrlConnectionFactory DEFAULT_CONNECTION_FACTORY = new DefaultHttpUrlConnectionFactory();
  
  private static final int INVALID_STATUS_CODE = -1;
  
  private static final int MAXIMUM_REDIRECTS = 5;
  
  private static final String TAG = "HttpUrlFetcher";
  
  private final HttpUrlConnectionFactory connectionFactory;
  
  private final GlideUrl glideUrl;
  
  private volatile boolean isCancelled;
  
  private InputStream stream;
  
  private final int timeout;
  
  private HttpURLConnection urlConnection;
  
  public HttpUrlFetcher(GlideUrl paramGlideUrl, int paramInt) {
    this(paramGlideUrl, paramInt, DEFAULT_CONNECTION_FACTORY);
  }
  
  HttpUrlFetcher(GlideUrl paramGlideUrl, int paramInt, HttpUrlConnectionFactory paramHttpUrlConnectionFactory) {
    this.glideUrl = paramGlideUrl;
    this.timeout = paramInt;
    this.connectionFactory = paramHttpUrlConnectionFactory;
  }
  
  private InputStream getStreamForSuccessfulRequest(HttpURLConnection paramHttpURLConnection) throws IOException {
    if (TextUtils.isEmpty(paramHttpURLConnection.getContentEncoding())) {
      int i = paramHttpURLConnection.getContentLength();
      this.stream = ContentLengthInputStream.obtain(paramHttpURLConnection.getInputStream(), i);
    } else {
      if (Log.isLoggable("HttpUrlFetcher", 3)) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Got non empty content encoding: ");
        stringBuilder.append(paramHttpURLConnection.getContentEncoding());
        Log.d("HttpUrlFetcher", stringBuilder.toString());
      } 
      this.stream = paramHttpURLConnection.getInputStream();
    } 
    return this.stream;
  }
  
  private static boolean isHttpOk(int paramInt) {
    boolean bool;
    if (paramInt / 100 == 2) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static boolean isHttpRedirect(int paramInt) {
    boolean bool;
    if (paramInt / 100 == 3) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private InputStream loadDataWithRedirects(URL paramURL1, int paramInt, URL paramURL2, Map<String, String> paramMap) throws IOException {
    if (paramInt < 5) {
      if (paramURL2 != null)
        try {
          if (paramURL1.toURI().equals(paramURL2.toURI())) {
            HttpException httpException1 = new HttpException();
            this("In re-direct loop");
            throw httpException1;
          } 
        } catch (URISyntaxException uRISyntaxException) {} 
      this.urlConnection = this.connectionFactory.build(paramURL1);
      for (Map.Entry<String, String> entry : paramMap.entrySet())
        this.urlConnection.addRequestProperty((String)entry.getKey(), (String)entry.getValue()); 
      this.urlConnection.setConnectTimeout(this.timeout);
      this.urlConnection.setReadTimeout(this.timeout);
      this.urlConnection.setUseCaches(false);
      this.urlConnection.setDoInput(true);
      this.urlConnection.setInstanceFollowRedirects(false);
      this.urlConnection.connect();
      this.stream = this.urlConnection.getInputStream();
      if (this.isCancelled)
        return null; 
      int i = this.urlConnection.getResponseCode();
      if (isHttpOk(i))
        return getStreamForSuccessfulRequest(this.urlConnection); 
      if (isHttpRedirect(i)) {
        String str = this.urlConnection.getHeaderField("Location");
        if (!TextUtils.isEmpty(str)) {
          URL uRL = new URL(paramURL1, str);
          cleanup();
          return loadDataWithRedirects(uRL, paramInt + 1, paramURL1, paramMap);
        } 
        throw new HttpException("Received empty or null redirect url");
      } 
      if (i == -1)
        throw new HttpException(i); 
      throw new HttpException(this.urlConnection.getResponseMessage(), i);
    } 
    HttpException httpException = new HttpException("Too many (> 5) redirects!");
    throw httpException;
  }
  
  public void cancel() {
    this.isCancelled = true;
  }
  
  public void cleanup() {
    InputStream inputStream = this.stream;
    if (inputStream != null)
      try {
        inputStream.close();
      } catch (IOException iOException) {} 
    HttpURLConnection httpURLConnection = this.urlConnection;
    if (httpURLConnection != null)
      httpURLConnection.disconnect(); 
    this.urlConnection = null;
  }
  
  public Class<InputStream> getDataClass() {
    return InputStream.class;
  }
  
  public DataSource getDataSource() {
    return DataSource.REMOTE;
  }
  
  public void loadData(Priority paramPriority, DataFetcher.DataCallback<? super InputStream> paramDataCallback) {
    StringBuilder stringBuilder;
    long l = LogTime.getLogTime();
    try {
      paramDataCallback.onDataReady(loadDataWithRedirects(this.glideUrl.toURL(), 0, null, this.glideUrl.getHeaders()));
      if (Log.isLoggable("HttpUrlFetcher", 2)) {
        stringBuilder = new StringBuilder();
      } else {
        return;
      } 
    } catch (IOException iOException) {
      if (Log.isLoggable("HttpUrlFetcher", 3))
        Log.d("HttpUrlFetcher", "Failed to load data for url", iOException); 
      paramDataCallback.onLoadFailed(iOException);
      if (Log.isLoggable("HttpUrlFetcher", 2)) {
        stringBuilder = new StringBuilder();
      } else {
        return;
      } 
    } finally {}
    stringBuilder.append("Finished http url fetcher fetch in ");
    stringBuilder.append(LogTime.getElapsedMillis(l));
    Log.v("HttpUrlFetcher", stringBuilder.toString());
  }
  
  private static class DefaultHttpUrlConnectionFactory implements HttpUrlConnectionFactory {
    public HttpURLConnection build(URL param1URL) throws IOException {
      return (HttpURLConnection)param1URL.openConnection();
    }
  }
  
  static interface HttpUrlConnectionFactory {
    HttpURLConnection build(URL param1URL) throws IOException;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/data/HttpUrlFetcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
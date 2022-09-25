package jp.co.cyberagent.android.gpuimage.filter;

import android.graphics.Point;
import android.graphics.PointF;
import android.opengl.GLES20;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class GPUImageToneCurveFilter extends GPUImageFilter {
  public static final String TONE_CURVE_FRAGMENT_SHADER = " varying highp vec2 textureCoordinate;\n uniform sampler2D inputImageTexture;\n uniform sampler2D toneCurveTexture;\n\n void main()\n {\n     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     lowp float redCurveValue = texture2D(toneCurveTexture, vec2(textureColor.r, 0.0)).r;\n     lowp float greenCurveValue = texture2D(toneCurveTexture, vec2(textureColor.g, 0.0)).g;\n     lowp float blueCurveValue = texture2D(toneCurveTexture, vec2(textureColor.b, 0.0)).b;\n\n     gl_FragColor = vec4(redCurveValue, greenCurveValue, blueCurveValue, textureColor.a);\n }";
  
  private PointF[] blueControlPoints;
  
  private ArrayList<Float> blueCurve;
  
  private PointF[] greenControlPoints;
  
  private ArrayList<Float> greenCurve;
  
  private PointF[] redControlPoints;
  
  private ArrayList<Float> redCurve;
  
  private PointF[] rgbCompositeControlPoints;
  
  private ArrayList<Float> rgbCompositeCurve;
  
  private int[] toneCurveTexture = new int[] { -1 };
  
  private int toneCurveTextureUniformLocation;
  
  public GPUImageToneCurveFilter() {
    super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", " varying highp vec2 textureCoordinate;\n uniform sampler2D inputImageTexture;\n uniform sampler2D toneCurveTexture;\n\n void main()\n {\n     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     lowp float redCurveValue = texture2D(toneCurveTexture, vec2(textureColor.r, 0.0)).r;\n     lowp float greenCurveValue = texture2D(toneCurveTexture, vec2(textureColor.g, 0.0)).g;\n     lowp float blueCurveValue = texture2D(toneCurveTexture, vec2(textureColor.b, 0.0)).b;\n\n     gl_FragColor = vec4(redCurveValue, greenCurveValue, blueCurveValue, textureColor.a);\n }");
    PointF[] arrayOfPointF = new PointF[3];
    arrayOfPointF[0] = new PointF(0.0F, 0.0F);
    arrayOfPointF[1] = new PointF(0.5F, 0.5F);
    arrayOfPointF[2] = new PointF(1.0F, 1.0F);
    this.rgbCompositeControlPoints = arrayOfPointF;
    this.redControlPoints = arrayOfPointF;
    this.greenControlPoints = arrayOfPointF;
    this.blueControlPoints = arrayOfPointF;
  }
  
  private ArrayList<Double> createSecondDerivative(Point[] paramArrayOfPoint) {
    int i = paramArrayOfPoint.length;
    if (i <= 1)
      return null; 
    double[][] arrayOfDouble = new double[i][3];
    double[] arrayOfDouble1 = new double[i];
    arrayOfDouble[0][1] = 1.0D;
    arrayOfDouble[0][0] = 0.0D;
    arrayOfDouble[0][2] = 0.0D;
    int j = 1;
    while (true) {
      int k = i - 1;
      if (j < k) {
        Point point1 = paramArrayOfPoint[j - 1];
        Point point2 = paramArrayOfPoint[j];
        k = j + 1;
        Point point3 = paramArrayOfPoint[k];
        double[] arrayOfDouble2 = arrayOfDouble[j];
        double d1 = (point2.x - point1.x);
        Double.isNaN(d1);
        arrayOfDouble2[0] = d1 / 6.0D;
        arrayOfDouble2 = arrayOfDouble[j];
        d1 = (point3.x - point1.x);
        Double.isNaN(d1);
        arrayOfDouble2[1] = d1 / 3.0D;
        arrayOfDouble2 = arrayOfDouble[j];
        d1 = (point3.x - point2.x);
        Double.isNaN(d1);
        arrayOfDouble2[2] = d1 / 6.0D;
        d1 = (point3.y - point2.y);
        double d2 = (point3.x - point2.x);
        Double.isNaN(d1);
        Double.isNaN(d2);
        d1 /= d2;
        d2 = (point2.y - point1.y);
        double d3 = (point2.x - point1.x);
        Double.isNaN(d2);
        Double.isNaN(d3);
        arrayOfDouble1[j] = d1 - d2 / d3;
        j = k;
        continue;
      } 
      arrayOfDouble1[0] = 0.0D;
      arrayOfDouble1[k] = 0.0D;
      arrayOfDouble[k][1] = 1.0D;
      arrayOfDouble[k][0] = 0.0D;
      arrayOfDouble[k][2] = 0.0D;
      for (j = 1; j < i; j++) {
        double d = arrayOfDouble[j][0];
        k = j - 1;
        d /= arrayOfDouble[k][1];
        double[] arrayOfDouble2 = arrayOfDouble[j];
        arrayOfDouble2[1] = arrayOfDouble2[1] - arrayOfDouble[k][2] * d;
        arrayOfDouble[j][0] = 0.0D;
        arrayOfDouble1[j] = arrayOfDouble1[j] - d * arrayOfDouble1[k];
      } 
      for (j = i - 2; j >= 0; j--) {
        double d = arrayOfDouble[j][2];
        k = j + 1;
        d /= arrayOfDouble[k][1];
        double[] arrayOfDouble2 = arrayOfDouble[j];
        arrayOfDouble2[1] = arrayOfDouble2[1] - arrayOfDouble[k][0] * d;
        arrayOfDouble[j][2] = 0.0D;
        arrayOfDouble1[j] = arrayOfDouble1[j] - d * arrayOfDouble1[k];
      } 
      j = 0;
      ArrayList<Double> arrayList = new ArrayList(i);
      while (j < i) {
        arrayList.add(Double.valueOf(arrayOfDouble1[j] / arrayOfDouble[j][1]));
        j++;
      } 
      return arrayList;
    } 
  }
  
  private ArrayList<Float> createSplineCurve(PointF[] paramArrayOfPointF) {
    if (paramArrayOfPointF == null || paramArrayOfPointF.length <= 0)
      return null; 
    PointF[] arrayOfPointF = (PointF[])paramArrayOfPointF.clone();
    Arrays.sort(arrayOfPointF, new Comparator<PointF>() {
          public int compare(PointF param1PointF1, PointF param1PointF2) {
            return (param1PointF1.x < param1PointF2.x) ? -1 : ((param1PointF1.x > param1PointF2.x) ? 1 : 0);
          }
        });
    Point[] arrayOfPoint = new Point[arrayOfPointF.length];
    int i;
    for (i = 0; i < paramArrayOfPointF.length; i++) {
      PointF pointF = arrayOfPointF[i];
      arrayOfPoint[i] = new Point((int)(pointF.x * 255.0F), (int)(pointF.y * 255.0F));
    } 
    ArrayList<Point> arrayList1 = createSplineCurve2(arrayOfPoint);
    Point point = arrayList1.get(0);
    if (point.x > 0)
      for (i = point.x; i >= 0; i--)
        arrayList1.add(0, new Point(i, 0));  
    point = arrayList1.get(arrayList1.size() - 1);
    if (point.x < 255) {
      i = point.x;
      while (++i <= 255)
        arrayList1.add(new Point(i, 255)); 
    } 
    ArrayList<Float> arrayList = new ArrayList(arrayList1.size());
    for (Point point2 : arrayList1) {
      Point point1 = new Point(point2.x, point2.x);
      float f1 = (float)Math.sqrt(Math.pow((point1.x - point2.x), 2.0D) + Math.pow((point1.y - point2.y), 2.0D));
      float f2 = f1;
      if (point1.y > point2.y)
        f2 = -f1; 
      arrayList.add(Float.valueOf(f2));
    } 
    return arrayList;
  }
  
  private ArrayList<Point> createSplineCurve2(Point[] paramArrayOfPoint) {
    ArrayList<Double> arrayList = createSecondDerivative(paramArrayOfPoint);
    int i = arrayList.size();
    if (i < 1)
      return null; 
    double[] arrayOfDouble = new double[i];
    int j = 0;
    int k;
    for (k = 0; k < i; k++)
      arrayOfDouble[k] = ((Double)arrayList.get(k)).doubleValue(); 
    arrayList = new ArrayList<Double>(i + 1);
    k = j;
    while (true) {
      Point[] arrayOfPoint = paramArrayOfPoint;
      if (k < i - 1) {
        Point point2 = arrayOfPoint[k];
        int m = k + 1;
        Point point1 = arrayOfPoint[m];
        for (j = point2.x; j < point1.x; j++) {
          double d1 = (j - point2.x);
          double d2 = (point1.x - point2.x);
          Double.isNaN(d1);
          Double.isNaN(d2);
          double d3 = d1 / d2;
          double d4 = 1.0D - d3;
          d2 = (point1.x - point2.x);
          double d5 = point2.y;
          Double.isNaN(d5);
          d1 = point1.y;
          Double.isNaN(d1);
          Double.isNaN(d2);
          Double.isNaN(d2);
          d1 = d5 * d4 + d1 * d3 + d2 * d2 / 6.0D * ((d4 * d4 * d4 - d4) * arrayOfDouble[k] + (d3 * d3 * d3 - d3) * arrayOfDouble[m]);
          d2 = 0.0D;
          if (d1 > 255.0D) {
            d1 = 255.0D;
          } else if (d1 < 0.0D) {
            d1 = d2;
          } 
          arrayList.add(new Point(j, (int)Math.round(d1)));
        } 
        k = m;
        continue;
      } 
      if (arrayList.size() == 255)
        arrayList.add(paramArrayOfPoint[paramArrayOfPoint.length - 1]); 
      return (ArrayList)arrayList;
    } 
  }
  
  private short readShort(InputStream paramInputStream) throws IOException {
    int i = paramInputStream.read();
    return (short)(paramInputStream.read() | i << 8);
  }
  
  private void updateToneCurveTexture() {
    runOnDraw(new Runnable() {
          public void run() {
            GLES20.glActiveTexture(33987);
            int[] arrayOfInt = GPUImageToneCurveFilter.this.toneCurveTexture;
            byte b = 0;
            GLES20.glBindTexture(3553, arrayOfInt[0]);
            if (GPUImageToneCurveFilter.this.redCurve.size() >= 256 && GPUImageToneCurveFilter.this.greenCurve.size() >= 256 && GPUImageToneCurveFilter.this.blueCurve.size() >= 256 && GPUImageToneCurveFilter.this.rgbCompositeCurve.size() >= 256) {
              byte[] arrayOfByte = new byte[1024];
              while (b < 'Ā') {
                int i = b * 4;
                float f = b;
                arrayOfByte[i + 2] = (byte)(byte)((int)Math.min(Math.max(((Float)GPUImageToneCurveFilter.this.blueCurve.get(b)).floatValue() + f + ((Float)GPUImageToneCurveFilter.this.rgbCompositeCurve.get(b)).floatValue(), 0.0F), 255.0F) & 0xFF);
                arrayOfByte[i + 1] = (byte)(byte)((int)Math.min(Math.max(((Float)GPUImageToneCurveFilter.this.greenCurve.get(b)).floatValue() + f + ((Float)GPUImageToneCurveFilter.this.rgbCompositeCurve.get(b)).floatValue(), 0.0F), 255.0F) & 0xFF);
                arrayOfByte[i] = (byte)(byte)((int)Math.min(Math.max(f + ((Float)GPUImageToneCurveFilter.this.redCurve.get(b)).floatValue() + ((Float)GPUImageToneCurveFilter.this.rgbCompositeCurve.get(b)).floatValue(), 0.0F), 255.0F) & 0xFF);
                arrayOfByte[i + 3] = (byte)-1;
                b++;
              } 
              GLES20.glTexImage2D(3553, 0, 6408, 256, 1, 0, 6408, 5121, ByteBuffer.wrap(arrayOfByte));
            } 
          }
        });
  }
  
  protected void onDrawArraysPre() {
    if (this.toneCurveTexture[0] != -1) {
      GLES20.glActiveTexture(33987);
      GLES20.glBindTexture(3553, this.toneCurveTexture[0]);
      GLES20.glUniform1i(this.toneCurveTextureUniformLocation, 3);
    } 
  }
  
  public void onInit() {
    super.onInit();
    this.toneCurveTextureUniformLocation = GLES20.glGetUniformLocation(getProgram(), "toneCurveTexture");
    GLES20.glActiveTexture(33987);
    GLES20.glGenTextures(1, this.toneCurveTexture, 0);
    GLES20.glBindTexture(3553, this.toneCurveTexture[0]);
    GLES20.glTexParameteri(3553, 10241, 9729);
    GLES20.glTexParameteri(3553, 10240, 9729);
    GLES20.glTexParameteri(3553, 10242, 33071);
    GLES20.glTexParameteri(3553, 10243, 33071);
  }
  
  public void onInitialized() {
    super.onInitialized();
    setRgbCompositeControlPoints(this.rgbCompositeControlPoints);
    setRedControlPoints(this.redControlPoints);
    setGreenControlPoints(this.greenControlPoints);
    setBlueControlPoints(this.blueControlPoints);
  }
  
  public void setBlueControlPoints(PointF[] paramArrayOfPointF) {
    this.blueControlPoints = paramArrayOfPointF;
    this.blueCurve = createSplineCurve(this.blueControlPoints);
    updateToneCurveTexture();
  }
  
  public void setFromCurveFileInputStream(InputStream paramInputStream) {
    try {
      readShort(paramInputStream);
      short s = readShort(paramInputStream);
      ArrayList<PointF[]> arrayList = new ArrayList();
      this(s);
      for (byte b = 0; b < s; b++) {
        short s1 = readShort(paramInputStream);
        PointF[] arrayOfPointF = new PointF[s1];
        for (byte b1 = 0; b1 < s1; b1++) {
          short s2 = readShort(paramInputStream);
          arrayOfPointF[b1] = new PointF(readShort(paramInputStream) * 0.003921569F, s2 * 0.003921569F);
        } 
        arrayList.add(arrayOfPointF);
      } 
      paramInputStream.close();
      this.rgbCompositeControlPoints = arrayList.get(0);
      this.redControlPoints = arrayList.get(1);
      this.greenControlPoints = arrayList.get(2);
      this.blueControlPoints = arrayList.get(3);
    } catch (IOException iOException) {
      iOException.printStackTrace();
    } 
  }
  
  public void setGreenControlPoints(PointF[] paramArrayOfPointF) {
    this.greenControlPoints = paramArrayOfPointF;
    this.greenCurve = createSplineCurve(this.greenControlPoints);
    updateToneCurveTexture();
  }
  
  public void setRedControlPoints(PointF[] paramArrayOfPointF) {
    this.redControlPoints = paramArrayOfPointF;
    this.redCurve = createSplineCurve(this.redControlPoints);
    updateToneCurveTexture();
  }
  
  public void setRgbCompositeControlPoints(PointF[] paramArrayOfPointF) {
    this.rgbCompositeControlPoints = paramArrayOfPointF;
    this.rgbCompositeCurve = createSplineCurve(this.rgbCompositeControlPoints);
    updateToneCurveTexture();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/filter/GPUImageToneCurveFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
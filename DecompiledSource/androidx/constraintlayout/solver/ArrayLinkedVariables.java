package androidx.constraintlayout.solver;

import java.io.PrintStream;
import java.util.Arrays;

public class ArrayLinkedVariables {
  private static final boolean DEBUG = false;
  
  private static final boolean FULL_NEW_CHECK = false;
  
  private static final int NONE = -1;
  
  private int ROW_SIZE = 8;
  
  private SolverVariable candidate = null;
  
  int currentSize = 0;
  
  private int[] mArrayIndices;
  
  private int[] mArrayNextIndices;
  
  private float[] mArrayValues;
  
  private final Cache mCache;
  
  private boolean mDidFillOnce;
  
  private int mHead;
  
  private int mLast;
  
  private final ArrayRow mRow;
  
  ArrayLinkedVariables(ArrayRow paramArrayRow, Cache paramCache) {
    int i = this.ROW_SIZE;
    this.mArrayIndices = new int[i];
    this.mArrayNextIndices = new int[i];
    this.mArrayValues = new float[i];
    this.mHead = -1;
    this.mLast = -1;
    this.mDidFillOnce = false;
    this.mRow = paramArrayRow;
    this.mCache = paramCache;
  }
  
  private boolean isNew(SolverVariable paramSolverVariable, LinearSystem paramLinearSystem) {
    int i = paramSolverVariable.usageInRowCount;
    boolean bool = true;
    if (i > 1)
      bool = false; 
    return bool;
  }
  
  final void add(SolverVariable paramSolverVariable, float paramFloat, boolean paramBoolean) {
    if (paramFloat == 0.0F)
      return; 
    int i = this.mHead;
    if (i == -1) {
      this.mHead = 0;
      float[] arrayOfFloat = this.mArrayValues;
      i = this.mHead;
      arrayOfFloat[i] = paramFloat;
      this.mArrayIndices[i] = paramSolverVariable.id;
      this.mArrayNextIndices[this.mHead] = -1;
      paramSolverVariable.usageInRowCount++;
      paramSolverVariable.addToRow(this.mRow);
      this.currentSize++;
      if (!this.mDidFillOnce) {
        i = ++this.mLast;
        arrayOfInt1 = this.mArrayIndices;
        if (i >= arrayOfInt1.length) {
          this.mDidFillOnce = true;
          this.mLast = arrayOfInt1.length - 1;
        } 
      } 
      return;
    } 
    int j = 0;
    int k = -1;
    while (i != -1 && j < this.currentSize) {
      if (this.mArrayIndices[i] == ((SolverVariable)arrayOfInt1).id) {
        float[] arrayOfFloat = this.mArrayValues;
        arrayOfFloat[i] = arrayOfFloat[i] + paramFloat;
        if (arrayOfFloat[i] == 0.0F) {
          if (i == this.mHead) {
            this.mHead = this.mArrayNextIndices[i];
          } else {
            int[] arrayOfInt = this.mArrayNextIndices;
            arrayOfInt[k] = arrayOfInt[i];
          } 
          if (paramBoolean)
            arrayOfInt1.removeFromRow(this.mRow); 
          if (this.mDidFillOnce)
            this.mLast = i; 
          ((SolverVariable)arrayOfInt1).usageInRowCount--;
          this.currentSize--;
        } 
        return;
      } 
      if (this.mArrayIndices[i] < ((SolverVariable)arrayOfInt1).id)
        k = i; 
      i = this.mArrayNextIndices[i];
      j++;
    } 
    i = this.mLast;
    if (this.mDidFillOnce) {
      int[] arrayOfInt = this.mArrayIndices;
      if (arrayOfInt[i] != -1)
        i = arrayOfInt.length; 
    } else {
      i++;
    } 
    int[] arrayOfInt2 = this.mArrayIndices;
    j = i;
    if (i >= arrayOfInt2.length) {
      j = i;
      if (this.currentSize < arrayOfInt2.length) {
        byte b = 0;
        while (true) {
          arrayOfInt2 = this.mArrayIndices;
          j = i;
          if (b < arrayOfInt2.length) {
            if (arrayOfInt2[b] == -1) {
              j = b;
              break;
            } 
            b++;
            continue;
          } 
          break;
        } 
      } 
    } 
    arrayOfInt2 = this.mArrayIndices;
    i = j;
    if (j >= arrayOfInt2.length) {
      i = arrayOfInt2.length;
      this.ROW_SIZE *= 2;
      this.mDidFillOnce = false;
      this.mLast = i - 1;
      this.mArrayValues = Arrays.copyOf(this.mArrayValues, this.ROW_SIZE);
      this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
      this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
    } 
    this.mArrayIndices[i] = ((SolverVariable)arrayOfInt1).id;
    this.mArrayValues[i] = paramFloat;
    if (k != -1) {
      arrayOfInt2 = this.mArrayNextIndices;
      arrayOfInt2[i] = arrayOfInt2[k];
      arrayOfInt2[k] = i;
    } else {
      this.mArrayNextIndices[i] = this.mHead;
      this.mHead = i;
    } 
    ((SolverVariable)arrayOfInt1).usageInRowCount++;
    arrayOfInt1.addToRow(this.mRow);
    this.currentSize++;
    if (!this.mDidFillOnce)
      this.mLast++; 
    i = this.mLast;
    int[] arrayOfInt1 = this.mArrayIndices;
    if (i >= arrayOfInt1.length) {
      this.mDidFillOnce = true;
      this.mLast = arrayOfInt1.length - 1;
    } 
  }
  
  SolverVariable chooseSubject(LinearSystem paramLinearSystem) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mHead : I
    //   4: istore_2
    //   5: aconst_null
    //   6: astore_3
    //   7: iconst_0
    //   8: istore #4
    //   10: aconst_null
    //   11: astore #5
    //   13: fconst_0
    //   14: fstore #6
    //   16: iconst_0
    //   17: istore #7
    //   19: fconst_0
    //   20: fstore #8
    //   22: iconst_0
    //   23: istore #9
    //   25: iload_2
    //   26: iconst_m1
    //   27: if_icmpeq -> 553
    //   30: iload #4
    //   32: aload_0
    //   33: getfield currentSize : I
    //   36: if_icmpge -> 553
    //   39: aload_0
    //   40: getfield mArrayValues : [F
    //   43: iload_2
    //   44: faload
    //   45: fstore #10
    //   47: aload_0
    //   48: getfield mCache : Landroidx/constraintlayout/solver/Cache;
    //   51: getfield mIndexedVariables : [Landroidx/constraintlayout/solver/SolverVariable;
    //   54: aload_0
    //   55: getfield mArrayIndices : [I
    //   58: iload_2
    //   59: iaload
    //   60: aaload
    //   61: astore #11
    //   63: fload #10
    //   65: fconst_0
    //   66: fcmpg
    //   67: ifge -> 101
    //   70: fload #10
    //   72: fstore #12
    //   74: fload #10
    //   76: ldc -0.001
    //   78: fcmpl
    //   79: ifle -> 132
    //   82: aload_0
    //   83: getfield mArrayValues : [F
    //   86: iload_2
    //   87: fconst_0
    //   88: fastore
    //   89: aload #11
    //   91: aload_0
    //   92: getfield mRow : Landroidx/constraintlayout/solver/ArrayRow;
    //   95: invokevirtual removeFromRow : (Landroidx/constraintlayout/solver/ArrayRow;)V
    //   98: goto -> 129
    //   101: fload #10
    //   103: fstore #12
    //   105: fload #10
    //   107: ldc 0.001
    //   109: fcmpg
    //   110: ifge -> 132
    //   113: aload_0
    //   114: getfield mArrayValues : [F
    //   117: iload_2
    //   118: fconst_0
    //   119: fastore
    //   120: aload #11
    //   122: aload_0
    //   123: getfield mRow : Landroidx/constraintlayout/solver/ArrayRow;
    //   126: invokevirtual removeFromRow : (Landroidx/constraintlayout/solver/ArrayRow;)V
    //   129: fconst_0
    //   130: fstore #12
    //   132: aload_3
    //   133: astore #13
    //   135: aload #5
    //   137: astore #14
    //   139: fload #6
    //   141: fstore #10
    //   143: iload #7
    //   145: istore #15
    //   147: fload #8
    //   149: fstore #16
    //   151: iload #9
    //   153: istore #17
    //   155: fload #12
    //   157: fconst_0
    //   158: fcmpl
    //   159: ifeq -> 517
    //   162: aload #11
    //   164: getfield mType : Landroidx/constraintlayout/solver/SolverVariable$Type;
    //   167: getstatic androidx/constraintlayout/solver/SolverVariable$Type.UNRESTRICTED : Landroidx/constraintlayout/solver/SolverVariable$Type;
    //   170: if_acmpne -> 315
    //   173: aload #5
    //   175: ifnonnull -> 209
    //   178: aload_0
    //   179: aload #11
    //   181: aload_1
    //   182: invokespecial isNew : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/LinearSystem;)Z
    //   185: istore #15
    //   187: aload_3
    //   188: astore #13
    //   190: aload #11
    //   192: astore #14
    //   194: fload #12
    //   196: fstore #10
    //   198: fload #8
    //   200: fstore #16
    //   202: iload #9
    //   204: istore #17
    //   206: goto -> 517
    //   209: fload #6
    //   211: fload #12
    //   213: fcmpl
    //   214: ifle -> 229
    //   217: aload_0
    //   218: aload #11
    //   220: aload_1
    //   221: invokespecial isNew : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/LinearSystem;)Z
    //   224: istore #15
    //   226: goto -> 187
    //   229: aload_3
    //   230: astore #13
    //   232: aload #5
    //   234: astore #14
    //   236: fload #6
    //   238: fstore #10
    //   240: iload #7
    //   242: istore #15
    //   244: fload #8
    //   246: fstore #16
    //   248: iload #9
    //   250: istore #17
    //   252: iload #7
    //   254: ifne -> 517
    //   257: aload_3
    //   258: astore #13
    //   260: aload #5
    //   262: astore #14
    //   264: fload #6
    //   266: fstore #10
    //   268: iload #7
    //   270: istore #15
    //   272: fload #8
    //   274: fstore #16
    //   276: iload #9
    //   278: istore #17
    //   280: aload_0
    //   281: aload #11
    //   283: aload_1
    //   284: invokespecial isNew : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/LinearSystem;)Z
    //   287: ifeq -> 517
    //   290: iconst_1
    //   291: istore #15
    //   293: aload_3
    //   294: astore #13
    //   296: aload #11
    //   298: astore #14
    //   300: fload #12
    //   302: fstore #10
    //   304: fload #8
    //   306: fstore #16
    //   308: iload #9
    //   310: istore #17
    //   312: goto -> 517
    //   315: aload_3
    //   316: astore #13
    //   318: aload #5
    //   320: astore #14
    //   322: fload #6
    //   324: fstore #10
    //   326: iload #7
    //   328: istore #15
    //   330: fload #8
    //   332: fstore #16
    //   334: iload #9
    //   336: istore #17
    //   338: aload #5
    //   340: ifnonnull -> 517
    //   343: aload_3
    //   344: astore #13
    //   346: aload #5
    //   348: astore #14
    //   350: fload #6
    //   352: fstore #10
    //   354: iload #7
    //   356: istore #15
    //   358: fload #8
    //   360: fstore #16
    //   362: iload #9
    //   364: istore #17
    //   366: fload #12
    //   368: fconst_0
    //   369: fcmpg
    //   370: ifge -> 517
    //   373: aload_3
    //   374: ifnonnull -> 413
    //   377: aload_0
    //   378: aload #11
    //   380: aload_1
    //   381: invokespecial isNew : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/LinearSystem;)Z
    //   384: istore #15
    //   386: iload #15
    //   388: istore #17
    //   390: aload #11
    //   392: astore #13
    //   394: aload #5
    //   396: astore #14
    //   398: fload #6
    //   400: fstore #10
    //   402: iload #7
    //   404: istore #15
    //   406: fload #12
    //   408: fstore #16
    //   410: goto -> 517
    //   413: fload #8
    //   415: fload #12
    //   417: fcmpl
    //   418: ifle -> 433
    //   421: aload_0
    //   422: aload #11
    //   424: aload_1
    //   425: invokespecial isNew : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/LinearSystem;)Z
    //   428: istore #15
    //   430: goto -> 386
    //   433: aload_3
    //   434: astore #13
    //   436: aload #5
    //   438: astore #14
    //   440: fload #6
    //   442: fstore #10
    //   444: iload #7
    //   446: istore #15
    //   448: fload #8
    //   450: fstore #16
    //   452: iload #9
    //   454: istore #17
    //   456: iload #9
    //   458: ifne -> 517
    //   461: aload_3
    //   462: astore #13
    //   464: aload #5
    //   466: astore #14
    //   468: fload #6
    //   470: fstore #10
    //   472: iload #7
    //   474: istore #15
    //   476: fload #8
    //   478: fstore #16
    //   480: iload #9
    //   482: istore #17
    //   484: aload_0
    //   485: aload #11
    //   487: aload_1
    //   488: invokespecial isNew : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/LinearSystem;)Z
    //   491: ifeq -> 517
    //   494: iconst_1
    //   495: istore #17
    //   497: fload #12
    //   499: fstore #16
    //   501: iload #7
    //   503: istore #15
    //   505: fload #6
    //   507: fstore #10
    //   509: aload #5
    //   511: astore #14
    //   513: aload #11
    //   515: astore #13
    //   517: aload_0
    //   518: getfield mArrayNextIndices : [I
    //   521: iload_2
    //   522: iaload
    //   523: istore_2
    //   524: iinc #4, 1
    //   527: aload #13
    //   529: astore_3
    //   530: aload #14
    //   532: astore #5
    //   534: fload #10
    //   536: fstore #6
    //   538: iload #15
    //   540: istore #7
    //   542: fload #16
    //   544: fstore #8
    //   546: iload #17
    //   548: istore #9
    //   550: goto -> 25
    //   553: aload #5
    //   555: ifnull -> 561
    //   558: aload #5
    //   560: areturn
    //   561: aload_3
    //   562: areturn
  }
  
  public final void clear() {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      SolverVariable solverVariable = this.mCache.mIndexedVariables[this.mArrayIndices[i]];
      if (solverVariable != null)
        solverVariable.removeFromRow(this.mRow); 
      i = this.mArrayNextIndices[i];
    } 
    this.mHead = -1;
    this.mLast = -1;
    this.mDidFillOnce = false;
    this.currentSize = 0;
  }
  
  final boolean containsKey(SolverVariable paramSolverVariable) {
    int i = this.mHead;
    if (i == -1)
      return false; 
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (this.mArrayIndices[i] == paramSolverVariable.id)
        return true; 
      i = this.mArrayNextIndices[i];
    } 
    return false;
  }
  
  public void display() {
    int i = this.currentSize;
    System.out.print("{ ");
    for (byte b = 0; b < i; b++) {
      SolverVariable solverVariable = getVariable(b);
      if (solverVariable != null) {
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(solverVariable);
        stringBuilder.append(" = ");
        stringBuilder.append(getVariableValue(b));
        stringBuilder.append(" ");
        printStream.print(stringBuilder.toString());
      } 
    } 
    System.out.println(" }");
  }
  
  void divideByAmount(float paramFloat) {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      float[] arrayOfFloat = this.mArrayValues;
      arrayOfFloat[i] = arrayOfFloat[i] / paramFloat;
      i = this.mArrayNextIndices[i];
    } 
  }
  
  public final float get(SolverVariable paramSolverVariable) {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (this.mArrayIndices[i] == paramSolverVariable.id)
        return this.mArrayValues[i]; 
      i = this.mArrayNextIndices[i];
    } 
    return 0.0F;
  }
  
  SolverVariable getPivotCandidate() {
    // Byte code:
    //   0: aload_0
    //   1: getfield candidate : Landroidx/constraintlayout/solver/SolverVariable;
    //   4: astore_1
    //   5: aload_1
    //   6: ifnonnull -> 105
    //   9: aload_0
    //   10: getfield mHead : I
    //   13: istore_2
    //   14: iconst_0
    //   15: istore_3
    //   16: aconst_null
    //   17: astore #4
    //   19: iload_2
    //   20: iconst_m1
    //   21: if_icmpeq -> 102
    //   24: iload_3
    //   25: aload_0
    //   26: getfield currentSize : I
    //   29: if_icmpge -> 102
    //   32: aload #4
    //   34: astore_1
    //   35: aload_0
    //   36: getfield mArrayValues : [F
    //   39: iload_2
    //   40: faload
    //   41: fconst_0
    //   42: fcmpg
    //   43: ifge -> 86
    //   46: aload_0
    //   47: getfield mCache : Landroidx/constraintlayout/solver/Cache;
    //   50: getfield mIndexedVariables : [Landroidx/constraintlayout/solver/SolverVariable;
    //   53: aload_0
    //   54: getfield mArrayIndices : [I
    //   57: iload_2
    //   58: iaload
    //   59: aaload
    //   60: astore #5
    //   62: aload #4
    //   64: ifnull -> 83
    //   67: aload #4
    //   69: astore_1
    //   70: aload #4
    //   72: getfield strength : I
    //   75: aload #5
    //   77: getfield strength : I
    //   80: if_icmpge -> 86
    //   83: aload #5
    //   85: astore_1
    //   86: aload_0
    //   87: getfield mArrayNextIndices : [I
    //   90: iload_2
    //   91: iaload
    //   92: istore_2
    //   93: iinc #3, 1
    //   96: aload_1
    //   97: astore #4
    //   99: goto -> 19
    //   102: aload #4
    //   104: areturn
    //   105: aload_1
    //   106: areturn
  }
  
  SolverVariable getPivotCandidate(boolean[] paramArrayOfboolean, SolverVariable paramSolverVariable) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mHead : I
    //   4: istore_3
    //   5: iconst_0
    //   6: istore #4
    //   8: aconst_null
    //   9: astore #5
    //   11: fconst_0
    //   12: fstore #6
    //   14: iload_3
    //   15: iconst_m1
    //   16: if_icmpeq -> 182
    //   19: iload #4
    //   21: aload_0
    //   22: getfield currentSize : I
    //   25: if_icmpge -> 182
    //   28: aload #5
    //   30: astore #7
    //   32: fload #6
    //   34: fstore #8
    //   36: aload_0
    //   37: getfield mArrayValues : [F
    //   40: iload_3
    //   41: faload
    //   42: fconst_0
    //   43: fcmpg
    //   44: ifge -> 161
    //   47: aload_0
    //   48: getfield mCache : Landroidx/constraintlayout/solver/Cache;
    //   51: getfield mIndexedVariables : [Landroidx/constraintlayout/solver/SolverVariable;
    //   54: aload_0
    //   55: getfield mArrayIndices : [I
    //   58: iload_3
    //   59: iaload
    //   60: aaload
    //   61: astore #9
    //   63: aload_1
    //   64: ifnull -> 85
    //   67: aload #5
    //   69: astore #7
    //   71: fload #6
    //   73: fstore #8
    //   75: aload_1
    //   76: aload #9
    //   78: getfield id : I
    //   81: baload
    //   82: ifne -> 161
    //   85: aload #5
    //   87: astore #7
    //   89: fload #6
    //   91: fstore #8
    //   93: aload #9
    //   95: aload_2
    //   96: if_acmpeq -> 161
    //   99: aload #9
    //   101: getfield mType : Landroidx/constraintlayout/solver/SolverVariable$Type;
    //   104: getstatic androidx/constraintlayout/solver/SolverVariable$Type.SLACK : Landroidx/constraintlayout/solver/SolverVariable$Type;
    //   107: if_acmpeq -> 129
    //   110: aload #5
    //   112: astore #7
    //   114: fload #6
    //   116: fstore #8
    //   118: aload #9
    //   120: getfield mType : Landroidx/constraintlayout/solver/SolverVariable$Type;
    //   123: getstatic androidx/constraintlayout/solver/SolverVariable$Type.ERROR : Landroidx/constraintlayout/solver/SolverVariable$Type;
    //   126: if_acmpne -> 161
    //   129: aload_0
    //   130: getfield mArrayValues : [F
    //   133: iload_3
    //   134: faload
    //   135: fstore #10
    //   137: aload #5
    //   139: astore #7
    //   141: fload #6
    //   143: fstore #8
    //   145: fload #10
    //   147: fload #6
    //   149: fcmpg
    //   150: ifge -> 161
    //   153: aload #9
    //   155: astore #7
    //   157: fload #10
    //   159: fstore #8
    //   161: aload_0
    //   162: getfield mArrayNextIndices : [I
    //   165: iload_3
    //   166: iaload
    //   167: istore_3
    //   168: iinc #4, 1
    //   171: aload #7
    //   173: astore #5
    //   175: fload #8
    //   177: fstore #6
    //   179: goto -> 14
    //   182: aload #5
    //   184: areturn
  }
  
  final SolverVariable getVariable(int paramInt) {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (b == paramInt)
        return this.mCache.mIndexedVariables[this.mArrayIndices[i]]; 
      i = this.mArrayNextIndices[i];
    } 
    return null;
  }
  
  final float getVariableValue(int paramInt) {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (b == paramInt)
        return this.mArrayValues[i]; 
      i = this.mArrayNextIndices[i];
    } 
    return 0.0F;
  }
  
  boolean hasAtLeastOnePositiveVariable() {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (this.mArrayValues[i] > 0.0F)
        return true; 
      i = this.mArrayNextIndices[i];
    } 
    return false;
  }
  
  void invert() {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      float[] arrayOfFloat = this.mArrayValues;
      arrayOfFloat[i] = arrayOfFloat[i] * -1.0F;
      i = this.mArrayNextIndices[i];
    } 
  }
  
  public final void put(SolverVariable paramSolverVariable, float paramFloat) {
    if (paramFloat == 0.0F) {
      remove(paramSolverVariable, true);
      return;
    } 
    int i = this.mHead;
    if (i == -1) {
      this.mHead = 0;
      float[] arrayOfFloat = this.mArrayValues;
      i = this.mHead;
      arrayOfFloat[i] = paramFloat;
      this.mArrayIndices[i] = paramSolverVariable.id;
      this.mArrayNextIndices[this.mHead] = -1;
      paramSolverVariable.usageInRowCount++;
      paramSolverVariable.addToRow(this.mRow);
      this.currentSize++;
      if (!this.mDidFillOnce) {
        i = ++this.mLast;
        arrayOfInt1 = this.mArrayIndices;
        if (i >= arrayOfInt1.length) {
          this.mDidFillOnce = true;
          this.mLast = arrayOfInt1.length - 1;
        } 
      } 
      return;
    } 
    int j = 0;
    int k = -1;
    while (i != -1 && j < this.currentSize) {
      if (this.mArrayIndices[i] == ((SolverVariable)arrayOfInt1).id) {
        this.mArrayValues[i] = paramFloat;
        return;
      } 
      if (this.mArrayIndices[i] < ((SolverVariable)arrayOfInt1).id)
        k = i; 
      i = this.mArrayNextIndices[i];
      j++;
    } 
    i = this.mLast;
    if (this.mDidFillOnce) {
      int[] arrayOfInt = this.mArrayIndices;
      if (arrayOfInt[i] != -1)
        i = arrayOfInt.length; 
    } else {
      i++;
    } 
    int[] arrayOfInt2 = this.mArrayIndices;
    j = i;
    if (i >= arrayOfInt2.length) {
      j = i;
      if (this.currentSize < arrayOfInt2.length) {
        byte b = 0;
        while (true) {
          arrayOfInt2 = this.mArrayIndices;
          j = i;
          if (b < arrayOfInt2.length) {
            if (arrayOfInt2[b] == -1) {
              j = b;
              break;
            } 
            b++;
            continue;
          } 
          break;
        } 
      } 
    } 
    arrayOfInt2 = this.mArrayIndices;
    i = j;
    if (j >= arrayOfInt2.length) {
      i = arrayOfInt2.length;
      this.ROW_SIZE *= 2;
      this.mDidFillOnce = false;
      this.mLast = i - 1;
      this.mArrayValues = Arrays.copyOf(this.mArrayValues, this.ROW_SIZE);
      this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
      this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
    } 
    this.mArrayIndices[i] = ((SolverVariable)arrayOfInt1).id;
    this.mArrayValues[i] = paramFloat;
    if (k != -1) {
      arrayOfInt2 = this.mArrayNextIndices;
      arrayOfInt2[i] = arrayOfInt2[k];
      arrayOfInt2[k] = i;
    } else {
      this.mArrayNextIndices[i] = this.mHead;
      this.mHead = i;
    } 
    ((SolverVariable)arrayOfInt1).usageInRowCount++;
    arrayOfInt1.addToRow(this.mRow);
    this.currentSize++;
    if (!this.mDidFillOnce)
      this.mLast++; 
    if (this.currentSize >= this.mArrayIndices.length)
      this.mDidFillOnce = true; 
    i = this.mLast;
    int[] arrayOfInt1 = this.mArrayIndices;
    if (i >= arrayOfInt1.length) {
      this.mDidFillOnce = true;
      this.mLast = arrayOfInt1.length - 1;
    } 
  }
  
  public final float remove(SolverVariable paramSolverVariable, boolean paramBoolean) {
    if (this.candidate == paramSolverVariable)
      this.candidate = null; 
    int i = this.mHead;
    if (i == -1)
      return 0.0F; 
    byte b = 0;
    int j = -1;
    while (i != -1 && b < this.currentSize) {
      if (this.mArrayIndices[i] == paramSolverVariable.id) {
        if (i == this.mHead) {
          this.mHead = this.mArrayNextIndices[i];
        } else {
          int[] arrayOfInt = this.mArrayNextIndices;
          arrayOfInt[j] = arrayOfInt[i];
        } 
        if (paramBoolean)
          paramSolverVariable.removeFromRow(this.mRow); 
        paramSolverVariable.usageInRowCount--;
        this.currentSize--;
        this.mArrayIndices[i] = -1;
        if (this.mDidFillOnce)
          this.mLast = i; 
        return this.mArrayValues[i];
      } 
      int k = this.mArrayNextIndices[i];
      b++;
      j = i;
      i = k;
    } 
    return 0.0F;
  }
  
  int sizeInBytes() {
    return this.mArrayIndices.length * 4 * 3 + 0 + 36;
  }
  
  public String toString() {
    int i = this.mHead;
    String str = "";
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str);
      stringBuilder.append(" -> ");
      str = stringBuilder.toString();
      stringBuilder = new StringBuilder();
      stringBuilder.append(str);
      stringBuilder.append(this.mArrayValues[i]);
      stringBuilder.append(" : ");
      str = stringBuilder.toString();
      stringBuilder = new StringBuilder();
      stringBuilder.append(str);
      stringBuilder.append(this.mCache.mIndexedVariables[this.mArrayIndices[i]]);
      str = stringBuilder.toString();
      i = this.mArrayNextIndices[i];
    } 
    return str;
  }
  
  final void updateFromRow(ArrayRow paramArrayRow1, ArrayRow paramArrayRow2, boolean paramBoolean) {
    int i = this.mHead;
    label22: while (true) {
      for (int j = 0; i != -1 && j < this.currentSize; j++) {
        if (this.mArrayIndices[i] == paramArrayRow2.variable.id) {
          float f = this.mArrayValues[i];
          remove(paramArrayRow2.variable, paramBoolean);
          ArrayLinkedVariables arrayLinkedVariables = paramArrayRow2.variables;
          j = arrayLinkedVariables.mHead;
          for (i = 0; j != -1 && i < arrayLinkedVariables.currentSize; i++) {
            add(this.mCache.mIndexedVariables[arrayLinkedVariables.mArrayIndices[j]], arrayLinkedVariables.mArrayValues[j] * f, paramBoolean);
            j = arrayLinkedVariables.mArrayNextIndices[j];
          } 
          paramArrayRow1.constantValue += paramArrayRow2.constantValue * f;
          if (paramBoolean)
            paramArrayRow2.variable.removeFromRow(paramArrayRow1); 
          i = this.mHead;
          continue label22;
        } 
        i = this.mArrayNextIndices[i];
      } 
      break;
    } 
  }
  
  void updateFromSystem(ArrayRow paramArrayRow, ArrayRow[] paramArrayOfArrayRow) {
    int i = this.mHead;
    label22: while (true) {
      for (int j = 0; i != -1 && j < this.currentSize; j++) {
        SolverVariable solverVariable = this.mCache.mIndexedVariables[this.mArrayIndices[i]];
        if (solverVariable.definitionId != -1) {
          float f = this.mArrayValues[i];
          remove(solverVariable, true);
          ArrayRow arrayRow = paramArrayOfArrayRow[solverVariable.definitionId];
          if (!arrayRow.isSimpleDefinition) {
            ArrayLinkedVariables arrayLinkedVariables = arrayRow.variables;
            j = arrayLinkedVariables.mHead;
            for (i = 0; j != -1 && i < arrayLinkedVariables.currentSize; i++) {
              add(this.mCache.mIndexedVariables[arrayLinkedVariables.mArrayIndices[j]], arrayLinkedVariables.mArrayValues[j] * f, true);
              j = arrayLinkedVariables.mArrayNextIndices[j];
            } 
          } 
          paramArrayRow.constantValue += arrayRow.constantValue * f;
          arrayRow.variable.removeFromRow(paramArrayRow);
          i = this.mHead;
          continue label22;
        } 
        i = this.mArrayNextIndices[i];
      } 
      break;
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/constraintlayout/solver/ArrayLinkedVariables.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
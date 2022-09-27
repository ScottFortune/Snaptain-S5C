package androidx.constraintlayout.solver;

public class ArrayRow implements LinearSystem.Row {
  private static final boolean DEBUG = false;
  
  private static final float epsilon = 0.001F;
  
  float constantValue = 0.0F;
  
  boolean isSimpleDefinition = false;
  
  boolean used = false;
  
  SolverVariable variable = null;
  
  public final ArrayLinkedVariables variables;
  
  public ArrayRow(Cache paramCache) {
    this.variables = new ArrayLinkedVariables(this, paramCache);
  }
  
  public ArrayRow addError(LinearSystem paramLinearSystem, int paramInt) {
    this.variables.put(paramLinearSystem.createErrorVariable(paramInt, "ep"), 1.0F);
    this.variables.put(paramLinearSystem.createErrorVariable(paramInt, "em"), -1.0F);
    return this;
  }
  
  public void addError(SolverVariable paramSolverVariable) {
    int i = paramSolverVariable.strength;
    float f = 1.0F;
    if (i != 1)
      if (paramSolverVariable.strength == 2) {
        f = 1000.0F;
      } else if (paramSolverVariable.strength == 3) {
        f = 1000000.0F;
      } else if (paramSolverVariable.strength == 4) {
        f = 1.0E9F;
      } else if (paramSolverVariable.strength == 5) {
        f = 1.0E12F;
      }  
    this.variables.put(paramSolverVariable, f);
  }
  
  ArrayRow addSingleError(SolverVariable paramSolverVariable, int paramInt) {
    this.variables.put(paramSolverVariable, paramInt);
    return this;
  }
  
  boolean chooseSubject(LinearSystem paramLinearSystem) {
    boolean bool;
    SolverVariable solverVariable = this.variables.chooseSubject(paramLinearSystem);
    if (solverVariable == null) {
      bool = true;
    } else {
      pivot(solverVariable);
      bool = false;
    } 
    if (this.variables.currentSize == 0)
      this.isSimpleDefinition = true; 
    return bool;
  }
  
  public void clear() {
    this.variables.clear();
    this.variable = null;
    this.constantValue = 0.0F;
  }
  
  ArrayRow createRowCentering(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt1, float paramFloat, SolverVariable paramSolverVariable3, SolverVariable paramSolverVariable4, int paramInt2) {
    if (paramSolverVariable2 == paramSolverVariable3) {
      this.variables.put(paramSolverVariable1, 1.0F);
      this.variables.put(paramSolverVariable4, 1.0F);
      this.variables.put(paramSolverVariable2, -2.0F);
      return this;
    } 
    if (paramFloat == 0.5F) {
      this.variables.put(paramSolverVariable1, 1.0F);
      this.variables.put(paramSolverVariable2, -1.0F);
      this.variables.put(paramSolverVariable3, -1.0F);
      this.variables.put(paramSolverVariable4, 1.0F);
      if (paramInt1 > 0 || paramInt2 > 0)
        this.constantValue = (-paramInt1 + paramInt2); 
    } else if (paramFloat <= 0.0F) {
      this.variables.put(paramSolverVariable1, -1.0F);
      this.variables.put(paramSolverVariable2, 1.0F);
      this.constantValue = paramInt1;
    } else if (paramFloat >= 1.0F) {
      this.variables.put(paramSolverVariable3, -1.0F);
      this.variables.put(paramSolverVariable4, 1.0F);
      this.constantValue = paramInt2;
    } else {
      ArrayLinkedVariables arrayLinkedVariables = this.variables;
      float f = 1.0F - paramFloat;
      arrayLinkedVariables.put(paramSolverVariable1, f * 1.0F);
      this.variables.put(paramSolverVariable2, f * -1.0F);
      this.variables.put(paramSolverVariable3, -1.0F * paramFloat);
      this.variables.put(paramSolverVariable4, 1.0F * paramFloat);
      if (paramInt1 > 0 || paramInt2 > 0)
        this.constantValue = -paramInt1 * f + paramInt2 * paramFloat; 
    } 
    return this;
  }
  
  ArrayRow createRowDefinition(SolverVariable paramSolverVariable, int paramInt) {
    this.variable = paramSolverVariable;
    float f = paramInt;
    paramSolverVariable.computedValue = f;
    this.constantValue = f;
    this.isSimpleDefinition = true;
    return this;
  }
  
  ArrayRow createRowDimensionPercent(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, SolverVariable paramSolverVariable3, float paramFloat) {
    this.variables.put(paramSolverVariable1, -1.0F);
    this.variables.put(paramSolverVariable2, 1.0F - paramFloat);
    this.variables.put(paramSolverVariable3, paramFloat);
    return this;
  }
  
  public ArrayRow createRowDimensionRatio(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, SolverVariable paramSolverVariable3, SolverVariable paramSolverVariable4, float paramFloat) {
    this.variables.put(paramSolverVariable1, -1.0F);
    this.variables.put(paramSolverVariable2, 1.0F);
    this.variables.put(paramSolverVariable3, paramFloat);
    this.variables.put(paramSolverVariable4, -paramFloat);
    return this;
  }
  
  public ArrayRow createRowEqualDimension(float paramFloat1, float paramFloat2, float paramFloat3, SolverVariable paramSolverVariable1, int paramInt1, SolverVariable paramSolverVariable2, int paramInt2, SolverVariable paramSolverVariable3, int paramInt3, SolverVariable paramSolverVariable4, int paramInt4) {
    if (paramFloat2 == 0.0F || paramFloat1 == paramFloat3) {
      this.constantValue = (-paramInt1 - paramInt2 + paramInt3 + paramInt4);
      this.variables.put(paramSolverVariable1, 1.0F);
      this.variables.put(paramSolverVariable2, -1.0F);
      this.variables.put(paramSolverVariable4, 1.0F);
      this.variables.put(paramSolverVariable3, -1.0F);
      return this;
    } 
    paramFloat1 = paramFloat1 / paramFloat2 / paramFloat3 / paramFloat2;
    this.constantValue = (-paramInt1 - paramInt2) + paramInt3 * paramFloat1 + paramInt4 * paramFloat1;
    this.variables.put(paramSolverVariable1, 1.0F);
    this.variables.put(paramSolverVariable2, -1.0F);
    this.variables.put(paramSolverVariable4, paramFloat1);
    this.variables.put(paramSolverVariable3, -paramFloat1);
    return this;
  }
  
  public ArrayRow createRowEqualMatchDimensions(float paramFloat1, float paramFloat2, float paramFloat3, SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, SolverVariable paramSolverVariable3, SolverVariable paramSolverVariable4) {
    this.constantValue = 0.0F;
    if (paramFloat2 == 0.0F || paramFloat1 == paramFloat3) {
      this.variables.put(paramSolverVariable1, 1.0F);
      this.variables.put(paramSolverVariable2, -1.0F);
      this.variables.put(paramSolverVariable4, 1.0F);
      this.variables.put(paramSolverVariable3, -1.0F);
      return this;
    } 
    if (paramFloat1 == 0.0F) {
      this.variables.put(paramSolverVariable1, 1.0F);
      this.variables.put(paramSolverVariable2, -1.0F);
    } else if (paramFloat3 == 0.0F) {
      this.variables.put(paramSolverVariable3, 1.0F);
      this.variables.put(paramSolverVariable4, -1.0F);
    } else {
      paramFloat1 = paramFloat1 / paramFloat2 / paramFloat3 / paramFloat2;
      this.variables.put(paramSolverVariable1, 1.0F);
      this.variables.put(paramSolverVariable2, -1.0F);
      this.variables.put(paramSolverVariable4, paramFloat1);
      this.variables.put(paramSolverVariable3, -paramFloat1);
    } 
    return this;
  }
  
  public ArrayRow createRowEquals(SolverVariable paramSolverVariable, int paramInt) {
    if (paramInt < 0) {
      this.constantValue = (paramInt * -1);
      this.variables.put(paramSolverVariable, 1.0F);
    } else {
      this.constantValue = paramInt;
      this.variables.put(paramSolverVariable, -1.0F);
    } 
    return this;
  }
  
  public ArrayRow createRowEquals(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt) {
    int i = 0;
    int j = 0;
    if (paramInt != 0) {
      i = j;
      j = paramInt;
      if (paramInt < 0) {
        j = paramInt * -1;
        i = 1;
      } 
      this.constantValue = j;
    } 
    if (i == 0) {
      this.variables.put(paramSolverVariable1, -1.0F);
      this.variables.put(paramSolverVariable2, 1.0F);
    } else {
      this.variables.put(paramSolverVariable1, 1.0F);
      this.variables.put(paramSolverVariable2, -1.0F);
    } 
    return this;
  }
  
  public ArrayRow createRowGreaterThan(SolverVariable paramSolverVariable1, int paramInt, SolverVariable paramSolverVariable2) {
    this.constantValue = paramInt;
    this.variables.put(paramSolverVariable1, -1.0F);
    return this;
  }
  
  public ArrayRow createRowGreaterThan(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, SolverVariable paramSolverVariable3, int paramInt) {
    int i = 0;
    int j = 0;
    if (paramInt != 0) {
      i = j;
      j = paramInt;
      if (paramInt < 0) {
        j = paramInt * -1;
        i = 1;
      } 
      this.constantValue = j;
    } 
    if (i == 0) {
      this.variables.put(paramSolverVariable1, -1.0F);
      this.variables.put(paramSolverVariable2, 1.0F);
      this.variables.put(paramSolverVariable3, 1.0F);
    } else {
      this.variables.put(paramSolverVariable1, 1.0F);
      this.variables.put(paramSolverVariable2, -1.0F);
      this.variables.put(paramSolverVariable3, -1.0F);
    } 
    return this;
  }
  
  public ArrayRow createRowLowerThan(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, SolverVariable paramSolverVariable3, int paramInt) {
    int i = 0;
    int j = 0;
    if (paramInt != 0) {
      i = j;
      j = paramInt;
      if (paramInt < 0) {
        j = paramInt * -1;
        i = 1;
      } 
      this.constantValue = j;
    } 
    if (i == 0) {
      this.variables.put(paramSolverVariable1, -1.0F);
      this.variables.put(paramSolverVariable2, 1.0F);
      this.variables.put(paramSolverVariable3, -1.0F);
    } else {
      this.variables.put(paramSolverVariable1, 1.0F);
      this.variables.put(paramSolverVariable2, -1.0F);
      this.variables.put(paramSolverVariable3, 1.0F);
    } 
    return this;
  }
  
  public ArrayRow createRowWithAngle(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, SolverVariable paramSolverVariable3, SolverVariable paramSolverVariable4, float paramFloat) {
    this.variables.put(paramSolverVariable3, 0.5F);
    this.variables.put(paramSolverVariable4, 0.5F);
    this.variables.put(paramSolverVariable1, -0.5F);
    this.variables.put(paramSolverVariable2, -0.5F);
    this.constantValue = -paramFloat;
    return this;
  }
  
  void ensurePositiveConstant() {
    float f = this.constantValue;
    if (f < 0.0F) {
      this.constantValue = f * -1.0F;
      this.variables.invert();
    } 
  }
  
  public SolverVariable getKey() {
    return this.variable;
  }
  
  public SolverVariable getPivotCandidate(LinearSystem paramLinearSystem, boolean[] paramArrayOfboolean) {
    return this.variables.getPivotCandidate(paramArrayOfboolean, null);
  }
  
  boolean hasKeyVariable() {
    boolean bool;
    SolverVariable solverVariable = this.variable;
    if (solverVariable != null && (solverVariable.mType == SolverVariable.Type.UNRESTRICTED || this.constantValue >= 0.0F)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  boolean hasVariable(SolverVariable paramSolverVariable) {
    return this.variables.containsKey(paramSolverVariable);
  }
  
  public void initFromRow(LinearSystem.Row paramRow) {
    if (paramRow instanceof ArrayRow) {
      ArrayRow arrayRow = (ArrayRow)paramRow;
      this.variable = null;
      this.variables.clear();
      for (byte b = 0; b < arrayRow.variables.currentSize; b++) {
        SolverVariable solverVariable = arrayRow.variables.getVariable(b);
        float f = arrayRow.variables.getVariableValue(b);
        this.variables.add(solverVariable, f, true);
      } 
    } 
  }
  
  public boolean isEmpty() {
    boolean bool;
    if (this.variable == null && this.constantValue == 0.0F && this.variables.currentSize == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  SolverVariable pickPivot(SolverVariable paramSolverVariable) {
    return this.variables.getPivotCandidate(null, paramSolverVariable);
  }
  
  void pivot(SolverVariable paramSolverVariable) {
    SolverVariable solverVariable = this.variable;
    if (solverVariable != null) {
      this.variables.put(solverVariable, -1.0F);
      this.variable = null;
    } 
    float f = this.variables.remove(paramSolverVariable, true) * -1.0F;
    this.variable = paramSolverVariable;
    if (f == 1.0F)
      return; 
    this.constantValue /= f;
    this.variables.divideByAmount(f);
  }
  
  public void reset() {
    this.variable = null;
    this.variables.clear();
    this.constantValue = 0.0F;
    this.isSimpleDefinition = false;
  }
  
  int sizeInBytes() {
    byte b;
    if (this.variable != null) {
      b = 4;
    } else {
      b = 0;
    } 
    return b + 4 + 4 + this.variables.sizeInBytes();
  }
  
  String toReadableString() {
    // Byte code:
    //   0: aload_0
    //   1: getfield variable : Landroidx/constraintlayout/solver/SolverVariable;
    //   4: ifnonnull -> 37
    //   7: new java/lang/StringBuilder
    //   10: dup
    //   11: invokespecial <init> : ()V
    //   14: astore_1
    //   15: aload_1
    //   16: ldc ''
    //   18: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   21: pop
    //   22: aload_1
    //   23: ldc '0'
    //   25: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   28: pop
    //   29: aload_1
    //   30: invokevirtual toString : ()Ljava/lang/String;
    //   33: astore_1
    //   34: goto -> 66
    //   37: new java/lang/StringBuilder
    //   40: dup
    //   41: invokespecial <init> : ()V
    //   44: astore_1
    //   45: aload_1
    //   46: ldc ''
    //   48: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   51: pop
    //   52: aload_1
    //   53: aload_0
    //   54: getfield variable : Landroidx/constraintlayout/solver/SolverVariable;
    //   57: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   60: pop
    //   61: aload_1
    //   62: invokevirtual toString : ()Ljava/lang/String;
    //   65: astore_1
    //   66: new java/lang/StringBuilder
    //   69: dup
    //   70: invokespecial <init> : ()V
    //   73: astore_2
    //   74: aload_2
    //   75: aload_1
    //   76: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   79: pop
    //   80: aload_2
    //   81: ldc ' = '
    //   83: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   86: pop
    //   87: aload_2
    //   88: invokevirtual toString : ()Ljava/lang/String;
    //   91: astore_1
    //   92: aload_0
    //   93: getfield constantValue : F
    //   96: fstore_3
    //   97: iconst_0
    //   98: istore #4
    //   100: fload_3
    //   101: fconst_0
    //   102: fcmpl
    //   103: ifeq -> 140
    //   106: new java/lang/StringBuilder
    //   109: dup
    //   110: invokespecial <init> : ()V
    //   113: astore_2
    //   114: aload_2
    //   115: aload_1
    //   116: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   119: pop
    //   120: aload_2
    //   121: aload_0
    //   122: getfield constantValue : F
    //   125: invokevirtual append : (F)Ljava/lang/StringBuilder;
    //   128: pop
    //   129: aload_2
    //   130: invokevirtual toString : ()Ljava/lang/String;
    //   133: astore_1
    //   134: iconst_1
    //   135: istore #5
    //   137: goto -> 143
    //   140: iconst_0
    //   141: istore #5
    //   143: aload_0
    //   144: getfield variables : Landroidx/constraintlayout/solver/ArrayLinkedVariables;
    //   147: getfield currentSize : I
    //   150: istore #6
    //   152: iload #4
    //   154: iload #6
    //   156: if_icmpge -> 403
    //   159: aload_0
    //   160: getfield variables : Landroidx/constraintlayout/solver/ArrayLinkedVariables;
    //   163: iload #4
    //   165: invokevirtual getVariable : (I)Landroidx/constraintlayout/solver/SolverVariable;
    //   168: astore_2
    //   169: aload_2
    //   170: ifnonnull -> 176
    //   173: goto -> 397
    //   176: aload_0
    //   177: getfield variables : Landroidx/constraintlayout/solver/ArrayLinkedVariables;
    //   180: iload #4
    //   182: invokevirtual getVariableValue : (I)F
    //   185: fstore #7
    //   187: fload #7
    //   189: fconst_0
    //   190: fcmpl
    //   191: ifne -> 197
    //   194: goto -> 397
    //   197: aload_2
    //   198: invokevirtual toString : ()Ljava/lang/String;
    //   201: astore #8
    //   203: iload #5
    //   205: ifne -> 249
    //   208: aload_1
    //   209: astore_2
    //   210: fload #7
    //   212: fstore_3
    //   213: fload #7
    //   215: fconst_0
    //   216: fcmpg
    //   217: ifge -> 320
    //   220: new java/lang/StringBuilder
    //   223: dup
    //   224: invokespecial <init> : ()V
    //   227: astore_2
    //   228: aload_2
    //   229: aload_1
    //   230: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   233: pop
    //   234: aload_2
    //   235: ldc '- '
    //   237: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   240: pop
    //   241: aload_2
    //   242: invokevirtual toString : ()Ljava/lang/String;
    //   245: astore_2
    //   246: goto -> 314
    //   249: fload #7
    //   251: fconst_0
    //   252: fcmpl
    //   253: ifle -> 288
    //   256: new java/lang/StringBuilder
    //   259: dup
    //   260: invokespecial <init> : ()V
    //   263: astore_2
    //   264: aload_2
    //   265: aload_1
    //   266: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   269: pop
    //   270: aload_2
    //   271: ldc ' + '
    //   273: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   276: pop
    //   277: aload_2
    //   278: invokevirtual toString : ()Ljava/lang/String;
    //   281: astore_2
    //   282: fload #7
    //   284: fstore_3
    //   285: goto -> 320
    //   288: new java/lang/StringBuilder
    //   291: dup
    //   292: invokespecial <init> : ()V
    //   295: astore_2
    //   296: aload_2
    //   297: aload_1
    //   298: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   301: pop
    //   302: aload_2
    //   303: ldc ' - '
    //   305: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   308: pop
    //   309: aload_2
    //   310: invokevirtual toString : ()Ljava/lang/String;
    //   313: astore_2
    //   314: fload #7
    //   316: ldc -1.0
    //   318: fmul
    //   319: fstore_3
    //   320: fload_3
    //   321: fconst_1
    //   322: fcmpl
    //   323: ifne -> 355
    //   326: new java/lang/StringBuilder
    //   329: dup
    //   330: invokespecial <init> : ()V
    //   333: astore_1
    //   334: aload_1
    //   335: aload_2
    //   336: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   339: pop
    //   340: aload_1
    //   341: aload #8
    //   343: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   346: pop
    //   347: aload_1
    //   348: invokevirtual toString : ()Ljava/lang/String;
    //   351: astore_1
    //   352: goto -> 394
    //   355: new java/lang/StringBuilder
    //   358: dup
    //   359: invokespecial <init> : ()V
    //   362: astore_1
    //   363: aload_1
    //   364: aload_2
    //   365: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   368: pop
    //   369: aload_1
    //   370: fload_3
    //   371: invokevirtual append : (F)Ljava/lang/StringBuilder;
    //   374: pop
    //   375: aload_1
    //   376: ldc ' '
    //   378: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   381: pop
    //   382: aload_1
    //   383: aload #8
    //   385: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   388: pop
    //   389: aload_1
    //   390: invokevirtual toString : ()Ljava/lang/String;
    //   393: astore_1
    //   394: iconst_1
    //   395: istore #5
    //   397: iinc #4, 1
    //   400: goto -> 152
    //   403: aload_1
    //   404: astore_2
    //   405: iload #5
    //   407: ifne -> 436
    //   410: new java/lang/StringBuilder
    //   413: dup
    //   414: invokespecial <init> : ()V
    //   417: astore_2
    //   418: aload_2
    //   419: aload_1
    //   420: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   423: pop
    //   424: aload_2
    //   425: ldc '0.0'
    //   427: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   430: pop
    //   431: aload_2
    //   432: invokevirtual toString : ()Ljava/lang/String;
    //   435: astore_2
    //   436: aload_2
    //   437: areturn
  }
  
  public String toString() {
    return toReadableString();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/constraintlayout/solver/ArrayRow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
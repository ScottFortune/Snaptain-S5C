package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;

class Chain {
  private static final boolean DEBUG = false;
  
  static void applyChainConstraints(ConstraintWidgetContainer paramConstraintWidgetContainer, LinearSystem paramLinearSystem, int paramInt) {
    int i;
    ChainHead[] arrayOfChainHead;
    byte b2;
    byte b1 = 0;
    if (paramInt == 0) {
      i = paramConstraintWidgetContainer.mHorizontalChainsSize;
      arrayOfChainHead = paramConstraintWidgetContainer.mHorizontalChainsArray;
      b2 = 0;
    } else {
      b2 = 2;
      i = paramConstraintWidgetContainer.mVerticalChainsSize;
      arrayOfChainHead = paramConstraintWidgetContainer.mVerticalChainsArray;
    } 
    while (b1 < i) {
      ChainHead chainHead = arrayOfChainHead[b1];
      chainHead.define();
      if (paramConstraintWidgetContainer.optimizeFor(4)) {
        if (!Optimizer.applyChainOptimized(paramConstraintWidgetContainer, paramLinearSystem, paramInt, b2, chainHead))
          applyChainConstraints(paramConstraintWidgetContainer, paramLinearSystem, paramInt, b2, chainHead); 
      } else {
        applyChainConstraints(paramConstraintWidgetContainer, paramLinearSystem, paramInt, b2, chainHead);
      } 
      b1++;
    } 
  }
  
  static void applyChainConstraints(ConstraintWidgetContainer paramConstraintWidgetContainer, LinearSystem paramLinearSystem, int paramInt1, int paramInt2, ChainHead paramChainHead) {
    // Byte code:
    //   0: aload #4
    //   2: getfield mFirst : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   5: astore #5
    //   7: aload #4
    //   9: getfield mLast : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   12: astore #6
    //   14: aload #4
    //   16: getfield mFirstVisibleWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   19: astore #7
    //   21: aload #4
    //   23: getfield mLastVisibleWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   26: astore #8
    //   28: aload #4
    //   30: getfield mHead : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   33: astore #9
    //   35: aload #4
    //   37: getfield mTotalWeight : F
    //   40: fstore #10
    //   42: aload #4
    //   44: getfield mFirstMatchConstraintWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   47: astore #11
    //   49: aload #4
    //   51: getfield mLastMatchConstraintWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   54: astore #11
    //   56: aload_0
    //   57: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   60: iload_2
    //   61: aaload
    //   62: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   65: if_acmpne -> 74
    //   68: iconst_1
    //   69: istore #12
    //   71: goto -> 77
    //   74: iconst_0
    //   75: istore #12
    //   77: iload_2
    //   78: ifne -> 136
    //   81: aload #9
    //   83: getfield mHorizontalChainStyle : I
    //   86: ifne -> 95
    //   89: iconst_1
    //   90: istore #13
    //   92: goto -> 98
    //   95: iconst_0
    //   96: istore #13
    //   98: aload #9
    //   100: getfield mHorizontalChainStyle : I
    //   103: iconst_1
    //   104: if_icmpne -> 113
    //   107: iconst_1
    //   108: istore #14
    //   110: goto -> 116
    //   113: iconst_0
    //   114: istore #14
    //   116: iload #13
    //   118: istore #15
    //   120: iload #14
    //   122: istore #16
    //   124: aload #9
    //   126: getfield mHorizontalChainStyle : I
    //   129: iconst_2
    //   130: if_icmpne -> 198
    //   133: goto -> 188
    //   136: aload #9
    //   138: getfield mVerticalChainStyle : I
    //   141: ifne -> 150
    //   144: iconst_1
    //   145: istore #13
    //   147: goto -> 153
    //   150: iconst_0
    //   151: istore #13
    //   153: aload #9
    //   155: getfield mVerticalChainStyle : I
    //   158: iconst_1
    //   159: if_icmpne -> 168
    //   162: iconst_1
    //   163: istore #14
    //   165: goto -> 171
    //   168: iconst_0
    //   169: istore #14
    //   171: iload #13
    //   173: istore #15
    //   175: iload #14
    //   177: istore #16
    //   179: aload #9
    //   181: getfield mVerticalChainStyle : I
    //   184: iconst_2
    //   185: if_icmpne -> 198
    //   188: iconst_1
    //   189: istore #17
    //   191: iload #14
    //   193: istore #16
    //   195: goto -> 205
    //   198: iconst_0
    //   199: istore #17
    //   201: iload #15
    //   203: istore #13
    //   205: aload #5
    //   207: astore #18
    //   209: iconst_0
    //   210: istore #14
    //   212: iload #13
    //   214: istore #15
    //   216: iload #14
    //   218: istore #13
    //   220: aconst_null
    //   221: astore #19
    //   223: iload #13
    //   225: ifne -> 607
    //   228: aload #18
    //   230: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   233: iload_3
    //   234: aaload
    //   235: astore #11
    //   237: iload #12
    //   239: ifne -> 256
    //   242: iload #17
    //   244: ifeq -> 250
    //   247: goto -> 256
    //   250: iconst_4
    //   251: istore #14
    //   253: goto -> 259
    //   256: iconst_1
    //   257: istore #14
    //   259: aload #11
    //   261: invokevirtual getMargin : ()I
    //   264: istore #20
    //   266: iload #20
    //   268: istore #21
    //   270: aload #11
    //   272: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   275: ifnull -> 302
    //   278: iload #20
    //   280: istore #21
    //   282: aload #18
    //   284: aload #5
    //   286: if_acmpeq -> 302
    //   289: iload #20
    //   291: aload #11
    //   293: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   296: invokevirtual getMargin : ()I
    //   299: iadd
    //   300: istore #21
    //   302: iload #17
    //   304: ifeq -> 328
    //   307: aload #18
    //   309: aload #5
    //   311: if_acmpeq -> 328
    //   314: aload #18
    //   316: aload #7
    //   318: if_acmpeq -> 328
    //   321: bipush #6
    //   323: istore #14
    //   325: goto -> 344
    //   328: iload #15
    //   330: ifeq -> 344
    //   333: iload #12
    //   335: ifeq -> 344
    //   338: iconst_4
    //   339: istore #14
    //   341: goto -> 344
    //   344: aload #11
    //   346: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   349: ifnull -> 428
    //   352: aload #18
    //   354: aload #7
    //   356: if_acmpne -> 382
    //   359: aload_1
    //   360: aload #11
    //   362: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   365: aload #11
    //   367: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   370: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   373: iload #21
    //   375: iconst_5
    //   376: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   379: goto -> 403
    //   382: aload_1
    //   383: aload #11
    //   385: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   388: aload #11
    //   390: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   393: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   396: iload #21
    //   398: bipush #6
    //   400: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   403: aload_1
    //   404: aload #11
    //   406: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   409: aload #11
    //   411: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   414: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   417: iload #21
    //   419: iload #14
    //   421: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   424: pop
    //   425: goto -> 428
    //   428: iload #12
    //   430: ifeq -> 513
    //   433: aload #18
    //   435: invokevirtual getVisibility : ()I
    //   438: bipush #8
    //   440: if_icmpeq -> 487
    //   443: aload #18
    //   445: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   448: iload_2
    //   449: aaload
    //   450: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   453: if_acmpne -> 487
    //   456: aload_1
    //   457: aload #18
    //   459: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   462: iload_3
    //   463: iconst_1
    //   464: iadd
    //   465: aaload
    //   466: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   469: aload #18
    //   471: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   474: iload_3
    //   475: aaload
    //   476: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   479: iconst_0
    //   480: iconst_5
    //   481: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   484: goto -> 487
    //   487: aload_1
    //   488: aload #18
    //   490: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   493: iload_3
    //   494: aaload
    //   495: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   498: aload_0
    //   499: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   502: iload_3
    //   503: aaload
    //   504: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   507: iconst_0
    //   508: bipush #6
    //   510: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   513: aload #18
    //   515: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   518: iload_3
    //   519: iconst_1
    //   520: iadd
    //   521: aaload
    //   522: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   525: astore #22
    //   527: aload #19
    //   529: astore #11
    //   531: aload #22
    //   533: ifnull -> 589
    //   536: aload #22
    //   538: getfield mOwner : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   541: astore #22
    //   543: aload #19
    //   545: astore #11
    //   547: aload #22
    //   549: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   552: iload_3
    //   553: aaload
    //   554: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   557: ifnull -> 589
    //   560: aload #22
    //   562: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   565: iload_3
    //   566: aaload
    //   567: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   570: getfield mOwner : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   573: aload #18
    //   575: if_acmpeq -> 585
    //   578: aload #19
    //   580: astore #11
    //   582: goto -> 589
    //   585: aload #22
    //   587: astore #11
    //   589: aload #11
    //   591: ifnull -> 601
    //   594: aload #11
    //   596: astore #18
    //   598: goto -> 604
    //   601: iconst_1
    //   602: istore #13
    //   604: goto -> 220
    //   607: aload #8
    //   609: ifnull -> 678
    //   612: aload #6
    //   614: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   617: astore #11
    //   619: iload_3
    //   620: iconst_1
    //   621: iadd
    //   622: istore #13
    //   624: aload #11
    //   626: iload #13
    //   628: aaload
    //   629: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   632: ifnull -> 678
    //   635: aload #8
    //   637: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   640: iload #13
    //   642: aaload
    //   643: astore #11
    //   645: aload_1
    //   646: aload #11
    //   648: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   651: aload #6
    //   653: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   656: iload #13
    //   658: aaload
    //   659: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   662: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   665: aload #11
    //   667: invokevirtual getMargin : ()I
    //   670: ineg
    //   671: iconst_5
    //   672: invokevirtual addLowerThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   675: goto -> 678
    //   678: iload #12
    //   680: ifeq -> 728
    //   683: aload_0
    //   684: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   687: astore_0
    //   688: iload_3
    //   689: iconst_1
    //   690: iadd
    //   691: istore #13
    //   693: aload_1
    //   694: aload_0
    //   695: iload #13
    //   697: aaload
    //   698: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   701: aload #6
    //   703: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   706: iload #13
    //   708: aaload
    //   709: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   712: aload #6
    //   714: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   717: iload #13
    //   719: aaload
    //   720: invokevirtual getMargin : ()I
    //   723: bipush #6
    //   725: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   728: aload #4
    //   730: getfield mWeightedMatchConstraintsWidgets : Ljava/util/ArrayList;
    //   733: astore_0
    //   734: aload_0
    //   735: ifnull -> 1023
    //   738: aload_0
    //   739: invokevirtual size : ()I
    //   742: istore #13
    //   744: iload #13
    //   746: iconst_1
    //   747: if_icmple -> 1023
    //   750: aload #4
    //   752: getfield mHasUndefinedWeights : Z
    //   755: ifeq -> 777
    //   758: aload #4
    //   760: getfield mHasComplexMatchWeights : Z
    //   763: ifne -> 777
    //   766: aload #4
    //   768: getfield mWidgetsMatchCount : I
    //   771: i2f
    //   772: fstore #23
    //   774: goto -> 781
    //   777: fload #10
    //   779: fstore #23
    //   781: aconst_null
    //   782: astore #11
    //   784: iconst_0
    //   785: istore #14
    //   787: fconst_0
    //   788: fstore #24
    //   790: iload #14
    //   792: iload #13
    //   794: if_icmpge -> 1023
    //   797: aload_0
    //   798: iload #14
    //   800: invokevirtual get : (I)Ljava/lang/Object;
    //   803: checkcast androidx/constraintlayout/solver/widgets/ConstraintWidget
    //   806: astore #18
    //   808: aload #18
    //   810: getfield mWeight : [F
    //   813: iload_2
    //   814: faload
    //   815: fstore #10
    //   817: fload #10
    //   819: fconst_0
    //   820: fcmpg
    //   821: ifge -> 870
    //   824: aload #4
    //   826: getfield mHasComplexMatchWeights : Z
    //   829: ifeq -> 864
    //   832: aload_1
    //   833: aload #18
    //   835: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   838: iload_3
    //   839: iconst_1
    //   840: iadd
    //   841: aaload
    //   842: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   845: aload #18
    //   847: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   850: iload_3
    //   851: aaload
    //   852: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   855: iconst_0
    //   856: iconst_4
    //   857: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   860: pop
    //   861: goto -> 907
    //   864: fconst_1
    //   865: fstore #10
    //   867: goto -> 870
    //   870: fload #10
    //   872: fconst_0
    //   873: fcmpl
    //   874: ifne -> 910
    //   877: aload_1
    //   878: aload #18
    //   880: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   883: iload_3
    //   884: iconst_1
    //   885: iadd
    //   886: aaload
    //   887: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   890: aload #18
    //   892: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   895: iload_3
    //   896: aaload
    //   897: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   900: iconst_0
    //   901: bipush #6
    //   903: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   906: pop
    //   907: goto -> 1017
    //   910: aload #11
    //   912: ifnull -> 1009
    //   915: aload #11
    //   917: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   920: iload_3
    //   921: aaload
    //   922: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   925: astore #19
    //   927: aload #11
    //   929: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   932: astore #11
    //   934: iload_3
    //   935: iconst_1
    //   936: iadd
    //   937: istore #12
    //   939: aload #11
    //   941: iload #12
    //   943: aaload
    //   944: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   947: astore #22
    //   949: aload #18
    //   951: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   954: iload_3
    //   955: aaload
    //   956: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   959: astore #25
    //   961: aload #18
    //   963: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   966: iload #12
    //   968: aaload
    //   969: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   972: astore #26
    //   974: aload_1
    //   975: invokevirtual createRow : ()Landroidx/constraintlayout/solver/ArrayRow;
    //   978: astore #11
    //   980: aload #11
    //   982: fload #24
    //   984: fload #23
    //   986: fload #10
    //   988: aload #19
    //   990: aload #22
    //   992: aload #25
    //   994: aload #26
    //   996: invokevirtual createRowEqualMatchDimensions : (FFFLandroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;)Landroidx/constraintlayout/solver/ArrayRow;
    //   999: pop
    //   1000: aload_1
    //   1001: aload #11
    //   1003: invokevirtual addConstraint : (Landroidx/constraintlayout/solver/ArrayRow;)V
    //   1006: goto -> 1009
    //   1009: aload #18
    //   1011: astore #11
    //   1013: fload #10
    //   1015: fstore #24
    //   1017: iinc #14, 1
    //   1020: goto -> 790
    //   1023: aload #7
    //   1025: ifnull -> 1229
    //   1028: aload #7
    //   1030: aload #8
    //   1032: if_acmpeq -> 1040
    //   1035: iload #17
    //   1037: ifeq -> 1229
    //   1040: aload #5
    //   1042: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1045: iload_3
    //   1046: aaload
    //   1047: astore #18
    //   1049: aload #6
    //   1051: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1054: astore_0
    //   1055: iload_3
    //   1056: iconst_1
    //   1057: iadd
    //   1058: istore #13
    //   1060: aload_0
    //   1061: iload #13
    //   1063: aaload
    //   1064: astore #11
    //   1066: aload #5
    //   1068: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1071: iload_3
    //   1072: aaload
    //   1073: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1076: ifnull -> 1096
    //   1079: aload #5
    //   1081: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1084: iload_3
    //   1085: aaload
    //   1086: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1089: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1092: astore_0
    //   1093: goto -> 1098
    //   1096: aconst_null
    //   1097: astore_0
    //   1098: aload #6
    //   1100: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1103: iload #13
    //   1105: aaload
    //   1106: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1109: ifnull -> 1131
    //   1112: aload #6
    //   1114: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1117: iload #13
    //   1119: aaload
    //   1120: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1123: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1126: astore #4
    //   1128: goto -> 1134
    //   1131: aconst_null
    //   1132: astore #4
    //   1134: aload #7
    //   1136: aload #8
    //   1138: if_acmpne -> 1160
    //   1141: aload #7
    //   1143: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1146: iload_3
    //   1147: aaload
    //   1148: astore #18
    //   1150: aload #7
    //   1152: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1155: iload #13
    //   1157: aaload
    //   1158: astore #11
    //   1160: aload_0
    //   1161: ifnull -> 2308
    //   1164: aload #4
    //   1166: ifnull -> 2308
    //   1169: iload_2
    //   1170: ifne -> 1183
    //   1173: aload #9
    //   1175: getfield mHorizontalBiasPercent : F
    //   1178: fstore #10
    //   1180: goto -> 1190
    //   1183: aload #9
    //   1185: getfield mVerticalBiasPercent : F
    //   1188: fstore #10
    //   1190: aload #18
    //   1192: invokevirtual getMargin : ()I
    //   1195: istore_2
    //   1196: aload #11
    //   1198: invokevirtual getMargin : ()I
    //   1201: istore #13
    //   1203: aload_1
    //   1204: aload #18
    //   1206: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1209: aload_0
    //   1210: iload_2
    //   1211: fload #10
    //   1213: aload #4
    //   1215: aload #11
    //   1217: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1220: iload #13
    //   1222: iconst_5
    //   1223: invokevirtual addCentering : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;IFLandroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   1226: goto -> 2308
    //   1229: iload #15
    //   1231: ifeq -> 1729
    //   1234: aload #7
    //   1236: ifnull -> 1729
    //   1239: aload #4
    //   1241: getfield mWidgetsMatchCount : I
    //   1244: ifle -> 1266
    //   1247: aload #4
    //   1249: getfield mWidgetsCount : I
    //   1252: aload #4
    //   1254: getfield mWidgetsMatchCount : I
    //   1257: if_icmpne -> 1266
    //   1260: iconst_1
    //   1261: istore #12
    //   1263: goto -> 1269
    //   1266: iconst_0
    //   1267: istore #12
    //   1269: aload #7
    //   1271: astore #4
    //   1273: aload #4
    //   1275: astore #18
    //   1277: aload #4
    //   1279: ifnull -> 2308
    //   1282: aload #4
    //   1284: getfield mNextChainWidget : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1287: iload_2
    //   1288: aaload
    //   1289: astore #11
    //   1291: aload #11
    //   1293: ifnull -> 1318
    //   1296: aload #11
    //   1298: invokevirtual getVisibility : ()I
    //   1301: bipush #8
    //   1303: if_icmpne -> 1318
    //   1306: aload #11
    //   1308: getfield mNextChainWidget : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1311: iload_2
    //   1312: aaload
    //   1313: astore #11
    //   1315: goto -> 1291
    //   1318: aload #11
    //   1320: ifnonnull -> 1336
    //   1323: aload #4
    //   1325: aload #8
    //   1327: if_acmpne -> 1333
    //   1330: goto -> 1336
    //   1333: goto -> 1708
    //   1336: aload #4
    //   1338: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1341: iload_3
    //   1342: aaload
    //   1343: astore #19
    //   1345: aload #19
    //   1347: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1350: astore #25
    //   1352: aload #19
    //   1354: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1357: ifnull -> 1373
    //   1360: aload #19
    //   1362: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1365: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1368: astore #9
    //   1370: goto -> 1376
    //   1373: aconst_null
    //   1374: astore #9
    //   1376: aload #18
    //   1378: aload #4
    //   1380: if_acmpeq -> 1399
    //   1383: aload #18
    //   1385: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1388: iload_3
    //   1389: iconst_1
    //   1390: iadd
    //   1391: aaload
    //   1392: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1395: astore_0
    //   1396: goto -> 1451
    //   1399: aload #9
    //   1401: astore_0
    //   1402: aload #4
    //   1404: aload #7
    //   1406: if_acmpne -> 1451
    //   1409: aload #9
    //   1411: astore_0
    //   1412: aload #18
    //   1414: aload #4
    //   1416: if_acmpne -> 1451
    //   1419: aload #5
    //   1421: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1424: iload_3
    //   1425: aaload
    //   1426: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1429: ifnull -> 1449
    //   1432: aload #5
    //   1434: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1437: iload_3
    //   1438: aaload
    //   1439: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1442: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1445: astore_0
    //   1446: goto -> 1451
    //   1449: aconst_null
    //   1450: astore_0
    //   1451: aload #19
    //   1453: invokevirtual getMargin : ()I
    //   1456: istore #17
    //   1458: aload #4
    //   1460: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1463: astore #9
    //   1465: iload_3
    //   1466: iconst_1
    //   1467: iadd
    //   1468: istore #21
    //   1470: aload #9
    //   1472: iload #21
    //   1474: aaload
    //   1475: invokevirtual getMargin : ()I
    //   1478: istore #14
    //   1480: aload #11
    //   1482: ifnull -> 1517
    //   1485: aload #11
    //   1487: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1490: iload_3
    //   1491: aaload
    //   1492: astore #9
    //   1494: aload #9
    //   1496: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1499: astore #19
    //   1501: aload #4
    //   1503: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1506: iload #21
    //   1508: aaload
    //   1509: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1512: astore #22
    //   1514: goto -> 1569
    //   1517: aload #6
    //   1519: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1522: iload #21
    //   1524: aaload
    //   1525: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1528: astore #26
    //   1530: aload #26
    //   1532: ifnull -> 1545
    //   1535: aload #26
    //   1537: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1540: astore #9
    //   1542: goto -> 1548
    //   1545: aconst_null
    //   1546: astore #9
    //   1548: aload #4
    //   1550: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1553: iload #21
    //   1555: aaload
    //   1556: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1559: astore #22
    //   1561: aload #9
    //   1563: astore #19
    //   1565: aload #26
    //   1567: astore #9
    //   1569: iload #14
    //   1571: istore #13
    //   1573: aload #9
    //   1575: ifnull -> 1588
    //   1578: iload #14
    //   1580: aload #9
    //   1582: invokevirtual getMargin : ()I
    //   1585: iadd
    //   1586: istore #13
    //   1588: iload #17
    //   1590: istore #14
    //   1592: aload #18
    //   1594: ifnull -> 1613
    //   1597: iload #17
    //   1599: aload #18
    //   1601: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1604: iload #21
    //   1606: aaload
    //   1607: invokevirtual getMargin : ()I
    //   1610: iadd
    //   1611: istore #14
    //   1613: aload #25
    //   1615: ifnull -> 1333
    //   1618: aload_0
    //   1619: ifnull -> 1333
    //   1622: aload #19
    //   1624: ifnull -> 1333
    //   1627: aload #22
    //   1629: ifnull -> 1333
    //   1632: aload #4
    //   1634: aload #7
    //   1636: if_acmpne -> 1651
    //   1639: aload #7
    //   1641: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1644: iload_3
    //   1645: aaload
    //   1646: invokevirtual getMargin : ()I
    //   1649: istore #14
    //   1651: aload #4
    //   1653: aload #8
    //   1655: if_acmpne -> 1674
    //   1658: aload #8
    //   1660: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1663: iload #21
    //   1665: aaload
    //   1666: invokevirtual getMargin : ()I
    //   1669: istore #13
    //   1671: goto -> 1674
    //   1674: iload #12
    //   1676: ifeq -> 1686
    //   1679: bipush #6
    //   1681: istore #17
    //   1683: goto -> 1689
    //   1686: iconst_4
    //   1687: istore #17
    //   1689: aload_1
    //   1690: aload #25
    //   1692: aload_0
    //   1693: iload #14
    //   1695: ldc 0.5
    //   1697: aload #19
    //   1699: aload #22
    //   1701: iload #13
    //   1703: iload #17
    //   1705: invokevirtual addCentering : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;IFLandroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   1708: aload #4
    //   1710: invokevirtual getVisibility : ()I
    //   1713: bipush #8
    //   1715: if_icmpeq -> 1722
    //   1718: aload #4
    //   1720: astore #18
    //   1722: aload #11
    //   1724: astore #4
    //   1726: goto -> 1277
    //   1729: iload #16
    //   1731: ifeq -> 2308
    //   1734: aload #7
    //   1736: ifnull -> 2308
    //   1739: aload #4
    //   1741: getfield mWidgetsMatchCount : I
    //   1744: ifle -> 1766
    //   1747: aload #4
    //   1749: getfield mWidgetsCount : I
    //   1752: aload #4
    //   1754: getfield mWidgetsMatchCount : I
    //   1757: if_icmpne -> 1766
    //   1760: iconst_1
    //   1761: istore #13
    //   1763: goto -> 1769
    //   1766: iconst_0
    //   1767: istore #13
    //   1769: aload #7
    //   1771: astore #4
    //   1773: aload #4
    //   1775: astore #11
    //   1777: aload #4
    //   1779: ifnull -> 2148
    //   1782: aload #4
    //   1784: getfield mNextChainWidget : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1787: iload_2
    //   1788: aaload
    //   1789: astore_0
    //   1790: aload_0
    //   1791: ifnull -> 1813
    //   1794: aload_0
    //   1795: invokevirtual getVisibility : ()I
    //   1798: bipush #8
    //   1800: if_icmpne -> 1813
    //   1803: aload_0
    //   1804: getfield mNextChainWidget : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1807: iload_2
    //   1808: aaload
    //   1809: astore_0
    //   1810: goto -> 1790
    //   1813: aload #4
    //   1815: aload #7
    //   1817: if_acmpeq -> 2121
    //   1820: aload #4
    //   1822: aload #8
    //   1824: if_acmpeq -> 2121
    //   1827: aload_0
    //   1828: ifnull -> 2121
    //   1831: aload_0
    //   1832: aload #8
    //   1834: if_acmpne -> 1842
    //   1837: aconst_null
    //   1838: astore_0
    //   1839: goto -> 1842
    //   1842: aload #4
    //   1844: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1847: iload_3
    //   1848: aaload
    //   1849: astore #9
    //   1851: aload #9
    //   1853: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1856: astore #26
    //   1858: aload #9
    //   1860: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1863: ifnull -> 1876
    //   1866: aload #9
    //   1868: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1871: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1874: astore #18
    //   1876: aload #11
    //   1878: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1881: astore #18
    //   1883: iload_3
    //   1884: iconst_1
    //   1885: iadd
    //   1886: istore #21
    //   1888: aload #18
    //   1890: iload #21
    //   1892: aaload
    //   1893: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1896: astore #25
    //   1898: aload #9
    //   1900: invokevirtual getMargin : ()I
    //   1903: istore #17
    //   1905: aload #4
    //   1907: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1910: iload #21
    //   1912: aaload
    //   1913: invokevirtual getMargin : ()I
    //   1916: istore #12
    //   1918: aload_0
    //   1919: ifnull -> 1964
    //   1922: aload_0
    //   1923: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1926: iload_3
    //   1927: aaload
    //   1928: astore #18
    //   1930: aload #18
    //   1932: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1935: astore #19
    //   1937: aload #18
    //   1939: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1942: ifnull -> 1958
    //   1945: aload #18
    //   1947: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1950: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1953: astore #9
    //   1955: goto -> 2016
    //   1958: aconst_null
    //   1959: astore #9
    //   1961: goto -> 2016
    //   1964: aload #4
    //   1966: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1969: iload #21
    //   1971: aaload
    //   1972: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1975: astore #22
    //   1977: aload #22
    //   1979: ifnull -> 1992
    //   1982: aload #22
    //   1984: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1987: astore #18
    //   1989: goto -> 1995
    //   1992: aconst_null
    //   1993: astore #18
    //   1995: aload #4
    //   1997: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2000: iload #21
    //   2002: aaload
    //   2003: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2006: astore #9
    //   2008: aload #18
    //   2010: astore #19
    //   2012: aload #22
    //   2014: astore #18
    //   2016: iload #12
    //   2018: istore #14
    //   2020: aload #18
    //   2022: ifnull -> 2035
    //   2025: iload #12
    //   2027: aload #18
    //   2029: invokevirtual getMargin : ()I
    //   2032: iadd
    //   2033: istore #14
    //   2035: iload #17
    //   2037: istore #12
    //   2039: aload #11
    //   2041: ifnull -> 2060
    //   2044: iload #17
    //   2046: aload #11
    //   2048: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2051: iload #21
    //   2053: aaload
    //   2054: invokevirtual getMargin : ()I
    //   2057: iadd
    //   2058: istore #12
    //   2060: iload #13
    //   2062: ifeq -> 2072
    //   2065: bipush #6
    //   2067: istore #17
    //   2069: goto -> 2075
    //   2072: iconst_4
    //   2073: istore #17
    //   2075: aload #26
    //   2077: ifnull -> 2118
    //   2080: aload #25
    //   2082: ifnull -> 2118
    //   2085: aload #19
    //   2087: ifnull -> 2118
    //   2090: aload #9
    //   2092: ifnull -> 2118
    //   2095: aload_1
    //   2096: aload #26
    //   2098: aload #25
    //   2100: iload #12
    //   2102: ldc 0.5
    //   2104: aload #19
    //   2106: aload #9
    //   2108: iload #14
    //   2110: iload #17
    //   2112: invokevirtual addCentering : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;IFLandroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   2115: goto -> 2118
    //   2118: goto -> 2121
    //   2121: aload #4
    //   2123: invokevirtual getVisibility : ()I
    //   2126: bipush #8
    //   2128: if_icmpeq -> 2134
    //   2131: goto -> 2138
    //   2134: aload #11
    //   2136: astore #4
    //   2138: aload #4
    //   2140: astore #11
    //   2142: aload_0
    //   2143: astore #4
    //   2145: goto -> 1777
    //   2148: aload #7
    //   2150: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2153: iload_3
    //   2154: aaload
    //   2155: astore_0
    //   2156: aload #5
    //   2158: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2161: iload_3
    //   2162: aaload
    //   2163: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2166: astore #4
    //   2168: aload #8
    //   2170: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2173: astore #11
    //   2175: iload_3
    //   2176: iconst_1
    //   2177: iadd
    //   2178: istore_2
    //   2179: aload #11
    //   2181: iload_2
    //   2182: aaload
    //   2183: astore #11
    //   2185: aload #6
    //   2187: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2190: iload_2
    //   2191: aaload
    //   2192: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2195: astore #9
    //   2197: aload #4
    //   2199: ifnull -> 2274
    //   2202: aload #7
    //   2204: aload #8
    //   2206: if_acmpeq -> 2231
    //   2209: aload_1
    //   2210: aload_0
    //   2211: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2214: aload #4
    //   2216: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2219: aload_0
    //   2220: invokevirtual getMargin : ()I
    //   2223: iconst_5
    //   2224: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   2227: pop
    //   2228: goto -> 2274
    //   2231: aload #9
    //   2233: ifnull -> 2274
    //   2236: aload_1
    //   2237: aload_0
    //   2238: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2241: aload #4
    //   2243: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2246: aload_0
    //   2247: invokevirtual getMargin : ()I
    //   2250: ldc 0.5
    //   2252: aload #11
    //   2254: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2257: aload #9
    //   2259: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2262: aload #11
    //   2264: invokevirtual getMargin : ()I
    //   2267: iconst_5
    //   2268: invokevirtual addCentering : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;IFLandroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   2271: goto -> 2274
    //   2274: aload #9
    //   2276: ifnull -> 2308
    //   2279: aload #7
    //   2281: aload #8
    //   2283: if_acmpeq -> 2308
    //   2286: aload_1
    //   2287: aload #11
    //   2289: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2292: aload #9
    //   2294: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2297: aload #11
    //   2299: invokevirtual getMargin : ()I
    //   2302: ineg
    //   2303: iconst_5
    //   2304: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   2307: pop
    //   2308: iload #15
    //   2310: ifne -> 2318
    //   2313: iload #16
    //   2315: ifeq -> 2522
    //   2318: aload #7
    //   2320: ifnull -> 2522
    //   2323: aload #7
    //   2325: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2328: iload_3
    //   2329: aaload
    //   2330: astore #9
    //   2332: aload #8
    //   2334: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2337: astore_0
    //   2338: iload_3
    //   2339: iconst_1
    //   2340: iadd
    //   2341: istore #13
    //   2343: aload_0
    //   2344: iload #13
    //   2346: aaload
    //   2347: astore #11
    //   2349: aload #9
    //   2351: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2354: ifnull -> 2370
    //   2357: aload #9
    //   2359: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2362: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2365: astore #4
    //   2367: goto -> 2373
    //   2370: aconst_null
    //   2371: astore #4
    //   2373: aload #11
    //   2375: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2378: ifnull -> 2393
    //   2381: aload #11
    //   2383: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2386: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2389: astore_0
    //   2390: goto -> 2395
    //   2393: aconst_null
    //   2394: astore_0
    //   2395: aload #6
    //   2397: aload #8
    //   2399: if_acmpeq -> 2431
    //   2402: aload #6
    //   2404: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2407: iload #13
    //   2409: aaload
    //   2410: astore_0
    //   2411: aload_0
    //   2412: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2415: ifnull -> 2429
    //   2418: aload_0
    //   2419: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2422: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2425: astore_0
    //   2426: goto -> 2431
    //   2429: aconst_null
    //   2430: astore_0
    //   2431: aload #7
    //   2433: aload #8
    //   2435: if_acmpne -> 2457
    //   2438: aload #7
    //   2440: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2443: iload_3
    //   2444: aaload
    //   2445: astore #9
    //   2447: aload #7
    //   2449: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2452: iload #13
    //   2454: aaload
    //   2455: astore #11
    //   2457: aload #4
    //   2459: ifnull -> 2522
    //   2462: aload_0
    //   2463: ifnull -> 2522
    //   2466: aload #9
    //   2468: invokevirtual getMargin : ()I
    //   2471: istore_2
    //   2472: aload #8
    //   2474: ifnonnull -> 2484
    //   2477: aload #6
    //   2479: astore #18
    //   2481: goto -> 2488
    //   2484: aload #8
    //   2486: astore #18
    //   2488: aload #18
    //   2490: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2493: iload #13
    //   2495: aaload
    //   2496: invokevirtual getMargin : ()I
    //   2499: istore_3
    //   2500: aload_1
    //   2501: aload #9
    //   2503: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2506: aload #4
    //   2508: iload_2
    //   2509: ldc 0.5
    //   2511: aload_0
    //   2512: aload #11
    //   2514: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2517: iload_3
    //   2518: iconst_5
    //   2519: invokevirtual addCentering : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;IFLandroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   2522: return
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/constraintlayout/solver/widgets/Chain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
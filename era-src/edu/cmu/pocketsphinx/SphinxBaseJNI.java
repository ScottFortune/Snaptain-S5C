package edu.cmu.pocketsphinx;

public class SphinxBaseJNI {
  public static final native boolean Config_exists(long paramLong, Config paramConfig, String paramString);
  
  public static final native boolean Config_getBoolean(long paramLong, Config paramConfig, String paramString);
  
  public static final native double Config_getFloat(long paramLong, Config paramConfig, String paramString);
  
  public static final native int Config_getInt(long paramLong, Config paramConfig, String paramString);
  
  public static final native String Config_getString(long paramLong, Config paramConfig, String paramString);
  
  public static final native void Config_setBoolean(long paramLong, Config paramConfig, String paramString, boolean paramBoolean);
  
  public static final native void Config_setFloat(long paramLong, Config paramConfig, String paramString, double paramDouble);
  
  public static final native void Config_setInt(long paramLong, Config paramConfig, String paramString, int paramInt);
  
  public static final native void Config_setString(long paramLong, Config paramConfig, String paramString1, String paramString2);
  
  public static final native void Config_setStringExtra(long paramLong, Config paramConfig, String paramString1, String paramString2);
  
  public static final native int FrontEnd_outputSize(long paramLong, FrontEnd paramFrontEnd);
  
  public static final native int FrontEnd_processUtt(long paramLong1, FrontEnd paramFrontEnd, String paramString, long paramLong2, long paramLong3);
  
  public static final native int FsgModel_addAlt(long paramLong, FsgModel paramFsgModel, String paramString1, String paramString2);
  
  public static final native int FsgModel_addSilence(long paramLong, FsgModel paramFsgModel, String paramString, int paramInt, float paramFloat);
  
  public static final native int FsgModel_nullTransAdd(long paramLong, FsgModel paramFsgModel, int paramInt1, int paramInt2, int paramInt3);
  
  public static final native int FsgModel_tagTransAdd(long paramLong, FsgModel paramFsgModel, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public static final native void FsgModel_transAdd(long paramLong, FsgModel paramFsgModel, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public static final native int FsgModel_wordAdd(long paramLong, FsgModel paramFsgModel, String paramString);
  
  public static final native int FsgModel_wordId(long paramLong, FsgModel paramFsgModel, String paramString);
  
  public static final native void FsgModel_writefile(long paramLong, FsgModel paramFsgModel, String paramString);
  
  public static final native boolean JsgfIterator_hasNext(long paramLong, JsgfIterator paramJsgfIterator);
  
  public static final native long JsgfIterator_next(long paramLong, JsgfIterator paramJsgfIterator);
  
  public static final native long JsgfRule_fromIter(long paramLong);
  
  public static final native String JsgfRule_getName(long paramLong, JsgfRule paramJsgfRule);
  
  public static final native boolean JsgfRule_isPublic(long paramLong, JsgfRule paramJsgfRule);
  
  public static final native long Jsgf_buildFsg(long paramLong1, Jsgf paramJsgf, long paramLong2, JsgfRule paramJsgfRule, long paramLong3, LogMath paramLogMath, float paramFloat);
  
  public static final native String Jsgf_getName(long paramLong, Jsgf paramJsgf);
  
  public static final native long Jsgf_getRule(long paramLong, Jsgf paramJsgf, String paramString);
  
  public static final native long Jsgf_iterator(long paramLong, Jsgf paramJsgf);
  
  public static final native double LogMath_exp(long paramLong, LogMath paramLogMath, int paramInt);
  
  public static final native boolean NGramModelSetIterator_hasNext(long paramLong, NGramModelSetIterator paramNGramModelSetIterator);
  
  public static final native long NGramModelSetIterator_next(long paramLong, NGramModelSetIterator paramNGramModelSetIterator);
  
  public static final native long NGramModelSet_add(long paramLong1, NGramModelSet paramNGramModelSet, long paramLong2, NGramModel paramNGramModel, String paramString, float paramFloat, boolean paramBoolean);
  
  public static final native int NGramModelSet_count(long paramLong, NGramModelSet paramNGramModelSet);
  
  public static final native String NGramModelSet_current(long paramLong, NGramModelSet paramNGramModelSet);
  
  public static final native long NGramModelSet_iterator(long paramLong, NGramModelSet paramNGramModelSet);
  
  public static final native long NGramModelSet_lookup(long paramLong, NGramModelSet paramNGramModelSet, String paramString);
  
  public static final native long NGramModelSet_select(long paramLong, NGramModelSet paramNGramModelSet, String paramString);
  
  public static final native int NGramModel_addWord(long paramLong, NGramModel paramNGramModel, String paramString, float paramFloat);
  
  public static final native void NGramModel_casefold(long paramLong, NGramModel paramNGramModel, int paramInt);
  
  public static final native long NGramModel_fromIter(long paramLong);
  
  public static final native int NGramModel_prob(long paramLong, NGramModel paramNGramModel, String[] paramArrayOfString);
  
  public static final native int NGramModel_size(long paramLong, NGramModel paramNGramModel);
  
  public static final native int NGramModel_strToType(long paramLong, NGramModel paramNGramModel, String paramString);
  
  public static final native String NGramModel_typeToStr(long paramLong, NGramModel paramNGramModel, int paramInt);
  
  public static final native void NGramModel_write(long paramLong, NGramModel paramNGramModel, String paramString, int paramInt);
  
  public static final native void delete_Config(long paramLong);
  
  public static final native void delete_Feature(long paramLong);
  
  public static final native void delete_FrontEnd(long paramLong);
  
  public static final native void delete_FsgModel(long paramLong);
  
  public static final native void delete_Jsgf(long paramLong);
  
  public static final native void delete_JsgfIterator(long paramLong);
  
  public static final native void delete_JsgfRule(long paramLong);
  
  public static final native void delete_LogMath(long paramLong);
  
  public static final native void delete_NGramModel(long paramLong);
  
  public static final native void delete_NGramModelSet(long paramLong);
  
  public static final native void delete_NGramModelSetIterator(long paramLong);
  
  public static final native long new_FrontEnd();
  
  public static final native long new_FsgModel__SWIG_0(String paramString, long paramLong, LogMath paramLogMath, float paramFloat, int paramInt);
  
  public static final native long new_FsgModel__SWIG_1(String paramString, long paramLong, LogMath paramLogMath, float paramFloat);
  
  public static final native long new_Jsgf(String paramString);
  
  public static final native long new_JsgfIterator(long paramLong);
  
  public static final native long new_JsgfRule();
  
  public static final native long new_LogMath();
  
  public static final native long new_NGramModelSet(long paramLong1, Config paramConfig, long paramLong2, LogMath paramLogMath, String paramString);
  
  public static final native long new_NGramModelSetIterator(long paramLong);
  
  public static final native long new_NGramModel__SWIG_0(String paramString);
  
  public static final native long new_NGramModel__SWIG_1(long paramLong1, Config paramConfig, long paramLong2, LogMath paramLogMath, String paramString);
  
  public static final native long new_feature();
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/edu/cmu/pocketsphinx/SphinxBaseJNI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package edu.cmu.pocketsphinx;

public class PocketSphinxJNI {
  public static final native void Decoder_addWord(long paramLong, Decoder paramDecoder, String paramString1, String paramString2, int paramInt);
  
  public static final native long Decoder_defaultConfig();
  
  public static final native void Decoder_endUtt(long paramLong, Decoder paramDecoder);
  
  public static final native long Decoder_fileConfig(String paramString);
  
  public static final native long Decoder_getConfig(long paramLong, Decoder paramDecoder);
  
  public static final native long Decoder_getFe(long paramLong, Decoder paramDecoder);
  
  public static final native long Decoder_getFeat(long paramLong, Decoder paramDecoder);
  
  public static final native long Decoder_getFsg(long paramLong, Decoder paramDecoder, String paramString);
  
  public static final native boolean Decoder_getInSpeech(long paramLong, Decoder paramDecoder);
  
  public static final native String Decoder_getKws(long paramLong, Decoder paramDecoder, String paramString);
  
  public static final native long Decoder_getLattice(long paramLong, Decoder paramDecoder);
  
  public static final native long Decoder_getLm(long paramLong, Decoder paramDecoder, String paramString);
  
  public static final native long Decoder_getLogmath(long paramLong, Decoder paramDecoder);
  
  public static final native short[] Decoder_getRawdata(long paramLong, Decoder paramDecoder);
  
  public static final native String Decoder_getSearch(long paramLong, Decoder paramDecoder);
  
  public static final native long Decoder_hyp(long paramLong, Decoder paramDecoder);
  
  public static final native void Decoder_loadDict(long paramLong, Decoder paramDecoder, String paramString1, String paramString2, String paramString3);
  
  public static final native String Decoder_lookupWord(long paramLong, Decoder paramDecoder, String paramString);
  
  public static final native int Decoder_nFrames(long paramLong, Decoder paramDecoder);
  
  public static final native long Decoder_nbest(long paramLong, Decoder paramDecoder);
  
  public static final native int Decoder_processRaw(long paramLong1, Decoder paramDecoder, short[] paramArrayOfshort, long paramLong2, boolean paramBoolean1, boolean paramBoolean2);
  
  public static final native void Decoder_reinit(long paramLong1, Decoder paramDecoder, long paramLong2, Config paramConfig);
  
  public static final native void Decoder_saveDict(long paramLong, Decoder paramDecoder, String paramString1, String paramString2);
  
  public static final native long Decoder_seg(long paramLong, Decoder paramDecoder);
  
  public static final native void Decoder_setAllphoneFile(long paramLong, Decoder paramDecoder, String paramString1, String paramString2);
  
  public static final native void Decoder_setFsg(long paramLong1, Decoder paramDecoder, String paramString, long paramLong2, FsgModel paramFsgModel);
  
  public static final native void Decoder_setJsgfFile(long paramLong, Decoder paramDecoder, String paramString1, String paramString2);
  
  public static final native void Decoder_setJsgfString(long paramLong, Decoder paramDecoder, String paramString1, String paramString2);
  
  public static final native void Decoder_setKeyphrase(long paramLong, Decoder paramDecoder, String paramString1, String paramString2);
  
  public static final native void Decoder_setKws(long paramLong, Decoder paramDecoder, String paramString1, String paramString2);
  
  public static final native void Decoder_setLm(long paramLong1, Decoder paramDecoder, String paramString, long paramLong2, NGramModel paramNGramModel);
  
  public static final native void Decoder_setLmFile(long paramLong, Decoder paramDecoder, String paramString1, String paramString2);
  
  public static final native void Decoder_setRawdataSize(long paramLong1, Decoder paramDecoder, long paramLong2);
  
  public static final native void Decoder_setSearch(long paramLong, Decoder paramDecoder, String paramString);
  
  public static final native void Decoder_startStream(long paramLong, Decoder paramDecoder);
  
  public static final native void Decoder_startUtt(long paramLong, Decoder paramDecoder);
  
  public static final native void Decoder_unsetSearch(long paramLong, Decoder paramDecoder, String paramString);
  
  public static final native int Hypothesis_bestScore_get(long paramLong, Hypothesis paramHypothesis);
  
  public static final native void Hypothesis_bestScore_set(long paramLong, Hypothesis paramHypothesis, int paramInt);
  
  public static final native String Hypothesis_hypstr_get(long paramLong, Hypothesis paramHypothesis);
  
  public static final native void Hypothesis_hypstr_set(long paramLong, Hypothesis paramHypothesis, String paramString);
  
  public static final native int Hypothesis_prob_get(long paramLong, Hypothesis paramHypothesis);
  
  public static final native void Hypothesis_prob_set(long paramLong, Hypothesis paramHypothesis, int paramInt);
  
  public static final native void Lattice_write(long paramLong, Lattice paramLattice, String paramString);
  
  public static final native void Lattice_writeHtk(long paramLong, Lattice paramLattice, String paramString);
  
  public static final native boolean NBestIterator_hasNext(long paramLong, NBestIterator paramNBestIterator);
  
  public static final native long NBestIterator_next(long paramLong, NBestIterator paramNBestIterator);
  
  public static final native long NBestList_iterator(long paramLong, NBestList paramNBestList);
  
  public static final native long NBest_fromIter(long paramLong);
  
  public static final native long NBest_hyp(long paramLong, NBest paramNBest);
  
  public static final native String NBest_hypstr_get(long paramLong, NBest paramNBest);
  
  public static final native void NBest_hypstr_set(long paramLong, NBest paramNBest, String paramString);
  
  public static final native int NBest_score_get(long paramLong, NBest paramNBest);
  
  public static final native void NBest_score_set(long paramLong, NBest paramNBest, int paramInt);
  
  public static final native boolean SegmentIterator_hasNext(long paramLong, SegmentIterator paramSegmentIterator);
  
  public static final native long SegmentIterator_next(long paramLong, SegmentIterator paramSegmentIterator);
  
  public static final native long SegmentList_iterator(long paramLong, SegmentList paramSegmentList);
  
  public static final native int Segment_ascore_get(long paramLong, Segment paramSegment);
  
  public static final native void Segment_ascore_set(long paramLong, Segment paramSegment, int paramInt);
  
  public static final native int Segment_endFrame_get(long paramLong, Segment paramSegment);
  
  public static final native void Segment_endFrame_set(long paramLong, Segment paramSegment, int paramInt);
  
  public static final native long Segment_fromIter(long paramLong);
  
  public static final native int Segment_lback_get(long paramLong, Segment paramSegment);
  
  public static final native void Segment_lback_set(long paramLong, Segment paramSegment, int paramInt);
  
  public static final native int Segment_lscore_get(long paramLong, Segment paramSegment);
  
  public static final native void Segment_lscore_set(long paramLong, Segment paramSegment, int paramInt);
  
  public static final native int Segment_prob_get(long paramLong, Segment paramSegment);
  
  public static final native void Segment_prob_set(long paramLong, Segment paramSegment, int paramInt);
  
  public static final native int Segment_startFrame_get(long paramLong, Segment paramSegment);
  
  public static final native void Segment_startFrame_set(long paramLong, Segment paramSegment, int paramInt);
  
  public static final native String Segment_word_get(long paramLong, Segment paramSegment);
  
  public static final native void Segment_word_set(long paramLong, Segment paramSegment, String paramString);
  
  public static final native void delete_Decoder(long paramLong);
  
  public static final native void delete_Hypothesis(long paramLong);
  
  public static final native void delete_Lattice(long paramLong);
  
  public static final native void delete_NBest(long paramLong);
  
  public static final native void delete_NBestIterator(long paramLong);
  
  public static final native void delete_NBestList(long paramLong);
  
  public static final native void delete_Segment(long paramLong);
  
  public static final native void delete_SegmentIterator(long paramLong);
  
  public static final native void delete_SegmentList(long paramLong);
  
  public static final native long new_Decoder__SWIG_0();
  
  public static final native long new_Decoder__SWIG_1(long paramLong, Config paramConfig);
  
  public static final native long new_Hypothesis(String paramString, int paramInt1, int paramInt2);
  
  public static final native long new_Lattice__SWIG_0(String paramString);
  
  public static final native long new_Lattice__SWIG_1(long paramLong, Decoder paramDecoder, String paramString);
  
  public static final native long new_NBestIterator(long paramLong);
  
  public static final native long new_SegmentIterator(long paramLong);
  
  public static final native long new_nBest();
  
  public static final native long new_segment();
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/edu/cmu/pocketsphinx/PocketSphinxJNI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
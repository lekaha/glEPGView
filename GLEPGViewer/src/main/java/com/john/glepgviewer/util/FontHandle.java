package com.john.glepgviewer.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author John
 * @description 
 * 	Handle about font.
 *
 */
public class FontHandle {
	
	private static final String TAG = "FontHandle";
	private static final String FONT_FILE_DIR = "/files/";
	private static String DATA_DIR = "";
	private static HashMap<Integer, String> mFontFileHash = new HashMap<Integer, String>();
	static{
		mFontFileHash.put(new Integer(FontMap.JP_ID),FontMap.JP_FONT_FILE);
	}
	
	private static FontHandle mFontHandle;
	private Context mContext;
	
	private int mCountryId = FontMap.JP_ID;
	private Typeface mTypeface;
	
	private String mFontFilePrefix;
	private int mFontFileCount;
	private int mFontFirstPostfixNumber;
	
	private FontHandle(){}

	private FontHandle(Context context){
		
		this.mContext = context;
//		DATA_DIR = this.mContext.getApplicationInfo().dataDir;
		mTypeface = Typeface.DEFAULT;
		mFontFilePrefix = "aribFont";
		mFontFileCount = 6;
		mFontFirstPostfixNumber = 1;
		initCountryHash();
	}
	
	private FontHandle(Context context, String fontFileFormat, int fileCount, int firstPostfixNumber){
		this.mContext = context;
		
		mTypeface = Typeface.DEFAULT;
		mFontFilePrefix = fontFileFormat;
		mFontFileCount = fileCount;
		mFontFirstPostfixNumber = firstPostfixNumber;
		initCountryHash();
	}
	
	private void init(Context context, String fontFileFormat, int fileCount, int firstPostfixNumber){
		this.mContext = context;
		mTypeface = Typeface.DEFAULT;
		mFontFilePrefix = fontFileFormat;
		mFontFileCount = fileCount;
		mFontFirstPostfixNumber = firstPostfixNumber;
		initCountryHash();
	}
	
	public static FontHandle getInstance(){
		if(null == mFontHandle){
			mFontHandle = new FontHandle();
		}
		
		return mFontHandle;
	}
	
	synchronized private void initCountryHash(){
		
		Log.d(TAG, "initCountryHash() entry.");
		DATA_DIR = this.mContext.getApplicationInfo().dataDir;
		
//		mFontFileHash.put(new Integer(FontMap.JP_ID), 
//				new CountryFontFile(FontMap.JP_ID,FontMap.JP_FONT_FILE));
		
		Log.d(TAG, "initCountryHash() leave.");
	}
	
	public void setupFont(Context context, FONT_COUNTRY_ID countryId, String fontFileFormat, int fileCount, int firstPostfixNumber){
		Log.d(TAG, "setupFont() entry.");
		init(context, fontFileFormat, fileCount, firstPostfixNumber);
		mCountryId = countryId.getId();
		
		switch(this.mCountryId){
		
			case FontMap.JP_ID:
				
				if(new File(this.mContext.getApplicationInfo().dataDir + 
				FONT_FILE_DIR + mFontFileHash.get(Integer.valueOf(mCountryId))).exists()){
					Log.d(TAG,"setupFont() The font file is exist. (" + this.mContext.getApplicationInfo().dataDir +
							FONT_FILE_DIR + mFontFileHash.get(Integer.valueOf(mCountryId)) + ")");
					
					mTypeface = Typeface.createFromFile(this.mContext.getApplicationInfo().dataDir + 
							FONT_FILE_DIR + mFontFileHash.get(Integer.valueOf(mCountryId)));
					return;
				}
				
				Log.d(TAG,"setupFont() Prepare setup font by mCountryId = " + this.mCountryId);
				String fontPostix = ".dat";
				String fontPath = "fonts/" + mFontFilePrefix;
				ArrayList<String> mJpFontPart = new ArrayList<String>();
				int count = 0;
				for(int i = mFontFirstPostfixNumber; count<mFontFileCount; i++){
					String font = fontPath + String.valueOf(i) + fontPostix;  
					mJpFontPart.add(font);
					count++;
				}
				
				boolean isMerge = false;
				try {
					isMerge = MergeFileUtil.mergeAssetsFile(mContext, DATA_DIR + FONT_FILE_DIR, 
							mFontFileHash.get(this.mCountryId), mJpFontPart);
				} catch (Exception e) {
					e.printStackTrace();
					Log.w(TAG, e.toString());
				}
				
				if(isMerge){
					if(new File(this.mContext.getApplicationInfo().dataDir + 
						FONT_FILE_DIR + mFontFileHash.get(Integer.valueOf(mCountryId))).exists()){
						mTypeface = Typeface.createFromFile(this.mContext.getApplicationInfo().dataDir + 
							FONT_FILE_DIR + mFontFileHash.get(Integer.valueOf(mCountryId)));
					}
					else
						break;
				}
				else{
					Log.e(TAG, "setupFont error: Merge font files error.");
				}
				break;
				
			default:
				mTypeface = Typeface.DEFAULT;
				break;
		}
		
		
		Log.d(TAG, "setupFont() leave.");
	}
	
//	private boolean mergeAssetsFile(String dir, String file, 
//			ArrayList<String> partFileList) throws Exception{
//		
//    	String DirectoryPath = dir;
//    	
//    	File directory = new File(DirectoryPath);
//        if (!directory.isDirectory()) {
//            if (!directory.mkdirs()) {
//                return false;
//            }
//        }
//        
//        String FilePath = DirectoryPath + file;
//        Log.d(TAG, "FilePath = " + FilePath);
//        
//    	if(!new File(FilePath).exists())
//    	{
//    		Log.d(TAG, "FilePath = " + FilePath + "is doesn't exist");
//    		try {
//				OutputStream out = new FileOutputStream(FilePath);
//				byte[] buf = new byte[1024];
//				
//				InputStream in;
//				int readLen = 0;
//				
//				for(int i=0; i<partFileList.size(); i++){
//					
//					in = this.mContext.getAssets().open(partFileList.get(i));
//					while((readLen = in.read(buf)) != -1){
//						
//						out.write(buf, 0, readLen);
//					}
//					out.flush();
//					in.close();
//				}
//				out.close();
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			
//    	}
//		return true;
//    }
	
	@SuppressWarnings("unused")
	private void setCountryId(int id){
		
		this.mCountryId = id;
	}
	
	public Typeface getFontTypeface(){
		if(null != mTypeface){
//			AVMLog.d(TAG, "getFontTypeface", "superimpose Get specify font");
			return mTypeface;
		}
		return Typeface.DEFAULT;
	}
	
//	private class CountryFontFile{
//		
//		private int mCountryId;
//		private String mFontFile;
//		
//		public CountryFontFile(int countryId, String fontFile){
//			this.mCountryId = countryId;
//			this.mFontFile = fontFile;
//		}
//		
//		private int getId(){
//			return this.mCountryId;
//		}
//		
//		private String getFontFile(){
//			return this.mFontFile;
//		}
//	}
	
	private class FontMap{		
		private static final int JP_ID = 81;
		private static final String JP_FONT_FILE = "wada.ttf";
	}
	
	public enum FONT_COUNTRY_ID {
		UNKNOWN(-1),
		JP(81);
		
		int mTypeId;
		FONT_COUNTRY_ID(int typeId){
			mTypeId = typeId;
		}
		
		public int getId(){return mTypeId;}
		
		public static FONT_COUNTRY_ID instance(int id){
			FONT_COUNTRY_ID deviceType = intToTypeMap.get(Integer.valueOf(id));
			if(deviceType == null){
				return FONT_COUNTRY_ID.UNKNOWN;
			}
			return deviceType;
		}
		
		private static final Map<Integer, FONT_COUNTRY_ID> intToTypeMap = new HashMap<Integer, FONT_COUNTRY_ID>();
		static {
		    for (FONT_COUNTRY_ID type : FONT_COUNTRY_ID.values()) {
		        intToTypeMap.put(type.mTypeId, type);
		    }
		}
	}
}

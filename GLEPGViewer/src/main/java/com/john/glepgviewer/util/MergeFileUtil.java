package com.john.glepgviewer.util;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MergeFileUtil {
	
	public static boolean mergeAssetsFile(Context context, String destinationDir, String destinationFile, 
			ArrayList<String> partFileList) throws Exception{
		
    	String DirectoryPath = destinationDir;
    	
    	File directory = new File(DirectoryPath);
        if (!directory.isDirectory()) {
            if (!directory.mkdirs()) {
                return false;
            }
        }
        
        String FilePath = DirectoryPath + destinationFile;
//        System.out.println("FilePath = " + FilePath);
        
    	if(!new File(FilePath).exists())
    	{
//    		System.out.println("FilePath = " + FilePath + " is doesn't exist");
    		try {
				OutputStream out = new FileOutputStream(FilePath);
				byte[] buf = new byte[1024];
				
				InputStream in;
				int readLen = 0;
				
				for(int i=0; i<partFileList.size(); i++){
					
					in = context.getAssets().open(partFileList.get(i));
					while((readLen = in.read(buf)) != -1){
						
						out.write(buf, 0, readLen);
					}
					out.flush();
					in.close();
				}
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			
			
    	}
    	else{
//    		System.out.println("File is exist");
    		Log.i("", "File is exist");
    	}
    		
		return true;
    }
}

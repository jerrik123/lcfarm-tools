package com.njq.nongfadai.common.util;

import java.io.File;
import java.nio.charset.Charset;

import com.google.common.io.Files;

public class FileUtils {
	public static void writeErrorInfo(String errorInfo,String fileName){
		try {
			File file = new File(OSInfo.isWindows()?IConstants.DEFAULT_DIR_WINDOWS:IConstants.DEFAULT_DIR_LINUX);
			if(!file.exists()){
				file.mkdirs();
			}
			File files = new File(file, fileName);
			if(!files.exists()){
				files.createNewFile();
			}
			Files.append(errorInfo, files, Charset.forName("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		FileUtils.writeErrorInfo("ok\r\n","error1.txt");
	}
	
}

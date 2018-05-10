package cn.gzitrans.soft.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
	
	public static void uploadFile(File file, String filePath, String fileName) throws Exception { 
        File targetFile = new File(filePath);  
        if(!targetFile.exists()){    
            targetFile.mkdirs();    
        }
        
        byte[] fileByte = InputStream2ByteArray(filePath+file.getName());
        
        
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(fileByte);
        out.flush();
        out.close();
    }
	
	
	
	private static byte[] InputStream2ByteArray(String filePath) throws IOException {
		 
	    InputStream in = new FileInputStream(filePath);
	    byte[] data = toByteArray(in);
	    in.close();
	 
	    return data;
	}
	 
	private static byte[] toByteArray(InputStream in) throws IOException {
	 
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    byte[] buffer = new byte[1024 * 4];
	    int n = 0;
	    while ((n = in.read(buffer)) != -1) {
	        out.write(buffer, 0, n);
	    }
	    return out.toByteArray();
	}

}

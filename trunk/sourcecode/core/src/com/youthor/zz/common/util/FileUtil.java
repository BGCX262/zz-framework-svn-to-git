package com.youthor.zz.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * 文件操作工具类
 *
 */
public class FileUtil {

     //判断文件是否存在
    public static boolean isFileExsit(String fileFullName){
        try{
            File file = new File(fileFullName);
            if(file.isFile()){
                return true;
            }
            return false;
        }
        catch(Exception e){
            return false;
        }
    }
    
    //删除文件
    public static boolean deleteFile(String fileFullName){
        try{
            File file = new File(fileFullName);
            if(file.isFile()){
                file.delete();
                return true;
            }
            return false;
        }
        catch(Exception e){
            return false;
        }
    }
    
    //字符串保存到文件中去
    public static boolean stringToFile(String filePath, String fileName, String fileContent){
        return stringToFile(filePath, fileName, fileContent, "UTF-8");
    }
    
    //字符串保存到文件中去
    public static boolean stringToFile(String filePath, String fileName, String fileContent,String encoding){
        BufferedWriter out = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(filePath + fileName);
            file.createNewFile();
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file), encoding);
            out = new BufferedWriter(write);   
            out.write(fileContent);
            return true;
        } 
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        finally {
            try{
                out.flush();
                out.close();
            }
            catch(Exception e){
                return false;
            }
        }
    }
}

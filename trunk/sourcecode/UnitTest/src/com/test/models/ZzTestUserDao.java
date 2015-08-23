package com.test.models;

import com.test.home.models.dao.mysql.UserDao;
import com.youthor.zz.common.ZzApp;
import com.youthor.zz.common.ZzObject;
import com.youthor.zz.common.test.ZzTest;


public class ZzTestUserDao extends ZzTest{
    
    public void testUserDaoSaveAction(){
        
    }
    public static void main(String [] args) {
         String appPath="E:/workspace/youthor/zz-framework/sourcecode/WebContent/";
         ZzApp.setAppPath(appPath);
         ZzApp.setAppendPath("WEB-INF");
         ZzApp.init();
         
         UserDao dao = (UserDao)ZzApp.getResourceSingleton("home/userDao");
         if(dao == null){
             throw new IllegalArgumentException("can not get the dao instance!");
         }
         System.out.println("create dao success, dao class ->" + dao.getClass());
         
         ZzObject zzObject = new ZzObject();
         //String id = randomSeqNo(9);
         String id="521512284";
         zzObject.addData("id", id);
         zzObject.addData("login_name", id + "_jason8");
         zzObject.addData("login_pwd", id + "_1234568");
         zzObject.addData("sex", "F8");
         zzObject.addData("name", "周旭顺8");
         dao.save(zzObject);
         System.out.println("save object to db success, id ->" + id);
    }
    
    /**
     * 产生随机的流水号
     * @param seqNoLen:流水号的长度
     * @return
     */
    public static String randomSeqNo(int seqNoLen){
        if(seqNoLen <= 0){
            return "";
        }
    
        StringBuilder sb = new StringBuilder(seqNoLen);
        for(int i=0;i<seqNoLen;i++){
        int curValue = (int)(Math.random( )*10);
        sb.append(curValue);
        }
    
        return sb.toString();
    }
}

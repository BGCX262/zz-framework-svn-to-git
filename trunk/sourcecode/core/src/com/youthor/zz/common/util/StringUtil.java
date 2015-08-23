package com.youthor.zz.common.util;

public class StringUtil {

    public static boolean isBlank(String str){
        if(str == null || "".equals(str)){
            return true;
        }
        return false;
    }
    public static boolean isNotBlank(String str){
        return (!isBlank(str));
    }
    
    public static String doWithNull(Object o) {
        if(o==null) return "";
        else 
        {
            String returnValue=o.toString();
            if(returnValue.equalsIgnoreCase("null")) return "";
            else return returnValue.trim();
        }
        
    }
    
    public  static String arrToString(String [] arr,String splitChar) 
    {
        if(arr==null) return "";
        StringBuffer sb=new StringBuffer("");
        for(int i=0;i<arr.length;i++)
        {
            if(arr[i]!=null&&!arr[i].equals(""))
                sb.append(arr[i]+splitChar);
        }
        String returnValue=sb.toString();
        if(returnValue.endsWith(splitChar)) returnValue=returnValue.substring(0,returnValue.length()-splitChar.length());
        return returnValue;
    }
    
    public static String changeFirstToUpper(String str)
    {
        if(str==null||str.equals("")) return "";
         String firstChar = str.substring(0, 1);
         String leaveChar = str.substring(1);
         return firstChar.toUpperCase()+leaveChar;
    }
    
    public static String[] split(String strSource, String strDiv) {
        int arynum = 0, intIdx = 0, intIdex = 0;
        int div_length = strDiv.length();
        if (strSource.compareTo("") != 0) {
                if (strSource.indexOf(strDiv) != -1) {
                        intIdx = strSource.indexOf(strDiv);
                        for (int intCount = 1; ; intCount++) {
                                if (strSource.indexOf(strDiv, intIdx + div_length) != -1) {
                                        intIdx = strSource.indexOf(strDiv, intIdx + div_length);
                                        arynum = intCount;
                                }
                                else {
                                        arynum += 2;
                                        break;
                                }
                        }
                }
                else {
                        arynum = 1;
                }
        }
        else {
                arynum = 0;

        }
        intIdx = 0;
        intIdex = 0;
        String[] returnStr = new String[arynum];

        if (strSource.compareTo("") != 0) {
                if (strSource.indexOf(strDiv) != -1) {
                        intIdx = (int) strSource.indexOf(strDiv);
                        returnStr[0] = (String) strSource.substring(0, intIdx);
                        for (int intCount = 1; ; intCount++) {
                                if (strSource.indexOf(strDiv, intIdx + div_length) != -1) {
                                        intIdex = (int) strSource.indexOf(strDiv, intIdx + div_length);
                                        returnStr[intCount] = (String) strSource.substring(intIdx + div_length,
                                                intIdex);
                                        intIdx = (int) strSource.indexOf(strDiv, intIdx + div_length);
                                }
                                else {
                                        returnStr[intCount] = (String) strSource.substring(intIdx + div_length,
                                                strSource.length());
                                        break;
                                }
                        }
                }
                else {
                        returnStr[0] = (String) strSource.substring(0, strSource.length());
                        return returnStr;
                }
        }
        else {
                return returnStr;
        }
        return returnStr;
    }
    
    public static String replaceAll(String str, String strSub, String strRpl) {
        String[] tmp = split(str, strSub);
        StringBuffer sb = new StringBuffer();;
        if (tmp.length != 0) {
            sb.append(tmp[0]) ;
            for (int i = 0; i < tmp.length - 1; i++) {
                sb.append(strRpl);
                sb.append(tmp[i + 1]);
            }
        }
        return sb.toString();
    }
}

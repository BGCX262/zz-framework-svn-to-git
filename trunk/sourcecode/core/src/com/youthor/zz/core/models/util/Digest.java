package com.youthor.zz.core.models.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

import com.youthor.zz.common.ZzApp;

public class Digest {
	private  final String SHA1 = "SHA-1";
    private  final String MD5 = "MD5";

    //-- String Hash function --//
    /**
     * 对输入字符串进行sha1散列, 返回Hex编码的结果.
     */
    public  String sha1ToHex(String input) {
        byte[] digestResult = digest(input, SHA1);
        Encode encode = (Encode)ZzApp.getSingletonModel("core/util_encode");
        return encode.hexEncode(digestResult);
    }
    
    public  String md5ToHex(String input) {
        byte[] digestResult = digest(input, MD5);
        Encode encode = (Encode)ZzApp.getSingletonModel("core/util_encode");
        return encode.hexEncode(digestResult);
    }

    /**
     * 对输入字符串进行sha1散列, 返回Base64编码的结果.
     */
    public  String sha1ToBase64(String input) {
        byte[] digestResult = digest(input, SHA1);
        Encode encode = (Encode)ZzApp.getSingletonModel("core/util_encode");
        return encode.base64Encode(digestResult);
    }

    /**
     * 对输入字符串进行sha1散列, 返回Base64编码的URL安全的结果.
     */
    public  String sha1ToBase64UrlSafe(String input) {
        byte[] digestResult = digest(input, SHA1);
        Encode encode = (Encode)ZzApp.getSingletonModel("core/util_encode");
        return encode.base64UrlSafeEncode(digestResult);
    }

    /**
     * 对字符串进行散列, 支持md5与sha1算法.
     */
    private  byte[] digest(String input, String algorithm) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            return messageDigest.digest(input.getBytes());
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Security exception", e);
        }
    }
    
    

    //-- File Hash function --//
    /**
     * 对文件进行md5散列,返回Hex编码结果.
     */
    public  String md5ToHex(InputStream input) throws IOException {
        return digest(input, MD5);
    }

    /**
     * 对文件进行sha1散列,返回Hex编码结果.
     */
    public  String sha1ToHex(InputStream input) throws IOException {
        return digest(input, SHA1);
    }

    /**
     * 对文件进行散列, 支持md5与sha1算法.
     */
    private  String digest(InputStream input, String algorithm)  {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            int bufferLength = 1024;
            byte[] buffer = new byte[bufferLength];
            int read = input.read(buffer, 0, bufferLength);

            while (read > -1) {
                messageDigest.update(buffer, 0, read);
                read = input.read(buffer, 0, bufferLength);
            }
            Encode encode = (Encode)ZzApp.getSingletonModel("core/util_encode");
            return encode.hexEncode(messageDigest.digest());

        } 
        catch (GeneralSecurityException e) {
            throw new IllegalStateException("Security exception", e);
        }
        catch (IOException e) {
            throw new IllegalStateException("Security exception", e);
        }
    }
}

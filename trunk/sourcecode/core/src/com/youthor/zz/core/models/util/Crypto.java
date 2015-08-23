package com.youthor.zz.core.models.util;

import java.security.GeneralSecurityException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.youthor.zz.common.ZzApp;

public class Crypto {
    private final String DES = "DES";
    private final String AES = "AES";
    private final String HMACSHA1 = "HmacSHA1";

    private final int DEFAULT_HMACSHA1_KEYSIZE = 160;//RFC2401
    private final int DEFAULT_AES_KEYSIZE = 128;
    
    
    //-- HMAC-SHA1 funciton --//
    /**
     * 使用HMAC-SHA1进行消息签名, 返回字节数组,长度为20字节.
     * 
     * @param input 原始输入字符串
     * @param keyBytes HMAC-SHA1密钥
     */
    public byte[] hmacSha1(String input, byte[] keyBytes) {
        try {
            SecretKey secretKey = new SecretKeySpec(keyBytes, HMACSHA1);
            Mac mac = Mac.getInstance(HMACSHA1);
            mac.init(secretKey);
            return mac.doFinal(input.getBytes());
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Security exception", e);
        }
    }

    /**
     * 使用HMAC-SHA1进行消息签名, 返回Hex编码的结果,长度为40字符.
     *  
     * @see #hmacSha1(String, byte[])
     */
    public String hmacSha1ToHex(String input, byte[] keyBytes) {
        byte[] macResult = hmacSha1(input, keyBytes);
        Encode encode = (Encode)ZzApp.getSingletonModel("core/util_encode");
        return encode.hexEncode(macResult);
    }

    /**
     * 使用HMAC-SHA1进行消息签名, 返回Base64编码的结果.
     * 
     * @see #hmacSha1(String, byte[])
     */
    public String hmacSha1ToBase64(String input, byte[] keyBytes) {
        byte[] macResult = hmacSha1(input, keyBytes);
        Encode encode = (Encode)ZzApp.getSingletonModel("core/util_encode");
        return encode.base64Encode(macResult);
    }

    /**
     * 使用HMAC-SHA1进行消息签名, 返回Base64编码的URL安全的结果.
     * 
     * @see #hmacSha1(String, byte[])
     */
    public String hmacSha1ToBase64UrlSafe(String input, byte[] keyBytes) {
        byte[] macResult = hmacSha1(input, keyBytes);
        Encode encode = (Encode)ZzApp.getSingletonModel("core/util_encode");
        return encode.base64UrlSafeEncode(macResult);
    }

    /**
     * 校验Hex编码的HMAC-SHA1签名是否正确.
     * 
     * @param hexMac Hex编码的签名
     * @param input 原始输入字符串
     * @param keyBytes 密钥
     */
    public boolean isHexMacValid(String hexMac, String input, byte[] keyBytes) {
        Encode encode = (Encode)ZzApp.getSingletonModel("core/util_encode");
        byte[] expected = encode.hexDecode(hexMac);
        byte[] actual = hmacSha1(input, keyBytes);

        return Arrays.equals(expected, actual);
    }

    /**
     * 校验Base64/Base64URLSafe编码的HMAC-SHA1签名是否正确.
     * 
     * @param base64Mac Base64/Base64URLSafe编码的签名
     * @param input 原始输入字符串
     * @param keyBytes 密钥
     */
    public boolean isBase64MacValid(String base64Mac, String input, byte[] keyBytes) {
    	Encode encode = (Encode)ZzApp.getSingletonModel("core/util_encode");
    	byte[] expected = encode.base64Decode(base64Mac);
        byte[] actual = hmacSha1(input, keyBytes);

        return Arrays.equals(expected, actual);
    }

    /**
     * 生成HMAC-SHA1密钥,返回字节数组,长度为160位(20字节).
     * HMAC-SHA1算法对密钥无特殊要求, RFC2401建议最少长度为160位(20字节).
     */
    public byte[] generateMacSha1Key() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(HMACSHA1);
            keyGenerator.init(DEFAULT_HMACSHA1_KEYSIZE);
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Security exception", e);
        }
    }

    /**
     * 生成HMAC-SHA1密钥, 返回Hex编码的结果,长度为40字符. 
     * @see #generateMacSha1Key()
     */
    public String generateMacSha1HexKey() {
        Encode encode = (Encode)ZzApp.getSingletonModel("core/util_encode");
        return encode.hexEncode(generateMacSha1Key());
    }

    //-- DES function --//
    /**
     * 使用DES加密原始字符串, 返回Hex编码的结果.
     * 
     * @param input 原始输入字符串
     * @param keyBytes 符合DES要求的密钥
     */
    public String desEncryptToHex(String input, byte[] keyBytes) {
        byte[] encryptResult = des(input.getBytes(), keyBytes, Cipher.ENCRYPT_MODE);
        Encode encode = (Encode)ZzApp.getSingletonModel("core/util_encode");
        return encode.hexEncode(encryptResult);
    }

    /**
     * 使用DES加密原始字符串, 返回Base64编码的结果.
     * 
     * @param input 原始输入字符串
     * @param keyBytes 符合DES要求的密钥
     */
    public String desEncryptToBase64(String input, byte[] keyBytes) {
        byte[] encryptResult = des(input.getBytes(), keyBytes, Cipher.ENCRYPT_MODE);
        Encode encode = (Encode)ZzApp.getSingletonModel("core/util_encode");
        return encode.base64Encode(encryptResult);
    }

    /**
     * 使用DES解密Hex编码的加密字符串, 返回原始字符串.
     * 
     * @param input Hex编码的加密字符串
     * @param keyBytes 符合DES要求的密钥
     */
    public String desDecryptFromHex(String input, byte[] keyBytes) {
        Encode encode = (Encode)ZzApp.getSingletonModel("core/util_encode");
        byte[] decryptResult = des(encode.hexDecode(input), keyBytes, Cipher.DECRYPT_MODE);
        
        return new String(decryptResult);
    }

    /**
     * 使用DES解密Base64编码的加密字符串, 返回原始字符串.
     * 
     * @param input Base64编码的加密字符串
     * @param keyBytes 符合DES要求的密钥
     */
    public String desDecryptFromBase64(String input, byte[] keyBytes) {
        Encode encode = (Encode)ZzApp.getSingletonModel("core/util_encode");
        byte[] decryptResult = des(encode.base64Decode(input), keyBytes, Cipher.DECRYPT_MODE);
        return new String(decryptResult);
    }

    /**
     * 使用DES加密或解密无编码的原始字节数组, 返回无编码的字节数组结果.
     * 
     * @param inputBytes 原始字节数组
     * @param keyBytes 符合DES要求的密钥
     * @param mode Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
     */
    private byte[] des(byte[] inputBytes, byte[] keyBytes, int mode) {
        try {
            DESKeySpec desKeySpec = new DESKeySpec(keyBytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

            Cipher cipher = Cipher.getInstance(DES);
            cipher.init(mode, secretKey);
            return cipher.doFinal(inputBytes);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Security exception", e);
        }
    }

    /**
     * 生成符合DES要求的密钥, 长度为64位(8字节).
     */
    public byte[] generateDesKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(DES);
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Security exception", e);
        }
    }

    /**
     * 生成符合DES要求的Hex编码密钥, 长度为16字符.
     */
    public String generateDesHexKey() {
        Encode encode = (Encode)ZzApp.getSingletonModel("core/util_encode");
        return encode.hexEncode(generateDesKey());
    }

    //-- AES funciton --//
    /**
     * 使用AES加密原始字符串, 返回Hex编码的结果.
     * 
     * @param input 原始输入字符串
     * @param keyBytes 符合AES要求的密钥
     */
    public String aesEncryptToHex(String input, byte[] keyBytes) {
        byte[] encryptResult = aes(input.getBytes(), keyBytes, Cipher.ENCRYPT_MODE);
        Encode encode = (Encode)ZzApp.getSingletonModel("core/util_encode");
        return encode.hexEncode(encryptResult);
    }

    /**
     * 使用AES加密原始字符串, 返回Base64编码的结果.
     * 
     * @param input 原始输入字符串
     * @param keyBytes 符合AES要求的密钥
     */
    public String aesEncryptToBase64(String input, byte[] keyBytes) {
        byte[] encryptResult = aes(input.getBytes(), keyBytes, Cipher.ENCRYPT_MODE);
        Encode encode = (Encode)ZzApp.getSingletonModel("core/util_encode");
        return encode.base64Encode(encryptResult);
    }

    /**
     * 使用AES解密Hex编码的加密字符串, 返回原始字符串.
     * 
     * @param input Hex编码的加密字符串
     * @param keyBytes 符合AES要求的密钥
     */
    public String aesDecryptFromHex(String input, byte[] keyBytes) {
        Encode encode = (Encode)ZzApp.getSingletonModel("core/util_encode");
        byte[] decryptResult = aes(encode.hexDecode(input), keyBytes, Cipher.DECRYPT_MODE);
        return new String(decryptResult);
    }

    /**
     * 使用AES解密Base64编码的加密字符串, 返回原始字符串.
     * 
     * @param input Base64编码的加密字符串
     * @param keyBytes 符合AES要求的密钥
     */
    public String aesDecryptFromBase64(String input, byte[] keyBytes) {
        Encode encode = (Encode)ZzApp.getSingletonModel("core/util_encode");
        byte[] decryptResult = aes(encode.base64Decode(input), keyBytes, Cipher.DECRYPT_MODE);
        return new String(decryptResult);
    }

    /**
     * 使用AES加密或解密无编码的原始字节数组, 返回无编码的字节数组结果.
     * 
     * @param inputBytes 原始字节数组
     * @param keyBytes 符合AES要求的密钥
     * @param mode Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
     */
    private byte[] aes(byte[] inputBytes, byte[] keyBytes, int mode) {
        try {
            SecretKey secretKey = new SecretKeySpec(keyBytes, AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(mode, secretKey);
            return cipher.doFinal(inputBytes);
        } catch (Exception e) {
            throw new IllegalStateException("Security exception", e);
        }
    }

    /**
     * 生成AES密钥,返回字节数组,长度为128位(16字节).
     */
    public byte[] generateAesKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
            keyGenerator.init(DEFAULT_AES_KEYSIZE);
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Security exception", e);
        }
    }

    /**
     * 生成AES密钥, 返回Hex编码的结果,长度为32字符. 
     * @see #generateMacSha1Key()
     */
    public String generateAesHexKey() {
        Encode encode = (Encode)ZzApp.getSingletonModel("core/util_encode");
        return encode.hexEncode(generateAesKey());
    }
}

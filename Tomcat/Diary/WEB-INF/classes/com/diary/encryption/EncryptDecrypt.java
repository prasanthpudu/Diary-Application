package com.diary.encryption;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptDecrypt {
    public static String ALGORITHM = "AES";
    private static int AES_128 = 128;


    private static byte[] encryptDecrypt(final int mode, final byte[] key, final byte[] message)
            throws Exception {
        final Cipher cipher = Cipher.getInstance(ALGORITHM);
        final SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
        cipher.init(mode, keySpec);
        return cipher.doFinal(message);
    }

    public static String generateKey() throws NoSuchAlgorithmException{
         KeyGenerator keyGenerator = KeyGenerator.getInstance(EncryptDecrypt.ALGORITHM);
         keyGenerator.init(AES_128);
         SecretKey key = keyGenerator.generateKey();
         String keyEncoded = Base64.getEncoder().encodeToString(key.getEncoded());
        return keyEncoded;

    }


    public static String encrypt(String message,String secretKey) throws Exception{
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        SecretKey key = new SecretKeySpec(decodedKey,"AES");    
        byte[] cipherText = encryptDecrypt(Cipher.ENCRYPT_MODE, key.getEncoded(),  message.getBytes());
        String encrypted_message =  Base64.getEncoder().encodeToString(cipherText);
        return encrypted_message;
    }

    public static String decrypt(String encryptedMessage,String key) throws Exception{
        byte[] decodedKey = Base64.getDecoder().decode(key);
        byte[] cipher_text = Base64.getDecoder().decode(encryptedMessage);
        SecretKey secretKey = new SecretKeySpec(decodedKey,"AES");    
        byte[] decryptedString = encryptDecrypt(Cipher.DECRYPT_MODE, secretKey.getEncoded(), cipher_text);
        String decryptedMessage = new String(decryptedString);
        return decryptedMessage;

    }


    public static void main(String[] args) throws Exception {

        // EncryptDecrypt cu = new EncryptDecrypt();
        // Map<String, String> map = cu.encrypt("prasanth");
        // String encryptedmessage =  map.get("value");
        // String key= map.get("key");
        // System.out.println(encryptedmessage);
        // String decryptedMessage = cu.decrypt(encryptedmessage,key);
        // System.out.println(decryptedMessage);
    }

}
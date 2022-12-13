package com.diary.encryption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Hash {
    public static String toSHA1(String password) {
        byte passwordByte[]= password.getBytes();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        }
        catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        } 
        byte bytes[]=md.digest(passwordByte);
        return byteArrayToHexString(bytes);
    }
    private static String byteArrayToHexString(byte[] b) {
        String result = "";
        for (int i=0; i < b.length; i++) {
          result +=
                Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
      }
      public static void main(String[] args) {
        String password = toSHA1("Prasanth1");
        System.out.println("result "+password);
      }
}
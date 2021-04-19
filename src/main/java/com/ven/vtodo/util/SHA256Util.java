package com.ven.vtodo.util;

import java.security.MessageDigest;

public class SHA256Util {
    /**
         * 利用java原生的类实现SHA256加密
         *
         * @return
         * @paramstr加密后的报文
         */
    public static String getSHA256(String str) {
        MessageDigest messageDigest;
        String encodestr ="";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodestr =byte2Hex(messageDigest.digest());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return encodestr;
    }
/**
     * 将byte转为16进制
     *
     * @return
     * @parambytes
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer =new StringBuffer();
        String temp;
        for (int i =0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] &0xFF);
            if (temp.length() ==1) {
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    public static void main(String[] args) {
        //generate password to save in MySQL
        String username = "veeoni";
        String passwrod = "111111";
        System.out.println(getSHA256(username+getSHA256(passwrod)));
    }
}
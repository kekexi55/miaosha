package com.imooc.miaosha.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    private static String md5(String str){
        return DigestUtils.md5Hex(str);
    }
    private static String salt = "1a2b3c4d";

    private static String inputPassToFormPass(String pass){
        pass = ""+salt.charAt(0)+salt.charAt(2) + pass +salt.charAt(5) + salt.charAt(4);
        return md5(pass);
    }

    public static String formPassToDbPass(String pass ,String salt){
        pass = ""+salt.charAt(0)+salt.charAt(2) + pass +salt.charAt(5) + salt.charAt(4);
        return md5(pass);
    }

    public static String inputPassToDbPass(String pass,String saltDB){
        return formPassToDbPass(inputPassToFormPass(pass),saltDB);
    }

    public static void main(String[] args) {
        System.out.println(inputPassToDbPass("123456","1a2b3c4d"));
    }

}

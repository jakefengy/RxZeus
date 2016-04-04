package com.xm.zeus.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 作者：小孩子xm on 2016-04-04 12:24
 * 邮箱：1065885952@qq.com
 */
public class Utils {

    private final static String[] md5_StrDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String getMd5(String input) {
        String resultString = null;
        try {
            resultString = new String(input);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteToString(md.digest(input.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }

    // 转换字节数组为16进制字串
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        // System.out.println("iRet="+iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return md5_StrDigits[iD1] + md5_StrDigits[iD2];
    }

}

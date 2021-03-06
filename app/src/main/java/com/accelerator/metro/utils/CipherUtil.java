package com.accelerator.metro.utils;


import android.util.Base64;

/**
 * 加密工具类
 * <p/>
 * Created by Nicholas on 2016/7/17.
 */
public class CipherUtil {

    private CipherUtil() {
        throw new UnsupportedOperationException("Do not need instantiate!");
    }

    /**
     * Base64加密
     *
     * @param phone 手机号
     * @param str   需要加密的内容
     * @return 加密后的字符串
     */
    public static String base64Encode(String phone, String str) {

        int times = Integer.parseInt(phone.substring(0,10)) % 4 + 2;

        for (int i = 0; i < times; i++) {
           str= Base64.encodeToString(str.getBytes(),Base64.DEFAULT);
        }

        return str;
    }

}

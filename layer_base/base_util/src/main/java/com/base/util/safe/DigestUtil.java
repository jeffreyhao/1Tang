package com.base.util.safe;


import com.blankj.utilcode.util.ConvertUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestUtil {


    public static String bytes2HexString(final byte[] bytes) {
        return ConvertUtils.bytes2HexString(bytes);
    }


    public static byte[] hashTemplate(final byte[] data, final String algorithm) {
        if (data == null || data.length == 0) return null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}

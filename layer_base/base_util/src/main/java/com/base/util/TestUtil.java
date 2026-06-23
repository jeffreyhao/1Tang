package com.base.util;

public class TestUtil {


    public static String subValue(String text){
        if (text == null || text.isEmpty() || text.length() < 4) {
            return null;
        }
        return text.substring(4);
    }
}

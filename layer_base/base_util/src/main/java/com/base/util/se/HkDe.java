package com.base.util.se;

import java.io.BufferedReader;
import java.io.FileReader;
;

public class HkDe {

    private static boolean checkFBySymbol() {
        try {
            String maps = readFile("/proc/" + android.os.Process.myPid() + "/maps");
            if (maps.contains("frida") || maps.contains("gum-js-loop") || maps.contains("libfrida")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String readFile(String path) {
        BufferedReader reader;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(path));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }


    private static boolean deX() {
        try {
            Class.forName("de.robv.android.xposed.XposedHelpers");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static boolean dX() {
        try {
            throw new Exception("Xposed Check");
        } catch (Exception e) {
            for (StackTraceElement element : e.getStackTrace()) {
                if (element.getClassName().contains("de.robv.android.xposed")) {
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean check() {
        return checkFBySymbol() || dX() || deX();
    }
}

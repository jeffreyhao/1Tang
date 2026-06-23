package com.xcyh.cheatsheet.app.utils;

import com.base.api.GlobalContext;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Created by haojiangfeng on 2024/11/18.
 */
public class SampleUtil {

    public String read(String fileName){
        try {
            File sampleDataFile = new File(GlobalContext.getContext().getFilesDir(), fileName);
            String json = new String(Files.readAllBytes(sampleDataFile.toPath()), StandardCharsets.UTF_8);
            // 解析JSON数据...

            return json;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


}

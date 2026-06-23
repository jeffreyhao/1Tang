package com.base.api;


import com.base.abs.ISensors;

public class SensorsApi {

    public static ISensors iSensors;


    public static void trackDownloadFileException(String contentLength, String fileLength) {
        if(iSensors != null) {
            iSensors.trackDownloadFileException(contentLength, fileLength);
        }
    }


}

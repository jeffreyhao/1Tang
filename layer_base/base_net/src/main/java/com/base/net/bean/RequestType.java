package com.base.net.bean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

@StringDef({
        RequestType.GET,
        RequestType.POST
})
@Retention(RetentionPolicy.SOURCE)
public @interface RequestType {

    String GET          = "get";

    String POST         = "post";

}

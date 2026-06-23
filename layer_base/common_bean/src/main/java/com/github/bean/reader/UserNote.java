package com.github.bean.reader;

import android.graphics.Rect;

import java.util.concurrent.CopyOnWriteArrayList;

public class UserNote {
    public String noteId;
    public String avatar;
    public String note;
    public int startElement;
    public int endElement;
    public CopyOnWriteArrayList<Rect> areas = new CopyOnWriteArrayList();


}

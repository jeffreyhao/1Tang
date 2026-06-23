package com.benefit.novelverse.event;

import androidx.annotation.Nullable;

import com.github.bean.database.table.BookChapter;

public class PurchaseEvent extends Event<BookChapter>{

    public static int PURCHASE_EVENT_CODE = 10000;

    public PurchaseEvent() {

    }

    public PurchaseEvent(@Nullable BookChapter chapter) {
        super(PURCHASE_EVENT_CODE,99,chapter);
    }
}

package com.github.bean.reader;

import androidx.annotation.Nullable;

import com.github.bean.database.table.ReadTimeRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReadTimeRecordTransferBean {
    @Nullable
    private String book_id = "";
    @Nullable
    private String start_time = "";
    private String end_time = "";
    private int duration = 0;

    public ReadTimeRecordTransferBean() {
    }

    @Nullable
    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(@Nullable String book_id) {
        this.book_id = book_id;
    }

    @Nullable
    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(@Nullable String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    // ==== 静态方法（原 Kotlin companion object） ====
    public static ReadTimeRecordTransferBean transfer(ReadTimeRecord record) {
        ReadTimeRecordTransferBean bean = new ReadTimeRecordTransferBean();
        bean.setBook_id(record.getBid());
        bean.setStart_time(record.getStartTime());
        bean.setDuration(record.getDuration());
        return bean;
    }

    public static List<ReadTimeRecordTransferBean> transferList(@Nullable List<ReadTimeRecord> records) {
        if (records == null || records.isEmpty()) {
            return Collections.emptyList();
        }
        List<ReadTimeRecordTransferBean> list = new ArrayList<>();
        for (ReadTimeRecord item : records) {
            list.add(transfer(item));
        }
        return list;
    }

    @Override
    public String toString() {
        return "ReadTimeRecordTransferBean{" +
                "book_id='" + book_id + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", duration=" + duration +
                '}';
    }
}

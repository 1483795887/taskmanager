package com.cheng.taskmanager.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class DateFactory {
    public static Date getDateFromString(String strDate) {
        Date date = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = new Date(simpleDateFormat.parse(strDate).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }

    public static Date getToday() {
        java.util.Date today = new java.util.Date();
        return new Date(today.getTime());
    }
}

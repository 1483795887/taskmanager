package com.cheng.taskmanager.entity;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class DateFactory {
    public static Date getDateFromString(String strDate){
        Date date = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = new Date(simpleDateFormat.parse(strDate).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }
}

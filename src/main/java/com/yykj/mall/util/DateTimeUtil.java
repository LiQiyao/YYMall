package com.yykj.mall.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Created by Lee on 2017/8/17.
 */
public class DateTimeUtil {

    private final static String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private DateTimeUtil(){

    }

    public static Date stringToDate(String dateTimeString, String format){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(format);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeString);
        return dateTime.toDate();
    }

    public static String dateToString(Date date, String format){
        if (date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(format);
    }

    public static Date stringToDate(String dateTimeString){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeString);
        return dateTime.toDate();
    }

    public static String dateToString(Date date){
        if (date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }
}

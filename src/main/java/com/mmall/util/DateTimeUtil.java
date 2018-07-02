package com.mmall.util;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;

import java.util.Date;

/**
 * Created by txk on 2018/6/4.
 */
public class DateTimeUtil {

    public static final String STANDARDFORMAT="yyyy-MM-dd HH:mm:ss";

    public static String dateToStr(Date date){
        if(date==null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime=new DateTime(date);
        return dateTime.toString(STANDARDFORMAT);
    }

    public static Date strToDate(String date){
        DateTimeFormatter dateTimeFormatter= DateTimeFormat.forPattern(STANDARDFORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(date);
        return dateTime.toDate();
    }

    public static void main(String[] args) {
        Date date = new Date();
        String s = new String("2010-1-1 11:11:11");
        System.out.println(dateToStr(date));
        System.out.println(strToDate(s));

    }
}

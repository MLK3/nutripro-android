package com.oddsix.nutripro.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Filippe on 22/10/16.
 */

public class DateHelper {

    public static String parseDate(String format, Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String formattedDate;
        formattedDate = sdf.format(date);
        return formattedDate;
    }

    public static Date addDay(int days, Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date); // Now use today date.
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

}

package com.nix.dimablyznyuk.student.contacts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Dima Blyznyuk on 20.07.15.
 */
public class MyDateUtils {

    public static final String DATE_FORMAT = "dd / MM / yyyy";

    public static String fromMilliseconds(long millisDate){
            Date date = new Date (millisDate);
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        String newDate = format.format(date);
        return newDate;
    }
    public static long toMilliseconds(String dateString) throws ParseException {
        Date date = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).parse(dateString);
        long milliseconds = date.getTime();
        return milliseconds;
    }
}

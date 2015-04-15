package com.example.akash.fourthestatesample;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by akash on 14/4/15.
 */
public class FlexibleDateParser {
    private List<ThreadLocal<SimpleDateFormat>> threadLocals = new ArrayList<ThreadLocal<SimpleDateFormat>>();
    private ArrayList<String> formats=new ArrayList<String>(Arrays.asList("EEE, dd MMM yyyy HH:mm:ss Z","yyyy-MM-dd HH:mm:ss"));
    private TimeZone timeZone=Calendar.getInstance().getTimeZone();
    public FlexibleDateParser(){
        threadLocals.clear();

        for (final String format : formats) {
            ThreadLocal<SimpleDateFormat> dateFormatTL = new ThreadLocal<SimpleDateFormat>() {
                protected SimpleDateFormat initialValue() {
                    SimpleDateFormat sdf = new SimpleDateFormat(format);
                    sdf.setTimeZone(timeZone);
                    sdf.setLenient(false);
                    return sdf;
                }
            };
            threadLocals.add(dateFormatTL);
        }
    }

    public  Date parseDate(String dateStr)  {
        for (ThreadLocal<SimpleDateFormat> tl : threadLocals) {
            SimpleDateFormat sdf = tl.get();
            try {
                return sdf.parse(dateStr);
            } catch (ParseException e) {
                // Ignore and try next date parser
            }
        }
        // All parsers failed
        return null;
    }

    public static void main(String[] args) {
//        System.out.println(new FlexibleDateParser().parseDate("Tue, 14 Apr 2015 04:41:24 +0000"));
        /*  StringBuffer title=new StringBuffer();
        String s = ",Messages,:Hello.,World,Hobbies,[Java],Programming";
            StringTokenizer stringTokenizer = new StringTokenizer(s, "#$[],");
            while (stringTokenizer.hasMoreTokens()) {
               // if()
                System.out.println(stringTokenizer.nextToken());
                //title.append(stringTokenizer.nextToken());
            }*/
    }
}
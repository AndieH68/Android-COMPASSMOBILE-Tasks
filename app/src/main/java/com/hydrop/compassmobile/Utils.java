package com.hydrop.compassmobile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Panos on 06/05/16.
 */
public class Utils {
    public static boolean isDebugEnabled = false;

    public  static <T> boolean checkNotNull(T object) {
        return object != null;
    }

    public static Date stringToDate(String stringDate){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try{
            Date date = dateFormat.parse(stringDate);
            return date;
        }catch (Exception e){

        }
        return null;
    }

    public static Date getUTCDate(Date date){
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        return calendar.getTime();
    }
    public static String getTaskRef(){
        DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmm");
        return "PDA"+dateFormat.format(getUTCDateNow());

    }

    public static Date getUTCDateNow(){
        return getUTCDate(new Date());
    }

    public static String getUTCStringDateNow(){
        return dateToISOString(getUTCDateNow());
    }

    public static String dateToISOString(Date date){
        if (date == null){
            return null;
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return dateFormat.format(date);

    }

    public static String dateToString(Date date){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);


    }

    public static int stringToInteger(String number){
        if (checkNotNull(number)){
            return Integer.parseInt(number);
        }
        return 0;
    }

    public static Calendar getCalendar(){
        return Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    }

    public static Date startOfDayFromString(String dateString){
        Calendar cal = getCalendar();
        Date date = stringToDate(dateString);
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);

        return cal.getTime();
    }


    public static Date startOfDay(Date date){
        Calendar cal = getCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);

        return cal.getTime();
    }

    public static Date endfDayFromString(String dateString){
        Calendar cal = getCalendar();
        Date date = stringToDate(dateString);

        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY,23);
        cal.set(Calendar.MINUTE,59);
        cal.set(Calendar.SECOND,59);
        cal.set(Calendar.MILLISECOND,59);


        return cal.getTime();
    }

    public static Date endfDay(Date date){
        Calendar cal = getCalendar();

        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY,23);
        cal.set(Calendar.MINUTE,59);
        cal.set(Calendar.SECOND,59);

        return cal.getTime();
    }

    public static Date startOfWeek(Date date){
        Calendar cal = getCalendar();
        Date newDate = startOfDay(date);
        cal.setTime(newDate);
        cal.setFirstDayOfWeek(Calendar.SUNDAY);

        return cal.getTime();

    }
    public static Date endOfWeek(Date date){
        Calendar cal = getCalendar();
        Date newDate = startOfWeek(date);
        cal.setTime(newDate);
        cal.add(Calendar.DAY_OF_WEEK,6);
        cal.setTime(endfDay(cal.getTime()));

        return cal.getTime();
    }

    public static Date startOfMonth(Date date){
        Calendar cal = getCalendar();
        Date newDate = startOfDay(date);
        cal.setTime(newDate);
        cal.set(Calendar.DAY_OF_MONTH,1);
        return cal.getTime();

    }

    public static Date endOfMonth(Date date){
        Calendar cal = getCalendar();
        Date newDate = endfDay(date);
        cal.setTime(newDate);

        cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();

    }

    public static Date startOfNextMonth(Date date){
        Calendar cal = getCalendar();
        Date newDate = startOfMonth(date);
        cal.setTime(newDate);
        cal.add(Calendar.MONTH,1);

        return cal.getTime();

    }

    public static Date endOfNextMonth(Date date){
        Calendar cal = getCalendar();
        Date newDate = endOfMonth(date);
        cal.setTime(newDate);
        cal.add(Calendar.MONTH,1);

        return cal.getTime();

    }







}

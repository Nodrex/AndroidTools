package com.nodrex.android.tools;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Time {

    public static final String UTC = "UTC";

    /**
     * Contains time formats.
     */
    public enum Format{
        Long,
        Short
    }

    public static int getYear(){
        Calendar c = Calendar.getInstance();
        if(c == null) return 0;
        c.setTimeInMillis(System.currentTimeMillis());
        return c.get(Calendar.YEAR);
    }

    public static int getMonth(){
        Calendar c = Calendar.getInstance();
        if(c == null) return 0;
        c.setTimeInMillis(System.currentTimeMillis());
        return c.get(Calendar.MONTH);
    }

    /**
     *
     * @return day of month.
     */
    public static int getDay(){
        Calendar c = Calendar.getInstance();
        if(c == null) return 0;
        c.setTimeInMillis(System.currentTimeMillis());
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     *
     * @return hour of day.
     */
    public static int getHour(){
        Calendar c = Calendar.getInstance();
        if(c == null) return 0;
        c.setTimeInMillis(System.currentTimeMillis());
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(){
        Calendar c = Calendar.getInstance();
        if(c == null) return 0;
        c.setTimeInMillis(System.currentTimeMillis());
        return c.get(Calendar.MINUTE);
    }

    public static int getSecond(){
        Calendar c = Calendar.getInstance();
        if(c == null) return 0;
        c.setTimeInMillis(System.currentTimeMillis());
        return c.get(Calendar.SECOND);
    }

    /**
     * Generates current device time zone.
     * @param format {@link Format} Long or Short. Long means in milliseconds and Short will return one integer number. for georgia example: +4
     * @return timZoneOffsets
     */
    public static long getTimeZone(Format format) {
        TimeZone tz = TimeZone.getDefault();
        Date now = new Date();
        long timeZone = tz.getOffset(now.getTime());
        switch (format){
            case Long: return timeZone;
            case Short:
            default: return TimeUnit.MILLISECONDS.toHours(timeZone);
        }
    }

    /**
     * Generates current device time zone in milliseconds.
     * @return timZoneOffsets
     */
    public static long getTimeZone() {
        return getTimeZone(Format.Long);
    }

    /**
     * Generates utc date with given time, for example 23,59,59
     * @param hour
     * @param minute
     * @param second
     * @return utc date with given hour, minute and second
     */
    public static long getUTCDate(int hour, int minute, int second){
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone(UTC));
        c.setTimeInMillis(System.currentTimeMillis());
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        c.set(year,month,day,hour,minute,second);
        return c.getTimeInMillis();
    }

    /**
     * Generates utc date with given date time, for example: 2016,7,26,23,59,59
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @return utc date with given date time.
     */
    public static long getUTC(int year, int month, int day,int hour, int minute, int second){
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone(UTC));
        c.set(year,month,day,hour,minute,second);
        return c.getTimeInMillis();
    }

    /**
     * @return just UTC date without hour, minute and second. It menas that hour,minute and second are 0.
     */
    public static long getUTCDate(){
        return getUTCDate(0,0,0);
    }

    /**
     * @param withTimeZone if true includes ime zone, if false does no includes.
     * @return UTC Date with 0 hour, minute and second and includes current device time zone, means current device's year, month, day depend on device's timezone.
     */
    public static long getUTCDate(boolean withTimeZone){
        if(!withTimeZone) return getUTCDate();
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getDefault());
        c.setTimeInMillis(System.currentTimeMillis());
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone(UTC));
        c.set(year,month,day,0,0,0);
        return c.getTimeInMillis();
    }

    /**
     *
     * @param year
     * @param month
     * @param day
     * @return utc date with given year, month and day, without hour, minute and second.
     */
    public static long getUTC(int year, int month, int day){
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone(UTC));
        c.set(year,month,day,0,0,0);
        return c.getTimeInMillis();
    }

}

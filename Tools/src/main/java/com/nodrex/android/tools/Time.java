package com.nodrex.android.tools;

import android.app.Activity;
import android.content.res.Resources;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Time {

    public static final String UTC = "UTC";
    private static int [] dateTime = new int[7];//creates only once and then fills when ever needed. is used for pix to load time more faster.
    private static Calendar c = new GregorianCalendar();//GregorianCalendar is created only once (optimization for pix).

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
     * Generates current device time zone in hours.
     * @return timZoneOffsets
     */
    public static long getTimeZoneGMT() {
        TimeZone tz = TimeZone.getDefault();
        long timeZone = tz.getOffset(new Date().getTime());
        return TimeUnit.MILLISECONDS.toHours(timeZone);
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
     * Coverts given time in to human readable string time of hour:minute
     * @param time witch should be converted.
     * @return string time in format => hour(24):minute
     */
    public static String getTimeHHmm(long time){
        return new SimpleDateFormat("HH:mm").format(time);
    }

    /**
     * Coverts given original time in to human readable string time of houe:minute:second.
     * @param time witch should be converted.
     * @return string time in format => hour(24):minute:second
     */
    public static String getTimeHHmmss(long time){
        return new SimpleDateFormat("HH:mm:ss").format(time);
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

    /**
     * @return UTC Date with 0 hour, minute and second and includes current device time zone.
     */
    public static long getUTCDateWithTimeZone(){
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
     * Generates UTC Date with given month and day, with 0 hour, minute and second and includes current device time zone.
     * @param month
     * @param day
     * @return UTC Date with 0 hour, minute and second and includes current device time zone.
     */
    public static long getUTCDateWithTimeZone(int month,int day){
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getDefault());
        c.setTimeInMillis(System.currentTimeMillis());
        int year = c.get(Calendar.YEAR);

        c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone(UTC));
        c.set(year,month,day,0,0,0);
        return c.getTimeInMillis();
    }


    /**
     * converts long time to array fo human readable time like hour,minute,year,month,day of month, second, day of week.
     * @param time to convert really human readable time
     * @return array of hour, minute, year, month, day, second, day of week.
     */
    public static int[] getTime(long time){
        c.setTimeInMillis(time);
        dateTime[0] = c.get(Calendar.HOUR_OF_DAY);
        dateTime[1] = c.get(Calendar.MINUTE);
        dateTime[2] = c.get(Calendar.YEAR);
        dateTime[3] = c.get(Calendar.MONTH);
        dateTime[4] = c.get(Calendar.DAY_OF_MONTH);
        dateTime[5] = c.get(Calendar.SECOND);
        dateTime[6] = c.get(Calendar.DAY_OF_WEEK);
        return dateTime;
    }

    /**
     * Reads String day value from resources depend on int day value.
     * </br>Returned value depends on locale(Language) configuration of app,
     * </br>Value is read from ge if language is georgian and so on...
     * @param activity
     * @param day
     * @return String representation of day from int value.
     */
    public static String getDayOfWeek(Activity activity, int day){
        if(activity == null) return null;
        Resources resources = Util.getResources(activity);
        if(resources == null) return null;
        String [] days = resources.getStringArray(R.array.daysOfWeek);
        if(days == null) return null;
        switch (day){
            case Calendar.MONDAY: return days[0];
            case Calendar.TUESDAY: return days[1];
            case Calendar.WEDNESDAY: return days[2];
            case Calendar.THURSDAY: return days[3];
            case Calendar.FRIDAY: return days[4];
            case Calendar.SATURDAY: return days[5];
            case Calendar.SUNDAY: return days[6];
        }
        return null;
    }

    /**
     * Reads String month value from resources depend on int month value.
     * </br>Returned value depends on locale(Language) configuration of app,
     * </br>Value is read from ge if language is georgian and so on...
     * @param activity
     * @param month
     * @param shortMonths
     * @return String representation of month from int value.
     */
    public static String getMonth(Activity activity,int month,boolean shortMonths){
        if(activity == null) return null;
        Resources resources = Util.getResources(activity);
        if(resources == null) return null;
        String [] months = resources.getStringArray(shortMonths ? R.array.monthsShort : R.array.monthsLong);
        if(months == null) return null;
        switch (month){
            case Calendar.JANUARY: return months[0];
            case Calendar.FEBRUARY: return months[1];
            case Calendar.MARCH: return months[2];
            case Calendar.APRIL: return months[3];
            case Calendar.MAY: return months[4];
            case Calendar.JUNE: return months[5];
            case Calendar.JULY: return months[6];
            case Calendar.AUGUST: return months[7];
            case Calendar.SEPTEMBER: return months[8];
            case Calendar.OCTOBER: return months[9];
            case Calendar.NOVEMBER: return months[10];
            case Calendar.DECEMBER: return months[11];
        }
        return null;
    }

    /**
     * Class that represents device's current day.
     */
    public static class ToDay{

        /**
         * @return device's current day of month.
         */
        public static int getDay(){
            Calendar cal = Calendar.getInstance();
            return cal.get(Calendar.DAY_OF_MONTH);
        }

        /**
         * @return device's current month.
         */
        public static int getMonth(){
            Calendar cal = Calendar.getInstance();
            return cal.get(Calendar.MONTH);
        }
    }

}

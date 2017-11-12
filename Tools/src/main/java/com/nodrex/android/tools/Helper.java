package com.nodrex.android.tools;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * Created by nchum on 11/8/2017.
 */
public class Helper {

    public static final String EMPTY_STRING = "";
    public static final String MD5 = "MD5";
    public static final String UTF8 = "UTF-8";
    public static final String SHA1 = "SHA-1";
    public static final String NEW_LINE = "\n";
    public static final String NEW_LINE_AND_TAB = "\n\t";
    public static final String SPACE = " ";

    /**
     * Extracts ip from host name like google.com
     * @param hostName
     * @return ip of hostName or null , if UnknownHostException occurred or hostName is null.
     */
    public static String resolveIp(String hostName){
        if(hostName == null) return null;
        try {
            InetAddress address = InetAddress.getByName(hostName);
            return address.getHostAddress();
        } catch (UnknownHostException e) {
            Util.log("can not resolveIp of " + hostName + " :" + e.toString());
        }
        return null;
    }

    /**
     * Converts number less then 10 in to String number concatenated with 0 before number, like 01,02,03...</br>
     * and number more then 10 is just converted in to String.
     * @param num
     * @return String number.
     */
    public static String format02(int num) {
        if (num < 10) return "0"+num;
        return num + Helper.EMPTY_STRING;
    }

    public static boolean isEmptyOrNull(String value){
        return value == null || "".equals(value) ;
    }

    /**
     * generates random number between 0 to max
     * @param max maximum random number which can be returned from this method.
     * @return random number between 0 to max
     */
    public static int generateRandom(int max){
        Random rn = new Random();
        int range = max + 1;
        return  rn.nextInt(range);
    }

}

package com.ireader.plug.tools;

import java.util.Date;
import java.util.TimeZone;

/**
 * 类说明：检查两个时间戳是否在同一天
 */

public class TimeUtil {

    public static final int     SECONDS_PER_DAY             = 3600*24;    //每天多少秒
    public static final long    MILLIS_PER_DAY              = 1000L * SECONDS_PER_DAY;  //每天多少毫秒


    /**
     * 两个时间戳是否为同一天
     * @param ms1
     * @param ms2
     * @return
     */
    public static boolean isSameDayOfMillis(final long ms1, final long ms2) {
        final long interval = ms1 - ms2;
        return interval < MILLIS_PER_DAY
                && interval > -1L * MILLIS_PER_DAY
                && toDay(ms1) == toDay(ms2);
    }

    private static long toDay(long millis) {
        return (millis + TimeZone.getDefault().getOffset(millis)) / MILLIS_PER_DAY;
    }

    /**
     *得到当前的时间戳
     *
     * @return the long
     */
    public static long getCurMillis(){
        Date date= new Date();
        return date.getTime();
    }
}

package com.aashir.bpc.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateUtils;

public class Utils {

    private final int SECOND = 1000;
    private final int MINUTE = 60 * SECOND;
    private final int HOUR = 60 * MINUTE;
    private final int DAY = 24 * HOUR;
    private String days = " Days ";
    private String hour = " Hours ";
    private String minute = " Minutes ";

    private StringBuilder mBuilder = new StringBuilder();

    public static boolean gotInternet(Context c) {
        if (c != null) {
            ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        } else {
            return false;
        }
    }

    public static String getRelativeTimeLong(long no) {
        return DateUtils.getRelativeTimeSpanString(no, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE | DateUtils.FORMAT_SHOW_YEAR).toString();
    }

    public String getRelativeTime(long oldTime) {
        mBuilder.setLength(0);
        long nowTime = System.currentTimeMillis();

        int days = (int) ((nowTime - oldTime) / DateUtils.DAY_IN_MILLIS);
        int minutes = (int) ((nowTime - oldTime) / DateUtils.MINUTE_IN_MILLIS);
        int hours = minutes / 60;
        if (days == 0) {

            if (minutes > 60) {
                if (hours >= 1 && hours <= 24) {
                    mBuilder.append(hours).append("h");
                }
            } else {

                int second = (int) ((nowTime - oldTime) / DateUtils.SECOND_IN_MILLIS);

                if (second <= 30) {
                    mBuilder.append("Now");
                } else if (second > 30 && second <= 60) {
                    mBuilder.append(second).append("s");
                } else if (second >= 60 && minutes <= 60) {
                    mBuilder.append(minutes).append("m");
                }
            }

        } else {
            if (days <= 50) {
                mBuilder.append(days).append("d");
            } else {
                int weeks = days / 7;
                mBuilder.append(weeks).append("w");
            }
        }
        return mBuilder.toString();
    }

    public String getAssignmentTime(long ms) {
        mBuilder.setLength(0);

        if (ms > DAY) {
            if ((ms / DAY) == 1) days = " Day ";
            mBuilder.append(ms / DAY).append(days);
            ms %= DAY;
        } else {
            if (ms > HOUR) {
                if ((ms / HOUR) == 1) hour = " Hour ";
                mBuilder.append(ms / HOUR).append(hour);
                ms %= HOUR;
            }
            if (ms > MINUTE) {
                if ((ms / MINUTE) == 1) minute = " Minute ";
                mBuilder.append(ms / MINUTE).append(minute);
                ms %= MINUTE;
            }
        }

        if (ms > HOUR) {
            if ((ms / HOUR) == 1) hour = " Hour ";
            mBuilder.append(ms / HOUR).append(hour);
        }

        return mBuilder.toString();
    }

}

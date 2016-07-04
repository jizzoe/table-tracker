package com.swingtech.app.tabletracker.service.util;

import org.joda.time.Interval;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class DateTimeUtil {
	public static Long getTimeDifferenceMilis(LocalDateTime startDateTime, LocalDateTime endDateTime) {
		Interval interval = new Interval(startDateTime.toDateTime(), endDateTime.toDateTime());
		
		return interval.toDurationMillis();
	}

	public static String getTimeDifferenceDisplayString(LocalDateTime startDateTime, LocalDateTime endDateTime) {
		Long interval = getTimeDifferenceMilis(startDateTime, endDateTime);
		
		return getDurationString(interval);
	}
	
    public static String getDurationString(long mili) {
        double time = 0;

        if (mili < 1000) {
            time = mili;
            time = decimalRound(time, 2);
            return Double.toString(time) + " mili seconds";
        }

        if (mili < 60000) {
            time = mili / 1000;
            time = decimalRound(time, 2);
            return Double.toString(time) + " seconds";
        }

        if (mili < 3600000) {
            time = mili / 60000;
            time = decimalRound(time, 2);
            return Double.toString(time) + " minutes";
        }

        if (mili < 86400000) {
            time = mili / 3600000;
            time = decimalRound(time, 2);
            return Double.toString(time) + " hours";
        }

        if (mili >= 86400000) {
            time = mili / 86400000;
            time = decimalRound(time, 2);
            return Double.toString(time) + " days";
        }

        time = decimalRound(time, 2);
        return Double.toString(time) + " days";
    }

    public static double decimalRound(double value, int roundPlaces) {
    	int temp=(int)((value*Math.pow(10,roundPlaces)));
    	return (((double)temp)/Math.pow(10,roundPlaces));
    }
    
}

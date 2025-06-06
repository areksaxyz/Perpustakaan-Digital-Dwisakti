package utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    public static long daysBetween(Date start, Date end) {
        long diff = end.getTime() - start.getTime();
        return diff / (1000 * 60 * 60 * 24);
    }
}
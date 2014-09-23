package net.joesteele.tissue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by joesteele on 5/30/14.
 */
public class DateHelper {
  public static final long SECOND_IN_MILLIS = 1000;
  public static final long MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60;
  public static final long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60;
  public static final long DAY_IN_MILLIS = HOUR_IN_MILLIS * 24;
  public static final long WEEK_IN_MILLIS = DAY_IN_MILLIS * 7;

  public static String getRelativeTimeSpanString(long time) {
    long now = System.currentTimeMillis();
    long duration = Math.abs(now - time);
    long count;
    String countFormat;
    if (duration < MINUTE_IN_MILLIS) {
      count = duration / SECOND_IN_MILLIS;
      if (count == 1) {
        return "1 second ago";
      }
      countFormat = "%d seconds ago";
    } else if (duration < HOUR_IN_MILLIS) {
      count = duration / MINUTE_IN_MILLIS;
      if (count == 1) {
        return "1 minute ago";
      }
      countFormat = "%d minutes ago";
    } else if (duration < DAY_IN_MILLIS) {
      count = duration / HOUR_IN_MILLIS;
      if (count == 1) {
        return "1 hour ago";
      }
      countFormat = "%d hours ago";
    } else if (duration < WEEK_IN_MILLIS) {
      count = duration / DAY_IN_MILLIS;
      if (count == 1) {
        return "1 day ago";
      }
      countFormat = "%d days ago";
    } else {
      count = duration / WEEK_IN_MILLIS;
      if (count == 1) {
        return "1 week ago";
      }
      countFormat = "%d weeks ago";
    }

    return String.format(countFormat, count);
  }

  public static String getRelativeTimeSpanString(String timestamp) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    try {
      Date date = format.parse(timestamp);
      return getRelativeTimeSpanString(date.getTime());
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return timestamp;
  }
}

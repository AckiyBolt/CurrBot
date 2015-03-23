package ua.bolt.twitterbot.execution;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import ua.bolt.twitterbot.prop.Names;
import ua.bolt.twitterbot.prop.PropertyHolder;

import java.util.TimeZone;

/**
 * Created by ackiybolt on 23.03.15.
 */
public class TimeUtil {

    public static int hoursTillEndOfWeek() {
        DateTime now = getNow();

        int result = hoursTillEndOfDay();
        result += (7 - 3 - now.dayOfWeek().get()) * 24;

        if(result < 0) {
            result = 0;
        }

        return result;
    }

    public static int hoursTillEndOfDay() {
        DateTime now = getNow();

        int result = 24 - 4 - now.hourOfDay().get();

        if(result < 0) {
            result = 0;
        }

        return result;
    }

    public static DateTime getNow() {
        return DateTime.now().withZone(
                DateTimeZone.forTimeZone(
                        TimeZone.getTimeZone(PropertyHolder.INSTANCE.getStr(Names.timezone))));
    }
}

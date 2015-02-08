package ua.bolt.twitterbot.miner;

/**
 * Created by ackiybolt on 25.12.14.
 */
public class Util {

    public static double formatDouble(double val) {
        double tmp = Math.pow(10, 2);
        return Math.round(val * tmp) / tmp;
    }

    public static double parseAndFormatDouble(String value) {
        double val = Double.valueOf(value);
        return formatDouble(val);
    }

    public static double parseDivideAndFormatDouble(String value, double divider) {
        double val = Double.valueOf(value);
        return formatDouble(val / divider);
    }

    public static boolean isExist(String str) {
        return str != null && str.length() > 2;
    }
}

package ua.bolt.twitterbot;

/**
 * Created by ackiybolt on 22.12.14.
 */
public class Constants {


    public static final boolean IS_DEBUUG = false;


    public static final String INTERBANK_MARKET_URL = "http://minfin.com.ua/currency/mb/";
    public static final String BLACK_MARKET_URL = "http://resources.finance.ua/chart/data?for=currency-order&currency=";

    public static final String TABLE_KEY_MAR = "Рыночные курсы";
    public static final String TABLE_KEY_OFF = "Курсы от банков";

    public static final String TIMEZONE_ID = "Europe/Kiev";

    public static final String CACHE_FILE_INTERBANK         = "/home/curr-bot/cache_interbank";
    public static final String CACHE_FILE_INTERBANKMARKET   = "/home/curr-bot/cache_interbank_market";
    public static final String CACHE_FILE_BLACKMARKET       = "/home/curr-bot/cache_blackmarket";
    public static final String CACHE_FILE_NBU               = "/home/curr-bot/cache_nbu";


    public static final int RATE_UPDATING_PERIOD_INTERBANK      = 15 * 60 * 1000; // 15 mins

    public static final int RATE_UPDATING_PERIOD_BLACKMARKET    = 60 * 60 * 1000; // 30 mins
    public static final int RATE_SLEEPING_PERIOD                = 60 * 60 * 1000; // 1 hour

    public static final int CRASHES_LIMIT = 3;
}
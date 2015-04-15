package ua.bolt.twitterbot.print.service;

import ua.bolt.twitterbot.execution.cache.CacheHolder;
import ua.bolt.twitterbot.print.printer.ConsolePrinter;
import ua.bolt.twitterbot.print.printer.Printer;
import ua.bolt.twitterbot.print.printer.TwitterPrinter;
import ua.bolt.twitterbot.prop.Names;
import ua.bolt.twitterbot.prop.PropertyHolder;

/**
 * Created by ackiybolt on 22.03.15.
 */
public abstract class AbstractPrintingService {

    protected final boolean IS_DEBUG = PropertyHolder.INSTANCE.getBool(Names.debug);

    protected Printer printer;

    public AbstractPrintingService() {
        this.printer = IS_DEBUG ? new ConsolePrinter() : new TwitterPrinter();
    }

    public abstract void printMarket(CacheHolder cacheHolder);



    // makes val size == 5
    protected String spacerAddingIfNeeded (Double val) {

        StringBuilder sb = new StringBuilder(val.toString());

        // before dot
        if (val < 1)
            sb.insert(0, "0");

        // after dot
        if (sb.length() < 5)
            sb.append("0");

        return sb.toString();
    }
}

package ua.bolt.twitterbot;

import twitter4j.Logger;
import ua.bolt.twitterbot.miner.BlackMarketMiner;
import ua.bolt.twitterbot.miner.InterbankOffMiner;
import ua.bolt.twitterbot.miner.NbuMiner;
import ua.bolt.twitterbot.print.AbstractPrinter;
import ua.bolt.twitterbot.print.ConsolePrinter;
import ua.bolt.twitterbot.print.TwitterPrinter;
import ua.bolt.twitterbot.prop.Names;
import ua.bolt.twitterbot.prop.PropertyHolder;
import ua.bolt.twitterbot.worker.Morpheus;
import ua.bolt.twitterbot.worker.Worker;

/**
 * Created by ackiybolt on 19.12.14.
 */

public class Main {

    private static Logger LOG = Logger.getLogger(Main.class);

    static {
        LOG.info("Init properties...");
        PropertyHolder.INSTANCE.load();
    }

    public static final AbstractPrinter printer = PropertyHolder.INSTANCE.getBool(Names.debug) ?
            new ConsolePrinter() : new TwitterPrinter();

    public static void main(String[] args) {

        LOG.info("Bot starting...");

        startThread(createBlackMarketWorker());
        startThread(createInterbankWorker());
        startThread(createNbuWorker());

        LOG.info("Threads has been started.");
    }

    private static void startThread(Worker worker) {
        if (worker != null)
            new Thread(worker).start();
    }

    private static Worker createBlackMarketWorker() {
        return new Worker(
                new BlackMarketMiner(),
                printer,
                new CacheManager(PropertyHolder.INSTANCE.getStr(Names.file_blackmarket)),
                new Morpheus(
                    1, // mo
                    7, // su
                    6,
                    23,
                    PropertyHolder.INSTANCE.getInt(Names.long_period),
                    PropertyHolder.INSTANCE.getInt(Names.long_period)
                ));
    }

    private static Worker createInterbankWorker() {
        return new Worker(
                new InterbankOffMiner(),
                printer,
                new CacheManager(PropertyHolder.INSTANCE.getStr(Names.file_interbank)),
                new Morpheus(
                        1, // mo
                        5, // fr
                        10,
                        17,
                        PropertyHolder.INSTANCE.getInt(Names.short_period),
                        PropertyHolder.INSTANCE.getInt(Names.long_period)
                ));
    }

    private static Worker createNbuWorker() {
        return new Worker(
                new NbuMiner(),
                printer,
                new CacheManager(PropertyHolder.INSTANCE.getStr(Names.file_interbank)),
                new Morpheus(
                        1, // mo
                        5, // fr
                        12,
                        14,
                        PropertyHolder.INSTANCE.getInt(Names.short_period),
                        PropertyHolder.INSTANCE.getInt(Names.long_period)
                ));
    }
}

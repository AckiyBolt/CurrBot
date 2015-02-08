package ua.bolt.twitterbot;

import twitter4j.Logger;
import ua.bolt.twitterbot.miner.BlackMarketMiner;
import ua.bolt.twitterbot.miner.InterbankMarketMiner;
import ua.bolt.twitterbot.miner.InterbankOffMiner;
import ua.bolt.twitterbot.miner.NbuMiner;
import ua.bolt.twitterbot.print.AbstractPrinter;
import ua.bolt.twitterbot.print.ConsolePrinter;
import ua.bolt.twitterbot.print.TwitterPrinter;
import ua.bolt.twitterbot.worker.Morpheus;
import ua.bolt.twitterbot.worker.Worker;

/**
 * Created by ackiybolt on 19.12.14.
 */

public class Main {

    public static final AbstractPrinter printer = Constants.IS_DEBUUG ? new ConsolePrinter() : new TwitterPrinter();
    private static Logger LOG = Logger.getLogger(Main.class);

    public static void main(String[] args) {

        LOG.info("Bot starting...");

        startThread(createBlackMarketWorker());
        startThread(createInterbankWorker());
        //startThread(createInterbankMarketWorker());
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
                new CacheManager(Constants.CACHE_FILE_BLACKMARKET),
                new Morpheus(
                    1, // mo
                    7, // su
                    6,
                    23,
                    Constants.RATE_UPDATING_PERIOD_BLACKMARKET,
                    Constants.RATE_SLEEPING_PERIOD
                ));
    }

    private static Worker createInterbankWorker() {
        return new Worker(
                new InterbankOffMiner(),
                printer,
                new CacheManager(Constants.CACHE_FILE_INTERBANK),
                new Morpheus(
                        1, // mo
                        5, // fr
                        10,
                        17,
                        Constants.RATE_UPDATING_PERIOD_INTERBANK,
                        Constants.RATE_SLEEPING_PERIOD
                ));
    }

    private static Worker createInterbankMarketWorker() {
        return new Worker(
                new InterbankMarketMiner(),
                printer,
                new CacheManager(Constants.CACHE_FILE_INTERBANKMARKET),
                new Morpheus(
                        1, // mo
                        5, // fr
                        10,
                        17,
                        Constants.RATE_UPDATING_PERIOD_INTERBANK,
                        Constants.RATE_SLEEPING_PERIOD
                ));
    }

    private static Worker createNbuWorker() {
        return new Worker(
                new NbuMiner(),
                printer,
                new CacheManager(Constants.CACHE_FILE_NBU),
                new Morpheus(
                        1, // mo
                        5, // fr
                        12,
                        14,
                        Constants.RATE_UPDATING_PERIOD_INTERBANK,
                        Constants.RATE_SLEEPING_PERIOD
                ));
    }
}

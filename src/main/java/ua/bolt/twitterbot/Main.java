package ua.bolt.twitterbot;

import twitter4j.Logger;
import ua.bolt.twitterbot.miner.BlackMarketMiner;
import ua.bolt.twitterbot.miner.InterbankMarketMiner;
import ua.bolt.twitterbot.miner.InterbankOffMiner;
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

    private static volatile boolean isAlive = Constants.IS_DEBUUG ? false : true;

    public static void main(String[] args) {

        Worker blackMarket       = null;
        Worker interbankOff      = null;
        Worker interbankMarket   = null;

//        do {
//
//            try {
                if (blackMarket == null || (!blackMarket.isAlive && !Constants.IS_DEBUUG)) {
                    blackMarket = createBlackMarketWorker();
                    new Thread(blackMarket).start();
                }

                if (interbankOff == null || (!interbankOff.isAlive && !Constants.IS_DEBUUG)) {
                    interbankOff = createInterbankWorker();
                    new Thread(interbankOff).start();
                }

                if (interbankMarket == null || (!interbankMarket.isAlive && !Constants.IS_DEBUUG)) {
                    interbankMarket = createInterbankMarketWorker();
                    new Thread(interbankMarket).start();
                }

//                Thread.sleep(10*60*1000);
//
//            } catch (Exception ex) {
//                isAlive = false;
//            }
//
//        } while(isAlive);
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
}

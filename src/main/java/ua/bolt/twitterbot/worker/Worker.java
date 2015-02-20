package ua.bolt.twitterbot.worker;

import twitter4j.Logger;
import ua.bolt.twitterbot.CacheManager;
import ua.bolt.twitterbot.prop.Names;
import ua.bolt.twitterbot.prop.PropertyHolder;
import ua.bolt.twitterbot.domain.Market;
import ua.bolt.twitterbot.miner.Miningable;
import ua.bolt.twitterbot.print.AbstractPrinter;
import ua.bolt.twitterbot.print.ConsolePrinter;
import ua.bolt.twitterbot.print.TwitterPrinter;

/**
 * Created by ackiybolt on 03.01.15.
 */
public class Worker implements Runnable {

    private static Logger LOG = Logger.getLogger(Worker.class);

    private int crashes = 0;
    private final boolean IS_DEBUG = PropertyHolder.INSTANCE.getBool(Names.debug);
    private final int crashesLimit = IS_DEBUG ?
            1 :
            PropertyHolder.INSTANCE.getInt(Names.crash_limit);
    public volatile boolean isAlive = !IS_DEBUG;

    private AbstractPrinter printer;
    private Miningable miner;
    private CacheManager cacheManager;
    private Morpheus morpheus;

    private Market previousMarket;


    public Worker(Miningable miner, AbstractPrinter printer, CacheManager cacheManager, Morpheus morpheus) {
        this.miner = miner;
        this.printer = printer;
        this.cacheManager = cacheManager;
        this.morpheus = morpheus;
    }

    @Override
    public void run() {
        while (++crashes <= crashesLimit) {

            try {
                touchCache();
                gotoTheLoop();

            } catch (Exception ex) {
                LOG.error(
                        String.format(
                                "Crash has a place! Attempt %s/%s.", crashes, crashesLimit)
                        , ex);

            } finally {
                if (crashes > crashesLimit) {
                    LOG.info("Worker die due to crashes limit.");
                }
            }
        }
    }

    public void touchCache () throws Exception {

//        LOG.info("Reading cached market...");
        previousMarket = cacheManager.readCache();

        if (previousMarket != null)
            LOG.info("Success. Previous market initialized as:\n" + previousMarket);
        else
            LOG.warn("Can't read cache. Previous market initialized as null");
    }

    public void gotoTheLoop () throws Exception {
        do {

            Market market = miner.mineRates();

            if (market != null && market.isFull()) {
                printMarket(market);

                previousMarket = market;
                cacheManager.updateCache(market);
            }

            if (isAlive)
                morpheus.gotoSleep();


        } while(isAlive);

        LOG.info("Loop ends because thread marked as dead.");
    }


    private void printMarket(Market market) {

        if (market.equals(previousMarket))
            LOG.info("Market does not change. Skipped.");
        else
            printer.print(market, previousMarket);
    }
}

package ua.bolt.twitterbot.execution.agent;

import twitter4j.Logger;
import ua.bolt.twitterbot.domain.Market;
import ua.bolt.twitterbot.domain.MarketType;
import ua.bolt.twitterbot.execution.ScheduleChecker;
import ua.bolt.twitterbot.print.service.CurrentPrintingService;
import ua.bolt.twitterbot.execution.cache.CacheHolder;
import ua.bolt.twitterbot.miner.Miningable;
import ua.bolt.twitterbot.miner.ex.MinerEmptyException;

/**
 * Created by ackiybolt on 12.03.15.
 */
public class CurrentAgent implements Runnable {

    private static Logger LOG = Logger.getLogger(CurrentAgent.class);

    private MarketType marketType;
    private Miningable miner;
    private CacheHolder cache;
    private ScheduleChecker scheduleChecker;
    private CurrentPrintingService printingService;

    public CurrentAgent(Miningable miner, CacheHolder cache) {
        this.miner = miner;
        this.cache = cache;
        this.marketType = miner.getType();
        this.scheduleChecker = scheduleChecker.INSTANCE;
        this.printingService = new CurrentPrintingService(marketType);
    }

    @Override
    public void run() {
        try {
            if (scheduleChecker.isTimeForWork(miner.getType())) {

                Market market = miner.mineRates();
                cache.updateCurrentMarket(market);

                Market previousMarket = cache.getPrevious(marketType);

                if (!market.equals(previousMarket)) {
                    printingService.printMarket(cache);
                    cache.updatePreviousMarket(market);
                } else {
                    LOG.info("Market " + marketType + " does not change. Skipping.");
                }
            }

        } catch (MinerEmptyException ex) {
            LOG.warn("Market " + marketType + " is empty.");
        }
    }
}

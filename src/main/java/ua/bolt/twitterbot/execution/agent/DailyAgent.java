package ua.bolt.twitterbot.execution.agent;

import ua.bolt.twitterbot.execution.cache.CacheHolder;
import ua.bolt.twitterbot.print.service.DailyPrintingService;

/**
 * Created by ackiybolt on 23.03.15.
 */
public class DailyAgent implements Runnable {

    private CacheHolder cache;
    private DailyPrintingService printingService;

    public DailyAgent(CacheHolder cache) {
        this.cache = cache;
        printingService = new DailyPrintingService();
    }

    @Override
    public void run() {
        printingService.printMarket(cache);
        cache.updateDailyMarkets();
    }
}

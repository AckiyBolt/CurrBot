package ua.bolt.twitterbot.execution.agent;

import ua.bolt.twitterbot.execution.cache.CacheHolder;
import ua.bolt.twitterbot.print.service.DailyPrintingService;
import ua.bolt.twitterbot.print.service.WeaklyPrintingService;

/**
 * Created by ackiybolt on 23.03.15.
 */
public class WeeklyAgent implements Runnable {

    private CacheHolder cache;
    private WeaklyPrintingService printingService;

    public WeeklyAgent(CacheHolder cache) {
        this.cache = cache;
        printingService = new WeaklyPrintingService();
    }

    @Override
    public void run() {
        printingService.printMarket(cache);
        cache.updateWeeklyMarkets();
    }
}

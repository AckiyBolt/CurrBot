package ua.bolt.twitterbot.print.service;

import ua.bolt.twitterbot.domain.Market;
import ua.bolt.twitterbot.domain.MarketType;
import ua.bolt.twitterbot.execution.cache.CacheHolder;

/**
 * Created by ackiybolt on 22.03.15.
 */
public class DailyPrintingService extends AbstractDeltaPrintingService {

    private static final String CURR_FORMAT = "%s - %s %s";

    private static final String MSG_TEMPLATE =
            "Дневная дельта (%s)\n" +   // type
                    "%s\n" +            // curr sign digits
                    "%s\n" +            // curr sign digits
                    "%s\n" +            // curr sign digits
                    "%s";               // tags


    @Override
    protected Market getPreviousMarket(CacheHolder cacheHolder, MarketType type) {
        return cacheHolder.getPreviousDay(type);
    }

    @Override
    protected String getMessageTemplate() {
        return MSG_TEMPLATE;
    }

    @Override
    protected String getCurrencyTemplate() {
        return CURR_FORMAT;
    }
}

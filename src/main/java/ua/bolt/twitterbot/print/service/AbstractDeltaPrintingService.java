package ua.bolt.twitterbot.print.service;

import ua.bolt.twitterbot.domain.Market;
import ua.bolt.twitterbot.domain.MarketType;
import ua.bolt.twitterbot.domain.RatePair;
import ua.bolt.twitterbot.execution.cache.CacheHolder;
import ua.bolt.twitterbot.miner.Util;

/**
 * Created by ackiybolt on 23.03.15.
 */
public abstract class AbstractDeltaPrintingService extends AbstractPrintingService {

    @Override
    public void printMarket(CacheHolder cacheHolder) {

        for (MarketType type: MarketType.values()) {

            Market current = cacheHolder.getCurrent(type);
            Market previous = getPreviousMarket(cacheHolder, type);

            String message = createMessage(current, previous);

            printer.print(message);
        }
    }

    private String createMessage(Market current, Market previous) {
        MarketType type = current.type;

        if (previous == null) {
            previous = current;
        }

        return String.format(getMessageTemplate(),
                type.name,
                createDelta(current.eur, previous.eur),
                createDelta(current.usd, previous.usd),
                createDelta(current.rub, previous.rub),
                getTags(type)
        );
    }

    private String createDelta(RatePair pair, RatePair previousPair) {
        return String.format(getCurrencyTemplate(),
                pair.currency.symbol,
                previousPair.getGrowing(pair).symbol,
                calcDelta(pair, previousPair)
        );
    }

    private double calcDelta(RatePair pair, RatePair previousPair) {
        return Math.abs(Util.formatDouble(
                ((previousPair.buy.value + previousPair.sell.value) / 2)
                -((pair.buy.value + pair.sell.value) / 2))
        );
    }

    protected abstract Market getPreviousMarket(CacheHolder cacheHolder, MarketType type);

    protected abstract String getMessageTemplate();

    protected abstract String getCurrencyTemplate();

    protected String getTags(MarketType type) {
        return type.tags + " #хуёваядельта";
    }
}
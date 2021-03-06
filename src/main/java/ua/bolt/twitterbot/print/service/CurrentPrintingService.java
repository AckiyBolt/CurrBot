package ua.bolt.twitterbot.print.service;


import ua.bolt.twitterbot.domain.Market;
import ua.bolt.twitterbot.domain.MarketType;
import ua.bolt.twitterbot.domain.RatePair;
import ua.bolt.twitterbot.execution.cache.CacheHolder;

/**
 * Created by ackiybolt on 22.03.15.
 */
public class CurrentPrintingService extends AbstractPrintingService {

    private static final String CURR_FORMAT       = "%s - %s / %s %s";
    private static final String SHORT_CURR_FORMAT = "%s - %s %s";

    private static final String MSG_TEMPLATE =
            "%s via %s\n" +     // name, source
            "%s\n" +            // eur
            "%s\n" +            // usd
            "%s\n" +            // rub
            "%s";               // tags

    private MarketType marketType;

    public CurrentPrintingService(MarketType marketType) {
        this.marketType = marketType;
    }

    @Override
    public void printMarket(CacheHolder cacheHolder) {

        Market current = cacheHolder.getCurrent(marketType);
        Market previous = cacheHolder.getPrevious(marketType);

        String message = createMessage(current, previous);

        printer.print(message);
    }

    protected String createMessage(Market current, Market previous) {
        MarketType type = current.type;

        if (previous == null) {
            previous = current;
        }

        return String.format(MSG_TEMPLATE,
                type.name, type.source,
                pairToString(current.eur, previous.eur),
                pairToString(current.usd, previous.usd),
                pairToString(current.rub, previous.rub),
                type.tags
        );
    }

    protected String pairToString (RatePair pair, RatePair previousPair) {
        return pair.buy.value.equals(pair.sell.value) ?
                String.format(
                        SHORT_CURR_FORMAT
                        , pair.currency.symbol
                        , spacerAddingIfNeeded(pair.buy.value)
                        , pair.getGrowing(previousPair).symbol)
                :
                String.format(
                        CURR_FORMAT
                        , pair.currency.symbol
                        , spacerAddingIfNeeded(pair.buy.value)
                        , spacerAddingIfNeeded(pair.sell.value)
                        , pair.getGrowing(previousPair).symbol);
    }
}

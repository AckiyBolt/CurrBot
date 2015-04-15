package ua.bolt.twitterbot.print.service;

import ua.bolt.twitterbot.domain.Market;
import ua.bolt.twitterbot.domain.MarketType;
import ua.bolt.twitterbot.domain.RatePair;
import ua.bolt.twitterbot.execution.cache.CacheHolder;
import ua.bolt.twitterbot.print.printer.ConsolePrinter;
import ua.bolt.twitterbot.print.printer.FilePrinter;
import ua.bolt.twitterbot.print.printer.TwitterPrinter;
import ua.bolt.twitterbot.prop.Names;
import ua.bolt.twitterbot.prop.PropertyHolder;

/**
 * Created by ackiybolt on 15.04.15.
 */
public class FilePrintingService extends AbstractPrintingService {

    private static final String CURR_FORMAT       = "%s - %s / %s %s";
    private static final String SHORT_CURR_FORMAT = "%s - %s %s";

    private static final String MSG_TEMPLATE =
            "%s via %s\n" +     // name, source
                    "%s\n" +            // eur
                    "%s\n" +            // usd
                    "%s";               // rub
    private static final String TAG =
            "#ХуёвыйМежбанк\n" +
            "https://twitter.com/hu_mezhbank";

    public FilePrintingService() {
        super();
        this.printer = IS_DEBUG ? new ConsolePrinter() : new FilePrinter();;
    }

    @Override
    public void printMarket(CacheHolder cacheHolder) {

        StringBuilder stringBuilder = new StringBuilder();

        for (MarketType type: MarketType.values()) {
            stringBuilder
                    .append(printMarket(cacheHolder, type))
                    .append("\n");
        }
        stringBuilder.append(TAG);

        printer.print(stringBuilder.toString());
    }

    private String printMarket(CacheHolder cacheHolder, MarketType type) {

        Market current = cacheHolder.getCurrent(type);
        Market previous = cacheHolder.getPrevious(type);

        return createMessage(current, previous);
    }

    private String createMessage(Market current, Market previous) {
        MarketType type = current.type;

        if (previous == null) {
            previous = current;
        }

        return String.format(MSG_TEMPLATE,
                type.name, type.source,
                pairToString(current.eur, previous.eur),
                pairToString(current.usd, previous.usd),
                pairToString(current.rub, previous.rub)
        );
    }

    private String pairToString (RatePair pair, RatePair previousPair) {
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

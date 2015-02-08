package ua.bolt.twitterbot.print;

import ua.bolt.twitterbot.domain.Market;
import ua.bolt.twitterbot.domain.MarketType;
import ua.bolt.twitterbot.domain.RatePair;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by ackiybolt on 26.12.14.
 */
public class MessageBuilder {

    private static final String CURR_FORMAT       = "%s - %s / %s %s";
    private static final String SHORT_CURR_FORMAT = "%s - %s %s";

    public static final String NEW_LINE = "\n";

    private static Map<MarketType, String> SOURCES = initSources();
    private static Map<MarketType, String> initSources() {
        Map<MarketType, String> result = new EnumMap<>(MarketType.class);

        result.put(MarketType.INTERBANK_MARKET,    "Рыночные курсы via minfin.com.ua");
        result.put(MarketType.INTERBANK_OFFICIAL,  "Межбанковские курсы via minfin.com.ua");
        result.put(MarketType.BLACK_MARKET,        "Чёрный рынок via http://finance.ua");
        result.put(MarketType.NBU,                 "Курсы НБУ via http://bank.gov.ua");

        return Collections.unmodifiableMap(result);
    }

    private static final Map<MarketType, String> TAGS = initTags();
    private static Map<MarketType, String> initTags() {
        Map<MarketType, String> result = new EnumMap<>(MarketType.class);

        result.put(MarketType.INTERBANK_MARKET,    "#гривна #межбанк #заторубльвжопе");
        result.put(MarketType.INTERBANK_OFFICIAL,  "#гривна #межбанк #заторубльвжопе");
        result.put(MarketType.BLACK_MARKET,        "#гривна #насамомделе #заторубльвжопе");
        result.put(MarketType.NBU,                 "#гривна #НБУ #заторубльвжопе");

        return Collections.unmodifiableMap(result);
    }



    public String buildForMarket (Market market, Market previousMarket) {

        if (previousMarket == null)
            previousMarket = market;

        return new StringBuilder()
                .append(resolveSource(market.type))
                .append(NEW_LINE)
                .append(pairToString(market.eur, previousMarket.eur))
                .append(NEW_LINE)
                .append(pairToString(market.usd, previousMarket.usd))
                .append(NEW_LINE)
                .append(pairToString(market.rub, previousMarket.rub))
                .append(NEW_LINE)
                .append(resolveTags(market.type))
                .toString();
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

    private String resolveTags(MarketType type) {
        return TAGS.get(type);
    }

    private String resolveSource(MarketType type) {
        return SOURCES.get(type);
    }

    // makes val size == 5
    private String spacerAddingIfNeeded (Double val) {

        StringBuilder sb = new StringBuilder(val.toString());

        // before dot
        if (val < 1)
            sb.insert(0, "0");

        // after dot
        if (sb.length() < 5)
            sb.append("0");

        return sb.toString();
    }
}

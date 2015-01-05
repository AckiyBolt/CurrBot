package ua.bolt.twitterbot.print;

import ua.bolt.twitterbot.domain.Market;
import ua.bolt.twitterbot.domain.MarketType;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by ackiybolt on 26.12.14.
 */
public class MessageBuilder {

    private static final String CURR_FORMAT =
              "%s - %s / %s %s" +
            "\n%s - %s / %s %s" +
            "\n%s - %s / %s %s";
    public static final String NEW_LINE = "\n";

    private static Map<MarketType, String> SOURCES = initSources();
    private static Map<MarketType, String> initSources() {
        Map<MarketType, String> result = new EnumMap<>(MarketType.class);

        result.put(MarketType.INTERBANK_MARKET,    "Рыночные курсы via minfin.com.ua");
        result.put(MarketType.INTERBANK_OFFICIAL,  "Банковские курсы via minfin.com.ua");
        result.put(MarketType.BLACK_MARKET,        "Чёрный рынок via http://finance.ua");

        return Collections.unmodifiableMap(result);
    }

    private static final Map<MarketType, String> TAGS = initTags();
    private static Map<MarketType, String> initTags() {
        Map<MarketType, String> result = new EnumMap<>(MarketType.class);

        result.put(MarketType.INTERBANK_MARKET,    "#гривна #межбанк #заторубльвжопе");
        result.put(MarketType.INTERBANK_OFFICIAL,  "#гривна #межбанк #заторубльвжопе");
        result.put(MarketType.BLACK_MARKET,        "#гривна #насамомделе #заторубльвжопе");

        return Collections.unmodifiableMap(result);
    }



    public String buildForMarket (Market market, Market previousMarket) {
        return new StringBuilder()
                .append(resolveSource(market.type))
                .append(NEW_LINE)
                .append(String.format(
                            CURR_FORMAT

                            , market.eur.currency.symbol
                            , spacerAddingIfNeeded(market.eur.buy.value)
                            , spacerAddingIfNeeded(market.eur.sell.value)
                            , previousMarket != null ? market.eur.getGrowing(previousMarket.eur).symbol : " "

                            , market.usd.currency.symbol
                            , spacerAddingIfNeeded(market.usd.buy.value)
                            , spacerAddingIfNeeded(market.usd.sell.value)
                            , previousMarket != null ? market.usd.getGrowing(previousMarket.usd).symbol : " "

                            , market.rub.currency.symbol
                            , spacerAddingIfNeeded(market.rub.buy.value)
                            , spacerAddingIfNeeded(market.rub.sell.value)
                            , previousMarket != null ? market.rub.getGrowing(previousMarket.rub).symbol : " "
                        ))
                .append(NEW_LINE)
                .append(resolveTags(market.type))
                .toString();
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

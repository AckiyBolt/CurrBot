package ua.bolt.twitterbot.domain;

import ua.bolt.twitterbot.Constants;

/**
 * Created by ackiybolt on 25.12.14.
 */
public enum MarketType {

    BLACK_MARKET        (Constants.BLACK_MARKET_URL),
    INTERBANK_MARKET    (Constants.INTERBANK_MARKET_URL),
    INTERBANK_OFFICIAL  (Constants.INTERBANK_MARKET_URL);

    public final String URL;

    MarketType(String url) {
        URL = url;
    }
}

package ua.bolt.twitterbot.domain;

/**
 * Created by ackiybolt on 20.12.14.
 */
public enum Currency {
    EUR("€"),
    USD("$"),
    RUB("†");

    public final String symbol;

    Currency(String symbol) {
        this.symbol = symbol;
    }
}

package ua.bolt.twitterbot.domain;

/**
 * Created by ackiybolt on 24.12.14.
 */
public enum Growing {
    UP   ("▲"),
    DOWN ("▼"),
    SAME (" ");

    public final String symbol;

    Growing(String symbol) {
        this.symbol = symbol;
    }
}

package ua.bolt.twitterbot.domain;

/**
 * Created by ackiybolt on 25.12.14.
 */
public enum MarketType {

    BLACK_MARKET        (
                        "Чёрный рынок",
                        "finance.ua",
                        "#гривна #насамомделе #заторубльвжопе"),

    INTERBANK_OFFICIAL  (
                        "Межбанк",
                        "minfin.com.ua",
                        "#гривна #межбанк #заторубльвжопе"),

    NBU                 (
                        "НБУ",
                        "bank.gov.ua",
                        "#гривна #НБУ #заторубльвжопе");

    public final String name;
    public final String source;
    public final String tags;

    MarketType(String name, String source, String tags) {
        this.name = name;
        this.source = source;
        this.tags = tags;
    }
}

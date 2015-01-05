package ua.bolt.twitterbot.print;

import ua.bolt.twitterbot.domain.Market;

/**
 * Created by ackiybolt on 24.12.14.
 */
public abstract class AbstractPrinter {

    private MessageBuilder builder = new MessageBuilder();

    private static final String MSG_FORMAT =
              "%s - %s / %s %s" +
            "\n%s - %s / %s %s" +
            "\n%s - %s / %s %s" +
            "\nРыночные курсы via minfin.com.ua" +
            "\n#гривна #межбанк #заторубльвжопе";

    public abstract void print (String message) throws RuntimeException;

    public void print (Market market, Market previousMarket) {
        print( builder.buildForMarket(market, previousMarket) );
    }
}

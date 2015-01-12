package ua.bolt.twitterbot.print;

import ua.bolt.twitterbot.domain.Market;

/**
 * Created by ackiybolt on 24.12.14.
 */
public abstract class AbstractPrinter {

    private MessageBuilder builder = new MessageBuilder();

    public abstract void print (String message) throws RuntimeException;

    public void print (Market market, Market previousMarket) {
        print( builder.buildForMarket(market, previousMarket) );
    }
}

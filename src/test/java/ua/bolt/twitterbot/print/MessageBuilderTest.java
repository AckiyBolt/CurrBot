package ua.bolt.twitterbot.print;

import org.junit.Before;
import org.junit.Test;
import ua.bolt.twitterbot.domain.*;

import static org.junit.Assert.*;

public class MessageBuilderTest {

    private int maxTwitterMessageLenght = 140;

    private MessageBuilder undertest;

    private Market current;
    private Market previous;

    private void initMarketsByMarketType (MarketType type) {
        current = new Market(
                type,
                new RatePair(
                        Currency.EUR,
                        new Rate( 10.0, RateType.BUY),
                        new Rate( 20.0 ,RateType.SELL)),
                new RatePair(
                        Currency.USD,
                        new Rate( 30.0, RateType.BUY),
                        new Rate( 40.0 ,RateType.SELL)),
                new RatePair(
                        Currency.RUB,
                        new Rate( 50.0, RateType.BUY),
                        new Rate( 60.0 ,RateType.SELL)));

        previous = new Market(
                type,
                new RatePair(
                        Currency.EUR,
                        new Rate( 100.0, RateType.BUY),
                        new Rate( 200.0 ,RateType.SELL)),
                new RatePair(
                        Currency.USD,
                        new Rate( 30.0, RateType.BUY),
                        new Rate( 40.0 ,RateType.SELL)),
                new RatePair(
                        Currency.RUB,
                        new Rate( 1.0, RateType.BUY),
                        new Rate( 2.0 ,RateType.SELL))
        );
    }

    @Before
    public void setUp() {
        current = previous = null;
        undertest = new MessageBuilder();
    }

    @Test
    public void testBuildForMarket_WhenInternalTrue_ThenCorrectMessageLength() throws Exception {

        initMarketsByMarketType(MarketType.INTERBANK_MARKET);
        makeTest();
    }

    @Test
    public void testBuildForMarket_WhenInternalOfficial_ThenCorrectMessageLength() throws Exception {

        initMarketsByMarketType(MarketType.INTERBANK_OFFICIAL);
        makeTest();
    }

    @Test
    public void testBuildForMarket_WhenBlack_ThenCorrectMessageLength() throws Exception {

        initMarketsByMarketType(MarketType.BLACK_MARKET);
        makeTest();
    }

    private void makeTest() {

        String message = undertest.buildForMarket(current, previous);
        int length = message.length();

        System.out.println(message);
        System.out.println("Length is: " + length);
        System.out.println(" ");

        assertTrue(length <= maxTwitterMessageLenght);
    }
}
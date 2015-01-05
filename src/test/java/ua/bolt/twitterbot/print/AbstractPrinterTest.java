package ua.bolt.twitterbot.print;

import org.junit.Before;
import ua.bolt.twitterbot.domain.*;

public class AbstractPrinterTest {

    private AbstractPrinter undertest;

    @Before
    public void setUp() {
        undertest = new ConsolePrinter();
    }

    //@Test
    public void testPrint() throws Exception {

        Market current = new Market(
                MarketType.BLACK_MARKET,
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

        Market previous = new Market(
                MarketType.BLACK_MARKET,
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


        System.out.println(  "Previous:\n" + previous);
        System.out.println("\nCurrent: \n" + current);

        System.out.println("\nPrint:\n");
        undertest.print(current, previous);
    }
}
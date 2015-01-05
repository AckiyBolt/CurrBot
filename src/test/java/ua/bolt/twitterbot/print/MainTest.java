package ua.bolt.twitterbot.print;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Test;
import ua.bolt.twitterbot.domain.*;

import java.util.Arrays;

public class MainTest {

    private AbstractPrinter undertest;

    @Before
    public void setUp() {
        undertest = new ConsolePrinter();
    }

    @Test
    public void testPrint() throws Exception {



//        String str = Jsoup.connect("http://resources.finance.ua/chart/data?for=currency-order&currency=RUB")..execute().body();
//        System.out.println(str);
    }
}
package ua.bolt.twitterbot.miner;

import com.google.gson.Gson;
import twitter4j.Logger;
import ua.bolt.twitterbot.domain.*;
import ua.bolt.twitterbot.miner.ex.MinerEmptyException;

import static ua.bolt.twitterbot.miner.Util.*;

import java.io.IOException;

/**
 * Created by ackiybolt on 25.12.14.
 */
public class BlackMarketMiner implements Miningable {

    private static final String BLACK_MARKET_URL = "http://resources.finance.ua/chart/data?for=currency-order&currency=";
    private static Logger LOG = Logger.getLogger(BlackMarketMiner.class);

    private static final MarketType type = MarketType.BLACK_MARKET;

    private Gson gson = new Gson();
    private DocumentDownloader downloader = DocumentDownloader.INSTANCE;

    @Override
    public Market mineRates() throws MinerEmptyException {
        Market result = null;

        String eur = null;
        String usd = null;
        String rub = null;

        try {
            eur = downloader.downloadRawData(BLACK_MARKET_URL + Currency.EUR);
            usd = downloader.downloadRawData(BLACK_MARKET_URL + Currency.USD);
            rub = downloader.downloadRawData(BLACK_MARKET_URL + Currency.RUB);

        } catch (IOException ex) {
            LOG.error("Bad url. Can't download data.", ex);
        }

        if (isExist(eur) && isExist(usd) && isExist(rub))
            result = new Market(
                    type,
                    parseRatePair(eur, Currency.EUR),
                    parseRatePair(usd, Currency.USD),
                    parseRatePair(rub, Currency.RUB));


        return result;
    }

    public RatePair parseRatePair(String page, Currency currency) {
        RatePair result =  null;

        String [][] data = prepareRawData(page);
        Double buy = null;
        Double sell = null;

        for (int i = data.length - 1; i != 0; i--) {
            try {
                buy = parseAndFormatDouble(data[i][1]);
                sell = parseAndFormatDouble(data[i][2]);
                break;

            } catch (NullPointerException ex) {
                // do nothing
            } catch (ArrayIndexOutOfBoundsException ex) {

            }
        }

        if (buy != null && sell != null)
            result = new RatePair(
                    currency,
                    new Rate(buy, RateType.BUY),
                    new Rate(sell, RateType.SELL));

        return result;
    }

    private String[][] prepareRawData(String rawData) {
        return gson.fromJson(rawData, String [][].class);
    }

}

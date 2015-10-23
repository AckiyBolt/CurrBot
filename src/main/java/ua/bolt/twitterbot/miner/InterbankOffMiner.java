package ua.bolt.twitterbot.miner;

import com.google.gson.Gson;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import twitter4j.Logger;
import ua.bolt.twitterbot.domain.*;
import ua.bolt.twitterbot.miner.ex.MinerEmptyException;
import ua.bolt.twitterbot.miner.ex.PageChangedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static ua.bolt.twitterbot.miner.Util.isExist;
import static ua.bolt.twitterbot.miner.Util.parseAndFormatDouble;

/**
 * Created by ackiybolt on 20.12.14.
 */
public class InterbankOffMiner implements Miningable {

    private static Logger LOG  = Logger.getLogger(InterbankOffMiner.class);
    private        Gson   gson = new Gson();

    private static final String URL_TEMPLATE = "http://minfin.com.ua/data/currency/ib/%s.ib.today.json";

    protected DocumentDownloader downloader = DocumentDownloader.INSTANCE;

    @Override
    public MarketType getType() {
        return MarketType.INTERBANK_OFFICIAL;
    }

    @Override
    public Market mineRates() throws MinerEmptyException {
        Market result = null;

        String eur = null;
        String usd = null;
        String rub = null;

        try {
            eur = downloader.downloadRawData(String.format(URL_TEMPLATE, Currency.EUR.toString().toLowerCase()));
            usd = downloader.downloadRawData(String.format(URL_TEMPLATE, Currency.USD.toString().toLowerCase()));
            rub = downloader.downloadRawData(String.format(URL_TEMPLATE, Currency.RUB.toString().toLowerCase()));

        } catch (IOException ex) {
            LOG.error("Bad url. Can't download data.", ex);
        }

        if (isExist(eur) && isExist(usd) && isExist(rub))
            result = new Market(
                   getType(),
                   parseRatePair(eur, Currency.EUR),
                   parseRatePair(usd, Currency.USD),
                   parseRatePair(rub, Currency.RUB));


        return result;
    }

    public RatePair parseRatePair(String page, Currency currency) {
        RatePair result =  null;

        Entry[] data = prepareRawData(page);
        Double buy = null;
        Double sell = null;

        for (int i = data.length - 1; i != 0; i--) {
            try {
                buy = parseAndFormatDouble(data[i].bid);
                sell = parseAndFormatDouble(data[i].ask);
                break;

            } catch (NullPointerException | ArrayIndexOutOfBoundsException ex) {
                // do nothing
            }
        }

        if (buy != null && sell != null)
            result = new RatePair(
                   currency,
                   new Rate(buy, RateType.BUY),
                   new Rate(sell, RateType.SELL));

        return result;
    }

    private Entry[] prepareRawData(String rawData) {
        return gson.fromJson(rawData, Entry[].class);
    }

    private static class Entry {
        private String date;
        private String bid;
        private String ask;

        @Override
        public String toString() {
            return "Entry{" +
                   "date='" + date + '\'' +
                   ", bid='" + bid + '\'' +
                   ", ask='" + ask + '\'' +
                   '}';
        }
    }
}

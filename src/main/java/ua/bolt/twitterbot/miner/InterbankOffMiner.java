package ua.bolt.twitterbot.miner;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import twitter4j.Logger;
import ua.bolt.twitterbot.Constants;
import ua.bolt.twitterbot.domain.*;
import ua.bolt.twitterbot.miner.ex.MinerEmptyException;
import ua.bolt.twitterbot.miner.ex.PageChangedException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static ua.bolt.twitterbot.miner.Util.*;

/**
 * Created by ackiybolt on 20.12.14.
 */
public class InterbankOffMiner implements Miningable {

    private static Logger LOG = Logger.getLogger(InterbankOffMiner.class);


    protected static final String URL = Constants.INTERBANK_MARKET_URL;

    private static final String TABLE_KEY = "mb-data";

    protected DocumentDownloader downloader = DocumentDownloader.Instance;

    @Override
    public Market mineRates() throws MinerEmptyException {

        Market result = null;

        try {
            Document page = downloader.downloadDocument(URL);

            Element table = findTableWithRates(page, TABLE_KEY);
            result = createMarket(MarketType.INTERBANK_OFFICIAL, parseTable(table));


        } catch (Exception ex) {
            LOG.error("Can't mine rates. Fix me!", ex);
            throw new MinerEmptyException(ex);
        }

        return result;
    }



    protected Element findTableWithRates(Document page, String tableKey) {
        Element result = null;

        Elements tables = page.select("table");
        for (Element table : tables)
                if (table.className().equals(TABLE_KEY))
                    result = table;

        if (result == null)
            throw new PageChangedException();

        return result;
    }



    protected Set<RatePair> parseTable(Element table) {
        ArrayList<String> currencies = new ArrayList<String>(3);
        ArrayList<String> ratesBuy = new ArrayList<String>(3);
        ArrayList<String> ratesSell = new ArrayList<String>(3);

        parseRates(table, ratesBuy, ratesSell);
        parseCurrencies(table, currencies);

        return parseRatePairs(currencies, ratesBuy, ratesSell);
    }

    private void parseRates(Element table, ArrayList<String> ratesBuy, ArrayList<String> ratesSell) {
        for (Element td : table.select("td"))
            if (!td.ownText().isEmpty() && td.ownText().matches("[.\\d]+"))
                if (ratesBuy.size() < 3)
                    ratesBuy.add(td.ownText());
                else
                    ratesSell.add(td.ownText());
    }

    private void parseCurrencies(Element table, ArrayList<String> currencies) {
        for(Element th : table.select("th")) {
            if (th.children().size() != 0)
                currencies.add(th.child(0).ownText());
        }
    }

    private Set<RatePair> parseRatePairs(
            ArrayList<String> currencies,
            ArrayList<String> ratesBuy,
            ArrayList<String> ratesSell) {

        Set<RatePair> pairs = new HashSet<RatePair>();

        for (int i = 0; i < currencies.size(); i++) {
            String currVal = currencies.get(i);
            String buyVal = ratesBuy.get(i);
            String sellVal = ratesSell.get(i);

            pairs.add(createRatePair(currVal, buyVal, sellVal));
        }

        return pairs;
    }

    private RatePair createRatePair(String currVal, String buyVal, String sellVal) {
        return new RatePair(
                Currency.valueOf(currVal),
                new Rate(
                        parseAndFormatDouble(buyVal),
                        RateType.BUY),
                new Rate(
                        parseAndFormatDouble(sellVal),
                        RateType.SELL));
    }



    protected Market createMarket(MarketType type, Set<RatePair> pairs) {

        RatePair eur = null;
        RatePair usd = null;
        RatePair rub = null;

        for (RatePair pair: pairs)
            switch (pair.currency) {
                case EUR: eur = pair; break;
                case USD: usd = pair; break;
                case RUB: rub = pair; break;
            }

        return new Market(type, eur, usd, rub);
    }
}

package ua.bolt.twitterbot.miner;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import twitter4j.Logger;
import ua.bolt.twitterbot.domain.*;
import ua.bolt.twitterbot.miner.ex.MinerEmptyException;
import ua.bolt.twitterbot.miner.ex.PageChangedException;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ackiybolt on 08.02.15.
 */
public class NbuMiner implements Miningable  {

    private static Logger LOG = Logger.getLogger(NbuMiner.class);

    private DocumentDownloader downloader = DocumentDownloader.INSTANCE;
    private static final String URL       = "http://www.bank.gov.ua/control/uk/index";
    private static final String TABLE_KEY = "Офіційний курс гривні до іноземних валют";


    @Override
    public Market mineRates() throws MinerEmptyException {

        Market result = null;

        try {
            Document page = downloader.downloadDocument(URL);

            Element tbody = findTableBodyWithRates(page, TABLE_KEY);
            result = createMarket(MarketType.NBU, parseTableBody(tbody));


        } catch (Exception ex) {
            LOG.error("Can't mine rates. Fix me!", ex);
            throw new MinerEmptyException(ex);
        }

        return result;
    }

    private Element findTableBodyWithRates(Document page, String tableKey) {
        Element result = null;

        Elements tables = page.select("table");
        for (Element table : tables) {
            Elements ths = table.select("th");
            for (Element th : ths)
                if (tableKey.equals(th.ownText()))
                    result = table.select("tbody").first();
        }

        if (result == null)
            throw new PageChangedException();

        return result;
    }

    private Set<RatePair> parseTableBody(Element tbody) {
        Set<RatePair> result = new HashSet<>();

        for (Element tr : tbody.children()) {
            String att = null;
            String val = null;
            for (Element td : tr.children()) {
                if (td.className().equals("attribute"))
                    att = td.ownText();
                else if (td.className().equals("value"))
                    val = td.ownText();
            }

            if (att == null || val == null)
                throw new PageChangedException();

            result.add(parseAndCreatePair(att, val));
        }

        return result;
    }

    private RatePair parseAndCreatePair(String att, String val) {
        String valuePattern = "([\\d.]{4,}).*";
        Currency curr = null;
        double value = 0;

        if (att.contains("США")) curr = Currency.USD;
        if (att.contains("Євро")) curr = Currency.EUR;
        if (att.contains("рублів")) curr = Currency.RUB;

        Pattern pattern = Pattern.compile(valuePattern);
        Matcher matcher = pattern.matcher(val);

        while(matcher.find())
            value = Util.parseDivideAndFormatDouble(matcher.group(1), curr == Currency.RUB ? 10 : 100);

        return new RatePair(
                curr,
                new Rate(value, RateType.BUY),
                new Rate(value, RateType.SELL));
    }

    private Market createMarket(MarketType type, Set<RatePair> pairs) {
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

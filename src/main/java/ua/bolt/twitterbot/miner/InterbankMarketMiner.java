package ua.bolt.twitterbot.miner;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import twitter4j.Logger;
import ua.bolt.twitterbot.Constants;
import ua.bolt.twitterbot.domain.Market;
import ua.bolt.twitterbot.domain.MarketType;
import ua.bolt.twitterbot.miner.ex.MinerEmptyException;

/**
 * Created by ackiybolt on 04.01.15.
 */
public class InterbankMarketMiner extends InterbankOffMiner {

    private static Logger LOG = Logger.getLogger(InterbankOffMiner.class);


    private static final String TABLE_KEY = Constants.TABLE_KEY_MAR;

    @Override
    public Market mineRates() throws MinerEmptyException {

        Market result = null;

        try {
            Document page = downloader.downloadDocument(URL);

            Element table = findTableWithRates(page, TABLE_KEY);
            result = createMarket(MarketType.INTERBANK_MARKET, parseTable(table));

        } catch (Exception ex) {
            LOG.error("Can't mine rates. Fix me!", ex);
            throw new MinerEmptyException(ex);
        }

        return result;
    }
}

package ua.bolt.twitterbot.miner;

import ua.bolt.twitterbot.domain.Market;
import ua.bolt.twitterbot.domain.MarketType;
import ua.bolt.twitterbot.miner.ex.MinerEmptyException;

/**
 * Created by ackiybolt on 26.12.14.
 */
public interface Miningable {

    public MarketType getType();
    public Market mineRates() throws MinerEmptyException;
}

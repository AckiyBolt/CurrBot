package ua.bolt.twitterbot.miner;

import ua.bolt.twitterbot.domain.Market;
import ua.bolt.twitterbot.miner.ex.MinerEmptyException;

import java.util.Set;

/**
 * Created by ackiybolt on 26.12.14.
 */
public interface Miningable {

    public Market mineRates() throws MinerEmptyException;
}

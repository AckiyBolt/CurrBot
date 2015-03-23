package ua.bolt.twitterbot;

import twitter4j.Logger;
import ua.bolt.twitterbot.execution.ExecutionSetup;
import ua.bolt.twitterbot.miner.ex.MinerEmptyException;
import ua.bolt.twitterbot.prop.PropertyHolder;

/**
 * Created by ackiybolt on 19.12.14.
 */

public class Main {

    private static Logger LOG = Logger.getLogger(Main.class);

    static {
        LOG.info("Init properties...");
        PropertyHolder.INSTANCE.load();
    }

    public static void main(String[] args) throws MinerEmptyException {

        LOG.info("Bot starting...");

        ExecutionSetup setup = new ExecutionSetup();
        setup
                .loadCache()
                .createAndRunCacheUpdaterAgent()
                .createAndRunCurrentAgents()
                .createAndRunDailyAgent()
                .createAndRunWeeklyAgent();

        LOG.info("Bot started.");
    }
}

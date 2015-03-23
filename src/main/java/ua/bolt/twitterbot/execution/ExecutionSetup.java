package ua.bolt.twitterbot.execution;

import twitter4j.Logger;
import ua.bolt.twitterbot.execution.agent.CacheUpdaterAgent;
import ua.bolt.twitterbot.execution.agent.CurrentAgent;
import ua.bolt.twitterbot.execution.cache.CacheHolder;
import ua.bolt.twitterbot.execution.cache.CacheManager;
import ua.bolt.twitterbot.miner.BlackMarketMiner;
import ua.bolt.twitterbot.miner.InterbankOffMiner;
import ua.bolt.twitterbot.miner.NbuMiner;
import ua.bolt.twitterbot.prop.Names;
import ua.bolt.twitterbot.prop.PropertyHolder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by ackiybolt on 12.03.15.
 */
public class ExecutionSetup {

    private static final int POOL_SIZE = 6;

    private static Logger LOG = Logger.getLogger(ExecutionSetup.class);

    private CacheHolder cacheHolder;
    ScheduledExecutorService scheduledExecutorService;

    public ExecutionSetup() {
        this.scheduledExecutorService = Executors.newScheduledThreadPool(POOL_SIZE);
    }

    public ExecutionSetup loadCache () {
        CacheManager manager = new CacheManager(PropertyHolder.INSTANCE.getStr(Names.cache_file));
        cacheHolder = manager.readCache();
        if (cacheHolder == null) {
            LOG.warn("Cache is not exist. Create new one.");
            cacheHolder = new CacheHolder();
        } else {
            LOG.info("Cache has been loaded.");
        }

        return this;
    }

    public ExecutionSetup createAndRunCacheUpdaterAgent() {
        checkCacheHolder();

        CacheUpdaterAgent cacheUpdaterAgent = new CacheUpdaterAgent(cacheHolder);
        cacheHolder.setUpdateAgent(cacheUpdaterAgent);
        new Thread(cacheUpdaterAgent).start();

        return this;
    }

    public ExecutionSetup createAndRunCurrentAgents() {
        checkCacheHolder();

        scheduledExecutorService.scheduleWithFixedDelay(
                new CurrentAgent(new InterbankOffMiner(), cacheHolder), 0, 1, TimeUnit.HOURS);

        scheduledExecutorService.scheduleWithFixedDelay(
                new CurrentAgent(new BlackMarketMiner(),  cacheHolder), 0, 1, TimeUnit.HOURS);

        scheduledExecutorService.scheduleWithFixedDelay(
                new CurrentAgent(new NbuMiner(), cacheHolder), 0, 1, TimeUnit.HOURS);

        return this;
    }

    public ExecutionSetup createAndRunDailyAgent() {
        checkCacheHolder();

        scheduledExecutorService.scheduleWithFixedDelay(
                new CurrentAgent(new NbuMiner(), cacheHolder), 0, 1, TimeUnit.HOURS);

        return this;
    }

    public ExecutionSetup createAndRunWeeklyAgent() {
        checkCacheHolder();

        scheduledExecutorService.scheduleWithFixedDelay(
                new CurrentAgent(new NbuMiner(), cacheHolder), 0, 1, TimeUnit.HOURS);

        return this;
    }

    private void checkCacheHolder() {
        if (cacheHolder == null) {
            LOG.error("Cache has not been initialized.");
            throw new RuntimeException();
        }
    }
}

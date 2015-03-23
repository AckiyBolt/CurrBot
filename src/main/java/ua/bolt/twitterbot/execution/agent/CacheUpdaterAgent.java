package ua.bolt.twitterbot.execution.agent;

import ua.bolt.twitterbot.execution.cache.CacheHolder;
import ua.bolt.twitterbot.execution.cache.CacheManager;
import ua.bolt.twitterbot.prop.Names;
import ua.bolt.twitterbot.prop.PropertyHolder;

/**
 * Created by ackiybolt on 12.03.15.
 */
public class CacheUpdaterAgent implements Runnable {

    private CacheManager manager;
    private CacheHolder holder;

    private volatile boolean isChanged;
    private volatile boolean isAlive = true;
    private final int SLEEP_PERIOD = PropertyHolder.INSTANCE.getInt(Names.min_5);

    public CacheUpdaterAgent(CacheHolder holder) {
        manager = new CacheManager(PropertyHolder.INSTANCE.getStr(Names.cache_file));
        this.holder = holder;
    }

    @Override
    public void run() {
        while (isAlive) {
            if (isChanged) {
                manager.updateCache(holder);
                isChanged = false;
            }

            try {
                Thread.sleep(SLEEP_PERIOD);
            } catch (InterruptedException e) {
                isAlive = false;
            }
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void holderChanged() {
        isChanged = true;
    }
}

package ua.bolt.twitterbot.execution.cache;

import com.google.gson.annotations.Expose;
import ua.bolt.twitterbot.domain.Market;
import ua.bolt.twitterbot.domain.MarketType;
import ua.bolt.twitterbot.execution.agent.CacheUpdaterAgent;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ackiybolt on 12.03.15.
 */
public class CacheHolder {

    @Expose
    private ConcurrentHashMap<MarketType, Market> current;
    private ConcurrentHashMap<MarketType, Market> previous;
    private ConcurrentHashMap<MarketType, Market> previousDay;
    private ConcurrentHashMap<MarketType, Market> previousWeek;

    transient private CacheUpdaterAgent updateAgent;

    public CacheHolder() {
        current = new ConcurrentHashMap<>();
        previous = new ConcurrentHashMap<>();
        previousDay = new ConcurrentHashMap<>();
        previousWeek = new ConcurrentHashMap<>();
    }

    public void setUpdateAgent(CacheUpdaterAgent updateAgent) {
        this.updateAgent = updateAgent;
    }

    public void updateCurrentMarket(Market market) {
        current.put(market.type, market);

        sandUpdateNotification();
    }

    public void updatePreviousMarket(Market market) {
        previous.put(market.type, market);

        sandUpdateNotification();
    }

    public void updateDailyMarkets() {
        for (MarketType type: MarketType.values()) {
            updateDayMarket(type);
        }

        sandUpdateNotification();
    }

    private void updateDayMarket(MarketType type) {
        Market currentMarket = current.get(type);

        if (currentMarket != null)
            previousDay.put(currentMarket.type, currentMarket);
    }

    public void updateWeeklyMarkets() {
        for (MarketType type: MarketType.values()) {
            updateWeekMarket(type);
        }

        sandUpdateNotification();
    }

    private void updateWeekMarket(MarketType type) {
        Market currentMarket = current.get(type);

        if (currentMarket != null)
            previousWeek.put(currentMarket.type, currentMarket);
    }

    public Market getCurrent (MarketType type) {
        return current.get(type);
    }

    public Market getPrevious (MarketType type) {
        return previous.get(type);
    }

    public Market getPreviousDay (MarketType type) {
        return previousDay.get(type);
    }

    public Market getPreviousWeek (MarketType type) {
        return previousWeek.get(type);
    }

    private void sandUpdateNotification() {
        if (updateAgent != null && updateAgent.isAlive())
            updateAgent.holderChanged();
    }
}

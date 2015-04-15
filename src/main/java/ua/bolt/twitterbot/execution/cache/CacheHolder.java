package ua.bolt.twitterbot.execution.cache;

import com.google.gson.annotations.Expose;
import ua.bolt.twitterbot.domain.Market;
import ua.bolt.twitterbot.domain.MarketType;
import ua.bolt.twitterbot.execution.agent.UpdatableAgent;

import java.util.HashSet;
import java.util.Set;
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

    transient private Set<UpdatableAgent> updatableAgents;

    public CacheHolder() {
        current = new ConcurrentHashMap<>();
        previous = new ConcurrentHashMap<>();
        previousDay = new ConcurrentHashMap<>();
        previousWeek = new ConcurrentHashMap<>();
        updatableAgents = new HashSet<>();
    }

    public void addUpdatableAgent(UpdatableAgent updatableAgent) {
        this.updatableAgents.add(updatableAgent);
    }

    public void updateCurrentMarket(Market market) {
        current.put(market.type, market);

        sendUpdateNotification();
    }

    public void updatePreviousMarket(Market market) {
        previous.put(market.type, market);

        sendUpdateNotification();
    }

    public void updateDailyMarkets() {
        for (MarketType type: MarketType.values()) {
            updateDayMarket(type);
        }

        sendUpdateNotification();
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

        sendUpdateNotification();
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

    private void sendUpdateNotification() {
        for (UpdatableAgent agent: updatableAgents) {
            if (agent != null && agent.isAlive())
                agent.holderChanged();
        }
    }
}

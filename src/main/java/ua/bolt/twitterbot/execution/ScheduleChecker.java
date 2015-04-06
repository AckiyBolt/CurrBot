package ua.bolt.twitterbot.execution;

import org.joda.time.DateTime;
import ua.bolt.twitterbot.domain.MarketType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ackiybolt on 22.03.15.
 */
public enum ScheduleChecker {

    INSTANCE;
    
    private Map<MarketType, Schedule> schedules;

    private ScheduleChecker() {
        schedules = new HashMap<>();

        schedules.put(MarketType.NBU, createNbu());
        schedules.put(MarketType.BLACK_MARKET, createBlack());
        schedules.put(MarketType.INTERBANK_OFFICIAL, createInterbank());
    }

    public boolean isTimeForWork (MarketType marketType) {

        boolean result = false;
        DateTime now = TimeUtil.getNow();
        
        
        Schedule schedule = schedules.get(marketType);
        
        if (  schedule.dayOfWeeks[now.dayOfWeek().get()] == 1
           && schedule.hours[now.hourOfDay().get()] == 1)
            result = true;

        return result;
    }

    private Schedule createInterbank() {
        Schedule result = new Schedule();

        for (byte i = 1; i <= 5; i++)
            result.dayOfWeeks[i] = 1;

        for (byte i = 10; i <= 18; i++)
            result.hours[i] = 1;

        return result;
    }

    private Schedule createBlack() {
        Schedule result = new Schedule();

        for (int i = 1; i <= 7; i++)
            result.dayOfWeeks[i] = 1;

        for (int i = 9; i <= 19; i++)
            result.hours[i] = 1;

        return result;
    }

    private Schedule createNbu () {
        Schedule result = new Schedule();

        for (int i = 1; i <= 5; i++)
            result.dayOfWeeks[i] = 1;

        result.hours[15] = 1;

        return result;
    }

    private class Schedule {
        byte [] hours = new byte[25];
        byte [] dayOfWeeks = new byte[8];
    }
}

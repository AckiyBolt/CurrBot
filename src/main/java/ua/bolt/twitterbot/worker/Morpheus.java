package ua.bolt.twitterbot.worker;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import twitter4j.Logger;
import ua.bolt.twitterbot.Constants;
import ua.bolt.twitterbot.domain.BankStatus;

/**
 * Created by ackiybolt on 04.01.15.
 */
public class Morpheus {

    private static Logger LOG = Logger.getLogger(Morpheus.class);

    private final int fromDay;
    private final int toDay;
    private final int fromHour;
    private final int toHour;
    private final int updatingPeriod;
    private final int sleepingPeriod;

    public Morpheus(int fromDay, int toDay, int fromHour, int toHour, int updatingPeriod, int sleepingPeriod) {
        this.fromDay = fromDay;
        this.toDay = toDay;
        this.fromHour = fromHour;
        this.toHour = toHour;
        this.updatingPeriod = updatingPeriod;
        this.sleepingPeriod = sleepingPeriod;
    }

    public void gotoSleep() throws InterruptedException {
        boolean isNotEnough = true;

        do {
            BankStatus status = guessBankStatus();
            Thread.sleep(status == BankStatus.SLEEPS ? sleepingPeriod : updatingPeriod);

            if (status == BankStatus.WORKS)
                isNotEnough = false;

        } while (isNotEnough);
    }

    private BankStatus guessBankStatus() {
        BankStatus result = BankStatus.WORKS;

        if (Constants.IS_DEBUUG) return result;

        DateTime now = DateTime.now(DateTimeZone.forID(Constants.TIMEZONE_ID));

        // day
        if (now.getDayOfWeek() < fromDay || now.getDayOfWeek() > toDay )
            result = BankStatus.SLEEPS;

        // hour
        if (now.hourOfDay().get() <= fromHour - 1 && now.minuteOfHour().get() < 30)
                result = BankStatus.SLEEPS;

        // hour
        if (now.hourOfDay().get() >= toHour && now.minuteOfHour().get() > 30)
            result = BankStatus.SLEEPS;

        return result;
    }
}

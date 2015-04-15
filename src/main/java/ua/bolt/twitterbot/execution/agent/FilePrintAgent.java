package ua.bolt.twitterbot.execution.agent;

import ua.bolt.twitterbot.execution.cache.CacheHolder;
import ua.bolt.twitterbot.print.service.FilePrintingService;
import ua.bolt.twitterbot.prop.Names;
import ua.bolt.twitterbot.prop.PropertyHolder;

/**
 * Created by ackiybolt on 15.04.15.
 */
public class FilePrintAgent implements Runnable, UpdatableAgent {

    private CacheHolder holder;

    private FilePrintingService printingService;
    private volatile boolean isChanged = true;
    private volatile boolean isAlive = true;
    private final int SLEEP_PERIOD = PropertyHolder.INSTANCE.getInt(Names.min_5);

    public FilePrintAgent(CacheHolder holder) {
        this.printingService = new FilePrintingService();
        this.holder = holder;
    }

    @Override
    public void run() {
        while (isAlive) {
            if (isChanged) {
                printingService.printMarket(holder);
                isChanged = false;
            }

            try {
                Thread.sleep(SLEEP_PERIOD);
            } catch (InterruptedException e) {
                isAlive = false;
            }
        }
    }

    @Override
    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public void holderChanged() {
        isChanged = true;
    }

}

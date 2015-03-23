package ua.bolt.twitterbot.print.printer;

import twitter4j.Logger;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Created by ackiybolt on 24.12.14.
 */
public class TwitterPrinter implements Printer {

    private static Logger LOG = Logger.getLogger(TwitterPrinter.class);

    @Override
    public void print(String message) throws RuntimeException {

        try {
            Twitter twitter = TwitterFactory.getSingleton();
            twitter.updateStatus(message);
            LOG.info("Status successfully updated.");

        } catch (TwitterException ex) {
            LOG.error("Can't update status in twitter.", ex);
        }
    }
}

package ua.bolt.twitterbot.miner.ex;

/**
 * Created by ackiybolt on 24.12.14.
 */
public class PageChangedException extends RuntimeException {

    public PageChangedException() {
        super("Styles has been changed! Fix me blyat'!");
    }

    public PageChangedException(String msg) {
        super(msg);
    }
}

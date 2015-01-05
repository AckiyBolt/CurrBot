package ua.bolt.twitterbot.miner.ex;

/**
 * Created by ackiybolt on 24.12.14.
 */
public class MinerEmptyException extends Exception {

    public MinerEmptyException(Throwable cause) {
        super("Can't mine rates. Fix me!", cause);
    }
}

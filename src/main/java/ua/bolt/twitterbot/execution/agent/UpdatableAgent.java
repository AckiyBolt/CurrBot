package ua.bolt.twitterbot.execution.agent;

/**
 * Created by ackiybolt on 15.04.15.
 */
public interface UpdatableAgent {
    void holderChanged();
    boolean isAlive();
}

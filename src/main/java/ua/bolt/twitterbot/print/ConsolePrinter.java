package ua.bolt.twitterbot.print;

/**
 * Created by ackiybolt on 24.12.14.
 */
public class ConsolePrinter extends AbstractPrinter {

    @Override
    public void print(String message) throws RuntimeException {
        System.out.println(message);
    }
}

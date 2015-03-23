package ua.bolt.twitterbot.print.printer;

/**
 * Created by ackiybolt on 24.12.14.
 */
public class ConsolePrinter implements Printer {

    @Override
    public void print(String message) throws RuntimeException {
        System.out.println(message);
    }
}

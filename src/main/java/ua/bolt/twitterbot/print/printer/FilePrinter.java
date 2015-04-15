package ua.bolt.twitterbot.print.printer;

import twitter4j.Logger;
import ua.bolt.twitterbot.FileManager;
import ua.bolt.twitterbot.prop.Names;
import ua.bolt.twitterbot.prop.PropertyHolder;

/**
 * Created by ackiybolt on 15.04.15.
 */
public class FilePrinter implements Printer {

    private static Logger LOG = Logger.getLogger(FilePrinter.class);

    private String path = PropertyHolder.INSTANCE.getStr(Names.print_file);

    @Override
    public void print(String message) {
        FileManager.writeContent(message, path);
        LOG.info("Data was printed to file '" + path + "'");
    }
}

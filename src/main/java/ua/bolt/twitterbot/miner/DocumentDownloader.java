package ua.bolt.twitterbot.miner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import twitter4j.Logger;
import ua.bolt.twitterbot.prop.Names;
import ua.bolt.twitterbot.prop.PropertyHolder;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by ackiybolt on 25.12.14.
 */
public enum DocumentDownloader {

    INSTANCE;

    private static Logger LOG = Logger.getLogger(DocumentDownloader.class);
    private static int RETRY_TIME = PropertyHolder.INSTANCE.getInt(Names.min_15);
    private static int RETRY_COUNT = PropertyHolder.INSTANCE.getInt(Names.doc_download_tries);
    private static int JSOUP_TIMEOUT = PropertyHolder.INSTANCE.getInt(Names.doc_download_timeout);

    private Random rnd;
    private List<String> userAgents;

    DocumentDownloader() {
        rnd = new Random(System.nanoTime());
        initUserAgents();
    }

    public Document downloadDocument (String url) throws IOException {
        return downloadDocument(url, RETRY_COUNT, RETRY_TIME);
    }

    public Document downloadDocument (String url, int tries, int period) throws IOException {
        Document result = null;

        try {
            result = Jsoup.connect(url)
                    .timeout(JSOUP_TIMEOUT)
                    .ignoreHttpErrors(false)
                    .userAgent(getRandomAgent()).get();

        } catch (SocketTimeoutException ex) {

            LOG.warn("Connection problem. SocketTimeoutException had a place while download:\n"
                    + url
                    + "\nRetrying...");

            try {
                Thread.currentThread().sleep(period);
            } catch (InterruptedException e) {}

            if (tries > 1)
                downloadDocument(url, --tries, period);
        }

        return result;
    }

    public String downloadRawData (String url) throws IOException {

        String result = null;

        try (java.util.Scanner s = new java.util.Scanner(new URL(url).openStream())) {
            result = s.useDelimiter("\\A").next();
        }

//        if(result != null && !result.isEmpty())
//            LOG.info("Data downloaded successfully.");

        return result;
    }

    private String getRandomAgent () {
        return userAgents.get(rnd.nextInt(userAgents.size()));
    }



    private void initUserAgents() {

        ArrayList<String> userAgents = userAgents = new ArrayList<>();
        userAgents.add("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/39.0.2171.65 Chrome/39.0.2171.65 Safari/537.36");
        userAgents.add("Google Chrome on Windows 7 32-bit");
        userAgents.add("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.116 Safari/537.36");
        userAgents.add("Mozilla Firefox on Ubuntu 13.10 64-bit");
        userAgents.add("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:27.0) Gecko/20100101 Firefox/27.0");
        userAgents.add("Opera on Android 4.3");
        userAgents.add("Mozilla/5.0 (Linux; Android 4.3; Galaxy Nexus Build/JWR66Y) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.166 Mobile Safari/537.36 OPR/20.0.1396.73172");

        this.userAgents = Collections.unmodifiableList(userAgents);
    }
}

package ua.bolt.twitterbot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import twitter4j.Logger;
import ua.bolt.twitterbot.domain.Market;

import java.io.*;
import java.util.Set;

/**
 * Created by ackiybolt on 25.12.14.
 */
public class CacheManager {

    private static Logger LOG = Logger.getLogger(CacheManager.class);


    private String cacheFile;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public CacheManager(String cacheFile) {
        this.cacheFile = cacheFile;
    }

    public Market readCache () {

        Market result = null;
        String content = null;

        try {
            content = readContent(cacheFile);

        } catch (FileNotFoundException ex) {
            LOG.warn("Can't find cache file. ", ex.toString());
        } catch (IOException ex) {
            LOG.warn("Can't read cache file. ", ex.toString());
        }

        if (content != null && !content.isEmpty())
            try {
                result = gson.fromJson(content, Market.class);

            } catch (JsonSyntaxException ex) {
                LOG.warn("Can't parse JSON. ", ex.toString());
            }

        return result;
    }

    public void updateCache(Market market) {

        String content = gson.toJson(market);

        try {
            writeContent(content);

        } catch (FileNotFoundException ex) {
            LOG.warn("Can't find cache file. Skipped. ", ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
            LOG.warn("Encoding problems. Skipped. ", ex.getMessage());
        }
    }

    private String readContent(String path) throws IOException {

        StringBuilder sb = new StringBuilder();

        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
        }
        return sb.toString();
    }

    public void writeContent(String content) throws FileNotFoundException, UnsupportedEncodingException {

        try (PrintWriter writer = new PrintWriter(cacheFile, "UTF-8")) {
            writer.println(content);
        }
    }
}

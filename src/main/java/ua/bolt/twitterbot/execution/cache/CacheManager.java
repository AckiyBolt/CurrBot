package ua.bolt.twitterbot.execution.cache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import twitter4j.Logger;
import ua.bolt.twitterbot.FileManager;

import java.io.*;

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

    public CacheHolder readCache () {

        CacheHolder result = null;
        String content = FileManager.readContent(cacheFile);

        if (content != null && !content.isEmpty())
            try {
                result = gson.fromJson(content, CacheHolder.class);

            } catch (JsonSyntaxException ex) {
                LOG.warn("Can't parse JSON. ", ex.toString());
            }

        return result;
    }

    public void updateCache(CacheHolder market) {

        String content = gson.toJson(market);

        FileManager.writeContent(content, cacheFile);
    }
}
